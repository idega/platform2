package is.idega.idegaweb.travel.service.tour.business;

import is.idega.idegaweb.travel.business.TravelStockroomBusiness;

import com.idega.util.IWTimestamp;

public interface TourBusiness extends TravelStockroomBusiness
{
 public int getMaxBookings(com.idega.block.trade.stockroom.data.Product p0,com.idega.util.IWTimestamp p1)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public int getMinBookings(com.idega.block.trade.stockroom.data.Product p0,com.idega.util.IWTimestamp p1)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.List getDepartureDays(com.idega.presentation.IWContext p0,com.idega.block.trade.stockroom.data.Product p1,com.idega.util.IWTimestamp p2,com.idega.util.IWTimestamp p3,boolean p4) throws java.rmi.RemoteException;
 public int updateTourService(int tourId,int supplierId, Integer fileId, String serviceName, String number, String serviceDescription, boolean isValid, String[] tourTypeIDs, String departureFrom, IWTimestamp departureTime, String arrivalAt, IWTimestamp arrivalTime, String[] pickupPlaceIds,  int[] activeDays, Integer numberOfSeats, Integer minNumberOfSeats, Integer numberOfDays, Float kilometers, int estimatedSeatsUsed, int discountTypeId) throws Exception;
// public int updateTourService(int p0,int p1,java.lang.Integer p2,java.lang.String p3,java.lang.String p4,java.lang.String p5,boolean p6,java.lang.String p7,com.idega.util.IWTimestamp p8,java.lang.String p9,com.idega.util.IWTimestamp p10,java.lang.String[] p11,int[] p12,java.lang.Integer p13,java.lang.Integer p14,java.lang.Integer p15,java.lang.Float p16,int p17,int p18)throws java.lang.Exception, java.rmi.RemoteException;
 public is.idega.idegaweb.travel.service.tour.data.Tour getTour(com.idega.block.trade.stockroom.data.Product p0)throws is.idega.idegaweb.travel.service.tour.business.TourNotFoundException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.List getDepartureDays(com.idega.presentation.IWContext p0,com.idega.block.trade.stockroom.data.Product p1) throws java.rmi.RemoteException;
 public com.idega.util.IWTimestamp getNextAvailableDay(com.idega.presentation.IWContext p0,is.idega.idegaweb.travel.service.tour.data.Tour p1,com.idega.block.trade.stockroom.data.Product p2,com.idega.util.IWTimestamp p3)throws java.sql.SQLException,java.rmi.RemoteException, java.rmi.RemoteException;
 public int getNumberOfTours(int p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2) throws java.rmi.RemoteException;
 public boolean getIfDay(com.idega.presentation.IWContext p0,com.idega.block.trade.stockroom.data.Product p1,com.idega.util.IWTimestamp p2,boolean p3) throws java.rmi.RemoteException;
 public com.idega.presentation.ui.DropdownMenu getDepartureDaysDropdownMenu(com.idega.presentation.IWContext p0,java.util.List p1,java.lang.String p2) throws java.rmi.RemoteException;
 public int createTourService(int supplierId, Integer fileId, String serviceName, String number, String serviceDescription, boolean isValid, String[] tourTypeIDs, String departureFrom, IWTimestamp departureTime, String arrivalAt, IWTimestamp arrivalTime, String[] pickupPlaceIds,  int[] activeDays, Integer numberOfSeats, Integer minNumberOfSeats, Integer numberOfDays, Float kilometers, int estimatedSeatsUsed, int discountTypeId) throws Exception;
// public int createTourService(int p0,java.lang.Integer p1,java.lang.String p2,java.lang.String p3,java.lang.String p4,boolean p5,java.lang.String p6,com.idega.util.IWTimestamp p7,java.lang.String p8,com.idega.util.IWTimestamp p9,java.lang.String[] p10,int[] p11,java.lang.Integer p12,java.lang.Integer p13,java.lang.Integer p14,java.lang.Float p15,int p16,int p17)throws java.lang.Exception, java.rmi.RemoteException;
 public boolean getIfDay(com.idega.presentation.IWContext p0,is.idega.idegaweb.travel.data.Contract p1,com.idega.block.trade.stockroom.data.Product p2,com.idega.util.IWTimestamp p3) throws java.rmi.RemoteException;
 public com.idega.util.IWTimestamp getNextAvailableDay(com.idega.presentation.IWContext p0,is.idega.idegaweb.travel.service.tour.data.Tour p1,com.idega.block.trade.stockroom.data.Product p2,com.idega.block.trade.stockroom.data.Timeframe[] p3,com.idega.util.IWTimestamp p4) throws java.rmi.RemoteException;
 public java.util.List getDepartureDays(com.idega.presentation.IWContext p0,com.idega.block.trade.stockroom.data.Product p1,boolean p2) throws java.rmi.RemoteException;
 public com.idega.util.IWTimestamp getNextAvailableDay(com.idega.presentation.IWContext p0,is.idega.idegaweb.travel.service.tour.data.Tour p1,com.idega.block.trade.stockroom.data.Product p2,com.idega.block.trade.stockroom.data.Timeframe p3,com.idega.util.IWTimestamp p4) throws java.rmi.RemoteException;
 public boolean invalidateTour(String tourID);
  public boolean invalidateTour(String tourID, String remoteDomainToExclude);

}
