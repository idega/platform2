/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.member.isi.block.accounting.presentation;

import is.idega.idegaweb.member.isi.block.accounting.data.ClubTariff;
import is.idega.idegaweb.member.isi.block.accounting.data.ClubTariffHome;
import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntry;
import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryHome;

import java.rmi.RemoteException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWConstants;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DatePicker;
import com.idega.presentation.ui.DoubleInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.presentation.UserChooserBrowser;
import com.idega.util.IWTimestamp;

/**
 * @author palli
 */
public class ManualAssessment extends CashierSubWindowTemplate {
	protected static final String ACTION_SUBMIT = "ma_submit";
	protected static final String ACTION_SELECT_USER = "ma_select_user";
	
	private final static String USER_CHOOSER_NAME = "ma_user_chooser_name";
	
	private final static String LABEL_SELECTED_USER = "isi_acc_ma_selected_user";
	private final static String LABEL_USERS_GROUPS = "isi_acc_ma_user_groups";
	private final static String LABEL_TARIFF = "isi_acc_ma_tariff";
	private final static String LABEL_AMOUNT = "isi_acc_ma_amount";
	private final static String LABEL_INFO = "isi_acc_ma_info";
	private final static String LABEL_PAYMENT_DATE = "isi_acc_ma_payment_date";
		
	private final static String ERROR_NO_GROUP_SELECTED = "isi_acc_ma_no_group_selected";
	private final static String ERROR_NO_TARIFF_SELECTED = "isi_acc_ma_no_tariff_selected";
	private final static String ERROR_NO_AMOUNT_ENTERED = "isi_acc_ma_no_amount_entered";
	
	private final static String LABEL_DIVISION = "isi_acc_ma_division";
	private final static String LABEL_GROUP = "isi_acc_ma_group";
	private final static String LABEL_DATE = "isi_acc_ma_date";
	private final static String LABEL_TYPE = "isi_acc_ma_type";
	
	/**
	 * 
	 */
	public ManualAssessment() {
		super();
	}

	private boolean saveAssessment(IWContext iwc) {
		errorList = new ArrayList();
		String group = iwc.getParameter(LABEL_USERS_GROUPS);
		String tariff = iwc.getParameter(LABEL_TARIFF);
		String amount = iwc.getParameter(LABEL_AMOUNT);
		String info = iwc.getParameter(LABEL_INFO);
		String paymentDate = iwc.getParameter(LABEL_PAYMENT_DATE);

		if (group == null || "".equals(group)) {
			errorList.add(ERROR_NO_GROUP_SELECTED);
		}
		
		if (tariff == null || "".equals(tariff)) {
			errorList.add(ERROR_NO_TARIFF_SELECTED);
		}
		
		if (amount == null || "".equals(amount)) {
			errorList.add(ERROR_NO_AMOUNT_ENTERED);
		}
		
		if (!errorList.isEmpty()) {
			return false;
		}
		
		IWTimestamp paymentDateTimestamp = null;
		
		try {
			paymentDateTimestamp = new IWTimestamp(paymentDate);
		}
		catch (IllegalArgumentException e) {
			paymentDateTimestamp = new IWTimestamp(Long.parseLong(paymentDate));
			paymentDateTimestamp.setHour(0);
			paymentDateTimestamp.setMinute(0);
			paymentDateTimestamp.setSecond(0);
			paymentDateTimestamp.setMilliSecond(0);
		}
		
		boolean insert = false;
		
		try {
			insert = getAccountingBusiness(iwc).insertManualAssessment(getClub(), getDivision(), getUser(), group, tariff, amount, info, iwc.getCurrentUser(), paymentDateTimestamp.getTimestamp());
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		
		return insert;
	}
	
	public void main(IWContext iwc) {
		Form f = new Form();
		Table t = new Table();
		Table inputTable = new Table();
		Table dataTable = new Table();
		t.setCellpadding(5);
		inputTable.setCellpadding(5);
		dataTable.setCellpadding(5);

		IWResourceBundle iwrb = getResourceBundle(iwc);
		
		double defaultAmount = -1;
		if (iwc.isParameterSet(ACTION_SUBMIT)) {
			if (!saveAssessment(iwc)) {
				Table error = new Table();
				Text labelError = new Text(iwrb.getLocalizedString(ERROR_COULD_NOT_SAVE, "Could not save") + ":");
				labelError.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE_RED);
				
				int r = 1;
				error.add(labelError, 1, r++);
				if (errorList != null && !errorList.isEmpty()) {
					Iterator it = errorList.iterator();
					while (it.hasNext()) {
						String loc = (String) it.next();
						Text errorText = new Text(iwrb.getLocalizedString(loc, ""));
						errorText.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE_RED);
						
						error.add(errorText, 1, r++);
					}
				}
				
				f.add(error);
			}
		}
		
