package com.idega.block.trade.stockroom.data;


public class PriceCategoryHomeImpl extends com.idega.data.IDOFactory implements PriceCategoryHome
{
 protected Class getEntityInterfaceClass(){
  return PriceCategory.class;
 }


 public PriceCategory create() throws javax.ejb.CreateException{
  return (PriceCategory) super.createIDO();
 }


 public PriceCategory createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }


public PriceCategory findByKey(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((PriceCategoryBMPBean)entity).ejbFindByKey(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public PriceCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (PriceCategory) super.findByPrimaryKeyIDO(pk);
 }


 public PriceCategory findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (PriceCategory) super.findByPrimaryKeyIDO(id);
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