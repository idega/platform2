/*
 * $Id: RequestFinder.java,v 1.3 2002/02/06 10:21:17 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.block.request.business;

import com.idega.data.EntityFinder;
import is.idega.idegaweb.campus.block.request.business.RequestHolder;
import is.idega.idegaweb.campus.block.request.data.Request;
import java.util.List;
import java.util.Iterator;
import java.util.Vector;
import java.util.Hashtable;
import java.sql.SQLException;

/**
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class RequestFinder {

  public static List getRequests(int userId) {
    try {
      System.out.println("finding all requests for user = " + userId);
      List l = EntityFinder.findAllByColumn(Request.getStaticInstance(Request.class),Request.getColumnUserId(),userId);
      System.out.println("found " + l.size() + " request(s)");
      if (l == null)
        return(null);

      Iterator i = l.iterator();
      Vector requests = new Vector();
      while (i.hasNext()) {
        Request r = (Request)i.next();
        RequestHolder holder = new RequestHolder(r);
        requests.add(holder);
      }

      return(requests);
    }
    catch(SQLException e) {
      return(null);
    }
  }
}