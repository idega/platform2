package com.idega.block.finance.data;


public class AccountInfoHomeImpl extends com.idega.data.IDOFactory implements AccountInfoHome
{
 protected Class getEntityInterfaceClass(){
  return AccountInfo.class;
 }


 public AccountInfo create() throws javax.ejb.CreateException{
  return (AccountInfo) super.createIDO();
 }


public java.util.Collection findByAssessmentRound(java.lang.Integer p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AccountInfoBMPBean)entity).ejbFindByAssessmentRound(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByAssessmentRound(java.lang.Integer p0,int p1,int p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AccountInfoBMPBean)entity).ejbFindByAssessmentRound(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByOwner(java.lang.Integer p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AccountInfoBMPBean)entity).ejbFindByOwner(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByOwnerAndType(java.lang.Integer p0,java.lang.String p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AccountInfoBMPBean)entity).ejbFindByOwnerAndType(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public AccountInfo findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (AccountInfo) super.findByPrimaryKeyIDO(pk);
 }



}