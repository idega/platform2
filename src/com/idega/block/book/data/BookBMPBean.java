package com.idega.block.book.data;

import com.idega.data.*;
import javax.ejb.RemoveException;
import java.rmi.RemoteException;

import com.idega.block.category.data.ICCategory;
import com.idega.core.file.data.ICFile;

import javax.ejb.FinderException;
import java.util.Collection;
import java.util.Iterator;
import java.sql.Timestamp;

/**
 * Title:        Book bean
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author <a href="mailto:laddi@idega.is">Þórhallur Helgason</a>
 * @version 1.0
 */

public class BookBMPBean extends GenericEntity implements Book {

  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(getColumnPublisherID(), "Publisher", true, true, Integer.class,"many-to-one",Publisher.class);
    addAttribute(getColumnName(), "Name", true, true, String.class);
    addAttribute(getColumnDescription(), "Description", true, true, String.class,10000);
    addAttribute(getColumnImageID(), "Image", true, true, Integer.class,"many-to-one",ICFile.class);
    addAttribute(getColumnPublished(),"Publish year",true,true,Integer.class);
    addAttribute(getColumnDate(),"Date added",true,true,Timestamp.class);
    setNullable(getColumnImageID(),true);
    setNullable(getColumnPublisherID(),true);
    addManyToManyRelationShip(Author.class,getTableNameBookAuthor());
    addManyToManyRelationShip(ICCategory.class,getTableNameBookCategory());
  }

  public String getIDColumnName(){ return "BO_BOOK_ID";}

  protected static String getEntityTableName(){ return "BO_BOOK";}
  protected static String getTableNameBookAuthor(){ return "BO_BOOK_AUTHOR";}
  protected static String getTableNameBookCategory(){ return "BO_BOOK_CATEGORY";}

  protected static String getColumnPublisherID(){ return "BO_PUBLISHER_ID";}
  protected static String getColumnName(){ return "BOOK_NAME";}
  protected static String getColumnImageID(){ return "IC_FILE_ID";}
  protected static String getColumnDescription(){ return "REVIEW";}
  protected static String getColumnPublished(){ return "PUBLISHED";}
  protected static String getColumnDate(){ return "DATE_ADDED";}


  public String getEntityName(){
    return getEntityTableName();
  }

  public int getPublisherID(){
    return getIntColumnValue(getColumnPublisherID());
  }

  public void setPublisherID(int publisherID){
    setColumn(getColumnPublisherID(), publisherID);
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

  public int getYear(){
    return getIntColumnValue(getColumnPublished());
  }

  public void setYear(int year){
    setColumn(getColumnPublished(), year);
  }

  public Timestamp getDateAdded(){
    return (Timestamp) getColumnValue(getColumnDate());
  }

  public void setDateAdded(Timestamp date){
    setColumn(getColumnDate(),date);
  }

  public Collection ejbFindAllNewestBooks(int[] categories,int numberOfReturns)throws FinderException{
    StringBuffer sql = new StringBuffer();
    sql.append("select b.* from ");
    sql.append(getEntityTableName()+" b,"+getTableNameBookCategory()+" bc");
    sql.append(" where ");
    sql.append("b."+getIDColumnName()+" = bc."+getIDColumnName());
    sql.append(" and bc.IC_CATEGORY_ID");
    sql.append(" in (");
    for ( int a = 0; a < categories.length; a++ ) {
      if ( a > 0 )
	sql.append(",");
      sql.append(categories[a]);
    }
    sql.append(") group by b.bo_book_id,b.bo_publisher_id,b.book_name,b.review,b.ic_file_id,b.date_added,b.published order by ");
    sql.append(getColumnDate());
    sql.append(" desc");
    return super.idoFindIDsBySQL(sql.toString(),numberOfReturns);
  }

  public Collection ejbFindAllBooksByPublisher(int publisherID)throws FinderException{
    return super.idoFindIDsBySQL("select * from "+this.getEntityTableName()+" where "+getColumnPublisherID()+" = "+String.valueOf(publisherID)+" order by "+getColumnPublished());
  }

  public Collection ejbFindAllBooksByYear(int year)throws FinderException{
    return super.idoFindIDsBySQL("select * from "+this.getEntityTableName()+" where "+getColumnPublished()+" = "+String.valueOf(year));
  }

  public Collection ejbFindAllBooksByAuthor(int authorID) throws FinderException {
    return super.idoFindIDsBySQL("select b.* from "+getEntityTableName()+" b,"+getTableNameBookAuthor()+" ba where b."+getIDColumnName()+" = ba."+getIDColumnName()+" and ba.BO_AUTHOR_ID = "+String.valueOf(authorID));
  }

  public Collection ejbFindAllBooksByCategory(int categoryID) throws FinderException {
    return super.idoFindIDsBySQL("select b.* from "+getEntityTableName()+" b,"+getTableNameBookCategory()+" c where b."+getIDColumnName()+" = c."+getIDColumnName()+" and c.IC_CATEGORY_ID = "+String.valueOf(categoryID));
  }

  public Collection ejbFindNewestBookByCategory(int categoryID) throws FinderException {
    return super.idoFindIDsBySQL("select b.* from "+getEntityTableName()+" b,"+getTableNameBookCategory()+" c where b."+getIDColumnName()+" = c."+getIDColumnName()+" and c.IC_CATEGORY_ID = "+String.valueOf(categoryID)+" order by "+getColumnDate()+" desc",1);
  }

  public Collection ejbFindAllBooksContaining(String name) throws FinderException {
    return super.idoFindIDsBySQL("select * from "+this.getEntityTableName()+" where "+getColumnName()+" like '%"+name+"%'");
  }

  public int ejbHomeGetNumberOfBooks(int categoryID) throws javax.ejb.EJBException,IDOException {
    return super.idoGetNumberOfRecords("select count(b."+getIDColumnName()+") from "+getEntityTableName()+" b,"+getTableNameBookCategory()+" c where b."+getIDColumnName()+" = c."+getIDColumnName()+" and c.IC_CATEGORY_ID = "+String.valueOf(categoryID));
  }

  public void addToCategory(ICCategory category) throws IDOException {
    super.idoAddTo(category);
  }

  public void addToAuthor(Author author) throws IDOException {
    super.idoAddTo(author);
  }

  public void removeFromAuthor() throws IDOException {
    idoRemoveFrom(Author.class);
  }

  public void removeFromCategory() throws IDOException {
    idoRemoveFrom(ICCategory.class);
  }

  public Collection findRelatedCategories() throws IDOException {
    return super.idoGetRelatedEntities(ICCategory.class);
  }

  public void remove() throws RemoveException {
    try {
      idoRemoveFrom(ICCategory.class);
      idoRemoveFrom(Author.class);
      Collection reviews = getReviewHome().findAllReviewsForBook(this.getID());
      Iterator iter = reviews.iterator();
      while (iter.hasNext()) {
	Review item = (Review)iter.next();
	item.remove();
      }
      //SimpleQuerier.execute("delete from "+ReviewBMPBean.getEntityTableName()+" where "+getIDColumnName()+" = "+String.valueOf(((Integer)this.getPrimaryKey()).intValue()));
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
      throw new RemoveException(e.getMessage());
    }
    super.remove();
  }

  public ReviewHome getReviewHome()throws RemoteException{
    return (ReviewHome)IDOLookup.getHome(Review.class);
  }
}