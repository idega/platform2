package com.idega.block.finance.data;


public class TariffIndexHomeImpl extends com.idega.data.IDOFactory implements TariffIndexHome
{
 protected Class getEntityInterfaceClass(){
  return TariffIndex.class;
 }


 public TariffIndex create() throws javax.ejb.CreateException{
  return (TariffIndex) super.createIDO();
 }


public TariffIndex findLastByType(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((TariffIndexBMPBean)entity).ejbFindLastByType(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public java.util.Collection findLastTypeGrouped()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((TariffIndexBMPBean)entity).ejbFindLastTypeGrouped();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public TariffIndex findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (TariffIndex) super.findByPrimaryKeyIDO(pk);
 }



}