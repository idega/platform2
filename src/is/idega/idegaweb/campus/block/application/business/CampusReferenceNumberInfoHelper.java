/*
 * $Id: CampusReferenceNumberInfoHelper.java,v 1.6 2002/02/27 13:54:58 palli Exp $
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
//import com.idega.block.application.data.ApplicantBean;
import com.idega.presentation.IWContext;
import com.idega.core.user.data.User;
import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.core.accesscontrol.data.LoginTable;
import com.idega.block.login.business.LoginCreator;
import com.idega.block.application.data.Applicant;
import com.idega.block.application.business.ReferenceNumberHandler;
import is.idega.idegaweb.campus.block.application.data.CampusApplication;
import is.idega.idegaweb.campus.block.application.data.WaitingList;
import is.idega.idegaweb.campus.block.application.business.CampusApplicationHolder;
import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.allocation.business.ContractFinder;
import java.sql.SQLException;
import java.util.Vector;
import java.util.List;
import java.util.Iterator;
import com.idega.data.GenericEntity;
import com.idega.data.EntityFinder;

/**
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class CampusReferenceNumberInfoHelper {
  public static void updatePhoneAndEmail(CampusApplicationHolder holder, String phone, String email) {
    Applicant applicant = (Applicant)holder.getApplicant();
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

  public static List getUserLogin(IWContext iwc) {
    String ref = ReferenceNumberHandler.getReferenceNumber(iwc);
    Vector l = new Vector();


    try {
      Applicant applicant = new Applicant();
      List li = EntityFinder.findAllByColumn(applicant,applicant.getSSNColumnName(),ref);
      if (li != null && !li.isEmpty()) {
        Iterator it = li.iterator();
        Contract contract = null;
        while (it.hasNext()) {
          applicant = (Applicant)it.next();
          contract = ContractFinder.findByApplicant(applicant.getID());
          if (contract != null) {
            if (contract.getIsRented())
              break;
            else
              contract = null;
          }
        }

        if (contract == null)
          return(null);

        User user = new User(contract.getUserId().intValue());
        LoginTable login = LoginDBHandler.getUserLogin(user.getID());

        java.sql.Timestamp t = login.getLastChanged();
        if (t != null)
          return(null);

        String passwd = LoginCreator.createPasswd(8);
        LoginDBHandler.updateLogin(user.getID(),login.getUserLogin(),passwd);

        l.add(login.getUserLogin());
        l.add(passwd);
      }
    }
    catch(SQLException e) {

    }
    catch(Exception ex) {

    }

    return(l);
  }
}