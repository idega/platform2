package is.idega.idegaweb.travel.block.search.presentation;

import is.idega.idegaweb.travel.block.search.business.ServiceSearchBusiness;
import is.idega.idegaweb.travel.block.search.data.ServiceSearchEngine;
import is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineHome;
import is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineStaffGroup;
import is.idega.idegaweb.travel.presentation.AdministratorReport;
import is.idega.idegaweb.travel.presentation.AdministratorReports;
import is.idega.idegaweb.travel.presentation.TravelManager;
import is.idega.idegaweb.travel.presentation.Users;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;

import com.idega.business.IBOLookup;
import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.core.accesscontrol.data.LoginTable;
import com.idega.core.accesscontrol.data.PermissionGroup;
import com.idega.core.user.business.UserBusiness;
import com.idega.core.user.business.UserGroupBusiness;
import com.idega.core.user.data.User;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.BooleanInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.PasswordInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

/**
 * @author gimmi
 */
public class ServiceSearchAdmin extends TravelManager {

	private String ACTION = "ssa_a";
	private String ACTION_USERS = "ssa_u";
	private String ACTION_NEW_USER = "ssa_nu";
	private String ACTION_SAVE_USER = "ssa_su";
	private String ACTION_DELETE_USER = "ssa_du";
	private String ACTION_DELETE_CONFIRMED = "ssa_dc";
	private String ACTION_REPORTS = "ssa_rp";
	
	private String PARAMETER_NAME = "ssa_pn";
	private String PARAMETER_LOGIN = "ssa_pl";
	private String PARAMETER_PASSWORD_1 = "ssa_pp";
	private String PARAMETER_PASSWORD_2 = "ssa_ppw";
	private String PARAMETER_IS_ADMIN = "ssa_pia";
	private String PARAMETER_USER_ID = "ssa_ppu";
	
	public static String PARAMETER_SERVICE_SEARCH_ENGINE_ID = "ssa_id";
	
	private ServiceSearchEngine engine = null;
	private IWResourceBundle iwrb = null;
	
