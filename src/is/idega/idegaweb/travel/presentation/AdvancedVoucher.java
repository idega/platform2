/*
 * $Id: AdvancedVoucher.java,v 1.1 2005/07/07 18:57:43 gimmi Exp $
 * Created on Jul 7, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.presentation;

import is.idega.idegaweb.travel.business.Booker;
import is.idega.idegaweb.travel.data.BookingEntry;
import is.idega.idegaweb.travel.data.GeneralBooking;
import is.idega.idegaweb.travel.data.GeneralBookingHome;
import is.idega.idegaweb.travel.data.PickupPlace;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import javax.ejb.FinderException;
import com.idega.block.trade.stockroom.business.ProductBusiness;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.ProductPrice;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.block.trade.stockroom.data.TravelAddress;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.contact.data.Phone;
import com.idega.core.contact.data.PhoneTypeBMPBean;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.data.IDOLookup;
import com.idega.data.IDORelationshipException;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.Window;
import com.idega.util.IWTimestamp;


public class AdvancedVoucher extends Window {

	public static final String PARAMETER_BOOKING_ID = "av_bi";
	
	public void main(IWContext iwc) {
		this.setTitle("Printing...");

		String[] bookingIds = iwc.getParameterValues(PARAMETER_BOOKING_ID);
		if (bookingIds == null || bookingIds.length == 0) {
			add("Nothing to print...");
		} else {
			try {
				Locale locale = iwc.getLocale();
				Booker booker = getBooker(iwc);
				ProductBusiness pBus = getProductBusiness(iwc);
				GeneralBookingHome gbHome = (GeneralBookingHome) IDOLookup.getHome(GeneralBooking.class);
				add("<font color=\"BLACK\"");
//				add("<font color=\"WHITE\"");
				addHeader();
				for (int i = 0; i < bookingIds.length; i++) {
					addBookingInfo(pBus, booker, gbHome.findByPrimaryKey(new Integer(bookingIds[i])), locale);
				}
				add("</font>");
				
				
//				this.setOnLoad("window.parent.focus();window.resizeTo(1,1);window.print();window.close();");
			} catch (Exception e) {
				add("Printing failed...");
			}
		} 
	
	}

	private void addBookingInfo(ProductBusiness productBus, Booker booker, GeneralBooking booking, Locale locale) throws RemoteException, FinderException {
		IWTimestamp stamp = new IWTimestamp(booking.getBookingDate());
		Product product = booking.getService().getProduct();
		Supplier supplier = product.getSupplier();
		
		add("<br>");
		add("LAYOUT RUN \"NETV1\"");
		add("<br>");
		add("$");
		add("@");
		
		// #1 Client  name
		add(booking.getName());
		
		add("@");
		
		// #2 Client contact info
		if (booking.getTelephoneNumber() != null) {
			add("Tel: "+booking.getTelephoneNumber());
		}
		
		add("@");
		
		// #3 Supplier Name
		if (supplier != null) {
			add(supplier.getName());
		}
		
		add("@");
		
		// #4 Supplier Address
		if (supplier != null) {
			try {
				boolean addDash = false;
				Address a = supplier.getAddress();
				if (a != null) {
					if (a.getStreetAddress() != null) {
						add(a.getStreetAddress());
						addDash = true;
					}
					PostalCode p = a.getPostalCode();
					if (p != null) {
						if (addDash) {
							add(" - ");
						}
						add(p.getPostalAddress());
						addDash = true;
					}
					
				}
				Collection c = supplier.getPhones(PhoneTypeBMPBean.HOME_PHONE_ID);
				if (c != null && !c.isEmpty()) {
					Iterator iter = c.iterator();
					Phone phone;
					while (iter.hasNext()) {
						phone = (Phone) iter.next();
						if (phone.getNumber() != null && !phone.getNumber().trim().equals(""))
						if (addDash) {
							add(" - ");
							addDash = true;
						}
						add(phone.getNumber());
					}
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		add("@");
		
		// #5 Product Name
		if (product != null) {
			add(productBus.getProductNameWithNumber(booking.getService().getProduct(), true));
		}
		
		add("@");
		
		// #6 booking date
		add(stamp.getLocaleDate(locale, IWTimestamp.SHORT));
		
		add("@");
		
		// #7 Departure place
		try {
			Collection c = booking.getTravelAddresses();
			if (c != null && !c.isEmpty()) {
				Iterator iter = c.iterator();
				TravelAddress tb = (TravelAddress) iter.next();
				add(tb.getName());
			}
		}
		catch (IDORelationshipException e) {
			e.printStackTrace();
		}
		
		add("@");

		// #8 Pickup
		PickupPlace pp = booking.getPickupPlace();
		if (pp != null) {
			add(pp.getAddress().getStreetAddress());
		}
		
		add("@");
		
		// #9 Seats w/price
		BookingEntry[] entries = booking.getBookingEntries();
		if (entries != null && entries.length > 0) {
			ProductPrice pprice = entries[0].getProductPrice();
			add(entries[0].getCount() + " "+pprice.getPriceCategory().getName() + ": ");
			add(TravelManager.decimalFormat.format(booker.getBookingEntryPrice(entries[0], booking)));
			add(" "+booker.getBookingCurrency(booking));
		}
		
		add("@");
		
		// #10 Seats 2 w/price ?
		if (entries != null && entries.length > 1) {
			for (int i = 1; i < entries.length; i++) {
				ProductPrice pprice = entries[i].getProductPrice();
				add(entries[i].getCount() + " "+pprice.getPriceCategory().getName() + ": ");
				add(TravelManager.decimalFormat.format(booker.getBookingEntryPrice(entries[i], booking)));
				add(" "+booker.getBookingCurrency(booking));
			}
		}
		
		add("@");
		
		// #11 Total price
		add(TravelManager.decimalFormat.format(booker.getBookingPrice(booker.getMultibleBookings(booking))));
		add(" "+booker.getBookingCurrency(booking));
		
		add("@");
		
		// #12 Provider Comments
		
		add("@");
		
		// #13 Customer comments
		
		add(booking.getComment());
		
		
		add("@");
		
		// #14 Voucher nr.
		
		add(booking.getReferenceNumber());
		
		add("@");
		
		// #15 undecided
		
		add("@");
		add("<br>");
		add("PF1");

		
//		add("<br>");
//		add("LAYOUT RUN \"FAR1\"");
//		add("<br>");
//		add("$");
//		add("1KEF");
//		add("@");
//		add(TravelManager.decimalFormat.format(booker.getBookingPrice(booker.getMultibleBookings(booking))));
//		add(" "+booker.getBookingCurrency(booking));
//		add("@");
//		add(booking.getReferenceNumber());
//		add("@");
//		add(Integer.toString(booking.getTotalCount()));
//		add("@");
//		add(productBus.getProductNameWithNumber(booking.getService().getProduct(), true));
//		add("@");
//		add(stamp.getLocaleDate(locale, IWTimestamp.SHORT));
//		add("@");
//		
//		add(TextSoap.addZero(stamp.getHour())+":"+TextSoap.addZero(stamp.getMinute()));
//		add("@");
//		add("HHH");
//		add("#");
//		add("<br>");
//		add("PF1");
		
	}
	
	private void addHeader() {
		add("PRINT KEY OFF");
		add("<br>");
		add("FORMAT INPUT \"$\",\"#\",\"@\"");
		add("<br>");
		add("INPUT OK");
	}
	
	private ProductBusiness getProductBusiness(IWContext iwc) {
		try {
			return (ProductBusiness) IBOLookup.getServiceInstance(iwc, ProductBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
	
	private Booker getBooker(IWContext iwc) {
		try {
			return (Booker) IBOLookup.getServiceInstance(iwc, Booker.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
	
}
