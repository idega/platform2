package is.idega.travel.business;

import com.idega.block.trade.stockroom.business.StockroomBusiness;
import com.idega.block.trade.stockroom.data.*;
import is.idega.travel.data.*;
import java.sql.Timestamp;
import com.idega.core.data.*;
import is.idega.travel.data.HotelPickupPlace;
import java.sql.SQLException;
import com.idega.util.*;
import java.sql.SQLException;
import com.idega.data.EntityFinder;
import com.idega.data.EntityControl;
import java.util.List;
import com.idega.transaction.IdegaTransactionManager;
import javax.transaction.TransactionManager;
import java.sql.Date;

/**
 * Title:        IW Travel
 * Description:  Stockroom Business
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <br><a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a><br><a href="mailto:gimmi@idega.is">Grímur Jónsson</a>
 * @version 1.0
 */

public class TravelStockroomBusiness extends StockroomBusiness {

  public static String uniqueDepartureAddressType = "TB_TRIP_DEPARTURE_ADDRESS";
  public static String uniqueArrivalAddressType = "TB_TRIP_ARRIVAL_ADDRESS";
  public static String uniqueHotelPickupAddressType = "TB_HOTEL_PICKUP_ADDRESS";

  private Timeframe timeframe;

  private TravelStockroomBusiness() {
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
    List lSupplies = EntityFinder.findAllByColumnOrdered(supplies,supplies.getColumnNameProductId(),Integer.toString(productId),Supplies.getColumnNameRecordTime());
    if(lSupplies != null && lSupplies.size() > 0){
      return ((Supplies)lSupplies.get(0)).getCurrentSupplies();
    }else{
      return 0;
    }
  }

  public float getSupplyStatus(int productId, Date date) throws SQLException {
    Supplies supplies = (Supplies)Supplies.getStaticInstance(Supplies.class);
    List lSupplies = EntityFinder.findAll(supplies,"SELECT * FROM  "+supplies.getEntityName()+" WHERE "+supplies.getColumnNameProductId()+" = "+Integer.toString(productId)+" AND "+Supplies.getColumnNameRecordTime()+" <= '"+date.toString()+"' ORDER BY "+ Supplies.getColumnNameRecordTime(),1);
    if(lSupplies != null && lSupplies.size() > 0){
      return ((Supplies)lSupplies.get(0)).getCurrentSupplies();
    }else{
      return 0;
    }
  }



