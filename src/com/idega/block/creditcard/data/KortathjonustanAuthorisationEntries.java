package com.idega.block.creditcard.data;


public interface KortathjonustanAuthorisationEntries extends com.idega.data.IDOEntity, CreditCardAuthorizationEntry
{
	
	public static final String AUTHORIZATION_TYPE_SALE = "0";
	public static final String AUTHORIZATION_TYPE_REFUND = "1";
	public static final String AUTHORIZATION_TYPE_DELAYED_TRANSACTION = "2";
	
 public double getAmount();
 public java.lang.String getAuthorizationCode();
 public java.lang.String getBrandName();
 public java.lang.String getCardExpires();
 public java.lang.String getCardNumber();
 public java.lang.String getCurrency();
 public java.sql.Date getDate();
 public java.lang.String getErrorNumber();
 public java.lang.String getErrorText();
 public com.idega.block.creditcard.data.CreditCardAuthorizationEntry getParent();
 public int getParentID();
 public java.lang.String getTransactionType();
 public void setAmount(double p0);
 public void setAuthorizationCode(java.lang.String p0);
 public void setBrandName(java.lang.String p0);
 public void setCardExpires(java.lang.String p0);
 public void setCardNumber(java.lang.String p0);
 public void setCurrency(java.lang.String p0);
 public void setDate(java.sql.Date p0);
 public void setErrorNumber(java.lang.String p0);
 public void setErrorText(java.lang.String p0);
 public void setParentID(int p0);
 public void setTransactionType(java.lang.String p0);
 public void setServerResponse(String response);
 public String getServerResponse();
}
