/*
 * $Id: PostingParameterListEditor.java,v 1.6 2003/08/21 15:58:22 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.posting.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.sql.Date;

import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.IWContext;

import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;
import se.idega.idegaweb.commune.accounting.presentation.ListTable;
import se.idega.idegaweb.commune.accounting.presentation.ApplicationForm;
import se.idega.idegaweb.commune.accounting.presentation.ButtonPanel;
import se.idega.idegaweb.commune.accounting.posting.data.PostingParameters;
import se.idega.idegaweb.commune.accounting.regulations.data.ActivityType;
import se.idega.idegaweb.commune.accounting.regulations.data.CommuneBelongingType;
import se.idega.idegaweb.commune.accounting.regulations.data.CompanyType;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType;
import se.idega.idegaweb.commune.accounting.regulations.business.RegulationsBusiness;
import se.idega.idegaweb.commune.accounting.posting.business.PostingBusiness;


/**
 * PostingParameterListEdit is an idegaWeb block that handles maintenance of some 
 * default data thatis used in a "posting". The block shows/edits Periode, Activity, Regulation specs, 
 * Company types and Commune belonging. 
 * It handles posting variables for both own and double entry accounting
 *  
 * <p>
 * $Id: PostingParameterListEditor.java,v 1.6 2003/08/21 15:58:22 anders Exp $
 *
 * @author <a href="http://www.lindman.se">Kjell Lindman</a>
 * @version $Revision: 1.6 $
 */
public class PostingParameterListEditor extends AccountingBlock {

	private final static int ACTION_DEFAULT = 0;
	private final static int ACTION_EDIT = 1;

	private final static String KEY_SAVE = "posting_parm_edit.save";
	private final static String KEY_CANCEL = "posting_parm_edit.cancel";

	private final static String KEY_ACTIVITY_HEADER = "posting_parm_edit.activity_header";
	private final static String KEY_REGSPEC_HEADER = "posting_parm_edit.reg_spec_header";
	private final static String KEY_COMPANY_TYPE_HEADER = "posting_parm_edit.company_type_header";
	private final static String KEY_COM_BEL_HEADER = "posting_parm_edit.com_bel_header";

	private final static String KEY_HEADER = "posting_parm_edit.header";
	private final static String KEY_FROM_DATE = "posting_parm_edit.from_date";
	private final static String KEY_TO_DATE = "posting_parm_edit.to_date";
	private final static String KEY_CHANGE_DATE = "posting_parm_edit.change_date";
	private final static String KEY_CHANGE_SIGN = "posting_parm_edit.change_sign";
	private final static String KEY_CONDITIONS = "posting_def_edit.conditions";
	private final static String KEY_ACTIVITY = "posting_parm_edit.activity";
	private final static String KEY_REG_SPEC = "posting_parm_edit.reg_spec";
	private final static String KEY_COMPANY_TYPE = "posting_parm_edit.company_type";
	private final static String KEY_COMMUNE_BELONGING = "posting_parm_edit.commune_belonging";
	private final static String KEY_OWN_ENTRY = "posting_parm_edit.own_entry";
	private final static String KEY_DOUBLE_ENTRY = "posting_parm_edit.double_entry";
	
	private final static String KEY_ACCOUNT = "posting_parm_edit.account";
	private final static String KEY_LIABILITY = "posting_parm_edit.liability";
	private final static String KEY_RESOURCE = "posting_parm_edit.resource";
	private final static String KEY_ACTIVITY_CODE = "posting_parm_edit.activity_code";
	private final static String KEY_DOUBLE_ENTRY_CODE = "posting_parm_edit.double_entry_code";
	private final static String KEY_ACTIVITY_FIELD = "posting_parm_edit.activity_field";
	private final static String KEY_PROJECT = "posting_parm_edit.project";
	private final static String KEY_OBJECT = "posting_parm_edit.object";

	private final static String KEY_POST_ACCOUNT = "posting_parm_edit_post.account";
	private final static String KEY_POST_LIABILITY = "posting_parm_edit_post.liability";
	private final static String KEY_POST_RESOURCE = "posting_parm_edit_post.resource";
	private final static String KEY_POST_ACTIVITY_CODE = "posting_parm_edit_post.activity_code";
	private final static String KEY_POST_DOUBLE_ENTRY_CODE = "posting_parm_edit_post.double_entry_code";
	private final static String KEY_POST_ACTIVITY_FIELD = "posting_parm_edit_post.activity_field";
	private final static String KEY_POST_PROJECT = "posting_parm_edit_post.project";
	private final static String KEY_POST_OBJECT = "posting_parm_edit_post.object";

