/*
 * $Id: PostingParameterListEditor.java,v 1.14 2003/08/28 12:55:34 kjell Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.posting.presentation;

import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;

import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.builder.business.BuilderLogic;
import com.idega.builder.data.IBPage;
import com.idega.user.data.User;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ExceptionWrapper;
import com.idega.util.IWTimestamp;

import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;
import se.idega.idegaweb.commune.accounting.presentation.ListTable;
import se.idega.idegaweb.commune.accounting.presentation.ApplicationForm;
import se.idega.idegaweb.commune.accounting.presentation.ButtonPanel;
import se.idega.idegaweb.commune.accounting.posting.data.PostingParameters;
import se.idega.idegaweb.commune.accounting.posting.data.PostingField;
import se.idega.idegaweb.commune.accounting.regulations.business.RegulationsBusiness;
import se.idega.idegaweb.commune.accounting.posting.business.PostingBusiness;
import se.idega.idegaweb.commune.accounting.posting.business.PostingParametersException;


/**
 * PostingParameterListEdit is an idegaWeb block that handles maintenance of some 
 * default data thatis used in a "posting". The block shows/edits Periode, Activity, Regulation specs, 
 * Company types and Commune belonging. 
 * It handles posting variables for both own and double entry accounting
 *  
 * <p>
 * $Id: PostingParameterListEditor.java,v 1.14 2003/08/28 12:55:34 kjell Exp $
 *
 * @author <a href="http://www.lindman.se">Kjell Lindman</a>
 * @version $Revision: 1.14 $
 */
public class PostingParameterListEditor extends AccountingBlock {

	private final static int ACTION_DEFAULT = 0;
	private final static int ACTION_EDIT = 1;
	private final static int ACTION_SAVE = 2;
	private final static int ACTION_CANCEL = 3;

	private final static String KEY_SAVE = "posting_parm_edit.save";
	private final static String KEY_CANCEL = "posting_parm_edit.cancel";

	private final static String KEY_ACTIVITY_HEADER = "posting_parm_edit.activity_header";
	private final static String KEY_REGSPEC_HEADER = "regulation_specification_header";
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

	private final static String PARAM_BUTTON_SAVE = "button_save";
	private final static String PARAM_BUTTON_CANCEL = "button_cancel";
	
	private final static String PARAM_POSTING_ID = "pp_edit_posting_id";
	private final static String PARAM_EDIT_ID = "param_edit_id";
	private final static String PARAM_PERIODE_FROM = "pp_edit_periode_from";
	private final static String PARAM_PERIODE_TO = "pp_edit_periode_to";
	private final static String PARAM_SIGNED = "pp_edit_signed";
	private final static String PARAM_MODE_COPY = "mode_copy";

	private final static String PARAM_OWN_STRING = "own_string";
	private final static String PARAM_DOUBLE_STRING = "double_string";

	private final static String PARAM_SELECTOR_ACTIVITY = "selector_activity";
	private final static String PARAM_SELECTOR_REGSPEC = "selector_regspec";
	private final static String PARAM_SELECTOR_COMPANY_TYPE = "selector_company_type";
	private final static String PARAM_SELECTOR_COM_BELONGING = "selector_com_belonging";

	private IBPage _responsePage;
	private String _errorText = "";
	private String _theOwnString = "";
	private String _theDoubleString = "";
	
	public void setResponsePage(IBPage page) {
		_responsePage = page;
	}

