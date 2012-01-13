package org.smartdox.processor.transformers.blogger

import org.smartdox.processor.entities.SmartDoxEntity
import org.goldenport.service.GServiceContext
import org.goldenport.entities.workspace.TreeWorkspaceEntity
import org.smartdox.processor.transformers.SmartDoxTransformerBase
import org.smartdox.processor.transformers.Dox2DoxTransformer

/**
 * @since   Jan. 11, 2012
 * @version Jan. 13, 2012
 * @author  ASAMI, Tomoharu
 */
class SmartDox2BloggerRealmTransformer(val context: GServiceContext, val dox: SmartDoxEntity
    ) extends SmartDoxTransformerBase with Dox2DoxTransformer {
  protected def transform_Dox() {
    
  } 
}
