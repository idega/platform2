/*
 * $Id: PostingParameterList.java,v 1.17 2003/09/08 08:10:07 laddi Exp $
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

import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.builder.data.IBPage;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.HiddenInput;


import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;
import se.idega.idegaweb.commune.accounting.presentation.ListTable;
import se.idega.idegaweb.commune.accounting.presentation.ApplicationForm;
import se.idega.idegaweb.commune.accounting.presentation.ButtonPanel;
import se.idega.idegaweb.commune.accounting.posting.business.PostingBusiness;
import se.idega.idegaweb.commune.accounting.posting.data.PostingParameters;


/**
 * PostingParameterList is an idegaWeb block that handles maintenance of some default data (PostingParameters) 
 * that is used in a "posting". The block shows/edits a list of Periode, Activity, Regulation specs, 
 * Company types and Commune belonging. 
 * This list shows only the most essential values of the PostingParameter.
 * 
 * Other submodules will use this data to search for a match on 
 * Periode, Activity, Regulation sec, Company type and Commune belonging.
 * When you have a hit you can retrive accounting data such as accounts, resources, activity codes 
 * etc. For PostingParameters only fields Account, Resources, Activity and double entry account
 * can be retrieved 
 * 
 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParameters;
 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingString;
 * <p>
 * $Id: PostingParameterList.java,v 1.17 2003/09/08 08:10:07 laddi Exp $
 *
 * @author <a href="http://www.lindman.se">Kjell Lindman</a>
 * @version $Revision: 1.17 $
 */
public class PostingParameterList extends AccountingBlock {

	private final static int ACTION_DEFAULT = 0;
//	private final static int ACTION_EDIT = 1;
	private final static int ACTION_DELETE = 1;
	
	private final static String KEY_HEADER = "posting_def_list.header";
	private final static String KEY_PERIOD = "posting_def_list.periode";
	private final static String KEY_PERIOD_SEARCH = "posting_def_list.periode_search";
//	private final static String KEY_FROM_DATE = "posting_parm_list.from_date";
//	private final static String KEY_TO_DATE = "posting_parm_list.to_date";
	private final static String KEY_ACTIVITY = "posting_parm_list.activity";
	private final static String KEY_REG_SPEC = "posting_parm_list.reg_spec";
	private final static String KEY_COMPANY_TYPE = "posting_parm_list.company_type";
	private final static String KEY_COMMUNE_BELONGING = "posting_parm_list.commune_belonging";
	private final static String KEY_OWN_ENTRY = "posting_parm_list.own_entry";
	private final static String KEY_DOUBLE_ENTRY = "posting_parm_list.double_entry";
	private final static String KEY_SEARCH = "posting_parm_list.search";
	private final static String KEY_COPY = "posting_parm_list.copy";
	private final static String KEY_EDIT = "posting_parm_list.edit";
	private final static String KEY_BUTTON_COPY = "posting_parm_list.button_copy";
	private final static String KEY_BUTTON_EDIT = "posting_parm_list.button_edit";
	private final static String KEY_NEW = "posting_parm_list.new";
	private final static String KEY_REMOVE = "posting_parm_list.remove";
	private final static String KEY_REMOVE_CONFIRM = "posting_parm_list.remove_confirm";
	private final static String KEY_CLICK_REMOVE = "posting_parm_list.click_to_remove";
//	private final static String KEY_CLICK_COPY = "posting_parm_list.click_to_copy";
//	private final static String KEY_CLICK_EDIT = "posting_parm_list.click_to_edit";
//	private final static String KEY_CANCEL = "posting_parm_list.cancel";
//	private final static String KEY_CONFIRM_REMOVE_MESSAGE = "posting_parm_list.confirm_remove_message";
	private final static String KEY_DEFAULT_BLANK = "posting_parm_list.blank";

	private final static String PARAM_SEARCH = "button_search";
//	private final static String PARAM_COPY = "button_copy";
	private final static String PARAM_NEW = "button_new";
//	private final static String PARAM_REMOVE = "button_remove";

