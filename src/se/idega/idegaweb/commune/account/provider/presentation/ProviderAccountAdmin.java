/*
 * $Id: ProviderAccountAdmin.java,v 1.4 2003/04/02 16:45:53 laddi Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.account.provider.presentation;
import java.rmi.RemoteException;
import java.util.Iterator;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.account.provider.business.ProviderAccountBusiness;
import se.idega.idegaweb.commune.account.provider.data.ProviderApplication;
import se.idega.idegaweb.commune.presentation.CommuneBlock;

import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.Form;
/**
 * @author <a href="mail:tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */
public class ProviderAccountAdmin extends CommuneBlock
{
	private final static int ACTION_VIEW_LIST = 0;
	private final static int ACTION_VIEW_DETAILS = 1;
	private final static int ACTION_SUBMIT = 2;
	//private final static int SUBACTION_APPROVE = 10;
	//private final static int SUBACTION_REJECT = 11;
	//private final static int SUBACTION_NOT_CITIZEN = 12;
	private final static String PARAM_NUM_PLACES = "paa_num_places";
	//private final static String PARAM_EMAIL = "paa_email";
	//private final static String PARAM_PHONE_HOME = "paa_phone_home";
	//private final static String PARAM_PHONE_WORK = "paa_phone_work";
	private final static String PARAM_NAME = "paa_adm_name";
	private final static String PARAM_ADDRESS = "paa_adm_address";
	private final static String PARAM_FORM_APPROVE = "paa_adm_approve";
	private final static String PARAM_FORM_REJECT = "paa_adm_reject";
	private final static String PARAM_FORM_NOT_CITIZEN = "paa_adm_not_citizen";
	//private final static String PARAM_FORM_DETAILS = "paa_add_details";
	private final static String PARAM_APPLICATION_ID = ProviderAccountApplication.PARAM_APPLICATION_ID;
	private final static String ERROR_LIST_VIEW = "paa_error_list_view";
	public void main(IWContext iwc)
	{
		setResourceBundle(getResourceBundle(iwc));
		try
		{
			int action = parseAction(iwc);
			switch (action)
			{
				case ACTION_VIEW_LIST :
					viewList(iwc);
					break;
				case ACTION_VIEW_DETAILS :
					viewDetails(iwc);
					break;
				case ACTION_SUBMIT :
					submit(iwc);
					break;
			}
		}
		catch (Exception e)
		{
			super.add(new ExceptionWrapper(e, this));
		}
	}
	private int parseAction(IWContext iwc)
	{
		if (iwc.isParameterSet(PARAM_APPLICATION_ID))
		{
			System.out.println("Viewing details ");
			return ACTION_VIEW_DETAILS;
		}
		else
		{
			if (iwc.isParameterSet(PARAM_FORM_APPROVE))
			{
				String value = iwc.getParameter(PARAM_FORM_APPROVE);
				if (value != null && !value.equals(""))
					System.out.println("Approving application " + value);
			}
			if (iwc.isParameterSet(PARAM_FORM_REJECT))
			{
				String value = iwc.getParameter(PARAM_FORM_REJECT);
				if (value != null && !value.equals(""))
					System.out.println("Rejecting application " + value);
			}
			if (iwc.isParameterSet(PARAM_FORM_NOT_CITIZEN))
			{
				String value = iwc.getParameter(PARAM_FORM_NOT_CITIZEN);
				if (value != null && !value.equals(""))
					System.out.println("Not citizen " + value);
			}
			if (iwc.isParameterSet(PARAM_APPLICATION_ID))
			{
				String value = iwc.getParameter(PARAM_APPLICATION_ID);
				if (value != null && !value.equals(""))
					System.out.println("Details for " + value);
			}
			System.out.println("Viewing list ");
			return ACTION_VIEW_LIST;
		}
	}
	private void viewList(IWContext iwc)
	{
		try
		{
			Form form = new Form();
			DataTable data = new DataTable();
			data.setUseTitles(false);
			data.setUseBottom(false);
			data.setUseTop(false);
			data.setWidth("100%");
			int row = 1;
			int col = 1;
			data.add(getHeader(localize(PARAM_NAME, "Name")), col++, row);
			data.add(getHeader(localize(PARAM_NUM_PLACES, "Number of Places")), col++, row);
			data.add(getHeader(localize(PARAM_ADDRESS, "Address")), col, row++);
			Iterator accounts = getBusiness(iwc).getAllPendingApplicationsIterator();
			int i = 0;
			while (accounts.hasNext())
			{
				i++;
				ProviderApplication element = (ProviderApplication) accounts.next();
				//}
				//for (int i = 1; i < 4; i++)
				//{
				col = 1;
				String name = element.getManagerName();
				int numPlaces = element.getNumberOfPlaces();
				String address = element.getAddress();
				data.add(getSmallText(name), col++, row);
				data.add(getSmallText(Integer.toString(numPlaces)), col++, row);
				data.add(getSmallText(address), col++, row);
				/*SubmitButton details =
					new SubmitButton(//element.getPrimaryKey().toString(),
						localize(PARAM_FORM_DETAILS, "Administrate"),
						PARAM_FORM_DETAILS,
						element.getPrimaryKey().toString());
				details.setAsImageButton(true);
				*/
				Link details = new Link(localize("paa_app_administrate", "Administrate"));
				details.addParameter(PARAM_APPLICATION_ID,
						element.getPrimaryKey().toString());
				details.setAsImageButton(true);
				data.add(details, col, row++);
			}
			form.add(data);
			add(form);
		}
		catch (RemoteException e)
		{
			add(this.getErrorText(localize(ERROR_LIST_VIEW, "Error viewing list")));
			e.printStackTrace();
		}
		catch (FinderException e)
		{
			add(this.getErrorText(localize(ERROR_LIST_VIEW, "Error viewing list")));
			e.printStackTrace();
		}
	}
	private void submit(IWContext iwc)
	{}
	private void viewDetails(IWContext iwc)
	{
		System.out.println("In viewDetails()");
		try{
			int appID = getViewApplicationID(iwc);
			System.out.println("appID="+appID);
			ProviderApplication app = getBusiness(iwc).getProviderApplication(appID);
			ProviderAccountApplicationView view = new ProviderAccountApplicationView();
			view.setProviderApplication(app);
			add(view);
		}
		catch(Exception e){
			add(e);	
		}

		/*      SubmitButton approve = new SubmitButton(localize(PARAM_FORM_APPROVE,"Approve"),PARAM_FORM_APPROVE,Integer.toString(i));
		      approve.setAsImageButton(true);
		      SubmitButton reject = new SubmitButton(localize(PARAM_FORM_REJECT,"Reject"),PARAM_FORM_REJECT,Integer.toString(i));
		      reject.setAsImageButton(true);
		      SubmitButton notCitizen = new SubmitButton(localize(PARAM_FORM_NOT_CITIZEN,"Not citizen"),PARAM_FORM_NOT_CITIZEN,Integer.toString(i));
		      notCitizen.setAsImageButton(true);*/
	}

	/**
	 * Method getViewApplicationID.
	 * @param iwc
	 * @return int
	 */
	protected int getViewApplicationID(IWContext iwc)
	{
		return Integer.parseInt(iwc.getParameter(PARAM_APPLICATION_ID));
	}

	protected ProviderAccountBusiness getBusiness(IWApplicationContext iwc) throws RemoteException
	{
		return (ProviderAccountBusiness) IBOLookup.getServiceInstance(iwc, ProviderAccountBusiness.class);
	}
}