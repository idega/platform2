package se.idega.idegaweb.commune.account.provider.business;
import java.util.Collection;

import se.idega.idegaweb.commune.account.business.AccountBusiness;
public interface ProviderAccountBusiness extends com.idega.business.IBOService, AccountBusiness
{
	public void rejectApplication(int p0, int p1)
		throws javax.ejb.CreateException, java.rmi.RemoteException, java.rmi.RemoteException;
	public void acceptApplication(int p0, int p1)
		throws javax.ejb.CreateException, java.rmi.RemoteException, java.rmi.RemoteException;
	public se.idega.idegaweb.commune.account.provider.data.ProviderApplication createApplication(
		String providerName,
		String address,
		String telephone,
		int numberOfPlaces,
		String managerName,
		String managerEmail,
		String additionalInfo,
		int postalCodeID,
		int schoolTypeID,
		int schoolAreaID)
		throws javax.ejb.CreateException, java.rmi.RemoteException;
	public se.idega.idegaweb.commune.account.provider.data.ProviderApplication createApplication(
		String providerName,
		String address,
		String telephone,
		int numberOfPlaces,
		String managerName,
		String managerEmail,
		String additionalInfo,
		int postalCodeID,
		int[] schoolTypeIDs,
		int schoolAreaID)
		throws javax.ejb.CreateException, java.rmi.RemoteException;
	public void acceptApplication(int p0, com.idega.user.data.User p1)
		throws java.rmi.RemoteException, javax.ejb.CreateException, javax.ejb.FinderException, java.rmi.RemoteException;
	public void rejectApplication(int p0, com.idega.user.data.User p1)
		throws java.rmi.RemoteException, javax.ejb.CreateException, javax.ejb.FinderException, java.rmi.RemoteException;
	public se.idega.idegaweb.commune.account.provider.data.ProviderApplication getProviderApplication(int p0)
		throws java.rmi.RemoteException, javax.ejb.FinderException, java.rmi.RemoteException;
	public void rejectApplication(int p0, com.idega.user.data.User p1, java.lang.String p2)
		throws java.rmi.RemoteException, javax.ejb.CreateException, javax.ejb.FinderException, java.rmi.RemoteException;


	/**
	 * Returns a collection of com.idega.core.data.PostalCode
	 */
	public Collection getAvailablePostalCodes() throws java.rmi.RemoteException;
	/**
	 * Returns a collection of com.idega.block.school.data.SchoolType
	 */
	public Collection getAvailableSchoolTypes() throws java.rmi.RemoteException;	
	/**
	 * Returns a collection of com.idega.block.school.data.SchoolArea
	 */
	public Collection getAvailableSchoolAreas() throws java.rmi.RemoteException;


}
