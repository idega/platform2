package com.idega.block.creditcard.business;

/**
 * @author gimmi
 */
public class CreditCardTransaction {
	
	private String authNumber = null;
	private String authString = null;
	private Object authObject = null;
	
	public CreditCardTransaction(String authNumber, String authString, Object authObject) {
		this.authNumber  = authNumber;
		this.authString = authString;
		this.authObject = authObject;
	}
	
	public String getAuthorizationNumber() {
		return authNumber;
	}
	
	public String getAuthorizationString() {
		return authString;
	}
	
	public Object getAuthorizationObject() {
		return authObject;
	}
	
}
