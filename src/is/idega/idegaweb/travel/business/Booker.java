package is.idega.idegaweb.travel.business;

import com.idega.presentation.IWContext;
import com.idega.presentation.ui.*;
import java.sql.*;
import com.idega.data.*;
import com.idega.data.SimpleQuerier;
import com.idega.util.idegaTimestamp;
import com.idega.block.trade.stockroom.data.*;
import com.idega.block.trade.stockroom.business.*;
import is.idega.idegaweb.travel.data.BookingEntry;
import is.idega.idegaweb.travel.data.GeneralBooking;
import com.idega.util.database.ConnectionBroker;
import java.util.*;
import is.idega.idegaweb.travel.interfaces.Booking;
import com.idega.block.trade.data.Currency;
import com.idega.idegaweb.*;
import com.idega.core.data.Address;

import com.idega.block.trade.stockroom.data.*;
import is.idega.idegaweb.travel.data.*;
import is.idega.idegaweb.travel.service.tour.data.*;
/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class Booker {

  private static String bookingPriceApplication = "bookingPriceApplication_";
  private static String bookingEntryPriceApplication = "bookingEntryPriceApplication_";

  public Booker() {
  }

  public static int BookBySupplier(int serviceId, String country, String name, String address, String city, String telephoneNumber, String email, idegaTimestamp date, int totalCount, String postalCode, int paymentType, int userId, int ownerId, int addressId) throws SQLException {
    return Book(-1, serviceId, country, name, address, city, telephoneNumber, email, date, totalCount, Booking.BOOKING_TYPE_ID_SUPPLIER_BOOKING, postalCode, paymentType, userId, ownerId, addressId);
  }

  public static int Book(int serviceId, String country, String name, String address, String city, String telephoneNumber, String email, idegaTimestamp date, int totalCount, int bookingType, String postalCode, int paymentType, int userId, int ownerId, int addressId) throws SQLException {
    return Book(-1, serviceId, country, name, address, city, telephoneNumber, email, date, totalCount, bookingType, postalCode, paymentType, userId, ownerId, addressId);
  }

  public static int updateBooking(int bookingId, int serviceId, String country, String name, String address, String city, String telephoneNumber, String email, idegaTimestamp date, int totalCount, String postalCode, int paymentType, int userId, int ownerId, int addressId) throws SQLException {
    return Book(bookingId, serviceId, country, name, address, city, telephoneNumber, email, date, totalCount, -1, postalCode, paymentType, userId, ownerId, addressId);
  }

  private static int Book(int bookingId, int serviceId, String country, String name, String address, String city, String telephoneNumber, String email, idegaTimestamp date, int totalCount, int bookingType, String postalCode, int paymentTypeId, int userId, int ownerId, int addressId) throws SQLException {
    Booking booking = null;
    int returner = bookingId;
    Object type = getServiceType(serviceId);

    if (type != null)
    if (bookingId == -1) {
      if (type instanceof Tour) booking = new GeneralBooking();
        booking.setServiceID(serviceId);
        booking.setAddress(address);
        booking.setBookingDate(date.getTimestamp());
        booking.setBookingTypeID(bookingType);
        booking.setCity(city);
        booking.setCountry(country);
        booking.setDateOfBooking(idegaTimestamp.getTimestampRightNow());
        booking.setEmail(email);
        booking.setName(name);
        booking.setPostalCode(postalCode);
        booking.setPaymentTypeId(paymentTypeId);
        booking.setTelephoneNumber(telephoneNumber);
        booking.setTotalCount(totalCount);
        booking.setUserId(userId);
        booking.setOwnerId(ownerId);
      booking.insert();

      returner =  booking.getID();
    }else {
      if (type instanceof Tour) booking = new GeneralBooking(bookingId);
        booking.setServiceID(serviceId);
        booking.setAddress(address);
        booking.setBookingDate(date.getTimestamp());
        if (bookingType != -1)
        booking.setBookingTypeID(bookingType);
        booking.setCity(city);
        booking.setCountry(country);
        booking.setDateOfBooking(idegaTimestamp.getTimestampRightNow());
        booking.setEmail(email);
        booking.setName(name);
        booking.setPostalCode(postalCode);
        booking.setPaymentTypeId(paymentTypeId);
        booking.setTelephoneNumber(telephoneNumber);
        booking.setTotalCount(totalCount);
        booking.setUserId(userId);
        booking.setOwnerId(ownerId);
      booking.update();
    }

    GeneralBooking temp = new GeneralBooking(booking.getID());
    temp.removeFrom(TravelAddress.class);
    temp.addTo(TravelAddress.class, addressId);

    return returner;
  }

  public static int getNumberOfBookingsByResellers(int serviceId, idegaTimestamp stamp) {
    return getNumberOfBookings(-1, serviceId, stamp);
  }

  public static int getNumberOfBookingsByResellers(int[] resellerIds, int serviceId, idegaTimestamp stamp) {
    int returner = 0;
    for (int i = 0; i < resellerIds.length; i++) {
      returner += getNumberOfBookings(resellerIds[i], serviceId, stamp);
    }
    return returner;
  }

  public static int getNumberOfBookingsByReseller(int resellerId, int serviceId, idegaTimestamp stamp) {
    return getNumberOfBookings(resellerId, serviceId, stamp);
  }

  private static int getNumberOfBookings(int resellerId, int serviceId, idegaTimestamp stamp) {
    if (resellerId != -1) {
      try {
        Reseller reseller = new Reseller(resellerId);
        Iterator iter = reseller.getChildren();
        List items = new Vector();
        if (iter == null) {
            int[] tempInts = {reseller.getID()};
            return getNumberOfBookings( tempInts , serviceId, stamp);
        }else {
          if (iter.hasNext()) {
            /**
             * @todo gera recusive fall sem skilar int[] af resellerId-um
             */
            while (iter.hasNext()) {
              items.add(iter.next());
            }
            int[] tempInts = new int[items.size()];
            for (int i = 0; i < tempInts.length; i++) {
              tempInts[i] = ((Reseller) items.get(i)).getID();
            }
            return getNumberOfBookings( tempInts , serviceId, stamp);
          }else {
            int[] tempInts = {reseller.getID()};
            return getNumberOfBookings( tempInts , serviceId, stamp);
          }
        }
      }catch (SQLException sql) {
        return 0;
      }
    }else {
      return getNumberOfBookings(null, serviceId, stamp);
    }
  }

/*
  private int[] getResellerIds(Reseller reseller) {
    List ids = new Vector();
    return getResellerIds(reseller, ids);
  }

  private int[] getResellerIds(Reseller reseller, List ids) {
    int[] returner = {};
    ids.add(reseller);

    Reseller tempReseller;
    Iterator iter = reseller.getChildren();
    while (iter.hasNext()) {
      tempReseller = (Reseller) iter.next();
      getResellerIds(tempReseller, ids);
    }

    return returner;
  }
*/


  public static Booking[] getBookings(int resellerId, int serviceId, idegaTimestamp stamp) {
    return getBookings(new int[] {resellerId}, serviceId, stamp);
  }

  public static Booking[] getBookings(int[] resellerIds, int serviceId, idegaTimestamp stamp) {
    Booking[] returner = {};
    try {
        if (resellerIds == null) {
          resellerIds = new int[0];
        }
        GeneralBooking booking = (GeneralBooking) (GeneralBooking.getStaticInstance(GeneralBooking.class));
        Reseller reseller = (Reseller) (Reseller.getStaticInstance(Reseller.class));

        String[] many = {};
          StringBuffer sql = new StringBuffer();
            sql.append("Select b.* from "+GeneralBooking.getBookingTableName()+" b, "+EntityControl.getManyToManyRelationShipTableName(GeneralBooking.class,Reseller.class)+" br");
            sql.append(" where ");
            if (resellerIds.length > 0 ) {
              sql.append(" br."+reseller.getIDColumnName()+" in (");
              for (int i = 0; i < resellerIds.length; i++) {
                if (i != 0) sql.append(", ");
                sql.append(resellerIds[i]);
              }

              sql.append(") and ");
            }
            sql.append(" b."+booking.getIDColumnName()+" = br."+booking.getIDColumnName());
            sql.append(" and ");
            sql.append(" b."+GeneralBooking.getIsValidColumnName()+"='Y'");
            sql.append(" and ");
            sql.append(" b."+GeneralBooking.getServiceIDColumnName()+"="+serviceId);
            sql.append(" and ");
            sql.append(" b."+GeneralBooking.getBookingDateColumnName()+" like '%"+stamp.toSQLDateString()+"%'");

        returner = (GeneralBooking[]) (GeneralBooking.getStaticInstance(GeneralBooking.class)).findAll(sql.toString());
//        returner = Integer.parseInt(many[0]);
    }catch (Exception e) {
        e.printStackTrace(System.err);
    }

    return returner;
  }


  public static int getNumberOfBookings(int[] resellerIds, int serviceId, idegaTimestamp stamp) {
    int returner = 0;
    try {
        if (resellerIds == null) {
          resellerIds = new int[0];
        }
        GeneralBooking booking = (GeneralBooking) (GeneralBooking.getStaticInstance(GeneralBooking.class));
        Reseller reseller = (Reseller) (Reseller.getStaticInstance(Reseller.class));

        String[] many = {};
          StringBuffer sql = new StringBuffer();
            sql.append("Select sum(b."+GeneralBooking.getTotalCountColumnName()+") from "+GeneralBooking.getBookingTableName()+" b, "+EntityControl.getManyToManyRelationShipTableName(GeneralBooking.class,Reseller.class)+" br");
            sql.append(" where ");
            if (resellerIds.length > 0 ) {
              sql.append(" br."+reseller.getIDColumnName()+" in (");
              for (int i = 0; i < resellerIds.length; i++) {
                if (i != 0) sql.append(", ");
                sql.append(resellerIds[i]);
              }

              sql.append(") and ");
            }
            sql.append(" b."+booking.getIDColumnName()+" = br."+booking.getIDColumnName());
            sql.append(" and ");
            sql.append(" b."+GeneralBooking.getIsValidColumnName()+"='Y'");
            sql.append(" and ");
            sql.append(" b."+GeneralBooking.getServiceIDColumnName()+"="+serviceId);
            sql.append(" and ");
            sql.append(" b."+GeneralBooking.getBookingDateColumnName()+" like '%"+stamp.toSQLDateString()+"%'");
        many = SimpleQuerier.executeStringQuery(sql.toString());

        if (many != null) {
          if (many[0] != null)
            returner = Integer.parseInt(many[0]);
        }


    }catch (Exception e) {
        e.printStackTrace(System.err);
    }

    return returner;
  }

  public static int getNumberOfBookings(int serviceId, idegaTimestamp stamp){
      return getNumberOfBookings(serviceId, stamp, null);
  }

  public static int getNumberOfBookings(int serviceId, idegaTimestamp stamp, int bookingType){
      return getNumberOfBookings(serviceId, stamp, null, bookingType);
  }

  public static int getNumberOfBookings(int serviceId, idegaTimestamp fromStamp, idegaTimestamp toStamp){
      return getNumberOfBookings(serviceId, fromStamp, toStamp, -1);
  }

  public static int getNumberOfBookings(int serviceId, idegaTimestamp fromStamp, idegaTimestamp toStamp, int bookingType){
    int returner = 0;
    //Connection conn = null;
    try {
//      Timeframe timeframe = TravelStockroomBusiness.getTimeframe(new Product(serviceId));
      Timeframe timeframe = ProductBusiness.getTimeframe(ProductBusiness.getProduct(serviceId), fromStamp);
      Product product = (Product) Product.getStaticInstance(Product.class);
      String middleTable = EntityControl.getManyToManyRelationShipTableName(Product.class, Timeframe.class);
      String pTable = Product.getProductEntityName();
      String tTable = Timeframe.getTimeframeTableName();

      //conn = ConnectionBroker.getConnection();
        String[] many = {};
          StringBuffer sql = new StringBuffer();
            sql.append("Select b."+GeneralBooking.getTotalCountColumnName()+" from "+GeneralBooking.getBookingTableName()+" b");
            sql.append(","+pTable+" p,"+middleTable+" m,"+tTable+" t");
            sql.append(" where ");
            sql.append("p."+product.getIDColumnName()+" = m."+product.getIDColumnName());
            sql.append(" and ");
            sql.append("m."+timeframe.getIDColumnName()+" = t."+timeframe.getIDColumnName());
            sql.append(" and ");
            if (timeframe != null) {
              sql.append("t."+timeframe.getIDColumnName()+" = "+timeframe.getID());
              sql.append(" and ");
            }
            sql.append("p."+product.getIDColumnName()+"="+serviceId);
            sql.append(" and ");
            sql.append("b."+GeneralBooking.getServiceIDColumnName()+"= p."+product.getIDColumnName());
            sql.append(" and ");
            sql.append("b."+GeneralBooking.getIsValidColumnName()+" = 'Y'");
            if (bookingType != -1) {
              sql.append(" and ");
              sql.append(GeneralBooking.getBookingTypeIDColumnName()+" = "+bookingType);
            }
            sql.append(" and (");
            if ( (fromStamp != null) && (toStamp == null) ) {
              sql.append(GeneralBooking.getBookingDateColumnName()+" like '"+fromStamp.toSQLDateString()+"%'");
            }else if ( (fromStamp != null) && (toStamp != null)) {
              sql.append(" (");
              sql.append(GeneralBooking.getBookingDateColumnName()+" >= '"+fromStamp.toSQLDateString()+"'");
              sql.append(" and ");
              sql.append(GeneralBooking.getBookingDateColumnName()+" <= '"+toStamp.toSQLDateString()+"')");
            }
            sql.append(" )");

        many = SimpleQuerier.executeStringQuery(sql.toString());
//        many = SimpleQuerier.executeStringQuery(sql.toString(),conn);

        for (int i = 0; i < many.length; i++) {
          returner += Integer.parseInt(many[i]);
        }

    }catch (Exception e) {
        e.printStackTrace(System.err);
    }finally {
      //ConnectionBroker.freeConnection(conn);
    }

    return returner;
  }

  public static Booking[] getBookings(List products, idegaTimestamp stamp) {
    return getBookings(products, stamp, null);
  }
  public static Booking[] getBookings(List products, idegaTimestamp fromStamp, idegaTimestamp toStamp) {
    return getBookings(products, fromStamp, toStamp, null, null);
  }
  public static Booking[] getBookings(List products, idegaTimestamp fromStamp, idegaTimestamp toStamp, String columnName, String columnValue) {
    if (products != null) {
      int[] ids = new int[products.size()];
      Product prod;
      for (int i = 0; i < ids.length; i++) {
        prod = (Product) products.get(i);
        ids[i] = prod.getID();
      }
      return getBookings(ids, fromStamp, toStamp,new int[]{}, columnName, columnValue);
    }
    return new Booking[]{};
  }

  public static Booking[] getBookings(int serviceId, idegaTimestamp stamp) {
    return getBookings(serviceId,stamp,new int[]{});
  }

  public static Booking[] getBookings(int serviceId, idegaTimestamp stamp, int bookingTypeId) {
    return getBookings(serviceId,stamp,new int[]{bookingTypeId});
  }

  public static Booking[] getBookings(int serviceId, idegaTimestamp stamp, int[] bookingTypeIds) {
    return getBookings(new int[]{serviceId}, stamp, bookingTypeIds);
  }

  public static Booking[] getBookings(int[] serviceIds, idegaTimestamp stamp, int[] bookingTypeIds) {
    return getBookings(serviceIds, stamp, null, bookingTypeIds);
  }

  public static Booking[] getBookings(int[] serviceIds, idegaTimestamp fromStamp, idegaTimestamp toStamp,int[] bookingTypeIds) {
    return getBookings(serviceIds, fromStamp, toStamp, bookingTypeIds, null, null);
  }
  public static Booking[] getBookings(int[] serviceIds, idegaTimestamp fromStamp, idegaTimestamp toStamp,int[] bookingTypeIds, String columnName, String columnValue) {
    Booking[] returner = {};
    StringBuffer sql = new StringBuffer();
    try {

        sql.append("Select * from "+GeneralBooking.getBookingTableName());
        sql.append(" where "+GeneralBooking.getServiceIDColumnName()+" in (");
        for (int i = 0; i < serviceIds.length; i++) {
          if (i > 0) sql.append(", ");
          sql.append(serviceIds[i]);
        }
        sql.append(") and ");
        sql.append(GeneralBooking.getIsValidColumnName()+" = 'Y'");
        if (fromStamp != null && toStamp == null) {
          sql.append(" and ");
          sql.append(GeneralBooking.getBookingDateColumnName()+" containing '"+fromStamp.toSQLDateString()+"'");
        }else if (fromStamp != null && toStamp != null) {
          sql.append(" and ");
          sql.append(GeneralBooking.getBookingDateColumnName()+" >= '"+fromStamp.toSQLDateString()+"'");
          sql.append(" and ");
          sql.append(GeneralBooking.getBookingDateColumnName()+" <= '"+toStamp.toSQLDateString()+"'");
        }
        if (bookingTypeIds != null) {
          if (bookingTypeIds.length > 0 ) {
            sql.append(" and (");
            for (int i = 0; i < bookingTypeIds.length; i++) {
              if (bookingTypeIds[i] != -1) {
                if (i > 0) sql.append(" OR ");
                sql.append(GeneralBooking.getBookingTypeIDColumnName()+" = "+bookingTypeIds[i]);
              }
            }
            sql.append(") ");
          }
        }
        if (columnName != null && columnValue != null) {
          sql.append(" and ").append(columnName).append(" = '").append(columnValue).append("'");
        }

        sql.append(" order by "+GeneralBooking.getBookingDateColumnName());

        returner = (GeneralBooking[]) (new GeneralBooking()).findAll(sql.toString());
    }catch (Exception e) {
        e.printStackTrace(System.err);
    }

    return returner;
  }

  public static float getBookingEntryPrice(IWContext iwc, BookingEntry entry, Booking booking) {
    String applName = bookingEntryPriceApplication+entry.getID();
      float total = 0;

      try {
        ProductPrice pPrice;
        Float temp = (Float) iwc.getApplicationAttribute(applName);
        if (temp == null) {
          pPrice = entry.getProductPrice();
          total = entry.getCount() * TravelStockroomBusiness.getPrice(pPrice.getID(), booking.getServiceID(), pPrice.getPriceCategoryID(), pPrice.getCurrencyId(), booking.getDateOfBooking());
          iwc.setApplicationAttribute(applName, new Float(total));
        }else {
          total = temp.floatValue();
        }

      }catch (SQLException sql) {
        sql.printStackTrace(System.err);
      }catch (com.idega.block.trade.stockroom.business.ProductPriceException ppe) {
        ppe.printStackTrace(System.err);
      }
      return total;
  }

  public static float getBookingPrice(IWContext iwc, List bookings) {
    float price = 0;
    Booking gBook;
    if (bookings != null) {
      for (int i = 0; i < bookings.size(); i++) {
        gBook = (Booking) bookings.get(i);
        price += getBookingPrice(iwc, gBook);
      }
    }
    return price;
  }

  public static float getBookingPrice(IWContext iwc, GeneralBooking[] bookings) {
    float price = 0;
    for (int i = 0; i < bookings.length; i++) {
      price += getBookingPrice(iwc, (Booking) bookings[i]);
    }
    return price;
  }

  public static float getBookingPrice(IWContext iwc, Booking[] bookings) {
    float price = 0;
    for (int i = 0; i < bookings.length; i++) {
      price += getBookingPrice(iwc, bookings[i]);
    }
    return price;
  }

  public static float getBookingPrice(IWContext iwc, Booking booking) {
      float total = 0;
      String applName = bookingPriceApplication+booking.getID();

      try {
        Float temp = (Float) iwc.getApplicationAttribute(applName);
        if (temp == null) {
          BookingEntry[] entries = booking.getBookingEntries();

          float price;
          ProductPrice pPrice;

          for (int i = 0; i < entries.length; i++) {
            total += getBookingEntryPrice(iwc, entries[i], booking);
          }
          iwc.setApplicationAttribute(applName, new Float(total));
        }else {
          total = temp.floatValue();
        }

      }catch (SQLException sql) {
        sql.printStackTrace(System.err);
      }
      return total;
  }

  public static void removeBookingPriceApplication(IWContext iwc, Booking booking) {
    String applName = bookingPriceApplication+booking.getID();
    String applNameEnt = bookingEntryPriceApplication+booking.getID();
    iwc.removeApplicationAttribute(applName);
    iwc.removeApplicationAttribute(applNameEnt);
  }

  public static BookingEntry[] getBookingEntries(Booking booking) {
    try {
      return booking.getBookingEntries();
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
      return null;
    }
  }

  public static boolean deleteBooking(int bookingId) {
    try {
      Booking booking = new GeneralBooking(bookingId);
      return deleteBooking(booking);
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
      return false;
    }
  }

  public static boolean deleteBooking(Booking booking) {
    try {
      booking.setIsValid(false);
      booking.update();
      return true;
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
      return false;
    }
  }

  /**
   * @todo finna betri stað
   */
  public static Object getServiceType(int serviceId) {
    Object object;
    try {
      object = new Tour(serviceId);
      return object;
    }catch (Exception e) {
      return null;
    }
  }

  public static DropdownMenu getPaymentTypes(IWResourceBundle iwrb) {
    DropdownMenu menu = getPaymentTypeDropdown(iwrb, "payment_type");
      menu.setAttribute("style","font-family: Verdana; font-size: 8pt; border: 1 solid #000000");
    return menu;
  }

  public static DropdownMenu getPaymentTypeDropdown(IWResourceBundle iwrb, String name) {
    DropdownMenu menu = new DropdownMenu(name);
      menu.addMenuElement(Booking.PAYMENT_TYPE_ID_NO_PAYMENT, iwrb.getLocalizedString("travel.unpaid","Unpaid"));
      menu.addMenuElement(Booking.PAYMENT_TYPE_ID_CASH, iwrb.getLocalizedString("travel.cash","Cash"));
      menu.addMenuElement(Booking.PAYMENT_TYPE_ID_CREDIT_CARD ,iwrb.getLocalizedString("travel.credit_card","Credit card"));
      menu.addMenuElement(Booking.PAYMENT_TYPE_ID_VOUCHER ,iwrb.getLocalizedString("travel.voucher","Voucher"));
      menu.addMenuElement(Booking.PAYMENT_TYPE_ID_ACCOUNT, iwrb.getLocalizedString("travel.account","Account"));
    return menu;
  }

  public static Currency getCurrency(Booking booking) throws SQLException {
      Currency currency = null;
      BookingEntry[] entries = booking.getBookingEntries();

      if (entries.length > 0)
        currency = new Currency(entries[0].getProductPrice().getCurrencyId());

      return currency;
  }
/*
  public static List getMultibleBookings(Booking booking) {
    List list = new Vector();
    try {
      getMultibleBookings(new GeneralBooking(booking.getID()));
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }
    return list;
  }*/

  public static List getMultibleBookings(GeneralBooking booking) {
    List list = new Vector();
    try {
      int numberOfDays = 1;
      try {
        numberOfDays = new Tour(booking.getServiceID()).getNumberOfDays();
        if (numberOfDays < 1){
          numberOfDays = 1;
        }
      }catch (SQLException sql) {
        sql.printStackTrace(System.err);
      }

      StringBuffer buff = new StringBuffer();
        buff.append("SELECT * FROM "+booking.getBookingTableName());
        buff.append(" WHERE ");
        buff.append(booking.getNameColumnName()+" = '"+booking.getName()+"'");
        buff.append(" AND ");
        buff.append(booking.getAddressColumnName()+" = '"+booking.getAddress()+"'");
        buff.append(" AND ");
        buff.append(booking.getAttendanceColumnName()+" = '"+booking.getAttendance()+"'");
        buff.append(" AND ");
        buff.append(booking.getBookingTypeIDColumnName()+" = '"+booking.getBookingTypeID()+"'");
        buff.append(" AND ");
        buff.append(booking.getCityColumnName()+" = '"+booking.getCity()+"'");
        buff.append(" AND ");
        buff.append(booking.getCountryColumnName()+" = '"+booking.getCountry()+"'");
        buff.append(" AND ");
        buff.append(booking.getEmailColumnName()+" = '"+booking.getEmail()+"'");
        buff.append(" AND ");
        if (booking.getIsValid()) {
          buff.append(booking.getIsValidColumnName()+" = 'Y'");
        }else {
          buff.append(booking.getIsValidColumnName()+" = 'N'");
        }
        buff.append(" AND ");
        buff.append(booking.getPaymentTypeIdColumnName()+" = '"+booking.getPaymentTypeId()+"'");
        buff.append(" AND ");
        buff.append(booking.getPostalCodeColumnName()+" = '"+booking.getPostalCode()+"'");
        buff.append(" AND ");
        buff.append(booking.getServiceIDColumnName()+" = '"+booking.getServiceID()+"'");
        buff.append(" AND ");
        buff.append(booking.getTelephoneNumberColumnName()+" = '"+booking.getTelephoneNumber()+"'");
        buff.append(" AND ");
        buff.append(booking.getTotalCountColumnName()+" = '"+booking.getTotalCount()+"'");
        buff.append(" ORDER BY "+booking.getBookingDateColumnName());
      list = EntityFinder.findAll(booking, buff.toString());

      if (list.size() < 2) {
        return list;
      }else {
        int myIndex = list.indexOf(booking);
        list = cleanList(list, booking, myIndex, numberOfDays);
      }



    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }
    return list;
  }

  private static List cleanList(List list, Booking booking, int mainIndex, int numberOfDays) {
    Booking book;
    int betw = 1;
    int index = mainIndex;
    boolean cont = true;


    if (mainIndex == 0) {
      while (cont) {
        ++index;
        book = (Booking) list.get(index);
        betw = idegaTimestamp.getDaysBetween(new idegaTimestamp(booking.getBookingDate()), new idegaTimestamp(book.getBookingDate()));
        if (betw != numberOfDays) {
          list = list.subList(mainIndex, index-1);
          cont = false;
        }
        if (index == list.size()-1) cont = false;
      }
    }else if (mainIndex == list.size() -1) {
      while (cont) {
        --index;
        book = (Booking) list.get(index);
        betw = idegaTimestamp.getDaysBetween(new idegaTimestamp(book.getBookingDate()), new idegaTimestamp(booking.getBookingDate()));
        if (betw != numberOfDays) {
          list = list.subList(index+1, mainIndex);
          cont = false;
        }
        if (index == 0) cont = false;
      }
    }else {
      while (cont) {
        --index;
        book = (Booking) list.get(index);
        betw = idegaTimestamp.getDaysBetween(new idegaTimestamp(book.getBookingDate()), new idegaTimestamp(booking.getBookingDate()));
        if (betw != numberOfDays) {
          list = list.subList(index+1, list.size()-1);
          cont = false;
        }

        if (index == 0) cont = false;
      }

      index = 0;
      cont = true;

      while (cont) {
        ++index;
        book = (Booking) list.get(index);
        betw = idegaTimestamp.getDaysBetween(new idegaTimestamp(booking.getBookingDate()), new idegaTimestamp(book.getBookingDate()));
        if (betw != numberOfDays) {
          list = list.subList(0, index-1);
//          list = list.subList(mainIndex, index-1);
          cont = false;
        }
        if (index == list.size()-1) cont = false;
      }
    }

    return list;
  }

  /**
   * returns int[], int[0] is number of current booking, int[1] is total bookings number
   */
  public static int[] getMultipleBookingNumber(GeneralBooking booking) {
    List list = getMultibleBookings(booking);
    int[] returner = new  int[2];
    if (list == null || list.size() < 2 ) {
      returner[0] = 0;
      returner[1] = 0;
    }else {
      returner[0] = list.indexOf(booking) + 1;
      returner[1] = list.size();
    }
    return returner;
  }

}
