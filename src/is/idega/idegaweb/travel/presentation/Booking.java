package is.idega.idegaweb.travel.presentation;

import is.idega.idegaweb.travel.business.InquirerBean;
import is.idega.idegaweb.travel.business.ServiceNotFoundException;
import is.idega.idegaweb.travel.business.TimeframeNotFoundException;
import is.idega.idegaweb.travel.business.TravelStockroomBusiness;
import is.idega.idegaweb.travel.data.Contract;
import is.idega.idegaweb.travel.data.GeneralBooking;
import is.idega.idegaweb.travel.data.Inquery;
import is.idega.idegaweb.travel.data.InqueryHome;
import is.idega.idegaweb.travel.data.Service;
import is.idega.idegaweb.travel.service.presentation.BookingForm;
import is.idega.idegaweb.travel.service.presentation.ServiceOverview;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.block.creditcard.business.CreditCardClient;
import com.idega.block.creditcard.data.CreditCardAuthorizationEntry;
import com.idega.block.creditcard.presentation.Receipt;
import com.idega.block.creditcard.presentation.ReceiptWindow;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.Reseller;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.block.trade.stockroom.data.SupplierHome;
import com.idega.block.trade.stockroom.data.Timeframe;
import com.idega.block.trade.stockroom.data.TravelAddress;
import com.idega.block.trade.stockroom.data.TravelAddressHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDORelationshipException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.CalendarParameters;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.IWTimestamp;

/**
 * Title: idegaWeb TravelBooking Description: Copyright: Copyright (c) 2001
 * Company: idega
 * 
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson </a>
 * @version 1.0
 */

public class Booking extends TravelManager {

	private IWBundle bundle;
	private IWResourceBundle iwrb;

	private Supplier supplier;
	private int supplierId;
	private Reseller reseller;
	private int resellerId;
	private Contract contract;
	private int contractId;
	private GeneralBooking _booking;
	private int bookingId;
	private TravelAddress travelAddress;
	Collection travelAddressIds = null;

	private Product product;
	private int productId;
	private TravelStockroomBusiness tsb;

	public static String BookingAction = "booking_action";
	public static String BookingParameter = "booking";
	public static String parameterBookingId = "bookingBookingId";

	private Service service;
	private Timeframe timeframe;
	//  private Tour tour;

	public static String parameterProductId = com.idega.block.trade.stockroom.data.ProductBMPBean.getProductEntityName();
	private static String parameterInqueryId = "bookInqueryId";
	private static String parameterRespondInquery = "bookingRespondInquery";
	private static String parameterRespondYes = "bookingYes";
	private static String parameterRespondNo = "bookingNo";
	private static String parameterRespondDel = "inquiryDel";
	private static String parameterRespondCCYes = "bookingCCYes";
	private static String parameterRespondCCNo = "bookingCCNo";
	private static String parameterRespondCCDel = "bookingCCDel";

	public static String parameterUpdateBooking = "bookinUpdateBooking";

	public static final int availableIfNoLimit = -1234;
	public static int available = availableIfNoLimit;

	private IWTimestamp stamp;
	private int iMax = 0;
	private int iMin = 0;
	private int iBookings = 0;

	public Booking() {
	}

	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		initialize(iwc);

