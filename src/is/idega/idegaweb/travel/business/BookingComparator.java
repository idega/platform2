package is.idega.idegaweb.travel.business;

import is.idega.idegaweb.travel.data.HotelPickupPlace;
import is.idega.idegaweb.travel.interfaces.Booking;
import is.idega.idegaweb.travel.service.tour.data.TourBooking;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.core.user.data.User;
import com.idega.presentation.IWContext;
import com.idega.util.IWTimestamp;
import com.idega.util.IsCollator;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */

public class BookingComparator implements Comparator {

  public static final int NAME = 101;
  public static final int HOTELPICKUP = 102;
  public static final int HOTELPICKUP_NAME = 103;
  public static final int TOTALCOUNT = 104;
  public static final int USER = 105;
  public static final int OWNER = 106;
  public static final int DATE = 107;
  public static final int AMOUNT = 108;
  public static final int DATE_OF_BOOKING = 109;


  private int sortBy;
  private IWContext iwc;
  private Booker booker;

  public BookingComparator(IWContext iwc) {
      sortBy = NAME;
      this.iwc = iwc;
  }

  public BookingComparator(IWContext iwc, int toSortBy) {
      sortBy = toSortBy;
      this.iwc = iwc;
  }

  public void sortBy(int toSortBy) {
      sortBy = toSortBy;
  }

  public int compare(Object o1, Object o2) {
      Booking p1 = (Booking) o1;
      Booking p2 = (Booking) o2;
      int result = 0;

      try {
        switch (this.sortBy) {
          case NAME     : result = nameSort(o1, o2);
          break;
          case HOTELPICKUP   : result = hppSort(o1, o2);
          break;
          case HOTELPICKUP_NAME   : result = hppNameSort(o1, o2);
          break;
          case TOTALCOUNT   : result = totalCountSort(o1, o2);
          break;
          case USER   : result = userSort(o1, o2);
          break;
          case OWNER   : result = ownerSort(o1, o2);
          break;
          case DATE   : result = dateSort(o1, o2, false);
          break;
          case AMOUNT   : result = amountSort(o1, o2);
          break;
        case DATE_OF_BOOKING   : result = dateSort(o1, o2, true);
          break;
        }
      }catch (RemoteException re) {
        throw new RuntimeException(re.getMessage());
      }catch (FinderException fe) {
        throw new RuntimeException(fe.getMessage());
      }

      return result;
  }

  private int hppNameSort(Object o1, Object o2)throws RemoteException {
    int result = hppSort(o1, o2);
    if (result == 0) result = nameSort(o1, o2);

    return result;
  }

  private int nameSort(Object o1, Object o2)throws RemoteException {
    Booking p1 = (Booking) o1;
    Booking p2 = (Booking) o2;

    String one = p1.getName()!=null?p1.getName():"";
    String two = p2.getName()!=null?p2.getName():"";

    return IsCollator.getIsCollator().compare(one,two);
  }

  private int totalCountSort(Object o1, Object o2)throws RemoteException {
    Booking p1 = (Booking) o1;
    Booking p2 = (Booking) o2;

    int one = p1.getTotalCount();
    int two = p2.getTotalCount();

    if (one > two) return -1;
    else if (one < two) return 1;
    else return 0;
  }

  private int amountSort(Object o1, Object o2) throws RemoteException, FinderException{
    try {
      Booking p1 = (Booking) o1;
      Booking p2 = (Booking) o2;

      float one = getBooker().getBookingPrice(p1);
      float two = getBooker().getBookingPrice(p2);

      if (one > two) return -1;
      else if (one < two) return 1;
      else return 0;
    }catch (RemoteException re) {
      throw new RuntimeException(re.getMessage());
    }
  }

  private int hppSort(Object o1, Object o2)throws RemoteException {
    Booking p1 = (Booking) o1;
    Booking p2 = (Booking) o2;

    TourBooking tp1 = null;
    TourBooking tp2 = null;

    boolean err1 = false;
    boolean err2 = false;
    try {
      tp1 = ((is.idega.idegaweb.travel.service.tour.data.TourBookingHome)com.idega.data.IDOLookup.getHome(TourBooking.class)).findByPrimaryKey(new Integer(p1.getID()));
    }catch (FinderException fe) {
      err1 = true;
    }
    try {
      tp2 = ((is.idega.idegaweb.travel.service.tour.data.TourBookingHome)com.idega.data.IDOLookup.getHome(TourBooking.class)).findByPrimaryKey(new Integer(p2.getID()));
    }catch (FinderException fe) {
      err2 = true;
    }

    if (err1 && err2) return 0;
    else if (err1 && !err2) return 1;
    else if (!err1 && err2) return -1;

    HotelPickupPlace hp1 = tp1.getHotelPickupPlace();
    HotelPickupPlace hp2 = tp2.getHotelPickupPlace();


    if (hp1 != null && hp1 != null) {
      String one = tp1.getHotelPickupPlace().getName()!=null?tp1.getHotelPickupPlace().getName():"";
      String two = tp2.getHotelPickupPlace().getName()!=null?tp2.getHotelPickupPlace().getName():"";
      return IsCollator.getIsCollator().compare(one,two);
    }else if (tp1.getHotelPickupPlace() == null && tp2.getHotelPickupPlace() == null) {
      return 0;
    }else if (tp1.getHotelPickupPlace() == null && tp2.getHotelPickupPlace() != null) {
      return 1;
    }else {
      return -1;
    }

  }

