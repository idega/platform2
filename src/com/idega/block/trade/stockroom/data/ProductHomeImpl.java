package com.idega.block.trade.stockroom.data;


public class ProductHomeImpl extends com.idega.data.IDOFactory implements ProductHome
{
 protected Class getEntityInterfaceClass(){
  return Product.class;
 }

 public Product create() throws javax.ejb.CreateException{
  return (Product) super.idoCreate();
 }

 public Product createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Product findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Product) super.idoFindByPrimaryKey(id);
 }

 public Product findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Product) super.idoFindByPrimaryKey(pk);
 }

 public Product findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}