package is.idega.idegaweb.travel.service.carrental.business;

import javax.ejb.*;

public interface CarRentalBusiness extends is.idega.idegaweb.travel.business.TravelStockroomBusiness
{
 public int updateCar(int p0,int p1,java.lang.Integer p2,java.lang.String p3,java.lang.String p4,java.lang.String p5,int[] p6,java.lang.String p7,com.idega.util.IWTimestamp p8,java.lang.String p9,com.idega.util.IWTimestamp p10,boolean p11,int p12)throws java.lang.Exception, java.rmi.RemoteException;
 public int createCar(int p0,java.lang.Integer p1,java.lang.String p2,java.lang.String p3,java.lang.String p4,int[] p5,java.lang.String p6,com.idega.util.IWTimestamp p7,java.lang.String p8,com.idega.util.IWTimestamp p9,boolean p10,int p11)throws java.lang.Exception, java.rmi.RemoteException;
}
