package is.idega.idegaweb.travel.presentation;

import is.idega.idegaweb.travel.service.presentation.InitialDataObject;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;
import javax.ejb.FinderException;
import com.idega.block.trade.stockroom.data.Reseller;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.business.IBOLookup;
import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.core.accesscontrol.data.LoginTable;
import com.idega.core.data.GenericGroup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.PasswordInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;
/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class Users extends TravelManager implements InitialDataObject {

  private Reseller _reseller;
  private Supplier _supplier;

  private IWResourceBundle _iwrb;
  private IWBundle _bundle;

  private String sAction = "usersAction";
  private String parameterNewUser = "usersNewUser";
  private String parameterSaveUser = "usersSaveUser";
  private String parameterDeleteUser = "usersDeleteUsers";
  private String parameterUpdateUser = "usersUpdateUsers";
  private String paramaterUserID = "usersUserID";

  private String parameterName = "usersName";
  private String parameterLogin = "usersLogin";
  private String parameterPasswordOne = "usersPasswordOne";
  private String parameterPasswordTwo = "usersPasswordTwo";
  private String parameterAdministrator = "usersAdministrator";

  private List parameterNames = new Vector();
  private List parameterValues= new Vector();

  public Users(IWContext iwc) throws Exception{
    main(iwc);
  }

  public void main(IWContext iwc) throws Exception{
    super.main(iwc);
    init();
  }

  private void init() throws RemoteException{
    _reseller = super.getReseller();
    _supplier = super.getSupplier();

    _iwrb = super.getResourceBundle();
    _bundle = super.getBundle();
  }


  public Form handleInsert(IWContext iwc) throws SQLException, RemoteException{
    String action = iwc.getParameter(this.sAction);
    if (action == null) {
      return getUserList(iwc);
    }else if (action.equals(this.parameterNewUser)) {
      return getUserCreation(iwc);
    }else if (action.equals(this.parameterSaveUser)) {
      return saveUser(iwc);
    }else if (action.equals(this.parameterUpdateUser)) {
      return updateUser(iwc);
    }else if (action.equals(this.parameterDeleteUser)) {
      return deleteUser(iwc);
    }else {
      return null;
    }

  }

  private Text getTextWhite(String content) {
    Text text = (Text) super.theText.clone();
      text.setText(content);
    return text;
  }
/*
  private Text getText(String content) {
    Text text = getTextWhite(content);
      text.setFontColor(super.BLACK);
    return text;
  }
*/
  private Form getUserList(IWContext iwc) throws RemoteException {
    Form form = new Form();
    Table table = new Table();
      table.setColor(super.WHITE);
      table.setCellpadding(1);
      table.setCellspacing(1);
      table.setWidth("50%");

    List users = null;
    if (_reseller != null) {
    	try {
    		users = getResellerManager(iwc).getUsersInPermissionGroup(_reseller);
    	} catch (FinderException f) {
    		
    	}
    }
    if (_supplier != null) {
    	try {
    		users = getSupplierManagerBusiness(iwc).getUsersInPermissionGroup(_supplier);
    	} catch (FinderException f) {
    		
    	}
    }
    if (users == null) users = com.idega.util.ListUtil.getEmptyList();


    User user;
    Link update;
    Link delete;
    int row = 1;

    Text tUsers = getTextWhite(_iwrb.getLocalizedString("travel.users","Users"));
      tUsers.setBold(true);
    table.add(tUsers,1,row);
    table.add(getTextWhite(""), 2, row);
    table.setRowColor(row, super.backgroundColor );

    ++row;
    Text admins = getText(_iwrb.getLocalizedString("travel.administrators","Administrators"));
      admins.setBold(true);
    table.add(admins, 1, row);
    table.setRowColor(row, super.GRAY);

    for (int i = 0; i < users.size(); i++) {
      ++row;
      table.setRowColor(row, super.GRAY);
      user = (User) users.get(i);

      table.add(getText(user.getName()), 1, row);
      if (isInPermissionGroup) {
        update = new Link(_iwrb.getImage("buttons/update.gif"));
          update.addParameter(this.sAction, this.parameterUpdateUser);
          update.addParameter(this.paramaterUserID, user.getID());

        delete = new Link(_iwrb.getImage("buttons/delete.gif"));
          delete.addParameter(this.sAction, this.parameterDeleteUser);
          delete.addParameter(this.paramaterUserID, user.getID());

        for (int j = 0; j < this.parameterNames.size(); j++) {
          update.addParameter((String) parameterNames.get(j), (String) parameterValues.get(j));
          delete.addParameter((String) parameterNames.get(j), (String) parameterValues.get(j));
        }


        table.add(update, 2, row);
        table.add(getText(Text.NON_BREAKING_SPACE), 2, row);
        table.add(delete, 2, row);
      }
      table.setAlignment(1, row, "left");
      table.setAlignment(2, row, "right");
    }

//    if (_reseller != null) users = ResellerManager.getUsersNotInPermissionGroup(_reseller);
//    if (_supplier != null) users = getSupplierManagerBusiness(iwc).getUsersNotInPermissionGroup(_supplier);
//    if (users == null) users = com.idega.util.ListUtil.getEmptyList();

    ++row;
    Text staff = getText(_iwrb.getLocalizedString("travel.staff","Staff"));
      staff.setBold(true);
    table.add(staff, 1, row);
    table.setRowColor(row, super.GRAY);

    for (int i = 0; i < users.size(); i++) {
      ++row;
      table.setRowColor(row, super.GRAY);
      user = (User) users.get(i);

      table.add(getText(user.getName()), 1, row);
      if (isInPermissionGroup) {
        update = new Link(_iwrb.getImage("buttons/update.gif"));
          update.addParameter(this.sAction, this.parameterUpdateUser);
          update.addParameter(this.paramaterUserID, user.getID());

        delete = new Link(_iwrb.getImage("buttons/delete.gif"));
          delete.addParameter(this.sAction, this.parameterDeleteUser);
          delete.addParameter(this.paramaterUserID, user.getID());

        for (int j = 0; j < this.parameterNames.size(); j++) {
          update.addParameter((String) parameterNames.get(j), (String) parameterValues.get(j));
          delete.addParameter((String) parameterNames.get(j), (String) parameterValues.get(j));
        }


        table.add(update, 2, row);
        table.add(getText(Text.NON_BREAKING_SPACE), 2, row);
        table.add(delete, 2, row);
      }
      table.setAlignment(1, row, "left");
      table.setAlignment(2, row, "right");
    }



    if (isInPermissionGroup) {
      ++row;
      table.setRowColor(row, super.GRAY);
      table.add(new SubmitButton(_iwrb.getImage("buttons/new.gif"),this.sAction, this.parameterNewUser),1,row);
    }

      form.add(table);
    return form;
  }

  private Form getUserCreation(IWContext iwc) throws SQLException, RemoteException{
    return getUserCreation(iwc, -1);
  }
  private Form getUserCreation(IWContext iwc, int userId) throws SQLException, RemoteException{
    Form form = new Form();

    User user = null;
    if (userId != -1) {
      try {
      	user = ((com.idega.user.data.UserHome)com.idega.data.IDOLookup.getHomeLegacy(User.class)).findByPrimaryKey(new Integer(userId));
			}
			catch (FinderException e) {
				throw new SQLException(e.getMessage());
			}
    }

    Table table = new Table();
      form.add(table);
      table.setColor(super.WHITE);
      table.setCellpadding(1);
      table.setCellspacing(1);

    TextInput iName = new TextInput(this.parameterName);
    TextInput iLogin = new TextInput(this.parameterLogin);
    PasswordInput iPassOne = new PasswordInput(this.parameterPasswordOne);
    PasswordInput iPassTwo = new PasswordInput(this.parameterPasswordTwo);
    CheckBox iAdmin = new CheckBox(this.parameterAdministrator);
      iAdmin.setChecked(false);

    if (user != null) {
      iName.setContent(user.getName());
      LoginTable lt = LoginDBHandler.getUserLogin(userId);
      if (lt != null) {
        iLogin.setContent(lt.getUserLogin());
      }else {
        iLogin.setContent("error");
      }
      iAdmin.setChecked(super.isInPermissionGroup(iwc, user));
      form.addParameter(this.paramaterUserID, userId);
    }


    Text tName = getText(_iwrb.getLocalizedString("travel.name","Name"));
    Text tLogin = getText(_iwrb.getLocalizedString("travel.user_name","User name"));
    Text tPassword = getText(_iwrb.getLocalizedString("travel.password","Password"));
    Text tPassword2= getText(_iwrb.getLocalizedString("travel.retype","Retype"));
    Text tAdmin = getText(_iwrb.getLocalizedString("travel.administrator","Administrator"));

    SubmitButton btnSave = new SubmitButton(_iwrb.getImage("buttons/save.gif"), this.sAction, this.parameterSaveUser);
//    ResetButton clrButton = new ResetButton(_iwrb.getImage("buttons/delete.gif"));

    int row = 1;
    table.add(this.getTextWhite(_iwrb.getLocalizedString("travel.new_user","New user")),1,row);
    table.setAlignment(1, row, "center");
    table.mergeCells(1,row,2,row);
    table.setRowColor(row, super.backgroundColor);

    ++row;
    table.add(tName, 1, row);
    table.add(iName, 2, row);
    table.setRowColor(row, super.GRAY);

    ++row;
    table.setRowColor(row, super.GRAY);
    table.add(tLogin, 1, row);
    table.add(iLogin, 2, row);

    ++row;
    table.setRowColor(row, super.GRAY);
    table.setVerticalAlignment(1, row, "top");
    table.add(tPassword, 1, row);
    table.add(iPassOne, 2, row);

    ++row;
    table.setRowColor(row, super.GRAY);
    table.setVerticalAlignment(1, row, "top");
    table.add(tPassword2, 1, row);
    table.add(iPassTwo, 2, row);

    ++row;
    table.add(tAdmin,1,row);
    table.add(iAdmin,2,row);
    table.setRowColor(row, super.GRAY);

    ++row;
    table.setRowColor(row, super.GRAY);
//    table.add(clrButton, 1, row);
    table.add(btnSave, 2, row);
    table.setAlignment(2, row, "right");

    return form;
  }

  private Form saveUser(IWContext iwc) {
    String name = iwc.getParameter(this.parameterName);
    String login = iwc.getParameter(this.parameterLogin);
    String passOne = iwc.getParameter(this.parameterPasswordOne);
    String  passTwo = iwc.getParameter(this.parameterPasswordTwo);
    String admin = iwc.getParameter(this.parameterAdministrator);

    String userId = iwc.getParameter(this.paramaterUserID);

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
      if (!login.equals(""))
        inUse = LoginDBHandler.isLoginInUse(login);
        if (userId != null) {
          LoginTable lt = LoginDBHandler.getUserLogin(Integer.parseInt(userId));
          if (lt != null) {
            if (lt.getUserLogin().equals(login)) {
              inUse = false;
            }
          }
        }
    }

    if (!inUse && passwordsOK) {
      try {
      	UserBusiness ub = (UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class);
//        UserBusiness ub = new UserBusiness();
        User user = null;

        boolean isAdmin = false;
        if (admin != null) isAdmin = true;

        if (userId == null) {
          user = ub.insertUser(name, "", "", name, "staff", null, null, null);
          LoginDBHandler.createLogin(user.getID(), login, passOne);
        }else {
          user = ((com.idega.user.data.UserHome)com.idega.data.IDOLookup.getHome(User.class)).findByPrimaryKey( new Integer(userId));
          ub.updateUser(user, name, "", "", name, "staff", null, null, null, null);
          if (passwordUpdate) {
            LoginTable lt = LoginDBHandler.getUserLogin(Integer.parseInt(userId));
            if (lt == null) {
              LoginDBHandler.createLogin(user.getID(), login, passOne);
            }else {
              LoginDBHandler.updateLogin(user.getID(), login, passOne);
            }
          }
          removeUserFromAllGroups(user);
        }



        if (_reseller != null) {
        	getResellerManager(iwc).addUser(_reseller, user, isAdmin);
        }
        if (_supplier != null) {
        	getSupplierManagerBusiness(iwc).addUser(_supplier, user, isAdmin);
        }

        return getSuccessForm(iwc);
      }catch (Exception e) {
        e.printStackTrace(System.err);
        return getErrorForm(iwc, inUse, passwordsOK, false);
      }
    }else {
      return getErrorForm(iwc, inUse, passwordsOK, false);
    }

  }

  private Form getErrorForm(IWContext iwc, boolean loginInUse, boolean passwordsOK, boolean userCreated) {
    Form form = new Form();
    Table table = new Table();
      form.add(table);

    int row = 1;
    table.add(getTextWhite(_iwrb.getLocalizedString("travel.error","Error")),1,row);

    if (loginInUse) {
      ++row;
      table.add(getTextWhite(_iwrb.getLocalizedString("travel.login_in_use","Login in use")), 1, row);
    }

    if (!passwordsOK) {
      ++row;
      table.add(getTextWhite(_iwrb.getLocalizedString("travel.passwords_not_the_same","Password not the same")), 1, row);
    }

//    if (!userCreated) {
//      ++row;
//      table.add(getTextWhite(_iwrb.getLocalizedString("travel.user_not_created","User not created")), 1, row);
//    }

    ++row;
    ++row;
    Link backLink = new Link(_iwrb.getImage("buttons/back.gif"));
      backLink.setOnClick("history.go(-1)");
    table.add(backLink, 1,row);

    return form;
  }

  private Form getSuccessForm(IWContext iwc) throws RemoteException{
    return getUserList(iwc);
  }

  private Form updateUser(IWContext iwc) throws SQLException, RemoteException{
    try {
      String userId = iwc.getParameter(this.paramaterUserID);
      return getUserCreation(iwc, Integer.parseInt(userId));
    }catch (Exception e) {
      e.printStackTrace(System.err);
      return getUserCreation(iwc);
    }
  }

  private Form deleteUser(IWContext iwc) throws RemoteException {
    String userId = iwc.getParameter(this.paramaterUserID);
    try {
      User user = ((com.idega.user.data.UserHome)com.idega.data.IDOLookup.getHome(User.class)).findByPrimaryKey( new Integer(userId));
      com.idega.core.accesscontrol.business.LoginDBHandler.deleteUserLogin(user.getID());
      removeUserFromAllGroups(user);
      add(getText(_iwrb.getLocalizedString("travel.operation_successful","Operation successful")));
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
      add(getText(_iwrb.getLocalizedString("travel.operation_failed","Operation failed")));
    }
		catch (IDOLookupException e) {
			e.printStackTrace();
      add(getText(_iwrb.getLocalizedString("travel.operation_failed","Operation failed")));
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
      add(getText(_iwrb.getLocalizedString("travel.operation_failed","Operation failed")));
		}
		catch (FinderException e) {
			e.printStackTrace();
      add(getText(_iwrb.getLocalizedString("travel.operation_failed","Operation failed")));
		}
    return getUserList(iwc);
  }

  public static void removeUserFromAllGroups(User user) throws SQLException{
    List groups = com.idega.core.user.business.UserBusiness.getUserGroups(user);
    if (groups != null) {
      GenericGroup group;
      for (int i = 0; i < groups.size(); i++) {
        group = (GenericGroup) groups.get(i);
        group.removeUser(user);
      }
    }
  }

  public void maintainParameter(String parameterName, String parameterValue) {
    parameterNames.add(parameterName);
    parameterValues.add(parameterValue);
  }

}
