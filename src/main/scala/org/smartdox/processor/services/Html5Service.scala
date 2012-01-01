package org.smartdox.processor.services

import org.goldenport.service._
import org.goldenport.entity._
import org.smartdox.processor.entities.SmartDoxEntity
import org.smartdox.processor.transformers.html5.SmartDox2Html5RealmTransformer

/**
 * @since   Jan.  1, 2012
 * @version Jan.  2, 2012
 * @author  ASAMI, Tomoharu
 */
class Html5Service(aCall: GServiceCall, serviceClass: GServiceClass) extends GService(aCall, serviceClass) {
  def execute_Service(aRequest: GServiceRequest, aResponse: GServiceResponse) = {
    val dox = aRequest.entity.asInstanceOf[SmartDoxEntity]
    val dox2html5 = new SmartDox2Html5RealmTransformer(aCall.serviceContext, dox)
    val html5Realm = dox2html5.transform
    aResponse.addRealm(html5Realm)
  }
}

object Html5Service extends GServiceClass("html5") {
  def new_Service(aCall: GServiceCall): GService =
    new Html5Service(aCall, this)
}
