package is.idega.idegaweb.travel.service.hotel.business;


public class HotelBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements HotelBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return HotelBusiness.class;
 }


 public HotelBusiness create() throws javax.ejb.CreateException{
  return (HotelBusiness) super.createIBO();
 }



}