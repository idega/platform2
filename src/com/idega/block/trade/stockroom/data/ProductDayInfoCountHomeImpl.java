package com.idega.block.trade.stockroom.data;

import java.sql.Date;


public class ProductDayInfoCountHomeImpl extends com.idega.data.IDOFactory implements ProductDayInfoCountHome
{
 protected Class getEntityInterfaceClass(){
  return ProductDayInfoCount.class;
 }


 public ProductDayInfoCount create() throws javax.ejb.CreateException{
  return (ProductDayInfoCount) super.createIDO();
 }


 public ProductDayInfoCount findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ProductDayInfoCount) super.findByPrimaryKeyIDO(pk);
 }
 
 public ProductDayInfoCount findByProductIdAndDate(int productId, Date date) throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ProductDayInfoCountBMPBean)entity).ejbFindByProductIdAndDate(productId,date);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
 }
}