package is.idega.idegaweb.travel.service.hotel.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.util.IWTimestamp;


public interface HotelHome extends com.idega.data.IDOHome
{
 public Hotel create() throws javax.ejb.CreateException;
 public Hotel findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Collection find(IWTimestamp fromStamp, IWTimestamp toStamp, Object[] roomTypeId, Object[] hotelTypeId, Object[] postalCodeId, Object[] supplierId, float minRating, float maxRating) throws FinderException;
 public Collection find(IWTimestamp fromStamp, IWTimestamp toStamp, Object[] roomTypeId, Object[] postalCodeId, Object[] supplierId) throws FinderException;

}