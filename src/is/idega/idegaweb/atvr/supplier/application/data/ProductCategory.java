package is.idega.idegaweb.atvr.supplier.application.data;

import javax.ejb.*;

public interface ProductCategory extends com.idega.data.IDOEntity
{
 public void setCategory(java.lang.String p0) throws java.rmi.RemoteException;
 public java.lang.String getCategory() throws java.rmi.RemoteException;
 public void initializeAttributes() throws java.rmi.RemoteException;
 public void setDescription(java.lang.String p0) throws java.rmi.RemoteException;
 public java.lang.String getDescription() throws java.rmi.RemoteException;
 public is.idega.idegaweb.atvr.supplier.application.data.ProductCategory getBelongsToCategory() throws java.rmi.RemoteException;
 public int getBelongsToCategoryId() throws java.rmi.RemoteException;
 public void setBelongsToCategory(int p0) throws java.rmi.RemoteException;
}
