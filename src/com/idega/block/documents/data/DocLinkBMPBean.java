package com.idega.block.documents.data;



import java.sql.SQLException;
import java.sql.Timestamp;

import com.idega.block.category.data.ICInformationCategory;
import com.idega.block.category.data.ICInformationFolder;
import com.idega.core.builder.data.ICPage;
import com.idega.core.data.GenericGroup;
import com.idega.core.file.data.ICFile;
import com.idega.core.user.data.User;


public class DocLinkBMPBean extends com.idega.data.GenericEntity implements com.idega.block.documents.data.DocLink {



  public DocLinkBMPBean(){

    super();

  }



  public DocLinkBMPBean(int id)throws SQLException{

    super(id);

  }



  public void initializeAttributes(){

    addAttribute(getIDColumnName());

    addAttribute(getColumnNameDocLinkName(), "name", true, true, String.class);

    this.addManyToOneRelationship(getColumnNameFolderID(), "Info Folder", ICInformationFolder.class);

    this.addManyToOneRelationship(getColumnNameCatID(), "Category", ICInformationCategory.class);

    addAttribute(getColumnNameURL(), "URL", true, true, String.class);

    this.addManyToOneRelationship(getColumnNameFileID(), "File", ICFile.class);

    this.addManyToOneRelationship(getColumnNamePageID(), "Page", ICPage.class);

    addAttribute(getColumnNameTarget(), "Target", true, true, String.class);

    addAttribute(getColumnNameCreationDate(), "Creation Date", true, true, Timestamp.class);

    this.addManyToOneRelationship(getColumnNameUserID(), "User", GenericGroup.class);

  }





  public static String getColumnNameDocLinkName() { return "doc_link_name"; }

  public static String getColumnNameFolderID() { return "IC_INFO_FOLDER_ID"; }

  public static String getColumnNameCatID() {return "IC_INFO_CATEGORY_ID";}

  public static String getColumnNameDocLinkID() { return "DOC_LINK_ID"; }

  public static String getColumnNameURL() { return "URL"; }

  public static String getColumnNameTarget() { return "TARGET"; }

  public static String getColumnNameFileID() { return "IC_FILE_ID"; }

  public static String getColumnNamePageID() { return "IB_PAGE_ID"; }

  public static String getColumnNameCreationDate() { return "CREATION_DATE"; }

  public static String getColumnNameUserID(){ return "OWNER_ID";}



  public static String getEntityTableName() { return "DOC_LINK"; }



  public String getIDColumnName(){

    return getColumnNameDocLinkID();

  }



  public String getEntityName(){

    return getEntityTableName();

  }





  public String getName() {

    return (String) getColumnValue(getColumnNameDocLinkName());

  }



  public int getFolderID() {

    return getIntColumnValue(getColumnNameFolderID());

  }



  public int getCategoryID() {

    return getIntColumnValue(getColumnNameCatID());

  }



  public String getURL() {

    return (String) getColumnValue(getColumnNameURL());

  }



  public String getTarget() {

    return (String) getColumnValue(getColumnNameTarget());

  }



  public int getFileID() {

    return getIntColumnValue(getColumnNameFileID());

  }



  public int getPageID() {

    return getIntColumnValue(getColumnNamePageID());

  }



  public Timestamp getCreationDate() {

    return (Timestamp) getColumnValue(getColumnNameCreationDate());

  }



  public int getUserGroupID() {

    return getIntColumnValue(getColumnNameUserID());

  }





  public void setName(String name) {

    setColumn(getColumnNameDocLinkName(),name);

  }



  public void setFolderID(int folderID) {

    setColumn(getColumnNameFolderID(),folderID);

  }



  public void setCategoryID(int categoryID) {

    setColumn(getColumnNameCatID(),categoryID);

  }



  public void setURL(String URL) {

    setColumn(getColumnNameURL(),URL);

  }



  public void setTarget(String target) {

    setColumn(getColumnNameTarget(),target);

  }



  public void setFileID(int ICFileID) {

    setColumn(getColumnNameFileID(),ICFileID);

  }



  public void setPageID(int IBPageID) {

    setColumn(getColumnNamePageID(),IBPageID);

  }



  public void setCreationDate(Timestamp date) {

    setColumn(getColumnNameCreationDate(),date);

  }



  public void setUser(User user) {

    setColumn(getColumnNameUserID(),user.getGroupID());

  }



  public void delete() throws SQLException{

    super.delete();

  }

}

