// idega 2000 Grimur Jonsson - Tryggvi Larusson
/*
 * Copyright 2000-2001 idega.is All Rights Reserved.
 */

package is.idega.idegaweb.golf.block.login.presentation;

import is.idega.idegaweb.golf.block.login.business.GolfLoginBusiness;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.presentation.GolfBlock;

import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.Parameter;
import com.idega.presentation.ui.PasswordInput;
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



	public GolfLogin() {
		super();

	}


	private void startState() {
		String userText = getResourceBundle().getLocalizedString("user", "User");
		String passwordText = getResourceBundle().getLocalizedString("password", "Password");

		Form myForm = new Form();
		myForm.setEventListener(GolfLoginBusiness.class);

		myForm.setMethod("post");
		myForm.maintainAllParameters();

		Text loginTexti = getSmallHeader(userText);
		Text passwordTexti = getSmallHeader(passwordText);

		Table myTable = new Table(6,1);
		myTable.setCellpadding(0);
		myTable.setCellspacing(0);
		myTable.setCellpaddingRight(1,1,5);
		myTable.setCellpaddingRight(2,1,5);
		myTable.setCellpaddingRight(3,1,5);
		myTable.setCellpaddingRight(4,1,5);
		myTable.setCellpaddingRight(5,1,5);
		myTable.setCellpaddingRight(6,1,5);

		myTable.add(loginTexti, 1, 1);


		TextInput login = (TextInput) getStyledSmallInterface(new TextInput("login"));
		login.setSize(8);
		myTable.add(login, 2, 1);


		myTable.add(passwordTexti, 3, 1);

		PasswordInput passw = (PasswordInput) getStyledSmallInterface(new PasswordInput("password"));
		passw.setSize(8);
		myTable.add(passw, 4, 1);

		/*GenericButton loginButton = getSubmitButton("tengja");
		loginButton.setContent(localize("login.login","Login"));
		myTable.add(loginButton, 5, 1);*/
		
		Text spacer = getStyleText("|", this.STYLENAME_TEMPLATE_HEADER2);
		myTable.add(spacer, 5, 1);
		
		Link loginLink = getStyleLink(localize("login.login","Login"), this.STYLENAME_TEMPLATE_LINK);
		loginLink.setToFormSubmit(myForm);
		myTable.add(loginLink, 6, 1);
		
//		if (showNewUserImage) {
//			GenericButton button = getSubmitButton();
//			button.setName(GolfLoginBusiness.newLoginStateParameter);
//			button.setContent(localize("login.register","Register"));
//			myTable.add(button, 6, 1);
//		} else if (forgotPasswordUrl != null) {
//			Link l = getSmallLink(localize("login.forgot","Forgot"));
//			l.setURL(forgotPasswordUrl);
//			myTable.add(l, 6, 1);
//		}

		myTable.add(new Parameter(GolfLoginBusiness.LoginStateParameter, "login"));



		myForm.add(myTable);
		add(myForm);

	}

	public void main(IWContext modinfo) throws Exception {

		String state = internalGetState(modinfo);
		if (state != null) {
			if (state.equals("loggedon")) {
				isLoggedOn(modinfo);
			} else if (state.equals("loggedoff")) {
				startState();
			} else if (state.equals("newlogin")) {
				String temp = modinfo.getParameter("login");
				if (temp != null) {
					if (temp.length() == 11) {
						loginFailed("toBig");
					} else if (temp.equals("") || temp.equals(" ")) {
						loginFailed("empty");
					} else {
						loginFailed("");
					}
				} else {
					loginFailed("");
				}
			} else if (state.equals("loginfailed")) {
				loginFailed("");
			} else {
				startState();
			}

		} else {
			startState();
		}
	}

	public String internalGetState(IWContext modinfo) {
		return GolfLoginBusiness.internalGetState(modinfo);
	}

	private void isLoggedOn(IWContext modinfo) {

		Form myForm = new Form();
		myForm.setEventListener(GolfLoginBusiness.class);

		myForm.setMethod("post");
		myForm.maintainAllParameters();

		Table myTable = new Table();
		myTable.setCellspacing(0);
		myTable.setCellpadding(0);
		myTable.setCellpaddingLeft(1,1,5);
		myTable.setCellpaddingRight(2,1,5);

		Member member = (Member) modinfo.getSession().getAttribute("member_login");
		Text userName = getSmallHeader(member.getName());
		myTable.add(userName,1,1);
		myTable.setNoWrap(1,1);

		Text spacer = getStyleText("|", this.STYLENAME_TEMPLATE_HEADER2);
		myTable.add(spacer, 2, 1);
		
		Link logout = getStyleLink(localize("login.logout","Log out"), this.STYLENAME_TEMPLATE_LINK);
		logout.setToFormSubmit(myForm);
		myTable.add(logout, 3, 1);

		/*GenericButton button = getSubmitButton();
		button.setContent(localize("login.logout","Log out"));
		myTable.add(button, 2, 1);*/
		myTable.add(new Parameter(GolfLoginBusiness.LoginStateParameter, "logoff"));

		myForm.add(myTable);
		add(myForm);
	}


	private void loginFailed(String what) {
		String userText = getResourceBundle().getLocalizedString("user", "User");
		String passwordText = getResourceBundle().getLocalizedString("password", "Password");


		Form myForm = new Form();
		myForm.setEventListener(GolfLoginBusiness.class);

		myForm.setMethod("post");
		myForm.maintainAllParameters();

		Text loginTexti = getSmallHeader(userText);
		Text passwordTexti =getSmallHeader(passwordText);

		Table myTable = new Table();
		myTable.setCellspacing(0);
		myTable.setCellpadding(0);
		myTable.setCellpaddingRight(1,1,5);
		myTable.setCellpaddingRight(2,1,5);
		
		Text failed = getMessageText(getResourceBundle().getLocalizedString("loginfailed", "Login failed"));
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
		
		Link tryAgain = getStyleLink(localize("login.try_again","Try again"), this.STYLENAME_TEMPLATE_LINK);
		tryAgain.setToFormSubmit(myForm);
		myTable.add(tryAgain, 3, 1);

		/*GenericButton tryAgain = getSaveButton();
		tryAgain.setContent(localize("login.try_again","Try again"));
		myTable.add(tryAgain, 3, 1);*/
		myTable.add(new Parameter(GolfLoginBusiness.LoginStateParameter, "tryagain"));

		myForm.add(myTable);
		add(myForm);

	}

	///// additional mothods /////////

	private void logOut(IWContext modinfo) throws Exception {
		GolfLoginBusiness.logOut2(modinfo);
	}

}