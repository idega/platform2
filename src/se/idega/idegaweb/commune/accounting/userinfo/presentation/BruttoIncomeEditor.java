/*
 * Created on Aug 18, 2003
 *
 */
package se.idega.idegaweb.commune.accounting.userinfo.presentation;

import is.idega.idegaweb.member.presentation.UserSearcher;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;
import se.idega.idegaweb.commune.accounting.userinfo.business.UserInfoService;
import se.idega.idegaweb.commune.accounting.userinfo.data.BruttoIncome;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;

import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * BruttoIncomeEditor used to create and list brutto income records for user
 * @author aron 
 * @version 1.0
 */

public class BruttoIncomeEditor extends AccountingBlock {
	
	private static final String searchIdentifier = "brtic";
	public static String PRM_USER_ID = UserSearcher.getUniqueUserParameterName(searchIdentifier);
	private static String PRM_CREATE = "brtic_create";
	private static String PRM_CANCEL = "brtic_cancel";
	private static String PRM_SAVE = "brtic_save";
	private static String PRM_DELETE = "brtic_delete";
	private static String localizePrefix = "brutto_income.";
	private User user = null;
	private Integer userID = null;
	private Table mainTable = null;
	private int mainRow = 1;
	private boolean showCancelCloseButton = true;
	

	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		debugParameters(iwc);
		init( iwc);
		process(iwc);
		presentate(iwc);
	}
	
	public void init(IWContext iwc){
		if(iwc.isParameterSet(PRM_USER_ID)){
			userID = Integer.valueOf(iwc.getParameter(PRM_USER_ID));
			try {
				user = getUserService(iwc).getUser(userID);
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void process(IWContext iwc){
		if(iwc.isParameterSet(PRM_SAVE)){
			if(userID!=null && iwc.isParameterSet("brt_income_amount") && iwc.isParameterSet("brt_valid_from")){
				Float income = Float.valueOf(iwc.getParameter("brt_income_amount"));
				IWTimestamp validFrom = new IWTimestamp(iwc.getParameter("brt_valid_from"));
				try {
					getUserInfoService(iwc).createBruttoIncome(userID,income,validFrom.getDate(),new Integer(iwc.getCurrentUserId()));
				}
				catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void presentate(IWContext iwc){
		mainTable = new Table();
		//mainTable.setWidth(this.getWidth());
		add(mainTable);
		presentateHeader(iwc);
		if(isCreateView(iwc))
			presentateCreateRecord(iwc);
		else
			presentateList(iwc);
		
	}
	
	private void presentateHeader(IWContext iwc){
		// set up user search if no user selected
		if(user==null){
			UserSearcher searcher = new UserSearcher();
			searcher.setUniqueIdentifier(searchIdentifier);
			searcher.setShowMiddleNameInSearch(false);
			addToMain(searcher);
		}
		else{
			presentateUserHeader( iwc);
		}
	}
	
	protected boolean isCreateView(IWContext iwc){
		return iwc.isParameterSet(PRM_CREATE);
	}
	
	protected boolean isListView(IWContext iwc){
		return !isCreateView(iwc);
	}
	
	private PresentationObject getButtons(IWContext iwc){
		Table buttonTable = new Table();
		int row = 1;
		int col = 1;
		if(isCreateView(iwc)){
			SubmitButton btnSave = new SubmitButton(localize("save","Save"),PRM_SAVE,"true");
			buttonTable.add(getButton(btnSave),col++,row);
			SubmitButton btnCancel = new SubmitButton(localize("cancel","Cancel"));
			buttonTable.add(getButton(btnCancel),col++,row);
		}
		else {
			SubmitButton btnNew = new SubmitButton(localize("new","New"),PRM_CREATE,"true");
			buttonTable.add(getButton(btnNew),col++,row);
			SubmitButton btnDelete = new SubmitButton(localize("delete","Delete"),PRM_CREATE,"true");
			String deleteWarning = localize("warning.selected_items_willl_be_deleted","Selected items will be deleted");
			btnDelete.setOnClick("return confirm("+deleteWarning+");");
			buttonTable.add(getButton(btnDelete),col++,row);
			if(showCancelCloseButton){
				CloseButton btnClose = new CloseButton(localize("close","Close"));
				buttonTable.add(getButton(btnClose),col++,row);
			}
		}
		return buttonTable;
	}
	
	private void presentateUserHeader(IWContext iwc){
		Table table = new Table();
		table.setWidth(Table.HUNDRED_PERCENT);
		table.add(getHeader(localize("personal_id","Personal ID")),1,1);
		table.add(getHeader(localize("name","Name")),3,1);
		if(user !=null){
			table.add(getText(user.getPersonalID()),2,1);
			table.add(getText(user.getNameLastFirst()),4,1);
		}
		addToMain(table);
	}
	
	private void presentateList(IWContext iwc){
		Table table = new Table();
		Form form = new Form();
		form.add(table);
		//table.setWidth(Table.HUNDRED_PERCENT);
		int row = 1;
		table.add(getHeader(localize("brutto_income","Brutto income")),1,row);
		table.add(getHeader(localize("from_date","From date")),2,row);
		table.add(getHeader(localize("creation_date","Creation date")),3,row);
		table.add(getHeader(localize("creator","Creator")),4,row);
		row++;
		NumberFormat nf = NumberFormat.getInstance(iwc.getCurrentLocale());
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT,iwc.getCurrentLocale());
		DateFormat tf = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT,iwc.getCurrentLocale());
		if(userID!=null){
			try {
				Collection incomes = getUserInfoService(iwc).getBruttoIncomeHome().findByUser(userID);
				if(incomes!=null && !incomes.isEmpty()){
					for (Iterator iter = incomes.iterator(); iter.hasNext();) {
						BruttoIncome income = (BruttoIncome) iter.next();
						table.add(getText(nf.format(income.getIncome().doubleValue())),1,row);
						table.add(getText(df.format(income.getValidFrom())),2,row);
						table.add(getText(tf.format(income.getCreated())),3,row);
						User creator = null;
						try {
							creator = getUserService(iwc).getUser(income.getCreatorID());
						}
						catch (RemoteException e) {
							e.printStackTrace();
						}
						if(creator!=null)
							table.add(getText(creator.getNameLastFirst()),4,row);
						
						row++;
					}
				}
				
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			catch (FinderException e) {
				e.printStackTrace();
			}
		}
		form.maintainParameter(PRM_USER_ID);
		table.mergeCells(1,row,table.getColumns(),row);
		table.add(getButtons(iwc),1,row);
		addToMain(form);
	}
	
	private void presentateCreateRecord(IWContext iwc){
		Table table = new Table();
		Form form = new Form();
		form.add(table);
			table.add(getHeader(localize("new_record","New record")),1,1);
			table.add(getHeader(localize("brutto_income_amount","Brutto Income amount")),1,2);
			table.add(getHeader(localize("valid_from_date","Valid from")),1,3);
			TextInput incomeInput = new TextInput("brt_income_amount");
			incomeInput.setAsIntegers(localize("warning.please_enter_number","Please enter a valid number"));
			setStyle(incomeInput,STYLENAME_INTERFACE);
			table.add(incomeInput,2,2);
			
			DateInput validFromInput = new DateInput("brt_valid_from");
			validFromInput.setToDisplayDayLast(true);
			IWTimestamp today = IWTimestamp.RightNow();
			validFromInput.setDate(today.getDate());
			int currentYear = today.getYear();
			validFromInput.setYearRange(currentYear-5,currentYear+5);
			setStyle(validFromInput,STYLENAME_INTERFACE);
			table.add(validFromInput,2,3);
			
			table.add(Text.getBreak(),1,4);
		
			//SubmitButton btnSave = new SubmitButton(localize("save","Save"),PRM_SAVE,"true");
			//SubmitButton btnCancel = new SubmitButton(localize("cancel","Cancel"),PRM_CANCEL,"true");
			form.maintainParameter(PRM_USER_ID);
			
			
			//table.add(getButton(btnSave),1,5);
			//table.add(getButton(btnCancel),2,5);			
			table.mergeCells(1,5,table.getColumns(),5);
			table.add(getButtons(iwc),1,5);
		addToMain(form);
	}
	
	private void addToMain(PresentationObject obj){
		mainTable.add(obj,1,mainRow++);
	}
	
	
	
	private PresentationObject getCancelButton(){
		return new CloseButton(localize("cancel","Cancel"));
	}
	
	private CommuneUserBusiness getUserService(IWContext iwc) throws RemoteException {
		return (CommuneUserBusiness) IBOLookup.getServiceInstance(iwc, CommuneUserBusiness.class);
	}
	
	private UserInfoService getUserInfoService(IWApplicationContext iwac) throws RemoteException{
		return (UserInfoService) IBOLookup.getServiceInstance(iwac,UserInfoService.class);
	}

	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.presentation.CommuneBlock#localize(java.lang.String, java.lang.String)
	 */
	public String localize(String textKey, String defaultText) {
		// TODO Auto-generated method stub
		return super.localize(localizePrefix+textKey, defaultText);
	}


}
