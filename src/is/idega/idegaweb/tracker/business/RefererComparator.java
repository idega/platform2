
package is.idega.idegaweb.tracker.business;

import java.util.*;
import com.idega.util.IsCollator;
import is.idega.idegaweb.tracker.data.ReferrerStatistics;


/**
 * Title: is.idega.idegaweb.tracker.business.RefererComparator
 * Description: Compares referer entities for sorting
 * Copyright:    Copyright (c) 2002
 * Company:      idega software
 * @author       <a href="mailto:aron@idega.is">eiki@idega.is</a>
 * @version 1.0
 */

public class RefererComparator implements Comparator {

  public static final int ORDER_BY_SESSIONS   = 1;
  public static final int REVERSE_ORDER_BY_SESSIONS   = 2;
  public static final int ORDER_BY_URL = 3;

  private int sortBy;

  public RefererComparator() {
      sortBy = ORDER_BY_SESSIONS;
  }

  public RefererComparator(int toSortBy) {
      sortBy = toSortBy;
  }

  public void sortBy(int toSortBy) {
      sortBy = toSortBy;
  }

  public int compare(Object o1, Object o2) {
      int result = 0;

      switch (this.sortBy) {
        case ORDER_BY_URL     : result = urlSort(o1, o2);
        break;
        case ORDER_BY_SESSIONS   : result = sessionSort(o1,o2);
        break;
        case REVERSE_ORDER_BY_SESSIONS   : result = reverseSessionSort(o1,o2);
        break;
      }

      return result;
  }

  private int sessionSort(Object o1, Object o2) {
    int result;
    ReferrerStatistics p1 = (ReferrerStatistics) o1;
    ReferrerStatistics p2 = (ReferrerStatistics) o2;
    if( p1.getSessions() > p2.getSessions() ){
      result = -1;
    }
    else if( p1.getSessions() < p2.getSessions() ){
      result = 1;
    }
    else result = 0;

    return result;
  }

  private int reverseSessionSort(Object o1, Object o2) {
    return (-1*sessionSort(o1,o2));
  }

  private int urlSort(Object o1, Object o2) {
      ReferrerStatistics p1 = (ReferrerStatistics) o1;
      ReferrerStatistics p2 = (ReferrerStatistics) o2;

      String one = p1.getReferrerUrl()!=null?p1.getReferrerUrl():"";
      String two = p2.getReferrerUrl()!=null?p2.getReferrerUrl():"";
      int result = IsCollator.getIsCollator().compare(one,two);

      return result;
  }

  public boolean equals(Object obj) {
    /**@todo: Implement this java.util.Comparator method*/
    throw new java.lang.UnsupportedOperationException("Method equals() not yet implemented.");
  }

  public Iterator sort(ReferrerStatistics[] referers, int toSortBy) {
      sortBy = toSortBy;
      List list = new LinkedList();
      for(int i = 0; i < referers.length; i++) {
          list.add(referers[i]);
      }
      Collections.sort(list, this);
      return list.iterator();
  }

  public Iterator sort(ReferrerStatistics[] referers) {
      List list = new LinkedList();
      for(int i = 0; i < referers.length; i++) {
          list.add(referers[i]);
      }
      Collections.sort(list, this);
      return list.iterator();
  }

  public ReferrerStatistics[] sortedArray(ReferrerStatistics[] referers, int toSortBy) {
      sortBy = toSortBy;
      List list = new LinkedList();
      for(int i = 0; i < referers.length; i++) {
          list.add(referers[i]);
      }
      Collections.sort(list, this);
      Object[] objArr = list.toArray();
      for(int i = 0; i < objArr.length; i++) {
          referers[i] = (ReferrerStatistics) objArr[i];
      }
      return (referers);
  }

  public Vector sortedArray(Vector list) {
      Collections.sort(list, this);
      return list;
  }

  public ArrayList sortedArrayList(ArrayList list) {
      Collections.sort(list, this);
      return list;
  }

  public ReferrerStatistics[] sortedArray(ReferrerStatistics[] referers) {
      List list = new LinkedList();
      for(int i = 0; i < referers.length; i++) {
          list.add(referers[i]);
      }
      Collections.sort(list, this);
      Object[] objArr = list.toArray();
      for(int i = 0; i < objArr.length; i++) {
          referers[i] = (ReferrerStatistics) objArr[i];
      }
      return (referers);
  }

  public ReferrerStatistics[] reverseSortedArray(ReferrerStatistics[] referers, int toSortBy) {
      sortBy = toSortBy;
      List list = new LinkedList();
      for(int i = 0; i < referers.length; i++) {
          list.add(referers[i]);
      }
      Collections.sort(list, this);
      Collections.reverse(list);
      Object[] objArr = list.toArray();
      for(int i = 0; i < objArr.length; i++) {
          referers[i] = (ReferrerStatistics) objArr[i];
      }
      return (referers);
  }

}
