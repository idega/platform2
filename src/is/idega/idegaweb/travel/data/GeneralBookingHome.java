package is.idega.idegaweb.travel.data;


public interface GeneralBookingHome extends com.idega.data.IDOHome
{
 public GeneralBooking create() throws javax.ejb.CreateException;
 public GeneralBooking findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findBookings(int[] p0,int p1,com.idega.util.IWTimestamp p2)throws javax.ejb.FinderException;
 public java.util.Collection findBookings(int[] p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2,int[] p3,java.lang.String p4,java.lang.String p5,com.idega.block.trade.stockroom.data.TravelAddress p6)throws javax.ejb.FinderException,java.rmi.RemoteException;
 public java.util.Collection findBookings(int p0,int p1,com.idega.util.IWTimestamp p2)throws javax.ejb.FinderException;
 public java.util.Collection findBookingsByDateOfBooking(int[] p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2,int[] p3,java.lang.String p4,java.lang.String p5,com.idega.block.trade.stockroom.data.TravelAddress p6)throws javax.ejb.FinderException,java.rmi.RemoteException;
 public int getBookingsTotalCount(int[] p0,int p1,com.idega.util.IWTimestamp p2,java.util.Collection p3,boolean p4);
 public int getBookingsTotalCount(int[] p0,int p1,com.idega.util.IWTimestamp p2);
 public int getBookingsTotalCount(int[] p0,int p1,com.idega.util.IWTimestamp p2,java.util.Collection p3);
 public int getBookingsTotalCount(int p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2,int p3,int[] p4);
 public int getBookingsTotalCount(int p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2,int p3,int[] p4,java.util.Collection p5);
 public int getBookingsTotalCount(int p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2,int p3,int[] p4,java.util.Collection p5,boolean p6);
 public int getBookingsTotalCount(int p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2,int p3,int[] p4,java.util.Collection p5,boolean p6,boolean p7);
 public int getBookingsTotalCountByDateOfBooking(int p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2,int p3,int[] p4,java.util.Collection p5);
 public java.util.List getMultibleBookings(is.idega.idegaweb.travel.data.GeneralBooking p0)throws java.rmi.RemoteException,javax.ejb.FinderException;
 public int getNumberOfBookings(int p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2,int p3);
 public int getNumberOfBookings(int[] p0,int p1,com.idega.util.IWTimestamp p2,java.util.Collection p3);

}