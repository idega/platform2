package is.idega.idegaweb.travel.data;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.GregorianCalendar;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.data.IDOLookup;
import com.idega.data.IDOQuery;
import com.idega.data.SimpleQuerier;
import com.idega.util.IWTimestamp;


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
    this.setAsPrimaryKey(getIDColumnName(), true);
		this.setAsPrimaryKey(getColumnNameDayOfWeek(), true);
		
    addIndex("IDX_SER_DAY", new String[]{getIDColumnName(), getColumnNameDayOfWeek()});
  }

  public String getEntityName() {
    return getServiceDaysTableName();
  }

  public Object ejbFindByPrimaryKey(ServiceDayPK primaryKey) throws FinderException {
	  return super.ejbFindByPrimaryKey(primaryKey);
  }
	
  public Object ejbCreate(ServiceDayPK primaryKey) throws CreateException {
	  setPrimaryKey(primaryKey);
	  return super.ejbCreate();
  }
 
  /* (non-Javadoc)
   * @see com.idega.data.IDOEntityBean#getPrimaryKeyClass()
   */
  public Class getPrimaryKeyClass() {
	  return ServiceDayPK.class;
  }

  protected boolean doInsertInCreate() {
	  return true;
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
  	ServiceDay[] serviceDays = new ServiceDay[]{};
  	
  	try {
			ServiceDayHome serviceDayHome = (ServiceDayHome)IDOLookup.getHome(ServiceDay.class);
			int[] days = ejbHomeGetDaysOfWeek(serviceId);
			serviceDays = new ServiceDay[days.length];
			for (int i = 0; i < days.length; i++) {
				serviceDays[i] = serviceDayHome.findByServiceAndDay(serviceId, days[i]);
			}
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}
		return serviceDays;
  
  	//ServiceDay[] days = (ServiceDay[]) (is.idega.idegaweb.travel.data.ServiceDayBMPBean.getStaticInstance(ServiceDay.class)).findAllByColumnOrdered(getColumnNameServiceId(),Integer.toString(serviceId),getColumnNameDayOfWeek());
    //return days;
  }

  public static int[] getDaysOfWeek(int serviceId)  throws RemoteException, RemoveException, FinderException{
  	ServiceDayHome sdHome = (ServiceDayHome) IDOLookup.getHome(ServiceDay.class);
  	return sdHome.getDaysOfWeek(serviceId);
  }
  

  public int[] ejbHomeGetDaysOfWeek(int serviceId) throws RemoteException, RemoveException, FinderException{
    int[] returner = {};
    try {
    		IDOQuery query = this.idoQuery();
    		query.appendSelect().append(getColumnNameDayOfWeek()).appendFrom().append(getEntityName())
    		.appendWhereEquals(getColumnNameServiceId(), new Integer(serviceId));
				String[] days = SimpleQuerier.executeStringQuery(query.toString());
   		if (days != null && days.length > 0) {
	        returner = new int[days.length];
	        for (int i = 0; i < days.length; i++) {
	          returner[i] = Integer.parseInt(days[i]);
	        }
    		}
    }catch (Exception sql) {
      sql.printStackTrace(System.err);
    }
    return returner;
  }

  /**
   * returns null if nothing...
   */
  public Object ejbFindServiceDay(int serviceId, IWTimestamp stamp) throws FinderException, RemoteException{
    return this.ejbFindByServiceAndDay(serviceId, stamp.getDayOfWeek());
  }
  
  public boolean ejbHomeGetIfDay(int serviceId, int dayOfWeek) {
    boolean returner = false;
    try {
        //ServiceDay[] days = (ServiceDay[]) (is.idega.idegaweb.travel.data.ServiceDayBMPBean.getStaticInstanceIDO(ServiceDay.class)).
        Collection coll = this.idoFindAllIDsByColumnsBySQL(getColumnNameServiceId(),Integer.toString(serviceId),getColumnNameDayOfWeek(),Integer.toString(dayOfWeek));
        
        if (coll != null && !coll.isEmpty()) {
        	if (coll.size() == 1) {
        		returner = true;
        	} else {
          	System.err.println("ServiceDay : getIfDay : Primary Key Error");
          }
        }
    }catch (FinderException sql) {
      sql.printStackTrace(System.err);
    }

    return returner;
  }
  
  public Object ejbFindByServiceAndDay(int serviceId, int day) throws FinderException {
  	IDOQuery query = this.idoQuery();
  	query.appendSelectAllFrom(this).appendWhereEquals(getColumnNameDayOfWeek(), day)
  	.appendAndEquals(getColumnNameServiceId(), serviceId);
  	return this.idoFindOnePKByQuery(query);
  }

  public boolean ejbHomeDeleteService(int serviceId) throws RemoteException , RemoveException, FinderException{
    try {
    	int[] days = this.ejbHomeGetDaysOfWeek(serviceId);
			if (days != null && days.length > 0) {
					ServiceDayHome serviceDayHome = (ServiceDayHome)IDOLookup.getHome(ServiceDay.class);
					ServiceDay sd;
					for (int i = 0; i < days.length; i++) {
						sd = (ServiceDay) serviceDayHome.findByServiceAndDay(serviceId, days[i]);
						sd.remove();
						sd.store();
					}
			}
      
      return true;
    }catch (FinderException sql) {
    	System.out.println(sql.getMessage());
    	//throw new RemoveException(sql.getMessage());
    }
    return false;
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
