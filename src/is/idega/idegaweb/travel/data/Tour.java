package is.idega.travel.data;

import com.idega.data.*;
import java.sql.*;


/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class Tour extends GenericEntity {

  public Tour() {
    super();
  }

  public Tour(int id) throws SQLException{
    super(id);
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName(),"Service_id",true,true,Integer.class,"one-to-one",Service.class);
    addAttribute(getHotelPickupColumnName(), "Hotel pick-up", true, true, Boolean.class);
    addAttribute(getHotelPickupTimeColumnName(), "Hotel pick-up time", true, true, Timestamp.class);
    addAttribute(getTotalSeatsColumnName(), "Total seats", true, true, Integer.class);

  }
  public String getEntityName() {
    return getTripTableName();
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

  public void setHotelPickupTime(Timestamp pickupTime) {
    setColumn(getHotelPickupTimeColumnName(), pickupTime);
  }

  public Timestamp getHotelPickupTime() {
    return (Timestamp) getColumnValue(getHotelPickupTimeColumnName());
  }

  public int getTotalSeats() {
    return getIntColumnValue(getTotalSeatsColumnName());
  }

  public void setTotalSeats(int totalSeats) {
    setColumn(getTotalSeatsColumnName(), totalSeats);
  }

  public static String getTripTableName() {return "TB_TOUR";}
  public static String getHotelPickupColumnName() {return "HOTEL_PICKUP";}
  public static String getHotelPickupTimeColumnName() {return "HOTEL_PICKUP_TIME";}
  public static String getTotalSeatsColumnName() {return "TOTAL_SEATS";}
  //  public static String getColumnName() {return "";}

}