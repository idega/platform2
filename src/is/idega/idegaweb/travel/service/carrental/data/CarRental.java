package is.idega.idegaweb.travel.service.carrental.data;


public interface CarRental extends com.idega.data.IDOEntity
{
 public void addDropoffPlace(is.idega.idegaweb.travel.data.PickupPlace p0)throws com.idega.data.IDOAddRelationshipException;
 public void addPickupPlace(is.idega.idegaweb.travel.data.PickupPlace p0)throws com.idega.data.IDOAddRelationshipException;
 public java.util.Collection getDropoffPlaces()throws com.idega.data.IDOLookupException,com.idega.data.IDORelationshipException,javax.ejb.FinderException;
 public java.util.Collection getPickupPlaces()throws com.idega.data.IDOLookupException,com.idega.data.IDORelationshipException,javax.ejb.FinderException;
 public void removeAllDropoffPlaces()throws com.idega.data.IDORemoveRelationshipException;
 public void removeAllPickupPlaces()throws com.idega.data.IDORemoveRelationshipException;
 public void setPrimaryKey(java.lang.Object p0);
}
