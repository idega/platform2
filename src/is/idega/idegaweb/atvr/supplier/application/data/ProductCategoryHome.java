package is.idega.idegaweb.atvr.supplier.application.data;


public interface ProductCategoryHome extends com.idega.data.IDOHome
{
 public ProductCategory create() throws javax.ejb.CreateException;
 public ProductCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllByCategory(java.lang.String p0)throws javax.ejb.FinderException,java.rmi.RemoteException;
 public java.util.Collection findAllCategoriesBelongingTo(java.lang.String p0)throws javax.ejb.FinderException,java.rmi.RemoteException;
 public java.util.Collection findAllMainCategories()throws javax.ejb.FinderException,java.rmi.RemoteException;

}