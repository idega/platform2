package se.idega.idegaweb.commune.account.provider.business;
import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.account.business.AccountApplicationBusinessBean;
import se.idega.idegaweb.commune.account.business.AccountBusiness;
import se.idega.idegaweb.commune.account.business.IncompleteApplicationException;
import se.idega.idegaweb.commune.account.provider.data.ProviderApplication;
import se.idega.idegaweb.commune.account.provider.data.ProviderApplicationHome;
import com.idega.data.IDOCreateException;
import com.idega.user.data.User;
import com.idega.util.Validator;
import com.idega.block.process.data.*;

/**
 * Title:        idegaWeb
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega software
 * @author tryggvil
 * @version 1.0
 */

public class ProviderAccountBusinessBean extends AccountApplicationBusinessBean implements ProviderAccountBusiness,AccountBusiness{
	
	protected Class getCaseEntityClass(){
		return ProviderApplication.class;	
	}
	
	public void acceptApplication(int applicationID, int performerUserID)throws CreateException,RemoteException{
		try{
			User user = this.getUser(performerUserID);
			this.acceptApplication(applicationID,user);
		}
		catch(FinderException fe){
			throw new IDOCreateException(fe);	
		}
	}
	
	public void acceptApplication(int applicationID, User performer)
		throws RemoteException, CreateException, FinderException {
		super.acceptApplication(applicationID, performer);
		
	}
	public void rejectApplication(int applicationID, User performer)
		throws RemoteException, CreateException, FinderException {
		rejectApplication(applicationID, performer,"");
	}
	
	public void rejectApplication(int applicationID, int performerUserID)throws CreateException,RemoteException{
		try{
			User user = this.getUser(performerUserID);
			this.rejectApplication(applicationID,user);
		}
		catch(FinderException fe){
			throw new IDOCreateException(fe);	
		}
	}

	public void rejectApplication(int applicationID, User performer, String reasonDescription)
		throws RemoteException, CreateException, FinderException {
		super.rejectApplication(applicationID, performer, reasonDescription);
		
	}

	protected ProviderApplicationHome getProviderApplicationHome() {
		try {
			return (ProviderApplicationHome) this.getIDOHome(ProviderApplication.class);
		}
		catch (RemoteException e) {
			throw new EJBException(e.getMessage());
		}
	}
	public ProviderApplication getProviderApplication(int applicationID)throws RemoteException,FinderException{
		return this.getProviderApplicationHome().findByPrimaryKey(new Integer(applicationID));	
	}
	public ProviderApplication createApplication(
		String providerName,
		String address,
		String telephone,
		int numberOfPlaces,
		String managerName,
		String managerEmail,
		String additionalInfo)
		throws CreateException {
		try {
			//if((providerName==null)||(address==null)||(telephone==null)||(numberOfPlaces==null)||(managerName==null)||(managerEmail==null))
			if (!getValidator().isStringValid(providerName))
				throw new IncompleteApplicationException("Provider name is not set");
			if (!getValidator().isStringValid(address))
				throw new IncompleteApplicationException("Address is not set");
			if (!getValidator().isStringValid(telephone))
				throw new IncompleteApplicationException("Telephone is not set");
			if ((numberOfPlaces < 1))
				throw new IncompleteApplicationException("Number of Places is invalid");
			if (!getValidator().isStringValid(managerName))
				throw new IncompleteApplicationException("Manager Name is not set");
			if (!getValidator().isEmail(managerEmail))
				throw new IncompleteApplicationException("Manager Email is invalid");

			ProviderApplication appl= getProviderApplicationHome().create();
			appl.setAddress(address);
			appl.setName(providerName);
			appl.setManagerName(managerName);
			appl.setAdditionalInfo(additionalInfo);
			appl.setEmailAddress(managerEmail);
			appl.setNumberOfPlaces(numberOfPlaces);
			appl.store();
			return appl;
		}
		catch (Exception e) {
			throw new IDOCreateException(e);
		}
	}

	
	protected Validator getValidator(){
		return Validator.getInstance();
	}

	/**
	 * @see se.idega.idegaweb.commune.account.business.AccountApplicationBusinessBean#getAllAcceptedApplications()
	 */
	public Collection getAllAcceptedApplications()throws RemoteException,FinderException
	{
			return getProviderApplicationHome().findAllApprovedApplications();
	}

	/**
	 * @see se.idega.idegaweb.commune.account.business.AccountApplicationBusinessBean#getAllPendingApplications()
	 */
	public Collection getAllPendingApplications()throws RemoteException,FinderException
	{
			return getProviderApplicationHome().findAllPendingApplications();
	}

	/**
	 * @see se.idega.idegaweb.commune.account.business.AccountApplicationBusinessBean#getAllRejectedApplications()
	 */
	public Collection getAllRejectedApplications()throws RemoteException,FinderException
	{
			return getProviderApplicationHome().findAllRejectedApplications();
	}

}