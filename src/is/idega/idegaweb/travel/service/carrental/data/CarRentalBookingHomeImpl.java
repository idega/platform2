package is.idega.idegaweb.travel.service.carrental.data;


public class CarRentalBookingHomeImpl extends com.idega.data.IDOFactory implements CarRentalBookingHome
{
 protected Class getEntityInterfaceClass(){
  return CarRentalBooking.class;
 }


 public CarRentalBooking create() throws javax.ejb.CreateException{
  return (CarRentalBooking) super.createIDO();
 }


 public CarRentalBooking findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (CarRentalBooking) super.findByPrimaryKeyIDO(pk);
 }



}