package org.smartdox.processor.importers

import scala.collection.mutable.ArrayBuffer
import org.goldenport.importer._
import org.goldenport.service._
import org.goldenport.entity.GEntity

/*
 * @since   Jan.  1, 2012
 *  version Jan. 11, 2012
 * @version Jul. 14, 2012
 * @author  ASAMI, Tomoharu
 */
class SmartDoxImporter(aCall: GServiceCall) extends GImporter(aCall) {
//  val packageNames: Seq[String] = request.parameter("source.package") match {
//    case Some(name) => name.asInstanceOf[AnyRef].toString.split(":").map(_.trim)
//    case None       => Nil
//  }
//  val packageName = if (packageNames.isEmpty) "" else packageNames.head

  override def execute_Import() {
    val args = request.arguments.map(reconstitute_entity)
    if (!args.isEmpty) {
      request.setEntity(args(0).get)
      request.setEntities(args.map(_.get))
    }
  }
}

object SmartDoxImporter extends GImporterClass {
  type Instance_TYPE = SmartDoxImporter

  override protected def accept_Service_Call(aCall: GServiceCall): Option[Boolean] = {
    aCall.service.name match {
      case "html3"    => Some(true)
      case "html4"    => Some(true)
      case "html5"  => Some(true)
      case "latex"     => Some(true)
      case "plain"    => Some(true)
      case "pdf"   => Some(true)
      case "blogger" => Some(true)
      case _         => None
    }
  }

  override protected def new_Importer(aCall: GServiceCall): GImporter = {
    new SmartDoxImporter(aCall)
  }
}
