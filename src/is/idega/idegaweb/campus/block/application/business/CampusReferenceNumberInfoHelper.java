/*
 * $Id: CampusReferenceNumberInfoHelper.java,v 1.3 2002/02/11 10:45:03 palli Exp $
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
import com.idega.presentation.IWContext;
import is.idega.idegaweb.campus.block.application.data.CampusApplication;
import is.idega.idegaweb.campus.block.application.data.WaitingList;
import is.idega.idegaweb.campus.block.application.business.CampusApplicationHolder;
import java.sql.SQLException;
import java.util.Vector;
import java.util.List;
import com.idega.data.GenericEntity;
import com.idega.data.EntityFinder;

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

  public static List getUserLogin(IWContext iwc) {
    String ref = com.idega.block.application.business.ReferenceNumberHandler.getReferenceNumber(iwc);
    Vector l = new Vector();


    try {
      com.idega.block.application.data.Applicant applicant = new com.idega.block.application.data.Applicant();
      List li = EntityFinder.findAllByColumn(applicant,applicant.getSSNColumnName(),ref);
      if (li != null && !li.isEmpty()) {
        applicant = (Applicant)li.get(li.size()-1);
        is.idega.idegaweb.campus.block.allocation.data.Contract contract = new is.idega.idegaweb.campus.block.allocation.data.Contract();
        li = EntityFinder.findAllByColumn(contract,contract.getApplicantIdColumnName(),applicant.getID());
        if (li != null && !li.isEmpty()) {
          contract = (is.idega.idegaweb.campus.block.allocation.data.Contract)li.get(0);
          com.idega.core.user.data.User user = new com.idega.core.user.data.User(contract.getUserId().intValue());

          com.idega.core.accesscontrol.data.LoginTable login = new com.idega.core.accesscontrol.data.LoginTable();
          li = EntityFinder.findAllByColumn(login,login.getUserIDColumnName(),user.getID());
          if (li != null && !li.isEmpty()) {
            login = (com.idega.core.accesscontrol.data.LoginTable)li.get(0);

            String passwd = com.idega.block.login.business.LoginCreator.createPasswd(8);

//            login.setUserPassword(passwd);
//            login.update();
//            com.idega.core.accesscontrol.business.LoginDBHandler.getUserLogin();
//            Contract contract = is.idega.idegaweb.campus.block.allocation.business.ContractFinder.findApplicant(applicant.getID());
            com.idega.core.accesscontrol.business.LoginDBHandler.updateLogin(user.getID(),login.getUserLogin(),passwd);


            l.add(login.getUserLogin());
            l.add(passwd);
          }
        }
      }
    }
    catch(SQLException e) {

    }
    catch(Exception ex) {

    }

    return(l);
  }
}