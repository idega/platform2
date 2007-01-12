/*
 * Created on Mar 10, 2004
 *
 */
package com.idega.block.finance.business;

/**
 * FinanceException
 * @author aron 
 * @version 1.0
 */
public class FinanceException extends Exception {
	
	private String textKey = null;
	private String defaultText = null;
	
	/**
	 * Constructs an accounting exception with the specified text key and default text.
	 * @param textKey the text key for the error message
	 * @param defaultText the default text for the error message
	 */
	public FinanceException(String textKey, String defaultText) {
		super(textKey);
		this.textKey = textKey;
		this.defaultText = defaultText;
	}
	
	/**
	 * Constructs an accounting exception with the specified text key and default text.
	 * @param textKey the text key for the error message
	 * @param defaultText the default text for the error message
	 */
	public FinanceException(String textKey, String defaultText,Exception cause) {
		super(textKey,cause);
		this.textKey = textKey;
		this.defaultText = defaultText;
	}
	
	/**
	 * Returns the error message text key.
	 */
	public String getTextKey() {
		return this.textKey; 
	}
	
	/**
	 * Returns the default error message text.
	 */
	public String getDefaultText() {
		return this.defaultText; 
	}
}
