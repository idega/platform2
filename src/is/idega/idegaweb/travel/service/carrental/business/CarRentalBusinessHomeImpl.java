package is.idega.idegaweb.travel.service.carrental.business;


public class CarRentalBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements CarRentalBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return CarRentalBusiness.class;
 }


 public CarRentalBusiness create() throws javax.ejb.CreateException{
  return (CarRentalBusiness) super.createIBO();
 }



}