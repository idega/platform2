package is.idega.idegaweb.travel.business;

import com.idega.block.trade.stockroom.business.*;
import com.idega.block.trade.stockroom.data.*;
import is.idega.idegaweb.travel.data.*;
import java.sql.Timestamp;
import com.idega.core.data.*;
import is.idega.idegaweb.travel.data.HotelPickupPlace;
import java.sql.SQLException;
import com.idega.util.*;
import java.sql.SQLException;
import com.idega.data.EntityFinder;
import com.idega.data.EntityControl;
import java.util.*;
import com.idega.util.datastructures.HashtableDoubleKeyed;
import com.idega.presentation.IWContext;
import com.idega.transaction.IdegaTransactionManager;
import javax.transaction.TransactionManager;
import java.sql.Date;
import com.idega.data.SimpleQuerier;
import com.idega.block.trade.data.Currency;

/**
 * Title:        IW Travel
 * Description:  Stockroom Business
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <br><a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a><br><a href="mailto:gimmi@idega.is">Grímur Jónsson</a>
 * @version 1.0
 */

public class TravelStockroomBusiness extends StockroomBusiness {

  private static String resellerDayHashtableSessionName = "resellerDayHashtable";
  private static String resellerDayOfWeekHashtableSessionName = "resellerDayOfWeekHashtable";
  private static String serviceDayHashtableSessionName = "serviceDayHashtable";

  protected Timeframe timeframe;

  public TravelStockroomBusiness() {
  }

  public static TravelStockroomBusiness getNewInstance(){
    return new TravelStockroomBusiness();
  }

  public void addSupplies(int product_id, float amount) {
    throw new java.lang.UnsupportedOperationException("Method addSupplies() not yet implemented.");
  }

  public void depleteSupplies(int product_id, float amount) {
    throw new java.lang.UnsupportedOperationException("Method depleteSupplies() not yet implemented.");
  }

  public void setSupplyStatus(int productId, float status, int period) throws SQLException {
    Supplies supplies = new Supplies();

    supplies.setProductId(productId);
    supplies.setCurrentSupplies(status);
    supplies.setRecordTime(idegaTimestamp.RightNow().getTimestamp());

    if(period > -1){
      supplies.setPeriod(period);
    }

    supplies.insert();
  }

  public float getSupplyStatus(int productId) throws SQLException {
    Supplies supplies = (Supplies)Supplies.getStaticInstance(Supplies.class);
    List lSupplies = EntityFinder.findAllByColumnOrdered(supplies,Supplies.getColumnNameProductId(),Integer.toString(productId),Supplies.getColumnNameRecordTime());
    if(lSupplies != null && lSupplies.size() > 0){
      return ((Supplies)lSupplies.get(0)).getCurrentSupplies();
    }else{
      return 0;
    }
  }

  public float getSupplyStatus(int productId, Date date) throws SQLException {
    Supplies supplies = (Supplies)Supplies.getStaticInstance(Supplies.class);
    List lSupplies = EntityFinder.findAll(supplies,"SELECT * FROM  "+supplies.getEntityName()+" WHERE "+Supplies.getColumnNameProductId()+" = "+Integer.toString(productId)+" AND "+Supplies.getColumnNameRecordTime()+" <= '"+date.toString()+"' ORDER BY "+ Supplies.getColumnNameRecordTime(),1);
    if(lSupplies != null && lSupplies.size() > 0){
      return ((Supplies)lSupplies.get(0)).getCurrentSupplies();
    }else{
      return 0;
    }
  }

  public int createPriceCategory(int supplierId, String name, String description, String type, String extraInfo, boolean isNetbooking) throws Exception {
      return this.createPriceCategory(supplierId, name, description,type,extraInfo, isNetbooking, -1);
  }

  public int createPriceCategory(int supplierId, String name, String description, String type, String extraInfo, boolean isNetbooking, int parentId) throws Exception {

    PriceCategory cat = new PriceCategory();

    cat.setName(name);

    if(parentId != -1) {
      cat.setParentId(parentId);
    }

    if(description != null){
      cat.setDescription(description);
    }

    if(type != null){
      cat.setType(type);
    }

    if(extraInfo != null){
      cat.setExtraInfo(extraInfo);
    }

    cat.isNetbookingCategory(isNetbooking);
    cat.setSupplierId(supplierId);

    cat.insert();


    return cat.getID();
  }


