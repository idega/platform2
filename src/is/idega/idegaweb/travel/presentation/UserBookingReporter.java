package is.idega.idegaweb.travel.presentation;

import is.idega.idegaweb.travel.business.BookingComparator;
import is.idega.idegaweb.travel.data.GeneralBooking;
import is.idega.idegaweb.travel.interfaces.Booking;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

import javax.ejb.FinderException;

import com.idega.block.trade.stockroom.data.Product;
import com.idega.core.user.data.User;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
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

public class UserBookingReporter extends TravelManager implements Report{

  private static final String PARAMETER_ORDER_BY = "ubr_par_order_by";
  private static final String PARAMETER_USER_ID = "ubr_par_user_id";
  private static final String PARAMETER_OWNER_ID = "ubr_par_owner_id";

  private IWBundle bundle;
  private IWResourceBundle iwrb;
  private int orderBy = BookingComparator.USER;
  private int userId = -1;
  private int ownerId = -1;
  private IWTimestamp _fromStamp;
  private IWTimestamp _toStamp;

  public UserBookingReporter(IWContext iwc) throws Exception {
    initialize(iwc);
  }

  public String getReportName() {
    return iwrb.getLocalizedString("travel.report_name.user_bookings","User bookings");
  }

  public String getReportDescription() {
    return iwrb.getLocalizedString("travel.report_description.user_bookings","Displays owner and user of bookins.");
  }

  public boolean useTwoDates() {
    return true;
  }

