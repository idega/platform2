package com.idega.block.trade.stockroom.data;


public class ResellerHomeImpl extends com.idega.data.IDOFactory implements ResellerHome
{
 protected Class getEntityInterfaceClass(){
  return Reseller.class;
 }

 public Reseller create() throws javax.ejb.CreateException{
  return (Reseller) super.idoCreate();
 }

 public Reseller createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Reseller findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Reseller) super.idoFindByPrimaryKey(id);
 }

 public Reseller findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Reseller) super.idoFindByPrimaryKey(pk);
 }

 public Reseller findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}