package is.idega.idegaweb.travel.business;


public interface TravelStockroomBusiness extends com.idega.block.trade.stockroom.business.StockroomBusiness
{
 public void addSupplies(int p0,float p1) throws java.rmi.RemoteException;
 public int createPriceCategory(int p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,java.lang.String p4,int p5)throws java.lang.Exception, java.rmi.RemoteException;
 public int createPriceCategory(int p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,java.lang.String p4,int p5,int p6)throws java.lang.Exception, java.rmi.RemoteException;
 public int createService(int p0,java.lang.Integer p1,java.lang.String p2,java.lang.String p3,java.lang.String p4,boolean p5,int[] p6,java.sql.Timestamp p7,java.sql.Timestamp p8,int p9)throws java.lang.Exception, java.rmi.RemoteException;
 public void depleteSupplies(int p0,float p1) throws java.rmi.RemoteException;
 public com.idega.block.trade.stockroom.data.Timeframe fixTimeframe(com.idega.block.trade.stockroom.data.Timeframe p0,com.idega.util.IWTimestamp p1) throws java.rmi.RemoteException;
 public com.idega.block.trade.stockroom.data.Timeframe fixTimeframe(com.idega.block.trade.stockroom.data.Timeframe p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2) throws java.rmi.RemoteException;
 public int getCurrencyIdForIceland() throws java.rmi.RemoteException;
 public java.util.List getDepartureDays(com.idega.presentation.IWContext p0,com.idega.block.trade.stockroom.data.Product p1,com.idega.util.IWTimestamp p2,com.idega.util.IWTimestamp p3,boolean p4)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.List getDepartureDays(com.idega.presentation.IWContext p0,com.idega.block.trade.stockroom.data.Product p1)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.List getDepartureDays(com.idega.presentation.IWContext p0,com.idega.block.trade.stockroom.data.Product p1,boolean p2)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.List getDepartureDays(com.idega.presentation.IWContext p0,com.idega.block.trade.stockroom.data.Product p1,com.idega.util.IWTimestamp p2,com.idega.util.IWTimestamp p3)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public boolean getIfDay(com.idega.presentation.IWContext p0,is.idega.idegaweb.travel.data.Contract p1,com.idega.block.trade.stockroom.data.Product p2,com.idega.util.IWTimestamp p3)throws is.idega.idegaweb.travel.business.ServiceNotFoundException,is.idega.idegaweb.travel.business.TimeframeNotFoundException,java.sql.SQLException,java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean getIfDay(com.idega.presentation.IWContext p0,com.idega.block.trade.stockroom.data.Product p1,com.idega.block.trade.stockroom.data.Timeframe[] p2,com.idega.util.IWTimestamp p3)throws is.idega.idegaweb.travel.business.ServiceNotFoundException,is.idega.idegaweb.travel.business.TimeframeNotFoundException,java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean getIfDay(com.idega.presentation.IWContext p0,com.idega.block.trade.stockroom.data.Product p1,com.idega.block.trade.stockroom.data.Timeframe[] p2,com.idega.util.IWTimestamp p3,boolean p4,boolean p5)throws is.idega.idegaweb.travel.business.ServiceNotFoundException,is.idega.idegaweb.travel.business.TimeframeNotFoundException,java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean getIfDay(com.idega.presentation.IWContext p0,com.idega.block.trade.stockroom.data.Product p1,com.idega.util.IWTimestamp p2)throws is.idega.idegaweb.travel.business.ServiceNotFoundException,is.idega.idegaweb.travel.business.TimeframeNotFoundException,java.sql.SQLException,java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean getIfDay(com.idega.presentation.IWContext p0,int p1,int p2)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean getIfExpired(is.idega.idegaweb.travel.data.Contract p0,com.idega.util.IWTimestamp p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getMaxBookings(com.idega.block.trade.stockroom.data.Product p0,com.idega.util.IWTimestamp p1)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public int getMinBookings(com.idega.block.trade.stockroom.data.Product p0,com.idega.util.IWTimestamp p1)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public com.idega.block.trade.stockroom.data.PriceCategory[] getMiscellaneousServices(int p0) throws java.rmi.RemoteException;
 public is.idega.idegaweb.travel.business.TravelStockroomBusinessBean getNewInstance(com.idega.idegaweb.IWApplicationContext p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.trade.stockroom.data.PriceCategory[] getPriceCategories(int p0) throws java.rmi.RemoteException;
 public com.idega.block.trade.stockroom.data.PriceCategory[] getPriceCategories(java.lang.String p0) throws java.rmi.RemoteException;
 public com.idega.block.trade.stockroom.data.Product[] getProducts(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.trade.stockroom.data.Product[] getProducts(com.idega.block.trade.stockroom.data.Reseller p0) throws java.rmi.RemoteException;
 public com.idega.block.trade.stockroom.data.Product[] getProducts(int p0,com.idega.util.IWTimestamp p1)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public com.idega.block.trade.stockroom.data.Product[] getProducts(int p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.util.datastructures.HashtableDoubleKeyed getResellerDayHashtable(com.idega.presentation.IWContext p0) throws java.rmi.RemoteException;
 public is.idega.idegaweb.travel.data.ResellerDayHome getResellerDayHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public is.idega.idegaweb.travel.data.Service getService(com.idega.block.trade.stockroom.data.Product p0)throws is.idega.idegaweb.travel.business.ServiceNotFoundException,java.rmi.RemoteException, java.rmi.RemoteException;
 public is.idega.idegaweb.travel.data.ServiceDayHome getServiceDayHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public float getSupplyStatus(int p0)throws java.sql.SQLException, java.rmi.RemoteException;
 public float getSupplyStatus(int p0,java.sql.Date p1)throws java.sql.SQLException, java.rmi.RemoteException;
 public com.idega.block.trade.stockroom.data.Timeframe getTimeframe(com.idega.block.trade.stockroom.data.Product p0)throws java.rmi.RemoteException,is.idega.idegaweb.travel.business.ServiceNotFoundException,is.idega.idegaweb.travel.business.TimeframeNotFoundException, java.rmi.RemoteException;
 public java.util.Collection getTravelAddressIdsFromRefill(com.idega.block.trade.stockroom.data.Product p0,com.idega.block.trade.stockroom.data.TravelAddress p1)throws java.rmi.RemoteException,com.idega.data.IDOFinderException, java.rmi.RemoteException;
 public java.util.Collection getTravelAddressIdsFromRefill(com.idega.block.trade.stockroom.data.Product p0,int p1)throws java.rmi.RemoteException,com.idega.data.IDOFinderException,javax.ejb.FinderException, java.rmi.RemoteException;
 public boolean isWithinTimeframe(com.idega.block.trade.stockroom.data.Timeframe p0,com.idega.util.IWTimestamp p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void removeDepartureDaysApplication(com.idega.idegaweb.IWApplicationContext p0,com.idega.block.trade.stockroom.data.Product p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void removeResellerHashtables(com.idega.presentation.IWContext p0) throws java.rmi.RemoteException;
 public void removeServiceDayHashtable(com.idega.presentation.IWContext p0) throws java.rmi.RemoteException;
 public void setSupplyStatus(int p0,float p1,int p2)throws java.sql.SQLException, java.rmi.RemoteException;
 public void setTimeframe(int p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2,boolean p3)throws java.sql.SQLException, java.rmi.RemoteException;
 public void setTimeframe(com.idega.util.IWTimestamp p0,com.idega.util.IWTimestamp p1,boolean p2)throws java.sql.SQLException, java.rmi.RemoteException;
 public int updateService(int p0,int p1,java.lang.Integer p2,java.lang.String p3,java.lang.String p4,java.lang.String p5,boolean p6,int[] p7,java.sql.Timestamp p8,java.sql.Timestamp p9,int p10)throws java.lang.Exception, java.rmi.RemoteException;
}
