package com.idega.block.creditcard.data;

import java.sql.Date;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;

/**
 * @author gimmi
 */
public class KortathjonustanAuthorisationEntriesBMPBean extends GenericEntity implements KortathjonustanAuthorisationEntries {

	private static final String TABLE_NAME = "CC_KORTTHJ_AUTH_ENTRIES";
	
	private static final String COLUMN_AMOUNT = "AMOUNT";
	private static final String COLUMN_AUTHORIZATION_CODE = "AUTH_CODE";
	private static final String COLUMN_BRAND_NAME = "BRAND_NAME";
	private static final String COLUMN_CARD_EXPIRES = "CARD_EXPIRES";
	private static final String COLUMN_CARD_NUMBER = "CARD_NUMBER";
	private static final String COLUMN_CURRENCY = "CURRENCY";
	private static final String COLUMN_DATE = "ENTRY_DATE";
	private static final String COLUMN_ERROR_NUMBER = "ERROR_NUMBER";
	private static final String COLUMN_ERROR_TEXT = "ERROR_TEXT";
	private static final String COLUMN_SERVER_RESPONSE = "SERVER_RESPONSE";
	
	private static final String COLUMN_PARENT_ID = "PARENT_ID";
	
	private static final String COLUMN_TRANSACTION_TYPE = "TRANSACTION_TYPE"; //sale or refund ?
	
	public String getEntityName() {
		return TABLE_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_AMOUNT, "amount", true, true, Double.class);
		addAttribute(COLUMN_AUTHORIZATION_CODE, "auth_code", true, true, String.class);
		addAttribute(COLUMN_BRAND_NAME, "brand_name", true, true, String.class);
		addAttribute(COLUMN_CARD_EXPIRES, "card expire date", true, true, String.class);
		addAttribute(COLUMN_CARD_NUMBER, "card number", true, true, String.class);
		addAttribute(COLUMN_CURRENCY, "currency", true, true, String.class);
		addAttribute(COLUMN_DATE, "date", true, true, Date.class);
		addAttribute(COLUMN_ERROR_NUMBER, "error number", true, true , String.class);
		addAttribute(COLUMN_ERROR_TEXT, "error text", true, true , String.class);		
		addAttribute(COLUMN_TRANSACTION_TYPE, "transaction_type", true, true, String.class);
		addOneToOneRelationship(COLUMN_PARENT_ID, KortathjonustanAuthorisationEntries.class);
		
		addAttribute(COLUMN_SERVER_RESPONSE, "server response", true, true, String.class, 1000);
	}

	public double getAmount() {
		return getDoubleColumnValue(COLUMN_AMOUNT);
	}
	
	public void setAmount(double amount) {
		setColumn(COLUMN_AMOUNT, amount);
	}

	public String getCurrency() {
		return getStringColumnValue(COLUMN_CURRENCY);
	}
	
	public void setCurrency(String currency) {
		setColumn(COLUMN_CURRENCY, currency);
	}

	public Date getDate() {
		return getDateColumnValue(COLUMN_DATE);
	}
	
	public void setDate(Date date) {
		setColumn(COLUMN_DATE, date);
	}

	public String getCardExpires() {
		return getStringColumnValue(COLUMN_CARD_EXPIRES);
	}

	public void setCardExpires(String expires) {
		setColumn(COLUMN_CARD_EXPIRES, expires);
	}
	
	public String getCardNumber() {
		return getStringColumnValue(COLUMN_CARD_NUMBER);
	}
	
	public void setCardNumber(String number) {
		setColumn(COLUMN_CARD_NUMBER, number);
	}

	public String getBrandName() {
		return getStringColumnValue(COLUMN_BRAND_NAME);
	}

	public void setBrandName(String name) {
		setColumn(COLUMN_BRAND_NAME, name);
	}
	
	public String getAuthorizationCode() {
		return getStringColumnValue(COLUMN_AUTHORIZATION_CODE);
	}
	
	public void setAuthorizationCode(String code) {
		setColumn(COLUMN_AUTHORIZATION_CODE, code);
	}
	
	public String getTransactionType() {
		return getStringColumnValue(COLUMN_TRANSACTION_TYPE);
	}
	
	public void setTransactionType(String type) {
		setColumn(COLUMN_TRANSACTION_TYPE, type);
	}
	
	public int getParentID() {
		return getIntColumnValue(COLUMN_PARENT_ID);
	}

	public CreditCardAuthorizationEntry getParent() {
		return (KortathjonustanAuthorisationEntries) getColumnValue(COLUMN_PARENT_ID);
	}
	
	public void setParentID(int id) {
		setColumn(COLUMN_PARENT_ID, id);
	}

	public Object ejbFindByAuthorizationCode(String code) throws FinderException {
		return this.idoFindOnePKByColumnBySQL(COLUMN_AUTHORIZATION_CODE, code);
	}

	
	public void setErrorNumber(String errorNumber) {
		setColumn(COLUMN_ERROR_NUMBER, errorNumber);
	}

	public String getErrorNumber() {
		return getStringColumnValue(COLUMN_ERROR_NUMBER);
	}

	public void setErrorText(String errorText) {
		setColumn(COLUMN_ERROR_TEXT, errorText);
	}

	public String getErrorText() {
		return getStringColumnValue(COLUMN_ERROR_TEXT);
	}
	
	public void setServerResponse(String response) {
		setColumn(COLUMN_SERVER_RESPONSE, response);
	}
	
	public String getServerResponse() {
		return getStringColumnValue(COLUMN_SERVER_RESPONSE);
	}
	
	public String getExtraField() {
		return getServerResponse();
	}
}
