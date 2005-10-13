package se.idega.idegaweb.commune.message.business;

import java.util.Comparator;


import com.idega.block.process.message.data.Message;
import com.idega.util.IWTimestamp;

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

    IWTimestamp one;
    IWTimestamp two;

    one = new IWTimestamp(p1.getCreated());
    two = new IWTimestamp(p2.getCreated());
    
    if ( one != null && two != null ) {
    	if ( one.isLaterThan(two) )
    		return -1;
    	else if ( two.isLaterThan(one) )
    		return 1;
    }
    
    return result;
  }

  public boolean equals(Object obj) {
    /**@todo: Implement this java.util.Comparator method*/
    throw new java.lang.UnsupportedOperationException("Method equals() not yet implemented.");
  }
}
