package com.idega.block.trade.stockroom.data;


public class SuppliesHomeImpl extends com.idega.data.IDOFactory implements SuppliesHome
{
 protected Class getEntityInterfaceClass(){
  return Supplies.class;
 }

 public Supplies create() throws javax.ejb.CreateException{
  return (Supplies) super.idoCreate();
 }

 public Supplies createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Supplies findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Supplies) super.idoFindByPrimaryKey(id);
 }

 public Supplies findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Supplies) super.idoFindByPrimaryKey(pk);
 }

 public Supplies findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}