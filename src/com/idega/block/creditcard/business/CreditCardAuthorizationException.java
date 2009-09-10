/*
 * Created on 15.12.2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.idega.block.creditcard.business;

import com.idega.idegaweb.IWResourceBundle;

/**
 * @author gimmi
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class CreditCardAuthorizationException extends Exception {

	protected String _errorMessage = null;
	protected String _errorNumber = null;
	protected String _displayError = null;

	
	/**
	 * 
	 */
	public CreditCardAuthorizationException() {
		super();
	}

	/**
	 * @param arg0
	 */
	public CreditCardAuthorizationException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public CreditCardAuthorizationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 */
	public CreditCardAuthorizationException(Throwable arg0) {
		super(arg0);
	}

	/**
	 *
	 */
	public void setErrorMessage(String message) {
		this._errorMessage = message;
	}

	/**
	 *
	 */
	public String getErrorMessage() {
		return(this._errorMessage);
	}

	/**
	 *
	 */
	public void setErrorNumber(String number) {
		this._errorNumber = number;
	}

	/**
	 *
	 */
	public String getErrorNumber() {
		return(this._errorNumber);
	}

	/**
	 *
	 */
	public void setDisplayError(String message) {
		this._displayError = message;
	}

	/**
	 *
	 */
	public String getDisplayError() {
		return(this._displayError);
	}
	
	public void setParentException(Exception e) {
		this.setStackTrace(e.getStackTrace());
	}
	
	public String getLocalizedMessage(IWResourceBundle iwrb) {
		return "unimplemented";
	}
}
