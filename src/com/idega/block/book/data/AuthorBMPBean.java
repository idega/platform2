package com.idega.block.book.data;

import javax.ejb.RemoveException;
import com.idega.data.IDORemoveRelationshipException;
import javax.ejb.FinderException;
import java.util.Collection;
import java.sql.Timestamp;
import com.idega.data.GenericEntity;

/**
 * Title:        Author bean
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author <a href="mailto:laddi@idega.is">��rhallur Helgason</a>
 * @version 1.0
 */

public class AuthorBMPBean extends GenericEntity implements Author {

  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(getColumnName(), "Name", true, true, String.class);
    addAttribute(getColumnDescription(), "Description", true, true, String.class,10000);
    addAttribute(getColumnImageID(), "Image", true, true, Integer.class);
    addAttribute(getColumnDate(),"Date added",true,true,Timestamp.class);
    setNullable(getColumnImageID(),true);
  }


  public String getIDColumnName(){ return "BO_AUTHOR_ID";}

  protected static String getEntityTableName(){ return "BO_AUTHOR";}
  protected static String getColumnName(){ return "AUTHOR_NAME";}
  protected static String getColumnImageID(){ return "IC_FILE_ID";}
  protected static String getColumnDescription(){ return "AUTHOR_DESCRIPTION";}
  protected static String getColumnDate(){ return "DATE_ADDED";}


  public String getEntityName(){
    return getEntityTableName();
  }

  public String getName(){
    return getStringColumnValue(getColumnName());
  }

  public void setName(String name){
    setColumn(getColumnName(), name);
  }

  public int getImage(){
    return getIntColumnValue(getColumnImageID());
  }

  public void setImageID(int imageID){
    setColumn(getColumnImageID(), imageID);
  }

  public String getDescription(){
    return getStringColumnValue(getColumnDescription());
  }

  public void setDescription(String description){
    setColumn(getColumnDescription(), description);
  }

  public Timestamp getDateAdded(){
    return (Timestamp) getColumnValue(getColumnDate());
  }

  public void setDateAdded(Timestamp date){
    setColumn(getColumnDate(),date);
  }

  public Collection ejbFindAllAuthors() throws FinderException {
    return super.idoFindIDsBySQL("select * from "+AuthorBMPBean.getEntityTableName());
  }

  public Collection ejbFindAllAuthorsByBook(int bookID) throws FinderException {
    return super.idoFindIDsBySQL("select a.* from "+getEntityTableName()+" a,"+BookBMPBean.getTableNameBookAuthor()+" ba where a."+getIDColumnName()+" = ba."+getIDColumnName()+" and ba.BO_BOOK_ID = "+String.valueOf(bookID));
  }

  public void remove() throws RemoveException{
    try{
      idoRemoveFrom(Book.class);
    }
    catch(IDORemoveRelationshipException e){
      throw new RemoveException(e.getMessage());
    }
    super.remove();
  }
}
