package com.idega.block.trade.data;


public class CurrencyHomeImpl extends com.idega.data.IDOFactory implements CurrencyHome
{
 protected Class getEntityInterfaceClass(){
  return Currency.class;
 }

 public Currency create() throws javax.ejb.CreateException{
  return (Currency) super.idoCreate();
 }

 public Currency createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Currency findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Currency) super.idoFindByPrimaryKey(id);
 }

 public Currency findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Currency) super.idoFindByPrimaryKey(pk);
 }

 public Currency findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}