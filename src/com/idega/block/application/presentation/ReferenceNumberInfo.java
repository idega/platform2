/*
 * $Id: ReferenceNumberInfo.java,v 1.3 2001/08/08 12:46:20 palli Exp $
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
import com.idega.block.application.business.ReferenceNumberHandler;
import is.idegaweb.campus.application.CampusApplicationFinder;
import is.idegaweb.campus.application.CampusApplicationHolder;
import is.idegaweb.campus.entity.CampusApplication;
import com.idega.util.CypherText;
import com.idega.jmodule.object.Editor;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.interfaceobject.Form;
import com.idega.jmodule.object.interfaceobject.TextInput;
import com.idega.jmodule.object.interfaceobject.SubmitButton;
import com.idega.jmodule.object.interfaceobject.DropdownMenu;
import com.idega.jmodule.object.interfaceobject.HiddenInput;
import com.idega.jmodule.object.textObject.Text;
import com.idega.jmodule.object.Table;
import com.idega.idegaweb.IWBundle;
import com.idega.util.idegaTimestamp;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class ReferenceNumberInfo extends Editor {
  private static final String IW_BUNDLE_IDENTIFIER = "is.idegaweb.campus";

  public ReferenceNumberInfo() {
  }

  protected void control(ModuleInfo modinfo) {
    String ref = ReferenceNumberHandler.getReferenceNumber(modinfo);

    int aid = Integer.parseInt(ref);

    CampusApplicationHolder holder = CampusApplicationFinder.getApplicationInfo(aid);

    if (holder == null) {
      add(new Text("Það er engin umsókn skráð á þetta tilvísunarnúmer"));
      add(Text.getBreak());
    }
    else {
      Applicant applicant = holder.getApplicant();
      Application app = holder.getApplication();

      add(new Text("Halló " + applicant.getFirstName()));
      add(Text.getBreak());
      add(new Text("Umsókn þín er : " + app.getStatus()));
    }
  }

  private void approved(ModuleInfo modinfo) {

  }

  private void rejected(ModuleInfo modinfo) {

  }

  public String getBundleIdentifier() {
    return(IW_BUNDLE_IDENTIFIER);
  }
}


/*public class ApplicationForm extends Editor {
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
    IWResourceBundle iwrb = getResourceBundle(modinfo);
    List subjects = ApplicationFinder.listOfNonExpiredSubjects();
    Text textTemplate = new Text();

    Form form = new Form();
    Table t = new Table(2,3);
    t.setWidth(1,"50%");
    t.setWidth(2,"50%");

    Text heading = (Text)textTemplate.clone();
    heading.setStyle("headlinetext");
    heading.setText(iwrb.getLocalizedString("applicationSubject","Veldu tegund umsóknar"));
    Text text1 = (Text)textTemplate.clone();
    text1.setStyle("bodytext");
    text1.setText(iwrb.getLocalizedString("applicationSubject","Umsókn um"));
    text1.setBold();
    Text required = (Text)textTemplate.clone();
    required.setText(" * ");
    required.setBold();
    required.setStyle("required");
    Text info = (Text)textTemplate.clone();
    info.setText(iwrb.getLocalizedString("mustFillOut","* Stjörnumerkt svæði verður að fylla út"));
    info.setStyle("subtext");

    DropdownMenu subject = new DropdownMenu(subjects,"subject");
    subject.setStyle("formstyle");
    SubmitButton ok = new SubmitButton("ok",iwrb.getLocalizedString("ok","áfram"));
    ok.setStyle("idega");

    form.add(heading);
    form.add(Text.getBreak());
    form.add(Text.getBreak());
    form.add(t);

    t.add(text1,1,1);
    t.add(required,1,1);
    t.add(subject,2,1);
    t.add(ok,2,3);
    form.add(Text.getBreak());
    form.add(Text.getBreak());
    form.add(Text.getBreak());
    form.add(info);
    form.add(new HiddenInput("status",Integer.toString(statusSubject_)));
    add(form);
  }

  protected void doGeneralInformation(ModuleInfo modinfo) {
    IWResourceBundle iwrb = getResourceBundle(modinfo);
    Text textTemplate = new Text();
    TextInput textInputTemplate = new TextInput();
    Form form = new Form();
    Table t = new Table(2,10);
    SubmitButton ok = new SubmitButton("ok",iwrb.getLocalizedString("ok","áfram"));
    ok.setStyle("idega");

    Text heading = (Text)textTemplate.clone();
    heading.setStyle("headlinetext");
    heading.setText(iwrb.getLocalizedString("generalInfo","Almennar upplýsingar um umsækjanda"));
    Text text1 = (Text)textTemplate.clone();
    text1.setStyle("bodytext");
    text1.setText(iwrb.getLocalizedString("firstName","Fornafn"));
    text1.setBold();
    Text text2 = (Text)textTemplate.clone();
    text2.setStyle("bodytext");
    text2.setText(iwrb.getLocalizedString("middleName","Millinafn"));
    Text text3 = (Text)textTemplate.clone();
    text3.setStyle("bodytext");
    text3.setText(iwrb.getLocalizedString("lastName","Eftirnafn"));
    text3.setBold();
    Text text4 = (Text)textTemplate.clone();
    text4.setStyle("bodytext");
    text4.setText(iwrb.getLocalizedString("ssn","Kennitala"));
    text4.setBold();
    Text text5 = (Text)textTemplate.clone();
    text5.setStyle("bodytext");
    text5.setText(iwrb.getLocalizedString("legalResidence","Lögheimili"));
    text5.setBold();
    Text text6 = (Text)textTemplate.clone();
    text6.setStyle("bodytext");
    text6.setText(iwrb.getLocalizedString("residence","Dvalarstaður"));
    text6.setBold();
    Text text7 = (Text)textTemplate.clone();
    text7.setStyle("bodytext");
    text7.setText(iwrb.getLocalizedString("residencePhone","Símanúmer á dvalarstað"));
    text7.setBold();
    Text text8 = (Text)textTemplate.clone();
    text8.setStyle("bodytext");
    text8.setText(iwrb.getLocalizedString("po","Póstnúmer"));
    text8.setBold();
    Text required = (Text)textTemplate.clone();
    required.setText(" * ");
    required.setBold();
    required.setStyle("required");
    Text info = (Text)textTemplate.clone();
    info.setText(iwrb.getLocalizedString("mustFillOut","* Stjörnumerkt svæði verður að fylla út"));
    info.setStyle("subtext");
    TextInput input1 = (TextInput)textInputTemplate.clone();
    input1.setName("firstName");
    input1.setStyle("formstyle");
    TextInput input2 = (TextInput)textInputTemplate.clone();
    input2.setName("middleName");
    input2.setStyle("formstyle");
    TextInput input3 = (TextInput)textInputTemplate.clone();
    input3.setName("lastName");
    input3.setStyle("formstyle");
    TextInput input4 = (TextInput)textInputTemplate.clone();
    input4.setName("ssn");
    input4.setLength(11);
    input4.setStyle("formstyle");
    TextInput input5 = (TextInput)textInputTemplate.clone();
    input5.setName("legalResidence");
    input5.setStyle("formstyle");
    TextInput input6 = (TextInput)textInputTemplate.clone();
    input6.setName("residence");
    input6.setStyle("formstyle");
    TextInput input7 = (TextInput)textInputTemplate.clone();
    input7.setName("residencePhone");
    input7.setLength(8);
    input7.setStyle("formstyle");
    TextInput input8 = (TextInput)textInputTemplate.clone();
    input8.setName("po");
    input8.setLength(3);
    input8.setStyle("formstyle");

    t.setWidth(1,"50%");
    t.setWidth(2,"50%");
    t.add(text1,1,1);
    t.add(required,1,1);
    t.add(input1,2,1);
    t.add(text2,1,2);
    t.add(input2,2,2);
    t.add(text3,1,3);
    t.add(required,1,3);
    t.add(input3,2,3);
    t.add(text4,1,4);
    t.add(required,1,4);
    t.add(input4,2,4);
    t.add(text5,1,5);
    t.add(required,1,5);
    t.add(input5,2,5);
    t.add(text6,1,6);
    t.add(required,1,6);
    t.add(input6,2,6);
    t.add(text7,1,7);
    t.add(required,1,7);
    t.add(input7,2,7);
    t.add(text8,1,8);
    t.add(required,1,8);
    t.add(input8,2,8);
    t.add(ok,2,10);

    form.add(heading);
    form.add(Text.getBreak());
    form.add(Text.getBreak());
    form.add(t);
    form.add(Text.getBreak());
    form.add(Text.getBreak());
    form.add(Text.getBreak());
    form.add(info);
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
*/