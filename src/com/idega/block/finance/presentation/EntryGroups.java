package com.idega.block.finance.presentation;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.block.finance.business.AssessmentBusiness;
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
				
				setLocalizedTitle("entry_groups", "Entry groups");
				setSearchPanel(makeLinkTable(1));
				setMainPanel(MO);
			
			}
			catch (Exception S) {
				S.printStackTrace();
			}
		}
		else
			add(getErrorText(localize("access_denied", "Access denies")));
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
			return getHeader(localize("group_created", "Group was created"));
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return getHeader(localize("group_not_created", "Group was not created"));
		}
	}
	protected PresentationObject makeLinkTable(int menuNr) {
		Table LinkTable = new Table(3, 1);
		int last = 3;
		LinkTable.setWidth(Table.HUNDRED_PERCENT);
		LinkTable.setCellpadding(2);
		LinkTable.setCellspacing(1);
		
		LinkTable.setWidth(last, Table.HUNDRED_PERCENT);
		Link Link1 = new Link(getHeader(localize("view", "View")));
		
		Link1.addParameter(this.strAction, String.valueOf(this.ACT1));
		Link Link2 = new Link(getHeader(localize("new", "New")));
		
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
		//List L = Finder.listOfEntryGroups();
		Collection groups = null;
		try {
			groups = getFinanceService().getEntryGroupHome().findAll();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		if (groups != null) {
			
			T.add(getHeader(localize("group_id", "Group id")), 1, 1);
			T.add(getHeader(localize("group_date", "Group date")), 2, 1);
			T.add(getHeader(localize("entry_from", "Entries from")), 3, 1);
			T.add(getHeader(localize("entry_to", "Entries to")), 4, 1);
			T.add(getHeader(localize("file_name", "File name")), 5, 1);
			T.add(getHeader(localize("real_count", "Real count")), 6, 1);
			//T.add(getHeader(sRollBack),4,1);
			int col = 1;
			row = 2;
			EntryGroup EG;
			for (Iterator iter = groups.iterator(); iter.hasNext();) {
				EG = (EntryGroup) iter.next();
		
				col = 1;
				
				T.add(getGroupLink(EG.getName(), (Integer)EG.getPrimaryKey()), col++, row);
				T.add(getText(new IWTimestamp(EG.getGroupDate()).getLocaleDate(iwc)), col++, row);
				T.add(getText(String.valueOf(EG.getEntryIdFrom())), col++, row);
				T.add(getText(String.valueOf(EG.getEntryIdTo())), col++, row);
				T.add(getText(EG.getFileName()), col++, row);
				AssessmentBusiness assBuiz =
					(AssessmentBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, AssessmentBusiness.class);
				T.add(getText(String.valueOf(assBuiz.getGroupEntryCount(EG))), col++, row);
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
			T.setHorizontalZebraColored(getZebraColor1(),getZebraColor2());
			T.setRowColor(1, getHeaderColor());
			row++;
		}
		else
			T.add(localize("no_groups", "No groups"), 1, row);
		return T;
	}
	private PresentationObject getTableOfAssessmentAccounts(IWContext iwc) {
		Table T = new Table();
		String id = iwc.getParameter("entry_group_id");
		if (id != null) {
			//List L = Finder.listOfEntriesInGroup(Integer.parseInt(id));
			Collection entries = null;
			try {
				entries = getFinanceService().getAccountEntryHome().findByEntryGroup(Integer.valueOf(id));
			} 
			catch (NumberFormatException e) {
				e.printStackTrace();
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			catch (FinderException e) {
				e.printStackTrace();
			}
			if (entries != null) {
			
				T.add(getHeader(localize("entry_name", "Entry name")), 1, 1);
				T.add(getHeader(localize("last_updated", "Last updated")), 2, 1);
				T.add(getHeader(localize("amount", "Amount")), 3, 1);
				int col = 1;
				int row = 2;
				AccountEntry A;
				//java.text.NumberFormat nf = java.text.NumberFormat.getNumberInstance(iwc.getCurrentLocale());
				for (Iterator iter = entries.iterator(); iter.hasNext();) {
					A = (AccountEntry) iter.next();
					col = 1;
					T.add(getText(A.getName()), col++, row);
					T.add(getText(new IWTimestamp(A.getLastUpdated()).getLocaleDate(iwc)), col++, row);
					T.add(getAmountText((A.getTotal())), col++, row);
					row++;
				}
				T.setWidth("100%");
				T.setCellpadding(2);
				T.setCellspacing(1);
				T.setHorizontalZebraColored(getZebraColor1(),getZebraColor2());
				T.setRowColor(1, getHeaderColor());
				row++;
			}
			else
				add(getErrorText("is empty"));
		}
		return T;
	}
	private PresentationObject doMainTable(IWContext iwc) {
		Form F = new Form();
		Table T = new Table();
		int row = 2;
		T.add(getHeader(localize("entries_from", "Entries from")), 1, row);
		IWTimestamp today = IWTimestamp.RightNow();
		DateInput di1 = new DateInput("ent_from", true);
		//di.setDate(new IWTimestamp(1,today.getMonth(),today.getYear()).getSQLDate());
		T.add(di1, 2, row);
		row++;
		T.add(getHeader(localize("entries_to", "Entries to")), 1, row);
		DateInput di2 = new DateInput("ent_to", true);
		di2.setDate(today.getSQLDate());
		T.add(di2, 2, row);
		SubmitButton sb = new SubmitButton("commit", localize("commit", "Commit"));
		sb = (SubmitButton) setStyle(sb,STYLENAME_INTERFACE);
		DropdownMenu drpAccountTypes = new DropdownMenu("account_type");
		drpAccountTypes.addMenuElement(
			com.idega.block.finance.data.AccountBMPBean.typeFinancial,
			localize("financial", "Financial"));
		drpAccountTypes.addMenuElement(
			com.idega.block.finance.data.AccountBMPBean.typePhone,
			localize("phone", "phone"));
		drpAccountTypes = (DropdownMenu) setStyle(drpAccountTypes,STYLENAME_INTERFACE);
		row++;
		T.add(sb, 2, row);
		row++;
		T.setHorizontalZebraColored(getZebraColor1(),getZebraColor2());
		T.setRowColor(1, getHeaderColor());
		T.mergeCells(1, 1, 2, 1);
		T.setWidth("100%");
		F.add(new HiddenInput(this.strAction, String.valueOf(this.ACT3)));
		F.add(T);
		return F;
	}

	private Link getGroupLink(String name, Integer id) {
		Link L = new Link(getText(name));
		L.addParameter(strAction, ACT4);
		L.addParameter("entry_group_id", id.toString());
		
		return L;
	}
	
	public void main(IWContext iwc) {
		control(iwc);
	}
}
