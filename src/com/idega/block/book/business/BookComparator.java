package com.idega.block.book.business;

import java.rmi.RemoteException;
import java.util.Comparator;

import com.idega.block.book.data.Author;
import com.idega.block.book.data.Book;
import com.idega.block.book.data.Publisher;
import com.idega.block.category.data.ICCategory;
import com.idega.util.IsCollator;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:laddi@idega.is">laddi@idega.is</a>
 * @version 1.0
 */

public class BookComparator implements Comparator {

  public static final int BOOK_NAME = 1;
  public static final int AUTHOR_NAME = 2;
  public static final int PUBLISHER_NAME = 3;
  public static final int CATEGORY_NAME = 4;
  public static final int PUBLISH_YEAR = 5;

  private int sortBy;

  public BookComparator() {
    this(BOOK_NAME);
  }

  public BookComparator(int toSortBy) {
    sortBy = toSortBy;
  }

  public void sortBy(int toSortBy) {
    sortBy = toSortBy;
  }

  public int compare(Object o1, Object o2) {
    int result = 0;

    switch (this.sortBy) {
      case BOOK_NAME:
      result = bookSort(o1, o2);
      break;
      case AUTHOR_NAME:
      result = authorSort(o1, o2);
      break;
      case PUBLISHER_NAME:
      result = publisherSort(o1, o2);
      break;
      case CATEGORY_NAME:
      result = categorySort(o1, o2);
      break;
      case PUBLISH_YEAR:
      result = yearSort(o1, o2);
      break;
    }

    return result;
  }

  private int bookSort(Object o1, Object o2) {
    Book p1 = (Book) o1;
    Book p2 = (Book) o2;

    String one;
    String two;

    try {
      one = p1.getName()!=null?p1.getName():"";
    }
    catch (RemoteException e) {
      one = "";
    }

    try {
      two = p2.getName()!=null?p2.getName():"";
    }
    catch (RemoteException e) {
      two = "";
    }

    return IsCollator.getIsCollator().compare(one,two);
  }

  private int authorSort(Object o1, Object o2) {
    Author p1 = (Author) o1;
    Author p2 = (Author) o2;

    String one;
    String two;

    try {
      one = p1.getName()!=null?p1.getName():"";
    }
    catch (RemoteException e) {
      one = "";
    }

    try {
      two = p2.getName()!=null?p2.getName():"";
    }
    catch (RemoteException e) {
      two = "";
    }

    return IsCollator.getIsCollator().compare(one,two);
  }

  private int publisherSort(Object o1, Object o2) {
    Publisher p1 = (Publisher) o1;
    Publisher p2 = (Publisher) o2;

    String one;
    String two;

    try {
      one = p1.getName()!=null?p1.getName():"";
    }
    catch (RemoteException e) {
      one = "";
    }

    try {
      two = p2.getName()!=null?p2.getName():"";
    }
    catch (RemoteException e) {
      two = "";
    }

    return IsCollator.getIsCollator().compare(one,two);
  }

  private int categorySort(Object o1, Object o2) {
    ICCategory p1 = (ICCategory) o1;
    ICCategory p2 = (ICCategory) o2;

    String one = p1.getName()!=null?p1.getName():"";
    String two = p2.getName()!=null?p2.getName():"";

    return IsCollator.getIsCollator().compare(one,two);
  }

  private int yearSort(Object o1, Object o2) {
    Book p1 = (Book) o1;
    Book p2 = (Book) o2;

    int year1;
    int year2;

    try {
      year1 = p1.getYear();
    }
    catch (RemoteException e) {
      year1 = 0;
    }

    try {
      year2 = p2.getYear();
    }
    catch (RemoteException e) {
      year2 = 0;
    }

    if ( year1 > year2 )
      return 1;
    else if ( year1 < year2 )
      return -1;
    else
      return bookSort(p1,p2);
  }

  public boolean equals(Object obj) {
    /**@todo: Implement this java.util.Comparator method*/
    throw new java.lang.UnsupportedOperationException("Method equals() not yet implemented.");
  }
}
