/*
 * Created on 24.9.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.accounting.invoice.presentation;

import is.idega.idegaweb.member.presentation.UserSearcher;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecord;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordHome;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeader;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderHome;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordHome;
import se.idega.idegaweb.commune.accounting.posting.business.PostingException;
import se.idega.idegaweb.commune.accounting.posting.business.PostingParametersException;
import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;
import se.idega.idegaweb.commune.accounting.presentation.ButtonPanel;
import se.idega.idegaweb.commune.accounting.presentation.OperationalFieldsMenu;
import se.idega.idegaweb.commune.accounting.presentation.RegulationSearchPanel;
import se.idega.idegaweb.commune.accounting.regulations.business.RegulationsBusiness;
import se.idega.idegaweb.commune.accounting.regulations.business.VATBusiness;
import se.idega.idegaweb.commune.accounting.regulations.data.Regulation;
import se.idega.idegaweb.commune.accounting.school.presentation.PostingBlock;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolHome;
import com.idega.block.school.data.SchoolType;
import com.idega.business.IBOLookup;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.Parameter;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.User;

/**
 * @author Roar
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ManuallyPaymentEntriesList extends AccountingBlock {


	private String ERROR_POSTING = "error_posting";
	private String ERROR_OWNPOSTING_EMPTY = "error_ownposting_empty";

	private String LOCALIZER_PREFIX = "regular_payment_entries_list.";
	
	private static final String KEY_OPERATIONAL_FIELD = "operational_field";
	private static final String KEY_AMOUNT_PR_MONTH = "amount_pr_month";
	private static final String KEY_CANCEL = "cancel";
	private static final String KEY_DOUBLE_ENTRY_ACCOUNT = "double_entry_account";
	private static final String KEY_FROM = "from";

	private static final String KEY_OWN_POSTING = "own_posting";
	private static final String KEY_PLACING = "placing";
	private static final String KEY_REMARK = "remark";
	private static final String KEY_SAVE = "save";
	private static final String KEY_TO = "to";
	private static final String KEY_VAT_PR_MONTH = "vat_pr_month";
	private static final String KEY_VAT_TYPE = "vattype";
	private static final String KEY_AMOUNT_PR_ITEM = "amount_pr_item";	
	private static final String KEY_NUMBER_OF_ITEMS = "number_of_items";
	private static final String KEY_SCH_TYPE = "school_type";
	private static final String KEY_SCH_YEAR_HOURS = "school_year_hours";
	private static final String KEY_SCH_GROUP = "school_group";

	
	private static final String PAR_AMOUNT_PR_MONTH = KEY_AMOUNT_PR_MONTH;
	private static final String PAR_DOUBLE_ENTRY_ACCOUNT  = KEY_DOUBLE_ENTRY_ACCOUNT;
	private static final String PAR_SEEK_FROM = "SEEK_" + KEY_FROM;	
	private static final String PAR_PLACING = KEY_PLACING;
	private static final String PAR_REMARK = KEY_REMARK;
	private static final String PAR_TO = KEY_TO;
	private static final String PAR_SEEK_TO = "SEEK_" + KEY_TO;	
	private static final String PAR_USER_SSN = "usrch_search_pid"; //Constant used in UserSearcher...
	private static final String PAR_OWN_POSTING = KEY_OWN_POSTING;	
	private static final String PAR_VAT_PR_MONTH = KEY_VAT_PR_MONTH;
	private static final String PAR_VAT_TYPE = KEY_VAT_TYPE;
	private static final String PAR_SELECTED_PROVIDER = "selected_provider";	
	private static final String PAR_NUMBER_OF_ITEMS  = KEY_NUMBER_OF_ITEMS;
	private static final String PAR_AMOUNT_PR_ITEM = KEY_AMOUNT_PR_ITEM;
	private static final String PAR_SCH_TYPE = KEY_SCH_TYPE;
	private static final String PAR_SCH_YEAR_HOURS = KEY_SCH_YEAR_HOURS;
	private static final String PAR_SCH_GROUP = KEY_SCH_GROUP;	
		
	private static final String PAR_PK = "pk";	
	
	private static final String PAR_USER_ID = "user_id";
	
	private static final int MIN_LEFT_COLUMN_WIDTH = 150;	
	

	private UserSearcher searcher = null;

	private static final int 
		ACTION_SHOW = 0, 
		ACTION_EDIT_FROM_SCREEN = 5, 
		ACTION_SAVE = 8,
		ACTION_CANCEL_NEW_EDIT = 9,
		ACTION_OPFIELD_DETAILSCREEN = 10,
		ACTION_CANCEL = 12;		
			
	private static final String PAR = "PARAMETER_";
	private static final String 
		PAR_EDIT_FROM_SCREEN = PAR + ACTION_EDIT_FROM_SCREEN, 
		PAR_SAVE = PAR + ACTION_SAVE,
		PAR_CANCEL_NEW_EDIT = PAR + ACTION_CANCEL_NEW_EDIT,
		PAR_OPFIELD_DETAILSCREEN = PAR + ACTION_OPFIELD_DETAILSCREEN;
	
//	int ijk = 0;
//	String searchPeopleAction = ""; 

	
	public void init(final IWContext iwc) {
		
//		School school = getSchool(iwc);
		
		int action = parseAction(iwc);
		
		try {
			switch (action) {
				case ACTION_EDIT_FROM_SCREEN:
				case ACTION_OPFIELD_DETAILSCREEN:				
					handleEditAction(iwc);
					break;
				case ACTION_SAVE:
					handleSaveAction(iwc/*, school*/);
					break;	
				case ACTION_CANCEL:
					handleCancelAction();
				
				default:
					handleEditAction(iwc);			
			}
		}
		catch (Exception e) {
			add(new ExceptionWrapper(e, this));
		}
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
	
	
//	private User getUser(IWContext iwc){
//		String userPid = iwc.getParameter(PAR_USER_SSN);
//		User user = null;
//		if (userPid != null && userPid.length() > 0){
//			try{
//				user = getUserBusiness(iwc.getApplicationContext()).getUser(userPid);
//			}catch(FinderException ex){
//				ex.printStackTrace(); 
//			}
//		}
//		return user;	
//	}
		

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


	private void handleCancelAction(){
		
	}
	
	private void handleSaveAction(IWContext iwc /*, School school*/){
		Map errorMessages = new HashMap();
		
		PaymentRecord pay = null;
		PaymentHeader payhdr = null;
		InvoiceRecord inv = null;

		try{
			payhdr = getPaymentHeaderHome().create();
			pay = getPaymentRecordHome().create();
			inv = getInvoiceRecordHome().create();			
		}catch(CreateException ex2){
			ex2.printStackTrace();
			return;
		}			
		
		
		if (iwc.getParameter(PAR_SELECTED_PROVIDER) != null){
			payhdr.setSchoolID(new Integer(iwc.getParameter(PAR_SELECTED_PROVIDER)).intValue());
		}
				
		pay.setPaymentHeader(payhdr);
		inv.setPaymentRecord(pay);
				
		float amountPrMonth = 0;
		try{
			amountPrMonth = new Float(iwc.getParameter(PAR_AMOUNT_PR_MONTH)).floatValue();
		}catch(NumberFormatException ex){
			
		}
		pay.setTotalAmount(amountPrMonth);
		
		pay.setNotes(iwc.getParameter(PAR_REMARK));
//			pay.setPlacing(iwc.getParameter(PAR_PLACING));
		pay.setTotalAmountVAT(new Float(iwc.getParameter(PAR_VAT_PR_MONTH)).floatValue());

//			pay.setUser(getUser(iwc));
		pay.setVATType(new Integer(iwc.getParameter(PAR_VAT_TYPE)).intValue());
		
		try{
			PostingBlock p = new PostingBlock(iwc);			
			pay.setOwnPosting(p.getOwnPosting());
			pay.setDoublePosting(p.getDoublePosting());
		} catch (PostingParametersException e) {
			errorMessages.put(ERROR_POSTING, localize(e.getTextKey(), e.getTextKey()) + e. getDefaultText());
		}	
	
		if (! errorMessages.isEmpty()){
			handleEditAction(iwc, errorMessages);	
		}else{		
	
			payhdr.store();		
			pay.store();		
			inv.store();		
			//TODO return to calling page
		}
	}



	private PaymentRecordHome getPaymentRecordHome() {
		PaymentRecordHome home = null;
		try{
			home = (PaymentRecordHome) IDOLookup.getHome(PaymentRecord.class);
			
		}catch(IDOLookupException ex){
			ex.printStackTrace();			
		}
		return home;
	}
	
	private PaymentHeaderHome getPaymentHeaderHome() {
		PaymentHeaderHome home = null;
		try{
			home = (PaymentHeaderHome) IDOLookup.getHome(PaymentHeader.class);
			
		}catch(IDOLookupException ex){
			ex.printStackTrace();			
		}
		return home;
	}	
		
	
	private InvoiceRecordHome getInvoiceRecordHome() {
		InvoiceRecordHome home = null;
		try{
			home = (InvoiceRecordHome) IDOLookup.getHome(InvoiceRecord.class);
			
		}catch(IDOLookupException ex){
			ex.printStackTrace();			
		}
		return home;
	}		

	private void handleEditAction(IWContext iwc){
		handleEditAction(iwc, new HashMap());
	}
				
	private void handleEditAction(IWContext iwc,Map errorMessages){
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
		form.maintainParameter(PAR_USER_SSN);			
		form.maintainParameter(PAR_SELECTED_PROVIDER);
	
		form.add(getDetailPanel(iwc, vatTypes, errorMessages));
		
		t1.add(form, 1, 2);
		add(t1);
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
	
	
	
	private Table getDetailPanel(IWContext iwc, Collection vatTypes, Map errorMessages){
				
		if (errorMessages == null){
			errorMessages = new HashMap();
		}
		
		final int EMPTY_ROW_HEIGHT = 8;
		Table table = new Table();
		int row = 1;

		//Regulation search panel
		RegulationSearchPanel regSearchPanel = new RegulationSearchPanel(iwc, PAR_SELECTED_PROVIDER); 	
		regSearchPanel.setLeftColumnMinWidth(MIN_LEFT_COLUMN_WIDTH);
				
		regSearchPanel.setPlacingIfNull(getValue(iwc, PAR_PLACING));
		regSearchPanel.setSchoolIfNull(getSchool(iwc));
		

		regSearchPanel.maintainParameter(new String[]{PAR_USER_SSN, PAR_SEEK_FROM, PAR_TO, PAR_AMOUNT_PR_MONTH, PAR_PK});
		
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
		
		table.setHeight(row++, EMPTY_ROW_HEIGHT);

		addFloatField(table, PAR_AMOUNT_PR_ITEM, KEY_AMOUNT_PR_ITEM, getValue(iwc, PAR_AMOUNT_PR_ITEM), 1, row);
		addField(table, PAR_NUMBER_OF_ITEMS, KEY_NUMBER_OF_ITEMS, getValue(iwc, PAR_NUMBER_OF_ITEMS), 3, row++, 30);
		
		
		//Amount, vat, remark
		String amount =(reg != null) ? ""+reg.getAmount() : getValue(iwc, PAR_AMOUNT_PR_MONTH);
		addFloatField(table, PAR_AMOUNT_PR_MONTH, KEY_AMOUNT_PR_MONTH, amount, 1, row++);
		//Vat is currently set to 0
		addFloatField(table, PAR_VAT_PR_MONTH, KEY_VAT_PR_MONTH, "0", 1, row++);
		table.setHeight(row++, EMPTY_ROW_HEIGHT);
		table.mergeCells(2, row, 10, row);
		addField(table, PAR_REMARK, KEY_REMARK, getValue(iwc, PAR_REMARK), 1, row++, 300);

		table.setHeight(row++, EMPTY_ROW_HEIGHT);
		
		try{
			Collection types = getSchoolBusiness(iwc).findAllSchoolTypes();
			SchoolType current = regSearchPanel.getCurrentSchoolType();
			int selected = current != null ? ((Integer) current.getPrimaryKey()).intValue() : -1;
			addDropDown(table, PAR_SCH_TYPE, KEY_SCH_TYPE, types, selected, "getSchoolTypeName", 1, row++);
		}catch(RemoteException ex){
			ex.printStackTrace();
		}
		
		addField(table, PAR_SCH_YEAR_HOURS, KEY_SCH_YEAR_HOURS, getValue(iwc, PAR_SCH_YEAR_HOURS), 1, row++, 40);
		addField(table, PAR_SCH_GROUP, KEY_SCH_GROUP, getValue(iwc, PAR_SCH_GROUP), 1, row++, 60);

		table.setHeight(row++, EMPTY_ROW_HEIGHT);
		
		if (errorMessages.get(ERROR_POSTING) != null) {
			table.add(getErrorText((String) errorMessages.get(ERROR_POSTING)), 2, row++);			
		} else if (errorMessages.get(ERROR_OWNPOSTING_EMPTY) != null) {
			table.add(getErrorText((String) errorMessages.get(ERROR_OWNPOSTING_EMPTY)), 2, row++);			
		} else if (postingError != null){
			table.add(getErrorText(postingError), 2, row++);				
		}

		
		//Posting strings
		table.mergeCells(1, row, 10, row);
		String ownPosting = (reg != null) ? posting[0] : getValue(iwc, PAR_OWN_POSTING);
		String dblPosting = (reg != null) ? posting[1] : getValue(iwc, PAR_DOUBLE_ENTRY_ACCOUNT);
		PostingBlock postingBlock = new PostingBlock(ownPosting, dblPosting);
		table.add(postingBlock, 1, row++);
						
		
//		addField(table, PAR_OWN_POSTING, KEY_OWN_POSTING, entry.getOwnPosting(), 1, row++);
//		addField(table, PAR_DOUBLE_ENTRY_ACCOUNT, KEY_DOUBLE_ENTRY_ACCOUNT, entry.getDoublePosting(), 1, row++);
		addDropDownLocalized(table, PAR_VAT_TYPE, KEY_VAT_TYPE, vatTypes, getIntValue(iwc, PAR_VAT_TYPE),  "getVATRule", 1, row++);
		
		table.setHeight(row++, EMPTY_ROW_HEIGHT);

		//user search		
		table.mergeCells(1, row, 10, row);
		int userId = getIntValue(iwc, PAR_USER_ID);
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

	/**
	 * 
	 */
	private SchoolBusiness getSchoolBusiness(IWContext iwc) throws RemoteException{
		return (SchoolBusiness) IDOLookup.getServiceInstance(iwc, SchoolBusiness.class);
	}

	/**
	 * @param iwc
	 * @param PAR_REMARK
	 * @return
	 */
	private String getValue(IWContext iwc, String parName) {
		if (iwc.getParameter(parName) == null){
			return "";
		} else {
			return iwc.getParameter(parName);
		}
	}
	
	private int getIntValue(IWContext iwc, String parName) {
		try{
			return Integer.parseInt(getValue(iwc, parName));
		}catch(NumberFormatException ex){
			return -1;
		}
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
