package com.idega.block.application.data;


public class ApplicationSubjectHomeImpl extends com.idega.data.IDOFactory implements ApplicationSubjectHome
{
 protected Class getEntityInterfaceClass(){
  return ApplicationSubject.class;
 }


 public ApplicationSubject create() throws javax.ejb.CreateException{
  return (ApplicationSubject) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ApplicationSubjectBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findNonExpired()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ApplicationSubjectBMPBean)entity).ejbFindNonExpired();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public ApplicationSubject findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ApplicationSubject) super.findByPrimaryKeyIDO(pk);
 }



}