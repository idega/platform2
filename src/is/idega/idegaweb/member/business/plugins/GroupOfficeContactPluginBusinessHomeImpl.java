package is.idega.idegaweb.member.business.plugins;


public class GroupOfficeContactPluginBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements GroupOfficeContactPluginBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return GroupOfficeContactPluginBusiness.class;
 }


 public GroupOfficeContactPluginBusiness create() throws javax.ejb.CreateException{
  return (GroupOfficeContactPluginBusiness) super.createIBO();
 }



}