package com.idega.block.finance.data;


public class EntryGroupHomeImpl extends com.idega.data.IDOFactory implements EntryGroupHome
{
 protected Class getEntityInterfaceClass(){
  return EntryGroup.class;
 }


 public EntryGroup create() throws javax.ejb.CreateException{
  return (EntryGroup) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((EntryGroupBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public EntryGroup findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (EntryGroup) super.findByPrimaryKeyIDO(pk);
 }



}