package com.idega.block.forum.business;



import com.idega.data.*;

import com.idega.block.forum.data.*;

import com.idega.util.*;

import java.sql.*;

import java.util.*;

import java.io.*;



/**

 * Title:        JForums<p>

 * Description:  <p>

 * Copyright:    Copyright (c) idega margmiðlun hf.<p>

 * Company:      idega margmiðlun hf.<p>

 * @author idega 2000 - idega team - <a href="mailto:gummi@idega.is">gummi@idega.is</a>

 * @version 1.0

 */



public class ForumService{



  private static IDOLegacyEntity[] ForumThread_ByDate;  //Þarf að uppfæra þegar skrifað/eytt er i grunni

  private IDOLegacyEntity[] Forums_ByName;

  private static Object[][] ThreadTree;

  private static Vector TopLevelThreads;

  private static ForumService staticService;



  private Forum myForum;

  private ForumAdminAttributes myForumAdminAttributes;

  private ForumAttributes myForumAttributes;



  //   ###  Smiðir  ###



  public ForumService(){



  }



  public static ForumService getStaticInstance(){

    if(staticService == null){

      staticService = new ForumService();

    }

    return staticService;

  }



  //   ###  Private - Föll  ###





  public IDOLegacyEntity[] getForumThread(int forum_id, int parent_id, String Ordered_by)throws SQLException{

    return getForumThread( forum_id, parent_id, Ordered_by, true );

  }



  public IDOLegacyEntity[] getForumThread(int forum_id, int parent_id, String Ordered_by, boolean DESC )throws SQLException{

    ForumThread myThread = ((com.idega.block.forum.data.ForumThreadHome)com.idega.data.IDOLookup.getHomeLegacy(ForumThread.class)).createLegacy();

    String desc = "";

    if (DESC)

      desc = " DESC ";

    return myThread.findAll("SELECT * FROM " + myThread.getEntityName() + " WHERE forum_id = " + forum_id + " AND parent_thread_id = " + parent_id + " ORDER BY " + Ordered_by + desc );

  }



  public IDOLegacyEntity[] getForumThread( int parent_id, String Ordered_by)throws SQLException{

    ForumThread myThread = ((com.idega.block.forum.data.ForumThreadHome)com.idega.data.IDOLookup.getHomeLegacy(ForumThread.class)).createLegacy();

    return myThread.findAll("SELECT * FROM " + myThread.getEntityName() + " WHERE parent_thread_id = " + parent_id + " ORDER BY " + Ordered_by + " DESC " );

  }



  private IdegaTree getThreadTree(int forum_id)throws SQLException{

    IdegaTree myTree = new IdegaTree();

    ForumThread myThread = ((com.idega.block.forum.data.ForumThreadHome)com.idega.data.IDOLookup.getHomeLegacy(ForumThread.class)).createLegacy();

    IDOLegacyEntity[] Results;



    Results = myThread.findAll("SELECT * FROM " + myThread.getEntityName() + " WHERE forum_id = " + forum_id + " ORDER BY thread_date DESC");



    for(int i = 0; i < Results.length; i++ ){

      myTree.addNode(((ForumThread)Results[i]), new Integer(((ForumThread)Results[i]).getID()), new Integer(((ForumThread)Results[i]).getParentThreadID()) );

    }



    return myTree;

  }





  private boolean updateTree(int forum_id)throws SQLException{

    boolean finished = false;

    if ( ThreadTree == null)

      InitializeThreadTree();



    for(int i = 0; i < ThreadTree.length; i++ ){

      if (((Integer)ThreadTree[i][0]).equals( new Integer(forum_id))){

        ThreadTree[i][1] = getThreadTree(forum_id);

        finished = true;

        continue;

      }

    }

    return finished;

  }









  private void InitializeThreadTree()throws SQLException{

    Forum myForum = ((com.idega.block.forum.data.ForumHome)com.idega.data.IDOLookup.getHomeLegacy(Forum.class)).createLegacy();

    IDOLegacyEntity[] Results;



    Results = myForum.findAll("SELECT " + myForum.getIDColumnName() + " FROM " + myForum.getEntityName() + " ORDER BY forum_name");



    ThreadTree = new Object[Results.length][2];

    for(int i = 0; i < Results.length; i++ ){

      ThreadTree[i][0] = new Integer(((Forum)Results[i]).getID());

    }

  }





