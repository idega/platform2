/*
 * $Id: CampusReferenceNumberInfoHelper.java,v 1.14 2004/05/24 14:21:42 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.block.application.business;

import is.idega.idegaweb.campus.block.allocation.business.ContractFinder;
import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.application.data.CampusApplication;
import is.idega.idegaweb.campus.block.application.data.WaitingList;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.idega.block.application.business.ReferenceNumberHandler;
import com.idega.block.application.data.Applicant;
import com.idega.core.accesscontrol.business.LoginCreator;
import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.core.accesscontrol.data.LoginTable;
import com.idega.core.user.data.User;
import com.idega.data.EntityFinder;
import com.idega.presentation.IWContext;
import com.idega.util.IWTimestamp;

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
      WaitingList li = ((is.idega.idegaweb.campus.block.application.data.WaitingListHome)com.idega.data.IDOLookup.getHomeLegacy(WaitingList.class)).findByPrimaryKeyLegacy(waitingListId);
      if (stayOnList) {
        li.setRemovedFromList(is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean.NO);
        li.setLastConfirmationDate(IWTimestamp.getTimestampRightNow());
      }
      else
        li.setRemovedFromList(is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean.YES);
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
      Applicant applicant = ((com.idega.block.application.data.ApplicantHome)com.idega.data.IDOLookup.getHomeLegacy(Applicant.class)).createLegacy();
      List li = EntityFinder.findAllByColumn(applicant,com.idega.block.application.data.ApplicantBMPBean.getSSNColumnName(),ref);
      if (li != null && !li.isEmpty()) {
        Iterator it = li.iterator();
        List contracts = null;
        Contract contract = null;
        while (it.hasNext()) {
          applicant = (Applicant)it.next();
          contracts = ContractFinder.findAllContractsByApplicant(applicant.getID());
          if (contracts != null && !contracts.isEmpty()) {
          	Iterator it2 = contracts.iterator();
          	while (it2.hasNext()) {
          		contract = (Contract)it2.next();
		          System.out.println("Contract id = " + contract.getID());
		          
  	  	      if (contract != null) {
    		        if (contract.getIsRented())
      		        break;
        		    else
          		    contract = null;
	          	}
          	}
          }
          
          if (contract != null)
          	break;
        }

        if (contract == null)
          return(null);


        User user = ((com.idega.core.user.data.UserHome)com.idega.data.IDOLookup.getHomeLegacy(User.class)).findByPrimaryKeyLegacy(contract.getUserId().intValue());
        l.add(new Integer(user.getID()));
        LoginTable login = LoginDBHandler.getUserLogin(user.getID());

        java.sql.Timestamp t = login.getLastChanged();
        if (t == null){
          String passwd = LoginCreator.createPasswd(8);
          LoginDBHandler.updateLogin(user.getID(),login.getUserLogin(),passwd);

          l.add(login.getUserLogin());
          l.add(passwd);
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
