/*
 * Created on 25.3.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.idega.block.creditcard.data;

import java.sql.Timestamp;

import javax.ejb.RemoveException;

import com.idega.data.IDOEntity;

/**
 * @author gimmi
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface CreditCardMerchant extends IDOEntity{
	public static final String MERCHANT_TYPE_TPOS = "TPOS";
	public static final String MERCHANT_TYPE_KORTHATHJONUSTAN = "KORTATHJONUSTAN";
	
  public static final String COLUMN_IS_DELETED = "IS_DELETED";

	public String getType();
	
	public String getName();
	public String getLocation();
	public String getUser();
	public String getPassword();
	public String getTerminalID();
	public String getMerchantID();
	public String getExtraInfo();
	public Timestamp getStartDate();
	public Timestamp getModificationDate();
	public Timestamp getEndDate();
	public boolean getIsDeleted();

	public void setName(String name);
	public void setLocation(String location);
	public void setUser(String user);
	public void setPassword(String password);
	public void setTerminalID(String terminalID);
	public void setMerchantID(String id);
	public void setExtraInfo(String extra);

	/**
	 * Modification date and possibly StartDate are set in store();
	 */
	public void store();
	/**
	 * End date is set in remove();
	 */
	public void remove() throws RemoveException;
}
