package com.idega.block.forum.business;



import java.util.*;

import java.sql.SQLException;

import com.idega.block.forum.data.*;

import com.idega.data.EntityFinder;

/**

 * Title:

 * Description:

 * Copyright:    Copyright (c) 2001

 * Company:

 * @author

 * @version 1.0

 */



public class ForumEmailHandler {



  ForumEmail myForumEmail;



  public ForumEmailHandler() throws Exception {

    myForumEmail = ((com.idega.block.forum.data.ForumEmailHome)com.idega.data.IDOLookup.getHomeLegacy(ForumEmail.class)).createLegacy();

  }









  public String[] getAddresses(int thread_id )throws SQLException{

    ForumThread thisThread = ((com.idega.block.forum.data.ForumThreadHome)com.idega.data.IDOLookup.getHomeLegacy(ForumThread.class)).findByPrimaryKeyLegacy(thread_id);

    int forumID = thisThread.getForumID();

    int parentID= thisThread.getParentThreadID();

    List forumEmails = null;

    List threadEmails = EntityFinder.findAllByColumn(myForumEmail, myForumEmail.getThreadIDColumnName(), thread_id);

    Vector myEmails = new Vector();



    if (parentID < 1){

      forumEmails = EntityFinder.findAllByColumn(myForumEmail, myForumEmail.getForumIDColumnName(), forumID);

    }



    if (forumEmails != null)

      myEmails.addAll(forumEmails);



    if (threadEmails != null)

      myEmails.addAll(threadEmails);



    if(myEmails.size() > 0){

      Vector myVector = new Vector();

      for (int i = 0; i < myEmails.size(); i++) {

        myVector.add(i,((ForumEmail)myEmails.elementAt(i)).getEmailAddress());

      }

      return (String[])myVector.toArray((Object[])new String[0]);

    }else{

      return null;

    }



  }





  public void addAddressToForumPostlist(String Address, int forumID)throws Exception{

    if(Address == null || forumID < 1){

      throw new Exception("Address og forumID invalid");

    }else{

      ForumEmail newAddress = ((com.idega.block.forum.data.ForumEmailHome)com.idega.data.IDOLookup.getHomeLegacy(ForumEmail.class)).createLegacy();

      newAddress.setEmailAddress(Address);

      newAddress.setForumID(forumID);

      newAddress.insert();

    }

  }



  public void addAddressToThreadPostlist(String Address, int threadID)throws Exception{

    if(Address == null || threadID < 1){

      throw new Exception("Address og forumID invalid");

    }else{

      ForumEmail newAddress = ((com.idega.block.forum.data.ForumEmailHome)com.idega.data.IDOLookup.getHomeLegacy(ForumEmail.class)).createLegacy();

      newAddress.setEmailAddress(Address);

      newAddress.setThreadID(threadID);

      newAddress.insert();

    }

  }



  public void removeFromForumPostlist(String Address, int forumID)throws SQLException{

    List toRemove = EntityFinder.findAllByColumn(myForumEmail,myForumEmail.getEmailAddressColumnName(), Address, myForumEmail.getForumIDColumnName(), Integer.toString(forumID));



    ForumEmail[] mails = (ForumEmail[])toRemove.toArray((Object[])java.lang.reflect.Array.newInstance(this.getClass(),0));



    if (mails != null){

      for (int i = 0; i < mails.length; i++) {

        mails[i].delete();

      }

    }



  }





  public void removeFromThreadPostlist(String Address, int threadID)throws SQLException{

    List toRemove = EntityFinder.findAllByColumn(myForumEmail,myForumEmail.getEmailAddressColumnName(), Address, myForumEmail.getThreadIDColumnName(), Integer.toString(threadID));



    ForumEmail[] mails = (ForumEmail[])toRemove.toArray((Object[])java.lang.reflect.Array.newInstance(this.getClass(),0));



    if (mails != null){

      for (int i = 0; i < mails.length; i++) {

        mails[i].delete();

      }

    }



  }



  public void removeFromPostlists(String Address)throws SQLException{

    List toRemove = EntityFinder.findAllByColumn(myForumEmail,myForumEmail.getEmailAddressColumnName(), Address);



    ForumEmail[] mails = (ForumEmail[])toRemove.toArray((Object[])java.lang.reflect.Array.newInstance(this.getClass(),0));



    if (mails != null){

      for (int i = 0; i < mails.length; i++) {

        mails[i].delete();

      }

    }



  }









}

