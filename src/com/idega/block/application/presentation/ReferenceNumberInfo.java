/*
 * $Id: ReferenceNumberInfo.java,v 1.5 2001/08/29 18:35:25 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.application.presentation;

import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.Application;
import is.idegaweb.campus.entity.Applied;
import is.idegaweb.campus.entity.Contract;
import is.idegaweb.campus.entity.WaitingList;
import com.idega.block.application.business.ReferenceNumberHandler;
import is.idegaweb.campus.application.CampusApplicationFinder;
import is.idegaweb.campus.application.CampusApplicationHolder;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.ModuleObjectContainer;
import com.idega.jmodule.object.textObject.Text;
import com.idega.idegaweb.IWResourceBundle;
import java.util.Vector;

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class ReferenceNumberInfo extends ModuleObjectContainer {
  private static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.application";
  private IWResourceBundle iwrb_ = null;

  public ReferenceNumberInfo() {
  }

  protected void control(ModuleInfo modinfo) {
    String ref = ReferenceNumberHandler.getReferenceNumber(modinfo);

    int aid = 0;
    try {
      aid = Integer.parseInt(ref);
    }
    catch(java.lang.NumberFormatException e) {
      aid = 0;
    }

    CampusApplicationHolder holder = CampusApplicationFinder.getApplicationInfo(aid);

    if (holder == null) {
      add(new Text(iwrb_.getLocalizedString("appNoSuchApplication","There is no application associated with that reference number")));
      add(Text.getBreak());
    }
    else {
      Applicant applicant = holder.getApplicant();
      Application app = holder.getApplication();

      add(new Text(iwrb_.getLocalizedString("appHello","Hello") + " " + applicant.getFullName()));
      add(Text.getBreak());
      add(new Text(iwrb_.getLocalizedString("appReceived","Your application was received the") + " " + app.getSubmitted()));
      add(Text.getBreak());

      String status = app.getStatus();

      if (status.equalsIgnoreCase(Application.statusSubmitted))
        status = iwrb_.getLocalizedString("appSubmitted","Waiting to be processed");
      else if (status.equalsIgnoreCase(Application.statusApproved))
        status = iwrb_.getLocalizedString("appApproved","Approved?");
      else if (status.equalsIgnoreCase(Application.statusRejected))
        status = iwrb_.getLocalizedString("appRejected","Rejected");
      else
        status = iwrb_.getLocalizedString("appUnknownStatus","Lost in limbo somewhere");

      add(new Text(iwrb_.getLocalizedString("appStatus","Application status") + " : " + status ));
      add(Text.getBreak());


      Contract c = holder.getContract();

      if (c == null) {//Fékk ekki úthlutað, eða ekki búið að úthluta.
        Vector wl = holder.getWaitingList();
        Vector choices = holder.getApplied();

        if (wl == null) { //Ekki búið að úthluta
          add(new Text(iwrb_.getLocalizedString("appNotYetAssigned","Apartments have not yet been allocated")));
          add(Text.getBreak());
        }

        add(new Text(iwrb_.getLocalizedString("appApplied","Applied") + " :"));
        add(Text.getBreak());
        for (int i = 0; i < choices.size(); i++) {
          add(new Text(iwrb_.getLocalizedString("appChoice" + i + 1,"Choice" + " " + i + 1)));
          add(new Text(" : "));
          Applied applied = (Applied)choices.elementAt(i);
          //Sæki nafn á morgun
          add(new Text(applied.getApartmentTypeId() + "-" + applied.getComplexId()));
          if (wl != null) {
            add(new Text(" - "));
            WaitingList wait = (WaitingList)wl.elementAt(i);
            add(new Text(iwrb_.getLocalizedString("appOrder","Number") + " " + wait.getOrder() + " " +
                         iwrb_.getLocalizedString("appOnList","on the waiting list")));
          }
          add(Text.getBreak());
        }
      }
      else {
        add(new Text(iwrb_.getLocalizedString("appAssigned","You have been assigned to apartment")));
        add(Text.getBreak());
      }
    }
  }

  private void approved(ModuleInfo modinfo) {

  }

  private void rejected(ModuleInfo modinfo) {

  }

  public String getBundleIdentifier() {
    return(IW_BUNDLE_IDENTIFIER);
  }

  public void main(ModuleInfo modinfo) {
    iwrb_ = getResourceBundle(modinfo);
//    iwb = getBundle(modinfo);
    control(modinfo);
  }
}