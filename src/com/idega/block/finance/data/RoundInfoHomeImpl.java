package com.idega.block.finance.data;


public class RoundInfoHomeImpl extends com.idega.data.IDOFactory implements RoundInfoHome
{
 protected Class getEntityInterfaceClass(){
  return RoundInfo.class;
 }


 public RoundInfo create() throws javax.ejb.CreateException{
  return (RoundInfo) super.createIDO();
 }


public java.util.Collection findByCategoryAndGroup(java.lang.Integer p0,java.lang.Integer p1,java.sql.Date p2,java.sql.Date p3)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((RoundInfoBMPBean)entity).ejbFindByCategoryAndGroup(p0,p1,p2,p3);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public RoundInfo findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (RoundInfo) super.findByPrimaryKeyIDO(pk);
 }



}