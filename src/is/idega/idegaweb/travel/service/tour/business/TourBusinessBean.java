package is.idega.idegaweb.travel.service.tour.business;

import javax.ejb.FinderException;
import com.idega.data.IDOAddRelationshipException;
import com.idega.business.IBOLookup;
import java.rmi.RemoteException;
import com.idega.data.IDOLookup;
import is.idega.idegaweb.travel.service.tour.data.*;
import is.idega.idegaweb.travel.business.*;
import com.idega.block.trade.stockroom.data.*;
import com.idega.block.trade.stockroom.business.*;
import is.idega.idegaweb.travel.data.*;
import com.idega.core.data.*;
import com.idega.util.*;
import java.sql.SQLException;
import java.util.*;
import com.idega.presentation.IWContext;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.transaction.IdegaTransactionManager;
import javax.transaction.TransactionManager;
import com.idega.presentation.ui.DropdownMenu;
import is.idega.idegaweb.travel.service.business.*;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class TourBusinessBean extends TravelStockroomBusinessBean implements TourBusiness{

  public TourBusinessBean() {
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

      int hotelPickupAddressTypeId = com.idega.core.data.AddressTypeBMPBean.getId(ProductBusiness.uniqueHotelPickupAddressType);


      int[] departureAddressIds = setDepartureAddress(tourId, departureFrom, departureTime);
      int[] arrivalAddressIds = setArrivalAddress(tourId, arrivalAt);

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

      javax.transaction.UserTransaction userT = getSessionContext().getUserTransaction();

      if (serviceId != -1)
      try {
          userT.begin();
          Service service = ((is.idega.idegaweb.travel.data.ServiceHome)com.idega.data.IDOLookup.getHome(Service.class)).findByPrimaryKey(new Integer(serviceId));
          Product product = ProductBusiness.getProduct(serviceId);// Product(serviceId);
          Tour tour;

          if (tourId == -1) {
            tour = ((is.idega.idegaweb.travel.service.tour.data.TourHome)com.idega.data.IDOLookup.getHome(Tour.class)).create();
            tour.setPrimaryKey(new Integer(serviceId));
          }else {
            tour = ((is.idega.idegaweb.travel.service.tour.data.TourHome)com.idega.data.IDOLookup.getHome(Tour.class)).findByPrimaryKey(new Integer(tourId));
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
              product.addTo(Address.class,arrivalAddressIds[i]);
            }catch (SQLException sql) {}
          }


          HotelPickupPlaceHome hppHome = (HotelPickupPlaceHome) IDOLookup.getHome(HotelPickupPlace.class);
          service.removeAllHotelPickupPlaces();
//          hppHome.create().removeFromService(service);
          //service.removeFrom(HotelPickupPlace.class);

          if(hotelPickupPlaceIds.length > 0){
            for (int i = 0; i < hotelPickupPlaceIds.length; i++) {
              if (hotelPickupPlaceIds[i] != -1)
              try{
                ((is.idega.idegaweb.travel.data.HotelPickupPlaceHome)com.idega.data.IDOLookup.getHome(HotelPickupPlace.class)).findByPrimaryKey(new Integer(hotelPickupPlaceIds[i])).addToService(service);
//                service.addTo(((is.idega.idegaweb.travel.data.HotelPickupPlaceHome)com.idega.data.IDOLookup.getHome(HotelPickupPlace.class)).findByPrimaryKey(new Integer(hotelPickupPlaceIds[i])));
              }catch (IDOAddRelationshipException sql) {}
            }
            tour.setHotelPickup(true);
          }else{
            tour.setHotelPickup(false);
          }

          tour.store();


          this.removeDepartureDaysApplication(this.getIWApplicationContext(), tour);
          setActiveDays(serviceId, activeDays);


          ProductCategory pCat = ( (ProductCategoryHome) IDOLookup.getHomeLegacy(ProductCategory.class)).getProductCategory(ProductCategoryFactoryBean.CATEGORY_TYPE_TOUR);
          try {
            if (pCat != null) {
              product.removeFrom(ProductCategory.class);
              pCat.addTo(Product.class, serviceId);
      //        product.addTo(pCat);
            }
          }catch (SQLException sql) {
          }

          userT.commit();
      }catch (Exception e) {
          e.printStackTrace(System.err);
          userT.rollback();
      }


      return serviceId;
  }

  public int getNumberOfTours(int serviceId, idegaTimestamp fromStamp, idegaTimestamp toStamp) {
    int returner = 0;
    try {
      idegaTimestamp toTemp = new idegaTimestamp(toStamp);

      int counter = 0;

      int[] daysOfWeek = new int[]{};//is.idega.idegaweb.travel.data.ServiceDayBMPBean.getDaysOfWeek(serviceId);
      try {
        ServiceDayHome sdayHome = (ServiceDayHome) IDOLookup.getHome(ServiceDay.class);
        ServiceDay sDay = sdayHome.create();
        daysOfWeek = sDay.getDaysOfWeek(serviceId);
      }catch (Exception e) {
        e.printStackTrace(System.err);
      }

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


  public Tour getTour(Product product) throws TourNotFoundException, RemoteException{
    Tour tour = null;
    try {
      tour = ((is.idega.idegaweb.travel.service.tour.data.TourHome)com.idega.data.IDOLookup.getHome(Tour.class)).findByPrimaryKey(product.getPrimaryKey());
    }
    catch (FinderException sql) {
      throw new TourNotFoundException();
    }
    return tour;
  }



  public DropdownMenu getDepartureDaysDropdownMenu(IWContext iwc, List days, String name) {
    DropdownMenu menu = new DropdownMenu(name);
    idegaTimestamp stamp;

    for (int i = 0; i < days.size(); i++) {
      stamp = (idegaTimestamp) days.get(i);
      menu.addMenuElement(stamp.toSQLDateString(),stamp.getLocaleDate(iwc));
    }

    return menu;
  }

  /**
   * return a date if the inserted date is part of a tour
   */
  private idegaTimestamp getDepartureDateForDate(IWContext iwc, Tour tour, idegaTimestamp stamp) throws RemoteException{
    idegaTimestamp returnStamp = null;

    idegaTimestamp stamp1 = null;
    idegaTimestamp stamp2 = null;
    boolean found = false;
    int numberOfDays = tour.getNumberOfDays();

    idegaTimestamp temp1 = new idegaTimestamp(stamp);
      temp1.addDays(numberOfDays);
    idegaTimestamp temp2 = new idegaTimestamp(stamp);
      temp2.addDays(-1 * numberOfDays);


    List days = getDepartureDays(iwc, tour, temp1, temp2, true);

    if ( numberOfDays > 1) {
      for (int i = 0; i < days.size(); i++) {
        if (i == 0) {
          stamp1 = (idegaTimestamp) days.get(0);
          stamp2 = (idegaTimestamp) days.get(1);
          ++i;
        }else {
          stamp1 = (idegaTimestamp) days.get(i-1);
          stamp2 = (idegaTimestamp) days.get(i);
        }

        if (stamp.isLaterThanOrEquals(stamp1) && stamp2.isLaterThan(stamp)) {
          found = true;
          break;
        }
      }

      if (found) {
        int daysBetween = stamp.getDaysBetween(stamp1, stamp);
        if (stamp1.equals(stamp)) {
          return stamp;
        }else if (stamp2.equals(stamp)) {
          return stamp;
        }else if (daysBetween < numberOfDays) {
          return stamp1;
        }else if (daysBetween >= numberOfDays) {
          return null;
        }

      }else {
        return null;
      }
    }else {

    }


    return returnStamp;
  }

  public boolean getIfDay(IWContext iwc, Contract contract, Tour tour, idegaTimestamp stamp) {
    try {
      idegaTimestamp temp = getDepartureDateForDate(iwc, tour, stamp);
      if (temp == null) {
        return getIfDay(iwc, contract, ProductBusiness.getProduct((Integer) tour.getPrimaryKey()), stamp);
      }else {
        return (stamp.equals(temp));
      }

    }catch (Exception e) {
      e.printStackTrace(System.err);
      return false;
    }
  }

  public boolean getIfDay(IWContext iwc, Tour tour, idegaTimestamp stamp, boolean includePast) {
    try {
      idegaTimestamp temp = getDepartureDateForDate(iwc, tour, stamp);
      if (temp == null) {
        Product product = ProductBusiness.getProduct((Integer) tour.getPrimaryKey());
        return getIfDay(iwc, product, product.getTimeframes(), stamp, includePast, true);
      }else {
        return (stamp.equals(temp));
      }
    }catch (Exception e) {
      e.printStackTrace(System.err);
      return false;
    }
  }

  public List getDepartureDays(IWContext iwc, Tour tour) {
    return getDepartureDays(iwc, tour, true);
  }

  public List getDepartureDays(IWContext iwc, Tour tour, boolean showPast) {
    return getDepartureDays(iwc, tour, null, null, showPast);
  }

  public void removeDepartureDaysApplication(IWApplicationContext iwac, Tour tour) throws RemoteException{
    Enumeration enum = iwac.getApplication().getAttributeNames();
    String name;
    while (enum.hasMoreElements()) {
      name = (String) enum.nextElement();
      if (name.indexOf("tourDepDays"+tour.getPrimaryKey().toString()+"_") != -1) {
        iwac.removeApplicationAttribute(name);
      }
    }
  }


  public List getDepartureDays(IWContext iwc, Tour tour, idegaTimestamp fromStamp, idegaTimestamp toStamp, boolean showPast) {
    List returner = new Vector();

    try {
      Product product = ProductBusiness.getProduct((Integer) tour.getPrimaryKey());
      Service service = ((is.idega.idegaweb.travel.data.ServiceHome)com.idega.data.IDOLookup.getHome(Service.class)).findByPrimaryKey(tour.getPrimaryKey());
      Timeframe[] frames = product.getTimeframes();
      Timeframe tempFrame = (Timeframe) IDOLookup.create(Timeframe.class);

      String applicationString = "tourDepDays"+tour.getPrimaryKey().toString()+"_"+fromStamp+"_"+toStamp+"_"+showPast;

      List tempList = (List) iwc.getApplicationAttribute(applicationString);
      if (tempList != null) {
        returner = tempList;
      }else {
//        System.err.println("TourBusiness : getDepartDays : "+fromStamp+ " - " +toStamp);
        for (int i = 0; i < frames.length; i++) {
          //System.err.println("------------------------------------------------");
          //System.err.println("-----------------------"+i+"------------------------");
          //System.err.println("------------------------------------------------");

          boolean yearly = frames[i].getIfYearly();

          idegaTimestamp tFrom = new idegaTimestamp(frames[i].getFrom());
          idegaTimestamp tTo = new idegaTimestamp(frames[i].getTo());

          idegaTimestamp from = null;
          if (fromStamp != null) from = new idegaTimestamp(fromStamp);
          idegaTimestamp to = null;
          if (toStamp != null) to = new idegaTimestamp(toStamp);

//          System.err.println("tFrom... : "+tFrom.toSQLDateString());
//          System.err.println("tTo..... : "+tTo.toSQLDateString());

          int numberOfDays = tour.getNumberOfDays();
            if (numberOfDays < 1) numberOfDays = 1;

          if (from == null) {
            from = new idegaTimestamp(tFrom);
          }
          if (to == null) {
            to   = new idegaTimestamp(tTo);
          }

          from.addDays(-1);
          to.addDays(1);

          int yearsBetween = 0;
          int toY = to.getYear();

          frames[i] = fixTimeframe(frames[i], from, to);
          tFrom = new idegaTimestamp(frames[i].getFrom());
          tTo = new idegaTimestamp(frames[i].getTo());

          int daysBetween = idegaTimestamp.getDaysBetween(from, to);

          to = new idegaTimestamp(from);
            to.addDays(daysBetween);
          yearsBetween = to.getYear() - toY;
//          System.err.println("tFrom : "+tFrom.toSQLDateString());
//          System.err.println("tTo   : "+tTo.toSQLDateString());

/*
          System.err.println("------------------------------------------------");
          System.err.println("from : "+from.toSQLDateString());
          System.err.println("to   : "+to.toSQLDateString());
          System.err.println("------------------------------------------------");
          System.err.println("tFrom... : "+tFrom.toSQLDateString());
          System.err.println("tTo..... : "+tTo.toSQLDateString());
*/

        idegaTimestamp stamp = new idegaTimestamp(from);
        idegaTimestamp temp;

        idegaTimestamp now = idegaTimestamp.RightNow();

        tempFrame.setFrom(tFrom.getTimestamp());
        tempFrame.setTo(tTo.getTimestamp());

          while (to.isLaterThan(stamp)) {
            //System.err.println("Stamp : "+stamp.toSQLDateString());
            temp = getNextAvailableDay(iwc, tour, product,tempFrame, stamp);
//            temp = getNextAvailableDay(iwc, tour, product,frames[i], stamp);
            if (temp != null) {
              if (idegaTimestamp.isInTimeframe(tFrom, tTo, temp, yearly)) {
                //System.err.println("TEMP : "+temp.toSQLDateString()+" .... yearsBetween : "+yearsBetween+" ... yearly ("+yearly+")");
                if (yearly) {
                  temp.addYears(-yearsBetween);
                }
                //System.err.println("TEMP : "+temp.toSQLDateString());
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
              //stamp = new idegaTimestamp(temp);
            }else {
              stamp.addDays(numberOfDays);
            }

          }
          //System.err.println("STAMP : "+stamp.toSQLDateString());
        }
        iwc.setApplicationAttribute(applicationString, returner);
      }
//      Exception ex = new  Exception("Repps");
//        throw ex;
    }catch (Exception sql) {
      sql.printStackTrace(System.err);
    }

    return returner;
  }

  public idegaTimestamp getNextAvailableDay(IWContext iwc, Tour tour, Product product, Timeframe timeframe, idegaTimestamp from) {
    return getNextAvailableDay(iwc, tour, product, new Timeframe[] {timeframe}, from);
  }

  public idegaTimestamp getNextAvailableDay(IWContext iwc, Tour tour, Product product,  idegaTimestamp from) throws SQLException {
    return getNextAvailableDay(iwc, tour, product, product.getTimeframes(), from);
  }

  public idegaTimestamp getNextAvailableDay(IWContext iwc, Tour tour, Product product, Timeframe[] timeframes, idegaTimestamp from) {
    idegaTimestamp stamp = new idegaTimestamp(from);
    boolean found = false;
/**
 * @todo Speed up....
 */
    try {
      int nod = tour.getNumberOfDays();
      if (nod < 1) nod = 1;
      int teljari = 0;


      while (teljari++ < nod) {
        stamp.addDays(1);
        if (getIfDay(iwc,product, timeframes, stamp, false, true)) {
          /** @todo breytti false i true..... skoda takk */
          found = true;
          break;
        }

      }

    }catch (Exception e) {
      e.printStackTrace(System.err);
    }
    if (found) {
      return stamp;
    }else {
      return null;
    }
  }
/*
  protected TravelStockroomBusiness getTravelStockroomBusiness() throws RemoteException {
    return (TravelStockroomBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), TravelStockroomBusiness.class);
  }*/
}
