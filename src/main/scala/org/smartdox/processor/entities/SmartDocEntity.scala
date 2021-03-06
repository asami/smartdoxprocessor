package org.smartdox.processor.entities

import scala.xml.{Node, Elem, Group}
import java.io.OutputStream
import org.goldenport.entity._
import org.goldenport.entity.datasource.{GDataSource, NullDataSource, ResourceDataSource}
import org.goldenport.entity.content.GContent
import org.goldenport.entity.locator.EntityLocator
import org.goldenport.sdoc.structure._
import org.goldenport.entities.workspace.TreeWorkspaceEntity
import org.goldenport.entities.zip.ZipEntity
import org.goldenport.value.GTreeBase

/*
 * @since   Jan. 2, 2011
 * @version Jan. 2, 2011
 * @author  ASAMI, Tomoharu
 */
class SmartDocEntity(aIn: GDataSource, aOut: GDataSource, aContext: GEntityContext) extends GEntity(aIn, aOut, aContext) {
  type DataSource_TYPE = GDataSource

  val doxContext = new GSubEntityContext(entityContext) {
    override def text_Encoding = Some("UTF-8")
  }

  def this(aDataSource: GDataSource, aContext: GEntityContext) = this(aDataSource, aDataSource, aContext)
  def this(aContext: GEntityContext) = this(null, aContext)

  override protected def open_Entity_Create() {
  }

  override protected def open_Entity_Create(aDataSource: GDataSource) {
  }

  override protected def open_Entity_Update(aDataSource: GDataSource) {
  }

  private def load_datasource(aDataSource: GDataSource) {
  }    

  override protected def write_Content(anOut: OutputStream): Unit = {
  }
}

class SmartDocEntityClass extends GEntityClass {
  type Instance_TYPE = SmartDocEntity

  override def accept_Suffix(suffix: String): Boolean = suffix == "sdoc"

  override def reconstitute_DataSource(aDataSource: GDataSource, aContext: GEntityContext): Option[Instance_TYPE] = Some(new SmartDocEntity(aDataSource, aContext))
}

object SmartDocEntity extends SmartDocEntityClass
