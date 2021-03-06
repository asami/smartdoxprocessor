package org.smartdox.processor.transformers.html5
import org.smartdox.processor.entities.SmartDoxEntity
import org.goldenport.service.GServiceContext
import org.goldenport.entities.workspace.TreeWorkspaceEntity
import org.smartdox.processor.transformers.SmartDoxTransformerBase

/**
 * @since   Jan.  2, 2012
 * @version Jan.  9, 2012
 * @author  ASAMI, Tomoharu
 */
class SmartDox2Html5RealmTransformer(val context: GServiceContext, val entity: SmartDoxEntity) extends SmartDoxTransformerBase {
  protected def transform_Dox() {
    set_content(name_body + ".html", dox.toString)
  }
}
