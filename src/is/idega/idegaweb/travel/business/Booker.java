package is.idega.idegaweb.travel.business;

import java.util.Collection;

public interface Booker extends com.idega.business.IBOService
{
 public int Book(int p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,java.lang.String p4,java.lang.String p5,java.lang.String p6,com.idega.util.IWTimestamp p7,int p8,int p9,java.lang.String p10,int p11,int p12,int p13,int p14,java.lang.String p15, String code)throws java.rmi.RemoteException,javax.ejb.CreateException, java.rmi.RemoteException;
 public int BookBySupplier(int p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,java.lang.String p4,java.lang.String p5,java.lang.String p6,com.idega.util.IWTimestamp p7,int p8,java.lang.String p9,int p10,int p11,int p12,int p13,java.lang.String p14, String code)throws java.rmi.RemoteException,javax.ejb.CreateException, java.rmi.RemoteException;
 public is.idega.idegaweb.travel.interfaces.Booking[] collectionToBookingsArray(java.util.Collection p0) throws java.rmi.RemoteException;
 public boolean deleteBooking(is.idega.idegaweb.travel.interfaces.Booking p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean deleteBooking(int p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public int getAvailableItems(com.idega.block.trade.stockroom.data.ProductPrice p0,com.idega.util.IWTimestamp p1) throws java.rmi.RemoteException;
 public is.idega.idegaweb.travel.data.BookingEntry[] getBookingEntries(is.idega.idegaweb.travel.interfaces.Booking p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public float getBookingEntryPrice(is.idega.idegaweb.travel.data.BookingEntry p0,is.idega.idegaweb.travel.interfaces.Booking p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public float getBookingPrice(java.util.List p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public float getBookingPrice(is.idega.idegaweb.travel.data.GeneralBooking[] p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public float getBookingPrice(is.idega.idegaweb.travel.interfaces.Booking[] p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public float getBookingPrice(is.idega.idegaweb.travel.interfaces.Booking p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 //public is.idega.idegaweb.travel.interfaces.Booking[] getBookings(int[] p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2,int[] p3)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 //public is.idega.idegaweb.travel.interfaces.Booking[] getBookings(int[] p0,com.idega.util.IWTimestamp p1,int[] p2)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public is.idega.idegaweb.travel.interfaces.Booking[] getBookings(int[] p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2,int[] p3,com.idega.block.trade.stockroom.data.TravelAddress p4)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public is.idega.idegaweb.travel.interfaces.Booking[] getBookings(int p0,com.idega.util.IWTimestamp p1)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 //public is.idega.idegaweb.travel.interfaces.Booking[] getBookings(int p0,com.idega.util.IWTimestamp p1,int[] p2)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public is.idega.idegaweb.travel.interfaces.Booking[] getBookings(int p0,com.idega.util.IWTimestamp p1,com.idega.block.trade.stockroom.data.TravelAddress p2)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 //public is.idega.idegaweb.travel.interfaces.Booking[] getBookings(int p0,com.idega.util.IWTimestamp p1,int p2)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 //public is.idega.idegaweb.travel.interfaces.Booking[] getBookings(java.util.List p0,int[] p1,com.idega.util.IWTimestamp p2)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 //public is.idega.idegaweb.travel.interfaces.Booking[] getBookings(java.util.List p0,int[] p1,com.idega.util.IWTimestamp p2,com.idega.util.IWTimestamp p3,java.lang.String p4,java.lang.String p5)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public is.idega.idegaweb.travel.interfaces.Booking[] getBookings(java.util.List p0,int[] p1,com.idega.util.IWTimestamp p2,com.idega.util.IWTimestamp p3,java.lang.String p4,java.lang.String p5,boolean p6, boolean validOnly)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 //public is.idega.idegaweb.travel.interfaces.Booking[] getBookings(java.util.List p0,com.idega.util.IWTimestamp p1)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public is.idega.idegaweb.travel.interfaces.Booking[] getBookings(java.util.List p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public is.idega.idegaweb.travel.interfaces.Booking[] getBookings(java.util.List p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2,java.lang.String p3,java.lang.String p4)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 
 public int getBookingsTotalCount(int p0,com.idega.util.IWTimestamp p1, int travelAddressID)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getBookingsTotalCount(com.idega.block.trade.stockroom.data.ProductPrice p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getBookingsTotalCount(int p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2, Collection travelAddresses)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getBookingsTotalCount(int p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2,int p3, Collection travelAddresses)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getBookingsTotalCount(int p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2,int p3,Collection travelAddresses, boolean p4)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getBookingsTotalCount(int p0,com.idega.util.IWTimestamp p1,int p2, Collection travelAddresses)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getBookingsTotalCount(java.util.List p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2,int p3, Collection travelAddresses)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getBookingsTotalCountByReseller(int p0,int p1,com.idega.util.IWTimestamp p2,com.idega.block.trade.stockroom.data.TravelAddress p3)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getBookingsTotalCountByReseller(int p0,int p1,com.idega.util.IWTimestamp p2)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getBookingsTotalCountByResellers(int[] p0,int p1,com.idega.util.IWTimestamp p2)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getBookingsTotalCountByResellers(int p0,com.idega.util.IWTimestamp p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.trade.data.Currency getCurrency(is.idega.idegaweb.travel.interfaces.Booking p0)throws java.sql.SQLException,javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.trade.stockroom.data.TravelAddress getDepartureAddress(is.idega.idegaweb.travel.interfaces.Booking p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public is.idega.idegaweb.travel.data.GeneralBookingHome getGeneralBookingHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.List getMultibleBookings(is.idega.idegaweb.travel.data.GeneralBooking p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public int[] getMultipleBookingNumber(is.idega.idegaweb.travel.data.GeneralBooking p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.lang.String getPaymentType(com.idega.idegaweb.IWResourceBundle p0,int p1) throws java.rmi.RemoteException;
 public com.idega.presentation.ui.DropdownMenu getPaymentTypeDropdown(com.idega.idegaweb.IWResourceBundle p0,java.lang.String p1) throws java.rmi.RemoteException;
 public com.idega.presentation.ui.DropdownMenu getPaymentTypes(com.idega.idegaweb.IWResourceBundle p0) throws java.rmi.RemoteException;
 public com.idega.block.trade.stockroom.business.ProductBusiness getProductBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public is.idega.idegaweb.travel.service.business.ProductCategoryFactory getProductCategoryFactory()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public is.idega.idegaweb.travel.service.business.ServiceHandler getServiceHandler()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.Object getServiceType(int p0) throws java.rmi.RemoteException;
 public void removeBookingPriceApplication(is.idega.idegaweb.travel.interfaces.Booking p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean setPickup(int p0,int p1,java.lang.String p2)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int updateBooking(int p0,int p1,java.lang.String p2,java.lang.String p3,java.lang.String p4,java.lang.String p5,java.lang.String p6,java.lang.String p7,com.idega.util.IWTimestamp p8,int p9,java.lang.String p10,int p11,int p12,int p13,int p14,java.lang.String p15, String p16)throws java.rmi.RemoteException,javax.ejb.CreateException, java.rmi.RemoteException;
}
