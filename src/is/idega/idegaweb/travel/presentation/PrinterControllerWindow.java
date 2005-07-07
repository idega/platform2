/*
 * $Id: PrinterControllerWindow.java,v 1.1 2005/07/07 18:57:43 gimmi Exp $
 * Created on Jul 7, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.presentation;

import is.idega.idegaweb.travel.business.TravelSessionManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import com.idega.block.trade.stockroom.business.TradeConstants;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.Window;


public class PrinterControllerWindow extends Window {
	
	public static final String ACTION = "pcw_a";
	public static final String ACTION_REBOOT = "pcw_arb";
	public static final String ACTION_RESET = "pcw_ars";
	public static final String ACTION_TEST_FEED = "pcw_atf";

	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		
		String action = iwc.getParameter(ACTION);
		
		if (getTravelSessionManager(iwc).hasRole(TradeConstants.SUPPLIER_MANAGER_ROLE_KEY)
				|| getTravelSessionManager(iwc).hasRole(TradeConstants.ROLE_SUPPLIER_MANAGER_CASHIER_STAFF)
				|| getTravelSessionManager(iwc).hasRole(TradeConstants.ROLE_SUPPLIER_MANAGER_BOOKING_STAFF)) {
		
			if (action != null) {
				add("<font color=\"BLACK\">");
				
				if (action.equals(ACTION_REBOOT)) {
					add("REBOOT");
				} else if (action.equals(ACTION_TEST_FEED)) {
					add("TESTFEED");
				} else if (action.equals(ACTION_RESET)) {
					resetPrinter();
				} else {
					add("HUH");
				}
				
	
				add("</font>");
				this.setOnLoad("window.parent.focus();window.resizeTo(1,1);window.print();window.close();");
			
			}
		} else {
			add("no permission");
		}
		
	}
	
	private void resetPrinter() throws IOException {
		IWBundle bundle = getIWApplicationContext().getIWMainApplication().getBundle(TravelBlock.IW_BUNDLE_IDENTIFIER);
		
		String fileLoc = bundle.getResourcesRealPath()+"/printerSettings.txt";
		File restartScript = new File(fileLoc);
	    FileReader fr = new FileReader(restartScript);
	    BufferedReader br = new BufferedReader(fr);
	    String line = null;
	    while ( (line = br.readLine()) != null) {
	    	add(line);
	    	add("<br>");
	    }
	    br.close();
	    fr.close();
	    
	}
	
	private TravelSessionManager getTravelSessionManager(IWContext iwc) {
		try {
			return (TravelSessionManager) IBOLookup.getSessionInstance(iwc, TravelSessionManager.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		} 
	}
	
}
