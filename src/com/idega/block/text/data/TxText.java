//idega 2000 - Laddi
package com.idega.block.text.data;

import java.sql.*;
import com.idega.data.*;
import com.idega.util.idegaTimestamp;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class TxText extends GenericEntity{

  public TxText(){
    super();
  }

  public TxText(int id)throws SQLException{
    super(id);
  }

  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(getColumnNameHeadline(), "Headline", true, true, java.lang.String.class);
    addAttribute(getColumnNameUserId(),"User",true,true, java.lang.Integer.class,"many-to-one",com.idega.core.user.data.User.class);
    addAttribute(getColumnNameTitle(), "Title", true, true, java.lang.String.class);
    addAttribute(getColumnNameBody(), "Body", true, true, java.lang.String.class,30000);
    addAttribute(getColumnNameCreated(), "Created", true, true, java.sql.Timestamp.class);
    addAttribute(getColumnNameUpdated(), "Headline", true, true, java.sql.Timestamp.class);
    addAttribute(getColumnNameIncludeImage(), "Include image", true, true, java.lang.Boolean.class,1);
    addAttribute(getColumnNameImageId(), "image_id", true, true, java.lang.Integer.class);
    addAttribute(getColumnNameAttribute(), "attribute", true, true, java.lang.String.class);

    this.addManyToManyRelationShip(com.idega.core.data.ICObjectInstance.class,"TX_TEXT_IC_OBJECT_INSTANCE");
    this.addManyToManyRelationShip(LocalizedText.class,"TX_TEXT_TX_LOCALIZED_TEXT");
  }


  public static String getEntityTableName(){ return "TX_TEXT";}

  public static String getColumnNameHeadline(){ return "HEADLINE";}
  public static String getColumnNameUserId(){ return "IC_USER_ID";}
  public static String getColumnNameTitle(){ return "TITLE";}
  public static String getColumnNameAttribute(){ return "ATTRIBUTE";}
  public static String getColumnNameBody(){ return "BODY";}
  public static String getColumnNameCreated(){ return "CREATED";}
  public static String getColumnNameUpdated(){ return "UPDATED";}
  public static String getColumnNameIncludeImage(){ return "INCLUDE_IMAGE";}
  public static String getColumnNameImageId(){ return "IMAGE_ID";}

  public String getEntityName(){
    return getEntityTableName();
  }
  public int getUserId(){
    return getIntColumnValue(getColumnNameImageId());
  }
  public void setUserId(int id){
    setColumn(getColumnNameImageId(),id);
  }
  public void setUserId(Integer id){
    setColumn(getColumnNameImageId(),id);
  }
  public int getImageId(){
    return getIntColumnValue(getColumnNameImageId());
  }
  public void setImageId(int id){
    setColumn(getColumnNameImageId(),id);
  }
  public void setImageId(Integer id){
    setColumn(getColumnNameImageId(),id);
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
  public String getAttribute(){
    return getStringColumnValue(getColumnNameAttribute());
  }
  public void setAttribute(String attribute){
    setColumn(getColumnNameAttribute(), attribute);
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
  public boolean getIncludeImage(){
    return getBooleanColumnValue(getColumnNameIncludeImage());
  }
  public void  setIncludeImage(boolean include){
    setColumn(getColumnNameIncludeImage(),include);
  }
  public void setIncludeImage(Boolean include){
    setColumn(getColumnNameIncludeImage(),include);
  }
}
