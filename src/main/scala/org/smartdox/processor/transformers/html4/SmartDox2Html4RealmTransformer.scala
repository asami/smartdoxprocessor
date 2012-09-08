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
import Dox.DoxVW

/**
 * @since   Jan.  2, 2012
 *  version Jan. 19, 2012
 * @version Sep.  8, 2012
 * @author  ASAMI, Tomoharu
 */
class SmartDox2Html4RealmTransformer(val context: GServiceContext, val entity: SmartDoxEntity
    ) extends SmartDoxTransformerBase with UseSmartDoc {
  val format = "html4"
}
