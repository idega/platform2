package is.idega.idegaweb.travel.business;


public class ContractBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements ContractBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return ContractBusiness.class;
 }


 public ContractBusiness create() throws javax.ejb.CreateException{
  return (ContractBusiness) super.createIBO();
 }



}