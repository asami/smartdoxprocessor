package org.smartdox.processor.services

import org.goldenport.service._
import org.goldenport.entity._
import org.smartdox.processor.entities.SmartDoxEntity
import org.smartdox.processor.transformers.latex.SmartDox2LatexRealmTransformer

/**
 * @since   Jan.  1, 2012
 * @version Jan.  2, 2012
 * @author  ASAMI, Tomoharu
 */
class LatexService(aCall: GServiceCall, serviceClass: GServiceClass) extends GService(aCall, serviceClass) {
  def execute_Service(aRequest: GServiceRequest, aResponse: GServiceResponse) = {
    val dox = aRequest.entity.asInstanceOf[SmartDoxEntity]
    val dox2latex = new SmartDox2LatexRealmTransformer(aCall.serviceContext, dox)
    val latexRealm = dox2latex.transform
    aResponse.addRealm(latexRealm)
  }
}

object LatexService extends GServiceClass("latex") {
  def new_Service(aCall: GServiceCall): GService =
    new LatexService(aCall, this)
}
