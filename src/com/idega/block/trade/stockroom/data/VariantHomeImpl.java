package com.idega.block.trade.stockroom.data;


public class VariantHomeImpl extends com.idega.data.IDOFactory implements VariantHome
{
 protected Class getEntityInterfaceClass(){
  return Variant.class;
 }

 public Variant create() throws javax.ejb.CreateException{
  return (Variant) super.idoCreate();
 }

 public Variant createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Variant findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Variant) super.idoFindByPrimaryKey(id);
 }

 public Variant findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Variant) super.idoFindByPrimaryKey(pk);
 }

 public Variant findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}