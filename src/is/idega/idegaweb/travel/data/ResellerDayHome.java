package is.idega.idegaweb.travel.data;

import java.util.Collection;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;


public interface ResellerDayHome extends com.idega.data.IDOHome
{
 public ResellerDay create() throws javax.ejb.CreateException;
 public ResellerDay findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public ResellerDay create(is.idega.idegaweb.travel.data.ResellerDayPK p0)throws javax.ejb.CreateException;
 public ResellerDay findByPrimaryKey(is.idega.idegaweb.travel.data.ResellerDayPK p0)throws javax.ejb.FinderException;
 public boolean getIfDay(int p0,int p1,int p2);
// public int[] getResellerDays(com.idega.block.trade.stockroom.data.Reseller p0,is.idega.idegaweb.travel.data.Service p1)throws java.rmi.RemoteException,javax.ejb.FinderException;
 public void removeResellerDays(com.idega.block.trade.stockroom.data.Reseller p0,is.idega.idegaweb.travel.data.Service p1)throws RemoveException;
 public Collection getDaysOfWeek(int resellerId, int serviceId) throws FinderException;
 public int[] getDaysOfWeekInt(int resellerId, int serviceId) throws FinderException;
}