package se.idega.idegaweb.commune.account.provider.data;


public class ProviderApplicationHomeImpl extends com.idega.data.IDOFactory implements ProviderApplicationHome
{
 protected Class getEntityInterfaceClass(){
  return ProviderApplication.class;
 }


 public ProviderApplication create() throws javax.ejb.CreateException{
  return (ProviderApplication) super.createIDO();
 }


 public ProviderApplication findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ProviderApplication) super.findByPrimaryKeyIDO(pk);
 }



}