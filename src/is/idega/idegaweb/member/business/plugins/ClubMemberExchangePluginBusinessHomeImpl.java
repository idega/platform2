package is.idega.idegaweb.member.business.plugins;


public class ClubMemberExchangePluginBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements ClubMemberExchangePluginBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return ClubMemberExchangePluginBusiness.class;
 }


 public ClubMemberExchangePluginBusiness create() throws javax.ejb.CreateException{
  return (ClubMemberExchangePluginBusiness) super.createIBO();
 }



}