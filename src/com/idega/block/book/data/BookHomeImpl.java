package com.idega.block.book.data;


public class BookHomeImpl extends com.idega.data.IDOFactory implements BookHome
{
 protected Class getEntityInterfaceClass(){
  return Book.class;
 }


 public Book create() throws javax.ejb.CreateException{
  return (Book) super.createIDO();
 }


public java.util.Collection findAllBooksContaining(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((BookBMPBean)entity).ejbFindAllBooksContaining(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllBooksByPublisher(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((BookBMPBean)entity).ejbFindAllBooksByPublisher(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllBooksByAuthor(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((BookBMPBean)entity).ejbFindAllBooksByAuthor(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllNewestBooks(int[] p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((BookBMPBean)entity).ejbFindAllNewestBooks(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllBooksByCategory(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((BookBMPBean)entity).ejbFindAllBooksByCategory(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findNewestBookByCategory(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((BookBMPBean)entity).ejbFindNewestBookByCategory(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllBooksByYear(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((BookBMPBean)entity).ejbFindAllBooksByYear(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public Book findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Book) super.findByPrimaryKeyIDO(pk);
 }


public int getNumberOfBooks(int p0)throws com.idega.data.IDOException,javax.ejb.EJBException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((BookBMPBean)entity).ejbHomeGetNumberOfBooks(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}