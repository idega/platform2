package is.idega.idegaweb.golf.handicap.business;


public class HandicapBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements HandicapBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return HandicapBusiness.class;
 }


 public HandicapBusiness create() throws javax.ejb.CreateException{
  return (HandicapBusiness) super.createIBO();
 }



}