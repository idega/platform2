//idega 2001 - Gimmi

package com.idega.projects.nat.data;

import java.sql.*;
import com.idega.data.*;
import com.idega.core.data.*;
import com.idega.block.trade.stockroom.data.Product;

public class Service extends GenericEntity{

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
    addAttribute(getHotelPickupColumnName(), "Hotel pick-up", true, true, Boolean.class);
//    addAttribute(getLowerBookingLimitColumnName(), "Lower booking limit", true, true, Integer.class);
//    addAttribute(getHigherBookingLimitColumnName(), "Higher booking limit", true, true, Integer.class);
/*
    addAttribute(getHotelPickupPlaceIDColumnName(),"Hotel pick-up staður",true,true,Integer.class,"many_to_one",HotelPickupPlace.class);
    addAttribute(getAddressIDColumnName(),"Heimilisfang",true,true, Integer.class,"many-to-one",Address.class);
*/

    this.addManyToManyRelationShip(Timeframe.class ,"TB_SERVICE_TIMEFRAME");
  }


  public String getEntityName(){
    return getServiceTableName();
  }
/*
  public String getName(){
    return getNameColumnName();
  }

  public void setName(String name){
    setColumn(getNameColumnName(),name);
  }
*/
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
/*
  public int getLowerBookingLimit() {
    return getIntColumnValue(getLowerBookingLimitColumnName());
  }

  public void setLowerBookingLimit(int limit) {
    setColumn(getLowerBookingLimitColumnName(), limit);
  }

  public int getHigherBookingLimit() {
    return getIntColumnValue(getHigherBookingLimitColumnName());
  }

  public void setHigherBookingLimit(int limit) {
    setColumn(getHigherBookingLimitColumnName(), limit);
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
*/
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

// Unimplemented
/*
  public Image getImage() {

  }

  public int getImageID() {

  }

  public void setImageID(int id) {

  }
*/
  public static String getServiceTableName(){return "TB_SERVICE";}
  public static String getArrivalTimeColumnName() {return "ARRIVAL_TIME";}
  public static String getDepartureTimeColumnName() {return "DEPARTURE_TIME";}
  public static String getHotelPickupColumnName() {return "HOTEL_PICKUP";}
  public static String getLowerBookingLimitColumnName() {return "LOWER_BOOKING_LIMIT";}
  public static String getHigherBookingLimitColumnName() {return "HIGHER_BOOKING_LIMIT";}
  public static String getHotelPickupPlaceIDColumnName() {return "TB_HOTEL_PICKUP_PLACE_ID";}
  public static String getAddressIDColumnName() {return "IC_ADDRESS_ID";}





}
