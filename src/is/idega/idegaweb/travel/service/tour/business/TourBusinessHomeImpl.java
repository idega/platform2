package is.idega.idegaweb.travel.service.tour.business;


public class TourBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements TourBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return TourBusiness.class;
 }


 public TourBusiness create() throws javax.ejb.CreateException{
  return (TourBusiness) super.createIBO();
 }



}