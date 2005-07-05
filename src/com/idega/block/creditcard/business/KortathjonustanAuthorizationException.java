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
			case 1029 :
				return (iwrb.getLocalizedString("cc.creditcard_number_incorrect","Creditcard number incorrect"));
//			case 10909 :
//				return (iwrb.getLocalizedString("cc.error_maybe_CVC","An error occured, CVC code could be incorrect"));
			case 10102 :
				return (iwrb.getLocalizedString("cc.declined","Declined"));
			case 10101 :
				return (iwrb.getLocalizedString("cc.card_is_expired","Card is expired"));
			default:
				return (iwrb.getLocalizedString("cc.cannot_connect","Cannot communicate with server"));
		}

	}	
}
