package is.idega.idegaweb.atvr.supplier.application.data;


public class ProductCategoryHomeImpl extends com.idega.data.IDOFactory implements ProductCategoryHome
{
 protected Class getEntityInterfaceClass(){
  return ProductCategory.class;
 }


 public ProductCategory create() throws javax.ejb.CreateException{
  return (ProductCategory) super.createIDO();
 }


public java.util.Collection findAllCategoriesBelongingTo(java.lang.String p0)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ProductCategoryBMPBean)entity).ejbFindAllCategoriesBelongingTo(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllMainCategories()throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ProductCategoryBMPBean)entity).ejbFindAllMainCategories();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public ProductCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ProductCategory) super.findByPrimaryKeyIDO(pk);
 }



}