  private void getForums_ByName(boolean just_Valid)throws SQLException{

    Forum myForum = ((com.idega.block.forum.data.ForumHome)com.idega.data.IDOLookup.getHomeLegacy(Forum.class)).createLegacy();

      if (just_Valid)

        Forums_ByName = myForum.findAll("SELECT " + myForum.getIDColumnName() + ", forum_name FROM " + myForum.getEntityName() + " WHERE valid != 'N' ORDER BY forum_name");

     else

        Forums_ByName = myForum.findAll("SELECT " + myForum.getIDColumnName() + ", forum_name FROM " + myForum.getEntityName() + " ORDER BY forum_name");

  }





  private void getForumThread_ByDate()throws SQLException{

    ForumThread myThread = ((com.idega.block.forum.data.ForumThreadHome)com.idega.data.IDOLookup.getHomeLegacy(ForumThread.class)).createLegacy();

//    ForumThread_ByDate = myThread.findAll("SELECT " + myThread.getIDColumnName() + ", thread_subject, thread_date FROM " + myThread.getEntityName() + " ORDER BY thread_date");

    ForumThread_ByDate = myThread.findAll("SELECT * FROM " + myThread.getEntityName() + " WHERE parent_thread_id = -1 AND valid != 'N' ORDER BY thread_date");

//    ForumThread_ByDate = myThread.findAll("SELECT * FROM " + myThread.getEntityName() + " WHERE parent_thread_id = -1 ORDER BY thread_date");

  }





  private IDOLegacyEntity[] getForumThread_ByDate(int forum_id, int parent_id)throws SQLException{

	return getForumThread( forum_id, parent_id, "thread_date" );

  }



  private IDOLegacyEntity[] getForumThread_ByDate(int forum_id, int parent_id, boolean DESC)throws SQLException{

	return getForumThread( forum_id, parent_id, "thread_date", DESC );

  }







  private IDOLegacyEntity[] getForumThread_ByDate(int parent_id)throws SQLException{

	return getForumThread( parent_id, "thread_date" );

  }





/*

  private IDOLegacyEntity[] getForumThread_ByDate(int forum_id, int parent_id)throws SQLException{

    ForumThread myThread = ((com.idega.block.forum.data.ForumThreadHome)com.idega.data.IDOLookup.getHomeLegacy(ForumThread.class)).createLegacy();

    return myThread.findAll("SELECT " + myThread.getIDColumnName() + ", thread_subject, thread_date FROM " + myThread.getEntityName() + " WHERE forum_id = " + forum_id + " AND parent_thread_id = " + parent_id + " ORDER BY thread_date");

  }

*/





/*  private ForumThread getThreadFromDB(int thread_id)throws SQLException{

    ForumThread myThread = ((com.idega.block.forum.data.ForumThreadHome)com.idega.data.IDOLookup.getHomeLegacy(ForumThread.class)).createLegacy();

    return (ForumThread)myThread.findAll("SELECT * FROM " + myThread.getEntityName() + "WHERE " + myThread.getIDColumnName() + " = " + thread_id)[0];

  }

*/





  //   ###  Public - Föll  ###





  public ForumThread[] getTopLevelThreads(int forum_id, int fromXmostNew, int toXmostNew)throws SQLException{

  /*  if (TopLevelThreads.elementAt(forum_id) == null){

      TopLevelThreads = new Vector();

      TopLevelThreads.add(forum_id,getForumThread_ByDate(forum_id, -1));

    }



    if (TopLevelThreads.elementAt(forum_id) == null){

      TopLevelThreads = new Vector();

      TopLevelThreads.add(forum_id,getForumThread_ByDate(forum_id, -1));

    }

*/

    ForumThread[] temp = (ForumThread[])getForumThread_ByDate(forum_id, -1);



    if (toXmostNew > temp.length){

      toXmostNew = temp.length;

      if (fromXmostNew > temp.length)

        fromXmostNew = temp.length;

    }



    ForumThread[] results = new ForumThread[toXmostNew - fromXmostNew];



    int k = 0;

    for (int i = fromXmostNew; i < toXmostNew; i++)

      results[k++] = temp[i];



    return results;

  }



