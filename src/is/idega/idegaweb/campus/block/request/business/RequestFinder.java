/*
 * $Id: RequestFinder.java,v 1.4 2002/02/11 10:46:04 palli Exp $
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
      List l = EntityFinder.findAllByColumn(Request.getStaticInstance(Request.class),Request.getColumnUserId(),userId);
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