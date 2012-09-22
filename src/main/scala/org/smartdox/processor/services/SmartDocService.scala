package org.smartdox.processor.services

import org.goldenport.service._
import org.goldenport.entity._
import org.smartdox.processor.entities.SmartDoxEntity
import org.smartdox.processor.transformers.sdoc.SmartDox2SmartDocRealmTransformer

/**
 * @since   Sep. 22, 2012
 * @version Sep. 22, 2012
 * @author  ASAMI, Tomoharu
 */
class SmartDocService(aCall: GServiceCall, serviceClass: GServiceClass) extends GService(aCall, serviceClass) {
  def execute_Service(aRequest: GServiceRequest, aResponse: GServiceResponse) = {
    val dox = aRequest.entity.asInstanceOf[SmartDoxEntity]
    val dox2sdoc = new SmartDox2SmartDocRealmTransformer(aCall.serviceContext, dox)
    val sdocRealm = dox2sdoc.transform
    aResponse.addRealm(sdocRealm)
  }
}

object SmartDocService extends GServiceClass("sdoc") {
  def new_Service(aCall: GServiceCall): GService =
    new SmartDocService(aCall, this)
}
