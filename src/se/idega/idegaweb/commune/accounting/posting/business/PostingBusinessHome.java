package se.idega.idegaweb.commune.accounting.posting.business;


public interface PostingBusinessHome extends com.idega.business.IBOHome
{
 public PostingBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;

}