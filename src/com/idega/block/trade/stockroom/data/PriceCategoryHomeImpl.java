package com.idega.block.trade.stockroom.data;


public class PriceCategoryHomeImpl extends com.idega.data.IDOFactory implements PriceCategoryHome
{
 protected Class getEntityInterfaceClass(){
  return PriceCategory.class;
 }

 public PriceCategory create() throws javax.ejb.CreateException{
  return (PriceCategory) super.idoCreate();
 }

 public PriceCategory createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public PriceCategory findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (PriceCategory) super.idoFindByPrimaryKey(id);
 }

 public PriceCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (PriceCategory) super.idoFindByPrimaryKey(pk);
 }

 public PriceCategory findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}