  public int createService(int supplierId, Integer fileId, String serviceName, String number, String serviceDescription, boolean isValid, int[] addressIds, Timestamp departure, Timestamp arrival, int discountTypeId) throws Exception{
    return createService(-1,supplierId, fileId, serviceName, number, serviceDescription, isValid, addressIds, departure,arrival, discountTypeId);
  }

  public int updateService(int serviceId,int supplierId, Integer fileId, String serviceName, String number, String serviceDescription, boolean isValid, int[] addressIds, Timestamp departure, Timestamp arrival, int discountTypeId) throws Exception {
    return createService(serviceId,supplierId, fileId, serviceName, number, serviceDescription, isValid, addressIds, departure,arrival, discountTypeId);
  }

  private int createService(int serviceId,int supplierId, Integer fileId, String serviceName, String number, String serviceDescription, boolean isValid, int[] addressIds, Timestamp departure, Timestamp arrival, int discountTypeId) throws Exception{
//    TransactionManager transaction = IdegaTransactionManager.getInstance();
    try{
      //transaction.begin();

      int id = -1;
      if (serviceId == -1) {
        id = StockroomBusiness.createProduct(supplierId,fileId,serviceName,number,serviceDescription,isValid, addressIds, discountTypeId);
      }else {
        id = StockroomBusiness.updateProduct(serviceId, supplierId,fileId,serviceName,number,serviceDescription,isValid, addressIds, discountTypeId);
      }

        Service service = new Service();
        service.setID(id);

        service.setDepartureTime(departure);
        service.setAttivalTime(arrival);

      if (serviceId == -1) {
        service.insert();
      }else {
        service.update();
      }


        if (timeframe != null) {
          try {
            Product product = ProductBusiness.getProduct(id);
            product.addTo(timeframe);
          }catch (SQLException sql) {
            //sql.printStackTrace(System.err);
          }
        }else {
          System.err.println("Timeframe is null");
        }
        //transaction.commit();

      return id;
    }catch(SQLException e){
      //transaction.rollback();
      e.printStackTrace(System.err);
      throw new RuntimeException("IWE226TB89");
    }
  }



  public void setTimeframe(idegaTimestamp from, idegaTimestamp to, boolean yearly) throws SQLException {
    setTimeframe(-1,from,to,yearly);
  }

  public void setTimeframe(int timeframeId, idegaTimestamp from, idegaTimestamp to, boolean yearly) throws SQLException {
    if (timeframeId != -1) {
      if ((from != null) && (to != null)) {
        timeframe = new Timeframe(timeframeId);
          timeframe.setTo(to.getTimestamp());
          timeframe.setFrom(from.getTimestamp());
          timeframe.setYearly(yearly);
          timeframe.update();
      }
    }else {
      if ((from != null) && (to != null)) {
        timeframe = new Timeframe();
          timeframe.setTo(to.getTimestamp());
          timeframe.setFrom(from.getTimestamp());
          timeframe.setYearly(yearly);
          timeframe.insert();
      }
    }
  }

  /**
   * @deprecated
   */
  public Product[] getProducts(Reseller reseller) {
    Product[] returner = {};
    try {
      Reseller parent = (Reseller) reseller.getParentEntity();
      if (parent != null) {

      }
    }catch (Exception sql) {
      sql.printStackTrace(System.err);
    }

    return returner;
  }

  /**
   * @deprecated
   */
  public Product[] getProducts(int supplierId) {
    List list = ProductBusiness.getProducts(supplierId);
    if (list == null) {
      return new Product[]{};
    }else {
      return (Product[]) list.toArray(new Product[]{});
    }
  }

  /**
   * @deprecated
   */
  public Product[] getProducts(int supplierId, idegaTimestamp stamp) {
    List list = ProductBusiness.getProducts(supplierId, stamp);
    if (list == null) {
      return new Product[]{};
    }else {
      return (Product[]) list.toArray(new Product[]{});
    }
  }

  /**
   * @deprecated
   */
  public Product[] getProducts(int supplierId, idegaTimestamp from, idegaTimestamp to) {
    List list = ProductBusiness.getProducts(supplierId, from, to);
    if (list == null) {
      return new Product[]{};
    }else {
      return (Product[]) list.toArray(new Product[]{});
    }
  }


