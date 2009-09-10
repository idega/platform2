 package com.idega.block.mailinglist.data;

 import java.sql.SQLException;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class MailinglistBMPBean extends com.idega.block.mailinglist.data.AccountBMPBean implements com.idega.block.mailinglist.data.Mailinglist {

  public final static String MAILINGLIST_NAME = "mailinglist_name";

  public MailinglistBMPBean() {
    super();
  }

  public MailinglistBMPBean(int id) throws SQLException{
    super(id);
  }

  public void initializeAttributes() {
    super.initializeAttributes();
    addAttribute(MAILINGLIST_NAME , "Mailinglist name", true, true, String.class);
  }

  public String getEntityName() {
    return "Mailinglist";
  }
  public String getName(){
    return getMailinglistName();
  }
  public String getMailinglistName(){
    return ((!isNull(MAILINGLIST_NAME)) ? getStringColumnValue(MAILINGLIST_NAME) : "");
  }

  public void setMailinglistName(String mailinglistName){
    setColumn(MAILINGLIST_NAME, mailinglistName);
  }
}
