/*
 * $Id: CampusApplicationForm.java,v 1.4 2001/07/10 17:03:43 palli Exp $
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
import com.idega.idegaweb.IWResourceBundle;
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
    IWResourceBundle iwrb = getResourceBundle(modinfo);
    List subjects = ApplicationFinder.listOfNonExpiredSubjects();
    List categories = BuildingFinder.listOfApartmentCategory();
    Text textTemplate = new Text();

    Form form = new Form();
    Table t = new Table(2,4);
    t.setWidth(1,"50%");
    t.setWidth(2,"50%");

    Text heading = (Text)textTemplate.clone();
    heading.setStyle("headlinetext");
    heading.setText(iwrb.getLocalizedString("applicationSubject","Veldu tegund umsóknar"));
    Text text1 = (Text)textTemplate.clone();
    text1.setStyle("bodytext");
    text1.setText(iwrb.getLocalizedString("applicationSubject","Umsókn um"));
    text1.setBold();
    Text text2 = (Text)textTemplate.clone();
    text2.setStyle("bodytext");
    text2.setText(iwrb.getLocalizedString("apartmentType","Tegund íbúðar"));
    text2.setBold();
    Text required = (Text)textTemplate.clone();
    required.setText(" * ");
    required.setBold();
    required.setStyle("required");
    Text info = (Text)textTemplate.clone();
    info.setText(iwrb.getLocalizedString("mustFillOut","* Stjörnumerkt svæði verður að fylla út"));
    info.setStyle("subtext");

    DropdownMenu subject = new DropdownMenu(subjects,"subject");
    subject.setStyle("formstyle");
    DropdownMenu aprtCat = new DropdownMenu(categories,"aprtCat");
    aprtCat.setStyle("formstyle");
    SubmitButton ok = new SubmitButton("ok",iwrb.getLocalizedString("ok","áfram"));
    ok.setStyle("idega");

    form.add(heading);
    form.add(Text.getBreak());
    form.add(Text.getBreak());
    form.add(t);

    t.add(text1,1,1);
    t.add(required,1,1);
    t.add(subject,2,1);
    t.add(text2,1,2);
    t.add(required,1,2);
    t.add(aprtCat,2,2);
    t.add(ok,2,4);
    form.add(Text.getBreak());
    form.add(Text.getBreak());
    form.add(Text.getBreak());
    form.add(info);
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
    IWResourceBundle iwrb = getResourceBundle(modinfo);
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
    aprtType.setStyle("formstyle");
    DropdownMenu aprtType2 = new DropdownMenu("aprtType2");
    aprtType2.setStyle("formstyle");
    DropdownMenu aprtType3 = new DropdownMenu("aprtType3");
    aprtType3.setStyle("formstyle");
    aprtType2.addMenuElement("-1","");
    aprtType3.addMenuElement("-1","");

    for (int i = 0; i < vAprtType.size(); i++) {
      ApartmentTypeComplexHelper eAprtType = (ApartmentTypeComplexHelper)vAprtType.elementAt(i);
      aprtType.addMenuElement(eAprtType.getKey(),eAprtType.getName());
      aprtType2.addMenuElement(eAprtType.getKey(),eAprtType.getName());
      aprtType3.addMenuElement(eAprtType.getKey(),eAprtType.getName());
    }

    Text textTemplate = new Text();

    Form form = new Form();
    Table t = new Table(2,5);
    t.setWidth(1,"50%");
    t.setWidth(2,"50%");

    Text heading = (Text)textTemplate.clone();
    heading.setStyle("headlinetext");
    heading.setText(iwrb.getLocalizedString("applied","Húsnæði sem sótt er um"));
    Text text1 = (Text)textTemplate.clone();
    text1.setStyle("bodytext");
    text1.setText(iwrb.getLocalizedString("firstChoice","Fyrsta val"));
    text1.setBold();
    Text text2 = (Text)textTemplate.clone();
    text2.setStyle("bodytext");
    text2.setText(iwrb.getLocalizedString("secondChoice","Annað val"));
    Text text3 = (Text)textTemplate.clone();
    text3.setStyle("bodytext");
    text3.setText(iwrb.getLocalizedString("thirdChoice","Þriðja val"));
    Text required = (Text)textTemplate.clone();
    required.setText(" * ");
    required.setBold();
    required.setStyle("required");
    Text info = (Text)textTemplate.clone();
    info.setText(iwrb.getLocalizedString("mustFillOut","* Stjörnumerkt svæði verður að fylla út"));
    info.setStyle("subtext");

    SubmitButton ok = new SubmitButton("ok",iwrb.getLocalizedString("ok","áfram"));
    ok.setStyle("idega");

    form.add(heading);
    form.add(Text.getBreak());
    form.add(Text.getBreak());
    form.add(t);

    t.add(text1,1,1);
    t.add(required,1,1);
    t.add(aprtType,2,1);
    t.add(text2,1,2);
    t.add(aprtType2,2,2);
    t.add(text3,1,3);
    t.add(aprtType3,2,3);
    t.add(ok,2,5);
    form.add(Text.getBreak());
    form.add(Text.getBreak());
    form.add(Text.getBreak());
    form.add(info);
    form.add(new HiddenInput("status",Integer.toString(statusAppliedFor_)));
    add(form);
  }

  protected void doCampusInformation(ModuleInfo modinfo) {
    IWResourceBundle iwrb = getResourceBundle(modinfo);
    List residences = CampusApplicationFinder.listOfResidences();
    List occupations = CampusApplicationFinder.listOfSpouseOccupations();
    DropdownMenu resSelect = new DropdownMenu(residences,"currentResidence");
    resSelect.setStyle("formstyle");
    DropdownMenu occSelect = new DropdownMenu(occupations,"spouseOccupation");
    occSelect.setStyle("formstyle");
    DropdownMenu studyBeginMo = new DropdownMenu("studyBeginMo");
    studyBeginMo.setStyle("formstyle");
    studyBeginMo.addMenuElement(1,iwrb.getLocalizedString("january","jan"));
    studyBeginMo.addMenuElement(2,iwrb.getLocalizedString("february","feb"));
    studyBeginMo.addMenuElement(3,iwrb.getLocalizedString("march","mar"));
    studyBeginMo.addMenuElement(4,iwrb.getLocalizedString("april","apr"));
    studyBeginMo.addMenuElement(5,iwrb.getLocalizedString("may","maí"));
    studyBeginMo.addMenuElement(6,iwrb.getLocalizedString("june","jún"));
    studyBeginMo.addMenuElement(7,iwrb.getLocalizedString("july","júl"));
    studyBeginMo.addMenuElement(8,iwrb.getLocalizedString("august","ágú"));
    studyBeginMo.addMenuElement(9,iwrb.getLocalizedString("september","sep"));
    studyBeginMo.addMenuElement(10,iwrb.getLocalizedString("october","okt"));
    studyBeginMo.addMenuElement(11,iwrb.getLocalizedString("november","nóv"));
    studyBeginMo.addMenuElement(12,iwrb.getLocalizedString("december","des"));
    DropdownMenu studyEndMo = (DropdownMenu)studyBeginMo.clone();
    studyEndMo.setName("studyEndMo");
    DropdownMenu spouseStudyBeginMo = (DropdownMenu)studyBeginMo.clone();
    spouseStudyBeginMo.setName("spouseStudyBeginMo");
    DropdownMenu spouseStudyEndMo = (DropdownMenu)studyBeginMo.clone();
    spouseStudyEndMo.setName("spouseStudyEndMo");

    int currentYear = idegaTimestamp.RightNow().getYear();

    DropdownMenu studyBeginYr = new DropdownMenu("studyBeginYr");
    studyBeginYr.setStyle("formstyle");
    for (int i = 9; i >= 0; i--)
      studyBeginYr.addMenuElement(currentYear - i,Integer.toString(currentYear-i));
    studyBeginYr.setSelectedElement(Integer.toString(currentYear));
    DropdownMenu spouseStudyBeginYr = (DropdownMenu)studyBeginYr.clone();
    spouseStudyBeginYr.setName("spouseStudyBeginYr");
    DropdownMenu studyEndYr = new DropdownMenu("studyEndYr");
    studyEndYr.setStyle("formstyle");
    for (int i = 0; i < 10; i++)
      studyEndYr.addMenuElement(currentYear + i,Integer.toString(currentYear+i));
    studyEndYr.setSelectedElement(Integer.toString(currentYear));
    DropdownMenu spouseStudyEndYr = (DropdownMenu)studyEndYr.clone();
    spouseStudyEndYr.setName("spouseStudyEndYr");


    SubmitButton ok = new SubmitButton("ok",iwrb.getLocalizedString("ok","áfram"));
    ok.setStyle("idega");

    Text textTemplate = new Text();

    Text heading = (Text)textTemplate.clone();
    heading.setStyle("headlinetext");
    heading.setText(iwrb.getLocalizedString("otherInfo","Aðrar upplýsingar um umsækjanda"));
    Text required = (Text)textTemplate.clone();
    required.setText(" * ");
    required.setBold();
    required.setStyle("required");
    Text info = (Text)textTemplate.clone();
    info.setText(iwrb.getLocalizedString("mustFillOut","* Stjörnumerkt svæði verður að fylla út"));
    info.setStyle("subtext");
    Text text1 = (Text)textTemplate.clone();
    text1.setText(iwrb.getLocalizedString("studyBegin","Nám hafið við HÍ (mán./ár)"));
    text1.setStyle("bodytext");
    text1.setBold();
    Text text2 = (Text)textTemplate.clone();
    text2.setText(iwrb.getLocalizedString("studyEnd","Áætluð námslok (mán./ár)"));
    text2.setStyle("bodytext");
    text2.setBold();
    Text text3 = (Text)textTemplate.clone();
    text3.setText(iwrb.getLocalizedString("faculty","Deild"));
    text3.setStyle("bodytext");
    text3.setBold();
    Text text4 = (Text)textTemplate.clone();
    text4.setText(iwrb.getLocalizedString("studyTrack","Námsbraut"));
    text4.setStyle("bodytext");
    text4.setBold();
    Text text5 = (Text)textTemplate.clone();
    text5.setText(iwrb.getLocalizedString("currentRes","Núverandi húsnæði"));
    text5.setStyle("bodytext");
    text5.setBold();
    Text text6 = (Text)textTemplate.clone();
    text6.setText(iwrb.getLocalizedString("spouseName","Nafn umsækjanda/maka"));
    text6.setStyle("bodytext");
    Text text7 = (Text)textTemplate.clone();
    text7.setText(iwrb.getLocalizedString("spouseSSN","Kennitala"));
    text7.setStyle("bodytext");
    Text text8 = (Text)textTemplate.clone();
    text8.setText(iwrb.getLocalizedString("spouseSchool","Skóli"));
    text8.setStyle("bodytext");
    Text text9 = (Text)textTemplate.clone();
    text9.setText(iwrb.getLocalizedString("spouseStudyTrack","Námsbraut"));
    text9.setStyle("bodytext");
    Text text10 = (Text)textTemplate.clone();
    text10.setText(iwrb.getLocalizedString("spouseStudyBegin","Nám hafið (mán./ár)"));
    text10.setStyle("bodytext");
    Text text11 = (Text)textTemplate.clone();
    text11.setText(iwrb.getLocalizedString("spouseStudyEnd","Áætluð námslok (mán./ár)"));
    text11.setStyle("bodytext");
    Text text12 = (Text)textTemplate.clone();
    text12.setText(iwrb.getLocalizedString("spouseOccupation","Maki er"));
    text12.setStyle("bodytext");
    Text text13 = (Text)textTemplate.clone();
    text13.setText(iwrb.getLocalizedString("children","Nöfn og fæðingardagur barna sem búa hjá umsækjanda"));
    text13.setStyle("bodytext");
    Text text14 = (Text)textTemplate.clone();
    text14.setText(iwrb.getLocalizedString("income","Tekjur, styrkir og námslán umsækjanda 1.1 - 1.6 í ár"));
    text14.setStyle("bodytext");
    text14.setBold();
    Text text15 = (Text)textTemplate.clone();
    text15.setText(iwrb.getLocalizedString("spouseIncome","Tekjur, styrkir og námslán umsækjanda/maka 1.1 - 1.6 í ár"));
    text15.setStyle("bodytext");
    Text text16 = (Text)textTemplate.clone();
    text16.setText(iwrb.getLocalizedString("wantHousingFrom","Húsnæði óskast frá og með"));
    text16.setStyle("bodytext");
    text16.setBold();
    Text text17 = (Text)textTemplate.clone();
    text17.setText(iwrb.getLocalizedString("waitingList","Óska eftir að vera á biðlista ef ég fæ ekki úthlutað húsnæði"));
    text17.setStyle("bodytext");
    Text text18 = (Text)textTemplate.clone();
    text18.setText(iwrb.getLocalizedString("furniture","Óska eftir að leigja húsgögn ef mögulegt er"));
    text18.setStyle("bodytext");
    Text text19 = (Text)textTemplate.clone();
    text19.setText(iwrb.getLocalizedString("contact","Ef ekki næst í mig í síma á dvalarstað má ná í mig eða skilja eftir skilaboð í sima"));
    text19.setStyle("bodytext");
    Text text20 = (Text)textTemplate.clone();
    text20.setText(iwrb.getLocalizedString("email","Tölvupóstur"));
    text20.setStyle("bodytext");
    text20.setBold();
    Text text21 = (Text)textTemplate.clone();
    text21.setText(iwrb.getLocalizedString("info","Aðrar upplýsingar"));
    text21.setStyle("bodytext");

    TextInput textInputTemplate = new TextInput();
    TextInput input1 = (TextInput)textInputTemplate.clone();
    input1.setName("faculty");
    input1.setStyle("formstyle");
    TextInput input2 = (TextInput)textInputTemplate.clone();
    input2.setName("studyTrack");
    input2.setStyle("formstyle");
    TextInput input3 = (TextInput)textInputTemplate.clone();
    input3.setName("resInfo");
    input3.setStyle("formstyle");
    input3.setLength(10);
    TextInput input4 = (TextInput)textInputTemplate.clone();
    input4.setName("spouseName");
    input4.setStyle("formstyle");
    TextInput input5 = (TextInput)textInputTemplate.clone();
    input5.setName("spouseSSN");
    input5.setStyle("formstyle");
    input5.setLength(12);
    TextInput input6 = (TextInput)textInputTemplate.clone();
    input6.setName("spouseSchool");
    input6.setStyle("formstyle");
    TextInput input7 = (TextInput)textInputTemplate.clone();
    input7.setName("spouseStudyTrack");
    input7.setStyle("formstyle");
    TextInput input8 = (TextInput)textInputTemplate.clone();
    input8.setName("income");
    input8.setStyle("formstyle");
    input8.setLength(10);
    TextInput input9 = (TextInput)textInputTemplate.clone();
    input9.setName("spouseIncome");
    input9.setStyle("formstyle");
    input9.setLength(10);
    TextInput input10 = (TextInput)textInputTemplate.clone();
    input10.setName("contact");
    input10.setStyle("formstyle");
    input10.setLength(10);
    TextInput input11 = (TextInput)textInputTemplate.clone();
    input11.setName("email");
    input11.setStyle("formstyle");

    TextArea input12 = new TextArea("children");
    input12.setStyle("formstyle");
    input12.setHeight(4);
    input12.setWidth(40);

    TextArea input13 = (TextArea)input12.clone();
    input13.setName("info");
    input13.setStyle("formstyle");

    CheckBox input14 = new CheckBox("waitingList");
    input14.setStyle("formstyle2");

    CheckBox input15 = (CheckBox)input14.clone();
    input15.setName("furniture");
    input15.setStyle("formstyle2");

    DateInput input16 = new DateInput("wantHousingFrom");
    input16.setToCurrentDate();
    input16.setStyle("formstyle");

    Form form = new Form();
    Table t = new Table(2,23);
    t.setWidth(1,"50%");
    t.setWidth(2,"50%");
    form.add(heading);
    form.add(Text.getBreak());
    form.add(Text.getBreak());
    form.add(t);
    t.add(text1,1,1);
    t.add(required,1,1);
    t.add(studyBeginMo,2,1);
    t.add("/",2,1);
    t.add(studyBeginYr,2,1);
    t.add(text2,1,2);
    t.add(required,1,2);
    t.add(studyEndMo,2,2);
    t.add("/",2,2);
    t.add(studyEndYr,2,2);
    t.add(text3,1,3);
    t.add(required,1,3);
    t.add(input1,2,3);
    t.add(text4,1,4);
    t.add(required,1,4);
    t.add(input2,2,4);
    t.add(text5,1,5);
    t.add(required,1,5);
    t.add(resSelect,2,5);
    t.add(input3,2,5);
    t.add(text6,1,6);
    t.add(input4,2,6);
    t.add(text7,1,7);
    t.add(input5,2,7);
    t.add(text8,1,8);
    t.add(input6,2,8);
    t.add(text9,1,9);
    t.add(input7,2,9);
    t.add(text10,1,10);
    t.add(spouseStudyBeginMo,2,10);
    t.add("/",2,10);
    t.add(spouseStudyBeginYr,2,10);
    t.add(text11,1,11);
    t.add(spouseStudyEndMo,2,11);
    t.add("/",2,11);
    t.add(spouseStudyEndYr,2,11);
    t.add(text12,1,12);
    t.add(occSelect,2,12);
    t.add(text13,1,13);
    t.add(input12,2,13);
    t.add(text14,1,14);
    t.add(required,1,14);
    t.add(input8,2,14);
    t.add(text15,1,15);
    t.add(input9,2,15);
    t.add(text16,1,16);
    t.add(required,1,16);
    t.add(input16,2,16);
    t.add(text17,1,17);
    t.add(input14,2,17);
    t.add(text18,1,18);
    t.add(input15,2,18);
    t.add(text19,1,19);
    t.add(input10,2,19);
    t.add(text20,1,20);
    t.add(required,1,20);
    t.add(input11,2,20);
    t.add(text21,1,21);
    t.add(input13,2,21);
    t.add(ok,2,23);
    form.add(Text.getBreak());
    form.add(Text.getBreak());
    form.add(Text.getBreak());
    form.add(info);
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
      studyBeginMon = Integer.parseInt(modinfo.getParameter("studyBeginMo"));
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

    Applied applied1 = null;
    Applied applied2 = null;
    Applied applied3 = null;

    applied1 = new Applied();
    int type = ApartmentTypeComplexHelper.getPartKey(key1,1);
    int complex = ApartmentTypeComplexHelper.getPartKey(key1,2);
    applied1.setApartmentTypeId(type);
    applied1.setComplexId(complex);
    applied1.setOrder(1);

    if ((key2 != null) && (!key2.equalsIgnoreCase("-1"))) {
      applied2 = new Applied();
      type = ApartmentTypeComplexHelper.getPartKey(key2,1);
      complex = ApartmentTypeComplexHelper.getPartKey(key2,2);
      applied2.setApartmentTypeId(type);
      applied2.setComplexId(complex);
      applied2.setOrder(2);
    }

    if ((key3 != null) && (!key3.equalsIgnoreCase("-1"))) {
      applied3 = new Applied();
      type = ApartmentTypeComplexHelper.getPartKey(key3,1);
      complex = ApartmentTypeComplexHelper.getPartKey(key3,2);
      applied3.setApartmentTypeId(type);
      applied3.setComplexId(complex);
      applied3.setOrder(3);
    }

    modinfo.setSessionAttribute("applied1",applied1);
    if (applied2 != null)
      modinfo.setSessionAttribute("applied2",applied2);
    if (applied3 != null)
      modinfo.setSessionAttribute("applied3",applied3);
  }

  protected boolean saveDataToDB(ModuleInfo modinfo) {
    Applicant applicant = (Applicant)modinfo.getSessionAttribute("applicant");
    com.idega.block.application.data.Application application = (com.idega.block.application.data.Application)modinfo.getSessionAttribute("application");
    Application campusApplication = (Application)modinfo.getSessionAttribute("campusapplication");
    Applied applied1 = (Applied)modinfo.getSessionAttribute("applied1");
    Applied applied2 = (Applied)modinfo.getSessionAttribute("applied2");
    Applied applied3 = (Applied)modinfo.getSessionAttribute("applied3");

    try {
      applicant.insert();

      application.setApplicantId(applicant.getID());
      application.insert();

      campusApplication.setAppApplicationId(application.getID());
      campusApplication.insert();

      applied1.setApplicationId(campusApplication.getID());
      applied1.insert();

      if (applied2 != null) {
        applied2.setApplicationId(campusApplication.getID());
        applied2.insert();
      }

      if (applied3 != null) {
        applied3.setApplicationId(campusApplication.getID());
        applied3.insert();
      }
    }
    catch(SQLException e) {
      System.err.println(e.toString());
      return(false);
    }
    finally {
      modinfo.removeSessionAttribute("applicant");
      modinfo.removeSessionAttribute("application");
      modinfo.removeSessionAttribute("campusapplication");
      modinfo.removeSessionAttribute("applied1");
      modinfo.removeSessionAttribute("applied2");
      modinfo.removeSessionAttribute("applied3");
      modinfo.removeSessionAttribute("aprtCat");
    }

    return(true);
  }
}
