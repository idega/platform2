/*
 * $Id: RequestHolder.java,v 1.1 2001/12/29 14:03:15 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.block.request.business;

import is.idega.idegaweb.campus.block.request.data.Request;
import is.idega.idegaweb.campus.block.request.data.RequestType;

/**
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class RequestHolder {
  private Request _request = null;
  private RequestType _type = null;

  RequestHolder(Request request, RequestType type) {
    _request = request;
    _type = type;
  }

  public Request getRequest() {
    return(_request);
  }

  public RequestType getRequestType() {
    return(_type);
  }
}