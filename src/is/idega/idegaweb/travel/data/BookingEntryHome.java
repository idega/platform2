package is.idega.idegaweb.travel.data;


public interface BookingEntryHome extends com.idega.data.IDOHome
{
 public BookingEntry create() throws javax.ejb.CreateException;
 public BookingEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection getEntries(is.idega.idegaweb.travel.interfaces.Booking p0)throws javax.ejb.FinderException,java.rmi.RemoteException;

}