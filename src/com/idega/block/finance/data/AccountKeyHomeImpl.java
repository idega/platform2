package com.idega.block.finance.data;


public class AccountKeyHomeImpl extends com.idega.data.IDOFactory implements AccountKeyHome
{
 protected Class getEntityInterfaceClass(){
  return AccountKey.class;
 }


 public AccountKey create() throws javax.ejb.CreateException{
  return (AccountKey) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AccountKeyBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByCategory(java.lang.Integer p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AccountKeyBMPBean)entity).ejbFindByCategory(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public AccountKey findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (AccountKey) super.findByPrimaryKeyIDO(pk);
 }



}