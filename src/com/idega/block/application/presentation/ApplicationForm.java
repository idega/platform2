/*
 * $Id: ApplicationForm.java,v 1.1 2001/06/27 14:40:19 palli Exp $
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
import com.idega.block.application.business.ApplicationFinder;
import com.idega.jmodule.object.Editor;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.interfaceobject.Form;
import com.idega.jmodule.object.interfaceobject.TextInput;
import com.idega.jmodule.object.interfaceobject.SubmitButton;
import com.idega.jmodule.object.interfaceobject.DropdownMenu;
import com.idega.jmodule.object.interfaceobject.HiddenInput;
import com.idega.jmodule.object.textObject.Text;
import com.idega.jmodule.object.Table;
import com.idega.util.idegaTimestamp;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class ApplicationForm extends Editor {
  private final int statusEnteringPage_ = 0;
  private final int statusSubject_ = 1;
  private final int statusGeneralInfo_ = 2;

  private String styleAttribute = "font-size: 8pt";

  private Text textTemplate = new Text();

  public ApplicationForm() {
  }

  protected void control(ModuleInfo modinfo) {
    String statusString = modinfo.getParameter("status");
    int status = 0;

    if (statusString == null){
      status = statusEnteringPage_;
    }
    else {
      status = Integer.parseInt(statusString);
    }

    if (status == statusEnteringPage_) {
      doSelectSubject(modinfo);
    }
    else if (status == statusSubject_) {
      saveSubject(modinfo);
      doGeneralInformation(modinfo);
    }
    else if (status == statusGeneralInfo_) {
      saveApplicantInformation(modinfo);
      if (saveDataToDB(modinfo))
        doDone();
      else
        doError();
    }
  }

  protected void doSelectSubject(ModuleInfo modinfo) {
    List subjects = ApplicationFinder.listOfSubject();
    Form form = new Form();

    DropdownMenu subject = new DropdownMenu(subjects,"subject");

    form.add("Umsókn um : ");
    form.add(subject);
    form.add(Text.getBreak());
    form.add(new SubmitButton("ok","áfram"));
    form.add(new HiddenInput("status",Integer.toString(statusSubject_)));
    add(form);
  }

  protected void doGeneralInformation(ModuleInfo modinfo) {
    Form form = new Form();
    Table t = new Table(2,11);
    form.add(t);
    t.mergeCells(1,1,2,1);
    t.add("Almennar upplýsingar um umsækjanda",1,1);
    t.add("Fornafn",1,3);
    t.add(new TextInput("firstName"),2,3);
    t.add("Millinafn",1,4);
    t.add(new TextInput("middleName"),2,4);
    t.add("Eftirnafn",1,5);
    t.add(new TextInput("lastName"),2,5);
    t.add("Kennitala",1,6);
    t.add(new TextInput("ssn"),2,6);
    t.add("Lögheimili",1,7);
    t.add(new TextInput("legalResidence"),2,7);
    t.add("Dvalarstaður",1,8);
    t.add(new TextInput("residence"),2,8);
    t.add("Símanúmer á dvalarstað",1,9);
    t.add(new TextInput("residencePhone"),2,9);
    t.add("Póstnúmer",1,10);
    t.add(new TextInput("po"),2,10);
    t.add(new SubmitButton("ok","Áfram"),2,11);
    form.add(new HiddenInput("status",Integer.toString(statusGeneralInfo_)));
    add(form);
  }

  protected void doDone() {
    add("Umsókn skráð");
  }

  protected void doError() {
    add("Gagnagrunnsvilla við skráningu umsóknar");
  }

  protected boolean saveDataToDB(ModuleInfo modinfo) {
    Applicant applicant = (Applicant)modinfo.getSessionAttribute("applicant");
    Application application = (Application)modinfo.getSessionAttribute("application");

    try {
      applicant.insert();

      application.setApplicantId(applicant.getID());
      application.insert();
    }
    catch(SQLException e) {
      System.err.println(e.toString());
      return(false);
    }
    finally {
      modinfo.removeSessionAttribute("applicant");
      modinfo.removeSessionAttribute("application");
    }

    return(true);
  }

  protected void saveSubject(ModuleInfo modinfo) {
    String subject = (String)modinfo.getParameter("subject");
    Application application = new Application();
    application.setSubjectId(Integer.parseInt(subject));
    application.setSubmitted(idegaTimestamp.getTimestampRightNow());
    application.setStatusSubmitted();
    application.setStatusChanged(idegaTimestamp.getTimestampRightNow());
    modinfo.setSessionAttribute("application",application);
  }

  protected void saveApplicantInformation(ModuleInfo modinfo) {
    String firstName = modinfo.getParameter("firstName");
    String middleName = modinfo.getParameter("middleName");
    String lastName = modinfo.getParameter("lastName");
    String ssn = modinfo.getParameter("ssn");
    String legalResidence = modinfo.getParameter("legalResidence");
    String residence = modinfo.getParameter("residence");
    String residencePhone = modinfo.getParameter("residencePhone");
    String po = modinfo.getParameter("po");

    Applicant applicant = new Applicant();
    applicant.setFirstName(firstName);
    applicant.setMiddleName(middleName);
    applicant.setLastName(lastName);
    applicant.setSSN(ssn);
    applicant.setLegalResidence(legalResidence);
    applicant.setResidence(residence);
    applicant.setResidencePhone(residencePhone);
    applicant.setPO(po);

    modinfo.setSessionAttribute("applicant",applicant);
  }

}