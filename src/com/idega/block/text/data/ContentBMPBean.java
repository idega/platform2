//idega 2000 - Laddi
package com.idega.block.text.data;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;

import com.idega.core.file.data.ICFile;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOLegacyEntity;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class ContentBMPBean extends com.idega.data.GenericEntity implements com.idega.block.text.data.Content {

  public ContentBMPBean(){
    super();
  }

  public ContentBMPBean(int id)throws SQLException{
    super(id);
  }

  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(getColumnNameUserId(), "User",true,true, java.lang.Integer.class,"many-to-one",com.idega.core.user.data.User.class);
    addAttribute(getColumnNameCreated(),"Created", true, true, java.sql.Timestamp.class);
    addAttribute(getColumnNameUpdated(),"Updated", true, true, java.sql.Timestamp.class);
    addAttribute(getColumnNamePublishFrom(), "Publish from", true, true, java.sql.Timestamp.class);
    addAttribute(getColumnNamePublishTo(), "Publish to", true, true, java.sql.Timestamp.class);
    addManyToManyRelationShip(LocalizedText.class);
    addManyToManyRelationShip(ICFile.class,"TX_CONTENT_IC_FILE");
  }

	public String getLocalizedTextMiddleTableName(IDOLegacyEntity returning,IDOLegacyEntity from){
	  return getNameOfMiddleTable(((com.idega.block.text.data.LocalizedTextHome)com.idega.data.IDOLookup.getHomeLegacy(LocalizedText.class)).createLegacy(),((com.idega.block.text.data.ContentHome)com.idega.data.IDOLookup.getHomeLegacy(Content.class)).createLegacy());
	}

  public static String getEntityTableName(){ return "TX_CONTENT";}

  public static String getColumnNameUserId(){     return "IC_USER_ID";}
  public static String getColumnNameTitle(){      return "TITLE";}
  public static String getColumnNameCreated(){    return "CREATED";}
  public static String getColumnNameUpdated(){    return "UPDATED";}
  public static String getColumnNamePublishFrom(){return "PUBLISH_FROM";}
  public static String getColumnNamePublishTo(){  return "PUBLISH_TO";}

  public String getEntityName(){
    return getEntityTableName();
  }
  public int getUserId(){
    return getIntColumnValue(getColumnNameUserId());
  }
  public void setUserId(int id){
    setColumn(getColumnNameUserId(),id);
  }
  public void setUserId(Integer id){
    setColumn(getColumnNameUserId(),id);
  }
  public Timestamp getCreated(){
    return (java.sql.Timestamp) getColumnValue(getColumnNameCreated());
  }
  public void setCreated(Timestamp stamp){
    setColumn(getColumnNameCreated(), stamp);
  }
  public Timestamp getLastUpdated(){
    return (Timestamp) getColumnValue(getColumnNameUpdated());
  }
  public void setLastUpdated(java.sql.Timestamp stamp){
    setColumn(getColumnNameUpdated(), stamp);
  }
  public Timestamp getPublishFrom(){
    return (Timestamp) getColumnValue(getColumnNamePublishFrom());
  }
  public void setPublishFrom(java.sql.Timestamp publish_from){
    setColumn(getColumnNamePublishFrom() ,publish_from);
  }
  public Timestamp getPublishTo(){
    return (Timestamp) getColumnValue(getColumnNamePublishTo());
  }
  public void setPublishTo(Timestamp publish_to){
    setColumn(getColumnNamePublishTo(),publish_to);
  }
  
  public Collection getContentFiles() throws IDORelationshipException{
  	return idoGetRelatedEntities(ICFile.class);
  }
  
  public void addFileToContent(ICFile file) throws IDOAddRelationshipException{
  	idoAddTo(file);
  }
  
  public void removeFileFromContent(ICFile file) throws IDORemoveRelationshipException{
	idoRemoveFrom(file);
  }
  
}
