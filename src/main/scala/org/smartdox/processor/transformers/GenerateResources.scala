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
import java.io.File
import com.asamioffice.goldenport.io.UFile
import Dox.TreeDoxVW
import Dox.TreeDoxW
import Dox.DoxVW
import java.io.InputStream
import com.asamioffice.goldenport.io.UIO

/**
 * @since   Jan. 20, 2012
 * @version Jan. 20, 2012
 * @author  ASAMI, Tomoharu
 */
trait GenerateResources extends SmartDoxTransformerBase {
  private val _outcomes = new CopyOnWriteArrayList[Promise[Either[Exception, BinaryContent]]]

  protected final def generate_resourcesVW(tree: TreeDoxVW): TreeDoxVW = {
    for (w <- tree) {
      println("generated = " + w.over.drawTree)
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
    _outcomes.add(p)
  }

  protected final def get_generation_outcomes(): List[BinaryContent] = {
    val os = _outcomes.map(_.get).asScala.toList
    os collect { case Right(b: BinaryContent) => b }
  }

  private def _generate_graphviz_png(text: String, outname: String): Either[Exception, BinaryContent] = {
    val layout = "dot"
    // val layout = "neato"
    _execute_command_stdio("dot -Tpng -K%s -q".format(layout), text,
        outname, Strings.mimetype.image_png)
  }

  private def _generate_ditaa_png(text: String, outname: String): Either[Exception, BinaryContent] = {
    _execute_command_tmpfiles("java -jar /opt/local/share/java/ditaa0_9.jar", text,
        outname, Strings.mimetype.image_png)
  }

  private def _generate_sm_csv_png(text: String, outname: String): Either[Exception, BinaryContent] = {
    val layout = "sm"
    // val layout = "neato"
    _execute_command_stdio("sm -diagram".format(layout), text,
        outname, Strings.mimetype.image_png)
  }

  private def _execute_command_stdio(command: String, intext: String,
      outname: String, mimetype: String): Either[Exception, BinaryContent] = {
    val cmd = entity_context.executeCommand(command);
    val in = cmd.getInputStream()
    val out = cmd.getOutputStream()
    //    println("start process = " + dot)
    try {
//      println("dot(class diagram) = " + text.string)
      out.write(intext.getBytes("utf-8"))
      out.flush
      out.close
      //      val b = com.asamioffice.io.UIO.stream2Bytes(in)
      //      println("b size = " + b.length)
      BinaryContent(in, entity_context, outname, mimetype).right
    } catch {
      case e: Exception => e.left
    } finally {
      if (in != null) in.close
      //      println("finish process = " + dot)
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
