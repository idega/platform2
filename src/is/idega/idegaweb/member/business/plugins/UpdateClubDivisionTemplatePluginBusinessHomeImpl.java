package is.idega.idegaweb.member.business.plugins;


public class UpdateClubDivisionTemplatePluginBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements UpdateClubDivisionTemplatePluginBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return UpdateClubDivisionTemplatePluginBusiness.class;
 }


 public UpdateClubDivisionTemplatePluginBusiness create() throws javax.ejb.CreateException{
  return (UpdateClubDivisionTemplatePluginBusiness) super.createIBO();
 }



}