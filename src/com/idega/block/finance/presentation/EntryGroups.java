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
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.IWTimestamp;

/**
 * 
 * Title: Description: Copyright: Copyright (c) 2000-2001 idega.is All Rights
 * Reserved Company: idega
 * 
 * @author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 * 
 */
public class EntryGroups extends Finance {

	protected static final String LOC_KEY_REAL_COUNT = "real_count";

	protected static final String LOC_KEY_FILE_NAME = "file_name";

	protected static final String LOC_KEY_GROUP_DATE = "group_date";

	protected static final String LOC_KEY_NEW = "new";

	protected static final String LOC_KEY_VIEW = "view";

	protected static final String LOC_KEY_GROUP_NOT_CREATED = "group_not_created";

	protected static final String LOC_KEY_GROUP_CREATED = "group_created";

	protected static final String LOC_KEY_INVALID_DATE = "invalid_date";

	protected static final String LOC_KEY_SUCCESS = "succeded";

	protected static final String LOC_KEY_FAILED = "failed";

	protected static final String LOC_KEY_ACCESS_DENIED = "access_denied";

	protected static final String LOC_KEY_NO_GROUPS = "no_groups";

	protected static final String LOC_KEY_ENTRY_NAME = "entry_name";

	protected static final String LOC_KEY_LAST_UPDATED = "last_updated";

	protected static final String LOC_KEY_AMOUNT = "amount";

	protected static final String LOC_KEY_IS_EMPTY = "is_empty";

	protected static final String PARAM_DUE_DATE = "due_date";

	protected static final String PARAM_PAYMENT_DATE = "payment_date";

	protected static final String PARAM_COMMIT = "commit";

	protected static final String PARAM_GROUP_DATE_FROM = "entries_from";

	protected static final String PARAM_GROUP_DATE_TO = "entries_to";

