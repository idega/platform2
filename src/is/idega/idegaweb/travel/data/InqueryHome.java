package is.idega.idegaweb.travel.data;


public interface InqueryHome extends com.idega.data.IDOHome
{
 public Inquery create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public Inquery findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findInqueries(int p0,com.idega.util.IWTimeStamp p1,int p2,boolean p3,com.idega.block.trade.stockroom.data.TravelAddress p4,java.lang.String p5)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findInqueries(int p0,com.idega.util.IWTimeStamp p1,int p2,boolean p3,java.lang.String p4)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public int getInqueredSeats(int p0,com.idega.util.IWTimeStamp p1,int p2,boolean p3)throws javax.ejb.FinderException, java.rmi.RemoteException;

}