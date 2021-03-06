/*
 * $Id$
 *
 * Copyright (C) 2000-2003 Idega Software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega Software.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.member.presentation;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.business.UserStatusBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.data.Status;
import com.idega.user.data.StatusHome;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;
import com.idega.user.presentation.GroupPropertyWindow;
import com.idega.user.presentation.UserStatusDropdown;

/**
 * @author palli
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class NewMemberWindow extends IWAdminWindow {
	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member";
	public static final String PARAMETER_GROUP_ID = GroupPropertyWindow.PARAMETERSTRING_GROUP_ID;

	private String ACTION = "nmw_act";
	private String ACTION_NEXT = "nmw_act_nx";
	private String ACTION_SAVE = "nmw_act_sv";
	private String ACTION_CANCEL = "nmw_act_cc";
	private String PARAMETER_PID = "nmw_pid";
	private String PARAMETER_NAME = "nmw_name";
	private String PARAMETER_STATUS = "nmw_sta";
	private String PARAMETER_SAVE = "nmw_sv";

	private int numberOfRows = 1;
	private Group group;
	private IWResourceBundle iwrb;
	private UserHome uHome;
	private StatusHome sHome;
	private List failedInserts;

	public NewMemberWindow() {
		setHeight(200);
		setWidth(400);
	}

	private void addForm(IWContext iwc, boolean verifyForm) {
		Form form = new Form();
		form.maintainParameter(PARAMETER_GROUP_ID);
		Table table = new Table();
		table.setCellpadding(2);
		table.setCellspacing(0);
		table.setBorder(0);

		boolean foundUser = false;

		int row = 1;
		if (verifyForm) {
			table.add(formatText(this.iwrb.getLocalizedString("save", "Save")), 1, row);
			table.add(formatText(this.iwrb.getLocalizedString("user.user_name", "User name")), 3, row);
		}
		else {
			table.add(formatText(this.iwrb.getLocalizedString("personal.id.number", "Personal ID number")), 1, row);
			table.add(formatText(this.iwrb.getLocalizedString("user.user_name", "User name")), 3, row);
		}
		table.setWidth(2, "10");
		table.setWidth(4, "10");
		table.add(formatText(this.iwrb.getLocalizedString("user.status", "Status")), 5, row);

		TextInput pid = new TextInput();
		TextInput name = new TextInput();
		UserStatusDropdown status = new UserStatusDropdown("noname");
		CheckBox check;
		String sPid;
		String sStat;
		User user;
		Status stat;
		for (int i = 1; i <= this.numberOfRows; i++) {
			/** Listing valid PersonalIDs */
			if (verifyForm) {
				sPid = iwc.getParameter(this.PARAMETER_PID + "_" + i);
				sStat = iwc.getParameter(this.PARAMETER_STATUS + "_" + i);
				if (sPid != null && !sPid.equals("")) {
					try {
						++row;
						user = this.uHome.findByPersonalID(sPid);
						if (UserStatusDropdown.NO_STATUS_KEY.equals(sStat)) {
							stat = null;
						}
						else {
							stat = this.sHome.findByPrimaryKey(new Integer(sStat));
						}
						check = new CheckBox(this.PARAMETER_SAVE + "_" + i);
						check.setStyleAttribute(STYLE_2);
						check.setChecked(true);

						table.add(check, 1, row);
						table.add(formatText(user.getName()), 3, row);
						if (stat != null) {
							table.add(formatText(this.iwrb.getLocalizedString(stat.getStatusKey(), stat.getStatusKey())), 5, row);
						}
						form.maintainParameter(this.PARAMETER_PID + "_" + i);
						form.maintainParameter(this.PARAMETER_STATUS + "_" + i);
						foundUser = true;
					}
					catch (FinderException e) {
						//e.printStackTrace(System.err);
						table.add(formatText(this.iwrb.getLocalizedString("user.user_not_found", "User not found") + " (" + sPid + ")"), 3, row);
					}
				}
			}
			/** Creating and adding inputs to form */
			else {
				++row;
				status = new UserStatusDropdown(this.PARAMETER_STATUS + "_" + i);
				status.setStyleAttribute(STYLE_2);
				pid = new TextInput(this.PARAMETER_PID + "_" + i);
				pid.setAsIcelandicSSNumber(this.iwrb.getLocalizedString("user.pid_incorrect_in_row", "Personal ID not correct for user in row") + " " + i);
				pid.setStyleAttribute(STYLE_2);
				pid.setMaxlength(10);
				name = new TextInput(this.PARAMETER_NAME + "_" + i);
//				table.add(formatText(Integer.toString(i)), 1, row);
				table.add(pid, 1, row);
				table.add(name, 3, row);
				table.add(status, 5, row);
			}
		}

		++row;
		++row;
		table.setAlignment(5, row, Table.HORIZONTAL_ALIGN_RIGHT);
		if (verifyForm) {
			table.mergeCells(1, row, 2, row);
			table.add(new BackButton(this.iwrb.getLocalizedImageButton("back", "Back")), 1, row);
			if (foundUser) {
				table.add(new SubmitButton(this.iwrb.getLocalizedImageButton("save", "Save"), this.ACTION, this.ACTION_SAVE), 5, row);
			}
		}
		else {
			table.add(new SubmitButton(this.iwrb.getLocalizedImageButton("next", "Next"), this.ACTION, this.ACTION_NEXT), 5, row);
		}
		// add close button
		table.add(new SubmitButton(this.iwrb.getLocalizedImageButton("cancel", "Cancel"), this.ACTION, this.ACTION_CANCEL), 4, row);
		form.add(table);
		add(form);
	}

	private void errorList() {
		Form form = new Form();
		form.maintainParameter(PARAMETER_GROUP_ID);
		Table table = new Table();
		table.setCellpadding(2);
		table.setCellspacing(0);
		table.setBorder(0);

		int row = 1;

		table.add(formatText(this.iwrb.getLocalizedString("save_failed_for_users", "Save failed for the following user/s:")), 1, row);
		Iterator iter = this.failedInserts.iterator();
		User user;
		while (iter.hasNext()) {
			++row;
			user = (User) iter.next();
			table.add(user.getName() + " (" + user.getPersonalID() + ")", 1, row);
		}

		++row;
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.add(new SubmitButton(this.iwrb.getLocalizedImageButton("back", "Back")), 1, row);

		form.add(table);
		add(form);
	}

	private boolean handleInsert(IWContext iwc) throws RemoteException {
		String sPid;
		String sStat;
		User user;
		Status stat;
		UserStatusBusiness usb = (UserStatusBusiness) IBOLookup.getServiceInstance(iwc, UserStatusBusiness.class);
		this.failedInserts = new Vector();
		boolean errorFree = true;

		for (int i = 1; i <= this.numberOfRows; i++) {
			if (iwc.isParameterSet(this.PARAMETER_SAVE + "_" + i)) {
				try {
					sPid = iwc.getParameter(this.PARAMETER_PID + "_" + i);
					sStat = iwc.getParameter(this.PARAMETER_STATUS + "_" + i);
					user = this.uHome.findByPersonalID(sPid);
					if (UserStatusDropdown.NO_STATUS_KEY.equals(sStat)) {
						stat = null;
					}
					else {
						stat = this.sHome.findByPrimaryKey(new Integer(sStat));
					}
					this.group.addGroup(user);
					if (stat != null && (!usb.setUserGroupStatus(((Integer)user.getPrimaryKey()).intValue(), ((Integer) this.group.getPrimaryKey()).intValue(), ((Integer) stat.getPrimaryKey()).intValue(),iwc.getCurrentUserId()))) {
						this.failedInserts.add(user);
						errorFree = false;
					}
					// Added this.... Is this OK?
					if (user.getPrimaryGroup() == null) {
						user.setPrimaryGroup(this.group);
						user.store();
					}
				}
				catch (FinderException e) {
					e.printStackTrace(System.err);
				}
			}
		}

		return errorFree;
	}

	private void init(IWContext iwc) {
		String sGroupId = iwc.getParameter(PARAMETER_GROUP_ID);
		if (sGroupId != null) {
			try {
				this.uHome = (UserHome) IDOLookup.getHome(User.class);
				this.sHome = (StatusHome) IDOLookup.getHome(Status.class);
				GroupHome gHome = (GroupHome) IDOLookup.getHome(Group.class);
				this.group = gHome.findByPrimaryKey(new Integer(sGroupId));
			}
			catch (IDOLookupException e) {
				e.printStackTrace(System.err);
			}
			catch (NumberFormatException e) {
				e.printStackTrace(System.err);
			}
			catch (FinderException e) {
				e.printStackTrace(System.err);
			}
		}
		this.iwrb = getResourceBundle(iwc);
	}

	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		setTitle("New Member Window");

		init(iwc);
		if (this.group != null) {

			String action = iwc.getParameter(this.ACTION);

			if (action == null) {
				addForm(iwc, false);
			}
			else if (action.equals(this.ACTION_CANCEL)) {
				close();
			}
			else if (action.equals(this.ACTION_NEXT)) {
				addForm(iwc, true);
			}
			else if (action.equals(this.ACTION_SAVE)) {
				if (handleInsert(iwc)) {
					addForm(iwc, false);
				}
				else {
					errorList();
				}
				setOnLoad("window.opener.parent.frames['iwb_main'].location.reload()");
			}
		}
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
}