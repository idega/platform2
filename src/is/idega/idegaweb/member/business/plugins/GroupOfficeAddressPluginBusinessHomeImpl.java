package is.idega.idegaweb.member.business.plugins;


public class GroupOfficeAddressPluginBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements GroupOfficeAddressPluginBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return GroupOfficeAddressPluginBusiness.class;
 }


 public GroupOfficeAddressPluginBusiness create() throws javax.ejb.CreateException{
  return (GroupOfficeAddressPluginBusiness) super.createIBO();
 }



}