	private final static String PARAM_MODE_COPY = "mode_copy";
//	private final static String PARAM_CHECKED_FOR_DELETE = "param_checked_for_delete";
	private final static String PARAM_FROM = "param_from";
	private final static String PARAM_TO = "param_to";
//	private final static String PARAM_POSTING_ID = "param_posting_id";
	private final static String PARAM_DELETE_ID = "param_delete_id";
	private final static String PARAM_EDIT_ID = "param_edit_id";

	private IBPage _editPage;
	private Date _currentFromDate;
	private Date _currentToDate;

	/**
	 * Handles the property editPage
	 */
	public void setEditPage(IBPage page) {
		_editPage = page;
	}

	/**
	 * Handles the property editPage
	 */
	public IBPage getEditPage() {
		return _editPage;
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
					viewForm(iwc);
					break;
				case ACTION_DELETE :
					deletePost(iwc);
					viewForm(iwc);
					break;
			}
		} catch (Exception e) {
			super.add(new ExceptionWrapper(e, this));
		}
	}

	/*
	 * Returns the action constant for the action to perform based 
	 * on the POST parameters in the specified context.
	 */
	private int parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAM_DELETE_ID)) {
			return ACTION_DELETE;
		}
		return ACTION_DEFAULT;
	}

	/*
	 * Delete posts marked with the checkbox 
	 */
	private void deletePost(IWContext iwc) {
		String id = iwc.getParameter(PARAM_DELETE_ID);
		if(id != null) {
			try {
				getPostingBusiness(iwc).deletePostingParameter(Integer.parseInt(id));
			} catch ( Exception e) {
				super.add(new ExceptionWrapper(e, this));
			}
		}
	}

	/*
	 * Adds the default form to the block.
	 */	
	private void viewForm(IWContext iwc) {
		ApplicationForm app = new ApplicationForm(this);
		
		if (iwc.isParameterSet(PARAM_FROM)) {
			_currentFromDate = parseDate(iwc.getParameter(PARAM_FROM));
		} else{
			_currentFromDate = parseDate("2001-01-01");
		}
		
		if (iwc.isParameterSet(PARAM_TO)) {
			_currentToDate = parseDate(iwc.getParameter(PARAM_TO));
		} else {
			_currentToDate = parseDate("2009-01-01");
		}

		app.setLocalizedTitle(KEY_HEADER, "Konteringlista");
		app.setSearchPanel(getSearchPanel());
		app.setMainPanel(getPostingTable(iwc));
		app.setButtonPanel(getButtonPanel());
		add(app);		
	}

	private ButtonPanel getButtonPanel() {
		ButtonPanel buttonPanel = new ButtonPanel(this);
		buttonPanel.addLocalizedButton(PARAM_NEW, KEY_NEW, "Ny", _editPage);
		return buttonPanel;
	}
	
	/*
	 * Returns the PostingList
	 */
	private ListTable getPostingTable(IWContext iwc) {

		PostingBusiness pBiz;
		
		
		ListTable list = new ListTable(this, 10);

		list.setLocalizedHeader(KEY_PERIOD, "Period", 1);
		list.setLocalizedHeader(KEY_ACTIVITY, "Verksamhet", 2);
		list.setLocalizedHeader(KEY_REG_SPEC, "Regelspec. typ", 3);
		list.setLocalizedHeader(KEY_COMPANY_TYPE, "Bolagstyp", 4);
		list.setLocalizedHeader(KEY_COMMUNE_BELONGING, "Kommuntillhörighet", 5);
		list.setLocalizedHeader(KEY_OWN_ENTRY, "Egen kontering", 6);
		list.setLocalizedHeader(KEY_DOUBLE_ENTRY, "Motkontering", 7);
		list.setLocalizedHeader(KEY_EDIT, "", 8);
		list.setLocalizedHeader(KEY_COPY, "", 9);
		list.setLocalizedHeader(KEY_REMOVE, "", 10);
		
		try {
			pBiz = getPostingBusiness(iwc);
			Collection items = pBiz.findPostingParametersByPeriode(_currentFromDate, _currentToDate);
			if(items != null) {
				Iterator iter = items.iterator();
				while (iter.hasNext()) {
					PostingParameters p = (PostingParameters) iter.next();
					Link link = getLink(formatDate(p.getPeriodeFrom(), 4) + "-" + formatDate(p.getPeriodeTo(), 4),
										 PARAM_EDIT_ID, p.getPrimaryKey().toString());
					
					link.setPage(_editPage);
					list.add(link);

					if(p.getActivity() == null) {
						list.add(KEY_DEFAULT_BLANK, "");					
					} else {
						list.add(p.getActivity().getLocalizationKey(), 
								p.getActivity().getLocalizationKey());
					}
					if(p.getRegSpecType() == null) {
						list.add(KEY_DEFAULT_BLANK, "");					
					} else {
						list.add(p.getRegSpecType().getLocalizationKey(), 
								p.getRegSpecType().getLocalizationKey());
					}
					if(p.getCompanyType() == null) {
						list.add(KEY_DEFAULT_BLANK, "");					
					} else {
						list.add(p.getCompanyType().getLocalizedKey(), 
								p.getCompanyType().getLocalizedKey());
					}
					if(p.getCommuneBelonging() == null) {
						list.add(KEY_DEFAULT_BLANK, "");					
					} else {
						list.add(p.getCommuneBelonging().getLocalizationKey(), 
								p.getCommuneBelonging().getLocalizationKey());
					}
		

					list.add(p.getPostingString().substring(0, 7));
					list.add(p.getDoublePostingString().substring(0, 7));


					Link edit = new Link(getEditIcon(localize(KEY_BUTTON_EDIT, "Redigera")));
					edit.addParameter(PARAM_EDIT_ID, p.getPrimaryKey().toString());
					edit.setPage(_editPage);
					list.add(edit);

					Link copy = new Link(getCopyIcon(localize(KEY_BUTTON_COPY, "Kopiera")));
					copy.addParameter(PARAM_EDIT_ID, p.getPrimaryKey().toString());
					copy.addParameter(PARAM_MODE_COPY, "1");
					copy.setPage(_editPage);
					list.add(copy);

					SubmitButton delete = new SubmitButton(getDeleteIcon(localize(KEY_REMOVE, "Radera")));
					delete.setDescription(localize(KEY_CLICK_REMOVE, "Klicka här för att radera post"));
					delete.setValueOnClick(PARAM_DELETE_ID, p.getPrimaryKey().toString());
					delete.setSubmitConfirm(localize(KEY_REMOVE_CONFIRM, "Vill du verkligen radera denna post?"));
					list.add(delete);
					
				}
			}			
		} catch (Exception e) {
			super.add(new ExceptionWrapper(e, this));
		}	
		
		return list;
	}
	
	private Table getSearchPanel() {
		Table table = new Table();
		table.setColumnAlignment(1, Table.HORIZONTAL_ALIGN_LEFT);
		table.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_CENTER);
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());

		table.add(getLocalizedLabel(KEY_PERIOD_SEARCH, "Period"), 1, 1);
		table.add(getFromToDatePanel(PARAM_FROM, _currentFromDate, PARAM_TO, _currentToDate), 2, 1);
		table.add(getLocalizedButton(PARAM_SEARCH, KEY_SEARCH, "Sök"), 3, 1);
		table.add(new HiddenInput(PARAM_DELETE_ID, "-1"));

		return table;
	}

	private Table getFromToDatePanel(String param_from, Date date_from, String param_to, Date date_to) {
		Table table = new Table();
		TextInput fromDate = getTextInput(param_from, formatDate(date_from, 4),  40, 4);
		TextInput toDate = getTextInput(param_to, formatDate(date_to, 4),  40, 4);
		fromDate.setLength(4);
		toDate.setLength(4);
		table.add(fromDate, 1, 1);
		table.add(getText("-"), 2, 1);
		table.add(toDate, 3, 1);
		return table;
	}

	private PostingBusiness getPostingBusiness(IWContext iwc) throws RemoteException {
		return (PostingBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, PostingBusiness.class);
	}
}

