package com.idega.jmodule.forum.presentation;

import com.idega.jmodule.forum.business.*;
import com.idega.jmodule.forum.data.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.textObject.*;
import java.sql.*;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public abstract class ForumAdminPresentation extends JModuleAdminWindow {


  protected ForumDBWriter DBWriter;
  private ForumList myList;
  protected SomeThreads Some;
  protected ThreadContents[] Contents;
  protected ThreadContents theThread;
  protected ThreadEntry Entry;
  protected ThreadTree Tree;
  protected String UserName;
  protected int UserID;
//  protected ForumAdministration Admin;
  private int AttributeID;
  private String AttributeName;
  private ForumAdmin Settings;

  private ForumService service;
  private ForumEmailHandler myEmailHandler;

  protected boolean UseForumList;

  public ModuleInfo modinfo;

  protected boolean isUser;

  protected ModuleObject SomeThreadsModule;
  protected Form ForumEditForm;
  protected int currentState;
  private String currentForumID;
  private String currentForum;

  private int lastSaved;

  protected boolean UseForums;
  private boolean UseUserRegistration;
  private boolean UseLogin;
  private boolean UseNameField;
  private boolean saveThread;

  protected boolean firstTree;

  private String language;

  private Table FrameTable;




  public ForumAdminPresentation() throws Exception{
    super();
    this.setName("Spjallþráðastjóri");
    initFrameTable();
    InitPresentation();
    myEmailHandler = new ForumEmailHandler();
  }


  // init methods

  public void initFrameTable(){
    FrameTable = new Table();
    super.add(FrameTable);
  }

 public void initBooleans(){
    setUseForums(true);
    setUseUserRegistration(false);
    setUseLogin(true);
    setUseNameField(true);
  }

  public void updateSettings()throws SQLException{
        Settings = service.getForumSettings(AttributeID, AttributeName);
        if (Settings != null){
          setUseForums(Settings.getUseForums());
        }else{
          setUseForums(false);
        }
 }


  public void InitPresentation()throws Exception{
    DBWriter =  new ForumDBWriter();
    this.initList();
    this.initAdmin();
    }




  // ## InitFöll sem má overwrite-a ef smíða á hlutinn eitthvað öðruvísi í framtíðinni ##

  public void initList() throws SQLException{
    if (myList == null)
      myList = new ForumList();
  }

  public void initTree(ModuleInfo modinfo) throws Exception{
    if((AttributeName != null) && (modinfo.getParameter("forum_id") == null)){
        int forum_id = service.getDefaultAttributeForumID(AttributeID, AttributeName);
        Tree = new ThreadTree(forum_id);
    }else{
        Tree = new ThreadTree(modinfo);
    }
  }

  public void initAdmin() throws SQLException{
    //Admin = new FourmAdministation();
  }

  public void initEntry() throws SQLException{
    Entry = new ThreadEntry(modinfo);
  }





  public ForumList getForumList() throws SQLException{
    if (myList == null)
      initList();
    return myList;
  }





 // ## stýriföll ##

  public int getCurrentState(){
    return currentState;
  }

  protected void addProperState() throws Exception{
    if (modinfo.getRequest().getParameter("state") == null){
      State1();
    } else {

      if (!(modinfo.getRequest().getParameter("state").equals("same"))){
        currentState = Integer.parseInt(modinfo.getRequest().getParameter("state"));
      }


      switch (currentState){
        case 1 :
          State1();
          break;
        case 2 :
          State2();
          break;
        case 3 :
          State3();
          break;
        case 4 :
          State4();
          break;
        case 5 :
          State5();
          break;
        case 6 :
          State6();
          break;
        default:
          Link myLink = new Link("Mistókst");  // CloseWindowButton
          myLink.addParameter("state", "3");
          add(myLink);
          add(getModuleInfo().getParameter("state"));
          // throw error
          break;
      }
    }
  }


  protected void State1() throws Exception{
    if (ForumEditForm == null)
      initializeForm();

    ForumEditForm.add(ForumEdit_Presentation());
    add(ForumEditForm);
  }

  protected void State2()throws Exception{
    if (ForumEditForm == null)
      initializeForm();

    ForumEditForm.add(ForumEmail_Presentation());
    add(ForumEditForm);
  }

  protected void State3() throws Exception{
  }

  protected void State4() throws Exception{
  }

  protected void State5() throws Exception{
  }

  protected synchronized void State6() throws Exception{
    add("Save");

    if ( getModuleInfo().getParameter("from") != null){
      if ( getModuleInfo().getParameter("from").equals("FLEdit") ){
        if (getModuleInfo().getParameter("action").equals("forum_email")){
            myEmailHandler.addAddressToForumPostlist(modinfo.getParameter("forum_email"),Integer.parseInt(modinfo.getParameter("forum_id")));
        }else{
          DBWriter.saveForum(modinfo, ForumPresentation.getForumAttributeName(modinfo), ForumPresentation.getForumAttributeValue(modinfo));
        }
      this.setParentToReload();
      this.close();
      }
    }

  }

  protected void State7(){
  }

  protected void State8(){
  }


  protected void initializeForm(){
    ForumEditForm = new Form();
    if (this.getModuleInfo().getParameter("forum_action") != null)
      ForumEditForm.add( new HiddenInput("forum_action", this.getModuleInfo().getParameter("forum_action")));

    ForumEditForm.add(new HiddenInput("state", "6"));
    ForumEditForm.add(new HiddenInput("from", "FLEdit"));
  }





  //Overwritten methods begin

  public void add(ModuleObject obj){
    if (FrameTable == null)
      initFrameTable();

    FrameTable.add(obj);
  }

  public void add(String text){
    FrameTable.add(text);
  }

  public void empty(){
    FrameTable.empty();
  }

  //Overwritten methods end



  public boolean getUseForums(){
    return UseForums;
  }

  public boolean getUseUserRegistration(){
    return UseUserRegistration;
  }

  public boolean getUseLogin(){
    return UseLogin;
  }

  public boolean getUseNameField(){
    return UseNameField;
  }


  public void setUseForums(boolean bool){
    this.UseForums = bool;
  }

  public void setUseUserRegistration(boolean bool){
    this.UseUserRegistration = bool;
  }

  public void setUseLogin(boolean bool){
    this.UseLogin = bool;
  }

  public void setUseNameField(boolean bool){
    this.UseNameField = bool;
  }





  public void checkSettings()throws Exception{
    super.checkSettings();
  }

  public ModuleInfo getModuleInfo(){
    return this.modinfo;
  }

  /**
   * initialized in the mainfunction.
   */

  public String getSpokenLanguage(){
    return language;
  }

  /**
   *
   */
  public void initAgain()throws Exception{
    updateSettings();
    initList();
    initTree(modinfo);
  }

  /**
   *
   */
  public void preset(){

  }

  public void setConnectionAttributes(String attribute_name, int attribute_id ){
    AttributeName = attribute_name;
    AttributeID = attribute_id;
  }



  public void main(ModuleInfo modinfo) throws Exception {
    this.modinfo = modinfo;
    if (AttributeName != null){
      String attributeTemp = modinfo.getRequest().getParameter(AttributeName);
      if(attributeTemp != null){
        int attribute = Integer.parseInt(attributeTemp);
        if(attribute != AttributeID){
          AttributeID = attribute;
          initAgain();
        }
      }
    }
    language = modinfo.getSpokenLanguage();
    if(language == null){
      language = "IS";
    }
//    this.checkSettings();
//    this.preset();
    this.empty();
//    super.empty();
    super.setTitle("Spjallþráðastrjór");
    this.addProperState();
  }


  abstract public ModuleObject ForumEdit_Presentation() throws SQLException;
  abstract public ModuleObject ForumEmail_Presentation() throws SQLException;



} // Class ForumAdminPresentation