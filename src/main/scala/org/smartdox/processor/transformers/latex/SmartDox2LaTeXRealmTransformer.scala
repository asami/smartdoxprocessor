package org.smartdox.processor.transformers.latex
import org.smartdox.processor.entities.SmartDoxEntity
import org.goldenport.service.GServiceContext
import org.goldenport.entities.workspace.TreeWorkspaceEntity
import org.smartdox.processor.transformers.SmartDoxTransformerBase
import org.smartdox.processor.transformers.UseSmartDoc

/**
 * @since   Jan.  2, 2012
 * @version Jan. 19, 2012
 * @author  ASAMI, Tomoharu
 */
class SmartDox2LatexRealmTransformer(val context: GServiceContext, val entity: SmartDoxEntity
    ) extends SmartDoxTransformerBase with UseSmartDoc {
  val format = "latex2e"
}
