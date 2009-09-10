package com.idega.block.book.data;


public class PublisherHomeImpl extends com.idega.data.IDOFactory implements PublisherHome
{
 protected Class getEntityInterfaceClass(){
  return Publisher.class;
 }


 public Publisher create() throws javax.ejb.CreateException{
  return (Publisher) super.createIDO();
 }


public java.util.Collection findAllPublishers()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PublisherBMPBean)entity).ejbFindAllPublishers();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public Publisher findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Publisher) super.findByPrimaryKeyIDO(pk);
 }



}