	private final static String PARAM_SAVE = "button_save";
	private final static String PARAM_CANCEL = "button_cancel";
	private final static String PARAM_POSTING_ID = "posting_id";

	private final static String PARAM_ACCOUNT = "pp_edit_account";
	private final static String PARAM_LIABILITY = "pp_edit_liability";
	private final static String PARAM_RESOURCE = "pp_edit_resource";
	private final static String PARAM_ACTIVITY_CODE = "pp_edit_activity_code";
	private final static String PARAM_DOUBLE_ENTRY_CODE = "pp_edit_double_entry_code";
	private final static String PARAM_ACTIVITY_FIELD = "p_edit_activity_field";
	private final static String PARAM_PROJECT = "pp_edit_project";
	private final static String PARAM_OBJECT = "pp_edit_object";

	private final static String PARAM_DOUBLE_ACCOUNT = "pp_double_edit_account";
	private final static String PARAM_DOUBLE_LIABILITY = "pp_double_edit_liability";
	private final static String PARAM_DOUBLE_RESOURCE = "pp_double_edit_resource";
	private final static String PARAM_DOUBLE_ACTIVITY_CODE = "pp_double_edit_activity_code";
	private final static String PARAM_DOUBLE_DOUBLE_ENTRY_CODE = "pp_double_edit_double_entry_code";
	private final static String PARAM_DOUBLE_ACTIVITY_FIELD = "p_double_edit_activity_field";
	private final static String PARAM_DOUBLE_PROJECT = "pp_double_edit_project";
	private final static String PARAM_DOUBLE_OBJECT = "pp_double_edit_object";

	private final static String PARAM_SELECTOR_ACTIVITY = "selector_activity";
	private final static String PARAM_SELECTOR_REGSPEC = "selector_regspec";
	private final static String PARAM_SELECTOR_COMPANY_TYPE = "selector_company_type";
	private final static String PARAM_SELECTOR_COM_BELONGING = "selector_com_belonging";


	/**
	 * Handles all of the blocks presentation.
	 * @param iwc user/session context 
	 */
	public void main(final IWContext iwc) {
		setResourceBundle(getResourceBundle(iwc));

		try {
			int action = parseAction(iwc);
			switch (action) {
				case ACTION_DEFAULT :
					viewMainForm(iwc);
					break;
			}
		}
		catch (Exception e) {
			super.add(new ExceptionWrapper(e, this));
		}
	}
	 
	/*
	 * Returns the action constant for the action to perform based 
	 * on the POST parameters in the specified context.
	 */
	private int parseAction(IWContext iwc) {
		return ACTION_DEFAULT;
	}

	/*
	 * Adds the default form to the block.
    */	
	private void viewMainForm(IWContext iwc) {
		ApplicationForm app = new ApplicationForm();
		PostingParameters pp = getThisPostingParameter(iwc);
		
		Table topPanel = getTopPanel(iwc, pp);		
		Table postingForm = getPostingForm(iwc, pp);
					
		ButtonPanel buttonPanel = new ButtonPanel();
		buttonPanel.addButton(PARAM_SAVE, KEY_SAVE, "Spara");
		buttonPanel.addButton(PARAM_CANCEL, KEY_CANCEL, "Avbryt");
		
		app.setTitle(KEY_HEADER, "Skapa/Ändra konteringlista");
		app.setSearchPanel(topPanel);
		app.setMainPanel(postingForm);
		app.setButtonPanel(buttonPanel);
		add(app);		
	}

	/*
	 * Returns a top panel consisting of from, to and changing dates + signature 
	 * @param iwc user/session context 
	 * @param pp PostingParameter 
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParameters
	 * @return populated table
	 */
	private Table getTopPanel(IWContext iwc, PostingParameters pp) {

		Table table = new Table();
		table.setWidth("75%");
		Date dd = Date.valueOf("2003-01-01");
		
		table.add(getFormLabel(KEY_FROM_DATE, "Från datum"),1 ,1);
		table.add(getSmallText(formatDateTemp(pp != null ? pp.getPeriodeFrom() : dd, 4)), 2, 1);
	
		table.add(getFormLabel(KEY_TO_DATE, "Tom datum"),3 ,1);
		table.add(getSmallText(formatDateTemp(pp != null ? pp.getPeriodeTo(): dd, 4)), 4, 1);

		table.add(getFormLabel(KEY_CHANGE_DATE, "Ändringsdatum"),1 ,2);
		table.add(getSmallText(formatDateTemp(pp != null ? pp.getChangedDate(): dd, 6)), 2, 2);
	
		table.add(getFormLabel(KEY_CHANGE_SIGN, "Ändringssignatur"),3 ,2);
		table.add(getSmallText(pp != null ? pp.getChangedSign(): ""), 4, 2);
		
		return table;	
	}
	
