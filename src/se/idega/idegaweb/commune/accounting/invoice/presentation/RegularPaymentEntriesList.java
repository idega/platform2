/*
 * Created on 24.9.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.accounting.invoice.presentation;

import is.idega.idegaweb.member.presentation.UserSearcher;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import se.idega.idegaweb.commune.accounting.business.AccountingUtil;
import se.idega.idegaweb.commune.accounting.invoice.business.RegularPaymentBusiness;
import se.idega.idegaweb.commune.accounting.invoice.data.RegularPaymentEntry;
import se.idega.idegaweb.commune.accounting.invoice.data.RegularPaymentEntryHome;
import se.idega.idegaweb.commune.accounting.posting.business.PostingException;
import se.idega.idegaweb.commune.accounting.posting.business.PostingParametersException;
import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;
import se.idega.idegaweb.commune.accounting.presentation.ButtonPanel;
import se.idega.idegaweb.commune.accounting.presentation.ListTable;
import se.idega.idegaweb.commune.accounting.presentation.OperationalFieldsMenu;
import se.idega.idegaweb.commune.accounting.presentation.RegulationSearchPanel;
import se.idega.idegaweb.commune.accounting.regulations.business.RegulationsBusiness;
import se.idega.idegaweb.commune.accounting.regulations.business.VATBusiness;
import se.idega.idegaweb.commune.accounting.regulations.business.VATException;
import se.idega.idegaweb.commune.accounting.regulations.data.Regulation;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationHome;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType;
import se.idega.idegaweb.commune.accounting.school.presentation.PostingBlock;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolHome;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolTypeHome;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.data.IDOEntityDefinition;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDOStoreException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Parameter;
import com.idega.presentation.ui.SelectOption;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * @author Roar
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class RegularPaymentEntriesList extends AccountingBlock {

	private String ERROR_PLACING_EMPTY = "error_placing_empty";
	private String ERROR_DATE_FORMAT = "error_date_form";
	private String ERROR_DATE_PERIODE_NEGATIVE = "error_date_periode_negative";
	private String ERROR_POSTING = "error_posting";
	private String ERROR_OWNPOSTING_EMPTY = "error_ownposting_empty";
	private String ERROR_AMOUNT_FORMAT = "error_amount_format";
	private String ERROR_PLACING_NULL = "error_placing_null";
	private String ERROR_NO_USER_SESSION = "error_no_user_session";
	

	private static String LOCALIZER_PREFIX = "regular_payment_entries_list.";
	
	private static final String KEY_OPERATIONAL_FIELD = "operational_field";
	private static final String KEY_AMOUNT_PR_MONTH = "amount_pr_month";
	private static final String KEY_AMOUNT = "amount";
	private static final String KEY_CANCEL = "cancel";
	private static final String KEY_DOUBLE_ENTRY_ACCOUNT = "double_entry_account";
	private static final String KEY_FROM = "from";
	private static final String KEY_PAYMENT_PERIODE = "payment_periode";
	private static final String KEY_NEW = "new";
	private static final String KEY_EDIT_TOOLTIP = "edit";	
	private static final String KEY_DELETE_TOOLTIP = "delete";		
	private static final String KEY_NOTE = "note";

	private static final String KEY_OWN_POSTING = "own_posting";
	private static final String KEY_PERIODE = "periode";
	private static final String KEY_PLACING = "placing";
	private static final String KEY_REGULATION_TYPE = "regulation_type";
	private static final String KEY_REMARK = "remark";
	private static final String KEY_SAVE = "save";
	private static final String KEY_SEARCH = "search";
	private static final String KEY_TO = "to";
	private static final String KEY_VAT_PR_MONTH = "vat_pr_month";
	private static final String KEY_VAT_TYPE = "vattype";
	private static final String KEY_PROVIDER = "provider";
	private static final String KEY_EDIT = "edit";
	private static final String KEY_DELETE = "delete";	
	private static final String KEY_SCH_TYPE = "school_type";	
	
	private static final String KEY_DAY_CREATED = "day_created";
	private static final String KEY_DAY_REGULATED = "day_regulated";	
	private static final String KEY_SIGNATURE = "signature";	
	
	private static final String KEY_SELECT="select";
	
	
	private static final String PAR_AMOUNT_PR_MONTH = KEY_AMOUNT_PR_MONTH;
	private static final String PAR_DOUBLE_ENTRY_ACCOUNT  = KEY_DOUBLE_ENTRY_ACCOUNT;
	private static final String PAR_FROM = KEY_FROM;
	private static final String PAR_SEEK_FROM = "SEEK_" + KEY_FROM;	
	private static final String PAR_PLACING = KEY_PLACING;
	private static final String PAR_REGULATION_TYPE = KEY_REGULATION_TYPE;
	private static final String PAR_REMARK = KEY_REMARK;
	private static final String PAR_TO = KEY_TO;
	private static final String PAR_SEEK_TO = "SEEK_" + KEY_TO;	
	private static final String PAR_USER_SSN = "usrch_search_pid"; //Constant used in UserSearcher...
	private static final String PAR_OWN_POSTING = KEY_OWN_POSTING;	
	private static final String PAR_VAT_PR_MONTH = KEY_VAT_PR_MONTH;
	private static final String PAR_VAT_TYPE = KEY_VAT_TYPE;
	private static final String PAR_SCH_TYPE = KEY_SCH_TYPE;	
//	public static final String PAR_SELECTED_PROVIDER = "selected_provider";	
	
	private static final String PAR_PK = "pk";	
	private static final String PAR_DELETE_PK = "delete_pk";
	
	private static final int MIN_LEFT_COLUMN_WIDTH = 150;	
	
	private boolean newPayment = false; //prevents the posting strings to be generated from old data, when new payment is created
	

	private UserSearcher searcher = null;

	private static final int 
		ACTION_SHOW = 0, 
		ACTION_NEW = 1, 
		ACTION_DELETE = 2, 
		ACTION_EDIT_FROM_DB = 4, 		
		ACTION_EDIT_FROM_SCREEN = 5, 
		ACTION_SEARCH_PAYMENTS = 6,
		ACTION_SEARCH_REGULATION = 7,
		ACTION_SAVE = 8,
		ACTION_CANCEL_NEW_EDIT = 9,
		ACTION_OPFIELD_DETAILSCREEN = 10,
		ACTION_OPFIELD_MAINSCREEN = 11,
		ACTION_SELECTED_PROVIDER = 12;
			
	private static final String PAR = "PARAMETER_";
	private static final String 
		PAR_NEW = PAR + ACTION_NEW, 
		PAR_DELETE = PAR + ACTION_DELETE, 
		PAR_EDIT_FROM_DB = PAR + ACTION_EDIT_FROM_DB, 
		PAR_EDIT_FROM_SCREEN = PAR + ACTION_EDIT_FROM_SCREEN, 
		PAR_SEARCH_PAYMENTS = PAR + ACTION_SEARCH_PAYMENTS,
		PAR_SAVE = PAR + ACTION_SAVE,
		PAR_CANCEL_NEW_EDIT = PAR + ACTION_CANCEL_NEW_EDIT,
		PAR_OPFIELD_DETAILSCREEN = PAR + ACTION_OPFIELD_DETAILSCREEN,
		PAR_OPFIELD_MAINSCREEN = PAR + ACTION_OPFIELD_MAINSCREEN;
		
	public static final String PAR_SELECTED_PROVIDER = PAR + ACTION_SELECTED_PROVIDER;
	
//	int ijk = 0;
//	String searchPeopleAction = ""; 

	
	public void init(final IWContext iwc) {
		
		School school = getSchool(iwc);

		String dateFormatErrorMessage = null;	

		String parFrom = iwc.getParameter(PAR_SEEK_FROM);
		Date fromDate = parseDate(parFrom);

		String parTo = iwc.getParameter(PAR_SEEK_TO);
		Date toDate = parseDate(parTo);
		
		int action = parseAction(iwc);
				
		if (action != ACTION_OPFIELD_MAINSCREEN && ((parFrom != null && fromDate == null) || (parTo != null && toDate == null))){
			dateFormatErrorMessage = localize(LOCALIZER_PREFIX + "date_format_yymm_warning", "Wrong date format. use: yymm.");
			handleDefaultAction(iwc, school, fromDate, toDate, dateFormatErrorMessage);
			return;
		}		
		
		
		try {

			switch (action) {
				case ACTION_SHOW:
					handleDefaultAction(iwc, school, fromDate, toDate);
					break;
				case ACTION_NEW:
					newPayment = true;
					handleEditAction(iwc, getEmptyEntry());
					break;
				case ACTION_DELETE:
					handleDeleteAction(iwc);
					handleDefaultAction(iwc, school, fromDate, toDate);
					break;
				case ACTION_CANCEL_NEW_EDIT:				
				case ACTION_SEARCH_PAYMENTS:		
				case ACTION_OPFIELD_MAINSCREEN:
					handleDefaultAction(iwc, school, fromDate, toDate);
					break;
				case ACTION_EDIT_FROM_DB:
					handleEditAction(iwc, getStoredEntry(iwc));
					break;
									
				case ACTION_EDIT_FROM_SCREEN:
				case ACTION_OPFIELD_DETAILSCREEN:				
					handleEditAction(iwc, getNotStoredEntry(iwc));
					break;
				case ACTION_SEARCH_REGULATION:
					//TODO implement
					break;	
				case ACTION_SAVE:
					handleSaveAction(iwc, school);
					break;	
				
				default:
					handleEditAction(iwc, getNotStoredEntry(iwc));		
					
			}
		}
		catch (Exception e) {
			add(new ExceptionWrapper(e, this));
		}
	}

	
	/**
	 * @param iwc
	 */
	private Collection doPaymentsSearch(IWContext iwc, School provider, Date from, Date to) {
		Collection payments = new ArrayList();		
		if (provider != null && from != null && to != null){

			RegularPaymentBusiness paymentsBusiness = getRegularPaymentBusiness(iwc);

			try{
				payments = paymentsBusiness.findRegularPaymentsForPeriodeAndSchool(from, to, provider);
			}catch(FinderException ex){
				ex.printStackTrace(); 
			}catch(IDOLookupException ex){
				ex.printStackTrace(); 
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return payments;		
	}
		

	
	private RegularPaymentEntry getStoredEntry(IWContext iwc){
		RegularPaymentEntry entry = null;
		RegularPaymentEntryHome home = getRegularPaymentEntryHome();
		if (home != null){
			try{
				entry = home.findByPrimaryKey(iwc.getParameter(PAR_PK));
			}catch(FinderException ex){
				ex.printStackTrace();
			}
		}
		return entry;
	}
	
	private School getSchool(IWContext iwc){
		return getSchool(iwc.getParameter(PAR_SELECTED_PROVIDER));
	}
	
	private School getSchool(String schoolId){
		School school = null;
		try{
			SchoolHome sh = (SchoolHome) IDOLookup.getHome(School.class);
			school = sh.findByPrimaryKey(schoolId);
		}catch(IDOLookupException ex){
			ex.printStackTrace(); 
		}catch(FinderException ex){
			ex.printStackTrace(); 
		}
		return school;		
	}
	
	
	private User getUser(IWContext iwc){
		String userPid = iwc.getParameter(PAR_USER_SSN);
		User user = null;
		if (userPid != null && userPid.length() > 0){
			try{
				user = getUserBusiness(iwc.getApplicationContext()).getUser(userPid);
			}catch(FinderException ex){
				ex.printStackTrace(); 
			}
		}
		return user;	
	}
		

	/*
	 * Returns the action constant for the action to perform based 
	 * on the POST parameters in the specified context.
	 */
	private int parseAction(IWContext iwc) {
		String userSearchCommited = iwc.getParameter(UserSearcher.SEARCH_COMMITTED);
		String userSearchCleared = iwc.getParameter(UserSearcher.SEARCH_CLEARED);
		if (new Boolean(userSearchCommited).booleanValue() ||
			new Boolean(userSearchCleared).booleanValue()){
			return ACTION_EDIT_FROM_SCREEN;
		}
		
		if (iwc.getParameter(RegulationSearchPanel.SEARCH_REGULATION) != null){
			return ACTION_EDIT_FROM_SCREEN;
		}
		
		int action = ACTION_SHOW;
		for (int a = 0; a <= 20; a++){ 
			if (iwc.isParameterSet(PAR + a)){
				action = a;
				break;					
			}
		}
		return action;
	}	


	private void handleDeleteAction(IWContext iwc){
		RegularPaymentEntry entry = getRegularPaymentEntry(iwc.getParameter(PAR_PK));
		try{
			entry.remove();
		} catch (EJBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private RegularPaymentBusiness getRegularPaymentBusiness(IWContext iwc){
		RegularPaymentBusiness paymentsBusiness = null;
		try{
			paymentsBusiness = (RegularPaymentBusiness)IBOLookup.getServiceInstance(iwc, RegularPaymentBusiness.class);
		}catch(RemoteException ex){
			ex.printStackTrace();	
			return null;		
		}		
		return paymentsBusiness;
	}
	
	private Collection getProvidersForOperationalField(IWContext iwc) {
		Collection providers = new ArrayList();		
		String opField = null;
		try{
			SchoolBusiness schoolBusiness = (SchoolBusiness) IBOLookup.getServiceInstance(iwc.getApplicationContext(), SchoolBusiness.class);
			opField = getSession().getOperationalField();
			try{
				SchoolCategory sc = schoolBusiness.getSchoolCategoryHome().findByPrimaryKey(opField);
				
				SchoolHome home = (SchoolHome) IDOLookup.getHome(School.class);				
				providers = home.findAllByCategory(sc);
			}catch(FinderException ex){
				ex.printStackTrace();
			}			
		}catch(RemoteException ex){
			ex.printStackTrace();
		}
		return providers;
	}	

		
	
	private void handleSaveAction(IWContext iwc, School school){
		Map errorMessages = new HashMap();
		checkNotNull(iwc, RegulationSearchPanel.PAR_PLACING, errorMessages, ERROR_PLACING_NULL, "Placing must be set");
				
		if (! iwc.isLoggedOn()){
			errorMessages.put(ERROR_NO_USER_SESSION, localize(ERROR_NO_USER_SESSION, "Not logged in."));
			return;
		}

		User loggedOnUser=iwc.getCurrentUser();
		
		RegularPaymentEntry entry = null;
		if (iwc.getParameter(PAR_PK) != null){
			entry = getRegularPaymentEntry(iwc.getParameter(PAR_PK));
		}

	
		if (errorMessages.isEmpty()){
			if (entry == null){
				try{
					entry = getRegularPaymentEntryHome().create();
					entry.setCreatedDate(new Date(new java.util.Date().getTime()));
					entry.setCreatedSign(loggedOnUser.getName());
					entry.setEditSign(" ");								
				
				}catch(CreateException ex2){
					ex2.printStackTrace();
					return;
				}			
			} else{
				entry.setEditDate(new Date(new java.util.Date().getTime()));
				entry.setEditSign(loggedOnUser.getName());
			}			
	
			long amountMonth = 0;
			try{
				amountMonth = AccountingUtil.roundAmount(new Float(iwc.getParameter(PAR_AMOUNT_PR_MONTH)).floatValue());
				entry.setAmount(amountMonth);
			}catch(NumberFormatException ex){
				ex.printStackTrace();
				errorMessages.put(ERROR_AMOUNT_FORMAT, localize(ERROR_AMOUNT_FORMAT, "Wrong format for amount"));
			}
	
			Date from = parseDate(iwc.getParameter(PAR_FROM));
			Date to = parseDate(iwc.getParameter(PAR_TO));
			if (from == null || to == null){
				errorMessages.put(ERROR_DATE_FORMAT, localize(LOCALIZER_PREFIX + "date_format_yymm_warning", "Wrong date format. use: yymm."));
			} else if (to.before(from)){
				errorMessages.put(ERROR_DATE_PERIODE_NEGATIVE, localize(LOCALIZER_PREFIX + "negative_periode", "Neagtive periode"));
			} else {			
			
				//Setting date to last day in month.
				IWTimestamp lastDay = new IWTimestamp(to);
				lastDay.addMonths(1);
				lastDay.addDays(-1);
				
				entry.setFrom(from);
				entry.setTo(lastDay.getDate());
			}
			
			String note = iwc.getParameter(PAR_REMARK);
			if (note == null || note.length() == 0){
				note = " "; //Oracle stores empty string as "null"
			}				
			entry.setNote(note);
		
			if (iwc.getParameter(PAR_SCH_TYPE) != null){
				entry.setSchoolTypeId(Integer.parseInt(iwc.getParameter(PAR_SCH_TYPE)));
			}
			entry.setPlacing(iwc.getParameter(PAR_PLACING));
			float vat = new Float(iwc.getParameter(PAR_VAT_PR_MONTH)).floatValue();
			entry.setVATAmount(vat);
			if (iwc.getParameter(PAR_SELECTED_PROVIDER) != null){
				entry.setSchoolId(new Integer(iwc.getParameter(PAR_SELECTED_PROVIDER)).intValue());
			}
			
			entry.setUser(getUser(iwc));
			
			if (iwc.getParameter(PAR_VAT_TYPE) != null && iwc.getParameter(PAR_VAT_TYPE).length() != 0){
				entry.setVatRuleRegulationId(new Integer(iwc.getParameter(PAR_VAT_TYPE)).intValue());
			}
	
			try{
				PostingBlock p = new PostingBlock(iwc);			
				entry.setOwnPosting(p.getOwnPosting());
				entry.setDoublePosting(p.getDoublePosting());
			} catch (PostingParametersException e) {
				errorMessages.put(ERROR_POSTING, localize(e.getTextKey(), e.getTextKey()) + e. getDefaultText());
			}	
						
	//			entry.setOwnPosting(iwc.getParameter(PAR_OWN_POSTING));
	//			entry.setDoublePosting(iwc.getParameter(PAR_DOUBLE_ENTRY_ACCOUNT));

			if (entry.getPlacing() == null || entry.getPlacing().length() == 0){
				errorMessages.put(ERROR_PLACING_EMPTY, localize(LOCALIZER_PREFIX + "placing_null", "Placing must be given a value"));
			} 
	//			if (entry.getAmount() == 0){
	//				errorMessages.put(ERROR_AMOUNT_EMPTY, localize(LOCALIZER_PREFIX + "amount_null", "Amount must be given a value"));
	//			}
	
			if (entry.getOwnPosting() == null || entry.getOwnPosting().length() == 0){
				errorMessages.put(ERROR_OWNPOSTING_EMPTY, localize(LOCALIZER_PREFIX + "own_posting_null", "Own posting must be given a value"));
			}
		} //END: errorMessages.isEmpty
			
		if (! errorMessages.isEmpty()){
			handleEditAction(iwc, entry, errorMessages);	
		}else{		
			entry.store();		
			handleDefaultAction(iwc, school, parseDate(iwc.getParameter(PAR_SEEK_FROM)), parseDate(iwc.getParameter(PAR_SEEK_TO)));
		}				
	}

	private RegularPaymentEntry getRegularPaymentEntry(String pk) {
		RegularPaymentEntryHome home = getRegularPaymentEntryHome();
		RegularPaymentEntry entry = null;
		if (home != null){
			try{
				entry = home.findByPrimaryKey(pk);
			}catch(FinderException ex){
				ex.printStackTrace();
			}
		}
		return entry;
	}

	private RegularPaymentEntryHome getRegularPaymentEntryHome() {
		RegularPaymentEntryHome home = null;
		try{
			home = (RegularPaymentEntryHome) IDOLookup.getHome(RegularPaymentEntry.class);
			
		}catch(IDOLookupException ex){
			ex.printStackTrace();			
		}
		return home;
	}

			
	private void handleEditAction(IWContext iwc, RegularPaymentEntry entry){
		handleEditAction(iwc, entry, null);
	}

			
	private void handleEditAction(IWContext iwc, RegularPaymentEntry entry, Map errorMessages){
		
		if (entry == null){ //may happen if user was not logged in and tried to save
			entry = getNotStoredEntry(iwc);
		}
		
		Table t1 = new Table();
		
		t1.setCellpadding(getCellpadding());
		t1.setCellspacing(getCellspacing());


		
		Collection vatRuleRegulations = new ArrayList();
		try {
			vatRuleRegulations = getRegulationsBusiness(iwc.getApplicationContext()).findAllVATRuleRegulations();			
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}	
		
		Form form = new Form();
		if (entry != null && entry.getPrimaryKey() != null){
			form.maintainParameter(PAR_PK);		
		}
		form.maintainParameter(PAR_USER_SSN);			
		form.maintainParameter(PAR_SEEK_FROM);
		form.maintainParameter(PAR_SEEK_TO);
		form.maintainParameter(PAR_SELECTED_PROVIDER);

	
		form.add(getDetailPanel(iwc, entry, vatRuleRegulations, errorMessages));
		
		t1.mergeCells(1, 2, 20, 2);		
		t1.add(form, 1, 2);
		add(t1);
	}

		
	private void handleDefaultAction(IWContext iwc, School school, Date fromDate, Date toDate, String errorMessage){
		add(getEntryListPage(iwc, doPaymentsSearch(iwc, school, fromDate, toDate), school, fromDate, toDate, errorMessage));
	}
			
	private void handleDefaultAction(IWContext iwc, School school, Date fromDate, Date toDate){
		add(getEntryListPage(iwc, doPaymentsSearch(iwc, school, fromDate, toDate), school, fromDate, toDate));
	}

	
	private Table getEntryListPage(IWContext iwc, Collection entries, School school, Date fromDate, Date toDate){
		return getEntryListPage(iwc, entries, school, fromDate, toDate, null);
	}
	
	private Table getEntryListPage(IWContext iwc, Collection entries, School school, Date fromDate, Date toDate, String errorMessage){
		
		Table maninTbl = new Table();
		maninTbl.setCellpadding(getCellpadding());
		maninTbl.setCellspacing(getCellspacing());
		
		Table opfieldTbl = new Table();		
		addOperationalFieldPanel(opfieldTbl, 1, PAR_OPFIELD_MAINSCREEN);
		maninTbl.add(opfieldTbl, 1, 1);
		
		int row = 1;				
		addPeriodeForm(iwc, maninTbl, fromDate, toDate, errorMessage, row++);
			
		maninTbl.add(getPaymentsList(entries, school, fromDate, toDate), 1, row++);
	
		ButtonPanel bp = new ButtonPanel(this);
		bp.addLocalizedButton(PAR_NEW, KEY_NEW, "New");
		maninTbl.add(bp, 1, row++);

		Form form = new Form();		
		form.maintainAllParameters();		
		form.add(new HiddenInput(PAR_DELETE_PK, "-1"));
		form.add(maninTbl);

		Table formTbl = new Table();
		formTbl.add(form, 1, 1);

		return formTbl;		
	}
	

	
	
	private int addOperationalFieldPanel(Table table, int row, String actionCommand) {
		
		table.add(getLocalizedLabel(KEY_OPERATIONAL_FIELD, "Huvudverksamhet"), 1, row);
		OperationalFieldsMenu ofm = new OperationalFieldsMenu();
		ofm.setParameter(actionCommand, " ");		
		ofm.maintainParameter(PAR_SEEK_FROM);
		ofm.maintainParameter(PAR_SEEK_TO);
		ofm.maintainParameter(PAR_USER_SSN);			
	
		table.add(ofm, 2, row);

//		table.add(new HiddenInput(actionCommand, " "), 3, row); //to make it return to the right page
		return row + 1;
	}
		

	private UserSearcher getUserSearcher(IWContext iwc, User user){
		
		searcher = new UserSearcher();
		searcher.setPersonalIDLength(15);
		searcher.setFirstNameLength(25);
		searcher.setLastNameLength(25);
		searcher.setShowMiddleNameInSearch(false);
		searcher.setOwnFormContainer(false);
		searcher.setUniqueIdentifier("");
		searcher.setBelongsToParent(true);
		searcher.setConstrainToUniqueSearch(false);
		searcher.maintainParameter(new Parameter(PAR_EDIT_FROM_SCREEN, " "));
		searcher.maintainParameter(new Parameter(PAR_OWN_POSTING, " "));		
		searcher.maintainParameter(new Parameter(PAR_DOUBLE_ENTRY_ACCOUNT, " "));		
		searcher.setToFormSubmit(true);
		searcher.setHeaderFontStyle (getSmallHeaderFontStyle ());		

		String pk = iwc.getParameter(PAR_PK);
		
		if (pk != null){
			searcher.add(new HiddenInput(PAR_PK, pk));
		}
		
		try{
			searcher.process(iwc);	
			if (searcher.getUser() == null && ! searcher.isHasManyUsers() && ! searcher.isClearedButtonPushed(iwc)){
				searcher.setUser(user);
			}			
		} catch (FinderException ex){
			
			ex.printStackTrace();
		} catch (RemoteException ex){
			ex.printStackTrace();			
		}
		
		return searcher;
	}
	

	private int addPeriodeForm(IWContext iwc, Table table, Date fromDate, Date toDate, String errorMessage, int row){
			
//		Form form = new Form();

		Table formTable = new Table();
		int formTableRow = 1;
		
		int selectedProvider = -1;
		try{
			selectedProvider = new Integer(iwc.getParameter(PAR_SELECTED_PROVIDER)).intValue();
		} catch(NumberFormatException ex){		}
				
		addDropDown(formTable, PAR_SELECTED_PROVIDER, KEY_PROVIDER, getProvidersForOperationalField(iwc), selectedProvider, "getSchoolName", 1, formTableRow++);

		if (errorMessage != null){
			formTable.mergeCells(2, formTableRow, 10, formTableRow);
			formTable.add(getErrorText(errorMessage), 2, formTableRow++);
		}
				
		String today = formatDate(new Date(System.currentTimeMillis()), 4); 
						
		formTable.add(getLocalizedLabel("KEY_PERIODE", "Periode"), 1, formTableRow);
		TextInput from = getTextInput(PAR_SEEK_FROM, today);
		from.setLength(4);
//		from.setAsNotEmpty(localize(LOCALIZER_PREFIX + "field_empty_warning", "Field should not be empty: ") + KEY_PERIODE);
		if (fromDate != null){
			from.setContent(formatDate(fromDate, 4));	
		}	
		
		TextInput to = getTextInput(PAR_SEEK_TO, today);
		to.setLength(4);
//		to.setAsNotEmpty(localize(LOCALIZER_PREFIX + "field_empty_warning", "Field should not be empty: ") + KEY_PERIODE);
		if (toDate != null){
			to.setContent(formatDate(toDate, 4));	
		}
		
		formTable.add(from, 2, formTableRow);
		formTable.add(getText(" - "), 2, formTableRow);	
		formTable.add(to, 2, formTableRow);			

		formTable.add(getLocalizedButton(PAR_SEARCH_PAYMENTS, KEY_SEARCH, "Search"), 10, formTableRow++);
//		form.maintainParameter(PAR_SELECTED_PROVIDER);
//		if (user != null) {
//			formTable.add(new HiddenInput(PAR_USER_SSN, user.getPersonalID()));		
//		}

//		form.add(formTable);
		table.add(formTable, 1, row++);
		return row;	
	}	
	
	
	private ListTable getPaymentsList(Collection payments, School school, Date fromDate, Date toDate) {
		
		ListTable list = new ListTable(this, 6);
		
		list.setLocalizedHeader(KEY_PAYMENT_PERIODE, "Payment periode", 1);
		list.setLocalizedHeader(KEY_PLACING, "Placing", 2);
		list.setLocalizedHeader(KEY_AMOUNT, "Amount", 3);
		list.setLocalizedHeader(KEY_NOTE, "Note", 4);
		list.setLocalizedHeader(KEY_EDIT, "Edit", 5);
		list.setLocalizedHeader(KEY_DELETE, "Delete", 6);
		
		try {
			if (payments != null) {
				Iterator i = payments.iterator();
				while (i.hasNext()) {
					RegularPaymentEntry entry = (RegularPaymentEntry) i.next();
					list.add(getText(formatDate(entry.getFrom(), 4) + " - " + formatDate(entry.getTo(), 4)));
					
					Link link = getLink(entry.getPlacing(), PAR_PK, "" + entry.getPrimaryKey());
					link.setParameter(PAR_EDIT_FROM_DB, " ");
					link.setParameter(PAR_SEEK_FROM, formatDate(fromDate, 4));
					link.setParameter(PAR_SEEK_TO, formatDate(toDate, 4));
					link.setParameter(PAR_SELECTED_PROVIDER, school.getPrimaryKey().toString());
					list.add(link);
					
					list.add(getText(""+AccountingUtil.roundAmount(entry.getAmount())));
					list.add(getText(entry.getNote()));

					Link edit = new Link(getEditIcon(localize(KEY_EDIT_TOOLTIP, "Edit")));
					edit.addParameter(PAR_EDIT_FROM_DB, " ");
					edit.addParameter(PAR_PK, entry.getPrimaryKey().toString());
					edit.setParameter(PAR_SEEK_FROM, formatDate(fromDate, 4));
					edit.setParameter(PAR_SEEK_TO, formatDate(toDate, 4));					
					edit.addParameter(PAR_SELECTED_PROVIDER, school.getPrimaryKey().toString());
					list.add(edit);
					
					Link delete = new Link(getDeleteIcon(localize(KEY_DELETE_TOOLTIP, "Delete")));
					delete.addParameter(PAR_DELETE, " ");
					delete.addParameter(PAR_PK, entry.getPrimaryKey().toString());
					delete.setParameter(PAR_SEEK_FROM, formatDate(fromDate, 4));
					delete.setParameter(PAR_SEEK_TO, formatDate(toDate, 4));					
					delete.addParameter(PAR_SELECTED_PROVIDER, school.getPrimaryKey().toString());
					delete.setOnClick("return confirm('Confirm deletion');");
					list.add(delete);
				}
			}			
		} catch (Exception e) {
			super.add(new ExceptionWrapper(e, this));
		}	
		list.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_RIGHT);
		list.setColumnAlignment(5, Table.HORIZONTAL_ALIGN_CENTER);
		list.setColumnAlignment(6, Table.HORIZONTAL_ALIGN_CENTER);		
		return list;
	}
	
	private Table getDetailPanel(IWContext iwc, RegularPaymentEntry entry, Collection vatTypes, Map errorMessages){
		if (errorMessages == null){
			errorMessages = new HashMap();
		}
						
		final int EMPTY_ROW_HEIGHT = 8;
		Table table = new Table();
		int row = 1;
		
		row = addOperationalFieldPanel(table, row, PAR_OPFIELD_DETAILSCREEN);
				
		
//		table.setHeight(row++, EMPTY_ROW_HEIGHT);
		if (errorMessages.get(ERROR_NO_USER_SESSION) != null){
			table.add(getErrorText((String) errorMessages.get(ERROR_NO_USER_SESSION)), 1, row++);			
		}	
		
		if (errorMessages.get(ERROR_PLACING_NULL) != null){
			table.add(getErrorText((String) errorMessages.get(ERROR_PLACING_NULL)), 1, row++);			
		}		

		RegulationSearchPanel regSearchPanel = new RegulationSearchPanel(iwc, PAR_SELECTED_PROVIDER); 	
		regSearchPanel.setLeftColumnMinWidth(MIN_LEFT_COLUMN_WIDTH);
				
		regSearchPanel.setPlacingIfNull(entry.getPlacing());
		regSearchPanel.setSchoolIfNull(getSchool(iwc));
		
//		School school = regSearchPanel.getSchool();		
//		
//		if (school != null){
//			HiddenInput h = new HiddenInput(PAR_SELECTED_PROVIDER, "" + school.getPrimaryKey());
//			table.add(h);			
//		}
		
		regSearchPanel.maintainParameter(new String[]{PAR_USER_SSN, PAR_SEEK_FROM, PAR_SEEK_TO, PAR_FROM, PAR_TO, PAR_AMOUNT_PR_MONTH});
//		if (errorMessages.get(ERROR_PLACING_EMPTY) != null) {
//			searchPanel.setError((String) errorMessages.get(ERROR_PLACING_EMPTY));			
//		}		

		
		regSearchPanel.setParameter(PAR_EDIT_FROM_SCREEN, " ");
		table.mergeCells(1, row, 20, row);
		table.add(regSearchPanel, 1, row++); 

		Regulation reg = regSearchPanel.getRegulation(); 
		
		String[] posting = new String[]{"",""};
		String postingError = null;
		try{
			posting = regSearchPanel.getPosting();
		}catch (PostingException ex){
			postingError = ex.getMessage();
		}		
		if (reg != null){
			entry = getNotStoredEntry(iwc, reg, posting);
		}
		

		
//		
//		addField(table, KEY_PROVIDER, getSchool(iwc).getName(), 1, row++);
//		
//		table.setHeight(row++, EMPTY_ROW_HEIGHT);		
//
//
//		addNoEmptyField(table, PAR_PLACING, KEY_PLACING, entry.getPlacing(), 1, row);		
//		addField(table, PAR_VALID_DATE, KEY_VALID_DATE, iwc.getParameter(PAR_VALID_DATE), 3, row);	
		
//		table.add(getLocalizedButton(PAR_SEARCH_REGULATION, KEY_SEARCH, "Search"), 5, row++);

		table.setHeight(row++, EMPTY_ROW_HEIGHT);
		
		if (errorMessages.get(ERROR_DATE_FORMAT) != null){
			table.mergeCells(1, row, 10, row);
			table.add(getErrorText((String) errorMessages.get(ERROR_DATE_FORMAT)), 1, row++);			
		}
		table.add(getLocalizedLabel(KEY_PERIODE, "Periode:"), 1, row);
		TextInput fromInput = getTextInput(PAR_FROM, KEY_FROM);
		fromInput.setContent(formatDate(entry.getFrom(), 4));
		fromInput.setLength(4);
		table.add(fromInput, 2, row);
		table.add(getText(" - "), 2, row);
		TextInput toInput = getTextInput(PAR_TO, KEY_TO);
		toInput.setContent(formatDate(entry.getTo(), 4));		
		toInput.setLength(4);
		table.add(toInput, 2, row++);
		
		table.setHeight(row++, EMPTY_ROW_HEIGHT);
		
		addField(table, KEY_DAY_CREATED, formatDate(entry.getCreatedDate(), 6), 1, row);
		addField(table, KEY_SIGNATURE, entry.getCreatedName(), 3, row++);
		addField(table, KEY_DAY_REGULATED, formatDate(entry.getEditDate(), 6), 1, row);
		addField(table, KEY_SIGNATURE, entry.getEditName(), 3, row++);		

		table.setHeight(row++, EMPTY_ROW_HEIGHT);
		
		if (errorMessages.get(ERROR_AMOUNT_FORMAT) != null){
			table.add(getErrorText((String) errorMessages.get(ERROR_AMOUNT_FORMAT)), 1, row++);			
		}
		
		addIntField(table, PAR_AMOUNT_PR_MONTH, KEY_AMOUNT_PR_MONTH, ""+AccountingUtil.roundAmount(entry.getAmount()), 1, row++);

		addIntField(table, PAR_VAT_PR_MONTH, KEY_VAT_PR_MONTH, ""+AccountingUtil.roundAmount(entry.getVATAmount()), 1, row++);

		table.setHeight(row++, EMPTY_ROW_HEIGHT);

		addField(table, PAR_REMARK, KEY_REMARK, entry.getNote(), 1, row++, 300);
		
		try{
			Collection types = getSchoolBusiness(iwc).findAllSchoolTypes();
			SchoolType current = regSearchPanel.getCurrentSchoolType();
			int selected = current != null ? ((Integer) current.getPrimaryKey()).intValue() : entry.getSchoolTypeId();
			addDropDown(table, PAR_SCH_TYPE, KEY_SCH_TYPE, types, selected, "getSchoolTypeName", 1, row++);
		}catch(RemoteException ex){
			ex.printStackTrace();
		}		

		table.setHeight(row++, EMPTY_ROW_HEIGHT);

		
		if (errorMessages.get(ERROR_POSTING) != null) {
			table.add(getErrorText((String) errorMessages.get(ERROR_POSTING)), 2, row++);			
		} else if (errorMessages.get(ERROR_OWNPOSTING_EMPTY) != null) {
			table.add(getErrorText((String) errorMessages.get(ERROR_OWNPOSTING_EMPTY)), 2, row++);			
		} else if (postingError != null){
			table.add(getErrorText(postingError), 2, row++);				
		}

		
		table.mergeCells(1, row, 10, row);
		
		PostingBlock postingBlock = null;
		if (entry.getOwnPosting() != null && entry.getOwnPosting().trim().length() != 0){
			postingBlock = new PostingBlock(entry.getOwnPosting(), entry.getDoublePosting());
		} else {
			//When searching for user, the posting info is lost
			postingBlock = new PostingBlock(); 
			
			if (newPayment){
				postingBlock.setToEmpty();
			}else{
				try{
					postingBlock.generateStrings(iwc);
				}catch(NullPointerException ex){
					postingBlock = new PostingBlock("", "");
				} catch (PostingParametersException e) {
					e.printStackTrace();
				}
			}			
		}
		table.add(postingBlock, 1, row++);
		
		
//		addField(table, PAR_OWN_POSTING, KEY_OWN_POSTING, entry.getOwnPosting(), 1, row++);
//		addField(table, PAR_DOUBLE_ENTRY_ACCOUNT, KEY_DOUBLE_ENTRY_ACCOUNT, entry.getDoublePosting(), 1, row++);

		
		addDropDownLocalized(table, PAR_VAT_TYPE, KEY_VAT_TYPE, vatTypes, entry.getVatRuleRegulationId(),  "getName", 1, row++);
		
		table.setHeight(row++, EMPTY_ROW_HEIGHT);
		
		table.mergeCells(1, row, 10, row);
		

		int userId = entry.getUserId();
		User user = null;
		if (userId != -1){
			try{
				user = getUserBusiness(iwc.getApplicationContext()).getUser(userId);
			}catch(RemoteException ex){
				ex.printStackTrace();
			}
		}



		UserSearcher searcher = getUserSearcher(iwc, user);
		table.add(searcher, 1, row++);
//		if (searcher.getUser() != null && ! searcher.isHasManyUsers()){
//			HiddenInput h = new HiddenInput(PAR_USER_SSN, searcher.getUser().getPersonalID());
//			table.add(h);
//		}

		table.setHeight(row++, EMPTY_ROW_HEIGHT);		
				
		ButtonPanel bp = new ButtonPanel(this);
		bp.addLocalizedButton(PAR_SAVE, KEY_SAVE, "Save");
		bp.addLocalizedButton(PAR_CANCEL_NEW_EDIT, KEY_CANCEL, "Delete");
		table.add(bp, 1, row);
		
		table.setColumnWidth(1, "" + MIN_LEFT_COLUMN_WIDTH);		
		
		return table;
	}

	private RegularPaymentEntry getEmptyEntry() {
		return getNotStoredEntry(null, null, null);		
	}
	
	private RegularPaymentEntry getNotStoredEntry(IWContext iwc) {
		return getNotStoredEntry(iwc, null, null);
	}	
	
	private RegularPaymentEntry getNotStoredEntry(IWContext iwc, Regulation reg, String[] posting) {
		final IWContext _iwc = iwc;
		final Regulation _reg = reg;
		final String[] _posting = posting;		
		
		return new RegularPaymentEntry() {
		
			public Date getFrom() {
				return  getDateValue(PAR_FROM);
			}
		
			public Date getTo() {
				return  getDateValue(PAR_TO);
			}
		
			public String getPlacing() {
				return  _reg != null ? _reg.getName() : getValue(PAR_PLACING);
			}
		
			public User getUser() {
				String userSsn = getValue(PAR_USER_SSN);
				if (userSsn != null && userSsn.length() > 0){
					try{
						return getUserBusiness(_iwc.getApplicationContext()).getUser(userSsn);
					}catch(FinderException ex){
						ex.printStackTrace();
					}
				}
				return null;
			}
			
			public int getUserId() {
				User user = getUser();
				return user != null ? user.getNodeID() : -1;
			}			
		
			public RegulationSpecType getRegSpecType() {
				return _reg != null ? _reg.getRegSpecType() : null;
			}
		
			public int getRegSpecTypeId() {
				return _reg != null ? new Integer(""+_reg.getRegSpecType().getPrimaryKey()).intValue() : getIntValue(PAR_REGULATION_TYPE);
			}
		
			public School getSchool() {
				School school = null;
				try{
					SchoolHome sh = (SchoolHome) IDOLookup.getHome(School.class);
					school = sh.findByPrimaryKey(""+getSchoolId());
				}catch(IDOLookupException ex){
					ex.printStackTrace(); 
				}catch(FinderException ex){
					ex.printStackTrace(); 
				}				
				return school;
			} 
		
			public int getSchoolId() {
				return getIntValue(PAR_SELECTED_PROVIDER);
			}
		
			public String getOwnPosting() {
//				try{
//					return new PostingBlock(_iwc).getOwnPosting();				
//				}catch(Exception ex){
//					return "";
//				}
				
				if (_posting != null && _posting.length >= 1){
					return _posting[0];
				}
				return getValue(PAR_OWN_POSTING);
			}
		
			public String getDoublePosting() {
//				try{
//					return new PostingBlock(_iwc).getDoublePosting();				
//				}catch(Exception ex){
//					return "";
//				}			
				if (_posting != null && _posting.length >= 2){
					return _posting[1];
				}					
				return getValue(PAR_DOUBLE_ENTRY_ACCOUNT);
			}
		
			public float getAmount() {
				return _reg != null ? _reg.getAmount().floatValue() : getFloatValue(PAR_AMOUNT_PR_MONTH);
			}
		
			public float getVATAmount() {
				if (_reg != null){
					try{
						VATBusiness vb = (VATBusiness) IBOLookup.getServiceInstance(_iwc, VATBusiness.class);
						return vb.getVATPercentForRegulation(_reg);
					}catch(IBOLookupException ex){
						ex.printStackTrace();
					}catch(VATException ex){
						ex.printStackTrace();
					}catch(RemoteException ex){
						ex.printStackTrace();						
					}					
				} else {
					return getFloatValue(PAR_VAT_PR_MONTH);
				}
				return 0;
			}
		
			public Regulation getVatRuleRegulation() {
				if (_reg != null){
					return _reg.getVATRuleRegulation();
				} else {
					Regulation vatRule = null;
					try{
						RegulationHome rhome = (RegulationHome) IDOLookup.getHome(Regulation.class);
						vatRule = rhome.findByPrimaryKey(new Integer(getVatRuleRegulationId()));
					}catch(IDOLookupException ex){
						ex.printStackTrace(); 
					}catch(FinderException ex){
						ex.printStackTrace(); 
					}				
					return vatRule;
				}
			}
		
			public int getVatRuleRegulationId() {
				if (_reg != null){
					Regulation r = getVatRuleRegulation();
					if (r != null){
						return ((Integer) r.getPrimaryKey()).intValue();
					} else{
						return -1;
					}
				} else {
					return getIntValue(PAR_VAT_TYPE);
				}
			}
		
			public String getNote() {
				return getValue(PAR_REMARK);
			}
		
			public Date getCreatedDate() {
				return new Date(System.currentTimeMillis());
			}
		
			public String getCreatedName() {
				return "";
			}
		
			public Date getEditDate() {
				return null;
			}
		
			public String getEditName() {
				return "";
			}
			
			public int getSchoolTypeId(){
				return getIntValue(PAR_SCH_TYPE);				
			}
			
			public SchoolType getSchoolType() {
				SchoolType stype = null;
				try{
					SchoolTypeHome sh = (SchoolTypeHome) IDOLookup.getHome(SchoolType.class);
					stype = sh.findByPrimaryKey(new Integer(getSchoolTypeId()));
				}catch(IDOLookupException ex){
					ex.printStackTrace(); 
				}catch(FinderException ex){
					ex.printStackTrace(); 
				}				
				return stype;
			} 
			
			String getValue(String parameter){
				return _iwc == null || _iwc.getParameter(parameter) == null ? "" : _iwc.getParameter(parameter);
			}				
			
			int getIntValue(String parameter){
				try {
					return _iwc == null || _iwc.getParameter(parameter) == null ? 0 : new Integer(_iwc.getParameter(parameter)).intValue();
				} catch (NullPointerException ex){
					return 0;					
				} catch (NumberFormatException ex){
					return 0;
				}
			}
			
			float getFloatValue(String parameter){
				try {
					return _iwc == null || _iwc.getParameter(parameter) == null ? 0 : new Float(_iwc.getParameter(parameter)).floatValue();
				} catch (NullPointerException ex){
					return 0;
				} catch (NumberFormatException ex){
					return 0;
				}
			}
			Date getDateValue(String parameter){
				return _iwc == null || _iwc.getParameter(parameter) == null ? null : parseDate(_iwc.getParameter(parameter));
			}
						
//dummy implementations - methods not used.
			public void setFrom(Date from) {}
			public void setTo(Date to) {}
			public void setPlacing(String plascint) {}
			public void setUser(User user) {}
			public void setSchoolId(int schoolId) {}
			public void setAmount(float amount) {}
			public void setVATAmount(float vat) {}
			public void setVatRuleRegulationId(int vatRuleId) {}
			public void setNote(String note) {}
			public void setOwnPosting(String ownPosting) {}
			public void setDoublePosting(String doublePosting) {}
			public void setSchoolTypeId(int id) {}
			public void delete() {}
			public void store() throws IDOStoreException {}
			public IDOEntityDefinition getEntityDefinition() {return null;}
			public EJBLocalHome getEJBLocalHome() throws EJBException {return null;}
			public Object getPrimaryKey() throws EJBException {return null;}
			public void remove() throws EJBException {}
			public boolean isIdentical(EJBLocalObject arg0) throws EJBException {return false;}
			public int compareTo(Object arg0) {return 0;}

			public void setRegSpecType(RegulationSpecType p0) {}
			public void setRegSpecTypeId(int p0) {}
			public void setVatRuleRegulation(Regulation p0) {}

			public void setCreatedDate(Date p0) {
				// TODO Auto-generated method stub
				
			}

			public void setCreatedSign(String p0) {
				// TODO Auto-generated method stub
				
			}

			public void setEditDate(Date p0) {
				// TODO Auto-generated method stub
				
			}

			public void setEditSign(String p0) {
				// TODO Auto-generated method stub
				
			}
			public Object decode(String string){return null;}
			public Collection decode(String[] pks){return null;}
		};
	}
	


	/**
	 * @param table
	 * @param KEY_REGULATION_TYPE
	 * @param regulationType
	 * @param options
	 * @param PAR_REGULATION_TYPE
	 * @param i
	 * @param j
	 */
	private Table addDropDown(Table table, String parameter, String key, Collection options, int selected, String method, int col, int row) {
		DropdownMenu dropDown = getDropdownMenu(parameter, options, method);
		dropDown.setSelectedElement(selected);
		return addWidget(table, key, dropDown, col, row);		
	}

	/**
	 * @param table
	 * @param KEY_REGULATION_TYPE
	 * @param regulationType
	 * @param options
	 * @param PAR_REGULATION_TYPE
	 * @param i
	 * @param j
	 */
	private Table addDropDownLocalized(Table table, String parameter, String key, Collection options, int selected, String method, int col, int row) {
		DropdownMenu dropDown = getDropdownMenuLocalized(parameter, options, method);
		dropDown.setSelectedElement(selected);
		String selectString = this.getResourceBundle().getLocalizedString(KEY_SELECT,"Select:");
		dropDown.addFirstOption(new SelectOption(selectString,""));
		return addWidget(table, key, dropDown, col, row);		
	}
	
	/**
	 * Adds a label and a TextInput to a table
	 * @param table
	 * @param key is used both as localization key for the label and default label value
	 * @param value
	 * @param parameter
	 * @param col
	 * @param row
	 * @return
	 */
//	private Table addNoEmptyField(Table table, String parameter, String key, String value, int col, int row){
//		TextInput input = getTextInput(parameter, value);
//		input.setAsNotEmpty(localize(LOCALIZER_PREFIX + "field_empty_warning", "Field should not be empty: ") + key);
//		return addWidget(table, key, input, col, row);
//	}
	
	/**
	 * Adds a label and a TextInput to a table
	 * @param table
	 * @param key is used both as localization key for the label and default label value
	 * @param value
	 * @param parameter
	 * @param col
	 * @param row
	 * @return
	 */
	private Table addField(Table table, String parameter, String key, String value, int col, int row, int width){
		return addWidget(table, key, getTextInput(parameter, value, width), col, row);
	}
	
	/**
	 * Adds a label and a Text to a table
	 * @param table
	 * @param key is used both as localization key for the label and default label value
	 * @param value
	 * @param parameter
	 * @param col
	 * @param row
	 * @return
	 */	
	private Table addField(Table table, String key, String value, int col, int row){
		return addWidget(table, key, getText(value), col, row);
	}	
//	
//	/**
//	 * Adds a label and a TextInput to a table
//	 * @param table
//	 * @param key is used both as localization key for the label and default label value
//	 * @param value
//	 * @param parameter
//	 * @param col
//	 * @param row
//	 * @return
//	 */
//	private Table addFloatField(Table table, String parameter, String key, String value, int col, int row){
//		TextInput input = getTextInput(parameter, value);
//		input.setAsFloat(localize(LOCALIZER_PREFIX + "float_format_error", "Format-error: Expecting float:" )+ " " + localize(key, ""), 2); 
//		return addWidget(table, key, input, col, row);
//	}

	
	private Table addIntField(Table table, String parameter, String key, String value, int col, int row){
		TextInput input = getTextInput(parameter, value);
		input.setAsPosNegIntegers(localize(LOCALIZER_PREFIX + "int_format_error", "Format-error: Expecting integer:" )+ " " + localize(key, "")); 
		return addWidget(table, key, input, col, row);
	}
		
		
	

	/**
	 * Adds a label and a value to a table
	 * @param table
	 * @param key is used both as localization key for the label and default label value
	 * @param value
	 * @param col
	 * @param row
	 * @return
	 */
//	private Table addField(Table table, String key, String value, int col, int row){
//		return addWidget(table, key, getText(value), col, row);
//	}	
	
	/**
	 * Adds a label and widget to a table
	 * @param table
	 * @param key is used both as localization key for the label and default label value
	 * @param widget
	 * @param col
	 * @param row
	 * @return
	 */
	private Table addWidget(Table table, String key, PresentationObject widget, int col, int row){
		table.add(getLocalizedLabel(key, key), col, row);
		table.add(widget, col + 1, row);
		return table;
	
	}		
	
	protected RegulationsBusiness getRegulationsBusiness(IWApplicationContext iwc) throws RemoteException {
		return (RegulationsBusiness) IBOLookup.getServiceInstance(iwc, RegulationsBusiness.class);
	}	
	
	protected VATBusiness getVATBusiness(IWApplicationContext iwc) throws RemoteException {
		return (VATBusiness) IBOLookup.getServiceInstance(iwc, VATBusiness.class);
	}
	private SchoolBusiness getSchoolBusiness(IWContext iwc) throws RemoteException{
		return (SchoolBusiness) IBOLookup.getServiceInstance(iwc, SchoolBusiness.class);
	}	
	
	private void checkNotNull(IWContext iwc, String par, Map errorMessages, String errorPar, String errorMsg){
		if (iwc.getParameter(par) == null || iwc.getParameter(par).length() == 0){
			errorMessages.put(errorPar, errorMsg);
		}
	}
		
}
