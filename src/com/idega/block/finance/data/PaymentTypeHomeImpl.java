package com.idega.block.finance.data;


public class PaymentTypeHomeImpl extends com.idega.data.IDOFactory implements PaymentTypeHome
{
 protected Class getEntityInterfaceClass(){
  return PaymentType.class;
 }


 public PaymentType create() throws javax.ejb.CreateException{
  return (PaymentType) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PaymentTypeBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public PaymentType findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (PaymentType) super.findByPrimaryKeyIDO(pk);
 }



}