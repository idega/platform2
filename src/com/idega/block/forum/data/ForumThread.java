package com.idega.block.forum.data;

import com.idega.data.*;
import java.sql.*;

/**
 * Title:        JForums<p>
 * Description:  <p>
 * Copyright:    Copyright (c) idega margmiðlun hf.<p>
 * Company:      idega margmiðlun hf.<p>
 * @author idega 2000 - idega team - <a href="mailto:gummi@idega.is">gummi@idega.is</a>
 * @version 1.0
 */

public class ForumThread extends GenericEntity {

  public ForumThread() {
    super();
  }

  public ForumThread(int id)throws SQLException{
    super(id);
  }

  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute("parent_thread_id","Foreldri",true,true,"java.lang.Integer");
    addAttribute("forum_id","Flokkur",true,true,"java.lang.Integer");
    addAttribute("thread_subject","Þráður",true,true,"java.lang.String");
    addAttribute("thread_body","Meginmál",true,true,"java.lang.String");
    addAttribute("user_id","Notandi",true,true,"java.lang.Integer");
    addAttribute("user_name","Notandi",true,true,"java.lang.String");
    addAttribute("thread_date","Dagsetning",true,true,"java.sql.Timestamp");
    addAttribute("number_of_responses","Fjöldi Svara",true,true,"java.lang.Integer");
    addAttribute("valid","Í gildi",true,true,"java.lang.String");
//    addAttribute("on_front","Synilegur utan tres",false,true,"java.lang.String");
  }

  public String getEntityName(){
    return "fo_forum_thread";
  }


  // ### get- & set-Föll ###


  public int getParentThreadID(){
    return getIntColumnValue("parent_thread_id");
  }

  public void setParentThreadID( Integer parent_thread_id){
    setColumn("parent_thread_id",parent_thread_id);
  }


  public int getForumID(){
    return getIntColumnValue("forum_id");
  }

  public void setForumID( Integer forum_id){
    setColumn("forum_id",forum_id);
  }


  public String getThreadSubject(){
    return (String)getColumnValue("thread_subject");
  }

  public void setThreadSubject( String thread_subject){
    setColumn("thread_subject",thread_subject);
  }


  public String getThreadBody(){
    return (String)getColumnValue("thread_body");
  }

  public void setThreadBody( String thread_body){
    setColumn("thread_body",thread_body);
  }


  public int getUserID(){
    return getIntColumnValue("user_id");
  }

  public void setUserID( Integer user_id){
    setColumn("user_id",user_id);
  }


  public String getUserName(){
    return (String)getColumnValue("user_name");
  }

  public void setUserName( String user_name){
    setColumn("user_name",user_name);
  }


  public Timestamp getThreadDate(){
    return (Timestamp)getColumnValue("thread_date");
  }

  public void setThreadDate( Timestamp thread_date ){
    setColumn("thread_date",thread_date);
  }


  public int getNumberOfResponses(){
    return getIntColumnValue("number_of_responses");
  }

  public void setNumberOfResponses( Integer number_of_responses){
    setColumn("number_of_responses",number_of_responses);
  }


   public boolean isValid(){
    if (((String)getColumnValue("valid")).equals("Y") )
      return true;
    else
      return false;
  }

  public void setValid( boolean valid ){
    if (valid)
      setColumn("valid","Y");
    else
      setColumn("valid","N");
  }






} // class ForumThread
