package is.idega.idegaweb.travel.service.carrental.data;


public class CarRentalHomeImpl extends com.idega.data.IDOFactory implements CarRentalHome
{
 protected Class getEntityInterfaceClass(){
  return CarRental.class;
 }


 public CarRental create() throws javax.ejb.CreateException{
  return (CarRental) super.createIDO();
 }


 public CarRental findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (CarRental) super.findByPrimaryKeyIDO(pk);
 }



}