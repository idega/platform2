package is.idega.idegaweb.travel.data;

import java.rmi.*;
import java.sql.*;
import java.util.*;

import javax.ejb.*;

import com.idega.block.trade.stockroom.business.*;
import com.idega.block.trade.stockroom.data.*;
import com.idega.business.*;
import com.idega.core.data.*;
import com.idega.data.*;
import com.idega.presentation.*;
import com.idega.util.*;
import is.idega.idegaweb.travel.interfaces.*;

/**
 * Title:        IW Travel
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <br><a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a><br><a href="mailto:gimmi@idega.is">Grímur Jónsson</a>
 * @version 1.0
 */


public class GeneralBookingBMPBean extends com.idega.data.GenericEntity implements is.idega.idegaweb.travel.data.GeneralBooking {

  public GeneralBookingBMPBean(){
          super();
  }

  public GeneralBookingBMPBean(int id)throws SQLException{
          super(id);
  }

  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(getNameColumnName(), "Name", true, true, String.class, 255);
    addAttribute(getTelephoneNumberColumnName(), "Símanúmer", true, true, String.class, 255);
    addAttribute(getEmailColumnName(), "Tölvupóstur", true, true, String.class, 255);
    addAttribute(getCityColumnName(), "Borg", true, true, String.class, 255);
    addAttribute(getAddressColumnName(), "Heimilisfang", true, true, String.class, 255);
    addAttribute(getBookingDateColumnName(), "Dagsetning", true, true, java.sql.Timestamp.class);
    addAttribute(getTotalCountColumnName(), "Fjöldi", true, true, Integer.class);
    addAttribute(getBookingTypeIDColumnName(), "Gerð bokunar", true, true, Integer.class);
    addAttribute(getServiceIDColumnName(), "Vara", true, true, Integer.class, "many-to-one", Service.class);
    addAttribute(getCountryColumnName(), "Land", true, true, String.class);
    addAttribute(getDateOfBookingColumnName(), "Hvenær bókun á sér stað", true, true, java.sql.Timestamp.class);
    addAttribute(getPostalCodeColumnName(), "Póstnúmer", true, true, String.class);
    addAttribute(getAttendanceColumnName(), "Mæting", true, true, Integer.class);
    addAttribute(getPaymentTypeIdColumnName(), "Gerð greiðslu", true, true, Integer.class);
    addAttribute(getIsValidColumnName(), "valid", true, true, Boolean.class);
    addAttribute(getReferenceNumberColumnName(), "reference number", true, true, String.class);
    addAttribute(getOwnerIdColumnName(), "owner id", true, true, Integer.class);
    addAttribute(getUserIdColumnName(), "user id", true, true, Integer.class);
    addAttribute(getCommentColumnName(), "comment", true, true, String.class);
    addAttribute(getCreditcardAuthorizationNumberColumnName(), "cc auth", true, true, String.class);

