package com.idega.block.forum.business;

 /*
    Mætti hafa tvo linka eða svo til að vísa til baka á þræðina, forumlist eða annað þannig að hægt
    sé að útbua siðu til að staðfesta að skrifast hafi i grunninn.
 */

import com.idega.jmodule.forum.business.*;
import com.idega.jmodule.forum.data.*;
import com.idega.jmodule.object.*;
import com.idega.util.*;
import com.idega.util.text.*;
import java.sql.*;
import java.io.*;

/**
 * Title:        JForums<p>
 * Description:  <p>
 * Copyright:    Copyright (c) idega margmiðlun hf.<p>
 * Company:      idega margmiðlun hf.<p>
 * @author idega 2000 - idega team - <a href="mailto:gummi@idega.is">gummi@idega.is</a>
 * @version 1.0
 */

public class ForumDBWriter {

  protected ForumService service;


  public ForumDBWriter(){
    service = new ForumService();
  }


  public int delThread(ModuleInfo modinfo)throws SQLException{

    ForumThread toDel = new ForumThread(Integer.parseInt(modinfo.getRequest().getParameter("thread_id")));
    int parent = toDel.getParentThreadID();
    int myParent = parent;
    int responses = toDel.getNumberOfResponses() + 1;
    int forum = toDel.getForumID();

    ForumThread toUpdate;

    while(parent >= 0){
      toUpdate = new ForumThread(parent);
      toUpdate.setNumberOfResponses(new Integer(toUpdate.getNumberOfResponses() - responses));
      parent = toUpdate.getParentThreadID();
      toUpdate.update();
    }

    toDel.delete();

    if (myParent == -1){
      Forum myForum = new Forum(forum);
      myForum.setNumberOfThreads(new Integer(myForum.getNumberOfThreads() - 1));
      myForum.update();
    }

    return myParent;
  }

  public int saveThread(ModuleInfo modinfo, String UserName, int UserID)throws SQLException{
    int parent_id;
    int forum_id;
    String Author;
    int user_id;
    String subject = modinfo.getRequest().getParameter("thread_subject");
    String body = modinfo.getRequest().getParameter("thread_body");

    if (modinfo.getRequest().getParameter("parent_id") != null){
      parent_id = Integer.parseInt(modinfo.getRequest().getParameter("parent_id"));
    } else {
      parent_id = -1;
    }

    if (modinfo.getRequest().getParameter("forum_id") != null){
      forum_id = Integer.parseInt(modinfo.getRequest().getParameter("forum_id"));
    } else {
      forum_id = 1;
    }

    if (modinfo.getRequest().getParameter("thread_author") != null){
      Author = modinfo.getRequest().getParameter("thread_author");
      user_id = -1;
    } else {
      Author = UserName;
      user_id = UserID;
    }


    if (subject == null || subject.equals("") )
      subject = "Untitled";
/*
    if (body == null)
      throw Exception....

    if (UserName == null)
      bla....
 */

//    return service.saveThread(parent_id, forum_id, TextSoap.findAndRemoveHtmlTags(subject), TextSoap.formatString(body), Author, user_id, idegaTimestamp.RightNow());
      return service.saveThread(parent_id, forum_id, subject, body, Author, user_id, idegaTimestamp.RightNow());
   }



  public int saveForum(ModuleInfo modinfo, String AttibuteName, Integer AttributeValue) throws SQLException{
    String ForumName;
    String ForumDescription;

    ForumName = modinfo.getParameter(ForumList.getForumNameParameterString());
    ForumDescription = modinfo.getParameter(ForumList.getForumDescriptionParameterString());

    if (ForumName == null)
      ForumName = "Untitled";

    if (ForumDescription == null)
      ForumDescription = " ";

    return service.saveForum(ForumName, ForumDescription, AttibuteName, AttributeValue );
  }



  public void deleteForum(int forum_id) throws SQLException{
    ForumAttributes attribute = new ForumAttributes();
      attribute.deleteMultiple("forum_id",Integer.toString(forum_id));

    Forum toDelete = new Forum(forum_id);
      toDelete.delete();
    ForumThread threads = new ForumThread();
    threads.deleteMultiple("forum_id", Integer.toString(forum_id));
  }

  public void setForumUnvalid(int forum_id) throws SQLException {
    Forum theForum = new Forum(forum_id);
    theForum.setValid(false);
  }





} // class ForumDBWriter
