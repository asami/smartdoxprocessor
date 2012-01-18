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
import com.asamioffice.goldenport.io.UURL
import com.asamioffice.goldenport.io.UIO

/**
 * @since   Jan. 11, 2012
 * @version Jan. 18, 2012
 * @author  ASAMI, Tomoharu
 */
class SmartDox2BloggerRealmTransformer(val context: GServiceContext, val entity: SmartDoxEntity
    ) extends SmartDoxTransformerBase with Dox2DoxTransformer {
  import Dox.TreeDoxVW
  import Dox.TreeDoxW
  import Dox.DoxVW

  protected def transform_Dox() {
    def modifyVW(d: TreeDoxVW): TreeDoxVW = {
      val root = d.map(_.map(x => find(x)(_.rootLabel.isInstanceOf[Body])))
      root.flatMap { w =>
        def trans(t: Tree[Dox]): TreeDoxVW = {
          _transform(t) |> (writer(nil[String], _).successNel[String])
        }
        w.over.fold(trans, "No root".failNel) 
      }
    }
    
    val result = Dox.treeLensVW.mod(dox.toVW, modifyVW) 
    set_main_contentVW("html", result.map(_.map(_.toString)))
  }

  private def _transform(t: Tree[Dox]): Tree[Dox] = {
    replace(t) {
      case (b: Body, cs) => (Div, cs)
      case (s: Section, cs) => {
        val hname = "h" + (s.level + 3)
        (Div, Stream.cons(Dox.html5(hname, s.title) |> Dox.tree, cs))
      }
      case (p: Program, cs) => (Pre(_program_text(p), List("name" -> "code", "class" -> "java")), cs)
      case (c: Console, cs) => (Pre(c.contents, List("class" -> "console")), cs)
      case (f: Figure, cs) => (Div, Stream(Text("*** embed manually: " + f.img.src + " ***").leaf))
    } ensuring { x => println("_transform = " + x.drawTree); true}
  }

  private def _program_text(p: Program): String = {
    val encoding = p.attribute("encoding") | "utf-8"
    p.attribute("src") match {
      case Some(s) => UIO.uri2String(s, encoding)
      case None => p.contents
    }
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
