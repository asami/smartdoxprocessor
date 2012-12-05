package org.smartdox.processor.entities

import scala.xml.{Node, Elem, Group}
import java.io.OutputStream
import scalaz._
import Scalaz._
import org.goldenport.entity._
import org.goldenport.entity.datasource.{GDataSource, NullDataSource, ResourceDataSource}
import org.goldenport.entity.content.GContent
import org.goldenport.entity.locator.EntityLocator
import org.goldenport.sdoc.structure._
import org.goldenport.entities.workspace.TreeWorkspaceEntity
import org.goldenport.entities.zip.ZipEntity
import org.goldenport.value.GTreeBase
import org.smartdox.parser.PureParser
import org.smartdox.Dox

/**
 * @since   Dec.  5, 2012
 * @version Dec.  5, 2012
 * @author  ASAMI, Tomoharu
 */
class PureSmartDoxEntity(aIn: GDataSource, aOut: GDataSource, aContext: GEntityContext) extends GEntity(aIn, aOut, aContext) with SmartDoxEntity {
  type DataSource_TYPE = GDataSource

  def dox: ValidationNEL[String, Dox] = _dox

  private var _dox: ValidationNEL[String, Dox] = m("文書が設定されていません。").failNel

  val doxContext = new GSubEntityContext(entityContext) {
    override def text_Encoding = Some("UTF-8")
  }

  def this(aDataSource: GDataSource, aContext: GEntityContext) = this(aDataSource, aDataSource, aContext)
  def this(aContext: GEntityContext) = this(null, aContext)

  override protected def open_Entity_Create() {
  }

  override protected def open_Entity_Create(aDataSource: GDataSource) {
    load_datasource(aDataSource)
  }

  override protected def open_Entity_Update(aDataSource: GDataSource) {
    load_datasource(aDataSource)
  }

  private def load_datasource(aDataSource: GDataSource) {
    name = aDataSource.simpleName
    val in = aDataSource.openReader()
    _dox = PureParser.parse(in)
    println("PureSmartDoxEntity#load_datasource = " + _dox)
  }

  override protected def write_Content(anOut: OutputStream): Unit = {
  }
}

class PureSmartDoxEntityClass extends GEntityClass {
  type Instance_TYPE = PureSmartDoxEntity

  override def accept_Suffix(suffix: String): Boolean = List("xml", "doxx").element(suffix)

  override def reconstitute_DataSource(aDataSource: GDataSource, aContext: GEntityContext): Option[Instance_TYPE] = Some(new PureSmartDoxEntity(aDataSource, aContext))
}

object PureSmartDoxEntity extends PureSmartDoxEntityClass
