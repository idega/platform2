package is.idega.idegaweb.travel.business;

import com.idega.util.IsCollator;
import com.idega.util.idegaTimestamp;
import java.util.*;
import java.util.Comparator;
import is.idega.idegaweb.travel.interfaces.Booking;
import is.idega.idegaweb.travel.service.tour.data.TourBooking;

import java.sql.SQLException;

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


  private int sortBy;

  public BookingComparator() {
      sortBy = NAME;
  }

  public BookingComparator(int toSortBy) {
      sortBy = toSortBy;
  }

  public void sortBy(int toSortBy) {
      sortBy = toSortBy;
  }

  public int compare(Object o1, Object o2) {
      Booking p1 = (Booking) o1;
      Booking p2 = (Booking) o2;
      int result = 0;

      switch (this.sortBy) {
        case NAME     : result = nameSort(o1, o2);
        break;
        case HOTELPICKUP   : result = hppSort(o1, o2);
        break;
        case HOTELPICKUP_NAME   : result = hppNameSort(o1, o2);
        break;
        case TOTALCOUNT   : result = totalCountSort(o1, o2);
        break;
      }

      return result;
  }

  private int hppNameSort(Object o1, Object o2) {
    int result = hppSort(o1, o2);
    if (result == 0) result = nameSort(o1, o2);

    return result;
  }

  private int nameSort(Object o1, Object o2) {
    Booking p1 = (Booking) o1;
    Booking p2 = (Booking) o2;

    String one = p1.getName()!=null?p1.getName():"";
    String two = p2.getName()!=null?p2.getName():"";

    return IsCollator.getIsCollator().compare(one,two);
  }

  private int totalCountSort(Object o1, Object o2) {
    Booking p1 = (Booking) o1;
    Booking p2 = (Booking) o2;

    int one = p1.getTotalCount();
    int two = p2.getTotalCount();

    if (one > two) return -1;
    else if (one < two) return 1;
    else return 0;
  }

  private int hppSort(Object o1, Object o2) {
    Booking p1 = (Booking) o1;
    Booking p2 = (Booking) o2;

    TourBooking tp1 = null;
    TourBooking tp2 = null;

    boolean err1 = false;
    boolean err2 = false;
    try {
      tp1 = new TourBooking(p1.getID());
    }catch (SQLException sql) {
      err1 = true;
    }
    try {
      tp2 = new TourBooking(p2.getID());
    }catch (SQLException sql) {
      err2 = true;
    }

    if (err1 && err2) return 0;
    else if (err1 && !err2) return 1;
    else if (!err1 && err2) return -1;

    if (tp1.getHotelPickupPlace() != null && tp2.getHotelPickupPlace() != null) {
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

}
