// idega 2000 Grimur Jonsson - Tryggvi Larusson
/*
 * Copyright 2000-2001 idega.is All Rights Reserved.
 */

package is.idega.idegaweb.golf.block.login.presentation;

import is.idega.idegaweb.golf.block.login.business.GolfLoginBusiness;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.presentation.GolfBlock;

import java.rmi.RemoteException;

import com.idega.core.builder.business.BuilderService;
import com.idega.core.builder.business.BuilderServiceFactory;
import com.idega.core.builder.data.ICPage;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWConstants;
import com.idega.presentation.IWContext;
import com.idega.presentation.Script;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Paragraph;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.Parameter;
import com.idega.presentation.ui.PasswordInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

/**
 * Title: Login Description: Copyright: Copyright (c) 2000-2001 idega.is All
 * Rights Reserved Company: idega
 * 
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson </a>, <a
 *         href="mailto:tryggvi@idega.is">Tryggvi Larusson </a>
 * @version 1.1
 */
public class GolfLogin extends GolfBlock {

	private Boolean _showFormWhenLoggedOn;
	private boolean _enterSubmit = false;
	private int _inputLength = 8;
	private int _indent = 8;
	private int _logOnPage = -1;
	private boolean lockedAsWapLayout = false;
	private static final String PRM_DISABLE_TIMER_ON_LOGGED_ON_PAGE = "nooptimer";

	public GolfLogin() {
		super();

	}


	private void startState(IWContext iwc) {
		if (showForm(false)) {
			String userText = getResourceBundle().getLocalizedString("user", "User");
			String passwordText = getResourceBundle().getLocalizedString("password", "Password");
	
			Form myForm = new Form();
			myForm.setEventListener(GolfLoginBusiness.class);
	
			myForm.setMethod("post");
			myForm.maintainAllParameters();
	
			Table myTable = new Table(5,1);
			myTable.setCellpadding(0);
			myTable.setCellspacing(0);
			myTable.setCellpaddingRight(1, 1, 5);
			myTable.setCellpaddingRight(2, 1, 10);
			myTable.setCellpaddingRight(3, 1, 5);
			myTable.setCellpaddingRight(4, 1, 10);
			myTable.setCellpaddingRight(5, 1, 5);
	
			Text loginTexti = getStyleText(userText, STYLENAME_TEMPLATE_HEADER2);
			Text passwordTexti = getStyleText(passwordText, STYLENAME_TEMPLATE_HEADER2);
	
			myTable.add(loginTexti, 1, 1);
	
			TextInput login = (TextInput) getStyledSmallInterface(new TextInput("login"));
			login.setSize(_inputLength);
			if (_enterSubmit) {
				login.setOnKeyPress("return enterSubmit(this,event)");
			}
			myTable.add(login, 2, 1);
	
			myTable.add(passwordTexti, 3, 1);
	
			PasswordInput passw = (PasswordInput) getStyledSmallInterface(new PasswordInput("password"));
			passw.setSize(_inputLength);
			if (_enterSubmit) {
				passw.setOnKeyPress("return enterSubmit(this,event)");
			}
			myTable.add(passw, 4, 1);
	
			Link loginLink = getStyleLink(localize("login.login","Login"), this.STYLENAME_TEMPLATE_HEADER_LINK2);
			loginLink.setToFormSubmit(myForm);
			myTable.add(loginLink, 5, 1);
			
			myTable.add(new Parameter(GolfLoginBusiness.LoginStateParameter, "login"));
			myForm.add(myTable);
			add(myForm);
		}
	}
	
