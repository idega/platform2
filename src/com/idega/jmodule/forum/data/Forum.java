package com.idega.jmodule.forum.data;

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

public class Forum extends GenericEntity {

  public Forum() {
    super();
  }


  public Forum(int id) throws SQLException{
    super(id);
  }

  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute("forum_name","Flokkar",true,true,"java.lang.String");
    addAttribute("forum_description","Lýsing",true,true,"java.lang.String");
    addAttribute("group_id","Hópur",true,true,"java.lang.Integer");
    addAttribute("valid","Í gildi",true,true,"java.lang.String");
    addAttribute("number_of_threads","Fjöldi þráða",true,true,"java.lang.Integer");
    addAttribute("new_thread_date","Nýasti þráður",true,true,"java.sql.Timestamp");
  }

  public String getEntityName(){
    return "forum";
  }


  // ### get- & set-Föll ###


  public String getForumName(){
    return getStringColumnValue("forum_name");
  }

  public void setForumName( String forum_name){
    setColumn("forum_name",forum_name);
  }


  public String getForumDescription(){
    return (String)getColumnValue("forum_description");
  }

  public void setForumDescription( String forum_description){
    setColumn("forum_description",forum_description);
  }


  public int getGroupID(){
    return getIntColumnValue("group_id");
  }

  public void setGroupID( Integer group_id){
    setColumn("group_id",group_id);
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

  public Timestamp getNewThreadDate(){
    return (Timestamp)getColumnValue("new_thread_date");
  }

  public void setNewThreadDate(Timestamp stamp){
    setColumn("new_thread_date",stamp);
  }

  public int getNumberOfThreads(){
    return getIntColumnValue("number_of_threads");
  }

  public void setNumberOfThreads(Integer num){
    setColumn("number_of_threads", num);
  }
}   // class Forum
