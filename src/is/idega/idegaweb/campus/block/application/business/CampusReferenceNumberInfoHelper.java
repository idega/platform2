/*
 * $Id: CampusReferenceNumberInfoHelper.java,v 1.1 2001/12/07 12:22:33 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.block.application.business;

import com.idega.util.idegaTimestamp;
import com.idega.block.application.data.Applicant;
import is.idega.idegaweb.campus.block.application.data.CampusApplication;
import is.idega.idegaweb.campus.block.application.data.WaitingList;
import is.idega.idegaweb.campus.block.application.business.CampusApplicationHolder;
import java.sql.SQLException;

/**
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class CampusReferenceNumberInfoHelper {
  public static void updatePhoneAndEmail(CampusApplicationHolder holder, String phone, String email) {
    Applicant applicant = holder.getApplicant();
    applicant.setResidencePhone(phone);
    try {
      applicant.update();
    }
    catch(SQLException e) {
      e.printStackTrace();
    }

    CampusApplication application = holder.getCampusApplication();
    application.setEmail(email);
    try {
      application.update();
    }
    catch(SQLException e) {
      e.printStackTrace();
    }
  }

  public static boolean stayOnWaitingList(int waitingListId, boolean stayOnList) {
    try {
      WaitingList li = new WaitingList(waitingListId);
      if (stayOnList) {
        li.setRemovedFromList(WaitingList.NO);
        li.setLastConfirmationDate(idegaTimestamp.getTimestampRightNow());
      }
      else
        li.setRemovedFromList(WaitingList.YES);
      li.update();
      return(true);
    }
    catch(SQLException e) {
      e.printStackTrace();
      return(false);
    }
  }
}