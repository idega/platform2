package is.idega.idegaweb.travel.data;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.block.trade.stockroom.data.Reseller;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDOQuery;


/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class ResellerDayBMPBean extends com.idega.data.GenericEntity implements is.idega.idegaweb.travel.data.ResellerDay {

  public static int SUNDAY = GregorianCalendar.SUNDAY;
  public static int MONDAY = GregorianCalendar.MONDAY;
  public static int TUESDAY = GregorianCalendar.TUESDAY;
  public static int WEDNESDAY = GregorianCalendar.WEDNESDAY;
  public static int THURSDAY = GregorianCalendar.THURSDAY;
  public static int FRIDAY = GregorianCalendar.FRIDAY;
  public static int SATURDAY = GregorianCalendar.SATURDAY;

  public ResellerDayBMPBean() {
  }
  public void initializeAttributes() {
    addAttribute(getColumnNameResellerId(),"Reseller_id",true,true,Integer.class,"many-to-one",Reseller.class);
    addAttribute(getColumnNameServiceId(), "Service_id", true, true, Integer.class, "many-to-one", Service.class);
    addAttribute(getColumnNameDayOfWeek(), "Day of week", true, true, Integer.class);

    this.setAsPrimaryKey(getColumnNameResellerId(), true);
    this.setAsPrimaryKey(getColumnNameServiceId(), true);
    this.setAsPrimaryKey(getColumnNameDayOfWeek(), true);
    
    addIndex("IDX_RES_SER_DAY", new String[]{getColumnNameResellerId(), getColumnNameServiceId(), getColumnNameDayOfWeek()});

    
  }
  public String getEntityName() {
    return getResellerDaysTableName();
  }

  public Object ejbFindByPrimaryKey(ResellerDayPK primaryKey) throws FinderException {
  	return super.ejbFindByPrimaryKey(primaryKey);
  }
  
  public Object ejbFindByPrimaryKey(Object primaryKey) throws FinderException {
  	return this.ejbFindByPrimaryKey((ResellerDayPK) primaryKey);
  }
  
  public Object ejbCreate(ResellerDayPK primaryKey) throws CreateException {
  	setPrimaryKey(primaryKey);
  	return super.ejbCreate();
  }
  
  /* (non-Javadoc)
   * @see com.idega.data.IDOEntityBean#getPrimaryKeyClass()
   */
  public Class getPrimaryKeyClass() {
  	return ResellerDayPK.class;
  }

  protected boolean doInsertInCreate() {
  	return true;
  }
  /*
   * @deprecated
   */
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

  
  public Collection ejbHomeGetDaysOfWeek(int resellerId, int serviceId) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this)
			.appendWhereEquals(getColumnNameResellerId(), resellerId)
			.appendAndEquals(getColumnNameServiceId(), serviceId)
			.appendOrderBy(getColumnNameDayOfWeek());
		return this.idoFindPKsByQuery(query);
  }

  public int[] ejbHomeGetDaysOfWeekInt(int resellerId, int serviceId) throws FinderException {
  	Collection coll = ejbHomeGetDaysOfWeek(resellerId, serviceId);
  	if (coll != null && !coll.isEmpty()) {
  		int[] returner = new int[coll.size()];
  		Iterator iter = coll.iterator();
  		try {
	  		ResellerDayHome rdHome = (ResellerDayHome) IDOLookup.getHome(ResellerDay.class);
	  		for (int i = 0; iter.hasNext(); i++) {
	  			returner[i] = ((ResellerDay) rdHome.findByPrimaryKey(iter.next())).getDayOfWeek();
	  		}
  		} catch (IDOLookupException e) {
  			throw new FinderException(e.getMessage());
  		}
  		return returner;
  	}
  	return new int[]{};
  }

  public boolean ejbHomeGetIfDay(int resellerId, int serviceId, int dayOfWeek) {
    boolean returner = false;
    try {
    	IDOQuery query = idoQuery();
    	query.appendSelectAllFrom(this)
			.appendWhereEquals(getColumnNameResellerId(), resellerId)
			.appendAndEquals(getColumnNameServiceId(), serviceId)
			.appendAndEquals(getColumnNameDayOfWeek(), dayOfWeek);
    	Collection days = this.idoFindPKsByQuery(query);
    	
    	if (days != null && !days.isEmpty()) {
        if (days.size() == 1) {
          returner = true;
        }else {
          returner = true;
          System.err.println("ResellerDay : getIfDay : Primary Key Error");
        }
    	}
    }catch (FinderException e) {
			e.printStackTrace();
		}

    return returner;
  }


  public void deleteService(int serviceId) throws RemoteException, FinderException, RemoveException{
    Collection coll = this.idoFindAllIDsByColumnBySQL(getColumnNameServiceId(), Integer.toString(serviceId));
    Iterator days = coll.iterator();
    ResellerDay rDay;
    ResellerDayHome rdHome = (ResellerDayHome) IDOLookup.getHome(ResellerDay.class);
    while (days.hasNext()) {
      rDay = (ResellerDay) rdHome.findByPrimaryKey(days.next());
      rDay.remove();
    }
  }

  public void deleteReseller(int resellerId) throws RemoteException, FinderException, RemoveException {
    Collection coll = this.idoFindAllIDsByColumnBySQL(getColumnNameResellerId(), Integer.toString(resellerId));
    Iterator days = coll.iterator();
    ResellerDay rDay;
    ResellerDayHome rdHome = (ResellerDayHome) IDOLookup.getHome(ResellerDay.class);
    while (days.hasNext()) {
      rDay = (ResellerDay) rdHome.findByPrimaryKey(days.next());
      rDay.remove();
    }

/*
      ResellerDay[] days = (ResellerDay[]) (((is.idega.idegaweb.travel.data.ResellerDayHome)com.idega.data.IDOLookup.getHomeLegacy(ResellerDay.class)).createLegacy()).findAllByColumn(getColumnNameResellerId(), resellerId);
      for (int i = 0; i < days.length; i++) {
          days[i].remove();
      }
      */
  }

	public void ejbHomeRemoveResellerDays(Reseller reseller, Service service) throws RemoveException {
		try {
			Collection days = ejbHomeGetDaysOfWeek(Integer.parseInt(reseller.getPrimaryKey().toString()),Integer.parseInt(service.getPrimaryKey().toString()));
			if (days != null && !days.isEmpty()) {
				Iterator iter = days.iterator();
				ResellerDay rDay;
				ResellerDayHome rdHome = (ResellerDayHome) IDOLookup.getHome(ResellerDay.class);
				while (iter.hasNext()) {
					rDay = (ResellerDay) rdHome.findByPrimaryKey(iter.next());
					rDay.remove();
				}
			}
		} catch (FinderException f) {
			throw new RemoveException(f.getMessage());
		} catch (IDOLookupException e) {
			throw new RemoveException(e.getMessage());
		}
	}