	private void init(IWContext iwc) {
		try {
			if (iwc.isParameterSet(PARAMETER_SERVICE_SEARCH_ENGINE_ID)) {
				ServiceSearchEngineHome eHome = (ServiceSearchEngineHome) IDOLookup.getHome(ServiceSearchEngine.class);
				engine = eHome.findByPrimaryKey(new Integer(iwc.getParameter(PARAMETER_SERVICE_SEARCH_ENGINE_ID)));
			} else {
				engine = super.getTravelSessionManager(iwc).getSearchEngine();
			}
			iwrb = getResourceBundle(iwc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		init(iwc);
		add(Text.BREAK);
		if (engine == null) {
			add(getText(iwrb.getLocalizedString("travel.search.no_engine_selected", "No engine selected")));
		} else {
			String action = iwc.getParameter(ACTION);
			
			if (action == null) {
				menu();
			} else if (action.equals(ACTION_USERS)) {
				users(iwc);
			} else if (action.equals(ACTION_NEW_USER)) {
				newUser(iwc);
			} else if (action.equals(ACTION_SAVE_USER)) {
				saveUser(iwc);
			} else if (action.equals(ACTION_DELETE_USER)) {
				deleteVerify(iwc);
			} else if (action.equals(ACTION_DELETE_CONFIRMED)) {
				deleteUser(iwc);
				users(iwc);
			} else if (action.equals(ACTION_REPORTS)) {
				reports(iwc);
			} 
		}
	}
	
	
	private void menu() {
		Form form = getForm();
		Table table = getTable();
		form.add(table);
		
		int row = 1;
		
		Link users = getLink(iwrb.getLocalizedString("travel.search.users", "Users"));
		users.addParameter(ACTION, ACTION_USERS);
				
		Link reports = getLink(iwrb.getLocalizedString("travel.search.reports", "Reports"));
		reports.addParameter(ACTION, ACTION_REPORTS);
		
		table.setRowColor(row, backgroundColor);
		table.add(getHeaderText(engine.getName()), 1, row++);
		table.setRowColor(row, GRAY);
		table.add(users, 1, row++);
		table.setRowColor(row, GRAY);
		table.add(reports, 1, row++);
//		table.add(getLink(), 1, row++);
//		table.add(getLink(), 1, row++);
		
		add(form);
	}
	
	private void reports(IWContext iwc) {
		try {
			Form form = new Form();
			form.maintainParameter(PARAMETER_SERVICE_SEARCH_ENGINE_ID);
			form.addParameter(ACTION, ACTION_REPORTS);

			AdministratorReports reps = new AdministratorReports();
			AdministratorReport rep = new SearchEngineBookingReport(iwc);
			((SearchEngineBookingReport)rep).setSearchEngine(engine);
			reps.setReport(rep);
			reps.setSuppliers(engine.getSuppliers());
			reps.maintainParameter(ACTION, ACTION_REPORTS);
			reps.maintainParameter(PARAMETER_SERVICE_SEARCH_ENGINE_ID, engine.getPrimaryKey().toString());
			reps.main(iwc);
			form.add(reps.topTable(iwc));
			form.add(reps.report(iwc));

			add(form);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void users(IWContext iwc) {
		Form form = getForm();
		Table table = getTable();
		form.add(table);
		int row = 1;

		List users = null;
		List admins = null;
		ServiceSearchEngineStaffGroup sGroup = null;
		PermissionGroup pGroup = null;
		try {
			sGroup = getBusiness(iwc).getServiceSearchEngineStaffGroup(engine);
			pGroup = getBusiness(iwc).getPermissionGroup(engine);			
			UserGroupBusiness ugb = new UserGroupBusiness();
			users = ugb.getUsersContained(sGroup);
			admins = ugb.getUsersContained(pGroup);
			if (users != null && admins != null) {
				users.removeAll(admins);
			}
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}

		table.add(getHeaderText(iwrb.getLocalizedString("travel.search.user", "User")), 1, row);
		table.add(getHeaderText(iwrb.getLocalizedString("travel.search.login", "Login")), 2, row);
//		table.add(getHeaderText(iwrb.getLocalizedString("travel.search.admin", "Admin")), 2, row);
		table.add(getHeaderText(""), 3, row);
		table.add(getHeaderText(""), 4, row);
//		table.mergeCells(2, row, 4, row);
		table.setRowColor(row, backgroundColor);
		++row;
		
		if (admins != null && !admins.isEmpty()) {
			table.add(getHeaderText(iwrb.getLocalizedString("travel.search.administrators", "Administrators")), 1, row);
			table.mergeCells(1, row, 4, row);
			table.setRowColor(row++, backgroundColor);
			row = insertUsers(table, row, admins);
		}
		
		if (users != null && !users.isEmpty()) {
			table.add(getHeaderText(iwrb.getLocalizedString("travel.search.other_users", "Other users")), 1, row);
			table.mergeCells(1, row, 4, row);
			table.setRowColor(row++, backgroundColor);
			row = insertUsers(table, row, users);
		}
		
		if (row == 1) {
			++row;
		}
		
		if (sGroup != null) {
			// Adda bara new takka ef ad sGrouppan se til
			SubmitButton newB = new SubmitButton(iwrb.getLocalizedImageButton("travel.search.new","New"), ACTION, ACTION_NEW_USER);
			table.add(newB, 1, row);
			table.mergeCells(1, row, 4, row);
		}
		table.setRowColor(row, GRAY);
		
		add(form);
	}
	
	private int insertUsers(Table table, int row, List users) {
		Iterator iter = users.iterator();
		User user;
		Link edit;
		Link delete;
		while (iter.hasNext()) {
			user = (User) iter.next();
			table.add(getText(user.getName()), 1, row);
			LoginTable lt = LoginDBHandler.getUserLogin(user.getID());
			if (lt != null) {
				table.add(lt.getUserLogin(), 2, row);
			}
			
			edit = getLink();
			edit.setImage(iwrb.getLocalizedImageButton("travel.search.edit", "Edit"));
			edit.addParameter(ACTION, ACTION_NEW_USER);
			edit.addParameter(PARAMETER_USER_ID, user.getID());
			delete = getLink();
			delete.setImage(iwrb.getLocalizedImageButton("travel.search.delete", "Delete"));
			delete.addParameter(ACTION, ACTION_DELETE_USER);
			delete.addParameter(PARAMETER_USER_ID, user.getID());

			table.add(edit, 3, row);
			table.add(delete, 4, row);
			table.setRowColor(row++, GRAY);
		}
		return row;
	}

	private void newUser(IWContext iwc) {
		Form form = getForm();
		Table table = getTable();
		form.add(table);
		int row = 1;
		
		String userID = iwc.getParameter(PARAMETER_USER_ID);
		User user = null;
		
		table.mergeCells(1, row, 2, row);
		table.setRowColor(row, backgroundColor);
		table.add(getHeaderText(iwrb.getLocalizedString("travel.search.user_creation", "User Creation")), 1, row++);
		
		TextInput name = new TextInput(PARAMETER_NAME);
		TextInput login = new TextInput(PARAMETER_LOGIN);
		PasswordInput pass1 = new PasswordInput(PARAMETER_PASSWORD_1);
		PasswordInput pass2 = new PasswordInput(PARAMETER_PASSWORD_2);
		BooleanInput isAdmin = new BooleanInput(PARAMETER_IS_ADMIN);
		
		if (userID != null) {
			form.addParameter(PARAMETER_USER_ID, userID);
			try {
				user = UserBusiness.getUser(Integer.parseInt(userID));
				LoginTable lt = LoginDBHandler.getUserLogin(user.getID());
				name.setContent(user.getName());
				if (lt != null) {
					login.setContent(lt.getUserLogin());
				}
				isAdmin.setSelected(getBusiness(iwc).isUserInPermissionGroup(engine, user));
			}catch (Exception n) {}
		}
		
		if (iwc.isParameterSet(PARAMETER_NAME)) {
			name.setContent(iwc.getParameter(PARAMETER_NAME));
			login.setContent(iwc.getParameter(PARAMETER_LOGIN));
			String sIsAdmin = iwc.getParameter(PARAMETER_IS_ADMIN);
			if (isAdmin != null && sIsAdmin.equalsIgnoreCase("Y")) {
				isAdmin.setSelected(true);
			} else {
				isAdmin.setSelected(false);
			}
		}
		
		table.add(getText(iwrb.getLocalizedString("travel.search.name", "Name")), 1,row);
		table.add(name, 2, row);
		table.setRowColor(row++, GRAY);
		
		table.add(getText(iwrb.getLocalizedString("travel.search.login", "Login")), 1,row);
		table.add(login, 2, row);
		table.setRowColor(row++, GRAY);

		table.add(getText(iwrb.getLocalizedString("travel.search.password", "Password")), 1,row);
		table.add(pass1, 2, row);
		table.setRowColor(row++, GRAY);
		
		table.add(getText(iwrb.getLocalizedString("travel.search.retype_password", "Retype password")), 1,row);
		table.add(pass2, 2, row);
		table.setRowColor(row++, GRAY);
		
		table.add(getText(iwrb.getLocalizedString("travel.search.administrator", "Administrator")), 1,row);
		table.add(isAdmin, 2, row);
		table.setRowColor(row++, GRAY);
		
		SubmitButton submit = null;
		if (user == null) {
			submit = new SubmitButton(iwrb.getLocalizedImageButton("travel.search.save", "Save"), ACTION, ACTION_SAVE_USER);
		} else {
			submit = new SubmitButton(iwrb.getLocalizedImageButton("travel.search.update", "Update"), ACTION, ACTION_SAVE_USER);
		}
		table.add(submit, 2, row);
		table.setAlignment(2, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setRowColor(row, GRAY);
		add(form);
	}
	
	private void saveUser(IWContext iwc) {

		String name = iwc.getParameter(PARAMETER_NAME);
		String login = iwc.getParameter(PARAMETER_LOGIN);
		String passOne = iwc.getParameter(PARAMETER_PASSWORD_1);
		String passTwo = iwc.getParameter(PARAMETER_PASSWORD_2);
		String sIsAdmin = iwc.getParameter(PARAMETER_IS_ADMIN);
		String userID = iwc.getParameter(PARAMETER_USER_ID);
		
		boolean inUse = true;
    boolean passwordsOK = false;
    boolean passwordUpdate = true;

    if (passOne != null && passTwo != null && !passOne.equals("") && passOne.equals(passTwo)) {
      passwordsOK = true;
    }else if (passOne.equals("") && passTwo.equals("")) {
      passwordsOK = true;
      passwordUpdate = false;
    }


    if (login != null) {
      if (!login.equals("")) {
        inUse = LoginDBHandler.isLoginInUse(login);
        if (userID!= null) {
          LoginTable lt = LoginDBHandler.getUserLogin(Integer.parseInt(userID));
          if (lt != null) {
            if (lt.getUserLogin().equals(login)) {
              inUse = false;
            }
          }
        }
    	}
    }
    
    if (inUse) {
    	add(getHeaderText(iwrb.getLocalizedString("travel.search.login_in_use", "Login is in use")+Text.BREAK));
    }
    if (!passwordsOK) {
    	add(getHeaderText(iwrb.getLocalizedString("travel.search.passwords_not_the_same", "Passwords are not the same")+Text.BREAK));
    }
    
    if (!inUse && passwordsOK) {
			try {
			  UserBusiness ub = new UserBusiness();
			  User user = null;
	
			  boolean isAdmin = false;
			  if (sIsAdmin != null) {
			  	if (sIsAdmin.equalsIgnoreCase("Y")) {
			  		isAdmin = true;
			  	}
			  }
			  if (userID == null) {
					user = ub.insertUser(name, "", "", name, "staff", null, null, null);
					LoginDBHandler.createLogin(user.getID(), login, passOne);
			  }else {
					user = ((com.idega.core.user.data.UserHome)com.idega.data.IDOLookup.getHomeLegacy(User.class)).findByPrimaryKeyLegacy(Integer.parseInt(userID));
					ub.updateUser(user.getID(), name, "", "", name, "staff", null, null, null);
					if (passwordUpdate) {
					  LoginTable lt = LoginDBHandler.getUserLogin(Integer.parseInt(userID));
					  if (lt == null) {
						LoginDBHandler.createLogin(user.getID(), login, passOne);
					  }else {
						LoginDBHandler.updateLogin(user.getID(), login, passOne);
					  }
					}
					Users.removeUserFromAllGroups(user);
			  }
	
		  	getBusiness(iwc).addUser(engine, user, isAdmin);
		  	users(iwc);
	
			} catch (Exception e) {
				e.printStackTrace(System.err);
				add(getHeaderText(iwrb.getLocalizedString("travel.search.could_not_create_user", "Could not create user")+Text.BREAK));
			}
    } else {
    	newUser(iwc);
    }
    
	}
	
	private void deleteVerify(IWContext iwc) {
		Table table = getTable();
		Form form = getForm();
		form.add(table);
		
		table.add(getHeaderText(iwrb.getLocalizedString("travel.search.delete_user", "Delete User")), 1, 1);
		table.mergeCells(1, 1, 2, 1);
		
		table.add(getText(iwrb.getLocalizedString("travel.search.verify_delete", "Are you sure you want to delete the user ")), 1, 2);
		table.mergeCells(1, 2, 2, 2);
		
		table.add(new BackButton(iwrb.getLocalizedImageButton("travel.search.back", "Back")), 1, 3);
		String userID = iwc.getParameter(PARAMETER_USER_ID);
		if (userID != null) {
			try {
				User user = ((com.idega.core.user.data.UserHome)com.idega.data.IDOLookup.getHomeLegacy(User.class)).findByPrimaryKeyLegacy(Integer.parseInt(userID));
				Text text = getText(user.getName());
				text.setBold(true);
				table.add(text, 1, 2);
				Link link = getLink();
				link.setImage(iwrb.getLocalizedImageButton("travel.search.delete", "Delete"));
				link.addParameter(PARAMETER_USER_ID, user.getID());
				link.addParameter(ACTION, ACTION_DELETE_CONFIRMED);
				table.add(link, 2, 3);
				table.setAlignment(2, 3, Table.HORIZONTAL_ALIGN_RIGHT);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		table.setRowColor(1, backgroundColor);
		table.setRowColor(2, GRAY);
		table.setRowColor(3, GRAY);
		
		add(form);
	}
	
	private void deleteUser(IWContext iwc) {
		String userID = iwc.getParameter(PARAMETER_USER_ID);
		if (userID != null) {
			try {
				User user = ((com.idega.core.user.data.UserHome)com.idega.data.IDOLookup.getHomeLegacy(User.class)).findByPrimaryKeyLegacy(Integer.parseInt(userID));
				com.idega.core.accesscontrol.business.LoginDBHandler.deleteUserLogin(user.getID());
				Users.removeUserFromAllGroups(user);
				add(getText(iwrb.getLocalizedString("travel.operation_successful","Operation successful")));
		  }catch (Exception e) {
			e.printStackTrace(System.err);
			add(getText(iwrb.getLocalizedString("travel.operation_failed","Operation failed")));
		  }
		}
	}

protected Form getForm() {
		Form form = new Form();
		form.maintainParameter(PARAMETER_SERVICE_SEARCH_ENGINE_ID);
		return form;
	}
	
	protected Link getLink() {
		Link link = new Link();
		link.addParameter(PARAMETER_SERVICE_SEARCH_ENGINE_ID, engine.getPrimaryKey().toString());
		return link;
	}

	protected Link getLink(String content) {
		Link link = getLink();
		link.setText(getText(content));
		return link;
	}

	public ServiceSearchBusiness getBusiness(IWApplicationContext iwac) throws RemoteException {
		return (ServiceSearchBusiness) IBOLookup.getServiceInstance(iwac, ServiceSearchBusiness.class);
	}
	
}
