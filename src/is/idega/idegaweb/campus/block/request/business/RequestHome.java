/*
 * $Id: RequestHome.java,v 1.1 2002/02/21 00:22:21 palli Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.block.request.business;

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
}