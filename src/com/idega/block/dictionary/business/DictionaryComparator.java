package com.idega.block.dictionary.business;

import com.idega.block.category.data.ICCategory;
import com.idega.block.dictionary.data.Word;

import java.rmi.RemoteException;
import java.text.Collator;
import java.util.*;
import java.util.Comparator;

import com.idega.util.IsCollator;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:laddi@idega.is">laddi@idega.is</a>
 * @version 1.0
 */

public class DictionaryComparator implements Comparator {

  public static final int WORD_NAME = 1;
  public static final int CATEGORY_NAME = 2;

  private int sortBy;
  private Locale _locale;

  public DictionaryComparator() {
    this(WORD_NAME);
  }

  public DictionaryComparator(Locale locale, int toSortBy) {
    sortBy = toSortBy;
    _locale = locale;
  }

  public DictionaryComparator(int toSortBy) {
    sortBy = toSortBy;
  }

  public void sortBy(int toSortBy) {
    sortBy = toSortBy;
  }

  public int compare(Object o1, Object o2) {
    int result = 0;

    switch (this.sortBy) {
      case WORD_NAME:
      result = wordSort(o1, o2);
      break;
      case CATEGORY_NAME:
      result = categorySort(o1, o2);
      break;
    }

    return result;
  }

  private int wordSort(Object o1, Object o2) {
    Word p1 = (Word) o1;
    Word p2 = (Word) o2;

    String one;
    String two;

    try {
      one = p1.getWord()!=null?p1.getWord():"";
    }
    catch (RemoteException e) {
      one = "";
    }

    try {
      two = p2.getWord()!=null?p2.getWord():"";
    }
    catch (RemoteException e) {
      two = "";
    }

    return IsCollator.getIsCollator().compare(one,two);
  }

  private int categorySort(Object o1, Object o2) {
    Collator collator = null;
    if (_locale != null)
    	collator = Collator.getInstance(_locale);
    else collator = IsCollator.getIsCollator();
    
    ICCategory p1 = (ICCategory) o1;
    ICCategory p2 = (ICCategory) o2;

    String one = p1.getName()!=null?p1.getName():"";
    String two = p2.getName()!=null?p2.getName():"";

    return collator.compare(one,two);
  }

  public boolean equals(Object obj) {
    /**@todo: Implement this java.util.Comparator method*/
    throw new java.lang.UnsupportedOperationException("Method equals() not yet implemented.");
  }
}
