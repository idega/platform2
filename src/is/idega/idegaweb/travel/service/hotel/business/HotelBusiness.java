package is.idega.idegaweb.travel.service.hotel.business;

import javax.ejb.*;
import com.idega.util.idegaTimestamp;

public interface HotelBusiness extends is.idega.idegaweb.travel.business.TravelStockroomBusiness
{
 public int createHotel(int supplierId, Integer fileId, String name, String number, String description, int[] activeDays, String departureFrom, idegaTimestamp departureTime, String arrivalAt, idegaTimestamp arrivalTime, boolean isValid, int discountTypeId) throws Exception;
 public int updateHotel(int serviceId, int supplierId, Integer fileId, String name, String number, String description, int[] activeDays,String departureFrom, idegaTimestamp departureTime, String arrivalAt, idegaTimestamp arrivalTime, boolean isValid, int discountTypeId) throws Exception;
}
