package com.idega.block.finance.presentation;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.ejb.FinderException;

import com.idega.block.finance.data.Account;
import com.idega.block.finance.data.AccountEntry;
import com.idega.block.finance.data.AccountInfo;
import com.idega.block.finance.data.AccountPhoneEntry;
import com.idega.block.finance.data.AssessmentStatus;
import com.idega.block.finance.data.FinanceAccount;
import com.idega.core.user.data.User;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWTimestamp;
/**
 * Title: Description: Copyright: Copyright (c) 2000-2001 idega.is All Rights
 * Reserved Company: idega
 * 
 * @author <a href="mailto:aron@idega.is">Aron Birkir </a>
 * @version 1.1
 */
public class AccountViewer extends Finance {
	private boolean isLoggedOn;
	private Image image;
	private float tax = 1.245f;
	private String sDebetColor, sKreditColor;
	private final String prmFromDate = "from_date", prmToDate = "to_date";
	public static final String prmUserId = "user_id", prmAccountId = "fin_acc_id";
	public static final String prmClean = "av_clean";
	private Collection accounts = null;
	private User eUser = null;
	protected String styleAttribute = "font-size: 8pt";
	private Integer userID = null, accountID = null;
	private boolean specialview = false;
	private DateFormat df,tf;
	private String roundStatus = null;
	
