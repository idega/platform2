package is.idega.idegaweb.travel.service.hotel.business;

import is.idega.idegaweb.travel.business.TravelStockroomBusiness;
import javax.ejb.*;

public interface HotelBusiness extends TravelStockroomBusiness
{
 public int getMaxBookings(com.idega.block.trade.stockroom.data.Product p0,com.idega.util.IWTimestamp p1)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public int updateHotel(int p0,int p1,java.lang.Integer p2,java.lang.String p3,java.lang.String p4,java.lang.String p5,int p6,boolean p7,int p8)throws java.lang.Exception, java.rmi.RemoteException;
 public java.util.List getDepartureDays(com.idega.presentation.IWContext p0,com.idega.block.trade.stockroom.data.Product p1,com.idega.util.IWTimestamp p2,com.idega.util.IWTimestamp p3,boolean p4)throws javax.ejb.FinderException,java.rmi.RemoteException,java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean getIfDay(com.idega.presentation.IWContext p0,com.idega.block.trade.stockroom.data.Product p1,com.idega.block.trade.stockroom.data.Timeframe[] p2,com.idega.util.IWTimestamp p3,boolean p4,boolean p5)throws is.idega.idegaweb.travel.business.ServiceNotFoundException,is.idega.idegaweb.travel.business.TimeframeNotFoundException,java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean getIfDay(com.idega.presentation.IWContext p0,int p1,int p2)throws java.rmi.RemoteException,java.rmi.RemoteException, java.rmi.RemoteException;
 public void finalizeHotelCreation(com.idega.block.trade.stockroom.data.Product p0) throws java.rmi.RemoteException, FinderException ;
 public int createHotel(int p0,java.lang.Integer p1,java.lang.String p2,java.lang.String p3,java.lang.String p4,int p5,boolean p6,int p7)throws java.lang.Exception, java.rmi.RemoteException;
}
