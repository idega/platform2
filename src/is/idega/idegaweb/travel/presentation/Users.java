package is.idega.idegaweb.travel.presentation;

import com.idega.presentation.IWContext;
import com.idega.idegaweb.*;
import com.idega.block.trade.stockroom.data.*;
import com.idega.block.trade.stockroom.business.*;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.core.accesscontrol.business.*;
import com.idega.core.user.business.UserBusiness;
import com.idega.core.user.data.*;
import java.sql.SQLException;
/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class Users extends TravelManager {

  private Reseller _reseller;
  private Supplier _supplier;

  private IWResourceBundle _iwrb;
  private IWBundle _bundle;

  private String sAction = "usersAction";
  private String parameterSaveUser = "usersSaveUser";

  private String parameterName = "usersName";
  private String parameterLogin = "usersLogin";
  private String parameterPasswordOne = "usersPasswordOne";
  private String parameterPasswordTwo = "usersPasswordTwo";


  public Users(IWContext iwc) throws Exception{
    main(iwc);
  }

  public void main(IWContext iwc) throws Exception{
    super.main(iwc);
    init();
/*    if (!handleInsert(iwc)) {
      Form form = getUserCreation(iwc);
      if (form == null) {
        System.err.println("Form == null");
      }else {
        System.err.println("Form != null");
      }
      add(form);
    }
*/
  }

  private void init(){
    _reseller = super.getReseller();
    _supplier = super.getSupplier();

    _iwrb = super.getResourceBundle();
    _bundle = super.getBundle();
  }


  public Form handleInsert(IWContext iwc) {
    String action = iwc.getParameter(this.sAction);
    if (action == null) {
      return getUserCreation(iwc);
    }else if (action.equals(this.parameterSaveUser)) {
      return saveUser(iwc);
    }else {
      return null;
    }

  }

  private Text getText(String content) {
    Text text = (Text) super.theText.clone();
      text.setText(content);
    return text;
  }

  private Form getUserCreation(IWContext iwc) {
    Form form = new Form();

    Table table = new Table();
      form.add(table);
      table.setColor(super.WHITE);
      table.setCellpadding(1);
      table.setCellspacing(1);

    TextInput iName = new TextInput(this.parameterName);
    TextInput iLogin = new TextInput(this.parameterLogin);
    PasswordInput iPassOne = new PasswordInput(this.parameterPasswordOne);
    PasswordInput iPassTwo = new PasswordInput(this.parameterPasswordTwo);

    Text tName = getText(_iwrb.getLocalizedString("travel.name","Name"));
    Text tLogin = getText(_iwrb.getLocalizedString("travel.user_name","User name"));
    Text tPassword = getText(_iwrb.getLocalizedString("travel.password","Password"));

    SubmitButton btnSave = new SubmitButton(_iwrb.getImage("buttons/save.gif"), this.sAction, this.parameterSaveUser);
//    ResetButton clrButton = new ResetButton(_iwrb.getImage("buttons/delete.gif"));

    int row = 1;
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
//    table.add(Text.BREAK, 2, row);
    table.add(iPassTwo, 2, row);

    ++row;
    table.setRowColor(row, super.GRAY);
//    table.add(clrButton, 1, row);
    table.add(btnSave, 2, row);

    return form;
  }

  private Form saveUser(IWContext iwc) {
    String name = iwc.getParameter(this.parameterName);
    String login = iwc.getParameter(this.parameterLogin);
    String passOne = iwc.getParameter(this.parameterPasswordOne);
    String  passTwo = iwc.getParameter(this.parameterPasswordTwo);

    boolean inUse = true;
    boolean passwordsOK = false;

    if (passOne != null && passTwo != null && !passOne.equals("") && passOne.equals(passTwo)) {
      passwordsOK = true;
    }

    if (login != null) {
      if (!login.equals(""))
        inUse = LoginDBHandler.isLoginInUse(login);
    }

    if (!inUse && passwordsOK) {
      try {
        UserBusiness ub = new UserBusiness();
        User user = ub.insertUser(name, "", "", name, "staff", null, null, null);
        LoginDBHandler.createLogin(user.getID(), login, passOne);

        if (_reseller != null) {
          ResellerManager.addUser(_reseller, user);
        }
        if (_supplier != null) {
          SupplierManager.addUser(_supplier, user);
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
    table.add(getText(_iwrb.getLocalizedString("travel.error","Error")),1,row);

    if (loginInUse) {
      ++row;
      table.add(getText(_iwrb.getLocalizedString("travel.login_in_use","Login in use")), 1, row);
    }

    if (!passwordsOK) {
      ++row;
      table.add(getText(_iwrb.getLocalizedString("travel.passwords_not_the_save","Password not the save")), 1, row);
    }

    if (!userCreated) {
      ++row;
      table.add(getText(_iwrb.getLocalizedString("travel.user_not_created","User not created")), 1, row);
    }

    return form;
  }

  private Form getSuccessForm(IWContext iwc) {
    Form form = new Form();
    Table table = new Table();
      form.add(table);

    int row = 1;
    table.add(getText(_iwrb.getLocalizedString("travel.user_created","User created")), 1, row);


    return form;
  }

}