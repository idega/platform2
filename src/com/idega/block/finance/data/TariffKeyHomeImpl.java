package com.idega.block.finance.data;


public class TariffKeyHomeImpl extends com.idega.data.IDOFactory implements TariffKeyHome
{
 protected Class getEntityInterfaceClass(){
  return TariffKey.class;
 }


 public TariffKey create() throws javax.ejb.CreateException{
  return (TariffKey) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((TariffKeyBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByCategory(java.lang.Integer p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((TariffKeyBMPBean)entity).ejbFindByCategory(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public TariffKey findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (TariffKey) super.findByPrimaryKeyIDO(pk);
 }



}