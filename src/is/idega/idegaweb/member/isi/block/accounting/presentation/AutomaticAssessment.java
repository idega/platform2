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
import is.idega.idegaweb.member.isi.block.accounting.data.ClubTariff;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWConstants;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SelectionBox;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.presentation.GroupChooser;

/**
 * @author palli
 */
public class AutomaticAssessment extends CashierSubWindowTemplate {
	protected static final String ACTION_SUBMIT = "aa_submit";
	protected static final String ACTION_DELETE = "aa_delete";
	
	protected static final String LABEL_NAME = "isi_acc_aa_name";
	protected static final String LABEL_CLUB = "isi_acc_aa_club";
	protected static final String LABEL_DIVISION = "isi_acc_aa_div";
	protected static final String LABEL_GROUP = "isi_acc_aa_group";
	protected static final String LABEL_START = "isi_acc_aa_start";
	protected static final String LABEL_END = "isi_acc_aa_end";
	protected static final String LABEL_USER = "isi_acc_aa_user";
	protected static final String LABEL_USE_PARENT = "isi_acc_aa_use_parent";
	protected static final String LABEL_INCLUDE_CHILDREN = "isi_acc_aa_incl_children";
	protected static final String LABEL_TARIFF = "isi_acc_aa_tariff";
	
	protected static final String LABEL_DELETE = "isi_acc_aa_delete";
	
	public AutomaticAssessment() {
		super();
	}

	private void executeAssessment(IWContext iwc) {
		String name = iwc.getParameter(LABEL_NAME);
		String group = iwc.getParameter(LABEL_GROUP);
		
		boolean useParent = false;
		if (iwc.isParameterSet(LABEL_USE_PARENT))
			useParent = true;
		boolean includeChildren = false;
		if (iwc.isParameterSet(LABEL_INCLUDE_CHILDREN))
			includeChildren = true;

		if (group != null) {
			group = group.substring(group.indexOf("_")+1);
		}
		
		try {
			getAccountingBusiness(iwc).doAssessment(name, getClub(), getDivision(), group, iwc.getCurrentUser(), useParent, includeChildren);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		
	}
	
	private void deleteAssessment(IWContext iwc) {
		
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
		Text labelClub = new Text(iwrb.getLocalizedString(LABEL_CLUB, "Club"));
		labelClub.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelDiv = new Text(iwrb.getLocalizedString(LABEL_DIVISION, "Division"));
		labelDiv.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelGroup = new Text(iwrb.getLocalizedString(LABEL_GROUP, "Group"));
		labelGroup.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelTariff = new Text(iwrb.getLocalizedString(LABEL_TARIFF, "Tariff"));
		labelTariff.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelStart = new Text(iwrb.getLocalizedString(LABEL_START, "Start time"));
		labelStart.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelEnd = new Text(iwrb.getLocalizedString(LABEL_END, "End time"));
		labelEnd.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelUser = new Text(iwrb.getLocalizedString(LABEL_USER, "User"));
		labelUser.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelUseParent = new Text(iwrb.getLocalizedString(LABEL_USE_PARENT, "Use parent"));
		labelUseParent.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelIncludeChildren = new Text(iwrb.getLocalizedString(LABEL_INCLUDE_CHILDREN, "Incl. child."));
		labelIncludeChildren.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);

		TextInput nameInput = new TextInput(LABEL_NAME);
		GroupChooser groupInput = new GroupChooser(LABEL_GROUP);
		CheckBox useParentInput = new CheckBox(LABEL_USE_PARENT,"true");
		CheckBox includeChildrenInput = new CheckBox(LABEL_INCLUDE_CHILDREN,"true");
		SubmitButton submit = new SubmitButton(iwrb.getLocalizedString(ACTION_SUBMIT, "Submit"), ACTION_SUBMIT, "submit");

		Collection tariff = null;
		try {
			if (getClub() != null) {
				tariff = getAccountingBusiness(iwc).findAllTariffByClub(getClub());
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		
		SelectionBox tariffInput = new SelectionBox(LABEL_TARIFF);
		if (tariff != null) {
			Iterator it = tariff.iterator();
			while (it.hasNext()) {
				ClubTariff entry = (ClubTariff) it.next();
				tariffInput.addMenuElement(((Integer) entry.getPrimaryKey()).intValue(), entry.getText());
			}
		}
		tariffInput.setMultiple(true);
		
		inputTable.add(labelName, 1, row);
		inputTable.add(labelGroup, 2, row);
		inputTable.add(labelTariff, 3, row);
		inputTable.add(labelUseParent, 4, row);
		inputTable.add(labelIncludeChildren, 5, row++);
		
		inputTable.add(nameInput, 1, row);
		inputTable.add(groupInput, 2, row);
		inputTable.add(tariffInput, 3, row);
		inputTable.add(useParentInput, 4, row);
		inputTable.add(includeChildrenInput, 5, row);
		inputTable.add(submit, 6, row);
		
		row = 1;
		t.add(labelName, 2, row);
		t.add(labelClub, 3, row);
		t.add(labelDiv, 4, row);
		t.add(labelGroup, 5, row);
		t.add(labelStart, 6, row);
		t.add(labelEnd, 7, row);
		t.add(labelUser, 8, row);
		t.add(labelUseParent, 9, row);
		t.add(labelIncludeChildren, 10, row++);
		
		Collection col = null;
		try {
			System.out.println("AutomaticAssessment.getClub() = " + getClub());
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
				t.add(round.getName(), 2, row);
				t.add(round.getClub().getName(), 3, row);
				if (round.getDivision() != null)
					t.add(round.getDivision().getName(), 4, row);
				if (round.getGroup() != null)
					t.add(round.getGroup().getName(), 5, row);
				t.add(round.getStartTime().toString(), 6, row);
				if (round.getEndTime() != null)
					t.add(round.getEndTime().toString(), 7, row);
				t.add(round.getExecutedBy().getName(), 8, row);
				CheckBox parent = (CheckBox)show.clone();
				if (round.getUseParentTariff())
					parent.setChecked(true);
				t.add(parent, 9, row);
				CheckBox children = (CheckBox)show.clone();
				if (round.getIncludeChildren())
					children.setChecked(true);
				t.add(children, 10, row);
				row++;
			}
			
			SubmitButton delete = new SubmitButton(iwrb.getLocalizedString(ACTION_DELETE, "Delete"), ACTION_DELETE, "delete");
			t.add(delete, 10, row);
		}

		f.maintainParameter(CashierWindow.ACTION);
		f.maintainParameter(CashierWindow.PARAMETER_GROUP_ID);
		f.maintainParameter(CashierWindow.PARAMETER_USER_ID);
		
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