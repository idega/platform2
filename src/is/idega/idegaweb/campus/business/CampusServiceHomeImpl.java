package is.idega.idegaweb.campus.business;


public class CampusServiceHomeImpl extends com.idega.business.IBOHomeImpl implements CampusServiceHome
{
 protected Class getBeanInterfaceClass(){
  return CampusService.class;
 }


 public CampusService create() throws javax.ejb.CreateException{
  return (CampusService) super.createIBO();
 }



}