package is.idega.idegaweb.member.business.plugins;


public class ClubDivisionHandlerPluginBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements ClubDivisionHandlerPluginBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return ClubDivisionHandlerPluginBusiness.class;
 }


 public ClubDivisionHandlerPluginBusiness create() throws javax.ejb.CreateException{
  return (ClubDivisionHandlerPluginBusiness) super.createIBO();
 }



}