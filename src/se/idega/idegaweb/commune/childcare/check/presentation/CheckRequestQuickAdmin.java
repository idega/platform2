package se.idega.idegaweb.commune.childcare.check.presentation;

import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.user.data.User;

import se.idega.idegaweb.commune.childcare.check.data.Check;

/**
 * @author Laddi
 */
public class CheckRequestQuickAdmin extends CheckRequestAdmin {

	public void main(IWContext iwc) {
		this.setResourceBundle(getResourceBundle(iwc));

		try {
			int action = parseAction(iwc);
			switch (action) {
				case ACTION_VIEW_CHECK :
					int checkId = getCheckID(iwc.getParameter(PARAM_CHECK_ID));
					Check check = getCheckBusiness(iwc).getCheck(checkId);
					viewCheck(iwc, check, false);
					break;
				case ACTION_GRANT_CHECK :
					grantCheck(iwc);
					break;
				case ACTION_RETRIAL_CHECK :
					retrialCheck(iwc);
					break;
				case ACTION_SAVE_CHECK :
					saveCheck(iwc);
					break;
			}
		} catch (Exception e) {
			add(new ExceptionWrapper(e, this));
		}
	}
	
	private int getCheckID(String fromString) {
		try {
			return Integer.parseInt(fromString);
		}
		catch (NumberFormatException nfe) {
			return -1;	
		}
	}
	
	protected int parseAction(IWContext iwc) {
		int action = getDefaultView();

		if (iwc.isParameterSet(PARAM_GRANT_CHECK)) {
			action = ACTION_GRANT_CHECK;
		}

		if (iwc.isParameterSet(PARAM_RETRIAL_CHECK)) {
			action = ACTION_RETRIAL_CHECK;
		}

		if (iwc.isParameterSet(PARAM_SAVE_CHECK)) {
			action = ACTION_SAVE_CHECK;
		}

		return action;
	}

	protected int getDefaultView() {
		return ACTION_VIEW_CHECK;
	}
	
	private Check verifyCheckRules(IWContext iwc) throws Exception {
		User child = getChild(iwc);
		int checkFee = getCheckBusiness(iwc).getCheckFee();
		int checkAmount = getCheckBusiness(iwc).getCheckAmount();
		Check check = null;
		
		if ( iwc.isParameterSet(PARAM_CHECK_ID) ) {
			check = getCheckBusiness(iwc).getCheck(getCheckID(iwc.getParameter(PARAM_CHECK_ID)));
		}
		if ( check == null && child != null ) {
			check = getCheckBusiness(iwc).createCheck(((Integer)child.getPrimaryKey()).intValue(),getCheckBusiness(iwc).getMethodSystem(),checkAmount,checkFee,getCheckBusiness(iwc).getParent(child));
		}
		
		if ( check != null ) {
			String[] selectedRules = iwc.getParameterValues(PARAM_RULE);
			String notes = iwc.getParameter(PARAM_NOTES);
			String userNotes = iwc.getParameter(PARAM_USER_NOTES);
			return getCheckBusiness(iwc).saveCheckRules(check, selectedRules, notes, userNotes, iwc.getCurrentUser());
		}

		return null;
	}

	private void grantCheck(IWContext iwc) throws Exception {
		Check check = verifyCheckRules(iwc);
		if ( check != null ) {
			if (!getCheckBusiness(iwc).allRulesVerified(check)) {
				viewCheck(iwc, check, true);
				return;
			}
			String subject = getResourceBundle(iwc).getLocalizedString("check.granted_message_headline","Check granted");
			String body = getResourceBundle(iwc).getLocalizedString("check.granted_message_body","Your check has been granted");
			getCheckBusiness(iwc).approveCheck(check,subject,body,iwc.getCurrentUser());
	
			if (getResponsePage() != null && !iwc.isInEditMode()) {
				iwc.forwardToIBPage(getParentPage(), getResponsePage());
			} else {
				add(getText(getResourceBundle(iwc).getLocalizedString("check.check_granted","Check granted")+": "+((Integer)check.getPrimaryKey()).toString()));
			}
		}
	}

	private void retrialCheck(IWContext iwc) throws Exception {
		Check check = verifyCheckRules(iwc);
		if ( check != null ) {
			String subject = getResourceBundle(iwc).getLocalizedString("check.retrial_message_headline","Check denied");
			String body = getResourceBundle(iwc).getLocalizedString("check.retrial_message_body","Your check has been denied");
			getCheckBusiness(iwc).retrialCheck(check,subject,body,iwc.getCurrentUser());

			if (getResponsePage() != null && !iwc.isInEditMode()) {
				iwc.forwardToIBPage(getParentPage(), getResponsePage());
			} else {
				add(getText(getResourceBundle(iwc).getLocalizedString("check.check_retrial","Check denied")+": "+((Integer)check.getPrimaryKey()).toString()));
			}
		}
	}

	/**
	 * A method that saves the current check.
	 * @param IWContext iwc
	 * @throws Exception
	 */
	private void saveCheck(IWContext iwc) throws Exception {
		Check check = verifyCheckRules(iwc);
		if ( check != null ) {
			viewCheck(iwc,check,false);
		}
	}

}
