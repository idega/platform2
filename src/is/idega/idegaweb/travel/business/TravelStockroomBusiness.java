package is.idega.travel.business;

import com.idega.block.trade.stockroom.business.StockroomBusiness;
import com.idega.block.trade.stockroom.data.*;
import is.idega.travel.data.*;
import java.sql.Timestamp;
import com.idega.core.data.*;
import is.idega.travel.data.HotelPickupPlace;
import java.sql.SQLException;
import com.idega.util.idegaTimestamp;
import java.sql.SQLException;
import com.idega.data.EntityFinder;
import java.util.List;
import com.idega.transaction.IdegaTransactionManager;
import javax.transaction.TransactionManager;
import java.sql.Date;

/**
 * Title:        IW Travel
 * Description:
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



  public int createPriceCategory(int supplierId, String name, String description, String type, String extraInfo) throws Exception {

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

    cat.insert();

    cat.addTo(Supplier.class,supplierId);

    return cat.getID();
  }



  public int createService(int supplierId, Integer fileId, String serviceName, String serviceDescription, boolean isValid, int[] addressIds, Timestamp departure, Timestamp arrival) throws Exception{


    TransactionManager transaction = IdegaTransactionManager.getInstance();
    try{
      transaction.begin();

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
      transaction.commit();

      return id;
    }catch(SQLException e){
      transaction.rollback();
      throw new RuntimeException("IWE226TB89");
    }
  }


  /**
   * @todo createTripService
   */
  public int createTripService(int supplierId, Integer fileId, String serviceName, String serviceDescription, boolean isValid, String departureFrom, idegaTimestamp departureTime, String arrivalAt, idegaTimestamp arrivalTime, String pickupPlace, idegaTimestamp pickupTime, int[] activeDays) throws Exception {

      boolean isError = false;

      if (timeframe == null) isError = true;

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
      int[] hotePickupPlaceIds = {hotelPickupAddress.getID()};

      int serviceId = createService(supplierId, fileId, serviceName, serviceDescription, isValid, departureAddressIds, departureTime.getTimestamp(), arrivalTime.getTimestamp());

      Service service = new Service(serviceId);
      Tour tour = new Tour();
        tour.setID(serviceId);

      if(hotePickupPlaceIds.length > 0){
        for (int i = 0; i < hotePickupPlaceIds.length; i++) {
          service.addTo(HotelPickupPlace.class, hotePickupPlaceIds[i]);
        }
        tour.setHotelPickup(true);
      }else{
        tour.setHotelPickup(false);
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

}