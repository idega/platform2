package com.idega.block.trade.stockroom.data;
import java.rmi.RemoteException;
import javax.ejb.FinderException;
import com.idega.util.IWTimestamp;
import java.util.Collection;


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


public java.util.Collection getProducts(int p0,int p1,com.idega.util.IWTimestamp p2,com.idega.util.IWTimestamp p3,java.lang.String p4)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection theReturn = ((ProductBMPBean)entity).ejbHomeGetProducts(p0,p1,p2,p3,p4);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public java.util.Collection getProducts(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection theReturn = ((ProductBMPBean)entity).ejbHomeGetProducts(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public java.util.Collection getProducts(int p0,int p1,com.idega.util.IWTimestamp p2,com.idega.util.IWTimestamp p3)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection theReturn = ((ProductBMPBean)entity).ejbHomeGetProducts(p0,p1,p2,p3);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public Collection getProducts(int supplierId, int productCategoryId ,IWTimestamp from, IWTimestamp to, String orderBy, int localeId, int filter) throws FinderException, RemoteException {
  com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
  java.util.Collection theReturn = ((ProductBMPBean)entity).ejbHomeGetProducts(supplierId, productCategoryId, from, to, orderBy, localeId, filter);
  this.idoCheckInPooledEntity(entity);
  return theReturn;
}

public java.util.Collection getProductsOrderedByProductCategory(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection theReturn = ((ProductBMPBean)entity).ejbHomeGetProductsOrderedByProductCategory(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public java.util.Collection getProductsOrderedByProductCategory(int p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2)throws javax.ejb.FinderException{
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection theReturn = ((ProductBMPBean)entity).ejbHomeGetProductsOrderedByProductCategory(p0, p1, p2);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
}
public java.util.Collection getProductsOrderedByProductCategory(int p0,com.idega.util.IWTimestamp p1)throws javax.ejb.FinderException{
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection theReturn = ((ProductBMPBean)entity).ejbHomeGetProductsOrderedByProductCategory(p0, p1);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
}

public int getProductFilterNotConnectedToAnyProductCategory() throws RemoteException {
  com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
  int theReturn = ((ProductBMPBean)entity).ejbHomeGetProductFilterNotConnectedToAnyProductCategory();
  this.idoCheckInPooledEntity(entity);
  return theReturn;
}


}