package se.idega.idegaweb.commune.account.provider.business;
import java.rmi.RemoteException;
import javax.ejb.*;
import se.idega.idegaweb.commune.account.business.AccountBusiness;
import se.idega.idegaweb.commune.account.provider.data.ProviderApplication;
public interface ProviderAccountBusiness extends com.idega.business.IBOService, AccountBusiness
{
	public void rejectApplication(int p0, int p1)
		throws javax.ejb.CreateException, java.rmi.RemoteException, java.rmi.RemoteException;
	public void acceptApplication(int p0, int p1)
		throws javax.ejb.CreateException, java.rmi.RemoteException, java.rmi.RemoteException;
	public se.idega.idegaweb.commune.account.provider.data.ProviderApplication createApplication(
		java.lang.String p0,
		java.lang.String p1,
		java.lang.String p2,
		int p3,
		java.lang.String p4,
		java.lang.String p5,
		java.lang.String p6)
		throws javax.ejb.CreateException, java.rmi.RemoteException;
	public void acceptApplication(int p0, com.idega.user.data.User p1)
		throws java.rmi.RemoteException, javax.ejb.CreateException, javax.ejb.FinderException, java.rmi.RemoteException;
	public void rejectApplication(int p0, com.idega.user.data.User p1)
		throws java.rmi.RemoteException, javax.ejb.CreateException, javax.ejb.FinderException, java.rmi.RemoteException;
	public se.idega.idegaweb.commune.account.provider.data.ProviderApplication getProviderApplication(int p0)
		throws java.rmi.RemoteException, javax.ejb.FinderException, java.rmi.RemoteException;
	public void rejectApplication(int p0, com.idega.user.data.User p1, java.lang.String p2)
		throws java.rmi.RemoteException, javax.ejb.CreateException, javax.ejb.FinderException, java.rmi.RemoteException;
}
