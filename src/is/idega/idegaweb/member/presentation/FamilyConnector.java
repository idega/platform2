package is.idega.idegaweb.member.presentation;

import is.idega.block.family.business.FamilyLogic;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.help.presentation.Help;
import com.idega.idegaweb.presentation.StyledIWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.TabbedPropertyPanel;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.StyledButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;

public class FamilyConnector extends StyledIWAdminWindow {
	private static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member";

	private static final String TAB_NAME = "usr_fam_win_name";
	private static final String DEFAULT_NAME = "Family";

	public static final String _PARAM_USER_ID = "user_id";
	public static final String _PARAM_RELATED_USER_ID = "related_user_id";
	public static final String _PARAM_ACTION = "action";
	public static final String _PARAM_TYPE = "type";
	public static final String _PARAM_METHOD = "method";
	
	private static final String FAMILY_RELATION_CUSTODIAN_AND_PARENT = "fam_rel_cust_par";
	
	private static final String HELP_TEXT_KEY_ATTATCH = "family_connector_attatch";
	private static final String HELP_TEXT_KEY_DETATCH = "family_connector_detatch";

	public static final int _METHOD_ATTACH = 1;
	public static final int _METHOD_DETACH = 2;

	public static final int _ACTION_ATTACH = 1;
	public static final int _ACTION_DETACH = 2;
	public static final int _ACTION_SAVE = 3;

	private int method = -1;
	private User user = null;
	
	private String mainStyleClass = "main";

	public FamilyConnector() {
		super();
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);

		setTitle(iwrb.getLocalizedString(TAB_NAME, DEFAULT_NAME));
		addTitle(iwrb.getLocalizedString(TAB_NAME, DEFAULT_NAME), TITLE_STYLECLASS);
		//		super("Family connections");
		setAllMargins(0);
		setWidth(240);
		setHeight(250);
		setScrollbar(false);
		setResizable(true);
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
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setHeight(2, 5);

		Table frameTable = new Table();
		frameTable.setWidth(Table.HUNDRED_PERCENT);
		frameTable.setStyleClass(mainStyleClass);
		frameTable.setCellpadding(5);
		frameTable.setCellspacing(0);

		IWResourceBundle iwrb = getResourceBundle(iwc);

		frameTable.add(new Text(iwrb.getLocalizedString("usr_fam_win_pin","Personal ID")), 1, 1);
		frameTable.add(Text.getBreak(), 1, 1);
		frameTable.add(new TextInput(_PARAM_RELATED_USER_ID), 1, 1);
		
		frameTable.add(new Text(iwrb.getLocalizedString("usr_fam_win_type","The person to connect is")), 1, 2);
		frameTable.add(Text.getBreak(), 1, 2);
		frameTable.add(getRelationMenu(iwc), 1, 2);
		
		Table bottomTable = new Table();
		bottomTable.setStyleClass(mainStyleClass);
		bottomTable.setAlignment(1,1,Table.HORIZONTAL_ALIGN_LEFT);
		bottomTable.setAlignment(2,1,Table.HORIZONTAL_ALIGN_RIGHT);
		bottomTable.setWidth("100%");
		Help help = getHelp(HELP_TEXT_KEY_ATTATCH);
		bottomTable.add(help,1,1);
		
		Table buttonTable = new Table();
		buttonTable.setCellpadding(0);
		buttonTable.setCellspacing(0);
		buttonTable.setWidth(2, "5");
		StyledButton submit = new StyledButton(new SubmitButton(iwrb.getLocalizedString("usr_fam_win_save","Save")));
		buttonTable.add(submit, 1, 1);
		StyledButton close = new StyledButton(new CloseButton(iwrb.getLocalizedString("usr_fam_win_cancel","Cancel")));
		buttonTable.add(close, 3, 1);
		bottomTable.add(buttonTable, 2, 1);
		
