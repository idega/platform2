/*
 * $Id: VATRegulations.java,v 1.1 2003/08/15 13:43:29 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.regulations.presentation;

import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.IWContext;

import se.idega.idegaweb.commune.presentation.CommuneBlock;

/**
 * VATRegulations is an idegaWeb block that handles VAT values and
 * regulations for providers.
 * <p>
 * Last modified: $Date: 2003/08/15 13:43:29 $ by $Author: anders $
 *
 * @author <a href="http://www.ncmedia.com">Anders Lindman</a>
 * @version $Revision: 1.1 $
 */
public class VATRegulations extends CommuneBlock {

	private final static int ACTION_DEFAULT = 0;
	
	/**
	 * Handles all of the blocks presentation.
	 * @param iwc user/session context 
	 */
	public void main(final IWContext iwc) {
		setResourceBundle(getResourceBundle(iwc));

		try {
			int action = parseAction(iwc);
			switch (action) {
				case ACTION_DEFAULT :
					viewDefaultForm(iwc);
					break;
			}
		}
		catch (Exception e) {
			super.add(new ExceptionWrapper(e, this));
		}
	}

	/*
	 * Returns the action constant for the action to perform based 
	 * on the POST parameters in the specified context.
	 */
	private int parseAction(IWContext iwc) {
		return ACTION_DEFAULT;
	}

	/*
	 * Adds the default form to the block.
	 */	
	private void viewDefaultForm(IWContext iwc) {
		Form form = new Form();
		add(form);
	}
}
