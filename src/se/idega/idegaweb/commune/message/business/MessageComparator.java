package se.idega.idegaweb.commune.message.business;

import com.idega.util.IWTimeStamp;

import se.idega.idegaweb.commune.message.data.*;
import java.rmi.RemoteException;
import java.util.*;
import java.util.Comparator;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:laddi@idega.is">laddi@idega.is</a>
 * @version 1.0
 */

public class MessageComparator implements Comparator {

  public MessageComparator() {
  }

  public int compare(Object o1, Object o2) {
    int result = 0;

    Message p1 = (Message) o1;
    Message p2 = (Message) o2;

    IWTimeStamp one;
    IWTimeStamp two;

    try {
      one = new IWTimeStamp(p1.getCreated());
    }
    catch (RemoteException e) {
      one = null;
    }

    try {
      two = new IWTimeStamp(p2.getCreated());
    }
    catch (RemoteException e) {
      two = null;
    }
    
    if ( one != null && two != null ) {
    	if ( one.isLaterThan(two) )
    		return -1;
    	else if ( two.isLaterThan(one) )
    		return 1;
    }
    System.out.println("Return: "+result);
    
    return result;
  }

  public boolean equals(Object obj) {
    /**@todo: Implement this java.util.Comparator method*/
    throw new java.lang.UnsupportedOperationException("Method equals() not yet implemented.");
  }
}
