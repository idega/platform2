package is.idega.idegaweb.travel.data;


public interface BookingEntryHome extends com.idega.data.IDOHome
{
 public BookingEntry create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public BookingEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;

}