  public static Service getService(Product product) throws ServiceNotFoundException {
    Service service = null;
    try {
      service = new Service(product.getID());
    }
    catch (SQLException sql) {
      throw new ServiceNotFoundException();
    }
    return service;
  }

  public static Timeframe getTimeframe(Product product) throws ServiceNotFoundException, TimeframeNotFoundException {
    Timeframe timeFrame = null;
    try {
//      Service service = TravelStockroomBusiness.getService(product);
      timeFrame = product.getTimeframe();
    }
    catch (SQLException sql) {
      throw new TimeframeNotFoundException();
    }/*
    catch (ServiceNotFoundException snf) {
      throw new ServiceNotFoundException();
    }*/
    return timeFrame;
  }


  public static HotelPickupPlace[] getHotelPickupPlaces(Service service) {
    HotelPickupPlace[] returner = null;
    try {
        HotelPickupPlace hp = (HotelPickupPlace) HotelPickupPlace.getStaticInstance(HotelPickupPlace.class);

        StringBuffer buffer = new StringBuffer();
          buffer.append("select h.* from ");
          buffer.append(Service.getServiceTableName()+" s,");
          buffer.append(com.idega.data.EntityControl.getManyToManyRelationShipTableName(Service.class,HotelPickupPlace.class)+" smh, ");
          buffer.append(HotelPickupPlace.getHotelPickupPlaceTableName() +" h ");
          buffer.append(" WHERE ");
          buffer.append("s."+service.getIDColumnName()+" = "+service.getID());
          buffer.append(" AND ");
          buffer.append("s."+service.getIDColumnName()+" = smh."+service.getIDColumnName());
          buffer.append(" AND ");
          buffer.append(" smh."+hp.getIDColumnName()+" = h."+hp.getIDColumnName());
          buffer.append(" AND ");
          buffer.append(HotelPickupPlace.getDeletedColumnName() +" = 'N'");
          buffer.append(" ORDER BY "+HotelPickupPlace.getNameColumnName());


        returner = (HotelPickupPlace[]) hp.findAll(buffer.toString());
    }catch (SQLException sql) {
        sql.printStackTrace(System.err);
    }
    return returner;
  }

  public static HotelPickupPlace[] getHotelPickupPlaces(Supplier supplier) {
    HotelPickupPlace[] returner = {};
    try {
        HotelPickupPlace hp = (HotelPickupPlace) HotelPickupPlace.getStaticInstance(HotelPickupPlace.class);

        StringBuffer buffer = new StringBuffer();
          buffer.append("select h.* from ");
          buffer.append(Supplier.getSupplierTableName()+" s,");
          buffer.append(com.idega.data.EntityControl.getManyToManyRelationShipTableName(Supplier.class,HotelPickupPlace.class)+" smh, ");
          buffer.append(HotelPickupPlace.getHotelPickupPlaceTableName() +" h ");
          buffer.append(" WHERE ");
          buffer.append("s."+supplier.getIDColumnName()+" = "+supplier.getID());
          buffer.append(" AND ");
          buffer.append("s."+supplier.getIDColumnName()+" = smh."+supplier.getIDColumnName());
          buffer.append(" AND ");
          buffer.append(" smh."+hp.getIDColumnName()+" = h."+hp.getIDColumnName());
          buffer.append(" AND ");
          buffer.append(HotelPickupPlace.getDeletedColumnName() +" = 'N'");
          buffer.append(" ORDER BY "+HotelPickupPlace.getNameColumnName());


        returner = (HotelPickupPlace[]) hp.findAll(buffer.toString());
    }catch (SQLException sql) {
        sql.printStackTrace(System.err);
    }
    return returner;
  }

  public static PriceCategory[] getPriceCategories(int supplierId) {
    PriceCategory[] returner = {};
    try {
      returner = (PriceCategory[]) PriceCategory.getStaticInstance(PriceCategory.class).findAllByColumn(PriceCategory.getColumnNameSupplierId(),Integer.toString(supplierId), PriceCategory.getColumnNameIsValid(), "Y");
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }
    return returner;
  }


