package com.idega.block.finance.data;


public class AccountUserHomeImpl extends com.idega.data.IDOFactory implements AccountUserHome
{
 protected Class getEntityInterfaceClass(){
  return AccountUser.class;
 }


 public AccountUser create() throws javax.ejb.CreateException{
  return (AccountUser) super.createIDO();
 }


public java.util.Collection findByAssessmentRound(java.lang.Integer p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AccountUserBMPBean)entity).ejbFindByAssessmentRound(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findBySearch(java.lang.String p0,java.lang.String p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AccountUserBMPBean)entity).ejbFindBySearch(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public AccountUser findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (AccountUser) super.findByPrimaryKeyIDO(pk);
 }



}