	protected void startStateWML(IWContext iwc) {

		
		String userText = getResourceBundle().getLocalizedString("user", "User");
		String passwordText = getResourceBundle().getLocalizedString("password", "Password");

		Form myForm = new Form();
		myForm.setEventListener(GolfLoginBusiness.class);

		myForm.setMethod("post");
		myForm.maintainAllParameters();

		Table myTable = new Table();
		int row = 1;

		TextInput login = (TextInput) getStyledSmallInterface(new TextInput("login"));
		login.setSize(_inputLength);
		if (_enterSubmit) {
			login.setOnKeyPress("return enterSubmit(this,event)");
		}

		PasswordInput passw = (PasswordInput) getStyledSmallInterface(new PasswordInput("password"));
		passw.setSize(_inputLength);
		if (_enterSubmit) {
			passw.setOnKeyPress("return enterSubmit(this,event)");
		}

		
		Label loginTexti = new Label(userText, login);
		Label passwordTexti = new Label(passwordText, passw);

		SubmitButton loginButton = new SubmitButton(localize("login.login","Login"));
		
		myTable.add(loginTexti,1,row++);
		myTable.add(login,1,row++);
		myTable.add(passwordTexti,1,row++);
		myTable.add(passw,1,row++);
		myTable.add(loginButton,1,row++);
		
		myTable.add(new Parameter(GolfLoginBusiness.LoginStateParameter, "login"));
		myForm.add(myTable);
		add(myForm);

//		
//		if (_logOnPage > 0) {
//			getMainForm().setPageToSubmitTo(_logOnPage);
//		}
//
//		Table myTable = new Table(1,5);
//		
//		TextInput login = new TextInput(LOGIN_PARAMETER_NAME);
//		login.setMarkupAttribute("style", styleAttribute);
//		login.setSize(inputLength);
//		
//		PasswordInput passw = new PasswordInput(PASSWORD_PARAMETER_NAME);
//		passw.setMarkupAttribute("style", styleAttribute);
//		passw.setSize(inputLength);
//		
//		Label loginTexti = new Label(userText,login);
//		Label passwordTexti = new Label(passwordText,passw);
//		
//		SubmitButton button = new SubmitButton(iwrb.getLocalizedString("login_text", "login"), "tengja");
//
//		int row = 1;
//		myTable.add(loginTexti,1,row++);
//		myTable.add(login,1,row++);
//		myTable.add(passwordTexti,1,row++);
//		myTable.add(passw,1,row++);
//		
//		myTable.add(new Parameter(LoginBusinessBean.LoginStateParameter, ACTION_LOG_IN));
//		myTable.add(button,1,row++);
//		
//		getMainForm().add(myTable);
	}

	
	private boolean showForm(boolean loggedOn) {
		if (_showFormWhenLoggedOn != null) {
			if (loggedOn && _showFormWhenLoggedOn.booleanValue()) {
				return true;
			}
			else if (!loggedOn && !_showFormWhenLoggedOn.booleanValue()) {
				return true;
			}
			return false;
		}
		return true;
	}

	public void main(IWContext modinfo) throws Exception {

		if (getParentPage() != null) {
			Script script = null;
			if (getParentPage().getAssociatedScript() != null)
				script = getParentPage().getAssociatedScript();
			else {
				script = new Script();
				getParentPage().setAssociatedScript(script);
			}
			script.addFunction("enterSubmit", "function enterSubmit(myfield,e) { var keycode; if (window.event) keycode = window.event.keyCode; else if (e) keycode = e.which; else return true; if (keycode == 13) { myfield.form.submit(); return false; } else return true; }");
			_enterSubmit = true;
		}

		String state = internalGetState(modinfo);
		if (state != null) {
			if (state.equals("loggedon")) {
				if(lockedAsWapLayout || IWConstants.MARKUP_LANGUAGE_WML.equals(modinfo.getMarkupLanguage())) {
					isLoggedOnWML(modinfo);
				} else {
					isLoggedOn(modinfo);
				}
			} else if (state.equals("loggedoff")) {
				if(lockedAsWapLayout || IWConstants.MARKUP_LANGUAGE_WML.equals(modinfo.getMarkupLanguage())) {
					startStateWML(modinfo);
				} else {
					startState(modinfo);
				}
			} else if (state.equals("newlogin")) {
				String temp = modinfo.getParameter("login");
				if (temp != null) {
					if (temp.length() == 11) {
						if(lockedAsWapLayout || IWConstants.MARKUP_LANGUAGE_WML.equals(modinfo.getMarkupLanguage())) {
							loginFailedWML("toBig");
						} else {
							loginFailed("toBig");
						}
					} else if (temp.equals("") || temp.equals(" ")) {
						if(lockedAsWapLayout || IWConstants.MARKUP_LANGUAGE_WML.equals(modinfo.getMarkupLanguage())) {
							loginFailedWML("empty");
						} else {
							loginFailed("empty");
						}
					} else {
						if(lockedAsWapLayout || IWConstants.MARKUP_LANGUAGE_WML.equals(modinfo.getMarkupLanguage())) {
							loginFailedWML("");
						} else {
							loginFailed("");
						}
					}
				} else {
					if(lockedAsWapLayout || IWConstants.MARKUP_LANGUAGE_WML.equals(modinfo.getMarkupLanguage())) {
						loginFailedWML("");
					} else {
						loginFailed("");
					}
				}
			} else if (state.equals("loginfailed")) {
				if(lockedAsWapLayout || IWConstants.MARKUP_LANGUAGE_WML.equals(modinfo.getMarkupLanguage())) {
					loginFailedWML("");
				} else {
					loginFailed("");
				}
			} else {
				if(lockedAsWapLayout || IWConstants.MARKUP_LANGUAGE_WML.equals(modinfo.getMarkupLanguage())) {
					startStateWML(modinfo);
				} else {
					startState(modinfo);
				}
			}

		} else {
			if(lockedAsWapLayout || IWConstants.MARKUP_LANGUAGE_WML.equals(modinfo.getMarkupLanguage())) {
				startStateWML(modinfo);
			} else {
				startState(modinfo);
			}
		}
	}

