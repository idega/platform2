package se.idega.idegaweb.commune.account.provider.business;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.account.business.AccountApplicationBusinessBean;
import se.idega.idegaweb.commune.account.business.AccountBusiness;
import se.idega.idegaweb.commune.account.business.IncompleteApplicationException;
import se.idega.idegaweb.commune.account.data.AccountApplication;
import se.idega.idegaweb.commune.account.provider.data.ProviderApplication;
import se.idega.idegaweb.commune.account.provider.data.ProviderApplicationHome;

import com.idega.core.data.PostalCode;
import com.idega.core.data.PostalCodeHome;
import com.idega.data.IDOCreateException;
import com.idega.user.data.User;
import com.idega.util.ListUtil;
import com.idega.util.Validator;
import com.idega.block.process.data.*;
import com.idega.block.school.business.SchoolAreaBusiness;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.business.SchoolTypeBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolType;
/**
 * Title:        idegaWeb
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega software
 * @author tryggvil
 * @version 1.0
 */
public class ProviderAccountBusinessBean
	extends AccountApplicationBusinessBean
	implements ProviderAccountBusiness, AccountBusiness
{
	protected Class getCaseEntityClass()
	{
		return ProviderApplication.class;
	}
	public void acceptApplication(int applicationID, int performerUserID) throws CreateException, RemoteException
	{
		try
		{
			User user = this.getUser(performerUserID);
			this.acceptApplication(applicationID, user);
		}
		catch (FinderException fe)
		{
			throw new IDOCreateException(fe);
		}
	}
	public void acceptApplication(int applicationID, User performer)
		throws RemoteException, CreateException, FinderException
	{
		super.acceptApplication(applicationID, performer);
	}
	public void rejectApplication(int applicationID, User performer)
		throws RemoteException, CreateException, FinderException
	{
		rejectApplication(applicationID, performer, "");
	}
	public void rejectApplication(int applicationID, int performerUserID) throws CreateException, RemoteException
	{
		try
		{
			User user = this.getUser(performerUserID);
			this.rejectApplication(applicationID, user);
		}
		catch (FinderException fe)
		{
			throw new IDOCreateException(fe);
		}
	}
	public void rejectApplication(int applicationID, User performer, String reasonDescription)
		throws RemoteException, CreateException, FinderException
	{
		super.rejectApplication(applicationID, performer, reasonDescription);
	}
	protected ProviderApplicationHome getProviderApplicationHome()
	{
		try
		{
			return (ProviderApplicationHome) this.getIDOHome(ProviderApplication.class);
		}
		catch (RemoteException e)
		{
			throw new EJBException(e.getMessage());
		}
	}
	public ProviderApplication getProviderApplication(int applicationID) throws RemoteException, FinderException
	{
		return this.getProviderApplicationHome().findByPrimaryKey(new Integer(applicationID));
	}
	
	public ProviderApplication createApplication(
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
		throws CreateException
	{

		int[] schoolTypeIDs = {schoolTypeID};
		return createApplication(providerName,address,telephone,numberOfPlaces,managerName,managerEmail,additionalInfo,
			postalCodeID,
			schoolTypeIDs,
			schoolAreaID);
	}

	
	public ProviderApplication createApplication(
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
		throws CreateException
	{
		try
		{
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
			ProviderApplication appl = getProviderApplicationHome().create();
			appl.setAddress(address);
			appl.setName(providerName);
			appl.setManagerName(managerName);
			appl.setAdditionalInfo(additionalInfo);
			appl.setEmailAddress(managerEmail);
			appl.setNumberOfPlaces(numberOfPlaces);
			appl.setPhone(telephone);
			if(postalCodeID!=-1){
					appl.setPostalCode(postalCodeID);
			}
			if(schoolTypeIDs!=null){
					appl.setSchoolTypes(schoolTypeIDs);					
			}
			if(schoolAreaID!=-1){
					appl.setSchoolArea(schoolAreaID);					
			}
			appl.store();
			return appl;
		}
		catch (Exception e)
		{
			throw new IDOCreateException(e);
		}
	}
	protected Validator getValidator()
	{
		return Validator.getInstance();
	}
	/**
	 * @see se.idega.idegaweb.commune.account.business.AccountApplicationBusinessBean#getAllAcceptedApplications()
	 */
	public Collection getAllAcceptedApplications() throws RemoteException, FinderException
	{
		return getProviderApplicationHome().findAllApprovedApplications();
	}
	/**
	 * @see se.idega.idegaweb.commune.account.business.AccountApplicationBusinessBean#getAllPendingApplications()
	 */
	public Collection getAllPendingApplications() throws RemoteException, FinderException
	{
		return getProviderApplicationHome().findAllPendingApplications();
	}
	/**
	 * @see se.idega.idegaweb.commune.account.business.AccountApplicationBusinessBean#getAllRejectedApplications()
	 */
	public Collection getAllRejectedApplications() throws RemoteException, FinderException
	{
		return getProviderApplicationHome().findAllRejectedApplications();
	}
	public AccountApplication getApplication(int applicationID) throws RemoteException, FinderException
	{
		return getProviderApplication(applicationID);
	}
	/**
	 * Overrided from superclass
	 */
	protected User createUserForApplication(AccountApplication theCase) throws CreateException, RemoteException
	{
		School school = createSchoolForApplication(theCase);
		User user = createProviderAdministratorForApplication(theCase,school);
		//createLoginAndSendMessage(theCase);
		return user;
	}	
	
	/**
	 * Creates a School/provider administrator from the application in the Commune system
	 */
	protected User createProviderAdministratorForApplication(AccountApplication theCase,School school) throws CreateException, RemoteException
	{
		String firstName = theCase.getApplicantName().substring(0, theCase.getApplicantName().indexOf(" "));
		String lastName =
			theCase.getApplicantName().substring(
				theCase.getApplicantName().lastIndexOf(" ") + 1,
				theCase.getApplicantName().length());
		User user = null;
		try{
			user = getUserBusiness().createProviderAdministrator(firstName, null, lastName, school);
		}
		catch(FinderException fe){
			throw new IDOCreateException(fe);	
		}
		theCase.setOwner(user);
		theCase.store();
		return user;
	}

	protected School createSchoolForApplication(AccountApplication theCase) throws CreateException, RemoteException
	{
		ProviderApplication appl = ((ProviderApplication)theCase);
		String providerName = appl.getName();
		String address = appl.getAddress();
		String phone = appl.getPhone();
		PostalCode pCode = appl.getPostalCode();
		String zipcode = pCode.getPostalCode();
		String ziparea = pCode.getName();
		/**
		 * @todo: Remove hardcoding
		 */
		//int school_type = 1;
		//int school_area = 1;
		int school_area = ((Integer)appl.getSchoolArea().getPrimaryKey()).intValue();
		//int[] school_types = {1};
		Collection schoolTypes = appl.getSchoolTypes();
		int[] school_types = new int[schoolTypes.size()];
		int i=0;
		for (Iterator iter = schoolTypes.iterator(); iter.hasNext();)
		{
			SchoolType element = (SchoolType) iter.next();
			int schTypeID = ((Integer)element.getPrimaryKey()).intValue();
			school_types[i++]=schTypeID;
		}
		School school = getSchoolBusiness().createSchool(providerName,address,phone,zipcode,ziparea,school_area,school_types);
		return school;
	}


	/**
	 * Returns a collection of com.idega.core.data.PostalCode
	 */
	public Collection getAvailablePostalCodes() throws java.rmi.RemoteException{
		try {
			Collection coll = null;
			coll = getPostalCodeHome().findAllOrdererByCode();
			return coll;
		} catch (FinderException e) {
			return ListUtil.getEmptyVector();
		}
	}

	/**
	 * Returns a collection of com.idega.block.school.data.SchoolType
	 */
	public Collection getAvailableSchoolTypes() throws java.rmi.RemoteException{
		return getSchoolTypeBusiness().findAllSchoolTypes();
	}
	
	/**
	 * Returns a collection of com.idega.block.school.data.SchoolArea
	 */
	public Collection getAvailableSchoolAreas() throws java.rmi.RemoteException{
		return getSchoolAreaBusiness().findAllSchoolAreas();
	}


	protected SchoolBusiness getSchoolBusiness() throws RemoteException
	{
		SchoolBusiness bus = (SchoolBusiness)this.getServiceInstance(SchoolBusiness.class);
		return bus;
	}
	
	protected SchoolTypeBusiness getSchoolTypeBusiness() throws RemoteException
	{
		SchoolTypeBusiness bus = (SchoolTypeBusiness)this.getServiceInstance(SchoolTypeBusiness.class);
		return bus;
	}

	protected SchoolAreaBusiness getSchoolAreaBusiness() throws RemoteException
	{
		SchoolAreaBusiness bus = (SchoolAreaBusiness)this.getServiceInstance(SchoolAreaBusiness.class);
		return bus;
	}
	
	protected PostalCodeHome getPostalCodeHome() throws RemoteException
	{
		PostalCodeHome bus = (PostalCodeHome)this.getIDOHome(PostalCode.class);
		return bus;
	}
}
