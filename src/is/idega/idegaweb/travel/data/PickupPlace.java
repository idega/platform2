package is.idega.idegaweb.travel.data;

import javax.ejb.*;

public interface PickupPlace extends com.idega.data.IDOEntity
{
 public void addToService(is.idega.idegaweb.travel.data.Service p0)throws com.idega.data.IDOAddRelationshipException, java.rmi.RemoteException;
 public void addToSupplier(com.idega.block.trade.stockroom.data.Supplier p0)throws com.idega.data.IDOAddRelationshipException, java.rmi.RemoteException;
 public void delete() throws java.rmi.RemoteException;
 public com.idega.core.data.Address getAddress() throws java.rmi.RemoteException;
 public java.lang.String getName() throws java.rmi.RemoteException;
 public void removeFromService(is.idega.idegaweb.travel.data.Service p0)throws com.idega.data.IDORemoveRelationshipException, java.rmi.RemoteException;
 public void removeFromSupplier(com.idega.block.trade.stockroom.data.Supplier p0)throws com.idega.data.IDORemoveRelationshipException, java.rmi.RemoteException;
 public void setAddress(com.idega.core.data.Address p0) throws java.rmi.RemoteException;
 public void setAddressId(int p0) throws java.rmi.RemoteException;
 public void setDefaultValues() throws java.rmi.RemoteException;
 public void setName(java.lang.String p0) throws java.rmi.RemoteException;
}
