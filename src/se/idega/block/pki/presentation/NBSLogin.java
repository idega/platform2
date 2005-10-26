/*
 * Created on 22.5.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.block.pki.presentation;

import se.idega.block.pki.business.NBSLoggedOnInfo;
import se.idega.block.pki.business.NBSLoginBusinessBean;
import se.nexus.nbs.sdk.HttpMessage;
import se.nexus.nbs.sdk.NBSException;
import se.nexus.nbs.sdk.NBSMessageHttp;
import se.nexus.nbs.sdk.NBSMessageResult;
import se.nexus.nbs.sdk.NBSServerHttp;
import se.nexus.nbs.sdk.servlet.ServletUtil;
import com.idega.block.login.presentation.Login;
import com.idega.core.accesscontrol.business.LoggedOnInfo;
import com.idega.core.accesscontrol.business.LoginBusinessBean;
import com.idega.core.builder.data.ICPage;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Parameter;
import com.idega.servlet.filter.IWAuthenticator;

/**
 * @author <a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class NBSLogin extends Login {

	

	private boolean _forwardToApplicationPage = true;

	private final static String IW_BUNDLE_IDENTIFIER = "se.idega.block.pki";
	
	public final static String PRM_PERSONAL_ID = "nbs_personal_id";
	public final static String PRM_GIVEN_NAME = "nbs_given_name";
	public final static String PRM_SURNAME = "nbs_surname";
	
	private String _loginHandlerClass = NBSLoginBusinessBean.class.getName();

	/** Names for objects stored in the servlet context or session. */

	//private String _errorPageID = null;
	//private String _loggedOnPageID = null;

	private static ICPage _page;
	
	private static ICPage _applicationPage;
	private boolean _showErrorCode = false;
	
	
	
	private String[] _colorPanel=null;
	private String[] _colorButton=null;
	private String[] _colorField=null;
	private String[] _colorMessage=null;
	private String[] _colorUser=null;
	
	private String _appletHeight=null;
	private String _appletWidth=null;

	/**
	 * 
	 */
	public NBSLogin() {
		super();
		
//		setPanelColor("#052F59","#386CB7");
//		setButtonColor("#052F59","#386CB7");
//		setFieldColor("#052F59","#386CB7");
//		setMessageColor("#052F59","#386CB7");
//		setUserColor("#052F59","#386CB7");
				
//		setPanelColor("#052F59","#6EA9FF");
//		setButtonColor("#052F59","#6699CC");
//		setFieldColor("#052F59","#FFFFFF");
//		setMessageColor("#052F59","#FFFFFF");
//		setUserColor("#052F59","#6EA9FF");
		
		
		//Default values
//		setPanelColor("#000000","#FFFFFF");
//		setButtonColor("#052F59","#6EA9FF");
//		setFieldColor("#000000","#FFFFFF");
//		setMessageColor("#000000","#FFFFFF");
//		setUserColor("#000000","#FFFFFF");
//		setAppletHeight("100");
//		setAppletWidth("350");
	}

	public void main(IWContext iwc) {
		try {
			IWBundle iwb = this.getBundle(iwc);
			IWResourceBundle iwrb = iwb.getResourceBundle(iwc);
			
			
//			String action = iwc.getParameter("nbs_login_action");
//			if(action != null && action.equals("try_again")){
//				(new NBSLoginBusinessBean()).logOutBankID(iwc);
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
				String errorMessage = iwc.getParameter(NBSSigningApplet.PARM_ERROR_MESSAGE);
				NBSLoginBusinessBean nbsloginbean = NBSLoginBusinessBean.createNBSLoginBusiness();
				NBSLoggedOnInfo info = nbsloginbean.getBankIDLoggedOnInfo(iwc);
				String givenName=null;
				String surName=null;
				if(info!=null){
					givenName= info.getGivenName();
					surName = info.getSurName();
				}
				if (nbsEX != null ) {
					// handle Exception
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
					String personalID="";
					
					boolean showApplicationPageLink = false;
					String message = ex.getMessage();
					if(message.startsWith(NBSLoginBusinessBean.IWEX_PKI_USR_NOT_REGISTERED)){
						// not registered user
						showApplicationPageLink = true;
						int start = message.indexOf('#')+1;
						int end = message.indexOf('#',start);
						if(start != -1 && end != -1){
							personalID = message.substring(start,end);
						}
						message = iwrb.getLocalizedString(NBSLoginBusinessBean.IWEX_PKI_USR_NOT_REGISTERED,"User not found");
						
					} else if(message.startsWith(NBSLoginBusinessBean.IWEX_USER_HAS_NO_ACCOUNT)){
						//"PKI login record could not be created: user has no account"
						showApplicationPageLink = true;
						
						int start = message.indexOf('#')+1;
						int end = message.indexOf('#',start);
						if(start != -1 && end != -1){
							personalID = message.substring(start,end);
						}
						//if(_forwardToApplicationPage && _applicationPage != null){
						//	iwc.forwardToIBPage(this.getParentPage(),_applicationPage);
						//}
						message = iwrb.getLocalizedString(NBSLoginBusinessBean.IWEX_USER_HAS_NO_ACCOUNT,"User has no account");
					}
					
					Link tryAgain = new Link(iwrb.getLocalizedString("try_again","Try again"));
					tryAgain.addParameter("nbs_login_action","try_again");

					
					this.add(message);
					this.addBreak();
					this.addBreak();
					
					
					if(showApplicationPageLink && _applicationPage!= null){
						Link applicationLink = new Link(iwrb.getLocalizedString("apply_for_account","Apply"));
						applicationLink.setPage(_applicationPage);
						applicationLink.setParameter(PRM_PERSONAL_ID,personalID);
						
						this.add(applicationLink);
						this.add(" | ");
						
						
						//
						//if(message.startsWith(NBSLoginBusinessBean.IWEX_USER_HAS_NO_ACCOUNT)){
							//iwc.forwardToIBPage(this.getParentPage(),_applicationPage);
							String pageUri = getBuilderService(iwc).getPageURI(getApplicationPage());
							if(pageUri.indexOf("?")==-1){
								pageUri+="?";
							}
							else{
								pageUri+="&";
							}
							pageUri+=PRM_PERSONAL_ID;
							pageUri+="=";
							pageUri+=personalID;
							pageUri+="&";
							pageUri+=PRM_GIVEN_NAME;
							pageUri+="=";
							pageUri+=givenName;
							pageUri+="&";
							pageUri+=PRM_SURNAME;
							pageUri+="=";
							pageUri+=surName;
							getParentPage().setToRedirect(pageUri);
						//}
					}
					this.add(tryAgain);
										
					NBSLoginBusinessBean.removeException(iwc);
					
				} else if(errorMessage!= null){
					
					showLoginApplet = false;
					
					Link tryAgain = new Link(iwrb.getLocalizedString("try_again","Try again"));
					tryAgain.addParameter("nbs_login_action","try_again");


					this.add(errorMessage);
					this.addBreak();
					this.addBreak();
					this.add(tryAgain);
					
				} else {
					showLoginApplet = true;
				}
				

			}

			if (showLoginApplet) {
				//System.out.println("Showing applet");
				NBSSigningApplet applet = new NBSSigningApplet(initAuthenticate(iwc));

				//applet.setAction(IWMainApplication.getIWMainApplication(iwc.getServletContext()).getTranslatedURIWithContext(_pkiServletUrl));
				applet.setEventListenerClassName(this._loginHandlerClass);
				applet.addParameter(new Parameter(LoginBusinessBean.LoginStateParameter, "login"));
				if(sendUserToHomePage){
					applet.addParameter(new Parameter(IWAuthenticator.PARAMETER_REDIRECT_USER_TO_PRIMARY_GROUP_HOME_PAGE,"redirect"));
				}
				

				if(_colorPanel!=null){
					applet.setPanelColor(_colorPanel[0],_colorPanel[1]);
				}
				if(_colorButton!=null){
					applet.setButtonColor(_colorButton[0],_colorButton[1]);
				}
				if(_colorField!=null){
					applet.setFieldColor(_colorField[0],_colorField[1]);
				}
				if(_colorMessage!=null){
					applet.setMessageColor(_colorMessage[0],_colorMessage[1]);
				}
				if(_colorUser!=null){
					applet.setUserColor(_colorUser[0],_colorUser[1]);
				}
				
				if(_appletWidth!=null){
					applet.setAppletWidth(_appletWidth);
				}
								
				if(_appletHeight!=null){
					applet.setHeight(_appletHeight);
				}
				
				
				
				
				
				applet.setErrorPageID(iwc.getCurrentIBPageID());
				
				
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
					+ "NBSException.ERROR_CLNT_ALG_NOT_SUPP : "	
					+ NBSException.ERROR_CLNT_ALG_NOT_SUPP
					+ "<br>"
			+ "NBSException.ERROR_CLNT_CANCEL : "
					+ NBSException.ERROR_CLNT_CANCEL
					+ "<br>"
			+ "NBSException.ERROR_CLNT_INTERNAL : "
					+ NBSException.ERROR_CLNT_INTERNAL
					+ "<br>"
			+ "NBSException.ERROR_CLNT_MSG_VERSION : "
					+ NBSException.ERROR_CLNT_MSG_VERSION
					+ "<br>"
			+ "NBSException.ERROR_CLNT_NO_CHAIN : "
					+ NBSException.ERROR_CLNT_NO_CHAIN
					+ "<br>"
			+ "NBSException.ERROR_CLNT_NO_KEY : "
					+ NBSException.ERROR_CLNT_NO_KEY
					+ "<br>"
			+ "NBSException.ERROR_CLNT_NO_RECEIPT : "
					+ NBSException.ERROR_CLNT_NO_RECEIPT
					+ "<br>"
			+ "NBSException.ERROR_CLNT_RECEIPT_NOT_SUPP : "
					+ NBSException.ERROR_CLNT_RECEIPT_NOT_SUPP
					+ "<br>"
			+ "NBSException.ERROR_SRV_BAD_TBS : "
					+ NBSException.ERROR_SRV_BAD_TBS
					+ "<br>"
			+ "NBSException.ERROR_SRV_BROWSER : "
					+ NBSException.ERROR_SRV_BROWSER
					+ "<br>"
			+ "NBSException.ERROR_SRV_CERT_EXPIRED : "
					+ NBSException.ERROR_SRV_CERT_EXPIRED
					+ "<br>"
			+ "NBSException.ERROR_SRV_CERT_NOT_YET_VALID : "
					+ NBSException.ERROR_SRV_CERT_NOT_YET_VALID
					+ "<br>"
			+ "NBSException.ERROR_SRV_CERT_POLICY : "
					+ NBSException.ERROR_SRV_CERT_POLICY
					+ "<br>"
			+ "NBSException.ERROR_SRV_CERT_REVOKED : "
					+ NBSException.ERROR_SRV_CERT_REVOKED
					+ "<br>"
			+ "NBSException.ERROR_SRV_CERT_SIG_INVALID : "
					+ NBSException.ERROR_SRV_CERT_SIG_INVALID
					+ "<br>"
			+ "NBSException.ERROR_SRV_CMS_COMM : "
					+ NBSException.ERROR_SRV_CMS_COMM
					+ "<br>"
			+ "NBSException.ERROR_SRV_CMS_CONTINUE - "
					+ NBSException.ERROR_SRV_CMS_CONTINUE
					+ "<br>"
			+ "NBSException.ERROR_SRV_CMS_ISSUE : "
					+ NBSException.ERROR_SRV_CMS_ISSUE
					+ "<br>"
			+ "NBSException.ERROR_SRV_CONFIG : "
					+ NBSException.ERROR_SRV_CONFIG
					+ "<br>"
			+ "NBSException.ERROR_SRVCLNT_INTERNAL : "
					+ NBSException.ERROR_SRVCLNT_INTERNAL
					+ "<br>"
			+ "NBSException.ERROR_SRVCLNT_SERVER_UNAVAILABLE : "
					+ NBSException.ERROR_SRVCLNT_SERVER_UNAVAILABLE
					+ "<br>"
			+ "NBSException.ERROR_UNKNOWN : "
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

	public void setGotoPage(ICPage page) {
		_page = page;
	}

	public ICPage getGotoPage() {
		return _page;
	}
	
	public void setApplicationPage(ICPage page) {
		_applicationPage = page;
	}

	public ICPage getApplicationPage() {
		return _applicationPage;
	}
	
	public String getBundleIdentifier(){
		return IW_BUNDLE_IDENTIFIER;
	}
	



	public void setButtonColor(String color1, String color2) {
		String colors[] = new String[2];
		colors[0]=color1;
		colors[1]=color2;
		_colorButton = colors;
	}


	public void setFieldColor(String color1, String color2) {
		String colors[] = new String[2];
		colors[0]=color1;
		colors[1]=color2;
		_colorField = colors;
	}


	public void setMessageColor(String color1, String color2) {
		String colors[] = new String[2];
		colors[0]=color1;
		colors[1]=color2;
		_colorMessage = colors;
	}


	public void setPanelColor(String color1, String color2) {
		String colors[] = new String[2];
		colors[0]=color1;
		colors[1]=color2;
		_colorPanel = colors;
	}


	public void setUserColor(String color1, String color2) {
		String colors[] = new String[2];
		colors[0]=color1;
		colors[1]=color2;
		_colorUser = colors;
	}


	public void setAppletHeight(String height) {
		_appletHeight = height;
	}

	public void setAppletWidth(String width) {
		_appletWidth = width;
	}
	
	public void forwardToApplicationPage(boolean value){
		_forwardToApplicationPage = value;
	}

}
