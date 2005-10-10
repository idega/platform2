/*
 * $Id: HotelBusiness.java,v 1.12 2005/10/10 10:57:26 gimmi Exp $
 * Created on Aug 12, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.service.hotel.business;

import is.idega.idegaweb.travel.business.ServiceNotFoundException;
import is.idega.idegaweb.travel.business.TimeframeNotFoundException;
import is.idega.idegaweb.travel.business.TravelStockroomBusiness;
import is.idega.idegaweb.travel.service.hotel.data.Hotel;
import java.rmi.RemoteException;
import java.util.List;
import javax.ejb.FinderException;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.Timeframe;
import com.idega.business.IBOService;
import com.idega.data.IDOLookupException;
import com.idega.presentation.IWContext;
import com.idega.util.IWTimestamp;


/**
 * 
 *  Last modified: $Date: 2005/10/10 10:57:26 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.12 $
 */
public interface HotelBusiness extends IBOService, TravelStockroomBusiness {

	/**
	 * @see is.idega.idegaweb.travel.service.hotel.business.HotelBusinessBean#createHotel
	 */
	public int createHotel(int supplierId, Integer fileId, String name, String number, String description,
			int numberOfUnits, int maxPerUnit, boolean isValid, int discountTypeId, int[] roomTypeIds,
			int[] hotelTypeIds, Float rating) throws Exception, java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.service.hotel.business.HotelBusinessBean#updateHotel
	 */
	public int updateHotel(int serviceId, int supplierId, Integer fileId, String name, String number,
			String description, int numberOfUnits, int maxPerUnit, boolean isValid, int discountTypeId,
			int[] roomTypeIds, int[] hotelTypeIds, Float rating) throws Exception, java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.service.hotel.business.HotelBusinessBean#finalizeHotelCreation
	 */
	public void finalizeHotelCreation(Product product) throws FinderException, RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.service.hotel.business.HotelBusinessBean#getIfDay
	 */
	public boolean getIfDay(IWContext iwc, int serviceId, int dayOfWeek) throws RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.service.hotel.business.HotelBusinessBean#getIfDay
	 */
	public boolean getIfDay(IWContext iwc, Product product, Timeframe[] timeframes, IWTimestamp stamp,
			boolean includePast, boolean fixTimeframe) throws ServiceNotFoundException, TimeframeNotFoundException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.service.hotel.business.HotelBusinessBean#getDepartureDays
	 */
	public List getDepartureDays(IWContext iwc, Product product, IWTimestamp fromStamp, IWTimestamp toStamp,
			boolean showPast) throws FinderException, RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.service.hotel.business.HotelBusinessBean#getHotel
	 */
	public Hotel getHotel(Object pk) throws IDOLookupException, FinderException, java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.service.hotel.business.HotelBusinessBean#getMaxBookings
	 */
	public int getMaxBookings(Product product, IWTimestamp stamp) throws RemoteException, FinderException;

	/**
	 * @see is.idega.idegaweb.travel.service.hotel.business.HotelBusinessBean#supportsSupplyPool
	 */
	public boolean supportsSupplyPool() throws java.rmi.RemoteException;
	public boolean invalidateHotel(String hotelID);
	public boolean invalidateHotel(String hotelID, String remoteDomainToExclude);

}
