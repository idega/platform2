package is.idega.idegaweb.travel.data;

import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.block.trade.stockroom.data.TravelAddress;
import com.idega.data.IDOFactory;
import com.idega.util.IWTimestamp;


/**
 * @author gimmi
 */
public class GeneralBookingHomeImpl extends IDOFactory implements GeneralBookingHome {

	protected Class getEntityInterfaceClass() {
		return GeneralBooking.class;
	}

	public GeneralBooking create() throws javax.ejb.CreateException {
		return (GeneralBooking) super.createIDO();
	}

	public GeneralBooking findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (GeneralBooking) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findBookings(int resellerId, int serviceId, IWTimestamp stamp, TravelAddress travelAddress)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((GeneralBookingBMPBean) entity).ejbFindBookings(resellerId, serviceId, stamp,
				travelAddress);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBookings(int[] resellerIds, int serviceId, IWTimestamp stamp, TravelAddress travelAddress)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((GeneralBookingBMPBean) entity).ejbFindBookings(resellerIds, serviceId, stamp,
				travelAddress);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBookings(int[] resellerIds, int serviceId, IWTimestamp stamp, String code,
			TravelAddress travelAddress) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((GeneralBookingBMPBean) entity).ejbFindBookings(resellerIds, serviceId, stamp,
				code, travelAddress);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public int getBookingsTotalCount(int[] resellerIds, int serviceId, IWTimestamp stamp) {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((GeneralBookingBMPBean) entity).ejbHomeGetBookingsTotalCount(resellerIds, serviceId, stamp);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getNumberOfBookings(int[] resellerIds, int serviceId, IWTimestamp stamp, Collection travelAddressIds) {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((GeneralBookingBMPBean) entity).ejbHomeGetNumberOfBookings(resellerIds, serviceId, stamp,
				travelAddressIds);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getBookingsTotalCount(int[] resellerIds, int serviceId, IWTimestamp stamp, Collection travelAddressIds) {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((GeneralBookingBMPBean) entity).ejbHomeGetBookingsTotalCount(resellerIds, serviceId, stamp,
				travelAddressIds);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getBookingsTotalCount(int[] resellerIds, int serviceId, IWTimestamp stamp, Collection travelAddressIds,
			boolean returnsTotalCountInsteadOfNumberOfBookings, String code) {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((GeneralBookingBMPBean) entity).ejbHomeGetBookingsTotalCount(resellerIds, serviceId, stamp,
				travelAddressIds, returnsTotalCountInsteadOfNumberOfBookings, code);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getNumberOfBookings(int serviceId, IWTimestamp fromStamp, IWTimestamp toStamp, int bookingType) {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((GeneralBookingBMPBean) entity).ejbHomeGetNumberOfBookings(serviceId, fromStamp, toStamp,
				bookingType);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getBookingsTotalCount(int serviceId, IWTimestamp fromStamp, IWTimestamp toStamp, int bookingType,
			int[] productPriceIds) {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((GeneralBookingBMPBean) entity).ejbHomeGetBookingsTotalCount(serviceId, fromStamp, toStamp,
				bookingType, productPriceIds);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getBookingsTotalCount(int serviceId, IWTimestamp fromStamp, IWTimestamp toStamp, int bookingType,
			int[] productPriceIds, Collection travelAddressIds) {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((GeneralBookingBMPBean) entity).ejbHomeGetBookingsTotalCount(serviceId, fromStamp, toStamp,
				bookingType, productPriceIds, travelAddressIds);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getBookingsTotalCountByDateOfBooking(int serviceId, IWTimestamp fromStamp, IWTimestamp toStamp,
			int bookingType, int[] productPriceIds, Collection travelAddressIds) {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((GeneralBookingBMPBean) entity).ejbHomeGetBookingsTotalCountByDateOfBooking(serviceId,
				fromStamp, toStamp, bookingType, productPriceIds, travelAddressIds);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getBookingsTotalCount(int serviceId, IWTimestamp fromStamp, IWTimestamp toStamp, int bookingType,
			int[] productPriceIds, Collection travelAddressIds, boolean useDateOfBookingColumn) {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((GeneralBookingBMPBean) entity).ejbHomeGetBookingsTotalCount(serviceId, fromStamp, toStamp,
				bookingType, productPriceIds, travelAddressIds, useDateOfBookingColumn);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getBookingsTotalCount(Collection serviceIds, IWTimestamp fromStamp, IWTimestamp toStamp,
			int bookingType, int[] productPriceIds, Collection travelAddressIds, boolean useDateOfBookingColumn,
			boolean returnTotalCountInsteadOfBookingCount, String code) {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((GeneralBookingBMPBean) entity).ejbHomeGetBookingsTotalCount(serviceIds, fromStamp, toStamp,
				bookingType, productPriceIds, travelAddressIds, useDateOfBookingColumn,
				returnTotalCountInsteadOfBookingCount, code);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public Collection findBookings(int[] serviceIds, IWTimestamp fromStamp, IWTimestamp toStamp, int[] bookingTypeIds,
			String columnName, String columnValue, TravelAddress address, String code, boolean validOnly)
			throws FinderException, RemoteException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((GeneralBookingBMPBean) entity).ejbFindBookings(serviceIds, fromStamp, toStamp,
				bookingTypeIds, columnName, columnValue, address, code, validOnly);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBookingsByDateOfBooking(int[] serviceIds, IWTimestamp fromStamp, IWTimestamp toStamp,
			int[] bookingTypeIds, String columnName, String columnValue, TravelAddress address, String code,
			boolean validOnly) throws FinderException, RemoteException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((GeneralBookingBMPBean) entity).ejbFindBookingsByDateOfBooking(serviceIds,
				fromStamp, toStamp, bookingTypeIds, columnName, columnValue, address, code, validOnly);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBookings(int[] serviceIds, IWTimestamp fromStamp, IWTimestamp toStamp, int[] bookingTypeIds,
			String columnName, String columnValue, TravelAddress address, String dateColumn, String code)
			throws FinderException, RemoteException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((GeneralBookingBMPBean) entity).ejbFindBookings(serviceIds, fromStamp, toStamp,
				bookingTypeIds, columnName, columnValue, address, dateColumn, code);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBookings(int[] serviceIds, IWTimestamp fromStamp, IWTimestamp toStamp, int[] bookingTypeIds,
			String columnName, String columnValue, TravelAddress address, String dateColumn, String code,
			boolean validOnly) throws FinderException, RemoteException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((GeneralBookingBMPBean) entity).ejbFindBookings(serviceIds, fromStamp, toStamp,
				bookingTypeIds, columnName, columnValue, address, dateColumn, code, validOnly);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection getMultibleBookings(GeneralBooking booking) throws RemoteException, FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection theReturn = ((GeneralBookingBMPBean) entity).ejbHomeGetMultibleBookings(booking);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public Collection findAllByCode(String code) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((GeneralBookingBMPBean) entity).ejbFindAllByCode(code);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByReferenceNumber(String refNum) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((GeneralBookingBMPBean) entity).ejbFindAllByReferenceNumber(refNum);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public GeneralBooking findByAuthorizationNumber(String number, IWTimestamp stamp) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((GeneralBookingBMPBean) entity).ejbFindByAuthorizationNumber(number, stamp);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}
}
