/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.member.isi.block.accounting.presentation;

import is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusiness;
import is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRound;
import is.idega.idegaweb.member.isi.block.accounting.data.ClubTariffType;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWConstants;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DatePicker;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SelectionBox;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.presentation.GroupChooser;
import com.idega.util.IWTimestamp;

/**
 * @author palli
 */
public class AutomaticAssessment extends CashierSubWindowTemplate {
	public static final String ASSESSMENT_ID = "isi_acc_aa_id";
	
	protected static final String ACTION_SUBMIT = "aa_submit";
	protected static final String ACTION_DELETE = "aa_delete";
	protected static final String ACTION_REFRESH = "aa_refresh";
	
	protected static final String LABEL_NAME = "isi_acc_aa_name";
	protected static final String LABEL_DIVISION = "isi_acc_aa_div";
	protected static final String LABEL_GROUP = "isi_acc_aa_group";
	protected static final String LABEL_START = "isi_acc_aa_start";
	protected static final String LABEL_END = "isi_acc_aa_end";
	protected static final String LABEL_USER = "isi_acc_aa_user";
	protected static final String LABEL_INCLUDE_CHILDREN = "isi_acc_aa_incl_children";
	protected static final String LABEL_TARIFF_TYPE = "isi_acc_aa_tariff_type";
	protected static final String LABEL_PAYMENT_DATE = "isi_acc_aa_payment_date";
	protected static final String LABEL_RUN_ON_DATE = "isi_acc_aa_run_on_date";
	
	protected static final String LABEL_DELETE = "isi_acc_aa_delete";
	protected static final String LABEL_REFRESH = "isi_acc_aa_refresh";
	
	public AutomaticAssessment() {
		super();
	}

