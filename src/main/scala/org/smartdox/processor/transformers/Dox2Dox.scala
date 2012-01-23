package org.smartdox.processor.transformers;

import scalaz._
import Scalaz._
import org.goldenport.Z._
import org.smartdox._
import Dox.TreeDoxVW
import Dox.TreeDoxW
import Dox.DoxVW

/**
 * @since   Jan. 11, 2012
 * @version Jan. 23, 2012
 * @author  ASAMI, Tomoharu
 */
trait Dox2Dox extends SmartDoxTransformerBase {
  override protected def transform_Dox() {
    set_main_contentVW("html", doxVW.map(_.map(_.toString)))
  }

  protected final def doxVW: DoxVW = {
    dox2doxVW(dox)
  }
  
  protected final def dox2doxVW(d: Dox): DoxVW = {
    Dox.treeLensVW.mod(dox.toVW, tdox2_TdoxVW)
  }

  protected def tdox2_TdoxVW(d: TreeDoxVW): TreeDoxVW = {
    aux_DoxVW(modify_doxVW(d))
  }

  protected final def modify_doxVW(d: TreeDoxVW): TreeDoxVW = {
    val root = d.map(_.map(find_Root))
    root.flatMap { w =>
      def trans(t: Tree[Dox]): TreeDoxVW = {
        def log(t: Tree[Dox]) = log_treedox("Dox2Dox transfered = ", t)
        transform_Dox(t) |> log |> (writer(nil[String], _).successNel[String])
      }
      w.over.fold(trans, "No root".failNel) 
    }
  }

  protected final def log_treedox(message: String, t: Tree[Dox]) = {
    record_trace(message + t.drawTree)
    t
  }
  
  protected def aux_DoxVW(d: TreeDoxVW): TreeDoxVW = d

  protected def find_Root(t:Tree[Dox]): Option[Tree[Dox]] = t.some

  protected def transform_Dox(t: Tree[Dox]): Tree[Dox]
}
