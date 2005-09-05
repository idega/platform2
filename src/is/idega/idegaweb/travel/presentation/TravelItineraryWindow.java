/*
 * $Id: TravelItineraryWindow.java,v 1.2 2005/09/05 23:05:14 gimmi Exp $
 * Created on Aug 27, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.presentation;

import is.idega.idegaweb.travel.data.GeneralBooking;
import is.idega.idegaweb.travel.data.GeneralBookingHome;
import java.util.Collection;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.AddressType;
import com.idega.core.location.data.AddressTypeHome;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.user.data.Group;


public class TravelItineraryWindow extends TravelWindow {

	public static final String PARAMETER_BOOKING_ID = "tiw_bid";
	
	public TravelItineraryWindow() {
		super(600, 250);
	}
	
	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		String[] bids = iwc.getParameterValues(PARAMETER_BOOKING_ID);
		
		if (bids != null && bids.length > 0) {
			GeneralBookingHome gbHome = (GeneralBookingHome) IDOLookup.getHome(GeneralBooking.class);
			GeneralBooking gBook = gbHome.findByPrimaryKey(new Integer(bids[0]));
			Group supplierManager = gBook.getService().getProduct().getSupplier().getSupplierManager();
			
			Table table = new Table();
			table.setAlignment(Table.HORIZONTAL_ALIGN_CENTER);
			table.setAlignment(1, 1, Table.HORIZONTAL_ALIGN_CENTER);
			table.setAlignment(1, 2, Table.HORIZONTAL_ALIGN_CENTER);
			table.add(getHeader(supplierManager.getName()), 1, 1);
			
			AddressTypeHome atHome = (AddressTypeHome) IDOLookup.getHome(AddressType.class);
			Collection coll = supplierManager.getAddresses(atHome.findAddressType1());
			boolean addDash = false;
			if (coll != null && !coll.isEmpty()) {
				Address a = (Address) coll.iterator().next();
				table.add(getText(a.getStreetAddress()), 1, 2);
				addDash = true;
			}
			
			coll = supplierManager.getPhones();
			if (coll != null && !coll.isEmpty()) {
				Phone p = (Phone) coll.iterator().next();
				if (addDash) {
					table.add(getText(" - "), 1, 2);
				}
				addDash = true;
				table.add(getText(p.getNumber()), 1, 2);
			}
			
			coll = supplierManager.getEmails();
			if (coll != null && !coll.isEmpty()) {
				Email e = (Email) coll.iterator().next();
				if (addDash) {
					table.add(getText(" - "), 1, 2);
				}
				addDash = true;
				table.add(getText(e.getEmailAddress()), 1, 2);
			}
			add(table);
		}
		TravelBasket tb = new TravelBasket();
		tb.setBookingPKs(bids);
		tb.setShowDeleteLink(false);
		tb.setShowCheckoutLink(false);
		tb.setHeaderStyleClass("sbrowser_header");
		tb.setTextStyleClass("sbrowser_text");
		add(tb);
	}
	
	private Text getHeader(String content) {
		Text text = new Text(content);
		text.setStyleClass("sbrowser_header");
		return text;
	}
	
	protected Text getText(String content) {
		Text text = new Text(content);
		text.setStyleClass("sbrowser_text");
		return text;
	}

}
