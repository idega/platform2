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



}