  private void initialize(IWContext iwc) throws Exception{
    super.main(iwc);
    bundle = super.getBundle();
    iwrb = super.getResourceBundle();

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

  public PresentationObject getReport(IWContext iwc, List products, IWTimestamp stamp) throws RemoteException, FinderException{
    return getReport(iwc, products, stamp, new IWTimestamp(stamp));
  }

  public PresentationObject getReport(IWContext iwc, List products, IWTimestamp fromStamp, IWTimestamp toStamp) throws RemoteException, FinderException{
    //initialize(iwc);

    if (userId != -1) {
      return getUserReport(iwc, products, fromStamp, toStamp);
    }else if (ownerId != -1) {
      return getOwnerReport(iwc, products, fromStamp, toStamp);
    }else {
      return getDefaultReport(iwc, products, fromStamp, toStamp);
    }
  }

  private Table getUserReport(IWContext iwc, List products, IWTimestamp fromStamp, IWTimestamp toStamp) throws RemoteException, FinderException{
    Table table = getTable();
      table.setWidth("100%");
    int row = 1;

    try {
      Product prod;
      User user = ((com.idega.core.user.data.UserHome)com.idega.data.IDOLookup.getHomeLegacy(User.class)).findByPrimaryKeyLegacy(userId);
      User owner;
      Link ownerLink;
      this._fromStamp = fromStamp;
      this._toStamp = toStamp;

      float price;
      float totalPrice = 0;
      int count;
      int totalCount = 0;

      Booking[] bookings = getBooker(iwc).getBookings(products, fromStamp, toStamp, is.idega.idegaweb.travel.data.GeneralBookingBMPBean.getUserIdColumnName(), Integer.toString(userId));
      BookingComparator bComp = new BookingComparator(iwc, orderBy);
      bookings = bComp.sortedArray(bookings);

      table.add(getHeaderText(user.getName()), 1, row);
      table.add(getHeaderText(Text.NON_BREAKING_SPACE+"("+iwrb.getLocalizedString("travel.user","User")+")"), 1, row);
      table.mergeCells(1, row, 5, row);
      table.setRowColor(row, super.backgroundColor);

      Link dateLink = Reports.getReportLink(getHeaderText(iwrb.getLocalizedString("travel.date","Date")));
        dateLink.addParameter(PARAMETER_ORDER_BY, BookingComparator.DATE);
      Link countLink = Reports.getReportLink(getHeaderText(iwrb.getLocalizedString("travel.count","Count")));
        countLink.addParameter(PARAMETER_ORDER_BY, BookingComparator.TOTALCOUNT);
      Link userHLink = Reports.getReportLink(getHeaderText(iwrb.getLocalizedString("travel.user","User")));
        userHLink.addParameter(PARAMETER_ORDER_BY, BookingComparator.USER);
      Link ownerHLink = Reports.getReportLink(getHeaderText(iwrb.getLocalizedString("travel.owner","Owner")));
        ownerHLink.addParameter(PARAMETER_ORDER_BY, BookingComparator.OWNER);
      Link amountLink = Reports.getReportLink(getHeaderText(iwrb.getLocalizedString("travel.amount","Amount")));
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
        prod = getProductBusiness(iwc).getProduct(bookings[i].getServiceID());
        price = getBooker(iwc).getBookingPrice(bookings[i]);
        count = bookings[i].getTotalCount();
        totalPrice += price;
        totalCount += count;

        ownerLink = Reports.getReportLink(getText(owner.getName()));
          ownerLink.addParameter(PARAMETER_OWNER_ID, owner.getID());

        table.add(getText(new IWTimestamp(bookings[i].getBookingDate()).getLocaleDate(getLocale())), 1, row);
        table.add(getText(prod.getProductName(getLocaleId())), 2, row);
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

  private Table getOwnerReport(IWContext iwc, List products, IWTimestamp fromStamp, IWTimestamp toStamp) throws RemoteException, FinderException{
    Table table = getTable();
      table.setWidth("100%");
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

      Booking[] bookings = getBooker(iwc).getBookings(products, fromStamp, toStamp, is.idega.idegaweb.travel.data.GeneralBookingBMPBean.getOwnerIdColumnName(), Integer.toString(ownerId));
      BookingComparator bComp = new BookingComparator(iwc, orderBy);
      bookings = bComp.sortedArray(bookings);

      table.add(getHeaderText(owner.getName()), 1, row);
      table.add(getHeaderText(Text.NON_BREAKING_SPACE+"("+iwrb.getLocalizedString("travel.owner","Owner")+")"), 1, row);
      table.mergeCells(1, row, 5, row);
      table.setRowColor(row, super.backgroundColor);

      Link dateLink = Reports.getReportLink(getHeaderText(iwrb.getLocalizedString("travel.date","Date")));
        dateLink.addParameter(PARAMETER_ORDER_BY, BookingComparator.DATE);
      Link countLink = Reports.getReportLink(getHeaderText(iwrb.getLocalizedString("travel.count","Count")));
        countLink.addParameter(PARAMETER_ORDER_BY, BookingComparator.TOTALCOUNT);
      Link userHLink = Reports.getReportLink(getHeaderText(iwrb.getLocalizedString("travel.user","User")));
        userHLink.addParameter(PARAMETER_ORDER_BY, BookingComparator.USER);
      Link ownerHLink = Reports.getReportLink(getHeaderText(iwrb.getLocalizedString("travel.owner","Owner")));
        ownerHLink.addParameter(PARAMETER_ORDER_BY, BookingComparator.OWNER);
      Link amountLink = Reports.getReportLink(getHeaderText(iwrb.getLocalizedString("travel.amount","Amount")));
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
        prod = getProductBusiness(iwc).getProduct(bookings[i].getServiceID());
        price = getBooker(iwc).getBookingPrice(bookings[i]);
        count = bookings[i].getTotalCount();
        totalPrice += price;
        totalCount += count;

        userLink = Reports.getReportLink(getText(user.getName()));
          userLink.addParameter(PARAMETER_USER_ID, user.getID());

        table.add(getText(new IWTimestamp(bookings[i].getBookingDate()).getLocaleDate(getLocale())), 1, row);
        table.add(getText(prod.getProductName(getLocaleId())), 2, row);
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


  private Table getDefaultReport(IWContext iwc, List products, IWTimestamp fromStamp, IWTimestamp toStamp) throws RemoteException, FinderException{

    Booking[] bookings = getBooker(iwc).getBookings(products, fromStamp, toStamp);
    BookingComparator bComp = new BookingComparator(iwc, orderBy);
    bookings = bComp.sortedArray(bookings);

    Table table = getTable();
      table.setWidth("100%");
    int row = 1;

    Product prod;
    User user;
    User owner;
    Link userLink;
    Link ownerLink;

    Link dateLink = Reports.getReportLink(getHeaderText(iwrb.getLocalizedString("travel.date","Date")));
      dateLink.addParameter(PARAMETER_ORDER_BY, BookingComparator.DATE);
//    Link nameLink = Reports.getReportLink(getHeaderText(iwrb.getLocalizedString("travel.product","Product")));
//      nameLink.addParameter(PARAMETER_ORDER_BY, BookingComparator.NAME);
    Link countLink = Reports.getReportLink(getHeaderText(iwrb.getLocalizedString("travel.count","Count")));
      countLink.addParameter(PARAMETER_ORDER_BY, BookingComparator.TOTALCOUNT);
    Link userHLink = Reports.getReportLink(getHeaderText(iwrb.getLocalizedString("travel.user","User")));
      userHLink.addParameter(PARAMETER_ORDER_BY, BookingComparator.USER);
    Link ownerHLink = Reports.getReportLink(getHeaderText(iwrb.getLocalizedString("travel.owner","Owner")));
      ownerHLink.addParameter(PARAMETER_ORDER_BY, BookingComparator.OWNER);
    Link amountLink = Reports.getReportLink(getHeaderText(iwrb.getLocalizedString("travel.amount","Amount")));
      amountLink.addParameter(PARAMETER_ORDER_BY, BookingComparator.AMOUNT);



    table.add(dateLink, 1, row);
    table.add(getHeaderText(iwrb.getLocalizedString("travel.product","Product")), 2, row);
    table.add(countLink, 3, row);
    table.add(userHLink, 4, row);
    table.add(ownerHLink, 5, row);
    table.add(amountLink, 6, row);
    table.setRowColor(row, super.backgroundColor);

    for (int i = 0; i < bookings.length; i++) {
      user = null;
      owner = null;
      try {
        ++row;
        if (bookings[i].getUserId() != -1) {
          user = ((com.idega.core.user.data.UserHome)com.idega.data.IDOLookup.getHomeLegacy(User.class)).findByPrimaryKeyLegacy(bookings[i].getUserId());
          userLink = Reports.getReportLink(getText(user.getName()));
            userLink.addParameter(PARAMETER_USER_ID, user.getID());
          table.add(userLink, 4, row);
        }else {
          if (( (GeneralBooking) bookings[i]).getCreditcardAuthorizationNumber() != null) {
            table.add(getText(iwrb.getLocalizedString("travel.-online-","-online-")), 4, row);
          }
        }
        if (bookings[i].getOwnerId() != -1) {
          owner = ((com.idega.core.user.data.UserHome)com.idega.data.IDOLookup.getHomeLegacy(User.class)).findByPrimaryKeyLegacy(bookings[i].getOwnerId());
          ownerLink = Reports.getReportLink(getText(owner.getName()));
            ownerLink.addParameter(PARAMETER_OWNER_ID, owner.getID());
          table.add(ownerLink, 5, row);
        }else {
          if (( (GeneralBooking) bookings[i]).getCreditcardAuthorizationNumber() != null) {
            table.add(getText(iwrb.getLocalizedString("travel.-online-","-online-")), 5, row);
          }
        }
        prod = getProductBusiness(iwc).getProduct(bookings[i].getServiceID());


        table.add(getText(new IWTimestamp(bookings[i].getBookingDate()).getLocaleDate(getLocale())), 1, row);
        table.add(getText(prod.getProductName(getLocaleId())), 2, row);
        table.add(getText(Integer.toString(bookings[i].getTotalCount())), 3, row);
        table.add(getText(TextSoap.decimalFormat(getBooker(iwc).getBookingPrice(bookings[i]), 0)), 6, row);

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

/*    if (_fromStamp != null) {
      link.addParameter(Reports.PARAMATER_DATE_FROM, _fromStamp.toSQLDateString());
    }
    if (_toStamp != null) {
      link.addParameter(Reports.PARAMATER_DATE_TO, _toStamp.toSQLDateString());
    }

    for (int i = 0; i < names.size(); i++) {
      link.addParameter((String) names.get(i), (String) values.get(i));
    }*/


  }


  protected Text getText(String content) {
    Text text = (Text) super.theText.clone();
      text.setText(content);
      text.setFontColor(super.BLACK);
    return text;
  }

  protected Text getHeaderText(String content) {
    Text text = (Text) super.theBoldText.clone();
      text.setText(content);
    return text;
  }

  public static Table getTable() {
    Table table = TravelManager.getTable();
      table.setCellpadding(2);
      table.setWidth("90%");
    return table;
  }

}
