/*
 * $Id: RequestBusiness.java,v 1.7.4.1 2007/09/03 22:44:41 eiki Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.block.request.business;

import is.idega.idegaweb.campus.block.request.data.Request;

import java.sql.Timestamp;

import com.idega.util.IWTimestamp;

/**
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class RequestBusiness {
/*  public static final String REQUEST_COMPUTER = Request.REQUEST_COMPUTER;
  public static final String REQUEST_REPAIR = Request.REQUEST_REPAIR;*/
  public static final String REQUEST_COMPUTER = "C";
  public static final String REQUEST_REPAIR = "R";

  /**
   *
   */
  public static boolean insertRequest(int userId, String comment, Timestamp dateOfFailure, String type, String special,boolean reportedViaTelephone) {
    try {
      Request req = ((is.idega.idegaweb.campus.block.request.data.RequestHome)com.idega.data.IDOLookup.getHome(Request.class)).create();
      req.setUserId(userId);
      req.setDescription(comment);
      req.setDateFailure(dateOfFailure);
      req.setRequestType(type);
      if (special != null){
        req.setSpecialTime(special);
      }
      req.setDateSent(IWTimestamp.getTimestampRightNow());
      req.setStatus(RequestFinder.REQUEST_STATUS_SENT);
      req.setReportedViaTelephone(reportedViaTelephone);
//      ((is.idega.idegaweb.campus.block.request.data.RequestHome)com.idega.data.IDOLookup.getHome(Request.class)).
      req.store();
    }
    catch(java.rmi.RemoteException e) {
      return(false);
    }
    catch(javax.ejb.CreateException e) {
      return(false);
    }

    return(true);
  }
}
