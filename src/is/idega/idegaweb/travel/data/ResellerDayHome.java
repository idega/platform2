package is.idega.idegaweb.travel.data;


public interface ResellerDayHome extends com.idega.data.IDOHome
{
 public ResellerDay create() throws javax.ejb.CreateException;
 public ResellerDay findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public int[] getResellerDays(com.idega.block.trade.stockroom.data.Reseller p0,is.idega.idegaweb.travel.data.Service p1)throws java.rmi.RemoteException,javax.ejb.FinderException;
 public boolean getIfDay(int p0,int p1,int p2);
 public boolean removeResellerDays(com.idega.block.trade.stockroom.data.Reseller p0,is.idega.idegaweb.travel.data.Service p1)throws java.lang.Exception;

}