package org.smartdox.processor.services

import org.goldenport.service._
import org.goldenport.entity._
import org.smartdox.processor.entities.SmartDoxEntity
import org.smartdox.processor.transformers.html3.SmartDox2Html3RealmTransformer

/**
 * @since   Jan.  1, 2012
 * @version Jan.  2, 2012
 * @author  ASAMI, Tomoharu
 */
class Html3Service(aCall: GServiceCall, serviceClass: GServiceClass) extends GService(aCall, serviceClass) {
  def execute_Service(aRequest: GServiceRequest, aResponse: GServiceResponse) = {
    val dox = aRequest.entity.asInstanceOf[SmartDoxEntity]
    val dox2html3 = new SmartDox2Html3RealmTransformer(aCall.serviceContext, dox) 
    val html3Realm = dox2html3.transform()
    aResponse.addRealm(html3Realm)
  }
}

object Html3Service extends GServiceClass("html3") {
  def new_Service(aCall: GServiceCall): GService =
    new Html3Service(aCall, this)
}
