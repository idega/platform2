/*
 * $Id: CampusApplicationForm.java,v 1.4 2002/02/21 00:20:57 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.block.application.presentation;

import com.idega.block.application.presentation.ApplicationForm;
import com.idega.block.application.business.ApplicationFinder;
import com.idega.block.application.business.ReferenceNumberHandler;
import com.idega.block.building.business.BuildingFinder;
import com.idega.block.building.business.ApartmentTypeComplexHelper;
import com.idega.block.building.data.ApartmentType;
import com.idega.block.building.presentation.ApartmentTypeViewer;
import com.idega.presentation.Image;
import com.idega.presentation.IWContext;
import com.idega.presentation.Script;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.Window;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.text.Text;
import com.idega.presentation.Page;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.util.idegaTimestamp;
import com.idega.util.SendMail;
import com.idega.util.CypherText;
import com.idega.idegaweb.IWBundle;
import is.idega.idegaweb.campus.block.application.business.CampusApplicationFinder;
import is.idega.idegaweb.campus.block.application.business.CampusApplicationFormHelper;
import is.idega.idegaweb.campus.presentation.Edit;
import java.util.List;
import java.util.Vector;
import java.util.Iterator;
import java.sql.SQLException;

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class CampusApplicationForm extends ApplicationForm {
  private final int _statusEnteringPage = 0;
  private final int _statusSubject = 1;
  private final int _statusGeneralInfo = 2;
  private final int _statusCampusInfo = 3;
  private final int _statusAppliedFor = 4;
  private final int _statusSelectingApartmentTypes = 99;
  private final int _numberOfStages = 3;

  private int _apartment1 = -1;
  private int _apartment2 = -1;
  private int _apartment3 = -1;

	private IWBundle _iwb;

  private static final String IW_RESOURCE_BUNDLE = "is.idega.idegaweb.campus";

  /**
   *
   */
  public CampusApplicationForm() {
  }

  /*
   *
   */
  protected void control(IWContext iwc) {
		_iwb = getBundle(iwc);
    String statusString = iwc.getParameter("status");
    int status = 0;

    if (statusString == null) {
      status = _statusEnteringPage;
    }
    else {
      try {
        status = Integer.parseInt(statusString);
      }
      catch (NumberFormatException e) {
      }
    }

    if (status == _statusEnteringPage) {
      List subjects = ApplicationFinder.listOfNonExpiredSubjects();
      if (subjects == null) {
        doSubjectError();
        return;
      }

      addStage(1);
      doGeneralInformation(iwc);
    }
    else if (status == _statusGeneralInfo) {
      addStage(2);
      CampusApplicationFormHelper.saveApplicantInformation(iwc);
      doCampusInformation(iwc);
    }
    else if (status == _statusCampusInfo) {
      addStage(3);
      CampusApplicationFormHelper.saveSubject(iwc);
      CampusApplicationFormHelper.saveCampusInformation(iwc);
      doSelectAppliedFor(iwc);
    }
    else if (status == _statusAppliedFor) {
      CampusApplicationFormHelper.saveAppliedFor(iwc);
      String cypher = CampusApplicationFormHelper.saveDataToDB(iwc);
      if ( cypher != null)
        doDone(cypher);
      else
        doError();
    }
    else if (status == _statusSelectingApartmentTypes) {
      addStage(3);
      checkAparmentTypesSelected(iwc);
      doSelectAppliedFor(iwc);
    }
  }

  /*
  /*
   *
   */
  protected void doSelectAppliedFor(IWContext iwc) {
    int id;
    String aprtCat = (String)iwc.getSessionAttribute("aprtCat");
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
    aprtType.addDisabledMenuElement("-1","");
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
    Table t = new Table(3,5);
    t.setWidth(1,"250");
    t.setCellpadding(5);

    Text heading = (Text)textTemplate.clone();
    heading.setStyle("headlinetext");
    heading.setText(_iwrb.getLocalizedString("applied","Húsnæði sem sótt er um"));
    Text text1 = (Text)textTemplate.clone();
    text1.setStyle("bodytext");
    text1.setText(_iwrb.getLocalizedString("firstChoice","Fyrsta val"));
    text1.setBold();
    Text text2 = (Text)textTemplate.clone();
    text2.setStyle("bodytext");
    text2.setText(_iwrb.getLocalizedString("secondChoice","Annað val"));
    Text text3 = (Text)textTemplate.clone();
    text3.setStyle("bodytext");
    text3.setText(_iwrb.getLocalizedString("thirdChoice","Þriðja val"));
    Text required = (Text)textTemplate.clone();
    required.setText(" * ");
    required.setBold();
    required.setStyle("required");
    Text info = (Text)textTemplate.clone();
    info.setText(_iwrb.getLocalizedString("mustFillOut","* Stjörnumerkt svæði verður að fylla út"));
    info.setStyle("subtext");

    Image back = _iwrb.getImage("back.gif");
    back.setAttribute("onClick","history.go(-1)");
    SubmitButton ok = new SubmitButton(_iwrb.getImage("next.gif",_iwrb.getLocalizedString("ok","áfram")),"status",Integer.toString(_statusAppliedFor));

    form.add(heading);
    form.add(Text.getBreak());
    form.add(Text.getBreak());
    form.add(t);

    t.add(text1,1,1);
    t.add(required,1,1);
    t.add(aprtType,2,1);

    Window window = new Window("Apartment Viewer",ApartmentTypeViewer.class,Page.class);
    window.setWidth(400);
    window.setHeight(550);
    window.setScrollbar(false);

    Image apartmentImage = _iwb.getImage("list.gif",_iwrb.getLocalizedString("get_apartment","Click for information about apartment"));
    apartmentImage.setAlignment("absmiddle");
    apartmentImage.setHorizontalSpacing(4);
    Link apartmentLink = new Link(apartmentImage);
    apartmentLink.setWindowToOpen(com.idega.block.building.presentation.ApartmentTypeWindow.class);
    Text apartmentText = new Text(_iwrb.getLocalizedString("see_apartment","view"));
    apartmentText.setFontStyle("font-family:arial; font-size:9px; color:#000000");
    Link apartmentLink2 = new Link(apartmentText);
    apartmentLink2.setWindowToOpen(com.idega.block.building.presentation.ApartmentTypeWindow.class);

    if (_apartment1 > -1) {
      try {
        Link link1 = (Link) apartmentLink.clone();
        link1.addParameter(ApartmentTypeViewer.PARAMETER_STRING,_apartment1);
        Link link12 = (Link) apartmentLink2.clone();
        link12.addParameter(ApartmentTypeViewer.PARAMETER_STRING,_apartment1);
        t.add(link1,3,1);
        t.add(link12,3,1);
      }
      catch(Exception e) {
        e.printStackTrace();
      }
    }

    t.add(text2,1,2);
    t.add(aprtType2,2,2);
    if (_apartment2 > -1) {
      try {
        Link link2 = (Link) apartmentLink.clone();
        link2.addParameter(ApartmentTypeViewer.PARAMETER_STRING,_apartment2);
        Link link22 = (Link) apartmentLink2.clone();
        link22.addParameter(ApartmentTypeViewer.PARAMETER_STRING,_apartment2);
        t.add(link2,3,2);
        t.add(link22,3,2);
      }
      catch(Exception e) {
        e.printStackTrace();
      }
    }

    t.add(text3,1,3);
    t.add(aprtType3,2,3);
    if (_apartment3 > -1) {
      try {
        Link link3 = (Link) apartmentLink.clone();
        link3.addParameter(ApartmentTypeViewer.PARAMETER_STRING,_apartment3);
        Link link32 = (Link) apartmentLink2.clone();
        link32.addParameter(ApartmentTypeViewer.PARAMETER_STRING,_apartment3);
        t.add(link3,3,3);
        t.add(link32,3,3);
      }
      catch(Exception e) {
        e.printStackTrace();
      }
    }

    t.add(back,1,5);
    t.add("&nbsp;&nbsp;&nbsp;",1,5);
    t.add(ok,1,5);
    form.add(Text.getBreak());
    form.add(Text.getBreak());
    form.add(Text.getBreak());
    form.add(info);
    aprtType.setOnChange("this.form.status.value='" + _statusSelectingApartmentTypes + "'");
    aprtType2.setOnChange("this.form.status.value='" + _statusSelectingApartmentTypes + "'");
    aprtType3.setOnChange("this.form.status.value='" + _statusSelectingApartmentTypes + "'");
    aprtType.setToSubmit();
    aprtType2.setToSubmit();
    aprtType3.setToSubmit();
    aprtType.keepStatusOnAction();
    aprtType2.keepStatusOnAction();
    aprtType3.keepStatusOnAction();
    add(form);
  }

  protected void addStage(int stage) {
    Text stageText = new Text(_iwrb.getLocalizedString("stage","Stage")+" "+Integer.toString(stage)+" "+_iwrb.getLocalizedString("of","of")+" "+Integer.toString(_numberOfStages));
    stageText.setFontStyle("font-family:Verdana, Helvetica, Arial, sans-serif; font-size: 14px; font-weight: bold; color: #932a2b;");

    add(stageText);
    add(Text.getBreak());
  }

  /*
   *
   */
  protected void doCampusInformation(IWContext iwc) {
    List subjects = ApplicationFinder.listOfNonExpiredSubjects();
    List categories = BuildingFinder.listOfApartmentCategory();
    Text textTemplate = new Text();

    Form form = new Form();
    Table t = new Table(2,2);
    t.setWidth(1,"220");

    Text heading = (Text)textTemplate.clone();
    heading.setStyle("headlinetext");
    heading.setText(_iwrb.getLocalizedString("applicationSubjectTitle","Veldu tegund umsóknar"));
    Text text1 = (Text)textTemplate.clone();
    text1.setStyle("bodytext");
    text1.setText(_iwrb.getLocalizedString("applicationSubject","Umsókn um"));
    text1.setBold();
    Text text2 = (Text)textTemplate.clone();
    text2.setStyle("bodytext");
    text2.setText(_iwrb.getLocalizedString("apartmentType","Tegund íbúðar"));
    text2.setBold();
    Text required = (Text)textTemplate.clone();
    required.setText(" * ");
    required.setBold();
    required.setStyle("required");
    Text info = (Text)textTemplate.clone();
    info.setText(_iwrb.getLocalizedString("mustFillOut","* Stjörnumerkt svæði verður að fylla út"));
    info.setStyle("subtext");

    DropdownMenu subject = new DropdownMenu(subjects,"subject");
    subject.setStyle("formstyle");
    DropdownMenu aprtCat = new DropdownMenu(categories,"aprtCat");
    aprtCat.setStyle("formstyle");
    Image back = _iwrb.getImage("back.gif");
    back.setAttribute("onClick","history.go(-1)");
    SubmitButton ok = new SubmitButton(_iwrb.getImage("next.gif",_iwrb.getLocalizedString("ok","áfram")));

    form.add(heading);
    form.add(Text.getBreak());
    form.add(Text.getBreak());
    form.add(t);
    form.add(Text.getBreak());
    form.add(Text.getBreak());

    t.add(text1,1,1);
    t.add(required,1,1);
    t.add(subject,2,1);
    t.add(text2,1,2);
    t.add(required,1,2);
    t.add(aprtCat,2,2);

    List residences = CampusApplicationFinder.listOfResidences();
    List occupations = CampusApplicationFinder.listOfSpouseOccupations();
    DropdownMenu resSelect = new DropdownMenu(residences,"currentResidence");
    resSelect.setStyle("formstyle");
    DropdownMenu occSelect = new DropdownMenu(occupations,"spouseOccupation");
    occSelect.setStyle("formstyle");
    DropdownMenu studyBeginMo = new DropdownMenu("studyBeginMo");
    studyBeginMo.setStyle("formstyle");
    studyBeginMo.addMenuElement(1,_iwrb.getLocalizedString("january","jan"));
    studyBeginMo.addMenuElement(2,_iwrb.getLocalizedString("february","feb"));
    studyBeginMo.addMenuElement(3,_iwrb.getLocalizedString("march","mar"));
    studyBeginMo.addMenuElement(4,_iwrb.getLocalizedString("april","apr"));
    studyBeginMo.addMenuElement(5,_iwrb.getLocalizedString("may","maí"));
    studyBeginMo.addMenuElement(6,_iwrb.getLocalizedString("june","jún"));
    studyBeginMo.addMenuElement(7,_iwrb.getLocalizedString("july","júl"));
    studyBeginMo.addMenuElement(8,_iwrb.getLocalizedString("august","ágú"));
    studyBeginMo.addMenuElement(9,_iwrb.getLocalizedString("september","sep"));
    studyBeginMo.addMenuElement(10,_iwrb.getLocalizedString("october","okt"));
    studyBeginMo.addMenuElement(11,_iwrb.getLocalizedString("november","nóv"));
    studyBeginMo.addMenuElement(12,_iwrb.getLocalizedString("december","des"));
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

    Text heading2 = (Text)textTemplate.clone();
    heading2.setStyle("headlinetext");
    heading2.setText(_iwrb.getLocalizedString("otherInfo","Aðrar upplýsingar um umsækjanda"));
    Text required2 = (Text)textTemplate.clone();
    required2.setText(" * ");
    required2.setBold();
    required2.setStyle("required");
    Text info2 = (Text)textTemplate.clone();
    info2.setText(_iwrb.getLocalizedString("mustFillOut","* Stjörnumerkt svæði verður að fylla út"));
    info2.setStyle("subtext");
    Text text1_1 = (Text)textTemplate.clone();
    text1_1.setText(_iwrb.getLocalizedString("studyBegin","Nám hafið við HÍ (mán./ár)"));
    text1_1.setStyle("bodytext");
    text1_1.setBold();
    Text text2_1 = (Text)textTemplate.clone();
    text2_1.setText(_iwrb.getLocalizedString("studyEnd","Áætluð námslok (mán./ár)"));
    text2_1.setStyle("bodytext");
    text2_1.setBold();
    Text text3 = (Text)textTemplate.clone();
    text3.setText(_iwrb.getLocalizedString("faculty","Deild"));
    text3.setStyle("bodytext");
    text3.setBold();
    Text text4 = (Text)textTemplate.clone();
    text4.setText(_iwrb.getLocalizedString("studyTrack","Námsbraut"));
    text4.setStyle("bodytext");
    text4.setBold();
    Text text5 = (Text)textTemplate.clone();
    text5.setText(_iwrb.getLocalizedString("currentRes","Núverandi húsnæði"));
    text5.setStyle("bodytext");
    text5.setBold();
    Text text6 = (Text)textTemplate.clone();
    text6.setText(_iwrb.getLocalizedString("spouseName","Nafn umsækjanda/maka"));
    text6.setStyle("bodytext");
    Text text7 = (Text)textTemplate.clone();
    text7.setText(_iwrb.getLocalizedString("spouseSSN","Kennitala"));
    text7.setStyle("bodytext");
    Text text8 = (Text)textTemplate.clone();
    text8.setText(_iwrb.getLocalizedString("spouseSchool","Skóli"));
    text8.setStyle("bodytext");
    Text text9 = (Text)textTemplate.clone();
    text9.setText(_iwrb.getLocalizedString("spouseStudyTrack","Námsbraut"));
    text9.setStyle("bodytext");
    Text text10 = (Text)textTemplate.clone();
    text10.setText(_iwrb.getLocalizedString("spouseStudyBegin","Nám hafið (mán./ár)"));
    text10.setStyle("bodytext");
    Text text11 = (Text)textTemplate.clone();
    text11.setText(_iwrb.getLocalizedString("spouseStudyEnd","Áætluð námslok (mán./ár)"));
    text11.setStyle("bodytext");
    Text text12 = (Text)textTemplate.clone();
    text12.setText(_iwrb.getLocalizedString("spouseOccupation","Maki er"));
    text12.setStyle("bodytext");
    Text text13 = (Text)textTemplate.clone();
    text13.setText(_iwrb.getLocalizedString("children","Nöfn og fæðingardagur barna sem búa hjá umsækjanda"));
    text13.setStyle("bodytext");
    Text text14 = (Text)textTemplate.clone();
    text14.setText(_iwrb.getLocalizedString("income","Tekjur, styrkir og námslán umsækjanda 1.1 - 1.6 í ár"));
    text14.setStyle("bodytext");
    text14.setBold();
    Text text15 = (Text)textTemplate.clone();
    text15.setText(_iwrb.getLocalizedString("spouseIncome","Tekjur, styrkir og námslán umsækjanda/maka 1.1 - 1.6 í ár"));
    text15.setStyle("bodytext");
    Text text16 = (Text)textTemplate.clone();
    text16.setText(_iwrb.getLocalizedString("wantHousingFrom","Húsnæði óskast frá og með"));
    text16.setStyle("bodytext");
    text16.setBold();
    Text text17 = (Text)textTemplate.clone();
    text17.setText(_iwrb.getLocalizedString("waitingList","Óska eftir að vera á biðlista ef ég fæ ekki úthlutað húsnæði"));
    text17.setStyle("bodytext");
    Text text18 = (Text)textTemplate.clone();
    text18.setText(_iwrb.getLocalizedString("furniture","Óska eftir að leigja húsgögn ef mögulegt er"));
    text18.setStyle("bodytext");
    Text text19 = (Text)textTemplate.clone();
    text19.setText(_iwrb.getLocalizedString("contact","Ef ekki næst í mig í síma á dvalarstað má ná í mig eða skilja eftir skilaboð í sima"));
    text19.setStyle("bodytext");
    Text text20 = (Text)textTemplate.clone();
    text20.setText(_iwrb.getLocalizedString("email","Tölvupóstur"));
    text20.setStyle("bodytext");
    text20.setBold();
    Text text21 = (Text)textTemplate.clone();
    text21.setText(_iwrb.getLocalizedString("info","Aðrar upplýsingar"));
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

    Table t2 = new Table(2,23);
      t2.setWidth(1,"220");
      t2.setColumnVerticalAlignment(1,"top");
      t2.setColumnVerticalAlignment(2,"top");

    form.add(heading2);
    form.add(Text.getBreak());
    form.add(Text.getBreak());
    form.add(t2);
    t2.add(text1_1,1,1);
    t2.add(required,1,1);
    t2.add(studyBeginMo,2,1);
    t2.add(studyBeginYr,2,1);
    t2.add(text2_1,1,2);
    t2.add(required,1,2);
    t2.add(studyEndMo,2,2);
    t2.add(studyEndYr,2,2);
    t2.add(text3,1,3);
    t2.add(required,1,3);
    t2.add(input1,2,3);
    t2.add(text4,1,4);
    t2.add(required,1,4);
    t2.add(input2,2,4);
    t2.add(text5,1,5);
    t2.add(required,1,5);
    t2.add(resSelect,2,5);
    t2.add(input3,2,5);
    t2.add(text6,1,6);
    t2.add(input4,2,6);
    t2.add(text7,1,7);
    t2.add(input5,2,7);
    t2.add(text8,1,8);
    t2.add(input6,2,8);
    t2.add(text9,1,9);
    t2.add(input7,2,9);
    t2.add(text10,1,10);
    t2.add(spouseStudyBeginMo,2,10);
    t2.add(spouseStudyBeginYr,2,10);
    t2.add(text11,1,11);
    t2.add(spouseStudyEndMo,2,11);
    t2.add(spouseStudyEndYr,2,11);
    t2.add(text12,1,12);
    t2.add(occSelect,2,12);
    t2.add(text13,1,13);
    t2.add(input12,2,13);
    t2.add(text14,1,14);
    t2.add(required,1,14);
    t2.add(input8,2,14);
    t2.add(text15,1,15);
    t2.add(input9,2,15);
    t2.add(text16,1,16);
    t2.add(required,1,16);
    t2.add(input16,2,16);
    t2.add(text17,1,17);
    t2.add(input14,2,17);
    t2.add(text18,1,18);
    t2.add(input15,2,18);
    t2.add(text19,1,19);
    t2.add(input10,2,19);
    t2.add(text20,1,20);
    t2.add(required,1,20);
    t2.add(input11,2,20);
    t2.add(text21,1,21);
    t2.add(input13,2,21);
    t2.add(back,1,23);
    t2.add("&nbsp;&nbsp;&nbsp;",1,23);
    t2.add(ok,1,23);
    form.add(Text.getBreak());
    form.add(Text.getBreak());
    form.add(info);
    form.add(new HiddenInput("status",Integer.toString(_statusCampusInfo)));
    add(form);
  }

  /**
   *
   */
  public String getBundleIdentifier() {
    return(IW_RESOURCE_BUNDLE);
  }

  /*
   *
   */
  private void checkAparmentTypesSelected(IWContext iwc) {
    String key1 = (String)iwc.getParameter("aprtType");
    String key2 = (String)iwc.getParameter("aprtType2");
    String key3 = (String)iwc.getParameter("aprtType3");

    try {
      int type = ApartmentTypeComplexHelper.getPartKey(key1,1);
      ApartmentType room = new ApartmentType(type);
      _apartment1 = room.getID();

      if ((key2 != null) && (!key2.equalsIgnoreCase("-1"))) {
        type = ApartmentTypeComplexHelper.getPartKey(key2,1);
        room = new ApartmentType(type);
        _apartment2 = room.getID();
      }

      if ((key3 != null) && (!key3.equalsIgnoreCase("-1"))) {
        type = ApartmentTypeComplexHelper.getPartKey(key3,1);
        room = new ApartmentType(type);
        _apartment3 = room.getID();
      }
    }
    catch(SQLException e) {
      e.printStackTrace();
    }
  }

  public void main(IWContext iwc){
    _iwrb = getResourceBundle(iwc);
    control(iwc);
  }

  /**
   *
   */
  protected void doGeneralInformation(IWContext iwc) {
    TextInput textInputTemplate = new TextInput();
    Form form = new Form();
    DataTable t = new DataTable();
    BackButton back = new BackButton(_iwrb.getImage("back.gif"));
    SubmitButton ok = new SubmitButton(_iwrb.getImage("next.gif",_iwrb.getLocalizedString("ok","áfram")));

    String heading = _iwrb.getLocalizedString("generalInfo","Almennar upplýsingar um umsækjanda");
    String text1 = _iwrb.getLocalizedString("firstName","Fornafn");
    String text2 = _iwrb.getLocalizedString("middleName","Millinafn");
    String text3 = _iwrb.getLocalizedString("lastName","Eftirnafn");
//    text3.setBold();
    String text4 = _iwrb.getLocalizedString("ssn","Kennitala");
//    text4.setBold();
    String text5 = _iwrb.getLocalizedString("legalResidence","Lögheimili");
//    text5.setBold();
    String text6 = _iwrb.getLocalizedString("residence","Dvalarstaður");
//    text6.setBold();
    String text7 = _iwrb.getLocalizedString("residencePhone","Símanúmer á dvalarstað");
//    text7.setBold();
    String text8 = _iwrb.getLocalizedString("po","Póstnúmer");
//    text8.setBold();
    String required = " * ";
//    required.setBold();
    String info = _iwrb.getLocalizedString("mustFillOut","* Stjörnumerkt svæði verður að fylla út");
//    info.setStyle("subtext");
    TextInput input1 = (TextInput)textInputTemplate.clone();
    input1.setName("firstName");
    input1.setLength(40);
    Edit.setStyle(input1);
    TextInput input2 = (TextInput)textInputTemplate.clone();
    input2.setName("middleName");
    input2.setLength(40);
    Edit.setStyle(input2);
    TextInput input3 = (TextInput)textInputTemplate.clone();
    input3.setName("lastName");
    input3.setLength(40);
    Edit.setStyle(input3);
    TextInput input4 = (TextInput)textInputTemplate.clone();
    input4.setName("ssn");
    input4.setLength(11);
    Edit.setStyle(input4);
    TextInput input5 = (TextInput)textInputTemplate.clone();
    input5.setName("legalResidence");
    input5.setLength(40);
    Edit.setStyle(input5);
    TextInput input6 = (TextInput)textInputTemplate.clone();
    input6.setName("residence");
    input6.setLength(40);
    Edit.setStyle(input6);
    TextInput input7 = (TextInput)textInputTemplate.clone();
    input7.setName("residencePhone");
    input7.setLength(8);
    Edit.setStyle(input7);
    TextInput input8 = (TextInput)textInputTemplate.clone();
    input8.setName("po");
    input8.setLength(3);
    Edit.setStyle(input8);

    t.addTitle(heading);
    t.add(Edit.formatText(text1,true),1,1);
    t.add(Edit.formatText(required,true),1,1);
    t.add(input1,2,1);
    t.add(Edit.formatText(text2),1,2);
    t.add(input2,2,2);
    t.add(Edit.formatText(text3),1,3);
    t.add(Edit.formatText(required),1,3);
    t.add(input3,2,3);
    t.add(Edit.formatText(text4),1,4);
    t.add(Edit.formatText(required),1,4);
    t.add(input4,2,4);
    t.add(Edit.formatText(text5),1,5);
    t.add(Edit.formatText(required),1,5);
    t.add(input5,2,5);
    t.add(Edit.formatText(text6),1,6);
    t.add(Edit.formatText(required),1,6);
    t.add(input6,2,6);
    t.add(Edit.formatText(text7),1,7);
    t.add(Edit.formatText(required),1,7);
    t.add(input7,2,7);
    t.add(Edit.formatText(text8),1,8);
    t.add(Edit.formatText(required),1,8);
    t.add(input8,2,8);
    t.addButton(ok);

    form.add(t);
    form.add(Text.getBreak());
    form.add(Text.getBreak());
    form.add(Text.getBreak());
    form.add(info);
    form.add(new HiddenInput("status",Integer.toString(_statusGeneralInfo)));
    add(form);
  }

}