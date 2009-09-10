/*
 * $Id: AccountApplicationBusinessBean.java,v 1.36.2.3 2005/11/29 16:13:03 palli Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.account.business;
import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import se.idega.idegaweb.commune.account.data.AccountApplication;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.message.business.CommuneMessageBusiness;
import se.idega.idegaweb.commune.presentation.CommuneBlock;
import com.idega.block.process.business.CaseBusinessBean;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.accesscontrol.business.LoginCreateException;
import com.idega.core.accesscontrol.business.UserHasLoginException;
import com.idega.core.accesscontrol.data.LoginTable;
import com.idega.core.accesscontrol.data.PasswordNotKnown;
import com.idega.core.business.ICApplicationBindingBusiness;
import com.idega.data.IDOCreateException;
import com.idega.idegaweb.IWBundle;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * @author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */
public abstract class AccountApplicationBusinessBean extends CaseBusinessBean implements AccountBusiness
{
  
  /**
   * If this proberty is true additionally an email is sent to the new user. Otherwise only a letter is sent.
   */
  private static final String PROP_SENT_EMAIL_AFTER_CREATING_NEW_LOGIN = "sent_email_after_creating_new_account_true_false";
  private static final boolean DEFAULT_SENT_EMAIL_AFTER_CREATING_NEW_LOGIN = true;
  
	private static final String COMMUNE_BUNDLE_IDENTIFIER=CommuneBlock.IW_BUNDLE_IDENTIFIER;
	protected abstract Class getCaseEntityClass();
	//	protected CaseHome getCaseHome() throws RemoteException {
	/*protected CaseHome getCaseHome() throws RemoteException {
		return (CaseHome) com.idega.data.IDOLookup.getHome(getCaseEntityClass());
	}
	}*/
	public CommuneMessageBusiness getMessageBusiness() throws RemoteException
	{
		return (CommuneMessageBusiness) this.getServiceInstance(CommuneMessageBusiness.class);
	}
	protected AccountApplication getApplication(int applicationID) throws FinderException
	{
		return (AccountApplication) this.getCase(applicationID);
	}
	
	
	public void acceptApplication(int applicationID, User performer, boolean createUserMessage, boolean createPasswordMessage)
		throws CreateException
		{
			acceptApplication(applicationID, performer, createUserMessage, createPasswordMessage, false);
		
	}
	
	
	/**
	 * Accepts the application for an application with ID applicationID by User performer
	 * @param The id of the application to be accepted
	 * @param performer The User that accepts the application
	 * @param createUserMessage
	 * @param createPasswordMessage
	 * @throws CreateException If there is an error creating data objects.
	 * @throws FinderException If an application with applicationID is not found.
	 */
	
	public void acceptApplication(int applicationID, User performer, boolean createUserMessage, boolean createPasswordMessage, boolean sendEmail)
		throws CreateException
	{
		UserTransaction trans=null;
		try
		{
			trans = this.getSessionContext().getUserTransaction();
			trans.begin();
			AccountApplication theCase = this.getApplication(applicationID);
			changeCaseStatus(theCase, getCaseStatusGranted().getStatus(), performer);
			//int ownerID = ((Integer) theCase.getOwner().getPrimaryKey()).intValue();
			User user = theCase.getOwner();
			if (user == null) {
				user = createUserForApplication(theCase);
			}
			
			//CANNOT DO THIS because if the user lives outside of the municipality he should be in a group that is outside of it until a
			//national registry change moves him to the root group
			//addUserToRootCitizenGroup(user);
			
			//this we can do!
			addUserToRootAcceptedCitizenGroup(user);
			
			createLoginAndSendMessage(theCase,createUserMessage, createPasswordMessage, sendEmail);
			
			//synca vi? skjalfanda og sunnan?

			trans.commit();
		}
		catch (Exception e)
		{
//			System.err.println(e.getMessage());
			e.printStackTrace();
			if(trans!=null){
				try
				{
					trans.rollback();
				}
				catch (SystemException se)
				{
					se.printStackTrace();
				}	
			}
			if ( e instanceof UserHasLoginException )  {
				//throw (UserHasLoginException)e;			
				throw new UserHasLoginException();
			}else {
				throw new CreateException("There was an error accepting the application. Message was: "+e.getMessage());
			}
		}
	}
	
/*	private void addUserToRootCitizenGroup(User user) throws RemoteException {
		getUserBusiness().moveCitizenToCommune(user, IWTimestamp.getTimestampRightNow(),user);		
	}*/
	
