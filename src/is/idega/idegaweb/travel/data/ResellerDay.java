package is.idega.travel.data;

import com.idega.data.*;
import com.idega.block.trade.stockroom.data.Reseller;
import java.sql.SQLException;
import java.util.GregorianCalendar;


/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class ResellerDay extends GenericEntity {

  public static int SUNDAY = GregorianCalendar.SUNDAY;
  public static int MONDAY = GregorianCalendar.MONDAY;
  public static int TUESDAY = GregorianCalendar.TUESDAY;
  public static int WEDNESDAY = GregorianCalendar.WEDNESDAY;
  public static int THURSDAY = GregorianCalendar.THURSDAY;
  public static int FRIDAY = GregorianCalendar.FRIDAY;
  public static int SATURDAY = GregorianCalendar.SATURDAY;

  public ResellerDay() {
  }
  public void initializeAttributes() {
    addAttribute(getIDColumnName(),"Reseller_id",true,true,Integer.class,"many-to-one",Reseller.class);
    addAttribute(getColumnNameServiceId(), "Service_id", true, true, Integer.class, "many-to-one", Service.class);
    addAttribute(getColumnNameDayOfWeek(), "Day of week", true, true, Integer.class);

  }
  public String getEntityName() {
    return getResellerDaysTableName();
  }

  public String getIDColumnName() {
    return getColumnNameResellerId();
  }

  public int getResellerId() {
    return getIntColumnValue(getColumnNameResellerId());
  }

  public void setResellerId(int resellerId) {
    setColumn(getColumnNameResellerId(), resellerId);
  }

  public void setServiceId(int serviceId) {
      setColumn(getColumnNameServiceId(), serviceId);
  }

  public void setDayOfWeek(int dayOfWeek) {
      setColumn(getColumnNameDayOfWeek(), dayOfWeek);
  }

  public void setDayOfWeek(int resellerId, int serviceId, int dayOfWeek) {
      setResellerId(resellerId);
      setServiceId(serviceId);
      setDayOfWeek(dayOfWeek);
  }

  public int getDayOfWeek() {
    return getIntColumnValue(getColumnNameDayOfWeek());
  }

  public int getServiceId() {
    return getIntColumnValue(getColumnNameServiceId());
  }

  public static int[] getDaysOfWeek(int resellerId, int serviceId) {
    int[] returner = {};
    try {
        ResellerDay[] days = (ResellerDay[]) (ResellerDay.getStaticInstance(ResellerDay.class)).findAllByColumnOrdered(getColumnNameResellerId(), Integer.toString(resellerId),getColumnNameServiceId(),Integer.toString(serviceId),getColumnNameDayOfWeek());
        returner = new int[days.length];
        for (int i = 0; i < days.length; i++) {
          returner[i] = days[i].getDayOfWeek();
        }
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }
    return returner;
  }


  public static boolean getIfDay(int resellerId, int serviceId, int dayOfWeek) {
    boolean returner = false;
    try {
        ResellerDay[] days = (ResellerDay[]) (ResellerDay.getStaticInstance(ResellerDay.class)).findAllByColumn(getColumnNameResellerId(), Integer.toString(resellerId) ,getColumnNameServiceId(),Integer.toString(serviceId),getColumnNameDayOfWeek(),Integer.toString(dayOfWeek));
        if (days.length == 1) {
          returner = true;
        }else if (days.length > 1) {
          returner = true;
          System.err.println("ResellerDay : getIfDay : Primary Key Error");
        }
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }

    return returner;
  }


  public static void deleteService(int serviceId) throws SQLException{
      ResellerDay[] days = (ResellerDay[]) (new ResellerDay()).findAllByColumn(getColumnNameServiceId(), serviceId);
      for (int i = 0; i < days.length; i++) {
          days[i].delete();
      }
  }

  public static void deleteReseller(int resellerId) throws SQLException{
      ResellerDay[] days = (ResellerDay[]) (new ResellerDay()).findAllByColumn(getColumnNameResellerId(), resellerId);
      for (int i = 0; i < days.length; i++) {
          days[i].delete();
      }
  }


  public static String getResellerDaysTableName() {return "TB_RESELLER_DAY";}
  public static String getColumnNameResellerId() {return "SR_RESELLER_ID";}
  public static String getColumnNameServiceId() {return "TB_SERVICE_ID";}
  public static String getColumnNameDayOfWeek() {return "DAY_OF_WEEK";}
}