package com.idega.block.forum.business;

import com.idega.block.forum.data.*;
import com.idega.block.forum.presentation.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.interfaceobject.*;
import java.util.*;
import java.sql.*;

/**
 * Title:        JForums<p>
 * Description:  <p>
 * Copyright:    Copyright (c) idega margmiðlun hf.<p>
 * Company:      idega margmiðlun hf.<p>
 * @author idega 2000 - idega team - <a href="mailto:gummi@idega.is">gummi@idega.is</a>
 * @version 1.0
 */

public class ForumList{

  private Forum thisForum;
  private ForumService service;
  private Link enterForum;
  private boolean FListParametersAdded;

 // admin object
  private TextInput ForumNameInput;
  private TextArea ForumDescriptionArea;
  private SubmitButton ForumCreationSubmitButton;
  private AdminButton ForumEditButton;
  private AdminButton ForumInsertButton;
  private AdminButton ForumDeletebutton;

  private static String ForumName = "forum_name";
  private static String ForumDescription = "forumDescription";



  public ForumList() throws SQLException {
    enterForum = new Link();
    FListParametersAdded = false;
    service = new ForumService();
  }

  public ForumList(int id) throws SQLException {
    this();
    thisForum = new Forum(id);
  }

  public ForumList(Forum theForum) throws SQLException {
    this();
    thisForum = theForum;
  }

  public int getID(){
    return thisForum.getID();
  }

  public String getForumName(){
    return thisForum.getForumName();
  }

  public String getForumDescription(){
    return thisForum.getForumDescription();
  }

  public int getGroupID(){
    return thisForum.getGroupID();
  }

  public boolean isValid(){
     return thisForum.isValid();
  }

  public Timestamp getNewThreadDate(){
    return thisForum.getNewThreadDate();
  }

  public int getNumberOfThreads(){
    return thisForum.getNumberOfThreads();
  }



  private void addFListParameters(){
    enterForum.setText(this.getForumName());
    enterForum.addParameter("forum_id", "" + this.getID());
    enterForum.addParameter("from","FList");
    enterForum.addParameter( "state", "2");
    FListParametersAdded = true;
  }

  public Link getForumLink(){
    if (!FListParametersAdded)
      addFListParameters();
    return enterForum;
  }




  public ForumList[] getForums() throws SQLException {
    if (thisForum == null)
      thisForum = new Forum();
    Forum[] list = (Forum[])thisForum.findAllOrdered("forum_name");

    return getList(list);
  }


  public ForumList[] getForums(int attribute_id, String attribute_name) throws SQLException {
    ForumAttributes[] attribute = service.getForumAttributes(attribute_id, attribute_name);

    ForumList[] list = new ForumList[attribute.length];

    for (int i = 0; i < attribute.length; i++){
      list[i] = new ForumList( attribute[i].getForumID() );
    }

    return list;
  }

/*
  public ForumList[] getForums(int union) throws SQLException {

  }
*/

  private ForumList[] getList(Forum[] list) throws SQLException{
    Vector listVector = new Vector();
    if (list != null){
    for(int i=0; i < list.length; i++){
      listVector.add(i, new ForumList(list[i]));
    }
      return (ForumList[])listVector.toArray((Object[])java.lang.reflect.Array.newInstance(this.getClass(),0));
    }else{
      return null;
    }
 }




 /*
 // admin object
 */



  public void setForumNameInput(){
    ForumNameInput = new TextInput(ForumName);
//    ForumNameInput.keepStatusOnAction();
  }

  public TextInput getForumNameInput(){
    if (ForumNameInput == null)
      setForumNameInput();

    return ForumNameInput;
  }


  public void setForumDescriptionArea(){
    ForumDescriptionArea = new TextArea(ForumDescription);
  }

  public TextArea getForumDescriptionArea(){
    if (ForumDescriptionArea == null)
      setForumDescriptionArea();

    return ForumDescriptionArea;
  }


  public void setForumCreationSubmitButton(){
    ForumCreationSubmitButton = new SubmitButton("Submit");
  }

  public SubmitButton getForumSubmitButton(){
    if (ForumCreationSubmitButton == null)
      setForumCreationSubmitButton();

    return ForumCreationSubmitButton;
  }



  private void initForumInsertButton()throws SQLException{
    ForumInsertButton = new AdminButton("Stofna svæði" , new ForumAdminTemplate());
    ForumInsertButton.addParameter("state", "1");
    ForumInsertButton.addParameter("forum_action","insert_forum");
  }

  public AdminButton getForumInsertButton()throws SQLException{
    if (ForumInsertButton == null)
      initForumInsertButton();

    return ForumInsertButton;
  }


  public static String getForumNameParameterString(){
    return ForumName;
  }

  public static String getForumDescriptionParameterString(){
    return ForumDescription;
  }



} // class ForumList
