package org.smartdox.processor

import scalaz._, Scalaz._
import org.goldenport.Z._
import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.goldenport.scalatest.ScalazMatchers

/*
 * @since   Aug.  5, 2012
 * @version Aug.  5, 2012
 * @author  ASAMI, Tomoharu
 */
@RunWith(classOf[JUnitRunner])
class SmartDoxProcessorSpec extends WordSpec with ShouldMatchers with ScalazMatchers {
  val TestReadDir = "/tmp/goldenport.d/read/"
  val TestCreateDir = "/tmp/goldenport.d/create/smartdox/"

  "SmartDoxProcessor" should {
    "execution" that {
      "no file" in {
        val args = Array[String]("-blogger", "nodata.org", "-output:" + TestCreateDir)
        val doxp = new SmartDox(args)
        doxp.executeShellCommand(args)
      }
    }
  }
}
