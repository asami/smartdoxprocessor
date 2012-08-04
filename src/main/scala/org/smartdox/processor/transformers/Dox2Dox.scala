package org.smartdox.processor.transformers;

import scalaz._
import Scalaz._
import org.goldenport.Z._
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
 * @version Aug.  4, 2012
 * @author  ASAMI, Tomoharu
 */
trait Dox2Dox extends SmartDoxTransformerBase {
  protected val target_Suffix: String = "html"
  
  override protected def transform_Dox() {
    set_main_contentVW(target_Suffix, doxVW.map(_.map(_.toString)))
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
        eval_Dox(t) |> transform_Dox |> log |> (writer(nil[String], _).successNel[String])
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

  protected def eval_Dox(t: Tree[Dox]): Tree[Dox] = {
    replace(t) {
      case (table: Table, cs) => {
        if (table.head.isEmpty && table.foot.isEmpty) {
          table.body.records match {
            case (t: TTable) :: Nil => load_table(table, t)
            case _ => (table, cs) // XXX
          }
        } else (table, cs)
      }
      case (table: TTable, cs) => load_table(Table(none, TBody(nil), none, none, none), table)
    }
  }

  protected final def load_table(t: Table, tt: TTable): (Table, Stream[Tree[Dox]]) = {
    val (h, b) = load_table(tt)
    (t, Stream(h, b.some).flatten.map(Dox.tree))
  }

  protected final def load_table(tt: TTable): (Option[THead], TBody) = {
    context.entitySpace.reconstitute(tt.uri) match {
      case Some(e) => load_table_entity(e)
      case None => {
        record_warning("%s not found in table.".format(tt.uri))
        (None, TBody(List(TR(List(TD(List(Text("%s not found.".format(tt.uri)))))))))
      }
    }
  }

  protected final def load_table_entity(entity: GEntity): (Option[THead], TBody) = {
    entity match {
      case table: GTableEntity[_] => {
        table using {
          _load_table_trees(table)
        }
      }
      case tables: GTableListEntity[_] => {
        tables using {
          _load_table_trees(tables.head)
        }
      }
      case _ => {
        val uri = entity.inputDataSource
        record_warning("%s not found in table.".format(uri))
        (None, TBody(List(TR(List(TD(List(Text("%s not found.".format(uri)))))))))
      }
    }
  }

  private def _load_table_trees(t: GTable[_]) = {
    val a = _load_table(t)
    (None, TBody(a.toList))    
  }

  private def _load_table(t: GTable[_]) = {
    for (y <- 0 until t.height) yield {
      val b = for (x <- 0 until t.width) yield {
        t.getOption(x, y) match {
          case Some(s) => TD(List(Text(s.toString)))
          case None => TD(Nil)
        }
      } 
      TR(b.toList)
    }    
  }

  protected def transform_Dox(t: Tree[Dox]): Tree[Dox]
}
