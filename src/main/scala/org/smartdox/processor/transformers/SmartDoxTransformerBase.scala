package org.smartdox.processor.transformers
import org.goldenport.service.GServiceContext
import org.goldenport.entities.workspace.TreeWorkspaceEntity
import org.smartdox.processor.entities.SmartDoxEntity

/**
 * @since   Jan.  2, 2012
 * @version Jan.  2, 2012
 * @author  ASAMI, Tomoharu
 */
trait SmartDoxTransformerBase {
  protected val dox: SmartDoxEntity
  protected val context: GServiceContext

  protected val entity_context = dox.entityContext
  protected val target_realm = new TreeWorkspaceEntity(entity_context)

  def transform() = {
    dox.open()
    target_realm.open()
    transform_Dox()
    target_realm ensuring(_.isOpened)
  }

  protected def transform_Dox(): Unit
}
