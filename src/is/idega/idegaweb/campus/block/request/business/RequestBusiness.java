/*
 * $Id: RequestBusiness.java,v 1.1 2002/02/06 10:21:17 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.block.request.business;

import com.idega.util.idegaTimestamp;
import is.idega.idegaweb.campus.block.request.data.Request;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class RequestBusiness {
  public static final String REQUEST_COMPUTER = Request.REQUEST_COMPUTER;
  public static final String REQUEST_REPAIR = Request.REQUEST_REPAIR;

  /**
   *
   */
  public static boolean insertRequest(int userId, String comment, Timestamp dateOfFailure, String type, String special) {
    try {
      Request req = new Request();
      req.setUserId(userId);
      req.setDescription(comment);
      req.setDateFailure(dateOfFailure);
      req.setRequestType(type);
      if (special != null)
        req.setSpecialTime(special);
      req.setDateSent(idegaTimestamp.getTimestampRightNow());
      req.insert();
    }
    catch(SQLException e) {
      return(false);
    }

    return(true);
  }
}