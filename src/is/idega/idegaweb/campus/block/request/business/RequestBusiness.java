/*
 * $Id: RequestBusiness.java,v 1.3 2002/04/06 19:11:14 tryggvil Exp $
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
import is.idega.idegaweb.campus.block.request.data.RequestBean;
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
      RequestBean req = ((is.idega.idegaweb.campus.block.request.data.RequestBeanHome)com.idega.data.IDOLookup.getHomeLegacy(RequestBean.class)).createLegacy();
      req.setUserId(userId);
      req.setDescription(comment);
      req.setDateFailure(dateOfFailure);
      req.setRequestType(type);
      if (special != null)
        req.setSpecialTime(special);
      req.setDateSent(idegaTimestamp.getTimestampRightNow());
      req.setStatus(Request.REQUEST_STATUS_SENT);
      req.insert();
    }
    catch(SQLException e) {
      return(false);
    }

    return(true);
  }
}
