package is.idega.idegaweb.atvr.supplier.application.data;

import javax.ejb.*;

public interface NewProductApplication extends com.idega.data.IDOEntity
{
 public java.lang.String getCountryOfOrigin() throws java.rmi.RemoteException;
 public void setApplicationSent(java.sql.Timestamp p0) throws java.rmi.RemoteException;
 public java.lang.String getStatus() throws java.rmi.RemoteException;
 public void setSupplierId(int p0) throws java.rmi.RemoteException;
 public void setDescription2(java.lang.String p0) throws java.rmi.RemoteException;
 public java.lang.String getProducer() throws java.rmi.RemoteException;
 public void setStatus(java.lang.String p0) throws java.rmi.RemoteException;
 public java.lang.String getQuantity() throws java.rmi.RemoteException;
 public void setCountryOfOrigin(java.lang.String p0) throws java.rmi.RemoteException;
 public int getProductCategoryId() throws java.rmi.RemoteException;
 public com.idega.core.user.data.User getSupplier() throws java.rmi.RemoteException;
 public void setSuppliersProductId(java.lang.String p0) throws java.rmi.RemoteException;
 public java.lang.String getApplicationType() throws java.rmi.RemoteException;
 public java.lang.String getBarCode() throws java.rmi.RemoteException;
 public void setProducer(java.lang.String p0) throws java.rmi.RemoteException;
 public void setPrice(float p0) throws java.rmi.RemoteException;
 public void setDescription(java.lang.String p0) throws java.rmi.RemoteException;
 public void setQuantity(java.lang.String p0) throws java.rmi.RemoteException;
 public float getPrice(float p0) throws java.rmi.RemoteException;
 public java.lang.String getWeigth() throws java.rmi.RemoteException;
 public void setSupplier(com.idega.core.user.data.User p0) throws java.rmi.RemoteException;
 public java.lang.String getStrength() throws java.rmi.RemoteException;
 public void setWeigth(java.lang.String p0) throws java.rmi.RemoteException;
 public is.idega.idegaweb.atvr.supplier.application.data.ProductCategory getProductCategory() throws java.rmi.RemoteException;
 public java.lang.String getAmount() throws java.rmi.RemoteException;
 public void setApplicationType(java.lang.String p0) throws java.rmi.RemoteException;
 public java.lang.String getDescription() throws java.rmi.RemoteException;
 public void initializeAttributes() throws java.rmi.RemoteException;
 public java.lang.String getSuppliersProductId() throws java.rmi.RemoteException;
 public int getSupplierId() throws java.rmi.RemoteException;
 public void setAmount(java.lang.String p0) throws java.rmi.RemoteException;
 public java.lang.String getDescription2() throws java.rmi.RemoteException;
 public java.sql.Timestamp getApplicationSent() throws java.rmi.RemoteException;
 public void setStrength(java.lang.String p0) throws java.rmi.RemoteException;
 public void setProductCategory(is.idega.idegaweb.atvr.supplier.application.data.ProductCategory p0) throws java.rmi.RemoteException;
 public void setBarCode(java.lang.String p0) throws java.rmi.RemoteException;
 public void setProductCategoryId(int p0) throws java.rmi.RemoteException;
}
