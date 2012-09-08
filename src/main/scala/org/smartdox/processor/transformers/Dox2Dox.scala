package org.smartdox.processor.transformers;

import com.asamioffice.goldenport.text.UPathString
import scalaz._
import Scalaz._
import org.goldenport.recorder.Recordable
import org.goldenport.Z._
import org.goldenport.entities.smartdox.Dox2Dox
import org.smartdox._
import Dox.TreeDoxVW
import Dox.TreeDoxW
import Dox.DoxVW
import org.goldenport.entity._
import org.goldenport.value.GTable

/**
 * @since   Jan. 11, 2012
 *  version Jan. 26, 2012
 *  version Jul. 21, 2012
 *  version Aug. 25, 2012
 * @version Sep.  9, 2012
 * @author  ASAMI, Tomoharu
 */
trait Dox2DoxSmartDoxTransformerBase extends SmartDoxTransformerBase with Dox2Dox {
  protected val target_Suffix: String = "html"
  protected val baseUri = base_Uri
  protected val entitySpace = context.entitySpace
  
  override protected def transform_Dox() {
    set_main_contentVW(target_Suffix, doxVW.map(_.map(_.toString)))
  }

  protected final def doxVW: DoxVW = {
    dox2doxVW(dox)
  }
}
