package is.idega.idegaweb.member.business.plugins;


public class UserHistoryPluginBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements UserHistoryPluginBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return UserHistoryPluginBusiness.class;
 }


 public UserHistoryPluginBusiness create() throws javax.ejb.CreateException{
  return (UserHistoryPluginBusiness) super.createIBO();
 }



}