	private void executeAssessment(IWContext iwc) {
		String name = iwc.getParameter(LABEL_NAME);
		String group = iwc.getParameter(LABEL_GROUP);
		String tariffs[] = iwc.getParameterValues(LABEL_TARIFF_TYPE);
		String paymentDate = iwc.getParameter(LABEL_PAYMENT_DATE);
		String runOnDate = iwc.getParameter(LABEL_RUN_ON_DATE);
		
		boolean includeChildren = false;
		if (iwc.isParameterSet(LABEL_INCLUDE_CHILDREN))
			includeChildren = true;

		if (group != null) {
			group = group.substring(group.indexOf("_")+1);
		}
		
		IWTimestamp paymentDateTimestamp = null;
		IWTimestamp runOnDateTimestamp = null;
		
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
		
		try {
			runOnDateTimestamp = new IWTimestamp(runOnDate);
		}
		catch (IllegalArgumentException e) {
			runOnDateTimestamp = new IWTimestamp(Long.parseLong(runOnDate));
			runOnDateTimestamp.setHour(0);
			runOnDateTimestamp.setMinute(0);
			runOnDateTimestamp.setSecond(0);
			runOnDateTimestamp.setMilliSecond(0);
		}
		
		
		try {
			getAccountingBusiness(iwc).doAssessment(name, getClub(), getDivision(), group, iwc.getCurrentUser(), includeChildren, tariffs, paymentDateTimestamp.getTimestamp(), runOnDateTimestamp.getTimestamp());
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	private void deleteAssessment(IWContext iwc) {
		String delete[] = iwc.getParameterValues(LABEL_DELETE);
		
		try {
			getAccountingBusiness(iwc).deleteAssessmentRound(delete);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public void main(IWContext iwc) {
		if (iwc.isParameterSet(ACTION_SUBMIT)) {
			executeAssessment(iwc);
		}
		else if (iwc.isParameterSet(ACTION_DELETE)) {
			deleteAssessment(iwc);
		}

		IWResourceBundle iwrb = getResourceBundle(iwc);

		Form f = new Form();
		Table t = new Table();
		Table inputTable = new Table();
		t.setCellpadding(5);
		inputTable.setCellpadding(5);

		int row = 1;
		Text labelName = new Text(iwrb.getLocalizedString(LABEL_NAME, "Name"));
		labelName.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelDiv = new Text(iwrb.getLocalizedString(LABEL_DIVISION, "Division"));
		labelDiv.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelGroup = new Text(iwrb.getLocalizedString(LABEL_GROUP, "Group"));
		labelGroup.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelTariff = new Text(iwrb.getLocalizedString(LABEL_TARIFF_TYPE, "Tariff type"));
		labelTariff.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelStart = new Text(iwrb.getLocalizedString(LABEL_START, "Start time"));
		labelStart.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelEnd = new Text(iwrb.getLocalizedString(LABEL_END, "End time"));
		labelEnd.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelUser = new Text(iwrb.getLocalizedString(LABEL_USER, "User"));
		labelUser.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelIncludeChildren = new Text(iwrb.getLocalizedString(LABEL_INCLUDE_CHILDREN, "Incl. child."));
		labelIncludeChildren.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelPaymentDate = new Text(iwrb.getLocalizedString(LABEL_PAYMENT_DATE, "Payment date"));
		labelPaymentDate.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelRunOnDate = new Text(iwrb.getLocalizedString(LABEL_RUN_ON_DATE, "Run on date"));
		labelRunOnDate.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		
		TextInput nameInput = new TextInput(LABEL_NAME);
		nameInput.setLength(10);
		GroupChooser groupInput = new GroupChooser(LABEL_GROUP);
		groupInput.setInputLength(10);
		CheckBox includeChildrenInput = new CheckBox(LABEL_INCLUDE_CHILDREN,"true");
		DatePicker paymentDateInput = new DatePicker(LABEL_PAYMENT_DATE);
		DatePicker runOnDateInput = new DatePicker(LABEL_RUN_ON_DATE);
		
		SubmitButton submit = new SubmitButton(iwrb.getLocalizedString(ACTION_SUBMIT, "Submit"), ACTION_SUBMIT, "submit");
		SubmitButton refresh = new SubmitButton(iwrb.getLocalizedString(LABEL_REFRESH, "Refresh"), ACTION_REFRESH, "refresh");
		
		Collection tariffType = null;
		try {
			if (getClub() != null) {
				tariffType = getAccountingBusiness(iwc).findAllTariffTypeByClub(getClub());
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		
		SelectionBox tariffTypeInput = new SelectionBox(LABEL_TARIFF_TYPE);
		if (tariffType != null) {
			Iterator it = tariffType.iterator();
			while (it.hasNext()) {
				ClubTariffType entry = (ClubTariffType) it.next();
				tariffTypeInput.addMenuElement(((Integer) entry.getPrimaryKey()).intValue(), entry.getName());
			}
		}
		tariffTypeInput.setMultiple(true);
		
		inputTable.add(labelName, 1, row);
		inputTable.add(labelGroup, 2, row);
		inputTable.add(labelTariff, 3, row);
		inputTable.add(labelIncludeChildren, 4, row);
		inputTable.add(labelPaymentDate, 5, row);
		inputTable.add(labelRunOnDate, 6, row++);
		
		inputTable.add(nameInput, 1, row);
		inputTable.add(groupInput, 2, row);
		inputTable.add(tariffTypeInput, 3, row);
		inputTable.add(includeChildrenInput, 4, row);
		inputTable.add(paymentDateInput, 5, row);
		inputTable.add(runOnDateInput, 6, row);
		inputTable.add(submit, 7, row);
		inputTable.add(refresh, 8, row);
		
		row = 1;
		t.add(labelName, 2, row);
//		t.add(labelClub, 3, row);
		t.add(labelDiv, 3, row);
		t.add(labelGroup, 4, row);
		t.add(labelStart, 5, row);
		t.add(labelEnd, 6, row);
		t.add(labelUser, 7, row);
		t.add(labelIncludeChildren, 8, row++);
		
		Collection col = null;
		try {
			if (getClub() != null) {
				col = getAccountingBusiness(iwc).findAllAssessmentRoundByClubAndDivision(getClub(), getDivision());
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}

		CheckBox show = new CheckBox();
		show.setDisabled(true);
		
		if (col != null && !col.isEmpty()) {
			Iterator it = col.iterator();
			while (it.hasNext()) {
				AssessmentRound round = (AssessmentRound) it.next();
				CheckBox delete = new CheckBox(LABEL_DELETE, round.getPrimaryKey().toString());
				t.add(delete, 1, row);
				if (round.getEndTime() == null) {
					t.add(round.getName(), 2, row);
				}
				else {
					Link nameLink = new Link(round.getName());
					nameLink.setParameter(this.ASSESSMENT_ID, round.getPrimaryKey().toString());
					nameLink.setWindowToOpen(AssessmentListWindow.class);
					t.add(nameLink, 2, row);
				}
				if (round.getDivision() != null)
					t.add(round.getDivision().getName(), 3, row);
				if (round.getGroup() != null)
					t.add(round.getGroup().getName(), 4, row);
				IWTimestamp startTime = new IWTimestamp(round.getStartTime());
				t.add(startTime.getDateString("dd.MM.yyyy HH:mm:ss"), 5, row);
				if (round.getEndTime() != null) {
					IWTimestamp endTime = new IWTimestamp(round.getEndTime());
					t.add(endTime.getDateString("dd.MM.yyyy HH:mm:ss"), 6, row);
				}
				t.add(round.getExecutedBy().getName(), 7, row);
				CheckBox children = (CheckBox)show.clone();
				if (round.getIncludeChildren())
					children.setChecked(true);
				t.add(children, 8, row);
				row++;
			}
			
			SubmitButton delete = new SubmitButton(iwrb.getLocalizedString(ACTION_DELETE, "Delete"), ACTION_DELETE, "delete");
			delete.setToEnableWhenChecked(LABEL_DELETE);
			t.add(delete, 8, row);
			t.setAlignment(8, row, "RIGHT");
		}

		f.maintainParameter(CashierWindow.ACTION);
		f.maintainParameter(CashierWindow.PARAMETER_GROUP_ID);
		f.maintainParameter(CashierWindow.PARAMETER_USER_ID);
		f.maintainParameter(CashierWindow.PARAMETER_DIVISION_ID);
		
		f.add(inputTable);
		f.add(t);
		add(f);
	}

	private AccountingBusiness getAccountingBusiness(IWApplicationContext iwc) {
		try {
			return (AccountingBusiness) IBOLookup.getServiceInstance(iwc, AccountingBusiness.class);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}

		return null;
	}
}