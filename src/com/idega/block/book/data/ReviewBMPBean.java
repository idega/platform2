package com.idega.block.book.data;

import com.idega.data.IDOException;
import javax.ejb.FinderException;
import java.util.Collection;
import java.sql.Timestamp;
import com.idega.data.GenericEntity;

/**
 * Title:        Review bean
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author <a href="mailto:laddi@idega.is">��rhallur Helgason</a>
 * @version 1.0
 */

public class ReviewBMPBean extends GenericEntity implements Review {

  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(getColumnBookID(), "Book ID", true, true, Integer.class,"many-to-one",Book.class);
    addAttribute(getColumnName(), "Name", true, true, String.class);
    addAttribute(getColumnReview(), "Review", true, true, String.class,4000);
    addAttribute(getColumnRating(), "Rating", true, true, Integer.class);
    addAttribute(getColumnDate(),"Date",true,true,Timestamp.class);
  }


  public String getIDColumnName(){ return "BO_REVIEW_ID";}

  protected static String getEntityTableName(){ return "BO_REVIEW";}
  protected static String getColumnBookID(){ return "BO_BOOK_ID";}
  protected static String getColumnName(){ return "REVIEWER_NAME";}
  protected static String getColumnReview(){ return "REVIEW";}
  protected static String getColumnRating(){ return "RATING";}
  protected static String getColumnDate(){ return "DATE_ADDED";}


  public String getEntityName(){
    return getEntityTableName();
  }

  public int getBookID(){
    return getIntColumnValue(getColumnBookID());
  }

  public void setBookID(int bookID){
    setColumn(getColumnBookID(), bookID);
  }

  public String getName(){
    return getStringColumnValue(getColumnName());
  }

  public void setName(String name){
    setColumn(getColumnName(), name);
  }

  public String getReview(){
    return getStringColumnValue(getColumnReview());
  }

  public void setReview(String review){
    setColumn(getColumnReview(), review);
  }

  public int getRating(){
    return getIntColumnValue(getColumnRating());
  }

  public void setRating(int rating){
    setColumn(getColumnRating(), rating);
  }

  public Timestamp getDateAdded(){
    return (Timestamp) getColumnValue(getColumnDate());
  }

  public void setDateAdded(Timestamp date){
    setColumn(getColumnDate(),date);
  }

  public Collection ejbFindAllReviewsForBook(int bookID)throws FinderException{
    return super.idoFindIDsBySQL("select * from "+ReviewBMPBean.getEntityTableName()+" where "+getColumnBookID()+" = "+String.valueOf(bookID)+" order by "+getColumnDate()+" desc");
  }

  public Collection ejbFindNewestReviewForBook(int bookID)throws FinderException{
    return super.idoFindIDsBySQL("select * from "+ReviewBMPBean.getEntityTableName()+" where "+getColumnBookID()+" = "+String.valueOf(bookID)+" order by +"+getColumnDate()+" desc",1);
  }

  public int ejbHomeGetNumberOfReviews(int bookID) throws javax.ejb.EJBException,IDOException {
    return super.idoGetNumberOfRecords("select count(*) from "+ReviewBMPBean.getEntityTableName()+" where "+getColumnBookID()+" = "+String.valueOf(bookID));
  }

  public int ejbHomeGetRatingTotal(int bookID) throws javax.ejb.EJBException,IDOException {
    return super.idoGetNumberOfRecords("select sum("+getColumnRating()+") from "+ReviewBMPBean.getEntityTableName()+" where "+getColumnBookID()+" = "+String.valueOf(bookID));
  }
}
