/*
 * Created on 24.9.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.accounting.invoice.presentation;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.user.business.UserBusiness;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.user.presentation.UserChooser;

import se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry;
import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;
import se.idega.idegaweb.commune.accounting.presentation.ButtonPanel;
import se.idega.idegaweb.commune.accounting.regulations.business.RegulationException;
import se.idega.idegaweb.commune.accounting.regulations.business.RegulationsBusiness;
import se.idega.idegaweb.commune.accounting.regulations.business.VATBusiness;

/**
 * @author Roar
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class RegularInvoiceEntriesList extends AccountingBlock {








	private static final String KEY_AMOUNT_PR_MONTH = "amount_pr_month";
	private static final String KEY_AMOUNT = "amount";
	private static final String KEY_CANCEL = "cancel";
	private static final String KEY_DAY_CREATED = "day_created";
	private static final String KEY_DAY_REGULATED = "day_regulated";
	private static final String KEY_DELETE = "delete";
	private static final String KEY_DOUBLE_ENTRY_ACCOUNT = "double_entry_account";
	private static final String KEY_FIRSTNAME = "first_name";	
	private static final String KEY_FROM = "from";
	private static final String KEY_INVOICE_PERIODE = "invoice_periode";
	private static final String KEY_NEW = "new";
	private static final String KEY_NOTE = "note";
	private static final String KEY_MAIN_BUSINESS = "main_business";	
	private static final String KEY_OWN_POSTING = "own_posting";
	private static final String KEY_PERIODE = "periode";
	private static final String KEY_PLACING = "placing";
	private static final String KEY_REGULATION_TYPE = "regulation_type";
	private static final String KEY_REMARK = "remark";
	private static final String KEY_SAVE = "save";
	private static final String KEY_SEARCH = "search";
	private static final String KEY_SURNAME ="surname";	
	private static final String KEY_SIGNATURE = "signature";
	private static final String KEY_SSN = "ssn";	
	private static final String KEY_TO = "to";
	private static final String KEY_VALID_DATE = "valid_date";
	private static final String KEY_VAT_PR_MONTH = "vat_pr_month";
	private static final String KEY_VAT_TYPE = "vattype";
	
	private static final String PAR_AMOUNT_PR_MONTH = KEY_AMOUNT_PR_MONTH;
	private static final String PAR_DOUBLE_ENTRY_ACCOUNT  = KEY_DOUBLE_ENTRY_ACCOUNT;
	private static final String PAR_FIRST_NAME = KEY_FIRSTNAME;	
	private static final String PAR_FROM = KEY_FROM;
	private static final String PAR_PLACING = KEY_PLACING;
	private static final String PAR_REGULATION_TYPE = KEY_REGULATION_TYPE;
	private static final String PAR_REMARK = KEY_REMARK;
	private static final String PAR_SSN = KEY_SSN;	
	private static final String PAR_SURNAME = KEY_SURNAME;	
	private static final String PAR_TO = KEY_TO;
	private static final String PAR_OWN_POSTING = KEY_OWN_POSTING;	
	private static final String PAR_VAT_PR_MONTH = KEY_VAT_PR_MONTH;
	private static final String PAR_VAT_TYPE = KEY_VAT_TYPE;
	private static final String PAR_VALID_DATE = KEY_VALID_DATE;

	private static final String PARAM_USER = "user";
	


	private static final int 
		ACTION_SHOW = 0, 
		ACTION_NEW = 1, 
		ACTION_DELETE = 2, 
		ACTION_CANCEL = 3,
		ACTION_EDIT = 4, 
		ACTION_SEARCH_PEOPLE = 5, 
		ACTION_SEARCH_INVOICE = 6,
		ACTION_SEARCH_REGULATION = 7,
		ACTION_SAVE = 8,
		ACTION_CANCEL_NEW_EDIT = 9;
			
	private static final String PAR = "PARAMETER_";
	private static final String 
		PAR_NEW = PAR + ACTION_NEW, 
		PAR_DELETE = PAR + ACTION_DELETE, 
		PAR_CANCEL = PAR + ACTION_CANCEL,
		//PAR_EDIT = PAR + ACTION_EDIT, 
		PAR_SEARCH_PEOPLE = PAR + ACTION_SEARCH_PEOPLE, 
		PAR_SEARCH_INVOICE = PAR + ACTION_SEARCH_INVOICE,
		PAR_SEARCH_REGULATION = PAR + ACTION_SEARCH_REGULATION,
		PAR_SAVE = PAR + ACTION_SAVE,
		PAR_CANCEL_NEW_EDIT = PAR + ACTION_CANCEL_NEW_EDIT;


	
	public void init(final IWContext iwc) {
		
			
		try {
			int action = parseAction(iwc);
			switch (action) {
				case ACTION_SHOW:
					handleShow(iwc);
					break;
				case ACTION_NEW:
					handleCreateAction(iwc);
					break;
				case ACTION_DELETE:
					handleDefaultAction(iwc);
					break;
				case ACTION_CANCEL:
					handleDefaultAction(iwc);
					break;
				case ACTION_EDIT:
					handleEditAction(iwc);
					break;
				case ACTION_SEARCH_PEOPLE:
					handleDefaultAction(iwc);
					break;
				case ACTION_SEARCH_INVOICE:
					handleDefaultAction(iwc);
					break;
				case ACTION_SEARCH_REGULATION:
					handleDefaultAction(iwc);
					break;	
				case ACTION_SAVE:
					handleDefaultAction(iwc);
					break;	
				case ACTION_CANCEL_NEW_EDIT:
					handleDefaultAction(iwc);
					break;					
			}
		}
		catch (Exception e) {
			add(new ExceptionWrapper(e, this));
		}
	}
	
	/*
	 * Returns the action constant for the action to perform based 
	 * on the POST parameters in the specified context.
	 */
	private int parseAction(IWContext iwc) {
		int action = ACTION_SHOW;
		for (int a = 0; a < ACTION_CANCEL_NEW_EDIT; a++){
			if (iwc.isParameterSet(PAR + a)){
				action = a;
				break;					
			}
		}
		return action;
	}	

	private void handleCreateAction(IWContext iwc){
		handleEditAction(iwc);
	}
	
	private void handleEditAction(IWContext iwc){
		Collection regTypes = new ArrayList(), vatTypes = new ArrayList();
		try {
			regTypes = getRegulationsBusiness(iwc.getApplicationContext()).findAllRegulationSpecTypes();
			vatTypes = getVATBusiness(iwc.getApplicationContext()).findAllVATRegulations();				
		} catch (RegulationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
		
		add(getDetailPanel(
			iwc, 
			"BOMS", 
			iwc.getParameter(PAR_SSN), 
			iwc.getParameter(PAR_SURNAME),
			iwc.getParameter(PAR_FIRST_NAME),
			iwc.getParameter(PAR_PLACING),			
			iwc.getParameter(PAR_VALID_DATE),
			iwc.getParameter(PAR_FROM),
			iwc.getParameter(PAR_TO),
			"[created date]",
			"[sign]",
			"[reg date]",
			"[sign]",
			iwc.getParameter(PAR_AMOUNT_PR_MONTH),
			iwc.getParameter(PAR_VAT_PR_MONTH),
			iwc.getParameter(PAR_REMARK),
			//iwc.getParameter(PAR_REGULATION_TYPE),
			regTypes,
			iwc.getParameter(PAR_OWN_POSTING),
			iwc.getParameter(PAR_DOUBLE_ENTRY_ACCOUNT),
			//iwc.getParameter(PAR_VAT_TYPE),
			vatTypes));
	}

		
	private void handleDefaultAction(IWContext iwc){
		handleShow(iwc);
	}

	public void handleShow(IWContext iwc) {

		
		add(getFirstPage(iwc));
	}
	

	private Table getFirstPage(IWContext iwc){
		Table table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		
		table.add(getTopPanel(iwc, "Barnomsorg", "888888-8888", "Larsson", "Sofie", "1234", "5678"), 1, 1);
		table.add(getInvoiceList(null), 1, 2);
		ButtonPanel bp = new ButtonPanel(this);
		bp.addLocalizedButton(PAR_NEW, KEY_NEW, "New");
		bp.addLocalizedButton(PAR_DELETE, KEY_DELETE, "Delete");
		bp.addLocalizedButton(PAR_CANCEL, KEY_CANCEL, "Cancel");
	
		table.add(bp, 1, 3);
		
		return table;		
	}
	
	/**
	 * Creates the top panel
	 * @param mainBuiness
	 * @param ssn
	 * @param surname
	 * @param firstName
	 * @param from
	 * @param to
	 * @return
	 */
	
	private Table getTopPanel(IWContext iwc, String mainBusiness, String ssn, String surname, String firstName, String from, String to){
		Table table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());	
		addPersonInformation(iwc, table, mainBusiness, ssn, surname, firstName);
		addPeriode(table, from, to);	
		return table;
	}
	
	private int addPersonInformation(IWContext iwc, Table table, String mainBuiness, String ssn, String surname, String firstName){
		int row = 1;
		
		Collection users = new ArrayList();
		UserChooser uc = new UserChooser(PARAM_USER);
		try {
			users = getUserBusiness(iwc).getUsersWithContract();
		} catch (FinderException e){
			e.printStackTrace();
		} catch (RemoteException e){
			e.printStackTrace();			
		}		
	
		PresentationObject p = this;
		uc.setParentObject(p);
		uc.setValidUserPks(users);	

		table.add(uc, 1, row++);
		
		table.add(getLocalizedLabel(KEY_MAIN_BUSINESS, "Main business"), 1, row);
		table.add(getText(mainBuiness), 2, row++);
		 
		table.add(getLocalizedLabel(KEY_SSN, "Social security number"), 1, row);
		table.add(getText(ssn), 2, row);	
		
		table.add(getLocalizedLabel(KEY_SURNAME, "Surname"), 4, row);
		table.add(getText(surname), 5, row);		
		
		table.add(getLocalizedLabel(KEY_FIRSTNAME, "First name"), 7, row);
		table.add(getText(firstName), 8, row);			
		
		table.add(getLocalizedButton(PAR_SEARCH_PEOPLE, "KEY_SEARCH", "Search"), 10, row);
		
		return row + 1;
	}
	

	private Table addPeriode(Table table, String from, String to){
		table.add(getLocalizedLabel("KEY_PERIODE", "Periode"), 1, 3);
		table.add(getText(from), 2, 3);			
		table.add(getText(" -"), 2, 3);			
		table.add(getText(to), 2, 3);	
		table.add(getLocalizedButton(PAR_SEARCH_INVOICE, "KEY_SEARCH", "Search"), 10, 3);
		return table;	
	}	
	
	private Table getInvoiceList(Collection invoices){
		Table table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
				
		table.add(getLocalizedHeader(KEY_INVOICE_PERIODE, "Invoice periode"), 1, 1);
		table.add(getLocalizedHeader(KEY_PLACING, "Placing"), 2, 1);
		table.add(getLocalizedHeader(KEY_AMOUNT, "Amount"), 3, 1);
		table.add(getLocalizedHeader(KEY_NOTE, "Note"), 4, 1);
		
		if (invoices != null){
			Iterator i = invoices.iterator();
			int row = 2;
			while(i.hasNext()){
				RegularInvoiceEntry entry = (RegularInvoiceEntry) i.next();
				table.add(getText(""+entry.getFrom() + " - " + entry.getTo()), 1, row);
				table.add(getText(entry.getPlacing()), 2, row);
				table.add(getText(""+entry.getAmount()), 3, row);
				table.add(getText(entry.getNote()), 4, row);
				row++;
			}
		}

		return table;
	}
	

	
	private Table getDetailPanel(
			IWContext iwc,
			String mainBusiness,
			String ssn,
			String surname,
			String firstname,
			String placing,
			String validDate,
			String from,
			String to,
			String dayCreated,
			String cratedSignature,
			String dayRegulated,
			String regulatedSignature,
			String amountPrMonth,
			String vatPrMonth,
			String remark,
			//String regulationType,
			Collection regTypeOptions,
			String ownPosting,
			String doubleEntryAccount,
			//String vat,			
			Collection vatOptions
			) {	
				
	
				
		Table table = new Table();
		int row = addPersonInformation(iwc, table, mainBusiness, ssn, surname, firstname);
		addField(table, KEY_PLACING, placing, 1, row);
		addField(table, KEY_VALID_DATE, validDate, 3, row);	
		table.add(getLocalizedButton(PAR_SEARCH_REGULATION, KEY_SEARCH, "Search"), 5, row++);
		table.add(getLocalizedText(KEY_PERIODE, "Periode"), 1, row);
		addField(table, PAR_FROM, KEY_FROM, from, 3, row);
		
		addField(table, PAR_TO, KEY_TO, to, 5, row++);
		addField(table, KEY_DAY_CREATED, dayCreated, 1, row);
		addField(table, KEY_SIGNATURE, cratedSignature, 5, row++);
		addField(table, KEY_DAY_REGULATED, dayRegulated, 1, row);
		addField(table, KEY_SIGNATURE, regulatedSignature, 5, row++);
		addField(table, PAR_AMOUNT_PR_MONTH, KEY_AMOUNT_PR_MONTH, amountPrMonth, 1, row++);
		addField(table, PAR_VAT_PR_MONTH, KEY_VAT_PR_MONTH, vatPrMonth, 1, row++);
		addField(table, PAR_REMARK, KEY_REMARK, remark, 1, row++);
		addDropDown(table, PAR_REGULATION_TYPE, KEY_REGULATION_TYPE, regTypeOptions, "getRegSpecType", 1, row++);
		
		addField(table, PAR_OWN_POSTING, KEY_OWN_POSTING, ownPosting, 1, row++);
		addField(table, PAR_DOUBLE_ENTRY_ACCOUNT, KEY_DOUBLE_ENTRY_ACCOUNT, doubleEntryAccount, 1, row++);
		addDropDown(table, PAR_VAT_TYPE, KEY_VAT_TYPE, vatOptions, "getVatRule", 1, row++);
		
		ButtonPanel bp = new ButtonPanel(this);
		bp.addLocalizedButton(PAR_SAVE, KEY_SAVE, "Save");
		bp.addLocalizedButton(PAR_CANCEL_NEW_EDIT, KEY_CANCEL, "Delete");
		table.add(bp, 1, row);
		
		return table;
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
	private Table addDropDown(Table table, String parameter, String key, Collection options, String method, int col, int row) {
		return addWidget(table, key, getDropdownMenu(parameter, options, method), col, row);		
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
	private Table addField(Table table, String parameter, String key, String value, int col, int row){
		return addWidget(table, key, getTextInput(parameter, value), col, row);
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
	private Table addField(Table table, String key, String value, int col, int row){
		return addWidget(table, key, getText(value), col, row);
	}	
	
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
	
	protected UserBusiness getUserBusiness(IWApplicationContext iwc) throws RemoteException {
		return (UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class);
	}	
	
	

}
