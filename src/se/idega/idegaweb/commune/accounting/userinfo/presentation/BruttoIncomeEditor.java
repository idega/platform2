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
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;
import se.idega.idegaweb.commune.accounting.presentation.ApplicationForm;
import se.idega.idegaweb.commune.accounting.presentation.ButtonPanel;
import se.idega.idegaweb.commune.accounting.presentation.ListTable;
import se.idega.idegaweb.commune.accounting.userinfo.business.UserInfoService;
import se.idega.idegaweb.commune.accounting.userinfo.data.BruttoIncome;
import se.idega.idegaweb.commune.accounting.userinfo.data.BruttoIncomeHome;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;

import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.text.Name;
/**
 * BruttoIncomeEditor used to create and list brutto income records for user
 * @author aron 
 * @version 1.0
 */
public class BruttoIncomeEditor extends AccountingBlock {
	private static final String PRM_DEL_ITEM = "brtic_del";
	private static final String searchIdentifier = "brtic";
	public static String PRM_USER_ID = UserSearcher.getUniqueUserParameterName(searchIdentifier);
	private static String PRM_CREATE = "brtic_create";
	//private static String PRM_CANCEL = "brtic_cancel";
	private static String PRM_SAVE = "brtic_save";
	//private static String PRM_DELETE = "brtic_delete";
	private static String localizePrefix = "brutto_income.";
	private User user = null;
	private Integer userID = null;
	private String registerErrorMsg = null;
	private boolean showCancelCloseButton = true;
	private ApplicationForm appForm = null;
	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		//debugParameters(iwc);
		init(iwc);
		process(iwc);
		presentate(iwc);
	}
	public void init(IWContext iwc) {
		if (iwc.isParameterSet(PRM_USER_ID)) {
			userID = Integer.valueOf(iwc.getParameter(PRM_USER_ID));
			try {
				user = getUserService(iwc).getUser(userID);
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	public void process(IWContext iwc) {
		if (iwc.isParameterSet(PRM_SAVE)) {
			if (userID != null && iwc.isParameterSet("brt_income_amount") && iwc.isParameterSet("brt_valid_from")) {
				Float income = Float.valueOf(iwc.getParameter("brt_income_amount"));
				IWTimestamp validFrom = new IWTimestamp(iwc.getParameter("brt_valid_from"));
				try {
					UserInfoService infoService = getUserInfoService(iwc);
					boolean validDate =true;
					try {
						// check if no from date is the same
						Collection incomes = infoService.getBruttoIncomeHome().findByUser(userID);
						if(incomes!=null && !incomes.isEmpty()){
							Iterator iter = incomes.iterator();
							while (iter.hasNext()) {
								BruttoIncome element = (BruttoIncome) iter.next();
								if(validFrom.isEqualTo(new IWTimestamp(element.getValidFrom()))){
									validDate = false;
									break;
								}
							}
						}
								
					}
					catch (FinderException e1) {
						
					}
					if(validDate)
						infoService.createBruttoIncome(userID,income,	validFrom.getDate(),new Integer(iwc.getCurrentUserId()));
					else
						registerErrorMsg = localize("error_same_from_date","Can't register same from date more than once");
				}
				catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
		else
			if (iwc.isParameterSet(PRM_DEL_ITEM)) {
				String[] deleteIDs = iwc.getParameterValues(PRM_DEL_ITEM);
				try {
					BruttoIncomeHome iHome = getUserInfoService(iwc).getBruttoIncomeHome();
					for (int i = 0; i < deleteIDs.length; i++) {
						BruttoIncome income = iHome.findByPrimaryKey(Integer.valueOf(deleteIDs[i]));
						income.remove();
					}
				}
				catch (NumberFormatException e) {
					e.printStackTrace();
				}
				catch (RemoteException e) {
					e.printStackTrace();
				}
				catch (EJBException e) {
					e.printStackTrace();
				}
				catch (FinderException e) {
					e.printStackTrace();
				}
				catch (RemoveException e) {
					e.printStackTrace();
				}
			
		}
	}
	public void presentate(IWContext iwc) {
		appForm = new ApplicationForm(this);
		appForm.setLocalizedTitle(localizePrefix + "title", "Brutto income registration");
		//mainTable.setWidth(this.getWidth());
		add(appForm);
		presentateHeader(iwc);
		if (isCreateView(iwc))
			presentateCreateRecord();
		else
			presentateList(iwc);
		presentateButtons(iwc);
	}
	private void presentateHeader(IWContext iwc) {
		// set up user search if no user selected
		if (user == null) {
			UserSearcher searcher = new UserSearcher();
			searcher.setUniqueIdentifier(searchIdentifier);
			searcher.setShowMiddleNameInSearch(false);
			appForm.setSearchPanel(searcher);
		}
		else {
			presentateUserHeader(iwc);
		}
	}
	protected boolean isCreateView(IWContext iwc) {
		return iwc.isParameterSet(PRM_CREATE) || registerErrorMsg!=null ;
	}
	protected boolean isListView(IWContext iwc) {
		return !isCreateView(iwc);
	}
	private void presentateButtons(IWContext iwc) {
		ButtonPanel bPanel = new ButtonPanel(this);
		if (isCreateView(iwc)) {
			SubmitButton btnSave = new SubmitButton(localize("save", "Save"), PRM_SAVE, "true");
			bPanel.addButton(btnSave);
			SubmitButton btnCancel = new SubmitButton(localize("cancel", "Cancel"));
			bPanel.addButton(btnCancel);
		}
		else {
			SubmitButton btnNew = new SubmitButton(localize("new", "New"), PRM_CREATE, "true");
			bPanel.addButton(btnNew);
			/*SubmitButton btnDelete = new SubmitButton(PRM_DELETE, localize("delete", "Delete"));
			String deleteWarning =
				localize("warning.selected_items_willl_be_deleted", "Selected items will be deleted");
			btnDelete.setOnClick("return confirm('" + deleteWarning + "');");
			bPanel.addButton(btnDelete);
			*/
			if (showCancelCloseButton) {
				CloseButton btnClose = new CloseButton(localize("close", "Close"));
				btnClose.setOnClick("window.opener.location.reload()");
				bPanel.addButton(btnClose);
			}
		}
		appForm.setButtonPanel(bPanel);
	}
	private void presentateUserHeader(IWContext iwc) {
		Table table = new Table();
		//table.setWidth(Table.HUNDRED_PERCENT);
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.add(getHeader(localize("personal_id", "Personal ID")), 1, 1);
		//table.setColor(1, 1, getHeaderColor());
		table.add(getHeader(localize("name", "Name")), 1, 2);
		//table.setColor(1, 2, getHeaderColor());
		if (user != null) {
			table.add(getText(user.getPersonalID()), 2, 1);
			Name name = new Name(user.getFirstName(), user.getMiddleName(), user.getLastName());
			table.add(getText(name.getName(iwc.getApplicationSettings().getDefaultLocale())), 2, 2);
		}
		appForm.setSearchPanel(table);
	}
	private void presentateList(IWContext iwc) {
		ListTable table = new ListTable(this, 5);
		int row = 1;
		table.setHeader(getHeader(localize("brutto_income", "Brutto income")), 1);
		table.setHeader(getHeader(localize("from_date", "From date")), 2);
		table.setHeader(getHeader(localize("creation_date", "Creation date")), 3);
		table.setHeader(getHeader(localize("creator", "Creator")), 4);
		table.setHeader(getHeader(localize("delete", "Delete")), 5);
		row++;
		NumberFormat nf = NumberFormat.getInstance(iwc.getCurrentLocale());
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, iwc.getCurrentLocale());
		DateFormat tf = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, iwc.getCurrentLocale());
		String tooltip  = localize("tooltip_delete_bruttonincome","Delete income");
		if (userID != null) {
			try {
				Collection incomes = getUserInfoService(iwc).getBruttoIncomeHome().findByUser(userID);
				if (incomes != null && !incomes.isEmpty()) {
					for (Iterator iter = incomes.iterator(); iter.hasNext();) {
						BruttoIncome income = (BruttoIncome) iter.next();
						table.add(getText(nf.format(income.getIncome().doubleValue())));
						table.add(getText(df.format(income.getValidFrom())));
						table.add(getText(tf.format(income.getCreated())));
						try {
							User creator = income.getCreator ();
							Name name = new Name(creator.getFirstName(), creator.getMiddleName(), creator.getLastName());
							table.add(getText(name.getName(iwc.getApplicationSettings().getDefaultLocale())));
						}	catch (Exception e) {
							table.skip();
							e.printStackTrace();
						}
						//table.add(getCheckBox(PRM_DEL_ITEM, income.getPrimaryKey().toString()));
						table.add(getDeleteLink(income.getPrimaryKey().toString(),tooltip) );
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
		appForm.maintainParameter(PRM_USER_ID);
		appForm.setMainPanel(table);
	}
	private void presentateCreateRecord() {
		Table table = new Table();
		table.add(getHeader(localize("new_record", "New record")), 1, 1);
		table.add(getHeader(localize("brutto_income_amount", "Brutto Income amount")), 1, 2);
		table.setColor(1, 2, getHeaderColor());
		table.add(getHeader(localize("valid_from_date", "Valid from")), 1, 3);
		table.setColor(1, 3, getHeaderColor());
		TextInput incomeInput = new TextInput("brt_income_amount");
		incomeInput.keepStatusOnAction(true);
		incomeInput.setAsIntegers(localize("warning.please_enter_number", "Please enter a valid number"));
		setStyle(incomeInput, STYLENAME_INTERFACE);
		table.add(incomeInput, 2, 2);
		DateInput validFromInput = new DateInput("brt_valid_from");
		validFromInput.setToDisplayDayLast(true);
		IWTimestamp today = IWTimestamp.RightNow();
		validFromInput.setDate(today.getDate());
		int currentYear = today.getYear();
		validFromInput.setYearRange(currentYear - 5, currentYear + 5);
		validFromInput.setToShowDay(false);
		setStyle(validFromInput, STYLENAME_INTERFACE);
		table.add(validFromInput, 2, 3);
		if(registerErrorMsg!=null){
			
			table.mergeCells(1,5,2,5);
			table.add(getErrorText(registerErrorMsg),1,5);
			
		}
		//SubmitButton btnSave = new SubmitButton(localize("save","Save"),PRM_SAVE,"true");
		//SubmitButton btnCancel = new SubmitButton(localize("cancel","Cancel"),PRM_CANCEL,"true");
		appForm.maintainParameter(PRM_USER_ID);
		table.setCellspacing(getCellspacing());
		table.setCellpadding(getCellpadding());
		//table.add(getButton(btnSave),1,5);
		//table.add(getButton(btnCancel),2,5);			
		//table.mergeCells(1,5,table.getColumns(),5);
		appForm.setMainPanel(table);
	}
	private CommuneUserBusiness getUserService(IWContext iwc) throws RemoteException {
		return (CommuneUserBusiness) IBOLookup.getServiceInstance(iwc, CommuneUserBusiness.class);
	}
	private UserInfoService getUserInfoService(IWApplicationContext iwac) throws RemoteException {
		return (UserInfoService) IBOLookup.getServiceInstance(iwac, UserInfoService.class);
	}
	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.presentation.CommuneBlock#localize(java.lang.String, java.lang.String)
	 */
	public String localize(String textKey, String defaultText) {
		return super.localize(localizePrefix + textKey, defaultText);
	}
	
	private Link getDeleteLink(String ID,String tooltip){
		Link link = new Link(getDeleteIcon(tooltip));
		link.addParameter(PRM_USER_ID,userID.toString());
		link.addParameter(PRM_DEL_ITEM,ID);
		return link;
	}
}
