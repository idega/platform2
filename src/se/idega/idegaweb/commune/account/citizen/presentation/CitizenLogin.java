/*
 * Created on 20.5.2004
 */
package se.idega.idegaweb.commune.account.citizen.presentation;

import java.text.MessageFormat;
import java.util.Map;

import se.idega.idegaweb.commune.message.business.MessageBusiness;

import com.idega.block.login.presentation.Login;
import com.idega.block.login.presentation.WelcomeMessage;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.accesscontrol.business.LoginBusinessBean;
import com.idega.core.builder.data.ICPage;
import com.idega.data.IDOException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.PasswordInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;


/**
 * @author laddi
 */
public class CitizenLogin extends Login {
	
	private String iTextStyleName = "TextStyle";
	private String iLabelStyleName = "LabelStyle";
	private String iInputStyleName = "InputStyle";
	private String iMessageStyleName = "MessageStyle";
	
	private ICPage iSettingsPage;
	private ICPage iRegisterPage;
	
	private Image iLoginImage;
	private Image iLogoutImage;
	private Image iTryAgainImage;
	private Image iSettingsImage;
	private Image iRegisterImage;
	
	private String iWidth = Table.HUNDRED_PERCENT;
	private String iInputLength = "16";

	/* (non-Javadoc)
	 * @see com.idega.block.login.presentation.Login#isLoggedOn(com.idega.presentation.IWContext)
	 */
	protected void isLoggedOn(IWContext iwc) throws Exception {
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(iWidth);
		int row = 1;
		
		WelcomeMessage welcomeMessage = new WelcomeMessage();
		welcomeMessage.showUserName(false);
		welcomeMessage.setStyleClass(getStyleName(iMessageStyleName));
		table.mergeCells(1, row, 2, row);
		table.add(welcomeMessage, 1, row++);
		
		Text userName = getStyleText(iwc.getCurrentUser().getName(), iTextStyleName);
		table.mergeCells(1, row, 2, row);
		table.add(userName, 1, row++);
		
		int numberOfMessages = 0;
		try {
			getMessageBusiness(iwc).getNumberOfNewMessages(iwc.getCurrentUser());
		}
		catch (IDOException ie) {
			numberOfMessages = 0;
		}
		String message = "";
		if (numberOfMessages > 0) {
			Object[] arguments = { String.valueOf(numberOfMessages) };
			message = MessageFormat.format(getResourceBundle().getLocalizedString("number_of_messages", "You have {0}Ênew messages"), arguments);
		}
		else {
			message = getResourceBundle().getLocalizedString("no_new_messages", "You have no new messages");
		}
		Text messages = getStyleText(message, iMessageStyleName);
		table.mergeCells(1, row, 2, row);
		table.setCellpaddingTop(1, row, 4);
		table.add(messages, 1, row++);
		
		SubmitButton logoutButton = null;
		if (iLogoutImage != null) {
			logoutButton = new SubmitButton(iLogoutImage);
		}
		else {
			logoutButton = (SubmitButton) getStyleObject(new SubmitButton(getResourceBundle().getLocalizedString("logout_text", "Logout")), iInputStyleName);
		}
		
		GenericButton settingsButton = null;
		if (iSettingsImage != null) {
			settingsButton = new GenericButton();
			settingsButton.setButtonImage(iSettingsImage);
		}
		else {
			settingsButton = (GenericButton) getStyleObject(new GenericButton("settings", getResourceBundle().getLocalizedString("settings", "Settings")), iInputStyleName);
		}
		if (iRegisterPage != null) {
			settingsButton.setPageToOpen(iSettingsPage);
		}
		
		table.setCellpaddingTop(1, row, 16);
		table.setCellpaddingTop(2, row, 16);
		table.setCellpaddingLeft(2, row, 6);
		table.add(logoutButton, 1, row);
		table.add(settingsButton, 2, row);
		
		getMainForm().addParameter(LoginBusinessBean.LoginStateParameter, ACTION_LOG_OFF);
		getMainForm().add(table);
	}
	
	/* (non-Javadoc)
	 * @see com.idega.block.login.presentation.Login#loginFailed(com.idega.presentation.IWContext, java.lang.String)
	 */
	protected void loginFailed(IWContext iwc, String message) {
		Text tryAgain = getStyleText(message, iTextStyleName);
		
		SubmitButton tryAgainButton = null;
		if (iTryAgainImage != null) {
			tryAgainButton = new SubmitButton(iTryAgainImage);
		}
		else {
			tryAgainButton = (SubmitButton) getStyleObject(new SubmitButton(getResourceBundle().getLocalizedString("login_text", "Login")), iInputStyleName);
		}
		
		GenericButton registerButton = null;
		if (iRegisterImage != null) {
			registerButton = new GenericButton();
			registerButton.setButtonImage(iRegisterImage);
		}
		else {
			registerButton = (GenericButton) getStyleObject(new GenericButton("register", getResourceBundle().getLocalizedString("register", "Register")), iInputStyleName);
		}
		if (iRegisterPage != null) {
			registerButton.setPageToOpen(iRegisterPage);
		}
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(iWidth);
		
		table.mergeCells(1, 1, 2, 1);
		table.add(tryAgain, 1, 2);
		
		table.setCellpaddingTop(1, 2, 16);
		table.setCellpaddingTop(2, 2, 16);
		table.setCellpaddingLeft(2, 2, 6);
		table.add(tryAgainButton, 1, 2);
		table.add(registerButton, 2, 2);
		
		getMainForm().addParameter(LoginBusinessBean.LoginStateParameter, ACTION_TRY_AGAIN);
		getMainForm().add(table);
	}
	
