package com.idega.block.forum.business;



 /*

    Mætti hafa tvo linka eða svo til að vísa til baka á þræðina, forumlist eða annað þannig að hægt

    sé að útbua siðu til að staðfesta að skrifast hafi i grunninn.

 */



import com.idega.block.forum.business.*;

import com.idega.util.SendMail;

import com.idega.block.forum.data.*;

import com.idega.presentation.*;

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

  private ForumEmailHandler myEmailHandler;



  public ForumDBWriter() throws Exception{

    service = new ForumService();

    myEmailHandler = new ForumEmailHandler();

  }





  public int delThread(IWContext iwc)throws SQLException{



    ForumThread toDel = ((com.idega.block.forum.data.ForumThreadHome)com.idega.data.IDOLookup.getHomeLegacy(ForumThread.class)).findByPrimaryKeyLegacy(Integer.parseInt(iwc.getParameter("thread_id")));

    int parent = toDel.getParentThreadID();

    int myParent = parent;

    int responses = toDel.getNumberOfResponses() + 1;

    int forum = toDel.getForumID();



    ForumThread toUpdate;



    while(parent >= 0){

      toUpdate = ((com.idega.block.forum.data.ForumThreadHome)com.idega.data.IDOLookup.getHomeLegacy(ForumThread.class)).findByPrimaryKeyLegacy(parent);

      toUpdate.setNumberOfResponses(new Integer(toUpdate.getNumberOfResponses() - responses));

      parent = toUpdate.getParentThreadID();

      toUpdate.update();

    }



    toDel.delete();



    if (myParent == -1){

      Forum myForum = ((com.idega.block.forum.data.ForumHome)com.idega.data.IDOLookup.getHomeLegacy(Forum.class)).findByPrimaryKeyLegacy(forum);

      myForum.setNumberOfThreads(new Integer(myForum.getNumberOfThreads() - 1));

      myForum.update();

    }



    return myParent;

  }



  public int saveThread(IWContext iwc, String UserName, int UserID)throws Exception{

    int newThreadID;

    int parent_id;

    int forum_id;

    String Author;

    int user_id;

    String emailAddress;

    String subject = iwc.getParameter("thread_subject");

    String body = iwc.getParameter("thread_body");



    if (iwc.getParameter("parent_id") != null){

      parent_id = Integer.parseInt(iwc.getParameter("parent_id"));

    } else {

      parent_id = -1;

    }



    if (iwc.getParameter("forum_id") != null){

      forum_id = Integer.parseInt(iwc.getParameter("forum_id"));

    } else {

      forum_id = 1;

    }



    if (iwc.getParameter("thread_author") != null){

      Author = iwc.getParameter("thread_author");

      user_id = -1;

    } else {

      Author = UserName;

      user_id = UserID;

    }



    if (iwc.getParameter("forum_email") != null){

      emailAddress = iwc.getParameter("forum_email");

    } else {

      emailAddress = null;

    }





    if (subject == null || subject.equals("") )

      subject = "Untitled";

/*

    if (body == null)

      throw Exception....



    if (UserName == null)

      bla....

 */



//    return service.saveThread(parent_id, forum_id, TextSoap.findAndRemoveHtmlTags(subject), TextSoap.formatString(body), Author, user_id, IWTimestamp.RightNow());

      newThreadID =  service.saveThread(parent_id, forum_id, subject, body, Author, user_id, IWTimestamp.RightNow());



    if (emailAddress != null && !emailAddress.equals("")){

      myEmailHandler.addAddressToThreadPostlist(emailAddress, newThreadID);

    }



   try {

      String[] emails = myEmailHandler.getAddresses(newThreadID);

      String URL = iwc.getRequestURI();

      String eSubject = "Nýtt innlegg á spjallþræði " + URL;

      String eText = "Fyrirsögning er : " + subject + " og sendandi : " + Author;



      for (int j = 0; j < emails.length; j++) {

        try {

          System.out.println(emails[j]);

          System.out.println(URL);

          System.out.println(eSubject);

          System.out.println(eText);

          SendMail.send(URL, emails[j],null,null, "mail.idega.is", eSubject , eText);

          //MailSender.send( URL, emails[j],null,null, "mail.idega.is", eSubject , eText);

        }

        catch (Exception ex) {



        }

      }

    }

    catch (Exception ex) {



    }





    return newThreadID;



   }







  public int saveForum(IWContext iwc, String AttibuteName, Integer AttributeValue) throws SQLException{

    String ForumName;

    String ForumDescription;



    ForumName = iwc.getParameter(ForumList.getForumNameParameterString());

    ForumDescription = iwc.getParameter(ForumList.getForumDescriptionParameterString());



    if (ForumName == null)

      ForumName = "Untitled";



    if (ForumDescription == null)

      ForumDescription = " ";



    return service.saveForum(ForumName, ForumDescription, AttibuteName, AttributeValue );

  }



  public void deleteForum(int forum_id) throws SQLException{

    ForumAttributes attribute = ((com.idega.block.forum.data.ForumAttributesHome)com.idega.data.IDOLookup.getHomeLegacy(ForumAttributes.class)).createLegacy();

      attribute.deleteMultiple("forum_id",Integer.toString(forum_id));



    Forum toDelete = ((com.idega.block.forum.data.ForumHome)com.idega.data.IDOLookup.getHomeLegacy(Forum.class)).findByPrimaryKeyLegacy(forum_id);

      toDelete.delete();

    ForumThread threads = ((com.idega.block.forum.data.ForumThreadHome)com.idega.data.IDOLookup.getHomeLegacy(ForumThread.class)).createLegacy();

    threads.deleteMultiple("forum_id", Integer.toString(forum_id));

  }



  public void setForumUnvalid(int forum_id) throws SQLException {

    Forum theForum = ((com.idega.block.forum.data.ForumHome)com.idega.data.IDOLookup.getHomeLegacy(Forum.class)).findByPrimaryKeyLegacy(forum_id);

    theForum.setValid(false);

  }



  public void saveToForumPostlist(IWContext iwc){



  }



  public void saveToThreadPostlist(IWContext iwc){



  }



} // class ForumDBWriter

