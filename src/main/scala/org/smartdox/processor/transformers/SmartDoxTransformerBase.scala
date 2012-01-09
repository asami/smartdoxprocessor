package org.smartdox.processor.transformers

import scalaz._
import Scalaz._
import org.goldenport.service.GServiceContext
import org.goldenport.entities.workspace.TreeWorkspaceEntity
import org.smartdox.processor.entities.SmartDoxEntity
import org.goldenport.entity.content.StringContent
import org.goldenport.entity.content.StringContent
import com.asamioffice.text.UPathString
import org.smartdox.parser.DoxParser
import org.smartdox._

/**
 * @since   Jan.  2, 2012
 * @version Jan.  9, 2012
 * @author  ASAMI, Tomoharu
 */
trait SmartDoxTransformerBase {
  protected val entity: SmartDoxEntity
  protected val context: GServiceContext

  protected val entity_context = entity.entityContext
  protected val target_realm = new TreeWorkspaceEntity(entity_context)

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

  protected final def name_body = UPathString.getLastComponentBody(entity.name)

  protected final def dox = {
    entity.dox | error_dox
  }

  protected final def error_dox = {
    Document(Head(), Body(List(Text("Error"))))
  }
}
