package is.idega.travel.presentation;

import com.idega.presentation.Block;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.text.*;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.block.trade.stockroom.data.*;
import com.idega.core.data.*;
import com.idega.core.accesscontrol.business.AccessControl;
import com.idega.block.trade.stockroom.business.*;
import is.idega.travel.business.TravelStockroomBusiness;
import com.idega.core.accesscontrol.data.PermissionGroup;
import com.idega.core.business.UserGroupBusiness;
import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.core.accesscontrol.data.LoginTable;
import com.idega.core.user.data.User;
import java.sql.SQLException;
import com.idega.core.accesscontrol.business.NotLoggedOnException;
import java.util.*;


/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class InitialData extends TravelManager {

  //private TravelManager tm;

  private IWBundle bundle;
  private IWResourceBundle iwrb;

  private Supplier supplier;
  String tableBackgroundColor = "#FFFFFF";

  public static String sAction = "admin_action";
  public static String parameterEdit = "edit";
  public static String parameterNew = "new";
  public static String parameterInvalidate = "invalidate";
  public static String parameterChoose = "InitialDataChooseSupplier";


  public InitialData() {
  }

  public void add(PresentationObject mo) {
    super.add(mo);
  }

  public String getBundleIdentifier(){
    return super.IW_BUNDLE_IDENTIFIER;
  }

  public void main(IWContext iwc) throws SQLException{
      super.main(iwc);
      initialize(iwc);

      String action = iwc.getParameter("supplier_action");
      if (action == null) {action = "";}

      if (action.equals("")) {
          displayForm(iwc);
      }else if (action.equals("create")) {
          createSupplier(iwc);
      }

      super.addBreak();
  }

  public void initialize(IWContext iwc) {
      bundle = super.getBundle();
      iwrb = super.getResourceBundle();

      supplier = super.getSupplier();
  }

  public void displayForm(IWContext iwc) throws SQLException {

      String action = iwc.getParameter(this.sAction);
        if (action == null) action = "";

      Form form = new Form();
      ShadowBox sb = new ShadowBox();
        form.add(sb);
        sb.setWidth("90%");
        sb.setAlignment("center");



        if (supplier != null) {
            sb.add(getSupplierCreation(supplier.getID()));
        }
        else {
            if (action.equals("")) {
              sb.add(selectSupplier(iwc));
            }
            else if (action.equals(this.parameterNew)) {
              sb.add(getSupplierCreation(-1));
            }
            else if (action.equals(this.parameterEdit)) {
              sb.add(getSupplierCreation(Integer.parseInt(iwc.getParameter(Supplier.getSupplierTableName()))));
            }
            else if (action.equals(this.parameterInvalidate)) {
              String supplier_id = iwc.getParameter(Supplier.getSupplierTableName());
              if (supplier_id != null)
              try {
                SupplierManager.invalidateSupplier(new Supplier(Integer.parseInt(supplier_id)));
              }catch (Exception e) {
                e.printStackTrace(System.err);
                sb.add("TEMP - henti ekki");
              }

              sb.add(selectSupplier(iwc));
            }
        }


      int row = 0;
      add(Text.getBreak());
      add(form);
  }


  public Table selectSupplier(IWContext iwc) throws SQLException {
      Table table = new Table();
        table.setBorder(0);
        table.setWidth("80%");

      int row=1;

      Link editLink = new Link(iwrb.getImage("buttons/change.gif"));
        editLink.addParameter(this.sAction, this.parameterEdit);

      Link deleteLink = new Link(iwrb.getImage("buttons/delete.gif"));
        deleteLink.addParameter(this.sAction, this.parameterInvalidate);

      Link chooseLink = new Link("T - use");
        chooseLink.addParameter(this.sAction, this.parameterChoose);

      Link loginEdit = new Link("t - Change login");
        loginEdit.setWindowToOpen(LoginChanger.class);


      Text suppText = (Text) theBoldText.clone();
        suppText.setText(iwrb.getLocalizedString("travel.suppliers","Suppliers"));
      Text suppLogin = (Text) theBoldText.clone();
        suppLogin.setText(iwrb.getLocalizedString("travel.user_name","User name"));
      Text suppPass = (Text) theBoldText.clone();
        suppPass.setText(iwrb.getLocalizedString("travel.password","Password"));


      Text suppNameText;
      Text suppLoginText;
      Text suppPassText;
      Link link;

      PermissionGroup pGroup;
      List users;
      User user;
      LoginTable logTable;

      table.add(suppText,1,row);
      table.add(suppLogin,2,row);

      Supplier[] supps = Supplier.getValidSuppliers();
      for (int i = 0; i < supps.length; i++) {
        ++row;

        link = (Link) editLink.clone();
          link.addParameter(Supplier.getSupplierTableName(),supps[i].getID());
        table.add(link,4,row);
        link = (Link) chooseLink.clone();
          link.addParameter(Supplier.getSupplierTableName(),supps[i].getID());
        table.add(link,4,row);
        link = (Link) deleteLink.clone();
          link.addParameter(Supplier.getSupplierTableName(),supps[i].getID());
        table.add(link,4,row);
        table.setAlignment(4,row,"right");

        suppNameText = (Text) theText.clone();
          suppNameText.setText(supps[i].getName());

        table.add(suppNameText,1,row);


        pGroup = SupplierManager.getPermissionGroup(supps[i]);
        try {
          users = UserGroupBusiness.getUsersContained(pGroup);
          if (users != null) {
            for (int j = 0; j < users.size(); j++) {
              if (j > 0) ++row;

              table.setRowColor(row,super.backgroundColor);

              user = (User) users.get(j);
              logTable = LoginDBHandler.findUserLogin(user.getID());
              suppLoginText = (Text) theText.clone();
              suppLoginText.setText(logTable.getUserLogin());
              suppPassText = (Text) theText.clone();
              suppPassText.setText(logTable.getUserPassword());

              table.add(suppLoginText,2,row);
              table.mergeCells(2,row,3,row);
            }

          }
        }catch (Exception e) {
          e.printStackTrace(System.err);
        }


      }

      ++row;
      SubmitButton newSupplier = new SubmitButton("new","admin_action","new");
      table.add(newSupplier,1,row);
      table.add(loginEdit,1,row);

      return table;
  }


  public Table getSupplierCreation(int supplier_id) throws SQLException{
      Table table = new Table();
        table.setColumnAlignment(1,"right");
        table.setColumnAlignment(2,"left");
        table.setBorder(1);

      int row = 0;
      Supplier supplier = null;

      Text newSupplierText = (Text) theBoldText.clone();
          newSupplierText.setText(iwrb.getLocalizedString("travel.new_supplier","New supplier"));

      Text nameText = (Text) theBoldText.clone();
          nameText.setText(iwrb.getLocalizedString("travel.name","Name"));
          nameText.addToText(":");

      Text descText = (Text) theBoldText.clone();
          descText.setText(iwrb.getLocalizedString("travel.Description","Description"));
          descText.addToText(":");

      Text addressText = (Text) theBoldText.clone();
          addressText.setText(iwrb.getLocalizedString("travel.address_long","Address"));
          addressText.addToText(":");

      Text phoneText = (Text) theBoldText.clone();
          phoneText.setText(iwrb.getLocalizedString("travel.telephone_number_lg","Telephone number"));
          phoneText.addToText(":");

      Text faxText = (Text) theBoldText.clone();
          faxText.setText(iwrb.getLocalizedString("travel.fax","Fax number"));
          faxText.addToText(":");

      Text emailText = (Text) theBoldText.clone();
          emailText.setText(iwrb.getLocalizedString("travel.email_lg","E-mail"));
          emailText.addToText(":");

      Text loginText = (Text) theBoldText.clone();
          loginText.setText(iwrb.getLocalizedString("travel.user_name","User name"));
          loginText.addToText(":");

      Text passwordText = (Text) theBoldText.clone();
          passwordText.setText(iwrb.getLocalizedString("travel.password","Password"));
          passwordText.addToText(":");

      int inputSize = 40;

      TextInput name = new TextInput("supplier_name");
        name.setSize(inputSize);
      TextArea description = new TextArea("supplier_description");
        description.setWidth(inputSize);
        description.setHeight(5);
      TextInput address = new TextInput("supplier_address");
        address.setSize(inputSize);
      TextInput phone = new TextInput("supplier_phone");
        phone.setSize(inputSize);
      TextInput fax = new TextInput("supplier_fax");
        fax.setSize(inputSize);
      TextInput email = new TextInput("supplier_email");
        email.setSize(inputSize);
      TextInput userName = new TextInput("supplier_user_name");
        userName.setAsNotEmpty(iwrb.getLocalizedString("travel.a_username_must_be_selected","Verður að velja notendanafn"));
      PasswordInput passOne = new PasswordInput("supplier_password_one");
        passOne.setAsNotEmpty("Gimmi flippar");
      PasswordInput passTwo = new PasswordInput("supplier_password_two");

      if (supplier_id != -1) {
        table.add(new HiddenInput("supplier_id",Integer.toString(supplier_id)));

        supplier = new Supplier(supplier_id);
          name.setContent(supplier.getName());
          description.setContent(supplier.getDescription());

          Address addr = supplier.getAddress();
          if (addr != null) {
            String namer = addr.getStreetName();
            String number = addr.getStreetNumber();
            if (number == null) {
                address.setContent(namer);
            }else {
                address.setContent(namer+" "+number);
            }
          }

          List phones = supplier.getHomePhone();
          if (phones != null) {
            if (phones.size() > 0) {
              Phone phone1 = (Phone) phones.get(0);
              phone.setContent(phone1.getNumber());
            }
          }

          phones = supplier.getFaxPhone();
          if (phones != null) {
            if (phones.size() > 0) {
              Phone phone2 = (Phone) phones.get(0);
              fax.setContent(phone2.getNumber());
            }
          }

          Email eEmail = supplier.getEmail();
          if (eEmail != null) {
            email.setContent(eEmail.getEmailAddress());
          }
      }

      SubmitButton submit = new SubmitButton(iwrb.getImage("buttons/save.gif"),"supplier_action","create");
      BackButton back = new BackButton("Til baka");


      ++row;
      table.mergeCells(1,row,2,row);
      table.setAlignment(1,row,"center");
      table.add(newSupplierText,1,row);

      ++row;
      ++row;
      table.add(nameText,1,row);
      table.add(name,2,row);

      ++row;
      table.add(descText,1,row);
      table.setVerticalAlignment(1,row,"top");
      table.add(description,2,row);

      ++row;
      table.add(addressText,1,row);
      table.add(address,2,row);

      ++row;
      table.add(phoneText,1,row);
      table.add(phone,2,row);

      ++row;
      table.add(faxText,1,row);
      table.add(fax,2,row);

      ++row;
      table.add(emailText,1,row);
      table.add(email,2,row);

      if (supplier_id == -1) {
        ++row;
        table.add(loginText,1,row);
        table.add(userName,2,row);

        ++row;
        table.add(passwordText,1,row);
        table.setVerticalAlignment(1,row,"top");
        table.add(passOne,2,row);
        table.addBreak(2,row);
        table.add(passTwo,2,row);
      }

      ++row;
      table.setAlignment(1,row,"left");
      table.add(back,1,row);
      table.setAlignment(2,row,"right");
      table.add(submit,2,row);


      return table;
  }

  public void createSupplier(IWContext iwc)  {
      add(Text.getBreak());
//      javax.transaction.TransactionManager tm = com.idega.transaction.IdegaTransactionManager.getInstance();

      try {
          String name = iwc.getParameter("supplier_name");
          String description = iwc.getParameter("supplier_description");
          String address = iwc.getParameter("supplier_address");
          String phone = iwc.getParameter("supplier_phone");
          String fax = iwc.getParameter("supplier_fax");
          String email = iwc.getParameter("supplier_email");
          String supplier_id = iwc.getParameter("supplier_id");

          String userName = iwc.getParameter("supplier_user_name");
          String passOne = iwc.getParameter("supplier_password_one");
          String passTwo = iwc.getParameter("supplier_password_one");


              boolean isUpdate = false;
              if (supplier != null) {
                isUpdate = true;
              }


              if (isUpdate) {
                  add("TEMP - Unimplemented");
              }
              else {
//                  tm.begin();
                  if (passOne.equals(passTwo)) {

                      Vector phoneIDS = new Vector();
                      if (phone.length() > 0) {
                        Phone phonePhone = new Phone();
                          phonePhone.setNumber(phone);
                          phonePhone.setPhoneTypeId(Phone.getHomeNumberID());
                        phonePhone.insert();
                        phoneIDS.add(new Integer(phonePhone.getID()));
                      }
                      if (fax.length() > 0) {
                        Phone faxPhone = new Phone();
                          faxPhone.setNumber(fax);
                          faxPhone.setPhoneTypeId(Phone.getFaxNumberID());
                        faxPhone.insert();
                        phoneIDS.add(new Integer(faxPhone.getID()));
                      }


                      int[] phoneIds = new int[phoneIDS.size()];
                      for (int i = 0; i < phoneIDS.size(); i++) {
                          phoneIds[i] = ((Integer) phoneIDS.get(i)).intValue() ;
                      }

                      int[] addressIds = new int[1];
                      Address addressAddress = new Address();
                          addressAddress.setStreetName(address);
                          addressAddress.insert();
                      addressIds[0] = addressAddress.getID();

                      int[] emailIds = new int[1];
                      Email eEmail = new Email();
                        eEmail.setEmailAddress(email);
                        eEmail.insert();
                      emailIds[0] = eEmail.getID();

                      SupplierManager suppMan = new SupplierManager();
                      Supplier supplier = suppMan.createSupplier(name, userName, passOne, description, addressIds, phoneIds, emailIds);


                      this.displayForm(iwc);
                  }else {
                      add("TEMP - PASSWORD not the same");

                  }
              }

      }
      catch (Exception sql) {
//        try {
//          tm.rollback();
          add(iwrb.getLocalizedString("travel.supplier_not_created","Supplier was not created"));
//        }
//        catch (javax.transaction.SystemException se) {
//          se.printStackTrace(System.err);
//        }
        sql.printStackTrace(System.err);
      }
  }



}