  public ForumThread[] getThreadChildrens(int forum_id, int thread_id)throws SQLException {

      return (ForumThread[])getForumThread_ByDate( forum_id, thread_id, false);

  }



  public ForumThread[] getThreadChildrens( int thread_id)throws SQLException {

      return (ForumThread[])getForumThread_ByDate( thread_id );

  }





  public IdegaTree getTree(int forum_id)throws SQLException{

    IdegaTree myTree = new IdegaTree();

    if ( ThreadTree == null)

      updateTree(forum_id);



    for(int i = 0; i < ThreadTree.length; i++ ){

      if (((Integer)ThreadTree[i][0]).equals( new Integer(forum_id))){

        myTree = (IdegaTree)ThreadTree[i][1];

        continue;

      }

    }

    return myTree;

  }





/*  Notað meðan User_id var i töflu

  public String getUser(int thread_id)throws SQLException{

    ForumThread someThread = ((com.idega.block.forum.data.ForumThreadHome)com.idega.data.IDOLookup.getHomeLegacy(ForumThread.class)).findByPrimaryKeyLegacy(thread_id);

    ForumUser someUser = ((com.idega.block.forum.data.ForumUserHome)com.idega.data.IDOLookup.getHomeLegacy(ForumUser.class)).findByPrimaryKeyLegacy(someThread.getUserID());

    return someUser.getName();

  }

*/





  public int numofThreads()throws SQLException{

    if (ForumThread_ByDate == null){

      getForumThread_ByDate();

    }

    return ForumThread_ByDate.length;

  }



  public Vector getNewThread( int howNew )throws SQLException{

    if (ForumThread_ByDate == null){

      getForumThread_ByDate();

    }



    Vector myVector = new Vector();

    int pos = ForumThread_ByDate.length - howNew;



    if ( ForumThread_ByDate.length >= pos ){

      myVector.add ( new Integer(((ForumThread)ForumThread_ByDate[pos]).getID()));

      myVector.add ((((ForumThread)ForumThread_ByDate[pos]).getThreadSubject()));

    }



    return myVector;



  }







/*

  public Vector getNewThread( int howNew, int forum_id )throws SQLException{

    IDOLegacyEntity[] Thread_ByDate = getForumThread_ByDate(forum_id);

    Vector myVector = new Vector();



    if (howNew > 0){

      myVector.add(0, new Integer((ForumThread)Thread_ByDate[howNew-1]).getID());)

      myVector.add(1,(String)((ForumThread)Thread_ByDate[howNew-1]).getThreadSubject()));

    }



    return myVector;

  }

*/



  public String getParentThread(int thread_id)throws SQLException{

    ForumThread myThread = ((com.idega.block.forum.data.ForumThreadHome)com.idega.data.IDOLookup.getHomeLegacy(ForumThread.class)).findByPrimaryKeyLegacy(thread_id);

    ForumThread parentThread = ((com.idega.block.forum.data.ForumThreadHome)com.idega.data.IDOLookup.getHomeLegacy(ForumThread.class)).findByPrimaryKeyLegacy(myThread.getParentThreadID());

    return parentThread.getThreadSubject();

  }



  public String getForumName(int forum_id) throws SQLException{

    Forum myForum = ((com.idega.block.forum.data.ForumHome)com.idega.data.IDOLookup.getHomeLegacy(Forum.class)).findByPrimaryKeyLegacy(forum_id);

    return myForum.getForumName();

  }





  public String getForum(int thread_id)throws SQLException{

    ForumThread myThread = ((com.idega.block.forum.data.ForumThreadHome)com.idega.data.IDOLookup.getHomeLegacy(ForumThread.class)).findByPrimaryKeyLegacy(thread_id);

    Forum myForum = ((com.idega.block.forum.data.ForumHome)com.idega.data.IDOLookup.getHomeLegacy(Forum.class)).findByPrimaryKeyLegacy(myThread.getForumID());

    return myForum.getForumName();

  }





/*   ####   set i bið   ####

  public String getMemberName(int user_id)throws SQLException{

    Member myMember = new Member(user_id);

    return myMember.getName();

  }

*/



