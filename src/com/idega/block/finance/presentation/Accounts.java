package com.idega.block.finance.presentation;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import com.idega.block.finance.business.FinanceFinder;
import com.idega.block.finance.data.Account;
import com.idega.core.user.data.User;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.text.TextFormat;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class Accounts extends Finance {

	protected final int ACT1 = 1, ACT2 = 2, ACT3 = 3, ACT4 = 4, ACT5 = 5;
	public String strAction = "tt_action";

	private int iCashierId = 1;
	private static String prmGroup = "taccs_grp";
	private static String prmNewAccount = "taccs_newaccount";
	private static String prmAccountUserId = "taccs_accuser_id";
	private Collection accountUsers = null;
	private Collection accounts = null;

	private int viewerPageId = -1;
	private int tarifferPageId = -1;
	private TextFormat Edit;

	public Accounts() {

	}

	public String getLocalizedNameKey() {
		return "accounts";
	}

	public String getLocalizedNameValue() {
		return "Accounts";
	}

	public void setAccountViewerPage(int pageId) {
		viewerPageId = pageId;
	}

	public void setAccountTarifferPage(int pageId) {
		tarifferPageId = pageId;
	}

	protected void control(IWContext iwc) throws java.rmi.RemoteException {
		Edit = TextFormat.getInstance();
		if (isAdmin) {
			Table T = new Table();
			T.setCellpadding(0);
			T.setCellspacing(0);
			T.setWidth("100%");
			/*
			if(iwc.isParameterSet(AccountViewer.prmAccountId)){
			  add(new AccountViewer());
			}
			else
			*/
			if (iwc.isParameterSet(prmNewAccount)) {
				int iUserId = Integer.parseInt(iwc.getParameter(prmNewAccount));
				T.add(getNewAccountForm(iUserId, iCategoryId), 1, 1);
			}
			else if (iwc.isParameterSet("sf_search")) {
				performSearch(iwc, iCategoryId);
				T.add(getSearchForm(iwc, iCategoryId), 1, 1);
				//T.add(new HorizontalRule(),1,2);
				Map MapOfUsers = getMapOfUsers(accountUsers);
				T.add(getAccountListTable(MapOfUsers, iCategoryId), 1, 3);
				if (MapOfUsers != null && !MapOfUsers.isEmpty())
					T.add(getUsersWithoutAccounts(MapOfUsers, iCategoryId), 1, 4);
			}
			else {
				T.add(getSearchForm(iwc, iCategoryId), 1, 1);
				//T.add(new HorizontalRule(),1,2);
			}
			add(T);

		}
		else
			add(iwrb.getLocalizedString("access_denied", "Access denies"));
	}

	private PresentationObject getSearchForm(IWContext iwc, int iCategoryId) {
		Form F = new Form();
		DataTable T = new DataTable();
		T.setWidth("100%");
		T.addTitle(iwrb.getLocalizedString("account_search", "Account Search"));
		// T.addBottom(false);
		T.add(Edit.format(iwrb.getLocalizedString("account_id", "Account id")), 1, 1);
		T.add(Edit.format(iwrb.getLocalizedString("first_name", "First name")), 1, 2);
		T.add(Edit.format(iwrb.getLocalizedString("middle_name", "Middle name")), 1, 3);
		T.add(Edit.format(iwrb.getLocalizedString("last_name", "Last name")), 1, 4);

		String id = iwc.getParameter("sf_id");
		String first = iwc.getParameter("sf_firstname");
		String middle = iwc.getParameter("sf_middlename");
		String last = iwc.getParameter("sf_lastname");

		TextInput accountid = new TextInput("sf_id");
		TextInput firstname = new TextInput("sf_firstname");
		TextInput middlename = new TextInput("sf_middlename");
		TextInput lastname = new TextInput("sf_lastname");
		String drpsel = iwc.isParameterSet("sf_type") ? iwc.getParameter("sf_type") : "";
		DropdownMenu drpTypes = getAccountTypes("sf_type", drpsel, null);

		if (id != null)
			accountid.setContent(id);

		if (first != null)
			firstname.setContent(first);

		if (middle != null)
			middlename.setContent(middle);

		if (last != null)
			lastname.setContent(last);

		int len = 20;
		accountid.setLength(6);
		firstname.setLength(len);
		middlename.setLength(len);
		lastname.setLength(len);

		T.add(accountid, 2, 1);
		T.add(drpTypes, 2, 1);
		T.add(firstname, 2, 2);
		T.add(middlename, 2, 3);
		T.add(lastname, 2, 4);

		T.add(Finance.getCategoryParameter(iCategoryId));
		SubmitButton search = new SubmitButton(iwrb.getLocalizedImageButton("search", "Search"), "sf_search", "true");
		T.addButton(search);

		F.add(T);

		return F;
	}

	private PresentationObject getNewAccountForm(int iUserId, int iCategoryId) {
		Form F = new Form();
		Table T = new Table(3, 4);
		T.add(Edit.format(iwrb.getLocalizedString("account_id", "Account id")), 1, 1);
		T.add(Edit.format(iwrb.getLocalizedString("first_name", "First name")), 1, 3);
		T.add(Edit.format(iwrb.getLocalizedString("middle_name", "Middle name")), 2, 3);
		T.add(Edit.format(iwrb.getLocalizedString("last_name", "Last name")), 3, 3);

		TextInput accountid = new TextInput("sf_id");
		TextInput firstname = new TextInput("sf_firstname");
		TextInput middlename = new TextInput("sf_middlename");
		TextInput lastname = new TextInput("sf_lastname");

		int len = 30;
		accountid.setLength(len);
		firstname.setLength(len);
		middlename.setLength(len);
		lastname.setLength(len);

		T.add(accountid, 1, 2);
		T.add(getAccountTypes("sf_type", "", null), 2, 2);
		T.add(firstname, 1, 4);
		T.add(middlename, 2, 4);
		T.add(lastname, 3, 4);

		T.add(Finance.getCategoryParameter(iCategoryId));
		SubmitButton search = new SubmitButton("sf_search", iwrb.getLocalizedString("search", "Search"));
		T.add(search, 3, 2);

		F.add(T);

		return F;
	}

	private void performSearch(IWContext iwc, int iCategoryId) {
		String id = null, first = null, middle = null, last = null, type = null;
		// See if we have an account id
		if (iwc.isParameterSet("sf_type"))
			type = iwc.getParameter("sf_type");
		boolean hasSomething = false;
		if (iwc.isParameterSet("sf_id"))
			id = iwc.getParameter("sf_id");
		if (id != null && !"".equals(id) && id.length() > 0) {
			accounts = FinanceFinder.getInstance().searchAccounts(id, first, middle, last, type, iCategoryId);
		}
		// Else we try to lookup by name
		else {
			if (iwc.isParameterSet("sf_firstname")) {
				first = iwc.getParameter("sf_firstname");
				hasSomething = true;
			}
			if (iwc.isParameterSet("sf_middlename")) {
				middle = iwc.getParameter("sf_middlename");
				hasSomething = true;
			}
			if (iwc.isParameterSet("sf_lastname")) {
				last = iwc.getParameter("sf_lastname");
				hasSomething = true;
			}
			if (hasSomething) {
				accounts = FinanceFinder.getInstance().searchAccounts(id, first, middle, last, type, iCategoryId);
				accountUsers = FinanceFinder.getInstance().searchAccountUsers(first, middle, last);
			}
		}

	}

	private Map getMapOfUsers(Collection listOfUsers) {
		if (listOfUsers != null) {
			Hashtable H = new Hashtable(listOfUsers.size());
			Iterator I = listOfUsers.iterator();
			while (I.hasNext()) {
				User u = (User)I.next();
				H.put(new Integer(u.getID()), u);
			}
			return H;
		}
		return null;
	}

	private PresentationObject getAccountListTable(Map mapOfUsers, int iCategoryId) throws java.rmi.RemoteException {
		DataTable T = new DataTable();
		T.setWidth("100%");
		T.setUseBottom(false);
		T.setTitlesHorizontal(true);
		int row = 1;
		int col = 1;
		Map M = mapOfUsers;
		User U;
		if (accounts != null) {

			T.addTitle(iwrb.getLocalizedString("users_with_accounts", "Users with accounts"));

			col = 1;
			T.add(Edit.format(iwrb.getLocalizedString("account_assessment", "Assessment")), col++, row);
			T.add(Edit.format(iwrb.getLocalizedString("account_id", "Account id")), col++, row);
			T.add(Edit.format(iwrb.getLocalizedString("user_name", "User name")), col++, row);
			T.add(Edit.format(iwrb.getLocalizedString("balance", "Balance")), col++, row);
			T.add(Edit.format(iwrb.getLocalizedString("last_updated", "Last updated")), col++, row);
			row++;
			Iterator I = accounts.iterator();
			Account A;

			Integer uid;
			Link accountLink, tariffLink;
			while (I.hasNext()) {
				col = 1;
				A = (Account)I.next();
				uid = new Integer(A.getUserId());
				if (M != null && M.containsKey(uid)) {
					U = (User)M.get(uid);
					M.remove(uid);
				}
				else
					U = FinanceFinder.getInstance().getUser(uid.intValue());

				if (tarifferPageId > 0) {
					tariffLink = new Link(Edit.format(A.getAccountId()));
					tariffLink.addParameter(prmAccountId, A.getAccountId());
					tariffLink.addParameter(getCategoryParameter(iCategoryId));
					tariffLink.setPage(tarifferPageId);
					T.add(tariffLink, col, row);
				}
				col++;
				if (viewerPageId > 0) {
					accountLink = new Link(Edit.format(A.getName()));
					accountLink.addParameter(AccountViewer.prmAccountId, A.getPrimaryKey().toString());
					accountLink.setPage(viewerPageId);
					T.add(accountLink, col, row);
				}
				col++;

				T.add(Edit.format(U.getName()), col++, row);
				T.add(Edit.format(Float.toString(A.getBalance())), col++, row);
				T.add(Edit.format(A.getLastUpdated().toString()), col++, row);
				row++;
			}
		}
		return T;
	}

	public PresentationObject getUsersWithoutAccounts(Map mapOfUsers, int iCategoryId) {
		DataTable T = new DataTable();
		T.setWidth("100%");
		T.setTitlesHorizontal(true);
		T.setUseBottom(false);
		T.addTitle(iwrb.getLocalizedString("users_without_accounts", "Users without accounts"));
		int row = 1;
		Iterator I2 = mapOfUsers.values().iterator();
		Image newImage = core.getImage("/shared/create.gif");
		User U;
		while (I2.hasNext()) {
			U = (User)I2.next();
			T.add(Edit.format(U.getName()), 1, row);
			T.add(getNewAccountLink(newImage, U.getID(), iCategoryId), 2, row);
			row++;
		}
		return T;

	}

	private DropdownMenu getAccountTypes(String name, String selected, String display) {
		DropdownMenu drp = new DropdownMenu(name);
		if (display != null)
			drp.addMenuElementFirst("", display);
		drp.addMenuElement(com.idega.block.finance.data.AccountBMPBean.typeFinancial);
		drp.addMenuElement(com.idega.block.finance.data.AccountBMPBean.typePhone);
		drp.setSelectedElement(selected);

		return drp;
	}

	private Link getNewAccountLink(PresentationObject obj, int iUserId, int iCategoryId) {
		Link L = new Link(obj);
		L.addParameter(Finance.getCategoryParameter(iCategoryId));
		L.addParameter(prmNewAccount, iUserId);
		return L;
	}

	private PresentationObject doMainTable(IWContext iwc) {
		Form F = new Form();

		return F;
	}

	public void main(IWContext iwc) throws java.rmi.RemoteException {
		control(iwc);
	}
}
