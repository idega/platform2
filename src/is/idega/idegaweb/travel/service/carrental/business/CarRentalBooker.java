package is.idega.idegaweb.travel.service.carrental.business;

import is.idega.idegaweb.travel.business.Booker;


public interface CarRentalBooker extends com.idega.business.IBOService, Booker
{
 public int Book(int p0,int p1,com.idega.util.IWTimestamp p2,int p3,com.idega.util.IWTimestamp p4,java.lang.String p5,java.lang.String p6,java.lang.String p7,java.lang.String p8,java.lang.String p9,java.lang.String p10,com.idega.util.IWTimestamp p11,int p12,int p13,java.lang.String p14,int p15,int p16,int p17,int p18,java.lang.String p19)throws java.sql.SQLException,java.rmi.RemoteException,javax.ejb.CreateException, java.rmi.RemoteException;
 public int BookBySupplier(int p0,int p1,com.idega.util.IWTimestamp p2,int p3,com.idega.util.IWTimestamp p4,java.lang.String p5,java.lang.String p6,java.lang.String p7,java.lang.String p8,java.lang.String p9,java.lang.String p10,com.idega.util.IWTimestamp p11,int p12,java.lang.String p13,int p14,int p15,int p16,int p17,java.lang.String p18)throws java.sql.SQLException,java.rmi.RemoteException,javax.ejb.CreateException, java.rmi.RemoteException;
 public int updateBooking(int p0,int p1,int p2,com.idega.util.IWTimestamp p3,int p4,com.idega.util.IWTimestamp p5,java.lang.String p6,java.lang.String p7,java.lang.String p8,java.lang.String p9,java.lang.String p10,java.lang.String p11,com.idega.util.IWTimestamp p12,int p13,java.lang.String p14,int p15,int p16,int p17,int p18,java.lang.String p19)throws java.sql.SQLException,java.rmi.RemoteException,javax.ejb.CreateException, java.rmi.RemoteException;
}
