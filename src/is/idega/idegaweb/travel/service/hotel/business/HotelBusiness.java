package is.idega.idegaweb.travel.service.hotel.business;

import javax.ejb.*;

public interface HotelBusiness extends is.idega.idegaweb.travel.business.TravelStockroomBusiness
{
 public int updateHotel(int p0,int p1,java.lang.Integer p2,java.lang.String p3,java.lang.String p4,java.lang.String p5,int p6,boolean p7,int p8)throws java.lang.Exception, java.rmi.RemoteException;
 public int createHotel(int p0,java.lang.Integer p1,java.lang.String p2,java.lang.String p3,java.lang.String p4,int p5,boolean p6,int p7)throws java.lang.Exception, java.rmi.RemoteException;
}
