package is.idega.idegaweb.travel.presentation;

import com.idega.core.user.data.User;
import com.idega.idegaweb.*;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.util.idegaTimestamp;
import com.idega.util.text.TextSoap;
import com.idega.block.trade.stockroom.data.*;
import com.idega.block.trade.stockroom.business.*;
import is.idega.idegaweb.travel.business.*;
import is.idega.idegaweb.travel.data.*;
import is.idega.idegaweb.travel.interfaces.Booking;
import is.idega.idegaweb.travel.service.tour.data.TourBooking;
import is.idega.idegaweb.travel.service.tour.business.TourBooker;

import java.util.*;
import java.sql.SQLException;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class UserBookingReporter extends TravelManager {

  private IWBundle bundle;
  private IWResourceBundle iwrb;

  public UserBookingReporter() {
  }

  public void main(IWContext iwc) throws Exception{
    super.main(iwc);
    initialize(iwc);
  }

  private void initialize(IWContext iwc) {
    if (bundle == null && iwrb == null) {
      try {
        super.main(iwc);
      }catch (Exception e) {e.printStackTrace(System.err);}
      bundle = super.getBundle(iwc);
      iwrb = super.getResourceBundle();
    }
  }

  public Table getReport(IWContext iwc, Supplier supplier, idegaTimestamp stamp) {
    return getReport(iwc, supplier, stamp, new idegaTimestamp(stamp));
  }
  public Table getReport(IWContext iwc, Supplier supplier, idegaTimestamp fromStamp, idegaTimestamp toStamp) {
    List products = ProductBusiness.getProducts(supplier.getID(), fromStamp, toStamp);
    return getReport(iwc, products, fromStamp, toStamp);
  }

  public Table getReport(IWContext iwc, Product product, idegaTimestamp stamp) {
    return getReport(iwc, product, stamp, new idegaTimestamp(stamp));
  }
  public Table getReport(IWContext iwc, Product product, idegaTimestamp fromStamp, idegaTimestamp toStamp) {
    List list = new Vector();
    list.add(product);
    return getReport(iwc, list, fromStamp, toStamp);
  }

  public Table getReport(IWContext iwc, List products, idegaTimestamp stamp) {
    return getReport(iwc, products, stamp, new idegaTimestamp(stamp));
  }
  public Table getReport(IWContext iwc, List products, idegaTimestamp fromStamp, idegaTimestamp toStamp) {
    initialize(iwc);

    debug("getting [] "+idegaTimestamp.RightNow().toSQLTimeString());
    Booking[] bookings = Booker.getBookings(products, fromStamp, toStamp);
    BookingComparator bComp = new BookingComparator(BookingComparator.USER);
    debug("before sort "+idegaTimestamp.RightNow().toSQLTimeString());
    bookings = bComp.sortedArray(bookings);
    debug("after sort "+idegaTimestamp.RightNow().toSQLTimeString());

    Table table = new Table();
      table.setColor(super.WHITE);
      table.setCellspacing(1);
    int row = 1;

    table.setRowColor(row, super.backgroundColor);

    Product prod;
    User user;
    User owner;
    for (int i = 0; i < bookings.length; i++) {
      try {
        ++row;
        user = new User(bookings[i].getUserId());
        owner = new User(bookings[i].getOwnerId());
        prod = ProductBusiness.getProduct(bookings[i].getServiceID());

        table.add(new idegaTimestamp(bookings[i].getBookingDate()).getLocaleDate(_locale), 1, row);
        table.add(ProductBusiness.getProductName(prod, _localeId), 2, row);
        table.add(Integer.toString(bookings[i].getTotalCount()), 3, row);
        table.add(user.getName(), 4, row);
        table.add(owner.getName(), 5, row);

        table.setRowColor(row, super.GRAY);
      }catch (SQLException sql) {
        sql.printStackTrace();
      }
    }

    return table;
  }




  private Text getText(String content) {
    Text text = new Text(content);
      text.setFontStyle(super.theTextStyle);
      text.setFontColor(super.BLACK);
    return text;
  }

  private List getBookingList(Booking[] bookings) {
    List list = new Vector(bookings.length);
    for (int i = 0; i < bookings.length; i++) {
      list.add(bookings[i]);
    }
    return list;
  }
}