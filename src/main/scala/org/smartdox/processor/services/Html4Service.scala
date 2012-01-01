package org.smartdox.processor.services

import org.goldenport.service._
import org.goldenport.entity._
import org.smartdox.processor.entities.SmartDoxEntity
import org.smartdox.processor.transformers.html4.SmartDox2Html4RealmTransformer

/**
 * @since   Jan.  1, 2012
 * @version Jan.  2, 2012
 * @author  ASAMI, Tomoharu
 */
class Html4Service(aCall: GServiceCall, serviceClass: GServiceClass) extends GService(aCall, serviceClass) {
  def execute_Service(aRequest: GServiceRequest, aResponse: GServiceResponse) = {
    val dox = aRequest.entity.asInstanceOf[SmartDoxEntity]
    val dox2html4 = new SmartDox2Html4RealmTransformer(aCall.serviceContext, dox) 
    val html4Realm = dox2html4.transform
    aResponse.addRealm(html4Realm)
  }
}

object Html4Service extends GServiceClass("html4") {
  def new_Service(aCall: GServiceCall): GService =
    new Html4Service(aCall, this)
}
