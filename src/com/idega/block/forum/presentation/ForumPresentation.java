package com.idega.block.forum.presentation;

import com.idega.block.forum.business.*;
import com.idega.block.forum.data.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.util.text.*;
import java.sql.*;

/**
 * Title:        idegaForums<p>
 * Description:  <p>
 * Copyright:    Copyright (c) idega margmiðlun hf.<p>
 * Company:      idega margmiðlun hf.<p>
 * @author idega 2000 - idega team - <a href="mailto:gummi@idega.is">gummi@idega.is</a>
 * @version 1.0
 */


public abstract class ForumPresentation extends JModuleObject {

  protected ForumDBWriter DBWriter;
  private ForumList myList;
  protected ForumList[] List;
  protected SomeThreads Some;
  protected ThreadContents[] Contents;
  protected ThreadContents theThread;
  protected ThreadEntry Entry;
  protected ThreadTree Tree;
  protected String UserName;
  protected int UserID;
  //protected ForumAdministration Admin;
  //protected ForumError Error;
  private int AttributeID;
  private String AttributeName;
  private ForumAdmin Settings;

  private ForumService service;

  protected boolean UseForumList;

  public ModuleInfo modinfo;

  protected boolean isUser;

  protected ModuleObject SomeThreadsModule;
  protected Form entryForm;
  protected int currentState;
  private String currentForumID;
  private String currentForum;

  private int lastSaved;

  protected boolean UseForums;
  private boolean UseUserRegistration;
  private boolean UseLogin;
  private boolean UseNameField;
  private boolean saveThread;

//  protected final ModuleObject OpenTreeLink = new Text("Opna Þræði");
//  protected final ModuleObject CloseTreeLink = new Text("Loka Þraðum");

  protected boolean firstTree;

  private String language;

  private boolean justSetConnectionAttributes;

  // ## Smiður ## //


  public ForumPresentation() throws SQLException {
    service = new ForumService();
    language = null;
    currentState = 1;
    InitPresentation();
    currentForum = "";
    lastSaved = -7;
    initBooleans();
    currentForumID = null;
    isUser = false;
    firstTree = true;
    saveThread = false;
  }

  public ForumPresentation( int attribute_id, String attribute_name) throws SQLException{
    this();
    AttributeID = attribute_id;
    AttributeName = attribute_name;
    updateSettings();
    if(Settings == null){
      setUseForums(false);
    } else {
      setUseForums(Settings.getUseForums());
    }
  }

  // notaður til að fá uppskift af þvi að finna username og -id og einnig staðinn i sessioninu
  public ForumPresentation(String UserClass, String UserMethod, String Attribute) throws SQLException {
    service = new ForumService();
    currentState = 1;
    InitPresentation();
    currentForum = "";
    lastSaved = -7;
    initBooleans();
    currentForumID = null;
    firstTree = true;
    saveThread = false;
  }

