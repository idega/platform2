package is.idega.idegaweb.travel.business;


public class TravelSessionManagerHomeImpl extends com.idega.business.IBOHomeImpl implements TravelSessionManagerHome
{
 protected Class getBeanInterfaceClass(){
  return TravelSessionManager.class;
 }


 public TravelSessionManager create() throws javax.ejb.CreateException{
  return (TravelSessionManager) super.createIBO();
 }



}