package is.idega.idegaweb.travel.service.hotel.data;


public interface HotelHome extends com.idega.data.IDOHome
{
 public Hotel create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public Hotel findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;

}