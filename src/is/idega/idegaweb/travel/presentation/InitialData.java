package is.idega.travel.presentation;

import com.idega.jmodule.object.JModuleObject;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.jmodule.object.textObject.Text;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.block.trade.stockroom.data.*;
import com.idega.core.accesscontrol.business.AccessControl;
import com.idega.projects.nat.business.NatBusiness;
import java.sql.SQLException;


/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class InitialData extends JModuleObject {

  private  TravelManager tm;

  private IWBundle bundle;
  private IWResourceBundle iwrb;

  String tableBackgroundColor = "#FFFFFF";

  Text theText = new Text();
  Text theBoldText = new Text();
  Text smallText = new Text();
  Text theSmallBoldText = new Text();

  public InitialData() {
  }

  public void add(ModuleObject mo) {
    tm.add(mo);
  }

  public String getBundleIdentifier(){
    return TravelManager.IW_BUNDLE_IDENTIFIER;
  }

  public void main(ModuleInfo modinfo) throws SQLException {
      initialize(modinfo);
      String action = modinfo.getParameter("supplier_action");
      if (action == null) {action = "";}

      if (action.equals("")) {
          displayForm(modinfo);
      }else if (action.equals("create")) {
          createSupplier(modinfo);
      }

      tm.addBreak();
      super.add(tm);
  }

  public void initialize(ModuleInfo modinfo) {
      tm = new TravelManager();
      bundle = getBundle(modinfo);
      iwrb = bundle.getResourceBundle(modinfo.getCurrentLocale());
      theText.setFontSize(Text.FONT_SIZE_10_HTML_2);
      theText.setFontFace(Text.FONT_FACE_VERDANA);
      theText.setFontColor(NatBusiness.textColor);
      theBoldText.setFontSize(Text.FONT_SIZE_10_HTML_2);
      theBoldText.setFontFace(Text.FONT_FACE_VERDANA);
      theBoldText.setBold();
      theBoldText.setFontColor(NatBusiness.textColor);
      smallText.setFontSize(Text.FONT_SIZE_7_HTML_1);
      smallText.setFontFace(Text.FONT_FACE_VERDANA);
      smallText.setFontColor(NatBusiness.textColor);
      theSmallBoldText.setFontFace(Text.FONT_FACE_VERDANA);
      theSmallBoldText.setFontSize(Text.FONT_SIZE_7_HTML_1);
      theSmallBoldText.setBold();
      theSmallBoldText.setFontColor(NatBusiness.textColor);
  }

  public void displayForm(ModuleInfo modinfo) throws SQLException {

      String action = modinfo.getParameter("admin_action");
        if (action == null) action = "";

      Form form = new Form();
      ShadowBox sb = new ShadowBox();
        form.add(sb);
        sb.setWidth("90%");
        sb.setAlignment("center");



        if (AccessControl.isAdmin(modinfo)) {

            if (action.equals("")) {
              sb.add(selectSupplier(modinfo));
            }
            else if (action.equals("new")) {
              sb.add(getSupplierCreation(-1));
            }
            else if (action.equals("edit")) {
              sb.add(getSupplierCreation(Integer.parseInt(modinfo.getParameter("SR_SUPPLIER"))));
            }
        }

      int row = 0;
      add(Text.getBreak());
      add(form);
  }


  public Table selectSupplier(ModuleInfo modinfo) throws SQLException {
      Table table = new Table();
        table.setBorder(1);

      Text suppText = (Text) theBoldText.clone();
        suppText.setText(iwrb.getLocalizedString("travel.suppliers","Suppliers"));

      DropdownMenu suppliers = new DropdownMenu(new Supplier().findAllOrdered(Supplier.getColumnNameName()));


      SubmitButton newSupplier = new SubmitButton("new","admin_action","new");
      SubmitButton editSuppliers = new SubmitButton("edit","admin_action","edit");


      table.add(suppText,1,1);
      table.add(suppliers,1,2 );
      table.mergeCells(1,1,2,1);
      table.mergeCells(1,2,2,2);

      table.add(newSupplier,1,3);
      table.add(editSuppliers,2,3);


      return table;
  }


  public Table getSupplierCreation(int supplier_id) throws SQLException{
      Table table = new Table(2,8);
        table.setColumnAlignment(1,"right");
        table.setColumnAlignment(2,"left");

      int row = 1;
      Supplier supplier = null;

      Text newSupplierText = (Text) theBoldText.clone();
          newSupplierText.setText(iwrb.getLocalizedString("travel.new_supplier","New supplier"));

      Text nameText = (Text) theBoldText.clone();
          nameText.setText(iwrb.getLocalizedString("travel.name","Name"));
          nameText.addToText(":");

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

      int inputSize = 40;

      TextInput name = new TextInput("supplier_name");
        name.setSize(inputSize);
      TextInput address = new TextInput("supplier_address");
        address.setSize(inputSize);
      TextInput phone = new TextInput("supplier_phone");
        phone.setSize(inputSize);
      TextInput fax = new TextInput("supplier_fax");
        fax.setSize(inputSize);
      TextInput email = new TextInput("supplier_email");
        email.setSize(inputSize);

/*
      if (supplier_id != -1) {
        table.add(new HiddenInput("supplier_id",Integer.toString(supplier_id)));

        supplier = new Supplier(supplier_id);
          name.setContent(supplier.getName());

          Address[] addresses = supplier.getAddress();
          if (addresses.length > 0) {
              String namer = addresses[0].getStreetName();
              String number = addresses[0].getStreetNumber();
              if (number == null) {
                  address.setContent(namer);
              }else {
                  address.setContent(namer+" "+number);
              }
          }

          List phones = supplier.getHomePhone();
          if (phones != null) {
              Phone phone1 = (Phone) phones.get(0);
              phone.setContent(phone1.getNumber());
          }

          phones = supplier.getFaxPhone();
          if (phones != null) {
              Phone phone2 = (Phone) phones.get(0);
              fax.setContent(phone2.getNumber());
          }

      }
*/


      SubmitButton submit = new SubmitButton(iwrb.getImage("buttons/save.gif"),"supplier_action","create");
      BackButton back = new BackButton("Til baka");

      table.mergeCells(1,1,2,1);
      table.setAlignment(1,1,"center");
      table.add(newSupplierText,1,1);

      table.add(nameText,1,3);
      table.add(addressText,1,4);
      table.add(phoneText,1,5);
      table.add(faxText,1,6);
      table.add(emailText,1,7);

      table.add(name,2,3);
      table.add(address,2,4);
      table.add(phone,2,5);
      table.add(fax,2,6);
      table.add(email,2,7);

      table.setAlignment(1,8,"left");
      table.add(back,1,8);
      table.setAlignment(2,8,"right");
      table.add(submit,2,8);


      return table;
  }

  public void createSupplier(ModuleInfo modinfo)  {
      try {
          String name = modinfo.getParameter("supplier_name");
          String address = modinfo.getParameter("supplier_address");
          String phone = modinfo.getParameter("supplier_phone");
          String fax = modinfo.getParameter("supplier_fax");
          String email = modinfo.getParameter("supplier_email");
          String supplier_id = modinfo.getParameter("supplier_id");
      }
      catch (Exception sql) {
        sql.printStackTrace(System.err);
      }
  }



}