  // notaður til að fá username og userid
  public ForumPresentation(String user_name, int user_id) throws SQLException {
    service = new ForumService();
    UserName = user_name;
    UserID = user_id;
    currentState = 1;
    InitPresentation();
    currentForum = "";
    lastSaved = -7;
    initBooleans();
    currentForumID = null;
    isUser = true;
    firstTree = true;
    saveThread = false;
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


  public void InitPresentation()throws SQLException{
    justSetConnectionAttributes = false;
/*    DBWriter = (ForumDBWriter)modinfo.getRequest().getSession().getAttribute("ForumDBWriter");
    if (DBWriter == null){*/
      DBWriter =  new ForumDBWriter();
/*      modinfo.getRequest().getSession().setAttribute("ForumDBWriter", DBWriter );
    }*/
/*
    Error = (ForumError)modinfo.getRequest().getSession().getAttribute("ForumError");
    if (Error == null){
      Error = new ForumError();
      modinfo.getRequest().getSession().setAttribute("ForumError", Error );
    }
*/
    this.initSomeThreads();
    this.initList();

    this.initAdmin();
    }




  // ## InitFöll sem má overwrite-a ef smíða á hlutinn eitthvað öðruvísi í framtíðinni ##

  public void initList() throws SQLException{
    if (myList == null)
      myList = new ForumList();

    if (AttributeName != null){
      List = myList.getForums(AttributeID, AttributeName);
    }else{
      List = myList.getForums();
    }
  }

  public void initTree(ModuleInfo modinfo) throws Exception{
    if((AttributeName != null) && (modinfo.getRequest().getParameter("forum_id") == null)){
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

  // Ma helst ekki overwrite
  protected void initializeForm(){
    entryForm = new Form();
    if (modinfo.getRequest().getParameter("parent_id") != null)
      entryForm.add(new HiddenInput("parent_id",modinfo.getRequest().getParameter("parent_id")));
    if (modinfo.getRequest().getParameter("forum_id") != null)
      entryForm.add(new HiddenInput("forum_id",modinfo.getRequest().getParameter("forum_id")));

    entryForm.add(new HiddenInput("state", "6"));
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

     if (modinfo.getRequest().getParameter("state").equals("same") && !(currentState == 2 || currentState == 3)){
       currentState = 2;
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
          Link myLink = new Link("Mistókst");
          myLink.addParameter("state", "3");
          add(myLink);
          // throw error
          break;
      }
    }
  }


  protected void State1() throws Exception{
    lastSaved = -7;
    currentState = 1;
    if (!UseForums){
      State2();
    }else{
      initList();
      String action = modinfo.getParameter("action");
      if (action != null && action.equals("insert")){
        add(call_ForumList_ForumCreation());
      }else{
        add(ForumList_Presentation());
        this.initTree(modinfo);
        firstTree = true;
      }
    }
  }

  protected void State2()throws Exception{
    lastSaved =-7;
    currentState = 2;
    updateCurrentForumName();
    if (Tree == null)
       this.initTree(this.modinfo);

    if(UseForums && currentForum != null)
      add(ForumNameHeader());

    add(ThreadTree_Presentation());
    //Tree.main(this.modinfo);
    firstTree = false;
  }

  protected void State3() throws Exception{
    lastSaved = -7;
    currentState = 3;

    String id = modinfo.getRequest().getParameter("forum_thread_id");
    if (id != null){
      theThread = new ThreadContents(Integer.parseInt(id));
      Contents = theThread.getThreads(false);
    }

    if(UseForums && currentForum != null)
      add(ForumNameHeader());

    add(ThreadContents_Presentation());
    if (Tree == null)
       this.initTree(modinfo);
    add(ThreadTree_Presentation());
    //Tree.main(modinfo);
    firstTree = false;
  }

  protected void State3(int fromDBWriter) throws Exception{
      currentState = 3;
      Tree.main(modinfo);
      theThread = new ThreadContents(fromDBWriter);
      Contents = theThread.getThreads(false);

      if(UseForums && currentForum != null)
        add(ForumNameHeader());

      add(ThreadContents_Presentation());
      if (Tree == null)
        this.initTree(modinfo);
      add(ThreadTree_Presentation());
      //Tree.main(modinfo);
      firstTree = false;
  }

  protected void State4() throws Exception{
    currentState = 4;


    if(UseForums && currentForum != null)
      add(ForumNameHeader());
    add(ThreadContents_Presentation());   // open All
  }

  protected void State5() throws Exception{
    currentState = 5;
    if(!isUser && UseLogin){
      State2();   //  temp  - throw error - benda á að skrá sig inn
    } else{
        saveThread = true;
        initializeForm();
        this.initEntry();


        if(UseForums && currentForum != null)
          add(ForumNameHeader());

        String parent = modinfo.getRequest().getParameter("parent_id");
        if(modinfo.getRequest().getParameter("from").equals("ATLink") && (parent != null)){
          theThread = new ThreadContents(Integer.parseInt(parent), false);
          Contents = theThread.getThreads(false);
          add(ThreadContents_Presentation());
        }

        entryForm.add(ThreadEntry_Presentation(UseNameField));
        add(entryForm);
    }
  }

  // synchronized til að koma í veg fyrir að þráður vistist oftar en einu sinni ef oft er ýtt á form takkann
  // saveThread þarf að vera örugglega orðið false þegar næst er reynt  (Tilraun)
  protected synchronized void State6() throws Exception{
    currentState = 6;
    String from = modinfo.getRequest().getParameter("from");
    if (from == null)
      from = "";

    // temp workaround OK boolean
    boolean OK = false;
    if (!UseLogin)
      OK = true;
    else if(isUser)
      OK = true;


    if (OK && saveThread && !from.equals("DELLink")){
      lastSaved = DBWriter.saveThread(modinfo, UserName, UserID);
      State3(lastSaved);
    }else if(!saveThread && !from.equals("DELLink")){
      if (lastSaved != -7)
        State3(lastSaved);
      else
        State2();
    }else if (!from.equals("DELLink")){
      //throw error;
      int i = 1; // temp
      //add("isUser == null");
    }


    if (from.equals("DELLink")){
      int delTemp = DBWriter.delThread(modinfo);
      if (0 < delTemp)
        State3(delTemp);
      else
        State2();
    }

    //  Finna rétta fallið til að save' í grunn og kalla svo a næsta State()
    saveThread = false;
  }

  protected void State7(){
    currentState = 7;
    add(UserRegistration_Presentation());
  }

  protected void State8(){
    currentState = 8;
    add(ForumAdministration_Presentation());
  }


  public ModuleObject getSomeThreads() throws SQLException{
    if (SomeThreadsModule == null)
      SomeThreadsModule = SomeThreads_Presentation();
    Some.initSomeThreads();
    return SomeThreadsModule;
  }

  private void updateCurrentForumName()throws SQLException{
    if(modinfo != null){
      String tempForumID = modinfo.getRequest().getParameter("forum_id");
      if (currentForumID != tempForumID && tempForumID != null){
        currentForumID = tempForumID;
        currentForum = new Forum(Integer.parseInt(currentForumID)).getForumName();
      }
    }
  }

  public String getCurrentForumName(){
    return currentForum;
  }



  public ForumList[] getForumListList(){
    return List;
  }

  public ForumList getForumList(){
    return myList;
  }

  public ThreadContents[] getThreadContents(){
    return Contents;
  }

  public ThreadEntry getThreadEntry(){
    return Entry;
  }

  public ThreadTree getThreadTree(){
    return Tree;
  }


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


  abstract protected ModuleObject ForumList_Presentation();
  abstract protected ModuleObject SomeThreads_Presentation();
  abstract protected ModuleObject ThreadContents_Presentation()throws Exception;
  abstract protected ModuleObject ThreadEntry_Presentation(boolean uesNameField) throws Exception;
  abstract protected ModuleObject ThreadTree_Presentation();
  abstract protected ModuleObject ForumError_Presentation( String errorType );
  abstract protected ModuleObject ForumAdministration_Presentation();
  abstract protected ModuleObject UserRegistration_Presentation();
  /**
   * to construct SomeThreads 'Some'
   */
  abstract protected void initSomeThreads()throws SQLException;
  abstract protected ModuleObject ForumNameHeader();

  /**
   * unimplemented
   */
  public ModuleObject ForumList_ForumCreation(){
    Table myTable = new Table(10,10);
    myTable.setBorder(3);
    myTable.setHorizontalZebraColored("#000000","#FFFFFF");
    return myTable;
  }

  public ModuleObject call_ForumList_ForumCreation(){
/*    Window myWindow = getForumList().getForumCreationWindow();
    myWindow.

    if(hasPermission())..............

*/
    return ForumList_ForumCreation();
  }


  private void findeCurrentForumName()throws SQLException{
    String tempID = modinfo.getRequest().getParameter("forum_id");
    if (tempID != null)
      currentForum = new Forum(Integer.parseInt(tempID)).getForumName();
  }

  private void checkSettings()throws SQLException{
    if (UseForums)
      findeCurrentForumName();
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
    justSetConnectionAttributes = true;
  }

  public static String getForumAttributeName(ModuleInfo modinfo){
    if (modinfo != null && modinfo.getSessionAttribute("forumattributename") != null)
      return (String)modinfo.getSessionAttribute("forumattributename");
    else
      return null;
  }

  public static Integer getForumAttributeValue(ModuleInfo modinfo){
    if (modinfo != null && modinfo.getSessionAttribute("forumattributevalue") != null)
      return (Integer)modinfo.getSessionAttribute("forumattributevalue");
    else
      return null;
  }


  public void main(ModuleInfo modinfo) throws Exception {
    this.modinfo = modinfo;
    if (AttributeName != null){
      String attributeTemp = modinfo.getRequest().getParameter(AttributeName);
      if (justSetConnectionAttributes){
        modinfo.setSessionAttribute("forumattributename", AttributeName);
        modinfo.setSessionAttribute("forumattributevalue", new Integer(AttributeID));
      }
      if(attributeTemp != null){
        int attribute = Integer.parseInt(attributeTemp);
        if(attribute != AttributeID){
          AttributeID = attribute;
          modinfo.setSessionAttribute("forumattributevalue", new Integer(AttributeID));
          initAgain();
        }
      }
    }
    language = modinfo.getSpokenLanguage();
    if(language == null){
      language = "IS";
    }
    this.checkSettings();
    this.preset();
    this.empty();
    this.addProperState();
  }

} // Class ForumPresentation