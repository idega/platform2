package is.idega.idegaweb.travel.presentation;
import com.idega.presentation.text.Text;
import com.idega.util.text.TextSoap;
import com.idega.presentation.text.Link;
import is.idega.idegaweb.travel.data.GeneralBooking;
import com.idega.presentation.*;
import is.idega.idegaweb.travel.business.BookingComparator;
import is.idega.idegaweb.travel.interfaces.Booking;
import is.idega.idegaweb.travel.business.Booker;
import com.idega.util.idegaTimestamp;
import java.util.List;
import com.idega.idegaweb.IWResourceBundle;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class OnlineBookingReport extends TravelManager implements Report{

  private IWResourceBundle _iwrb;

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

  private void init(IWContext iwc) {
    _iwrb = super.getResourceBundle();
  }

  public boolean useTwoDates() {
    return true;
  }

  public PresentationObject getReport(IWContext iwc, List products, idegaTimestamp stamp) {
    Booking[] bookings = Booker.getBookings(products, new int[] {Booking.BOOKING_TYPE_ID_ONLINE_BOOKING},stamp);
    return getReport(iwc, bookings);
  }

  public PresentationObject getReport(IWContext iwc, List products, idegaTimestamp fromStamp, idegaTimestamp toStamp) {
    Booking[] bookings = Booker.getBookings(products, new int[] {Booking.BOOKING_TYPE_ID_ONLINE_BOOKING},fromStamp, toStamp, null, null);
    return getReport(iwc, bookings);
  }

  private PresentationObject getReport(IWContext iwc, Booking[] bookings) {
    BookingComparator bc = new BookingComparator(iwc, BookingComparator.DATE);
    bookings = bc.sortedArray(bookings);

    Table table = super.getTable();
      table.setWidth("100%");
    int row = 1;
    GeneralBooking gBooking;
    idegaTimestamp stamp;
    Link link;
    int count = 0;
    int tCount = 0;
    float price = 0;
    float tPrice = 0;

    table.add(getHeaderText(_iwrb.getLocalizedString("travel.date","Date")), 1, row);
    table.add(getHeaderText(_iwrb.getLocalizedString("travel.name","Name")), 2, row);
    table.add(getHeaderText(_iwrb.getLocalizedString("travel.count","Count")), 3, row);
    table.add(getHeaderText(_iwrb.getLocalizedString("travel.price","Price")), 4, row);


    for (int i = 0; i < bookings.length; i++) {
      ++row;
      gBooking = (GeneralBooking) bookings[i];
      stamp = new idegaTimestamp(gBooking.getBookingDate());
      count = bookings[i].getTotalCount();
      tCount += count;
      price = Booker.getBookingPrice(iwc, bookings[i]);
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
}