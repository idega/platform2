package com.idega.block.staff.presentation;

import com.idega.presentation.Table;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Text;
import com.idega.block.staff.business.StaffBusiness;
import com.idega.block.staff.data.StaffMetaData;
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

public class StaffMetaTab extends UserTab{

  private TextInput attribute1;
  private TextInput attribute2;
  private TextInput attribute3;
  private TextInput attribute4;
  private TextInput attribute5;
  private TextInput attribute6;

  private TextArea value1;
  private TextArea value2;
  private TextArea value3;
  private TextArea value4;
  private TextArea value5;
  private TextArea value6;

  private String attribute1FieldName;
  private String attribute2FieldName;
  private String attribute3FieldName;
  private String attribute4FieldName;
  private String attribute5FieldName;
  private String attribute6FieldName;

  private String value1FieldName;
  private String value2FieldName;
  private String value3FieldName;
  private String value4FieldName;
  private String value5FieldName;
  private String value6FieldName;

  public StaffMetaTab() {
    super();
    this.setName("Other");
  }

  public StaffMetaTab(int userId){
    this();
    this.setUserID(userId);
  }

  public void initializeFieldNames(){
    attribute1FieldName = "attribute0";
    attribute2FieldName = "attribute1";
    attribute3FieldName = "attribute2";
    attribute4FieldName = "attribute3";
    attribute5FieldName = "attribute4";
    attribute6FieldName = "attribute5";

    value1FieldName = "value0";
    value2FieldName = "value1";
    value3FieldName = "value2";
    value4FieldName = "value3";
    value5FieldName = "value4";
    value6FieldName = "value5";
  }

  public void initializeFieldValues(){
    fieldValues.put(this.attribute1FieldName,"");
    fieldValues.put(this.attribute2FieldName,"");
    fieldValues.put(this.attribute3FieldName,"");
    fieldValues.put(this.attribute4FieldName,"");
    fieldValues.put(this.attribute5FieldName,"");
    fieldValues.put(this.attribute6FieldName,"");

    fieldValues.put(this.value1FieldName,"");
    fieldValues.put(this.value2FieldName,"");
    fieldValues.put(this.value3FieldName,"");
    fieldValues.put(this.value4FieldName,"");
    fieldValues.put(this.value5FieldName,"");
    fieldValues.put(this.value6FieldName,"");

    this.updateFieldsDisplayStatus();
  }

  public void updateFieldsDisplayStatus(){
    attribute1.setContent((String)fieldValues.get(this.attribute1FieldName));
    attribute2.setContent((String)fieldValues.get(this.attribute2FieldName));
    attribute3.setContent((String)fieldValues.get(this.attribute3FieldName));
    attribute4.setContent((String)fieldValues.get(this.attribute4FieldName));
    attribute5.setContent((String)fieldValues.get(this.attribute5FieldName));
    attribute6.setContent((String)fieldValues.get(this.attribute6FieldName));

    value1.setContent((String)fieldValues.get(this.value1FieldName));
    value2.setContent((String)fieldValues.get(this.value2FieldName));
    value3.setContent((String)fieldValues.get(this.value3FieldName));
    value4.setContent((String)fieldValues.get(this.value4FieldName));
    value5.setContent((String)fieldValues.get(this.value5FieldName));
    value6.setContent((String)fieldValues.get(this.value6FieldName));
  }

  public void initializeFields(){
    attribute1 = new TextInput(attribute1FieldName);
    attribute2 = new TextInput(attribute2FieldName);
    attribute3 = new TextInput(attribute3FieldName);
    attribute4 = new TextInput(attribute4FieldName);
    attribute5 = new TextInput(attribute5FieldName);
    attribute6 = new TextInput(attribute6FieldName);

    value1 = new TextArea(value1FieldName);
    value2 = new TextArea(value2FieldName);
    value3 = new TextArea(value3FieldName);
    value4 = new TextArea(value4FieldName);
    value5 = new TextArea(value5FieldName);
    value6 = new TextArea(value6FieldName);
  }

  public void initializeTexts(){
  }


