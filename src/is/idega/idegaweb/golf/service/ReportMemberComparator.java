

package is.idega.idegaweb.golf.service;



import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import com.idega.util.IsCollator;





/**

 * Title:

 * Description:

 * Copyright:    Copyright (c) 2001

 * Company:      idega multimedia

 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>

 * @version 1.0

 */



public class ReportMemberComparator implements Comparator {



  public static final int NAME      = 1;

  public static final int SOCIAL    = 2;

  public static final int ADDRESS   = 3;

  public static final int EMAIL     = 4;

  public static final int HANDICAP  = 5;

  public static final int BALANCE   = 6;

  public static final int PHONE     = 7;

  public static final int LOCKER    = 8;

  public static final int STATUS    = 9;

  private int sortBy;



  public ReportMemberComparator() {

      sortBy = NAME;

  }



  public ReportMemberComparator(int toSortBy) {

      sortBy = toSortBy;

  }



  public void sortBy(int toSortBy) {

      sortBy = toSortBy;

  }



  public int compare(Object o1, Object o2) {

      ReportMember p1 = (ReportMember) o1;

      ReportMember p2 = (ReportMember) o2;

      int result = 0;



      switch (this.sortBy) {

        case NAME     : result = nameSort(o1, o2);

        break;



        case SOCIAL   : result = p1.getSocial().compareTo(p2.getSocial());

                        if(result == 0)

                          result = nameSort(o1, o2);

        break;



        case ADDRESS  : result = IsCollator.getIsCollator().compare(p1.getAddress(),p2.getAddress());

                        //  result = p1.getAddress().compareTo(p2.getAddress());

                        if(result == 0)

                          result = nameSort(o1, o2);

        break;



        case EMAIL    : result = p1.getEmail().compareTo(p2.getEmail());

                        if(result == 0)

                          result = nameSort(o1, o2);

        break;



        case HANDICAP : result = p1.getHandicap().compareTo(p2.getHandicap());

                        if(result == 0)

                          result = nameSort(o1, o2);

        break;

        case BALANCE  : result = new Integer(p1.getBalance()).compareTo(new Integer(p2.getBalance()));

                        if(result == 0)

                          result = nameSort(o1, o2);

        break;

        case PHONE    : result = p1.getPhone().compareTo(p2.getPhone());

                        if(result == 0)

                          result = nameSort(o1, o2);

        break;

        case LOCKER   : result = p1.getLocker().compareTo(p2.getLocker());

                        if(result == 0)

                          result = nameSort(o1, o2);

        break;

        case STATUS   : result = p1.getStatus().compareTo(p2.getStatus());

                        if(result == 0)

                          result = nameSort(o1, o2);

        break;

        default:

          break;

      }



      return result;

  }



  private int nameSort(Object o1, Object o2) {

      ReportMember p1 = (ReportMember) o1;

      ReportMember p2 = (ReportMember) o2;



      // check on first name first...



      //int result = p1.getFirstName().compareTo(p2.getFirstName());

      int result = IsCollator.getIsCollator().compare(p1.getFirstName(),p2.getFirstName());



      // if equal, check middle name...

      if (result == 0){

          //result = p1.getMiddleName().compareTo(p2.getMiddleName());

          result = IsCollator.getIsCollator().compare(p1.getMiddleName(),p2.getMiddleName());

      }

      // if equal, check last name...

      if (result == 0){

          //result = p1.getLastName().compareTo(p2.getLastName());

          result = IsCollator.getIsCollator().compare(p1.getLastName(),p2.getLastName());

      }

      return result;

  }

  public boolean equals(Object obj) {

    /**@todo: Implement this java.util.Comparator method*/

    throw new java.lang.UnsupportedOperationException("Method equals() not yet implemented.");

  }



  public Iterator sort(ReportMember[] members, int toSortBy) {

      sortBy = toSortBy;

      List list = new LinkedList();

      for(int i = 0; i < members.length; i++) {

          list.add(members[i]);

      }

      Collections.sort(list, this);

      return list.iterator();

  }



  public Iterator sort(ReportMember[] members) {

      List list = new LinkedList();

      for(int i = 0; i < members.length; i++) {

          list.add(members[i]);

      }

      Collections.sort(list, this);

      return list.iterator();

  }



  public ReportMember[] sortedArray(ReportMember[] members, int toSortBy) {

      sortBy = toSortBy;

      List list = new LinkedList();

      for(int i = 0; i < members.length; i++) {

          list.add(members[i]);

      }

      Collections.sort(list, this);

      Object[] objArr = list.toArray();

      for(int i = 0; i < objArr.length; i++) {

          members[i] = (ReportMember) objArr[i];

      }

      return (members);

  }



   public Vector sortedArray(Vector list) {

      Collections.sort(list, this);

      return list;

  }





  public ReportMember[] sortedArray(ReportMember[] members) {

      List list = new LinkedList();

      for(int i = 0; i < members.length; i++) {

          list.add(members[i]);

      }

      Collections.sort(list, this);

      Object[] objArr = list.toArray();

      for(int i = 0; i < objArr.length; i++) {

          members[i] = (ReportMember) objArr[i];

      }

      return (members);

  }



  public ReportMember[] reverseSortedArray(ReportMember[] members, int toSortBy) {

      sortBy = toSortBy;

      List list = new LinkedList();

      for(int i = 0; i < members.length; i++) {

          list.add(members[i]);

      }

      Collections.sort(list, this);

      Collections.reverse(list);

      Object[] objArr = list.toArray();

      for(int i = 0; i < objArr.length; i++) {

          members[i] = (ReportMember) objArr[i];

      }

      return (members);

  }



}

