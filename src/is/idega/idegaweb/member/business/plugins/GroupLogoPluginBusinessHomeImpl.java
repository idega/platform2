package is.idega.idegaweb.member.business.plugins;


public class GroupLogoPluginBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements GroupLogoPluginBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return GroupLogoPluginBusiness.class;
 }


 public GroupLogoPluginBusiness create() throws javax.ejb.CreateException{
  return (GroupLogoPluginBusiness) super.createIBO();
 }



}