
/**
 * Title:
 * Description:  Class that can sort ListedMember
 * Copyright:    Copyright (c) 2000
 * Company:
 * @author       Ægir
 * @version 1.0
 */

package is.idega.idegaweb.golf.service;

import java.util.*;
import java.util.Comparator;


public class ComparableMemberList implements Comparator {

  public static final int NAME      = 1;
  public static final int ADDRESS   = 2;
  public static final int EMAIL     = 3;
  public static final int HANDICAP  = 4;
  public static final int PAYMENT   = 5;
  private int sortBy;

  public ComparableMemberList() {
      sortBy = NAME;
  }

  public ComparableMemberList(int toSortBy) {
      sortBy = toSortBy;
  }

  public void sortBy(int toSortBy) {
      sortBy = toSortBy;
  }

  public int compare(Object o1, Object o2) {
      ListedMember p1 = (ListedMember) o1;
      ListedMember p2 = (ListedMember) o2;
      int result = 0;

      if(sortBy == NAME) {
          result = nameSort(o1, o2);
      }
      else if(sortBy == ADDRESS) {
          result = p1.getAddress().compareTo(p2.getAddress());
          if(result == 0)
              result = nameSort(o1, o2);
      }
      else if(sortBy == EMAIL) {
          result = p1.getEmail().compareTo(p2.getEmail());
          if(result == 0)
              result = nameSort(o1, o2);
      }
      else if(sortBy == HANDICAP) {
          result = p1.getHandicap().compareTo(p2.getHandicap());
          if(result == 0)
              result = nameSort(o1, o2);
      }
      else if(sortBy == PAYMENT) {
          result = p1.getPayment().compareTo(p2.getPayment());
          if(result == 0)
              result = nameSort(o1, o2);
      }
      return result;
  }

  private int nameSort(Object o1, Object o2) {
      ListedMember p1 = (ListedMember) o1;
      ListedMember p2 = (ListedMember) o2;

      // check on first name first...
      int result = p1.getFirstName().compareTo(p2.getFirstName());

      // if equal, check middle name...
      if (result == 0)
          result = p1.getMiddleName().compareTo(p2.getMiddleName());

      // if equal, check last name...
      if (result == 0)
          result = p1.getLastName().compareTo(p2.getLastName());
      return result;
  }
  public boolean equals(Object obj) {
    /**@todo: Implement this java.util.Comparator method*/
    throw new java.lang.UnsupportedOperationException("Method equals() not yet implemented.");
  }

  public Iterator sort(ListedMember[] members, int toSortBy) {
      sortBy = toSortBy;
      List list = new LinkedList();
      for(int i = 0; i < members.length; i++) {
          list.add(members[i]);
      }
      Collections.sort(list, this);
      return list.iterator();
  }

  public Iterator sort(ListedMember[] members) {
      List list = new LinkedList();
      for(int i = 0; i < members.length; i++) {
          list.add(members[i]);
      }
      Collections.sort(list, this);
      return list.iterator();
  }

  public ListedMember[] sortedArray(ListedMember[] members, int toSortBy) {
      sortBy = toSortBy;
      List list = new LinkedList();
      for(int i = 0; i < members.length; i++) {
          list.add(members[i]);
      }
      Collections.sort(list, this);
      Object[] objArr = list.toArray();
      for(int i = 0; i < objArr.length; i++) {
          members[i] = (ListedMember) objArr[i];
      }
      return (members);
  }

  public ListedMember[] sortedArray(ListedMember[] members) {
      List list = new LinkedList();
      for(int i = 0; i < members.length; i++) {
          list.add(members[i]);
      }
      Collections.sort(list, this);
      Object[] objArr = list.toArray();
      for(int i = 0; i < objArr.length; i++) {
          members[i] = (ListedMember) objArr[i];
      }
      return (members);
  }

  public ListedMember[] reverseSortedArray(ListedMember[] members, int toSortBy) {
      sortBy = toSortBy;
      List list = new LinkedList();
      for(int i = 0; i < members.length; i++) {
          list.add(members[i]);
      }
      Collections.sort(list, this);
      Collections.reverse(list);
      Object[] objArr = list.toArray();
      for(int i = 0; i < objArr.length; i++) {
          members[i] = (ListedMember) objArr[i];
      }
      return (members);
  }

}
