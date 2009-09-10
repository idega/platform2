package com.idega.block.staff.presentation;

import com.idega.block.staff.business.StaffBusiness;
import com.idega.block.staff.data.StaffMetaData;
import com.idega.core.user.presentation.UserTab;
import com.idega.data.GenericEntity;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;


/**
 * Title:        User
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <a href="mailto:gummi@idega.is">Gu�mundur �g�st S�mundsson</a>
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
    this.attribute1FieldName = "attribute0";
    this.attribute2FieldName = "attribute1";
    this.attribute3FieldName = "attribute2";
    this.attribute4FieldName = "attribute3";
    this.attribute5FieldName = "attribute4";
    this.attribute6FieldName = "attribute5";

    this.value1FieldName = "value0";
    this.value2FieldName = "value1";
    this.value3FieldName = "value2";
    this.value4FieldName = "value3";
    this.value5FieldName = "value4";
    this.value6FieldName = "value5";
  }

  public void initializeFieldValues(){
    this.fieldValues.put(this.attribute1FieldName,"");
    this.fieldValues.put(this.attribute2FieldName,"");
    this.fieldValues.put(this.attribute3FieldName,"");
    this.fieldValues.put(this.attribute4FieldName,"");
    this.fieldValues.put(this.attribute5FieldName,"");
    this.fieldValues.put(this.attribute6FieldName,"");

    this.fieldValues.put(this.value1FieldName,"");
    this.fieldValues.put(this.value2FieldName,"");
    this.fieldValues.put(this.value3FieldName,"");
    this.fieldValues.put(this.value4FieldName,"");
    this.fieldValues.put(this.value5FieldName,"");
    this.fieldValues.put(this.value6FieldName,"");

    this.updateFieldsDisplayStatus();
  }

  public void updateFieldsDisplayStatus(){
    this.attribute1.setContent((String)this.fieldValues.get(this.attribute1FieldName));
    this.attribute2.setContent((String)this.fieldValues.get(this.attribute2FieldName));
    this.attribute3.setContent((String)this.fieldValues.get(this.attribute3FieldName));
    this.attribute4.setContent((String)this.fieldValues.get(this.attribute4FieldName));
    this.attribute5.setContent((String)this.fieldValues.get(this.attribute5FieldName));
    this.attribute6.setContent((String)this.fieldValues.get(this.attribute6FieldName));

    this.value1.setContent((String)this.fieldValues.get(this.value1FieldName));
    this.value2.setContent((String)this.fieldValues.get(this.value2FieldName));
    this.value3.setContent((String)this.fieldValues.get(this.value3FieldName));
    this.value4.setContent((String)this.fieldValues.get(this.value4FieldName));
    this.value5.setContent((String)this.fieldValues.get(this.value5FieldName));
    this.value6.setContent((String)this.fieldValues.get(this.value6FieldName));
  }

  public void initializeFields(){
    this.attribute1 = new TextInput(this.attribute1FieldName);
    this.attribute2 = new TextInput(this.attribute2FieldName);
    this.attribute3 = new TextInput(this.attribute3FieldName);
    this.attribute4 = new TextInput(this.attribute4FieldName);
    this.attribute5 = new TextInput(this.attribute5FieldName);
    this.attribute6 = new TextInput(this.attribute6FieldName);

    this.value1 = new TextArea(this.value1FieldName);
    this.value2 = new TextArea(this.value2FieldName);
    this.value3 = new TextArea(this.value3FieldName);
    this.value4 = new TextArea(this.value4FieldName);
    this.value5 = new TextArea(this.value5FieldName);
    this.value6 = new TextArea(this.value6FieldName);
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

    metaTable.add(this.attribute1,1,1);
    metaTable.add(this.value1,2,1);

    metaTable.add(this.attribute2,1,2);
    metaTable.add(this.value2,2,2);

    metaTable.add(this.attribute3,1,3);
    metaTable.add(this.value3,2,3);

    metaTable.add(this.attribute4,1,4);
    metaTable.add(this.value4,2,4);

    metaTable.add(this.attribute5,1,5);
    metaTable.add(this.value5,2,5);

    metaTable.add(this.attribute6,1,6);
    metaTable.add(this.value6,2,6);

    add(metaTable,1,1);
  }


  public boolean collect(IWContext iwc){
    if(iwc != null){
      String attribute1 = iwc.getParameter(this.attribute1FieldName);
      if(attribute1 != null){
        this.fieldValues.put(this.attribute1FieldName,attribute1);
      }

      String attribute2 = iwc.getParameter(this.attribute2FieldName);
      if(attribute2 != null){
        this.fieldValues.put(this.attribute2FieldName,attribute2);
      }

      String attribute3 = iwc.getParameter(this.attribute3FieldName);
      if(attribute3 != null){
        this.fieldValues.put(this.attribute3FieldName,attribute3);
      }

      String attribute4 = iwc.getParameter(this.attribute4FieldName);
      if(attribute4 != null){
        this.fieldValues.put(this.attribute4FieldName,attribute4);
      }

      String attribute5 = iwc.getParameter(this.attribute5FieldName);
      if(attribute5 != null){
        this.fieldValues.put(this.attribute5FieldName,attribute5);
      }

      String attribute6 = iwc.getParameter(this.attribute6FieldName);
      if(attribute6 != null){
        this.fieldValues.put(this.attribute6FieldName,attribute6);
      }

      String value1 = iwc.getParameter(this.value1FieldName);
      if(value1 != null){
        this.fieldValues.put(this.value1FieldName,value1);
      }

      String value2 = iwc.getParameter(this.value2FieldName);
      if(value2 != null){
        this.fieldValues.put(this.value2FieldName,value2);
      }

      String value3 = iwc.getParameter(this.value3FieldName);
      if(value3 != null){
        this.fieldValues.put(this.value3FieldName,value3);
      }

      String value4 = iwc.getParameter(this.value4FieldName);
      if(value4 != null){
        this.fieldValues.put(this.value4FieldName,value4);
      }

      String value5 = iwc.getParameter(this.value5FieldName);
      if(value5 != null){
        this.fieldValues.put(this.value5FieldName,value5);
      }

      String value6 = iwc.getParameter(this.value6FieldName);
      if(value6 != null){
        this.fieldValues.put(this.value6FieldName,value6);
      }

      return true;
    }
    return false;
  }

  public boolean store(IWContext iwc){
    try{
      if(getUserId() > -1){
        StaffBusiness business = new StaffBusiness();
        StaffBusiness.updateMetaData(getUserId(),(String)this.fieldValues.get(this.attribute1FieldName),(String)this.fieldValues.get(this.value1FieldName)
                                          ,(String)this.fieldValues.get(this.attribute2FieldName),(String)this.fieldValues.get(this.value2FieldName)
                                          ,(String)this.fieldValues.get(this.attribute3FieldName),(String)this.fieldValues.get(this.value3FieldName)
                                          ,(String)this.fieldValues.get(this.attribute4FieldName),(String)this.fieldValues.get(this.value4FieldName)
                                          ,(String)this.fieldValues.get(this.attribute5FieldName),(String)this.fieldValues.get(this.value5FieldName)
                                          ,(String)this.fieldValues.get(this.attribute6FieldName),(String)this.fieldValues.get(this.value6FieldName));
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
      StaffMetaData[] staffMetaData = (StaffMetaData[]) GenericEntity.getStaticInstance(StaffMetaData.class).findAllByColumn(com.idega.block.staff.data.StaffMetaDataBMPBean.getColumnNameUserID(),Integer.toString(getUserId()),"=");

      for ( int a = 0; a < staffMetaData.length; a++ ) {
        this.fieldValues.put("attribute"+Integer.toString(a),(staffMetaData[a].getAttribute() != null) ? staffMetaData[a].getAttribute():"" );
        this.fieldValues.put("value"+Integer.toString(a),(staffMetaData[a].getValue() != null) ? staffMetaData[a].getValue():"" );
      }


      this.updateFieldsDisplayStatus();
    }
    catch(Exception e){
      System.err.println("StaffMetaTab error initFieldContents, userId : " + getUserId());
    }


  }


} // Class StaffInfoTab
