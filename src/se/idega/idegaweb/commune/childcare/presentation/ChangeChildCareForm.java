/*
 * $Id:$
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.childcare.presentation;

import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.user.data.User;

import se.idega.idegaweb.commune.childcare.business.ChildCareBusiness;
import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;
import se.idega.idegaweb.commune.presentation.CommuneBlock;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

/**
 * This class does something very clever.....
 * 
 * @author <a href="palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class ChangeChildCareForm extends CommuneBlock {
	private final static int ACTION_VIEW_CONTRACTS = 0;
	private final static int ACTION_VIEW_FORM = 1;
	private final static int ACTION_SUBMIT_CHANGE = 2;
	private final static int ACTION_SUBMIT_CANCEL = 3;

	private final static String NOT_LOGGED_IN = "cca_not_logged_in";
	private final static String NO_CHILDREN = "cccf_no_children";

	private final static String PARAM_CHILD_ID = "cca_child_id";

	private final static String ERROR_NO_CHILD_IN_CHILDCARE = "cccf_no_child_in_childcare";

	protected User _user = null;

	/**
	 * @see com.idega.presentation.PresentationObject#main(IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		_user = iwc.getCurrentUser();

		if (_user != null) {
			setResourceBundle(getResourceBundle(iwc));

			try {
				int action = parseAction(iwc);
				switch (action) {
					case ACTION_VIEW_CONTRACTS :
						viewContracts(iwc);
						break;
					case ACTION_VIEW_FORM :
						viewForm(iwc);
						break;
					case ACTION_SUBMIT_CHANGE :
						submitChange(iwc);
						break;
					case ACTION_SUBMIT_CANCEL :
						submitCancel(iwc);
						break;
				}
			}
			catch (Exception e) {
				super.add(new ExceptionWrapper(e, this));
			}
		}
		else {
			add(getErrorText(localize(NOT_LOGGED_IN, "No user logged in")));
		}
	}

	private int parseAction(IWContext iwc) {
		return ACTION_VIEW_CONTRACTS;
	}

	private void viewContracts(IWContext iwc) {
		Collection applications = null;

		try {
			applications = getChildCareBusiness(iwc).getGrantedApplicationsByUser(iwc.getCurrentUser());
		}
		catch (RemoteException e) {
		}

		if (applications != null) {
			Iterator it = applications.iterator();
			while (it.hasNext()) {
				ChildCareApplication appl = (ChildCareApplication) it.next();

				User child = null;
				try {
					child = appl.getChild();
				}
				catch (RemoteException e) {
				}
				catch (Exception e) {
				}

				Link link = null;
				//try {
					if (child != null) {
						link = new Link(child.getName());
						link.addParameter(PARAM_CHILD_ID, ((Integer) appl.getPrimaryKey()).toString());
					}
				//}
				//catch (RemoteException e) {
				//}

				if (link != null) {
					add(link);
					add(Text.BREAK);
				}
			}
		}
		else {
			add(getErrorText(localize(ERROR_NO_CHILD_IN_CHILDCARE, "User has no children in childcare the nacka register")));
		}

	}

	private void viewForm(IWContext iwc) {

	}

	private void submitChange(IWContext iwc) {

	}

	private void submitCancel(IWContext iwc) {

	}

	private ChildCareBusiness getChildCareBusiness(IWContext iwc) {
		try {
			return (ChildCareBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, ChildCareBusiness.class);
		}
		catch (RemoteException e) {
			return null;
		}
	}
}