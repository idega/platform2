package se.idega.idegaweb.commune.accounting.update.business;


public class ChildCareContractPlacementBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements ChildCareContractPlacementBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return ChildCareContractPlacementBusiness.class;
 }


 public ChildCareContractPlacementBusiness create() throws javax.ejb.CreateException{
  return (ChildCareContractPlacementBusiness) super.createIBO();
 }



}