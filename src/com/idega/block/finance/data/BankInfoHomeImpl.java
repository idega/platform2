package com.idega.block.finance.data;

import java.util.Collection;
import com.idega.user.data.Group;


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
 
 public Collection findAllByClub(Group club) throws javax.ejb.FinderException {
  com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
  java.util.Collection ids = ((BankInfoBMPBean) entity).ejbFindAllByClub(club);
  this.idoCheckInPooledEntity(entity);
  return this.getEntityCollectionForPrimaryKeys(ids);

 }




}