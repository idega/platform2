package com.idega.block.finance.presentation;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.finance.business.FinanceService;
import com.idega.block.finance.data.Account;
import com.idega.block.finance.data.AssessmentStatus;
import com.idega.core.user.data.User;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Parameter;
import com.idega.presentation.ui.ResetButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

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

	private DateFormat df;
	private static String prmNewAccount = "taccs_newaccount";
	private Collection accountUsers = null;
	private Collection accounts = null;

	private int viewerPageId = -1;
	private int tarifferPageId = -1;
	private String type = null;

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
		if (isAdmin) {
			 df = getDateTimeFormat(iwc.getCurrentLocale());
			if (iwc.isParameterSet(prmNewAccount)) {
				int iUserId = Integer.parseInt(iwc.getParameter(prmNewAccount));
				setMainPanel(getNewAccountForm(iwc,iUserId, iCategoryId));
			}
			if(iwc.isParameterSet("fin_create_account")){
				createAccount(iwc);
				setSearchPanel(getSearchForm(iwc, iCategoryId));
			}
			else if (iwc.isParameterSet("sf_search")) {
				performSearch(iwc, iCategoryId);
				setSearchPanel(getSearchForm(iwc, iCategoryId));
				//T.add(new HorizontalRule(),1,2);
				Map MapOfUsers = getMapOfUsers(accountUsers);
				setMainPanel(getAccountListTable(MapOfUsers, iCategoryId));
				if (MapOfUsers != null && !MapOfUsers.isEmpty())
					setMainPanel(getUsersWithoutAccounts(MapOfUsers, iCategoryId));
			}
			else {
				setSearchPanel(getSearchForm(iwc, iCategoryId));
				//T.add(new HorizontalRule(),1,2);
			}
			

		}
		else
			add(localize("access_denied", "Access denied"));
	}
	
	/**
	 * @param iwc
	 */
	private void createAccount(IWContext iwc){
		String accountName = iwc.getParameter("account_name");
		int userID = Integer.parseInt(iwc.getParameter("fin_usr_id"));
		type = iwc.getParameter("fin_type");
		try {
			getFinanceService().getAccountBusiness().createNewAccount(userID,accountName,"",1,type,getFinanceCategoryId().intValue());
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
		}
	}

	private PresentationObject getSearchForm(IWContext iwc, int iCategoryId) {
		
		DataTable T = getDataTable();
		T.setUseBottom(false);
		T.setWidth(Table.HUNDRED_PERCENT);
		T.addTitle(localize("account_search", "Account Search"));
		// T.addBottom(false);
		T.add(getHeader(localize("account_id", "Account id")), 1, 1);
		T.add(getHeader(localize("name", "Name")), 1, 2);
		T.add(getHeader(localize("personal_id", "Personal ID")), 1, 3);
		//T.add(getHeader(localize("first_name", "First name")), 1, 2);
		//T.add(getHeader(localize("middle_name", "Middle name")), 1, 3);
		//T.add(getHeader(localize("last_name", "Last name")), 1, 4);

		String id = iwc.getParameter("sf_id");
		String name = iwc.getParameter("sf_name");
		String pid = iwc.getParameter("sf_pid");
		//String first = iwc.getParameter("sf_firstname");
		//String middle = iwc.getParameter("sf_middlename");
		//String last = iwc.getParameter("sf_lastname");

		TextInput accountid = getTextInput("sf_id");
		TextInput nameinput = getTextInput("sf_name");
		TextInput pidinput = getTextInput("sf_pid");
		//TextInput firstname = getTextInput("sf_firstname");
		//TextInput middlename = getTextInput("sf_middlename");
		//TextInput lastname = getTextInput("sf_lastname");
		String drpsel = iwc.isParameterSet("sf_type") ? iwc.getParameter("sf_type") : "";
		DropdownMenu drpTypes = getAccountTypes("sf_type", drpsel, null);

		if (id != null)
			accountid.setContent(id);
		if(name!=null){
			nameinput.setContent(name);
		}
		if(pid!=null){
			pidinput.setContent(pid);
		}
		/*if (first != null)
			firstname.setContent(first);

		if (middle != null)
			middlename.setContent(middle);

		if (last != null)
			lastname.setContent(last);
		*/
		
		//int len = 20;
		accountid.setLength(6);
		nameinput.setLength(40);
		pidinput.setLength(14);
		
		accountid.keepStatusOnAction(true);
		nameinput.keepStatusOnAction(true);
		pidinput.keepStatusOnAction(true);
		//firstname.setLength(len);
		//middlename.setLength(len);
		//lastname.setLength(len);

		T.add(accountid, 2, 1);
		T.add(drpTypes, 2, 1);
		T.add(nameinput,2,2);
		T.add(pidinput,2,3);
		//T.add(firstname, 2, 2);
		//T.add(middlename, 2, 3);
		//T.add(lastname, 2, 4);

		T.add(Finance.getCategoryParameter(iCategoryId));
		SubmitButton search = new SubmitButton(localize("search", "Search"), "sf_search", "true");
		search = (SubmitButton) setStyle(search,STYLENAME_INTERFACE_BUTTON);
		T.addButton(search);
		
		ResetButton reset = new ResetButton(localize("clear","Clear"));
		reset = (ResetButton) setStyle(reset,STYLENAME_INTERFACE_BUTTON);
		T.addButton(reset);

		

		return T;
	}

	private PresentationObject getNewAccountForm(IWContext iwc, int iUserId, int iCategoryId) {
		
		DataTable T = getDataTable();
		T.setUseBottom(false);
		T.addTitle(localize("create_new_account","Create new account"));
		TextInput accountName = new TextInput("account_name");
		type = iwc.getParameter("fin_type");
		
		SubmitButton create = new SubmitButton(localize("create", "Create"), "fin_create_account", "true");
		create = (SubmitButton) setStyle(create,STYLENAME_INTERFACE_BUTTON);
		T.addButton(create);
		T.add(getHeader(localize("user_name","User name")),1,1);
		T.add(getText(localize("persona_id","Personal ID")),1,2);
		T.add(getHeader(localize("account_id","Account ID")),1,3);
		try {
			User user = (User)getFinanceService().getAccountUserHome().findByPrimaryKey(new Integer(iUserId));
			T.add(getText(user.getName()),2,1);
			T.add(getText(user.getPersonalID()),2,2);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		T.add(accountName,2,3);
		T.add(new Parameter("fin_usr_id",String.valueOf(iUserId)));
		T.add(new Parameter("fin_type",type));
		T.add(Finance.getCategoryParameter(iCategoryId));
	

		return T;
	}

	private void performSearch(IWContext iwc, int iCategoryId) {
		String id = null;//, first = null, middle = null, last = null;
		String pid = null, name = null;
		// See if we have an account id
		if (iwc.isParameterSet("sf_type"))
			type = iwc.getParameter("sf_type");
		boolean hasSomething = false;
		if (iwc.isParameterSet("sf_id"))
			id = iwc.getParameter("sf_id");
		if (id != null && !"".equals(id) && id.length() > 0) {
			try {
				//accounts = FinanceFinder.getInstance().searchAccounts(id, first, middle, last, type, iCategoryId);
				
				accounts = getFinanceService().getAccountHome().findBySearch(id,null,null,type,getFinanceCategoryId().intValue());
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (FinderException e) {
				e.printStackTrace();
			}
		}
		// Else we try to lookup by name
		else {
			if (iwc.isParameterSet("sf_name")) {
				name = iwc.getParameter("sf_name");
				hasSomething = true;
			}
			if (iwc.isParameterSet("sf_pid")) {
				pid = iwc.getParameter("sf_pid");
				hasSomething = true;
			}/*
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
			}*/
			if (hasSomething) {
				try {
					//Name nm = new Name(name);
					//accounts = getFinanceService().getAccountHome().findBySearch(id,first,middle,last,type,getFinanceCategoryId().intValue());
					//accountUsers = getFinanceService().getAccountUserHome().findBySearch(first,middle,last);
					accounts = getFinanceService().getAccountHome().findBySearch(id,name,pid,type,getFinanceCategoryId().intValue());
					accountUsers = getFinanceService().getAccountUserHome().findBySearch(name,pid);
				} catch (RemoteException e) {
					e.printStackTrace();
				} catch (FinderException e) {
					e.printStackTrace();
				}
//				accounts = FinanceFinder.getInstance().searchAccounts(id, first, middle, last, type, iCategoryId);
				//accountUsers = FinanceFinder.getInstance().searchAccountUsers(first, middle, last);
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
		T.setWidth(Table.HUNDRED_PERCENT);
		T.setUseBottom(false);
		T.setTitlesHorizontal(true);
		int row = 1;
		int col = 1;
		Map M = mapOfUsers;
		User U = null;
		if (accounts != null) {

			T.addTitle(localize("users_with_accounts", "Users with accounts"));

			col = 1;
			T.add(getHeader(localize("account_assessment", "Assessment")), 1, row);
			T.add(getHeader(localize("account_id", "Account id")), 2, row);
			T.add(getHeader(localize("user_name", "User name")), 3, row);
			T.add(getHeader(localize("personal_id", "Personal ID")), 4, row);
			T.add(getHeader(localize("published_balance", "Published")), 5, row);
			T.add(getHeader(localize("total_balance", "Total")), 6, row);
			T.add(getHeader(localize("last_updated", "Last updated")), 7, row);
			row++;
			Iterator I = accounts.iterator();
			Account A;
			Integer accountID;
			FinanceService financeService = getFinanceService();
			Integer uid;
			Link accountLink, tariffLink;
			while (I.hasNext()) {
				col = 1;
				A = (Account)I.next();
				accountID = A.getAccountId();
				uid = new Integer(A.getUserId());
				if (M != null && M.containsKey(uid)) {
					U = (User)M.get(uid);
					M.remove(uid);
				} else{
					try {
						U = (User)getFinanceService().getAccountUserHome().findByPrimaryKey(uid);
					} catch (RemoteException e) {
						e.printStackTrace();
					} catch (FinderException e) {
						e.printStackTrace();
					}
			}
				if (tarifferPageId > 0) {
					tariffLink = getLink((A.getAccountId().toString()));
					tariffLink.addParameter(prmAccountId, A.getAccountId().toString());
					tariffLink.addParameter(getCategoryParameter(iCategoryId));
					tariffLink.setPage(tarifferPageId);
					T.add(tariffLink, col, row);
				}
				col++;
				if (viewerPageId > 0) {
					accountLink = new Link(getText(A.getName()));
					accountLink.addParameter(AccountViewer.prmAccountId, A.getAccountId().toString());
					accountLink.setPage(viewerPageId);
					T.add(accountLink, col, row);
				}
				col++;

				T.add(getText(U.getName()), col++, row);
				T.add(getText(U.getPersonalID()), col++, row);
				T.add(getAmountText((financeService.getAccountBalance(accountID,AssessmentStatus.PUBLISHED))), col++, row);
				T.add(getAmountText((financeService.getAccountBalance(accountID))), col++, row);
				Date date = financeService.getAccountLastUpdate(accountID);
				if(date!=null)
				T.add(getText(df.format(date)), col++, row);
				row++;
			}
			T.getContentTable().setColumnAlignment(5,Table.HORIZONTAL_ALIGN_RIGHT);
			T.getContentTable().setColumnAlignment(6,Table.HORIZONTAL_ALIGN_RIGHT);
			
		}
		return T;
	}

	public PresentationObject getUsersWithoutAccounts(Map mapOfUsers, int iCategoryId) {
		DataTable T = new DataTable();
		T.setWidth(Table.HUNDRED_PERCENT);
		T.setTitlesHorizontal(true);
		T.setUseBottom(false);
		int row = 1;
		T.addTitle(localize("users_without_accounts", "Users without accounts"));
		T.add(getHeader(localize("user_name", "User name")), 1, row);
		T.add(getHeader(localize("personal_id", "Personal ID")), 2, row);
		T.add(getHeader(localize("create_account", "Create account")), 3, row);
		row++;
		Iterator I2 = mapOfUsers.values().iterator();
		Image newImage = core.getImage("/shared/create.gif");
		User U;
		while (I2.hasNext()) {
			U = (User)I2.next();
			T.add(getText(U.getName()), 1, row);
			T.add(getText(U.getPersonalID()), 2, row);
			T.add(getNewAccountLink(newImage, U.getID(), iCategoryId,type), 3, row);
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

	private Link getNewAccountLink(PresentationObject obj, int iUserId, int iCategoryId,String type) {
		Link L = new Link(obj);
		L.addParameter(Finance.getCategoryParameter(iCategoryId));
		L.addParameter(prmNewAccount, iUserId);
		L.addParameter("fin_type",type);
		return L;
	}

	public void main(IWContext iwc) throws java.rmi.RemoteException {
		control(iwc);
	}
}
