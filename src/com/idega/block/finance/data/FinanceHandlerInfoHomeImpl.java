package com.idega.block.finance.data;


public class FinanceHandlerInfoHomeImpl extends com.idega.data.IDOFactory implements FinanceHandlerInfoHome
{
 protected Class getEntityInterfaceClass(){
  return FinanceHandlerInfo.class;
 }


 public FinanceHandlerInfo create() throws javax.ejb.CreateException{
  return (FinanceHandlerInfo) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((FinanceHandlerInfoBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public FinanceHandlerInfo findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (FinanceHandlerInfo) super.findByPrimaryKeyIDO(pk);
 }



}