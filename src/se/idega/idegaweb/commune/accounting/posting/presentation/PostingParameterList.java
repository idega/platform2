/*
 * $Id: PostingParameterList.java,v 1.33 2004/02/24 18:25:22 aron Exp $
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
import com.idega.core.builder.data.ICPage;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.HiddenInput;


import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;
import se.idega.idegaweb.commune.accounting.presentation.ListTable;
import se.idega.idegaweb.commune.accounting.presentation.ApplicationForm;
import se.idega.idegaweb.commune.accounting.presentation.ButtonPanel;
import se.idega.idegaweb.commune.accounting.presentation.OperationalFieldsMenu;
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
 * $Id: PostingParameterList.java,v 1.33 2004/02/24 18:25:22 aron Exp $
 *
 * @author <a href="http://www.lindman.se">Kjell Lindman</a>
 * @version $Revision: 1.33 $
 */
public class PostingParameterList extends AccountingBlock {

	private final static int ACTION_DEFAULT = 0;
//	private final static int ACTION_EDIT = 1;
	private final static int ACTION_DELETE = 1;
	
	private final static String KEY_HEADER = "posting_def_list.header";
	private final static String KEY_PERIOD = "posting_def_list.periode";
	private final static String KEY_PERIOD_SEARCH = "posting_def_list.periode_search";
	private final static String KEY_ACTIVITY = "posting_parm_list.activity";
	private final static String KEY_REG_SPEC = "posting_parm_list.reg_spec";
	private final static String KEY_STUDY_PATH = "posting_parm_list.study_path";
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
	private final static String KEY_SCHOOL_YEAR = "posting_parm_list.school_year";
	private final static String KEY_HEADER_OPERATION =  "posting_parm_list.operation_header";
	private final static String KEY_HEADER_SORT_BY =  "posting_parm_list.sort_by_header"; 

	private final static String BLANK = " ";

	private final static String PARAM_SEARCH = "button_search";
	private final static String PARAM_NEW = "button_new";

	private final static String PARAM_MODE_COPY = "mode_copy";
	private final static String PARAM_FROM = "param_from";
	private final static String PARAM_TO = "param_to";
	public final static String PARAM_RETURN_FROM_DATE = "return_from_date";
	public final static String PARAM_RETURN_TO_DATE = "return_to_date";
	private final static String PARAM_DELETE_ID = "param_delete_id";
	private final static String PARAM_EDIT_ID = "param_edit_id";

	private ICPage _editPage;
	private Date _currentFromDate;
	private Date _currentToDate;
	
	private String _currentOperation;

	/**
	 * Handles the property editPage
	 */
	public void setEditPage(ICPage page) {
		_editPage = page;
	}

	/**
	 * Handles the property editPage
	 */
	public ICPage getEditPage() {
		return _editPage;
	}


