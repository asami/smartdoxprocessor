package org.smartdox.processor.transformers.html4

import scala.collection.JavaConverters._
import org.smartdox.processor.entities.SmartDoxEntity
import org.goldenport.service.GServiceContext
import org.goldenport.entities.workspace.TreeWorkspaceEntity
import org.smartdox.processor.transformers.SmartDoxTransformerBase
import org.xmlsmartdoc.SmartDoc.SmartDocBeans
import com.AsamiOffice.jaba2.xml.ProcessorFactory
import org.w3c.dom.Document

import com.asamioffice.goldenport.text.UPathString;
import com.AsamiOffice.jaba2.j2fw.generator.TextArtifact

/**
 * @since   Jan.  2, 2012
 * @version Jan. 18, 2012
 * @author  ASAMI, Tomoharu
 */
class SmartDox2Html4RealmTransformer(val context: GServiceContext, val entity: SmartDoxEntity) extends SmartDoxTransformerBase {
  protected def transform_Dox() {
    val url = entity.inputDataSource.getUrl();
    val dir = UPathString.getContainerPathname(url.toString());
    val sdoc = new SmartDocBeans
    sdoc.setFormat("html4")
//    sdoc.setPackager("zip")
    sdoc.setProjectDirectory(dir)
    val processor = ProcessorFactory.getProcessor()
    val doc = processor.parseDocumentByText(dox.toString)
    sdoc.setInputDocument(doc)
    val result = sdoc.getArtifacts()
    for (r <- result) {
      r match {
        case t: TextArtifact => set_content(t.getName, t.getString()) 
      }
    }
  } 
}
