package se.idega.idegaweb.commune.accounting.school.business;


public class ProviderBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements ProviderBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return ProviderBusiness.class;
 }


 public ProviderBusiness create() throws javax.ejb.CreateException{
  return (ProviderBusiness) super.createIBO();
 }



}