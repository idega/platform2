package com.idega.block.mailinglist.data;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.lang.Integer;
import java.lang.String;
import com.idega.data.*;
import com.idega.core.user.data.User;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class MailAccount extends Account {

  public final static String USER_ID = "IC_USER_ID";
  public final static String USER_NAME = "USER_NAME";

  public MailAccount() {
    super();
  }

  public MailAccount(int id) throws SQLException{
    super(id);
  }

  public void initializeAttributes() {
    this.doInitializeAttributes();
    addAttribute(USER_ID , "User", true, false, Integer.class, "many_to_one", User.class);
    addAttribute(USER_NAME , "User name", true, true, String.class);
    addManyToManyRelationShip(Mailinglist.class);
  }
  public String getName(){
    return getUserName();
  }

  public String getEntityName() {
    return "MailAccount";
  }

  public int getUserID(){
    return getIntColumnValue(USER_ID);
  }
  public String getUserName(){
    return ((!isNull(USER_NAME)) ? getStringColumnValue(USER_NAME) : "");
  }
  public void setUserID(int userID){
    setColumn(USER_ID, userID);
  }
  public void setUserName(String userName){
    setColumn(USER_NAME, userName);
  }
}