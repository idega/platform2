package is.idega.idegaweb.travel.data;

import javax.ejb.FinderException;
import java.util.Collection;
import com.idega.data.*;
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

public class ServiceDayBMPBean extends com.idega.data.GenericEntity implements is.idega.idegaweb.travel.data.ServiceDay {
  public static final int SUNDAY = GregorianCalendar.SUNDAY;
  public static final int MONDAY = GregorianCalendar.MONDAY;
  public static final int TUESDAY = GregorianCalendar.TUESDAY;
  public static final int WEDNESDAY = GregorianCalendar.WEDNESDAY;
  public static final int THURSDAY = GregorianCalendar.THURSDAY;
  public static final int FRIDAY = GregorianCalendar.FRIDAY;
  public static final int SATURDAY = GregorianCalendar.SATURDAY;

  public ServiceDayBMPBean() {
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName(),"Service_id",true,true,Integer.class,"one-to-one",Service.class);
    addAttribute(getColumnNameDayOfWeek(), "Day of week", true, true, Integer.class);
    addAttribute(getColumnNameMax(), "max", true, true, Integer.class);
    addAttribute(getColumnNameMin(), "min", true, true, Integer.class);
    addAttribute(getColumnNameEstimated(), "estimated", true, true, Integer.class);
  }

  public String getEntityName() {
    return getServiceDaysTableName();
  }

  public String getIDColumnName() {
    return getColumnNameServiceId();
  }

  public void setServiceId(int serviceId) {
      setColumn(getIDColumnName(), serviceId);
  }

  public void setDayOfWeek(int dayOfWeek) {
      setColumn(getColumnNameDayOfWeek(), dayOfWeek);
  }

  public void setDayOfWeek(int serviceId, int dayOfWeek) {
      setServiceId(serviceId);
      setDayOfWeek(dayOfWeek);
  }

  public int getDayOfWeek() {
    return getIntColumnValue(getColumnNameDayOfWeek());
  }

  public int getServiceId() {
    return getIntColumnValue(getIDColumnName());
  }

  public int getMax() {
    return getIntColumnValue(getColumnNameMax());
  }

  public void setMax(int max) {
    setColumn(getColumnNameMax(), max);
  }

  public int getMin() {
    return getIntColumnValue(getColumnNameMin());
  }

  public void setMin(int min) {
    setColumn(getColumnNameMin(), min);
  }

  public int getEstimated() {
    return getIntColumnValue(getColumnNameEstimated());
  }

  public void setEstimated(int estimated) {
    setColumn(getColumnNameEstimated(), estimated);
  }

  public Collection getServiceDays(int serviceId) throws FinderException{
    return super.idoFindAllIDsByColumnOrderedBySQL(getColumnNameServiceId(),serviceId,getColumnNameDayOfWeek());
  }

  public static int[] getDaysOfWeek(int serviceId) {
    int[] returner = {};
    try {
        ServiceDay[] days = (ServiceDay[]) (is.idega.idegaweb.travel.data.ServiceDayBMPBean.getStaticInstance(ServiceDay.class)).findAllByColumnOrdered(getColumnNameServiceId(),Integer.toString(serviceId),getColumnNameDayOfWeek());
        returner = new int[days.length];
        for (int i = 0; i < days.length; i++) {
          returner[i] = days[i].getDayOfWeek();
        }
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }
    return returner;
  }

  /**
   * @deprecated
   */
  public static boolean getIfDay(int serviceId, int dayOfWeek) {
    boolean returner = false;
    try {
        ServiceDay[] days = (ServiceDay[]) (is.idega.idegaweb.travel.data.ServiceDayBMPBean.getStaticInstance(ServiceDay.class)).findAllByColumn(getColumnNameServiceId(),Integer.toString(serviceId),getColumnNameDayOfWeek(),Integer.toString(dayOfWeek));
        if (days.length == 1) {
          returner = true;
        }else if (days.length > 1) {
          System.err.println("ServiceDay : getIfDay : Primary Key Error");
        }
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }

    return returner;
  }

  public static void deleteService(int serviceId) throws SQLException{
      ServiceDay[] days = (ServiceDay[]) (((is.idega.idegaweb.travel.data.ServiceDayHome)com.idega.data.IDOLookup.getHomeLegacy(ServiceDay.class)).createLegacy()).findAllByColumn(getColumnNameServiceId(), serviceId);
      for (int i = 0; i < days.length; i++) {
          days[i].delete();
      }

  }

  public static String getServiceDaysTableName() {return "TB_SERVICE_DAY";}
  public static String getColumnNameServiceId() {return "SERVICE_ID";}
  public static String getColumnNameDayOfWeek() {return "DAY_OF_WEEK";}
  public static String getColumnNameMax() {return "SD_MAX";}
  public static String getColumnNameMin() {return "SD_MIN";}
  public static String getColumnNameEstimated() {return "SD_ESTIMATED";}

}
