package se.idega.idegaweb.commune.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.core.accesscontrol.data.LoginTable;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.EmailHome;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.PasswordInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.User;
  
/**
 * @author Gimmi - idega
 */
public class CitizenInfomationEditor extends CommuneBlock {

	private String PARAMETER_LOGIN = "cie_lgn";
	private String PARAMETER_OLD_LOGIN = "cie_old_lgn";
	private String PARAMETER_CURRENT_PASSWORD = "cie_cr_psw";
	private String PARAMETER_NEW_PASSWORD = "cie_nw_psw";
	private String PARAMETER_NEW_PASSWORD_AGAIN = "cie_nwa_psw";
	private String PARAMETER_EMAIL = "cie_eml";
	private String PARAMETER_EMAIL_ID = "cie_eml_id";

	private String SPACER = "_";
	
	private String ACTION_PARAMETER = "cie_act";
	private String ACTION_UPDATE = "cie_act_upd";

	private User user;

	public void main(IWContext iwc) throws RemoteException {
		user = iwc.getCurrentUser();		
		if (user != null) {
			
			String action = iwc.getParameter(ACTION_PARAMETER);
			if (action == null) {
				drawForm();
			}else if (action.equals(ACTION_UPDATE)) {
				if (!updateEmails(iwc)) {
					add(super.getHeader(getResourceBundle().getLocalizedString("emails_update_failed","Failed to update emails")));
					add(super.getHeader(Text.BREAK));
				}
				try {
					String logErr = updateLogin(iwc);
					if (logErr == null) {
						add(super.getHeader(getResourceBundle().getLocalizedString("login_updated","Login updated")));
					}else {		
						add(super.getHeader(getResourceBundle().getLocalizedString("login_not_updated","Login not updated")+" ( "+logErr+" ) "));
					}
				}catch (Exception e) {
					e.printStackTrace(System.err);
					add(super.getHeader(getResourceBundle().getLocalizedString("login_update_failed","Failed to update login")+" ( "+e.getMessage()+" ) "));
				}
				add(super.getHeader(Text.BREAK));
				drawForm();	
			}
			
		}else {
			add(super.getSmallText(getResourceBundle().getLocalizedString("not_logged_in","Not logged in")));
		}
	}
	
	private String updateLogin(IWContext iwc) throws Exception{
		
		String login    = iwc.getParameter(PARAMETER_LOGIN);
		String loginOld = iwc.getParameter(PARAMETER_OLD_LOGIN);
		String currPass = iwc.getParameter(PARAMETER_CURRENT_PASSWORD);
		String newPass1 = iwc.getParameter(PARAMETER_NEW_PASSWORD);
		String newPass2 = iwc.getParameter(PARAMETER_NEW_PASSWORD_AGAIN);


		/** Verifying old password */
		if (LoginDBHandler.verifyPassword(loginOld, currPass)) {
			/** Checking if new passwords are the same, and not "" */
			if (newPass1.equals(newPass2) && !newPass1.equals("")) {
				LoginDBHandler.updateLogin(((Integer)user.getPrimaryKey()).intValue(), login, newPass1);
				return null;
			}	else {
				return getResourceBundle().getLocalizedString("passwords_not_the_same_or_invalid","Passwords not the same or invalid");
			}
		}else {
			return getResourceBundle().getLocalizedString("current_password_invalid","Current password is invalid");
		}
//		return getResourceBundle().getLocalizedString("unknown_reason","Unknown reason");
	}

