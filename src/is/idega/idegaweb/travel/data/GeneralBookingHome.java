package is.idega.idegaweb.travel.data;


public interface GeneralBookingHome extends com.idega.data.IDOHome
{
 public GeneralBooking create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public GeneralBooking findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findBookings(int p0,int p1,com.idega.util.IWTimeStamp p2)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findBookings(int[] p0,com.idega.util.IWTimeStamp p1,com.idega.util.IWTimeStamp p2,int[] p3,java.lang.String p4,java.lang.String p5,com.idega.block.trade.stockroom.data.TravelAddress p6)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findBookings(int[] p0,int p1,com.idega.util.IWTimeStamp p2)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.List getMultibleBookings(is.idega.idegaweb.travel.data.GeneralBooking p0)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public int getNumberOfBookings(int p0,com.idega.util.IWTimeStamp p1,com.idega.util.IWTimeStamp p2,int p3,int[] p4,java.util.Collection p5) throws java.rmi.RemoteException;
 public int getNumberOfBookings(int[] p0,int p1,com.idega.util.IWTimeStamp p2) throws java.rmi.RemoteException;
 public int getNumberOfBookings(int[] p0,int p1,com.idega.util.IWTimeStamp p2,java.util.Collection p3) throws java.rmi.RemoteException;
 public int getNumberOfBookings(int p0,com.idega.util.IWTimeStamp p1,com.idega.util.IWTimeStamp p2,int p3,int[] p4) throws java.rmi.RemoteException;

}