package is.idega.idegaweb.member.business.plugins;


public class ClubInformationPluginBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements ClubInformationPluginBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return ClubInformationPluginBusiness.class;
 }


 public ClubInformationPluginBusiness create() throws javax.ejb.CreateException{
  return (ClubInformationPluginBusiness) super.createIBO();
 }



}