	/**
	 * Handles all of the blocks presentation.
	 * @param iwc user/session context 
	 */
	public void init(final IWContext iwc) {
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
		if (id != null) {
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
		setupDefaultDates(iwc);
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
		
		
		ListTable list = new ListTable(this, 12);

		list.setLocalizedHeader(KEY_PERIOD, "Period", 1);
		list.setLocalizedHeader(KEY_ACTIVITY, "Verksamhet", 2);
		list.setLocalizedHeader(KEY_REG_SPEC, "Regelspec. typ", 3);
		list.setLocalizedHeader(KEY_COMPANY_TYPE, "Bolagstyp", 4);
		list.setLocalizedHeader(KEY_COMMUNE_BELONGING, "Kommuntillhörighet", 5);
		list.setLocalizedHeader(KEY_SCHOOL_YEAR, "Skolår", 6);
		list.setLocalizedHeader(KEY_OWN_ENTRY, "Egen kontering", 7);
		list.setLocalizedHeader(KEY_DOUBLE_ENTRY, "Motkontering", 8);
		list.setLocalizedHeader(KEY_STUDY_PATH, "Studieväg", 9);
		list.setLocalizedHeader(KEY_EDIT, "", 10);
		list.setLocalizedHeader(KEY_COPY, "", 11);
		list.setLocalizedHeader(KEY_REMOVE, "", 12);

		String tod = iwc.isParameterSet(PARAM_TO) ? 
				iwc.getParameter(PARAM_TO) : iwc.getParameter(PARAM_RETURN_TO_DATE);  
		String fromd = iwc.isParameterSet(PARAM_FROM) ? 
				iwc.getParameter(PARAM_FROM) : iwc.getParameter(PARAM_RETURN_FROM_DATE);

		try {
			pBiz = getPostingBusiness(iwc);
			int accountLength = pBiz.getPostingFieldByDateAndFieldNo(_currentFromDate, 1);						
			//Collection items = pBiz.findPostingParametersByPeriod(_currentFromDate, _currentToDate);
			// aron 18.02.2004
			Collection items = pBiz.findPostingParametersByPeriod(_currentFromDate, _currentToDate,_currentOperation);
			if (items != null) {
				Iterator iter = items.iterator();
				while (iter.hasNext()) {
					PostingParameters p = (PostingParameters) iter.next();
					Link link = getLink(formatDate(p.getPeriodFrom(), 4) + "-" + formatDate(p.getPeriodTo(), 4),
										 PARAM_EDIT_ID, p.getPrimaryKey().toString());
					link.setPage(_editPage);
					link.addParameter(PARAM_RETURN_FROM_DATE, fromd);
					link.addParameter(PARAM_RETURN_TO_DATE, tod);
					list.add(link);

					if (p.getActivity() == null) {
						list.add(BLANK);					
					} else {
						list.add(p.getActivity().getName());
					}
					
					if (p.getRegSpecType() == null) {
						list.add(BLANK);					
					} else {
						list.add(p.getRegSpecType().getLocalizationKey(), 
								p.getRegSpecType().getLocalizationKey());
					}
					
					if (p.getCompanyType() == null) {
						list.add(BLANK);					
					} else {
						list.add(p.getCompanyType().getLocalizedKey(), 
								p.getCompanyType().getLocalizedKey());
					}
					
					if (p.getCommuneBelonging() == null) {
						list.add(BLANK);					
					} else {
						list.add(p.getCommuneBelonging().getLocalizationKey(), 
								p.getCommuneBelonging().getLocalizationKey());
					}
					
					if (p.getSchoolYear1() != null && p.getSchoolYear2() != null) {
						list.add(p.getSchoolYear1().getSchoolYearName() + "-" + p.getSchoolYear2().getSchoolYearName()); 
					} else {
						list.add(BLANK);					
					}
					list.add(p.getPostingString().substring(0, accountLength));
					list.add(p.getDoublePostingString().substring(0, accountLength));

					if (p.getStudyPath() == null) {
						list.add(BLANK);					
					} else {
						list.add(p.getStudyPath().getCode());
					}

					Link edit = new Link(getEditIcon(localize(KEY_BUTTON_EDIT, "Redigera")));
					edit.addParameter(PARAM_EDIT_ID, p.getPrimaryKey().toString());
					edit.addParameter(PARAM_RETURN_FROM_DATE, fromd);
					edit.addParameter(PARAM_RETURN_TO_DATE, tod);
					edit.setPage(_editPage);
					list.add(edit);
					
					Link copy = new Link(getCopyIcon(localize(KEY_BUTTON_COPY, "Kopiera")));
					copy.addParameter(PARAM_EDIT_ID, p.getPrimaryKey().toString());
					copy.addParameter(PARAM_MODE_COPY, "1");
					edit.addParameter(PARAM_RETURN_FROM_DATE, fromd);
					edit.addParameter(PARAM_RETURN_TO_DATE, tod);
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
		
		table.add(getLocalizedLabel(KEY_HEADER_OPERATION, "Huvudverksamhet"), 1, 1);
		table.add(new OperationalFieldsMenu(), 2, 1);
		//table.add(getLocalizedLabel(KEY_HEADER_SORT_BY, "Sortera pa"), 3, 1);
		
		table.add(getLocalizedLabel(KEY_PERIOD_SEARCH, "Period"), 1, 2);
		table.add(getFromToDatePanel(PARAM_FROM, _currentFromDate, PARAM_TO, _currentToDate), 2, 2);
		table.add(getLocalizedButton(PARAM_SEARCH, KEY_SEARCH, "Sök"), 3, 2);
		table.add(new HiddenInput(PARAM_DELETE_ID, ""));

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


	private void setupDefaultDates(IWContext iwc) { 
		try {
			_currentOperation = getSession().getOperationalField();
			_currentOperation = _currentOperation == null ? "" : _currentOperation;
		} catch (RemoteException e) {}
		
		
		
		Date sessionFromDate = null;
		Date sessionToDate = null;
		try {
			sessionFromDate = (Date) getSession().getUserContext().getSessionAttribute(PARAM_FROM);
			sessionToDate = (Date) getSession().getUserContext().getSessionAttribute(PARAM_TO);
		} catch (Exception e) {}

		if (iwc.isParameterSet(PARAM_FROM)) {
			_currentFromDate = parseDate(iwc.getParameter(PARAM_FROM));
		} else {
			if (iwc.isParameterSet(PARAM_RETURN_FROM_DATE)) {
				_currentFromDate = parseDate(iwc.getParameter(PARAM_RETURN_FROM_DATE));
			}
			if (_currentFromDate == null) {
				if (sessionFromDate != null) {
					_currentFromDate = sessionFromDate;				
				} else {
					_currentFromDate = getFlattenedTodaysDate();
				}
			}
		}
			
		if (iwc.isParameterSet(PARAM_TO)) {
			_currentToDate = parseDate(iwc.getParameter(PARAM_TO));
		} else {
			if (iwc.isParameterSet(PARAM_RETURN_TO_DATE)) {
				_currentToDate = parseDate(iwc.getParameter(PARAM_RETURN_TO_DATE));
			}
			if (_currentToDate == null) {
				if (sessionToDate != null) {
					_currentToDate = sessionToDate;				
				} else {
					_currentToDate = parseDate("9999-12-31");
				}
			}
		}
			
		if(_currentToDate == null) {
			_currentToDate = parseDate("9999-12-31");
		}
		if(_currentFromDate == null) {
			_currentFromDate = getFlattenedTodaysDate();
		}
		_currentFromDate = parseDate(formatDate(_currentFromDate, 4));
		_currentToDate = parseDate(formatDate(_currentToDate, 4));
		
		try {
			getSession().getUserContext().setSessionAttribute(PARAM_FROM, _currentFromDate);
			getSession().getUserContext().setSessionAttribute(PARAM_TO, _currentToDate);
		} catch (Exception e) {}
	}
	
	private Date getFlattenedTodaysDate() {
		Date date = new Date(System.currentTimeMillis());
		String dd = formatDate(date, 4);
		date = parseDate(dd.substring(0,2)+"01");
		return date;
	}


	private PostingBusiness getPostingBusiness(IWContext iwc) throws RemoteException {
		return (PostingBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, PostingBusiness.class);
	}
}

