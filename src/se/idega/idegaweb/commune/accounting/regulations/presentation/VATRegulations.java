/*
 * $Id: VATRegulations.java,v 1.3 2003/08/18 14:45:16 anders Exp $
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
import se.idega.idegaweb.commune.presentation.ListTable;

/**
 * VATRegulations is an idegaWeb block that handles VAT values and
 * regulations for providers.
 * <p>
 * Last modified: $Date: 2003/08/18 14:45:16 $ by $Author: anders $
 *
 * @author <a href="http://www.ncmedia.com">Anders Lindman</a>
 * @version $Revision: 1.3 $
 */
public class VATRegulations extends CommuneBlock {

	private final static int ACTION_DEFAULT = 0;
	
	private final static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune.accounting";

	private final static String KEY_PREFIX = "vat_regulations."; 
	
	private final static String KEY_PERIOD = KEY_PREFIX + "period";
	private final static String KEY_DESCRIPTION = KEY_PREFIX + "description";
	private final static String KEY_VAT_PERCENT = KEY_PREFIX + "vat_percent";
	private final static String KEY_DIRECTION = KEY_PREFIX + "direction";
	private final static String KEY_PROVIDER_TYPE = KEY_PREFIX + "provider_type";

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

	/**
	 * Returns the name of the bundle for this block.
	 */
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
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
		form.add(getVATList());	
		add(form);
	}
	
	/*
	 * Returns the VATList
	 */
	private ListTable getVATList() {
		ListTable list = new ListTable(this, 5);
		list.setHeader(localize(KEY_PERIOD, "Period"), 1);
		list.setHeader(localize(KEY_DESCRIPTION, "Benämning"), 2);
		list.setHeader(localize(KEY_VAT_PERCENT, "Procentsats"), 3);
		list.setHeader(localize(KEY_DIRECTION, "Ström"), 4);
		list.setHeader(localize(KEY_PROVIDER_TYPE, "Anordnartyp"), 5);
		
		list.add("0301-");
		list.add("Grundmoms");
		list.add("6");
		list.add("Ut");
		list.add("Privat");
		list.add("0301-");
		list.add("Grundmoms");
		list.add("6");
		list.add("Ut");
		list.add("Privat");
		list.add("0301-");
		list.add("Grundmoms");
		list.add("6");
		list.add("Ut");
		list.add("Privat");
		
		return list;
	}
}
