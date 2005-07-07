/*
 * $Id: SupplierBrowserBookingForm.java,v 1.2 2005/07/07 03:04:44 gimmi Exp $
 * Created on Jul 4, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.presentation;

import is.idega.idegaweb.travel.block.search.business.SearchEventListener;
import is.idega.idegaweb.travel.block.search.business.ServiceSearchBusiness;
import is.idega.idegaweb.travel.block.search.business.ServiceSearchSession;
import is.idega.idegaweb.travel.block.search.presentation.AbstractSearchForm;
import is.idega.idegaweb.travel.business.SupplierBrowserListener;
import is.idega.idegaweb.travel.data.CashierQueue;
import is.idega.idegaweb.travel.data.CashierQueueHome;
import is.idega.idegaweb.travel.data.GeneralBooking;
import is.idega.idegaweb.travel.interfaces.Booking;
import is.idega.idegaweb.travel.service.presentation.BookingForm;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import com.idega.block.basket.business.BasketBusiness;
import com.idega.block.basket.data.BasketEntry;
import com.idega.block.creditcard.business.CreditCardAuthorizationException;
import com.idega.block.trade.stockroom.business.TradeConstants;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWTimestamp;


public class SupplierBrowserBookingForm extends TravelManager {
	
	public static final String PARAMETER_CASHIER_ID = "sbbf_pci";
	public static final String PARAMETER_CLIENT_NAME = "sbbf_cn";
	public static final String PARAMETER_CASHIER_QUEUE_ID = "sbbf_cqi";
	
	private String textStyleClass 	= "sbrowser_text";
	private String headerStyleClass = "sbrowser_header";
	private String ioStyleClass 	= "sbrowser_interface";
	
	public void main (IWContext iwc) throws Exception {
		super.main(iwc);
		add(Text.BREAK);
		if (super.isSupplierManagerBookerStaff() || super.hasRole(iwc, TradeConstants.ROLE_SUPPLIER_MANAGER_CASHIER_STAFF)) {
			addBookingForm(iwc);
		} else if (!super.isLoggedOn(iwc)) {
			add(super.getResourceBundle().getLocalizedString("travel.not_logged_on", "Not logged on"));
		} else {
			add(super.getResourceBundle().getLocalizedString("travel.no_permission", "No permission"));
		}
	}
	
	private void addBookingForm(IWContext iwc) throws Exception {
		Form form = new Form();
		Table btable = new Table();
		btable.setWidth("90%");
		btable.setCellpaddingAndCellspacing(0);
		btable.setColor(super.WHITE);
		form.add(btable);
		form.maintainParameter(PARAMETER_CASHIER_QUEUE_ID);
		
		TravelBasket b = new TravelBasket();
		b.setShowCheckoutLink(false);
		b.setUseTravelLook(true);
		b.setShowDeleteLink(!hasRole(iwc, TradeConstants.ROLE_SUPPLIER_MANAGER_CASHIER_STAFF));
		b.setWidth("100%");
		btable.add(b);
		add(form);
		
		Table table = getTable();
		int row = 1;
		
		boolean cashiersOnly = getSupplierManagerBusiness(iwc).hasRole(getSupplierManager(), TradeConstants.ROLE_SUPPLIER_MANAGER_CASHIER_STAFF);
		boolean isCashier = super.hasRole(iwc, TradeConstants.ROLE_SUPPLIER_MANAGER_CASHIER_STAFF);
		boolean basketIsEmpty = getBasketBusiness(iwc).getBasket().isEmpty();
		
		if (iwc.isParameterSet(AbstractSearchForm.ACTION)) {
			row = addSubmitResults(iwc, form, table, row);
		}
		
		if (iwc.isParameterSet(SupplierBrowserListener.ACTION)) {
			add(Text.BREAK);
			try {
				getServiceSearchSession(iwc).throwException();
				add(getHeaderText("Sent to cashier"));
			} catch (Exception e) {
				add(getHeaderText(getResourceBundle().getLocalizedString(e.getMessage(), e.getMessage())));
			}
		}
		
		if (!basketIsEmpty) {
			
			if ( cashiersOnly  && !isCashier) {
				row = addCashierForm(iwc, form, table, row);
			} else if (!cashiersOnly || (cashiersOnly && isCashier)) {
				row = addBookingForm(iwc, form, table, row);
			}
			
		} 

		if (row > 1) {
			form.add(Text.BREAK);
			form.add(table);
		}
		
	}

	private int addCashierForm(IWContext iwc, Form form, Table table, int row) throws RemoteException {
		table.add(getHeaderText("Send to cashier"), 1, row);
		table.setRowColor(row, backgroundColor);
		table.mergeCells(1, row, 2, row++);
		
		TextInput name = new TextInput(PARAMETER_CLIENT_NAME);
		table.add(getText(getResourceBundle().getLocalizedString("travel.client_name", "Client name")), 1, row);
		table.add(name, 2, row);
		table.setRowColor(row++, GRAY);

		
		table.add(getText(getResourceBundle().getLocalizedString("travel.cashier", "Cashier")), 1, row);
		Collection coll = getSupplierManagerBusiness(iwc).getSupplierManagerCashiers(getSupplierManager());
		DropdownMenu menu = new DropdownMenu(coll, PARAMETER_CASHIER_ID);
		menu.addMenuElementFirst("-1", getResourceBundle().getLocalizedString("travel.any", "Any"));
		table.add(menu, 2, row);
		table.setRowColor(row++, GRAY);

		SubmitButton save = new SubmitButton(getResourceBundle().getLocalizedString("travel.send", "Send"), SupplierBrowserListener.ACTION, SupplierBrowserListener.ACTION_SEND_TO_CASHIER);
		form.setEventListener(SupplierBrowserListener.class);
		
		table.setRowColor(row, GRAY);
		table.mergeCells(1, row, 2, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.add(save, 1,row++);		
		return row;
	}
	
	private int addSubmitResults(IWContext iwc, Form form, Table table, int row) throws RemoteException {
		form.add(Text.BREAK);
		try {
			getServiceSearchBusiness(iwc).getSearchSession(iwc).throwException();
			Collection bookings = getServiceSearchBusiness(iwc).getSearchSession(iwc).getBookingsSavedFromBasket(); 
			table.mergeCells(1, row, 2, row);
			table.setRowColor(row, backgroundColor);
			table.add(getHeaderText(getResourceBundle().getLocalizedString("travel.authorization_successful", "Authorization Successful")), 1, row++);
			
			boolean ccAuthAdded = false;
			int localeID = iwc.getCurrentLocaleId();
			GeneralBooking gBooking;
			Product product;
			Iterator iter = bookings.iterator();
			while (iter.hasNext()) {
				gBooking = (GeneralBooking) iter.next();
				if (!ccAuthAdded && gBooking.getPaymentTypeId() == Booking.PAYMENT_TYPE_ID_CREDIT_CARD) {
					table.mergeCells(1, row, 2, row);
					table.setRowColor(row, GRAY);
					table.add(getText(getResourceBundle().getLocalizedString("travel.credidcard_authorization_number_is","Creditcard authorization number is")), 1, row);
					table.add(getText(gBooking.getCreditcardAuthorizationNumber()), 1, row++);
					
					table.mergeCells(1, row, 2, row);
					table.setRowColor(row, GRAY);
					table.setRowHeight(row, "14");
					++row;

					ccAuthAdded = true;
				}
				product = gBooking.getService().getProduct();
				table.mergeCells(1, row, 2, row);
				table.setRowColor(row, GRAY);
				table.add(getText(getResourceBundle().getLocalizedString("travel.service","Service")), 1, row);
				table.add(getText(" : "), 1, row);
				table.add(getText(product.getProductName(localeID)+" - "+product.getSupplier().getName()), 1, row);
				table.add(Text.BREAK, 1, row);
				
				table.mergeCells(1, row, 2, row);
				table.setRowColor(row, GRAY);
				table.add(getText(getResourceBundle().getLocalizedString("travel.reference_number_is","Reference number")), 1, row);
				table.add(getText(" : "), 1, row);
				table.add(getText(gBooking.getReferenceNumber()), 1, row);
				table.add(Text.BREAK, 1, row);
//					table.mergeCells(1, row, 2, row);
//					table.setRowColor(row, GRAY);
//					table.add(getText(getResourceBundle().getLocalizedString("travel.if_unable_to_print","If you are unable to print the voucher, write the reference number down else proceed to printing the voucher.")), 1, row++);
				
				Link printVoucher = new Link(getText(getResourceBundle().getLocalizedString("travel.print_voucher","Print voucher")));
				printVoucher.addParameter(VoucherWindow.parameterBookingId, gBooking.getID());
				printVoucher.setWindowToOpen(VoucherWindow.class);
				
				table.mergeCells(1, row, 2, row);
				table.setRowColor(row, GRAY);
				table.add(printVoucher, 1, row);
				++row;
				
				table.mergeCells(1, row, 2, row);
				table.setRowColor(row, GRAY);
				table.setRowHeight(row, "14");

				++row;
			}
			
			// QueueStuff
			if (iwc.isParameterSet(PARAMETER_CASHIER_QUEUE_ID)) {
				CashierQueueHome qHome = (CashierQueueHome) IDOLookup.getHome(CashierQueue.class);
				CashierQueue queue = qHome.findByPrimaryKey(new Integer(iwc.getParameter(PARAMETER_CASHIER_QUEUE_ID)));
				queue.remove();
			}
			
		} catch (CreditCardAuthorizationException e)  {
			table.mergeCells(1, row, 2, row);
			table.setRowColor(row, backgroundColor);
			table.add(getHeaderText(getResourceBundle().getLocalizedString("travel.authorization_failed", "Authorization Failed")), 1, row++);
			table.mergeCells(1, row, 2, row);
			table.setRowColor(row, GRAY);
			table.add(getText(e.getLocalizedMessage(getResourceBundle())), 1, row++);
		} catch (Exception e)  {
			table.mergeCells(1, row, 2, row);
			table.setRowColor(row, backgroundColor);
			table.add(getHeaderText(getResourceBundle().getLocalizedString("travel.authorization_failed", "Authorization failed")), 1, row++);
			table.mergeCells(1, row, 2, row);
			table.setRowColor(row, GRAY);
			table.add(getText(e.getMessage()), 1, row++);
		}
		return row;
	}

	private int addBookingForm(IWContext iwc, Form form, Table table, int row) throws RemoteException, Exception {
		TextInput ccName = getStyleTextInput(new TextInput(BookingForm.PARAMETER_NAME_ON_CARD));
		TextInput ccNumber = getStyleTextInput(new TextInput(BookingForm.parameterCCNumber));
		TextInput ccYear = getStyleTextInput(new TextInput(BookingForm.parameterCCYear));
		TextInput ccMonth = getStyleTextInput(new TextInput(BookingForm.parameterCCMonth));
		TextInput ccCVC = getStyleTextInput(new TextInput(BookingForm.parameterCCCVC));
		HiddenInput ccRef = new HiddenInput(AbstractSearchForm.PARAMETER_REFERENCE_NUMBER, IWTimestamp.RightNow().toSQLString(false));
		ccYear.setSize(3);
		ccMonth.setSize(3);
		ccCVC.setSize(4);
		
		TextInput name = getStyleTextInput(new TextInput(BookingForm.PARAMETER_FIRST_NAME));
		TextInput email = getStyleTextInput(new TextInput(BookingForm.PARAMETER_EMAIL));
		TextInput phone = getStyleTextInput(new TextInput(BookingForm.PARAMETER_PHONE));
		TextArea comment = new TextArea(BookingForm.PARAMETER_COMMENT);
		if (ioStyleClass != null) {
			comment.setStyleClass(ioStyleClass);
		}
		comment.setWidth("150");
		comment.setHeight("60");
		
		DropdownMenu payType = getBooker(iwc).getPaymentTypeDropdown(getResourceBundle(), BookingForm.PARAMETER_PAYMENT_TYPE);
		payType.setToEnableWhenSelected(ccName, Integer.toString(Booking.PAYMENT_TYPE_ID_CREDIT_CARD));
		payType.setToEnableWhenSelected(ccNumber, Integer.toString(Booking.PAYMENT_TYPE_ID_CREDIT_CARD));
		payType.setToEnableWhenSelected(ccYear, Integer.toString(Booking.PAYMENT_TYPE_ID_CREDIT_CARD));
		payType.setToEnableWhenSelected(ccMonth, Integer.toString(Booking.PAYMENT_TYPE_ID_CREDIT_CARD));
		payType.setToEnableWhenSelected(ccCVC, Integer.toString(Booking.PAYMENT_TYPE_ID_CREDIT_CARD));
		
		name.keepStatusOnAction();
		email.keepStatusOnAction();
		phone.keepStatusOnAction();
		comment.keepStatusOnAction();
		payType.keepStatusOnAction();
		
		ccName.keepStatusOnAction();
		ccNumber.keepStatusOnAction();
		ccYear.keepStatusOnAction();
		ccMonth.keepStatusOnAction();
		ccCVC.keepStatusOnAction();
		ccRef.keepStatusOnAction();
		
		
		
//		if (iwc.isParameterSet(BookingForm.PARAMETER_PAYMENT_TYPE) && Integer.parseInt(iwc.getParameter(BookingForm.PARAMETER_PAYMENT_TYPE)) != Booking.PAYMENT_TYPE_ID_CREDIT_CARD) {
//		}
			payType.setSelectedElement(Booking.PAYMENT_TYPE_ID_ACCOUNT);
			ccName.setDisabled(true);
			ccMonth.setDisabled(true);
			ccYear.setDisabled(true);
			ccCVC.setDisabled(true);
			ccNumber.setDisabled(true);
		
		table.mergeCells(1, row, 2, row);
		table.setRowColor(row, backgroundColor);
		table.add(getHeaderText(getResourceBundle().getLocalizedString("travel.client_information", "Client Information")), 1, row++);
		table.add(getText(getResourceBundle().getLocalizedString("travel.name", "Name")), 1, row);
		table.add(name, 2, row);
		table.setRowColor(row++, GRAY);
		table.setRowColor(row, GRAY);
		table.add(getText(getResourceBundle().getLocalizedString("travel.email", "Email")), 1, row);
		table.add(email, 2, row++);
		table.setRowColor(row, GRAY);
		table.add(getText(getResourceBundle().getLocalizedString("travel.phone", "Phone")), 1, row);
		table.add(phone, 2, row++);
		table.setRowColor(row, GRAY);
		table.add(getText(getResourceBundle().getLocalizedString("travel.comment", "Comment")), 1, row);
		table.setVerticalAlignment(1, row, Table.VERTICAL_ALIGN_TOP);
		table.add(comment, 2, row++);
		
		
		
		table.mergeCells(1, row, 2, row);
		table.setRowColor(row, backgroundColor);
		table.add(getHeaderText(getResourceBundle().getLocalizedString("travel.payment", "Payment")), 1, row++);
		table.setRowColor(row, GRAY);
		table.add(getText(getResourceBundle().getLocalizedString("travel.payment_type", "Payment type")), 1, row);
		table.add(payType, 2, row++);
		table.setRowColor(row, GRAY);
		table.add(ccRef, 2, row);
		table.add(getText(getResourceBundle().getLocalizedString("travel.name_on_card", "Name as it appears on the card")), 1, row);
		table.add(ccName, 2, row++);
		table.setRowColor(row, GRAY);
		table.add(getText(getResourceBundle().getLocalizedString("travel.creditcard_number", "Creditcard Number")), 1, row);
		table.add(ccNumber, 2, row++);
		table.setRowColor(row, GRAY);
		table.add(getText(getResourceBundle().getLocalizedString("travel.expire_date", "Creditcard expire date (MM/YY)")), 1, row);
		table.add(ccMonth, 2, row);
		table.add(getText(" / "), 2, row);
		table.add(ccYear, 2, row++);
		table.setRowColor(row, GRAY);
		table.add(getText(getResourceBundle().getLocalizedString("travel.creditcard_cvc", "Creditcard CVC code")), 1, row);
		table.add(ccCVC, 2, row++);
		table.setRowColor(row, GRAY);
		table.add(getText(getResourceBundle().getLocalizedString("travel.total_amount", "Total amount")), 1, row);
		table.add(getText(getTotalPriceString(iwc)), 2, row++);
		
		ccCVC.setAsNotEmpty(getResourceBundle().getLocalizedString("travel.creditcard_cvc_number_must_be_provided", "Creditcard CVC number must be provided"));
		ccMonth.setAsNotEmpty(getResourceBundle().getLocalizedString("travel.month_of_expire_must_be_provided", "Month of expire must be provided"));
		ccYear.setAsNotEmpty(getResourceBundle().getLocalizedString("travel.year_of_expire_must_be_provided", "Year of expire must be provided"));
		ccNumber.setAsNotEmpty(getResourceBundle().getLocalizedString("travel.creditcard_number_must_be_provided", "Creditcard number must be provided"));
		ccName.setAsNotEmpty(getResourceBundle().getLocalizedString("travel.name_on_creditcard_must_be_provided", "Name on card must be provided"));
		name.setAsNotEmpty(getResourceBundle().getLocalizedString("travel.client_name_must_be_provided", "Client name must be provided"));
		
		
		SubmitButton save = new SubmitButton(getResourceBundle().getLocalizedString("travel.book", "Book"), AbstractSearchForm.ACTION, AbstractSearchForm.ACTION_CONFIRM);
		form.setEventListener(SearchEventListener.class);
		
		table.setRowColor(row, GRAY);
		table.mergeCells(1, row, 2, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.add(save, 1,row++);
		return row;
	}
	
	private String getTotalPriceString(IWContext iwc) throws Exception {
		float price, totalPrice = 0;
		String currency = null;
		
		Collection values = getBasketBusiness(iwc).getBasket().values();
		GeneralBooking booking;
		Iterator iter = values.iterator();
		while (iter.hasNext()) {
			booking = (GeneralBooking)  ((BasketEntry) iter.next()).getItem();
			price = getBooker(iwc).getBookingPrice(getBooker(iwc).getMultibleBookings(booking));
			totalPrice += price;
			String curr = getBooker(iwc).getBookingCurrency(booking).getCurrencyAbbreviation();
			if (curr != null && (currency == null)) {
				currency = curr;
			} else if (curr != null && !currency.equals(curr)) {
				throw new Exception("Too many currencies");
			}
		}
		
		if (currency != null) {
			return super.decimalFormat.format(totalPrice) + " " +currency;
		}
		return null;
	}
	
	public TextInput getStyleTextInput(TextInput io) {
		return io;
	}
	
	protected ServiceSearchBusiness getServiceSearchBusiness(IWApplicationContext iwac) {
		try {
			return (ServiceSearchBusiness) IBOLookup.getServiceInstance(iwac, ServiceSearchBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
	private ServiceSearchSession getServiceSearchSession(IWContext iwc) {
		try {
			return (ServiceSearchSession) IBOLookup.getServiceInstance(iwc, ServiceSearchSession.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
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
