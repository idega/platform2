package is.idega.idegaweb.travel.data;

import javax.ejb.FinderException;
import com.idega.util.IWTimestamp;


public interface GeneralBookingHome extends com.idega.data.IDOHome
{
 public GeneralBooking create() throws javax.ejb.CreateException;
 public GeneralBooking findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllByCode(java.lang.String p0)throws javax.ejb.FinderException;
 public java.util.Collection findAllByReferenceNumber(java.lang.String p0)throws javax.ejb.FinderException;
 public java.util.Collection findBookings(int[] p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2,int[] p3,java.lang.String p4,java.lang.String p5,com.idega.block.trade.stockroom.data.TravelAddress p6,java.lang.String p7,java.lang.String p8,boolean p9)throws javax.ejb.FinderException,java.rmi.RemoteException;
 public java.util.Collection findBookings(int[] p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2,int[] p3,java.lang.String p4,java.lang.String p5,com.idega.block.trade.stockroom.data.TravelAddress p6,java.lang.String p7,boolean p8)throws javax.ejb.FinderException,java.rmi.RemoteException;
 public java.util.Collection findBookings(int[] p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2,int[] p3,java.lang.String p4,java.lang.String p5,com.idega.block.trade.stockroom.data.TravelAddress p6,java.lang.String p7,java.lang.String p8)throws javax.ejb.FinderException,java.rmi.RemoteException;
 public java.util.Collection findBookings(int[] p0,int p1,com.idega.util.IWTimestamp p2,com.idega.block.trade.stockroom.data.TravelAddress p3)throws javax.ejb.FinderException;
 public java.util.Collection findBookings(int[] p0,int p1,com.idega.util.IWTimestamp p2,java.lang.String p3,com.idega.block.trade.stockroom.data.TravelAddress p4)throws javax.ejb.FinderException;
 public java.util.Collection findBookings(int p0,int p1,com.idega.util.IWTimestamp p2,com.idega.block.trade.stockroom.data.TravelAddress p3)throws javax.ejb.FinderException;
 public java.util.Collection findBookingsByDateOfBooking(int[] p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2,int[] p3,java.lang.String p4,java.lang.String p5,com.idega.block.trade.stockroom.data.TravelAddress p6,java.lang.String p7,boolean p8)throws javax.ejb.FinderException,java.rmi.RemoteException;
 public int getBookingsTotalCount(int p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2,int p3,int[] p4,java.util.Collection p5,boolean p6,boolean p7,java.lang.String p8);
 public int getBookingsTotalCount(int[] p0,int p1,com.idega.util.IWTimestamp p2);
 public int getBookingsTotalCount(int[] p0,int p1,com.idega.util.IWTimestamp p2,java.util.Collection p3);
 public int getBookingsTotalCount(int[] p0,int p1,com.idega.util.IWTimestamp p2,java.util.Collection p3,boolean p4,java.lang.String p5);
 public int getBookingsTotalCount(int p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2,int p3,int[] p4);
 public int getBookingsTotalCount(int p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2,int p3,int[] p4,java.util.Collection p5);
 public int getBookingsTotalCount(int p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2,int p3,int[] p4,java.util.Collection p5,boolean p6);
 public int getBookingsTotalCountByDateOfBooking(int p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2,int p3,int[] p4,java.util.Collection p5);
 public java.util.Collection getMultibleBookings(is.idega.idegaweb.travel.data.GeneralBooking p0)throws java.rmi.RemoteException,javax.ejb.FinderException;
 public int getNumberOfBookings(int[] p0,int p1,com.idega.util.IWTimestamp p2,java.util.Collection p3);
 public int getNumberOfBookings(int p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2,int p3);
 public GeneralBooking findByAuthorizationNumber(String number, IWTimestamp stamp) throws FinderException;
}