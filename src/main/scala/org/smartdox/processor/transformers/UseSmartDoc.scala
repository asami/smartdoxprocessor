package org.smartdox.processor.transformers

import scalaz._
import Scalaz._
import org.goldenport.Z._
import com.asamioffice.goldenport.text.UPathString
import com.AsamiOffice.jaba2.xml.ProcessorFactory
import com.AsamiOffice.jaba2.j2fw.generator.TextArtifact
import org.xmlsmartdoc.SmartDoc.SmartDocBeans
import org.smartdox._
import Dox._
import com.AsamiOffice.jaba2.j2fw.generator.LinkArtifact
import com.AsamiOffice.jaba2.j2fw.generator.BinaryArtifact

/**
 * @since   Jan. 18, 2012
 * @version Jan. 19, 2012
 * @author  ASAMI, Tomoharu
 */
trait UseSmartDoc extends Dox2Dox {
  val format: String

  override protected def transform_Dox() {
    transformed_SdocVW match {
      case Success(s) => _transform_success(s)
      case Failure(e) => _transform_failure(e)
    }
  }

  protected def transformed_SdocVW: DoxVW = {
    doxVW
  }

  override protected def transform_Dox(t: Tree[Dox]): Tree[Dox] = {
    def figureattrs(f: Figure): List[(String, String)] = {
      List(attribute_entry("title", f.caption.contents).some,
           ("src" -> f.img.src.toASCIIString).some,
           f.label.map("label" -> _)).flatten
    }
    replace(t) {
      case (d: Document, cs) => (SDoc("doc", List("xml:lang" -> "ja"), nil), cs) // XXX language
      case (s: Section, cs) => {
        val sname = s.level match {
          case 1 => "section"
          case 2 => "subsection"
          case 3 => "subsubsection"
          case _ => "div"
        }
        (SDoc(sname, List(attribute_entry("title", s.title)), nil), cs)
      }
      case (p: Program, cs) => (SDoc("program", nil, nil), Stream(Text(program_text(p)).leaf))
      case (c: Console, cs) => (SDoc("console", nil, nil), Stream(Text(c.contents).leaf))
      case (f: Figure, cs) => (SDoc("figure", figureattrs(f), nil), Stream.empty)
    } ensuring { x => println("_transform = " + x.drawTree); true}
  }

  private def _transform_success(d: DoxW) {
    // XXX warning
    val sdoc = new SmartDocBeans
    sdoc.setFormat(format)
    for (url <- entity.inputDataSource.getUrl()) {
      val dir = UPathString.getContainerPathname(url.toString())
      sdoc.setProjectDirectory(dir)
    }
    val processor = ProcessorFactory.getProcessor()
    val doc = processor.parseDocumentByText(d.over.toString)
    sdoc.setInputDocument(doc)
    val result = sdoc.getArtifacts()
    for (r <- result) {
      r match {
        case t: TextArtifact if t.getName().startsWith("null.") => {
          val suffix = UPathString.getSuffix(t.getName)
          set_main_content(suffix, t.getString())
        }
        case t: TextArtifact => set_content(t.getName(), t.getString())
        case l: LinkArtifact => {
          val name = UPathString.getLastComponent(l.from_)
          val suffix = UPathString.getSuffix(l.from_)
          if (name.startsWith("null.")) {
            set_main_content(suffix, l.getBytes())
          } else {
            set_content(name, l.getBytes())  
          }
        }
        case b: BinaryArtifact => {
          val name = UPathString.getLastComponent(b.getName());
          val suffix = UPathString.getSuffix(b.getName());
          if (name.startsWith("null.")) {
            set_main_content(suffix, b.getBytes())
          } else {
            set_content(name, b.getBytes())  
          }          
        }
      }
    }    
  }

  private def _transform_failure(e: NonEmptyList[String]) {
    throw new IllegalArgumentException(e.list.mkString)
  }
}
