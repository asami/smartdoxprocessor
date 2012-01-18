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
 * @version Jan. 18, 2012
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
    Dox.treeLensVW.mod(dox.toVW, modify_doxVW)
  }

  protected final def modify_doxVW(d: TreeDoxVW): TreeDoxVW = {
    val root = d.map(_.map(find_Root))
    root.flatMap { w =>
      def trans(t: Tree[Dox]): TreeDoxVW = {
        transform_Dox(t) |> (writer(nil[String], _).successNel[String])
      }
      w.over.fold(trans, "No root".failNel) 
    }
  }

  protected def find_Root(t:Tree[Dox]): Option[Tree[Dox]] = t.some

  protected def transform_Dox(t: Tree[Dox]): Tree[Dox]
}