  public boolean hasChilde(int thread_id)throws SQLException{

    ForumThread myThread = ((com.idega.block.forum.data.ForumThreadHome)com.idega.data.IDOLookup.getHomeLegacy(ForumThread.class)).createLegacy();

    IDOLegacyEntity[] result = myThread.findAll("select " + myThread.getIDColumnName() + " from " + myThread.getEntityName() + " where parent_thread_id='" + thread_id + "'");

    if (result == null)

      return false;

    if (result.length > 0)

      return true;

    else

      return false;



    /*  // betri utgafa hefði eg haldið?

    ForumThread myThread = ((com.idega.block.forum.data.ForumThreadHome)com.idega.data.IDOLookup.getHomeLegacy(ForumThread.class)).findByPrimaryKeyLegacy(thread_id);

    if (myThread.getNumberOfResponses() > 0)

      return true;

    else

      return false;

    */



  }



  public ForumThread[] getThreads(int forum_id)throws SQLException{

    ForumThread myThread = ((com.idega.block.forum.data.ForumThreadHome)com.idega.data.IDOLookup.getHomeLegacy(ForumThread.class)).createLegacy();

    return (ForumThread[])myThread.findAll("select * from " + myThread.getEntityName() + " where forum_id='"+forum_id+"' order by " + myThread.getIDColumnName() );

  }



  public ForumThread getThread(int thread_id)throws SQLException{

     return ((com.idega.block.forum.data.ForumThreadHome)com.idega.data.IDOLookup.getHomeLegacy(ForumThread.class)).findByPrimaryKeyLegacy(thread_id);

  }



  public Timestamp getTimestamp(int thread_id)throws SQLException{

    ForumThread myThread = ((com.idega.block.forum.data.ForumThreadHome)com.idega.data.IDOLookup.getHomeLegacy(ForumThread.class)).findByPrimaryKeyLegacy(thread_id);

    return myThread.getThreadDate();

  }



  public IWTimestamp get_idegaTimestamp(int thread_id)throws SQLException{

    return new IWTimestamp(getTimestamp(thread_id));

  }



  /*  // Var notað meðan UserID var i töflu

  public String getUserName(int thread_id)throws SQLException{

    ForumThread myThread = ((com.idega.block.forum.data.ForumThreadHome)com.idega.data.IDOLookup.getHomeLegacy(ForumThread.class)).findByPrimaryKeyLegacy(thread_id);

    ForumUser theUser = ((com.idega.block.forum.data.ForumUserHome)com.idega.data.IDOLookup.getHomeLegacy(ForumUser.class)).createLegacy();

    ForumUser[] myUser;

    myUser = (ForumUser[])theUser.findAll("SELECT * FROM " + theUser.getEntityName() + " WHERE user_id = '" + myThread.getUserID() + "'"); //WHERE user_id ekki forum_user_id

    return myUser[0].getName();

  }

*/