	private void addUserToRootAcceptedCitizenGroup(User user) {
		Group acceptedCitizens;
		try {
			acceptedCitizens = getUserBusiness().getRootAcceptedCitizenGroup();
			acceptedCitizens.addGroup(user, IWTimestamp.getTimestampRightNow());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * calls acceptApplication(int applicationID, User performer, Boolean createUserMessage, Boolean createPasswordMessage)
	 * with createUserMessage=shouldEmailBeSentWhenANewAccountIsInserted() and createPasswordMessage=true
	 * @param The id of the application to be accepted
	 * @param performer The User that accepts the application
	 * @throws CreateException If there is an error creating data objects.
	 * @throws FinderException If an application with applicationID is not found.
	 */
	public void acceptApplication(int applicationID, User performer)
		throws CreateException
	{
		acceptApplication(applicationID,performer,shouldEmailBeSentWhenANewAccountIsInserted(),true);
	}
	
	/**
	 * calls acceptApplication(int applicationID, User performer, Boolean createUserMessage, Boolean createPasswordMessage)
	 * with createUserMessage=shouldEmailBeSentWhenANewAccountIsInserted() and createPasswordMessage=true
	 * @param The id of the application to be accepted
	 * @param performer The User that accepts the application
	 * @throws CreateException If there is an error creating data objects.
	 * @throws FinderException If an application with applicationID is not found.
	 */
	public void acceptApplication(int applicationID, User performer, boolean sendEmail, String empty)
	throws CreateException
	{
		acceptApplication(applicationID,performer,shouldEmailBeSentWhenANewAccountIsInserted(),true, sendEmail);
	}
	
/**
	 * calls acceptApplication(int applicationID, User performer, Boolean createUserMessage, Boolean createPasswordMessage)
	 * with createUserMessage=shouldEmailBeSentWhenANewAccountIsInserted()
	 * @param The id of the application to be accepted
	 * @param performer The User that accepts the application
	 * @throws CreateException If there is an error creating data objects.
	 * @throws FinderException If an application with applicationID is not found.
	 */
	public void acceptApplication(int applicationID, User performer, boolean createPasswordMessage)
		throws CreateException
	{
		acceptApplication(applicationID,performer,shouldEmailBeSentWhenANewAccountIsInserted(),createPasswordMessage);
	}
	
	
	public void rejectApplication(int applicationID, User performer)
		throws RemoteException, CreateException, FinderException
	{
		rejectApplication(applicationID, performer, null);
	}
	public void rejectApplication(int applicationID, User performer, String reasonDescription)
		throws RemoteException, CreateException, FinderException
	{
		AccountApplication theCase = this.getApplication(applicationID);
		changeCaseStatus(theCase, getCaseStatusDenied().getStatus(), performer);
		int ownerID = -1;
		try
		{
			ownerID = ((Integer) theCase.getOwner().getPrimaryKey()).intValue();
		}
		catch (NullPointerException e)
		{
			//nothing logged	
		}
		String rejectedSubject = this.getRejectMessageSubject();
		String rejectedBody = this.getRejectMessageBody(theCase, reasonDescription);
		if (ownerID != -1)
		{
			getMessageBusiness().createUserMessage(ownerID, rejectedSubject, rejectedBody);
		}
		else
		{
			sendRejectMessage(theCase, rejectedSubject, rejectedBody);
		}
	}
	protected String getRejectMessageBody(AccountApplication theCase, String reasonDescription)
	{
		//int ownerID = ((Integer) theCase.getOwner().getPrimaryKey()).intValue();
		String rejectedBody = this.getLocalizedString("acc.app.rej.body1", "Dear mr./ms./mrs. ");
		rejectedBody += theCase.getApplicantName() + "\n";
		rejectedBody += this.getLocalizedString("acc.app.rej.body2", "Your application has been rejected.\n");
		if (reasonDescription != null)
		{
			rejectedBody += this.getLocalizedString("acc.app.rej.reason", " The reason was:\n\n") + reasonDescription;
		}
		return rejectedBody;
	}
	public String getRejectMessageSubject()
	{
		return this.getLocalizedString("acc.app.rej.subj", "Concerning your application");
	}
	protected String getAcceptMessageBody(AccountApplication theCase, String login, String password)
	{
		Object[] arguments = { theCase.getApplicantName(), login, password, getApplicationLoginURL() };
		String body = getLocalizedString("acc.app.appr.body", "Dear mr./ms./mrs. {0}\n\nYour application has been accepted and you have been given acces to the system with the following login information:\n\nUserName: {1}\nPassword: {2}\n\nYou can log on via: {3}.");
		return MessageFormat.format(body, arguments);
		
		//int ownerID = ((Integer) theCase.getOwner().getPrimaryKey()).intValue();
		/*String body = this.getLocalizedString("acc.app.acc.body1", "Dear mr./ms./mrs. ");
		body += theCase.getApplicantName() + "\n";
		body += this.getLocalizedString("acc.app.acc.body2", "Your application has been accepted.\n");
		body
			+= this.getLocalizedString("acc.app.acc.body3", "You have been given access to the system with username: ");
		body += "\"" + login + "\"";
		body += this.getLocalizedString("acc.app.acc.body4", " and password: ");
		body += "\"" + password + "\"";
		body += "\n\n";
		body += this.getLocalizedString("acc.app.acc.body5", "You can log on via: ");
		body += getApplicationLoginURL();
		return body;*/
	}
	public String getAcceptMessageSubject()
	{
		return this.getLocalizedString("acc.app.appr.subj", "Your application has been approved");
	}
	protected String getApplicationLoginURL()
	{
		//return "http://nacka1.idega.is";
		return getBundle().getProperty("app_url_login","http://nacka1.idega.is");
	}

	protected void sendAcceptMessage(AccountApplication accAppl, String subject, String body) throws RemoteException
	{
		sendAcceptEMailMessage(accAppl.getEmail(), subject, body);
	}
	protected void sendAcceptEMailMessage(String email, String subject, String body) throws RemoteException
	{
        if (email != null) {
            getMessageBusiness().sendMessage(email, subject, body);
        }
	}
	protected void sendRejectMessage(AccountApplication accAppl, String subject, String body) throws RemoteException
	{
		sendRejectEMailMessage(accAppl.getEmail(), subject, body);
	}
	protected void sendRejectEMailMessage(String email, String subject, String body) throws RemoteException
	{
        if (email != null) {
            getMessageBusiness().sendMessage(email, subject, body);
        }
	}
	/**
	 * Meant to be overrided in subclasses when creating a user from the application
	 */
	protected abstract User createUserForApplication(AccountApplication theCase) throws CreateException, RemoteException;

	
	
	protected void createLoginAndSendMessage(AccountApplication theCase, boolean createUserMessage, boolean createPasswordMessage) throws RemoteException, CreateException, LoginCreateException
	{
		createLoginAndSendMessage(theCase, createUserMessage, createPasswordMessage, false);
	}
	
	/**
	 * Creates a Login for a user with application theCase and send a message to the user that applies if it is successful.
	 * @param theCase The Account Application
	 * @throws CreateException Error creating data objects.
	 * @throws LoginCreateException If an error occurs creating login for the user.
	 */
	protected void createLoginAndSendMessage(AccountApplication theCase, boolean createUserMessage, boolean createPasswordMessage, boolean sendEmail) throws RemoteException, CreateException, LoginCreateException
	{	
		boolean sendLetter = false;
		LoginTable lt;
		String login;
		User citizen;
		citizen = theCase.getOwner();
		lt = getUserBusiness().generateUserLogin(citizen);
		login = lt.getUserLogin();
		try
		{
			String password = lt.getUnencryptedUserPassword();
			String messageBody = this.getAcceptMessageBody(theCase, login, password);
			String messageSubject = this.getAcceptMessageSubject();

			if (createUserMessage){
				this.getMessageBusiness().createUserMessage(citizen, messageSubject, messageBody,sendLetter);
			}			
			if(createPasswordMessage){
				this.getMessageBusiness().createPasswordMessage(citizen,login,password);			
			}
			
			createUserMessage = sendEmail;
			// Why is this done, when the message service does this for you ? ( aron )
			/*
			if(sendEmail){
				Email mail = ((UserBusiness)com.idega.business.IBOLookup.getServiceInstance(getIWApplicationContext(),UserBusiness.class)).getUserMail(citizen);	
				if (mail != null) {
					if (mail.getEmailAddress() != null)
						sendEmail = true;
				try {
					this.getMessageBusiness().sendMessage(mail.getEmailAddress(),messageSubject,messageBody);
				}
				catch (Exception e) {
					System.err.println("Couldn't send message to user via e-mail.");
				}
				}
			}*/
		}
		catch (PasswordNotKnown e)
		{
			//e.printStackTrace();
			throw new IDOCreateException(e);
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
		}
	}
		
/**
	 * calls createLoginAndSendMessage(AccountApplication theCase, boolean createUserMessage, boolean createPasswordMessage)
	 * with createUserMessage determined by shouldEmailBeSentWhenANewAccountIsInserted() and createPasswordMessage=true
	 * @param theCase The Account Application
	 * @throws CreateException Error creating data objects.
	 * @throws LoginCreateException If an error occurs creating login for the user.
	 */
	protected void createLoginAndSendMessage(AccountApplication theCase) throws RemoteException, CreateException, LoginCreateException{
		createLoginAndSendMessage(theCase, shouldEmailBeSentWhenANewAccountIsInserted(), true);
	}
	
	/**
	 * calls createLoginAndSendMessage(AccountApplication theCase, boolean createUserMessage, boolean createPasswordMessage)
	 * with createUserMessage determined by shouldEmailBeSentWhenANewAccountIsInserted()
	 * @param theCase The Account Application
	 * @param createPasswordMessage
	 * @throws CreateException Error creating data objects.
	 * @throws LoginCreateException If an error occurs creating login for the user.
	 */
	protected void createLoginAndSendMessage(AccountApplication theCase, boolean createPasswordMessage) throws RemoteException, CreateException, LoginCreateException{
		createLoginAndSendMessage(theCase, shouldEmailBeSentWhenANewAccountIsInserted(), createPasswordMessage);
	}
	
	protected CommuneUserBusiness getUserBusiness() throws RemoteException
	{
		return (CommuneUserBusiness) this.getServiceInstance(CommuneUserBusiness.class);
	}
	public abstract Collection getAllPendingApplications() throws FinderException, RemoteException;
	public Iterator getAllPendingApplicationsIterator() throws FinderException, RemoteException
	{
		return getAllPendingApplications().iterator();
	}
	public abstract Collection getAllRejectedApplications() throws FinderException, RemoteException;
	public Iterator getAllRejectedApplicationsIterator() throws FinderException, RemoteException
	{
		return getAllRejectedApplications().iterator();
	}
	public abstract Collection getAllAcceptedApplications() throws FinderException, RemoteException;
	public Iterator getAllAcceptedApplicationsIterator() throws FinderException, RemoteException
	{
		return getAllAcceptedApplications().iterator();
	}

	public String getBundleIdentifier()
	{
		return COMMUNE_BUNDLE_IDENTIFIER;
	}
  
  private boolean shouldEmailBeSentWhenANewAccountIsInserted()  {
    String result;
    String defaultString = String.valueOf(DEFAULT_SENT_EMAIL_AFTER_CREATING_NEW_LOGIN);
    IWBundle iwb = getIWApplicationContext().getIWMainApplication().getBundle(getBundleIdentifier());
    result = getPropertyValue(iwb, PROP_SENT_EMAIL_AFTER_CREATING_NEW_LOGIN, defaultString);
    return ((String.valueOf(true)).equals(result));
  }
  
	/**
	 * Gets the value for a property name ... replaces the bundle properties that were used previously
	 * @param propertyName
	 * @return
	 */
	private String getPropertyValue(IWBundle iwb, String propertyName, String defaultValue) {
		try {
			String value = getBindingBusiness().get(propertyName);
			if (value != null) {
				return value;
			}
			else {
				value = iwb.getProperty(propertyName);
				getBindingBusiness().put(propertyName, value != null ? value : defaultValue);
			}
		}
		catch (RemoveException re) {
			re.printStackTrace();
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
		catch (CreateException ce) {
			ce.printStackTrace();
		}
		return defaultValue;
	}
	
	private ICApplicationBindingBusiness getBindingBusiness() {
		try {
			return (ICApplicationBindingBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), ICApplicationBindingBusiness.class);
		}
		catch (IBOLookupException ibe) {
			throw new IBORuntimeException(ibe);
		}
	}
}