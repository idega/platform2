/*

 *  $Id: TPosAuthorisationEntries.java,v 1.1 2004/04/22 21:40:27 gimmi Exp $

 *

 *  Copyright (C) 2002 Idega hf. All Rights Reserved.

 *

 *  This software is the proprietary information of Idega hf.

 *  Use is subject to license terms.

 *

 */

package com.idega.block.creditcard.data;



/**

 * @author    <a href="mail:palli@idega.is">Pall Helgason</a>

 * @version   1.0

 */

public interface TPosAuthorisationEntries {

  public String getAuthorisationAmount();

  public String getAuthorisationCurrency();

  public String getAuthorisationCode();

  public String getAuthorisationIdRsp();

  public String getAuthorisationPathReasonCode();

  public String getBatchNumber();

  public String getBrandId();

  public String getBrandName();

  public String getCardCharacteristics();

  public String getCardType();

  public String getCardName();

  public String getEntryDate();

  public String getDetailExpected();

  public String getErrorNo();

  public String getErrorText();

  public String getCardExpires();

  public String getLocationNr();

  public String getMerchantNrAuthorisation();

  public String getMerchantNrOtherServices();

  public String getMerchantNrSubmission();

  public String getAttachmentCount();

  public String getPan();

  public String getPosNr();

  public String getPosSerialNr();

  public String getPrintData();

  public String getSubmissionAmount();

  public String getSubmissionCurrency();

  public String getEntryTime();

  public String getTotalResponseCode();

  public String getTransactionNr();

  public String getVoidedAuthorisationIdResponse();

  public String getVoidedTransactionNr();

  public String getXMLAttachment();



  public void setAuthorisationAmount(String amount);

  public void setAuthorisationCurrency(String currency);

  public void setAuthorisationCode(String code);

  public void setAuthorisationIdRsp(String rsp);

  public void setAuthorisationPathReasonCode(String code);

  public void setBatchNumber(String number);

  public void setBrandId(String id);

  public void setBrandName(String name);

  public void setCardCharacteristics(String characteristics);

  public void setCardType(String type);

  public void setCardName(String name);

  public void setEntryDate(String date);

  public void setDetailExpected(String expected);

  public void setErrorNo(String no);

  public void setErrorText(String text);

  public void setCardExpires(String expires);

  public void setLocationNr(String location);

  public void setMerchantNrAuthorisation(String nr);

  public void setMerchantNrOtherServices(String nr);

  public void setMerchantNrSubmission(String nr);

  public void setAttachmentCount(String count);

  public void setPan(String pan);

  public void setPosNr(String nr);

  public void setPosSerialNr(String nr);

  public void setPrintData(String data);

  public void setSubmissionAmount(String amount);

  public void setSubmissionCurrency(String currency);

  public void setEntryTime(String time);

  public void setTotalResponseCode(String code);

  public void setTransactionNr(String nr);

  public void setVoidedAuthorisationIdResponse(String response);

  public void setVoidedTransactionNr(String nr);

  public void setXMLAttachment(String xml);

}
