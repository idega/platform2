package is.idega.idegaweb.travel.data;

import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.block.trade.stockroom.data.TravelAddress;
import com.idega.data.IDOHome;
import com.idega.util.IWTimestamp;


/**
 * @author gimmi
 */
public interface GeneralBookingHome extends IDOHome {

	public GeneralBooking create() throws javax.ejb.CreateException;

	public GeneralBooking findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#ejbFindBookings
	 */
	public Collection findBookings(int resellerId, int serviceId, IWTimestamp stamp, TravelAddress travelAddress)
			throws FinderException;

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#ejbFindBookings
	 */
	public Collection findBookings(int[] resellerIds, int serviceId, IWTimestamp stamp, TravelAddress travelAddress)
			throws FinderException;

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#ejbFindBookings
	 */
	public Collection findBookings(int[] resellerIds, int serviceId, IWTimestamp stamp, String code,
			TravelAddress travelAddress) throws FinderException;

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#ejbHomeGetBookingsTotalCount
	 */
	public int getBookingsTotalCount(int[] resellerIds, int serviceId, IWTimestamp stamp);

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#ejbHomeGetNumberOfBookings
	 */
	public int getNumberOfBookings(int[] resellerIds, int serviceId, IWTimestamp stamp, Collection travelAddressIds);

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#ejbHomeGetBookingsTotalCount
	 */
	public int getBookingsTotalCount(int[] resellerIds, int serviceId, IWTimestamp stamp, Collection travelAddressIds);

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#ejbHomeGetBookingsTotalCount
	 */
	public int getBookingsTotalCount(int[] resellerIds, int serviceId, IWTimestamp stamp, Collection travelAddressIds,
			boolean returnsTotalCountInsteadOfNumberOfBookings, String code);

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#ejbHomeGetNumberOfBookings
	 */
	public int getNumberOfBookings(int serviceId, IWTimestamp fromStamp, IWTimestamp toStamp, int bookingType);

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#ejbHomeGetBookingsTotalCount
	 */
	public int getBookingsTotalCount(int serviceId, IWTimestamp fromStamp, IWTimestamp toStamp, int bookingType,
			int[] productPriceIds);

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#ejbHomeGetBookingsTotalCount
	 */
	public int getBookingsTotalCount(int serviceId, IWTimestamp fromStamp, IWTimestamp toStamp, int bookingType,
			int[] productPriceIds, Collection travelAddressIds);

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#ejbHomeGetBookingsTotalCountByDateOfBooking
	 */
	public int getBookingsTotalCountByDateOfBooking(int serviceId, IWTimestamp fromStamp, IWTimestamp toStamp,
			int bookingType, int[] productPriceIds, Collection travelAddressIds);

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#ejbHomeGetBookingsTotalCount
	 */
	public int getBookingsTotalCount(int serviceId, IWTimestamp fromStamp, IWTimestamp toStamp, int bookingType,
			int[] productPriceIds, Collection travelAddressIds, boolean useDateOfBookingColumn);

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#ejbHomeGetBookingsTotalCount
	 */
	public int getBookingsTotalCount(Collection serviceIds, IWTimestamp fromStamp, IWTimestamp toStamp,
			int bookingType, int[] productPriceIds, Collection travelAddressIds, boolean useDateOfBookingColumn,
			boolean returnTotalCountInsteadOfBookingCount, String code);

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#ejbFindBookings
	 */
	public Collection findBookings(int[] serviceIds, IWTimestamp fromStamp, IWTimestamp toStamp, int[] bookingTypeIds,
			String columnName, String columnValue, TravelAddress address, String code, boolean validOnly)
			throws FinderException, RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#ejbFindBookingsByDateOfBooking
	 */
	public Collection findBookingsByDateOfBooking(int[] serviceIds, IWTimestamp fromStamp, IWTimestamp toStamp,
			int[] bookingTypeIds, String columnName, String columnValue, TravelAddress address, String code,
			boolean validOnly) throws FinderException, RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#ejbFindBookings
	 */
	public Collection findBookings(int[] serviceIds, IWTimestamp fromStamp, IWTimestamp toStamp, int[] bookingTypeIds,
			String columnName, String columnValue, TravelAddress address, String dateColumn, String code)
			throws FinderException, RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#ejbFindBookings
	 */
	public Collection findBookings(int[] serviceIds, IWTimestamp fromStamp, IWTimestamp toStamp, int[] bookingTypeIds,
			String columnName, String columnValue, TravelAddress address, String dateColumn, String code,
			boolean validOnly) throws FinderException, RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#ejbHomeGetMultibleBookings
	 */
	public Collection getMultibleBookings(GeneralBooking booking) throws RemoteException, FinderException;

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#ejbFindAllByCode
	 */
	public Collection findAllByCode(String code) throws FinderException;

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#ejbFindAllByReferenceNumber
	 */
	public Collection findAllByReferenceNumber(String refNum) throws FinderException;

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#ejbFindByAuthorizationNumber
	 */
	public GeneralBooking findByAuthorizationNumber(String number, IWTimestamp stamp) throws FinderException;
}
