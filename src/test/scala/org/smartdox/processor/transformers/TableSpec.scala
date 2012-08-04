package org.smartdox.processor.transformers

import scalaz._, Scalaz._
import org.goldenport.Z._
import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.goldenport.scalatest.ScalazMatchers
import org.smartdox.processor.SmartDox

/*
 * @since   Jul. 11, 2012
 * @version Aug.  4, 2012
 * @author  ASAMI, Tomoharu
 */
@RunWith(classOf[JUnitRunner])
class TableSpec extends WordSpec with ShouldMatchers with ScalazMatchers {
  val TestReadDir = "/tmp/goldenport.d/read/"
  val TestCreateDir = "/tmp/goldenport.d/create/smartdox/"

  "Table" should {
    "Table" that {
      "CSV" in {
        val args = Array[String]("-blogger", "resource:/org/smartdox/processor/transformers/test.org", "-output:" + TestCreateDir)
//        val args = Array[String]("-blogger", TestReadDir + "test.org", "-output:" + TestCreateDir, "-container.log:trace")
        val doxp = new SmartDox(args)
        doxp.executeShellCommand(args)
      }
      "Excel" in {
        val args = Array[String]("-blogger", "resource:/org/smartdox/processor/transformers/testexcel.org", "-output:" + TestCreateDir)
//        val args = Array[String]("-blogger", TestReadDir + "testexcel.org", "-output:" + TestCreateDir, "-container.log:trace")
        val doxp = new SmartDox(args)
        doxp.executeShellCommand(args)
      }
      "Excelx" in {
//        val args = Array[String]("-blogger", "resource:/org/smartdox.processor.transformers/test.csv", "-output:" + TestCreateDir)
        val args = Array[String]("-blogger", TestReadDir + "testexcelx.org", "-output:" + TestCreateDir, "-container.log:trace")
        val doxp = new SmartDox(args)
        doxp.executeShellCommand(args)
      }
      "Excelx title" in {
//        val args = Array[String]("-blogger", "resource:/org/smartdox.processor.transformers/test.csv", "-output:" + TestCreateDir)
        val args = Array[String]("-blogger", TestReadDir + "testexcelxtitle.org", "-output:" + TestCreateDir, "-container.log:trace")
        val doxp = new SmartDox(args)
        doxp.executeShellCommand(args)
      }
    }
  }
}