  public int createPriceCategory(int supplierId, String name, String description, String type, String extraInfo, boolean isNetbooking) throws Exception {

    PriceCategory cat = new PriceCategory();

    cat.setName(name);

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



  public int createService(int supplierId, Integer fileId, String serviceName, String serviceDescription, boolean isValid, int[] addressIds, Timestamp departure, Timestamp arrival) throws Exception{


//    TransactionManager transaction = IdegaTransactionManager.getInstance();
    try{
      //transaction.begin();

      int id = StockroomBusiness.createProduct(supplierId,fileId,serviceName,serviceDescription,isValid);
      Service service = new Service();
      service.setID(id);

      service.setDepartureTime(departure);
      service.setAttivalTime(arrival);

      if(addressIds != null){
        for (int i = 0; i < addressIds.length; i++) {
          service.addTo(Address.class, addressIds[i]);
        }
      }

      if (timeframe != null) {
          service.addTo(timeframe);
      }
      service.insert();
      //transaction.commit();

      return id;
    }catch(SQLException e){
      //transaction.rollback();
      throw new RuntimeException("IWE226TB89");
    }
  }


  /**
   * @todo createTourService
   */
  public int createTourService(int supplierId, Integer fileId, String serviceName, String serviceDescription, boolean isValid, String departureFrom, idegaTimestamp departureTime, String arrivalAt, idegaTimestamp arrivalTime, String pickupPlace, idegaTimestamp pickupTime, int[] activeDays, Integer numberOfSeats) throws Exception {

      boolean isError = false;

      /**
       * @todo handle isError
       */
      if (timeframe == null) isError = true;
      if (activeDays.length == 0) isError = true;

      int departureAddressTypeId = AddressType.getId(uniqueDepartureAddressType);
      int arrivalAddressTypeId = AddressType.getId(uniqueArrivalAddressType);
      int hotelPickupAddressTypeId = AddressType.getId(uniqueHotelPickupAddressType);

      Address departureAddress = new Address();
        departureAddress.setAddressTypeID(departureAddressTypeId);
        departureAddress.setStreetName(departureFrom);
        departureAddress.insert();

      Address arrivalAddress = new Address();
        arrivalAddress.setAddressTypeID(arrivalAddressTypeId);
        arrivalAddress.setStreetName(arrivalAt);
        arrivalAddress.insert();

      Address hotelPickupAddress = new Address();
        hotelPickupAddress.setAddressTypeID(hotelPickupAddressTypeId);
        hotelPickupAddress.setStreetName(pickupPlace);
        hotelPickupAddress.insert();

      HotelPickupPlace hpp = new HotelPickupPlace();
        hpp.setName(pickupPlace);
        hpp.setAddress(hotelPickupAddress);
        hpp.insert();

      int[] departureAddressIds = {departureAddress.getID()};
      int[] arrivalAddressIds = {arrivalAddress.getID()};
      int[] hotePickupPlaceIds = {hpp.getID()};

      int serviceId = createService(supplierId, fileId, serviceName, serviceDescription, isValid, departureAddressIds, departureTime.getTimestamp(), arrivalTime.getTimestamp());

//      javax.transaction.TransactionManager tm = com.idega.transaction.IdegaTransactionManager.getInstance();
      try {
          //tm.begin();
          Service service = new Service(serviceId);

          Tour tour = new Tour();
            tour.setID(serviceId);
            tour.setTotalSeats(numberOfSeats.intValue());


          if(hotePickupPlaceIds.length > 0){
            for (int i = 0; i < hotePickupPlaceIds.length; i++) {
              service.addTo(HotelPickupPlace.class, hotePickupPlaceIds[i]);
            }
            tour.setHotelPickup(true);
            tour.setHotelPickupTime(pickupTime.getTimestamp());
          }else{
            tour.setHotelPickup(false);
          }
          tour.insert();

          if (activeDays.length > 0) {
            ServiceDay sDay;
            for (int i = 0; i < activeDays.length; i++) {
              sDay = new ServiceDay();
                sDay.setServiceId(serviceId);
                sDay.setDayOfWeek(activeDays[i]);
              sDay.insert();
            }
          }


          //tm.commit();
      }catch (Exception e) {
          e.printStackTrace(System.err);
          //tm.rollback();
      }

      return serviceId;
  }

  public void setTimeframe(idegaTimestamp from, idegaTimestamp to, boolean yearly) throws SQLException {
        if ((from != null) && (to != null)) {
          timeframe = new Timeframe();
            timeframe.setTo(to.getTimestamp());
            timeframe.setFrom(from.getTimestamp());
            timeframe.setYearly(yearly);
            timeframe.insert();
        }
  }

  public Product[] getProducts(int supplierId) {
      Product[] products ={};

      try {
        String pTable = Product.getProductEntityName();
        String sTable = Service.getServiceTableName();
        String tTable = Timeframe.getTimeframeTableName();

        StringBuffer sqlQuery = new StringBuffer();
          sqlQuery.append("SELECT "+pTable+".* FROM "+pTable+", "+tTable);
          sqlQuery.append(" WHERE "+pTable+"."+Product.getStaticInstance(Product.class).getIDColumnName()+" = "+tTable+"."+Timeframe.getStaticInstance(Timeframe.class).getIDColumnName());
          sqlQuery.append(" AND "+pTable+"."+Product.getColumnNameSupplierId()+" = "+supplierId);
          sqlQuery.append(" ORDER by "+Timeframe.getTimeframeFromColumnName());

        products = (Product[]) (new Product()).findAll(sqlQuery.toString());
      }catch(SQLException sql) {
        sql.printStackTrace(System.err);
      }

      return products;
  }

  public Product[] getProducts(int supplierId, idegaTimestamp stamp) {
      Product[] products = {};

      try {
          /**
           * @todo Oracle support...
           */
          Product[] tempProducts = this.getProducts(supplierId);

          if (tempProducts.length > 0) {
            idegaCalendar calendar = new idegaCalendar();

            int dayOfWeek = calendar.getDayOfWeek(stamp.getYear(),stamp.getMonth(),stamp.getDay());
            Timeframe timeframe = (Timeframe) Timeframe.getStaticInstance(Timeframe.class);
            Supplier supplier = (Supplier) Supplier.getStaticInstance(Supplier.class);
            Service service = (Service) Service.getStaticInstance(Service.class);
            Product producter = (Product) Product.getStaticInstance(Product.class);

            String middleTable = EntityControl.getManyToManyRelationShipTableName(Timeframe.class,Service.class);
            String Ttable = Timeframe.getTimeframeTableName();
            String Ptable = Product.getProductEntityName();
            String Stable = Supplier.getSupplierTableName();

            StringBuffer timeframeSQL = new StringBuffer();
              timeframeSQL.append("SELECT "+Ptable+".* FROM  "+Ttable+", "+Ptable+","+middleTable);
              timeframeSQL.append(" WHERE ");
              timeframeSQL.append(Ttable+"."+timeframe.getIDColumnName()+" = "+middleTable+"."+timeframe.getIDColumnName());
              timeframeSQL.append(" AND ");
              timeframeSQL.append(Ptable+"."+producter.getIDColumnName()+" = "+middleTable+"."+service.getIDColumnName());
              timeframeSQL.append(" AND ");
              timeframeSQL.append(Ptable+"."+supplier.getIDColumnName()+" = "+supplierId);
              timeframeSQL.append(" AND ");
              timeframeSQL.append(Timeframe.getTimeframeFromColumnName()+" <= '"+stamp.toSQLDateString()+"'");
              timeframeSQL.append(" AND ");
              timeframeSQL.append(Timeframe.getTimeframeToColumnName()+" >= '"+stamp.toSQLDateString()+"'");
              timeframeSQL.append(" ORDER BY "+Ttable+"."+Timeframe.getTimeframeFromColumnName()+","+Ptable+"."+Product.getColumnNameProductName());

            products = (Product[]) (new Product()).findAll(timeframeSQL.toString());

          }

      }catch(SQLException sql) {
        sql.printStackTrace(System.err);
      }


      return products;
  }

  public Product[] getProducts(int supplierId, idegaTimestamp from, idegaTimestamp to) {
      Product[] products ={};

      try {
          /**
           * @todo Oracle support...
           */
          Product[] tempProducts = this.getProducts(supplierId);
          if (tempProducts.length > 0) {

              Timeframe timeframe = (Timeframe) Timeframe.getStaticInstance(Timeframe.class);
              Service service = (Service) Service.getStaticInstance(Service.class);

              String middleTable = EntityControl.getManyToManyRelationShipTableName(Timeframe.class,Service.class);
              String Ttable = Timeframe.getTimeframeTableName();
              String Stable = Service.getServiceTableName();


              StringBuffer timeframeSQL = new StringBuffer();
                timeframeSQL.append("SELECT "+Stable+".* FROM "+Stable+", "+Ttable+", "+middleTable);
                timeframeSQL.append(" WHERE ");
                timeframeSQL.append(Ttable+"."+timeframe.getIDColumnName()+" = "+middleTable+"."+timeframe.getIDColumnName());
                timeframeSQL.append(" AND ");
                timeframeSQL.append(Stable+"."+service.getIDColumnName()+" = "+middleTable+"."+service.getIDColumnName());
                  timeframeSQL.append(" AND ");
                  timeframeSQL.append(middleTable+"."+service.getIDColumnName()+" in (");
                  for (int i = 0; i < tempProducts.length; i++) {
                    if (i == 0) {
                      timeframeSQL.append(tempProducts[i].getID());
                    }else {
                      timeframeSQL.append(","+tempProducts[i].getID());
                    }
                  }
                  timeframeSQL.append(")");

                timeframeSQL.append(" AND ");
                timeframeSQL.append("(");
                timeframeSQL.append(" ("+Timeframe.getTimeframeFromColumnName()+" <= '"+from.toSQLDateString()+"' AND "+Timeframe.getTimeframeToColumnName()+" >= '"+from.toSQLDateString()+"')");
                timeframeSQL.append(" OR ");
                timeframeSQL.append(" ("+Timeframe.getTimeframeFromColumnName()+" <= '"+to.toSQLDateString()+"' AND "+Timeframe.getTimeframeToColumnName()+" >= '"+to.toSQLDateString()+"')");
                timeframeSQL.append(" OR ");
                timeframeSQL.append(" ("+Timeframe.getTimeframeFromColumnName()+" >= '"+from.toSQLDateString()+"' AND "+Timeframe.getTimeframeToColumnName()+" <= '"+to.toSQLDateString()+"')");
                timeframeSQL.append(")");
                timeframeSQL.append(" ORDER BY "+Timeframe.getTimeframeFromColumnName());

              Service[] services = (Service[]) (new Service()).findAll(timeframeSQL.toString());
              products = new Product[services.length];
              for (int i = 0; i < products.length; i++) {
                products[i] = new Product(services[i].getID());
              }
          }


      }catch(SQLException sql) {
        sql.printStackTrace(System.err);
      }

      return products;
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
      Service service = TravelStockroomBusiness.getService(product);
      timeFrame = service.getTimeframe();
    }
    catch (SQLException sql) {
      throw new TimeframeNotFoundException();
    }
    catch (ServiceNotFoundException snf) {
      throw new ServiceNotFoundException();
    }
    return timeFrame;
  }

