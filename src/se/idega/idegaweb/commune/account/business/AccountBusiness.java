package se.idega.idegaweb.commune.account.business;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.*;

public interface AccountBusiness extends com.idega.business.IBOService
{
	
	public  Collection getAllPendingApplications()throws FinderException,RemoteException;
	public Iterator getAllPendingApplicationsIterator() throws FinderException, RemoteException;
	
	public  Collection getAllRejectedApplications()throws FinderException,RemoteException;
	public Iterator getAllRejectedApplicationsIterator() throws FinderException, RemoteException;
	
	public  Collection getAllAcceptedApplications()throws FinderException,RemoteException;
	public Iterator getAllAcceptedApplicationsIterator() throws FinderException, RemoteException;
}