	public IBPage getResponsePage() {
		return _responsePage;
	}
	
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
					viewMainForm(iwc, "");
					break;
				case ACTION_SAVE :
					if(!saveData(iwc)) {
						viewMainForm(iwc, _errorText);
					}
					break;
				case ACTION_CANCEL :
					closeMe(iwc);
					break;
			}
		}
		catch (Exception e) {
			super.add(new ExceptionWrapper(e, this));
		}
	}
		 
	private boolean saveData(IWContext iwc) {
		
			try {

				String id = null;
				if(iwc.isParameterSet(PARAM_EDIT_ID)) {
					id = iwc.getParameter(PARAM_EDIT_ID);
				}
				if(iwc.isParameterSet(PARAM_MODE_COPY)) {
					id = null;
				}
				getPostingBusiness(iwc).savePostingParameter(id,
				(Date) parseDate(iwc.getParameter(PARAM_PERIODE_FROM)),
				(Date) parseDate(iwc.getParameter(PARAM_PERIODE_TO)),
				iwc.getParameter(PARAM_SIGNED),
				iwc.getParameter(PARAM_SELECTOR_ACTIVITY),				
				iwc.getParameter(PARAM_SELECTOR_REGSPEC),					
				iwc.getParameter(PARAM_SELECTOR_COMPANY_TYPE),					
				iwc.getParameter(PARAM_SELECTOR_COM_BELONGING),
				iwc.getParameter(_theOwnString),
				iwc.getParameter(_theDoubleString)
				);
			} catch (PostingParametersException e) {
				_errorText = localize(e.getTextKey(), e.getDefaultText());
				return false;
			} catch (RemoteException e) {
				super.add(new ExceptionWrapper(e, this));
				return false;
			}
			
			closeMe(iwc);
			return true;
	}

	private void closeMe(IWContext iwc) {
		getParentPage().setToRedirect(BuilderLogic.getInstance().getIBPageURL(iwc, _responsePage.getID()));
	}
	 
	/*
	 * Returns the action constant for the action to perform based 
	 * on the POST parameters in the specified context.
	 */
	private int parseAction(IWContext iwc) {
		int action = ACTION_DEFAULT;
		if (iwc.isParameterSet(PARAM_BUTTON_SAVE)) {
		  action = ACTION_SAVE;
		}
		if (iwc.isParameterSet(PARAM_BUTTON_CANCEL)) {
		  action = ACTION_CANCEL;
		}
		return action;
	}
	/*
	 * Adds the default form to the block.
    */	
	private void viewMainForm(IWContext iwc, String error) {
		ApplicationForm app = new ApplicationForm(this);
		PostingParameters pp = getThisPostingParameter(iwc);
		
		Table topPanel = getTopPanel(iwc, pp);		
		Table postingForm = getPostingForm(iwc, pp);
					
		ButtonPanel buttonPanel = new ButtonPanel(this);
		buttonPanel.addLocalizedButton(PARAM_BUTTON_SAVE, KEY_SAVE, "Spara");
		buttonPanel.addLocalizedButton(PARAM_BUTTON_CANCEL, KEY_CANCEL, "Avbryt");
		
		app.setLocalizedTitle(KEY_HEADER, "Skapa/Ändra konteringlista");
		app.setSearchPanel(topPanel);
		app.setMainPanel(postingForm);
		app.setButtonPanel(buttonPanel);
		add(app);		
		if(error.length() != 0) {
			add(getSmallErrorText(error));
		}
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
		User user = iwc.getCurrentUser();
		String userName = "";
		if(user != null) {
			userName = user.getFirstName();
		}
		Timestamp rightNow = IWTimestamp.getTimestampRightNow();
		Date dd = new Date(System.currentTimeMillis());
		
		table.add(getLocalizedLabel(KEY_FROM_DATE, "Från datum"),1 ,1);
		table.add(getTextInput(PARAM_PERIODE_FROM, (formatDate(pp != null ? pp.getPeriodeFrom() : dd, 4)), 40, 4), 2, 1);
	
		table.add(getLocalizedLabel(KEY_TO_DATE, "Tom datum"),3 ,1);
		table.add(getTextInput(PARAM_PERIODE_TO, (formatDate(pp != null ? pp.getPeriodeTo() : dd, 4)), 40, 4), 4, 1);

		table.add(getLocalizedLabel(KEY_CHANGE_DATE, "Ändringsdatum"),1 ,2);
		table.add(getText(formatDate(pp != null ? pp.getChangedDate(): rightNow, 6)), 2, 2);

		table.add(getLocalizedLabel(KEY_CHANGE_SIGN, "Ändringssignatur"),3 ,2);
		table.add(""+userName, 4, 2);
		table.add(new HiddenInput(PARAM_SIGNED, ""+userName));
		if(iwc.isParameterSet(PARAM_MODE_COPY)) {
			table.add(new HiddenInput(PARAM_MODE_COPY, ""+iwc.getParameter(PARAM_MODE_COPY)));
		}
		if(iwc.isParameterSet(PARAM_EDIT_ID)) {
			table.add(new HiddenInput(PARAM_EDIT_ID, ""+iwc.getParameter(PARAM_EDIT_ID)));
		}
		return table;	
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
		ListTable list1 = null;
		ListTable list2 = null;
		try {
			int index = 1;
			Collection fields = getPostingBusiness(iwc).getAllPostingFieldsByDate(pp.getPeriodeFrom());
			int size = fields.size();
			
			list1 = new ListTable(this, size);
			list2 = new ListTable(this, size);
			Iterator iter = fields.iterator();
			while (iter.hasNext()) {
				PostingField field = (PostingField) iter.next();
				list1.setHeader(field.getFieldTitle(), index);
				list2.setHeader(field.getFieldTitle(), index);
				list1.add(getTextInput(PARAM_OWN_STRING+"_"+index, getFieldData(field, pp.getPostingString()), 60, field.getLen()));
				list1.add(getTextInput(PARAM_DOUBLE_STRING+"_"+index, getFieldData(field, pp.getDoublePostingString()), 60, field.getLen()));
				index++;
			}
			
		} catch (RemoteException e) {
		}


		/*
		list1.add(getTextInput(PARAM_ACCOUNT, pp != null ? pp.getPostingAccount() : "", 60, 6));
		list1.add(getTextInput(PARAM_LIABILITY, pp != null ? pp.getPostingLiability() : "", 60, 10));
		list1.add(getTextInput(PARAM_RESOURCE, pp != null ? pp.getPostingResource() : "", 60, 6));
		list1.add(getTextInput(PARAM_ACTIVITY_CODE, pp != null ? pp.getPostingActivityCode() : "", 60, 4));
		list1.add(getTextInput(PARAM_DOUBLE_ENTRY_CODE, pp != null ? pp.getPostingDoubleEntry() : "", 60, 6));
		list1.add(getTextInput(PARAM_ACTIVITY_FIELD, pp != null ? pp.getPostingActivity() : "", 80, 20));
		list1.add(getTextInput(PARAM_PROJECT, pp != null ? pp.getPostingProject() : "", 80, 20));
		list1.add(getTextInput(PARAM_OBJECT, pp != null ? pp.getPostingObject() : "", 80, 20));

		list2.add(getTextInput(PARAM_DOUBLE_ACCOUNT, pp != null ? pp.getDoublePostingAccount() : "", 60, 6));
		list2.add(getTextInput(PARAM_DOUBLE_LIABILITY, pp != null ? pp.getDoublePostingLiability() : "", 60, 10));
		list2.add(getTextInput(PARAM_DOUBLE_RESOURCE, pp != null ? pp.getDoublePostingResource() : "", 60, 6));
		list2.add(getTextInput(PARAM_DOUBLE_ACTIVITY_CODE, pp != null ? pp.getDoublePostingActivityCode() : "", 60, 4));
		list2.add(getTextInput(PARAM_DOUBLE_DOUBLE_ENTRY_CODE, pp != null ? pp.getDoublePostingDoubleEntry() : "", 60, 6));
		list2.add(getTextInput(PARAM_DOUBLE_ACTIVITY_FIELD, pp != null ? pp.getDoublePostingActivity() : "", 80, 20));
		list2.add(getTextInput(PARAM_DOUBLE_PROJECT, pp != null ? pp.getDoublePostingProject() : "", 80, 20));
		list2.add(getTextInput(PARAM_DOUBLE_OBJECT, pp != null ? pp.getDoublePostingObject() : "", 80, 20));
*/
		try {
			selectors.add(getLocalizedLabel(KEY_ACTIVITY, "Verksamhet"), 1, 1);
			selectors.add(activitySelector(iwc, PARAM_SELECTOR_ACTIVITY, 
				Integer.parseInt(pp != null ? pp.getActivity().getPrimaryKey().toString() : "0")), 2, 1);
						
			selectors.add(getLocalizedLabel(KEY_REG_SPEC, "Regelspec.typ"), 1, 2);
			selectors.add(regSpecSelector(iwc, PARAM_SELECTOR_REGSPEC, 
											Integer.parseInt(pp != null ? 
											pp.getRegSpecType().getPrimaryKey().toString() : "0")), 2, 2);
	
			selectors.add(getLocalizedLabel(KEY_COMPANY_TYPE, "Bolagstyp"), 1, 3);
			selectors.add(companyTypeSelector(iwc, PARAM_SELECTOR_COMPANY_TYPE, 
											Integer.parseInt(pp != null ? 
											pp.getCompanyType().getPrimaryKey().toString() : "0")), 2, 3);
	
			selectors.add(getLocalizedLabel(KEY_COMMUNE_BELONGING, "Kommuntillhörighet:"), 1, 4);
			selectors.add(communeBelongingSelector(iwc, PARAM_SELECTOR_COM_BELONGING, 
											Integer.parseInt(pp != null ? 
											pp.getCommuneBelonging().getPrimaryKey().toString() : "0")), 2, 4);
		} catch (Exception e) {
			super.add(new ExceptionWrapper(e, this));
		}	

		accounts.add(getLocalizedText(KEY_OWN_ENTRY, "Egen kontering"), 1, 1);
		accounts.add(list1, 1, 2);
		accounts.add(getLocalizedText(KEY_DOUBLE_ENTRY, "Mot kontering"), 1, 3);
		accounts.add(list2, 1, 4);
		
		table.add(getLocalizedLabel(KEY_CONDITIONS, "Villkor"), 1, 1);
		table.add(selectors, 1, 2);
		table.add(accounts, 1, 3);
		
		return table;
	}


	private String getFieldData(PostingField pf, String s ) {
		// dummy
		return "";	
	}


	/*
	 * Retrives from business the current posting data that is pointed out by PARAM_EDIT_ID.
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
			
			if(iwc.isParameterSet(PARAM_EDIT_ID)) {
				postingID = Integer.parseInt(iwc.getParameter(PARAM_EDIT_ID));
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
		DropdownMenu menu = (DropdownMenu) getStyledInterface(
					getDropdownMenuLocalized(name, getRegulationsBusiness(iwc).findAllActivityTypes(), 
					"getTextKey"));
		menu.addMenuElementFirst("0", localize(KEY_ACTIVITY_HEADER, "Välj Aktivitet"));
		menu.setSelectedElement(refIndex);
		return menu;
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
		DropdownMenu menu = (DropdownMenu) getStyledInterface(
					getDropdownMenuLocalized(name, getRegulationsBusiness(iwc).findAllRegulationSpecTypes(), 
					"getTextKey"));
		menu.addMenuElementFirst("0", localize(KEY_REGSPEC_HEADER, "Välj Regelspec. typ"));
		menu.setSelectedElement(refIndex);
		return menu;
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
			DropdownMenu menu = (DropdownMenu) getStyledInterface(
					getDropdownMenuLocalized(name, getRegulationsBusiness(iwc).findAllCompanyTypes(), 
					"getTextKey"));
		menu.addMenuElementFirst("0", localize(KEY_COMPANY_TYPE_HEADER, "Välj Bolagstyp"));
		menu.setSelectedElement(refIndex);
		return menu;
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
		DropdownMenu menu = (DropdownMenu) getStyledInterface(
				getDropdownMenuLocalized(name, getRegulationsBusiness(iwc).findAllCommuneBelongingTypes(), 
				"getTextKey"));
		menu.addMenuElementFirst("0", localize(KEY_COM_BEL_HEADER, "Välj Kommuntillhörighet"));
		menu.setSelectedElement(refIndex);
		return menu;
	}

	private RegulationsBusiness getRegulationsBusiness(IWContext iwc) throws RemoteException {
		return (RegulationsBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, RegulationsBusiness.class);
	}
	private PostingBusiness getPostingBusiness(IWContext iwc) throws RemoteException {
		return (PostingBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, PostingBusiness.class);
	}
}
