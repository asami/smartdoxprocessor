package org.smartdox.processor.transformers.sdoc

import scalaz._, Scalaz._
import org.smartdox.processor.entities.SmartDoxEntity
import org.goldenport.service.GServiceContext
import org.goldenport.entities.workspace.TreeWorkspaceEntity
import org.smartdox.processor.transformers.SmartDoxTransformerBase
import org.smartdox.processor.transformers.UseSmartDoc

/**
 * @since   Sep. 22, 2012
 * @version Sep. 22, 2012
 * @author  ASAMI, Tomoharu
 */
class SmartDox2SmartDocRealmTransformer(
  val context: GServiceContext, val entity: SmartDoxEntity
) extends SmartDoxTransformerBase with UseSmartDoc {
  val format = "sdoc" // not used
  override protected def transform_Dox() {
    transformed_SdocVW match {
      case Success(s) => set_content(name_body + ".sdoc", s._2.toString)
      case Failure(e) => transform_failure(e)
    }
  }
}
