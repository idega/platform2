package com.idega.block.mailinglist.data;

import com.idega.data.*;
import java.sql.Timestamp;
import java.sql.SQLException;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public abstract class Account extends GenericEntity {


  public final static String CREATION_DATE = "creation_date";
  public final static String EMAIL = "email";
  public final static String REPLY_EMAIL = "reply_email";

  public final static String SMTP_HOST = "smtp_host";
  public final static String SMTP_PORT = "smtp_port";
  public final static String SMTP_LOGIN_NAME = "smtp_login_name";
  public final static String SMTP_PASSWORD = "smtp_password";

  public final static String POP3_HOST = "pop3_host";
  public final static String POP3_PORT = "pop3_port";
  public final static String POP3_LOGIN_NAME = "pop3_login_name";
  public final static String POP3_PASSWORD = "pop3_password";

  public Account() {
    super();
  }

  public Account(int id) throws SQLException{
    super(id);
  }

  protected void doInitializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(CREATION_DATE , "creation date", true, false, Timestamp.class);

    addAttribute(EMAIL, "Email", true, true, String.class);
    addAttribute(REPLY_EMAIL, "Reply email", true, true, String.class);

    addAttribute(SMTP_HOST, "SMTP server", true, true, String.class);
    addAttribute(SMTP_PORT, "SMTP port", true, true, Integer.class);
    addAttribute(SMTP_LOGIN_NAME, "SMTP login", true, true, String.class);
    addAttribute(SMTP_PASSWORD, "SMTP password", true, true, String.class);

    addAttribute(POP3_HOST, "POP3 server", true, true, String.class);
    addAttribute(POP3_PORT, "POP3 port", true, true, Integer.class);
    addAttribute(POP3_LOGIN_NAME, "POP3 login", true, true, String.class);
    addAttribute(POP3_PASSWORD, "POP3 password", true, true, String.class);
  }

  public abstract void initializeAttributes();

  public abstract String getEntityName();

  public abstract String getName();

  public String getEmail(){
    return ((!isNull(EMAIL)) ? getStringColumnValue(EMAIL) : "");
  }
  public String getReplyEmail(){
    return ((!isNull(REPLY_EMAIL)) ? getStringColumnValue(REPLY_EMAIL) : "");
  }

  public String getSMTPHost(){
    return ((!isNull(SMTP_HOST)) ? getStringColumnValue(SMTP_HOST) : "");
  }
  public int getSMTPPort(){
    return getIntColumnValue(SMTP_PORT);
  }
  public String getSMTPLoginName(){
    return ((!isNull(SMTP_LOGIN_NAME)) ? getStringColumnValue(SMTP_LOGIN_NAME) : "");
  }
  public String getSMTPPassword(){
    return ((!isNull(SMTP_PASSWORD)) ? getStringColumnValue(SMTP_PASSWORD) : "");
  }
  public String getPOP3Host(){
    return ((!isNull(POP3_HOST)) ? getStringColumnValue(POP3_HOST) : "");
  }
  public int getPOP3Port(){
    return getIntColumnValue(POP3_PORT);
  }
  public String getPOP3LoginName(){
    return ((!isNull(POP3_LOGIN_NAME)) ? getStringColumnValue(POP3_LOGIN_NAME) : "");
  }
  public String getPOP3Password(){
    return ((!isNull(POP3_PASSWORD)) ? getStringColumnValue(POP3_PASSWORD) : "");
  }
  public String getCreationDate(){
    return ((!isNull(CREATION_DATE)) ? getStringColumnValue(CREATION_DATE) : "");
  }


  public void setEmail(String email){
    setColumn(EMAIL, email);
  }
  public void setReplyEmail(String replyEmail){
    setColumn(REPLY_EMAIL, replyEmail);
  }

  public void setSMTPHost(String smtpHost){
    setColumn(SMTP_HOST, smtpHost);
  }
  public void setSMTPPort(int smtpPort){
    setColumn(SMTP_PORT, smtpPort);
  }
  public void setSMTPLoginName(String smtpLoginName){
    setColumn(SMTP_LOGIN_NAME, smtpLoginName);
  }
  public void setSMTPPassword(String smtpPassword){
    setColumn(SMTP_PASSWORD, smtpPassword);
  }
  public void setPOP3Host(String pop3Host){
    setColumn(POP3_HOST, pop3Host);
  }
  public void setPOP3Port(int pop3Port){
    setColumn(POP3_PORT, pop3Port);
  }
  public void setPOP3LoginName(String pop3LoginName){
    setColumn(POP3_LOGIN_NAME, pop3LoginName);
  }
  public void setPOP3Password(String pop3Password){
    setColumn(POP3_PASSWORD, pop3Password);
  }

  public void setCreationDate(Timestamp creationDate){
    setColumn(CREATION_DATE, creationDate);
  }

}