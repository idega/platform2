package com.idega.block.trade.stockroom.data;

import java.sql.Date;


public class ProductPriceHomeImpl extends com.idega.data.IDOFactory implements ProductPriceHome
{
 protected Class getEntityInterfaceClass(){
  return ProductPrice.class;
 }


 public ProductPrice create() throws javax.ejb.CreateException{
  return (ProductPrice) super.createIDO();
 }


 public ProductPrice createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }


 public ProductPrice findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ProductPrice) super.findByPrimaryKeyIDO(pk);
 }


 public ProductPrice findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (ProductPrice) super.findByPrimaryKeyIDO(id);
 }


 public ProductPrice findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }
 
 public ProductPrice findByData(int productId, int timeframeId, int addressId, int currencyId, int priceCategoryId, Date date) throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ProductPriceBMPBean)entity).ejbFindByData(productId,timeframeId,addressId,currencyId,priceCategoryId,date);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
 }




}