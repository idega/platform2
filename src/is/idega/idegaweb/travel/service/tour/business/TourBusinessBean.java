package is.idega.idegaweb.travel.service.tour.business;

import is.idega.idegaweb.travel.business.TravelStockroomBusinessBean;
import is.idega.idegaweb.travel.data.Contract;
import is.idega.idegaweb.travel.data.PickupPlace;
import is.idega.idegaweb.travel.data.PickupPlaceHome;
import is.idega.idegaweb.travel.data.Service;
import is.idega.idegaweb.travel.data.ServiceDay;
import is.idega.idegaweb.travel.data.ServiceDayHome;
import is.idega.idegaweb.travel.service.business.ProductCategoryFactoryBean;
import is.idega.idegaweb.travel.service.tour.data.Tour;
import is.idega.idegaweb.travel.service.tour.data.TourHome;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.block.trade.stockroom.business.ProductBusiness;
import com.idega.block.trade.stockroom.business.ProductBusinessBean;
import com.idega.block.trade.stockroom.business.StockroomBusiness;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.ProductCategory;
import com.idega.block.trade.stockroom.data.ProductCategoryHome;
import com.idega.block.trade.stockroom.data.Timeframe;
import com.idega.business.IBOLookup;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.AddressHome;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.util.IWTimestamp;

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

  public int updateTourService(int tourId,int supplierId, Integer fileId, String serviceName, String number, String serviceDescription, boolean isValid, String[] tourTypeIDs, String departureFrom, IWTimestamp departureTime, String arrivalAt, IWTimestamp arrivalTime, String[] pickupPlaceIds,  int[] activeDays, Integer numberOfSeats, Integer minNumberOfSeats, Integer numberOfDays, Float kilometers, int estimatedSeatsUsed, int discountTypeId) throws Exception{
    return createTourService(tourId,supplierId, fileId, serviceName, number, serviceDescription, isValid, tourTypeIDs, departureFrom, departureTime, arrivalAt, arrivalTime, pickupPlaceIds, activeDays, numberOfSeats, minNumberOfSeats, numberOfDays, kilometers, estimatedSeatsUsed, discountTypeId);
  }

  public int createTourService(int supplierId, Integer fileId, String serviceName, String number, String serviceDescription, boolean isValid, String[] tourTypeIDs, String departureFrom, IWTimestamp departureTime, String arrivalAt, IWTimestamp arrivalTime, String[] pickupPlaceIds,  int[] activeDays, Integer numberOfSeats, Integer minNumberOfSeats, Integer numberOfDays, Float kilometers, int estimatedSeatsUsed, int discountTypeId) throws Exception {
    return createTourService(-1,supplierId, fileId, serviceName, number, serviceDescription, isValid, tourTypeIDs, departureFrom, departureTime, arrivalAt, arrivalTime, pickupPlaceIds, activeDays, numberOfSeats,minNumberOfSeats, numberOfDays, kilometers, estimatedSeatsUsed, discountTypeId);
  }

  private int createTourService(int tourId, int supplierId, Integer fileId, String serviceName, String number,  String serviceDescription, boolean isValid, String[] tourTypeIDs, String departureFrom, IWTimestamp departureTime, String arrivalAt, IWTimestamp arrivalTime, String[] pickupPlaceIds,  int[] activeDays, Integer numberOfSeats, Integer minNumberOfSeats,Integer numberOfDays, Float kilometers, int estimatedSeatsUsed, int discountTypeId) throws Exception {
      boolean isError = false;

      /**
       * @todo handle isError og pickupTime
       */
      if (super.timeframe == null) isError = true;
      if (activeDays.length == 0) isError = true;

      int hotelPickupAddressTypeId = com.idega.core.location.data.AddressTypeBMPBean.getId(ProductBusinessBean.uniqueHotelPickupAddressType);


      int[] departureAddressIds = setDepartureAddress(tourId, departureFrom, departureTime);
      int[] arrivalAddressIds = setArrivalAddress(tourId, arrivalAt);

      int[] hotelPickupPlaceIds ={};
      if (pickupPlaceIds != null && pickupPlaceIds.length > 0 && !pickupPlaceIds[0].equals("") ) {
        hotelPickupPlaceIds = new int[pickupPlaceIds.length];
        for (int i = 0; i < pickupPlaceIds.length; i++) {
          hotelPickupPlaceIds[i] = Integer.parseInt(pickupPlaceIds[i]);
        }
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
          Product product = getProductBusiness().getProduct(serviceId);// Product(serviceId);
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

          if (arrivalAddressIds.length > 0) {
            Address addrs;
            AddressHome aHome = (AddressHome) IDOLookup.getHome(Address.class);
            try {
              for (int i = 0; i < arrivalAddressIds.length; i++) {
                addrs = aHome.findByPrimaryKey(arrivalAddressIds[i]);
                product.addArrivalAddress(addrs);
  //                product.addTo(Address.class,arrivalAddressIds[i]);
              }
            }catch (Exception e) {
              e.printStackTrace(System.err);
            }
          }


          PickupPlaceHome hppHome = (PickupPlaceHome) IDOLookup.getHome(PickupPlace.class);
          service.removeAllHotelPickupPlaces();
//          hppHome.create().removeFromService(service);
          //service.removeFrom(HotelPickupPlace.class);

          if(hotelPickupPlaceIds.length > 0){
            for (int i = 0; i < hotelPickupPlaceIds.length; i++) {
              if (hotelPickupPlaceIds[i] != -1)
              try{
                ((is.idega.idegaweb.travel.data.PickupPlaceHome)com.idega.data.IDOLookup.getHome(PickupPlace.class)).findByPrimaryKey(new Integer(hotelPickupPlaceIds[i])).addToService(service);
//                service.addTo(((is.idega.idegaweb.travel.data.HotelPickupPlaceHome)com.idega.data.IDOLookup.getHome(HotelPickupPlace.class)).findByPrimaryKey(new Integer(hotelPickupPlaceIds[i])));
              }catch (IDOAddRelationshipException sql) {}
            }
            tour.setHotelPickup(true);
          }else{
            tour.setHotelPickup(false);
          }

          tour.store();

          tour.setTourTypes(tourTypeIDs);
          
          


          this.removeDepartureDaysApplication(this.getIWApplicationContext(), product);
          setActiveDays(serviceId, activeDays);


          ProductCategory pCat = ( (ProductCategoryHome) IDOLookup.getHomeLegacy(ProductCategory.class)).getProductCategory(ProductCategoryFactoryBean.CATEGORY_TYPE_TOUR);
          try {
            if (pCat != null) {
              product.removeAllFrom(ProductCategory.class);
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

  public int getNumberOfTours(int serviceId, IWTimestamp fromStamp, IWTimestamp toStamp) {
    int returner = 0;
    try {
      IWTimestamp toTemp = new IWTimestamp(toStamp);

      int counter = 0;

      int[] daysOfWeek = new int[]{};//is.idega.idegaweb.travel.data.ServiceDayBMPBean.getDaysOfWeek(serviceId);
      try {
        ServiceDayHome sdayHome = (ServiceDayHome) IDOLookup.getHome(ServiceDay.class);
        daysOfWeek = sdayHome.getDaysOfWeek(serviceId);
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
    IWTimestamp stamp;

    for (int i = 0; i < days.size(); i++) {
      stamp = (IWTimestamp) days.get(i);
      menu.addMenuElement(stamp.toSQLDateString(),stamp.getLocaleDate(iwc));
    }

    return menu;
  }

  /**
   * return a date if the inserted date is part of a tour
   */
  private IWTimestamp getDepartureDateForDate(IWContext iwc, Product product, IWTimestamp stamp) throws RemoteException, FinderException{
		Tour tour = ( (TourHome) IDOLookup.getHome(Tour.class)).findByPrimaryKey(product.getPrimaryKey());

    IWTimestamp returnStamp = null;

    IWTimestamp stamp1 = null;
    IWTimestamp stamp2 = null;
    boolean found = false;
    int numberOfDays = tour.getNumberOfDays();

    IWTimestamp temp1 = new IWTimestamp(stamp);
      temp1.addDays(numberOfDays);
    IWTimestamp temp2 = new IWTimestamp(stamp);
      temp2.addDays(-1 * numberOfDays);


    List days = getDepartureDays(iwc, product, temp1, temp2, true);

    if ( numberOfDays > 1) {
      for (int i = 0; i < days.size(); i++) {
        if (i == 0) {
          stamp1 = (IWTimestamp) days.get(0);
          stamp2 = (IWTimestamp) days.get(1);
          ++i;
        }else {
          stamp1 = (IWTimestamp) days.get(i-1);
          stamp2 = (IWTimestamp) days.get(i);
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

  public boolean getIfDay(IWContext iwc, Contract contract, Product product, IWTimestamp stamp) {
    try {
      IWTimestamp temp = getDepartureDateForDate(iwc, product, stamp);
      if (temp == null) {
        return super.getIfDay(iwc, contract, product, stamp);
      }else {
        return (stamp.equals(temp));
      }

    }catch (Exception e) {
      e.printStackTrace(System.err);
      return false;
    }
  }

  public boolean getIfDay(IWContext iwc, Product product, IWTimestamp stamp, boolean includePast) {
    try {
      IWTimestamp temp = getDepartureDateForDate(iwc, product, stamp);
      if (temp == null) {
//        Product product = getProductBusiness().getProduct((Integer) tour.getPrimaryKey());
        return super.getIfDay(iwc, product, product.getTimeframes(), stamp, includePast, true);
      }else {
        return (stamp.equals(temp));
      }
    }catch (Exception e) {
      e.printStackTrace(System.err);
      return false;
    }
  }


  public List getDepartureDays(IWContext iwc, Product product) {
    return getDepartureDays(iwc, product, true);
  }

  public List getDepartureDays(IWContext iwc, Product product, boolean showPast) {
    return getDepartureDays(iwc, product, null, null, showPast);
  }

  public List getDepartureDays(IWContext iwc, Product product, IWTimestamp fromStamp, IWTimestamp toStamp, boolean showPast) {
    List returner = new Vector();
    try {
    	Tour tour = ((TourHome) IDOLookup.getHome(Tour.class)).findByPrimaryKey(product.getPrimaryKey());
//      Product product = getProductBusiness().getProduct((Integer) tour.getPrimaryKey());
      Service service = ((is.idega.idegaweb.travel.data.ServiceHome)com.idega.data.IDOLookup.getHome(Service.class)).findByPrimaryKey(tour.getPrimaryKey());
      Timeframe[] frames = product.getTimeframes();
      Timeframe tempFrame = (Timeframe) IDOLookup.create(Timeframe.class);

      String applicationString = "prodDepDays"+tour.getPrimaryKey().toString()+"_"+fromStamp+"_"+toStamp+"_"+showPast;

      List tempList = (List) iwc.getApplicationAttribute(applicationString);
      if (tempList != null) {
        returner = tempList;
      }else {
//        System.err.println("TourBusiness : getDepartDays : "+fromStamp+ " - " +toStamp);
        for (int i = 0; i < frames.length; i++) {
//          System.err.println("------------------------------------------------");
//          System.err.println("-----------------------"+i+"------------------------");
//          System.err.println("------------------------------------------------");

          boolean yearly = frames[i].getIfYearly();

          IWTimestamp tFrom = new IWTimestamp(frames[i].getFrom());
          IWTimestamp tTo = new IWTimestamp(frames[i].getTo());

          IWTimestamp from = null;
          if (fromStamp != null) from = new IWTimestamp(fromStamp);
          IWTimestamp to = null;
          if (toStamp != null) to = new IWTimestamp(toStamp);

//          System.err.println("tFrom... : "+tFrom.toSQLDateString());
//          System.err.println("tTo..... : "+tTo.toSQLDateString());

          int numberOfDays = tour.getNumberOfDays();
            if (numberOfDays < 1) numberOfDays = 1;

          if (from == null) {
            from = new IWTimestamp(tFrom);
          }
          if (to == null) {
            to   = new IWTimestamp(tTo);
          }

          from.addDays(-1);
          to.addDays(1);

          int yearsBetween = 0;
          int toY = to.getYear();

          frames[i] = fixTimeframe(frames[i], from, to);
          tFrom = new IWTimestamp(frames[i].getFrom());
          tTo = new IWTimestamp(frames[i].getTo());

          int daysBetween = IWTimestamp.getDaysBetween(from, to);

//WTF          to = new IWTimestamp(from);
//            to.addDays(daysBetween);
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

        IWTimestamp stamp = new IWTimestamp(from);
        IWTimestamp temp;

        IWTimestamp now = IWTimestamp.RightNow();

        tempFrame.setFrom(tFrom.getTimestamp());
        tempFrame.setTo(tTo.getTimestamp());

          while (to.isLaterThan(stamp)) {
//            System.err.println("Stamp : "+stamp.toSQLDateString());
            temp = getNextAvailableDay(iwc, tour, product,tempFrame, stamp);
//            temp = getNextAvailableDay(iwc, tour, product,frames[i], stamp);
            if (temp != null) {
              if (getStockroomBusiness().isInTimeframe(tFrom, tTo, temp, yearly)) {
//                System.err.println("TEMP : "+temp.toSQLDateString()+" .... yearsBetween : "+yearsBetween+" ... yearly ("+yearly+")");
                if (yearly) {
                  temp.addYears(-yearsBetween);
                }
                //System.err.println("TEMP : "+temp.toSQLDateString());
                if (!showPast) {
                  if (temp.isLaterThanOrEquals(now)) {
                    returner.add(temp);
                    stamp = new IWTimestamp(temp);
                  }else {
                    stamp = new IWTimestamp(temp);
                  }
                }else {
                  returner.add(temp);
                  stamp = new IWTimestamp(temp);
                }

                if (yearly) {
                  stamp.addYears(yearsBetween);
                }

              }
              //stamp = new IWTimestamp(temp);
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

  public IWTimestamp getNextAvailableDay(IWContext iwc, Tour tour, Product product, Timeframe timeframe, IWTimestamp from) {
    return getNextAvailableDay(iwc, tour, product, new Timeframe[] {timeframe}, from);
  }

  public IWTimestamp getNextAvailableDay(IWContext iwc, Tour tour, Product product,  IWTimestamp from) throws SQLException, RemoteException {
    return getNextAvailableDay(iwc, tour, product, product.getTimeframes(), from);
  }

  public IWTimestamp getNextAvailableDay(IWContext iwc, Tour tour, Product product, Timeframe[] timeframes, IWTimestamp from) {
    IWTimestamp stamp = new IWTimestamp(from);
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
        if (this.getIfDay(iwc,product, timeframes, stamp, false, true)) {
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

	public int getMaxBookings(Product product, IWTimestamp stamp) throws RemoteException, FinderException {
		Tour tour = ((TourHome) IDOLookup.getHome(Tour.class)).findByPrimaryKey(product.getPrimaryKey());
		int returner = tour.getTotalSeats();
		if (returner > 0) {
			return returner;
		}else {
			return super.getMaxBookings(product, stamp);
		}
	}


	public int getMinBookings(Product product, IWTimestamp stamp) throws RemoteException, FinderException {
		Tour tour = ((TourHome) IDOLookup.getHome(Tour.class)).findByPrimaryKey(product.getPrimaryKey());
		int returner = tour.getMinimumSeats();
		if (returner > 0) {
			return returner;
		}else {
			return super.getMinBookings(product, stamp);
		}
	}


  protected ProductBusiness getProductBusiness() throws RemoteException {
    return (ProductBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), ProductBusiness.class);
  }

  protected StockroomBusiness getStockroomBusiness() throws RemoteException {
    return (StockroomBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), StockroomBusiness.class);
  }
}