  public int saveThread(int parent_id, int forum_id, String subject, String body, String user_name, int user_id, IWTimestamp date )throws SQLException{

    ForumThread myThread = ((com.idega.block.forum.data.ForumThreadHome)com.idega.data.IDOLookup.getHomeLegacy(ForumThread.class)).createLegacy();



    myThread.setForumID(new Integer(forum_id));

    myThread.setNumberOfResponses(new Integer(0));

    myThread.setParentThreadID( new Integer(parent_id) );

    myThread.setThreadBody( body );

    myThread.setThreadDate(date.getTimestamp());

    myThread.setThreadSubject(subject);

    myThread.setUserName(user_name);

    myThread.setUserID(new Integer(user_id));

    myThread.setValid(true);



    if(parent_id == -1){

      Forum myForum = ((com.idega.block.forum.data.ForumHome)com.idega.data.IDOLookup.getHomeLegacy(Forum.class)).findByPrimaryKeyLegacy(forum_id);

      myForum.setNumberOfThreads(new Integer(myForum.getNumberOfThreads() + 1));

      myForum.setNewThreadDate(date.getTimestamp());

      myForum.update();

    }





    int parent = parent_id;

    ForumThread parentThread;





    while(parent >= 0){

      parentThread = ((com.idega.block.forum.data.ForumThreadHome)com.idega.data.IDOLookup.getHomeLegacy(ForumThread.class)).findByPrimaryKeyLegacy(parent);

      if ((parentThread.getNumberOfResponses()+1) <= 0 )

        parentThread.setNumberOfResponses(new Integer(parentThread.getNumberOfResponses()+2));

      else

        parentThread.setNumberOfResponses(new Integer(parentThread.getNumberOfResponses()+1));



      parent = parentThread.getParentThreadID();

      parentThread.update();

    }



    myThread.insert();

    updateTree(forum_id);



    return myThread.getID();

  }





  public void saveNewThread( int forum_id, String subject, String body, String user_name, IWTimestamp date )throws SQLException{

    ForumThread myThread = ((com.idega.block.forum.data.ForumThreadHome)com.idega.data.IDOLookup.getHomeLegacy(ForumThread.class)).createLegacy();



    myThread.setForumID(new Integer(forum_id));

    myThread.setNumberOfResponses( new Integer(0) );

    myThread.setParentThreadID( new Integer(-1) );

    myThread.setThreadBody( body );

    myThread.setThreadDate(date.getTimestamp());

    myThread.setThreadSubject(subject);

    myThread.setUserName(user_name);

    myThread.setValid(true);

    myThread.insert();



    Forum myForum = ((com.idega.block.forum.data.ForumHome)com.idega.data.IDOLookup.getHomeLegacy(Forum.class)).findByPrimaryKeyLegacy(forum_id);

    myForum.setNumberOfThreads(new Integer(myForum.getNumberOfThreads() + 1));

    myForum.setNewThreadDate(date.getTimestamp());

    myForum.update();



    updateTree(forum_id);

    getForumThread_ByDate();

  }











  public Vector getForums(boolean just_Valid)throws SQLException{

    getForums_ByName(just_Valid);

    Vector myVector = new Vector();

    Vector id = new Vector();

    Vector name = new Vector();



    for(int i = 0; i < Forums_ByName.length; i++){

      id.add(i, new Integer(((Forum)Forums_ByName[i]).getID()));

      name.add(i, ((Forum)Forums_ByName[i]).getForumName());

    }



    myVector.add(0, id);

    myVector.add(1, name);





    return myVector;

  }





  public int createForum(String name, String description)throws SQLException{

    Forum toConstruct = ((com.idega.block.forum.data.ForumHome)com.idega.data.IDOLookup.getHomeLegacy(Forum.class)).createLegacy();

    toConstruct.setForumDescription(description);

    toConstruct.setForumName(name);

    toConstruct.setGroupID(new Integer(1));

    toConstruct.setNewThreadDate(IWTimestamp.getTimestampRightNow());

    toConstruct.setNumberOfThreads(new Integer(0));

    toConstruct.setValid(true);



    toConstruct.insert();



    return toConstruct.getID();

  }





  public int getDefaultAttributeForumID(int attribute_id, String attribute_name)throws SQLException{

    int toreturn = 1;

    ForumAttributes[] forums = getForumAttributes(attribute_id, attribute_name);



    if (forums.length > 0){

      toreturn = forums[0].getForumID();

    } else {

      toreturn = createForum("Forum", "");

      ForumAttributes attribute = ((com.idega.block.forum.data.ForumAttributesHome)com.idega.data.IDOLookup.getHomeLegacy(ForumAttributes.class)).createLegacy();

      attribute.setAttributeName(attribute_name);

      attribute.setAttributeID(new Integer(attribute_id));

      attribute.setForumID(new Integer(toreturn));

      attribute.insert();

    }



    return toreturn;

  }





  /**

   * @todo check implementation

   * beta implementation

   */