	/*
	 * Formats a date according to specifications 
	 * @param dt user/session context 
	 * @param pp PostingParameter 
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParameters
	 * @return formated date in String format
	 */
	private String formatDateTemp(Date dt, int len) {
		String ret = "";
		String y = ("00" + dt.getYear()).substring(2);
		String year = y.substring(y.length()-2);
		String m = ("00" + (dt.getMonth() + 1));
		String month = m.substring(m.length()-2);
		String d = ("00" + dt.getDay());
		String day = m.substring(d.length()-2);
		if (len == 4) {
			ret = year+month;
		}
		if (len == 6) {
			ret = year+month+day;
		}
		return ret;
	}
	
	/*
	 * Generates the main posting form with selectors for Activity, Regulation specs, Company types
	 * and Commune belonging type.
	 * It contains edit fields for "own entry" account as well as "double entry" accounts
	 *    
	 * @param dt user/session context 
	 * @param pp PostingParameter 
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParameters
	 * @return main posting table
	 */
	private Table getPostingForm(IWContext iwc, PostingParameters pp) {

		Table table = new Table();
		Table selectors = new Table();
		Table accounts = new Table();
		ListTable list1 = new ListTable(8);
		ListTable list2 = new ListTable(8);

		list1.setHeader(KEY_POST_ACCOUNT, "Konto", 1);
		list1.setHeader(KEY_POST_LIABILITY, "Ansvar", 2);
		list1.setHeader(KEY_POST_RESOURCE, "Resurs", 3);
		list1.setHeader(KEY_POST_ACTIVITY_CODE, "Verksamhet", 4);
		list1.setHeader(KEY_POST_DOUBLE_ENTRY_CODE, "Motpart", 5);
		list1.setHeader(KEY_POST_ACTIVITY_FIELD, "Aktivitet", 6);
		list1.setHeader(KEY_POST_PROJECT, "Projekt", 7);
		list1.setHeader(KEY_POST_OBJECT, "Objekt", 8);

		list2.setHeader(KEY_POST_ACCOUNT, "Konto", 1);
		list2.setHeader(KEY_POST_LIABILITY, "Ansvar", 2);
		list2.setHeader(KEY_POST_RESOURCE, "Resurs", 3);
		list2.setHeader(KEY_POST_ACTIVITY_CODE, "Verksamhet", 4);
		list2.setHeader(KEY_POST_DOUBLE_ENTRY_CODE, "Motpart", 5);
		list2.setHeader(KEY_POST_ACTIVITY_FIELD, "Aktivitet", 6);
		list2.setHeader(KEY_POST_PROJECT, "Projekt", 7);
		list2.setHeader(KEY_POST_OBJECT, "Objekt", 8);
		
		list1.add(getFormTextInput(PARAM_ACCOUNT, pp != null ? pp.getPostingAccount() : "", 60, 6));
		list1.add(getFormTextInput(PARAM_LIABILITY, pp != null ? pp.getPostingLiability() : "", 60, 10));
		list1.add(getFormTextInput(PARAM_RESOURCE, pp != null ? pp.getPostingResource() : "", 60, 6));
		list1.add(getFormTextInput(PARAM_ACTIVITY_CODE, pp != null ? pp.getPostingActivityCode() : "", 60, 4));
		list1.add(getFormTextInput(PARAM_DOUBLE_ENTRY_CODE, pp != null ? pp.getPostingDoubleEntry() : "", 60, 6));
		list1.add(getFormTextInput(PARAM_ACTIVITY_FIELD, pp != null ? pp.getPostingActivity() : "", 80, 20));
		list1.add(getFormTextInput(PARAM_PROJECT, pp != null ? pp.getPostingProject() : "", 80, 20));
		list1.add(getFormTextInput(PARAM_OBJECT, pp != null ? pp.getPostingProject() : "", 80, 20));

		list2.add(getFormTextInput(PARAM_DOUBLE_ACCOUNT, pp != null ? pp.getDoublePostingAccount() : "", 60, 6));
		list2.add(getFormTextInput(PARAM_DOUBLE_LIABILITY, pp != null ? pp.getDoublePostingLiability() : "", 60, 10));
		list2.add(getFormTextInput(PARAM_DOUBLE_RESOURCE, pp != null ? pp.getDoublePostingResource() : "", 60, 6));
		list2.add(getFormTextInput(PARAM_DOUBLE_ACTIVITY_CODE, pp != null ? pp.getDoublePostingActivityCode() : "", 60, 4));
		list2.add(getFormTextInput(PARAM_DOUBLE_DOUBLE_ENTRY_CODE, pp != null ? pp.getDoublePostingDoubleEntry() : "", 60, 6));
		list2.add(getFormTextInput(PARAM_DOUBLE_ACTIVITY_FIELD, pp != null ? pp.getDoublePostingActivity() : "", 80, 20));
		list2.add(getFormTextInput(PARAM_DOUBLE_PROJECT, pp != null ? pp.getDoublePostingProject() : "", 80, 20));
		list2.add(getFormTextInput(PARAM_DOUBLE_OBJECT, pp != null ? pp.getDoublePostingProject() : "", 80, 20));

		try {
			selectors.add(getFormLabel(KEY_ACTIVITY, "Verksamhet"), 1, 1);
			selectors.add(activitySelector(iwc, PARAM_SELECTOR_ACTIVITY, 
				Integer.parseInt(pp != null ? pp.getActivity().getPrimaryKey().toString() : "0")), 2, 1);
						
			selectors.add(getFormLabel(KEY_REG_SPEC, "Regelspec.typ"), 1, 2);
			selectors.add(regSpecSelector(iwc, PARAM_SELECTOR_REGSPEC, 
											Integer.parseInt(pp != null ? 
											pp.getRegSpecType().getPrimaryKey().toString() : "0")), 2, 2);
	
			selectors.add(getFormLabel(KEY_COMPANY_TYPE, "Bolagstyp"), 1, 3);
			selectors.add(companyTypeSelector(iwc, PARAM_SELECTOR_COMPANY_TYPE, 
											Integer.parseInt(pp != null ? 
											pp.getCompanyType().getPrimaryKey().toString() : "0")), 2, 3);
	
			selectors.add(getFormLabel(KEY_COMMUNE_BELONGING, "Kommuntillhörighet:"), 1, 4);
			selectors.add(communeBelongingSelector(iwc, PARAM_SELECTOR_COM_BELONGING, 
											Integer.parseInt(pp != null ? 
											pp.getCompanyType().getPrimaryKey().toString() : "0")), 2, 4);
		} catch (Exception e) {
			super.add(new ExceptionWrapper(e, this));
		}	

		accounts.add(getFormText(KEY_OWN_ENTRY, "Egen kontering"), 1, 1);
		accounts.add(list1, 1, 2);
		accounts.add(getFormText(KEY_DOUBLE_ENTRY, "Mot kontering"), 1, 3);
		accounts.add(list2, 1, 4);
		
		table.add(getFormLabel(KEY_CONDITIONS, "Villkor"), 1, 1);
		table.add(selectors, 1, 2);
		table.add(accounts, 1, 3);
		
		return table;
	}

