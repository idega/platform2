package is.idega.idegaweb.travel.service.tour.data;


public class TourBookingHomeImpl extends com.idega.data.IDOFactory implements TourBookingHome
{
 protected Class getEntityInterfaceClass(){
  return TourBooking.class;
 }


 public TourBooking create() throws javax.ejb.CreateException{
  return (TourBooking) super.createIDO();
 }


 public TourBooking findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (TourBooking) super.findByPrimaryKeyIDO(pk);
 }



}