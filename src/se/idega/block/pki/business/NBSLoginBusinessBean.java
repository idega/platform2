/*
 * Created on 26.5.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.block.pki.business;

import java.io.File;

import se.nexus.nbs.sdk.HttpMessage;
import se.nexus.nbs.sdk.NBSAuthResult;
import se.nexus.nbs.sdk.NBSException;
import se.nexus.nbs.sdk.NBSResult;
import se.nexus.nbs.sdk.NBSServerFactory;
import se.nexus.nbs.sdk.NBSServerHttp;
import se.nexus.nbs.sdk.servlet.ServletUtil;

import com.idega.core.accesscontrol.business.LoginBusinessBean;
import com.idega.core.accesscontrol.business.LoginCreateException;
import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.core.accesscontrol.data.LoginTable;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWException;
import com.idega.presentation.IWContext;
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
	

	public final static String IWEX_PKI_USR_NOT_REGISTERED ="IWEX_PKI_USR_NOT_REGISTERED";
	public final static String IWEX_USER_HAS_NO_ACCOUNT = "IWEX_USER_HAS_NO_ACCOUNT";

	/** Names for objects stored in the servlet context or session. */
	private final static String SERVER_FACTORY = "se.idega.block.pki.ServerFactory", SERVER = "se.idega.block.pki.Server", SERVLET_URI = "se.nexus.cbt.ServletURI";
	
	/**
	 * 
	 */
	public NBSLoginBusinessBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * The method invoked when the login presentation module sends a login to this class
	 */
	public boolean actionPerformed(IWContext iwc) throws IWException {
		System.out.println("NBSLoginBusiness.actionPerformed(...) - begins");
		//NBSLoginBusinessBean.removeNBSException(iwc);
		//NBSLoginBusinessBean.removeException(iwc);
		NBSResult result = null;


		try {

			// Get the server object.
			NBSServerHttp server = this.getNBSServer(iwc);
			HttpMessage httpReq = new HttpMessage();
			ServletUtil.servletRequestToHttpMessage(iwc.getRequest(), httpReq);

			// No action specified means that a message
			// probably has been received.

			System.out.println("NBSLoginBusiness - Message received");

			//							System.out.println("ib_page"+" = "+req.getParameter("ib_page"));
			//							System.out.println("ib_error_page"+" = "+req.getParameter("ib_error_page"));

			// Process the message.

			result = server.handleMessage(httpReq);

			// Interpret the result.
			int type = result.getType();
			switch (type) {
				case (NBSResult.TYPE_AUTH) :

					System.out.println("NBSResult = TYPE_AUTH");
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
			System.err.println(mpse.getMessage());
			mpse.printStackTrace();
			//printErrorCode(res, mpse.getCode(), mpse.getMessage());
		} catch (Exception e) {
			this.carryOnException(iwc, e);
			e.printStackTrace();
			System.out.println("Exception");
			//printErrorMessage(res, e.getMessage());
		}
		System.out.println("NBSLoginBusiness.actionPerformed(...) - ends");
		return true;
	}

	/**
	 * 
	 * @param loginRecords - all login records for one user
	 * @return LoginTable record to log on the system
	 */
	protected LoginTable chooseLoginRecord(IWContext iwc, LoginTable[] loginRecords) throws Exception {
		LoginTable chosenRecord = null;
		if (loginRecords != null)
			for (int i = 0; i < loginRecords.length; i++) {
				String type = loginRecords[i].getLoginType();
				if (type != null && type.equals(PKI_LOGIN_TYPE)) {
					chosenRecord = loginRecords[i];
				}
			}

		if (chosenRecord == null) {

			if (loginRecords.length > 0) {
				String newLogin = StringHandler.getRandomString(20);
				chosenRecord = LoginDBHandler.createLogin(loginRecords[0].getUserId(), newLogin, "noPassword");
				chosenRecord.setLoginType(NBSLoginBusinessBean.PKI_LOGIN_TYPE);
				chosenRecord.store();
				return chosenRecord;
			} else {
				
				Exception e = new LoginCreateException(IWEX_USER_HAS_NO_ACCOUNT);
				this.carryOnException(iwc,e);
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
		iwc.getApplicationContext().removeApplicationAttribute(SERVER_FACTORY); //TODO Roar - Temporary

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
		String servletUri = (String) (iwc.getServletContext().getAttribute(SERVLET_URI));

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
		//String commonName = authResult.getSubjectAttributeValue(nameKey);
		//String country = authResult.getSubjectAttributeValue(countryKey);
		//String organization = authResult.getSubjectAttributeValue(organizationKey);
		try {

			//TODO Change this implementation to a more graceful usage of IWContext or equivalent
			iwc.setServletContext(iwc.getServletContext());
			//

			loginSuccessful = this.logInByPersonalID(iwc, personalID);
			
			if(!loginSuccessful){
				throw new Exception(IWEX_PKI_USR_NOT_REGISTERED);
			}

			System.out.println("idegaWeb Login " + ((loginSuccessful) ? "successful" : "failed") + " for personalId : '" + personalID + "'");
		} catch (Exception ex) {
			this.carryOnException(iwc,ex);
			System.out.println("idegaWeb Login failed for personalId : '" + personalID + "'");
			ex.printStackTrace();
			
		}
		return loginSuccessful;
	}

	private String getConfigFilePath(IWApplicationContext iwac){
		IWBundle iwb = getBundle(iwac);
		String path = iwb.getProperty(BIDT_SDK_PATH_PROPERTY);
		return path != null ? path : iwb.getPropertiesRealPath()+File.separator+"bidt_sdk.properties";
	}
	
	private IWBundle getBundle(IWApplicationContext iwac){
			return iwac.getApplication().getBundle(IW_BUNDLE_IDENTIFIER);
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

}
