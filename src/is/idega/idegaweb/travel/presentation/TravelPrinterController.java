/*
 * $Id: TravelPrinterController.java,v 1.1 2005/07/07 18:57:43 gimmi Exp $
 * Created on Jul 7, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.presentation;

import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;


public class TravelPrinterController extends TravelManager {
	
	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		IWResourceBundle iwrb = getResourceBundle();
		Table table = getTable();
		int row = 1;
		
		Link reset = new Link(getText(iwrb.getLocalizedString("travel.reset_printer_settings", "Reset printer setting")));
		reset.setWindowToOpen(PrinterControllerWindow.class);
		reset.addParameter(PrinterControllerWindow.ACTION, PrinterControllerWindow.ACTION_RESET);
		
		Link reboot = new Link(getText(iwrb.getLocalizedString("travel.reboot_the_printer", "Reboot the printer")));
		reboot.setWindowToOpen(PrinterControllerWindow.class);
		reboot.addParameter(PrinterControllerWindow.ACTION, PrinterControllerWindow.ACTION_REBOOT);

		Link testfeed = new Link(getText(iwrb.getLocalizedString("travel.test_feed", "Test feed")));
		testfeed.setWindowToOpen(PrinterControllerWindow.class);
		testfeed.addParameter(PrinterControllerWindow.ACTION, PrinterControllerWindow.ACTION_TEST_FEED);
		
		table.add(getHeaderText(iwrb.getLocalizedString("travel.printer_administration", "Printer Administration")), 1, row);
		table.setRowColor(row++, backgroundColor);
		
		table.add(testfeed, 1, row);
		table.setRowColor(row++, GRAY);
		table.add(reboot, 1, row);
		table.setRowColor(row++, GRAY);
		table.add(reset, 1, row);
		table.setRowColor(row++, GRAY);
		
		add(Text.BREAK);
		add(table);
	}
	
}
