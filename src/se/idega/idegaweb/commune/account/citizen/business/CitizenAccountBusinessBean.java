/*
 * $Id: CitizenAccountBusinessBean.java,v 1.1 2002/06/28 15:16:30 palli Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.account.citizen.business;

import com.idega.block.process.business.CaseBusinessBean;
import com.idega.data.IDOLookup;
import com.idega.user.data.UserHome;
import com.idega.user.data.User;
import se.idega.idegaweb.commune.account.citizen.data.CitizenAccount;
import se.idega.idegaweb.commune.account.citizen.data.CitizenAccountHome;

/**
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class CitizenAccountBusinessBean extends CaseBusinessBean implements CitizenAccountBusiness {
  public boolean insertApplication(User user, String pid, String email, String phoneHome, String phoneWork) {
    try {
      CitizenAccount application = ((CitizenAccountHome)IDOLookup.getHome(CitizenAccount.class)).create();
      application.setPID(pid);
      if (user != null)
        application.setOwner(user);
      application.setPhoneHome(phoneHome);
      if (email != null)
        application.setEmail(email);
      if (phoneWork != null)
        application.setPhoneWork(phoneWork);
      application.setCaseStatus(getCaseStatusOpen());

      application.store();
    }
    catch(Exception e) {
      e.printStackTrace();

      return false;
    }
    return true;
  }

  public User getUser(String pid) {
    User user = null;
    try {
      user = ((UserHome)IDOLookup.getHome(User.class)).findByPersonalID(pid);
    }
    catch(Exception e) {
      return null;
    }

    return user;
  }
}