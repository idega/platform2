/*
 * $Id$
 *
 * Copyright (C) 2000-2003 Idega Software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega Software.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.atvr.supplier.application.presentation;

import java.rmi.RemoteException;

import is.idega.idegaweb.atvr.supplier.application.business.NewProductApplicationBusiness;

import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;

/**
 * @author palli
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class TestReportsReader extends Block {
	public void main(IWContext iwc) {
		IWBundle bun = iwc.getApplication().getBundle("is.idega.idegaweb.atvr");
		String path = bun.getProperty("npaa_report_location");
		
		if (iwc.isParameterSet("submit")) {
			NewProductApplicationBusiness biz = getNewProductApplicationBusiness(iwc);
			if (biz != null)
				try {
					biz.checkForNewReports(path,bun);
				}
				catch (RemoteException e) {
					e.printStackTrace();
				}
		}
		
		Form f = new Form();
		SubmitButton b = new SubmitButton("Testa","submit","submit");
		b.setAsImageButton(true);
		f.add(b);
		add(f);
	}
	
	public NewProductApplicationBusiness getNewProductApplicationBusiness(IWApplicationContext iwc) {
		NewProductApplicationBusiness business = null;
		try {
			business = (NewProductApplicationBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, NewProductApplicationBusiness.class);
		}
		catch (java.rmi.RemoteException rme) {
			throw new RuntimeException(rme.getMessage());
		}
		return business;
	}	
}