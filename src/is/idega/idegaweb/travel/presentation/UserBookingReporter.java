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

  private static final String PARAMETER_ORDER_BY = "ubr_par_order_by";
  private static final String PARAMETER_USER_ID = "ubr_par_user_id";
  private static final String PARAMETER_OWNER_ID = "ubr_par_owner_id";

  private IWBundle bundle;
  private IWResourceBundle iwrb;
  private int orderBy = BookingComparator.USER;
  private int userId = -1;
  private int ownerId = -1;

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

    String sOrderBy = iwc.getParameter(PARAMETER_ORDER_BY);
    if (sOrderBy != null) {
      orderBy = Integer.parseInt(sOrderBy);
    }
    String sUserId = iwc.getParameter(PARAMETER_USER_ID);
    if (sUserId != null) {
      userId = Integer.parseInt(sUserId);
    }
    String sOwnerId = iwc.getParameter(PARAMETER_OWNER_ID);
    if (sOwnerId != null) {
      ownerId = Integer.parseInt(sOwnerId);
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

    if (userId != -1) {
      return getUserReport(iwc, products, fromStamp, toStamp);
    }else if (ownerId != -1) {
      return getOwnerReport(iwc, products, fromStamp, toStamp);
    }else {
      return getDefaultReport(iwc, products, fromStamp, toStamp);
    }
  }

  private Table getUserReport(IWContext iwc, List products, idegaTimestamp fromStamp, idegaTimestamp toStamp) {
    Table table = getTable();

    int row = 1;

    try {
      Product prod;
      User user = ((com.idega.core.user.data.UserHome)com.idega.data.IDOLookup.getHomeLegacy(User.class)).findByPrimaryKeyLegacy(userId);
      User owner;
      Link ownerLink;

      float price;
      float totalPrice = 0;
      int count;
      int totalCount = 0;

      Booking[] bookings = Booker.getBookings(products, fromStamp, toStamp, is.idega.idegaweb.travel.data.GeneralBookingBMPBean.getUserIdColumnName(), Integer.toString(userId));
      BookingComparator bComp = new BookingComparator(iwc, orderBy);
      bookings = bComp.sortedArray(bookings);

      table.add(getHeaderText(user.getName()), 1, row);
      table.add(getHeaderText(Text.NON_BREAKING_SPACE+"("+iwrb.getLocalizedString("travel.user","User")+")"), 1, row);
      table.mergeCells(1, row, 5, row);
      table.setRowColor(row, super.backgroundColor);

      Link dateLink = new Link(getHeaderText(iwrb.getLocalizedString("travel.date","Date")));
        dateLink.addParameter(PARAMETER_ORDER_BY, BookingComparator.DATE);
      Link countLink = new Link(getHeaderText(iwrb.getLocalizedString("travel.count","Count")));
        countLink.addParameter(PARAMETER_ORDER_BY, BookingComparator.TOTALCOUNT);
      Link userHLink = new Link(getHeaderText(iwrb.getLocalizedString("travel.user","User")));
        userHLink.addParameter(PARAMETER_ORDER_BY, BookingComparator.USER);
      Link ownerHLink = new Link(getHeaderText(iwrb.getLocalizedString("travel.owner","Owner")));
        ownerHLink.addParameter(PARAMETER_ORDER_BY, BookingComparator.OWNER);
      Link amountLink = new Link(getHeaderText(iwrb.getLocalizedString("travel.amount","Amount")));
        amountLink.addParameter(PARAMETER_ORDER_BY, BookingComparator.AMOUNT);

        addParameters(dateLink);
        addParameters(countLink);
        addParameters(userHLink);
        addParameters(ownerHLink);
        addParameters(amountLink);

      ++row;
      table.add(dateLink, 1, row);
      table.add(getHeaderText(iwrb.getLocalizedString("travel.product","Product")), 2, row);
      table.add(countLink, 3, row);
      table.add(ownerHLink, 4, row);
      table.add(amountLink, 5, row);
      table.setRowColor(row, super.backgroundColor);

      for (int i = 0; i < bookings.length; i++) {
        ++row;
        owner = ((com.idega.core.user.data.UserHome)com.idega.data.IDOLookup.getHomeLegacy(User.class)).findByPrimaryKeyLegacy(bookings[i].getOwnerId());
        prod = ProductBusiness.getProduct(bookings[i].getServiceID());
        price = Booker.getBookingPrice(iwc, bookings[i]);
        count = bookings[i].getTotalCount();
        totalPrice += price;
        totalCount += count;

        ownerLink = new Link(getText(owner.getName()));
          ownerLink.addParameter(PARAMETER_OWNER_ID, owner.getID());

        table.add(getText(new idegaTimestamp(bookings[i].getBookingDate()).getLocaleDate(_locale)), 1, row);
        table.add(getText(ProductBusiness.getProductName(prod, _localeId)), 2, row);
        table.add(getText(Integer.toString(count)), 3, row);
        table.add(ownerLink, 4, row);
        table.add(getText(TextSoap.decimalFormat(price, 0)), 5, row);

        table.setRowColor(row, super.GRAY);
      }
      ++row;
      Text totalPriceText = getText(TextSoap.decimalFormat(totalPrice, 0));
        totalPriceText.setBold(true);
      table.add(totalPriceText, 5, row);
      Text totalCountText = getText(Integer.toString(totalCount));
        totalCountText.setBold(true);
      table.add(totalCountText, 3, row);
      table.setRowColor(row, super.GRAY);

    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }

    table.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_CENTER);
    table.setColumnAlignment(5, Table.HORIZONTAL_ALIGN_RIGHT);
    return table;
  }

  private Table getOwnerReport(IWContext iwc, List products, idegaTimestamp fromStamp, idegaTimestamp toStamp) {
    Table table = getTable();
    int row = 1;

    try {
      Product prod;
      User user;
      User owner = ((com.idega.core.user.data.UserHome)com.idega.data.IDOLookup.getHomeLegacy(User.class)).findByPrimaryKeyLegacy(ownerId);
      Link userLink;
      float price;
      float totalPrice = 0;
      int count;
      int totalCount = 0;

      Booking[] bookings = Booker.getBookings(products, fromStamp, toStamp, is.idega.idegaweb.travel.data.GeneralBookingBMPBean.getOwnerIdColumnName(), Integer.toString(ownerId));
      BookingComparator bComp = new BookingComparator(iwc, orderBy);
      bookings = bComp.sortedArray(bookings);

      table.add(getHeaderText(owner.getName()), 1, row);
      table.add(getHeaderText(Text.NON_BREAKING_SPACE+"("+iwrb.getLocalizedString("travel.owner","Owner")+")"), 1, row);
      table.mergeCells(1, row, 5, row);
      table.setRowColor(row, super.backgroundColor);

      Link dateLink = new Link(getHeaderText(iwrb.getLocalizedString("travel.date","Date")));
        dateLink.addParameter(PARAMETER_ORDER_BY, BookingComparator.DATE);
      Link countLink = new Link(getHeaderText(iwrb.getLocalizedString("travel.count","Count")));
        countLink.addParameter(PARAMETER_ORDER_BY, BookingComparator.TOTALCOUNT);
      Link userHLink = new Link(getHeaderText(iwrb.getLocalizedString("travel.user","User")));
        userHLink.addParameter(PARAMETER_ORDER_BY, BookingComparator.USER);
      Link ownerHLink = new Link(getHeaderText(iwrb.getLocalizedString("travel.owner","Owner")));
        ownerHLink.addParameter(PARAMETER_ORDER_BY, BookingComparator.OWNER);
      Link amountLink = new Link(getHeaderText(iwrb.getLocalizedString("travel.amount","Amount")));
        amountLink.addParameter(PARAMETER_ORDER_BY, BookingComparator.AMOUNT);

        addParameters(dateLink);
        addParameters(countLink);
        addParameters(userHLink);
        addParameters(ownerHLink);
        addParameters(amountLink);

      ++row;
      table.add(dateLink, 1, row);
      table.add(getHeaderText(iwrb.getLocalizedString("travel.product","Product")), 2, row);
      table.add(countLink, 3, row);
      table.add(userHLink, 4, row);
      table.add(amountLink, 5, row);
      table.setRowColor(row, super.backgroundColor);

      for (int i = 0; i < bookings.length; i++) {
        ++row;
        user = ((com.idega.core.user.data.UserHome)com.idega.data.IDOLookup.getHomeLegacy(User.class)).findByPrimaryKeyLegacy(bookings[i].getUserId());
        prod = ProductBusiness.getProduct(bookings[i].getServiceID());
        price = Booker.getBookingPrice(iwc, bookings[i]);
        count = bookings[i].getTotalCount();
        totalPrice += price;
        totalCount += count;

        userLink = new Link(getText(user.getName()));
          userLink.addParameter(PARAMETER_USER_ID, user.getID());

        table.add(getText(new idegaTimestamp(bookings[i].getBookingDate()).getLocaleDate(_locale)), 1, row);
        table.add(getText(ProductBusiness.getProductName(prod, _localeId)), 2, row);
        table.add(getText(Integer.toString(count)), 3, row);
        table.add(userLink, 4, row);
        table.add(getText(TextSoap.decimalFormat(price, 0)), 5, row);

        table.setRowColor(row, super.GRAY);
      }
      ++row;
      Text totalPriceText = getText(TextSoap.decimalFormat(totalPrice, 0));
        totalPriceText.setBold(true);
      table.add(totalPriceText, 5, row);
      Text totalCountText = getText(Integer.toString(totalCount));
        totalCountText.setBold(true);
      table.add(totalCountText, 3, row);
      table.setRowColor(row, super.GRAY);


    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }

    table.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_CENTER);
    table.setColumnAlignment(5, Table.HORIZONTAL_ALIGN_RIGHT);
    return table;
  }


  private Table getDefaultReport(IWContext iwc, List products, idegaTimestamp fromStamp, idegaTimestamp toStamp) {

    Booking[] bookings = Booker.getBookings(products, fromStamp, toStamp);
    BookingComparator bComp = new BookingComparator(iwc, orderBy);
    bookings = bComp.sortedArray(bookings);

    Table table = getTable();
    int row = 1;

    Product prod;
    User user;
    User owner;
    Link userLink;
    Link ownerLink;

    Link dateLink = new Link(getHeaderText(iwrb.getLocalizedString("travel.date","Date")));
      dateLink.addParameter(PARAMETER_ORDER_BY, BookingComparator.DATE);
//    Link nameLink = new Link(getHeaderText(iwrb.getLocalizedString("travel.product","Product")));
//      nameLink.addParameter(PARAMETER_ORDER_BY, BookingComparator.NAME);
    Link countLink = new Link(getHeaderText(iwrb.getLocalizedString("travel.count","Count")));
      countLink.addParameter(PARAMETER_ORDER_BY, BookingComparator.TOTALCOUNT);
    Link userHLink = new Link(getHeaderText(iwrb.getLocalizedString("travel.user","User")));
      userHLink.addParameter(PARAMETER_ORDER_BY, BookingComparator.USER);
    Link ownerHLink = new Link(getHeaderText(iwrb.getLocalizedString("travel.owner","Owner")));
      ownerHLink.addParameter(PARAMETER_ORDER_BY, BookingComparator.OWNER);
    Link amountLink = new Link(getHeaderText(iwrb.getLocalizedString("travel.amount","Amount")));
      amountLink.addParameter(PARAMETER_ORDER_BY, BookingComparator.AMOUNT);

    table.add(dateLink, 1, row);
    table.add(getHeaderText(iwrb.getLocalizedString("travel.product","Product")), 2, row);
    table.add(countLink, 3, row);
    table.add(userHLink, 4, row);
    table.add(ownerHLink, 5, row);
    table.add(amountLink, 6, row);
    table.setRowColor(row, super.backgroundColor);

    for (int i = 0; i < bookings.length; i++) {
      try {
        ++row;
        user = ((com.idega.core.user.data.UserHome)com.idega.data.IDOLookup.getHomeLegacy(User.class)).findByPrimaryKeyLegacy(bookings[i].getUserId());
        owner = ((com.idega.core.user.data.UserHome)com.idega.data.IDOLookup.getHomeLegacy(User.class)).findByPrimaryKeyLegacy(bookings[i].getOwnerId());
        prod = ProductBusiness.getProduct(bookings[i].getServiceID());

        userLink = new Link(getText(user.getName()));
          userLink.addParameter(PARAMETER_USER_ID, user.getID());
        ownerLink = new Link(getText(owner.getName()));
          ownerLink.addParameter(PARAMETER_OWNER_ID, owner.getID());

        table.add(getText(new idegaTimestamp(bookings[i].getBookingDate()).getLocaleDate(_locale)), 1, row);
        table.add(getText(ProductBusiness.getProductName(prod, _localeId)), 2, row);
        table.add(getText(Integer.toString(bookings[i].getTotalCount())), 3, row);
        table.add(userLink, 4, row);
        table.add(ownerLink, 5, row);
        table.add(getText(TextSoap.decimalFormat(Booker.getBookingPrice(iwc, bookings[i]), 0)), 6, row);

        table.setRowColor(row, super.GRAY);
      }catch (SQLException sql) {
        sql.printStackTrace();
      }
    }

    table.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_CENTER);
    table.setColumnAlignment(6, Table.HORIZONTAL_ALIGN_RIGHT);

    return table;
  }


  private void addParameters(Link link) {
    if (userId != -1) {
      link.addParameter(PARAMETER_USER_ID, userId);
    }
    if (ownerId != -1) {
      link.addParameter(PARAMETER_OWNER_ID, ownerId);
    }
  }

  private Text getText(String content) {
    Text text = (Text) super.theText.clone();
      text.setText(content);
      text.setFontColor(super.BLACK);
    return text;
  }

  private Text getHeaderText(String content) {
    Text text = (Text) super.theBoldText.clone();
      text.setText(content);
    return text;
  }

  private Table getTable() {
    Table table = new Table();
      table.setColor(super.WHITE);
      table.setCellspacing(1);
      table.setCellpadding(2);
      table.setWidth("90%");
    return table;
  }

}
