package com.idega.block.staff.presentation;

import com.idega.presentation.Table;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Text;
import com.idega.block.staff.business.StaffBusiness;
import com.idega.block.staff.data.StaffInfo;
import com.idega.util.idegaTimestamp;
import java.util.Hashtable;
import java.util.StringTokenizer;
import com.idega.core.user.presentation.UserTab;


/**
 * Title:        User
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public class StaffInfoTab extends UserTab{

  private TextInput titleField;
  private TextArea educationField;
  private TextArea schoolField;
  private TextArea areaField;
  private DateInput beganWorkField;

  private String titleFieldName;
  private String educationFieldName;
  private String schoolFieldName;
  private String areaFieldName;
  private String beganWorkFieldName;

  private Text titleText;
  private Text educationText;
  private Text schoolText;
  private Text areaText;
  private Text beganWorkText;

  public StaffInfoTab() {
    super();
    this.setName("Staff");
  }

  public StaffInfoTab(int userId){
    this();
    this.setUserID(userId);
  }

  public void initializeFieldNames(){
    titleFieldName = "STtitle";
    educationFieldName = "STeducation";
    schoolFieldName = "STschool";
    areaFieldName = "STarea";
    beganWorkFieldName = "STbwork";
  }

  public void initializeFieldValues(){
    fieldValues.put(this.titleFieldName,"");
    fieldValues.put(this.educationFieldName,"");
    fieldValues.put(this.schoolFieldName,"");
    fieldValues.put(this.areaFieldName,"");
    fieldValues.put(this.beganWorkFieldName,"");

    this.updateFieldsDisplayStatus();
  }

  public void updateFieldsDisplayStatus(){
    titleField.setContent((String)fieldValues.get(this.titleFieldName));

    educationField.setContent((String)fieldValues.get(this.educationFieldName));

    schoolField.setContent((String)fieldValues.get(this.schoolFieldName));

    areaField.setContent((String)fieldValues.get(this.areaFieldName));

    StringTokenizer date = new StringTokenizer((String)fieldValues.get(this.beganWorkFieldName)," -");

    if(date.hasMoreTokens()){
      beganWorkField.setYear(date.nextToken());
    }
    if(date.hasMoreTokens()){
      beganWorkField.setMonth(date.nextToken());
    }
    if(date.hasMoreTokens()){
      beganWorkField.setDay(date.nextToken());
    }
  }


  public void initializeFields(){
    titleField = new TextInput(titleFieldName);
    titleField.setLength(24);

    educationField = new TextArea(educationFieldName);
    educationField.setHeight(4);
    educationField.setWidth(42);
    educationField.setWrap(true);

    schoolField = new TextArea(schoolFieldName);
    schoolField.setHeight(4);
    schoolField.setWidth(42);
    schoolField.setWrap(true);

    areaField = new TextArea(areaFieldName);
    areaField.setHeight(4);
    areaField.setWidth(42);
    areaField.setWrap(true);

    beganWorkField = new DateInput(beganWorkFieldName);
    idegaTimestamp time = idegaTimestamp.RightNow();
    beganWorkField.setYearRange(time.getYear(),time.getYear()-100);
  }

  public void initializeTexts(){
    titleText = getTextObject();
    titleText.setText("Title");

    educationText = getTextObject();
    educationText.setText("Education:");

    schoolText = getTextObject();
    schoolText.setText("School:");

    areaText = getTextObject();
    areaText.setText("Area:");

    beganWorkText = getTextObject();
    beganWorkText.setText("Began work: ");

  }


  public void lineUpFields(){
    this.resize(1,1);

    String rowHeight2 = Integer.toString(Integer.parseInt(rowHeight) + 67);

    Table staffTable = new Table(2,5);
    staffTable.setWidth("100%");
    staffTable.setCellpadding(0);
    staffTable.setCellspacing(0);
    staffTable.mergeCells(1,3,2,3);
    staffTable.mergeCells(1,4,2,4);
    staffTable.mergeCells(1,5,2,5);
    staffTable.setHeight(1,rowHeight);
    staffTable.setHeight(2,rowHeight);
    staffTable.setHeight(3,rowHeight2);
    staffTable.setHeight(4,rowHeight2);
    staffTable.setHeight(5,rowHeight2);

    staffTable.add(titleText,1,1);
    staffTable.add(this.titleField,2,1);
    staffTable.add(beganWorkText,1,2);
    staffTable.add(this.beganWorkField,2,2);
    staffTable.add(educationText,1,3);
    staffTable.add(Text.getBreak(),1,3);
    staffTable.add(this.educationField,1,3);
    staffTable.add(schoolText,1,4);
    staffTable.add(Text.getBreak(),1,4);
    staffTable.add(this.schoolField,1,4);
    staffTable.add(areaText,1,5);
    staffTable.add(Text.getBreak(),1,5);
    staffTable.add(this.areaField,1,5);
    this.add(staffTable,1,1);
  }


  public boolean collect(IWContext iwc){
    if(iwc != null){

      String title = iwc.getParameter(this.titleFieldName);
      String education = iwc.getParameter(this.educationFieldName);
      String school = iwc.getParameter(this.schoolFieldName);
      String area = iwc.getParameter(this.areaFieldName);
      String beganWork = iwc.getParameter(this.beganWorkFieldName);

      if(title != null){
        fieldValues.put(this.titleFieldName,title);
      }
      if(education != null){
        fieldValues.put(this.educationFieldName,education);
      }
      if(school != null){
        fieldValues.put(this.schoolFieldName,school);
      }
      if(area != null){
        fieldValues.put(this.areaFieldName,area);
      }
      if(beganWork != null){
        fieldValues.put(this.beganWorkFieldName,beganWork);
      }

      this.updateFieldsDisplayStatus();

      return true;
    }
    return false;
  }

  public boolean store(IWContext iwc){
    try{
      if(getUserId() > -1){
        idegaTimestamp beganWorkTS = null;
        String st = (String)fieldValues.get(this.beganWorkFieldName);
        if( st != null && !st.equals("")){
          beganWorkTS = new idegaTimestamp(st);
        }
        StaffBusiness.updateStaff(getUserId(),(String)fieldValues.get(this.titleFieldName),
                            (String)fieldValues.get(this.educationFieldName),(String)fieldValues.get(this.schoolFieldName),
                            (String)fieldValues.get(this.areaFieldName),beganWorkTS);
      }
    }
    catch(Exception e){
      e.printStackTrace(System.err);
      throw new RuntimeException("update user exception");
    }
    return true;
  }


  public void initFieldContents(){

    try{
      StaffInfo staffInfo = ((com.idega.block.staff.data.StaffInfoHome)com.idega.data.IDOLookup.getHomeLegacy(StaffInfo.class)).findByPrimaryKeyLegacy(getUserId());

      fieldValues.put(this.titleFieldName,(staffInfo.getTitle() != null) ? staffInfo.getTitle():"" );
      fieldValues.put(this.educationFieldName,(staffInfo.getEducation() != null) ? staffInfo.getEducation():"" );
      fieldValues.put(this.schoolFieldName,(staffInfo.getSchool() != null) ? staffInfo.getSchool():"" );
      fieldValues.put(this.areaFieldName,(staffInfo.getArea() != null) ? staffInfo.getArea():"" );
      fieldValues.put(this.beganWorkFieldName,(staffInfo.getBeganWork() != null) ? new idegaTimestamp(staffInfo.getBeganWork()).toSQLDateString() : "");
      this.updateFieldsDisplayStatus();

    }
    catch(Exception e){
      System.err.println("StaffInfoTab error initFieldContents, userId : " + getUserId());
    }


  }


} // Class StaffInfoTab
