package is.idega.idegaweb.travel.presentation;
import is.idega.idegaweb.travel.business.BookingComparator;
import is.idega.idegaweb.travel.data.GeneralBooking;
import is.idega.idegaweb.travel.interfaces.Booking;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.FinderException;

import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.block.trade.stockroom.data.SupplierHome;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.util.IWTimestamp;
import com.idega.util.text.TextSoap;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class OnlineBookingReport extends TravelManager implements Report, AdministratorReport{

  private IWResourceBundle _iwrb;
  protected Supplier _supplier;
  protected String PARAMETER_VIEW_SUPPLIER = AdministratorReports.PARAMETER_SUPPLIER_ID_STATIC;//"obrVS";
  private String PARAMETER_SEARCH_BY = "obrVDB";
  private String PARAMETER_SEARCH_BY_DATE = "obrSBD";
  private String PARAMETER_SEARCH_BY_DATE_OF_BOOKING = "obrSBDB";
  protected boolean isAdminReport = false;
  protected boolean searchByDateOfBooking = false;

  private String reportWidth = "90%";

  public OnlineBookingReport(IWContext iwc) throws Exception{
    super.main(iwc);
    init(iwc);
  }

  public String getReportName() {
    return _iwrb.getLocalizedString("travel.report_name_online_booking_report","Online-booking report");
  }
  public String getReportDescription() {
    return _iwrb.getLocalizedString("travel_report_description_online_booking_report","Displays booking made online");
  }

  private void init(IWContext iwc) throws RemoteException {
    _iwrb = super.getResourceBundle();
    _supplier = super.getSupplier();

    String sortBy = iwc.getParameter(PARAMETER_SEARCH_BY);
    if (sortBy != null) {
      this.searchByDateOfBooking = sortBy.equals(PARAMETER_SEARCH_BY_DATE_OF_BOOKING);
    }
  }

  public boolean useTwoDates() {
    return true;
  }

  public PresentationObject getAdministratorReport(List suppliers, IWContext iwc, IWTimestamp stamp) throws RemoteException, FinderException {
    return getAdminReport(iwc, suppliers, stamp, null);
  }
  public PresentationObject getAdministratorReport(List suppliers, IWContext iwc, IWTimestamp fromStamp, IWTimestamp toStamp) throws RemoteException, FinderException {
    isAdminReport = true;
    String view_supplier = iwc.getParameter(PARAMETER_VIEW_SUPPLIER);
    if (view_supplier == null) {
      return getAdminReport(iwc, suppliers, fromStamp, toStamp);
    } else {
      SupplierHome sHome = (SupplierHome) IDOLookup.getHomeLegacy(Supplier.class);
      _supplier = sHome.findByPrimaryKey(new Integer(view_supplier));
      List products = getProductBusiness(iwc).getProducts(iwc, Integer.parseInt(view_supplier));

      return getReport(iwc, products, fromStamp, toStamp);
    }

  }
  public PresentationObject getReport(IWContext iwc, List products, IWTimestamp stamp) throws RemoteException, FinderException {
    if (_supplier != null) {
      Booking[] bookings = new Booking[]{};
      bookings = getBookings(iwc, stamp, null, products);
/*      if (this.searchByDateOfBooking ){
        bookings = getBooker(iwc).getBookings(products, new int[] {Booking.BOOKING_TYPE_ID_ONLINE_BOOKING}, stamp, null, null, null, true);
      } else {
        bookings = getBooker(iwc).getBookings(products, new int[] {Booking.BOOKING_TYPE_ID_ONLINE_BOOKING},stamp);
      }*/
      return getReport(iwc, bookings);
    } else {
      return super.getLoggedOffTable(iwc);
    }
  }

  public PresentationObject getReport(IWContext iwc, List products, IWTimestamp fromStamp, IWTimestamp toStamp) throws RemoteException, FinderException {
    if (_supplier != null || super.getTravelSessionManager(iwc).getSearchEngine() != null) {
      Booking[] bookings = new Booking[]{};
      bookings = getBookings(iwc, fromStamp, toStamp, products);
/*      if (this.searchByDateOfBooking ){
        bookings = getBooker(iwc).getBookings(products, new int[] {Booking.BOOKING_TYPE_ID_ONLINE_BOOKING}, fromStamp, toStamp, null, null, true);
      } else {
        bookings = getBooker(iwc).getBookings(products, new int[] {Booking.BOOKING_TYPE_ID_ONLINE_BOOKING},fromStamp, toStamp, null, null);
      }*/
      return getReport(iwc, bookings);
    } else {
      return super.getLoggedOffTable(iwc);
    }
  }


  private PresentationObject getReport(IWContext iwc, Booking[] bookings) throws RemoteException, FinderException{
    if ( this.searchByDateOfBooking ) {
      BookingComparator bc = new BookingComparator(iwc, BookingComparator.DATE_OF_BOOKING);
      bookings = bc.sortedArray(bookings);
    }else {
      BookingComparator bc = new BookingComparator(iwc, BookingComparator.DATE);
      bookings = bc.sortedArray(bookings);
    }

    Table table = super.getTable();
      table.setWidth(reportWidth);
    int row = 1;

    row = insertSearchByDropdown(table, row, 4);

    GeneralBooking gBooking;
    IWTimestamp stamp;
    Link link;
    int count = 0;
    int tCount = 0;
    float price = 0;
    float tPrice = 0;

    table.add(getHeaderText(_iwrb.getLocalizedString("travel.date","Date")), 1, row);
    table.add(getHeaderText(_iwrb.getLocalizedString("travel.name","Name")), 2, row);
    table.add(getHeaderText(_iwrb.getLocalizedString("travel.count","Count")), 3, row);
    table.add(getHeaderText(_iwrb.getLocalizedString("travel.price","Price")), 4, row);
    table.add(getHeaderText(_iwrb.getLocalizedString("travel.reference_number","Reference Number")), 6, row);
    table.setRowColor(row, super.backgroundColor);


    for (int i = 0; i < bookings.length; i++) {
      ++row;
      gBooking = (GeneralBooking) bookings[i];
      if (this.searchByDateOfBooking) {
        stamp = new IWTimestamp(gBooking.getDateOfBooking());
      }else {
        stamp = new IWTimestamp(gBooking.getBookingDate());
      }
      count = bookings[i].getTotalCount();
      tCount += count;
      price = getBooker(iwc).getBookingPrice(bookings[i]);
      if (!gBooking.getIsValid()) {
      		System.out.println("Invalid booking");
      		price *= -1;
      }

      tPrice += price;

      table.add(getText(stamp.getLocaleDate(iwc)), 1, row);
      link = VoucherWindow.getVoucherLink(bookings[i]);
        link.setText(getText(gBooking.getName()));
      table.add(link, 2, row);
      table.add(getText(Integer.toString(count)), 3, row);
      table.add(getText(TextSoap.decimalFormat(price, 2)), 4, row);

      if (gBooking.getCreditcardAuthorizationNumber() == null) {
        table.add(getText(_iwrb.getLocalizedString("travel.check_payment","Check payment")), 5, row);
        table.setColumnColor(5, super.GRAY);
      }
      table.add(getText(gBooking.getReferenceNumber()), 6, row);
      table.setRowColor(row, super.GRAY);
    }

    ++row;
    table.setRowColor(row, super.GRAY);
    Text countTxt = getText(Integer.toString(tCount));
      countTxt.setBold(true);
    Text priceTxt = getText(TextSoap.decimalFormat(tPrice, 2));
      priceTxt.setBold(true);
    table.add(countTxt, 3, row);
    table.add(priceTxt, 4, row);

    table.setRowColor(1, super.backgroundColor);
    table.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_CENTER);
    table.setColumnAlignment(4, Table.HORIZONTAL_ALIGN_RIGHT);


    return table;
  }

  protected Table getAdminReport(IWContext iwc, Collection suppliers, IWTimestamp fromStamp, IWTimestamp toStamp) throws FinderException, RemoteException{
    Table table = super.getTable();
      table.setWidth(reportWidth);
      table.setAlignment(Table.HORIZONTAL_ALIGN_CENTER);
      int row = 1;

    row = insertSearchByDropdown(table, row, 3);

    List products;
    Booking[] bookings;

    table.add(super.getHeaderText(_iwrb.getLocalizedString("travel.supplier","Supplier")), 1, row);
    table.add(super.getHeaderText(_iwrb.getLocalizedString("travel.no_bookings","# Bookings")), 2, row);
    table.add(super.getHeaderText(_iwrb.getLocalizedString("travel.amount","Amount")), 3, row);
    table.setRowColor(row, super.backgroundColor);

    int iCount = 0;
    float price = 0;
    int totalCount = 0;
    float totalPrice = 0;

    Iterator iter = suppliers.iterator();
    while (iter.hasNext()) {
      _supplier = (Supplier) iter.next();
      products = getProductBusiness(iwc).getProducts(iwc, _supplier.getID());
      bookings = getBookings(iwc, fromStamp, toStamp, products);

      iCount = bookings.length;
      price = getBooker(iwc).getBookingPrice(bookings);

      totalCount += iCount;
      totalPrice += price;

      ++row;
      Link link = AdministratorReports.getReportLink(getText(_supplier.getName()));
        link.addParameter(PARAMETER_VIEW_SUPPLIER, _supplier.getPrimaryKey().toString());
        if (this.searchByDateOfBooking) {
          link.addParameter(PARAMETER_SEARCH_BY, PARAMETER_SEARCH_BY_DATE_OF_BOOKING);
        }
      table.add(link, 1, row);
      table.add(super.getText(Integer.toString(iCount)), 2, row);
      table.add(super.getText(TextSoap.decimalFormat(price, 2)), 3, row);
      table.setRowColor(row, GRAY);
    }
    ++row;
    Text txtCount = getText(Integer.toString(totalCount));
    Text txtPrice = getText(TextSoap.decimalFormat(totalPrice, 2));
      txtCount.setBold(true);
      txtPrice.setBold(true);
    table.add(txtCount, 2, row);
    table.add(txtPrice, 3, row);
    table.setRowColor(row, GRAY);

    table.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_CENTER);
    table.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_RIGHT);

    return table;
  }

  protected Booking[] getBookings(IWContext iwc, IWTimestamp fromStamp, IWTimestamp toStamp, List products) throws RemoteException, FinderException {
    return getBooker(iwc).getBookings(products, new int[] {Booking.BOOKING_TYPE_ID_ONLINE_BOOKING}, fromStamp, toStamp, null, null, searchByDateOfBooking, true); // Setja i false, thegar a ad skoda deleted
	}
	
	private int insertSearchByDropdown(Table table, int row, int noColumns) {
    Text orderByText = super.getHeaderText(_iwrb.getLocalizedString("travel.search_by", "Search by"));
    orderByText.addToText(" : ");
    DropdownMenu searchMenu = new DropdownMenu(PARAMETER_SEARCH_BY);
    searchMenu.addMenuElement(PARAMETER_SEARCH_BY_DATE, _iwrb.getLocalizedString("travel.search_by_date", "Search by date"));
    searchMenu.addMenuElement(PARAMETER_SEARCH_BY_DATE_OF_BOOKING, _iwrb.getLocalizedString("travel.search_by_date_of_booking", "Search by date of booking"));
    if (this.searchByDateOfBooking) {
      searchMenu.setSelectedElement(PARAMETER_SEARCH_BY_DATE_OF_BOOKING);
    }
    searchMenu.setToSubmit();

    table.mergeCells(1, row, noColumns, row);
    table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
    table.setRowColor(row, super.backgroundColor);
    table.add(orderByText, 1, row);
    table.add(searchMenu, 1, row);
    ++row;
    return row;
  }

}