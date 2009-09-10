/*
 * $Id: TPosAuthorisationEntriesBean.java,v 1.3 2005/06/15 16:36:24 gimmi Exp $
 * Created on 8.6.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.creditcard.data;

import java.sql.Date;
import javax.ejb.FinderException;
import com.idega.data.IDOEntity;


/**
 * 
 *  Last modified: $Date: 2005/06/15 16:36:24 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.3 $
 */
public interface TPosAuthorisationEntriesBean extends IDOEntity, CreditCardAuthorizationEntry {

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#getAuthorisationAmount
	 */
	public String getAuthorisationAmount();

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#getAuthorisationCurrency
	 */
	public String getAuthorisationCurrency();

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#getAuthorisationCode
	 */
	public String getAuthorisationCode();

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#getAuthorisationIdRsp
	 */
	public String getAuthorisationIdRsp();

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#getAuthorisationPathReasonCode
	 */
	public String getAuthorisationPathReasonCode();

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#getBatchNumber
	 */
	public String getBatchNumber();

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#getBrandId
	 */
	public String getBrandId();

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#getBrandName
	 */
	public String getBrandName();

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#getCardCharacteristics
	 */
	public String getCardCharacteristics();

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#getCardType
	 */
	public String getCardType();

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#getCardName
	 */
	public String getCardName();

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#getEntryDate
	 */
	public String getEntryDate();

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#getDetailExpected
	 */
	public String getDetailExpected();

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#getErrorNo
	 */
	public String getErrorNo();

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#getErrorText
	 */
	public String getErrorText();

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#getCardExpires
	 */
	public String getCardExpires();

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#getLocationNr
	 */
	public String getLocationNr();

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#getMerchantNrAuthorisation
	 */
	public String getMerchantNrAuthorisation();

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#getMerchantNrOtherServices
	 */
	public String getMerchantNrOtherServices();

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#getMerchantNrSubmission
	 */
	public String getMerchantNrSubmission();

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#getAttachmentCount
	 */
	public String getAttachmentCount();

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#getPan
	 */
	public String getPan();

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#getPosNr
	 */
	public String getPosNr();

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#getPosSerialNr
	 */
	public String getPosSerialNr();

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#getPrintData
	 */
	public String getPrintData();

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#getSubmissionAmount
	 */
	public String getSubmissionAmount();

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#getSubmissionCurrency
	 */
	public String getSubmissionCurrency();

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#getEntryTime
	 */
	public String getEntryTime();

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#getTotalResponseCode
	 */
	public String getTotalResponseCode();

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#getTransactionNr
	 */
	public String getTransactionNr();

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#getVoidedAuthorisationIdResponse
	 */
	public String getVoidedAuthorisationIdResponse();

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#getVoidedTransactionNr
	 */
	public String getVoidedTransactionNr();

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#getXMLAttachment
	 */
	public String getXMLAttachment();

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#setAuthorisationAmount
	 */
	public void setAuthorisationAmount(String amount);

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#setAuthorisationCurrency
	 */
	public void setAuthorisationCurrency(String currency);

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#setAuthorisationCode
	 */
	public void setAuthorisationCode(String code);

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#setAuthorisationIdRsp
	 */
	public void setAuthorisationIdRsp(String rsp);

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#setAuthorisationPathReasonCode
	 */
	public void setAuthorisationPathReasonCode(String code);

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#setBatchNumber
	 */
	public void setBatchNumber(String number);

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#setBrandId
	 */
	public void setBrandId(String id);

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#setBrandName
	 */
	public void setBrandName(String name);

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#setCardCharacteristics
	 */
	public void setCardCharacteristics(String characteristics);

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#setCardType
	 */
	public void setCardType(String type);

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#setCardName
	 */
	public void setCardName(String name);

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#setEntryDate
	 */
	public void setEntryDate(String date);

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#setDetailExpected
	 */
	public void setDetailExpected(String expected);

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#setErrorNo
	 */
	public void setErrorNo(String no);

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#setErrorText
	 */
	public void setErrorText(String text);

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#setCardExpires
	 */
	public void setCardExpires(String expires);

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#setLocationNr
	 */
	public void setLocationNr(String location);

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#setMerchantNrAuthorisation
	 */
	public void setMerchantNrAuthorisation(String nr);

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#setMerchantNrOtherServices
	 */
	public void setMerchantNrOtherServices(String nr);

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#setMerchantNrSubmission
	 */
	public void setMerchantNrSubmission(String nr);

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#setAttachmentCount
	 */
	public void setAttachmentCount(String count);

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#setPan
	 */
	public void setPan(String pan);

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#setPosNr
	 */
	public void setPosNr(String nr);

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#setPosSerialNr
	 */
	public void setPosSerialNr(String nr);

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#setPrintData
	 */
	public void setPrintData(String data);

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#setSubmissionAmount
	 */
	public void setSubmissionAmount(String amount);

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#setSubmissionCurrency
	 */
	public void setSubmissionCurrency(String currency);

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#setEntryTime
	 */
	public void setEntryTime(String time);

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#setTotalResponseCode
	 */
	public void setTotalResponseCode(String code);

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#setTransactionNr
	 */
	public void setTransactionNr(String nr);

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#setVoidedAuthorisationIdResponse
	 */
	public void setVoidedAuthorisationIdResponse(String response);

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#setVoidedTransactionNr
	 */
	public void setVoidedTransactionNr(String nr);

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#setXMLAttachment
	 */
	public void setXMLAttachment(String xml);

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#getAmount
	 */
	public double getAmount();

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#getCurrency
	 */
	public String getCurrency();

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#getDate
	 */
	public Date getDate();

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#getAuthorizationCode
	 */
	public String getAuthorizationCode();

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#setCardNumber
	 */
	public void setCardNumber(String cardNumber);

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#getCardNumber
	 */
	public String getCardNumber();

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#getParentID
	 */
	public int getParentID();

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#getParent
	 */
	public CreditCardAuthorizationEntry getParent();

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#setParentID
	 */
	public void setParentID(int id);

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#getErrorNumber
	 */
	public String getErrorNumber();

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#getChild
	 */
	public CreditCardAuthorizationEntry getChild() throws FinderException;

	/**
	 * @see com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanBMPBean#getExtraField
	 */
	public String getExtraField();
}
