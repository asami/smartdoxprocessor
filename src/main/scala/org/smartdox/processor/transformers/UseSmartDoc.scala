package org.smartdox.processor.transformers

import scalaz._
import Scalaz._
import com.asamioffice.goldenport.text.UPathString
import com.AsamiOffice.jaba2.xml.ProcessorFactory
import com.AsamiOffice.jaba2.j2fw.generator.TextArtifact
import org.xmlsmartdoc.SmartDoc.SmartDocBeans
import org.smartdox._
import Dox._

/**
 * @since   Jan. 18, 2012
 * @version Jan. 18, 2012
 * @author  ASAMI, Tomoharu
 */
trait UseSmartDoc extends SmartDoxTransformerBase {
  val format: String

  override protected def transform_Dox() {
    transformed_SdocVW match {
      case Success(s) => _transform_success(s)
      case Failure(e) => _transform_failure(e)
    }
  }

  protected def transformed_SdocVW: DoxVW

  private def _transform_success(d: DoxW) {
    // XXX warning
    val url = entity.inputDataSource.getUrl()
    val dir = UPathString.getContainerPathname(url.toString())
    val sdoc = new SmartDocBeans
    sdoc.setFormat(format)
    sdoc.setProjectDirectory(dir)
    val processor = ProcessorFactory.getProcessor()
    val doc = processor.parseDocumentByText(d.over.toString)
    sdoc.setInputDocument(doc)
    val result = sdoc.getArtifacts()
    for (r <- result) {
      r match {
        case t: TextArtifact if t.getName() == "null.html" => set_main_content("html", t.getString()) // XXX 
      }
    }    
  }

  private def _transform_failure(e: NonEmptyList[String]) {
    throw new IllegalArgumentException(e.list.mkString)
  }
}
