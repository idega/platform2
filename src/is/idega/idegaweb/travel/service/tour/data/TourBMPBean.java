package is.idega.idegaweb.travel.service.tour.data;

import java.rmi.RemoteException;
import javax.ejb.FinderException;
import java.util.Collection;
import is.idega.idegaweb.travel.data.PickupPlace;
import com.idega.data.*;
import is.idega.idegaweb.travel.data.Service;
import java.sql.*;


/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class TourBMPBean extends GenericEntity implements Tour {

  public TourBMPBean() {
    super();
  }

  public TourBMPBean(int id) throws SQLException{
    super(id);
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName(),"Service_id",true,true,Integer.class,"one-to-one",Service.class);
    addAttribute(getHotelPickupColumnName(), "Hotel pick-up", true, true, Boolean.class);
    addAttribute(getHotelPickupTimeColumnName(), "Hotel pick-up time", true, true, Timestamp.class);
    addAttribute(getTotalSeatsColumnName(), "Total seats", true, true, Integer.class);
    addAttribute(getMinimumSeatsColumnName(), "Lágmark sæta", true, true, Integer.class);
    addAttribute(getNumberOfDaysColumnName(), "Fjöldi daga", true, true, Integer.class);
    addAttribute(getLengthColumnName(), "Lengd", true, true, Float.class);
    addAttribute(getEstimatedSeatsUsedColumnName(), "estimated seats used", true, true, Integer.class);
  }
  public String getEntityName() {
    return getTripTableName();
  }


  public void setDefaultValues() {
      this.setLength(0);
      this.setTotalSeats(0);
      this.setMinumumSeats(0);
      this.setNumberOfDays(1);
  }

  public boolean getIsHotelPickup() throws RemoteException{
    return getHotelPickup();
  }

  public boolean getHotelPickup() throws RemoteException{
    try {
      Service service = ((is.idega.idegaweb.travel.data.ServiceHome) com.idega.data.IDOLookup.getHome(Service.class)).findByPrimaryKey(this.getPrimaryKey());
      Collection coll = service.getHotelPickupPlaces();
//      HotelPickupPlace[] rugl = (HotelPickupPlace[]) (()).findRelated((HotelPickupPlace) (is.idega.idegaweb.travel.data.HotelPickupPlaceBMPBean.getStaticInstance(HotelPickupPlace.class)));
      if (coll.size() == 0) {
        return false;
      }else {
        return true;
      }
    }catch (FinderException sql) {
      sql.printStackTrace(System.err);
    }catch (IDORelationshipException re) {
      re.printStackTrace(System.err);
    }
    return false;
    //return getBooleanColumnValue(getHotelPickupColumnName());
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

  public int getMinimumSeats() {
    return getIntColumnValue(getMinimumSeatsColumnName());
  }

  public void setMinumumSeats(int seats) {
    setColumn(getMinimumSeatsColumnName(), seats);
  }

  public void setNumberOfDays(int numberOfSeats) {
    setColumn(getNumberOfDaysColumnName(), numberOfSeats);
  }

  public int getNumberOfDays() {
    return getIntColumnValue(getNumberOfDaysColumnName());
  }

  public void setLength(float length) {
    setColumn(getLengthColumnName(), length);
  }

  public float getLength() {
    return getFloatColumnValue(getLengthColumnName());
  }

  public void setEstimatedSeatsUsed(int seats) {
    setColumn(getEstimatedSeatsUsedColumnName(), seats);
  }

  public int getEstimatedSeatsUsed() {
    return getIntColumnValue(getEstimatedSeatsUsedColumnName());
  }



  public static String getTripTableName() {return "TB_TOUR";}
  public static String getHotelPickupColumnName() {return "HOTEL_PICKUP";}
  public static String getHotelPickupTimeColumnName() {return "HOTEL_PICKUP_TIME";}
  public static String getTotalSeatsColumnName() {return "TOTAL_SEATS";}
  public static String getMinimumSeatsColumnName() {return "MINIMUM_SEATS";}
  public static String getNumberOfDaysColumnName() {return "NUMBER_OF_DAYS";}
  public static String getLengthColumnName() {return "TOUR_LENGTH";}
  public static String getEstimatedSeatsUsedColumnName() {return "ESTIMATED_SEATS_USED";}

  public void setPrimaryKey(Object object) {
    super.setPrimaryKey(object);
  }

}
