package com.idega.jmodule.forum.business;

import com.idega.jmodule.forum.data.*;
import com.idega.presentation.text.*;
import com.idega.presentation.*;
import java.util.*;
import java.sql.*;

/**
 * Title:        JForum<p>
 * Description:  <p>
 * Copyright:    Copyright (c) idega margmiðlun hf.<p>
 * Company:      idega margmiðlun hf.<p>
 * @author idega 2000 - idega team - <a href="mailto:gummi@idega.is">gummi@idega.is</a>
 * @version 1.0
 */

public class ThreadContents {

  private Link AnswerLink;
  private Link DeleteLink;
  private boolean ALParametersAdded;
  private boolean DELParametersAdded;
  private boolean ReturnLinks;
  private IWContext iwc;

  private ForumThread thisThread;

  private ForumService service;
  private Vector contents;
  private int placeInVector;

  private ThreadContents() {
    AnswerLink = new Link("Svara");
    DeleteLink = new Link("Eyða");
    ALParametersAdded = false;
    DELParametersAdded = false;
    ReturnLinks = true;
    service = new ForumService();
  }

  public ThreadContents(int id) throws SQLException{
    this();
    thisThread = new ForumThread(id);
  }

  public ThreadContents(int id, boolean links) throws SQLException{
    this(id);
    ReturnLinks = links;
  }

  public ThreadContents(ForumThread threads) throws SQLException{
    this();
    thisThread = threads;
  }

  //Föll

  public int getID(){
    return thisThread.getID();
  }

  public int getParentThreadID(){
    return thisThread.getParentThreadID();
  }

  public int getForumID(){
    return thisThread.getForumID();
  }

  public String getThreadSubject(){
    return thisThread.getThreadSubject();
  }

  public String getThreadBody(){
    return thisThread.getThreadBody();
  }

  public int getUserID(){
    return thisThread.getUserID();
  }

  public String getUserName(){
    return thisThread.getUserName();
  }

  public Timestamp getThreadDate(){
    return thisThread.getThreadDate();
  }

  public int getNumberOfResponses(){
    return thisThread.getNumberOfResponses();
  }

   public boolean isValid(){
      return thisThread.isValid();
  }


  public void setDeleteLinkObject( PresentationObject onLink ){
    DeleteLink.setObject(onLink);
  }

  public void setDeleteLinkText(String text){
    DeleteLink.setText(text);
  }


  public void setAnswerLinkObject( PresentationObject onLink ){
    AnswerLink.setObject(onLink);
  }

  public void setAnswerLinkText(String text){
    AnswerLink.setText(text);
  }




  private void addAnswerLinkParameters(){
    AnswerLink.addParameter("parent_id", "" + this.getID());
    AnswerLink.addParameter("thread_subj", this.getThreadSubject());
    AnswerLink.addParameter("forum_id", "" + this.getForumID());
    AnswerLink.addParameter("from","ATLink");
    AnswerLink.addParameter( "state", "5");
    ALParametersAdded = true;
  }

  public Link getAnswerLink(){
    if (!ReturnLinks){
      return null;
    }else{
      if (!ALParametersAdded)
        addAnswerLinkParameters();
      return AnswerLink;
    }
  }


  private void addDeleteLinkParameters(){
    DeleteLink.addParameter("thread_id", "" + this.getID());
    DeleteLink.addParameter("forum_id", "" + this.getForumID());
    DeleteLink.addParameter("from","DELLink");
    DeleteLink.addParameter( "state", "6");
    DELParametersAdded = true;
  }

  public Link getDeleteLink(){
    if (!ReturnLinks){
      return null;
    }else{
      if (!DELParametersAdded)
        addDeleteLinkParameters();
      return DeleteLink;
    }
  }

  public boolean ReturnsLinks(){
    return ReturnLinks;
  }



  private ThreadContents[] getSubThreadsTo() throws SQLException {
    contents = new Vector();

    placeInVector = 0;
    contents.add(placeInVector++, this);
    addSubThreads(this.getID());


    contents.trimToSize();
    return (ThreadContents[])contents.toArray((Object[])java.lang.reflect.Array.newInstance(this.getClass(),0));
  }


  private void addSubThreads(int thread_id) throws SQLException{
    ForumThread[] Temp;
    Temp = (ForumThread[])service.getThreadChildrens(thread_id);

    for (int i = 0; i < Temp.length; i++){
      contents.add(placeInVector++,new ThreadContents(Temp[i]));
      if(((ForumThread)Temp[i]).getNumberOfResponses() > 0)
        addSubThreads(Temp[i].getID());
    }
  }

  public ThreadContents[] getThreads(boolean All) throws SQLException {
    ThreadContents[] toReturn = null;
    if (All){
      toReturn = getSubThreadsTo();
    }else{
      contents = new Vector();
      contents.add(0, this);
      contents.trimToSize();
      toReturn = (ThreadContents[])contents.toArray((Object[])java.lang.reflect.Array.newInstance(this.getClass(),0));
    }
    return toReturn;
  }


} // Class ThreadContents2
