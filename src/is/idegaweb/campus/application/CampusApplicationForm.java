/*
 * $Id: CampusApplicationForm.java,v 1.2 2001/06/28 13:07:45 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.application;

import com.idega.block.application.presentation.ApplicationForm;
import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.ApplicationSubject;
import com.idega.block.application.business.ApplicationFinder;
import com.idega.block.building.business.BuildingFinder;
import com.idega.block.building.business.ApartmentTypeComplexHelper;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.interfaceobject.DropdownMenu;
import com.idega.jmodule.object.interfaceobject.Form;
import com.idega.jmodule.object.interfaceobject.TextInput;
import com.idega.jmodule.object.interfaceobject.TextArea;
import com.idega.jmodule.object.interfaceobject.DateInput;
import com.idega.jmodule.object.interfaceobject.CheckBox;
import com.idega.jmodule.object.interfaceobject.SubmitButton;
import com.idega.jmodule.object.interfaceobject.HiddenInput;
import com.idega.jmodule.object.textObject.Text;
import com.idega.util.idegaTimestamp;
import is.idegaweb.campus.application.CampusApplicationFinder;
import is.idegaweb.campus.entity.Application;
import is.idegaweb.campus.entity.Applied;
import java.util.List;
import java.sql.SQLException;

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class CampusApplicationForm extends ApplicationForm {
  private final int statusEnteringPage_ = 0;
  private final int statusSubject_ = 1;
  private final int statusGeneralInfo_ = 2;
  private final int statusCampusInfo_ = 3;
  private final int statusAppliedFor_ = 4;

  private String styleAttribute = "font-size: 8pt";
  private TextInput textInputTemplate = new TextInput();

  public CampusApplicationForm() {
    setStyleAttribute(styleAttribute);
  }

  protected void control(ModuleInfo modinfo) {
    String statusString = modinfo.getParameter("status");
    int status = 0;

    if (statusString == null) {
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
      doCampusInformation(modinfo);
    }
    else if (status == statusCampusInfo_) {
      saveCampusInformation(modinfo);
      doSelectAppliedFor(modinfo);
    }
    else if (status == statusAppliedFor_) {
      saveAppliedFor(modinfo);
      if (saveDataToDB(modinfo))
        doDone();
      else
        doError();
    }
  }

  protected void doSelectSubject(ModuleInfo modinfo) {
    List subjects = ApplicationFinder.listOfSubject();
    List categories = BuildingFinder.listOfApartmentCategory();

    Form form = new Form();

    DropdownMenu subject = new DropdownMenu(subjects,"subject");
    DropdownMenu aprtCat = new DropdownMenu(categories,"aprtCat");

    form.add("Umsókn um : ");
    form.add(subject);
    form.add(Text.getBreak());
    form.add("Veldu tegund íbúðar sem sækja á um : ");
    form.add(aprtCat);
    form.add(Text.getBreak());
    form.add(new SubmitButton("ok","áfram"));
    form.add(new HiddenInput("status",Integer.toString(statusSubject_)));
    add(form);
  }

  protected void saveSubject(ModuleInfo modinfo) {
    String subject = (String)modinfo.getParameter("subject");
    String aprtCat = (String)modinfo.getParameter("aprtCat");
    com.idega.block.application.data.Application application = new com.idega.block.application.data.Application();
    application.setSubjectId(Integer.parseInt(subject));
    application.setSubmitted(idegaTimestamp.getTimestampRightNow());
    application.setStatusSubmitted();
    application.setStatusChanged(idegaTimestamp.getTimestampRightNow());
    modinfo.setSessionAttribute("application",application);
    modinfo.setSessionAttribute("aprtCat",aprtCat);
  }


  protected void doSelectAppliedFor(ModuleInfo modinfo) {
    int id;
    String aprtCat = (String)modinfo.getSessionAttribute("aprtCat");
    try {
      id = Integer.parseInt(aprtCat);
    }
    catch(Exception e) {
      id = 0;
    }

    java.util.Vector vAprtType = BuildingFinder.getApartmentTypesComplexForCategory(id);
    DropdownMenu aprtType = new DropdownMenu("aprtType");
    DropdownMenu aprtType2 = new DropdownMenu("aprtType2");
    DropdownMenu aprtType3 = new DropdownMenu("aprtType3");
    aprtType2.addMenuElement("-1","");
    aprtType3.addMenuElement("-1","");

    for (int i = 0; i < vAprtType.size(); i++) {
      ApartmentTypeComplexHelper eAprtType = (ApartmentTypeComplexHelper)vAprtType.elementAt(i);
      aprtType.addMenuElement(eAprtType.getKey(),eAprtType.getName());
      aprtType2.addMenuElement(eAprtType.getKey(),eAprtType.getName());
      aprtType3.addMenuElement(eAprtType.getKey(),eAprtType.getName());
    }

    Form form = new Form();
    form.add("Húsnæði sem sótt er um");
    form.add(Text.getBreak());
    form.add(Text.getBreak());
    form.add("1. ");
    form.add(aprtType);
    form.add(Text.getBreak());
    form.add("2. ");
    form.add(aprtType2);
    form.add(Text.getBreak());
    form.add("3. ");
    form.add(aprtType3);
    form.add(new SubmitButton("ok","áfram"));
    form.add(new HiddenInput("status",Integer.toString(statusAppliedFor_)));
    add(form);
  }

  protected void doCampusInformation(ModuleInfo modinfo) {
    List residences = CampusApplicationFinder.listOfResidences();
    List occupations = CampusApplicationFinder.listOfSpouseOccupations();
    DropdownMenu resSelect = new DropdownMenu(residences,"currentResidence");
    DropdownMenu occSelect = new DropdownMenu(occupations,"spouseOccupation");

    Form form = new Form();
    Table t = new Table(3,24);
    form.add(t);
    t.mergeCells(1,1,3,1);
    t.add("Aðrar upplýsingar um umsækjanda",1,1);

    t.add("Nám hafið við HÍ (mán./ár)",1,3);
    t.add(new TextInput("studyBeginMo"),2,3);
    t.add(new TextInput("studyBeginYr"),3,3);
    t.add("Áætluð námslok (mán./ár)",1,4);
    t.add(new TextInput("studyEndMo"),2,4);
    t.add(new TextInput("studyEndYr"),3,4);
    t.add("Deild",1,5);
    t.add(new TextInput("faculty"),2,5);
    t.add("Námsbraut",1,6);
    t.add(new TextInput("studyTrack"),2,6);
    t.add("Núverandi húsnæði",1,7);
    t.add(resSelect,2,7);
    t.add(new TextInput("resInfo"),3,7);
    t.add("Nafn umsækjanda/maka",1,8);
    t.add(new TextInput("spouseName"),2,8);
    t.add("Kennitala",1,9);
    t.add(new TextInput("spouseSSN"),2,9);
    t.add("Skóli",1,10);
    t.add(new TextInput("spouseSchool"),2,10);
    t.add("Námsbraut",1,11);
    t.add(new TextInput("spouseStudyTrack"),2,11);
    t.add("Nám hafið (mán./ár)",1,12);
    t.add(new TextInput("spouseStudyBeginMo"),2,12);
    t.add(new TextInput("spouseStudyBeginYr"),3,12);
    t.add("Áætluð námslok (mán./ár)",1,13);
    t.add(new TextInput("spouseStudyEndMo"),2,13);
    t.add(new TextInput("spouseStudyEndYr"),3,13);
    t.add("Maki er",1,14);
    t.add(occSelect,2,14);
    t.add("Nöfn og fæðingardagur barna sem búa hjá umsækjanda",1,15);
    t.add(new TextArea("children"),2,15);
    t.add("Tekjur, styrkir og námslán umsækjanda 1.1 - 1.6 í ár",1,16);
    t.add(new TextInput("income"),2,16);
    t.add("Tekjur, styrkir og námslán umsækjanda/maka 1.1 - 1.6 í ár",1,17);
    t.add(new TextInput("spouseIncome"),2,17);
    t.add("Húsnæði óskast frá og með",1,18);
    DateInput d = new DateInput("wantHousingFrom");
    d.setToCurrentDate();
    t.add(d,2,18);
    t.add("Óska eftir að vera á biðlista ef ég fæ ekki úthlutað húsnæði",1,19);
    t.add(new CheckBox("waitingList"),2,19);
    t.add("Óska eftir að leigja húsgögn ef mögulegt er",1,20);
    t.add(new CheckBox("furniture"),2,20);
    t.add("Ef ekki næst í mig í síma á dvalarstað má ná í mig eða skilja eftir skilaboð í sima:",1,21);
    t.add(new TextInput("contact"),2,21);
    t.add("Tölvupóstur",1,22);
    t.add(new TextInput("email"),2,22);
    t.add("Aðrar upplýsingar",1,23);
    t.add(new TextArea("info"),2,23);
    t.add(new SubmitButton("ok","áfram"),3,24);
    form.add(new HiddenInput("status",Integer.toString(statusCampusInfo_)));
    add(form);
  }

  protected void saveCampusInformation(ModuleInfo modinfo) {
    int studyBeginMon = 0;
    int studyBeginYr = 0;
    int studyEndMo = 0;
    int studyEndYr = 0;
    String faculty = modinfo.getParameter("faculty");
    String studyTrack = modinfo.getParameter("studyTrack");
    int currentResidence = 0;
    int spouseOccupation = 0;
    String resInfo = modinfo.getParameter("resInfo");
    String spouseName = modinfo.getParameter("spouseName");
    String spouseSSN = modinfo.getParameter("spouseSSN");
    String spouseSchool = modinfo.getParameter("spouseSchool");
    String spouseStudyTrack = modinfo.getParameter("spouseStudyTrack");
    int spouseStudyBeginMo = 0;
    int spouseStudyBeginYr = 0;
    int spouseStudyEndMo = 0;
    int spouseStudyEndYr = 0;
    String children = modinfo.getParameter("children");
    int income = 0;
    int spouseIncome = 0;
    String wantHousingFrom = modinfo.getParameter("wantHousingFrom");
    String waitingList = modinfo.getParameter("waitingList");
    String furniture = modinfo.getParameter("furniture");
    String contact = modinfo.getParameter("contact");
    String email = modinfo.getParameter("email");
    String info = modinfo.getParameter("info");

    Application application = new Application();

    try {
      currentResidence = Integer.parseInt(modinfo.getParameter("currentResidence"));
    }
    catch(java.lang.NumberFormatException e) {}

    try {
      spouseOccupation = Integer.parseInt(modinfo.getParameter("spouseOccupation"));
    }
    catch(java.lang.NumberFormatException e) {}

    try {
      studyBeginMon = Integer.parseInt(modinfo.getParameter("studyBeginMon"));
    }
    catch(java.lang.NumberFormatException e) {}

    try {
      studyBeginYr = Integer.parseInt(modinfo.getParameter("studyBeginYr"));
    }
    catch(java.lang.NumberFormatException e) {}

    try {
      studyEndMo = Integer.parseInt(modinfo.getParameter("studyEndMo"));
    }
    catch(java.lang.NumberFormatException e) {}

    try {
      studyEndYr = Integer.parseInt(modinfo.getParameter("studyEndYr"));
    }
    catch(java.lang.NumberFormatException e) {}

    try {
      spouseIncome = Integer.parseInt(modinfo.getParameter("spouseIncome"));
    }
    catch(java.lang.NumberFormatException e) {}

    try {
      spouseStudyBeginMo = Integer.parseInt(modinfo.getParameter("spouseStudyBeginMo"));
    }
    catch(java.lang.NumberFormatException e) {}

    try {
      spouseStudyBeginYr = Integer.parseInt(modinfo.getParameter("spouseStudyBeginYr"));
    }
    catch(java.lang.NumberFormatException e) {}

    try {
      spouseStudyEndMo = Integer.parseInt(modinfo.getParameter("spouseStudyEndMo"));
    }
    catch(java.lang.NumberFormatException e) {}

    try {
      spouseStudyEndYr = Integer.parseInt(modinfo.getParameter("spouseStudyEndYr"));
    }
    catch(java.lang.NumberFormatException e) {}

    try {
      income = Integer.parseInt(modinfo.getParameter("income"));
    }
    catch(java.lang.NumberFormatException e) {}

    application.setCurrentResidenceId(currentResidence);
    application.setSpouseOccupationId(spouseOccupation);
    application.setStudyBeginMonth(studyBeginMon);
    application.setStudyBeginYear(studyBeginYr);
    application.setStudyEndMonth(studyEndMo);
    application.setStudyEndYear(studyEndYr);
    application.setFaculty(faculty);
    application.setStudyTrack(studyTrack);
    application.setSpouseName(spouseName);
    application.setSpouseIncome(spouseIncome);
    application.setSpouseSSN(spouseSSN);
    application.setSpouseSchool(spouseSchool);
    application.setSpouseStudyTrack(spouseStudyTrack);
    application.setSpouseStudyBeginMonth(spouseStudyBeginMo);
    application.setSpouseStudyBeginYear(spouseStudyBeginYr);
    application.setSpouseStudyEndMonth(spouseStudyEndMo);
    application.setSpouseStudyEndYear(spouseStudyEndYr);
    application.setChildren(children);
    application.setIncome(income);
    idegaTimestamp t = new idegaTimestamp(wantHousingFrom);
    application.setHousingFrom(t.getSQLDate());
    if (waitingList == null)
      application.setOnWaitinglist(false);
    else
      application.setOnWaitinglist(true);
    if (furniture == null)
      application.setWantFurniture(false);
    else
      application.setWantFurniture(true);
    application.setContactPhone(contact);
    application.setOtherInfo(info);
    application.setEmail(email);

    modinfo.setSessionAttribute("campusapplication",application);
  }

  protected void saveAppliedFor(ModuleInfo modinfo) {
    String key1 = (String)modinfo.getParameter("aprtType");
    String key2 = (String)modinfo.getParameter("aprtType2");
    String key3 = (String)modinfo.getParameter("aprtType3");



  }

  protected boolean saveDataToDB(ModuleInfo modinfo) {
    Applicant applicant = (Applicant)modinfo.getSessionAttribute("applicant");
    com.idega.block.application.data.Application application = (com.idega.block.application.data.Application)modinfo.getSessionAttribute("application");
    Application campusApplication = (Application)modinfo.getSessionAttribute("campusapplication");
    Applied applied = (Applied)modinfo.getSessionAttribute("applied");

    try {
      applicant.insert();

      application.setApplicantId(applicant.getID());
      application.insert();

      campusApplication.setAppApplicationId(application.getID());
      campusApplication.insert();

//      applied.setApplicationId(campusApplication.getID());
//      applied.insert();
    }
    catch(SQLException e) {
      System.err.println(e.toString());
      return(false);
    }
    finally {
      modinfo.removeSessionAttribute("applicant");
      modinfo.removeSessionAttribute("application");
      modinfo.removeSessionAttribute("campusapplication");
      modinfo.removeSessionAttribute("applied");
      modinfo.removeSessionAttribute("aprtCat");
    }

    return(true);
  }
}
