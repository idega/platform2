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

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import se.idega.idegaweb.commune.childcare.business.ChildCareBusiness;
import se.idega.idegaweb.commune.childcare.data.ChangeChildCare;
import se.idega.idegaweb.commune.childcare.data.ChangeChildCareHome;
import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;
import se.idega.idegaweb.commune.presentation.CommuneBlock;

import com.idega.data.IDOLookup;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.User;

/**
 * This class does something very clever.....
 * 
 * @author <a href="palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class ChangeChildCareForm extends CommuneBlock {
	private final static String ACTION = "ACTION";
	private final static int ACTION_VIEW_CONTRACTS = 0;
	private final static int ACTION_VIEW_FORM = 1;
	private final static int ACTION_SUBMIT_CHANGE = 2;
	private final static int ACTION_SUBMIT_CANCEL = 3;

	private final static String NOT_LOGGED_IN = "cca_not_logged_in";
	//private final static String NO_CHILDREN = "cccf_no_children";

	private final static String PARAM_CHILD_ID = "cca_child_id";

	private final static String ERROR_NO_CHILD_IN_CHILDCARE = "cccf_no_child_in_childcare";


	private final static String PARAM_CARE_TIME = "cca_care_time";
	
	protected User _user = null;

	/**
	 * @see com.idega.presentation.PresentationObject#main(IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		

		if (iwc.isLoggedOn()) {
			_user = iwc.getCurrentUser();
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
		try{
			return Integer.parseInt(iwc.getParameter(ACTION));
		} catch(NumberFormatException ex){
			return ACTION_VIEW_CONTRACTS;
		}
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
				catch (Exception e) {
				}

				Link link = null;
				//try {
					if (child != null) {
						link = new Link(child.getName());
						link.addParameter(PARAM_CHILD_ID, ((Integer) appl.getPrimaryKey()).toString());
						link.addParameter(ACTION, ACTION_VIEW_FORM);
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

	private void viewForm(IWContext iwc) throws RemoteException{
		ChildCareApplication application = getChildCareBusiness(iwc).getApplicationByPrimaryKey(iwc.getParameter(PARAM_CHILD_ID));
		ChangeChildCare change = getChangeChildCare(iwc);
		
		
		int careTime = change.getCareTime();
		String name = application.getChild().getName();

		Form form = new Form();

		HiddenInput childId = new HiddenInput(PARAM_CHILD_ID, ((Integer) application.getPrimaryKey()).toString());
		SubmitButton submitBtn = new SubmitButton("Submit", ACTION, new Integer(ACTION_SUBMIT_CHANGE).toString());
		submitBtn.setAsImageButton(true);
		SubmitButton cancelBtn = new SubmitButton("Cancel", ACTION, new Integer(ACTION_SUBMIT_CANCEL).toString());
		cancelBtn.setAsImageButton(true);

		add(new Text(name));
		add(form);
		form.add(new Text("Care time:"));
		form.add(new TextInput(PARAM_CARE_TIME, new Integer(careTime).toString()));
		form.add(childId);
		form.add(new Break(1));
		form.add(submitBtn);
		form.add(cancelBtn);

	}

	private void submitChange(IWContext iwc) throws RemoteException{
		ChildCareApplication application = getChildCareBusiness(iwc).getApplicationByPrimaryKey(iwc.getParameter(PARAM_CHILD_ID));
		int careTime = new Integer(iwc.getParameter(PARAM_CARE_TIME).trim()).intValue();
		add(new Text("<br>Care time:" + careTime + "."));

		ChangeChildCare change = getChangeChildCare(iwc);
		add(new Text("<br>providerId" + new Integer(application.getProviderId()).toString()));
		add(new Text("<br>childId" + new Integer(change.getChildId()) + "<br>"));
		change.setProviderId(application.getProviderId());
		change.setCareTime(careTime);
		change.store();


//		application.setCareTime(careTime);
		
//		iwc.forwardToIBPage(null, getResponsePage());
	}

	private void submitCancel(IWContext iwc){
		iwc.forwardToIBPage(null, getResponsePage());
	}

	private ChildCareBusiness getChildCareBusiness(IWContext iwc) {
		try {
			return (ChildCareBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, ChildCareBusiness.class);
		}
		catch (RemoteException e) { 
			return null;
		}
	}
	
	private ChangeChildCare getChangeChildCare(IWContext iwc) {
		try {
			ChangeChildCareHome home = (ChangeChildCareHome) IDOLookup.getHome(ChangeChildCare.class);
            try{
				return home.findByPrimaryKey(iwc.getParameter(PARAM_CHILD_ID));
            }catch(FinderException ex){
            	try{
					ChangeChildCare ccc = (ChangeChildCare) home.createIDO();
					ccc.setChildId(new Integer(iwc.getParameter(PARAM_CHILD_ID)).intValue());
					
					
					return ccc;
            	}catch(CreateException e){
            		add(new Text(e.toString()));
            		return null;            	
            	}
            }
			
		}
		catch (RemoteException e) {
			return null;
		}
	}

	
	
		
}