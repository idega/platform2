package is.idega.idegaweb.travel.service.tour.business;

import is.idega.idegaweb.travel.service.tour.data.*;
import is.idega.idegaweb.travel.business.*;
import com.idega.block.trade.stockroom.data.Product;
import is.idega.idegaweb.travel.data.*;
import com.idega.core.data.*;
import com.idega.util.*;
import java.sql.SQLException;
import java.util.*;
import com.idega.presentation.IWContext;
import com.idega.transaction.IdegaTransactionManager;
import javax.transaction.TransactionManager;
import com.idega.presentation.ui.DropdownMenu;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class TourBusiness extends TravelStockroomBusiness {

  public TourBusiness() {
  }

  public int updateTourService(int tourId,int supplierId, Integer fileId, String serviceName, String number, String serviceDescription, boolean isValid, String departureFrom, idegaTimestamp departureTime, String arrivalAt, idegaTimestamp arrivalTime, String[] pickupPlaceIds,  int[] activeDays, Integer numberOfSeats, Integer minNumberOfSeats, Integer numberOfDays, Float kilometers, int estimatedSeatsUsed, int discountTypeId) throws Exception{
    return createTourService(tourId,supplierId, fileId, serviceName, number, serviceDescription, isValid, departureFrom, departureTime, arrivalAt, arrivalTime, pickupPlaceIds, activeDays, numberOfSeats, minNumberOfSeats, numberOfDays, kilometers, estimatedSeatsUsed, discountTypeId);
  }

  public int createTourService(int supplierId, Integer fileId, String serviceName, String number, String serviceDescription, boolean isValid, String departureFrom, idegaTimestamp departureTime, String arrivalAt, idegaTimestamp arrivalTime, String[] pickupPlaceIds,  int[] activeDays, Integer numberOfSeats, Integer minNumberOfSeats, Integer numberOfDays, Float kilometers, int estimatedSeatsUsed, int discountTypeId) throws Exception {
    return createTourService(-1,supplierId, fileId, serviceName, number, serviceDescription, isValid, departureFrom, departureTime, arrivalAt, arrivalTime, pickupPlaceIds, activeDays, numberOfSeats,minNumberOfSeats, numberOfDays, kilometers, estimatedSeatsUsed, discountTypeId);
  }

  private int createTourService(int tourId, int supplierId, Integer fileId, String serviceName, String number,  String serviceDescription, boolean isValid, String departureFrom, idegaTimestamp departureTime, String arrivalAt, idegaTimestamp arrivalTime, String[] pickupPlaceIds,  int[] activeDays, Integer numberOfSeats, Integer minNumberOfSeats,Integer numberOfDays, Float kilometers, int estimatedSeatsUsed, int discountTypeId) throws Exception {

      boolean isError = false;

      /**
       * @todo handle isError og pickupTime
       */
      if (super.timeframe == null) isError = true;
      if (activeDays.length == 0) isError = true;

      int departureAddressTypeId = AddressType.getId(uniqueDepartureAddressType);
      int arrivalAddressTypeId = AddressType.getId(uniqueArrivalAddressType);
      int hotelPickupAddressTypeId = AddressType.getId(uniqueHotelPickupAddressType);

      Address departureAddress = null;
      Address arrivalAddress = null;

      if (tourId == -1) {

        departureAddress = new Address();
        departureAddress.setAddressTypeID(departureAddressTypeId);
        departureAddress.setStreetName(departureFrom);
        departureAddress.insert();

        arrivalAddress = new Address();
        arrivalAddress.setAddressTypeID(arrivalAddressTypeId);
        arrivalAddress.setStreetName(arrivalAt);
        arrivalAddress.insert();

      }else {
          Service service = new Service(tourId);
          Address[] tempAddresses = (Address[]) (service.findRelated( (Address) Address.getStaticInstance(Address.class), Address.getColumnNameAddressTypeId(), Integer.toString(arrivalAddressTypeId)));
          if (tempAddresses.length > 0) {
            arrivalAddress = new Address(tempAddresses[tempAddresses.length -1].getID());
            arrivalAddress.setAddressTypeID(arrivalAddressTypeId);
            arrivalAddress.setStreetName(arrivalAt);
            arrivalAddress.update();
          }else {
            arrivalAddress = new Address();
            arrivalAddress.setAddressTypeID(arrivalAddressTypeId);
            arrivalAddress.setStreetName(arrivalAt);
            arrivalAddress.insert();
          }

          tempAddresses = (Address[]) (service.findRelated( (Address) Address.getStaticInstance(Address.class), Address.getColumnNameAddressTypeId(), Integer.toString(departureAddressTypeId)));
          if (tempAddresses.length > 0) {
            departureAddress = new Address(tempAddresses[tempAddresses.length -1].getID());
            departureAddress.setAddressTypeID(departureAddressTypeId);
            departureAddress.setStreetName(departureFrom);
            departureAddress.update();
          }else {
            departureAddress = new Address();
            departureAddress.setAddressTypeID(departureAddressTypeId);
            departureAddress.setStreetName(departureFrom);
            departureAddress.insert();
          }

      }


      int[] departureAddressIds = {departureAddress.getID()};
      int[] arrivalAddressIds = {arrivalAddress.getID()};
      int[] hotelPickupPlaceIds ={};
      if (pickupPlaceIds != null && pickupPlaceIds.length > 0 && !pickupPlaceIds[0].equals("") ) hotelPickupPlaceIds = new int[pickupPlaceIds.length];
      for (int i = 0; i < hotelPickupPlaceIds.length; i++) {
        hotelPickupPlaceIds[i] = Integer.parseInt(pickupPlaceIds[i]);
      }

      int serviceId = -1;
      if (tourId == -1) {
        serviceId = createService(supplierId, fileId, serviceName, number, serviceDescription, isValid, departureAddressIds, departureTime.getTimestamp(), arrivalTime.getTimestamp(), discountTypeId);
      }else {
        serviceId = updateService(tourId,supplierId, fileId, serviceName, number, serviceDescription, isValid, departureAddressIds, departureTime.getTimestamp(), arrivalTime.getTimestamp(), discountTypeId);
      }

//      javax.transaction.TransactionManager tm = com.idega.transaction.IdegaTransactionManager.getInstance();
      if (serviceId == -1)
      System.err.println("Tour Business : UpdateTour() - serviceID == -1");

      if (serviceId != -1)
      try {
          //tm.begin();
          Service service = new Service(serviceId);

          Tour tour;
          if (tourId == -1) {
            tour = new Tour();
            tour.setID(serviceId);
          }else {
            tour = new Tour(tourId);
          }
          if (numberOfSeats != null)
            tour.setTotalSeats(numberOfSeats.intValue());
          if (minNumberOfSeats != null)
            tour.setMinumumSeats(minNumberOfSeats.intValue());
          if (numberOfDays != null)
            tour.setNumberOfDays(numberOfDays.intValue());
          if (kilometers != null)
            tour.setLength(kilometers.floatValue());

          if (estimatedSeatsUsed != -1)
            tour.setEstimatedSeatsUsed(estimatedSeatsUsed);

          if (arrivalAddressIds.length > 0)
          for (int i = 0; i < arrivalAddressIds.length; i++) {
            try {
              service.addTo(Address.class,arrivalAddressIds[i]);
            }catch (SQLException sql) {}
          }


          if(hotelPickupPlaceIds.length > 0){
            for (int i = 0; i < hotelPickupPlaceIds.length; i++) {
              if (hotelPickupPlaceIds[i] != -1)
              try{
              service.addTo(new HotelPickupPlace(hotelPickupPlaceIds[i]));
              }catch (SQLException sql) {}
            }
            tour.setHotelPickup(true);
          }else{
            tour.setHotelPickup(false);
          }

          if (tourId == -1) {
            tour.insert();
          }else {
            tour.update();
          }

          ServiceDay.deleteService(serviceId);

          if (activeDays.length > 0) {
            ServiceDay sDay;
            for (int i = 0; i < activeDays.length; i++) {
              sDay = new ServiceDay();
                sDay.setServiceId(serviceId);
                sDay.setDayOfWeek(activeDays[i]);
              sDay.insert();
            }
          }


          //tm.commit();
      }catch (Exception e) {
          e.printStackTrace(System.err);
          //tm.rollback();
      }

      return serviceId;
  }

  public static int getNumberOfTours(int serviceId, idegaTimestamp fromStamp, idegaTimestamp toStamp) {
    int returner = 0;
    try {
      idegaTimestamp toTemp = new idegaTimestamp(toStamp);

      int counter = 0;

      int[] daysOfWeek = ServiceDay.getDaysOfWeek(serviceId);
      int fromDayOfWeek = fromStamp.getDayOfWeek();
      int toDayOfWeek = toStamp.getDayOfWeek();

      toTemp.addDays(1);
      int daysBetween = toStamp.getDaysBetween(fromStamp, toTemp);

      if (fromStamp.getWeekOfYear() != toTemp.getWeekOfYear()) {
          daysBetween = daysBetween - (8 - fromDayOfWeek + toDayOfWeek);

          for (int i = 0; i < daysOfWeek.length; i++) {
              if (daysOfWeek[i]  >= fromDayOfWeek) {
                ++counter;
              }
              if (daysOfWeek[i] <= toDayOfWeek) {
                ++counter;
              }
          }

          counter += ( (daysBetween / 7) * daysOfWeek.length );

      }else {
          for (int i = 0; i < daysOfWeek.length; i++) {
              if ((daysOfWeek[i]  >= fromDayOfWeek) && (daysOfWeek[i] <= toDayOfWeek)) {
                ++counter;
              }
          }
      }
      returner = counter;

    }catch (Exception e) {
        e.printStackTrace(System.err);
    }

    return returner;
  }


  public static Tour getTour(Product product) throws TourNotFoundException{
    Tour tour = null;
    try {
      tour = new Tour(product.getID());
    }
    catch (SQLException sql) {
      throw new TourNotFoundException();
    }
    return tour;
  }

  public static class TourNotFoundException extends Exception{
    TourNotFoundException(){
      super("Tour not found");
    }
  }


  public static DropdownMenu getDepartureDaysDropdownMenu(IWContext iwc, List days, String name) {
    DropdownMenu menu = new DropdownMenu(name);
    idegaTimestamp stamp;

    for (int i = 0; i < days.size(); i++) {
      stamp = (idegaTimestamp) days.get(i);
      menu.addMenuElement(stamp.toSQLDateString(),stamp.getLocaleDate(iwc));
    }

    return menu;
  }

  public static boolean getIfDay(IWContext iwc, Contract contract, Tour tour, idegaTimestamp stamp) {
    try {
      if (!getIfDayInManyDayTour(tour, stamp)) return false;
      return TravelStockroomBusiness.getIfDay(iwc, contract, new Product(tour.getID()), stamp);
    }catch (Exception e) {
      e.printStackTrace(System.err);
      return false;
    }
  }

  public static boolean getIfDay(IWContext iwc, Tour tour, idegaTimestamp stamp, boolean includePast) {
    try {
      if (!getIfDayInManyDayTour(tour, stamp)) return false;
      return TravelStockroomBusiness.getIfDay(iwc, new Product(tour.getID()), stamp, includePast);
    }catch (Exception e) {
      e.printStackTrace(System.err);
      return false;
    }
  }

  private static boolean getIfDayInManyDayTour(Tour tour, idegaTimestamp stamp) throws SQLException {
    int numberOfDays = tour.getNumberOfDays();
    if (numberOfDays > 1) {
      Timeframe frame = new Product(tour.getID()).getTimeframe();
      idegaTimestamp from = new idegaTimestamp(frame.getFrom());
      if (frame.getIfYearly()) {
        from.setYear(stamp.getYear());
      }
      int daysBetween = from.getDaysBetween(from, stamp);
      if (daysBetween % numberOfDays != 0) {
        return false;
      }
    }
    return true;
  }


  public static List getDepartureDays(IWContext iwc, Tour tour) {
    return getDepartureDays(iwc, tour, true);
  }

  public static List getDepartureDays(IWContext iwc, Tour tour, boolean showPast) {
    return getDepartureDays(iwc, tour, null, null, showPast);
  }

  public static List getDepartureDays(IWContext iwc, Tour tour, idegaTimestamp fromStamp, idegaTimestamp toStamp, boolean showPast) {
    List returner = new Vector();
    try {
      Product product = new Product(tour.getID());
      Service service = new Service(tour.getID());
      Timeframe frame = product.getTimeframe();
      if (frame == null) return returner;
      boolean yearly = frame.getIfYearly();

      idegaTimestamp tFrom = new idegaTimestamp(frame.getFrom());
      idegaTimestamp tTo = new idegaTimestamp(frame.getTo());


          idegaTimestamp from = null;
          if (fromStamp != null) from = new idegaTimestamp(fromStamp);
          idegaTimestamp to = null;
          if (toStamp != null) to = new idegaTimestamp(toStamp);

          int numberOfDays = tour.getNumberOfDays();
            if (numberOfDays < 1) numberOfDays = 1;

          if (from == null) {
            from = new idegaTimestamp(tFrom);
          }
          if (to == null) {
            to   = new idegaTimestamp(tTo);
          }

          int toMonth = tTo.getMonth();
          int toM = to.getMonth();
          int fromM = from.getMonth();
          int yearsBetween = 0;

          to.addDays(1);

            if (yearly) {
              int fromYear = tFrom.getYear();
              int toYear   = tTo.getYear();
              int fromY = from.getYear();
              int toY = to.getYear();

              int daysBetween = idegaTimestamp.getDaysBetween(from, to);

              if (fromYear == toYear) {
                from.setYear(fromYear);
              }else {
                  if (fromY >= toYear) {
                    if (fromM > toMonth) {
                      from.setYear(fromYear);
                    }else {
                      from.setYear(toYear);
                    }
                  }
              }

              to = new idegaTimestamp(from);
                to.addDays(daysBetween);

              yearsBetween = to.getYear() - toY;
            }

          idegaTimestamp stamp = new idegaTimestamp(from);
          idegaTimestamp temp;

      idegaTimestamp now = idegaTimestamp.RightNow();

          while (to.isLaterThan(stamp)) {
            temp = getNextAvailableDay(iwc, service, stamp);
            if (temp != null) {
              if (idegaTimestamp.isInTimeframe(tFrom, tTo, temp, yearly)) {
                if (yearly) {
                  temp.addYears(-yearsBetween);
                }
                if (!showPast) {
                  if (temp.isLaterThanOrEquals(now)) {
                    returner.add(temp);
                    stamp = new idegaTimestamp(temp);
                  }else {
                    stamp = new idegaTimestamp(temp);
                  }
                }else {
                  returner.add(temp);
                  stamp = new idegaTimestamp(temp);
                }
                if (yearly) {
                  stamp.addYears(yearsBetween);
                }
              }
            }
            stamp.addDays(numberOfDays);
          }

    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }

    return returner;
  }

  public static idegaTimestamp getNextAvailableDay(IWContext iwc, Service service, idegaTimestamp from) {
    idegaTimestamp stamp = new idegaTimestamp(from);
    boolean found = false;
    for (int i = 1; i <= 7; i++) {
      if (TravelStockroomBusiness.getIfDay(iwc,service.getID(), stamp.getDayOfWeek())) {
        found = true;
        break;
      }else {
        stamp.addDays(1);
      }
    }

    if (found) {
      return stamp;
    }else {
      return null;
    }
  }


}
