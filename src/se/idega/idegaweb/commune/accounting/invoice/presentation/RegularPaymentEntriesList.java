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
import java.sql.SQLException;
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
import se.idega.idegaweb.commune.accounting.regulations.data.Regulation;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType;
import se.idega.idegaweb.commune.accounting.regulations.data.VATRule;
import se.idega.idegaweb.commune.accounting.school.presentation.PostingBlock;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolHome;
import com.idega.business.IBOLookup;
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
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.User;

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
	

	private String LOCALIZER_PREFIX = "regular_payment_entries_list.";
	
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
	public static final String PAR_SELECTED_PROVIDER = "selected_provider";	
	
	private static final String PAR_PK = "pk";	
	private static final String PAR_DELETE_PK = "delete_pk";
	
	private static final int MIN_LEFT_COLUMN_WIDTH = 150;	
	

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
		ACTION_OPFIELD_MAINSCREEN = 11;
			
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
					handleDefaultAction(iwc, school, fromDate, toDate);				
					
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
			entry.delete();
		} catch(SQLException ex){
			ex.printStackTrace();
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
				

		RegularPaymentEntry entry = null;
		
		if (iwc.getParameter(PAR_PK) != null){
			entry = getRegularPaymentEntry(iwc.getParameter(PAR_PK));
		}
		
		if (entry == null){
			try{
				entry = getRegularPaymentEntryHome().create();
			
			}catch(CreateException ex2){
				ex2.printStackTrace();
				return;
			}			
		}

		entry.setAmount(new Float(iwc.getParameter(PAR_AMOUNT_PR_MONTH)).floatValue());

		Date from = parseDate(iwc.getParameter(PAR_FROM));
		Date to = parseDate(iwc.getParameter(PAR_TO));
		entry.setFrom(from);
		entry.setTo(to);
			
		entry.setNote(iwc.getParameter(PAR_REMARK));
		entry.setPlacing(iwc.getParameter(PAR_PLACING));
		entry.setVAT(new Float(iwc.getParameter(PAR_VAT_PR_MONTH)).floatValue());
		if (iwc.getParameter(PAR_SELECTED_PROVIDER) != null){
			entry.setSchoolId(new Integer(iwc.getParameter(PAR_SELECTED_PROVIDER)).intValue());
		}
		
		entry.setUser(getUser(iwc));
		entry.setVatRuleId(new Integer(iwc.getParameter(PAR_VAT_TYPE)).intValue());
		
		try{
			PostingBlock p = new PostingBlock(iwc);			
			entry.setOwnPosting(p.getOwnPosting());
			entry.setDoublePosting(p.getDoublePosting());
		} catch (PostingParametersException e) {
			errorMessages.put(ERROR_POSTING, localize(e.getTextKey(), e.getTextKey()) + e. getDefaultText());
		}	
					
//			entry.setOwnPosting(iwc.getParameter(PAR_OWN_POSTING));
//			entry.setDoublePosting(iwc.getParameter(PAR_DOUBLE_ENTRY_ACCOUNT));
		if (from == null || to == null){
			errorMessages.put(ERROR_DATE_FORMAT, localize(LOCALIZER_PREFIX + "date_format_yymm_warning", "Wrong date format. use: yymm."));
		} else if (to.before(from)){
			errorMessages.put(ERROR_DATE_PERIODE_NEGATIVE, localize(LOCALIZER_PREFIX + "negative_periode", "Neagtive periode"));
		} 
		if (entry.getPlacing() == null || entry.getPlacing().length() == 0){
			errorMessages.put(ERROR_PLACING_EMPTY, localize(LOCALIZER_PREFIX + "placing_null", "Placing must be given a value"));
		} 
//			if (entry.getAmount() == 0){
//				errorMessages.put(ERROR_AMOUNT_EMPTY, localize(LOCALIZER_PREFIX + "amount_null", "Amount must be given a value"));
//			}

		if (entry.getOwnPosting() == null || entry.getOwnPosting().length() == 0){
			errorMessages.put(ERROR_OWNPOSTING_EMPTY, localize(LOCALIZER_PREFIX + "own_posting_null", "Own posting must be given a value"));
		}
	
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
		Table t1 = new Table();
		
		t1.setCellpadding(getCellpadding());
		t1.setCellspacing(getCellspacing());

		t1.add(getOperationalFieldPanel(PAR_OPFIELD_DETAILSCREEN), 1, 1);
		
		Collection vatTypes = new ArrayList();
		try {
			vatTypes = getRegulationsBusiness(iwc.getApplicationContext()).findAllVATRules();			
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}	
		
		Form form = new Form();
		form.maintainParameter(PAR_PK);		
		form.maintainParameter(PAR_USER_SSN);			
		form.maintainParameter(PAR_SEEK_FROM);
		form.maintainParameter(PAR_SEEK_TO);
		form.maintainParameter(PAR_SELECTED_PROVIDER);

	
		form.add(getDetailPanel(iwc, entry, vatTypes, errorMessages));
		
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
		
		Table t1 = new Table();
		t1.setCellpadding(getCellpadding());
		t1.setCellspacing(getCellspacing());
		
		int row = 1;
		t1.add(getOperationalFieldPanel(PAR_OPFIELD_MAINSCREEN), 1, row++); 
				
		addPeriodeForm(iwc, t1, fromDate, toDate, errorMessage, row++);
			
		Table t2 = new Table();				
		t2.add(getPaymentsList(entries, school, fromDate, toDate), 1, 1);
	
		ButtonPanel bp = new ButtonPanel(this);
		bp.addLocalizedButton(PAR_NEW, KEY_NEW, "New");
		t2.add(bp, 1, 2);


//		form.add(t2);	
		t1.mergeCells(1, row, 10, row);	
		t1.add(t2, 1, row);		
		Form form = new Form();		
		form.maintainAllParameters();		
		form.add(new HiddenInput(PAR_DELETE_PK, "-1"));
		form.add(t1);
//		t1.add(form, 1, row++);	

		Table t0 = new Table();
		t0.add(form);

		return t0;		
	}
	

	private Table getOperationalFieldPanel(String actionCommand) {
		
		Table inner = new Table();

		inner.add(getLocalizedLabel(KEY_OPERATIONAL_FIELD, "Huvudverksamhet"), 1, 1);
		OperationalFieldsMenu ofm = new OperationalFieldsMenu();
		ofm.setParameter(actionCommand, " ");
		ofm.maintainParameter(PAR_SEEK_TO);
		ofm.maintainParameter(PAR_SEEK_FROM);
		ofm.maintainParameter(PAR_USER_SSN);	
	
		inner.add(ofm, 2, 1);
		inner.setColumnWidth(1, "" + MIN_LEFT_COLUMN_WIDTH);		
//		inner.add(new HiddenInput(actionCommand, " ")); //to make it return to the right page
		return inner;
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
		searcher.setToFormSubmit(true);

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
					
					list.add(getText(formatCurrency(entry.getAmount())));
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
				
		final int EMPTY_ROW_HEIGHT = 8;
		Table table = new Table();
		int row = 1;
//		table.mergeCells(1,1,3,1);
//		table.add(getOperationalFieldPanel(PAR_OPFIELD_DETAILSCREEN), 1, row++);
//		
//			
//		table.setHeight(row++, EMPTY_ROW_HEIGHT);
		

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
		
		regSearchPanel.maintainParameter(new String[]{PAR_USER_SSN, PAR_SEEK_FROM, PAR_SEEK_TO, PAR_FROM, PAR_TO, PAR_AMOUNT_PR_MONTH, PAR_PK});
//		if (errorMessages.get(ERROR_PLACING_EMPTY) != null) {
//			searchPanel.setError((String) errorMessages.get(ERROR_PLACING_EMPTY));			
//		}		

		
		regSearchPanel.setParameter(PAR_EDIT_FROM_SCREEN, " ");
		table.mergeCells(1, row, 10, row);
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

		addFloatField(table, PAR_AMOUNT_PR_MONTH, KEY_AMOUNT_PR_MONTH, ""+entry.getAmount(), 1, row++);
		//Vat is currently set to 0
		addFloatField(table, PAR_VAT_PR_MONTH, KEY_VAT_PR_MONTH, ""+0, 1, row++);

		table.setHeight(row++, EMPTY_ROW_HEIGHT);

		addField(table, PAR_REMARK, KEY_REMARK, entry.getNote(), 1, row++, 300);

		table.setHeight(row++, EMPTY_ROW_HEIGHT);

		
		if (errorMessages.get(ERROR_POSTING) != null) {
			table.add(getErrorText((String) errorMessages.get(ERROR_POSTING)), 2, row++);			
		} else if (errorMessages.get(ERROR_OWNPOSTING_EMPTY) != null) {
			table.add(getErrorText((String) errorMessages.get(ERROR_OWNPOSTING_EMPTY)), 2, row++);			
		} else if (postingError != null){
			table.add(getErrorText(postingError), 2, row++);				
		}

		
		table.mergeCells(1, row, 10, row);
		PostingBlock postingBlock = new PostingBlock(entry.getOwnPosting(), entry.getDoublePosting());
		table.add(postingBlock, 1, row++);
						
		
//		addField(table, PAR_OWN_POSTING, KEY_OWN_POSTING, entry.getOwnPosting(), 1, row++);
//		addField(table, PAR_DOUBLE_ENTRY_ACCOUNT, KEY_DOUBLE_ENTRY_ACCOUNT, entry.getDoublePosting(), 1, row++);
		addDropDownLocalized(table, PAR_VAT_TYPE, KEY_VAT_TYPE, vatTypes, entry.getVatRuleId(),  "getVATRule", 1, row++);
		
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
		return getNotStoredEntry(null);		
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
				return _reg != null ? _reg.getPeriodFrom() : getDateValue(PAR_FROM);
			}
		
			public Date getTo() {
				return  _reg != null ? _reg.getPeriodTo() : getDateValue(PAR_TO);
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
				if (_posting != null && _posting.length >= 1){
					return _posting[0];
				}
				return getValue(PAR_OWN_POSTING);
			}
		
			public String getDoublePosting() {
				if (_posting != null && _posting.length >= 2){
					return _posting[1];
				}					
				return getValue(PAR_DOUBLE_ENTRY_ACCOUNT);
			}
		
			public float getAmount() {
				return _reg != null ? _reg.getAmount().floatValue() : getFloatValue(PAR_AMOUNT_PR_MONTH);
			}
		
			public float getVAT() {
				return _reg != null ? _reg.getVATEligible().floatValue() : getFloatValue(PAR_VAT_PR_MONTH);
			}
		
			public VATRule getVatRule() {
				return null;
			}
		
			public int getVatRuleId() {
				return getIntValue(PAR_VAT_TYPE);
			}
		
			public String getNote() {
				return getValue(PAR_REMARK);
			}
		
			public Date getCreatedDate() {
				return null;
			}
		
			public String getCreatedName() {
				return null;
			}
		
			public Date getEditDate() {
				return null;
			}
		
			public String getEditName() {
				return null;
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
			public void setVAT(float vat) {}
			public void setVatRuleId(int vatRuleId) {}
			public void setNote(String note) {}
			public void setOwnPosting(String ownPosting) {}
			public void setDoublePosting(String doublePosting) {}
			public void delete() throws SQLException {}
			public void store() throws IDOStoreException {}
			public IDOEntityDefinition getEntityDefinition() {return null;}
			public EJBLocalHome getEJBLocalHome() throws EJBException {return null;}
			public Object getPrimaryKey() throws EJBException {return null;}
			public void remove() throws RemoveException, EJBException {}
			public boolean isIdentical(EJBLocalObject arg0) throws EJBException {return false;}
			public int compareTo(Object arg0) {return 0;}
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
	 * Adds a label and a TextInput to a table
	 * @param table
	 * @param key is used both as localization key for the label and default label value
	 * @param value
	 * @param parameter
	 * @param col
	 * @param row
	 * @return
	 */
	private Table addFloatField(Table table, String parameter, String key, String value, int col, int row){
		TextInput input = getTextInput(parameter, value);
		input.setAsFloat(localize(LOCALIZER_PREFIX + "float_format_error", "Format-error: Expecting float:" )+ " " + localize(key, ""), 2); 
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
}
