package se.idega.idegaweb.commune.provider.business;


public class ProviderSessionHomeImpl extends com.idega.business.IBOHomeImpl implements ProviderSessionHome
{
 protected Class getBeanInterfaceClass(){
  return ProviderSession.class;
 }


 public ProviderSession create() throws javax.ejb.CreateException{
  return (ProviderSession) super.createIBO();
 }



}