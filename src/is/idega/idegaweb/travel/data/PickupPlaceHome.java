package is.idega.idegaweb.travel.data;


public interface PickupPlaceHome extends com.idega.data.IDOHome
{
 public PickupPlace create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public PickupPlace findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findHotelPickupPlaces(is.idega.idegaweb.travel.data.Service p0)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection findHotelPickupPlaces(com.idega.block.trade.stockroom.data.Supplier p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public void removeFromAllServices()throws com.idega.data.IDORemoveRelationshipException, java.rmi.RemoteException;

}