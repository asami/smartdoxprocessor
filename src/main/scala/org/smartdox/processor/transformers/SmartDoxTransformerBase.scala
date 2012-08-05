package org.smartdox.processor.transformers

import scalaz._
import Scalaz._
import org.goldenport.service.GServiceContext
import org.goldenport.entities.workspace.TreeWorkspaceEntity
import org.smartdox.processor.entities.SmartDoxEntity
import org.goldenport.entity.content.StringContent
import org.goldenport.entity.content.StringContent
import com.asamioffice.goldenport.text.UPathString
import org.smartdox.parser.DoxParser
import org.smartdox._
import org.goldenport.recorder.Recordable
import Dox.TreeDoxVW
import com.asamioffice.goldenport.io.UIO
import org.goldenport.entity.content.BinaryContent

/**
 * @since   Jan.  2, 2012
 *  version Jan. 19, 2012
 * @version Aug.  5, 2012
 * @author  ASAMI, Tomoharu
 */
trait SmartDoxTransformerBase extends Recordable {
  protected val entity: SmartDoxEntity
  protected val context: GServiceContext

  protected val entity_context = entity.entityContext
  protected val target_realm = new TreeWorkspaceEntity(entity_context)

  setup_FowardingRecorder(context)

  def transform() = {
    entity.using {
      target_realm.open()
      transform_Dox()
      target_realm ensuring(_.isOpened)
    }
  }

  protected def transform_Dox(): Unit

  protected final def set_content(path: String, content: String) {
    target_realm.setContent(path, new StringContent(content, entity_context))
  }

  protected final def set_content(path: String, content: Array[Byte]) {
    target_realm.setContent(path, new BinaryContent(content, entity_context))
  }

  protected final def set_main_content(suffix: String, content: String) {
    set_content(name_body + "." + suffix, content)
  }

  protected final def set_main_content(suffix: String, content: Array[Byte]) {
    set_content(name_body + "." + suffix, content)
  }

  protected final def set_main_contentV(suffix: String, content: ValidationNEL[String, String]) {
    content match {
      case Success(c) => set_main_content(suffix, c)
      case Failure(e) => set_error_content(suffix, e)
    }
  }

  protected final def set_main_contentVW(suffix: String, content: ValidationNEL[String, Writer[List[String], String]]) {
    content match {
      case Success(c) => {
        set_main_content(suffix, c.over)
        set_warning(c.written)
      }
      case Failure(e) => set_error_content(suffix, e)
    }
  }

  protected final def set_error_content(suffix: String, errors: NonEmptyList[String]) {
    errors.foreach(record_warning(_))
    set_main_content(suffix, error_list_dox(errors.list).toString)
  }

  protected final def set_warning(warns: List[String]) {
    warns.foreach(record_warning(_))
  }

  protected final def name_body = UPathString.getLastComponentBody(entity.name)

  protected final def dox = {
    entity.dox ||| (x => error_list_dox(x.list))
  }

  protected final def error_dox = {
    Document(Head(), Body(List(Text("Error"))))
  }

  protected final def error_list_dox(messages: List[String]) = {
    Li(List(Text("ok")))
    Document(Head(), Body(Ul(messages.map(Li(_)))))
  }

  protected final def program_text(p: Program): String = {
    val encoding = p.attribute("encoding") | "utf-8"
    p.attribute("src") match {
      case Some(s) => UIO.uri2String(s, encoding)
      case None => p.contents
    }
  }

  protected final def attribute_entry(name: String, value: List[Dox]): (String, String) = {
    (name, value.foldMap(_.toText()))
  }
}
