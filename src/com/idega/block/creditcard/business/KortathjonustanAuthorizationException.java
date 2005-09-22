/*
 * Created on 15.4.2004
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
public class KortathjonustanAuthorizationException extends CreditCardAuthorizationException {

	public KortathjonustanAuthorizationException() {
		super();
	}

	public KortathjonustanAuthorizationException(String arg0) {
		super(arg0);
	}

	public KortathjonustanAuthorizationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public KortathjonustanAuthorizationException(Throwable arg0) {
		super(arg0);
	}
	
	public String getLocalizedMessage(IWResourceBundle iwrb) {
		System.out.println("Kortathjonustan errormessage = " + this.getErrorMessage());
		System.out.println("number = " + this.getErrorNumber());
		System.out.println("display = " + this.getDisplayError());
		int number = -2;
		try {
			number = Integer.parseInt(this.getErrorNumber());
		} catch (NumberFormatException ignore) {}
		
		switch (number) {
			case 946 :
				return (iwrb.getLocalizedString("cc.system_failure_retry","System failure - Retry"));
			case 100 :
				return (iwrb.getLocalizedString("cc.declined","Declined"));
			case 909 :
				return (iwrb.getLocalizedString("cc.system_failure_error","System failure - Error"));
			default:
				return (iwrb.getLocalizedString("cc.cannot_connect","Cannot communicate with server"));
		}

	}	
}
