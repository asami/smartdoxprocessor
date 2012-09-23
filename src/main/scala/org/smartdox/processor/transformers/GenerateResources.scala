package org.smartdox.processor.transformers;

import scala.collection.JavaConverters._
import scala.util.control.Exception._
import scalaz._
import Scalaz._
import scalaz.concurrent.Promise
import org.goldenport.Z._
import org.smartdox._
import org.goldenport.entity.content.BinaryContent
import org.goldenport.Strings
import java.util.concurrent.CopyOnWriteArrayList
import java.io.{File, IOException}
import com.asamioffice.goldenport.io.UFile
import Dox.TreeDoxVW
import Dox.TreeDoxW
import Dox.DoxVW
import java.io.InputStream
import com.asamioffice.goldenport.io.UIO

/**
 * TODO explicitly error handling for gentle message
 * 
 * @since   Jan. 20, 2012
 *  version Jan. 30, 2012
 * @version Sep. 22, 2012
 * @author  ASAMI, Tomoharu
 */
trait GenerateResources extends SmartDoxTransformerBase {
  private val _outcomes = new CopyOnWriteArrayList[Promise[Either[Exception, BinaryContent]]]

  private var _is_async = true

  protected final def generate_resourcesVW(tree: TreeDoxVW): TreeDoxVW = {
    for (w <- tree) {
      val z = w.over.flatten.toList
      w.over.flatten.toList.collect {
        case i: DotImg => generate_graphviz_pngBG(i.contents, i.src.toString) 
        case i: DitaaImg => generate_ditaa_pngBG(i.contents, i.src.toString) 
        case i: SmCsvImg => generate_sm_csv_pngBG(i.contents, i.src.toString)
      }
    }
    tree
  }

  protected final def generate_graphviz_pngBG(text: String, outname: String) {
    val p = promise {
      _generate_graphviz_png(text, outname)
    }
    _outcomes.add(p)
  }

  protected final def generate_ditaa_pngBG(text: String, outname: String) {
    val p = promise {
      _generate_ditaa_png(text, outname)
    }
    _outcomes.add(p)
  }

  protected final def generate_sm_csv_pngBG(text: String, outname: String) {
    val p = promise {
      _generate_sm_csv_png(text, outname)
    }
    if (!_is_async) {
      p.get
    }
    _outcomes.add(p)
  }

  protected final def get_generation_outcomes(): List[BinaryContent] = {
    val os = _outcomes.map(_.get).asScala.toList
    os collect { case Right(b: BinaryContent) => b }
  }

  private def _generate_graphviz_png(text: String, outname: String): Either[Exception, BinaryContent] = {
    try {
      val layout = "dot"
      // val layout = "neato"
      _execute_command_stdio("dot -Tpng -K%s -q".format(layout), text,
                             outname, Strings.mimetype.image_png)
    } catch {
      case e => throw new IOException("""graphvizのdotコマンドが動作しませんでした。
graphvizについてはhttp://www.graphviz.org/を参照してください。
[詳細エラー: %s]""".format(e.getMessage))
    }
  }

  private def _generate_ditaa_png(text: String, outname: String): Either[Exception, BinaryContent] = {
    try {
      val r = _execute_command_tmpfiles("ditaa", text,
                                        outname, Strings.mimetype.image_png)
      if (r.isRight) r
      else {
        _execute_command_tmpfiles("java -jar /opt/local/share/java/ditaa0_9.jar", text,
                                  outname, Strings.mimetype.image_png)
      }
    } catch {
      case e => throw new IOException("""ditaaコマンドが動作しませんでした。
dittaについてはhttp://ditaa.sourceforge.net/を参照してください。
[詳細エラー: %s]""".format(e.getMessage))
    }
  }

  private def _generate_sm_csv_png(text: String, outname: String): Either[Exception, BinaryContent] = {
    import org.simplemodeling.SimpleModeler.SimpleModelerDescriptor
    import org.goldenport.Goldenport
    import org.goldenport.exporter.FirstLeafOrZipResultExporterClass   

    record_trace("simplemodeler csv = " + text)
    val sm = new Goldenport(Array(), SimpleModelerDescriptor)
    sm.addExporterClass(FirstLeafOrZipResultExporterClass)
    sm.open()
    try {
      val ds = sm.createStringDataSource("model.csv", text)
      val props = Map("import.builder.policy" -> "none")
            // "container.message" -> "trace"
      sm.executeAsAnyRef("diagram", List(ds), props) match {
        case Some(b: BinaryContent) => {
          BinaryContent(b.getBinary(), entity_context, outname, b.mimeType | Strings.mimetype.image_png).right
        }
        case Some(c) => (new IllegalArgumentException(c.toString)).left // XXX toString too big case
        case None => (new IllegalArgumentException("empty result")).left
      }
    } finally {
      sm.close()
    }
  }

  private def _execute_command_stdio(command: String, intext: String,
      outname: String, mimetype: String): Either[Exception, BinaryContent] = {
    val cmd = entity_context.executeCommand(command);
    val in = cmd.getInputStream()
    val out = cmd.getOutputStream()
    try {
      out.write(intext.getBytes("utf-8"))
      out.flush
      out.close
      cmd.waitFor()
      BinaryContent(in, entity_context, outname, mimetype).right
    } catch {
      case e: Exception => e.left
    } finally {
      if (in != null) in.close
    }
  }  

  private def _execute_command_tmpfiles(command: String, intext: String,
      outname: String, mimetype: String): Either[Exception, BinaryContent] = {
    var dir: File = null
    var in: InputStream = null
    var infile: File = null
    var outfile: File = null
    try {
      dir = entity_context.createWorkDirectory()
      dir.deleteOnExit()
      infile = new File(dir, "infile")
      UFile.createFile(infile, intext, "utf-8")
      outfile = new File(dir, "outfile")
      val line = "%s %s %s".format(command, infile.toString, outfile.toString)
      val cmd = entity_context.executeCommand(line)
      in = cmd.getInputStream()
      UIO.stream2Bytes(in) // skip
      cmd.waitFor()
      val outcome = UIO.file2Bytes(outfile)
      BinaryContent(outcome, entity_context, outname, mimetype).right
    } catch {
      case e: Exception => e.left
    } finally {
      if (in != null) in.close
      if (infile != null) infile.delete()
      if (outfile != null) outfile.delete()
      if (dir != null) dir.delete()        
    }
  }
}
