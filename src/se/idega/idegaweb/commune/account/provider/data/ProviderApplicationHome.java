package se.idega.idegaweb.commune.account.provider.data;


public interface ProviderApplicationHome extends com.idega.data.IDOHome
{
 public ProviderApplication create() throws javax.ejb.CreateException;
 public ProviderApplication findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllApprovedApplications()throws javax.ejb.FinderException;
 public java.util.Collection findAllPendingApplications()throws javax.ejb.FinderException;
 public java.util.Collection findAllRejectedApplications()throws javax.ejb.FinderException;

}