	public String internalGetState(IWContext modinfo) {
		return GolfLoginBusiness.internalGetStateString(modinfo);
	}

	private void isLoggedOn(IWContext modinfo) {
		if (showForm(true)) {
			Form myForm = new Form();
			myForm.setEventListener(GolfLoginBusiness.class);
	
			myForm.setMethod("post");
			myForm.maintainAllParameters();
	
			Table myTable = new Table();
			myTable.setCellspacing(0);
			myTable.setCellpadding(0);
			myTable.setCellpaddingRight(1, 1, 5);
			myTable.setCellpaddingRight(2, 1, 5);
			myTable.setCellpaddingRight(3, 1, 5);
	
			Member member = (Member) modinfo.getSession().getAttribute("member_login");
			Text userName = getStyleText(member.getName(), this.STYLENAME_TEMPLATE_HEADER2);
			myTable.add(userName, 1, 1);
			myTable.setNoWrap(1, 1);
	
			Text spacer = getStyleText("|", this.STYLENAME_TEMPLATE_HEADER2);
			myTable.add(spacer, 2, 1);
			
			Link logout = getStyleLink(localize("login.logout","Log out"), this.STYLENAME_TEMPLATE_HEADER_LINK2);
			logout.setToFormSubmit(myForm);
			myTable.add(logout, 3, 1);
	
			/*GenericButton button = getSubmitButton();
			button.setContent(localize("login.logout","Log out"));
			myTable.add(button, 2, 1);*/
			myTable.add(new Parameter(GolfLoginBusiness.LoginStateParameter, "logoff"));
	
			myForm.add(myTable);
			add(myForm);
		}
	}

