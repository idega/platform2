/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.member.isi.block.accounting.presentation;

import com.idega.presentation.IWContext;

/**
 * @author palli
 */
public class UserCreditcard extends CashierSubWindowTemplate {

	/**
	 * 
	 */
	public UserCreditcard() {
		super();
	}

	public void main(IWContext iwc) {
		add("User creditcard info");
	}
}