/*
 * $Id: RequestFinder.java,v 1.9 2003/08/11 20:56:04 aron Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.block.request.business;

import is.idega.idegaweb.campus.block.request.data.Request;
import is.idega.idegaweb.campus.block.request.data.RequestHome;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.idega.data.IDOLookup;

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
      Collection l = getRequstHome().findByUser(new Integer(userId));
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
    catch(Exception e) {
      return(null);
    }
  }

  public static List getAllRequests() {
    try {
      Collection l = getRequstHome().findAll();

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
    catch(Exception e) {
      return(null);
    }
  }

  public static List getAllRequestsByType(String type) {
    try {
    	
    	Collection l = getRequstHome().findByType(type);
     

      if (l == null )
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
    catch(Exception e) {
      return(null);
    }
  }
  
  public static List getAllRequestsByStatus(String type) {
	 try {
    	
		 Collection l = getRequstHome().findByStatus(type);
     

	   if (l == null )
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
	 catch(Exception e) {
	   return(null);
	 }
   }
  
  public static RequestHome getRequstHome() throws RemoteException{
  	return (RequestHome) IDOLookup.getHome(Request.class);
  }
  
}
