/*
 * $Id: ReferenceNumberInfo.java,v 1.7 2001/08/29 22:55:57 laddi Exp $
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
import java.util.Date;
import java.text.DateFormat;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.interfaceobject.BackButton;

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

    Table refTable = new Table();
      refTable.setCellpadding(5);

    int row = 1;

    if (holder == null) {
      refTable.add(new Text(iwrb_.getLocalizedString("appNoSuchApplication","There is no application associated with that reference number")),1,row);
      row++;
    }
    else {
      Applicant applicant = holder.getApplicant();
      Application app = holder.getApplication();

      Text nameText = new Text(iwrb_.getLocalizedString("appHello","Hello") + " " + applicant.getFullName());
        nameText.setBold();

      refTable.add(nameText,1,row);
      row++;

      DateFormat format = DateFormat.getDateInstance(1,modinfo.getCurrentLocale());
      String date = format.format(new Date(app.getSubmitted().getTime()));
      Text dateText = new Text(date);
        dateText.setBold();

      refTable.add(new Text(iwrb_.getLocalizedString("appReceived","Your application was received") + " "),1,row);
      refTable.add(dateText,1,row);
      row++;

      String status = app.getStatus();

      if (status.equalsIgnoreCase(Application.statusSubmitted))
        status = iwrb_.getLocalizedString("appSubmitted","Waiting to be processed");
      else if (status.equalsIgnoreCase(Application.statusApproved))
        status = iwrb_.getLocalizedString("appApproved","Approved / On waiting list");
      else if (status.equalsIgnoreCase(Application.statusRejected))
        status = iwrb_.getLocalizedString("appRejected","Rejected");
      else
        status = iwrb_.getLocalizedString("appUnknownStatus","Lost in limbo somewhere");

      Contract c = holder.getContract();

      if (c == null) { //Fékk ekki úthlutað, eða ekki búið að úthluta.
        Vector wl = holder.getWaitingList();
        Vector choices = holder.getApplied();

        refTable.add(new Text(iwrb_.getLocalizedString("appStatus","Application status") + ": "),1,row);
        Text statusText = new Text(status);
          statusText.setBold();
        refTable.add(statusText,1,row);
        if ( wl == null ) {
          Text star = new Text(" *");
            star.setStyle("required");
          refTable.add(star,1,row);
          row++;
        }
        else {
          row++;
        }

        refTable.add(new Text(iwrb_.getLocalizedString("appApplied","You applied for") + ":"),1,row);
        refTable.add(Text.getBreak(),1,row);
        for (int i = 0; i < choices.size(); i++) {
          refTable.add(new Text("<li>"+iwrb_.getLocalizedString("appChoice","Choice") + " " + i + 1)+": ",1,row);
          Applied applied = (Applied)choices.elementAt(i);
          //Sæki nafn á morgun
          Text appType = new Text(applied.getApartmentTypeId() + "-" + applied.getComplexId());
            appType.setBold();
          refTable.add(appType,1,row);
          if (wl != null) {
            refTable.add(new Text(" - "),1,row);
            WaitingList wait = (WaitingList)wl.elementAt(i);
            refTable.add(new Text(iwrb_.getLocalizedString("appOrder","Number") + " " + wait.getOrder() + " " +
                         iwrb_.getLocalizedString("appOnList","on the waiting list")),1,row);
          }
          refTable.add(Text.getBreak(),1,row);
        }
        row++;

        if (wl == null) { //Ekki búið að úthluta
          Text notAllocated = new Text("&nbsp;*&nbsp;"+iwrb_.getLocalizedString("appNotYetAssigned","Apartments have not yet been allocated"));
            notAllocated.setStyle("required");
          refTable.add(notAllocated,1,row);
          row++;
        }

      }
      else {
        refTable.add(new Text(iwrb_.getLocalizedString("appAssigned","You have been assigned to an apartment")),1,row);
        row++;
      }
    }

    row++;
    refTable.add(new BackButton(iwrb_.getImage("back.gif")),1,row);
    add(refTable);
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