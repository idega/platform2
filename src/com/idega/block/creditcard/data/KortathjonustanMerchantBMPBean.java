package com.idega.block.creditcard.data;

import java.sql.Timestamp;

import javax.ejb.RemoveException;

import com.idega.data.GenericEntity;
import com.idega.util.IWTimestamp;

/**
 * @author gimmi
 */

public class KortathjonustanMerchantBMPBean extends GenericEntity implements KortathjonustanMerchant {

	private static final String ENTITY_NAME = "CC_KTH_MERCHANT";
	private static final String COLUMN_NAME = "MERCHANT_NAME";
	private static final String COLUMN_SITE = "SITE";
	private static final String COLUMN_USER = "USER_ID";
	private static final String COLUMN_PASSWORD    = "USER_PASSWORD";
	private static final String COLUMN_ACCEPTOR_TERMINAL_ID = "ACCEPTOR_TERM_ID";
	private static final String COLUMN_ACCEPTOR_IDENTIFICATION = "ACCEPTOR_IDENTIFICATION";
	private static final String COLUMN_START_DATE = "START_DATE";
	private static final String COLUMN_MODIFICATION_DATE = "MODIFICATION_DATE";
	private static final String COLUMN_END_DATE = "END_DATE";

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public String getType() {
		return MERCHANT_TYPE_KORTHATHJONUSTAN;
	}
	
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME, "name", true, true, String.class);
		addAttribute(COLUMN_SITE, "site", true, true, String.class);
		addAttribute(COLUMN_USER, "user", true, true, String.class);
		addAttribute(COLUMN_PASSWORD, "password", true, true, String.class);
		addAttribute(COLUMN_ACCEPTOR_TERMINAL_ID, "accTermID", true, true, String.class);
		addAttribute(COLUMN_ACCEPTOR_IDENTIFICATION, "accId", true, true, String.class);
    addAttribute(COLUMN_START_DATE, "Start date", true, true, Timestamp.class);
    addAttribute(COLUMN_MODIFICATION_DATE, "Modification date", true, true, Timestamp.class);
    addAttribute(COLUMN_END_DATE, "End date", true, true, Timestamp.class);
    addAttribute(COLUMN_IS_DELETED, "Is delted", true, true, Boolean.class);
	}

	public void setName(String name) {
		setColumn(COLUMN_NAME, name);
	}
	
	public String getName() {
		return getStringColumnValue(COLUMN_NAME);
	}
	
	/**
	 * @see com.idega.block.creditcard.data.CreditCardMerchant#getLocation()
	 */
	public String getLocation() {
		return getStringColumnValue(COLUMN_SITE);
	}

	/**
	 * @see com.idega.block.creditcard.data.CreditCardMerchant#getUser()
	 */
	public String getUser() {
		return getStringColumnValue(COLUMN_USER);
	}

	/**
	 * @see com.idega.block.creditcard.data.CreditCardMerchant#getPassword()
	 */
	public String getPassword() {
		return getStringColumnValue(COLUMN_PASSWORD);
	}

	/**
	 * @see com.idega.block.creditcard.data.CreditCardMerchant#getTerminalID()
	 */
	public String getTerminalID() {
		return getStringColumnValue(COLUMN_ACCEPTOR_TERMINAL_ID);
	}

	/**
	 * @see com.idega.block.creditcard.data.CreditCardMerchant#getMerchantID()
	 */
	public String getMerchantID() {
		return getStringColumnValue(COLUMN_ACCEPTOR_IDENTIFICATION);
	}

	/**
	 * Not implemented
	 */
	public String getExtraInfo() {
		return null;
	}

	/** (non-Javadoc)
	 * @see com.idega.block.creditcard.data.CreditCardMerchant#setLocation(java.lang.String)
	 */
	public void setLocation(String location) {
		setColumn(COLUMN_SITE, location);
	}

	/**
	 * @see com.idega.block.creditcard.data.CreditCardMerchant#setUser(java.lang.String)
	 */
	public void setUser(String user) {
		setColumn(COLUMN_USER, user);
	}

	/**
	 * @see com.idega.block.creditcard.data.CreditCardMerchant#setPassword(java.lang.String)
	 */
	public void setPassword(String password) {
		setColumn(COLUMN_PASSWORD, password);
	}

	/**
	 * @see com.idega.block.creditcard.data.CreditCardMerchant#setTerminalID(java.lang.String)
	 */
	public void setTerminalID(String terminalID) {
		setColumn(COLUMN_ACCEPTOR_TERMINAL_ID, terminalID);
	}

	/**
	 * @see com.idega.block.creditcard.data.CreditCardMerchant#setMerchantID(java.lang.String)
	 */
	public void setMerchantID(String id) {
		setColumn(COLUMN_ACCEPTOR_IDENTIFICATION, id);
	}

	/**
	 * Not Implemented
	 */
	public void setExtraInfo(String extra) {
	}

	/**
	 * @see com.idega.block.creditcard.data.CreditCardMerchant#getStartDate()
	 */
	public Timestamp getStartDate() {
		return getTimestampColumnValue(COLUMN_START_DATE);
	}

	/**
	 * @see com.idega.block.creditcard.data.CreditCardMerchant#getEndDate()
	 */
	public Timestamp getEndDate() {
		return getTimestampColumnValue(COLUMN_END_DATE);
	}

	/**
	 * @see com.idega.block.creditcard.data.CreditCardMerchant#IsDeleted()
	 */
	public boolean getIsDeleted() {
		return getBooleanColumnValue(COLUMN_IS_DELETED);
	}

	private void setStartDate(Timestamp startDate) {
		setColumn(COLUMN_START_DATE, startDate);
	}

	/**
	 * @see com.idega.block.creditcard.data.CreditCardMerchant#setEndDate(java.sql.Date)
	 */
	private void setEndDate(Timestamp endDate) {
		setColumn(COLUMN_END_DATE, endDate);
	}

	/**
	 * @see com.idega.block.creditcard.data.CreditCardMerchant#getModificationDate()
	 */
	public Timestamp getModificationDate() {
		return getTimestampColumnValue(COLUMN_MODIFICATION_DATE);
	}

	private void setModificationDate(Timestamp modificationDate) {
		setColumn(COLUMN_MODIFICATION_DATE, modificationDate);
	}

	/**
	 * @see com.idega.block.creditcard.data.CreditCardMerchant#store()
	 */
	public void store() {
		setModificationDate(IWTimestamp.RightNow().getTimestamp());
		if (getStartDate() == null) {
			setStartDate(IWTimestamp.RightNow().getTimestamp());
		}
		super.store();
	}

	/**
	 * @see com.idega.block.creditcard.data.CreditCardMerchant#remove()
	 */
	public void remove() throws RemoveException {
		setModificationDate(IWTimestamp.RightNow().getTimestamp());
		setEndDate(IWTimestamp.RightNow().getTimestamp());
		setColumn(COLUMN_IS_DELETED, true);
		store();
	}
}
