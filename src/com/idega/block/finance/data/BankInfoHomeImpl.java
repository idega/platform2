package com.idega.block.finance.data;


public class BankInfoHomeImpl extends com.idega.data.IDOFactory implements BankInfoHome
{
 protected Class getEntityInterfaceClass(){
  return BankInfo.class;
 }


 public BankInfo create() throws javax.ejb.CreateException{
  return (BankInfo) super.createIDO();
 }


 public BankInfo findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (BankInfo) super.findByPrimaryKeyIDO(pk);
 }
 
 public BankInfo findByGroupId(int groupId) throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((BankInfoBMPBean)entity).ejbFindByGroupId(groupId);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
 
 }



}