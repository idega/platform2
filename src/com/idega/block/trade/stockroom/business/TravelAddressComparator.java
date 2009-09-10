package com.idega.block.trade.stockroom.business;

import com.idega.util.IsCollator;
import com.idega.util.IWTimestamp;
import java.util.*;
import java.util.Comparator;
import com.idega.block.trade.stockroom.data.*;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */

public class TravelAddressComparator implements Comparator {

  public static final int NAME = 1;
  public static final int TIME = 2;
  public static final int NAME_TIME = 3;


  private int sortBy;

  public TravelAddressComparator() {
      this.sortBy = NAME_TIME;
  }

  public TravelAddressComparator(int toSortBy) {
      this.sortBy = toSortBy;
  }

  public void sortBy(int toSortBy) {
      this.sortBy = toSortBy;
  }

  public int compare(Object o1, Object o2) {
      int result = 0;

      switch (this.sortBy) {
        case NAME     : result = nameSort(o1, o2);
        break;
        case TIME   : result = timeSort(o1,o2);
        break;
        case NAME_TIME   : result = nameTimeSort(o1,o2);
        break;
      }

      return result;
  }

  private int nameSort(Object o1, Object o2) {
    TravelAddress p1 = (TravelAddress) o1;
    TravelAddress p2 = (TravelAddress) o2;

    String one = p1.getStreetName()!=null?p1.getStreetName():"";
    String two = p2.getStreetName()!=null?p2.getStreetName():"";

    return IsCollator.getIsCollator().compare(one,two);
  }

  private int timeSort(Object o1, Object o2) {
    TravelAddress p1 = (TravelAddress) o1;
    TravelAddress p2 = (TravelAddress) o2;

    IWTimestamp t1 = new IWTimestamp(p1.getTime());
    IWTimestamp t2 = new IWTimestamp(p2.getTime());

    if (t1.isLaterThan(t2)) {
      return 1;
    }else if (t2.isLaterThan(t1)) {
      return -1;
    }else {
      return 0;
    }
  }

  private int nameTimeSort(Object o1, Object o2) {
    int returner = 0;

    returner = nameSort(o1, o2);
    if (returner == 0) {
      returner = timeSort(o1, o2);
    }

    return returner;
  }

  public boolean equals(Object obj) {
    /**@todo: Implement this java.util.Comparator method*/
    throw new java.lang.UnsupportedOperationException("Method equals() not yet implemented.");
  }

  public Iterator sort(Timeframe[] tFrames, int toSortBy) {
      this.sortBy = toSortBy;
      List list = new LinkedList();
      for(int i = 0; i < tFrames.length; i++) {
          list.add(tFrames[i]);
      }
      Collections.sort(list, this);
      return list.iterator();
  }

  public Iterator sort(Timeframe[] tFrames) {
      List list = new LinkedList();
      for(int i = 0; i < tFrames.length; i++) {
          list.add(tFrames[i]);
      }
      Collections.sort(list, this);
      return list.iterator();
  }

  public Timeframe[] sortedArray(Timeframe[] tFrames, int toSortBy) {
      this.sortBy = toSortBy;
      List list = new LinkedList();
      for(int i = 0; i < tFrames.length; i++) {
          list.add(tFrames[i]);
      }
      Collections.sort(list, this);
      Object[] objArr = list.toArray();
      for(int i = 0; i < objArr.length; i++) {
          tFrames[i] = (Timeframe) objArr[i];
      }
      return (tFrames);
  }

   public Vector sortedArray(Vector list) {
      Collections.sort(list, this);
      return list;
  }


  public Timeframe[] sortedArray(Timeframe[] tFrame) {
      List list = new LinkedList();
      for(int i = 0; i < tFrame.length; i++) {
          list.add(tFrame[i]);
      }
      Collections.sort(list, this);
      Object[] objArr = list.toArray();
      for(int i = 0; i < objArr.length; i++) {
          tFrame[i] = (Timeframe) objArr[i];
      }
      return (tFrame);
  }

  public Timeframe[] reverseSortedArray(Timeframe[] tFrame, int toSortBy) {
      this.sortBy = toSortBy;
      List list = new LinkedList();
      for(int i = 0; i < tFrame.length; i++) {
          list.add(tFrame[i]);
      }
      Collections.sort(list, this);
      Collections.reverse(list);
      Object[] objArr = list.toArray();
      for(int i = 0; i < objArr.length; i++) {
          tFrame[i] = (Timeframe) objArr[i];
      }
      return (tFrame);
  }

}