    this.addManyToManyRelationShip(Reseller.class);
    this.addManyToManyRelationShip(Address.class);
    this.addManyToManyRelationShip(TravelAddress.class);
  }


  public void setDefaultValues() {
      this.setIsValid(true);
      this.setAttendance(-1000);
      this.setPaymentTypeId(Booking.PAYMENT_TYPE_ID_NO_PAYMENT);
      //this.setDiscountTypeId(Booking.DISCOUNT_TYPE_ID_PERCENT);
  }


  public String getEntityName(){
    return getBookingTableName();
  }

  public String getName(){
    return getStringColumnValue(getNameColumnName());
  }

  public void setName(String name){
    setColumn(getNameColumnName(),name);
  }

  public Timestamp getBookingDate() {
    return (Timestamp) getColumnValue(getBookingDateColumnName());
  }

  public void setBookingDate(Timestamp timestamp) {
    setColumn(getBookingDateColumnName(),timestamp);
  }

  public Service getService() {
    return (Service) getColumnValue(getServiceIDColumnName());
  }

  public int getServiceID() {
    return getIntColumnValue(getServiceIDColumnName());
  }

  public void setServiceID(int id) {
    setColumn(getServiceIDColumnName(), id);
  }

  public void setCountry(String country) {
    setColumn(getCountryColumnName(), country);
  }

  public String getTelephoneNumber() {
    return getStringColumnValue(getTelephoneNumberColumnName());
  }

  public void setTelephoneNumber(String number) {
    setColumn(getTelephoneNumberColumnName(), number);
  }

  public String getEmail() {
    return getStringColumnValue(getEmailColumnName());
  }

  public void setEmail(String email) {
    setColumn(getEmailColumnName(),email);
  }

  public String getCity() {
    return getStringColumnValue(getCityColumnName());
  }

  public void setCity(String city) {
    setColumn(getCityColumnName(),city);
  }

  public String getAddress() {
    return getStringColumnValue(getAddressColumnName());
  }

  public void setAddress(String address) {
    setColumn(getAddressColumnName(),address);
  }

  public int getTotalCount() {
    return getIntColumnValue(getTotalCountColumnName());
  }

  public void setTotalCount(int totalCount) {
    setColumn(getTotalCountColumnName(),totalCount);
  }

  public int getBookingTypeID() {
    return getIntColumnValue(getBookingTypeIDColumnName());
  }

  public void setBookingTypeID(int id) {
    setColumn(getBookingTypeIDColumnName(),id);
  }

  public Timestamp getDateOfBooking() {
    return (Timestamp) getColumnValue(getDateOfBookingColumnName());
  }

  public void setDateOfBooking(Timestamp dateOfBooking) {
    setColumn(getDateOfBookingColumnName(), dateOfBooking);
  }

  public String getPostalCode() {
    return getStringColumnValue(getPostalCodeColumnName());
  }

  public void setPostalCode(String code) {
    setColumn(getPostalCodeColumnName(), code);
  }

  public String getCountry() {
    return getStringColumnValue(getCountryColumnName());
  }

  public void setAttendance(int attendance) {
    setColumn(getAttendanceColumnName(), attendance);
  }

  public int getAttendance() {
    return getIntColumnValue(getAttendanceColumnName());
  }

  public void setPaymentTypeId(int id) {
    setColumn(getPaymentTypeIdColumnName(), id);
  }

  public int getPaymentTypeId() {
    return getIntColumnValue(getPaymentTypeIdColumnName());
  }

  public boolean getIsValid() {
    return getBooleanColumnValue(getIsValidColumnName());
  }

  public void setIsValid(boolean isValid) {
    setColumn(getIsValidColumnName(), isValid);
  }

  public BookingEntry[] getBookingEntries() throws FinderException {
    try {
      return (BookingEntry[]) (is.idega.idegaweb.travel.data.BookingEntryBMPBean.getStaticInstance(BookingEntry.class).findAllByColumn(is.idega.idegaweb.travel.data.BookingEntryBMPBean.getBookingIDColumnName(), this.getID()));
    }catch (SQLException sql) {
      throw new FinderException(sql.getMessage());
    }
  }

  public void setReferenceNumber(String number) {
    setColumn(getReferenceNumberColumnName(), number);
  }

  public String getReferenceNumber() {
    return getStringColumnValue(getReferenceNumberColumnName());
  }

  public int getUserId() {
    return getIntColumnValue(getUserIdColumnName());
  }

  public void setUserId(int userId) {
    setColumn(getUserIdColumnName(), userId);
  }

  public int getOwnerId() {
    return getIntColumnValue(getOwnerIdColumnName());
  }

  public void setOwnerId(int ownerId) {
    setColumn(getOwnerIdColumnName(), ownerId);
  }

  public String getComment() {
    return getStringColumnValue(getCommentColumnName());
  }

  public void setComment(String comment) {
    setColumn(getCommentColumnName(), comment);
  }

  public String getCreditcardAuthorizationNumber() {
    return getStringColumnValue(getCreditcardAuthorizationNumberColumnName());
  }

  public void setCreditcardAuthorizationNumber(String number) {
    setColumn(getCreditcardAuthorizationNumberColumnName(), number);
  }

  public void store() {
    CypherText cyph = new CypherText();
    String key = cyph.getKey(8);

    try {
      Collection bookingIds = this.idoFindAllIDsByColumnBySQL(getReferenceNumberColumnName(), key);

      while (bookingIds.size() > 0) {
        key = cyph.getKey(8);
        bookingIds = this.idoFindAllIDsByColumnBySQL(getReferenceNumberColumnName(), key);
      }
      setReferenceNumber(key);
      super.store();
    }catch (FinderException fe) {
    throw new IDOStoreException(fe.getMessage());
    }
  }

  public static String getBookingTableName(){return "TB_BOOKING";}
  public static String getNameColumnName() {return "NAME";}
  public static String getTelephoneNumberColumnName() {return "TELEPHONE_NUMBER";}
  public static String getEmailColumnName() {return "EMAIL";}
  public static String getCityColumnName() {return "CITY";}
  public static String getAddressColumnName() {return "ADDRESS";}
  public static String getBookingDateColumnName() {return "BOOKING_DATE";}
  public static String getBookingTypeIDColumnName() {return "BOOKING_TYPE_ID";}
  public static String getTotalCountColumnName() {return "TOTAL_COUNT";}
  public static String getServiceIDColumnName() {return "TB_SERVICE_ID";}
  public static String getCountryColumnName() {return "COUNTRY";}
  public static String getDateOfBookingColumnName() {return "DATE_OF_BOOKING";}
  public static String getPostalCodeColumnName() {return "POSTAL_CODE";}
  public static String getAttendanceColumnName() {return "ATTENDANCE";}
  public static String getPaymentTypeIdColumnName() {return "PAYMENT_TYPE";}
  public static String getIsValidColumnName() {return "IS_VALID";}
  public static String getReferenceNumberColumnName() {return "REFERENCE_NUMBER";}
  public static String getOwnerIdColumnName() {return "OWNER_ID";}
  public static String getUserIdColumnName() {return "IC_USER_ID";}
  public static String getCommentColumnName() {return "BK_COMMENT";}
  public static String getCreditcardAuthorizationNumberColumnName() {return "CC_AUTH_NUMBER";}


  public  Collection ejbFindBookings(int resellerId, int serviceId, IWTimestamp stamp) throws FinderException{
    return ejbFindBookings(new int[] {resellerId}, serviceId, stamp);
  }

  public  Collection ejbFindBookings(int[] resellerIds, int serviceId, IWTimestamp stamp) throws FinderException{
    Collection returner = null;

    if (resellerIds == null) {
      resellerIds = new int[0];
    }
    Reseller reseller = (Reseller) (com.idega.block.trade.stockroom.data.ResellerBMPBean.getStaticInstance(Reseller.class));

    String[] many = {};
      StringBuffer sql = new StringBuffer();
        sql.append("Select b.* from "+getBookingTableName()+" b, "+EntityControl.getManyToManyRelationShipTableName(GeneralBooking.class,Reseller.class)+" br");
        sql.append(" where ");
        if (resellerIds.length > 0 ) {
          sql.append(" br."+reseller.getIDColumnName()+" in (");
          for (int i = 0; i < resellerIds.length; i++) {
            if (i != 0) sql.append(", ");
            sql.append(resellerIds[i]);
          }

          sql.append(") and ");
        }
        sql.append(" b."+getIDColumnName()+" = br."+this.getIDColumnName());
        sql.append(" and ");
        sql.append(" b."+getIsValidColumnName()+"='Y'");
        sql.append(" and ");
        sql.append(" b."+getServiceIDColumnName()+"="+serviceId);
        sql.append(" and ");
        sql.append(" b."+getBookingDateColumnName()+" like '%"+stamp.toSQLDateString()+"%'");

    returner = this.idoFindPKsBySQL(sql.toString());

    return returner;
  }

  public int ejbHomeGetNumberOfBookings(int[] resellerIds, int serviceId, IWTimestamp stamp) {
    return ejbHomeGetNumberOfBookings(resellerIds, serviceId, stamp, null);
  }

  public int ejbHomeGetNumberOfBookings(int[] resellerIds, int serviceId, IWTimestamp stamp, Collection travelAddressIds) {
    int returner = 0;
    try {
        if (resellerIds == null) {
          resellerIds = new int[0];
        }
        Reseller reseller = (Reseller) (com.idega.block.trade.stockroom.data.ResellerBMPBean.getStaticInstance(Reseller.class));
        String addressMiddleTable = EntityControl.getManyToManyRelationShipTableName(GeneralBooking.class, TravelAddress.class);

        String[] many = {};
          StringBuffer sql = new StringBuffer();
            sql.append("Select sum(b."+getTotalCountColumnName()+") from "+getBookingTableName()+" b, "+EntityControl.getManyToManyRelationShipTableName(GeneralBooking.class,Reseller.class)+" br");
            if (travelAddressIds != null) {
              sql.append(", "+addressMiddleTable+" am");
            }

            sql.append(" where ");
            if (travelAddressIds != null) {
              sql.append("am."+getIDColumnName()+" = b."+getIDColumnName());
              sql.append(" and ");
              sql.append("am."+TravelAddressBMPBean.getTravelAddressTableName()+"_ID in (");
              Iterator iter = travelAddressIds.iterator();
              while (iter.hasNext()) {
                Object item = iter.next();
                sql.append(item.toString());
                if (iter.hasNext()) {
                  sql.append(", ");
                }
              }
              sql.append(") and ");
            }

//            sql.append(" where ");
            if (resellerIds.length > 0 ) {
              sql.append(" br."+reseller.getIDColumnName()+" in (");
              for (int i = 0; i < resellerIds.length; i++) {
                if (i != 0) sql.append(", ");
                sql.append(resellerIds[i]);
              }

              sql.append(") and ");
            }
            sql.append(" b."+getIDColumnName()+" = br."+getIDColumnName());
            sql.append(" and ");
            sql.append(" b."+getIsValidColumnName()+"='Y'");
            sql.append(" and ");
            sql.append(" b."+getServiceIDColumnName()+"="+serviceId);
            sql.append(" and ");
            sql.append(" b."+getBookingDateColumnName()+" like '%"+stamp.toSQLDateString()+"%'");
        many = SimpleQuerier.executeStringQuery(sql.toString());

        if (many != null && many.length > 0) {
          if (many[0] != null)
            returner = Integer.parseInt(many[0]);
        }


    }catch (Exception e) {
        e.printStackTrace(System.err);
    }

    return returner;
  }

  public int ejbHomeGetNumberOfBookings(int serviceId, IWTimestamp fromStamp, IWTimestamp toStamp, int bookingType, int[] productPriceIds){
    return ejbHomeGetNumberOfBookings(serviceId, fromStamp, toStamp, bookingType, productPriceIds, null);
  }

  public int ejbHomeGetNumberOfBookings(int serviceId, IWTimestamp fromStamp, IWTimestamp toStamp, int bookingType, int[] productPriceIds, Collection travelAddressIds){
    return ejbHomeGetNumberOfBookings(serviceId, fromStamp, toStamp, bookingType, productPriceIds, travelAddressIds, false);
  }

  public int ejbHomeGetNumberOfBookingsByDateOfBooking(int serviceId, IWTimestamp fromStamp, IWTimestamp toStamp, int bookingType, int[] productPriceIds, Collection travelAddressIds){
    return ejbHomeGetNumberOfBookings(serviceId, fromStamp, toStamp, bookingType, productPriceIds, travelAddressIds, true);
  }

  public int ejbHomeGetNumberOfBookings(int serviceId, IWTimestamp fromStamp, IWTimestamp toStamp, int bookingType, int[] productPriceIds, Collection travelAddressIds, boolean useDateOfBookingColumn){
    int returner = 0;
    StringBuffer sql = new StringBuffer();
    //Connection conn = null;
    try {
//      Timeframe timeframe = TravelStockroomBusiness.getTimeframe(((com.idega.block.trade.stockroom.data.ProductHome)com.idega.data.IDOLookup.getHomeLegacy(Product.class)).findByPrimaryKeyLegacy(serviceId));

      /** @todo lonsa við getInstance crap */
//      ProductBusiness pBus = new ProductBusiness();//(ProductBusiness) IBOLookup.getServiceInstance(IWContext.getInstance(), ProductBusiness.class);
      ProductBusiness pBus = (ProductBusiness) IBOLookup.getServiceInstance(IWContext.getInstance(), ProductBusiness.class);
      Timeframe timeframe = pBus.getTimeframe(pBus.getProduct(serviceId), fromStamp);
//      Product product = (Product) com.idega.block.trade.stockroom.data.ProductBMPBean.getStaticInstance(Product.class);
      String middleTable = EntityControl.getManyToManyRelationShipTableName(Product.class, Timeframe.class);
      String addressMiddleTable = EntityControl.getManyToManyRelationShipTableName(GeneralBooking.class, TravelAddress.class);

      String pTable = com.idega.block.trade.stockroom.data.ProductBMPBean.getProductEntityName();
      String tTable = com.idega.block.trade.stockroom.data.TimeframeBMPBean.getTimeframeTableName();

      //conn = ConnectionBroker.getConnection();
      String dateColumn = this.getBookingDateColumnName();
      if (useDateOfBookingColumn) {
        dateColumn = this.getDateOfBookingColumnName();
      }

        String[] many = {};
            sql.append("Select b."+getTotalCountColumnName()+" from "+getBookingTableName()+" b,"+pTable+" p");
            if (timeframe != null) {
              sql.append(","+middleTable+" m,"+tTable+" t");
            }
            if (travelAddressIds != null) {
              sql.append(", "+addressMiddleTable+" am");
            }

            sql.append(" where ");
            if (travelAddressIds != null) {
              sql.append("am."+getIDColumnName()+" = b."+getIDColumnName());
              sql.append(" and ");
              sql.append("am."+TravelAddressBMPBean.getTravelAddressTableName()+"_ID in (");
              Iterator iter = travelAddressIds.iterator();
              while (iter.hasNext()) {
                Object item = iter.next();
                sql.append(item.toString());
                if (iter.hasNext()) {
                  sql.append(", ");
                }
              }
              sql.append(") and ");
            }

            if (timeframe != null) {
              sql.append("p."+ProductBMPBean.getIdColumnName()+" = m."+ProductBMPBean.getIdColumnName());
              sql.append(" and ");
              sql.append("m."+timeframe.getIDColumnName()+" = t."+timeframe.getIDColumnName());
              sql.append(" and ");
              sql.append("t."+timeframe.getIDColumnName()+" = "+timeframe.getID());
              sql.append(" and ");
            }
            sql.append("p."+ProductBMPBean.getIdColumnName()+"="+serviceId);
            sql.append(" and ");
            sql.append("b."+getServiceIDColumnName()+"= p."+ProductBMPBean.getIdColumnName());
            sql.append(" and ");
            sql.append("b."+getIsValidColumnName()+" = 'Y'");
            if (bookingType != -1) {
              sql.append(" and ");
              sql.append(getBookingTypeIDColumnName()+" = "+bookingType);
            }
            sql.append(" and (");
            if ( (fromStamp != null) && (toStamp == null) ) {
              sql.append(dateColumn+" like '"+fromStamp.toSQLDateString()+"%'");
            }else if ( (fromStamp != null) && (toStamp != null)) {
              sql.append(" (");
              sql.append(dateColumn+" >= '"+fromStamp.toSQLDateString()+"'");
              sql.append(" and ");
              sql.append(dateColumn+" <= '"+toStamp.toSQLDateString()+"')");
            }
            sql.append(" )");


//            System.out.println(sql.toString());
        many = SimpleQuerier.executeStringQuery(sql.toString());
//        many = SimpleQuerier.executeStringQuery(sql.toString(),conn);

        for (int i = 0; i < many.length; i++) {
          returner += Integer.parseInt(many[i]);
        }

    }catch (Exception e) {
        System.err.println(sql.toString());
        e.printStackTrace(System.err);
    }finally {
      //ConnectionBroker.freeConnection(conn);
    }

    return returner;
  }

  public Collection ejbFindBookings(int[] serviceIds, IWTimestamp fromStamp, IWTimestamp toStamp,int[] bookingTypeIds, String columnName, String columnValue, TravelAddress address) throws FinderException, RemoteException{
    return ejbFindBookings(serviceIds, fromStamp, toStamp, bookingTypeIds, columnName, columnValue, address,getBookingDateColumnName() );
  }

  public Collection ejbFindBookingsByDateOfBooking(int[] serviceIds, IWTimestamp fromStamp, IWTimestamp toStamp,int[] bookingTypeIds, String columnName, String columnValue, TravelAddress address) throws FinderException, RemoteException{
    return ejbFindBookings(serviceIds, fromStamp, toStamp, bookingTypeIds, columnName, columnValue, address,this.getDateOfBookingColumnName() );
  }

  private Collection ejbFindBookings(int[] serviceIds, IWTimestamp fromStamp, IWTimestamp toStamp,int[] bookingTypeIds, String columnName, String columnValue, TravelAddress address, String dateColumn) throws FinderException, RemoteException{
    Collection returner = null;

    if (serviceIds.length == 0) {
      return new Vector();
    }
    StringBuffer sql = new StringBuffer();


    String addressMiddleTable = "";
    boolean useAddress = false;
    if (address != null) {
      addressMiddleTable = EntityControl.getManyToManyRelationShipTableName(GeneralBookingBMPBean.class, TravelAddress.class);
      useAddress = true;
    }

    sql.append("Select * from "+getBookingTableName()+" b");
    if (useAddress) {
      sql.append(" , "+addressMiddleTable+" am");
    }
    sql.append(" where b."+getServiceIDColumnName()+" in (");
    for (int i = 0; i < serviceIds.length; i++) {
      if (i > 0) sql.append(", ");
      sql.append(serviceIds[i]);
    }
    sql.append(") and ");
    if (useAddress) {
      sql.append("am."+TravelAddressBMPBean.getTravelAddressTableName()+"_id = "+address.getPrimaryKey().toString());
      sql.append(" and ");
      sql.append("b."+this.getIDColumnName() +" = am."+this.getIDColumnName());
      sql.append(" and ");
    }
    sql.append("b."+getIsValidColumnName()+" = 'Y'");
    if (fromStamp != null && toStamp == null) {
      sql.append(" and ");
      sql.append("b."+dateColumn+" like '"+fromStamp.toSQLDateString()+"%'");
    }else if (fromStamp != null && toStamp != null) {
      sql.append(" and ");
      sql.append("b."+dateColumn+" >= '"+fromStamp.toSQLDateString()+"'");
      sql.append(" and ");
      sql.append("b."+dateColumn+" <= '"+toStamp.toSQLDateString()+"'");
    }
    if (bookingTypeIds != null) {
      if (bookingTypeIds.length > 0 ) {
        sql.append(" and (");
        for (int i = 0; i < bookingTypeIds.length; i++) {
          if (bookingTypeIds[i] != -1) {
            if (i > 0) sql.append(" OR ");
            sql.append("b."+getBookingTypeIDColumnName()+" = "+bookingTypeIds[i]);
          }
        }
        sql.append(") ");
      }
    }
    if (columnName != null && columnValue != null) {
      sql.append(" and ").append("b."+columnName).append(" = '").append(columnValue).append("'");
    }

    if (dateColumn != null)  {
      sql.append(" order by "+dateColumn);
    }

    returner = this.idoFindPKsBySQL(sql.toString());
    //returner = (GeneralBooking[]) (((is.idega.idegaweb.travel.data.GeneralBookingHome)com.idega.data.IDOLookup.getHomeLegacy(GeneralBooking.class)).createLegacy()).findAll(sql.toString());

    return returner;
  }

  public List ejbHomeGetMultibleBookings(GeneralBooking booking) throws RemoteException, FinderException{
    List list = new Vector();

    try {
      StringBuffer buff = new StringBuffer();
        buff.append("SELECT * FROM "+getBookingTableName());
        buff.append(" WHERE ");
        buff.append(getNameColumnName()+" = '"+booking.getName()+"'");
        buff.append(" AND ");
        buff.append(getAddressColumnName()+" = '"+booking.getAddress()+"'");
        buff.append(" AND ");
        buff.append(getAttendanceColumnName()+" = '"+booking.getAttendance()+"'");
        buff.append(" AND ");
        buff.append(getBookingTypeIDColumnName()+" = '"+booking.getBookingTypeID()+"'");
        buff.append(" AND ");
        buff.append(getCityColumnName()+" = '"+booking.getCity()+"'");
        buff.append(" AND ");
        buff.append(getCountryColumnName()+" = '"+booking.getCountry()+"'");
        buff.append(" AND ");
        buff.append(getEmailColumnName()+" = '"+booking.getEmail()+"'");
        buff.append(" AND ");
        if (booking.getIsValid()) {
          buff.append(getIsValidColumnName()+" = 'Y'");
        }else {
          buff.append(getIsValidColumnName()+" = 'N'");
        }
        buff.append(" AND ");
        buff.append(getPaymentTypeIdColumnName()+" = '"+booking.getPaymentTypeId()+"'");
        buff.append(" AND ");
        buff.append(getPostalCodeColumnName()+" = '"+booking.getPostalCode()+"'");
        buff.append(" AND ");
        buff.append(getServiceIDColumnName()+" = '"+booking.getServiceID()+"'");
        buff.append(" AND ");
        buff.append(getTelephoneNumberColumnName()+" = '"+booking.getTelephoneNumber()+"'");
        buff.append(" AND ");
        buff.append(getTotalCountColumnName()+" = '"+booking.getTotalCount()+"'");
        buff.append(" ORDER BY "+getBookingDateColumnName());
      //coll = this.idoFindPKsBySQL(buff.toString());
      list = EntityFinder.getInstance().findAll(GeneralBooking.class, buff.toString());
    }catch (FinderException fe) {
      System.err.println("[GeneralBookingBMPBean] Error in sql : getting multiple bookings for bookingId : "+booking.getID());
      fe.printStackTrace(System.err);
      list.add(booking);
    }

    /*
    if (list.size() < 2) {
      return list;
    }else {
      int myIndex = list.indexOf(booking);
      list = cleanList(list, booking, myIndex, numberOfDays);
    }*/

    return list;
  }

  public void removeAllTravelAddresses() throws IDORemoveRelationshipException{
    this.idoRemoveFrom(TravelAddress.class);
  }

  public void addTravelAddress(TravelAddress tAddress) throws IDOAddRelationshipException{
    this.idoAddTo(tAddress);
  }

  public Collection getTravelAddresses() throws IDORelationshipException {
    return this.idoGetRelatedEntities(TravelAddress.class);
  }

  public void setPrimaryKey(Object primaryKey) {
    super.setPrimaryKey(primaryKey);
  }

  public void removeFromReseller(Reseller reseller) throws IDORemoveRelationshipException {
    super.idoRemoveFrom(reseller);
  }

  public void removeFromAllResellers() throws IDORemoveRelationshipException {
    super.idoRemoveFrom(Reseller.class);
  }

  public void addToReseller(Reseller reseller) throws IDOAddRelationshipException {
    super.idoAddTo(reseller);
  }

  public Reseller getReseller() throws RemoteException, IDORelationshipException, FinderException{
    Collection coll = super.idoGetRelatedEntities(Reseller.class);
    if (coll != null && coll.size() > 0) {
      Iterator iter = coll.iterator();
      return ((ResellerHome) IDOLookup.getHome(Reseller.class)).findByPrimaryKey(iter.next());
    }else {
      throw new FinderException("Booking not connected to any reseller");
    }
  }

}

