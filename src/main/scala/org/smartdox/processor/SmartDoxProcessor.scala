package org.smartdox.processor

import org.goldenport._
import org.goldenport.application.GApplicationDescriptor
import org.goldenport.service._
import org.goldenport.entity._
import org.goldenport.entities.csv._
import org.goldenport.entities.xmind._
import content.BinaryContent
import org.goldenport.exporter.FirstLeafResultExporterClass
import org.smartdox.processor.entities.SmartDoxEntity
import org.smartdox.processor.importers.SmartDoxImporter
import org.goldenport.entities.smartdoc.SmartDocEntity
import org.smartdox.processor.entities.SmartDocEntity
import org.smartdox.processor.services._

/*
 * @since   Jan.  1, 2012
 * @version Jan. 30, 2012
 * @auther  ASAMI, Tomoharu
 */
class SmartDox(args: Array[String]) {
  lazy val goldenport = new Goldenport(args, new SmartDoxDescriptor)

  final def executeShellCommand(args: Array[String]) {
    goldenport.open()
    goldenport.executeShellCommand(args)
    goldenport.close()
  }
}

class AppMain extends xsbti.AppMain {
  def run(config: xsbti.AppConfiguration) = {
    val args = config.arguments
    val smartdox = new SmartDox(args)
    smartdox.executeShellCommand(args)
    new xsbti.Exit {
      val code = 0
    }
  }    
}

object Main {
  def main(args: Array[String]) {
    val smartdox = new SmartDox(args)
    smartdox.executeShellCommand(args)
  }
}

class SmartDoxDescriptor extends GApplicationDescriptor {
  name = "SmartDox"
  version = "0.2.2-SNAPSHOT"
  version_build = "20120210"
  copyright_years = "1998-2012"
  copyright_owner = "ASAMI, Tomoharu"
  command_name = "dox"
  //
  importers(SmartDoxImporter)
  entities(SmartDoxEntity, SmartDocEntity, XMindEntity)
  services(Html4Service,
	   Html3Service,
	   LatexService,
	   PdfService,
	   PlainTextService,
	   BloggerService,
	   Html5Service)
}