	/*
	 * Retrives from business the current posting data that is pointed out by PARAM_POSTING_ID.
	 * Remeber that this app only can edit one record at a time.
	 *    
	 * @param iwc Idega Web Context 
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParameters
	 * @return PostingParameter loaded with data
	 */
	private PostingParameters getThisPostingParameter(IWContext iwc) {
		PostingBusiness pBiz;
		PostingParameters pp = null;
		try {
			int postingID = 0;
			
			if(iwc.isParameterSet(PARAM_POSTING_ID)) {
				postingID = Integer.parseInt(iwc.getParameter(PARAM_POSTING_ID));
			}
			
			pBiz = getPostingBusiness(iwc);
			pp = (PostingParameters) pBiz.findPostingParameter(postingID);
			
		} catch (Exception e) {
			super.add(new ExceptionWrapper(e, this));
		}	
		return pp;
	}
	
	/*
	 * Generates a DropDownSelector for activites that is collected from the regulation framework. 
	 *    
	 * @param iwc Idega Web Context 
	 * @param name HTML Parameter ID for this selector
	 * @param refIndex The initial position to set the selector to 
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.ActivityType
	 * @return the drop down menu
	 */
	private DropdownMenu activitySelector(IWContext iwc, String name, int refIndex) throws Exception {
		DropdownMenu menu = (DropdownMenu) getStyledInterface(new DropdownMenu(name));
		menu.addMenuElement(0, localize(KEY_ACTIVITY_HEADER, "Välj aktivitet"));
		Collection col = getRegulationsBusiness(iwc).findAllActivityTypes();
		if(col != null){
			Iterator iter = col.iterator();
			while (iter.hasNext()) {
				ActivityType element = (ActivityType) iter.next();
				menu.addMenuElement("" + (((Integer) element.getPrimaryKey()).intValue()), 
									localize(element.getTextKey(), element.getTextKey()));
			}
			if (refIndex != -1) {
				menu.setSelectedElement(refIndex);
			}
		}
		return (DropdownMenu) menu;	
	}

