package com.idega.block.category.business;

import java.rmi.RemoteException;
import com.idega.business.IBOLookup;
import com.idega.core.data.ICCategory;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.util.IsCollator;
import java.util.*;
import java.util.Comparator;
import java.sql.SQLException;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */

public class CategoryComparator implements Comparator {


  public CategoryComparator() {
   }

  public int compare(Object o1, Object o2) {
      ICCategory p1 = (ICCategory) o1;
      ICCategory p2 = (ICCategory) o2;
      int result = 0;

    String one = p1.getName();
    String two = p2.getName();

    return IsCollator.getIsCollator().compare(one,two);

  }

  public boolean equals(Object obj) {
    return true;
  }

}
