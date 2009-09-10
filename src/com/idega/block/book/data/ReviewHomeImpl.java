package com.idega.block.book.data;


public class ReviewHomeImpl extends com.idega.data.IDOFactory implements ReviewHome
{
 protected Class getEntityInterfaceClass(){
  return Review.class;
 }


 public Review create() throws javax.ejb.CreateException{
  return (Review) super.createIDO();
 }


public java.util.Collection findAllReviewsForBook(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ReviewBMPBean)entity).ejbFindAllReviewsForBook(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findNewestReviewForBook(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ReviewBMPBean)entity).ejbFindNewestReviewForBook(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public Review findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Review) super.findByPrimaryKeyIDO(pk);
 }


public int getRatingTotal(int p0)throws com.idega.data.IDOException,javax.ejb.EJBException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((ReviewBMPBean)entity).ejbHomeGetRatingTotal(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfReviews(int p0)throws com.idega.data.IDOException,javax.ejb.EJBException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((ReviewBMPBean)entity).ejbHomeGetNumberOfReviews(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}