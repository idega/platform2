package is.idega.idegaweb.travel.data;


public interface PickupPlace extends com.idega.data.IDOEntity
{
 public void addToService(is.idega.idegaweb.travel.data.Service p0)throws com.idega.data.IDOAddRelationshipException;
 public void addToSupplier(com.idega.block.trade.stockroom.data.Supplier p0)throws com.idega.data.IDOAddRelationshipException;
 public com.idega.core.location.data.Address getAddress();
 public boolean getIsDropoff();
 public boolean getIsPickup();
 public java.lang.String getName();
 public int getType();
 public void removeFromService(is.idega.idegaweb.travel.data.Service p0)throws com.idega.data.IDORemoveRelationshipException;
 public void removeFromSupplier(com.idega.block.trade.stockroom.data.Supplier p0)throws com.idega.data.IDORemoveRelationshipException;
 public void setAddress(com.idega.core.location.data.Address p0);
 public void setAddressId(int p0);
 public void setAsDropoff();
 public void setAsPickup();
 public void setName(java.lang.String p0);
 public void setType(int p0);
}
