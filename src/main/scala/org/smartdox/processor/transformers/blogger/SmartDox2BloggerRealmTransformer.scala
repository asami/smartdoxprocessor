package org.smartdox.processor.transformers.blogger

import scalaz._
import Scalaz._
import org.goldenport.Z._
import org.goldenport.service.GServiceContext
import org.goldenport.entities.workspace.TreeWorkspaceEntity
import org.smartdox.processor.entities.SmartDoxEntity
import org.smartdox.processor.transformers.SmartDoxTransformerBase
import org.smartdox.processor.transformers.Dox2DoxTransformer
import org.smartdox._

/**
 * @since   Jan. 11, 2012
 * @version Jan. 15, 2012
 * @author  ASAMI, Tomoharu
 */
class SmartDox2BloggerRealmTransformer(val context: GServiceContext, val entity: SmartDoxEntity
    ) extends SmartDoxTransformerBase with Dox2DoxTransformer {
  import Dox.TreeDoxV

  protected def transform_Dox() {
    def modifyV(d: TreeDoxV) = {
      val root = d.map(x => find(x)(_.rootLabel.isInstanceOf[Body])).sequence | "No root".failNel
      root.map(x => replaceShallow(x) {
        case (b: Body, cs) => (Div, cs)
      })
    }
    
    val result = Dox.treeLensV.mod(dox.success, modifyV) 
    set_main_contentV("html", result.map(_.toString))
  }

/*
  def traverse[T, U](tree: Tree[T],
      enter: PartialFunction[Tree[T], U],
      leave: PartialFunction[Tree[T], U] = Map.empty[Tree[T], U])(implicit mo: Monoid[U]): U = {
    foldTraverse(tree, mo.zero, enter, leave)
  }

  def foldTraverse[T, U: Monoid](tree: Tree[T],
      monoid: U,
      enter: PartialFunction[Tree[T], U],
      leave: PartialFunction[Tree[T], U] = Map.empty[Tree[T], U]): U = {
    val m1 = if (enter.isDefinedAt(tree)) {
      monoid |+| enter(tree)
    } else monoid
    val m2 = tree.subForest.foldl(m1) {
      (m, t) => foldTraverse(t, m, enter, leave)
    }
    if (leave.isDefinedAt(tree)) {
      m2 |+| leave(tree)
    } else m2
  }

  def replace[T](tree: Tree[T])(
      pf: PartialFunction[(T, Stream[Tree[T]]), (T, Stream[Tree[T]])]): Tree[T] = {
    replaceDeep(tree)(pf)
  }

  def replaceShallow[T](tree: Tree[T])(
      pf: PartialFunction[(T, Stream[Tree[T]]), (T, Stream[Tree[T]])]): Tree[T] = {
    if (pf.isDefinedAt((tree.rootLabel, tree.subForest))) {
      val (r, cs) = pf((tree.rootLabel, tree.subForest))
      if (cs.isEmpty) r.leaf
      else r.node(cs.toArray: _*)
    } else {
      val r = tree.rootLabel
      val cs = tree.subForest
      if (cs.isEmpty) tree
      else r.node(cs.map(replaceShallow(_)(pf)).toArray: _*)
    }
  }

  def replaceDeep[T](tree: Tree[T])
      (pf: PartialFunction[(T, Stream[Tree[T]]), (T, Stream[Tree[T]])]): Tree[T] = {
    if (pf.isDefinedAt((tree.rootLabel, tree.subForest))) {
      val (r, cs) = pf((tree.rootLabel, tree.subForest))
      if (cs.isEmpty) r.leaf
      else r.node(cs.map(replaceDeep(_)(pf)).toArray: _*)
    } else {
      val r = tree.rootLabel
      val cs = tree.subForest
      if (cs.isEmpty) tree
      else r.node(cs.map(replaceDeep(_)(pf)).toArray: _*)
    }
  }

  def replaceNode[T](tree: Tree[T])
      (pf: PartialFunction[T, T]): Tree[T] = {
    if (pf.isDefinedAt(tree.rootLabel)) {
      val r = pf(tree.rootLabel)
      val cs = tree.subForest
      if (cs.isEmpty) tree
      else r.node(cs.map(replaceNode(_)(pf)).toArray: _*)
    } else {
      val r = tree.rootLabel
      val cs = tree.subForest
      if (cs.isEmpty) tree
      else r.node(cs.map(replaceNode(_)(pf)).toArray: _*)
    }
  }

  def transform[T, U](tree: Tree[T],
      f: (T, Stream[Tree[T]]) => (U, Stream[Tree[U]])): Tree[U] = {
    val (r, cs) = f(tree.rootLabel, tree.subForest)
    if (cs.isEmpty) r.leaf
    else r.node(cs.toArray: _*)
  }
*/
}