		f.add(t);
		f.add(inputTable);
		f.add(dataTable);
		
		Text labelUser = new Text(iwrb.getLocalizedString(LABEL_SELECTED_USER, "Selected user") + ":");
		labelUser.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		
		int row = 1;
		SubmitButton selectUser = new SubmitButton(iwrb.getLocalizedString(ACTION_SELECT_USER, "Select User"), ACTION_SELECT_USER, "select_user");
		
		t.add(labelUser, 1, row);
		if (getUser() != null) {
			t.add(getUser().getName(), 2, row);
		}
		
		row++;
		t.add(new UserChooserBrowser(CashierWindow.PARAMETER_USER_ID), 1, row);
		t.add(selectUser, 2, row);
		
		t.setAlignment(2, row, "RIGHT");
		
		if (getUser() != null) {
			String selectedGroup = iwc.getParameter(LABEL_USERS_GROUPS);
			String selectedTariff = iwc.getParameter(LABEL_TARIFF);
			if (selectedTariff != null) {
				try {
					ClubTariff tariff = getClubTariffHome().findByPrimaryKey(new Integer(selectedTariff));
					defaultAmount = tariff.getAmount();
				}
				catch (NumberFormatException e) {
					e.printStackTrace();
				}
				catch (FinderException e) {
					e.printStackTrace();
				} 
			}
			
			row = 1;
			Text labelUsersGroups = new Text(iwrb.getLocalizedString(LABEL_USERS_GROUPS, "Users groups"));
			labelUsersGroups.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
			Text labelTariff = new Text(iwrb.getLocalizedString(LABEL_TARIFF, "Tariff"));
			labelTariff.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
			Text labelAmount = new Text(iwrb.getLocalizedString(LABEL_AMOUNT, "Amount"));
			labelAmount.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
			Text labelInfo = new Text(iwrb.getLocalizedString(LABEL_INFO, "Info"));
			labelInfo.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
			Text labelPaymentDate = new Text(iwrb.getLocalizedString(LABEL_PAYMENT_DATE, "Payment date"));
			labelPaymentDate.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
			
			inputTable.add(labelUsersGroups, 1, row);
			inputTable.add(labelTariff, 2, row);
			inputTable.add(labelAmount, 3, row);
			inputTable.add(labelInfo, 4, row);
			inputTable.add(labelPaymentDate, 5, row++);
			
			DropdownMenu usersGroupsInput = new DropdownMenu(LABEL_USERS_GROUPS);
			Collection groups = getGroupsForUser(iwc);
			if (groups != null) {
				Iterator it = groups.iterator();
				while (it.hasNext()) {
					Group userGroup = (Group) it.next();
					usersGroupsInput.addMenuElement(userGroup.getPrimaryKey().toString(), userGroup.getName());
					if (selectedGroup == null) {
						selectedGroup = userGroup.getPrimaryKey().toString();
					}
				}
			}
			usersGroupsInput.setToSubmit();
			if (selectedGroup != null) {
				usersGroupsInput.setSelectedElement(selectedGroup);
			}
			
			Collection tariff = null;
			try {
				if (getClub() != null) {
					tariff = getAccountingBusiness(iwc).findAllValidTariffByGroup(selectedGroup);
				}
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			
			DropdownMenu tariffInput = new DropdownMenu(LABEL_TARIFF);
			if (tariff != null) {
				Iterator it = tariff.iterator();
				while (it.hasNext()) {
					ClubTariff entry = (ClubTariff) it.next();
					tariffInput.addMenuElement(entry.getPrimaryKey().toString(), entry.getText());
					if (selectedTariff == null) {
						selectedTariff = entry.getPrimaryKey().toString();
						try {
							ClubTariff selTariff = getClubTariffHome().findByPrimaryKey(new Integer(selectedTariff));
							defaultAmount = selTariff.getAmount();
						}
						catch (NumberFormatException e) {
							e.printStackTrace();
						}
						catch (FinderException e) {
							e.printStackTrace();
						} 
					}
				}
			}
			tariffInput.setToSubmit();
			if (selectedTariff != null) {
				tariffInput.setSelectedElement(selectedTariff);
			}
			
			DoubleInput amountInput = new DoubleInput(LABEL_AMOUNT);
			amountInput.setLength(10);
			if (defaultAmount > 0) {
				amountInput.setValue(defaultAmount);
			}
			TextInput infoInput = new TextInput(LABEL_INFO);
			infoInput.setLength(20);
			infoInput.setMaxlength(255);
			DatePicker paymentDateInput = new DatePicker(LABEL_PAYMENT_DATE);
			
			inputTable.add(usersGroupsInput, 1, row);
			inputTable.add(tariffInput, 2, row);
			inputTable.add(amountInput, 3, row);
			inputTable.add(infoInput, 4, row);
			inputTable.add(paymentDateInput, 5, row);
			
			SubmitButton submit = new SubmitButton(iwrb.getLocalizedString(ACTION_SUBMIT, "Submit"), ACTION_SUBMIT, "submit");
			inputTable.add(submit, 6, row++);	
			
			try {
			    IWTimestamp now = IWTimestamp.RightNow();
			    now.addYears(-1);
				Collection entries = getFinanceEntryHome().findAllAssessmentByUser(getClub(), getDivision(), getUser(), now);
				if (entries != null && !entries.isEmpty()) {
					Text labelDivision = new Text(iwrb.getLocalizedString(LABEL_DIVISION, "Division"));
					labelDivision.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
					Text labelGroup = new Text(iwrb.getLocalizedString(LABEL_GROUP, "Group"));
					labelGroup.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
					Text labelDate = new Text(iwrb.getLocalizedString(LABEL_DATE, "Date"));
					labelDate.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
					Text labelType = new Text(iwrb.getLocalizedString(LABEL_TYPE, "Type"));
					labelType.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
					
					int r = 1;
					dataTable.add(labelDivision, 1, r);
					dataTable.add(labelGroup, 2, r);
					dataTable.add(labelInfo, 3, r);
					dataTable.add(labelDate, 4, r);
					dataTable.add(labelAmount, 5, r);
					dataTable.setAlignment(5, r, "RIGHT");
					dataTable.add(labelType, 6, r++);
					
					NumberFormat nf = NumberFormat.getInstance(iwc.getCurrentLocale());
					nf.setMaximumFractionDigits(0);
					
					Iterator it = entries.iterator();
					while (it.hasNext()) {
						FinanceEntry entry = (FinanceEntry) it.next();
						
						if (entry.getDivision() != null) {
							dataTable.add(entry.getDivision().getName(), 1, r);
						}
						if (entry.getGroup() != null) {
							dataTable.add(entry.getGroup().getName(), 2, r);
						}
						if (entry.getInfo() != null) {
							dataTable.add(entry.getInfo(), 3, r);
						}
						if (entry.getDateOfEntry() != null) {
							IWTimestamp date = new IWTimestamp(entry.getDateOfEntry());
							dataTable.add(date.getDateString("dd.MM.yyyy"), 4, r);
						}
						dataTable.add(nf.format(entry.getAmount()), 5, r);
						dataTable.setAlignment(5, r, "RIGHT");
						if (entry.getTypeLocalizationKey() != null) {
							dataTable.add(iwrb.getLocalizedString(entry.getTypeLocalizationKey(), entry.getTypeLocalizationKey()), 6, r);
						}
						r++;
					}
				}
			}
			catch (FinderException e1) {
				e1.printStackTrace();
			} 
		}
			
		f.maintainParameter(CashierWindow.ACTION);
		f.maintainParameter(CashierWindow.PARAMETER_GROUP_ID);
		f.maintainParameter(CashierWindow.PARAMETER_DIVISION_ID);
		f.maintainParameter(CashierWindow.PARAMETER_CLUB_ID);
		
		add(f);
	}
	