	/*
	 * Generates a DropDownSelector for Regulation specifications that is collected 
	 * from the regulation framework. 
	 *    
	 * @param iwc Idega Web Context 
	 * @param name HTML Parameter ID for this selector
	 * @param refIndex The initial position to set the selector to 
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType
	 * @return the drop down menu
	 */
	private DropdownMenu regSpecSelector(IWContext iwc, String name, int refIndex) throws Exception {
		DropdownMenu menu = (DropdownMenu) getStyledInterface(new DropdownMenu(name));
		menu.addMenuElement(0, localize(KEY_REGSPEC_HEADER, "Välj regelspec.typ"));
		Collection col = getRegulationsBusiness(iwc).findAllRegulationSpecTypes();

		if(col != null){
			Iterator iter = col.iterator();
			while (iter.hasNext()) {
				RegulationSpecType element = (RegulationSpecType) iter.next();
				menu.addMenuElement("" + (((Integer) element.getPrimaryKey()).intValue()), 
										localize(element.getTextKey(), element.getTextKey()));
			}
			if (refIndex != -1) {
				menu.setSelectedElement(refIndex);
			}
		}
		return (DropdownMenu) menu;	
	}

	/*
	 * Generates a DropDownSelector for Company types that is collected 
	 * from the regulation framework. 
	 *    
	 * @param iwc Idega Web Context 
	 * @param name HTML Parameter ID for this selector
	 * @param refIndex The initial position to set the selector to 
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.CompanyType
	 * @return the drop down menu
	 */
	private DropdownMenu companyTypeSelector(IWContext iwc, String name, int refIndex) throws Exception {
		DropdownMenu menu = (DropdownMenu) getStyledInterface(new DropdownMenu(name));
		menu.addMenuElement(0, localize(KEY_COMPANY_TYPE_HEADER, "Välj bolagstyp"));
		Collection col = getRegulationsBusiness(iwc).findAllCompanyTypes();

		if(col != null){
			Iterator iter = col.iterator();
			while (iter.hasNext()) {
				CompanyType element = (CompanyType) iter.next();
				menu.addMenuElement("" + (((Integer) element.getPrimaryKey()).intValue()), 
										localize(element.getTextKey(), element.getTextKey()));
			}
			if (refIndex != -1) {
				menu.setSelectedElement(refIndex);
			}
		}
		return (DropdownMenu) menu;	
	}

	/*
	 * Generates a DropDownSelector for Commune belongings that is collected 
	 * from the regulation framework. 
	 *    
	 * @param iwc Idega Web Context 
	 * @param name HTML Parameter ID for this selector
	 * @param refIndex The initial position to set the selector to 
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.CommuneBelongingType
	 * @return the drop down menu
	 */
	private DropdownMenu communeBelongingSelector(IWContext iwc, String name, int refIndex) throws Exception {
		DropdownMenu menu = (DropdownMenu) getStyledInterface(new DropdownMenu(name));
		menu.addMenuElement(0, localize(KEY_COM_BEL_HEADER, "Välj kommuntillhörighet"));
		Collection col = getRegulationsBusiness(iwc).findAllCommuneBelongingTypes();

		if(col != null){
			Iterator iter = col.iterator();
			while (iter.hasNext()) {
				CommuneBelongingType element = (CommuneBelongingType) iter.next();
				menu.addMenuElement("" + (((Integer) element.getPrimaryKey()).intValue()), 
										localize(element.getTextKey(), element.getTextKey()));
			}
			if (refIndex != -1) {
				menu.setSelectedElement(refIndex);
			}
		}
		return (DropdownMenu) menu;	
	}


	private RegulationsBusiness getRegulationsBusiness(IWContext iwc) throws RemoteException {
		return (RegulationsBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, RegulationsBusiness.class);
	}
	private PostingBusiness getPostingBusiness(IWContext iwc) throws RemoteException {
		return (PostingBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, PostingBusiness.class);
	}
}
