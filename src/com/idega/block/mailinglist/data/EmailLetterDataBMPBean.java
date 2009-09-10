package com.idega.block.mailinglist.data;

/**
 * Title:        idegaWeb Classes
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="bjarni@idega.is">Bjarni Viljhalmsson</a>
 * @version 1.0
 */
import java.sql.SQLException;
import java.sql.Timestamp;

import com.idega.core.file.data.ICFile;

public class EmailLetterDataBMPBean extends com.idega.data.GenericEntity implements com.idega.block.mailinglist.data.EmailLetterData {


  public final static String EMAIL_LETTER_DATA_SUBJECT = "subject";
  public final static String EMAIL_LETTER_DATA_BODY = "body";
  public final static String EMAIL_LETTER_DATA_SENT = "sent";
  public final static String EMAIL_LETTER_DATA_ATTACHMENTS = "attachments";
  public final static String EMAIL_LETTER_DATA_DATE = "creation_date";
  public final static String EMAIL_LETTER_DATA_FROM = "from_email";
  public final static String EMAIL_LETTER_DATA_TO = "to_email";
  public final static String EMAIL_LETTER_DATA_BCC = "bbc_email";
  public final static String EMAIL_LETTER_DATA_CC = "cc_email";

  public EmailLetterDataBMPBean() {
    super();
  }

  public EmailLetterDataBMPBean(int id)throws SQLException{
    super(id);
  }

  public void setSubject (String subject){
    setColumn(EMAIL_LETTER_DATA_SUBJECT, subject);
  }

  public void setBody (String body){
    setColumn(EMAIL_LETTER_DATA_BODY, body);
  }

  public void setHasSent(Boolean sent) {
    setColumn(EMAIL_LETTER_DATA_SENT, sent);
  }

  public void setAttachments(ICFile attachments) {
    setColumn(EMAIL_LETTER_DATA_ATTACHMENTS, attachments);
  }

  public void setToEmail(String toEmail) {
    setColumn(EMAIL_LETTER_DATA_TO, toEmail);
  }

  public void setFromEmail(String fromEmail) {
    setColumn(EMAIL_LETTER_DATA_FROM, fromEmail);
  }

  public void setBCCEmail(String bccEmail) {
    setColumn(EMAIL_LETTER_DATA_BCC, bccEmail);
  }

  public void setCCEmail(String ccEmail) {
    setColumn(EMAIL_LETTER_DATA_CC, ccEmail);
  }

  public void setDate(Timestamp date) {
    setColumn(EMAIL_LETTER_DATA_DATE, date);
  }

  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(EMAIL_LETTER_DATA_TO, "to", true, true, String.class);
    addAttribute(EMAIL_LETTER_DATA_FROM, "from", true, false, String.class);
    addAttribute(EMAIL_LETTER_DATA_CC, "cc", true, false, String.class);
    addAttribute(EMAIL_LETTER_DATA_BCC, "bcc", true, false, String.class);
    addAttribute(EMAIL_LETTER_DATA_DATE, "date", true, false, "java.sql.Timestamp");
    addAttribute(EMAIL_LETTER_DATA_SUBJECT, "subject", true, true, String.class);
    addAttribute(EMAIL_LETTER_DATA_BODY, "body", true, false, java.lang.String.class, 10000);
    addAttribute(EMAIL_LETTER_DATA_SENT, "sent", true, false, Boolean.class);
    addAttribute(EMAIL_LETTER_DATA_ATTACHMENTS, "attachments", true, false, com.idega.core.file.data.ICFile.class);
  }

  public String getEntityName(){
    return "emailLetterData";
  }

  public String getCCEmail(){
    if (!isNull(EMAIL_LETTER_DATA_CC)) {
		return getStringColumnValue(EMAIL_LETTER_DATA_CC);
	}
	else {
		return "";
	}
  }

  public String getBCCEmail(){
    if (!isNull(EMAIL_LETTER_DATA_BCC)) {
		return getStringColumnValue(EMAIL_LETTER_DATA_BCC);
	}
	else {
		return "";
	}
  }

  public String getSubject(){
    if (!isNull(EMAIL_LETTER_DATA_SUBJECT)) {
		return getStringColumnValue(EMAIL_LETTER_DATA_SUBJECT);
	}
	else {
		return "";
	}
  }

  public String getBody(){
    if (!isNull(EMAIL_LETTER_DATA_BODY)) {
		return getStringColumnValue(EMAIL_LETTER_DATA_BODY);
	}
	else {
		return "";
	}
  }

  public boolean getHasSent(){
    /*if (!isNull(EMAIL_LETTER_DATA_SENT))*/     return getBooleanColumnValue(EMAIL_LETTER_DATA_SENT);
  }

  public ICFile getAttachments(){
    /*if (!isNull(EMAIL_LETTER_DATA_ATTACHMENTS))*/    return (ICFile) getColumnValue(EMAIL_LETTER_DATA_ATTACHMENTS);
  }

  public String getToEmail(){
    if (!isNull(EMAIL_LETTER_DATA_TO)) {
		return getStringColumnValue(EMAIL_LETTER_DATA_TO);
	}
	else {
		return "";
	}
  }

  public String getFromEmail(){
    if (!isNull(EMAIL_LETTER_DATA_FROM)) {
		return getStringColumnValue(EMAIL_LETTER_DATA_FROM);
	}
	else {
		return "";
	}
  }

  public String getDate(){
     if (!isNull(EMAIL_LETTER_DATA_DATE)) {
		return getStringColumnValue(EMAIL_LETTER_DATA_DATE);
	}
	else {
		return "";
	}
  }
}