	private ClubTariffHome getClubTariffHome() {
		try {
			return (ClubTariffHome) IDOLookup.getHome(ClubTariff.class);
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	private Collection getGroupsForUser(IWContext iwc) {
		ArrayList userGroupsInClub = new ArrayList();
		try {
			Collection allUserGroups = getUserBusiness(iwc).getUserGroupsDirectlyRelated(getUser());
			
			if (allUserGroups != null && !allUserGroups.isEmpty()) {
				Group parent = null;
				if (getDivision() != null) {
					parent = getDivision();
				}
				else {
					parent = getClub();
				}
		
				Iterator it = allUserGroups.iterator();
				while (it.hasNext()) {
					Group group = (Group) it.next();
					
					if (isGroupInClubAndDivision(group, parent)) {
						userGroupsInClub.add(group);
					}
				}
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}		
		
		return userGroupsInClub;
	}
	
	private boolean isGroupInClubAndDivision(Group group, Group parent) {
		if (group == null)
			return false;
		
		if (group.equals(parent))
			return true;
		
		List parentGroups = group.getParentGroups();
		Iterator it = parentGroups.iterator();
		while (it.hasNext()) {
			Group parentGroup = (Group) it.next();
			
			if (parent.equals(parentGroup)) {
				return true;
			}

			if (isGroupInClubAndDivision(parentGroup, parent)) {
				return true;
			}
		}
		
		return false;
	}
	
	private UserBusiness getUserBusiness(IWApplicationContext iwc) {
		try {
			return (UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		
		return null;
	}	
	
	private FinanceEntryHome getFinanceEntryHome() {
		try {
			return (FinanceEntryHome) IDOLookup.getHome(FinanceEntry.class);
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}

		return null;
	}	
}