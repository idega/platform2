/*
 * Created on 26.5.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.block.pki.business;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.FinderException;
import javax.servlet.http.HttpServletResponse;
import se.nexus.nbs.sdk.HttpMessage;
import se.nexus.nbs.sdk.NBSAuthResult;
import se.nexus.nbs.sdk.NBSException;
import se.nexus.nbs.sdk.NBSResult;
import se.nexus.nbs.sdk.NBSServerFactory;
import se.nexus.nbs.sdk.NBSServerHttp;
import se.nexus.nbs.sdk.servlet.ServletUtil;
import com.idega.core.accesscontrol.business.LoggedOnInfo;
import com.idega.core.accesscontrol.business.LoginBusinessBean;
import com.idega.core.accesscontrol.business.LoginCreateException;
import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.core.accesscontrol.data.LoginTable;
import com.idega.core.accesscontrol.data.LoginTableHome;
import com.idega.core.builder.business.BuilderService;
import com.idega.core.builder.business.BuilderServiceFactory;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWContext;
import com.idega.servlet.filter.IWAuthenticator;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.StringHandler;

/**
 * @author <a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class NBSLoginBusinessBean extends LoginBusinessBean {

	private final static String IW_BUNDLE_IDENTIFIER = "se.idega.block.pki";
	private final static String BIDT_SDK_PATH_PROPERTY = "bidt_sdk_path";
	public final static String PKI_LOGIN_TYPE = "se-pki-nexus";
	public final static String PKI_NBSEXCEPTION = "se-pki-nexus-nbsexception";
	public final static String PKI_EXCEPTION = "se-pki-nexus-exception";

	public final static String IWEX_PKI_USR_NOT_REGISTERED = "IWEX_PKI_USR_NOT_REGISTERED";
	public final static String IWEX_USER_HAS_NO_ACCOUNT = "IWEX_USER_HAS_NO_ACCOUNT";

	private final static String NBS_BANKID_LOGIN_RESULT = "nbs_bankid_login_result";

	/** Names for objects stored in the servlet context or session. */
	private final static String SERVER_FACTORY = "se.idega.block.pki.ServerFactory", SERVER = "se.idega.block.pki.Server", SERVLET_URI = "se.nexus.cbt.ServletURI";

	/**
	 * 
	 */
	public NBSLoginBusinessBean() {
		super();
	}

	/**
	 * The method invoked when the login presentation module sends a login to this class
	 */
	public boolean actionPerformed(IWContext iwc) {
		NBSResult result = null;
		try {
			// Get the server object.
			NBSServerHttp server = this.getNBSServer(iwc);
			HttpMessage httpReq = new HttpMessage();
			ServletUtil.servletRequestToHttpMessage(iwc.getRequest(), httpReq);

			// No action specified means that a message
			// probably has been received.

			// Process the message.
			result = server.handleMessage(httpReq);

			// Interpret the result.
			int type = result.getType();
			switch (type) {
				case (NBSResult.TYPE_AUTH) :
					this.logOutBankID(iwc);
					NBSLoggedOnInfo info = (NBSLoggedOnInfo)createLoggedOnInfo(iwc);
					info.setNbsAuthResult((NBSAuthResult)result);
					this.setBankIDLoggedOnInfo(iwc, info);
					logInUser(iwc, result);
					
					break;
				case (NBSResult.TYPE_SIGN) :
					throw new Exception("Unexpected result: NBSResult = TYPE_SIGN");
				case (NBSResult.TYPE_MESSAGE) :
					throw new Exception("Unexpected result: NBSResult = TYPE_MESSAGE");
				default :
					throw new Exception("Unknown result");
			}
		} catch (NBSException mpse) {
			this.carryOnNBSException(iwc, mpse);
			//System.err.println(mpse.getMessage());
			//mpse.printStackTrace();
			//printErrorCode(res, mpse.getCode(), mpse.getMessage());
		} catch (Exception e) {
			this.carryOnException(iwc, e);
			//System.out.println("Exception:"+e.getMessage());
			//e.printStackTrace();
			//printErrorMessage(res, e.getMessage());
		}
		return true;
	}

	/**
	 * if requireExisitingLogin is true then this method throws an exception if the user hasn't already gotten a login, otherwise it will create a new bankId login
	 * @return LoginTable record to log on the system
	 */
	public LoginTable chooseLoginRecord(IWContext iwc, LoginTable[] loginRecords, User user,boolean requireExisitingLogin) throws Exception {
		LoginTable chosenRecord = null;
		if (loginRecords != null) {
			for (int i = 0; i < loginRecords.length; i++) {
				String type = loginRecords[i].getLoginType();
				if (type != null && type.equals(PKI_LOGIN_TYPE)) {
					chosenRecord = loginRecords[i];
				}
			}
		}

		if (chosenRecord == null) {
			boolean mayCreateNewLogin=false;
			if(!requireExisitingLogin){
				mayCreateNewLogin=true;
			}
			else{
				if(loginRecords.length > 0){
					mayCreateNewLogin=true;
				}
			}
			
			if(mayCreateNewLogin){
			//if (loginRecords.length > 0) {
				String newLogin = StringHandler.getRandomString(20);
				chosenRecord = LoginDBHandler.createLogin(user.getID(), newLogin, "noPassword");
				chosenRecord.setLoginType(NBSLoginBusinessBean.PKI_LOGIN_TYPE);
				chosenRecord.store();
				return chosenRecord;
			} else {
				Exception e = new Exception(IWEX_USER_HAS_NO_ACCOUNT + "#" + user.getPersonalID() + "#");
				this.carryOnException(iwc, e);
				throw e;
			}

			//			try {

			//				throw new LoginCreateException("PKI login record could not be created");
			//			} catch (LoginCreateException e) {
			//				System.out.println(e.getMessage());
			//				e.printStackTrace();
			//				return null;
			//			}
		} else {
			return chosenRecord;
		}
	}

	public NBSServerFactory getServerGenerator(IWContext iwc) throws NBSException {
//		iwc.getApplicationContext().removeApplicationAttribute(SERVER_FACTORY); 

		NBSServerFactory serverGenerator = (NBSServerFactory)iwc.getApplicationContext().getApplicationAttribute(SERVER_FACTORY);
		if (serverGenerator == null) {
			File configFile = new File(getConfigFilePath(iwc));
			//System.out.println("configFile: "+ configFile);	
			serverGenerator = new NBSServerFactory();
			serverGenerator.init(configFile);
			iwc.getApplicationContext().setApplicationAttribute(SERVER_FACTORY, serverGenerator);
		}

		return serverGenerator;
	}

	/**
	 * Gets the BidtServer instance.
	 */
	public NBSServerHttp getNBSServer(IWContext iwc) throws NBSException {
		NBSServerFactory serverGenerator = getServerGenerator(iwc);

		// If created, the server should be in the session.
		NBSServerHttp server = (NBSServerHttp)iwc.getSession().getAttribute(SERVER);

		// If not created, create it now.
		if (server == null) {
			// Create a server and save it in the session.
			server = (NBSServerHttp)serverGenerator.getInstance("Http");
			iwc.getSession().setAttribute(SERVER, server);

		} else {
			// In case of a replicated session environment
			serverGenerator.updateInstance(server);
		}
		String servletUri = (String) (iwc.getApplicationAttribute(SERVLET_URI));

		server.setActionUrl(servletUri);

		return server;
	}

	/**
	 * Method called after a successful authentication to the BankID server to do the idegaWeb login.
	 * @return
	 */
	private boolean logInUser(IWContext iwc, NBSResult result) {
		boolean loginSuccessful = false;
		NBSAuthResult authResult = (NBSAuthResult)result;

		String personalIDKey = "serialNumber";
		String personalID = authResult.getSubjectAttributeValue(personalIDKey);
		try {
			loginSuccessful = this.logInByPersonalID(iwc, personalID);

			System.out.println("idegaWeb Login " + ((loginSuccessful) ? "successful" : "failed") + " for personalId : '" + personalID + "'");
			if (!loginSuccessful) {
				throw new Exception(IWEX_PKI_USR_NOT_REGISTERED + "#" + personalID + "#");
			}
			
			if (iwc.isParameterSet(IWAuthenticator.PARAMETER_REDIRECT_USER_TO_PRIMARY_GROUP_HOME_PAGE)){
				if(iwc.isLoggedOn()||LoginBusinessBean.isLogOnAction(iwc)) {
					Group prmg = iwc.getCurrentUser().getPrimaryGroup(); 
					if (prmg != null) {
						int homePageID = prmg.getHomePageID();
						if (homePageID > 0) {
							BuilderService builderService = BuilderServiceFactory.getBuilderService(iwc);
							HttpServletResponse response = iwc.getResponse();
							response.sendRedirect(builderService.getPageURI(homePageID));
						}
					}
				}
			}
			
		} catch (Exception ex) {
			this.carryOnException(iwc, ex);
			//System.out.println("idegaWeb Login failed for personalId : '" + personalID + "'");
			//ex.printStackTrace();

		}
		return loginSuccessful;
	}

	private String getConfigFilePath(IWApplicationContext iwac) {
		IWBundle iwb = getBundle(iwac);
		String path = iwb.getProperty(BIDT_SDK_PATH_PROPERTY);
		return path != null ? path : iwb.getPropertiesRealPath() + File.separator + "bidt_sdk.properties";
	}

	private IWBundle getBundle(IWApplicationContext iwac) {
		return iwac.getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER);
	}

	private void carryOnNBSException(IWContext iwc, NBSException e) {
		iwc.setSessionAttribute(PKI_NBSEXCEPTION, e);
	}

	public static void removeNBSException(IWContext iwc) {
		iwc.removeSessionAttribute(PKI_NBSEXCEPTION);
	}

	/**
	 * 
	 * @param iwc
	 * @return returns NBSExeption that has occured, else null
	 */
	public static NBSException getNBSException(IWContext iwc) {
		return (NBSException)iwc.getSessionAttribute(PKI_NBSEXCEPTION);
	}

	private void carryOnException(IWContext iwc, Exception e) {
		if (e instanceof NBSException) {
			carryOnNBSException(iwc, (NBSException)e);
		} else {
			iwc.setSessionAttribute(PKI_EXCEPTION, e);
		}
	}

	public static void removeException(IWContext iwc) {
		iwc.removeSessionAttribute(PKI_EXCEPTION);
	}

	/**
	 * 
	 * @param iwc
	 * @return returns Exeption that has occured, else null
	 */
	public static Exception getException(IWContext iwc) {
		return (Exception)iwc.getSessionAttribute(PKI_EXCEPTION);
	}

	public LoggedOnInfo createLoggedOnInfo(IWContext iwc) {
		LoggedOnInfo info = getBankIDLoggedOnInfo(iwc);
		if (info == null) {
			info = new NBSLoggedOnInfo();
		}
		return info;
	}

	public NBSLoggedOnInfo getBankIDLoggedOnInfo(IWContext iwc) {
		return (NBSLoggedOnInfo)iwc.getSessionAttribute(NBS_BANKID_LOGIN_RESULT);
	}

	private void setBankIDLoggedOnInfo(IWContext iwc, NBSLoggedOnInfo info) {
		iwc.setSessionAttribute(NBS_BANKID_LOGIN_RESULT, info);
	}

	public void logOutBankID(IWContext iwc) {
		iwc.removeSessionAttribute(NBS_BANKID_LOGIN_RESULT);
	}

	public void logOut(IWContext iwc) throws Exception {
		super.logOut(iwc);
		this.logOutBankID(iwc);
	}

	/**
	 * temp: same implementation as in superclass
	 * This method by default throws an exception if the user hasn't already gotten a login.
	 */
	public boolean logInByPersonalID(IWContext iwc, String personalID) throws Exception {
		return logInByPersonalID(iwc,personalID,true);
	}
	
	/**
	 * if requireExisitingLogin is true then this method throws an exception if the user hasn't already gotten a login.
	 */
	public boolean logInByPersonalID(IWContext iwc, String personalID,boolean requireExistingLogin) throws Exception {
		boolean returner = false;
		try {
			com.idega.user.data.User user = getUserBusiness(iwc).getUser(personalID);
			//LoginTable[] login_table = (LoginTable[]) (com.idega.core.accesscontrol.data.LoginTableBMPBean.getStaticInstance()).findAllByColumn(com.idega.core.accesscontrol.data.LoginTableBMPBean.getColumnNameUserID(), user.getPrimaryKey().toString());

			Collection loginRecords = ((LoginTableHome)IDOLookup.getHome(LoginTable.class)).findLoginsForUser(user);
			LoginTable[] login_table = (LoginTable[])loginRecords.toArray(new LoginTable[loginRecords.size()]);


			LoginTable lTable = this.chooseLoginRecord(iwc, login_table, user,requireExistingLogin);
			if (lTable != null) {
				returner = logIn(iwc, lTable);
				if (returner)
					onLoginSuccessful(iwc);
			} else {
				try {
					throw new LoginCreateException("No record chosen");
				} catch (LoginCreateException e1) {
					e1.printStackTrace();
				}
			}

		} catch (FinderException e) {
			System.err.println("User with personalId:"+personalID+" not found in db.");
			returner = false;
		}
		return returner;

	}

	public static NBSLoginBusinessBean createNBSLoginBusiness() {
		return new NBSLoginBusinessBean();
	}

	public boolean hasBankLogin(User user){
		try {
			Collection loginRecords = ((LoginTableHome)IDOLookup.getHome(LoginTable.class)).findLoginsForUser(user);
			
			for (Iterator iter = loginRecords.iterator(); iter.hasNext();) {
				String type = ((LoginTable)iter.next()).getLoginType();
				if (type != null && type.equals(PKI_LOGIN_TYPE)) {
					return true;
				}
			}
		} catch (IDOLookupException e) {
			e.printStackTrace();
			return false;
		} catch (FinderException e) {
			e.printStackTrace();
			return false;
		}
		
		//LoginTable[] loginRecords = (LoginTable[]) (com.idega.core.accesscontrol.data.LoginTableBMPBean.getStaticInstance()).findAllByColumn(com.idega.core.accesscontrol.data.LoginTableBMPBean.getColumnNameUserID(), Integer.toString(userID));
		
//		if (loginRecords != null) {
//			for (int i = 0; i < loginRecords.length; i++) {
//				String type = loginRecords[i].getLoginType();
//				if (type != null && type.equals(PKI_LOGIN_TYPE)) {
//					return true;
//				}
//			}
//		}

		return false;
	}

}
