package is.idega.idegaweb.travel.service.fishing.business;


public class FishingBookerHomeImpl extends com.idega.business.IBOHomeImpl implements FishingBookerHome
{
 protected Class getBeanInterfaceClass(){
  return FishingBooker.class;
 }


 public FishingBooker create() throws javax.ejb.CreateException{
  return (FishingBooker) super.createIBO();
 }



}