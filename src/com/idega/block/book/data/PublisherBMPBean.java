package com.idega.block.book.data;

import java.util.Collection;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.core.file.data.ICFile;
import com.idega.data.GenericEntity;
import com.idega.data.IDORemoveException;
import com.idega.data.SimpleQuerier;

/**
 * Title:        Publisher bean
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author <a href="mailto:laddi@idega.is">��rhallur Helgason</a>
 * @version 1.0
 */

public class PublisherBMPBean extends GenericEntity implements Publisher {

  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(getColumnName(), "Name", true, true, String.class);
    addAttribute(getColumnDescription(), "Description", true, true, String.class,10000);
    addAttribute(getColumnImageID(), "Image", true, true, Integer.class,"many-to-one",ICFile.class);
  }


  public String getIDColumnName(){ return "BO_PUBLISHER_ID";}

  protected static String getEntityTableName(){ return "BO_PUBLISHER";}
  protected static String getColumnName(){ return "PUBLISHER_NAME";}
  protected static String getColumnDescription(){ return "PUBLISHER_DESCRIPTION";}
  protected static String getColumnImageID(){ return "IC_FILE_ID";}


  public String getEntityName(){
    return getEntityTableName();
  }

  public String getName(){
    return getStringColumnValue(getColumnName());
  }

  public void setName(String name){
    setColumn(getColumnName(), name);
  }

  public String getDescription(){
    return getStringColumnValue(getColumnDescription());
  }

  public void setDescription(String description){
    setColumn(getColumnDescription(), description);
  }

  public int getImage(){
    return getIntColumnValue(getColumnImageID());
  }

  public void setImageID(int imageID){
    setColumn(getColumnImageID(), imageID);
  }

  public Collection ejbFindAllPublishers() throws FinderException {
    return super.idoFindIDsBySQL("select * from "+PublisherBMPBean.getEntityTableName());
  }

  public void remove() throws IDORemoveException,RemoveException {
    try {
      SimpleQuerier.execute("update "+BookBMPBean.getEntityTableName()+" set "+this.getIDColumnName()+" = null where "+getIDColumnName()+" = "+String.valueOf(((Integer)this.getPrimaryKey()).intValue()));
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
    super.remove();
  }
}
