package org.smartdox.processor.services

import org.goldenport.service._
import org.goldenport.entity._
import org.smartdox.processor.entities.SmartDoxEntity
import org.smartdox.processor.transformers.html5.SmartDox2Html5RealmTransformer
import org.smartdox.processor.transformers.blogger.SmartDox2BloggerRealmTransformer

/**
 * @since   Jan. 11, 2012
 * @version Jan. 15, 2012
 * @author  ASAMI, Tomoharu
 */
class BloggerService(aCall: GServiceCall, serviceClass: GServiceClass) extends GService(aCall, serviceClass) {
  def execute_Service(aRequest: GServiceRequest, aResponse: GServiceResponse) = {
    val dox = aRequest.entity.asInstanceOf[SmartDoxEntity]
    val dox2html5 = new SmartDox2BloggerRealmTransformer(aCall.serviceContext, dox)
    val html5Realm = dox2html5.transform
    aResponse.addRealm(html5Realm)
  }
}

object BloggerService extends GServiceClass("blogger") {
  def new_Service(aCall: GServiceCall): GService =
    new BloggerService(aCall, this)
}