  public static Tour getTour(Product product) throws TourNotFoundException{
    Tour tour = null;
    try {
      tour = new Tour(product.getID());
    }
    catch (SQLException sql) {
      throw new TourNotFoundException();
    }
    return tour;
  }

  public static boolean getIfDay(int productId, int dayOfWeek) {
      return ServiceDay.getIfDay(productId, dayOfWeek);
  }

  public static boolean getIfDay(Product product, idegaTimestamp stamp) throws ServiceNotFoundException, TimeframeNotFoundException{
      boolean isDay = false;

      int dayOfWeek = stamp.getDayOfWeek();
      boolean isValidWeekDay = TravelStockroomBusiness.getIfDay(product.getID(), dayOfWeek);
      Timeframe timeframe = TravelStockroomBusiness.getTimeframe(product);

      if (isValidWeekDay) {
          idegaTimestamp from = new idegaTimestamp(timeframe.getFrom());
          idegaTimestamp to = new idegaTimestamp(timeframe.getTo());
          if (stamp.isLaterThan(from) && to.isLaterThan(stamp)  ) {
              isDay = true;
          }else if (stamp.toSQLDateString().equals(from.toSQLDateString()) || stamp.toSQLDateString().equals(to.toSQLDateString())) {
              isDay = true;
          }
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

  public static class TourNotFoundException extends Exception{
    TourNotFoundException(){
      super("Tour not found");
    }
  }

  /**
   * @todo skoða betur
   */
  public static int getCurrencyIdForIceland(){
      com.idega.block.trade.data.Currency curr = new com.idega.block.trade.data.Currency();
      int returner = -1;
      try {
        String iceKr = "Íslenskar Krónur";
        String[] id = com.idega.data.SimpleQuerier.executeStringQuery("Select "+curr.getIDColumnName()+" from "+curr.getEntityName()+" where "+curr.getColumnNameCurrencyName() +" = '"+iceKr+"'");
        if (id == null || id.length == 0) {
            curr = new com.idega.block.trade.data.Currency();
            curr.setCurrencyName(iceKr);
            curr.setCurrencyAbbreviation("Kr");
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


}