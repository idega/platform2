package com.idega.block.finance.data;


public class CashierHomeImpl extends com.idega.data.IDOFactory implements CashierHome
{
 protected Class getEntityInterfaceClass(){
  return Cashier.class;
 }

 public Cashier create() throws javax.ejb.CreateException{
  return (Cashier) super.idoCreate();
 }

 public Cashier createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Cashier findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Cashier) super.idoFindByPrimaryKey(id);
 }

 public Cashier findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Cashier) super.idoFindByPrimaryKey(pk);
 }

 public Cashier findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}