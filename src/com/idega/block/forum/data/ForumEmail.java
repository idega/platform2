package com.idega.block.forum.data;

import com.idega.data.*;
import java.sql.SQLException;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class ForumEmail extends GenericEntity {

  public ForumEmail() {
    super();
  }

  public ForumEmail(int id) throws SQLException{
    super(id);
  }

  public void initializeAttributes() {
    this.addAttribute(getIDColumnName());
    this.addAttribute(getEmailAddressColumnName(), "Póstfang",true,true,"java.lang.String");
    this.addAttribute(getForumIDColumnName(), "Forum ID",true,true,"java.lang.Integer");
    this.addAttribute(getThreadIDColumnName(), "Thread ID",true,true,"java.lang.Integer");
  }

  public void setDefaultValues(){
    this.setForumID(-1);
    this.setThreadID(-1);
  }

  public String getEntityName() {
    return "fo_forum_email";
  }

  public String getEmailAddressColumnName() {
    return "email_address";
  }

  public String getForumIDColumnName() {
    return "forum_id";
  }

  public String getThreadIDColumnName() {
    return "thread_id";
  }

  public void setEmailAddress(String address){
    this.setColumn(getEmailAddressColumnName(),address);
  }

  public void setForumID(int id){
    this.setColumn(getForumIDColumnName(),id);
  }

  public void setThreadID(int id){
    this.setColumn(getThreadIDColumnName(),id);
  }

  public String getEmailAddress()throws SQLException{
    return this.getStringColumnValue(this.getEmailAddressColumnName());
  }

  public int getForumID()throws SQLException{
    return this.getIntColumnValue(this.getForumIDColumnName());
  }

  public int getThreadID()throws SQLException{
    return this.getIntColumnValue(this.getThreadIDColumnName());
  }

} // Class ForumEmail
