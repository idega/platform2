
package is.idega.idegaweb.tracker.business;

import java.util.*;
import com.idega.util.IsCollator;
import is.idega.idegaweb.tracker.data.PageStatistics;


/**
 * Title: is.idega.idegaweb.tracker.business.PageComparator
 * Description: Compares page entities for sorting
 * Copyright:    Copyright (c) 2002
 * Company:      idega software
 * @author       <a href="mailto:eiki@idega.is">Eirikur Hrafnsson</a>
 * @version 1.0
 */

public class PageComparator implements Comparator {

  public static final int ORDER_BY_HITS   = 1;
  public static final int REVERSE_ORDER_BY_HITS   = 2;
  public static final int ORDER_BY_PAGE_NAME = 3;

  private int sortBy;

  public PageComparator() {
      sortBy = ORDER_BY_HITS;
  }

  public PageComparator(int toSortBy) {
      sortBy = toSortBy;
  }

  public void sortBy(int toSortBy) {
      sortBy = toSortBy;
  }

  public int compare(Object o1, Object o2) {
      int result = 0;

      switch (this.sortBy) {
        case ORDER_BY_PAGE_NAME     : result = urlSort(o1, o2);
        break;
        case ORDER_BY_HITS   : result = hitSort(o1,o2);
        break;
        case REVERSE_ORDER_BY_HITS   : result = reverseHitSort(o1,o2);
        break;
      }

      return result;
  }

  private int hitSort(Object o1, Object o2) {
    int result;
    PageStatistics p1 = (PageStatistics) o1;
    PageStatistics p2 = (PageStatistics) o2;
    if( p1.getHits() > p2.getHits() ){
      result = -1;
    }
    else if( p1.getHits() < p2.getHits() ){
      result = 1;
    }
    else result = 0;

    return result;
  }

  private int reverseHitSort(Object o1, Object o2) {
    return (-1*hitSort(o1,o2));
  }

  private int urlSort(Object o1, Object o2) {
      PageStatistics p1 = (PageStatistics) o1;
      PageStatistics p2 = (PageStatistics) o2;

      String one = p1.getName()!=null?p1.getName():"";
      String two = p2.getName()!=null?p2.getName():"";
      int result = IsCollator.getIsCollator().compare(one,two);

      return result;
  }

  public boolean equals(Object obj) {
    /**@todo: Implement this java.util.Comparator method*/
    throw new java.lang.UnsupportedOperationException("Method equals() not yet implemented.");
  }

  public Iterator sort(PageStatistics[] pages, int toSortBy) {
      sortBy = toSortBy;
      List list = new LinkedList();
      for(int i = 0; i < pages.length; i++) {
          list.add(pages[i]);
      }
      Collections.sort(list, this);
      return list.iterator();
  }

  public Iterator sort(PageStatistics[] pages) {
      List list = new LinkedList();
      for(int i = 0; i < pages.length; i++) {
          list.add(pages[i]);
      }
      Collections.sort(list, this);
      return list.iterator();
  }

  public PageStatistics[] sortedArray(PageStatistics[] pages, int toSortBy) {
      sortBy = toSortBy;
      List list = new LinkedList();
      for(int i = 0; i < pages.length; i++) {
          list.add(pages[i]);
      }
      Collections.sort(list, this);
      Object[] objArr = list.toArray();
      for(int i = 0; i < objArr.length; i++) {
          pages[i] = (PageStatistics) objArr[i];
      }
      return (pages);
  }

  public Vector sortedArray(Vector list) {
      Collections.sort(list, this);
      return list;
  }

  public ArrayList sortedArrayList(ArrayList list) {
      Collections.sort(list, this);
      return list;
  }

  public PageStatistics[] sortedArray(PageStatistics[] pages) {
      List list = new LinkedList();
      for(int i = 0; i < pages.length; i++) {
          list.add(pages[i]);
      }
      Collections.sort(list, this);
      Object[] objArr = list.toArray();
      for(int i = 0; i < objArr.length; i++) {
          pages[i] = (PageStatistics) objArr[i];
      }
      return (pages);
  }

  public PageStatistics[] reverseSortedArray(PageStatistics[] pages, int toSortBy) {
      sortBy = toSortBy;
      List list = new LinkedList();
      for(int i = 0; i < pages.length; i++) {
          list.add(pages[i]);
      }
      Collections.sort(list, this);
      Collections.reverse(list);
      Object[] objArr = list.toArray();
      for(int i = 0; i < objArr.length; i++) {
          pages[i] = (PageStatistics) objArr[i];
      }
      return (pages);
  }

}
