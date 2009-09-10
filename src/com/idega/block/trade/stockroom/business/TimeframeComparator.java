package com.idega.block.trade.stockroom.business;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import com.idega.block.trade.stockroom.data.Timeframe;
import com.idega.util.IWTimestamp;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */

public class TimeframeComparator implements Comparator {

  public static final int FROMDATE = 1;
  public static final int TODATE = 2;
//  public static final int YEARFROMDATE = 3;
//  public static final int TOFROMDATE = 3;


  private int sortBy;

  public TimeframeComparator() {
      this.sortBy = FROMDATE;
  }

  public TimeframeComparator(int toSortBy) {
      this.sortBy = toSortBy;
  }

  public void sortBy(int toSortBy) {
      this.sortBy = toSortBy;
  }

  public int compare(Object o1, Object o2) {
      int result = 0;

      switch (this.sortBy) {
        case FROMDATE     : result = fromSort(o1, o2);
        break;
        case TODATE   : result = toSort(o1,o2);
        break;
      }

      return result;
  }

  private int fromSort(Object o1, Object o2) {
    Timeframe p1 = (Timeframe) o1;
    Timeframe p2 = (Timeframe) o2;

    IWTimestamp t1 = new IWTimestamp(p1.getFrom());
    IWTimestamp t2 = new IWTimestamp(p2.getFrom());

    if (t1.isLaterThan(t2)) {
      return 1;
    }else if (t2.isLaterThan(t1)) {
      return -1;
    }else {
      return 0;
    }
  }

  private int toSort(Object o1, Object o2) {
    Timeframe p1 = (Timeframe) o1;
    Timeframe p2 = (Timeframe) o2;

    IWTimestamp t1 = new IWTimestamp(p1.getTo());
    IWTimestamp t2 = new IWTimestamp(p2.getTo());

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
