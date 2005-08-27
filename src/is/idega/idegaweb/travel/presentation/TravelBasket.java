/*
 * $Id: TravelBasket.java,v 1.3 2005/08/27 15:37:15 gimmi Exp $
 * Created on 22.6.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.presentation;

import is.idega.idegaweb.travel.block.search.business.SearchEventListener;
import is.idega.idegaweb.travel.block.search.business.ServiceSearchBusinessBean;
import is.idega.idegaweb.travel.block.search.presentation.AbstractSearchForm;
import is.idega.idegaweb.travel.business.BookingComparator;
import is.idega.idegaweb.travel.data.GeneralBooking;
import is.idega.idegaweb.travel.data.GeneralBookingHome;
import is.idega.idegaweb.travel.service.presentation.BookingForm;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import javax.ejb.FinderException;
import com.idega.block.basket.business.BasketBusiness;
import com.idega.block.basket.data.BasketEntry;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.builder.data.ICPage;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.util.IWTimestamp;


public class TravelBasket extends TravelBlock {
	
	private BasketBusiness basketBusiness = null;
	private Collection bookings = null;
	
	private String textStyleClass = null;
	private String headerStyleClass = null;
	private String linkStyleClass = null;
	
	private String bookingURL = null;
	private ICPage bookingPage = null;
	private Class bookingClass = null;
	
	private boolean showCheckoutLink = true;
	private boolean showDeleteLink = true;
	private boolean useTravelLook = false;
	
	private Vector parName = null;
	private Vector parValue = null;
	
	private boolean checkoutIsOnAnotherServer = false;
	private String encryptedListenerName = null;
	
	public void main(IWContext iwc) throws Exception {
		
		super.main(iwc);

		init(iwc);
		displayBasketContent(iwc);
	}

	private void init(IWContext iwc) {
		checkoutIsOnAnotherServer = true;
		basketBusiness = getBasketBusiness(iwc);
	}
	
	
	private void displayBasketContent(IWContext iwc) throws Exception {
		Table table = new Table();
		if (useTravelLook) {
			table = TravelManager.getTable();
		}
		table.setWidth("100%");
		table.setBorder(0);
		int row = 1;
		Iterator iter = getBookings(iwc).iterator();
		GeneralBooking booking;
		Product prod;
		Supplier supp;
		BookingForm bf;
		float totalPrice = 0;
		float price;
		int localeID = iwc.getCurrentLocaleId();
		Locale locale = iwc.getCurrentLocale();
		Link remove = null;
		String currency = null;
		
		table.add(getHeader(getResourceBundle().getLocalizedString("travel.service", "Service")), 1, row);
		table.add(getHeader(getResourceBundle().getLocalizedString("travel.date_s", "Date(s)")), 2, row);
		table.add(getHeader(getResourceBundle().getLocalizedString("travel.prices", "Prices")), 3, row);
		if (showDeleteLink) {
			table.add(getHeader(""), 4, row);
		}
		if (useTravelLook) {
			table.setRowColor(row, TravelManager.backgroundColor);
		}
		++row;
		while (iter.hasNext()) {
			booking = (GeneralBooking) iter.next();
			prod = booking.getService().getProduct();
			supp = prod.getSupplier();
			bf = getServiceHandler(iwc).getBookingForm(iwc, prod, false);
			table.add(getText(prod.getProductName(localeID)+" - "+supp.getName()), 1, row);
			addBookingDates(iwc, bf, booking, locale, table, 2, row);
			price = getBooker(iwc).getBookingPrice(getBooker(iwc).getMultibleBookings(booking));
			totalPrice += price;
			String curr = getBooker(iwc).getBookingCurrency(booking).getCurrencyAbbreviation();
			if (curr != null && (currency == null)) {
				currency = curr;
			} else if (curr != null && !currency.equals(curr)) {
				throw new Exception("Too many currencies");
			}
			table.add(getText(decimalFormat.format(price)+" "+currency), 3, row);
			
			if (showDeleteLink) {
				remove = getLink(getResourceBundle().getLocalizedString("travel.basket.remove", "Remove"));
				remove.addParameter(ServiceSearchBusinessBean.PARAMETER_BOOKING_ID_REMOVAL, booking.getPrimaryKey().toString());
				remove.addParameter(AbstractSearchForm.ACTION, ServiceSearchBusinessBean.PARAMETER_BOOKING_ID_REMOVAL);
				remove.setEventListener(SearchEventListener.class);
				table.add(remove, 4, row);
				table.setVerticalAlignment(4, row, Table.VERTICAL_ALIGN_TOP);
				table.setAlignment(4, row, Table.HORIZONTAL_ALIGN_RIGHT);
			}
			
			table.setVerticalAlignment(1, row, Table.VERTICAL_ALIGN_TOP);
			table.setVerticalAlignment(2, row, Table.VERTICAL_ALIGN_TOP);
			table.setVerticalAlignment(3, row, Table.VERTICAL_ALIGN_TOP);
			
			if (useTravelLook) {
				table.setRowColor(row, TravelManager.GRAY);
			}
			++row;
		}
		if (currency != null) {
			if (useTravelLook) {
				table.setRowColor(row, TravelManager.GRAY);
			}
			table.add(getBoldText(getResourceBundle().getLocalizedString("travel.total", "Total")), 1, row);
			table.add(getBoldText(decimalFormat.format(totalPrice)+" "+currency), 3, row++);

			if (showCheckoutLink) {
				Link book = getLink(super.getResourceBundle().getLocalizedString("travel.check_out", "Check out"));
				if (bookingURL != null) {
					book.setURL(bookingURL);
				}
				if (bookingPage != null) {
					book.setPage(bookingPage);
				}
				if (bookingClass != null) {
					book.setClassToInstanciate(bookingClass);
				}
	 
				book.addParameter(AbstractSearchForm.ACTION, AbstractSearchForm.ACTION_BOOKING_FORM);
				book.addParameter(AbstractSearchForm.PARAMETER_REFERENCE_NUMBER, IWTimestamp.RightNow().toString());
					
				if (checkoutIsOnAnotherServer) {
					if (encryptedListenerName == null) {
						book.setEventListener(SearchEventListener.class);
					} else {
						book.setEventListener(encryptedListenerName);
					}
					Collection values = basketBusiness.getBasket().values();
					if (values != null) {
						Iterator viter = values.iterator();
						while (viter.hasNext()) {
							BasketEntry e = (BasketEntry) viter.next();
							book.addParameter(ServiceSearchBusinessBean.PARAMETER_BOOKING_IDS_FOR_BASKET, e.getItem().getItemID().toString());
						}
					}
				}
				if (useTravelLook) {
					table.setRowColor(row, TravelManager.GRAY);
				}

				table.add(book, 3, row);
				if (showDeleteLink) {
					table.mergeCells(3, row, 4, row);
				}
				table.setAlignment(3, row, Table.HORIZONTAL_ALIGN_RIGHT);
			}

			++row;
		
		} else {
			table.add(getText(getResourceBundle().getLocalizedString("travel.basket_is_empty", "Basket is empty")), 1, row);
			if (useTravelLook) {
				table.setRowColor(row, TravelManager.GRAY);
			}
		}
		
		table.setWidth(2, "90");
		table.setWidth(3, "100");
		if (showDeleteLink) {
			table.setWidth(4, "50");
		}
		
		add(table);
	}

	protected void addBookingDates(IWContext iwc, BookingForm bf, GeneralBooking booking, Locale locale, Table table, int column, int row) throws RemoteException {
		try {
			List list = getBooker(iwc).getMultibleBookings(booking);
			table.add(getText(bf.getBookingDateString(list, locale)), column, row);
		}
		catch (FinderException e) {
			e.printStackTrace();
			table.add(getText(new IWTimestamp(booking.getBookingDate()).getLocaleDate(locale)),column,row);
			
		}
	}
	
	private Collection getBookings(IWContext iwc) throws RemoteException {
		if (bookings == null) {
			Collection entries = basketBusiness.getBasket().values();
			bookings = new Vector();
			BasketEntry entry;
			GeneralBooking booking;
			Iterator iter = entries.iterator();
			while (iter.hasNext()) {
				entry = (BasketEntry) iter.next();
				booking = (GeneralBooking) entry.getItem();
				bookings.add(booking);
			}
			Collections.sort((List) bookings, new BookingComparator(iwc, BookingComparator.DATE));
		}
		return bookings;
	}
	
	protected Text getText(String content) {
		if (useTravelLook) {
			return TravelManager.getText(content);
		} else {
			return  getText(content, textStyleClass);
		}
	}
	
	private Text getHeader(String content) {
		if (useTravelLook) {
			return TravelManager.getHeaderText(content);
		} else {
			return getText(content, headerStyleClass);
		}
	}
	
	private Text getBoldText(String content) {
		if (useTravelLook) {
			Text text = getText(content);
			text.setBold(true);
			return text;
			
		} else {
			return getHeader(content);
		}
	}
	
	private Link getLink(String content) {
		Link l = new Link(getText(content, linkStyleClass));
		if (parName != null) {
			for (int i = 0; i < parName.size(); i++) {
				l.addParameter(parName.get(i).toString(), parValue.get(i).toString());
			}
		}
		return l;
	}
	
	private Text getText(String content, String styleClass) {
		Text text = new Text(content);
		if (styleClass != null) {
			text.setStyleClass(styleClass);
		}
		return text;
	}
	
	
	public void setBookingURL(String url) {
		this.bookingURL = url;
	}
	
	public void setBookingPage(ICPage page) {
		this.bookingPage = page;
	}
	
	public void setTextStyleClass(String styleClass) {
		this.textStyleClass = styleClass;
	}
	
	public void setHeaderStyleClass(String styleClass) {
		this.headerStyleClass = styleClass;
	}
	
	public void setLinkStyleClass(String styleClass) {
		this.linkStyleClass = styleClass;
	}
	
	public void setBookingClass(Class bookingClass) {
		this.bookingClass = bookingClass;
	}
	
	/**
	 * Set whether or not the checkout/booking form is on another server
	 */
	public void setCheckoutIsOnAnotherServer(boolean is) {
		this.checkoutIsOnAnotherServer = is;
	}
	
	public void addParameter(String name, String value) {
		if (name != null && !name.equals("") && value != null) {
			if (parName == null) {
				parName = new Vector();
				parValue = new Vector();
			}
			parName.add(name);
			parValue.add(value);
		}
	}
	
	public void setShowCheckoutLink(boolean show) {
		this.showCheckoutLink = show;
	}
	
	public void setShowDeleteLink(boolean show) {
		this.showDeleteLink = show;
	}
	
	public void setUseTravelLook(boolean use) {
		this.useTravelLook = use;
	}
	
	public void setBookingPKs(String[] bookingPKs) {
		try {
			GeneralBookingHome gbHome = (GeneralBookingHome) IDOLookup.getHome(GeneralBooking.class);
			bookings = new Vector();
			if (bookingPKs != null) {
				for (int i = 0; i < bookingPKs.length; i++) {
					bookings.add(gbHome.findByPrimaryKey(new Integer(bookingPKs[i])));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Set the encrypted name of ActionListener for the other server  
	 * @param name
	 */
	public void setEncryptedListenerName(String name) {
		this.encryptedListenerName = name;
	}
	
	protected BasketBusiness getBasketBusiness(IWUserContext iwuc) {
		try {
			return (BasketBusiness) IBOLookup.getSessionInstance(iwuc, BasketBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
	
}
