package is.idega.idegaweb.travel.service.hotel.business;


public class HotelBookerHomeImpl extends com.idega.business.IBOHomeImpl implements HotelBookerHome
{
 protected Class getBeanInterfaceClass(){
  return HotelBooker.class;
 }


 public HotelBooker create() throws javax.ejb.CreateException{
  return (HotelBooker) super.createIBO();
 }



}