package is.idega.idegaweb.travel.data;


public interface Service extends com.idega.data.IDOLegacyEntity
{
 public java.util.Collection getHotelPickupPlaces()throws com.idega.data.IDORelationshipException;
 public int getID();
 public java.sql.Timestamp getArrivalTime();
 public void initializeAttributes();
 public com.idega.core.location.data.Address[] getAddresses()throws java.sql.SQLException;
 public java.lang.String getDescription(int p0)throws java.rmi.RemoteException;
 public void setPrimaryKey(java.lang.Integer p0);
 public com.idega.core.location.data.Address getAddress()throws java.sql.SQLException;
 public java.util.Collection getAddressesColl()throws com.idega.data.IDORelationshipException;
 public void removeAllHotelPickupPlaces()throws com.idega.data.IDORemoveRelationshipException;
 public void setAttivalTime(java.sql.Timestamp p0);
 public java.lang.String getName(int p0)throws java.rmi.RemoteException;
 public java.sql.Timestamp getDepartureTime();
 public com.idega.block.trade.stockroom.data.Product getProduct()throws java.rmi.RemoteException;
 public void setDepartureTime(java.sql.Timestamp p0);
}
