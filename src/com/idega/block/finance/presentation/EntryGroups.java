package com.idega.block.finance.presentation;
import java.util.List;

import com.idega.block.finance.business.AssessmentBusiness;
import com.idega.block.finance.business.Finder;
import com.idega.block.finance.data.AccountEntry;
import com.idega.block.finance.data.EntryGroup;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.IWTimestamp;
import com.idega.util.text.Edit;
/**

 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1

 */
public class EntryGroups extends Finance {
	protected final int ACT1 = 1, ACT2 = 2, ACT3 = 3, ACT4 = 4, ACT5 = 5;
	public String strAction = "tt_action";
	private int iCashierId = -1;
	
	public String getLocalizedNameKey() {
		return "entrygroups";
	}
	public String getLocalizedNameValue() {
		return "Entrygroups";
	}
	protected void control(IWContext iwc) {
		if (isAdmin) {
			try {
				PresentationObject MO = new Text();
				if (iwc.getParameter(strAction) == null) {
					MO = getTableOfGroups(iwc);
				}
				if (iwc.getParameter(strAction) != null) {
					String sAct = iwc.getParameter(strAction);
					int iAct = Integer.parseInt(sAct);
					switch (iAct) {
						case ACT1 :
							MO = getTableOfGroups(iwc);
							break;
						case ACT2 :
							MO = doMainTable(iwc);
							break;
						case ACT3 :
							MO = doSomeThing(iwc);
							break;
						case ACT4 :
							MO = getTableOfAssessmentAccounts(iwc);
							break;
						default :
							MO = getTableOfGroups(iwc);
							break;
					}
				}
				Table T = new Table();
				T.setWidth("100%");
				T.setCellpadding(2);
				T.setCellspacing(0);
				T.add(Edit.headerText(iwrb.getLocalizedString("entry_groups", "Entry groups"), 3), 1, 1);
				T.add(makeLinkTable(1), 1, 2);
				T.add(MO, 1, 3);
				add(T);
			}
			catch (Exception S) {
				S.printStackTrace();
			}
		}
		else
			add(iwrb.getLocalizedString("access_denied", "Access denies"));
	}
	private PresentationObject doSomeThing(IWContext iwc) {
		PresentationObject MO = new Text("failed");
		if (iwc.getParameter("ent_to") != null) {
			String dateFrom = iwc.getParameter("ent_from");
			String dateTo = iwc.getParameter("ent_to");
			if (!"".equals(dateTo)) {
				IWTimestamp to = new IWTimestamp(dateTo);
				IWTimestamp from = null;
				if (!"".equals(dateFrom))
					from = new IWTimestamp(dateFrom);
				doGroup(iwc, from, to);
				return new Text("Succeeded");
			}
			return new Text("Invalid to date");
		}
		return MO;
	}
	private PresentationObject doGroup(IWContext iwc, IWTimestamp from, IWTimestamp to) {
		try {
			//System.err.println(" doGroup start :"+IWTimestamp.RightNow().toString());
			//CampusAssessmentBusiness.groupEntries(from,to);
			AssessmentBusiness assBuiz =
				(AssessmentBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, AssessmentBusiness.class);
			assBuiz.groupEntriesWithSQL(from, to);
			//System.err.println(" doGroup end   :"+IWTimestamp.RightNow().toString());
			return Edit.formatText(iwrb.getLocalizedString("group_created", "Group was created"));
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return Edit.formatText(iwrb.getLocalizedString("group_not_created", "Group was not created"));
		}
	}
	protected PresentationObject makeLinkTable(int menuNr) {
		Table LinkTable = new Table(3, 1);
		int last = 3;
		LinkTable.setWidth("100%");
		LinkTable.setCellpadding(2);
		LinkTable.setCellspacing(1);
		LinkTable.setColor(Edit.colorDark);
		LinkTable.setWidth(last, "100%");
		Link Link1 = new Link(iwrb.getLocalizedString("view", "View"));
		Link1.setFontColor(Edit.colorLight);
		Link1.addParameter(this.strAction, String.valueOf(this.ACT1));
		Link Link2 = new Link(iwrb.getLocalizedString("new", "New"));
		Link2.setFontColor(Edit.colorLight);
		Link2.addParameter(this.strAction, String.valueOf(this.ACT2));
		if (isAdmin) {
			LinkTable.add(Link1, 1, 1);
			LinkTable.add(Link2, 2, 1);
		}
		return LinkTable;
	}
	private PresentationObject getTableOfGroups(IWContext iwc) throws java.rmi.RemoteException {
		Table T = new Table();
		int row = 2;
		List L = Finder.listOfEntryGroups();
		if (L != null) {
			int len = L.size();
			String sRollBack = iwrb.getLocalizedString("rollback", "Rollback");
			T.add(Edit.formatText(iwrb.getLocalizedString("group_id", "Group id")), 1, 1);
			T.add(Edit.formatText(iwrb.getLocalizedString("group_date", "Group date")), 2, 1);
			T.add(Edit.formatText(iwrb.getLocalizedString("entry_from", "Entries from")), 3, 1);
			T.add(Edit.formatText(iwrb.getLocalizedString("entry_to", "Entries to")), 4, 1);
			T.add(Edit.formatText(iwrb.getLocalizedString("file_name", "File name")), 5, 1);
			T.add(Edit.formatText(iwrb.getLocalizedString("real_count", "Real count")), 6, 1);
			//T.add(Edit.formatText(sRollBack),4,1);
			int col = 1;
			row = 2;
			EntryGroup EG;
			java.text.NumberFormat nf = java.text.NumberFormat.getNumberInstance(iwc.getCurrentLocale());
			for (int i = 0; i < len; i++) {
				col = 1;
				EG = (EntryGroup) L.get(i);
				T.add(getGroupLink(EG.getName(), EG.getID()), col++, row);
				T.add(Edit.formatText(new IWTimestamp(EG.getGroupDate()).getLocaleDate(iwc)), col++, row);
				T.add(Edit.formatText(EG.getEntryIdFrom()), col++, row);
				T.add(Edit.formatText(EG.getEntryIdTo()), col++, row);
				T.add(Edit.formatText(EG.getFileName()), col++, row);
				AssessmentBusiness assBuiz =
					(AssessmentBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, AssessmentBusiness.class);
				T.add(Edit.formatText(assBuiz.getGroupEntryCount(EG)), col++, row);
				/*
				
				Link R = new Link(iwb.getImage("rollback.gif"));
				
				R.addParameter("rollback",AR.getID());
				
				R.addParameter(strAction ,ACT5);
				
				
				
				T.add(R,col++,row);
				
				*/
				row++;
			}
			T.setWidth("100%");
			T.setCellpadding(2);
			T.setCellspacing(1);
			T.setHorizontalZebraColored(Edit.colorLight, Edit.colorWhite);
			T.setRowColor(1, Edit.colorMiddle);
			row++;
		}
		else
			T.add(iwrb.getLocalizedString("no_groups", "No groups"), 1, row);
		return T;
	}
	private PresentationObject getTableOfAssessmentAccounts(IWContext iwc) {
		Table T = new Table();
		String id = iwc.getParameter("entry_group_id");
		if (id != null) {
			List L = Finder.listOfEntriesInGroup(Integer.parseInt(id));
			if (L != null) {
				int len = L.size();
				T.add(Edit.titleText(iwrb.getLocalizedString("entry_name", "Entry name")), 1, 1);
				T.add(Edit.titleText(iwrb.getLocalizedString("last_updated", "Last updated")), 2, 1);
				T.add(Edit.titleText(iwrb.getLocalizedString("amount", "Amount")), 3, 1);
				int col = 1;
				int row = 2;
				AccountEntry A;
				java.text.NumberFormat nf = java.text.NumberFormat.getNumberInstance(iwc.getCurrentLocale());
				for (int i = 0; i < len; i++) {
					col = 1;
					A = (AccountEntry) L.get(i);
					T.add(Edit.formatText(A.getName()), col++, row);
					T.add(Edit.formatText(new IWTimestamp(A.getLastUpdated()).getLocaleDate(iwc)), col++, row);
					T.add(Edit.formatText(nf.format(A.getTotal())), col++, row);
					row++;
				}
				T.setWidth("100%");
				T.setCellpadding(2);
				T.setCellspacing(1);
				T.setHorizontalZebraColored(Edit.colorLight, Edit.colorWhite);
				T.setRowColor(1, Edit.colorMiddle);
				row++;
			}
			else
				add("is empty");
		}
		return T;
	}
	private PresentationObject doMainTable(IWContext iwc) {
		Form F = new Form();
		Table T = new Table();
		int row = 2;
		T.add(Edit.formatText(iwrb.getLocalizedString("entries_from", "Entries from")), 1, row);
		IWTimestamp today = IWTimestamp.RightNow();
		DateInput di1 = new DateInput("ent_from", true);
		//di.setDate(new IWTimestamp(1,today.getMonth(),today.getYear()).getSQLDate());
		T.add(di1, 2, row);
		row++;
		T.add(Edit.formatText(iwrb.getLocalizedString("entries_to", "Entries to")), 1, row);
		DateInput di2 = new DateInput("ent_to", true);
		di2.setDate(today.getSQLDate());
		T.add(di2, 2, row);
		SubmitButton sb = new SubmitButton("commit", iwrb.getLocalizedString("commit", "Commit"));
		Edit.setStyle(sb);
		DropdownMenu drpAccountTypes = new DropdownMenu("account_type");
		drpAccountTypes.addMenuElement(
			com.idega.block.finance.data.AccountBMPBean.typeFinancial,
			iwrb.getLocalizedString("financial", "Financial"));
		drpAccountTypes.addMenuElement(
			com.idega.block.finance.data.AccountBMPBean.typePhone,
			iwrb.getLocalizedString("phone", "phone"));
		Edit.setStyle(drpAccountTypes);
		row++;
		T.add(sb, 2, row);
		row++;
		T.setHorizontalZebraColored(Edit.colorLight, Edit.colorWhite);
		T.setRowColor(1, Edit.colorMiddle);
		T.mergeCells(1, 1, 2, 1);
		T.setWidth("100%");
		F.add(new HiddenInput(this.strAction, String.valueOf(this.ACT3)));
		F.add(T);
		return F;
	}
	private DropdownMenu drpAccountKeys(List AK, String name) {
		DropdownMenu drp = new DropdownMenu(name);
		drp.addMenuElement(0, "--");
		if (AK != null) {
			drp.addMenuElements(AK);
		}
		return drp;
	}
	private PresentationObject makeEntryGroups() {
		return new Text();
	}
	private Link getGroupLink(String name, int id) {
		Link L = new Link(name);
		L.addParameter(strAction, ACT4);
		L.addParameter("entry_group_id", id);
		L.setFontSize(Edit.textFontSize);
		return L;
	}
	
	public void main(IWContext iwc) {
		control(iwc);
	}
}
