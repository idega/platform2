/*
 * $Id: RequestFinder.java,v 1.8 2002/04/15 16:10:09 palli Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
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
/*  public static final String REQUEST_STATUS_SENT = Request.REQUEST_STATUS_SENT;
  public static final String REQUEST_STATUS_RECEIVED = Request.REQUEST_STATUS_RECEIVED;
  public static final String REQUEST_STATUS_IN_PROGRESS = Request.REQUEST_STATUS_IN_PROGRESS;
  public static final String REQUEST_STATUS_DONE = Request.REQUEST_STATUS_DONE;
  public static final String REQUEST_STATUS_DENIED = Request.REQUEST_STATUS_DENIED;*/

  public static final String REQUEST_COMPUTER = "C";
  public static final String REQUEST_REPAIR = "R";

  public static final String REQUEST_STATUS_SENT = "S";
  public static final String REQUEST_STATUS_RECEIVED = "R";
  public static final String REQUEST_STATUS_IN_PROGRESS = "P";
  public static final String REQUEST_STATUS_DONE = "D";
  public static final String REQUEST_STATUS_DENIED = "X";


  public static List getRequests(int userId) {
    try {
      List l = EntityFinder.findAllByColumn(is.idega.idegaweb.campus.block.request.data.RequestBMPBean.getStaticInstance(Request.class),is.idega.idegaweb.campus.block.request.data.RequestBMPBean.getColumnUserId(),userId);
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

  public static List getAllRequests() {
    try {
      List l = EntityFinder.findAll(is.idega.idegaweb.campus.block.request.data.RequestBMPBean.getStaticInstance(Request.class));

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

  public static List getAllRequestsByType(String type) {
    try {
      List l = EntityFinder.findAllByColumn(is.idega.idegaweb.campus.block.request.data.RequestBMPBean.getStaticInstance(Request.class),is.idega.idegaweb.campus.block.request.data.RequestBMPBean.getColumnStatus(),type);

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
