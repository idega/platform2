
package is.idega.idegaweb.tracker.business;

import java.util.*;
import com.idega.util.IsCollator;
import is.idega.idegaweb.tracker.data.UserAgentStatistics;


/**
 * Title: is.idega.idegaweb.tracker.business.AgentComparator
 * Description: Compares agent entities for sorting
 * Copyright:    Copyright (c) 2002
 * Company:      idega software
 * @author       <a href="mailto:aron@idega.is">eiki@idega.is</a>
 * @version 1.0
 */

public class AgentComparator implements Comparator {

  public static final int ORDER_BY_SESSIONS   = 1;
  public static final int REVERSE_ORDER_BY_SESSIONS   = 2;
  public static final int ORDER_BY_BROWSER_TYPE = 3;

  private int sortBy;

  public AgentComparator() {
      sortBy = ORDER_BY_SESSIONS;
  }

  public AgentComparator(int toSortBy) {
      sortBy = toSortBy;
  }

  public void sortBy(int toSortBy) {
      sortBy = toSortBy;
  }

  public int compare(Object o1, Object o2) {
      int result = 0;

      switch (this.sortBy) {
        case ORDER_BY_BROWSER_TYPE     : result = browserSort(o1, o2);
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
    UserAgentStatistics p1 = (UserAgentStatistics) o1;
    UserAgentStatistics p2 = (UserAgentStatistics) o2;
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

  private int browserSort(Object o1, Object o2) {
      UserAgentStatistics p1 = (UserAgentStatistics) o1;
      UserAgentStatistics p2 = (UserAgentStatistics) o2;

      String one = p1.getUserAgent()!=null?p1.getUserAgent():"";
      String two = p2.getUserAgent()!=null?p2.getUserAgent():"";
      int result = IsCollator.getIsCollator().compare(one,two);

      return result;
  }

  public boolean equals(Object obj) {
    /**@todo: Implement this java.util.Comparator method*/
    throw new java.lang.UnsupportedOperationException("Method equals() not yet implemented.");
  }

  public Iterator sort(UserAgentStatistics[] agents, int toSortBy) {
      sortBy = toSortBy;
      List list = new LinkedList();
      for(int i = 0; i < agents.length; i++) {
          list.add(agents[i]);
      }
      Collections.sort(list, this);
      return list.iterator();
  }

  public Iterator sort(UserAgentStatistics[] agents) {
      List list = new LinkedList();
      for(int i = 0; i < agents.length; i++) {
          list.add(agents[i]);
      }
      Collections.sort(list, this);
      return list.iterator();
  }

  public UserAgentStatistics[] sortedArray(UserAgentStatistics[] agents, int toSortBy) {
      sortBy = toSortBy;
      List list = new LinkedList();
      for(int i = 0; i < agents.length; i++) {
          list.add(agents[i]);
      }
      Collections.sort(list, this);
      Object[] objArr = list.toArray();
      for(int i = 0; i < objArr.length; i++) {
          agents[i] = (UserAgentStatistics) objArr[i];
      }
      return (agents);
  }

  public Vector sortedArray(Vector list) {
      Collections.sort(list, this);
      return list;
  }

  public ArrayList sortedArrayList(ArrayList list) {
      Collections.sort(list, this);
      return list;
  }

  public UserAgentStatistics[] sortedArray(UserAgentStatistics[] agents) {
      List list = new LinkedList();
      for(int i = 0; i < agents.length; i++) {
          list.add(agents[i]);
      }
      Collections.sort(list, this);
      Object[] objArr = list.toArray();
      for(int i = 0; i < objArr.length; i++) {
          agents[i] = (UserAgentStatistics) objArr[i];
      }
      return (agents);
  }

  public UserAgentStatistics[] reverseSortedArray(UserAgentStatistics[] agents, int toSortBy) {
      sortBy = toSortBy;
      List list = new LinkedList();
      for(int i = 0; i < agents.length; i++) {
          list.add(agents[i]);
      }
      Collections.sort(list, this);
      Collections.reverse(list);
      Object[] objArr = list.toArray();
      for(int i = 0; i < objArr.length; i++) {
          agents[i] = (UserAgentStatistics) objArr[i];
      }
      return (agents);
  }

}
