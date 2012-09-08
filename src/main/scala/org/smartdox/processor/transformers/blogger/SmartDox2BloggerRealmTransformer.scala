package org.smartdox.processor.transformers.blogger

import scalaz._
import Scalaz._
import org.goldenport.Z._
import org.goldenport.service.GServiceContext
import org.goldenport.entities.workspace.TreeWorkspaceEntity
import org.smartdox.processor.entities.SmartDoxEntity
import org.smartdox.processor.transformers.SmartDoxTransformerBase
import org.smartdox.processor.transformers.Dox2DoxSmartDoxTransformerBase
import org.smartdox._
import com.asamioffice.goldenport.io.UURL
import com.asamioffice.goldenport.io.UIO

/**
 * @since   Jan. 11, 2012
 *  version Jan. 26, 2012
 * @version Sep.  8, 2012
 * @author  ASAMI, Tomoharu
 */
class SmartDox2BloggerRealmTransformer(val context: GServiceContext, val entity: SmartDoxEntity
    ) extends Dox2DoxSmartDoxTransformerBase {
  import Dox.TreeDoxVW
  import Dox.TreeDoxW
  import Dox.DoxVW

  override val target_Suffix = "blogger"

  override protected def find_Root(t:Tree[Dox]): Option[Tree[Dox]] = {
    find(t)(_.rootLabel.isInstanceOf[Body])
  }

  override protected def transform_Dox(t: Tree[Dox]): Tree[Dox] = {
    replace(t) {
      case (b: Body, cs) => (Div, cs)
      case (s: Section, cs) => {
        val hname = "h" + (s.level + 3)
        (Div, Stream.cons(Dox.html5(hname, s.title) |> Dox.tree, cs))
      }
      case (p: Program, cs) => (Pre(program_text(p), List("name" -> "code", "class" -> "java")), cs)
      case (c: Console, cs) => (Pre(c.contents, List("class" -> "console")), cs)
      case (f: Figure, cs) => (Div, Stream(Text("*** embed manually: " + f.img.src + " ***").leaf))
    } // ensuring { x => println("_transform = " + x.drawTree); true}
  }
}
