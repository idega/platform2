package is.idega.idegaweb.campus.block.mailinglist.data;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

import java.sql.SQLException;

public class EmailLetterBMPBean extends com.idega.data.GenericEntity implements is.idega.idegaweb.campus.block.mailinglist.data.EmailLetter {

  public final static String TableName = "cam_email_letter";
  public final static String EMAIL_KEY = "email_key";
  public final static String SUBJECT = "subject";
  public final static String BODY = "body";
  public final static String FROM = "from_email";
  public final static String PARSE = "parse";
  public final static String ONLYUSER = "only_user";
  public final static String HOST = "host";
  public final static String TYPE = "mail_type";

  public EmailLetterBMPBean() {
    super();
  }
  public EmailLetterBMPBean(int id)throws SQLException{
    super(id);
  }
  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(FROM, "from", true, true, String.class);
    addAttribute(EMAIL_KEY, "body", true, true, java.lang.String.class);
    addAttribute(SUBJECT, "subject", true, true, java.lang.String.class);
    addAttribute(BODY, "body", true, true, java.lang.String.class,4000);
    addAttribute(PARSE,"parse",true,true,java.lang.Boolean.class);
    addAttribute(ONLYUSER,"Only user",true,true,java.lang.Boolean.class);
    addAttribute(HOST,"host",true,true,java.lang.String.class);
    addAttribute(TYPE,"type",true,true,java.lang.String.class);
    addManyToManyRelationShip(MailingList.class);

  }
  public String getEntityName(){
    return TableName;
  }
  public String getEmailKey(){
    return getStringColumnValue(EMAIL_KEY);
  }
  public void setEmailKey(String key){
    setColumn(EMAIL_KEY,key);
  }
  public String getBody(){
    return getStringColumnValue(BODY);
  }
  public void setBody(String body){
    setColumn(BODY,body);
  }
  public String getSubject(){
    return getStringColumnValue(SUBJECT);
  }
  public void setSubject(String subject){
    setColumn(SUBJECT,subject);
  }
  public String getFrom(){
    return getStringColumnValue(FROM);
  }
  public void setFrom(String from){
    setColumn(FROM,from);
  }
  public String getHost(){
    return getStringColumnValue(HOST);
  }
  public void setHost(String host){
    setColumn(HOST,host);
  }
  public String getType(){
    return getStringColumnValue(TYPE);
  }
  public void setType(String type){
    setColumn(TYPE,type);
  }
  public boolean getParse(){
    return getBooleanColumnValue(PARSE);
  }
  public void setParse(boolean parse){
    setColumn(PARSE,parse);
  }

  public boolean getOnlyUser(){
    return getBooleanColumnValue(ONLYUSER);
  }
  public void setOnlyUser(boolean onlyUser){
    setColumn(ONLYUSER,onlyUser);
  }

  public String getSubjectKey(){
    return getEmailKey()+"_subject";
  }

}
