//idega 2000 - Laddi
package com.idega.block.text.data;

import java.sql.*;
import com.idega.data.*;
import com.idega.util.idegaTimestamp;

public class LocalizedText extends GenericEntity{

  public LocalizedText(){
    super();
    setBody("");
  }

  public LocalizedText(int id)throws SQLException{
    super(id);
    setBody("");
  }

  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(getColumnNameLocaleId(), "Locale", true, true, java.lang.Integer.class,"many_to_one",com.idega.core.data.ICLocale.class);
    addAttribute(getColumnNameHeadline(), "Headline", true, true, java.lang.String.class);
    addAttribute(getColumnNameTitle(), "Title", true, true, java.lang.String.class);
    addAttribute(getColumnNameBody(), "Body", true, true, java.lang.String.class,30000);
    addAttribute(getColumnNameCreated(), "Created", true, true, java.sql.Timestamp.class);
    addAttribute(getColumnNameUpdated(), "Updated", true, true, java.sql.Timestamp.class);
  }

  public static String getEntityTableName(){ return "TX_LOCALIZED_TEXT";}
  public static String getColumnNameLocaleId(){ return "IC_LOCALE_ID";}
  public static String getColumnNameHeadline(){ return "HEADLINE";}
  public static String getColumnNameTitle(){ return "TITLE";}
  public static String getColumnNameBody(){ return "BODY";}
  public static String getColumnNameCreated(){ return "CREATED";}
  public static String getColumnNameUpdated(){ return "UPDATED";}


  public String getEntityName(){
    return getEntityTableName();
  }
  public int getLocaleId(){
    return getIntColumnValue(getColumnNameLocaleId());
  }
  public void setLocaleId(int id){
    setColumn(getColumnNameLocaleId(),id);
  }
  public void setLocaleId(Integer id){
    setColumn(getColumnNameLocaleId(),id);
  }
  public String getHeadline(){
    return getStringColumnValue(getColumnNameHeadline());
  }
  public void setHeadline(String headline){
    setColumn(getColumnNameHeadline(), headline);
  }
  public String getTitle(){
    return getStringColumnValue(getColumnNameTitle());
  }
  public void setTitle(String title){
    setColumn(getColumnNameTitle(), title);
  }
  public String getBody(){
    return getStringColumnValue(getColumnNameBody());
  }
  public void setBody(String body){
    setColumn(getColumnNameBody(), body);
  }
  public java.sql.Timestamp getCreated(){
    return (java.sql.Timestamp) getColumnValue(getColumnNameCreated());
  }
  public void setCreated(java.sql.Timestamp stamp){
    setColumn(getColumnNameCreated(), stamp);
  }
  public java.sql.Timestamp getUpdated(){
    return (java.sql.Timestamp) getColumnValue(getColumnNameUpdated());
  }
  public void setUpdated(java.sql.Timestamp stamp){
    setColumn(getColumnNameUpdated(), stamp);
  }

}
