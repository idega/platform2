package se.idega.idegaweb.commune.complaint.business;


public interface ComplaintBusinessHome extends com.idega.business.IBOHome
{
 public ComplaintBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;

}