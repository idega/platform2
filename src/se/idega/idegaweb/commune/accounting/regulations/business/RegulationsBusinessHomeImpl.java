package se.idega.idegaweb.commune.accounting.regulations.business;


public class RegulationsBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements RegulationsBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return RegulationsBusiness.class;
 }


 public RegulationsBusiness create() throws javax.ejb.CreateException{
  return (RegulationsBusiness) super.createIBO();
 }



}