  public void lineUpFields(){
    this.resize(1,1);

    Table metaTable = new Table(2,6);
      metaTable.setColumnVerticalAlignment(1,"top");

    metaTable.setWidth("100%");
    metaTable.setCellpadding(0);
    metaTable.setCellspacing(2);

    metaTable.add(attribute1,1,1);
    metaTable.add(value1,2,1);

    metaTable.add(attribute2,1,2);
    metaTable.add(value2,2,2);

    metaTable.add(attribute3,1,3);
    metaTable.add(value3,2,3);

    metaTable.add(attribute4,1,4);
    metaTable.add(value4,2,4);

    metaTable.add(attribute5,1,5);
    metaTable.add(value5,2,5);

    metaTable.add(attribute6,1,6);
    metaTable.add(value6,2,6);

    add(metaTable,1,1);
  }


  public boolean collect(IWContext iwc){
    if(iwc != null){
      String attribute1 = iwc.getParameter(this.attribute1FieldName);
      if(attribute1 != null){
        fieldValues.put(this.attribute1FieldName,attribute1);
      }

      String attribute2 = iwc.getParameter(this.attribute2FieldName);
      if(attribute2 != null){
        fieldValues.put(this.attribute2FieldName,attribute2);
      }

      String attribute3 = iwc.getParameter(this.attribute3FieldName);
      if(attribute3 != null){
        fieldValues.put(this.attribute3FieldName,attribute3);
      }

      String attribute4 = iwc.getParameter(this.attribute4FieldName);
      if(attribute4 != null){
        fieldValues.put(this.attribute4FieldName,attribute4);
      }

      String attribute5 = iwc.getParameter(this.attribute5FieldName);
      if(attribute5 != null){
        fieldValues.put(this.attribute5FieldName,attribute5);
      }

      String attribute6 = iwc.getParameter(this.attribute6FieldName);
      if(attribute6 != null){
        fieldValues.put(this.attribute6FieldName,attribute6);
      }

      String value1 = iwc.getParameter(this.value1FieldName);
      if(value1 != null){
        fieldValues.put(this.value1FieldName,value1);
      }

      String value2 = iwc.getParameter(this.value2FieldName);
      if(value2 != null){
        fieldValues.put(this.value2FieldName,value2);
      }

      String value3 = iwc.getParameter(this.value3FieldName);
      if(value3 != null){
        fieldValues.put(this.value3FieldName,value3);
      }

      String value4 = iwc.getParameter(this.value4FieldName);
      if(value4 != null){
        fieldValues.put(this.value4FieldName,value4);
      }

      String value5 = iwc.getParameter(this.value5FieldName);
      if(value5 != null){
        fieldValues.put(this.value5FieldName,value5);
      }

      String value6 = iwc.getParameter(this.value6FieldName);
      if(value6 != null){
        fieldValues.put(this.value6FieldName,value6);
      }

      return true;
    }
    return false;
  }

  public boolean store(IWContext iwc){
    try{
      if(getUserId() > -1){
        StaffBusiness business = new StaffBusiness();
        business.updateMetaData(getUserId(),(String)fieldValues.get(this.attribute1FieldName),(String)fieldValues.get(this.value1FieldName)
                                          ,(String)fieldValues.get(this.attribute2FieldName),(String)fieldValues.get(this.value2FieldName)
                                          ,(String)fieldValues.get(this.attribute3FieldName),(String)fieldValues.get(this.value3FieldName)
                                          ,(String)fieldValues.get(this.attribute4FieldName),(String)fieldValues.get(this.value4FieldName)
                                          ,(String)fieldValues.get(this.attribute5FieldName),(String)fieldValues.get(this.value5FieldName)
                                          ,(String)fieldValues.get(this.attribute6FieldName),(String)fieldValues.get(this.value6FieldName));
      }
      this.updateFieldsDisplayStatus();
    }
    catch(Exception e){
      e.printStackTrace(System.err);
      throw new RuntimeException("update user exception");
    }
    return true;
  }


  public void initFieldContents(){

    try{
      StaffMetaData[] staffMetaData = (StaffMetaData[]) StaffMetaData.getStaticInstance(StaffMetaData.class).findAllByColumn(StaffMetaData.getColumnNameUserID(),Integer.toString(getUserId()),"=");

      for ( int a = 0; a < staffMetaData.length; a++ ) {
        fieldValues.put("attribute"+Integer.toString(a),(staffMetaData[a].getAttribute() != null) ? staffMetaData[a].getAttribute():"" );
        fieldValues.put("value"+Integer.toString(a),(staffMetaData[a].getValue() != null) ? staffMetaData[a].getValue():"" );
      }


      this.updateFieldsDisplayStatus();
    }
    catch(Exception e){
      System.err.println("StaffMetaTab error initFieldContents, userId : " + getUserId());
    }


  }


} // Class StaffInfoTab