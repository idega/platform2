package is.idega.idegaweb.travel.service.carrental.business;


public class CarRentalBookerHomeImpl extends com.idega.business.IBOHomeImpl implements CarRentalBookerHome
{
 protected Class getBeanInterfaceClass(){
  return CarRentalBooker.class;
 }


 public CarRentalBooker create() throws javax.ejb.CreateException{
  return (CarRentalBooker) super.createIBO();
 }



}