		if (super.isLoggedOn(iwc)) {

			if (reseller != null && contract == null) {
				product = null;
			}

			String action = iwc.getParameter(BookingForm.sAction);
			/** Backwards comp... */
			String baction = iwc.getParameter(BookingForm.BookingAction);
			if (baction != null && baction.equals(this.parameterUpdateBooking)) {
				action = baction;
			}

			if (action == null) {
				action = "";
			}

			if (action.equals("")) {
				displayForm(iwc);
			}
			else if (action.equals(parameterRespondInquery)) {
				inqueryResponse(iwc);
			}
			else if (action.equals(this.parameterUpdateBooking)) {
				//System.out.println("[Booking] updateBooking :D");
				this.updateBooking(iwc);
			}
			else {
				handleInsert(iwc, true);
			}

			super.addBreak();
		}
		else {
			super.addBreak();
			add(super.getLogin(iwc));
		}
	}

	private void initialize(IWContext iwc) throws RemoteException {
		bundle = super.getBundle();
		iwrb = super.getResourceBundle();
		tsb = getTravelStockroomBusiness(iwc);
		supplier = super.getSupplier();
		reseller = super.getReseller();
		if (supplier != null) supplierId = supplier.getID();
		if (reseller != null) resellerId = reseller.getID();

		String sProductId = iwc.getParameter(parameterProductId);
		try {
			if (sProductId == null) {
				sProductId = (String) iwc.getSessionAttribute("TB_BOOKING_PRODUCT_ID");
			}
			else {
				iwc.setSessionAttribute("TB_BOOKING_PRODUCT_ID", sProductId);
			}
			if (sProductId != null && !sProductId.equals("-1") && Integer.parseInt(sProductId) > 0) {
				productId = Integer.parseInt(sProductId);
				product = getProductBusiness(iwc).getProduct(productId);
				service = tsb.getService(product);
				timeframe = tsb.getTimeframe(product);

				String travelAddressId = iwc.getParameter(BookingForm.parameterDepartureAddressId);
				if (travelAddressId == null || travelAddressId.equals("-1")) {
					List addresses = product.getDepartureAddresses(true);
					if (addresses != null && addresses.size() > 0) {
						travelAddress = (TravelAddress) addresses.get(0);
					}
				}
				else {
					travelAddress = ((TravelAddressHome) (IDOLookup.getHomeLegacy(TravelAddress.class))).findByPrimaryKey(Integer.parseInt(travelAddressId));
				}

				if (travelAddress != null) {
					travelAddressIds = super.getTravelStockroomBusiness(iwc).getTravelAddressIdsFromRefill(product, travelAddress);
				}
			}

		}
		catch (ServiceNotFoundException snfe) {
			snfe.printStackTrace(System.err);
		}
		catch (TimeframeNotFoundException tfnfe) {
			tfnfe.printStackTrace(System.err);
		}
		catch (FinderException fe) {
			fe.printStackTrace(System.err);
		}

		if ((reseller != null) && (product != null)) {
			contract = getContractBusiness(iwc).getContract(reseller, product);
			contractId = ((Integer) contract.getPrimaryKey()).intValue();
		}

		String sBookingId = iwc.getParameter(this.parameterBookingId);
		if (sBookingId != null) {
			try {
				this.bookingId = Integer.parseInt(sBookingId);
				_booking = ((is.idega.idegaweb.travel.data.GeneralBookingHome) com.idega.data.IDOLookup.getHome(GeneralBooking.class)).findByPrimaryKey(new Integer(bookingId));
			}
			catch (FinderException fe) {
				/** not handled */
			}
		}
		stamp = getIdegaTimestamp(iwc);

	}

	private void displayForm(IWContext iwc) throws Exception {
		displayForm(iwc, null);
	}

	private void displayForm(IWContext iwc, PresentationObject contentTableForm) throws Exception {

		Form topTable = getTopTable(iwc);
		add(Text.BREAK);

		if ((supplier != null) || (reseller != null) || ((supplier == null) && (reseller == null) && (product == null))) {
			add(topTable);
			add(Text.BREAK);
		}

		if (product != null) {
			System.out.println("Beginning to create BookingForm ... : " + IWTimestamp.RightNow().toSQLTimeString());
			Table contentTable = new Table(1, 1);
			contentTable.setBorder(1);
			contentTable.add(getContentHeader(iwc));
			contentTable.add(Text.BREAK);
			contentTable.add(getTotalTable(iwc));
			if (contentTableForm == null) {
				contentTable.add(getContentTable(iwc));
			}
			else {
				contentTable.add(contentTableForm);
			}
			System.out.println("Finished creating BookingForm ..... : " + IWTimestamp.RightNow().toSQLTimeString());
			contentTable.setWidth("90%");
			contentTable.setCellspacing(0);
			contentTable.setCellpadding(0);
			contentTable.setBorderColor(super.textColor);
			add(contentTable);
		}
		else {
			add(iwrb.getLocalizedString("travel.select_a_product", "Select a product"));
		}

		int row = 0;
		add(Text.getBreak());
	}

	private Form getTopTable(IWContext iwc) throws RemoteException {
		Form form = new Form();
		Table topTable = new Table(5, 1);
		form.add(topTable);
		topTable.setBorder(0);
		topTable.setWidth("90%");

		Text dateText = (Text) theText.clone();
		dateText.setText(iwrb.getLocalizedString("travel.date", "Date"));
		dateText.addToText(":");

		DropdownMenu trip = null;
		if (supplier != null) {
			trip = getProductBusiness(iwc).getDropdownMenuWithProducts(iwc, supplierId);
		}
		else if (reseller != null) {
			trip = getContractBusiness(iwc).getDropdownMenuWithProducts(iwc, resellerId);
		}
		else if (product == null) {
			trip = new DropdownMenu(getProductBusiness(iwc).getProducts(iwc, -1));
		}

		if (trip != null) if (product != null) {
			trip.setSelectedElement(Integer.toString(product.getID()));
		}

		Text nameText = (Text) theText.clone();
		nameText.setText(iwrb.getLocalizedString("travel.product_name_lg", "Name of product"));
		nameText.addToText(":");

		DateInput dateInp = new DateInput("IWCalendar");
		dateInp.setDate(stamp.getSQLDate());
		dateInp.setYearRange(2001, IWTimestamp.RightNow().getYear() + 4);

		topTable.setColumnAlignment(1, "right");
		topTable.setColumnAlignment(2, "left");
		topTable.add(nameText, 1, 1);
		topTable.add(trip, 2, 1);
		topTable.add(dateText, 3, 1);
		topTable.add(dateInp, 4, 1);

		topTable.setAlignment(5, 1, "right");
		topTable.add(new SubmitButton(iwrb.getImage("buttons/get.gif"), "", ""), 5, 1);

		return form;
	}

	private Table getContentHeader(IWContext iwc) throws Exception {
		ServiceOverview so = super.getServiceHandler(iwc).getServiceOverview(iwc, this.product);
		Table table = so.getServiceInfoTable(iwc, this.product);

		return table;
	}

	private Table getContentTable(IWContext iwc) throws Exception {
		Table table = new Table();
		table.setWidth("100%");
		table.setBorder(0);
		table.setCellspacing(0);
		table.setCellpadding(2);
		table.setWidth(6, "200");

		/**
		 * @todo minnka endurtekningar
		 */

		BookingForm bf = super.getServiceHandler(iwc).getBookingForm(iwc, product);
		bf.setTimestamp(stamp);

		int row = 1;
		boolean isDayVisible = false;
		boolean isExpired = false;
		int iBookings = 0;
		try {
			if (reseller != null) {
				isExpired = bf.getIfExpired(iwc);

				if (!isExpired) {
					isDayVisible = bf.getIsDayVisible(iwc);
				}
			}
			else {
				isDayVisible = bf.getIsDayVisible(iwc);
				if (supplier == null) {
					if (isDayVisible) {
						iBookings = getBooker(iwc).getBookingsTotalCount(productId, stamp, -1);
					}
				}
			}
		}
		catch (ServiceNotFoundException snfe) {
			snfe.printStackTrace(System.err);
		}
		catch (TimeframeNotFoundException tfnfe) {
			tfnfe.printStackTrace(System.err);
		}

		boolean yearly = true;
		if (timeframe != null) {
			yearly = timeframe.getIfYearly();
		}
		List depDays = null;

		if (isDayVisible) {
			table.setColor(6, row, super.backgroundColor);
			table.add(Text.BREAK, 6, row);
			table.add(getCalendar(iwc), 6, row);
			table.setVerticalAlignment(6, row, "top");

			table.mergeCells(1, row, 5, row);
			if (supplier != null) {
				Inquery[] inqueries = getInquirer(iwc).getInqueries(product.getID(), stamp, true, is.idega.idegaweb.travel.data.InqueryBMPBean.getInqueryPostDateColumnName());

				if (inqueries.length > 0) {
					table.add(getInqueries(iwc, inqueries, product), 1, row);
					table.setColor(1, row, super.YELLOW);
					++row;
				}
				else {
					table.setColor(1, row, super.backgroundColor);
				}
			}
			else if (reseller != null) {
				Collection coll = getInquirer(iwc).getInquiryHome().findInqueries(product.getID(), stamp, reseller.getID(), true, is.idega.idegaweb.travel.data.InqueryBMPBean.getInqueryPostDateColumnName());
				Inquery[] inqueries = getInquirer(iwc).collectionToInqueryArray(coll);

				if (inqueries.length > 0) {
					table.add(getInqueries(iwc, inqueries, product), 1, row);
					table.setColor(1, row, super.YELLOW);
					++row;
				}
				else {
					table.setColor(1, row, super.backgroundColor);
				}
			}

			table.mergeCells(1, row, 5, row);
			table.setColor(1, row, super.backgroundColor);
			table.mergeCells(6, 1, 6, row);
			table.add(Text.BREAK, 1, row);

			if (bf.isFullyBooked(iwc, product, stamp)) {
				table.add(super.getHeaderText(iwrb.getLocalizedString("travel.attention_fully_booked", "Attention! Fully booked")), 1, row);
				table.add(Text.BREAK, 1, row);
			}

			if (bf.isUnderBooked(iwc, product, stamp)) {
				table.add(super.getHeaderText(iwrb.getLocalizedString("travel.attention_under_booked", "Attention! Booked seats are fewer than the service minimum.")), 1, row);
				table.add(Text.BREAK, 1, row);
			}

			table.add(Text.BREAK, 1, row);
			table.add(getBookingForm(iwc, bf), 1, row);

		}
		else {
			if (isExpired) {
				if (supplier != null) {
					Inquery[] inqueries = getInquirer(iwc).getInqueries(product.getID(), stamp, true, is.idega.idegaweb.travel.data.InqueryBMPBean.getInqueryPostDateColumnName());

					if (inqueries.length > 0) {
						table.add(getInqueries(iwc, inqueries, product), 1, row);
						table.setColor(1, row, super.YELLOW);
						++row;
					}
					else {
						table.setColor(1, row, super.backgroundColor);
					}
				}
				else if (reseller != null) {
					Collection coll = getInquirer(iwc).getInquiryHome().findInqueries(product.getID(), stamp, reseller.getID(), true, is.idega.idegaweb.travel.data.InqueryBMPBean.getInqueryPostDateColumnName());
					Inquery[] inqueries = getInquirer(iwc).collectionToInqueryArray(coll);

					if (inqueries.length > 0) {
						table.add(getInqueries(iwc, inqueries, product), 1, row);
						table.setColor(1, row, super.YELLOW);
						++row;
					}
					else {
						table.setColor(1, row, super.backgroundColor);
					}
				}
				table.add(iwrb.getLocalizedString("travel.time_for_booking_has_passed", "Time for booking has passed"), 1, row + 2);
			}
			else {
				if (supplier != null) {
					Inquery[] inqueries = getInquirer(iwc).getInqueries(product.getID(), stamp, true, is.idega.idegaweb.travel.data.InqueryBMPBean.getInqueryPostDateColumnName());

					if (inqueries.length > 0) {
						table.add(getInqueries(iwc, inqueries, product), 1, row);
						table.setColor(1, row, super.YELLOW);
						++row;
					}
					else {
						table.setColor(1, row, super.backgroundColor);
					}
				}
				else if (reseller != null) {
					Collection coll = getInquirer(iwc).getInquiryHome().findInqueries(product.getID(), stamp, reseller.getID(), true, is.idega.idegaweb.travel.data.InqueryBMPBean.getInqueryPostDateColumnName());
					Inquery[] inqueries = getInquirer(iwc).collectionToInqueryArray(coll);

					if (inqueries.length > 0) {
						table.add(getInqueries(iwc, inqueries, product), 1, row);
						table.setColor(1, row, super.YELLOW);
						++row;
					}
					else {
						table.setColor(1, row, super.backgroundColor);
					}
				}

				table.add(iwrb.getLocalizedString("travel.trip_is_not_scheduled_this_day", "Trip is not scheduled this day") + " : " + stamp.getLocaleDate(iwc));
			}
			table.mergeCells(1, row, 5, row);
			table.setAlignment(1, row, "center");

			table.setColor(6, row, super.backgroundColor);
			table.add(Text.BREAK, 6, row);
			table.add(getCalendar(iwc), 6, row);
			table.setVerticalAlignment(6, row, "top");

			++row;
			table.mergeCells(1, row, 5, row);
			table.setColor(1, row, super.backgroundColor);
			table.mergeCells(6, 1, 6, row);
		}

		return table;

	}

	private Table getCalendar(IWContext iwc) {
		try {
			CalendarHandler ch = new CalendarHandler(iwc);
			if (contract != null) ch.setContract(contract);
			if (product != null) ch.setProduct(product);
			if (reseller != null) ch.setReseller(reseller);
			//      if (tour != null) ch.setTour(tour);
			ch.setTimestamp(stamp);
			ch.setCellpadding(2);
//			ch.showInquiries(true);

			
//      ch.setTextStyle(getStyleName(BookingForm.STYLENAME_TEXT));
//      ch.setHeaderStyle(getStyleName(BookingForm.STYLENAME_HEADER_TEXT));
//      ch.setLinkStyle(getStyleName(BookingForm.STYLENAME_LINK));
//      ch.setBackgroundStyle(getStyleName(BookingForm.STYLENAME_BACKGROUND_COLOR));
//      ch.setAvailableDayFontStyle(getStyleName(BookingForm.STYLENAME_TEXT));
//      ch.setAvailableDayStyle(getStyleName(BookingForm.STYLENAME_AVAILABLE_DAY));
//      ch.setFullyBookedStyle(getStyleName(BookingForm.STYLENAME_FULLY_BOOKED));
//      ch.setTodayStyle(getStyleName(BookingForm.STYLENAME_TODAY));
//      ch.setInquiryStyle(getStyleName(BookingForm.STYLENAME_INQUIRY));
//      ch.setInActiveCellStyle(getStyleName(BookingForm.STYLENAME_HEADER_BACKGROUND_COLOR));
//
//	    ch.addParameterToLink(this.parameterProductId, productId);			
			
			return ch.getCalendarTable(iwc);
		}
		catch (Exception e) {
			return new Table();
		}
	}

	private Table getInqueries(IWContext iwc, Inquery[] inqueries, Product product) throws RemoteException, FinderException, CreateException {
		Table table = new Table();
		table.setWidth("100%");
		table.setBorder(0);
		table.setCellspacing(1);

		int row = 0;

		table.setWidth(1, "100");

		Text dateText;
		Text addrText;
		Text nameText;
		Text countText;
		Text contentText;

		IWTimestamp theStamp;
		Link answerYes;
		Link answerNo;
		Link answerDel;

		List groupInq;
		IWTimestamp tempStamp;
		GeneralBooking booking;
		Collection coll;

		for (int i = 0; i < inqueries.length; i++) {
			theStamp = new IWTimestamp(inqueries[i].getInqueryDate());
			booking = inqueries[i].getBooking();

			groupInq = getInquirer(iwc).getInquiryHome().create().getMultibleInquiries(inqueries[i]);
			dateText = (Text) theSmallBoldText.clone();
			dateText.setFontColor(BLACK);
			if (groupInq == null || groupInq.size() <= 1) {
				dateText.setText(theStamp.getLocaleDate(iwc));
			}
			else {
				InqueryHome iHome = (InqueryHome) IDOLookup.getHome(Inquery.class);
				theStamp = new IWTimestamp((iHome.findByPrimaryKey(groupInq.get(0))).getInqueryDate());
				tempStamp = new IWTimestamp((iHome.findByPrimaryKey(groupInq.get(groupInq.size() - 1))).getInqueryDate());
				dateText.setText(theStamp.getLocaleDate(iwc) + " - " + tempStamp.getLocaleDate(iwc));
			}
			nameText = (Text) theSmallBoldText.clone();
			nameText.setFontColor(BLACK);
			nameText.setText(inqueries[i].getName());
			addrText = (Text) theSmallBoldText.clone();
			addrText.setFontColor(BLACK);
			countText = (Text) theSmallBoldText.clone();
			countText.setFontColor(BLACK);
			countText.setText(Integer.toString(inqueries[i].getNumberOfSeats()));
			contentText = (Text) theSmallBoldText.clone();
			contentText.setFontColor(BLACK);
			contentText.setText(inqueries[i].getInquery());

			++row;
			table.setAlignment(1, row, "left");
			table.setAlignment(2, row, "left");
			table.setAlignment(3, row, "right");
			table.add(dateText, 1, row);
			table.add(nameText, 2, row);
			table.add(countText, 3, row);

			++row;
			try {
				coll = booking.getTravelAddresses();
				Iterator iter = coll.iterator();
				if (iter.hasNext()) {
					TravelAddress item = (TravelAddress) iter.next();
					IWTimestamp time = new IWTimestamp(item.getTime());
					addrText.setText(item.getName());
					table.add(addrText, 1, row);
					table.setAlignment(1, row, "left");
				}
			}
			catch (IDORelationshipException idoRelEx) {
				idoRelEx.printStackTrace(System.err);
			}

			++row;
			table.mergeCells(2, row, 3, row);
			table.setAlignment(2, row, "left");
			table.add(contentText, 2, row);

			++row;
			++row;
			table.mergeCells(2, row, 3, row);
			table.setAlignment(2, row, "right");
			
			Collection resellers = null;
			try {
				resellers = inqueries[i].getResellers();
			}
			catch (IDORelationshipException e) {
				e.printStackTrace();
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}

			if (supplier != null) {
				if(inqueries[i].getInqueryType() != null && inqueries[i].getInqueryType().equals(InquirerBean.INQUERYTYPE_CREDITCARD)) {
					Link answerCCYes = new Link(iwrb.getLocalizedImageButton("travel.confirm_booking", "Confirm booking"));
					answerCCYes.addParameter(this.parameterInqueryId, ((Integer) inqueries[i].getPrimaryKey()).intValue());
					answerCCYes.addParameter(this.parameterRespondInquery, this.parameterRespondCCYes);
					answerCCYes.addParameter(BookingForm.sAction, this.parameterRespondInquery);
					answerCCYes.addParameter("year", this.stamp.getYear());
					answerCCYes.addParameter("month", this.stamp.getMonth());
					answerCCYes.addParameter("day", this.stamp.getDay());
					
					Link answerCCNo = new Link(iwrb.getLocalizedImageButton("travel.reject_booking", "Reject booking"));
					answerCCNo.addParameter(this.parameterInqueryId, ((Integer) inqueries[i].getPrimaryKey()).intValue());
					answerCCNo.addParameter(this.parameterRespondInquery, this.parameterRespondCCNo);
					answerCCNo.addParameter(BookingForm.sAction, this.parameterRespondInquery);
					answerCCNo.addParameter("year", this.stamp.getYear());
					answerCCNo.addParameter("month", this.stamp.getMonth());
					answerCCNo.addParameter("day", this.stamp.getDay());
					
					table.add(answerCCYes, 2, row);
					table.add("&nbsp;&nbsp;", 2, row);
					table.add(answerCCNo, 2, row);
				}
				else {
					answerYes = new Link(iwrb.getLocalizedImageButton("travel.confirm_booking", "Confirm booking"));
					answerYes.addParameter(this.parameterInqueryId, ((Integer) inqueries[i].getPrimaryKey()).intValue());
					answerYes.addParameter(this.parameterRespondInquery, this.parameterRespondYes);
					answerYes.addParameter(BookingForm.sAction, this.parameterRespondInquery);
					answerYes.addParameter("year", this.stamp.getYear());
					answerYes.addParameter("month", this.stamp.getMonth());
					answerYes.addParameter("day", this.stamp.getDay());

					answerNo = new Link(iwrb.getLocalizedImageButton("travel.reject_booking", "Reject booking"));
					answerNo.addParameter(this.parameterInqueryId, ((Integer) inqueries[i].getPrimaryKey()).intValue());
					answerNo.addParameter(this.parameterRespondInquery, this.parameterRespondNo);
					answerNo.addParameter(BookingForm.sAction, this.parameterRespondInquery);
					answerNo.addParameter("year", this.stamp.getYear());
					answerNo.addParameter("month", this.stamp.getMonth());
					answerNo.addParameter("day", this.stamp.getDay());

					table.add(answerYes, 2, row);
					table.add("&nbsp;&nbsp;", 2, row);
					table.add(answerNo, 2, row);
				}
			}
			else if (reseller != null) {
				answerNo = new Link(iwrb.getLocalizedImageButton("travel.cancel_inquiry", "Cancel sinquiry"));
				answerNo.addParameter(this.parameterInqueryId, ((Integer) inqueries[i].getPrimaryKey()).intValue());
				answerNo.addParameter(this.parameterRespondInquery, this.parameterRespondNo);
				answerNo.addParameter(BookingForm.sAction, this.parameterRespondInquery);

				table.add(answerNo, 2, row);
			}
			if(inqueries[i].getInqueryType() != null && inqueries[i].getInqueryType().equals(InquirerBean.INQUERYTYPE_CREDITCARD)) {
				Link answerCCDel = new Link(iwrb.getLocalizedImageButton("travel.delete_inquiry", "Delete inquiry"));
				answerCCDel.addParameter(this.parameterInqueryId, ((Integer) inqueries[i].getPrimaryKey()).intValue());
				answerCCDel.addParameter(this.parameterRespondInquery, this.parameterRespondCCDel);
				answerCCDel.addParameter(BookingForm.sAction, this.parameterRespondInquery);
				answerCCDel.addParameter("year", this.stamp.getYear());
				answerCCDel.addParameter("month", this.stamp.getMonth());
				answerCCDel.addParameter("day", this.stamp.getDay());
			}
			else {
				answerDel = new Link(iwrb.getLocalizedImageButton("travel.delete_inquiry", "Delete inquiry"));
				answerDel.addParameter(this.parameterInqueryId, ((Integer) inqueries[i].getPrimaryKey()).intValue());
				answerDel.addParameter(this.parameterRespondInquery, this.parameterRespondDel);
				answerDel.addParameter(BookingForm.sAction, this.parameterRespondInquery);
				answerDel.addParameter("year", this.stamp.getYear());
				answerDel.addParameter("month", this.stamp.getMonth());
				answerDel.addParameter("day", this.stamp.getDay());
				table.add("&nbsp;&nbsp;", 2, row);
				table.add(answerDel, 2, row);
			}

			
		}

		return table;
	}

	private Form getTotalTable(IWContext iwc) throws Exception {
		Form form = new Form();
		Table table = new Table();
		form.add(table);
		table.setFrameHsides();
		table.setWidth("100%");
		table.setCellspacing(0);
		table.setColor(super.WHITE);
		table.setBorder(1);
		table.setBorderColor(super.textColor);
		int row = 1;

		String cellWidth = "60";
		table.setWidth(2, cellWidth);
		table.setWidth(3, cellWidth);
		table.setWidth(4, cellWidth);
		table.setWidth(5, cellWidth);
		table.setWidth(6, "200");

		int iCount = 0;
		int iBooked = 0;
		int iInquery = 0;
		int iAvailable = 0;
		int iMin = 0;
		int iBookingExtra = 0;
		
		Text dateText = (Text) theBoldText.clone();
		dateText.setText(stamp.getLocaleDate(iwc));
		dateText.setFontColor(super.BLACK);
		Text countText = (Text) theText.clone();
		countText.setText(iwrb.getLocalizedString("travel.count_sm", "count"));
		countText.setFontColor(super.BLACK);
		Text assignedText = (Text) theText.clone();
		assignedText.setText(iwrb.getLocalizedString("travel.assigned_small_sm", "assigned"));
		assignedText.setFontColor(super.BLACK);
		Text inqText = (Text) theText.clone();
		inqText.setText(iwrb.getLocalizedString("travel.inqueries_small_sm", "inq."));
		inqText.setFontColor(super.BLACK);
		Text bookedText = (Text) theText.clone();
		bookedText.setText(iwrb.getLocalizedString("travel.booked_sm", "booked"));
		bookedText.setFontColor(super.BLACK);
		Text availableText = (Text) theText.clone();
		availableText.setText(iwrb.getLocalizedString("travel.available_small_sm", "avail."));
		availableText.setFontColor(super.BLACK);
		Text bookingStatusText = (Text) theBoldText.clone();
		bookingStatusText.setText(iwrb.getLocalizedString("travel.booking_status", "Booking status"));
		bookingStatusText.setFontColor(super.BLACK);
		Text calendarForBooking = (Text) theText.clone();
		calendarForBooking.setText(iwrb.getLocalizedString("travel.booking_status", "Booking status"));
		calendarForBooking.setFontColor(super.BLACK);

		Text dateTextBold = (Text) theSmallBoldText.clone();
		dateTextBold.setText("");
		Text nameTextBold = (Text) theSmallBoldText.clone();
		nameTextBold.setText("");
		Text countTextBold = (Text) theSmallBoldText.clone();
		countTextBold.setText("");
		Text assignedTextBold = (Text) theSmallBoldText.clone();
		assignedTextBold.setText("");
		Text inqTextBold = (Text) theSmallBoldText.clone();
		inqTextBold.setText("");
		Text bookedTextBold = (Text) theSmallBoldText.clone();
		bookedTextBold.setText("");
		Text availableTextBold = (Text) theSmallBoldText.clone();
		availableTextBold.setText("");
		dateTextBold.setFontColor(super.BLACK);
		nameTextBold.setFontColor(super.BLACK);
		countTextBold.setFontColor(super.BLACK);
		assignedTextBold.setFontColor(super.BLACK);
		inqTextBold.setFontColor(super.BLACK);
		bookedTextBold.setFontColor(super.BLACK);
		availableTextBold.setFontColor(super.BLACK);

		//      BookingForm bf = super.getServiceHandler(iwc).getBookingForm(iwc,
		// product);

		try {
			if (reseller != null) {
				try {
					//          if (bf.getIsDayVisible(iwc)) {
					if (tsb.getIfDay(iwc, contract, product, this.stamp)) {
						iCount = contract.getAlotment();

						if (iCount > 0) {
							countTextBold.setText(Integer.toString(iCount));
						}

						iBooked = getBooker(iwc).getBookingsTotalCountByReseller(resellerId, ((Integer) service.getPrimaryKey()).intValue(), this.stamp);
						bookedTextBold.setText(Integer.toString(iBooked));

						iInquery = getInquirer(iwc).getInquiryHome().getInqueredSeats(((Integer) service.getPrimaryKey()).intValue(), this.stamp, reseller.getID(), true);
						inqTextBold.setText(Integer.toString(iInquery));

						if (iCount > 0) {
							iAvailable = iCount - iBooked - iInquery;
							available = iAvailable;
							availableTextBold.setText(Integer.toString(iAvailable));
						}

						iCount = iCount - iBooked;

					}
				}
				catch (SQLException sql) {
					sql.printStackTrace(System.err);
				}
			}
			else {
				try {
					if (tsb.getIfDay(iwc, this.product, this.product.getTimeframes(), this.stamp, false, true)) {
						iCount = 0;
						iBooked = getBooker(iwc).getGeneralBookingHome().getBookingsTotalCount(((Integer) product.getPrimaryKey()).intValue(), this.stamp, null, -1, new int[] {}, travelAddressIds);
						iInquery = getInquirer(iwc).getInqueredSeats(((Integer) service.getPrimaryKey()).intValue(), this.stamp, true);

						TravelStockroomBusiness tsb = super.getServiceHandler(iwc).getServiceBusiness(this.product);
						iCount = tsb.getMaxBookings(product, stamp);
						iMin = tsb.getMinBookings(product, stamp);
						//iBookingExtra = getBooker(iwc).getBookingsTotalCountByOthersInPool(product, this.stamp);

						if (supplier != null) {
							if (iCount > 0) {
								countTextBold.setText(Integer.toString(iCount));
							}
							bookedTextBold.setText(Integer.toString(iBooked));
							if (iBookingExtra > 0) {
								bookedTextBold.addToText(" ("+iBookingExtra+") *");
							}
							inqTextBold.setText(Integer.toString(iInquery));
						}

						if (iCount > 0) {
							iAvailable = iCount - iBooked - iBookingExtra;
							available = iAvailable;
							availableTextBold.setText(Integer.toString(iAvailable));
						}

					}
				}
				catch (SQLException sql) {
					sql.printStackTrace(System.err);
				}
			}

		}
		catch (ServiceNotFoundException snfe) {
			snfe.printStackTrace(System.err);
		}
		catch (TimeframeNotFoundException tfnfe) {
			tfnfe.printStackTrace(System.err);
		}

		this.iMax = iCount;
		this.iBookings = iBooked;
		this.iMin = iMin;

		table.add(dateText, 1, row);

		table.add(countText, 2, row);
		table.add(bookedText, 3, row);
		table.add(inqText, 4, row);
		table.add(availableText, 5, row);
		table.add(calendarForBooking, 6, row);
		table.setRowColor(row, super.GRAY);

		++row;
		table.add(bookingStatusText, 1, row);
		table.setColor(2, row, super.backgroundColor);

		table.add(countTextBold, 2, row);
		table.add(bookedTextBold, 3, row);
		table.add(inqTextBold, 4, row);
		table.add(availableTextBold, 5, row);
		if (iBookingExtra > 0) {
			Text extra = (Text) theSmallBoldText.clone();
			extra.setFontColor(super.BLACK);
			extra.setText(iwrb.getLocalizedString("travel.extra_bookings_explated","* Bookings made on pool"));
			table.add(extra, 6, row);
		}

		table.setColumnAlignment(1, "left");
		table.setColumnAlignment(2, "center");
		table.setColumnAlignment(3, "center");
		table.setColumnAlignment(4, "center");
		table.setColumnAlignment(5, "center");
		table.setColumnAlignment(6, "center");
		table.setRowColor(row, super.GRAY);

		return form;
	}

	private Form getBookingForm(IWContext iwc, BookingForm bf) throws Exception {

		try {
			bf.setTimestamp(stamp);
			if (_booking != null) {
				bf.setBooking(_booking);
				bf.setTimestamp(new IWTimestamp(_booking.getBookingDate()));
			}
			return bf.getBookingForm(iwc);
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
			return new Form();
		}
	}

	// BUSINESS
	private IWTimestamp getIdegaTimestamp(IWContext iwc) {
		IWTimestamp stamp = null;

		if (this._booking == null) {
			String year = iwc.getParameter("year");
			String month = iwc.getParameter("month");
			String day = iwc.getParameter("day");

			String IWCalendar_year = iwc.getParameter(CalendarParameters.PARAMETER_YEAR);
			String IWCalendar_month = iwc.getParameter(CalendarParameters.PARAMETER_MONTH);
			String IWCalendar_day = iwc.getParameter(CalendarParameters.PARAMETER_DAY);
			if (IWCalendar_year != null) year = IWCalendar_year;
			if (IWCalendar_month != null) month = IWCalendar_month;
			if (IWCalendar_day != null) day = IWCalendar_day;

			String dateStr = iwc.getParameter("booking_date");

			if (dateStr == null) {
				String chYear = iwc.getParameter("chosen_year");
				if ((chYear != null) && (year != null)) year = chYear;

				if (stamp == null)

				try {
					if ((day != null) && (month != null) && (year != null)) {
						stamp = new IWTimestamp(Integer.parseInt(day), Integer.parseInt(month), Integer.parseInt(year));
					}
					else if ((day == null) && (month == null) && (year != null)) {
						stamp = new IWTimestamp(1, IWTimestamp.RightNow().getMonth(), Integer.parseInt(year));
					}
					else if ((day == null) && (month != null) && (year != null)) {
						stamp = new IWTimestamp(1, Integer.parseInt(month), Integer.parseInt(year));
					}
					else {
						stamp = IWTimestamp.RightNow();
					}
				}
				catch (Exception e) {
					stamp = IWTimestamp.RightNow();
				}
			}
			else {
				stamp = new IWTimestamp(dateStr);
			}
		}
		else {
			stamp = new IWTimestamp(_booking.getBookingDate());
		}
		return stamp;
	}

	private int handleInsert(IWContext iwc, boolean displayForm) throws Exception {

		BookingForm bf = super.getServiceHandler(iwc).getBookingForm(iwc, product);
		//    TourBookingForm tbf = new TourBookingForm(iwc, product);
		try {
			if (reseller != null) bf.setReseller(reseller);
			bf.setTimestamp(stamp);

			int returner = bf.handleInsert(iwc);

			if (returner == bf.inquirySent) {
				/** @todo Cleara form eftir a� inquiry hefur att ser stad  */
				displayForm(iwc);
			}
			else if (returner == 0) {
				displayForm(iwc);
			}
			else if (returner > 1) {
				add(Text.BREAK);
				add(bookingInformation(iwc, returner));
			}
			else {
				displayForm(iwc, bf.getErrorForm(iwc, returner));
			}
			return returner;
		}
		catch (Exception e) {
			//e.printStackTrace(System.err);
			add(Text.BREAK);
			add(bookingError(e));

			return -1;
		}
	}

	private Table bookingError(Exception e) throws RemoteException {
		Text bookingFailed = (Text) super.theBoldText.clone();
		bookingFailed.setText(iwrb.getLocalizedString("travel.booking_failed", "Booking failed"));

		//e.printStackTrace(System.err);

		Text reason = (Text) super.theText.clone();
		reason.setText(e.getMessage());
		reason.setFontColor(BLACK);

		Link link = super.getBackLink();

		Table table = new Table();
		table.setColor(super.WHITE);
		table.setCellpaddingAndCellspacing(1);

		table.add(bookingFailed, 1, 1);
		table.setRowColor(1, super.backgroundColor);
		table.add(reason, 1, 2);
		table.setRowColor(2, super.GRAY);
		table.add(link, 1, 3);
		table.setRowColor(3, super.GRAY);
		table.setAlignment(1, 3, Table.HORIZONTAL_ALIGN_LEFT);

		return table;
	}

	private Table bookingInformation(IWContext iwc, int bookingId) throws RemoteException {
		try {
			GeneralBooking booking = ((is.idega.idegaweb.travel.data.GeneralBookingHome) com.idega.data.IDOLookup.getHome(GeneralBooking.class)).findByPrimaryKey(new Integer(bookingId));
			int voucherNumber = Voucher.getVoucherNumber(bookingId);
			String referenceNumber = booking.getReferenceNumber();

			Text bookingSuccessful = (Text) super.theBoldText.clone();
			bookingSuccessful.setText(iwrb.getLocalizedString("travel.booking_was_successful", "Booking was successful"));

			Text refNumTxt = (Text) super.theText.clone();
			refNumTxt.setText(iwrb.getLocalizedString("travel.reference_number", "Reference number"));
			refNumTxt.setFontColor(super.BLACK);
			Text voucherNumTxt = (Text) super.theText.clone();
			voucherNumTxt.setText(iwrb.getLocalizedString("travel.voucher_number", "Voucher number"));
			voucherNumTxt.setFontColor(super.BLACK);
			Text voucher = (Text) super.theBoldText.clone();
			voucher.setText(iwrb.getLocalizedString("travel.voucher", "Voucher"));
			voucher.setFontColor(super.BLACK);
			Text receiptText = (Text) super.theBoldText.clone();
			receiptText.setText(iwrb.getLocalizedString("travel.receipt", "Receipt"));
			receiptText.setFontColor(super.BLACK);

			Link voucherLink = new Link(voucher);
			voucherLink.setWindowToOpen(VoucherWindow.class);
			voucherLink.addParameter(VoucherWindow.parameterBookingId, bookingId);

			Link printCCReceipt = null;
			try {
				CreditCardAuthorizationEntry entry = this.getCreditCardBusiness(iwc).getAuthorizationEntry(supplier, booking.getCreditcardAuthorizationNumber(), new IWTimestamp(booking.getDateOfBooking()));
				if (entry != null) {
					printCCReceipt = new Link(receiptText);
					printCCReceipt.setWindowToOpen(ReceiptWindow.class);

					Receipt r = new Receipt(entry, supplier);
					iwc.setSessionAttribute(ReceiptWindow.RECEIPT_SESSION_NAME, r);
				}
			}
			catch (Exception e) {
				e.printStackTrace(System.err);
			}

			Text refNum = (Text) super.theBoldText.clone();
			refNum.setText(referenceNumber);
			refNum.setFontColor(super.BLACK);
			Text voucherNum = (Text) super.theBoldText.clone();
			voucherNum.setText(Integer.toString(voucherNumber));
			voucherNum.setFontColor(super.BLACK);

			Link btnBook = new Link(super.getTravelSessionManager(iwc).getIWResourceBundle().getImage("buttons/change.gif"), is.idega.idegaweb.travel.presentation.Booking.class);
			btnBook.addParameter(is.idega.idegaweb.travel.presentation.Booking.BookingAction, is.idega.idegaweb.travel.presentation.Booking.parameterUpdateBooking);
			btnBook.addParameter(is.idega.idegaweb.travel.presentation.Booking.parameterBookingId, booking.getID());
			btnBook.addParameter(BookingForm.parameterDepartureAddressId, iwc.getParameter(BookingForm.parameterDepartureAddressId));

			Link backLink = new Link(iwrb.getImage("buttons/back.gif"));
			backLink.addParameter(this.parameterProductId, this.productId);
			backLink.addParameter(CalendarParameters.PARAMETER_DAY, stamp.getDay());
			backLink.addParameter(CalendarParameters.PARAMETER_MONTH, stamp.getMonth());
			backLink.addParameter(CalendarParameters.PARAMETER_YEAR, stamp.getYear());

			Table table = new Table();
			table.setColor(super.WHITE);
			table.add(bookingSuccessful, 1, 1);
			table.add(refNumTxt, 1, 2);
			table.add(refNum, 2, 2);
			table.add(voucherNumTxt, 1, 3);
			table.add(voucherNum, 2, 3);
			table.add(voucherLink, 1, 4);
			if (printCCReceipt != null) {
				table.add(printCCReceipt, 1, 5);
			}
			table.add(backLink, 1, 6);
			table.add(btnBook, 2, 6);

			table.mergeCells(1, 1, 2, 1);
			table.mergeCells(1, 4, 2, 4);
			table.mergeCells(1, 5, 2, 5);
			table.setAlignment(1, 6, Table.HORIZONTAL_ALIGN_LEFT);
			table.setAlignment(2, 6, Table.HORIZONTAL_ALIGN_RIGHT);
			//table.mergeCells(1,6,2,6);

			table.setRowColor(1, super.backgroundColor);
			table.setRowColor(2, super.GRAY);
			table.setRowColor(3, super.GRAY);
			table.setRowColor(4, super.GRAY);
			table.setRowColor(5, super.GRAY);
			table.setRowColor(6, super.GRAY);

			return table;
		}
		catch (FinderException fe) {
			fe.printStackTrace(System.err);
			return new Table();
		}
	}

	private void inqueryResponse(IWContext iwc) throws Exception {
		String yesNo = iwc.getParameter(this.parameterRespondInquery);
		String sInqueryId = iwc.getParameter(this.parameterInqueryId);
		Boolean book = null;
		int inqueryId = -1;
		Inquery inquery =  null;
		
		if(sInqueryId !=  null && !sInqueryId.equals("")) {
			inqueryId = Integer.parseInt(sInqueryId);
			inquery = ((is.idega.idegaweb.travel.data.InqueryHome)com.idega.data.IDOLookup.getHome(Inquery.class)).findByPrimaryKey(new Integer(inqueryId));
		}

		if (yesNo != null) {
			if (yesNo.equals(this.parameterRespondYes)) book = new Boolean(true);
			if (yesNo.equals(this.parameterRespondNo)) book = new Boolean(false);
		}
		
		if (book != null) {
			int errorMessage = getInquirer(iwc).inquiryResponse(iwc, iwrb, inqueryId, book.booleanValue(), this.supplier);

			switch (errorMessage) {
				case 0:
					displayForm(iwc);
					break;
				case 1:
					displayForm(iwc, getInquirer(iwc).getInquiryResponseError(iwrb));
					break;
			}
		}
		else if (yesNo != null && yesNo.equals(this.parameterRespondDel)) {
			int errorMessage = getInquirer(iwc).inquiryResponse(iwc, iwrb, inqueryId, false, false, this.supplier);

			switch (errorMessage) {
				case 0:
					displayForm(iwc);
					break;
				case 1:
					displayForm(iwc, getInquirer(iwc).getInquiryResponseError(iwrb));
					break;
			}
		}
		else if(yesNo.equals(this.parameterRespondCCYes)) {
			if(inquery != null) {
				int errorMessage = getInquirer(iwc).getCreditcardInqueryRespons(iwc,iwrb,inqueryId,true,true,this.supplier,this.reseller);
				GeneralBooking booking = inquery.getBooking();
	      CreditCardClient t = getCreditCardClient(iwc, booking);
				switch(errorMessage) {
					case 1:
						displayForm(iwc);
						t.finishTransaction(inquery.getAuthorizationString());
				}
			}
		}
		else if(yesNo.equals(this.parameterRespondCCNo)) {
			int errorMessage = getInquirer(iwc).getCreditcardInqueryRespons(iwc,iwrb,inqueryId,false,true,this.supplier,this.reseller);
			switch(errorMessage) {
				case 1:
					displayForm(iwc);
			}			
		}
		else if(yesNo.equals(this.parameterRespondCCDel)) {
			int errorMessage = getInquirer(iwc).getCreditcardInqueryRespons(iwc,iwrb,inqueryId,false,true,this.supplier,this.reseller);
			switch(errorMessage) {
				case 1:
					displayForm(iwc);
			}
		}
			
		
		

	}

	private void updateBooking(IWContext iwc) throws Exception {
		if (_booking != null) {
			displayForm(iwc);
		}
		else {
			displayForm(iwc, getErrorUpdateBookingTable());
		}
	}

	private Table getErrorUpdateBookingTable() {
		Table table = new Table();
		Text text = (Text) theBoldText.clone();
		text.setFontColor(super.WHITE);
		text.setText(iwrb.getLocalizedString("travel.cannot_edit_booking", "Cannot edit booking"));
		table.add(text);
		return table;
	}
	
	public CreditCardClient getCreditCardClient(IWContext iwc, GeneralBooking gBooking) {
		try {
			int productSupplierId = gBooking.getService().getProduct().getSupplierId();
			Supplier suppTemp = ((SupplierHome) IDOLookup.getHomeLegacy(Supplier.class)).findByPrimaryKeyLegacy(productSupplierId);
			return getCreditCardBusiness(iwc).getCreditCardClient(suppTemp, new IWTimestamp(gBooking.getDateOfBooking()));
		}catch (Exception e) {
			System.out.println("CreditCardMerchant NOT found");
		}		
		return null;
	}



}