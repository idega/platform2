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
import is.idega.travel.data.HotelPickupPlace;


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

  private TravelStockroomBusiness tsb = TravelStockroomBusiness.getNewInstance();

  private Supplier supplier;
  String tableBackgroundColor = "#FFFFFF";

  private static String sAction = "admin_action";
  private static String parameterEdit = "edit";
  private static String parameterNew = "new";
  private static String parameterInvalidate = "invalidate";
  private static String parameterChoose = "InitialDataChooseSupplier";


  private static String parameterSupplierId = "supplier_id";

  private static String supplierView = "supplierView";
  private static String parameteViewSupplierInfo = "supplierViewInfo";
  private static String parameteViewHotelPickup = "parameteViewHotelPickup";
  private static String parameteViewPriceCategories = "parameteViewPriceCategories";

  private static String parameterSavePriceCategories = "parameterSavePriceCategories";
  private static String parameterPriceCategoryId = "parameterPriceCategoryId";

  public InitialData() {
  }

  public void add(PresentationObject mo) {
    super.add(mo);
  }

  public String getBundleIdentifier(){
    return super.IW_BUNDLE_IDENTIFIER;
  }

  public void main(IWContext iwc) throws Exception{
      super.main(iwc);
      initialize(iwc);

      if (super.isLoggedOn(iwc)) {
          String action = iwc.getParameter("supplier_action");
          if (action == null) {action = "";}

          if (action.equals("")) {
              displayForm(iwc);
          }else if (action.equals("create")) {
              createSupplier(iwc);
          }else if (action.equals("update")) {
              updateSupplier(iwc);
          }

          super.addBreak();
      }else {
        add(super.getLoggedOffTable(iwc));
      }
  }

  public void initialize(IWContext iwc) {
      bundle = super.getBundle();
      iwrb = super.getResourceBundle();

      supplier = super.getSupplier();
  }

  private Form getSupplierDropdownForm(IWContext iwc) {
    Form form = new Form();
    Table table = new Table(1,1);
      table.setWidth("90%");
      form.add(table);

      DropdownMenu menu = new DropdownMenu(this.supplierView);
        menu.addMenuElement(this.parameteViewSupplierInfo,iwrb.getLocalizedString("travel.supplier_information","Supplier information"));
        menu.addMenuElement(this.parameteViewHotelPickup,iwrb.getLocalizedString("travel.hotel_pickup_places","Hotel pick-up places"));
        menu.addMenuElement(this.parameteViewPriceCategories,iwrb.getLocalizedString("travel.price_categories","Price categories"));
      menu.setToSubmit();

      String selected = iwc.getParameter(this.supplierView);
      if (selected != null) {
        menu.setSelectedElement(selected);
      }

      table.add(menu);
    return form;
  }

  public void displayForm(IWContext iwc) throws SQLException {
      add(Text.getBreak());

      String action = iwc.getParameter(this.sAction);
        if (action == null) action = "";

        if (supplier != null) {
            add(getSupplierDropdownForm(iwc));
            String selected = iwc.getParameter(this.supplierView);
            if (selected == null)  selected = this.parameteViewSupplierInfo;
            Form form = null;
            if (selected.equals(this.parameteViewSupplierInfo)) {
              form = getSupplierCreation(supplier.getID());
            }else if (selected.equals(this.parameteViewHotelPickup)) {
              try {
                HotelPickupPlaceDesigner.handleInsert(iwc,supplier);
                HotelPickupPlaceDesigner hppd = new HotelPickupPlaceDesigner(iwc);
                form = hppd.getHotelPickupPlaceForm(supplier.getID());
              }catch (Exception e) {
                e.printStackTrace(System.err);
                form = new Form();
              }
            }else if (selected.equals(this.parameteViewPriceCategories)) {
              if (action.equals(this.parameterSavePriceCategories)) {
                this.savePriceCategories(iwc);
              }
              form = getPriceCategories(supplier.getID());
            }else {form = new Form();}


            form.maintainParameter(this.supplierView);
            add(form);
        }
        else {
            if (action.equals("")) {
              Table extra = new Table();
                extra.setWidth("90%");
                extra.setAlignment(1,1,"left");
                extra.setAlignment("center");
              Link newSupplier = new Link(iwrb.getImage("buttons/new.gif"));
                newSupplier.addParameter("admin_action","new");
              extra.add(newSupplier,1,1);
              add(extra);
              add(selectSupplier(iwc));
            }
            else if (action.equals(this.parameterNew)) {
              add(getSupplierCreation(-1));
            }
            else if (action.equals(this.parameterEdit)) {
              add(getSupplierCreation(Integer.parseInt(iwc.getParameter(Supplier.getSupplierTableName()))));
            }
            else if (action.equals(this.parameterInvalidate)) {
              String supplier_id = iwc.getParameter(Supplier.getSupplierTableName());
              if (supplier_id != null)
              try {
                SupplierManager.invalidateSupplier(new Supplier(Integer.parseInt(supplier_id)));
              }catch (Exception e) {
                e.printStackTrace(System.err);
                add(iwrb.getLocalizedString("travel.supplier_was_not_deleted","Supplier was not deleted"));
              }

              add(selectSupplier(iwc));
            }
        }


      int row = 0;
  }


  public Table selectSupplier(IWContext iwc) throws SQLException {
      Table table = new Table();
        table.setBorder(0);
        table.setCellspacing(1);
        table.setColor(super.WHITE);
        table.setWidth("90%");

      int row=1;

      Link editLink = new Link(iwrb.getImage("buttons/change.gif"));
        editLink.addParameter(this.sAction, this.parameterEdit);

      Link deleteLink = new Link(iwrb.getImage("buttons/delete.gif"));
        deleteLink.addParameter(this.sAction, this.parameterInvalidate);

      Link chooseLink = new Link(iwrb.getImage("buttons/use.gif"));
        chooseLink.addParameter(this.sAction, this.parameterChoose);


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
//      table.add(Text.NON_BREAKING_SPACE, 3, row);
              table.mergeCells(2,row,3,row);
      table.add(Text.NON_BREAKING_SPACE, 4, row);
      table.setRowColor(row, super.backgroundColor);

      Supplier[] supps = Supplier.getValidSuppliers();

      String theColor = super.GRAY;

      for (int i = 0; i < supps.length; i++) {
        ++row;
//        theColor = super.getNextZebraColor(super.GRAY, super.WHITE, theColor);

        link = (Link) editLink.clone();
          link.addParameter(Supplier.getSupplierTableName(),supps[i].getID());
        table.add(link,4,row);
        table.add(Text.NON_BREAKING_SPACE,4,row);
        link = (Link) chooseLink.clone();
          link.addParameter(Supplier.getSupplierTableName(),supps[i].getID());
        table.add(link,4,row);
        table.add(Text.NON_BREAKING_SPACE,4,row);
        link = (Link) deleteLink.clone();
          link.addParameter(Supplier.getSupplierTableName(),supps[i].getID());
        table.add(link,4,row);
        table.setAlignment(4,row,"right");

        table.setRowColor(row, theColor);

        suppNameText = (Text) theText.clone();
          suppNameText.setText(supps[i].getName());
          suppNameText.setFontColor(super.BLACK);

        table.add(suppNameText,1,row);


        //pGroup = SupplierManager.getPermissionGroup(supps[i]);
        try { // skoða eftir mat.......
          //users = UserGroupBusiness.getUsersContained(pGroup);
          users = UserGroupBusiness.getUsersContained(new GenericGroup(supps[i].getGroupId()));
          if (users != null) {
            for (int j = 0; j < users.size(); j++) {
              if (j > 0) ++row;

              //table.setRowColor(row,super.backgroundColor);

              user = (User) users.get(j);
              logTable = LoginDBHandler.getUserLogin(user.getID());
              suppLoginText = (Text) theText.clone();
              suppLoginText.setText(logTable.getUserLogin());
              suppLoginText.setFontColor(super.BLACK);
              suppPassText = (Text) theText.clone();
              suppPassText.setText(logTable.getUserPassword());
              suppPassText.setFontColor(super.BLACK);

              table.add(suppLoginText,2,row);
              table.mergeCells(2,row,3,row);
            }

          }
        }catch (Exception e) {
          e.printStackTrace(System.err);
        }


      }


      return table;
  }


  public Form getSupplierCreation(int supplier_id) throws SQLException{
      Form form = new Form();

      Table table = new Table();
        form.add(table);
        table.setColumnAlignment(1,"right");
        table.setColumnAlignment(2,"left");
        table.setBorder(0);

      int row = 0;
      Supplier lSupplier = null;

      Text newSupplierText = (Text) theBoldText.clone();
        if (supplier_id == -1) newSupplierText.setText(iwrb.getLocalizedString("travel.new_supplier","New supplier"));
        else newSupplierText.setText(iwrb.getLocalizedString("travel.update_supplier_information","Update supplier information"));


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
        table.add(new HiddenInput(this.parameterSupplierId,Integer.toString(supplier_id)));

        lSupplier = new Supplier(supplier_id);
          name.setContent(lSupplier.getName());
          description.setContent(lSupplier.getDescription());

          Address addr = lSupplier.getAddress();
          if (addr != null) {
            String namer = addr.getStreetName();
            String number = addr.getStreetNumber();
            if (number == null) {
                address.setContent(namer);
            }else {
                address.setContent(namer+" "+number);
            }
          }

          List phones = lSupplier.getHomePhone();
          if (phones != null) {
            if (phones.size() > 0) {
              Phone phone1 = (Phone) phones.get(0);
              phone.setContent(phone1.getNumber());
            }
          }

          phones = lSupplier.getFaxPhone();
          if (phones != null) {
            if (phones.size() > 0) {
              Phone phone2 = (Phone) phones.get(0);
              fax.setContent(phone2.getNumber());
            }
          }

          Email eEmail = lSupplier.getEmail();
          if (eEmail != null) {
            email.setContent(eEmail.getEmailAddress());
          }
      }

      SubmitButton submit = null;
      if (supplier_id == -1) {
        submit = new SubmitButton(iwrb.getImage("buttons/save.gif"),"supplier_action","create");
      } else {
        submit = new SubmitButton(iwrb.getImage("buttons/update.gif"),"supplier_action","update");
      }
      BackButton back = new BackButton(iwrb.getImage("buttons/back.gif"));
      Link lBack = new Link(super.getBackLink());


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
        table.add(Text.NON_BREAKING_SPACE,1,row);
      ++row;
      if (supplier == null ) {
        table.setAlignment(1,row,"left");
        table.add(lBack,1,row);
      }
      table.setAlignment(2,row,"right");
      table.add(submit,2,row);


      return form;
  }

  public void updateSupplier(IWContext iwc) {
    String supplierId = iwc.getParameter(this.parameterSupplierId);
    try {
      createSupplier(iwc, Integer.parseInt(supplierId));
    }catch (NumberFormatException n) {}

  }

  public void createSupplier(IWContext iwc)  {
    createSupplier(iwc, -1);
  }

  public void createSupplier(IWContext iwc, int supplierId)  {
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
              if (supplierId != -1) isUpdate = true;


              if (isUpdate) {
                  Vector phoneIDS = new Vector();
                  Supplier supplier = new Supplier(supplierId);

                  Phone ph;
                  List phones = supplier.getPhones(Phone.getHomeNumberID());
                  if (phones != null) {
                    if (phones.size() > 0) {
                      for (int i = 0; i < phones.size(); i++) {
                        ph = (Phone) phones.get(i);
                          ph.setNumber(phone);
                        ph.update();
                        phoneIDS.add(new Integer(ph.getID()));
                      }
                    }else {
                      ph = new Phone();
                        ph.setNumber(phone);
                        ph.setPhoneTypeId(Phone.getHomeNumberID());
                      ph.insert();
                      phoneIDS.add(new Integer(ph.getID()));
                    }
                  }
                  phones = supplier.getPhones(Phone.getFaxNumberID());
                  if (phones != null) {
                    if (phones.size() > 0) {
                      for (int i = 0; i < phones.size(); i++) {
                        ph = (Phone) phones.get(i);
                          ph.setNumber(fax);
                        ph.update();
                        phoneIDS.add(new Integer(ph.getID()));
                      }
                    }else {
                      ph = new Phone();
                        ph.setNumber(fax);
                        ph.setPhoneTypeId(Phone.getFaxNumberID());
                      ph.insert();
                      phoneIDS.add(new Integer(ph.getID()));
                    }
                  }

                  int[] phoneIds = new int[phoneIDS.size()];
                  for (int i = 0; i < phoneIDS.size(); i++) {
                      phoneIds[i] = ((Integer) phoneIDS.get(i)).intValue() ;
                  }


                  Address addr = supplier.getAddress();
                    addr.setStreetName(address);
                  addr.update();

                  int[] addressIds = new int[1];
                  addressIds[0] = addr.getID();


                  Email eml = supplier.getEmail();
                    eml.setEmailAddress(email);
                  eml.update();

                  int[] emailIds = new int[1];
                  emailIds[0] = eml.getID();

                  SupplierManager suppMan = new SupplierManager();
                  supplier = suppMan.updateSupplier(supplierId,name, description, addressIds, phoneIds, emailIds);


                  add(iwrb.getLocalizedString("travel.information_updated","Information updated"));
                  this.displayForm(iwc);
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






  private Form getPriceCategories(int supplierId) {
    int extraRows = 3;

    Form form = new Form();

    Table table = new Table();
      form.add(table);
      table.setColor(super.WHITE);
      table.setCellspacing(1);

      PriceCategory[] categories = tsb.getPriceCategories(supplierId);
      Text nameTxt = (Text) theText.clone();
        nameTxt.setFontColor(super.WHITE);
        nameTxt.setBold();
        nameTxt.setText(iwrb.getLocalizedString("travel.name","Name"));

      Text onlineTxt = (Text)  theText.clone();
        onlineTxt.setFontColor(super.WHITE);
        onlineTxt.setBold();
        onlineTxt.setText(iwrb.getLocalizedString("travel.online","Online"));

      Text typeTxt = (Text)  theText.clone();
        typeTxt.setFontColor(super.WHITE);
        typeTxt.setBold();
        typeTxt.setText(iwrb.getLocalizedString("travel.type","Type"));

      Text discOfTxt = (Text)  theText.clone();
        discOfTxt.setFontColor(super.WHITE);
        discOfTxt.setBold();
        discOfTxt.setText(iwrb.getLocalizedString("travel.discount_of","Discount of"));

      Text deleteTxt = (Text)  theText.clone();
        deleteTxt.setFontColor(super.WHITE);
        deleteTxt.setBold();
        deleteTxt.setText(iwrb.getLocalizedString("travel.delete","Delete"));

      int row = 1;
      int counter = 0;

      table.add(nameTxt,2,row);
      table.add(onlineTxt,3,row);
      table.add(typeTxt,4,row);
      table.add(discOfTxt,5,row);
      table.add(deleteTxt,6,row);
      table.setRowColor(row,super.backgroundColor);

      Text numberTxt;
      TextInput nameInp;
      BooleanInput online;
      DropdownMenu ddType = new DropdownMenu("priceCategoryType");
        ddType.addMenuElement(PriceCategory.PRICETYPE_PRICE, iwrb.getLocalizedString("travel.price","Price"));
        ddType.addMenuElement(PriceCategory.PRICETYPE_DISCOUNT, iwrb.getLocalizedString("travel.discount","Discount"));
      DropdownMenu ddDisc = new DropdownMenu(categories,"priceCategoryParent");
        ddDisc.addMenuElementFirst("-1",Text.NON_BREAKING_SPACE);

      DropdownMenu ddOne;
      DropdownMenu ddTwo;

      CheckBox delete;

      for (int i = 0; i < categories.length; i++) {
        ++counter;
        ++row;
        numberTxt = (Text) super.smallText.clone();
          numberTxt.setFontColor(super.BLACK);
          numberTxt.setText(Integer.toString(counter));

        nameInp = new TextInput("priceCategoryName");
          nameInp.setContent(categories[i].getName());

        online = new BooleanInput("priceCategoryOnline");
          online.setSelected(categories[i].isNetbookingCategory());

        ddOne = (DropdownMenu) ddType.clone();
          ddOne.setSelectedElement(categories[i].getType());

        ddTwo = (DropdownMenu) ddDisc.clone();
          ddTwo.setSelectedElement(Integer.toString(categories[i].getParentId()));

        delete = new CheckBox("priceCategoryToDelete_"+categories[i].getID());

        table.add(new HiddenInput(this.parameterPriceCategoryId,Integer.toString(categories[i].getID())));
        table.add(numberTxt,1,row);
        table.add(nameInp,2,row);
        table.add(online,3,row);
        table.add(ddOne,4,row);
        table.add(ddTwo,5,row);
        table.add(delete,6,row);
        table.setRowColor(row,super.GRAY);
      }
      for (int i = 0; i < extraRows; i++) {
        ++counter;
        ++row;
        numberTxt = (Text) super.smallText.clone();
          numberTxt.setFontColor(super.BLACK);
          numberTxt.setText(Integer.toString(counter));
        nameInp = new TextInput("priceCategoryName");
        online = new BooleanInput("priceCategoryOnline");
          online.setSelected(true);
        ddOne = (DropdownMenu) ddType.clone();
        ddTwo = (DropdownMenu) ddDisc.clone();

        table.add(new HiddenInput(this.parameterPriceCategoryId,"-1"));
        table.add(numberTxt,1,row);
        table.add(nameInp,2,row);
        table.add(online,3,row);
        table.add(ddOne,4,row);
        table.add(ddTwo,5,row);
        table.setRowColor(row,super.GRAY);
      }
      ++row;
      table.setRowColor(row,super.GRAY);
      SubmitButton lSave = new SubmitButton(iwrb.getImage("buttons/save.gif"),this.sAction, this.parameterSavePriceCategories);
      table.mergeCells(1,row,6,row);
      table.setColumnAlignment(1,"center");
      table.setColumnAlignment(6,"center");
      table.setWidth(1,row-1,"15");
      table.setAlignment(1,row,"right");
      table.add(lSave,1,row);


      return form;
  }

  public void savePriceCategories(IWContext iwc) {
    String[] catIds = iwc.getParameterValues(this.parameterPriceCategoryId);
    String[] names  = iwc.getParameterValues("priceCategoryName");
    String[] online = iwc.getParameterValues("priceCategoryOnline");
    String[] type   = iwc.getParameterValues("priceCategoryType");
    String[] parent = iwc.getParameterValues("priceCategoryParent");


    try {
      for (int i = 0; i < catIds.length; i++) {
        if (catIds[i].equals("-1")) {   //NEW
          if ((names[i] != null) && (!names[i].equals(""))){
            int priceCategoryId = 0;
            int parentId;
            boolean bOnline;
            if (online[i].equals("Y")) {
                bOnline = true;
            }else {
              bOnline = false;
            }

            if (type[i].equals(PriceCategory.PRICETYPE_DISCOUNT)) {
              parentId = Integer.parseInt(parent[i]);
              priceCategoryId = tsb.createPriceCategory(supplier.getID(), names[i], "",type[i], "", bOnline, parentId);
            }else if (type[i].equals(PriceCategory.PRICETYPE_PRICE)) {
              priceCategoryId = tsb.createPriceCategory(supplier.getID(), names[i], "",type[i], "", bOnline);
            }
          }
        }else {   //UPDATE
          String del = iwc.getParameter("priceCategoryToDelete_"+catIds[i]);
          PriceCategory pCat = new PriceCategory(Integer.parseInt(catIds[i]));
          if (del != null) {
            pCat.delete();
          }else {
            boolean bOnline;
            if (online[i].equals("Y")) {
                bOnline = true;
            }else {
              bOnline = false;
            }

              pCat.setName(names[i]);
              pCat.setDescription("");
              pCat.setType(type[i]);
              if (type[i].equals(PriceCategory.PRICETYPE_DISCOUNT)) {
                pCat.setParentId(Integer.parseInt(parent[i]));
              }
              pCat.setSupplierId(supplier.getID());
              pCat.setExtraInfo("");
              pCat.isNetbookingCategory(bOnline);
            pCat.update();
          }
        }
      }




    }catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }


}