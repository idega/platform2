package com.idega.block.trade.stockroom.data;


public class ProductHomeImpl extends com.idega.data.IDOFactory implements ProductHome
{
 protected Class getEntityInterfaceClass(){
  return Product.class;
 }


 public Product create() throws javax.ejb.CreateException{
  return (Product) super.createIDO();
 }


 public Product findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Product) super.findByPrimaryKeyIDO(pk);
 }


public java.util.Collection getProducts(int p0,int p1,com.idega.util.IWTimestamp p2,com.idega.util.IWTimestamp p3)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection theReturn = ((ProductBMPBean)entity).ejbHomeGetProducts(p0,p1,p2,p3);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public java.util.Collection getProducts(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection theReturn = ((ProductBMPBean)entity).ejbHomeGetProducts(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}