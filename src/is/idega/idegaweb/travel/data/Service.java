package is.idega.travel.data;

import java.sql.*;
import com.idega.data.*;
import com.idega.core.data.*;
import com.idega.block.trade.stockroom.data.Product;

/**
 * Title:        IW Travel
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <br><a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a><br><a href="mailto:gimmi@idega.is">Grímur Jónsson</a>
 * @version 1.0
 */

public class Service extends GenericEntity{
  private Product product;

  public Service(){
          super();
  }
  public Service(int id)throws SQLException{
          super(id);
  }
  public void initializeAttributes(){
    addAttribute(getIDColumnName(),"Service_id",true,true,Integer.class,"one-to-one",Product.class);
    addAttribute(getArrivalTimeColumnName(), "Arrival time", true, true, Timestamp.class);
    addAttribute(getDepartureTimeColumnName(), "Departure time", true, true, Timestamp.class);
//    addAttribute(getHotelPickupColumnName(), "Hotel pick-up", true, true, Boolean.class);
//    addAttribute(getHotelPickupPlaceIDColumnName(),"Hotel pick-up staður",true,true,Integer.class,"many_to_one",HotelPickupPlace.class);
//    addAttribute(getAddressIDColumnName(),"Heimilisfang",true,true, Integer.class,"many-to-one",Address.class);

    this.addManyToManyRelationShip(HotelPickupPlace.class, "TB_SERVICE_HOTEL_PICKUP_PLACE");
    this.addManyToManyRelationShip(Address.class, "TB_SERVICE_IC_ADDRESS");
    this.addManyToManyRelationShip(Timeframe.class ,"TB_SERVICE_TIMEFRAME");
  }

  public Product getProduct()  {
    if (this.product == null) {
      try {
      product = new Product(this.getID());
      }catch (SQLException sql) {
        sql.printStackTrace(System.err);
      }
    }
    return product;
  }

  public String getEntityName(){
    return getServiceTableName();
  }

  public String getName(){
    return getProduct().getName();
  }

  public String getDescription() {
    return getProduct().getProdcutDescription();
  }

  public Timestamp getArrivalTime() {
    return (Timestamp) getColumnValue(getArrivalTimeColumnName());
  }

  public void setAttivalTime(Timestamp timestamp) {
    setColumn(getArrivalTimeColumnName(),timestamp);
  }

  public Timestamp getDepartureTime() {
    return (Timestamp) getColumnValue(getDepartureTimeColumnName());
  }

  public void setDepartureTime(Timestamp timestamp) {
    setColumn(getDepartureTimeColumnName(),timestamp);
  }

  public Timeframe[] getTimeframes() throws SQLException  {
    return (Timeframe[]) this.findRelated(Timeframe.getStaticInstance(Timeframe.class));
  }

  public Timeframe getTimeframe() throws SQLException{
    Timeframe[] temp = getTimeframes();
    if (temp.length > 0) {
      return temp[temp.length -1];
    }
    else {
      return null;
    }
  }

  public Address[] getAddresses() throws SQLException  {
    return (Address[]) this.findRelated(Address.getStaticInstance(Address.class));
  }

  public Address getAddress() throws SQLException{
    Address[] temp = getAddresses();
    if (temp.length > 0) {
      return temp[temp.length -1];
    }
    else {
      return null;
    }
  }


/*
  public boolean getIsHotelPickup() {
    return getHotelPickup();
  }

  public boolean getHotelPickup() {
    return getBooleanColumnValue(getHotelPickupColumnName());
  }

  public void setIsHotelPickup(boolean pickup) {
    setHotelPickup(pickup);
  }

  public void setHotelPickup(boolean pickup) {
    setColumn(getHotelPickupColumnName(),pickup);
  }
*/
/*
  public Supplier getSupplier() {
    return (Supplier) getColumnValue(getSupplierIDColumnName());
  }

  public int getSupplierID() {
    return getIntColumnValue(getSupplierIDColumnName());
  }

  public void setSupplierID(int supplierID) {
    setColumn(getSupplierIDColumnName(),supplierID);
  }
*//*
  public HotelPickupPlace getHotelPickupPlace() {
    return (HotelPickupPlace) getColumnValue(getHotelPickupPlaceIDColumnName());
  }

  public int getHotelPickupPlaceID() {
    return getIntColumnValue(getHotelPickupPlaceIDColumnName());
  }

  public void setHotelPickupPlaceID(int id) {
    setColumn(getHotelPickupPlaceIDColumnName(),id);
  }

  public Address getAddress() {
    return (Address) getColumnValue(getAddressIDColumnName());
  }

  public int getAddressID() {
    return getIntColumnValue(getAddressIDColumnName());
  }

  public void setAddressID(int id) {
    setColumn(getAddressIDColumnName(),id);
  }
*/

  public static String getServiceTableName(){return "TB_SERVICE";}
  public static String getArrivalTimeColumnName() {return "ARRIVAL_TIME";}
  public static String getDepartureTimeColumnName() {return "DEPARTURE_TIME";}
//  public static String getHotelPickupColumnName() {return "HOTEL_PICKUP";}
//  public static String getHotelPickupPlaceIDColumnName() {return "TB_HOTEL_PICKUP_PLACE_ID";}
//  public static String getAddressIDColumnName() {return "IC_ADDRESS_ID";}





}
