package is.idega.idegaweb.campus.block.allocation.business;


public class ContractServiceHomeImpl extends com.idega.business.IBOHomeImpl implements ContractServiceHome
{
 protected Class getBeanInterfaceClass(){
  return ContractService.class;
 }


 public ContractService create() throws javax.ejb.CreateException{
  return (ContractService) super.createIBO();
 }



}