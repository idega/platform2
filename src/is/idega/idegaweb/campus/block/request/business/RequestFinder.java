/*
 * $Id: RequestFinder.java,v 1.1 2001/12/29 14:03:15 palli Exp $
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
import is.idega.idegaweb.campus.block.request.data.RequestType;
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
      Request R = new Request();
      List l = EntityFinder.findAllByColumn(R,R.getColumnUserId(),userId);
      if (l == null)
        return(null);

      List type = getRequestTypes();
      if (type == null)
        return(null);

      Iterator i = type.iterator();
      Hashtable types = new Hashtable();
      while (i.hasNext()) {
        RequestType t = (RequestType)i.next();
        types.put(t.getIDInteger(),t);
      }

      i = l.iterator();
      Vector requests = new Vector();
      while (i.hasNext()) {
        Request r = (Request)i.next();
        int iType = r.getRequestTypeId();
        RequestType t = (RequestType)types.get(new Integer(iType));
        RequestHolder holder = new RequestHolder(r,t);
        requests.add(holder);
      }

      return(requests);
    }
    catch(SQLException e) {
      return(null);
    }
  }

  public static List getRequestTypes() {
    try {
      return(EntityFinder.findAll(new RequestType()));
    }
    catch(SQLException e) {
      return(null);
    }
  }
}