		table.add(frameTable,1,1);
		table.add(bottomTable,1,3);
		form.add(table);
		add(form,iwc);
	}

	private void getConfirmation(IWContext iwc) throws Exception {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		Form form = new Form();
		form.add(new HiddenInput(_PARAM_USER_ID, user.getPrimaryKey().toString()));
		form.add(new HiddenInput(_PARAM_RELATED_USER_ID, iwc.getParameter(_PARAM_RELATED_USER_ID)));
		form.add(new HiddenInput(_PARAM_TYPE, iwc.getParameter(_PARAM_TYPE)));
		form.add(new HiddenInput(_PARAM_METHOD, String.valueOf(_METHOD_DETACH)));
		form.add(new HiddenInput(_PARAM_ACTION, String.valueOf(_ACTION_SAVE)));

		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		
		Table frameTable = new Table();
		frameTable.setStyleClass(mainStyleClass);
		frameTable.setAlignment(1, 1, Table.HORIZONTAL_ALIGN_LEFT);
		frameTable.setAlignment(1, 2, Table.HORIZONTAL_ALIGN_LEFT);
		frameTable.setAlignment(2,2, Table.HORIZONTAL_ALIGN_RIGHT);
		frameTable.setWidth("100%");
		frameTable.add(new Text(iwrb.getLocalizedString("usr_fam_win_sure","Are you sure ?")), 1, 1);

		Table bottomTable = new Table();
		bottomTable.setCellpadding(0);
		bottomTable.setCellspacing(0);
		bottomTable.setStyleClass(mainStyleClass);
		bottomTable.setWidth("100%");
		
		
		Help help = getHelp(HELP_TEXT_KEY_DETATCH);
		bottomTable.add(help,1,1);
		bottomTable.setAlignment(1,2,Table.HORIZONTAL_ALIGN_RIGHT);
		bottomTable.add(new CloseButton(iwrb.getLocalizedImageButton("usr_fam_win_cancel","Cancel")), 1, 2);
		bottomTable.add(Text.NON_BREAKING_SPACE,1,2);
		bottomTable.add(new SubmitButton(iwrb.getLocalizedImageButton("usr_fam_win_yes","Yes")), 1, 2);

		table.setVerticalAlignment(1,1,Table.VERTICAL_ALIGN_TOP);
		table.setVerticalAlignment(1,3,Table.VERTICAL_ALIGN_TOP);
		table.add(frameTable,1,1);
		table.add(bottomTable,1,3);
		
		form.add(table);
		add(form,iwc);
	}

	private void save(IWContext iwc) throws RemoteException {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		FamilyLogic logic = getMemberFamilyLogic(iwc);
		String relationType = iwc.getParameter(_PARAM_TYPE);
		String relatedPerson = iwc.getParameter(_PARAM_RELATED_USER_ID);
		if (relatedPerson != null) {
			switch (method) {
				case _METHOD_ATTACH :
					try {
						User relatedUser = getUserBusiness(iwc).getUserHome().findByPersonalID(relatedPerson);
						
						if(!user.getPrimaryKey().equals(relatedUser.getPrimaryKey())) {
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
							else if (relationType.equals(logic.getCustodianRelationType())) {
								logic.setAsCustodianFor(relatedUser, user);
							}
							else if( relationType.equals(FAMILY_RELATION_CUSTODIAN_AND_PARENT)){
								logic.setAsParentFor(relatedUser, user);
								logic.setAsCustodianFor(relatedUser, user);
							}
							close();

						}
						else{
							setAlertOnLoad(iwrb.getLocalizedString("fam_conn.same_user", "You cannot connect a user to himself"));
							try {
								getAttachForm(iwc);
							}
							catch(Exception e) {
								
							}
							
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
						else if (relationType.equals(logic.getCustodianRelationType())) {
							logic.removeAsCustodianFor(relatedUser, user);
						}
						else if( relationType.equals(FAMILY_RELATION_CUSTODIAN_AND_PARENT)){
							logic.removeAsParentFor(relatedUser, user);
							logic.removeAsCustodianFor(relatedUser, user);
						}
						close();
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

		
		iwc.setSessionAttribute(TabbedPropertyPanel.TAB_STORE_WINDOW, "TRUE");
		setParentPageFormToSubmitOnUnLoad(TabbedPropertyPanel.TAB_FORM_NAME);
	}

	private DropdownMenu getRelationMenu(IWContext iwc) throws RemoteException {
		DropdownMenu menu = new DropdownMenu(_PARAM_TYPE);
		IWResourceBundle iwrb = getResourceBundle(iwc);
		menu.addMenuElement(getMemberFamilyLogic(iwc).getChildRelationType(), iwrb.getLocalizedString("usr_fam_win_child","Child"));
		menu.addMenuElement(getMemberFamilyLogic(iwc).getParentRelationType(), iwrb.getLocalizedString("usr_fam_win_parent","Parent"));
		menu.addMenuElement(getMemberFamilyLogic(iwc).getCustodianRelationType(), iwrb.getLocalizedString("usr_fam_win_custodian","Custodian"));
		menu.addMenuElement(FAMILY_RELATION_CUSTODIAN_AND_PARENT, iwrb.getLocalizedString("usr_fam_win_custodian_and_parent","Custodian and parent"));
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

	public FamilyLogic getMemberFamilyLogic(IWApplicationContext iwc) {
		FamilyLogic familyLogic = null;
		if (familyLogic == null) {
			try {
				familyLogic = (FamilyLogic) com.idega.business.IBOLookup.getServiceInstance(iwc, FamilyLogic.class);
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