/*
 * $Id: RequestHolder.java,v 1.3 2002/04/06 19:11:14 tryggvil Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.block.request.business;

import is.idega.idegaweb.campus.block.request.data.Request;

/**
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class RequestHolder {
  private Request _request = null;

  RequestHolder(Request request) {
    _request = request;
  }

  public Request getRequest() {
    return(_request);
  }
}
