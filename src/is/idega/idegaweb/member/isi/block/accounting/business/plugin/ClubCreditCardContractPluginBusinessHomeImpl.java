package is.idega.idegaweb.member.isi.block.accounting.business.plugin;


public class ClubCreditCardContractPluginBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements ClubCreditCardContractPluginBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return ClubCreditCardContractPluginBusiness.class;
 }


 public ClubCreditCardContractPluginBusiness create() throws javax.ejb.CreateException{
  return (ClubCreditCardContractPluginBusiness) super.createIBO();
 }



}