	public AccountViewer() {
		this(-1);
	}
	public AccountViewer(User eUser) {
		this(eUser.getID());
	}
	public AccountViewer(int userID) {
		this.userID = new Integer(userID);
	}
	protected void control(IWContext iwc) throws java.rmi.RemoteException {
		image = Table.getTransparentCell(iwc);
		image.setHeight(6);
		checkIds(iwc);
		IWTimestamp itFromDate = getFromDate(iwc);
		IWTimestamp itToDate = getToDate(iwc);
		specialview = iwc.isParameterSet("specview");
		boolean clean = iwc.isParameterSet(prmClean);
		if(!isAdmin){
			roundStatus = AssessmentStatus.PUBLISHED;
		}
		if (isAdmin || isLoggedOn) {
			if (accounts != null && accounts.size() > 0) {
				FinanceAccount eAccount = getAccount(accountID, accounts);
				setMainPanel(getAccountView(eAccount, accounts, itFromDate, itToDate, isAdmin, clean));
			} else
				setMainPanel(getErrorText(localize("no_account_selected","No account selected")));
		} else {
			setMainPanel(getErrorText(localize("accessdenied", "Access denied")));
		}
	}
	private FinanceAccount getAccount(Integer accountID, Collection listOfAccounts) throws java.rmi.RemoteException {
		Iterator iter = listOfAccounts.iterator();
	    if (accountID == null) {
	    		FinanceAccount account =  (FinanceAccount)iter.next();
	    		this.accountID = account.getAccountId();
	    		return account;
	    }
	    
		
		FinanceAccount account = (FinanceAccount) iter.next();
		while (iter.hasNext()) {
			FinanceAccount acc = (FinanceAccount) iter.next();
			if (acc.getAccountId().intValue() == accountID.intValue()) {
				account = acc;
				break;
			}
		}
		return account;
	}
	public PresentationObject getMainTable(IWContext iwc) {
		return new Text();
	}
	public PresentationObject getAccountView(FinanceAccount eAccount, Collection accounts, IWTimestamp FromDate,
			IWTimestamp ToDate, boolean showallkeys, boolean clean) throws java.rmi.RemoteException {
		Table T = new Table(1, 3);
		T.setWidth(Table.HUNDRED_PERCENT);
		T.add(getEntrySearchTable(accountID, accounts, FromDate, ToDate), 1, 2);
		if (clean) {
			T.add(getCleanAccountTable(accountID), 1, 2);
			T.add(getEntryTable(accountID, FromDate, ToDate, showallkeys, true), 1, 3);
		} else {
			T.add(getAccountTable(eAccount, accounts), 1, 2);
			T.add(getEntryTable(eAccount, FromDate, ToDate, showallkeys, false), 1, 3);
		}
		return T;
	}
	private String getDateString(IWTimestamp stamp) {
		return stamp.getISLDate(".", true);
	}
	public PresentationObject getEntrySearchTable(Integer accountID, Collection accounts, IWTimestamp from,
			IWTimestamp to) {
		Table T = new Table(6, 2);
		T.setWidth(Table.HUNDRED_PERCENT);
		String sFromDate = getDateString(from);
		String sToDate = getDateString(to);
		//DropdownMenu drpAccounts = new DropdownMenu(accounts,prmAccountId);
		//drpAccounts.setToSubmit();
		//drpAccounts.setAttribute("style",styleAttribute);
		//drpAccounts.setSelectedElement(String.valueOf(accountID));
		if (accountID != null) {
		    T.add(new HiddenInput(prmAccountId, accountID.toString()));
		}
		TextInput tiFromDate = new TextInput(prmFromDate, sFromDate);
		tiFromDate.setLength(10);
		tiFromDate.setMarkupAttribute("style", styleAttribute);
		TextInput tiToDate = new TextInput(prmToDate, sToDate);
		tiToDate.setLength(10);
		tiToDate.setMarkupAttribute("style", styleAttribute);
		SubmitButton fetch = new SubmitButton("fetch", localize("fetch", "Fetch"));
		fetch.setMarkupAttribute("style", styleAttribute);
		CheckBox specialCheck = new CheckBox("specview");
		if (specialview)
			specialCheck.setChecked(true);
		int row = 1;
		int col = 1;
		//T.add(formatText(localize("account","Account")),1,row);
		T.add(getHeader(localize("from", "From")), col++, row);
		T.add(getHeader(localize("to", "To")), col++, row);
		T.add(getHeader(localize("special", "Special")), col++, row);
		row++;
		col = 1;
		//T.add(drpAccounts,1,row);
		T.add(tiFromDate, col++, row);
		T.add(tiToDate, col++, row);
		T.add(specialCheck, col++, row);
		T.add(fetch, col++, row);
		T.setWidth(col, row, "100%");
		//myForm.add(new
		// HiddenInput(IWMainApplication.classToInstanciateParameter,"com.idega.block.finance.presentation.AccountViewer"));
		
		return T;
	}
	public PresentationObject getAccountTable(FinanceAccount eAccount, Collection accounts)
			throws java.rmi.RemoteException {
		if (eAccount != null) {
			if (eUser.getID() != eAccount.getUserId()) {
				try {
					eUser = getFinanceService().getAccountUserHome().findByPrimaryKey(new Integer(eAccount.getUserId()));
				} catch (RemoteException e) {
					e.printStackTrace();
				} catch (FinderException e) {
					e.printStackTrace();
				}
			}
		}
		
		DataTable T = new DataTable();
		T.setUseBottom(false);
		T.setTitlesHorizontal(true);
		T.addTitle(localize("accounts", "Accounts"));
		T.setWidth(Table.HUNDRED_PERCENT);
		int row = 1;
		int col = 1;
		T.add(getHeader(localize("account", "Account")), col++, row);
		T.add(getHeader(localize("owner", "Owner")), col++, row);
		T.add(getHeader(localize("lastentry", "Last entry")), col++, row);
		
		if(isAdmin){
			T.add(getHeader(localize("published_balance", "Published")), col++, row);
			T.add(getHeader(localize("total_balance", "Total")), col++, row);
		}
		else{
			T.add(getHeader(localize("balance", "Balance")), col++, row);
		}
		row++;
		if (accounts != null) {
			Iterator iter = accounts.iterator();
			while (iter.hasNext()) {
				col = 1;
				FinanceAccount account = (FinanceAccount) iter.next();
				Link accountLink = getLink((account.getAccountName()));
				accountLink.addParameter(prmAccountId, account.getAccountId().toString());
				//if(eUser.getID() != account.getUserId())
				accountLink.addParameter(prmUserId, account.getUserId());
				T.add(accountLink, col++, row);
				T.add(getText(eUser.getName()), col++, row);
				T.add(getText(getDateString(new IWTimestamp(account.getLastUpdated()))), col++, row);
				double b = getFinanceService().getAccountBalance(account.getAccountId(),AssessmentStatus.PUBLISHED);
					//b = b * tax;
				
				T.add(getAmountText( b), col++, row);
				if(isAdmin)
					T.add(getAmountText(getFinanceService().getAccountBalance(account.getAccountId())),col++,row);
				row++;
			}
			T.getContentTable().setColumnAlignment(4, Table.HORIZONTAL_ALIGN_RIGHT);
			T.getContentTable().setColumnAlignment(3, Table.HORIZONTAL_ALIGN_RIGHT);
			if(isAdmin)
				T.getContentTable().setColumnAlignment(5, Table.HORIZONTAL_ALIGN_RIGHT);
		} else {
			T.add(localize("no_account", "No Account"));
		}
		return T;
	}
	public PresentationObject getCleanAccountTable(Integer accountID) {
		AccountInfo eAccount = null;
		try {
			eAccount = getFinanceService().getAccountInfoHome().findByPrimaryKey(accountID);
			//AccountInfo eAccount =
			// FinanceFinder.getInstance().getAccountInfo(AccountId);
			if (eAccount != null)
				eUser = getFinanceService().getAccountUserHome().findByPrimaryKey(new Integer(eAccount.getUserId()));
			// eUser =
			// FinanceFinder.getInstance().getUser(eAccount.getUserId());
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		Table T = new Table(2, 4);
		if (eAccount != null) {
			T.setWidth(Table.HUNDRED_PERCENT);
			T.setCellspacing(0);
			T.setCellpadding(2);
			//int fontSize = 1;
			int row = 1;
			T.add(getHeader(localize("account", "Account")), 1, row);
			T.add(getText(eAccount.getName()), 2, row);
			row++;
			T.add(getHeader(localize("owner", "Owner")), 1, row);
			T.add(getText(eUser.getName()), 2, row);
			row++;
			T.add(getHeader(localize("lastentry", "Last Entry")), 1, row);
			T.add(getText(getDateString(new IWTimestamp(eAccount.getLastUpdated()))), 2, row);
			row++;
			T.add(getHeader(localize("balance", "Balance")), 1, row);
			float b = eAccount.getBalance();
			T.add(getAmountText(b), 2, row);
			row++;
		} else {
			T.add(localize("no_account", "No Account"));
		}
		return T;
	}
	private PresentationObject getEntryTable(Integer accountID, IWTimestamp from, IWTimestamp to, boolean showallkeys,
			boolean clean) {
		PresentationObject mo = null;
		try {
			//Account a = accBuiz.getAccount(accountID);
			//Account a =
			// ((com.idega.block.finance.data.AccountHome)com.idega.data.IDOLookup.getHomeLegacy(Account.class)).findByPrimaryKeyLegacy(accountID);
			Account a = getFinanceService().getAccountHome().findByPrimaryKey(accountID);
			mo = getEntryTable(a, from, to, showallkeys, clean);
		} catch (Exception ex) {
			ex.printStackTrace();
			mo = new Text();
		}
		return mo;
	}
	private PresentationObject getEntryTable(FinanceAccount account, IWTimestamp from, IWTimestamp to,
			boolean showallkeys, boolean clean) throws java.rmi.RemoteException {
		Collection entries = null;
		
		if (account == null) {
		    return new Text();
		}
		
		try {
			if (account.getAccountType().equals(getFinanceService().getAccountTypeFinance())) {
				if (showallkeys) {
					//entries =
					// getFinanceService().//accBuiz.listOfAccountEntries(eAccount.getAccountId(),from,to);
					entries = getFinanceService().getAccountBusiness().getAccountEntries(accountID.intValue(),from, to,null,roundStatus);
				} else {
					//entries =
					// accBuiz.listOfKeySortedEntries(account.getAccountId(),from,to);
					entries = getFinanceService().getAccountBusiness().getKeySortedAccountEntries(accountID.intValue(),from, to,roundStatus);
				}
				if (clean)
					return getCleanFinanceEntryTable(account, entries, from, to);
				else
					return getFinanceEntryTable(account, entries, from, to);
			} else if (account.getAccountType().equals(getFinanceService().getAccountTypePhone())) {
				entries = getFinanceService().getAccountPhoneEntryHome().findByAccountAndStatus(accountID, null,
						from.getDate(), to.getDate());
				//listEntries =
				// accBuiz.listOfPhoneEntries(account.getAccountId(),from,to);
				if (specialview)
					return getPhoneEntryReportTable(account, entries, from, to);
				else
					return getPhoneEntryTable(account, entries, from, to);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		return new Text();
	}
	private PresentationObject getPhoneEntryTable(FinanceAccount eAccount, Collection listEntries, IWTimestamp from,
			IWTimestamp to) {
		int tableDepth = 4;
		int cols = 6;
		if (listEntries != null) {
			tableDepth += listEntries.size();
		}
		int row = 1;
		Table T = new Table(cols, tableDepth);
		T.setWidth(Table.HUNDRED_PERCENT);
		T.setCellspacing(0);
		T.setCellpadding(2);
		T.setColumnAlignment(1, Table.HORIZONTAL_ALIGN_LEFT);
		T.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_RIGHT);
		T.setColumnAlignment(cols, Table.HORIZONTAL_ALIGN_RIGHT);
		T.setAlignment(1, 1, Table.HORIZONTAL_ALIGN_LEFT);
		T.setAlignment(1, 2, Table.HORIZONTAL_ALIGN_LEFT);
		T.setHorizontalZebraColored(FinanceColors.LIGHTGREY, FinanceColors.WHITE);
		T.setRowColor(1, FinanceColors.DARKBLUE);
		T.setRowColor(2, FinanceColors.DARKGREY);
		int fontSize = 1;
		String title = localize("entries", "Entries") + "  " + localize("for", "for") + " "
				+ df.format(from.getSQLDate()) + " - " + df.format(to.getSQLDate());
		Text Title = new Text(title, true, false, false);
		Title.setFontColor(FinanceColors.WHITE);
		T.add(Title, 1, row);
		T.mergeCells(1, row, cols, row);
		row++;
		Text[] TableTitles = new Text[cols];
		TableTitles[0] = getHeader(localize("dating", "Dating"));
		TableTitles[1] = getHeader(localize("number", "Number"));
		TableTitles[2] = getHeader(localize("night_time", "Night time"));
		TableTitles[3] = getHeader(localize("day_time", "Day time"));
		TableTitles[4] = getHeader(localize("time", "Time"));
		TableTitles[5] = getHeader(localize("amount", "Amount"));
		for (int i = 0; i < TableTitles.length; i++) {
			TableTitles[i].setFontSize(fontSize);
			T.add(TableTitles[i], i + 1, row);
		}
		row++;
		Text[] TableTexts = new Text[cols];
		boolean debet = false;
		if (listEntries != null) {
			int totNight = 0, totDay = 0, totDur = 0;
			float totPrice = 0;
			for (Iterator iter = listEntries.iterator(); iter.hasNext();) {
				AccountPhoneEntry entry = (AccountPhoneEntry) iter.next();
				//TableTexts[0] = new Text(getDateString(new
				// IWTimestamp(entry.getLastUpdated())));
				//TableTexts[1] = new Text(entry.getMainNumber());
				//TableTexts[0] = new Text(entry.getSubNumber());
				TableTexts[0] = getText(new IWTimestamp(entry.getPhonedStamp()).toSQLString());
				TableTexts[1] = getText(entry.getPhonedNumber());
				TableTexts[2] = getText(getCorrectedTimeString(entry.getNightDuration()));
				TableTexts[3] = getText(getCorrectedTimeString(entry.getDayDuration()));
				TableTexts[4] = getText(getCorrectedTimeString(entry.getDuration()));
				totNight += entry.getNightDuration();
				totDay += entry.getDayDuration();
				totDur += entry.getDuration();
				float p = entry.getPrice();
				totPrice += p;
				debet = p >= 0 ? true : false;
				TableTexts[5] = getAmountText((p));
				for (int i = 0; i < cols; i++) {
					TableTexts[i].setFontSize(fontSize);
					TableTexts[i].setFontColor("#000000");
					if (i == 5) {
						if (debet)
							TableTexts[i].setFontColor(sDebetColor);
						else
							TableTexts[i].setFontColor(sKreditColor);
					} else
						TableTexts[i].setFontColor("#000000");
					T.add(TableTexts[i], i + 1, row);
				}
				row++;
			}
			Text txTotNight = getText(tf.format(new java.sql.Time(totNight * 1000)));
			Text txTotDay = getText(tf.format(new java.sql.Time(totDay * 1000)));
			Text txTotDur = getText(tf.format(new java.sql.Time(totDur * 1000)));
			Text txTotPrice = getAmountText((totPrice));
			txTotNight.setFontColor("#000000");
			txTotDay.setFontColor("#000000");
			txTotDur.setFontColor("#000000");
			if (totPrice >= 0)
				txTotPrice.setFontColor(sDebetColor);
			else
				txTotPrice.setFontColor(sKreditColor);
			txTotNight.setFontSize(fontSize);
			txTotDay.setFontSize(fontSize);
			txTotDur.setFontSize(fontSize);
			txTotPrice.setFontSize(fontSize);
			T.add(txTotNight, 3, row);
			T.add(txTotDay, 4, row);
			T.add(txTotDur, 5, row);
			T.add(txTotPrice, 6, row);
		}
		T.mergeCells(1, row, cols, row);
		T.add(image, 1, row);
		T.setColor(1, row, FinanceColors.DARKRED);
		return T;
	}
	private PresentationObject getPhoneEntryReportTable(FinanceAccount eAccount, Collection listEntries,
			IWTimestamp from, IWTimestamp to) {
		String sMob1 = "8";
		String sMob2 = "6";
		String sFor = "00";
		float tax = 0.245f;
		Table T = new Table(5, 9);
		T.setWidth("100%");
		T.setCellpadding(2);
		T.setCellspacing(1);
		T.mergeCells(1, 1, 5, 1);
		T.setHorizontalZebraColored(FinanceColors.WHITE, FinanceColors.LIGHTGREY);
		T.setRowColor(1, FinanceColors.DARKBLUE);
		T.setRowColor(2, FinanceColors.LIGHTGREY);
		T.setColumnAlignment(3, "right");
		T.setColumnAlignment(4, "right");
		T.setColumnAlignment(5, "right");
		//int fontSize = 1;
		String title = localize("sum_report", "Report") + "  " + localize("for", "for") + " "
				+ df.format(from.getSQLDate()) + " - " + df.format(to.getSQLDate());
		Text Title = getHeader(title);
		Title.setFontColor(FinanceColors.WHITE);
		T.add(Title, 1, 1);
		T.add(getHeader(localize("type", "Type")), 2, 2);
		T.add(getHeader(localize("count", "Count")), 3, 2);
		T.add(getHeader(localize("duration", "Duration")), 4, 2);
		T.add(getHeader(localize("amount", "amount")), 5, 2);
		if (listEntries != null) {
			Iterator IT = listEntries.iterator();
			String phonedNumber;
			long otherTime = 0, forTime = 0, mobTime = 0, totalTime = 0;
			float otherPrice = 0, forPrice = 0, mobPrice = 0, totalPrice = 0;
			int otherCount = 0, forCount = 0, mobCount = 0, totalCount = 0;
			AccountPhoneEntry entry;
			while (IT.hasNext()) {
				entry = (AccountPhoneEntry) IT.next();
				phonedNumber = entry.getPhonedNumber();
				if (phonedNumber != null) {
					if (phonedNumber.startsWith(sFor)) {
						forTime += entry.getDuration();
						forPrice += entry.getPrice();
						forCount++;
					}
					//mobile
					else if (phonedNumber.startsWith(sMob1) || phonedNumber.startsWith(sMob2)) {
						mobTime += entry.getDuration();
						mobPrice += entry.getPrice();
						mobCount++;
					} else {
						otherTime += entry.getDuration();
						otherPrice += entry.getPrice();
						otherCount++;
					}
				}
			}
			totalCount = otherCount + mobCount + forCount;
			totalPrice = otherPrice + mobPrice + forPrice;
			totalTime = otherTime + mobTime + forTime;
			float taxprice = totalPrice * tax;
			T.add(getHeader(localize("other", "Other")), 2, 3);
			T.add(getHeader(localize("mobile", "Mobile")), 2, 4);
			T.add(getHeader(localize("foreign", "Foreign")), 2, 5);
			T.add(getHeader(localize("total", "Total")), 2, 6);
			//new java.sql.Time(entry.getDuration()*1000).toString()
			T.add(getHeader(String.valueOf(otherCount)), 3, 3);
			T.add(getHeader(String.valueOf(mobCount)), 3, 4);
			T.add(getHeader(String.valueOf(forCount)), 3, 5);
			T.add(getHeader(String.valueOf(totalCount)), 3, 6);
			T.add(getHeader(getCorrectedTimeString(otherTime)), 4, 3);
			T.add(getHeader(getCorrectedTimeString(mobTime)), 4, 4);
			T.add(getHeader(getCorrectedTimeString(forTime)), 4, 5);
			T.add(getHeader(getCorrectedTimeString(totalTime)), 4, 6);
			T.add(getAmountText((otherPrice)), 5, 3);
			T.add(getAmountText((mobPrice)), 5, 4);
			T.add(getAmountText((forPrice)), 5, 5);
			T.add(getAmountText((totalPrice)), 5, 6);
			T.add(Text.getNonBrakingSpace(), 4, 7);
			T.add(getHeader(localize("tax", "Tax")), 4, 8);
			T.add(getAmountText((taxprice)), 5, 8);
			T.add(getHeader(localize("amount", "Amount")), 4, 9);
			T.add(getAmountText((totalPrice + taxprice)), 5, 9);
			T.setLineAfterRow(5);
		}
		return T;
	}
	private PresentationObject getFinanceEntryTable(FinanceAccount eAccount, Collection listEntries, IWTimestamp from,
			IWTimestamp to) throws java.rmi.RemoteException {
		int tableDepth = 5;
		if (listEntries != null) {
			tableDepth += listEntries.size();
		}
		int row = 1;
		DataTable T = new DataTable();
		T.setWidth(Table.HUNDRED_PERCENT);
		T.setTitlesHorizontal(true);
		//int fontSize = 1;
		String title = localize("entries", "Entries") + " " + eAccount.getAccountName() + "   "
				+ df.format(from.getSQLDate()) + " - " + df.format(to.getSQLDate());
		T.addTitle(title);
		Text[] TableTitles = new Text[4];
		TableTitles[0] = getHeader(localize("date", "Date"));
		TableTitles[1] = getHeader(localize("description", "Description"));
		TableTitles[2] = getHeader(localize("text", "Text"));
		TableTitles[3] = getHeader(localize("amount", "Amount"));
		for (int i = 0; i < TableTitles.length; i++) {
			//TableTitles[i].setFontSize(fontSize);
			//TableTitles[i].setFontColor(sWhiteColor);
			T.add(TableTitles[i], i + 1, row);
		}
		row++;
		Text[] TableTexts = new Text[4];
		//boolean debet = false;
		if (listEntries != null) {
			double totPrice = 0;
			for (Iterator iter = listEntries.iterator(); iter.hasNext();) {
				AccountEntry entry = (AccountEntry) iter.next();
				TableTexts[0] = getText(getDateString(new IWTimestamp(entry.getLastUpdated())));
				TableTexts[1] = getText(entry.getName());
				TableTexts[2] = getText(entry.getInfo());
				double p = entry.getTotal();
				//debet = p > 0 ? true : false;
				totPrice += p;
				TableTexts[3] = getAmountText((p));
				for (int i = 0; i < 4; i++) {
					T.add(TableTexts[i], i + 1, row);
				}
				row++;
			}
			T.add(getAmountText((totPrice)), 4, row++);
			T.getContentTable().setColumnAlignment(4, Table.HORIZONTAL_ALIGN_RIGHT);
		}
		return T;
	}
	private PresentationObject getCleanFinanceEntryTable(FinanceAccount eAccount, Collection listEntries,
			IWTimestamp from, IWTimestamp to) {
		int tableDepth = 3;
		if (listEntries != null) {
			tableDepth += listEntries.size();
		}
		Table T = new Table(4, tableDepth);
		T.setWidth(Table.HUNDRED_PERCENT);
		T.setCellspacing(0);
		T.setCellpadding(0);
		T.setLineColor("#000000");
		T.setColumnAlignment(1, "right");
		T.setColumnAlignment(2, "left");
		T.setColumnAlignment(3, "left");
		T.setColumnAlignment(4, "right");
		T.setAlignment(1, 1, "left");
		T.setAlignment(1, 2, "left");
		T.setWidth(1, "20");
		int fontSize = 1;
		T.add(getHeader(localize("entries", "Entries")), 1, 1);
		T.mergeCells(1, 1, 4, 1);
		Text[] TableTitles = new Text[4];
		TableTitles[0] = getText(localize("date", "Date"));
		TableTitles[1] = getText(localize("description", "Description"));
		TableTitles[2] = getText(localize("text", "Text"));
		TableTitles[3] = getText(localize("amount", "Amount"));
		for (int i = 0; i < TableTitles.length; i++) {
			T.add(TableTitles[i], i + 1, 2);
		}
		T.add(getHeader(localize("date", "Date")), 1, 2);
		T.add(getHeader(localize("description", "Description")), 2, 2);
		T.add(getHeader(localize("text", "Text")), 3, 2);
		T.add(getHeader(localize("amount", "Amount")), 4, 2);
		T.setTopLine(true);
		//boolean debet = false;
		if (listEntries != null) {
			//int len = listEntries.size();
			double totPrice = 0;
			int row = 3;
			for (Iterator iter = listEntries.iterator(); iter.hasNext();) {
				AccountEntry entry = (AccountEntry) iter.next();
				double p = entry.getTotal();
				//debet = p > 0 ? true : false;
				totPrice += p;
				T.add(getText(getDateString(new IWTimestamp(entry.getLastUpdated()))), 1, row);
				T.add(getText(entry.getName()), 2, row);
				T.add(getText(entry.getInfo()), 3, row);
				T.add(getAmountText(p), 4, row);
				row++;
			}
			Text txTotPrice = getAmountText((totPrice));
			txTotPrice.setFontSize(fontSize);
			if (totPrice >= 0)
				txTotPrice.setFontColor(sDebetColor);
			else
				txTotPrice.setFontColor(sKreditColor);
			T.add(txTotPrice, 4, tableDepth);
			//T.setLineAfterColumn(3);
			//T.setLineAfterRow(tableDepth-1);
			T.setLinesBetween(true);
		}
		return T;
	}
	private IWTimestamp getFromDate(IWContext iwc) {
		if (iwc.getParameter(prmFromDate) != null) {
			String sFromDate = iwc.getParameter(prmFromDate);
			return parseStamp(sFromDate);
		} else {
			IWTimestamp today = IWTimestamp.RightNow();
			return new IWTimestamp(1, today.getMonth(), today.getYear());
		}
	}
	private IWTimestamp getToDate(IWContext iwc) {
		if (iwc.getParameter(prmToDate) != null) {
			String sToDate = iwc.getParameter(prmToDate);
			return parseStamp(sToDate);
		} else {
			return IWTimestamp.RightNow();
		}
	}
	private void checkIds(IWContext iwc) throws java.rmi.RemoteException {
		try {
			if (iwc.isParameterSet(prmAccountId)) {
				accountID = Integer.valueOf(iwc.getParameter(prmAccountId));
				FinanceAccount acc = getFinanceService().getAccountHome().findByPrimaryKey(accountID);
				if (acc != null)
					userID = new Integer(acc.getUserId());
			} else if (iwc.isParameterSet(prmUserId)) {
				userID = Integer.valueOf(iwc.getParameter(prmUserId));
				eUser = getFinanceService().getAccountUserHome().findByPrimaryKey(userID);
			} else if (iwc.isLoggedOn()) {
				eUser = iwc.getUser();
				userID = (Integer) eUser.getPrimaryKey();
			}
			//accounts =
			// FinanceFinder.getInstance().listOfFinanceAccountsByUserId(userID);
			accounts = getFinanceService().getAccountHome().findAllByUserId(userID.intValue());
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}
	private IWTimestamp parseStamp(String sDate) {
		IWTimestamp it = new IWTimestamp();
		try {
			StringTokenizer st = new StringTokenizer(sDate, " .-/+");
			int day = 1, month = 1, year = 2001;
			if (st.hasMoreTokens()) {
				day = Integer.parseInt(st.nextToken());
				month = Integer.parseInt(st.nextToken());
				year = Integer.parseInt(st.nextToken());
			}
			it = new IWTimestamp(day, month, year);
		} catch (Exception pe) {
			it = new IWTimestamp();
		}
		return it;
	}
	public void main(IWContext iwc) throws java.rmi.RemoteException {
		isLoggedOn = iwc.isLoggedOn();
		eUser = iwc.getUser();
		df = DateFormat.getDateInstance(DateFormat.SHORT);
		tf = DateFormat.getTimeInstance();
		control(iwc);
	}
	public String getCorrectedTimeString(long seconds) {
		return tf.format(new java.sql.Time((seconds + (60 * 60)) * 1000));
	}
}// class AccountViewer
