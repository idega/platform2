package com.idega.block.trade.stockroom.data;


public class StockHomeImpl extends com.idega.data.IDOFactory implements StockHome
{
 protected Class getEntityInterfaceClass(){
  return Stock.class;
 }

 public Stock create() throws javax.ejb.CreateException{
  return (Stock) super.idoCreate();
 }

 public Stock createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Stock findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Stock) super.idoFindByPrimaryKey(id);
 }

 public Stock findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Stock) super.idoFindByPrimaryKey(pk);
 }

 public Stock findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}