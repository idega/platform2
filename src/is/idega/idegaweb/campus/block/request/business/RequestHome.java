/*
 * $Id: RequestHome.java,v 1.4 2002/04/15 16:10:09 palli Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.block.request.business;

import is.idega.idegaweb.campus.block.request.data.Request;
//import is.idega.idegaweb.campus.block.request.data.RequestBean;

/**
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class RequestHome {
  private static RequestHome _instance = null;

  private RequestHome() {
  }

  public static RequestHome getInstance() {
    if (_instance != null)
      _instance = new RequestHome();

    return(_instance);
  }

  public Request getNewRequest() {
    Request req = null;
    try {
      req = ((is.idega.idegaweb.campus.block.request.data.RequestHome)com.idega.data.IDOLookup.getHome(Request.class)).create();
    }
    catch(java.rmi.RemoteException e) {

    }
    catch(javax.ejb.CreateException e) {

    }
    return(req);
  }
}
