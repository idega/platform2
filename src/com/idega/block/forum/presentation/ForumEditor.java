package com.idega.block.forum.presentation;

import com.idega.builder.presentation.IBAdminWindow;

import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.Table;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Text;
import com.idega.core.user.data.User;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.block.forum.business.ForumService;

/**
 * Title:        ForumBlock
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public class ForumEditor extends IWAdminWindow {

  public final static String _PARMETERSTRING_ICOBJECT_INSTANCE_ID = "ic_object_instance_id";


  private Text groupNameText;
  private Text descriptionText;

  private TextInput groupNameField;
  private TextArea descriptionField;

  private SubmitButton okButton;
  private SubmitButton cancelButton;

  public static String okButtonParameterValue = "ok";
  public static String cancelButtonParameterValue = "cancel";
  public static String submitButtonParameterName = "submit";

  public static String groupNameFieldParameterName = "groupName";
  public static String descriptionFieldParameterName = "description";
  public static String groupTypeFieldParameterName = "group_type";

  private String rowHeight = "37";
  private Form myForm;

  public ForumEditor() {
    super();
    this.setHeight(340);
    this.setWidth(390);
    initializeTexts();
    initializeFields();
  }


   protected void initializeTexts(){

    groupNameText = new Text("Name:");
    descriptionText = new Text("Description : ");
  }

  protected void initializeFields(){
    groupNameField = new TextInput(groupNameFieldParameterName);
    groupNameField.setLength(20);

    descriptionField = new TextArea(descriptionFieldParameterName);
    descriptionField.setHeight(3);
    descriptionField.setWidth(20);

    okButton = new SubmitButton("     OK     ",submitButtonParameterName,okButtonParameterValue);
    cancelButton = new SubmitButton(" Cancel ",submitButtonParameterName,cancelButtonParameterValue);

  }


  public void lineUpElements(IWContext iwc){
    myForm.empty();

    Table frameTable = new Table(1,2);
    frameTable.setAlignment("center");
    frameTable.setVerticalAlignment("middle");
    frameTable.setCellpadding(0);
    frameTable.setCellspacing(0);

    // nameTable begin
    Table nameTable = new Table(1,4);
    nameTable.setCellpadding(0);
    nameTable.setCellspacing(0);
    nameTable.setHeight(1,rowHeight);
    nameTable.setHeight(2,rowHeight);

    nameTable.add(groupNameText,1,1);
    nameTable.add(groupNameField,1,2);
    nameTable.add(descriptionText,1,3);
    nameTable.add(descriptionField,1,4);
    // nameTable end


    // buttonTable begin
    Table buttonTable = new Table(3,1);
    buttonTable.setCellpadding(0);
    buttonTable.setCellspacing(0);
    buttonTable.setHeight(1,rowHeight);
    buttonTable.setWidth(2,"5");

    buttonTable.add(okButton,1,1);
    buttonTable.add(cancelButton,3,1);
    // buttonTable end


    frameTable.add(nameTable,1,1);


    frameTable.add(buttonTable,1,2);
    frameTable.setAlignment(1,2,"right");

    myForm.add(frameTable);

  }


   public void commitCreation(IWContext iwc) throws Exception{

    String name = iwc.getParameter(this.groupNameFieldParameterName);
    String description = iwc.getParameter(this.descriptionFieldParameterName);

    if(name != null && description != null){
      ForumService service = new ForumService();
      service.createForum((name.equals(""))?"Untitled":name,description);
    }
  }


  public void main(IWContext iwc) throws Exception {
    super.main(iwc);
    if(myForm == null){
      myForm = new Form();
      this.add(myForm);
    }
    String submit = iwc.getParameter("submit");
    if(submit != null){
      if(submit.equals("ok")){
        this.commitCreation(iwc);
        this.close();
        this.setParentToReload();
      }else if(submit.equals("cancel")){
        this.close();
      }
    }
    lineUpElements(iwc);
  }



}
