package is.idega.idegaweb.golf.business;


public class UserSynchronizationBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements UserSynchronizationBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return UserSynchronizationBusiness.class;
 }


 public UserSynchronizationBusiness create() throws javax.ejb.CreateException{
  return (UserSynchronizationBusiness) super.createIBO();
 }



}