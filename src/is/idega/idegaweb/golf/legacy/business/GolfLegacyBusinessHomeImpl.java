package is.idega.idegaweb.golf.legacy.business;


public class GolfLegacyBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements GolfLegacyBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return GolfLegacyBusiness.class;
 }


 public GolfLegacyBusiness create() throws javax.ejb.CreateException{
  return (GolfLegacyBusiness) super.createIBO();
 }



}