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
public class UserContract extends CashierSubWindowTemplate {

	/**
	 * 
	 */
	public UserContract() {
		super();
	}

	public void main(IWContext iwc) {
		add("Enter user contract");
	}
}