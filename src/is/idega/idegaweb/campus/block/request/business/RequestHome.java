/*
 * $Id: RequestHome.java,v 1.2 2002/03/18 15:50:44 palli Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.block.request.business;

import is.idega.idegaweb.campus.block.request.data.Request;
import is.idega.idegaweb.campus.block.request.data.RequestBean;

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
    return(new RequestBean());
  }
}