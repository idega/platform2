package com.idega.block.trade.stockroom.data;


public class ProductCategoryHomeImpl extends com.idega.data.IDOFactory implements ProductCategoryHome
{
 protected Class getEntityInterfaceClass(){
  return ProductCategory.class;
 }

 public ProductCategory create() throws javax.ejb.CreateException{
  return (ProductCategory) super.idoCreate();
 }

 public ProductCategory createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public ProductCategory findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (ProductCategory) super.idoFindByPrimaryKey(id);
 }

 public ProductCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ProductCategory) super.idoFindByPrimaryKey(pk);
 }

 public ProductCategory findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }

 public ProductCategory getProductCategory(String type) throws javax.ejb.FinderException, java.rmi.RemoteException {
    com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
    ProductCategory theReturn = ((ProductCategoryBMPBean)entity).ejbHomeGetProductCategory(type);
    this.idoCheckInPooledEntity(entity);
    return theReturn;
 }


}
