package com.idega.block.finance.data;


public class TariffGroupHomeImpl extends com.idega.data.IDOFactory implements TariffGroupHome
{
 protected Class getEntityInterfaceClass(){
  return TariffGroup.class;
 }


 public TariffGroup create() throws javax.ejb.CreateException{
  return (TariffGroup) super.createIDO();
 }


public java.util.Collection findByCategory(java.lang.Integer p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((TariffGroupBMPBean)entity).ejbFindByCategory(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByCategoryWithouthHandlers(java.lang.Integer p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((TariffGroupBMPBean)entity).ejbFindByCategoryWithouthHandlers(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public TariffGroup findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (TariffGroup) super.findByPrimaryKeyIDO(pk);
 }



}