/*
 * $Id: CampusApplicationForm.java,v 1.13 2001/08/20 17:51:56 laddi Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.application;

import com.idega.block.application.presentation.ApplicationForm;
import com.idega.block.application.business.ApplicationFinder;
import com.idega.block.application.business.ReferenceNumberHandler;
import com.idega.block.building.business.BuildingFinder;
import com.idega.block.building.business.ApartmentTypeComplexHelper;
import com.idega.jmodule.object.Image;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.Script;
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
import com.idega.idegaweb.IWBundle;
import com.idega.util.idegaTimestamp;
import com.idega.util.SendMail;
import com.idega.util.CypherText;
import is.idegaweb.campus.application.CampusApplicationFinder;
import is.idegaweb.campus.application.CampusApplicationFormHelper;
import java.util.List;
import java.util.Vector;
import java.util.Iterator;
import java.sql.SQLException;
import com.idega.block.building.data.ApartmentType;
import com.idega.idegaweb.IWResourceBundle;

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
  private final int statusSelectingApartmentTypes_ = 99;

  private int pic1 = -1;
  private int pic2 = -1;
  private int pic3 = -1;

  private static final String IW_RESOURCE_BUNDLE = "is.idegaweb.campus";

  /**
   *
   */
  public CampusApplicationForm() {
  }

  /*
   *
   */
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
      CampusApplicationFormHelper.saveSubject(modinfo);
      doGeneralInformation(modinfo);
    }
    else if (status == statusGeneralInfo_) {
      CampusApplicationFormHelper.saveApplicantInformation(modinfo);
      doCampusInformation(modinfo);
    }
    else if (status == statusCampusInfo_) {
      CampusApplicationFormHelper.saveCampusInformation(modinfo);
      doSelectAppliedFor(modinfo);
    }
    else if (status == statusAppliedFor_) {
      CampusApplicationFormHelper.saveAppliedFor(modinfo);
      if (CampusApplicationFormHelper.saveDataToDB(modinfo))
        doDone();
      else
        doError();
    }
    else if (status == statusSelectingApartmentTypes_) {
      Vector pics = CampusApplicationFormHelper.checkAparmentTypesSelected(modinfo);
      pic1 = -1;
      pic2 = -1;
      pic3 = -1;
      Iterator it = pics.iterator();
      while (it.hasNext()) {
        Integer pic = (Integer)it.next();
      }

      doSelectAppliedFor(modinfo);
    }
  }

  /*
   *
   */
  protected void doSelectSubject(ModuleInfo modinfo) {
    List subjects = ApplicationFinder.listOfNonExpiredSubjects();
    List categories = BuildingFinder.listOfApartmentCategory();
    Text textTemplate = new Text();

    Form form = new Form();
    Table t = new Table(2,4);
      t.setWidth(1,"250");
      t.setCellpadding(5);

    Text heading = (Text)textTemplate.clone();
    heading.setStyle("headlinetext");
    heading.setText(iwrb_.getLocalizedString("applicationSubject","Veldu tegund umsóknar"));
    Text text1 = (Text)textTemplate.clone();
    text1.setStyle("bodytext");
    text1.setText(iwrb_.getLocalizedString("applicationSubject","Umsókn um"));
    text1.setBold();
    Text text2 = (Text)textTemplate.clone();
    text2.setStyle("bodytext");
    text2.setText(iwrb_.getLocalizedString("apartmentType","Tegund íbúðar"));
    text2.setBold();
    Text required = (Text)textTemplate.clone();
    required.setText(" * ");
    required.setBold();
    required.setStyle("required");
    Text info = (Text)textTemplate.clone();
    info.setText(iwrb_.getLocalizedString("mustFillOut","* Stjörnumerkt svæði verður að fylla út"));
    info.setStyle("subtext");

    DropdownMenu subject = new DropdownMenu(subjects,"subject");
    subject.setStyle("formstyle");
    DropdownMenu aprtCat = new DropdownMenu(categories,"aprtCat");
    aprtCat.setStyle("formstyle");
    SubmitButton ok = new SubmitButton("ok",iwrb_.getLocalizedString("ok","áfram"));
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

  /*
   *
   */
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

      /**
       * @todo Fjarlægja þetta úr þessum klasa og setja yfir í helperinn. Hann skilar
       * síðan bara myndinni eða null.
       */
      if (i == 0) {
        if (pic1 == -1) {
          try {
            int type = ApartmentTypeComplexHelper.getPartKey(eAprtType.getKey(),1);
            ApartmentType room = new ApartmentType(type);
            pic1 = room.getFloorPlanId();
          }
          catch(SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }

    Text textTemplate = new Text();

    Form form = new Form();
    Table t = new Table(3,5);
      t.setWidth(1,"250");
      t.setCellpadding(5);

    Text heading = (Text)textTemplate.clone();
    heading.setStyle("headlinetext");
    heading.setText(iwrb_.getLocalizedString("applied","Húsnæði sem sótt er um"));
    Text text1 = (Text)textTemplate.clone();
    text1.setStyle("bodytext");
    text1.setText(iwrb_.getLocalizedString("firstChoice","Fyrsta val"));
    text1.setBold();
    Text text2 = (Text)textTemplate.clone();
    text2.setStyle("bodytext");
    text2.setText(iwrb_.getLocalizedString("secondChoice","Annað val"));
    Text text3 = (Text)textTemplate.clone();
    text3.setStyle("bodytext");
    text3.setText(iwrb_.getLocalizedString("thirdChoice","Þriðja val"));
    Text required = (Text)textTemplate.clone();
    required.setText(" * ");
    required.setBold();
    required.setStyle("required");
    Text info = (Text)textTemplate.clone();
    info.setText(iwrb_.getLocalizedString("mustFillOut","* Stjörnumerkt svæði verður að fylla út"));
    info.setStyle("subtext");

    SubmitButton ok = new SubmitButton(iwrb_.getLocalizedString("ok","áfram"),"status",Integer.toString(statusAppliedFor_));
    ok.setStyle("idega");

    form.add(heading);
    form.add(Text.getBreak());
    form.add(Text.getBreak());
    form.add(t);

    t.add(text1,1,1);
    t.add(required,1,1);
    t.add(aprtType,2,1);

    if (pic1 > -1) {
      try {
        Image floorPlan1 = new Image(pic1);
        t.add(floorPlan1,3,1);
      }
      catch(SQLException e) {
        e.printStackTrace();
      }
    }

    t.add(text2,1,2);
    t.add(aprtType2,2,2);
    if (pic2 > -1) {
      try {
        Image floorPlan2 = new Image(pic2);
        t.add(floorPlan2,3,2);
      }
      catch(SQLException e) {
        e.printStackTrace();
      }
    }

    t.add(text3,1,3);
    t.add(aprtType3,2,3);
    if (pic3 > -1) {
      try {
        Image floorPlan3 = new Image(pic3);
        t.add(floorPlan3,3,3);
      }
      catch(SQLException e) {
        e.printStackTrace();
      }
    }

    t.add(ok,2,5);
    form.add(Text.getBreak());
    form.add(Text.getBreak());
    form.add(Text.getBreak());
    form.add(info);
    aprtType.setOnChange("this.form.status.value='" + statusSelectingApartmentTypes_ + "'");
    aprtType2.setOnChange("this.form.status.value='" + statusSelectingApartmentTypes_ + "'");
    aprtType3.setOnChange("this.form.status.value='" + statusSelectingApartmentTypes_ + "'");
    aprtType.setToSubmit();
    aprtType2.setToSubmit();
    aprtType3.setToSubmit();
    aprtType.keepStatusOnAction();
    aprtType2.keepStatusOnAction();
    aprtType3.keepStatusOnAction();
    add(form);
  }

  /*
   *
   */
  protected void doCampusInformation(ModuleInfo modinfo) {
    List residences = CampusApplicationFinder.listOfResidences();
    List occupations = CampusApplicationFinder.listOfSpouseOccupations();
    DropdownMenu resSelect = new DropdownMenu(residences,"currentResidence");
    resSelect.setStyle("formstyle");
    DropdownMenu occSelect = new DropdownMenu(occupations,"spouseOccupation");
    occSelect.setStyle("formstyle");
    DropdownMenu studyBeginMo = new DropdownMenu("studyBeginMo");
    studyBeginMo.setStyle("formstyle");
    studyBeginMo.addMenuElement(1,iwrb_.getLocalizedString("january","jan"));
    studyBeginMo.addMenuElement(2,iwrb_.getLocalizedString("february","feb"));
    studyBeginMo.addMenuElement(3,iwrb_.getLocalizedString("march","mar"));
    studyBeginMo.addMenuElement(4,iwrb_.getLocalizedString("april","apr"));
    studyBeginMo.addMenuElement(5,iwrb_.getLocalizedString("may","maí"));
    studyBeginMo.addMenuElement(6,iwrb_.getLocalizedString("june","jún"));
    studyBeginMo.addMenuElement(7,iwrb_.getLocalizedString("july","júl"));
    studyBeginMo.addMenuElement(8,iwrb_.getLocalizedString("august","ágú"));
    studyBeginMo.addMenuElement(9,iwrb_.getLocalizedString("september","sep"));
    studyBeginMo.addMenuElement(10,iwrb_.getLocalizedString("october","okt"));
    studyBeginMo.addMenuElement(11,iwrb_.getLocalizedString("november","nóv"));
    studyBeginMo.addMenuElement(12,iwrb_.getLocalizedString("december","des"));
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


    SubmitButton ok = new SubmitButton("ok",iwrb_.getLocalizedString("ok","áfram"));
    ok.setStyle("idega");

    Text textTemplate = new Text();

    Text heading = (Text)textTemplate.clone();
    heading.setStyle("headlinetext");
    heading.setText(iwrb_.getLocalizedString("otherInfo","Aðrar upplýsingar um umsækjanda"));
    Text required = (Text)textTemplate.clone();
    required.setText(" * ");
    required.setBold();
    required.setStyle("required");
    Text info = (Text)textTemplate.clone();
    info.setText(iwrb_.getLocalizedString("mustFillOut","* Stjörnumerkt svæði verður að fylla út"));
    info.setStyle("subtext");
    Text text1 = (Text)textTemplate.clone();
    text1.setText(iwrb_.getLocalizedString("studyBegin","Nám hafið við HÍ (mán./ár)"));
    text1.setStyle("bodytext");
    text1.setBold();
    Text text2 = (Text)textTemplate.clone();
    text2.setText(iwrb_.getLocalizedString("studyEnd","Áætluð námslok (mán./ár)"));
    text2.setStyle("bodytext");
    text2.setBold();
    Text text3 = (Text)textTemplate.clone();
    text3.setText(iwrb_.getLocalizedString("faculty","Deild"));
    text3.setStyle("bodytext");
    text3.setBold();
    Text text4 = (Text)textTemplate.clone();
    text4.setText(iwrb_.getLocalizedString("studyTrack","Námsbraut"));
    text4.setStyle("bodytext");
    text4.setBold();
    Text text5 = (Text)textTemplate.clone();
    text5.setText(iwrb_.getLocalizedString("currentRes","Núverandi húsnæði"));
    text5.setStyle("bodytext");
    text5.setBold();
    Text text6 = (Text)textTemplate.clone();
    text6.setText(iwrb_.getLocalizedString("spouseName","Nafn umsækjanda/maka"));
    text6.setStyle("bodytext");
    Text text7 = (Text)textTemplate.clone();
    text7.setText(iwrb_.getLocalizedString("spouseSSN","Kennitala"));
    text7.setStyle("bodytext");
    Text text8 = (Text)textTemplate.clone();
    text8.setText(iwrb_.getLocalizedString("spouseSchool","Skóli"));
    text8.setStyle("bodytext");
    Text text9 = (Text)textTemplate.clone();
    text9.setText(iwrb_.getLocalizedString("spouseStudyTrack","Námsbraut"));
    text9.setStyle("bodytext");
    Text text10 = (Text)textTemplate.clone();
    text10.setText(iwrb_.getLocalizedString("spouseStudyBegin","Nám hafið (mán./ár)"));
    text10.setStyle("bodytext");
    Text text11 = (Text)textTemplate.clone();
    text11.setText(iwrb_.getLocalizedString("spouseStudyEnd","Áætluð námslok (mán./ár)"));
    text11.setStyle("bodytext");
    Text text12 = (Text)textTemplate.clone();
    text12.setText(iwrb_.getLocalizedString("spouseOccupation","Maki er"));
    text12.setStyle("bodytext");
    Text text13 = (Text)textTemplate.clone();
    text13.setText(iwrb_.getLocalizedString("children","Nöfn og fæðingardagur barna sem búa hjá umsækjanda"));
    text13.setStyle("bodytext");
    Text text14 = (Text)textTemplate.clone();
    text14.setText(iwrb_.getLocalizedString("income","Tekjur, styrkir og námslán umsækjanda 1.1 - 1.6 í ár"));
    text14.setStyle("bodytext");
    text14.setBold();
    Text text15 = (Text)textTemplate.clone();
    text15.setText(iwrb_.getLocalizedString("spouseIncome","Tekjur, styrkir og námslán umsækjanda/maka 1.1 - 1.6 í ár"));
    text15.setStyle("bodytext");
    Text text16 = (Text)textTemplate.clone();
    text16.setText(iwrb_.getLocalizedString("wantHousingFrom","Húsnæði óskast frá og með"));
    text16.setStyle("bodytext");
    text16.setBold();
    Text text17 = (Text)textTemplate.clone();
    text17.setText(iwrb_.getLocalizedString("waitingList","Óska eftir að vera á biðlista ef ég fæ ekki úthlutað húsnæði"));
    text17.setStyle("bodytext");
    Text text18 = (Text)textTemplate.clone();
    text18.setText(iwrb_.getLocalizedString("furniture","Óska eftir að leigja húsgögn ef mögulegt er"));
    text18.setStyle("bodytext");
    Text text19 = (Text)textTemplate.clone();
    text19.setText(iwrb_.getLocalizedString("contact","Ef ekki næst í mig í síma á dvalarstað má ná í mig eða skilja eftir skilaboð í sima"));
    text19.setStyle("bodytext");
    Text text20 = (Text)textTemplate.clone();
    text20.setText(iwrb_.getLocalizedString("email","Tölvupóstur"));
    text20.setStyle("bodytext");
    text20.setBold();
    Text text21 = (Text)textTemplate.clone();
    text21.setText(iwrb_.getLocalizedString("info","Aðrar upplýsingar"));
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
      t.setWidth(1,"250");
      t.setColumnVerticalAlignment(1,"top");
      t.setColumnVerticalAlignment(2,"top");
      t.setCellpadding(5);

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

  /**
   *
   */
  public String getBundleIdentifier() {
    return(IW_RESOURCE_BUNDLE);
  }

  /*
   *
   */
  private void checkAparmentTypesSelected(ModuleInfo modinfo) {
    String key1 = (String)modinfo.getParameter("aprtType");
    String key2 = (String)modinfo.getParameter("aprtType2");
    String key3 = (String)modinfo.getParameter("aprtType3");

    try {
      int type = ApartmentTypeComplexHelper.getPartKey(key1,1);
      ApartmentType room = new ApartmentType(type);
      pic1 = room.getFloorPlanId();

      if ((key2 != null) && (!key2.equalsIgnoreCase("-1"))) {
        type = ApartmentTypeComplexHelper.getPartKey(key2,1);
        room = new ApartmentType(type);
        pic2 = room.getFloorPlanId();
      }

      if ((key3 != null) && (!key3.equalsIgnoreCase("-1"))) {
        type = ApartmentTypeComplexHelper.getPartKey(key3,1);
        room = new ApartmentType(type);
        pic3 = room.getFloorPlanId();
      }
    }
    catch(SQLException e) {
      e.printStackTrace();
    }
  }

  public void main(ModuleInfo modinfo){
    System.out.println("Main fall i CampusApplicationForm");
        iwrb_ = getResourceBundle(modinfo);
    if (iwrb_ == null)
      System.out.println("bundle null í CampusApplicationForm");
        control(modinfo);
      }
}
