package is.idega.idegaweb.travel.service.carrental.business;

import is.idega.idegaweb.travel.business.TravelStockroomBusiness;


public interface CarRentalBusiness extends TravelStockroomBusiness
{
 public int createCar(int p0,java.lang.Integer p1,java.lang.String p2,java.lang.String p3,java.lang.String p4,int[] p5,java.lang.String[] p6,java.lang.String[] p7,boolean p8,int p9)throws java.lang.Exception, java.rmi.RemoteException;
 public int updateCar(int p0,int p1,java.lang.Integer p2,java.lang.String p3,java.lang.String p4,java.lang.String p5,int[] p6,java.lang.String[] p7,java.lang.String[] p8,boolean p9,int p10)throws java.lang.Exception, java.rmi.RemoteException;
}
