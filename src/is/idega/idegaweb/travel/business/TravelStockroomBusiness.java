package is.idega.idegaweb.travel.business;

import is.idega.idegaweb.travel.data.Contract;
import is.idega.idegaweb.travel.data.ResellerDayHome;
import is.idega.idegaweb.travel.data.Service;
import is.idega.idegaweb.travel.data.ServiceDayHome;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import javax.ejb.FinderException;
import com.idega.block.trade.stockroom.business.StockroomBusiness;
import com.idega.block.trade.stockroom.data.PriceCategory;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.Reseller;
import com.idega.block.trade.stockroom.data.SupplyPool;
import com.idega.block.trade.stockroom.data.Timeframe;
import com.idega.block.trade.stockroom.data.TravelAddress;
import com.idega.data.IDOFinderException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.util.IWTimestamp;
import com.idega.util.datastructures.HashtableDoubleKeyed;


/**
 * @author gimmi
 */
public interface TravelStockroomBusiness extends StockroomBusiness {

	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#getNewInstance
	 */
	public TravelStockroomBusinessBean getNewInstance(IWApplicationContext iwac) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#addSupplies
	 */
	public void addSupplies(int product_id, float amount) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#depleteSupplies
	 */
	public void depleteSupplies(int product_id, float amount) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#setSupplyStatus
	 */
	public void setSupplyStatus(int productId, float status, int period) throws SQLException, java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#getSupplyStatus
	 */
	public float getSupplyStatus(int productId) throws SQLException, java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#getSupplyStatus
	 */
	public float getSupplyStatus(int productId, Date date) throws SQLException, java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#createPriceCategory
	 */
	public int createPriceCategory(int supplierId, String name, String description, String type, String extraInfo,
			int visibility) throws Exception, java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#createPriceCategory
	 */
	public int createPriceCategory(int supplierId, String name, String description, String type, String extraInfo,
			int visibility, int parentId) throws Exception, java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#createService
	 */
	public int createService(int supplierId, Integer fileId, String serviceName, String number,
			String serviceDescription, boolean isValid, int[] addressIds, Timestamp departure, Timestamp arrival,
			int discountTypeId) throws Exception, java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#updateService
	 */
	public int updateService(int serviceId, int supplierId, Integer fileId, String serviceName, String number,
			String serviceDescription, boolean isValid, int[] addressIds, Timestamp departure, Timestamp arrival,
			int discountTypeId) throws Exception, java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#setTimeframe
	 */
	public void setTimeframe(IWTimestamp from, IWTimestamp to, boolean yearly) throws SQLException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#setTimeframe
	 */
	public void setTimeframe(int timeframeId, IWTimestamp from, IWTimestamp to, boolean yearly) throws SQLException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#getProducts
	 */
	public Product[] getProducts(Reseller reseller) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#getProducts
	 */
	public Product[] getProducts(int supplierId) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#getProducts
	 */
	public Product[] getProducts(int supplierId, IWTimestamp stamp) throws RemoteException, FinderException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#getProducts
	 */
	public Product[] getProducts(int supplierId, IWTimestamp from, IWTimestamp to) throws FinderException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#getService
	 */
	public Service getService(Product product) throws ServiceNotFoundException, RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#getTimeframe
	 */
	public Timeframe getTimeframe(Product product) throws RemoteException, ServiceNotFoundException,
			TimeframeNotFoundException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#getPriceCategories
	 */
	public PriceCategory[] getPriceCategories(int supplierId) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#getPriceCategories
	 */
	public PriceCategory[] getPriceCategories(String key) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#getMiscellaneousServices
	 */
	public PriceCategory[] getMiscellaneousServices(int supplierId) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#getIfDay
	 */
	public boolean getIfDay(IWContext iwc, int productId, int dayOfWeek) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#getIfDay
	 */
	public boolean getIfDay(IWContext iwc, Product product, IWTimestamp stamp) throws ServiceNotFoundException,
			TimeframeNotFoundException, SQLException, RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#getIfDay
	 */
	public boolean getIfDay(IWContext iwc, Product product, Timeframe[] timeframes, IWTimestamp stamp)
			throws ServiceNotFoundException, TimeframeNotFoundException, RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#getIfDay
	 */
	public boolean getIfDay(IWContext iwc, Product product, Timeframe[] timeframes, IWTimestamp stamp,
			boolean includePast, boolean fixTimeframe) throws ServiceNotFoundException, TimeframeNotFoundException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#fixTimeframe
	 */
	public Timeframe fixTimeframe(Timeframe frame, IWTimestamp stamp) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#fixTimeframe
	 */
	public Timeframe fixTimeframe(Timeframe frame, IWTimestamp from, IWTimestamp to) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#getResellerDayHashtable
	 */
	public HashtableDoubleKeyed getResellerDayHashtable(IWContext iwc) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#removeResellerHashtables
	 */
	public void removeResellerHashtables(IWContext iwc) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#removeServiceDayHashtable
	 */
	public void removeServiceDayHashtable(IWContext iwc) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#getIfExpired
	 */
	public boolean getIfExpired(Contract contract, IWTimestamp stamp) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#getIfDay
	 */
	public boolean getIfDay(IWContext iwc, Contract contract, Product product, IWTimestamp stamp)
			throws ServiceNotFoundException, TimeframeNotFoundException, SQLException, RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#getCurrencyIdForIceland
	 */
	public int getCurrencyIdForIceland() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#getDepartureDays
	 */
	public List getDepartureDays(IWContext iwc, Product product) throws RemoteException, FinderException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#getDepartureDays
	 */
	public List getDepartureDays(IWContext iwc, Product product, boolean showPast) throws RemoteException,
			FinderException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#getDepartureDays
	 */
	public List getDepartureDays(IWContext iwc, Product product, IWTimestamp from, IWTimestamp to)
			throws RemoteException, FinderException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#getDepartureDays
	 */
	public List getDepartureDays(IWContext iwc, Product product, IWTimestamp fromStamp, IWTimestamp toStamp,
			boolean showPast) throws RemoteException, FinderException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#getWeekDays
	 */
	public int[] getWeekDays(Product product) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#isWithinTimeframe
	 */
	public boolean isWithinTimeframe(Timeframe timeframe, IWTimestamp stamp) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#getServiceDayHome
	 */
	public ServiceDayHome getServiceDayHome() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#getResellerDayHome
	 */
	public ResellerDayHome getResellerDayHome() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#getTravelAddressIdsFromRefill
	 */
	public Collection getTravelAddressIdsFromRefill(Product product, int tAddressId) throws RemoteException,
			IDOFinderException, FinderException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#getTravelAddressIdsFromRefill
	 */
	public Collection getTravelAddressIdsFromRefill(Product product, TravelAddress tAddress) throws RemoteException,
			IDOFinderException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#removeDepartureDaysApplication
	 */
	public void removeDepartureDaysApplication(IWApplicationContext iwac, Product product) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#getProductsSharingPool
	 */
	public Collection getProductsSharingPool(Product product) throws RemoteException;

	public void invalidateMaxDayCache(Collection products) throws RemoteException;
	public void invalidateMaxDayCache(SupplyPool supplyPool) throws RemoteException;
	
	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#getMaxBookings
	 */
	public int getMaxBookings(Product product, IWTimestamp stamp) throws RemoteException, FinderException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#getMinBookings
	 */
	public int getMinBookings(Product product, IWTimestamp stamp) throws RemoteException, FinderException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelStockroomBusinessBean#supportsSupplyPool
	 */
	public boolean supportsSupplyPool() throws java.rmi.RemoteException;
}
