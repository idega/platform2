package is.idega.idegaweb.member.business.plugins;


public class UserStatusPluginBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements UserStatusPluginBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return UserStatusPluginBusiness.class;
 }


 public UserStatusPluginBusiness create() throws javax.ejb.CreateException{
  return (UserStatusPluginBusiness) super.createIBO();
 }



}