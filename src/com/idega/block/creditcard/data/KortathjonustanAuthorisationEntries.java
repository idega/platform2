/*
 * $Id: KortathjonustanAuthorisationEntries.java,v 1.3 2005/06/15 16:37:14 gimmi Exp $
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
 *  Last modified: $Date: 2005/06/15 16:37:14 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.3 $
 */
public interface KortathjonustanAuthorisationEntries extends IDOEntity, CreditCardAuthorizationEntry {

	public static final String AUTHORIZATION_TYPE_SALE = "0";
	public static final String AUTHORIZATION_TYPE_REFUND = "1";
	public static final String AUTHORIZATION_TYPE_DELAYED_TRANSACTION = "2";

	/**
	 * @see com.idega.block.creditcard.data.KortathjonustanAuthorisationEntriesBMPBean#getAmount
	 */
	public double getAmount();

	/**
	 * @see com.idega.block.creditcard.data.KortathjonustanAuthorisationEntriesBMPBean#setAmount
	 */
	public void setAmount(double amount);

	/**
	 * @see com.idega.block.creditcard.data.KortathjonustanAuthorisationEntriesBMPBean#getCurrency
	 */
	public String getCurrency();

	/**
	 * @see com.idega.block.creditcard.data.KortathjonustanAuthorisationEntriesBMPBean#setCurrency
	 */
	public void setCurrency(String currency);

	/**
	 * @see com.idega.block.creditcard.data.KortathjonustanAuthorisationEntriesBMPBean#getDate
	 */
	public Date getDate();

	/**
	 * @see com.idega.block.creditcard.data.KortathjonustanAuthorisationEntriesBMPBean#setDate
	 */
	public void setDate(Date date);

	/**
	 * @see com.idega.block.creditcard.data.KortathjonustanAuthorisationEntriesBMPBean#getCardExpires
	 */
	public String getCardExpires();

	/**
	 * @see com.idega.block.creditcard.data.KortathjonustanAuthorisationEntriesBMPBean#setCardExpires
	 */
	public void setCardExpires(String expires);

	/**
	 * @see com.idega.block.creditcard.data.KortathjonustanAuthorisationEntriesBMPBean#getCardNumber
	 */
	public String getCardNumber();

	/**
	 * @see com.idega.block.creditcard.data.KortathjonustanAuthorisationEntriesBMPBean#setCardNumber
	 */
	public void setCardNumber(String number);

	/**
	 * @see com.idega.block.creditcard.data.KortathjonustanAuthorisationEntriesBMPBean#getBrandName
	 */
	public String getBrandName();

	/**
	 * @see com.idega.block.creditcard.data.KortathjonustanAuthorisationEntriesBMPBean#setBrandName
	 */
	public void setBrandName(String name);

	/**
	 * @see com.idega.block.creditcard.data.KortathjonustanAuthorisationEntriesBMPBean#getAuthorizationCode
	 */
	public String getAuthorizationCode();

	/**
	 * @see com.idega.block.creditcard.data.KortathjonustanAuthorisationEntriesBMPBean#setAuthorizationCode
	 */
	public void setAuthorizationCode(String code);

	/**
	 * @see com.idega.block.creditcard.data.KortathjonustanAuthorisationEntriesBMPBean#getTransactionType
	 */
	public String getTransactionType();

	/**
	 * @see com.idega.block.creditcard.data.KortathjonustanAuthorisationEntriesBMPBean#setTransactionType
	 */
	public void setTransactionType(String type);

	/**
	 * @see com.idega.block.creditcard.data.KortathjonustanAuthorisationEntriesBMPBean#getParentID
	 */
	public int getParentID();

	/**
	 * @see com.idega.block.creditcard.data.KortathjonustanAuthorisationEntriesBMPBean#getParent
	 */
	public CreditCardAuthorizationEntry getParent();

	/**
	 * @see com.idega.block.creditcard.data.KortathjonustanAuthorisationEntriesBMPBean#setParentID
	 */
	public void setParentID(int id);

	/**
	 * @see com.idega.block.creditcard.data.KortathjonustanAuthorisationEntriesBMPBean#setErrorNumber
	 */
	public void setErrorNumber(String errorNumber);

	/**
	 * @see com.idega.block.creditcard.data.KortathjonustanAuthorisationEntriesBMPBean#getErrorNumber
	 */
	public String getErrorNumber();

	/**
	 * @see com.idega.block.creditcard.data.KortathjonustanAuthorisationEntriesBMPBean#setErrorText
	 */
	public void setErrorText(String errorText);

	/**
	 * @see com.idega.block.creditcard.data.KortathjonustanAuthorisationEntriesBMPBean#getErrorText
	 */
	public String getErrorText();

	/**
	 * @see com.idega.block.creditcard.data.KortathjonustanAuthorisationEntriesBMPBean#setServerResponse
	 */
	public void setServerResponse(String response);

	/**
	 * @see com.idega.block.creditcard.data.KortathjonustanAuthorisationEntriesBMPBean#getServerResponse
	 */
	public String getServerResponse();

	/**
	 * @see com.idega.block.creditcard.data.KortathjonustanAuthorisationEntriesBMPBean#getExtraField
	 */
	public String getExtraField();

	/**
	 * @see com.idega.block.creditcard.data.KortathjonustanAuthorisationEntriesBMPBean#getChild
	 */
	public CreditCardAuthorizationEntry getChild() throws FinderException;
}
