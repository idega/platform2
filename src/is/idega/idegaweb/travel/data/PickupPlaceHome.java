package is.idega.idegaweb.travel.data;


public interface PickupPlaceHome extends com.idega.data.IDOHome
{
 public PickupPlace create() throws javax.ejb.CreateException;
 public PickupPlace findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllPlaces(int p0)throws javax.ejb.FinderException;
 public java.util.Collection findDropoffPlaces(com.idega.block.trade.stockroom.data.Supplier p0)throws javax.ejb.FinderException;
 public java.util.Collection findDropoffPlaces(is.idega.idegaweb.travel.data.Service p0)throws java.rmi.RemoteException,javax.ejb.FinderException;
 public java.util.Collection findHotelPickupPlaces(is.idega.idegaweb.travel.data.Service p0,int p1)throws java.rmi.RemoteException,javax.ejb.FinderException;
 public java.util.Collection findHotelPickupPlaces(com.idega.block.trade.stockroom.data.Supplier p0)throws javax.ejb.FinderException;
 public java.util.Collection findHotelPickupPlaces(com.idega.block.trade.stockroom.data.Supplier p0,int p1)throws javax.ejb.FinderException;
 public java.util.Collection findHotelPickupPlaces(is.idega.idegaweb.travel.data.Service p0)throws java.rmi.RemoteException,javax.ejb.FinderException;

}