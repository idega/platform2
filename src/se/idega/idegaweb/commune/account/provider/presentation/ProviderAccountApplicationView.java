/*
 * $Id: ProviderAccountApplicationView.java,v 1.8 2003/10/06 12:36:03 laddi Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.account.provider.presentation;
import se.idega.idegaweb.commune.account.provider.data.ProviderApplication;

import com.idega.data.IDOUtil;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.Parameter;
import com.idega.presentation.ui.SubmitButton;
/**
 * @author <a href="mail:tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */
public class ProviderAccountApplicationView extends ProviderAccountApplication
{
	
	private static final int ACTION_ACCEPT=2;
	private static final int ACTION_REJECT=3;
	
	private static final String PARAM_FORM_ACCEPT = "paa_v_accept";
	private static final String PARAM_FORM_REJECT = "paa_v_reject";
	private static final String PARAM_ACTION = "paa_v_action";
	//private static final String PARAM_FORM_APP_ID = "paa_v_app_id";
	
	
	public void setProviderApplication(ProviderApplication appl){
		setApplicationID((Integer)appl.getPrimaryKey());
		setProviderName(appl.getName());
		setManagerName(appl.getManagerName());
		setNumberOfPlaces(appl.getNumberOfPlaces());
		setAddress(appl.getAddress());
		setAdditionalInfo(appl.getAdditionalInfo());
		setPhone(appl.getPhone());
		setEmail(appl.getEmail());
		IDOUtil idoUtil = IDOUtil.getInstance();
		int postalCodeID = idoUtil.getID(appl.getPostalCode());
		setPostalCode(postalCodeID);
		int schoolAreaID = idoUtil.getID(appl.getSchoolArea());
		setSchoolArea(schoolAreaID);

		int[] schoolTypeIDs=idoUtil.getIDs(appl.getSchoolTypes());
		setSchoolTypes(schoolTypeIDs);	
	}
	
	/**
	 * Method addButtons.
	 * @param iwc
	 */
	protected void addButtons(IWContext iwc)
	{
		addButton(getAcceptButton());
		addButton(getRejectButton());
		addButton(new Parameter(PARAM_APPLICATION_ID,Integer.toString(getApplicationID())));
	}
	/**
	 * Method getRejectButton.
	 * @param iwc
	 * @return PresentationObject
	 */
	private PresentationObject getRejectButton()
	{
		SubmitButton butt = new SubmitButton(localize(PARAM_FORM_REJECT,"Reject"),PARAM_ACTION,PARAM_FORM_REJECT);
		butt.setAsImageButton(true);
		return butt;
	}


	/**
	 * Method getAcceptButton.
	 * @param iwc
	 * @return PresentationObject
	 */
	private PresentationObject getAcceptButton()
	{
		SubmitButton butt = new SubmitButton(localize(PARAM_FORM_ACCEPT,"Accept"),PARAM_ACTION,PARAM_FORM_ACCEPT);
		butt.setAsImageButton(true);
		return butt;
	}



	/**
	 * Can be ovverrided in subclasses
	 */
	protected void performAction(int action,IWContext iwc)throws Exception
	{
			switch (action)
			{
				case ACTION_ACCEPT :
					acceptApplication(iwc);
					break;
				case ACTION_REJECT :
					rejectApplication(iwc);
					break;
				default:
					super.performAction(action,iwc);
			}
	}
	/**
	 * This method can be overrided to add new actions
	 */
	protected int parseAction(IWContext iwc)
	{
		String action = iwc.getParameter(PARAM_ACTION);
		if (action != null && action.equals(PARAM_FORM_ACCEPT))
		{
			return ACTION_ACCEPT;
		}
		else if (action != null && action.equals(PARAM_FORM_REJECT))
		{
			return ACTION_REJECT;
		}
		return super.parseAction(iwc);
	}
	
	private void acceptApplication(IWContext iwc)
	{
		try{
			int applicationID=this.getApplicationID();
			int userID;
			try{
				userID = ((Integer)iwc.getCurrentUser().getPrimaryKey()).intValue();
			}
			catch(NullPointerException ne){
				throw new Exception(localize("paa_not_logged_on","You are not logged on"));
			}
			getBusiness(iwc).acceptApplication(applicationID,userID);
			add(getSmallText(localize("paa_acc_appl_complete","Accepted application successfully")));
		}
		catch(Exception e){
			add(getSmallText(localize("paa_acc_appl_failed","Failed accepting application, reason was: ")+e.getMessage()));
			e.printStackTrace();
		}
	}

	private void rejectApplication(IWContext iwc)
	{
		try{
			int applicationID=this.getApplicationID();
			int userID;
			try{
				userID = ((Integer)iwc.getCurrentUser().getPrimaryKey()).intValue();
			}
			catch(NullPointerException ne){
				throw new Exception(localize("paa_not_logged_on","You are not logged on"));
			}
			getBusiness(iwc).rejectApplication(applicationID,userID);
			add(getSmallText(localize("paa_rej_appl_complete","Rejected application successfully")));
		}
		catch(Exception e){
			add(getSmallText(localize("paa_rej_appl_failed","Failed rejecting application, reason was: ")+e.getMessage()));
			e.printStackTrace();
		}
	}
	/**
	 * Method getViewApplicationID.
	 * @param iwc
	 * @return int
	 */
	/*protected int getViewApplicationID(IWContext iwc)
	{
		return Integer.parseInt(iwc.getParameter(PARAM_FORM_APP_ID));
	}*/
}