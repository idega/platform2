

package com.idega.block.reports.business ;



import java.util.Comparator;





/**

 * Title:

 * Description:

 * Copyright:    Copyright (c) 2001

 * Company:      idega multimedia

 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>

 * @version 1.0

 */



public class ContentComparator implements Comparator {



  private int sortBy;



  public ContentComparator() {

      this.sortBy = 0;

  }

  public ContentComparator(int toSortBy) {

      this.sortBy = toSortBy;

  }

  public void sortBy(int toSortBy) {

      this.sortBy = toSortBy;

  }

  public int compare(Object o1, Object o2) {

    int result = 0;

    //if(obOne instanceof Comparable && obOne instanceof Comparable)

    //  result = obOne.compare(obTwo);// IsCollator.getIsCollator().compare(p1.getContent(sortBy),p2.getContent(sortBy));

    return result;

  }

  public boolean equals(Object obj) {

    /**@todo: Implement this java.util.Comparator method*/

    throw new java.lang.UnsupportedOperationException("Method equals() not yet implemented.");

  }

}

