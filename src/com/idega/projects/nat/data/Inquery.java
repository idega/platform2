//idega 2001 - Gimmi

package com.idega.projects.nat.data;

import java.sql.*;
import com.idega.data.*;
import com.idega.core.data.*;

public class Inquery extends GenericEntity{

  public Inquery(){
          super();
  }
  public Inquery(int id)throws SQLException{
          super(id);
  }
  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(getNameColumnName(), "Name", true, true, String.class, 255);
    addAttribute(getEmailColumnName(), "Tölvupóstur", true, true, String.class, 255);
    addAttribute(getInqueryColumnName(), "Fyrirspurn",true ,true, String.class, 510);
    addAttribute(getInqueryDateColumnName(), "Dagur sem spurt er um", true ,true, java.sql.Date.class);
    addAttribute(getInqueryPostDateColumnName(), "Dagur þegar spurt var", true ,true, java.sql.Date.class);
    addAttribute(getAnsweredColumnName(), "Svarað", true,true, Boolean.class);
    addAttribute(getAnswerDateColumnName(), "Hvenær var svarað", true, true, java.sql.Date.class);
    addAttribute(getServiceIDColumnName(), "Vara", true, true, Integer.class, "many-to-one", Service.class);
  }


  public String getEntityName(){
    return getInqueryTableName();
  }
  public String getName(){
    return getNameColumnName();
  }

  public void setName(String name){
    setColumn(getNameColumnName(),name);
  }

  public Service getService() {
    return (Service) getColumnValue(getServiceIDColumnName());
  }

  public int getServiceID() {
    return getIntColumnValue(getServiceIDColumnName());
  }

  public void setServiceID(int id) {
    setColumn(getServiceIDColumnName(), id);
  }

  public String getEmail() {
    return getStringColumnValue(getEmailColumnName());
  }

  public void setEmail(String email) {
    setColumn(getEmailColumnName(), email);
  }

  public String getInquery() {
    return getStringColumnValue(getInqueryColumnName());
  }

  public void setInquery(String inquery) {
    setColumn(getInqueryColumnName(), inquery);
  }

  public Timestamp getInqueryDate() {
    return (Timestamp) getColumnValue(getInqueryDateColumnName());
  }

  public void setInqueryDate(Timestamp timestamp) {
    setColumn(getInqueryDateColumnName(), timestamp);
  }

  public Timestamp getInqueryPostDate() {
    return (Timestamp) getColumnValue(getInqueryPostDateColumnName());
  }

  public void setInqueryPostDate(Timestamp timestamp) {
    setColumn(getInqueryPostDateColumnName(), timestamp);
  }

  public boolean getIfAnswered() {
    return getAnswered();
  }

  public boolean getAnswered() {
    return getBooleanColumnValue(getAnsweredColumnName());
  }

  public void setIfAnswered(boolean answered) {
    setAnswered(answered);
  }

  public void setAnswered(boolean answered) {
    setColumn(getAnsweredColumnName(), answered);
  }

  public Timestamp getAnswerDate() {
    return (Timestamp) getColumnValue(getAnswerDateColumnName());
  }

  public void setAnswerDate(Timestamp timestamp) {
    setColumn(getAnswerDateColumnName(), timestamp);
  }

  public static String getInqueryTableName(){return "TB_INQUERY";}
  public static String getNameColumnName() {return "NAME";}
  public static String getEmailColumnName() {return "EMAIL";}
  public static String getInqueryColumnName() {return "INQUERY";}
  public static String getInqueryDateColumnName() {return "INQUERY_DATE";}
  public static String getInqueryPostDateColumnName() {return "INQUERY_POST_DATE";}
  public static String getAnsweredColumnName() {return "ANSWERED";}

  public static String getAnswerDateColumnName() {return "ANSWER_DATE";}

  public static String getServiceIDColumnName() {return "TB_SERVICE_ID";}





}