	protected static final String PARAM_ENTRY_GROUP_ID = "entry_group_id";

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
						case ACT1:
							MO = getTableOfGroups(iwc);
							break;
						case ACT2:
							MO = doMainTable(iwc);
							break;
						case ACT3:
							MO = doSomeThing(iwc);
							break;
						case ACT4:
							MO = getTableOfAssessmentAccounts(iwc);
							break;
						default:
							MO = getTableOfGroups(iwc);
							break;
					}
				}

				setLocalizedTitle(getLocalizedNameKey(), getLocalizedNameValue());
				setSearchPanel(makeLinkTable(1));
				setMainPanel(MO);

			}
			catch (Exception S) {
				S.printStackTrace();
			}
		}
		else
			add(getErrorText(localize(LOC_KEY_ACCESS_DENIED, "Access denies")));
	}

	protected PresentationObject doSomeThing(IWContext iwc) {
		PresentationObject MO = new Text(localize(LOC_KEY_INVALID_DATE, "Invalid to date"));
		String dateFrom = iwc.getParameter(PARAM_GROUP_DATE_FROM);
		String paymentDateString = iwc.getParameter(PARAM_PAYMENT_DATE);
		String dueDateString = iwc.getParameter(PARAM_DUE_DATE);
		if (dateFrom != null && !"".equals(dateFrom) && paymentDateString != null && !"".equals(paymentDateString)
				&& dueDateString != null && !"".equals(dueDateString)) {
			String dateTo = iwc.getParameter(PARAM_GROUP_DATE_TO);
			IWTimestamp to = new IWTimestamp(dateTo);
			IWTimestamp from = null;
			if (!"".equals(dateFrom))
				from = new IWTimestamp(dateFrom);

			IWTimestamp paymentDate = new IWTimestamp(paymentDateString);
			IWTimestamp dueDate = new IWTimestamp(dueDateString);
			doGroup(iwc, from, to, paymentDate, dueDate);

			return new Text(localize(LOC_KEY_SUCCESS, "Succeded"));
		}
		return MO;
	}

	protected PresentationObject doGroup(IWContext iwc, IWTimestamp from, IWTimestamp to, IWTimestamp paymentDate, IWTimestamp dueDate) {
		try {
			AssessmentBusiness assBuiz = (AssessmentBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc,
					AssessmentBusiness.class);
			EntryGroup group = assBuiz.groupEntriesWithSQL(from, to, paymentDate, dueDate);
							
			return getHeader(localize(LOC_KEY_GROUP_CREATED, "Group was created"));
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return getHeader(localize(LOC_KEY_GROUP_NOT_CREATED, "Group was not created"));
		}
	}

	protected PresentationObject makeLinkTable(int menuNr) {
		Table LinkTable = new Table(3, 1);
		int last = 3;
		LinkTable.setWidth(Table.HUNDRED_PERCENT);
		LinkTable.setCellpadding(2);
		LinkTable.setCellspacing(1);

		LinkTable.setWidth(last, Table.HUNDRED_PERCENT);
		Link Link1 = new Link(getHeader(localize(LOC_KEY_VIEW, "View")));

		Link1.addParameter(this.strAction, String.valueOf(this.ACT1));
		Link Link2 = new Link(getHeader(localize(LOC_KEY_NEW, "New")));

		Link2.addParameter(this.strAction, String.valueOf(this.ACT2));
		if (isAdmin) {
			LinkTable.add(Link1, 1, 1);
			LinkTable.add(Link2, 2, 1);
		}
		return LinkTable;
	}

	protected PresentationObject getTableOfGroups(IWContext iwc) throws java.rmi.RemoteException {
		Table displayTable = new Table();
		int row = 1;
		Collection groups = null;
		try {
			groups = getFinanceService().getEntryGroupHome().findAll();
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		if (groups != null) {
			displayTable.add(getHeader(localize(PARAM_ENTRY_GROUP_ID, "Group id")), 1, row);
			displayTable.add(getHeader(localize(LOC_KEY_GROUP_DATE, "Group date")), 2, row);
			displayTable.add(getHeader(localize(PARAM_GROUP_DATE_FROM, "Entries from")), 3, row);
			displayTable.add(getHeader(localize(PARAM_GROUP_DATE_TO, "Entries to")), 4, row);
			displayTable.add(getHeader(localize(LOC_KEY_FILE_NAME, "File name")), 5, row);
			displayTable.add(getHeader(localize(LOC_KEY_REAL_COUNT, "Real count")), 6, row);
			displayTable.add(getHeader(localize(PARAM_PAYMENT_DATE, "Payment date")), 7, row);
			displayTable.add(getHeader(localize(PARAM_DUE_DATE, "Due date")), 8, row++);

			EntryGroup EG;
			for (Iterator iter = groups.iterator(); iter.hasNext();) {
				EG = (EntryGroup) iter.next();
				displayTable.add(getGroupLink(EG.getName(), (Integer) EG.getPrimaryKey()), 1, row);
				displayTable.add(getText(new IWTimestamp(EG.getGroupDate()).getLocaleDate(iwc.getCurrentLocale())), 2,
						row);
				displayTable.add(getText(String.valueOf(EG.getEntryIdFrom())), 3, row);
				displayTable.add(getText(String.valueOf(EG.getEntryIdTo())), 4, row);
				if (EG.getFileId() > 0) {
					displayTable.add(new Link(EG.getFileId()), 5, row);				
				}
				AssessmentBusiness assBuiz = (AssessmentBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc,
						AssessmentBusiness.class);
				displayTable.add(getText(String.valueOf(assBuiz.getGroupEntryCount(EG))), 6, row);
				if (EG.getFileInvoiceDate() != null) {
				displayTable.add(
						getText(new IWTimestamp(EG.getFileInvoiceDate()).getLocaleDate(iwc.getCurrentLocale())), 7, row);
				}
				if (EG.getFileDueDate() != null) {
				displayTable.add(getText(new IWTimestamp(EG.getFileDueDate()).getLocaleDate(iwc.getCurrentLocale())),
						8, row);
				}
				row++;
			}
			displayTable.setWidth("100%");
			displayTable.setCellpadding(2);
			displayTable.setCellspacing(1);
			displayTable.setHorizontalZebraColored(getZebraColor1(), getZebraColor2());
			displayTable.setRowColor(1, getHeaderColor());
			row++;
		}
		else
			displayTable.add(localize(LOC_KEY_NO_GROUPS, "No groups"), 1, row);
		return displayTable;
	}

	protected PresentationObject getTableOfAssessmentAccounts(IWContext iwc) {
		Table T = new Table();
		String id = iwc.getParameter(PARAM_ENTRY_GROUP_ID);
		if (id != null) {
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

				T.add(getHeader(localize(LOC_KEY_ENTRY_NAME, "Entry name")), 1, 1);
				T.add(getHeader(localize(LOC_KEY_LAST_UPDATED, "Last updated")), 2, 1);
				T.add(getHeader(localize(LOC_KEY_AMOUNT, "Amount")), 3, 1);
				int col = 1;
				int row = 2;
				AccountEntry A;
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
				T.setHorizontalZebraColored(getZebraColor1(), getZebraColor2());
				T.setRowColor(1, getHeaderColor());
				row++;
			}
			else
				add(getErrorText(localize(LOC_KEY_IS_EMPTY, "is empty")));
		}
		return T;
	}

	protected PresentationObject doMainTable(IWContext iwc) {
		Table inputTable = new Table();
		int row = 2;
		inputTable.add(getHeader(localize(PARAM_GROUP_DATE_FROM, "Entries from")), 1, row);
		IWTimestamp today = IWTimestamp.RightNow();
		DateInput di1 = new DateInput(PARAM_GROUP_DATE_FROM, true);
		inputTable.add(di1, 2, row++);
		inputTable.add(getHeader(localize(PARAM_GROUP_DATE_TO, "Entries to")), 1, row);
		DateInput di2 = new DateInput(PARAM_GROUP_DATE_TO, true);
		di2.setDate(today.getDate());
		inputTable.add(di2, 2, row++);
		inputTable.add(getHeader(localize(PARAM_PAYMENT_DATE, "Payment date")), 1, row);
		DateInput paymentDate = new DateInput(PARAM_PAYMENT_DATE, true);
		inputTable.add(paymentDate, 2, row++);
		inputTable.add(getHeader(localize(PARAM_DUE_DATE, "Due date")), 1, row);
		DateInput dueDate = new DateInput(PARAM_DUE_DATE, true);
		inputTable.add(dueDate, 2, row++);

		SubmitButton sb = new SubmitButton(PARAM_COMMIT, localize(PARAM_COMMIT, "Commit"));
		sb = (SubmitButton) setStyle(sb, STYLENAME_INTERFACE);
		inputTable.setAlignment(2, row, "RIGHT");
		inputTable.add(sb, 2, row);
		inputTable.setHorizontalZebraColored(getZebraColor1(), getZebraColor2());
		inputTable.setRowColor(1, getHeaderColor());
		inputTable.mergeCells(1, 1, 2, 1);
		inputTable.setWidth("100%");
		inputTable.add(new HiddenInput(strAction, String.valueOf(ACT3)));

		return inputTable;
	}

	protected Link getGroupLink(String name, Integer id) {
		Link L = new Link(getText(name));
		L.addParameter(strAction, ACT4);
		L.addParameter(PARAM_ENTRY_GROUP_ID, id.toString());

		return L;
	}

	public void main(IWContext iwc) {
		control(iwc);
	}
}