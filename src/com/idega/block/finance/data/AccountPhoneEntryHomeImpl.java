package com.idega.block.finance.data;


public class AccountPhoneEntryHomeImpl extends com.idega.data.IDOFactory implements AccountPhoneEntryHome
{
 protected Class getEntityInterfaceClass(){
  return AccountPhoneEntry.class;
 }


 public AccountPhoneEntry create() throws javax.ejb.CreateException{
  return (AccountPhoneEntry) super.createIDO();
 }


public java.util.Collection findByAccountAndStatus(java.lang.Integer p0,java.lang.String p1,java.sql.Date p2,java.sql.Date p3)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AccountPhoneEntryBMPBean)entity).ejbFindByAccountAndStatus(p0,p1,p2,p3);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findUnbilledByAccountAndPeriod(java.lang.Integer p0,java.sql.Date p1,java.sql.Date p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AccountPhoneEntryBMPBean)entity).ejbFindUnbilledByAccountAndPeriod(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByPhoneNumber(String number)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AccountPhoneEntryBMPBean)entity).ejbFindByPhoneNumber(number);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}


 public AccountPhoneEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (AccountPhoneEntry) super.findByPrimaryKeyIDO(pk);
 }



}