	private boolean updateEmails(IWContext iwc) throws RemoteException{
	
		try {
			String[] emailIds = iwc.getParameterValues(PARAMETER_EMAIL_ID);
			Email email;
			EmailHome emailHome = (EmailHome) IDOLookup.getHome(Email.class);
			
			/** Update Emails */
			if (emailIds != null) {
				for (int i = 0; i < emailIds.length; i++) {
					email = emailHome.findByPrimaryKey(new Integer(emailIds[i]));
					String sEmail = iwc.getParameter(PARAMETER_EMAIL+SPACER+emailIds[i]);
					if (sEmail == null || sEmail.equals("")) {
						/** Remove email from user... */
						user.removeEmail(email);
						email.remove();
					}else {
						/** Update email */
						email.setEmailAddress(sEmail);
						email.store();
					}
				}	
				
			}
			/** New Emails */
			// Prevent multiple emails...
			if (user.getEmails() != null && user.getEmails().size() > 0)
			{}else{
				String sEmail = iwc.getParameter(PARAMETER_EMAIL);
				if (sEmail != null && !sEmail.equals("")) {
					email = emailHome.create();
					email.setEmailAddress(sEmail);
					email.store();
					user.addEmail(email);
				}
				
			}
			return true;	
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		return false;

	}
	
	
	private void drawForm() throws RemoteException{
		Form form = new Form();
		Table table = new Table();	
		form.add(table);
		int row = 1;

		Text tEmail = super.getSmallText(getResourceBundle().getLocalizedString("email","Email"));

		Text tLogin = super.getSmallText(getResourceBundle().getLocalizedString("login","Login")+":");
		TextInput tiLogin = (TextInput) super.getStyledInterface(new TextInput(PARAMETER_LOGIN));
		
		Text tCurrentPassword = super.getSmallText(getResourceBundle().getLocalizedString("current_password","Current password")+":");
		PasswordInput tiCurrentPassword = (PasswordInput) super.getStyledInterface(new PasswordInput(PARAMETER_CURRENT_PASSWORD));
		
//		Text tNewPassword = super.getSmallText(getResourceBundle().getLocalizedString("new_password","New password")+":");
		Text tNewPassword = getSmallText(localize("new_password", "New password") + ":");
		PasswordInput tiNewPassword = (PasswordInput) super.getStyledInterface(new PasswordInput(PARAMETER_NEW_PASSWORD));

		Text tNewPasswordAgain = super.getSmallText(getResourceBundle().getLocalizedString("repeat_password","Repeat password")+":");
		PasswordInput tiNewPasswordAgain = (PasswordInput) super.getStyledInterface(new PasswordInput(PARAMETER_NEW_PASSWORD_AGAIN));

		SubmitButton update = (SubmitButton) super.getStyledInterface(new SubmitButton(getResourceBundle().getLocalizedString("update","Update"), ACTION_PARAMETER, ACTION_UPDATE));


		Collection emails = user.getEmails();
		try {
			if (emails != null) {
				Iterator iter = emails.iterator();
				Email email;
				EmailHome emailHome = (EmailHome) IDOLookup.getHome(Email.class);
				String emId;
				while (iter.hasNext()) {
					email = emailHome.findByPrimaryKey(iter.next());
					emId = email.getPrimaryKey().toString();
					TextInput tiEmail = (TextInput) super.getStyledInterface(new TextInput(PARAMETER_EMAIL+SPACER+emId, email.getEmailAddress()));		
					table.add(tEmail, 1, row);
					table.add(new HiddenInput(PARAMETER_EMAIL_ID, emId), 2, row);
					table.add(tiEmail, 2, row);
					++row;
				}
			}
			
			/** Remove IF to enable multiple emails... */
			if (emails.size() == 0) {
				TextInput tiEmail = (TextInput) super.getStyledInterface(new TextInput(PARAMETER_EMAIL));		
				table.add(tEmail, 1, row);
				table.add(tiEmail, 2, row);
			}
			
		} catch (FinderException e) {
			e.printStackTrace(System.err);
		}

		
		++row;
		table.addBreak(1, row);
		
		++row;
		LoginTable loginTable = LoginDBHandler.getUserLogin(((Integer) user.getPrimaryKey()).intValue());
		if (loginTable != null) {
			tiLogin.setContent(loginTable.getUserLogin());	
			table.add(new HiddenInput(PARAMETER_OLD_LOGIN, loginTable.getUserLogin()), 2, row);
		}
		table.add(tLogin, 1, row);
		table.add(tiLogin, 2, row);
		
		++row;
		table.add(tCurrentPassword, 1, row);
		table.add(tiCurrentPassword, 2, row);

		++row;
		table.add(tNewPassword, 1, row);
		table.add(tiNewPassword, 2, row);

		++row;
		table.add(tNewPasswordAgain, 1, row);
		table.add(tiNewPasswordAgain, 2, row);

		++row;
		table.add(update, 2, row);
		table.setAlignment(2, row, Table.HORIZONTAL_ALIGN_RIGHT);
	
		add(form);
	}

}
