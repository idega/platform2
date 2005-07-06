/*
 * $Id: TravelCarRentalBrowser.java,v 1.2 2005/07/06 02:22:33 gimmi Exp $
 * Created on Jul 5, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.service.carrental.presentation;

import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import is.idega.idegaweb.travel.presentation.TravelSupplierBrowser;


public class TravelCarRentalBrowser extends TravelSupplierBrowser {

	protected Class getPlugin() {
		return CarRentalBrowser.class;
	}

	protected Table getHeaderTable(IWContext iwc) {
		return null;
	}

	protected boolean showBrowser(IWContext iwc) {
		return true;
	}
}