	private void isLoggedOnWML(IWContext modinfo) {
			Form myForm = new Form();
			myForm.setEventListener(GolfLoginBusiness.class);
	
			myForm.setMethod("post");
			myForm.maintainAllParameters();
	
			Table myTable = new Table();
	
			Member member = (Member) modinfo.getSession().getAttribute("member_login");
			Text userName = new Text(member.getName());

			

			SubmitButton logout = new SubmitButton(localize("login.logout","Log out"));
			
			int row = 1;
			
			Paragraph p = new Paragraph();
			p.add(userName);
			myTable.add(p, 1, row++);
			
			if(_logOnPage>0){
				Link go = new Link(getResourceBundle().getLocalizedString("login.forward","forward >"));
				go.setPage(_logOnPage);
				Paragraph p2 = new Paragraph();
				p2.add(go);
				myTable.add(p2, 1, row++);
				try {
					if(modinfo.getParameter(PRM_DISABLE_TIMER_ON_LOGGED_ON_PAGE)==null){ 
						StringBuffer url = new StringBuffer();
						url.append(getBuilderService(modinfo).getPageURI(_logOnPage));
						if(modinfo.getParameter(IWConstants.PARAM_NAME_OUTPUT_MARKUP_LANGUAGE)!=null){
							if(IWConstants.MARKUP_LANGUAGE_WML.equals(modinfo.getMarkupLanguage())){
								url.append("&amp;");
							} else {
								url.append("&");
							}
							url.append(IWConstants.PARAM_NAME_OUTPUT_MARKUP_LANGUAGE).append("=").append(modinfo.getParameter(IWConstants.PARAM_NAME_OUTPUT_MARKUP_LANGUAGE));
						}
						String redirectURL = url.toString();
						if(IWConstants.MARKUP_LANGUAGE_WML.equals(modinfo.getMarkupLanguage())){
							redirectURL = modinfo.getResponse().encodeURL(redirectURL);
						}
//						System.out.println("Golflogin redirect url: "+redirectURL);
						this.getParentPage().setToRedirect(redirectURL,3);
					}
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
			
			myForm.add(logout);

			myForm.add(new Parameter(GolfLoginBusiness.LoginStateParameter, "logoff"));
			myTable.add(myForm, 1,row++);
			add(myTable);
	}


	private void loginFailed(String what) {
		if (showForm(false)) {
			String userText = getResourceBundle().getLocalizedString("user", "User");
			String passwordText = getResourceBundle().getLocalizedString("password", "Password");
	
			Form myForm = new Form();
			myForm.setEventListener(GolfLoginBusiness.class);
	
			myForm.setMethod("post");
			myForm.maintainAllParameters();
	
			Table myTable = new Table(3, 1);
			myTable.setWidth(Table.HUNDRED_PERCENT);
			myTable.setCellspacing(0);
			myTable.setCellpadding(0);
			myTable.setCellpaddingRight(1, 1, 5);
			myTable.setCellpaddingRight(2, 1, 5);
			myTable.setCellpaddingRight(3, 1, 5);
			
			Text failed = getStyleText(getResourceBundle().getLocalizedString("loginfailed", "Login failed"), this.STYLENAME_TEMPLATE_HEADER2);
			//failed.setFontSize(1);
			if (what.equals("empty")) {
				failed.setText(getResourceBundle().getLocalizedString("id_needed", "Identification needed"));
			} else if (what.equals("toBig")) {
				failed.setText(getResourceBundle().getLocalizedString("wrong_format", "Wrong format"));
			}
			myTable.add(failed,1,1);
			myTable.setAlignment(1, 1, "center");
			
			Text spacer = getStyleText("|", this.STYLENAME_TEMPLATE_HEADER2);
			myTable.add(spacer, 2, 1);
			
			Link tryAgain = getStyleLink(localize("login.try_again","Try again"), this.STYLENAME_TEMPLATE_LINK3);
			tryAgain.setToFormSubmit(myForm);
			myTable.add(tryAgain, 3, 1);
	
			/*GenericButton tryAgain = getSaveButton();
			tryAgain.setContent(localize("login.try_again","Try again"));
			myTable.add(tryAgain, 3, 1);*/
			myTable.add(new Parameter(GolfLoginBusiness.LoginStateParameter, "tryagain"));
	
			myForm.add(myTable);
			add(myForm);
		}
	}
	
	private void loginFailedWML(String what) {
		
		String userText = getResourceBundle().getLocalizedString("user", "User");
		String passwordText = getResourceBundle().getLocalizedString("password", "Password");

		Form myForm = new Form();
		myForm.setEventListener(GolfLoginBusiness.class);

		myForm.setMethod("post");
		myForm.maintainAllParameters();

		Table myTable = new Table();
		int row = 1;


		Text failed = new Text(getResourceBundle().getLocalizedString("loginfailed", "Login failed"));
		//failed.setFontSize(1);
		if (what.equals("empty")) {
			failed.setText(getResourceBundle().getLocalizedString("id_needed", "Identification needed"));
		} else if (what.equals("toBig")) {
			failed.setText(getResourceBundle().getLocalizedString("wrong_format", "Wrong format"));
		}

		
		SubmitButton loginButton = new SubmitButton(localize("login.try_again","Try again"));
		
		Paragraph p = new Paragraph();
		p.add(failed);
		myTable.add(p,1,row++);
		myTable.add(loginButton,1,row++);
		
		myTable.add(new Parameter(GolfLoginBusiness.LoginStateParameter, "tryagain"));
		myForm.add(myTable);
		add(myForm);

		
		
//		String userText = getResourceBundle().getLocalizedString("user", "User");
//		String passwordText = getResourceBundle().getLocalizedString("password", "Password");
//
//		Form myForm = new Form();
//		myForm.setEventListener(GolfLoginBusiness.class);
//
//		myForm.setMethod("post");
//		myForm.maintainAllParameters();
//
//		Table myTable = new Table(3, 1);
//		myTable.setWidth(Table.HUNDRED_PERCENT);
//		myTable.setCellspacing(0);
//		myTable.setCellpadding(0);
//		myTable.setCellpaddingRight(1, 1, 5);
//		myTable.setCellpaddingRight(2, 1, 5);
//		myTable.setCellpaddingRight(3, 1, 5);
//		
//		Text failed = getStyleText(getResourceBundle().getLocalizedString("loginfailed", "Login failed"), this.STYLENAME_TEMPLATE_HEADER2);
//		//failed.setFontSize(1);
//		if (what.equals("empty")) {
//			failed.setText(getResourceBundle().getLocalizedString("id_needed", "Identification needed"));
//		} else if (what.equals("toBig")) {
//			failed.setText(getResourceBundle().getLocalizedString("wrong_format", "Wrong format"));
//		}
//		myTable.add(failed,1,1);
//		myTable.setAlignment(1, 1, "center");
//		
//		Text spacer = getStyleText("|", this.STYLENAME_TEMPLATE_HEADER2);
//		myTable.add(spacer, 2, 1);
//		
//		Link tryAgain = getStyleLink(localize("login.try_again","Try again"), this.STYLENAME_TEMPLATE_LINK3);
//		tryAgain.setToFormSubmit(myForm);
//		myTable.add(tryAgain, 3, 1);
//
//		/*GenericButton tryAgain = getSaveButton();
//		tryAgain.setContent(localize("login.try_again","Try again"));
//		myTable.add(tryAgain, 3, 1);*/
//		myTable.add(new Parameter(GolfLoginBusiness.LoginStateParameter, "tryagain"));
//
//		myForm.add(myTable);
//		add(myForm);
		
		
	}


	///// additional mothods /////////

	private void logOut(IWContext modinfo) throws Exception {
		GolfLoginBusiness.logOut2(modinfo);
	}

	/**
	 * @param formWhenLoggedOn The _showFormWhenLoggedOn to set.
	 */
	public void setShowFormWhenLoggedOn(boolean formWhenLoggedOn) {
		this._showFormWhenLoggedOn = new Boolean(formWhenLoggedOn);
	}
	
	/**
	 * @param length The _inputLength to set.
	 */
	public void setInputLength(int length) {
		this._inputLength = length;
	}
	
	/**
	 * @param _indent The _indent to set.
	 */
	public void setIndent(int indent) {
		this._indent = indent;
	}
	
	/**
	 * @param lockedAsWapLayout The lockedAsWapLayout to set.
	 */
	public void setLockedAsWapLayout(boolean lockedAsWapLayout) {
		this.lockedAsWapLayout = lockedAsWapLayout;
	}
	
	public void setLogOnPage(ICPage page) {
		_logOnPage = page.getID();
	}

	public void setLogOnPage(int page) {
		_logOnPage = page;
	}
	
	/**
	 * Convenience method to return the instance of BuilderService
	 * 
	 * @param iwc
	 * @return @throws
	 *         RemoteException
	 */
	protected BuilderService getBuilderService(IWApplicationContext iwc) throws RemoteException
	{
		return BuilderServiceFactory.getBuilderService(iwc);
	}
}