	/* (non-Javadoc)
	 * @see com.idega.block.login.presentation.Login#startState(com.idega.presentation.IWContext)
	 */
	protected void startState(IWContext iwc) {
		TextInput login = (TextInput) getStyleObject(new TextInput(LOGIN_PARAMETER_NAME), iInputStyleName);
		login.setWidth(iInputLength);
		if (isEnterSubmit()) {
			login.setOnKeyPress("return enterSubmit(this,event)");
		}
		
		PasswordInput password = (PasswordInput) getStyleObject(new PasswordInput(PASSWORD_PARAMETER_NAME), iInputStyleName);
		password.setWidth(iInputLength);
		if (isEnterSubmit()) {
			password.setOnKeyPress("return enterSubmit(this,event)");
		}
		
		Text loginLabel = getStyleText(getResourceBundle().getLocalizedString("user", "User"), iLabelStyleName);
		Text passwordLabel = getStyleText(getResourceBundle().getLocalizedString("password", "Password"), iLabelStyleName);
		
		SubmitButton loginButton = null;
		if (iLoginImage != null) {
			loginButton = new SubmitButton(iLoginImage);
		}
		else {
			loginButton = (SubmitButton) getStyleObject(new SubmitButton(getResourceBundle().getLocalizedString("login_text", "Login")), iInputStyleName);
		}
		
		GenericButton registerButton = null;
		if (iRegisterImage != null) {
			registerButton = new GenericButton();
			registerButton.setButtonImage(iRegisterImage);
		}
		else {
			registerButton = (GenericButton) getStyleObject(new GenericButton("register", getResourceBundle().getLocalizedString("register", "Register")), iInputStyleName);
		}
		if (iRegisterPage != null) {
			registerButton.setPageToOpen(iRegisterPage);
		}
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(iWidth);
		
		table.setCellpaddingLeft(2, 1, 6);
		table.add(loginLabel, 1, 1);
		table.add(passwordLabel, 2, 1);
		
		table.setCellpaddingTop(1, 2, 3);
		table.setCellpaddingTop(2, 2, 3);
		table.setCellpaddingLeft(2, 2, 6);
		table.add(login, 1, 2);
		table.add(password, 2, 2);
		
		table.setCellpaddingTop(1, 3, 8);
		table.setCellpaddingTop(2, 3, 8);
		table.setCellpaddingLeft(2, 3, 6);
		table.add(loginButton, 1, 3);
		table.add(registerButton, 2, 3);
		
		getMainForm().addParameter(LoginBusinessBean.LoginStateParameter, ACTION_LOG_IN);
		getMainForm().add(table);
	}
	
	/* (non-Javadoc)
	 * @see com.idega.presentation.Block#getStyleNames()
	 */
	public Map getStyleNames() {
		Map map = super.getStyleNames();
		map.put(iTextStyleName, "");
		map.put(iLabelStyleName, "");
		map.put(iInputStyleName, "");
		map.put(iMessageStyleName, "");
		
		return map;
	}
	
	private MessageBusiness getMessageBusiness(IWApplicationContext iwac) {
		try {
			return (MessageBusiness) IBOLookup.getServiceInstance(iwac, MessageBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}
	
	/**
	 * @param registerPage The registerPage to set.
	 */
	public void setRegisterPage(ICPage registerPage) {
		this.iRegisterPage = registerPage;
	}
	
	/**
	 * @param settingsPage The settingsPage to set.
	 */
	public void setSettingsPage(ICPage settingsPage) {
		this.iSettingsPage = settingsPage;
	}
	
	/**
	 * @param loginImage The loginImage to set.
	 */
	public void setLoginImage(Image loginImage) {
		this.iLoginImage = loginImage;
	}
	
	/**
	 * @param logoutImage The logoutImage to set.
	 */
	public void setLogoutImage(Image logoutImage) {
		this.iLogoutImage = logoutImage;
	}
	
	/**
	 * @param registerImage The registerImage to set.
	 */
	public void setRegisterImage(Image registerImage) {
		this.iRegisterImage = registerImage;
	}
	
	/**
	 * @param settingsImage The settingsImage to set.
	 */
	public void setSettingsImage(Image settingsImage) {
		this.iSettingsImage = settingsImage;
	}
	
	/**
	 * @param tryAgainImage The tryAgainImage to set.
	 */
	public void setTryAgainImage(Image tryAgainImage) {
		this.iTryAgainImage = tryAgainImage;
	}
	
	/**
	 * @param width The width to set.
	 */
	public void setWidth(String width) {
		this.iWidth = width;
	}
	
	public void setInputLength(String length) {
		this.iInputLength = length;
	}
}