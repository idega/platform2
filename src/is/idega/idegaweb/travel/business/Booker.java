package is.idega.idegaweb.travel.business;

import is.idega.idegaweb.travel.data.BookingEntry;
import is.idega.idegaweb.travel.data.GeneralBooking;
import is.idega.idegaweb.travel.data.GeneralBookingHome;
import is.idega.idegaweb.travel.interfaces.Booking;
import is.idega.idegaweb.travel.service.business.ProductCategoryFactory;
import is.idega.idegaweb.travel.service.business.ServiceHandler;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.block.trade.data.Currency;
import com.idega.block.trade.stockroom.business.ProductBusiness;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.ProductPrice;
import com.idega.block.trade.stockroom.data.TravelAddress;
import com.idega.business.IBOService;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.util.IWTimestamp;


/**
 * @author gimmi
 */
public interface Booker extends IBOService {

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#BookBySupplier
	 */
	public int BookBySupplier(int serviceId, String country, String name, String address, String city,
			String telephoneNumber, String email, IWTimestamp date, int totalCount, String postalCode, int paymentType,
			int userId, int ownerId, int addressId, String comment, String code, String referenceNumber)
			throws RemoteException, CreateException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#Book
	 */
	public int Book(int serviceId, String country, String name, String address, String city, String telephoneNumber,
			String email, IWTimestamp date, int totalCount, int bookingType, String postalCode, int paymentType,
			int userId, int ownerId, int addressId, String comment, String code, String referenceNumber)
			throws RemoteException, CreateException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#updateBooking
	 */
	public int updateBooking(int bookingId, int serviceId, String country, String name, String address, String city,
			String telephoneNumber, String email, IWTimestamp date, int totalCount, String postalCode, int paymentType,
			int userId, int ownerId, int addressId, String comment, String code, String referenceNumber)
			throws RemoteException, CreateException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#setPickup
	 */
	public boolean setPickup(int bookingId, int pickupPlaceId, String pickupInfo) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#getBookingsTotalCountByResellers
	 */
	public int getBookingsTotalCountByResellers(int serviceId, IWTimestamp stamp) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#getBookingsTotalCountByResellers
	 */
	public int getBookingsTotalCountByResellers(int[] resellerIds, int serviceId, IWTimestamp stamp)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#getBookingsTotalCountByReseller
	 */
	public int getBookingsTotalCountByReseller(int resellerId, int serviceId, IWTimestamp stamp) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#getBookingsTotalCountByReseller
	 */
	public int getBookingsTotalCountByReseller(int resellerId, int serviceId, IWTimestamp stamp,
			TravelAddress travelAddress) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#getBookingsTotalCount
	 */
	public int getBookingsTotalCount(List products, IWTimestamp fromStamp, IWTimestamp toStamp, int bookingType,
			Collection travelAddresses) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#getBookingsTotalCount
	 */
	public int getBookingsTotalCount(int serviceId, IWTimestamp stamp, int travelAddressID) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#getBookingsTotalCount
	 */
	public int getBookingsTotalCount(int serviceId, IWTimestamp stamp, int bookingType, Collection travelAddresses)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#getBookingsTotalCount
	 */
	public int getBookingsTotalCount(int serviceId, IWTimestamp fromStamp, IWTimestamp toStamp,
			Collection travelAddresses) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#getBookingsTotalCount
	 */
	public int getBookingsTotalCount(int serviceId, IWTimestamp fromStamp, IWTimestamp toStamp, int bookingType,
			Collection travelAddresses) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#getBookingsTotalCount
	 */
	public int getBookingsTotalCount(int serviceId, IWTimestamp fromStamp, IWTimestamp toStamp, int bookingType,
			Collection travelAddresses, boolean orderByDateOfBooking) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#getBookings
	 */
	public Booking[] getBookings(List products, IWTimestamp stamp) throws RemoteException, FinderException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#getBookings
	 */
	public Booking[] getBookings(List products, int[] bookingTypeIds, IWTimestamp stamp) throws RemoteException,
			FinderException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#getBookings
	 */
	public Booking[] getBookings(List products, IWTimestamp fromStamp, IWTimestamp toStamp) throws RemoteException,
			FinderException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#getBookings
	 */
	public Booking[] getBookings(List products, IWTimestamp fromStamp, IWTimestamp toStamp, String columnName,
			String columnValue) throws RemoteException, FinderException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#getBookings
	 */
	public Booking[] getBookings(List products, int[] bookingTypeIds, IWTimestamp fromStamp, IWTimestamp toStamp,
			String columnName, String columnValue) throws RemoteException, FinderException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#getBookings
	 */
	public Booking[] getBookings(List products, int[] bookingTypeIds, IWTimestamp fromStamp, IWTimestamp toStamp,
			String columnName, String columnValue, boolean searchByDateOfBooking, boolean validOnly)
			throws RemoteException, FinderException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#getBookingsTotalCountByOthersInPool
	 */
	public int getBookingsTotalCountByOthersInPool(Product product, IWTimestamp stamp) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#getBookings
	 */
	public Booking[] getBookings(int serviceId, IWTimestamp stamp) throws RemoteException, FinderException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#getBookings
	 */
	public Booking[] getBookings(int serviceId, IWTimestamp stamp, TravelAddress address) throws RemoteException,
			FinderException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#getBookings
	 */
	public Booking[] getBookings(int serviceId, IWTimestamp stamp, int bookingTypeId) throws RemoteException,
			FinderException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#getBookings
	 */
	public Booking[] getBookings(int serviceId, IWTimestamp stamp, int[] bookingTypeIds) throws RemoteException,
			FinderException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#getBookings
	 */
	public Booking[] getBookings(int[] serviceIds, IWTimestamp stamp, int[] bookingTypeIds) throws RemoteException,
			FinderException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#getBookings
	 */
	public Booking[] getBookings(int[] serviceIds, IWTimestamp fromStamp, IWTimestamp toStamp, int[] bookingTypeIds)
			throws RemoteException, FinderException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#getBookings
	 */
	public Booking[] getBookings(int[] serviceIds, IWTimestamp fromStamp, IWTimestamp toStamp, int[] bookingTypeIds,
			TravelAddress address) throws RemoteException, FinderException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#getBookingEntryPrice
	 */
	public float getBookingEntryPrice(BookingEntry entry, Booking booking) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#getBookingPrice
	 */
	public float getBookingPrice(List bookings) throws RemoteException, FinderException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#getBookingPrice
	 */
	public float getBookingPrice(GeneralBooking[] bookings) throws RemoteException, FinderException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#getBookingPrice
	 */
	public float getBookingPrice(Booking[] bookings) throws RemoteException, FinderException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#getBookingPrice
	 */
	public float getBookingPrice(Booking booking) throws RemoteException, FinderException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#removeBookingPriceApplication
	 */
	public void removeBookingPriceApplication(Booking booking) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#getBookingEntries
	 */
	public BookingEntry[] getBookingEntries(Booking booking) throws RemoteException, FinderException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#deleteBooking
	 */
	public boolean deleteBooking(int bookingId) throws RemoteException, FinderException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#deleteBooking
	 */
	public boolean deleteBooking(Booking booking, boolean deleteRelated) throws RemoteException, FinderException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#deleteBooking
	 */
	public boolean deleteBooking(Booking booking) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#getServiceType
	 */
	public Object getServiceType(int serviceId) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#getPaymentTypes
	 */
	public DropdownMenu getPaymentTypes(IWResourceBundle iwrb) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#getPaymentTypeDropdown
	 */
	public DropdownMenu getPaymentTypeDropdown(IWResourceBundle iwrb, String name) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#getPaymentType
	 */
	public String getPaymentType(IWResourceBundle iwrb, int paymentType) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#getCurrency
	 */
	public Currency getCurrency(Booking booking) throws SQLException, FinderException, RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#getDepartureAddress
	 */
	public TravelAddress getDepartureAddress(Booking _booking) throws RemoteException, FinderException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#getMultibleBookings
	 */
	public List getMultibleBookings(GeneralBooking booking) throws RemoteException, FinderException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#getAvailableItems
	 */
	public int getAvailableItems(ProductPrice pPrice, IWTimestamp stamp) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#getMultipleBookingNumber
	 */
	public int[] getMultipleBookingNumber(GeneralBooking booking) throws RemoteException, FinderException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#getBookingsTotalCount
	 */
	public int getBookingsTotalCount(ProductPrice pPrice) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#addCacheKeyToInvalidateOnSave
	 */
	public void addCacheKeyToInvalidateOnSave(String key) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#getGeneralBookingHome
	 */
	public GeneralBookingHome getGeneralBookingHome() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#collectionToBookingsArray
	 */
	public Booking[] collectionToBookingsArray(Collection coll) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#getServiceHandler
	 */
	public ServiceHandler getServiceHandler() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#getProductCategoryFactory
	 */
	public ProductCategoryFactory getProductCategoryFactory() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.BookerBean#getProductBusiness
	 */
	public ProductBusiness getProductBusiness() throws RemoteException;
	
  public void invalidateCache(int bookingID);
}
