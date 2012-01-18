package org.smartdox.processor.transformers.html4

import scala.collection.JavaConverters._
import scalaz._
import Scalaz._
import org.smartdox.processor.entities.SmartDoxEntity
import org.goldenport.service.GServiceContext
import org.goldenport.entities.workspace.TreeWorkspaceEntity
import org.smartdox.processor.transformers.SmartDoxTransformerBase
import org.xmlsmartdoc.SmartDoc.SmartDocBeans
import com.AsamiOffice.jaba2.xml.ProcessorFactory
import org.w3c.dom.Document
import com.asamioffice.goldenport.text.UPathString
import com.AsamiOffice.jaba2.j2fw.generator.TextArtifact
import org.smartdox.processor.transformers.UseSmartDoc
import org.smartdox._
import org.goldenport.Z._
import org.smartdox.processor.transformers.Dox2Dox
import Dox.DoxVW

/**
 * @since   Jan.  2, 2012
 * @version Jan. 18, 2012
 * @author  ASAMI, Tomoharu
 */
class SmartDox2Html4RealmTransformer(val context: GServiceContext, val entity: SmartDoxEntity
    ) extends SmartDoxTransformerBase with Dox2Dox with UseSmartDoc {
  val format = "html4"

  override protected def transformed_SdocVW: DoxVW = {
    doxVW
  }

  override protected def transform_Dox(t: Tree[Dox]): Tree[Dox] = {
    def figureattrs(f: Figure): List[(String, String)] = {
      List(attribute_entry("title", f.caption.contents).some,
           ("src" -> f.img.src.toASCIIString).some,
           f.label.map("label" -> _)).flatten
    }
    replace(t) {
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

  protected final def attribute_entry(name: String, value: List[Dox]): (String, String) = {
    (name, value.foldMap(_.toText()))
  }
}