  private static HashtableDoubleKeyed getServiceDayHashtable(IWContext iwc) {
      HashtableDoubleKeyed hash = (HashtableDoubleKeyed) iwc.getSessionAttribute(serviceDayHashtableSessionName);
      if (hash == null) {
        hash =  new HashtableDoubleKeyed();
        iwc.setSessionAttribute(serviceDayHashtableSessionName, hash);
      }

      return hash;
  }

  public static boolean getIfDay(IWContext iwc,int productId, int dayOfWeek) {
      boolean returner = false;

      HashtableDoubleKeyed hash = getServiceDayHashtable(iwc);
      Object obj = hash.get(productId+"_"+dayOfWeek,"");
      if (obj == null) {
        returner = ServiceDay.getIfDay(productId, dayOfWeek);
        hash.put(productId+"_"+dayOfWeek,"",new Boolean(returner));
      }else {
        returner = ((Boolean)obj).booleanValue();
      }

      return returner;
  }

  public static boolean getIfDay(IWContext iwc, Product product, idegaTimestamp stamp) throws ServiceNotFoundException, TimeframeNotFoundException{
    return getIfDay(iwc, product, stamp, true);
  }

  public static boolean getIfDay(IWContext iwc, Product product, idegaTimestamp stamp, boolean includePast) throws ServiceNotFoundException, TimeframeNotFoundException{
      boolean isDay = false;
      String key1 = Integer.toString(product.getID());
      String key2 = stamp.toSQLDateString();

      HashtableDoubleKeyed serviceDayHash = getServiceDayHashtable(iwc);
      //Object obj = serviceDayHash.get(key1, key2);
      Object obj = null;
      if (obj == null) {

          int dayOfWeek = stamp.getDayOfWeek();
          Timeframe timeframe = TravelStockroomBusiness.getTimeframe(product);

          boolean tooEarly = false;
          if (!includePast) {
            idegaTimestamp now = idegaTimestamp.RightNow();
            idegaTimestamp tNow = new idegaTimestamp(now.getDay(), now.getMonth(), now.getYear());
            if (tNow.isLaterThan(stamp)) {
              tooEarly = true;
            }
          }

          if (!tooEarly) {
            boolean isValidWeekDay = TravelStockroomBusiness.getIfDay(iwc, product.getID(), dayOfWeek);
            if (isValidWeekDay) {
              if (isDayValid(product, stamp)) {
                isDay = true;
                serviceDayHash.put(key1, key2, new Boolean(true) );
              }
              else {
                serviceDayHash.put(key1, key2, new Boolean(false) );
              }
            }else {
                serviceDayHash.put(key1, key2, new Boolean(false) );
            }
          }
      }
      else {
        isDay = ((Boolean) obj).booleanValue();
      }
      return isDay;
  }

  private static boolean isDateValid(Contract contract, idegaTimestamp stamp) {
    idegaTimestamp theStamp= idegaTimestamp.RightNow();
      theStamp.addDays(contract.getExpireDays()-1);

    return idegaTimestamp.isInTimeframe(new idegaTimestamp(contract.getFrom()), new idegaTimestamp(contract.getTo()), stamp, false);
    /*
    if (stamp.isLaterThan(theStamp)) {
      return new idegaTimestamp(contract.getTo()).isLaterThan(stamp);
    }else {
      return false;
    }
    */

  }

  private static boolean isDayValid(Product product, idegaTimestamp stamp) {
    return isDayValid(product, null, stamp);
  }

  private static boolean isDayValid(Product product, Contract contract, idegaTimestamp stamp) {
    boolean returner = false;

    try {
      boolean goOn = false;
      if (contract == null) {
        goOn = true;
      }else {
        goOn = isDateValid(contract, stamp);
      }


      if (goOn) {
//        Service service = TravelStockroomBusiness.getService(product);
        Timeframe[] frames = product.getTimeframes();
        boolean isYearly = false;

        for (int i = 0; i < frames.length; i++) {
          isYearly = frames[i].getIfYearly();
          returner = idegaTimestamp.isInTimeframe(new idegaTimestamp(frames[i].getFrom()), new idegaTimestamp(frames[i].getTo() ), stamp, isYearly);
          if (returner) break;
        }


      }
    }catch (Exception e) {
        e.printStackTrace(System.err);
    }

    return returner;
  }