  public ForumAttributes[] getForumAttributes(int attribute_id, String attribute_name)throws SQLException{

   if (myForumAttributes == null)

      myForumAttributes = ((com.idega.block.forum.data.ForumAttributesHome)com.idega.data.IDOLookup.getHomeLegacy(ForumAttributes.class)).createLegacy();

    ForumAttributes[] forums = null;

    ForumAttributes[] forums2 = null;

    try{

      forums = new ForumAttributes[1];

      forums[0] = ((com.idega.block.forum.data.ForumAttributesHome)com.idega.data.IDOLookup.getHomeLegacy(ForumAttributes.class)).findByPrimaryKeyLegacy(1);



      forums2 = (ForumAttributes[])myForumAttributes.findAllByColumn("attribute_name",attribute_name,"attribute_id", "" + attribute_id);



      if (forums2 != null)

        forums = forums2;



    }catch(SQLException e){

      //myForumAttributes.insertStartData();



      forums = new ForumAttributes[1];

      forums[0] = ((com.idega.block.forum.data.ForumAttributesHome)com.idega.data.IDOLookup.getHomeLegacy(ForumAttributes.class)).findByPrimaryKeyLegacy(1);



      forums2 = (ForumAttributes[])myForumAttributes.findAllByColumn("attribute_name",attribute_name,"attribute_id", "" + attribute_id);



      if (forums2 != null)

        forums = forums2;



    }

    return forums;

  }



  public ForumAdmin getForumSettings(int attribute_id, String attribute_name)throws SQLException{

    ForumAdmin settings = null;

    if (myForumAttributes == null)

       myForumAdminAttributes = ((com.idega.block.forum.data.ForumAdminAttributesHome)com.idega.data.IDOLookup.getHomeLegacy(ForumAdminAttributes.class)).createLegacy();



    ForumAdminAttributes admin = (ForumAdminAttributes)(myForumAdminAttributes.findAllByColumn("attribute_name",attribute_name,"attribute_id", "" + attribute_id )[0]);



    if (admin != null)

      settings = ((com.idega.block.forum.data.ForumAdminHome)com.idega.data.IDOLookup.getHomeLegacy(ForumAdmin.class)).findByPrimaryKeyLegacy(admin.getForumID());



    return settings;

  }









  public Forum[] getForums()throws SQLException{

    if (myForum == null)

       myForum = ((com.idega.block.forum.data.ForumHome)com.idega.data.IDOLookup.getHomeLegacy(Forum.class)).createLegacy();



    Forum[] forums = null;



    forums = (Forum[])myForum.findAllByColumn("Valid", "Y");



    return forums;

  }







  public int saveForum(String Name, String Description, String AttributeName, Integer AttributeValue ) throws SQLException{

    Forum newForum = ((com.idega.block.forum.data.ForumHome)com.idega.data.IDOLookup.getHomeLegacy(Forum.class)).createLegacy();



    newForum.setForumName(Name);

    newForum.setForumDescription(Description);

    newForum.setNumberOfThreads(new Integer("0"));

    newForum.setGroupID(new Integer(-1));

    newForum.setValid(true);

    newForum.insert();



    ForumAttributes attribute = ((com.idega.block.forum.data.ForumAttributesHome)com.idega.data.IDOLookup.getHomeLegacy(ForumAttributes.class)).createLegacy();



    if (AttributeName != null){

      attribute.setForumID(new Integer(newForum.getID()));

      attribute.setAttributeName(AttributeName);

      attribute.setAttributeID(AttributeValue);

      attribute.insert();

    }



    return newForum.getID();

  }









/*

  public String TreeContent()throws SQLException{

    String temp = "";

    if (ThreadTree == null)

      InitializeThreadTree();

    for(int i = 0; i < ThreadTree.length; i++ ){

      updateTree(((Integer)ThreadTree[i][0]).intValue());

      temp = temp + "Tree : " + (((Integer)ThreadTree[i][0]).toString()) + " :<br>";

      if (ThreadTree[i][1] != null)

        temp = temp + ThreadTree[i][1].toString();

      temp = temp + "<br>";

    }

    return temp;

  }*/



} // class ForumService

