/*
 * Created on 22.5.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.block.pki.presentation;

import se.idega.block.pki.business.NBSLoginBusinessBean;
import se.nexus.nbs.sdk.HttpMessage;
import se.nexus.nbs.sdk.NBSException;
import se.nexus.nbs.sdk.NBSMessageHttp;
import se.nexus.nbs.sdk.NBSMessageResult;
import se.nexus.nbs.sdk.NBSServerHttp;
import se.nexus.nbs.sdk.servlet.ServletUtil;

import com.idega.block.login.presentation.Login;
import com.idega.builder.data.IBPage;
import com.idega.core.accesscontrol.business.LoggedOnInfo;
import com.idega.core.accesscontrol.business.LoginBusinessBean;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;

/**
 * @author <a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class NBSLogin extends Login {

	private final static String IW_BUNDLE_IDENTIFIER = "se.idega.block.pki";

	private String _pkiServletUrl = "/pki";

	private String _loginHandlerClass = NBSLoginBusinessBean.class.getName();

	/** Names for objects stored in the servlet context or session. */

	private String _errorPageID = null;
	private String _loggedOnPageID = null;

	private static IBPage _page;
	
	private static IBPage _applicationPage;
	private boolean _showErrorCode = false;

	/**
	 * 
	 */
	public NBSLogin() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void main(IWContext iwc) {
		try {
			IWBundle iwb = this.getBundle(iwc);
			IWResourceBundle iwrb = iwb.getResourceBundle(iwc);
			
			String action = iwc.getParameter("nbs_login_action");

//			if(action != null && action.equals("try_again")){
//				NBSLoginBusinessBean.removeNBSException(iwc);
//				NBSLoginBusinessBean.removeException(iwc);
//			}
			
			//this.add(new Text("<br>NBSLogin<br>"));

			boolean showLoginApplet = true;

			if (iwc.isLoggedOn()) {
				//is logged on
				LoggedOnInfo lInfo = LoginBusinessBean.getLoggedOnInfo(iwc);
				if (lInfo.getLoginType() != null && !lInfo.getLoginType().equals(NBSLoginBusinessBean.PKI_LOGIN_TYPE)) {
					showLoginApplet = true;
				} else {
					showLoginApplet = false;
					//forward to another page
					//temp - impliment like in com.idega.block.presentation.Login
					//if (_page != null && !(iwc.isInEditMode() || iwc.isInPreviewMode())) {
					//	iwc.forwardToIBPage(getParentPage(), getGotoPage());
					//}
				}
			} else {
				// is not logged on
				NBSException nbsEX = NBSLoginBusinessBean.getNBSException(iwc);
				Exception ex = NBSLoginBusinessBean.getException(iwc);
				
				if (nbsEX != null ) {
					// hanle Exception
					showLoginApplet = false;
					
					Link tryAgain = new Link(iwrb.getLocalizedString("try_again","Try again"));
					tryAgain.addParameter("nbs_login_action","try_again");
					
					
					String message = iwrb.getLocalizedString("NBSException_code_"+nbsEX.getCode(),"NBSException - Code:"+nbsEX.getCode());
					
					if(_showErrorCode){
						String code = "<br>Code: " + nbsEX.getCode();
						Text tCode = new Text(code);
						tCode.setBold();
						this.add(tCode);
						
					}
					//String message = nbsEX.getMessage();
					this.add(message);
					this.addBreak();
					this.addBreak();
					this.add(tryAgain);
					
					NBSLoginBusinessBean.removeNBSException(iwc);	
				} else if(ex != null){
					showLoginApplet = false;
					
					boolean showApplicationPageLink = false;
					String message = ex.getMessage();
					if(message.startsWith(NBSLoginBusinessBean.IWEX_PKI_USR_NOT_REGISTERED)){
						// not registered user
						showApplicationPageLink = true;
						message = iwrb.getLocalizedString(NBSLoginBusinessBean.IWEX_PKI_USR_NOT_REGISTERED,"User not found");
						
					} else if(message.startsWith(NBSLoginBusinessBean.IWEX_USER_HAS_NO_ACCOUNT)){
						//"PKI login record could not be created: user has no account"
						showApplicationPageLink = true;
						message = iwrb.getLocalizedString(NBSLoginBusinessBean.IWEX_PKI_USR_NOT_REGISTERED,"User has no account");
					}
					
					
					
					
					Link tryAgain = new Link(iwrb.getLocalizedString("try_again","Try again"));
					tryAgain.addParameter("nbs_login_action","try_again");

					
					
					
										
					
					
					this.add(message);
					this.addBreak();
					this.addBreak();
					
					
					if(showApplicationPageLink && _applicationPage!= null){
						Link applicationLink = new Link(iwrb.getLocalizedString("apply_for_account","Apply"));
						applicationLink.setPage(_applicationPage);
						
						this.add(applicationLink);
						this.add(" | ");
					}
					this.add(tryAgain);
										
					NBSLoginBusinessBean.removeException(iwc);
					
				} else {
					showLoginApplet = true;
				}
				

			}

			if (showLoginApplet) {
				System.out.println("Showing applet");
				NBSSigningApplet applet = new NBSSigningApplet(initAuthenticate(iwc));

				//applet.setAction(IWMainApplication.getIWMainApplication(iwc.getServletContext()).getTranslatedURIWithContext(_pkiServletUrl));
				applet.setEventListenerClassName(this._loginHandlerClass);
				add(applet);

				//iwc.setSessionAttribute(INIT_DONE, new Object());			

			}
			//			else {	
			//				//System.out.println("Not showing applet");
			//				//add(new Link("Login Successful"));
			//				//this.getParentPage().setToRedirect(iwc.get);
			//				// TODO(gummi) - Show logout link	
			//				//iwc.removeSessionAttribute(INIT_DONE);	
			//				//processSignContract(iwc);	
			//				
			//			}
			/*
			add(
				"<br>NBSException codes:<br>"
					+ NBSException.ERROR_CLNT_ALG_NOT_SUPP
					+ "<br>"
					+ NBSException.ERROR_CLNT_CANCEL
					+ "<br>"
					+ NBSException.ERROR_CLNT_INTERNAL
					+ "<br>"
					+ NBSException.ERROR_CLNT_MSG_VERSION
					+ "<br>"
					+ NBSException.ERROR_CLNT_NO_CHAIN
					+ "<br>"
					+ NBSException.ERROR_CLNT_NO_KEY
					+ "<br>"
					+ NBSException.ERROR_CLNT_NO_RECEIPT
					+ "<br>"
					+ NBSException.ERROR_CLNT_RECEIPT_NOT_SUPP
					+ "<br>"
					+ NBSException.ERROR_SRV_BAD_TBS
					+ "<br>"
					+ NBSException.ERROR_SRV_BROWSER
					+ "<br>"
					+ NBSException.ERROR_SRV_CERT_EXPIRED
					+ "<br>"
					+ NBSException.ERROR_SRV_CERT_NOT_YET_VALID
					+ "<br>"
					+ NBSException.ERROR_SRV_CERT_POLICY
					+ "<br>"
					+ NBSException.ERROR_SRV_CERT_REVOKED
					+ "<br>"
					+ NBSException.ERROR_SRV_CERT_SIG_INVALID
					+ "<br>"
					+ NBSException.ERROR_SRV_CMS_COMM
					+ "<br>"
					+ NBSException.ERROR_SRV_CMS_CONTINUE
					+ "<br>"
					+ NBSException.ERROR_SRV_CMS_ISSUE
					+ "<br>"
					+ NBSException.ERROR_SRV_CONFIG
					+ "<br>"
					+ NBSException.ERROR_SRVCLNT_INTERNAL
					+ "<br>"
					+ NBSException.ERROR_SRVCLNT_SERVER_UNAVAILABLE
					+ "<br>"
					+ NBSException.ERROR_UNKNOWN
					+ "<br>");
*/
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Sets the login handler business class which this class sends the login/logout event to.<br><br>
	 * This Class must implement com.idega.event.IWEventHandler.<br>
	 * The default is LoginBusiness
	 */
	public void setLoginHandlerClass(String className) {
		this._loginHandlerClass = className;
		/*if (myForm != null)
		{
			myForm.setEventListener(className);
		}*/
	}

	public NBSMessageHttp initAuthenticate(IWContext iwc) throws NBSException, Exception {
		//System.out.println("initAuthenticate()");

		//System.out.println("toBeSigned: " + toBeSigned);	

		NBSServerHttp server = new NBSLoginBusinessBean().getNBSServer(iwc);
		HttpMessage httpReq = new HttpMessage();
		ServletUtil.servletRequestToHttpMessage(iwc.getRequest(), httpReq);

		NBSMessageResult result = (NBSMessageResult)server.doAuthenticate(httpReq);

		if (result != null) {
			// Send the message to the Client.
			return (NBSMessageHttp)result.getMessage();
		}
		return null;
	}

	public void setGotoPage(IBPage page) {
		_page = page;
	}

	public IBPage getGotoPage() {
		return _page;
	}
	
	public void setApplicationPage(IBPage page) {
		_applicationPage = page;
	}

	public IBPage getApplicationPage() {
		return _applicationPage;
	}
	
	public String getBundleIdentifier(){
		return this.IW_BUNDLE_IDENTIFIER;
	}
	


}
