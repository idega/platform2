package is.idega.idegaweb.travel.presentation;

import is.idega.idegaweb.travel.business.TravelSessionManager;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import com.idega.block.login.business.LoginBusiness;
import com.idega.business.IBOLookup;
import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.PasswordInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.data.User;


/**
 * @author gimmi
 */
public class SupplierManagerEditor extends TravelManager {
	
	private static String ACTION = "sme_ac";
	private static String ACTION_NEW = "sme_acn";
	private static String ACTION_EDIT = "sme_ace";
	private static String ACTION_SAVE = "sme_acs";
	private static String ACTION_DELETE = "sme_acd";
	private static String PARAMETER_CHOOSE = "sme_chU";
	private static String PARAMETER_MANAGER_ID = "sme_mid";
	private static String PARAMETER_MANAGER_NAME = "sme_mn";
	private static String PARAMETER_MANAGER_DESCRIPTION = "sme_md";
	private static String PARAMETER_USER_NAME = "sme_un";
	private static String PARAMETER_PASSWORD_1 = "sme_p1";
	private static String PARAMETER_PASSWORD_2 = "sme_p2";
	private static String PARAMETER_ADMIN_NAME = "sme_an";
	
	
	private IWResourceBundle iwrb;
	private String errorMessage;

	public void _main(IWContext iwc) throws Exception {
		if (PARAMETER_CHOOSE.equals(iwc.getParameter(this.ACTION))) {
			try {
				GroupHome uhome = (GroupHome) IDOLookup.getHome(Group.class);
				
				Group group = uhome.findByPrimaryKey(new Integer(iwc.getParameter(PARAMETER_MANAGER_ID)));
				TravelSessionManager tsm = (TravelSessionManager) IBOLookup.getSessionInstance(iwc, TravelSessionManager.class);
				tsm.clearAll();
				Collection coll = getSupplierManagerBusiness(iwc).getSupplierManagerAdmins(group);
				LoginBusiness lBiz = new LoginBusiness();
				if (coll != null) {
					Iterator iter = coll.iterator();
					if (iter.hasNext()) {
						User user = (User) iter.next();
						lBiz.logInAsAnotherUser(iwc, user);
						add(Text.getBreak());
						add(getHeaderText("Logged in as "+group.getName()));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		super._main(iwc);
	}
	
	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		add(Text.getBreak());
		iwrb = getResourceBundle();
		String action = iwc.getParameter(ACTION);
		if (action == null || "".equals(action)) {
			managerList(iwc);
		} else if (action.equals(ACTION_NEW)) {
			managerEditor(null);
		} else if (action.equals(ACTION_EDIT)) {
			String mID = iwc.getParameter(PARAMETER_MANAGER_ID);
			managerEditor( ((GroupHome) IDOLookup.getHome(Group.class)).findByPrimaryKey(new Integer(mID))  );
		} else if (action.equals(ACTION_SAVE)) {
			managerEditor(saveManager(iwc));
		} else if (action.equals(ACTION_DELETE)) {
			String mID = iwc.getParameter(PARAMETER_MANAGER_ID);
			deleteManager( ((GroupHome) IDOLookup.getHome(Group.class)).findByPrimaryKey(new Integer(mID))  );
			managerList(iwc);
		}
		
	}
	
	private void deleteManager(Group group) throws EJBException, RemoveException {
		group.remove();
	}
	
	private Group saveManager(IWContext iwc) throws IDOLookupException, RemoteException, CreateException {
		String mID = iwc.getParameter(PARAMETER_MANAGER_ID);
		String name = iwc.getParameter(PARAMETER_MANAGER_NAME);
		String adminName = iwc.getParameter(PARAMETER_ADMIN_NAME);
		String description = iwc.getParameter(PARAMETER_MANAGER_DESCRIPTION);
		
		if (mID == null) {
			mID = "-349857"; // some number, to force FinderException
		}
		
		Group manager = null;
		User user = null;
		if (name != null && !"".equals(name)) {
			try {
				manager = getSupplierManagerBusiness(iwc).updateSupplierManager(new Integer(mID), name, description);
			}
			catch (FinderException e) {
				String username = iwc.getParameter(PARAMETER_USER_NAME);
				String pass1 = iwc.getParameter(PARAMETER_PASSWORD_1);
				String pass2 = iwc.getParameter(PARAMETER_PASSWORD_2);
				if ("".equals(username.trim()) || "".equals(pass1.trim()) || "".equals(pass2.trim())) {
					errorMessage = iwrb.getLocalizedString("travel.username_and_password_required","Username and password required");
				} else if (!pass1.equals(pass2)) {
					errorMessage = iwrb.getLocalizedString("travel.passwords_must_be_identical", "Passwords must be identical");
				} else {
	        boolean inUse = LoginDBHandler.isLoginInUse(username);
	        if (!inUse) {
	        	manager = getSupplierManagerBusiness(iwc).createSupplierManager(name, description, adminName, username, pass1, iwc);
		      } else {
		      	errorMessage = iwrb.getLocalizedString("travel.username_in_use", "Username in use");
		      }

				}
			}
		}
		
		return manager;
	}
	
	private void managerEditor(Group groupToEdit) {
		Form form = new Form();
		Table table = getTable();
		form.add(table);
		int row = 1;
		
		if (errorMessage != null) {
			add(getHeaderText(errorMessage));
		}
		
		table.add(getHeaderText(iwrb.getLocalizedString("travel.supplier_manager", "Supplier manager")), 1, row);
		table.mergeCells(1, row, 2, row);
		table.setRowColor(row++, backgroundColor);
		
		TextInput name = new TextInput(PARAMETER_MANAGER_NAME);
		TextInput description = new TextInput(PARAMETER_MANAGER_DESCRIPTION);

		if (groupToEdit != null) {
			name.setContent(groupToEdit.getName());
			description.setContent(groupToEdit.getDescription());
			table.add(new HiddenInput(PARAMETER_MANAGER_ID, groupToEdit.getPrimaryKey().toString()), 1, row);
		}

		table.add(getText(iwrb.getLocalizedString("travel.name", "Name")), 1, row);
		table.add(name, 2, row);
		table.setRowColor(row++, GRAY);
		table.add(getText(iwrb.getLocalizedString("travel.description", "Description")), 1, row);
		table.add(description, 2, row);
		table.setRowColor(row++, GRAY);

		if (groupToEdit == null) {
			TextInput adminName = new TextInput(PARAMETER_ADMIN_NAME);
			TextInput login = new TextInput(PARAMETER_USER_NAME);
			PasswordInput pass1 = new PasswordInput(PARAMETER_PASSWORD_1);
			PasswordInput pass2 = new PasswordInput(PARAMETER_PASSWORD_2);
			table.add(getText(iwrb.getLocalizedString("travel.admin_name", "Administrator name")), 1, row);
			table.add(adminName, 2, row);
			table.setRowColor(row++, GRAY);
			table.add(getText(iwrb.getLocalizedString("travel.username", "Username")), 1, row);
			table.add(login, 2, row);
			table.setRowColor(row++, GRAY);
			table.add(getText(iwrb.getLocalizedString("travel.password", "Password")), 1, row);
			table.add(pass1, 2, row);
			table.setRowColor(row++, GRAY);
			table.add(getText(iwrb.getLocalizedString("travel.retype_password", "Retype password")), 1, row);
			table.add(pass2, 2, row);
			table.setRowColor(row++, GRAY);
		}
		
		SubmitButton back = new SubmitButton(iwrb.getLocalizedImageButton("travel.back", "Back"));
		SubmitButton saveButton = new SubmitButton(iwrb.getLocalizedImageButton("travel.save", "Save"), ACTION, ACTION_SAVE);
		table.add(back, 1, row);
		table.setAlignment(2, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.add(saveButton, 2, row);
		table.setRowColor(row, GRAY);
		
		add(form);
	}
	
	private void managerList(IWContext iwc) throws RemoteException {
		Form form = new Form();
		Table table = getTable();
		form.add(table);
		int row = 1;
		
		table.add(getHeaderText(iwrb.getLocalizedString("travel.supplier_manager", "Supplier manager")), 1, row);
		table.add(getHeaderText(iwrb.getLocalizedString("travel.description", "Discription")), 2, row);
		table.add(getHeaderText(""), 3, row);
		table.add(getHeaderText(""), 4, row);
		table.setRowColor(row++, backgroundColor);
		List supplierManagers = getSupplierManagerBusiness(iwc).getSupplierManagerGroup().getChildGroups();
		if (supplierManagers != null) {
			Iterator iter = supplierManagers.iterator();
			Link name;
			Link use;
			Link del;
			Group manager;
			while (iter.hasNext()) {
				manager = (Group) (iter.next());
				name = new Link(getText(manager.getName()));
				name.addParameter(ACTION, ACTION_EDIT);
				name.addParameter(PARAMETER_MANAGER_ID, manager.getPrimaryKey().toString());
				del = new Link(getText(iwrb.getLocalizedString("delete", "Delete")));
				del.addParameter(ACTION, ACTION_DELETE);
				del.addParameter(PARAMETER_MANAGER_ID, manager.getPrimaryKey().toString());
				use = new Link(iwrb.getLocalizedImageButton("use", "Use"));
				use.addParameter(ACTION, PARAMETER_CHOOSE);
				use.addParameter(PARAMETER_MANAGER_ID, manager.getPrimaryKey().toString());

				table.add(name, 1, row);
				table.add(getText(manager.getDescription()), 2, row);
				table.add(use, 3, row);
				table.add(del, 4, row);
				table.setRowColor(row++, GRAY);
			}
		}
		SubmitButton newButton = new SubmitButton(iwrb.getLocalizedImageButton("travel.new", "New"), ACTION, ACTION_NEW);
		table.add(newButton, 1, row);
		table.setRowColor(row, GRAY);
		
		add(form);
	}


	
}
