package is.idega.travel.presentation;

import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.idegaweb.*;
import is.idega.travel.business.TravelStockroomBusiness;
import java.util.*;
import com.idega.core.data.*;
import com.idega.block.trade.stockroom.data.*;
import com.idega.block.trade.stockroom.business.*;
import java.sql.SQLException;
import is.idega.travel.data.*;
import com.idega.util.idegaTimestamp;
import com.idega.projects.nat.business.NatBusiness;
/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class Contracts extends TravelManager {
  private IWBundle bundle;
  private IWResourceBundle iwrb;

  private Supplier supplier;
  private TravelStockroomBusiness tsb = TravelStockroomBusiness.getNewInstance();

  private String sAction = "contractAction";
  private String parameterAssignReseller = "contractAssignReseller";
  private String parameterResellerId = "contractResellerId";
  private String parameterNewReseller = "contractNewReseller";
  private String parameterSaveNewReseller = "contractSaveNewReseller";

  Reseller[] resellers = {};


  public Contracts() {
  }

  public void main(ModuleInfo modinfo) throws SQLException{
    super.main(modinfo);
    initialize(modinfo);

    String action = modinfo.getParameter(this.sAction);
        if (action == null) action = "";

    if (action.equals("")) {
      mainMenu(modinfo);
    }else if (action.equals(this.parameterNewReseller)) {
      resellerCreation();
    }else if (action.equals(this.parameterSaveNewReseller)) {
      saveReseller(modinfo);
    }else if (action.equals(this.parameterAssignReseller)) {
      assignReseller(modinfo);
    }



  }

  public void initialize(ModuleInfo modinfo) {
      bundle = super.getBundle();
      iwrb = super.getResourceBundle();
      supplier = super.getSupplier();

      resellers = tsb.getResellers(supplier.getID(),Reseller.getColumnNameName());

  }

  public void mainMenu(ModuleInfo modinfo) {
      Form form = new Form();
      ShadowBox sb = new ShadowBox();
        sb.add(form);
        sb.setWidth("90%");

      Table table = new Table();
        form.add(new SubmitButton("T - new reseller", this.sAction,this.parameterNewReseller));
        form.add(table);
        table.setAlignment("center");
        table.setWidth("80%");

      int row = 0;

      Text resName;
      Text refNum;
      Link assign;

      ++row;
      resName = (Text) theText.clone();
        resName.setBold();
        resName.setText(iwrb.getLocalizedString("travel.name","Name"));
      refNum = (Text) theText.clone();
        refNum.setBold();
        refNum.setText(iwrb.getLocalizedString("travel.reference_number","Reference number"));
      table.add(resName,1,row);
      table.add(refNum,2,row);


      for (int i = 0; i < resellers.length; i++) {
          ++row;
          resName = (Text) theText.clone();
            resName.setText(resellers[i].getName());
          refNum = (Text) theText.clone();
            refNum.setText(resellers[i].getReferenceNumber());
          assign = new Link("T - assign");
            assign.addParameter(this.sAction,this.parameterAssignReseller);
            assign.addParameter(this.parameterResellerId,resellers[i].getID());

          table.add(resName,1,row);
          table.add(refNum,2,row);
          table.add(assign,3,row);
          table.setColor(1,row,NatBusiness.backgroundColor);
          table.setColor(2,row,NatBusiness.backgroundColor);

      }
//      table.add(resellers);

      add(sb);
  }

  public void resellerCreation() throws SQLException{
      ShadowBox sb = new ShadowBox();
        sb.setWidth("90%");
      Form form = new Form();
        sb.add(form);
      Table table = new Table();
        form.add(table);
        table.setAlignment("center");
        table.setColumnAlignment(1,"right");
        table.setColumnAlignment(2,"left");
        table.setBorder(0);

      int row = 0;

      Text newSupplierText = (Text) theBoldText.clone();
          newSupplierText.setText(iwrb.getLocalizedString("travel.new_reseller","New Reseller"));

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

      TextInput name = new TextInput("reseller_name");
        name.setSize(inputSize);
      TextArea description = new TextArea("reseller_description");
        description.setWidth(inputSize);
        description.setHeight(5);
      TextInput address = new TextInput("reseller_address");
        address.setSize(inputSize);
      TextInput phone = new TextInput("reseller_phone");
        phone.setSize(inputSize);
      TextInput fax = new TextInput("reseller_fax");
        fax.setSize(inputSize);
      TextInput email = new TextInput("reseller_email");
        email.setSize(inputSize);
      TextInput userName = new TextInput("reseller_user_name");
        userName.setAsNotEmpty(iwrb.getLocalizedString("travel.a_username_must_be_selected","Verður að velja notendanafn"));
      PasswordInput passOne = new PasswordInput("reseller_password_one");
        passOne.setAsNotEmpty("Gimmi flippar");
      PasswordInput passTwo = new PasswordInput("reseller_password_two");


      SubmitButton submit = new SubmitButton(iwrb.getImage("buttons/save.gif"),this.sAction,this.parameterSaveNewReseller);
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

      ++row;
      table.add(loginText,1,row);
      table.add(userName,2,row);

      ++row;
      table.add(passwordText,1,row);
      table.setVerticalAlignment(1,row,"top");
      table.add(passOne,2,row);
      table.addBreak(2,row);
      table.add(passTwo,2,row);

      ++row;
      table.setAlignment(1,row,"left");
      table.add(back,1,row);
      table.setAlignment(2,row,"right");
      table.add(submit,2,row);

      add(Text.getBreak());
      add(sb);
  }

  public void saveReseller(ModuleInfo modinfo)  {
      add(Text.getBreak());

      try {
          String name = modinfo.getParameter("reseller_name");
          String description = modinfo.getParameter("reseller_description");
          String address = modinfo.getParameter("reseller_address");
          String phone = modinfo.getParameter("reseller_phone");
          String fax = modinfo.getParameter("reseller_fax");
          String email = modinfo.getParameter("reseller_email");

          String userName = modinfo.getParameter("reseller_user_name");
          String passOne = modinfo.getParameter("reseller_password_one");
          String passTwo = modinfo.getParameter("reseller_password_one");
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

                ResellerManager resellerMan = new ResellerManager();
                Reseller reseller = resellerMan.createReseller(name, userName, passOne, description, addressIds, phoneIds, emailIds);
                reseller.addTo(supplier);

//                  tm.commit();
                add(iwrb.getLocalizedString("travel.reseller_created","Reseller was created"));
            }else {
                add("TEMP - PASSWORD not the same");

            }

      }
      catch (Exception sql) {
          add(iwrb.getLocalizedString("travel.supplier_not_created","Supplier was not created"));
        sql.printStackTrace(System.err);
      }
  }

  private void assignReseller(ModuleInfo modinfo) throws SQLException {
    String sResellerId = modinfo.getParameter(this.parameterResellerId);
    Reseller reseller = new Reseller(Integer.parseInt(sResellerId));

    ShadowBox sb = new ShadowBox();
      sb.setWidth("90%");
    Form form = new Form();
      sb.add(form);
    Table table = new Table();
      table.setAlignment("center");
      table.setWidth("80%");
      table.setBorder(1);
      form.add(table);

    Product[] products = tsb.getProducts(supplier.getID());
    int[] serviceDays;
    int row = 1;


    Text header = (Text) theBoldText.clone();
      header.setText(reseller.getName());
    table.add(header,1,row);
    table.mergeCells(1,row,4,row);

    Text tNumberOfSeatsPerTour = (Text) theText.clone();
      tNumberOfSeatsPerTour.setText(iwrb.getLocalizedString("travel.number_of_seats_per_tour","Number of seats per tour"));
    Text tWeekdays = (Text) theText.clone();
      tWeekdays.setText(iwrb.getLocalizedString("travel.weekdays","Weekdays"));
    Text tTimeframe = (Text) theText.clone();
      tTimeframe.setText(iwrb.getLocalizedString("travel.timeframe","Timeframe"));
    Text tValidUntil = (Text) theText.clone();
      tValidUntil.setText("T - valid until");
    Text tDaysBefore = (Text) theText.clone();
      tDaysBefore.setText("T - days before departure");
    Text tfFromText = (Text) theText.clone();
      tfFromText.setText(iwrb.getLocalizedString("travel.from","from"));
    Text tfToText = (Text) theText.clone();
      tfToText.setText(iwrb.getLocalizedString("travel.to","to"));



    Text pName;
    TextInput pAlot;
    DateInput pFrom = new DateInput("from");
    DateInput pTo = new DateInput("to");
    TextInput pDays;

    Timeframe timeframe;

    for (int i = 0; i < products.length; i++) {
        ++row;
        pName = (Text) theText.clone();
          pName.setText(products[i].getName());

        pAlot = new TextInput("alotment");
          pAlot.setSize(3);

        pDays = new TextInput("valid");
          pDays.setSize(3);

        serviceDays  = ServiceDay.getDaysOfWeek(products[i].getID());

        pFrom = new DateInput("from");
        pTo = new DateInput("to");
        try {
          timeframe = tsb.getTimeframe(products[i]);
          pFrom.setDate(new idegaTimestamp(timeframe.getFrom()).getSQLDate());
          pTo.setDate(new idegaTimestamp(timeframe.getTo()).getSQLDate());
        }catch (TravelStockroomBusiness.TimeframeNotFoundException tnfe) {
          tnfe.printStackTrace(System.err);
          timeframe = null;
        }catch (TravelStockroomBusiness.ServiceNotFoundException snfe) {
          snfe.printStackTrace(System.err);
          timeframe = null;
        }


        table.add(pName,1,row);
        table.add(tNumberOfSeatsPerTour,3,row);
        table.add(pAlot,3,row);

        ++row;
        table.add(tWeekdays,1,row);

        ++row;
        table.add(tTimeframe,1,row);
        table.mergeCells(3,row,4,row);
        table.add(tfFromText,2,row);
        table.add(pFrom,3,row);
        table.add(tfToText,3,row);
        table.add(pTo,3,row);

        ++row;
        table.add(tValidUntil,1,row);
        table.mergeCells(3,row,4,row);
        table.add(pDays,3,row);
        table.add(tDaysBefore,3,row);

    }
/*
      CheckBox allDays = new CheckBox("all_days");
      CheckBox mondays = new CheckBox("mondays");
      CheckBox tuesdays = new CheckBox("tuesdays");
      CheckBox wednesdays = new CheckBox("wednesdays");
      CheckBox thursdays = new CheckBox("thursdays");
      CheckBox fridays = new CheckBox("fridays");
      CheckBox saturdays = new CheckBox("saturdays");
      CheckBox sundays = new CheckBox("sundays");

      Table weekdayFixTable = new Table(9,2);
        weekdayFixTable.setCellpadding(0);
        weekdayFixTable.setCellspacing(1);
        weekdayFixTable.setWidth("350");
        weekdayFixTable.setColumnAlignment(1,"center");
        weekdayFixTable.setColumnAlignment(2,"center");
        weekdayFixTable.setColumnAlignment(3,"center");
        weekdayFixTable.setColumnAlignment(4,"center");
        weekdayFixTable.setColumnAlignment(5,"center");
        weekdayFixTable.setColumnAlignment(6,"center");
        weekdayFixTable.setColumnAlignment(7,"center");
        weekdayFixTable.setColumnAlignment(8,"center");
        weekdayFixTable.setColumnAlignment(9,"center");

        Text alld = (Text) smallText.clone();
            alld.setText(iwrb.getLocalizedString("travel.all_days","All"));
        Text mond = (Text) smallText.clone();
            mond.setText(iwrb.getLocalizedString("travel.mon","mon"));
        Text tued = (Text) smallText.clone();
            tued.setText(iwrb.getLocalizedString("travel.tue","tue"));
        Text wedd = (Text) smallText.clone();
            wedd.setText(iwrb.getLocalizedString("travel.wed","wed"));
        Text thud = (Text) smallText.clone();
            thud.setText(iwrb.getLocalizedString("travel.thu","thu"));
        Text frid = (Text) smallText.clone();
            frid.setText(iwrb.getLocalizedString("travel.fri","fri"));
        Text satd = (Text) smallText.clone();
            satd.setText(iwrb.getLocalizedString("travel.sat","sat"));
        Text sund = (Text) smallText.clone();
            sund.setText(iwrb.getLocalizedString("travel.sun","sun"));


        weekdayFixTable.add(alld,1,1);
        weekdayFixTable.add(mond,3,1);
        weekdayFixTable.add(tued,4,1);
        weekdayFixTable.add(wedd,5,1);
        weekdayFixTable.add(thud,6,1);
        weekdayFixTable.add(frid,7,1);
        weekdayFixTable.add(satd,8,1);
        weekdayFixTable.add(sund,9,1);

        weekdayFixTable.add(allDays,1,2);
        weekdayFixTable.add(mondays,3,2);
        weekdayFixTable.add(tuesdays,4,2);
        weekdayFixTable.add(wednesdays,5,2);
        weekdayFixTable.add(thursdays,6,2);
        weekdayFixTable.add(fridays,7,2);
        weekdayFixTable.add(saturdays,8,2);
        weekdayFixTable.add(sundays,9,2);

      Text weekdaysText = (Text) theBoldText.clone();
          weekdaysText.setText(iwrb.getLocalizedString("travel.weekdays","Weekdays"));
      table.add(weekdaysText,1,row);
      table.add(weekdayFixTable,2,row);
*/



    add(Text.getBreak());
    add(sb);
  }
}