/*
 * $Id: CampusApplicationForm.java,v 1.6 2002/03/18 19:59:28 aron Exp $
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
import com.idega.presentation.Table;
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
  protected final int _statusEnteringPage = 0;
  protected final int _statusSubject = 1;
  protected final int _statusGeneralInfo = 2;
  protected final int _statusCampusInfo = 3;
  protected final int _statusAppliedFor = 4;
  protected final int _statusSelectingApartmentTypes = 99;
  protected final int _numberOfStages = 3;

  private int _apartment1 = -1;
  private int _apartment2 = -1;
  private int _apartment3 = -1;

	protected IWBundle _iwb;

  private static final String IW_RESOURCE_BUNDLE = "is.idega.idegaweb.campus";

  protected Text _required = Edit.formatText(" * ",true);
  protected Text _info = null;

  /**
   *
   */
  public CampusApplicationForm() {
  }

  /*
   *
   */
  protected void control(IWContext iwc) {
    debugParameters(iwc);
		_iwb = getBundle(iwc);
    String statusString = iwc.getParameter(APP_STATUS);
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
    Edit.setStyle(aprtType);
    DropdownMenu aprtType2 = new DropdownMenu("aprtType2");
    Edit.setStyle(aprtType2);
    DropdownMenu aprtType3 = new DropdownMenu("aprtType3");
    Edit.setStyle(aprtType3);
    aprtType.addDisabledMenuElement("-1","");
    aprtType2.addMenuElement("-1","");
    aprtType3.addMenuElement("-1","");

    for (int i = 0; i < vAprtType.size(); i++) {
      ApartmentTypeComplexHelper eAprtType = (ApartmentTypeComplexHelper)vAprtType.elementAt(i);
      aprtType.addMenuElement(eAprtType.getKey(),eAprtType.getName());
      aprtType2.addMenuElement(eAprtType.getKey(),eAprtType.getName());
      aprtType3.addMenuElement(eAprtType.getKey(),eAprtType.getName());
    }

    Form form = new Form();
    DataTable t = new DataTable();
    Edit.setStyle(t);

    String text1 = _iwrb.getLocalizedString("firstChoice","Fyrsta val");
    String text2 = _iwrb.getLocalizedString("secondChoice","Annað val");
    String text3 = _iwrb.getLocalizedString("thirdChoice","Þriðja val");

    Image back = _iwrb.getImage("back.gif");
    back.setAttribute("onClick","history.go(-1)");
    SubmitButton ok = new SubmitButton(_iwrb.getImage("next.gif",_iwrb.getLocalizedString("ok","áfram")),APP_STATUS,Integer.toString(_statusAppliedFor));

    form.add(t);

    t.addTitle(_iwrb.getLocalizedString("applied","Húsnæði sem sótt er um"));
    t.add(Edit.formatText(text1,true),1,1);
    t.add(Edit.formatText(_required,true),1,1);
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

    t.add(Edit.formatText(text2),1,2);
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

    t.add(Edit.formatText(text3),1,3);
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

    t.addButton(back);
    t.addButton(ok);
    form.add(Text.getBreak());
    form.add(Text.getBreak());
    form.add(Text.getBreak());
    form.add(_info);
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

    Edit.setStyle(textTemplate);

    Form form = new Form();
    DataTable t = new DataTable();
    Edit.setStyle(t);

    String text1 = _iwrb.getLocalizedString("applicationSubject","Umsókn um");
    String text2 = _iwrb.getLocalizedString("apartmentType","Tegund íbúðar");

    DropdownMenu subject = new DropdownMenu(subjects,"subject");
    Edit.setStyle(subject);
    DropdownMenu aprtCat = new DropdownMenu(categories,"aprtCat");
    Edit.setStyle(aprtCat);
    Image back = _iwrb.getImage("back.gif");
    back.setAttribute("onClick","history.go(-1)");
    SubmitButton ok = new SubmitButton(_iwrb.getImage("next.gif",_iwrb.getLocalizedString("ok","áfram")));

    form.add(t);

    t.addTitle(_iwrb.getLocalizedString("applicationSubjectTitle","Veldu tegund umsóknar"));
    t.add(Edit.formatText(text1,true),1,1);
    t.add(Edit.formatText(_required,true),1,1);
    t.add(subject,2,1);
    t.add(Edit.formatText(text2,true),1,2);
    t.add(Edit.formatText(_required,true),1,2);
    t.add(aprtCat,2,2);

    List residences = CampusApplicationFinder.listOfResidences();
    List occupations = CampusApplicationFinder.listOfSpouseOccupations();
    DropdownMenu resSelect = new DropdownMenu(residences,"currentResidence");
    Edit.setStyle(resSelect);
    DropdownMenu occSelect = new DropdownMenu(occupations,"spouseOccupation");
    Edit.setStyle(occSelect);
    DateInput studyBegin = new DateInput("studyBegin");
    Edit.setStyle(studyBegin);
    studyBegin.setToShowDay(false);
    DateInput studyEnd = new DateInput("studyEnd");
    Edit.setStyle(studyEnd);
    studyEnd.setToShowDay(false);
    DateInput spouseStudyBegin = new DateInput("spouseStudyBegin");
    Edit.setStyle(spouseStudyBegin);
    spouseStudyBegin.setToShowDay(false);
    DateInput spouseStudyEnd = new DateInput("spouseStudyEnd");
    Edit.setStyle(spouseStudyEnd);
    spouseStudyEnd.setToShowDay(false);

    int currentYear = idegaTimestamp.RightNow().getYear();

/*    DropdownMenu studyBeginYr = new DropdownMenu("studyBeginYr");
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
    spouseStudyEndYr.setName("spouseStudyEndYr");*/

    String text1_1 = _iwrb.getLocalizedString("studyBegin","Nám hafið við HÍ (mán./ár)");
    String text2_1 = _iwrb.getLocalizedString("studyEnd","Áætluð námslok (mán./ár)");
    String text3 = _iwrb.getLocalizedString("faculty","Deild");
    String text4 = _iwrb.getLocalizedString("studyTrack","Námsbraut");
    String text5 = _iwrb.getLocalizedString("currentRes","Núverandi húsnæði");
    String text6 = _iwrb.getLocalizedString("spouseName","Nafn umsækjanda/maka");
    String text7 = _iwrb.getLocalizedString("spouseSSN","Kennitala");
    String text8 = _iwrb.getLocalizedString("spouseSchool","Skóli");
    String text9 = _iwrb.getLocalizedString("spouseStudyTrack","Námsbraut");
    String text10 = _iwrb.getLocalizedString("spouseStudyBegin","Nám hafið (mán./ár)");
    String text11 = _iwrb.getLocalizedString("spouseStudyEnd","Áætluð námslok (mán./ár)");
    String text12 = _iwrb.getLocalizedString("spouseOccupation","Maki er");
    String text13 = _iwrb.getLocalizedString("children","Nöfn og fæðingardagur barna sem búa hjá umsækjanda");
    String text14 = _iwrb.getLocalizedString("income","Tekjur, styrkir og námslán umsækjanda 1.1 - 1.6 í ár");
    String text15 = _iwrb.getLocalizedString("spouseIncome","Tekjur, styrkir og námslán umsækjanda/maka 1.1 - 1.6 í ár");
    String text16 = _iwrb.getLocalizedString("wantHousingFrom","Húsnæði óskast frá og með");
    String text17 = _iwrb.getLocalizedString("waitingList","Óska eftir að vera á biðlista ef ég fæ ekki úthlutað húsnæði");
    String text18 = _iwrb.getLocalizedString("furniture","Óska eftir að leigja húsgögn ef mögulegt er");
    String text19 = _iwrb.getLocalizedString("contact","Ef ekki næst í mig í síma á dvalarstað má ná í mig eða skilja eftir skilaboð í sima");
    String text20 = _iwrb.getLocalizedString("email","Tölvupóstur");
    String text21 = _iwrb.getLocalizedString("info","Aðrar upplýsingar");

    TextInput textInputTemplate = new TextInput();
    Edit.setStyle(textInputTemplate);
    TextInput input1 = (TextInput)textInputTemplate.clone();
    input1.setName("faculty");
    TextInput input2 = (TextInput)textInputTemplate.clone();
    input2.setName("studyTrack");
    TextInput input3 = (TextInput)textInputTemplate.clone();
    input3.setName("resInfo");
    input3.setLength(10);
    TextInput input4 = (TextInput)textInputTemplate.clone();
    input4.setName("spouseName");
    TextInput input5 = (TextInput)textInputTemplate.clone();
    input5.setName("spouseSSN");
    input5.setLength(12);
    TextInput input6 = (TextInput)textInputTemplate.clone();
    input6.setName("spouseSchool");
    TextInput input7 = (TextInput)textInputTemplate.clone();
    input7.setName("spouseStudyTrack");
    /*
    TextInput input8 = (TextInput)textInputTemplate.clone();
    input8.setName("income");
    input8.setLength(10);
    TextInput input9 = (TextInput)textInputTemplate.clone();
    input9.setName("spouseIncome");
    input9.setLength(10);
    */
    TextInput input10 = (TextInput)textInputTemplate.clone();
    input10.setName("contact");
    input10.setLength(10);
    TextInput input11 = (TextInput)textInputTemplate.clone();
    input11.setName("email");


    int children = 2;
    Table childrenTable = new Table(2,children);
    for (int i = 0; i < children; i++) {
      TextInput childName = new TextInput("childname"+i);
      TextInput childBirth = new TextInput("childbirth"+i);
      Edit.setStyle(childName);
      Edit.setStyle(childBirth);
      childName.setLength(10);
      childBirth.setLength(10);
      childrenTable.add(childName,1,i+1);
      childrenTable.add(childBirth,2,i+1);
    }
    childrenTable.add(new HiddenInput("children_count",String.valueOf(children)));


    TextArea input13 = new TextArea("children");
    Edit.setStyle(input13);
    input13.setHeight(4);
    input13.setWidth(30);

    /*
    CheckBox input14 = new CheckBox("waitingList");
    Edit.setStyle(input14);

    CheckBox input15 = (CheckBox)input14.clone();
    input15.setName("furniture");
*/
    DateInput input16 = new DateInput("wantHousingFrom");
    input16.setToCurrentDate();

    DataTable t2 = new DataTable();
    Edit.setStyle(t2);

    form.add(t2);
    t2.addTitle(_iwrb.getLocalizedString("otherInfo","Aðrar upplýsingar um umsækjanda"));
    int row = 1;
    t2.add(Edit.formatText(text1_1,true),1,row);
    t2.add(Edit.formatText(_required,true),1,row);
    t2.add(studyBegin,2,row);
    row++;
    t2.add(Edit.formatText(text2_1,true),1,row);
    t2.add(_required,1,row);
    t2.add(studyEnd,2,row);
    row++;
    t2.add(Edit.formatText(text3,true),1,row);
    t2.add(_required,1,row);
    t2.add(input1,2,row);
    row++;
    t2.add(Edit.formatText(text4,true),1,row);
    t2.add(_required,1,row);
    t2.add(input2,2,row);
    row++;
    t2.add(Edit.formatText(text5,true),1,row);
    t2.add(_required,1,row);
    t2.add(resSelect,2,row);
    t2.add(input3,2,row);
    row++;
    t2.add(Edit.formatText(text6),1,row);
    t2.add(input4,2,row);
    row++;
    t2.add(Edit.formatText(text7),1,row);
    t2.add(input5,2,row);
    row++;
    t2.add(Edit.formatText(text8),1,row);
    t2.add(input6,2,row);
    row++;
    t2.add(Edit.formatText(text9),1,row);
    t2.add(input7,2,row);
    row++;
    t2.add(Edit.formatText(text10),1,row);
    t2.add(spouseStudyBegin,2,row);
    row++;
    t2.add(Edit.formatText(text11),1,row);
    t2.add(spouseStudyEnd,2,row);
    row++;
    t2.add(Edit.formatText(text12),1,row);
    t2.add(occSelect,2,row);
    row++;
    t2.add(Edit.formatText(text13),1,row);
    t2.add(childrenTable,2,row);
    row++;
    /*
    t2.add(Edit.formatText(text14,true),1,row);
    t2.add(_required,1,row);
    t2.add(input8,2,row);
    row++;
    t2.add(Edit.formatText(text15),1,row);
    t2.add(input9,2,row);
    row++;
    */
    t2.add(Edit.formatText(text16,true),1,row);
    t2.add(_required,1,row);
    t2.add(input16,2,row);
    row++;
    /*
    t2.add(Edit.formatText(text17),1,row);
    t2.add(input14,2,row);
    row++;
    t2.add(Edit.formatText(text18),1,row);
    t2.add(input15,2,row);
    row++;
    */
    t2.add(Edit.formatText(text19),1,row);
    t2.add(input10,2,row);
    row++;
    t2.add(Edit.formatText(text20,true),1,row);
    t2.add(_required,1,row);
    t2.add(input11,2,row);
    row++;
    t2.add(Edit.formatText(text21),1,row);
    t2.add(input13,2,row);
    row++;

    t2.addButton(back);
    t2.addButton(ok);

    form.add(Text.getBreak());
    form.add(Text.getBreak());
    form.add(_info);
    form.add(new HiddenInput(APP_STATUS,Integer.toString(_statusCampusInfo)));
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
    _info = Edit.formatText(_iwrb.getLocalizedString("mustFillOut","* Stjörnumerkt svæði verður að fylla út"));

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

    String heading = _iwrb.getLocalizedString(APP_GENINFO,"General information about applicant");
    String firstNameLabel = _iwrb.getLocalizedString(APP_FIRST_NAME,"First name");
    String middleNameLabel = _iwrb.getLocalizedString(APP_MIDDLE_NAME,"Middle name");
    String lastNameLabel = _iwrb.getLocalizedString(APP_LAST_NAME,"Last name");
    String ssnLabel = _iwrb.getLocalizedString(APP_SSN,"Social security number");
    String legalResidenceLabel = _iwrb.getLocalizedString(APP_LEGAL_RESIDENCE,"Legal residence");
    String residenceLabel = _iwrb.getLocalizedString(APP_RESIDENCE,"Residence");
    String phoneLabel = _iwrb.getLocalizedString(APP_PHONE,"Residence phone");
    String poLabel = _iwrb.getLocalizedString(APP_PO,"PO");
    TextInput firstName = (TextInput)textInputTemplate.clone();
    firstName.setName(APP_FIRST_NAME);
    firstName.setLength(40);
    Edit.setStyle(firstName);
    TextInput middleName = (TextInput)textInputTemplate.clone();
    middleName.setName(APP_MIDDLE_NAME);
    middleName.setLength(40);
    Edit.setStyle(middleName);
    TextInput lastName = (TextInput)textInputTemplate.clone();
    lastName.setName(APP_LAST_NAME);
    lastName.setLength(40);
    Edit.setStyle(lastName);
    TextInput ssn = (TextInput)textInputTemplate.clone();
    ssn.setName(APP_SSN);
    ssn.setLength(11);
    Edit.setStyle(ssn);
    TextInput legalResidence = (TextInput)textInputTemplate.clone();
    legalResidence.setName(APP_LEGAL_RESIDENCE);
    legalResidence.setLength(40);
    Edit.setStyle(legalResidence);
    TextInput residence = (TextInput)textInputTemplate.clone();
    residence.setName(APP_RESIDENCE);
    residence.setLength(40);
    Edit.setStyle(residence);
    TextInput phone = (TextInput)textInputTemplate.clone();
    phone.setName(APP_PHONE);
    phone.setLength(8);
    Edit.setStyle(phone);
    TextInput po = (TextInput)textInputTemplate.clone();
    po.setName(APP_PO);
    po.setLength(3);
    Edit.setStyle(po);

    int row = 1;
    t.addTitle(heading);
    t.add(Edit.formatText(firstNameLabel,true),1,row);
    t.add(_required,1,row);
    t.add(firstName,2,row);
    row++;
    t.add(Edit.formatText(middleNameLabel),1,row);
    t.add(middleName,2,row);
    row++;
    t.add(Edit.formatText(lastNameLabel,true),1,row);
    t.add(_required,1,row);
    t.add(lastName,2,row);
    row++;
    t.add(Edit.formatText(ssnLabel,true),1,row);
    t.add(_required,1,row);
    t.add(ssn,2,row);
    row++;
    t.add(Edit.formatText(legalResidenceLabel,true),1,row);
    t.add(_required,1,row);
    t.add(legalResidence,2,row);
    row++;
    t.add(Edit.formatText(residenceLabel,true),1,row);
    t.add(_required,1,row);
    t.add(residence,2,row);
    row++;
    t.add(Edit.formatText(phoneLabel,true),1,row);
    t.add(_required,1,row);
    t.add(phone,2,row);
    row++;
    t.add(Edit.formatText(poLabel,true),1,row);
    t.add(_required,1,row);
    t.add(po,2,row);
    row++;
    t.addButton(ok);

    form.add(t);
    form.add(Text.getBreak());
    form.add(Text.getBreak());
    form.add(Text.getBreak());
    form.add(_info);
    form.add(new HiddenInput(APP_STATUS,Integer.toString(_statusGeneralInfo)));
    add(form);
  }

}