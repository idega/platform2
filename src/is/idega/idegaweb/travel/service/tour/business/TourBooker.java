package is.idega.idegaweb.travel.service.tour.business;

import is.idega.idegaweb.travel.business.Booker;
import javax.ejb.*;

public interface TourBooker extends com.idega.business.IBOService, Booker
{
 public int Book(int p0,int p1,java.lang.String p2,java.lang.String p3,java.lang.String p4,java.lang.String p5,java.lang.String p6,java.lang.String p7,java.lang.String p8,com.idega.util.IWTimeStamp p9,int p10,int p11,java.lang.String p12,int p13,int p14,int p15,int p16,java.lang.String p17)throws javax.ejb.CreateException,java.rmi.RemoteException,java.sql.SQLException, java.rmi.RemoteException;
 public int BookBySupplier(int p0,int p1,java.lang.String p2,java.lang.String p3,java.lang.String p4,java.lang.String p5,java.lang.String p6,java.lang.String p7,java.lang.String p8,com.idega.util.IWTimeStamp p9,int p10,java.lang.String p11,int p12,int p13,int p14,int p15,java.lang.String p16)throws javax.ejb.CreateException,java.rmi.RemoteException,java.sql.SQLException, java.rmi.RemoteException;
 public is.idega.idegaweb.travel.interfaces.Booking[] getBookings(int p0,com.idega.util.IWTimeStamp p1,boolean p2)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public int updateBooking(int p0,int p1,int p2,java.lang.String p3,java.lang.String p4,java.lang.String p5,java.lang.String p6,java.lang.String p7,java.lang.String p8,java.lang.String p9,com.idega.util.IWTimeStamp p10,int p11,java.lang.String p12,int p13,int p14,int p15,int p16,java.lang.String p17)throws javax.ejb.CreateException,java.rmi.RemoteException,java.sql.SQLException, java.rmi.RemoteException;
}
