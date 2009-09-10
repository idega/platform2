package com.idega.block.documents.data;



import java.sql.SQLException;
import java.sql.Timestamp;

import com.idega.core.builder.data.ICPage;
import com.idega.core.data.GenericGroup;
import com.idega.core.file.data.ICFile;
import com.idega.core.user.business.UserBusiness;
import com.idega.core.user.data.User;



public class DocVersionBMPBean extends com.idega.data.GenericEntity implements com.idega.block.documents.data.DocVersion {



  public DocVersionBMPBean(){

    super();

  }



  public DocVersionBMPBean(int id)throws SQLException{

    super(id);

  }



  public void initializeAttributes(){

    addAttribute(getIDColumnName());

    this.addManyToOneRelationship(getColumnNameDocLinkID(), "Version of", DocLink.class);

    this.addManyToOneRelationship(getColumnNameFileID(), "File", ICFile.class);

    this.addManyToOneRelationship(getColumnNamePageID(), "Page", ICPage.class);

    this.addAttribute(getColumnNameCreationDate(), "Creation Date", true, true, Timestamp.class);

    this.addManyToOneRelationship(getColumnNameUserID(), "User", GenericGroup.class);



  }



  public static String getColumnNameDocLinkID() { return "VERSION_OF"; }

  public static String getColumnNameFileID() { return "IC_FILE_ID"; }

  public static String getColumnNamePageID() { return "IB_PAGE_ID"; }

  public static String getColumnNameCreationDate() { return "CREATION_DATE"; }

  public static String getColumnNameUserID(){ return "OWNER_ID";}



  public static String getEntityTableName() { return "DOC_VERSION"; }



  public String getIDColumnName(){

    return getColumnNameDocLinkID();

  }



  public String getEntityName(){

    return getEntityTableName();

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



  public int getUserID() throws SQLException {

    int id = getIntColumnValue(getColumnNameUserID());

    if(id != -1){

      User user = UserBusiness.getUser(id);



      return user.getID();

    }

    return id;

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