/*
  public int[] ejbHomeGetResellerDays(Reseller reseller, Service service) throws RemoteException, FinderException{
//    return this.idoFindPKsBySQL("select * from "+getResellerDaysTableName()+" where "+getColumnNameResellerId()+" = "+reseller.getPrimaryKey().toString()+" AND "+getColumnNameServiceId()+" = "+service.getPrimaryKey().toString());
//		System.out.println("[ResellerDayBMPBean] Service : "+service.getName(1)+", id = "+service.getID());
		//String sql = "select * from "+getResellerDaysTableName()+" where "+getColumnNameResellerId()+" = "+reseller.getPrimaryKey().toString()+" AND "+getColumnNameServiceId()+" = "+service.getID();
		String sql2 = "select "+getColumnNameDayOfWeek()+" from "+getResellerDaysTableName()+" where "+getColumnNameResellerId()+" = "+reseller.getPrimaryKey().toString()+" AND "+getColumnNameServiceId()+" = "+service.getID();
		System.out.println("[ResellerDayBMPBean] Depprad fall... kannski : ejbHomeGetResellerDays");
		//return this.idoFindPKsBySQL(sql);
		try {
			String[] sRepps = SimpleQuerier.executeStringQuery(sql2); 
			int[] repps = new int[sRepps.length];
			for ( int i = 0 ; i < sRepps.length ; i++ ) {
				repps[i] = Integer.parseInt(sRepps[i]);	
			}
			return repps;
		}catch(Exception e) {
			throw new FinderException(e.getMessage());	
		}
  }
*/

  public static String getResellerDaysTableName() {return "TB_RESELLER_DAY";}
  public static String getColumnNameResellerId() {return "SR_RESELLER_ID";}
  public static String getColumnNameServiceId() {return "TB_SERVICE_ID";}
  public static String getColumnNameDayOfWeek() {return "DAY_OF_WEEK";}
}
