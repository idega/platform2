package is.idega.idegaweb.travel.service.fishing.business;


public class FishingBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements FishingBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return FishingBusiness.class;
 }


 public FishingBusiness create() throws javax.ejb.CreateException{
  return (FishingBusiness) super.createIBO();
 }



}
