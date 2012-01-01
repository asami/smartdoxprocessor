package org.smartdox.processor.services

import org.goldenport.service._
import org.goldenport.entity._
import org.smartdox.processor.entities.SmartDoxEntity
import org.smartdox.processor.transformers.plain.SmartDox2PlainTextRealmTransformer

/**
 * @since   Jan.  1, 2012
 * @version Jan.  2, 2012
 * @author  ASAMI, Tomoharu
 */
class PlainTextService(aCall: GServiceCall, serviceClass: GServiceClass) extends GService(aCall, serviceClass) {
  def execute_Service(aRequest: GServiceRequest, aResponse: GServiceResponse) = {
    val dox = aRequest.entity.asInstanceOf[SmartDoxEntity]
    val dox2plain = new SmartDox2PlainTextRealmTransformer(aCall.serviceContext, dox) 
    val plainRealm = dox2plain.transform
    aResponse.addRealm(plainRealm)
  }
}

object PlainTextService extends GServiceClass("plain") {
  def new_Service(aCall: GServiceCall): GService =
    new PlainTextService(aCall, this)
}
