package se.idega.idegaweb.commune.account.provider.business;


public class ProviderAccountBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements ProviderAccountBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return ProviderAccountBusiness.class;
 }


 public ProviderAccountBusiness create() throws javax.ejb.CreateException{
  return (ProviderAccountBusiness) super.createIBO();
 }



}