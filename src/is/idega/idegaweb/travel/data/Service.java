package is.idega.idegaweb.travel.data;

import javax.ejb.*;

public interface Service extends com.idega.data.IDOEntity
{
 public java.util.Collection getHotelPickupPlaces()throws com.idega.data.IDORelationshipException, java.rmi.RemoteException;
 public int getID() throws java.rmi.RemoteException;
 public java.sql.Timestamp getArrivalTime() throws java.rmi.RemoteException;
 public com.idega.core.data.Address[] getAddresses()throws java.sql.SQLException, java.rmi.RemoteException;
 public java.lang.String getDescription() throws java.rmi.RemoteException;
 public void setPrimaryKey(java.lang.Integer p0) throws java.rmi.RemoteException;
 public com.idega.core.data.Address getAddress()throws java.sql.SQLException, java.rmi.RemoteException;
 public void removeAllHotelPickupPlaces()throws com.idega.data.IDORemoveRelationshipException, java.rmi.RemoteException;
 public void setAttivalTime(java.sql.Timestamp p0) throws java.rmi.RemoteException;
 public java.lang.String getName() throws java.rmi.RemoteException;
 public java.sql.Timestamp getDepartureTime() throws java.rmi.RemoteException;
 public com.idega.block.trade.stockroom.data.Product getProduct() throws java.rmi.RemoteException;
 public void delete()throws java.sql.SQLException, java.rmi.RemoteException;
 public void setDefaultValues() throws java.rmi.RemoteException;
 public void setDepartureTime(java.sql.Timestamp p0) throws java.rmi.RemoteException;
}
