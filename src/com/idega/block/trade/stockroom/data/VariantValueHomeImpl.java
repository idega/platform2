package com.idega.block.trade.stockroom.data;


public class VariantValueHomeImpl extends com.idega.data.IDOFactory implements VariantValueHome
{
 protected Class getEntityInterfaceClass(){
  return VariantValue.class;
 }

 public VariantValue create() throws javax.ejb.CreateException{
  return (VariantValue) super.idoCreate();
 }

 public VariantValue createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public VariantValue findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (VariantValue) super.idoFindByPrimaryKey(id);
 }

 public VariantValue findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (VariantValue) super.idoFindByPrimaryKey(pk);
 }

 public VariantValue findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}