package is.idega.idegaweb.travel.service.tour.business;


public class TourBookerHomeImpl extends com.idega.business.IBOHomeImpl implements TourBookerHome
{
 protected Class getBeanInterfaceClass(){
  return TourBooker.class;
 }


 public TourBooker create() throws javax.ejb.CreateException{
  return (TourBooker) super.createIBO();
 }



}