package is.idega.idegaweb.travel.data;


public interface ResellerDayHome extends com.idega.data.IDOHome
{
 public ResellerDay create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public ResellerDay findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findResellerDays(com.idega.block.trade.stockroom.data.Reseller p0,is.idega.idegaweb.travel.data.Service p1)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean getIfDay(int p0,int p1,int p2) throws java.rmi.RemoteException;

}