/*
 * Created on 1.12.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.idega.block.staff.presentation;

import com.idega.block.staff.business.StaffUserBusiness;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;

/**
 * @author laddi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StaffUserViewer extends Block {

	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.staff";

	private IWBundle iwb;
	private IWResourceBundle iwrb;
	

	public void main(IWContext iwc) throws Exception {
		iwb = iwc.getIWMainApplication().getBundle(IW_CORE_BUNDLE_IDENTIFIER);
		iwrb = getResourceBundle(iwc);
		
	}

	private StaffUserBusiness getBusiness(IWApplicationContext iwac) {
		try {
			return (StaffUserBusiness) IBOLookup.getServiceInstance(iwac, StaffUserBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
}