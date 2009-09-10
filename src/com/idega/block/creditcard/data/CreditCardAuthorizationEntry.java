/*
 * Created on 1.4.2004
 */
package com.idega.block.creditcard.data;

import java.sql.Date;

import javax.ejb.FinderException;

import com.idega.data.IDOEntity;

/**
 * @author gimmi
 */
public interface CreditCardAuthorizationEntry extends  IDOEntity {

  public static float amountMultiplier = 100;
 	
  public double getAmount();
	public String getCurrency();
	
	public Date getDate();
	
	/**
	 * Get the card expire date
	 * @return Exiredate for card MMYY
	 */
	public String getCardExpires();
	public String getCardNumber();
	
	public String getBrandName();
	public String getAuthorizationCode();
	
	public String getErrorNumber();
	public String getErrorText();
	
	public String getExtraField();

	public int getParentID();
	public CreditCardAuthorizationEntry getParent();
	
	public CreditCardAuthorizationEntry getChild() throws FinderException;
	
}
