package com.idega.block.trade.stockroom.data;


public class ProductPriceHomeImpl extends com.idega.data.IDOFactory implements ProductPriceHome
{
 protected Class getEntityInterfaceClass(){
  return ProductPrice.class;
 }

 public ProductPrice create() throws javax.ejb.CreateException{
  return (ProductPrice) super.idoCreate();
 }

 public ProductPrice createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public ProductPrice findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (ProductPrice) super.idoFindByPrimaryKey(id);
 }

 public ProductPrice findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ProductPrice) super.idoFindByPrimaryKey(pk);
 }

 public ProductPrice findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}