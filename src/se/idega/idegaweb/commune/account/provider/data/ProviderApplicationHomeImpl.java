package se.idega.idegaweb.commune.account.provider.data;


public class ProviderApplicationHomeImpl extends com.idega.data.IDOFactory implements ProviderApplicationHome
{
 protected Class getEntityInterfaceClass(){
  return ProviderApplication.class;
 }


 public ProviderApplication create() throws javax.ejb.CreateException{
  return (ProviderApplication) super.createIDO();
 }


public java.util.Collection findAllApprovedApplications()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ProviderApplicationBMPBean)entity).ejbFindAllApprovedApplications();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllPendingApplications()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ProviderApplicationBMPBean)entity).ejbFindAllPendingApplications();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllRejectedApplications()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ProviderApplicationBMPBean)entity).ejbFindAllRejectedApplications();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public ProviderApplication findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ProviderApplication) super.findByPrimaryKeyIDO(pk);
 }



}