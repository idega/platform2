/*
 * $Id$
 *
 * Copyright (C) 2000-2003 Idega Software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega Software.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.member.presentation;

import is.idega.idegaweb.member.business.plugins.ClubInformationPluginBusiness;
import is.idega.idegaweb.member.util.IWMemberConstants;
import java.rmi.RemoteException;
import javax.ejb.FinderException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.StyledIWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.presentation.GroupPropertyWindow;

/**
 * @author palli
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class UpdateClubDivisionTemplate extends StyledIWAdminWindow {
	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member";
	public static final String PARAMETER_GROUP_ID = GroupPropertyWindow.PARAMETERSTRING_GROUP_ID;

	protected static final String WINDOW_NAME = "updatecdiv_name";
	protected static final String SUBMIT_LABEL = "updatecdiv_submit";
	protected static final String CANCEL_LABEL = "updatecdiv_cancel";
	
	protected static final String WRONG_GROUP_TYPE = "updatecdiv_wrongtype";
	
	protected static final String UPDATE_INFO = "updatecdiv_info";

	protected static final String ACTION = "updatecdiv_action";
	protected static final String ACTION_CANCEL = "updatecdiv_cancel";
	protected static final String ACTION_UPDATE = "updatecdiv_update";
	

	private Group group;
	private IWResourceBundle iwrb;
	public UpdateClubDivisionTemplate() {
		setHeight(150);
		setWidth(300);
	}

	private void addForm(IWContext iwc) {
		Form form = new Form();
		form.maintainParameter(PARAMETER_GROUP_ID);

		Table t = new Table(1,3);
		t.add(getResourceBundle(iwc).getLocalizedString(UPDATE_INFO,"Press Update to update all clubs linked to this league."),1,1);
		t.add(new SubmitButton(iwrb.getLocalizedImageButton("update", "Update"), ACTION, ACTION_UPDATE),1,3);
		t.add(Text.getNonBrakingSpace(),1,1);
		t.add(new SubmitButton(iwrb.getLocalizedImageButton("cancel", "Cancel"), ACTION, ACTION_CANCEL),1,3);
		
		form.add(t);
		
		add(form);
	}

	private void init(IWContext iwc) {
		String sGroupId = iwc.getParameter(PARAMETER_GROUP_ID);
		if (sGroupId != null) {
			try {
				GroupHome gHome = (GroupHome) IDOLookup.getHome(Group.class);
				group = gHome.findByPrimaryKey(new Integer(sGroupId));
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
		iwrb = getResourceBundle(iwc);
	}

	private boolean updateChildren(IWContext iwc) {
		try {
			return getClubInformationPluginBusiness(iwc).updateConnectedToSpecial(group);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		
		iwrb.getLocalizedString(WINDOW_NAME,"Update League Template Window");
		addTitle(iwrb.getLocalizedString(WINDOW_NAME, "Update League Template Window"), TITLE_STYLECLASS);

		init(iwc);
		if (group != null) {
			if (group.getGroupType().equals(IWMemberConstants.GROUP_TYPE_CLUB_DIVISION_TEMPLATE) ||
			     group.getGroupType().equals(IWMemberConstants.GROUP_TYPE_LEAGUE)) {

				String action = iwc.getParameter(ACTION);
	
				if (action == null) {
					addForm(iwc);
				}
				else if (action.equals(ACTION_CANCEL)) {
					close();
				}
				else if (action.equals(ACTION_UPDATE)) {
					updateChildren(iwc);
					setOnLoad("window.opener.parent.frames['iwb_main'].location.reload()");
					close();
				}
			}
			else {
				add(getResourceBundle(iwc).getLocalizedString(WRONG_GROUP_TYPE,"Please select either a league, or a division template under a league."));
			}			
		}
		else {
			add(getResourceBundle(iwc).getLocalizedString(WRONG_GROUP_TYPE,"Please select either a league, or a division template under a league."));
		}
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	
	public ClubInformationPluginBusiness getClubInformationPluginBusiness(IWApplicationContext iwc) {
		ClubInformationPluginBusiness business = null;
		try {
			business = (ClubInformationPluginBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, ClubInformationPluginBusiness.class);
		}
		catch (java.rmi.RemoteException rme) {
			throw new RuntimeException(rme.getMessage());
		}
		return business;
	}
	
}