package is.idega.idegaweb.golf.business;


public class GolfBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements GolfBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return GolfBusiness.class;
 }


 public GolfBusiness create() throws javax.ejb.CreateException{
  return (GolfBusiness) super.createIBO();
 }



}