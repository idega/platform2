package is.idega.idegaweb.campus.business;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class HabitantsComparator implements Comparator {

  public static final int NAME = 1;
  public static final int APARTMENT = 2;
  public static final int FLOOR = 3;
  public static final int ADDRESS = 4;
  private Locale locale = Locale.ENGLISH;
  private Collator collator = null;
  private int sortBy;

  public HabitantsComparator(Locale locale) {
    sortBy = NAME;
    this.locale = locale;
    this.collator = Collator.getInstance(locale);
  }

  public HabitantsComparator(Locale locale,int toSortBy) {
      sortBy = toSortBy;
      this.locale = locale;
      this.collator = Collator.getInstance(locale);
  }

  public void sortBy(int toSortBy) {
      sortBy = toSortBy;
  }

  public int compare(Object o1, Object o2) {
    int result = 0;

    switch (sortBy) {
      case NAME :
        result = nameCompare(o1,o2);
      break;

      case APARTMENT :
        result = apartmentCompare(o1,o2);
        if (result == 0)
          result = nameCompare(o1,o2);
      break;

      case ADDRESS :
        result = addressCompare(o1,o2);
        if (result == 0)
          result = apartmentCompare(o1,o2);
        if (result == 0)
          result = nameCompare(o1,o2);
      break;

      case FLOOR :
        result = floorCompare(o1,o2);
        if (result == 0)
          result = addressCompare(o1,o2);
        if (result == 0)
          result = apartmentCompare(o1,o2);
        if (result == 0)
          result = nameCompare(o1,o2);
      break;

    }
    return(result);
  }

  public boolean equals(Object obj) {
    if (compare(this,obj) == 0)
      return(true);
    else
      return(false);
  }

  public int nameCompare(Object o1, Object o2) {
    HabitantsCollector r1 = (HabitantsCollector) o1;
    HabitantsCollector r2 = (HabitantsCollector) o2;
    int result = 0;

    String one = r1.getName()!=null?r1.getName():"";
    String two = r2.getName()!=null?r2.getName():"";
    result = collator.compare(one,two);
    /*
    String one = r1.getFirstName()!=null?r1.getFirstName():"";
    String two = r2.getFirstName()!=null?r2.getFirstName():"";
    result = IsCollator.getIsCollator().compare(one,two);

    if (result == 0){
      one = r1.getLastName()!=null?r1.getLastName():"";
      two = r2.getLastName()!=null?r2.getLastName():"";
      result = IsCollator.getIsCollator().compare(one,two);
    }
    if (result == 0){
      one = r1.getMiddleName()!=null?r1.getMiddleName():"";
      two = r2.getMiddleName()!=null?r2.getMiddleName():"";
      result = IsCollator.getIsCollator().compare(one,two);
    }
    */

    return result;
  }

  public int apartmentCompare(Object o1, Object o2) {
    HabitantsCollector r1 = (HabitantsCollector) o1;
    HabitantsCollector r2 = (HabitantsCollector) o2;
    int result = 0;

    String one = r1.getApartment()!=null?r1.getApartment():"";
    String two = r2.getApartment()!=null?r2.getApartment():"";
    result = collator.compare(one,two);

    return result;
  }

  public int floorCompare(Object o1, Object o2) {
    HabitantsCollector r1 = (HabitantsCollector) o1;
    HabitantsCollector r2 = (HabitantsCollector) o2;
    int result = 0;

    String one = r1.getFloor()!=null?r1.getFloor():"";
    String two = r2.getFloor()!=null?r2.getFloor():"";
    result = collator.compare(one,two);

    return result;
  }

  public int addressCompare(Object o1, Object o2) {

    HabitantsCollector r1 = (HabitantsCollector) o1;
    HabitantsCollector r2 = (HabitantsCollector) o2;
    int result = 0;


    String one = r1.getAddress()!=null?r1.getAddress():"";
    String two = r2.getAddress()!=null?r2.getAddress():"";
    result = collator.compare(one,two);

    return result;
  }

}
