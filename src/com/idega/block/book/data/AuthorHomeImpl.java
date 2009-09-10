package com.idega.block.book.data;


public class AuthorHomeImpl extends com.idega.data.IDOFactory implements AuthorHome
{
 protected Class getEntityInterfaceClass(){
  return Author.class;
 }


 public Author create() throws javax.ejb.CreateException{
  return (Author) super.createIDO();
 }


public java.util.Collection findAllAuthors()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AuthorBMPBean)entity).ejbFindAllAuthors();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllAuthorsByBook(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AuthorBMPBean)entity).ejbFindAllAuthorsByBook(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public Author findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Author) super.findByPrimaryKeyIDO(pk);
 }



}