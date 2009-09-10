package com.idega.block.staff.presentation;

import java.util.StringTokenizer;

import com.idega.block.staff.business.StaffBusiness;
import com.idega.block.staff.data.StaffInfo;
import com.idega.core.user.presentation.UserTab;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWTimestamp;


/**
 * Title:        User
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <a href="mailto:gummi@idega.is">Gu�mundur �g�st S�mundsson</a>
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
    this.titleFieldName = "STtitle";
    this.educationFieldName = "STeducation";
    this.schoolFieldName = "STschool";
    this.areaFieldName = "STarea";
    this.beganWorkFieldName = "STbwork";
  }

  public void initializeFieldValues(){
    this.fieldValues.put(this.titleFieldName,"");
    this.fieldValues.put(this.educationFieldName,"");
    this.fieldValues.put(this.schoolFieldName,"");
    this.fieldValues.put(this.areaFieldName,"");
    this.fieldValues.put(this.beganWorkFieldName,"");

    this.updateFieldsDisplayStatus();
  }

  public void updateFieldsDisplayStatus(){
    this.titleField.setContent((String)this.fieldValues.get(this.titleFieldName));

    this.educationField.setContent((String)this.fieldValues.get(this.educationFieldName));

    this.schoolField.setContent((String)this.fieldValues.get(this.schoolFieldName));

    this.areaField.setContent((String)this.fieldValues.get(this.areaFieldName));

    StringTokenizer date = new StringTokenizer((String)this.fieldValues.get(this.beganWorkFieldName)," -");

    if(date.hasMoreTokens()){
      this.beganWorkField.setYear(date.nextToken());
    }
    if(date.hasMoreTokens()){
      this.beganWorkField.setMonth(date.nextToken());
    }
    if(date.hasMoreTokens()){
      this.beganWorkField.setDay(date.nextToken());
    }
  }


  public void initializeFields(){
    this.titleField = new TextInput(this.titleFieldName);
    this.titleField.setLength(24);

    this.educationField = new TextArea(this.educationFieldName);
    this.educationField.setHeight(4);
    this.educationField.setWidth(42);
    this.educationField.setWrap(true);

    this.schoolField = new TextArea(this.schoolFieldName);
    this.schoolField.setHeight(4);
    this.schoolField.setWidth(42);
    this.schoolField.setWrap(true);

    this.areaField = new TextArea(this.areaFieldName);
    this.areaField.setHeight(4);
    this.areaField.setWidth(42);
    this.areaField.setWrap(true);

    this.beganWorkField = new DateInput(this.beganWorkFieldName);
    IWTimestamp time = IWTimestamp.RightNow();
    this.beganWorkField.setYearRange(time.getYear(),time.getYear()-100);
  }

  public void initializeTexts(){
    this.titleText = getTextObject();
    this.titleText.setText("Title");

    this.educationText = getTextObject();
    this.educationText.setText("Education:");

    this.schoolText = getTextObject();
    this.schoolText.setText("School:");

    this.areaText = getTextObject();
    this.areaText.setText("Area:");

    this.beganWorkText = getTextObject();
    this.beganWorkText.setText("Began work: ");

  }


  public void lineUpFields(){
    this.resize(1,1);

    String rowHeight2 = Integer.toString(Integer.parseInt(this.rowHeight) + 67);

    Table staffTable = new Table(2,5);
    staffTable.setWidth("100%");
    staffTable.setCellpadding(0);
    staffTable.setCellspacing(0);
    staffTable.mergeCells(1,3,2,3);
    staffTable.mergeCells(1,4,2,4);
    staffTable.mergeCells(1,5,2,5);
    staffTable.setHeight(1,this.rowHeight);
    staffTable.setHeight(2,this.rowHeight);
    staffTable.setHeight(3,rowHeight2);
    staffTable.setHeight(4,rowHeight2);
    staffTable.setHeight(5,rowHeight2);

    staffTable.add(this.titleText,1,1);
    staffTable.add(this.titleField,2,1);
    staffTable.add(this.beganWorkText,1,2);
    staffTable.add(this.beganWorkField,2,2);
    staffTable.add(this.educationText,1,3);
    staffTable.add(Text.getBreak(),1,3);
    staffTable.add(this.educationField,1,3);
    staffTable.add(this.schoolText,1,4);
    staffTable.add(Text.getBreak(),1,4);
    staffTable.add(this.schoolField,1,4);
    staffTable.add(this.areaText,1,5);
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
        this.fieldValues.put(this.titleFieldName,title);
      }
      if(education != null){
        this.fieldValues.put(this.educationFieldName,education);
      }
      if(school != null){
        this.fieldValues.put(this.schoolFieldName,school);
      }
      if(area != null){
        this.fieldValues.put(this.areaFieldName,area);
      }
      if(beganWork != null){
        this.fieldValues.put(this.beganWorkFieldName,beganWork);
      }

      this.updateFieldsDisplayStatus();

      return true;
    }
    return false;
  }

  public boolean store(IWContext iwc){
    try{
      if(getUserId() > -1){
        IWTimestamp beganWorkTS = null;
        String st = (String)this.fieldValues.get(this.beganWorkFieldName);
        if( st != null && !st.equals("")){
          beganWorkTS = new IWTimestamp(st);
        }
        StaffBusiness.updateStaff(getUserId(),(String)this.fieldValues.get(this.titleFieldName),
                            (String)this.fieldValues.get(this.educationFieldName),(String)this.fieldValues.get(this.schoolFieldName),
                            (String)this.fieldValues.get(this.areaFieldName),beganWorkTS);
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

      this.fieldValues.put(this.titleFieldName,(staffInfo.getTitle() != null) ? staffInfo.getTitle():"" );
      this.fieldValues.put(this.educationFieldName,(staffInfo.getEducation() != null) ? staffInfo.getEducation():"" );
      this.fieldValues.put(this.schoolFieldName,(staffInfo.getSchool() != null) ? staffInfo.getSchool():"" );
      this.fieldValues.put(this.areaFieldName,(staffInfo.getArea() != null) ? staffInfo.getArea():"" );
      this.fieldValues.put(this.beganWorkFieldName,(staffInfo.getBeganWork() != null) ? new IWTimestamp(staffInfo.getBeganWork()).toSQLDateString() : "");
      this.updateFieldsDisplayStatus();

    }
    catch(Exception e){
      System.err.println("StaffInfoTab error initFieldContents, userId : " + getUserId());
    }


  }


} // Class StaffInfoTab
