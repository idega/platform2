package com.idega.block.staff.presentation;

import com.idega.presentation.Table;
import com.idega.block.media.presentation.ImageInserter;
//import com.idega.jmodule.image.presentation.ImageInserter;
//import com.idega.jmodule.image.presentation.ImageEditorWindow;
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

public class StaffImageTab extends UserTab{

  private ImageInserter imageField;

  private String imageFieldName;

  private Text imageText;

  public StaffImageTab() {
    super();
    this.setName("Image");
  }

  public StaffImageTab(int userId){
    this();
    this.setUserID(userId);
  }

  public void initializeFieldNames(){
    imageFieldName = "STimage";
  }

  public void initializeFieldValues(){
    fieldValues.put(this.imageFieldName,"");

    this.updateFieldsDisplayStatus();
  }

  public void updateFieldsDisplayStatus(){
    int imageId = -1;
    try {
      imageId = Integer.parseInt((String)fieldValues.get(this.imageFieldName));
    }
    catch (NumberFormatException ex) {
      imageId = -1;
    }

    if ( imageId != -1 )
      imageField.setImageId(imageId);
  }

  public void initializeFields(){
    imageField = new ImageInserter(imageFieldName);
    imageField.setHasUseBox(false);
  }

  public void initializeTexts(){
    imageText = getTextObject();
    imageText.setText("Image"+":");
  }


  public void lineUpFields(){
    this.resize(1,1);

    Table imageTable = new Table(1,2);
    imageTable.setWidth("100%");
    imageTable.setCellpadding(0);
    imageTable.setCellspacing(0);

    imageTable.add(imageText,1,1);
    imageTable.add(this.imageField,1,2);
    this.add(imageTable,1,1);
  }


  public boolean collect(IWContext iwc){
    if(iwc != null){

      String image = iwc.getParameter(this.imageFieldName);

      if(image != null){
        fieldValues.put(this.imageFieldName,image);
      }

      return true;
    }
    return false;
  }

  public boolean store(IWContext iwc){
    try{
      if(getUserId() > -1){
        StaffBusiness business = new StaffBusiness();
        business.updateImage(getUserId(),(String)fieldValues.get(this.imageFieldName));
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
      StaffInfo staffInfo = new StaffInfo(getUserId());

      fieldValues.put(this.imageFieldName,(staffInfo.getImageID() != -1) ? Integer.toString(staffInfo.getImageID()):"" );
      this.updateFieldsDisplayStatus();
    }
    catch(Exception e){
      System.err.println("StaffImageTab error initFieldContents, userId : " + getUserId());
    }


  }


} // Class StaffInfoTab