  public static HashtableDoubleKeyed getResellerDayHashtable(IWContext iwc) {
      HashtableDoubleKeyed hash = (HashtableDoubleKeyed) iwc.getSessionAttribute(resellerDayHashtableSessionName);
      if (hash == null) {
        hash =  new HashtableDoubleKeyed();
        iwc.setSessionAttribute(resellerDayHashtableSessionName, hash);
      }
      return hash;
  }

  public static void removeResellerHashtables(IWContext iwc) {
      iwc.removeSessionAttribute(resellerDayHashtableSessionName);
      iwc.removeSessionAttribute(resellerDayOfWeekHashtableSessionName);
  }

  public static void removeServiceDayHashtable(IWContext iwc) {
    iwc.removeSessionAttribute(serviceDayHashtableSessionName);
  }

  private static HashtableDoubleKeyed getResellerDayOfWeekHashtable(IWContext iwc) {
      HashtableDoubleKeyed hash = (HashtableDoubleKeyed) iwc.getSessionAttribute(resellerDayOfWeekHashtableSessionName);
      if (hash == null) {
        hash =  new HashtableDoubleKeyed();
        iwc.setSessionAttribute(resellerDayOfWeekHashtableSessionName, hash);
      }
      return hash;
  }

  public static boolean getIfExpired(Contract contract, idegaTimestamp stamp) {
    boolean returner = false;
      int daysBetween = stamp.getDaysBetween(idegaTimestamp.RightNow(), stamp);
      if (daysBetween < contract.getExpireDays()) {
        returner = true;
      }
    return returner;
  }

  public static boolean getIfDay(IWContext iwc, Contract contract, Product product, idegaTimestamp stamp) throws ServiceNotFoundException, TimeframeNotFoundException{
      boolean isDay = false;
      String key1 = Integer.toString(contract.getID());
      String key2 = stamp.toSQLDateString();

      int dayOfWeek = stamp.getDayOfWeek();
      boolean isValidWeekDay = false;
      boolean isValidServiceDay = false;


      isValidServiceDay = TravelStockroomBusiness.getIfDay(iwc,product,stamp);

      if (isValidServiceDay) {
        HashtableDoubleKeyed resellerDayOfWeekHash = getResellerDayHashtable(iwc);
        Object object = resellerDayOfWeekHash.get(key1, key2);
        if (object == null) {
            isValidWeekDay = ResellerDay.getIfDay(contract.getResellerId(),contract.getServiceId() , dayOfWeek);
            resellerDayOfWeekHash.put(key1, key2, new Boolean(isValidWeekDay));
        }else {
          isValidWeekDay = ((Boolean) object).booleanValue();
        }
      }


      HashtableDoubleKeyed resellerDayHash = getResellerDayHashtable(iwc);
      Object obj = resellerDayHash.get(key1, key2);
      if (obj == null) {
        if (isValidWeekDay) {
          idegaTimestamp from = new idegaTimestamp(contract.getFrom());
          idegaTimestamp to = new idegaTimestamp(contract.getTo());
          if (stamp.isLaterThan(from) && to.isLaterThan(stamp)  ) {
            isDay = true;
            resellerDayHash.put(key1, key2, new Boolean(true));
          }else if (stamp.toSQLDateString().equals(from.toSQLDateString()) || stamp.toSQLDateString().equals(to.toSQLDateString())) {
            isDay = true;
            resellerDayHash.put(key1, key2, new Boolean(true));
          }else {
            resellerDayHash.put(key1, key2, new Boolean(false));
          }
        }else {
          resellerDayHash.put(key1, key2, new Boolean(false));
        }
      }else {
        isDay = ((Boolean) obj).booleanValue();
      }

      return isDay;
  }


  public static class ServiceNotFoundException extends Exception{
    ServiceNotFoundException(){
      super("Service not found");
    }
  }
  public static class TimeframeNotFoundException extends Exception{
    TimeframeNotFoundException(){
      super("Timeframe not found");
    }
  }

