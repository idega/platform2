package com.idega.block.blog.data;


public class BlogEntityHomeImpl extends com.idega.data.IDOFactory implements BlogEntityHome
{
 protected Class getEntityInterfaceClass(){
  return BlogEntity.class;
 }


 public BlogEntity create() throws javax.ejb.CreateException{
  return (BlogEntity) super.createIDO();
 }


public java.util.Collection findAll(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((BlogEntityBMPBean)entity).ejbFindAll(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public BlogEntity findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (BlogEntity) super.findByPrimaryKeyIDO(pk);
 }



}