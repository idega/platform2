package com.idega.block.trade.data;


public class CurrencyValuesHomeImpl extends com.idega.data.IDOFactory implements CurrencyValuesHome
{
 protected Class getEntityInterfaceClass(){
  return CurrencyValues.class;
 }

 public CurrencyValues create() throws javax.ejb.CreateException{
  return (CurrencyValues) super.idoCreate();
 }

 public CurrencyValues createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public CurrencyValues findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (CurrencyValues) super.idoFindByPrimaryKey(id);
 }

 public CurrencyValues findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (CurrencyValues) super.idoFindByPrimaryKey(pk);
 }

 public CurrencyValues findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}