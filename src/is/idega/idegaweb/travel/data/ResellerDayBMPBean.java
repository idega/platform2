package is.idega.idegaweb.travel.data;

import javax.ejb.RemoveException;
import javax.ejb.FinderException;
import java.rmi.RemoteException;
import java.util.*;
import com.idega.data.*;
import com.idega.block.trade.stockroom.data.Reseller;
import java.sql.SQLException;


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

  public int[] getDaysOfWeek(int resellerId, int serviceId) throws RemoteException{
    int[] returner = {};
    try {
        ResellerDay[] days = (ResellerDay[]) (is.idega.idegaweb.travel.data.ResellerDayBMPBean.getStaticInstance(ResellerDay.class)).findAllByColumnOrdered(getColumnNameResellerId(), Integer.toString(resellerId),getColumnNameServiceId(),Integer.toString(serviceId),getColumnNameDayOfWeek());
        returner = new int[days.length];
        for (int i = 0; i < days.length; i++) {
          returner[i] = days[i].getDayOfWeek();
        }
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }
    return returner;
  }


  public boolean ejbHomeGetIfDay(int resellerId, int serviceId, int dayOfWeek) {
    boolean returner = false;
    try {
        ResellerDay[] days = (ResellerDay[]) (is.idega.idegaweb.travel.data.ResellerDayBMPBean.getStaticInstance(ResellerDay.class)).findAllByColumn(getColumnNameResellerId(), Integer.toString(resellerId) ,getColumnNameServiceId(),Integer.toString(serviceId),getColumnNameDayOfWeek(),Integer.toString(dayOfWeek));
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


  public void deleteService(int serviceId) throws RemoteException, FinderException, RemoveException{
    Collection coll = this.idoFindAllIDsByColumnBySQL(getColumnNameServiceId(), Integer.toString(serviceId));
    Iterator days = coll.iterator();
    ResellerDay rDay;
    while (days.hasNext()) {
      rDay = (ResellerDay) this.ejbFindByPrimaryKey(days.next());
      rDay.remove();
    }
  }

  public void deleteReseller(int resellerId) throws RemoteException, FinderException, RemoveException {
    Collection coll = this.idoFindAllIDsByColumnBySQL(getColumnNameResellerId(), Integer.toString(resellerId));
    Iterator days = coll.iterator();
    ResellerDay rDay;
    while (days.hasNext()) {
      rDay = (ResellerDay) this.ejbFindByPrimaryKey(days.next());
      rDay.remove();
    }

/*
      ResellerDay[] days = (ResellerDay[]) (((is.idega.idegaweb.travel.data.ResellerDayHome)com.idega.data.IDOLookup.getHomeLegacy(ResellerDay.class)).createLegacy()).findAllByColumn(getColumnNameResellerId(), resellerId);
      for (int i = 0; i < days.length; i++) {
          days[i].remove();
      }
      */
  }

	public boolean ejbHomeRemoveResellerDays(Reseller reseller, Service service) throws Exception {
		String sql = "delete from "+getResellerDaysTableName()+" where "+getColumnNameResellerId()+" = "+reseller.getPrimaryKey().toString()+" AND "+getColumnNameServiceId()+" = "+service.getID();
		return SimpleQuerier.execute(sql);	
	}

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


  public static String getResellerDaysTableName() {return "TB_RESELLER_DAY";}
  public static String getColumnNameResellerId() {return "SR_RESELLER_ID";}
  public static String getColumnNameServiceId() {return "TB_SERVICE_ID";}
  public static String getColumnNameDayOfWeek() {return "DAY_OF_WEEK";}
}