  /**
   * @todo skoða betur
   */
  public static int getCurrencyIdForIceland(){
      Currency curr = new Currency();
      int returner = -1;
      try {
        String iceKr = "Íslenskar Krónur";
        String[] id = com.idega.data.SimpleQuerier.executeStringQuery("Select "+curr.getIDColumnName()+" from "+curr.getEntityName()+" where "+Currency.getColumnNameCurrencyName() +" = '"+iceKr+"'");
        if (id == null || id.length == 0) {
            curr = new Currency();
            curr.setCurrencyName(iceKr);
            curr.setCurrencyAbbreviation("ISK");
            curr.insert();
            returner = curr.getID();
        } else if (id.length > 0) {
          returner = Integer.parseInt(id[id.length -1]);
        }

      }
      catch (Exception e) {
        e.printStackTrace(System.err);
      }

      return returner;
  }

  public static List getDepartureDays(IWContext iwc, Product product) {
    return getDepartureDays(iwc, product, null, null, true);
  }

  public static List getDepartureDays(IWContext iwc, Product product, boolean showPast) {
    return getDepartureDays(iwc, product, null, null, showPast);
  }

  public static List getDepartureDays(IWContext iwc, Product product, idegaTimestamp from, idegaTimestamp to) {
    return getDepartureDays(iwc, product, from, to, true);
  }

  public static List getDepartureDays(IWContext iwc, Product product, idegaTimestamp fromStamp, idegaTimestamp toStamp, boolean showPast) {
    List returner = new Vector();
    try {
//      Service service = new Service(product.getID());
      Timeframe[] frames = product.getTimeframes();

      for (int j = 0; j < frames.length; j++) {
        boolean yearly = frames[j].getIfYearly();


        idegaTimestamp tFrom = new idegaTimestamp(frames[j].getFrom());
        idegaTimestamp tTo = new idegaTimestamp(frames[j].getTo());


        idegaTimestamp from = null;
        if (fromStamp != null) from = new idegaTimestamp(fromStamp);
        idegaTimestamp to = null;
        if (toStamp != null) to = new idegaTimestamp(toStamp);

        if (from == null) {
          from = new idegaTimestamp(tFrom);
        }
        if (to == null) {
          to   = new idegaTimestamp(tTo);
        }

        int toMonth = tTo.getMonth();
        int toM = to.getMonth();
        int fromM = from.getMonth();
        int yearsBetween = 0;

        to.addDays(1);

        if (yearly) {
          int fromYear = tFrom.getYear();
          int toYear   = tTo.getYear();

          int fromY = from.getYear();
          int toY = to.getYear();

          int daysBetween = idegaTimestamp.getDaysBetween(from, to);

          if (fromYear == toYear) {
            from.setYear(fromYear);
          }else {
              if (fromY >= toYear) {
                if (fromM > toMonth) {
                  from.setYear(fromYear);
                }else {
                  from.setYear(toYear);
                }
              }
          }

          to = new idegaTimestamp(from);
            to.addDays(daysBetween);

          yearsBetween = to.getYear() - toY;
        }

        idegaTimestamp stamp = new idegaTimestamp(from);
        idegaTimestamp temp;

        if (!showPast) {
          idegaTimestamp now = idegaTimestamp.RightNow();
          if (now.isLaterThan(from) && to.isLaterThan(now)) {
            stamp = new idegaTimestamp(now);
          }else if (now.isLaterThan(from) && now.isLaterThan(to)) {
            stamp = new idegaTimestamp(to);
          }
        }


        int[] weekDays = ServiceDay.getDaysOfWeek(product.getID());

        while (to.isLaterThan(stamp)) {
          for (int i = 0; i < weekDays.length; i++) {
            if (stamp.getDayOfWeek() == weekDays[i]) {
              if (yearly) stamp.addYears(-yearsBetween);
              returner.add(stamp);
              stamp = new idegaTimestamp(stamp);
              if (yearly) stamp.addYears(yearsBetween);
            }
          }
          stamp.addDays(1);
        }
      }

    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }

    return returner;
  }

  public static boolean isWithinTimeframe(Timeframe timeframe, idegaTimestamp stamp) {
    boolean yearly = timeframe.getIfYearly();
    idegaTimestamp from = new idegaTimestamp(timeframe.getFrom());
    idegaTimestamp to   = new idegaTimestamp(timeframe.getTo());
    return idegaTimestamp.isInTimeframe(from, to, stamp,yearly);
  }

}
