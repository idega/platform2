package is.idega.idegaweb.travel.service.tour.data;


public interface TourHome extends com.idega.data.IDOHome
{
 public Tour create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public Tour findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;

}