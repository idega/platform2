package is.idega.idegaweb.travel.data;

import javax.ejb.RemoveException;
import com.idega.util.IWTimestamp;
import java.rmi.RemoteException;
import java.util.*;
import javax.ejb.FinderException;
import com.idega.data.*;
import java.sql.SQLException;


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

  public void setDayOfWeek(int serviceId, int dayOfWeek, int max, int min, int estimated) {
      setServiceId(serviceId);
      setDayOfWeek(dayOfWeek);
      setMax(max);
      setMin(min);
      setEstimated(estimated);
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

  public ServiceDay[] getServiceDaysOfWeek(int serviceId) throws SQLException {
    ServiceDay[] days = (ServiceDay[]) (is.idega.idegaweb.travel.data.ServiceDayBMPBean.getStaticInstance(ServiceDay.class)).findAllByColumnOrdered(getColumnNameServiceId(),Integer.toString(serviceId),getColumnNameDayOfWeek());
    return days;
  }


  public int[] getDaysOfWeek(int serviceId) throws RemoteException, RemoveException, FinderException{
/*    int[] returner = {};
    try {
        ServiceDay[] days = (ServiceDay[]) (is.idega.idegaweb.travel.data.ServiceDayBMPBean.getStaticInstance(ServiceDay.class)).findAllByColumnOrdered(getColumnNameServiceId(),Integer.toString(serviceId),getColumnNameDayOfWeek());
        returner = new int[days.length];
        for (int i = 0; i < days.length; i++) {
          if (days[i].getDayOfWeek() == -1) {
            days[i].remove();
            debug("deleting ServiceDay with dayOfWeek is NULL");
          }else {
            returner[i] = days[i].getDayOfWeek();
          }
        }
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }
    return returner;
*/
    int[] returner = {};
    try {
				Collection coll = this.idoFindAllIDsByColumnBySQL(getColumnNameServiceId(),Integer.toString(serviceId));
    //    ServiceDay[] days = (ServiceDay[]) (is.idega.idegaweb.travel.data.ServiceDayBMPBean.getStaticInstance(ServiceDay.class)).findAllByColumnOrdered(getColumnNameServiceId(), Integer.toString(serviceId),getColumnNameDayOfWeek());
    		if (coll != null && !coll.isEmpty()) {
	        returner = new int[coll.size()];
					ServiceDayHome serviceDayHome = (ServiceDayHome)IDOLookup.getHome(ServiceDay.class);
					Iterator iter = coll.iterator();
					ServiceDay sd;
					int counter = 0;
	        while (iter.hasNext()) {
	        	sd = (ServiceDay) serviceDayHome.findByPrimaryKey(iter.next());
	          returner[counter++] = sd.getDayOfWeek();
	        }
    		}
    }catch (FinderException sql) {
      sql.printStackTrace(System.err);
    }
    return returner;
  }

  /**
   * returns null if nothing...
   */
  public ServiceDay getServiceDay(int serviceId, IWTimestamp stamp) throws FinderException, RemoteException{
    return getServiceDay(serviceId, stamp.getDayOfWeek());
  }
  /**
   * returns null if nothing...
   */
  public ServiceDay getServiceDay(int serviceId, int dayOfWeek) throws FinderException, RemoteException{
    try {
			Collection coll = this.idoFindAllIDsByColumnsBySQL(getColumnNameServiceId(),Integer.toString(serviceId),getColumnNameDayOfWeek(),Integer.toString(dayOfWeek));
      //ServiceDay[] days = (ServiceDay[]) (is.idega.idegaweb.travel.data.ServiceDayBMPBean.getStaticInstance(ServiceDay.class)).findAllByColumn(getColumnNameServiceId(),Integer.toString(serviceId),getColumnNameDayOfWeek(),Integer.toString(dayOfWeek));
      //Collection coll = super.idoFindIDsBySQL("Select "+this.getIDColumnName()+" from "+getTableName()+" where "+this.getColumnNameServiceId()+" = "+serviceId+" and "+this.getColumnNameDayOfWeek()+" = "+dayOfWeek);
      if (coll != null && coll.size() > 0) {
        ServiceDayHome serviceDayHome = (ServiceDayHome)IDOLookup.getHome(ServiceDay.class);
        Iterator iter = coll.iterator();
        return (ServiceDay) serviceDayHome.findByPrimaryKey(iter.next());
      }
      //if (days.length == 1) {
        //return days[0];
      //}else if (days.length > 1) {
        //System.err.println("ServiceDay : getIfDay : Primary Key Error");
      //}
    }catch (FinderException sql) {
      throw new FinderException(sql.getMessage());
    }
    return null;
    //    ServiceDay[] days = (ServiceDay[]) (is.idega.idegaweb.travel.data.ServiceDayBMPBean.getStaticInstance(ServiceDay.class)).findAllByColumn(getColumnNameServiceId(),Integer.toString(serviceId),getColumnNameDayOfWeek(),Integer.toString(dayOfWeek));
  }

  public boolean ejbHomeGetIfDay(int serviceId, int dayOfWeek) {
    boolean returner = false;
    try {
        //ServiceDay[] days = (ServiceDay[]) (is.idega.idegaweb.travel.data.ServiceDayBMPBean.getStaticInstanceIDO(ServiceDay.class)).
        Collection coll = this.idoFindAllIDsByColumnsBySQL(getColumnNameServiceId(),Integer.toString(serviceId),getColumnNameDayOfWeek(),Integer.toString(dayOfWeek));
        
        if (coll != null && !coll.isEmpty()) {
          returner = true;
        }else {
          System.err.println("ServiceDay : getIfDay : Primary Key Error");
        }
    }catch (FinderException sql) {
      sql.printStackTrace(System.err);
    }

    return returner;
  }

  public boolean ejbHomeDeleteService(int serviceId) throws RemoteException , RemoveException, FinderException{
/*    Collection coll = this.idoFindAllIDsByColumnBySQL(getColumnNameServiceId(), Integer.toString(serviceId));
    Iterator days = coll.iterator();
    ServiceDayHome sDayHome = (ServiceDayHome) IDOLookup.getHome(ServiceDay.class);
    ServiceDay sDay;
    while (days.hasNext()) {
      sDay = sDayHome.findByPrimaryKey(days.next());
//      sDay = (ServiceDay) this.ejbFindByPrimaryKey(days.next());
      sDay.remove();
    }*/


    try {
			Collection coll = this.idoFindAllIDsByColumnBySQL(getColumnNameServiceId(),Integer.toString(serviceId));
//    ServiceDay[] days = (ServiceDay[]) (is.idega.idegaweb.travel.data.ServiceDayBMPBean.getStaticInstance(ServiceDay.class)).findAllByColumnOrdered(getColumnNameServiceId(), Integer.toString(serviceId),getColumnNameDayOfWeek());
			if (coll != null && !coll.isEmpty()) {
					ServiceDayHome serviceDayHome = (ServiceDayHome)IDOLookup.getHome(ServiceDay.class);
					Iterator iter = coll.iterator();
					ServiceDay sd;
					while (iter.hasNext()) {
						sd = (ServiceDay) serviceDayHome.findByPrimaryKey(iter.next());
						sd.remove();
					}
			}
      //ServiceDay[] days = (ServiceDay[]) (is.idega.idegaweb.travel.data.ServiceDayBMPBean.getStaticInstance(ServiceDay.class)).findAllByColumn(getColumnNameServiceId(),Integer.toString(serviceId));
//      ServiceDay[] days = (ServiceDay[]) (((is.idega.idegaweb.travel.data.ServiceDayHome)com.idega.data.IDOLookup.getHomeLegacy(ServiceDay.class)).createLegacy()).findAllByColumn(getColumnNameServiceId(), serviceId);
/*
      for (int i = 0; i < days.length; i++) {
          days[i].remove();
      }*/
      
      return true;
    }catch (FinderException sql) {
      throw new RemoveException(sql.getMessage());
    }
  }

  public boolean ejbHomeSetServiceWithNoDays(int serviceId)  throws RemoteException , RemoveException, FinderException{
    return ejbHomeDeleteService(serviceId);
  }


  public static String getServiceDaysTableName() {return "TB_SERVICE_DAY";}
  public static String getColumnNameServiceId() {return "SERVICE_ID";}
  public static String getColumnNameDayOfWeek() {return "DAY_OF_WEEK";}
  public static String getColumnNameMax() {return "SD_MAX";}
  public static String getColumnNameMin() {return "SD_MIN";}
  public static String getColumnNameEstimated() {return "SD_ESTIMATED";}

}
