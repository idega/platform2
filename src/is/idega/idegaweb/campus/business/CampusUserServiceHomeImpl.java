package is.idega.idegaweb.campus.business;


public class CampusUserServiceHomeImpl extends com.idega.business.IBOHomeImpl implements CampusUserServiceHome
{
 protected Class getBeanInterfaceClass(){
  return CampusUserService.class;
 }


 public CampusUserService create() throws javax.ejb.CreateException{
  return (CampusUserService) super.createIBO();
 }



}