package is.idega.idegaweb.member.presentation;

import is.idega.idegaweb.member.business.MemberFamilyLogic;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.Window;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;

public class FamilyConnector extends Window {
	private static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member";

	private static final String TAB_NAME = "usr_fam_win_name";
	private static final String DEFAULT_NAME = "Family";

	public static final String _PARAM_USER_ID = "user_id";
	public static final String _PARAM_RELATED_USER_ID = "related_user_id";
	public static final String _PARAM_ACTION = "action";
	public static final String _PARAM_TYPE = "type";
	public static final String _PARAM_METHOD = "method";

	public static final int _METHOD_ATTACH = 1;
	public static final int _METHOD_DETACH = 2;

	public static final int _ACTION_ATTACH = 1;
	public static final int _ACTION_DETACH = 2;
	public static final int _ACTION_SAVE = 3;

	private int method = -1;
	private User user = null;

	public FamilyConnector() {
		super();
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);

		setTitle(iwrb.getLocalizedString(TAB_NAME, DEFAULT_NAME));
		//		super("Family connections");
		setAllMargins(0);
		setWidth(250);
		setHeight(200);
		setBackgroundColor("#d4d0c8");
	}

	public void main(IWContext iwc) throws Exception {
		method = parseMethod(iwc);
		user = getUser(iwc);

		if (user != null) {
			switch (parseAction(iwc)) {
				case _ACTION_ATTACH :
					getAttachForm(iwc);
					break;
				case _ACTION_DETACH :
					getConfirmation(iwc);
					break;
				case _ACTION_SAVE :
					save(iwc);
					break;
			}
		}
		else {
			close();
		}
	}

	private void getAttachForm(IWContext iwc) throws Exception {
		Form form = new Form();
		form.add(new HiddenInput(_PARAM_USER_ID, user.getPrimaryKey().toString()));
		form.add(new HiddenInput(_PARAM_METHOD, String.valueOf(_METHOD_ATTACH)));
		form.add(new HiddenInput(_PARAM_ACTION, String.valueOf(_ACTION_SAVE)));

		Table frameTable = new Table(2, 3);
		frameTable.setColumnAlignment(1, Table.HORIZONTAL_ALIGN_RIGHT);
		frameTable.mergeCells(1, 3, 2, 3);
		frameTable.setAlignment(1, 3, Table.HORIZONTAL_ALIGN_CENTER);
		frameTable.setWidth("100%");
		frameTable.setHeight("100%");

		IWResourceBundle iwrb = getResourceBundle(iwc);

		frameTable.add(new Text(iwrb.getLocalizedString("usr_fam_win_pin","Personal ID")), 1, 1);
		frameTable.add(new Text(iwrb.getLocalizedString("usr_fam_win_type","Type")), 1, 2);
		frameTable.add(new TextInput(_PARAM_RELATED_USER_ID), 2, 1);
		frameTable.add(getRelationMenu(iwc), 2, 2);
		frameTable.add(new CloseButton(iwrb.getLocalizedString("usr_fam_win_cancel","Cancel")), 1, 3);
		frameTable.add(new SubmitButton(iwrb.getLocalizedString("usr_fam_win_save","Save")), 1, 3);

		form.add(frameTable);
		add(form);
	}

	private void getConfirmation(IWContext iwc) throws Exception {
		Form form = new Form();
		form.add(new HiddenInput(_PARAM_USER_ID, user.getPrimaryKey().toString()));
		form.add(new HiddenInput(_PARAM_RELATED_USER_ID, iwc.getParameter(_PARAM_RELATED_USER_ID)));
		form.add(new HiddenInput(_PARAM_TYPE, iwc.getParameter(_PARAM_TYPE)));
		form.add(new HiddenInput(_PARAM_METHOD, String.valueOf(_METHOD_DETACH)));
		form.add(new HiddenInput(_PARAM_ACTION, String.valueOf(_ACTION_SAVE)));

		Table frameTable = new Table(1, 2);
		frameTable.setAlignment(1, 1, Table.HORIZONTAL_ALIGN_CENTER);
		frameTable.setAlignment(1, 2, Table.HORIZONTAL_ALIGN_CENTER);
		frameTable.setWidth("100%");
		frameTable.setHeight("100%");

		IWResourceBundle iwrb = getResourceBundle(iwc);
		frameTable.add(new Text(iwrb.getLocalizedString("usr_fam_win_sure","Are you sure ?")), 1, 1);
		frameTable.add(new CloseButton(iwrb.getLocalizedString("usr_fam_win_cancel","Cancel")), 1, 2);
		frameTable.add(new SubmitButton(iwrb.getLocalizedString("usr_fam_win_yes","Yes")), 1, 2);

		form.add(frameTable);
		add(form);
	}

	private void save(IWContext iwc) throws RemoteException {
		MemberFamilyLogic logic = getMemberFamilyLogic(iwc);
		String relationType = iwc.getParameter(_PARAM_TYPE);
		String relatedPerson = iwc.getParameter(_PARAM_RELATED_USER_ID);
		if (relatedPerson != null) {
			switch (method) {
				case _METHOD_ATTACH :
					try {
						User relatedUser = getUserBusiness(iwc).getUserHome().findByPersonalID(relatedPerson);

						if (relationType.equals(logic.getChildRelationType())) {
							logic.setAsChildFor(relatedUser, user);
						}
						else if (relationType.equals(logic.getParentRelationType())) {
							logic.setAsParentFor(relatedUser, user);
						}
						else if (relationType.equals(logic.getSpouseRelationType())) {
							logic.setAsSpouseFor(relatedUser, user);
						}
						else if (relationType.equals(logic.getSiblingRelationType())) {
							logic.setAsSiblingFor(relatedUser, user);
						}
					}
					catch (FinderException fe) {
						fe.printStackTrace(System.err);
					}
					catch (CreateException ce) {
						ce.printStackTrace(System.err);
					}

					break;

				case _METHOD_DETACH :
					try {
						User relatedUser = getUserBusiness(iwc).getUser(Integer.parseInt(relatedPerson));

						if (relationType.equals(logic.getChildRelationType())) {
							logic.removeAsChildFor(relatedUser, user);
						}
						else if (relationType.equals(logic.getParentRelationType())) {
							logic.removeAsParentFor(relatedUser, user);
						}
						else if (relationType.equals(logic.getSpouseRelationType())) {
							logic.removeAsSpouseFor(relatedUser, user);
						}
						else if (relationType.equals(logic.getSiblingRelationType())) {
							logic.removeAsSiblingFor(relatedUser, user);
						}
					}
					catch (NumberFormatException nfe) {
					}
					catch (RemoteException re) {
					}
					catch (RemoveException re) {
					}

					break;
			}
		}

		setParentToReload();
		close();
	}

	private DropdownMenu getRelationMenu(IWContext iwc) throws RemoteException {
		DropdownMenu menu = new DropdownMenu(_PARAM_TYPE);
		IWResourceBundle iwrb = getResourceBundle(iwc);
		menu.addMenuElement(getMemberFamilyLogic(iwc).getChildRelationType(), iwrb.getLocalizedString("usr_fam_win_child","Child"));
		menu.addMenuElement(getMemberFamilyLogic(iwc).getParentRelationType(), iwrb.getLocalizedString("usr_fam_win_parent","Parent"));
		menu.addMenuElement(getMemberFamilyLogic(iwc).getSiblingRelationType(), iwrb.getLocalizedString("usr_fam_win_sibling","Sibling"));
		menu.addMenuElement(getMemberFamilyLogic(iwc).getSpouseRelationType(), iwrb.getLocalizedString("usr_fam_win_spouse","Spouse"));
		return menu;
	}

	private User getUser(IWContext iwc) throws RemoteException {
		return getUserBusiness(iwc).getUser(Integer.parseInt(iwc.getParameter(_PARAM_USER_ID)));
	}

	private int parseAction(IWContext iwc) {
		try {
			return Integer.parseInt(iwc.getParameter(_PARAM_ACTION));
		}
		catch (NumberFormatException nfe) {
			return _METHOD_ATTACH;
		}
	}

	private int parseMethod(IWContext iwc) {
		try {
			return Integer.parseInt(iwc.getParameter(_PARAM_METHOD));
		}
		catch (NumberFormatException nfe) {
			return _ACTION_ATTACH;
		}
	}

	public MemberFamilyLogic getMemberFamilyLogic(IWApplicationContext iwc) {
		MemberFamilyLogic familyLogic = null;
		if (familyLogic == null) {
			try {
				familyLogic = (MemberFamilyLogic) com.idega.business.IBOLookup.getServiceInstance(iwc, MemberFamilyLogic.class);
			}
			catch (java.rmi.RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return familyLogic;
	}

	public UserBusiness getUserBusiness(IWApplicationContext iwc) {
		UserBusiness business = null;
		if (business == null) {
			try {
				business = (UserBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, UserBusiness.class);
			}
			catch (java.rmi.RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return business;
	}

	/**
	* @see com.idega.presentation.PresentationObject#getBundleIdentifier()
	*/
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
}