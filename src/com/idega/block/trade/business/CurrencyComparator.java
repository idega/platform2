
package com.idega.block.trade.business;

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

public class CurrencyComparator implements Comparator {

  public CurrencyComparator() {
  }

  public int compare(Object o1, Object o2) {
    CurrencyHolder p1 = (CurrencyHolder) o1;
    CurrencyHolder p2 = (CurrencyHolder) o2;

    String one = p1.getCurrencyName()!=null?p1.getCurrencyName():"";
    String two = p2.getCurrencyName()!=null?p2.getCurrencyName():"";

    return IsCollator.getIsCollator().compare(one,two);
  }

  public boolean equals(Object obj) {
    /**@todo: Implement this java.util.Comparator method*/
    throw new java.lang.UnsupportedOperationException("Method equals() not yet implemented.");
  }

}
