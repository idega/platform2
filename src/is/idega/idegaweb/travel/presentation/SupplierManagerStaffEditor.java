package is.idega.idegaweb.travel.presentation;

import is.idega.idegaweb.travel.business.TravelSessionManager;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import com.idega.block.login.business.LoginBusiness;
import com.idega.business.IBOLookup;
import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.PasswordInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;


public class SupplierManagerStaffEditor extends TravelManager {

	private static String ACTION_SAVE_USER = "saveuser";
	private static String ACTION_NEW_USER = "newuser";
	private static String ACTION_DELETE_USER = "deleteuser";
	private static String ACTION_EDIT_USER = "edituser";
	private static String ACTION_SAVENEW_USER = "savenewuser";
	private static String ACTION_USE_USER = "useuser";
	private static String ACTION_UPDATE_USER = "updateuser";
	private static String PARAM_USER_ID = "usr_id";
	
	public void _main(IWContext iwc) throws Exception {
		if (ACTION_USE_USER.equals(iwc.getParameter(this.sAction))) {
			try {
				UserHome uhome = (UserHome) IDOLookup.getHome(User.class);
				
				User user = uhome.findByPrimaryKey(new Integer(iwc.getParameter(PARAM_USER_ID)));
				TravelSessionManager tsm = (TravelSessionManager) IBOLookup.getSessionInstance(iwc, TravelSessionManager.class);
				tsm.clearAll();
				LoginBusiness lBiz = new LoginBusiness();
				lBiz.logInAsAnotherUser(iwc, user);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		super._main(iwc);
	}
	
	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		add(Text.BREAK);
		String action = iwc.getParameter(this.sAction);		
		if (action != null && action.equals(ACTION_SAVE_USER)) {
			saveUser(iwc);
			showUserlist(iwc);
		} else if (action != null && action.equals(ACTION_NEW_USER)) {
			showForm(iwc);
		} else if (action != null && action.equals(ACTION_DELETE_USER)) {
			deleteUser(iwc);
			showUserlist(iwc);
		} else if (action != null && action.equals(ACTION_EDIT_USER)) {
			showForm(iwc);
		} else if (action != null && action.equals(ACTION_UPDATE_USER)) {
			updateUser(iwc);
			add(getUser(iwc).getName()+super.getResourceBundle().getLocalizedString("travel.user_updated"," updated."));
			showUserlist(iwc);
		} else {
			showUserlist(iwc);
		}
	}
	
	private void showUserlist(IWContext iwc) throws RemoteException {	
		List users = null;
		try {
			users = getSupplierManagerBusiness(iwc).getSupplierManagerStaffUsers(getSupplierManager());
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		} 
		Table table = getTable();
		table.setWidth(300);
		table.setBorder(0);
		
		int startrow = 1;
		table.mergeCells(1, startrow, 5, startrow);
		table.setRowColor(startrow, backgroundColor);
		table.add(getHeaderText(getResourceBundle().getLocalizedString("travel.users", "Users")), 1, startrow++);
//		int startrow = 3;
		User user;
		Link delete;
		Link edit;
		Link use;
		String userid = "";
		for(int i=0; i<users.size();i++) {
			user = (User) users.get(i);
			userid = user.getPrimaryKey().toString();
			table.add(user.getName(), 1, startrow);
			
			table.add(user.getPrimaryGroup().getName(), 2, startrow);
			
			delete = new Link(super.getResourceBundle().getLocalizedString("travel.link_delete", " Delete "));
			delete.addParameter(sAction, ACTION_DELETE_USER);
			delete.setOnClick("return confirm('"+super.getResourceBundle().getLocalizedString("travel.are_you_sure","Are you sure")+"?');");
			delete.addParameter(PARAM_USER_ID, userid);
			if (!iwc.getCurrentUser().equals(user)) {
				table.add(delete, 3, startrow);
			}
			table.setAlignment(3, startrow, "center");
			
			edit = new Link(super.getResourceBundle().getLocalizedString("travel.link_edit"," Edit "));
			edit.addParameter(sAction, ACTION_EDIT_USER);
			edit.addParameter(PARAM_USER_ID, userid);
			table.add(edit, 4, startrow);
			table.setAlignment(4, startrow, "center");
			
			use = new Link(super.getResourceBundle().getLocalizedString("travel.link_use"," Use "));
			use.addParameter(sAction, ACTION_USE_USER);
			use.addParameter(PARAM_USER_ID, userid);
			table.add(use, 5, startrow);
			table.setAlignment(5, startrow, "center");
			table.setRowColor(startrow, GRAY);

			++startrow;
		}
		Link newuser = new Link(tsm.getIWResourceBundle().getLocalizedImageButton("travel.new_user2", " New User "), SupplierManagerStaffEditor.class);
		newuser.addParameter(this.sAction, ACTION_NEW_USER);
		table.setRowColor(startrow, GRAY);
		table.add(newuser, 1, startrow++);

		add(table);
	}
	
	private void saveUser(IWContext iwc) throws RemoteException {
		String name = iwc.getParameter("namefield");
		String login = iwc.getParameter("loginfield");
		String passw = iwc.getParameter("passfield");
		String passw2 = iwc.getParameter("passrfield");
		String usertype = iwc.getParameter("user_type");
		if (LoginDBHandler.isLoginInUse(login)) {
			add(super.getResourceBundle().getLocalizedString("travel.login_in_use", "Login in use"));
		} else if (!passw.equals(passw2)||passw==null||passw.equals("")) {
			add(super.getResourceBundle().getLocalizedString("travel.passw_empty", "Passwords do not match or are empty"));
		}	else {
			User user;
			try {
				user = getSupplierManagerBusiness(iwc).createSupplierManagerStaff(getSupplierManager(), usertype, name , login, passw);
				add(super.getResourceBundle().getLocalizedString("travel.user_created","Created user "+user.getName()));
			} catch (CreateException e) {
				add(super.getResourceBundle().getLocalizedString("travel.user_creation_failed","Failed to created user"));
				e.printStackTrace();
			}
		}
	}
	
	private void updateUser(IWContext iwc) throws RemoteException {
		String name = iwc.getParameter("namefield");
		String login = iwc.getParameter("loginfield");
		String passw = iwc.getParameter("passfield");
		String passw2 = iwc.getParameter("passrfield");
		String usertype = iwc.getParameter("user_type");
		if (iwc.getParameter(PARAM_USER_ID)!=null) {
			int userid = Integer.parseInt(iwc.getParameter(PARAM_USER_ID));
			if (!passw.equals(passw2)||passw==null||passw.equals("")) {
				add(super.getResourceBundle().getLocalizedString("travel.passw_empty", "Passwords do not match or are empty"));
			}	else {			
				try {
					LoginDBHandler.changePassword(userid, passw);
					LoginDBHandler.updateLogin(userid, login, login);			
				} catch (NumberFormatException e) {			
					e.printStackTrace();
				} catch (Exception e) {			
					e.printStackTrace();
				}
			
				getUserBusiness().updateUser(userid, name, "", "", "", "", null, "", null, new Integer(getUser(iwc).getPrimaryGroupID()) );
				updateSupplierManagerStaffUserPrimaryGroup(iwc, usertype);
			}		
		} else {
			add(super.getResourceBundle().getLocalizedString("travel.user_id_is_null","Can't update. usr_id is null"));
		}		
	}
	
	private void deleteUser(IWContext iwc) {
		try {
			getUserBusiness().deleteUser(getUser(iwc), iwc.getCurrentUser());
		} catch (RemoteException e) {	
			e.printStackTrace();
		} catch (RemoveException e) {
			e.printStackTrace();
		}	 
	}
	
	public void updateSupplierManagerStaffUserPrimaryGroup(IWContext iwc, String grouptype) {
		User user = getUser(iwc);
		try {
			user.setPrimaryGroup(getSupplierManagerBusiness(iwc).getGroupFromGroupType(getSupplierManager(), grouptype));
			user.store();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * get User obj using primary key.
	 */
	private User getUser(IWContext iwc) {
		List users = null;
		String userID = iwc.getParameter(PARAM_USER_ID);
		try {
			users = getSupplierManagerBusiness(iwc).getSupplierManagerStaffUsers(getSupplierManager());
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		} 
		User user = null;
		Iterator listiter = users.iterator();
		while(listiter.hasNext()) {
			user = (User) listiter.next();
			if(user.getPrimaryKey().toString().equals(userID)) {
				return user;
			}
		}
		return user;
	}
	
	
	
	private void showForm(IWContext iwc) throws RemoteException {
		Form form = new Form();
		Collection userTypes = new Vector();
		
		Table table = getTable();
		table.setWidth(300);
//		table.setCellpadding(1);
		table.setBorder(0);
		
		table.add(getHeaderText(getResourceBundle().getLocalizedString("travel.create_user", "Create User")), 1, 1);
		table.add(super.getResourceBundle().getLocalizedString("travel.name", "Name"), 1, 2);
		TextInput namefield = new TextInput("namefield");
        namefield.setSize(15);
        namefield.keepStatusOnAction();
		table.add(namefield, 2, 2);
		
		
		table.add(super.getResourceBundle().getLocalizedString("travel.login", "Login"), 1, 3);
		TextInput loginfield = new TextInput("loginfield");
        loginfield.setSize(15);
        loginfield.keepStatusOnAction();
		table.add(loginfield, 2, 3);
		
		table.add(super.getResourceBundle().getLocalizedString("travel.password", "Password"), 1, 4);
		PasswordInput passfield = new PasswordInput("passfield");
        passfield.setSize(15);
        passfield.keepStatusOnAction();      
        table.add(super.getResourceBundle().getLocalizedString("travel.retype_password", "Retype password"), 1, 5);
		table.add(passfield, 2, 4);
		
		PasswordInput passrfield = new PasswordInput("passrfield");
        passrfield.setSize(15);
        passrfield.keepStatusOnAction();
		table.add(passrfield, 2, 5);	
		
		table.add(super.getResourceBundle().getLocalizedString("travel.user_type", "User type"), 1, 6);
		DropdownMenu userType = new DropdownMenu("user_type");
		
		table.add(userType, 2,6);
		
		table.mergeCells(1,1, 2,1);
		
		String action = iwc.getParameter(this.sAction);
		if(action != null && action.equals(ACTION_EDIT_USER)) {
			User user=getUser(iwc);
			namefield.setValue(user.getFirstName());
			loginfield.setValue( LoginDBHandler.getUserLogin(Integer.parseInt(iwc.getParameter(PARAM_USER_ID))).getUserLogin() );
			table.add(new SubmitButton(super.getResourceBundle().getImage("buttons/save.gif"), this.sAction, ACTION_UPDATE_USER),2,7);
			form.addParameter(PARAM_USER_ID, iwc.getParameter(PARAM_USER_ID));
			
			Collection col = getSupplierManagerBusiness(iwc).getStaffGroups(getSupplierManager());
			Iterator iter = col.iterator();
			Group g;
			while (iter.hasNext()) {
				g = (Group )iter.next();
				userType.addMenuElement(g.getGroupType(),
						super.getResourceBundle().getLocalizedString("travel.groupname."+g.getGroupType(),g.getName()));
			}
			
			userType.setSelectedElement(getUser(iwc).getPrimaryGroup().getGroupType().toString());
			
		} else {
			Collection col = getSupplierManagerBusiness(iwc).getStaffGroups(getSupplierManager());
			Iterator iter = col.iterator();
			Group g;
			while (iter.hasNext()) {
				g = (Group )iter.next();
				userType.addMenuElement(g.getGroupType(),
						super.getResourceBundle().getLocalizedString("travel.groupname."+g.getGroupType(),g.getName()));
			}
			table.add(new SubmitButton(super.getResourceBundle().getImage("buttons/save.gif"), this.sAction, ACTION_SAVE_USER),2,7);
		}
		
		table.setRowColor(1, backgroundColor);
		table.setRowColor(2, GRAY);
		table.setRowColor(3, GRAY);
		table.setRowColor(4, GRAY);
		table.setRowColor(5, GRAY);
		table.setRowColor(6, GRAY);
		table.setRowColor(7, GRAY);
		table.setAlignment(2, 7, Table.HORIZONTAL_ALIGN_RIGHT);
		form.add(table);
		add(form);
	}
	
}
