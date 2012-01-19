package org.smartdox.processor.services

import org.goldenport.service._
import org.goldenport.entity._
import org.smartdox.processor.entities.SmartDoxEntity
import org.smartdox.processor.transformers.pdf.SmartDox2PdfRealmTransformer

/**
 * @since   Jan. 19, 2012
 * @version Jan. 19, 2012
 * @author  ASAMI, Tomoharu
 */
class PdfService(aCall: GServiceCall, serviceClass: GServiceClass) extends GService(aCall, serviceClass) {
  def execute_Service(aRequest: GServiceRequest, aResponse: GServiceResponse) = {
    val dox = aRequest.entity.asInstanceOf[SmartDoxEntity]
    val dox2pdf = new SmartDox2PdfRealmTransformer(aCall.serviceContext, dox)
    val pdfRealm = dox2pdf.transform
    aResponse.addRealm(pdfRealm)
  }
}

object PdfService extends GServiceClass("pdf") {
  def new_Service(aCall: GServiceCall): GService =
    new PdfService(aCall, this)
}
