package is.idega.idegaweb.travel.data;


public class BookingEntryHomeImpl extends com.idega.data.IDOFactory implements BookingEntryHome
{
 protected Class getEntityInterfaceClass(){
  return BookingEntry.class;
 }


 public BookingEntry create() throws javax.ejb.CreateException{
  return (BookingEntry) super.createIDO();
 }


 public BookingEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (BookingEntry) super.findByPrimaryKeyIDO(pk);
 }


public java.util.Collection getEntries(is.idega.idegaweb.travel.interfaces.Booking p0)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection theReturn = ((BookingEntryBMPBean)entity).ejbHomeGetEntries(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}