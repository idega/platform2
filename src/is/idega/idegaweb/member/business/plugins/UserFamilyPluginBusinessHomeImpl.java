package is.idega.idegaweb.member.business.plugins;


public class UserFamilyPluginBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements UserFamilyPluginBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return UserFamilyPluginBusiness.class;
 }


 public UserFamilyPluginBusiness create() throws javax.ejb.CreateException{
  return (UserFamilyPluginBusiness) super.createIBO();
 }



}