  private int userSort(Object o1, Object o2)throws RemoteException {
    Booking p1 = (Booking) o1;
    Booking p2 = (Booking) o2;
    try {
      int id1 = p1.getUserId();
      int id2 = p2.getUserId();

      if (id1 == -1 && id2 == -1) {
        return 0;
      }else if (id1 == -1 && id2 != -1) {
        return 1;
      }else if (id1 != -1 && id2 == -1) {
        return -1;
      }


      User user1 = ((com.idega.core.user.data.UserHome)com.idega.data.IDOLookup.getHomeLegacy(User.class)).findByPrimaryKeyLegacy(id1);
      User user2 = ((com.idega.core.user.data.UserHome)com.idega.data.IDOLookup.getHomeLegacy(User.class)).findByPrimaryKeyLegacy(id2);

      String one = user1.getName()!=null?user1.getName():"";
      String two = user2.getName()!=null?user2.getName():"";

      return IsCollator.getIsCollator().compare(one, two);
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
      return 0;
    }
  }

  private int ownerSort(Object o1, Object o2)throws RemoteException {
    Booking p1 = (Booking) o1;
    Booking p2 = (Booking) o2;

    try {
      int id1 = p1.getOwnerId();
      int id2 = p2.getOwnerId();

      if (id1 == -1 && id2 == -1) {
        return 0;
      }else if (id1 == -1 && id2 != -1) {
        return 1;
      }else if (id1 != -1 && id2 == -1) {
        return -1;
      }

      User user1 = ((com.idega.core.user.data.UserHome)com.idega.data.IDOLookup.getHomeLegacy(User.class)).findByPrimaryKeyLegacy(id1);
      User user2 = ((com.idega.core.user.data.UserHome)com.idega.data.IDOLookup.getHomeLegacy(User.class)).findByPrimaryKeyLegacy(id2);

      String one = user1.getName()!=null?user1.getName():"";
      String two = user2.getName()!=null?user2.getName():"";

      return IsCollator.getIsCollator().compare(one, two);
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
      return 0;
    }
  }

  private int dateSort(Object o1, Object o2, boolean useDateOfBooking)throws RemoteException {
    Booking p1 = (Booking) o1;
    Booking p2 = (Booking) o2;

    IWTimestamp t1 = null;
    IWTimestamp t2 = null;

    if (useDateOfBooking) {
      t1 = new IWTimestamp(p1.getDateOfBooking());
      t2 = new IWTimestamp(p2.getDateOfBooking());
    }else{
      t1 = new IWTimestamp(p1.getBookingDate());
      t2 = new IWTimestamp(p2.getBookingDate());
    }

    if (t1.isLaterThan(t2)) {
      return 1;
    }else if (t2.isLaterThan(t1)) {
      return -1;
    }else {
      return 0;
    }
  }

  public boolean equals(Object obj) {
    /**@todo: Implement this java.util.Comparator method*/
    throw new java.lang.UnsupportedOperationException("Method equals() not yet implemented.");
  }

  public Iterator sort(Booking[] bookings, int toSortBy) {
      sortBy = toSortBy;
      List list = new LinkedList();
      for(int i = 0; i < bookings.length; i++) {
          list.add(bookings[i]);
      }
      Collections.sort(list, this);
      return list.iterator();
  }

  public Iterator sort(Booking[] bookings) {
      List list = new LinkedList();
      for(int i = 0; i < bookings.length; i++) {
          list.add(bookings[i]);
      }
      Collections.sort(list, this);
      return list.iterator();
  }

  public Booking[] sortedArray(Booking[] bookings, int toSortBy) {
      sortBy = toSortBy;
      List list = new LinkedList();
      for(int i = 0; i < bookings.length; i++) {
          list.add(bookings[i]);
      }
      Collections.sort(list, this);
      Object[] objArr = list.toArray();
      for(int i = 0; i < objArr.length; i++) {
          bookings[i] = (Booking) objArr[i];
      }
      return (bookings);
  }

   public Vector sortedArray(Vector list) {
      Collections.sort(list, this);
      return list;
  }


  public Booking[] sortedArray(Booking[] bookings) {
      List list = new LinkedList();
      for(int i = 0; i < bookings.length; i++) {
          list.add(bookings[i]);
      }
      Collections.sort(list, this);
      Object[] objArr = list.toArray();
      for(int i = 0; i < objArr.length; i++) {
          bookings[i] = (Booking) objArr[i];
      }
      return (bookings);
  }

  public Booking[] reverseSortedArray(Booking[] bookings, int toSortBy) {
      sortBy = toSortBy;
      List list = new LinkedList();
      for(int i = 0; i < bookings.length; i++) {
          list.add(bookings[i]);
      }
      Collections.sort(list, this);
      Collections.reverse(list);
      Object[] objArr = list.toArray();
      for(int i = 0; i < objArr.length; i++) {
          bookings[i] = (Booking) objArr[i];
      }
      return (bookings);
  }

  private Booker getBooker() {
    try {
      if (booker == null) {
        booker = (Booker)com.idega.business.IBOLookup.getServiceInstance(iwc,Booker.class);
      }
      return booker;
    }catch (RemoteException re) {
      throw new RuntimeException(re.getMessage());
    }

  }

}
