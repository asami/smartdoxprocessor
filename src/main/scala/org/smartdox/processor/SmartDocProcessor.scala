package org.smartdox.processor

import org.xmlsmartdoc.SmartDoc.{SmartDoc => XmlSmartDoc}

/*
 * @since   Jan.  7, 2012
 * @version Jan.  7, 2012
 * @auther  ASAMI, Tomoharu
 */
class SmartDocApp extends xsbti.AppMain {
  def run(config: xsbti.AppConfiguration) = {
    val sdoc = new XmlSmartDoc(config.arguments)
    sdoc.start()
    new xsbti.Exit {
      val code = 0
    }
  }
}

object SmartDocMain {
  def main(args: Array[String]) {
    XmlSmartDoc.main(args)
  }
}
