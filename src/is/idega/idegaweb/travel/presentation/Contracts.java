package is.idega.travel.presentation;

import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.idegaweb.*;
import is.idega.travel.business.TravelStockroomBusiness;
import java.util.*;
import com.idega.core.data.*;
import com.idega.block.trade.stockroom.data.*;
import com.idega.block.trade.stockroom.business.*;
import java.sql.SQLException;
import is.idega.travel.data.*;
import com.idega.util.idegaTimestamp;

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
  private String parameterProductId = "contractProductId";
  private String parameterSaveProductInfo = "contractSaveProductInfo";
  private String parameterNewReseller = "contractNewReseller";
  private String parameterEditReseller = "contractEditReseller";
  private String parameterContractId = "contractContractId";
  private String parameterSaveNewReseller = "contractSaveNewReseller";
  private String parameterUpdateReseller = "contractUpdateReseller";

  Reseller[] resellers = {};


  public Contracts() {
  }

  public void main(IWContext iwc) throws Exception{
    super.main(iwc);
    initialize(iwc);

    String action = iwc.getParameter(this.sAction);
        if (action == null) action = "";

    if (action.equals("")) {
      mainMenu(iwc);
    }else if (action.equals(this.parameterNewReseller)) {
      resellerCreation(-1);
    }else if (action.equals(this.parameterEditReseller)) {
      String sResellerId = iwc.getParameter(this.parameterResellerId);
      resellerCreation(Integer.parseInt(sResellerId));
    }else if (action.equals(this.parameterSaveNewReseller)) {
      saveReseller(iwc,-1);
    }else if (action.equals(this.parameterUpdateReseller)) {
      String sResellerId = iwc.getParameter(this.parameterResellerId);
      saveReseller(iwc,Integer.parseInt(sResellerId));
    }else if (action.equals(this.parameterAssignReseller)) {
      assignReseller(iwc);
    }else if (action.equals(this.parameterSaveProductInfo)) {
      saveProductInfo(iwc);
      assignReseller(iwc);
    }



  }

  public void initialize(IWContext iwc) {
      bundle = super.getBundle();
      iwrb = super.getResourceBundle();
      supplier = super.getSupplier();

      resellers = ResellerManager.getResellers(supplier.getID(),Reseller.getColumnNameName());

  }

  public void mainMenu(IWContext iwc) {
      Form form = new Form();
      /*
      ShadowBox sb = new ShadowBox();
        sb.add(form);
        sb.setWidth("90%");
      */

      Table table = new Table();
        form.add(new SubmitButton("T - new reseller", this.sAction,this.parameterNewReseller));
        form.add(table);
        table.setAlignment("center");
        table.setWidth("80%");

      int row = 0;

      Text resName;
      Text refNum;
      Link assign;
      Link edit;

      ++row;
      resName = (Text) theText.clone();
        resName.setBold();
        resName.setText(iwrb.getLocalizedString("travel.name","Name"));
      refNum = (Text) theText.clone();
        refNum.setBold();
        refNum.setText(iwrb.getLocalizedString("travel.reference_number","Reference number"));
      table.add(resName,1,row);
      table.add(refNum,2,row);

      if (resellers == null) {
        resellers = ResellerManager.getResellers(supplier.getID(),Reseller.getColumnNameName());
      }

      for (int i = 0; i < resellers.length; i++) {
          ++row;
          resName = (Text) theText.clone();
            resName.setText(resellers[i].getName());
          refNum = (Text) theText.clone();
            refNum.setText(resellers[i].getReferenceNumber());
          assign = new Link("T - assign");
            assign.setFontColor(super.textColor);
            assign.addParameter(this.sAction,this.parameterAssignReseller);
            assign.addParameter(this.parameterResellerId,resellers[i].getID());
          edit = new Link(iwrb.getImage("buttons/change.gif"));
            edit.setFontColor(super.textColor);
            edit.addParameter(this.sAction,this.parameterEditReseller);
            edit.addParameter(this.parameterResellerId,resellers[i].getID());

          table.add(resName,1,row);
          table.add(refNum,2,row);
          table.add(edit,3,row);
          table.add(assign,3,row);
          table.setColor(1,row,super.backgroundColor);
          table.setColor(2,row,super.backgroundColor);

      }
//      table.add(resellers);

      add(form);
  }

  public void resellerCreation(int resellerId) throws SQLException{
  /*
      ShadowBox sb = new ShadowBox();
        sb.setWidth("90%");
*/
      Form form = new Form();
      Table table = new Table();
        form.add(table);
        table.setAlignment("center");
        table.setColumnAlignment(1,"right");
        table.setColumnAlignment(2,"left");
        table.setBorder(0);

      boolean isUpdate = false;
      if (resellerId != -1) {
        isUpdate = true;
      }

      int row = 0;

      Text newSupplierText = (Text) theBoldText.clone();
        if (isUpdate) newSupplierText.setText(iwrb.getLocalizedString("travel.update_reseller_information","Update reseller information"));
        else newSupplierText.setText(iwrb.getLocalizedString("travel.new_reseller","New Reseller"));

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



      if (resellerId != -1) {
        table.add(new HiddenInput(this.parameterResellerId,Integer.toString(resellerId)));

        Reseller reseller = new Reseller(resellerId);
          name.setContent(reseller.getName());
          description.setContent(reseller.getDescription());

          Address addr = reseller.getAddress();
          if (addr != null) {
            String namer = addr.getStreetName();
            String number = addr.getStreetNumber();
            if (number == null) {
                address.setContent(namer);
            }else {
                address.setContent(namer+" "+number);
            }
          }

          List phones = reseller.getHomePhone();
          if (phones != null) {
            if (phones.size() > 0) {
              Phone phone1 = (Phone) phones.get(0);
              phone.setContent(phone1.getNumber());
            }
          }

          phones = reseller.getFaxPhone();
          if (phones != null) {
            if (phones.size() > 0) {
              Phone phone2 = (Phone) phones.get(0);
              fax.setContent(phone2.getNumber());
            }
          }

          Email eEmail = reseller.getEmail();
          if (eEmail != null) {
            email.setContent(eEmail.getEmailAddress());
          }
          submit = new SubmitButton("update",this.sAction,this.parameterUpdateReseller);
      }


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

      if (!isUpdate) {
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

      add(Text.getBreak());
      add(form);
  }

  public void saveReseller(IWContext iwc) {
    saveReseller(iwc, -1);
  }

  public void saveReseller(IWContext iwc, int resellerId)  {
      add(Text.getBreak());

      try {
          String name = iwc.getParameter("reseller_name");
          String description = iwc.getParameter("reseltler_description");
          String address = iwc.getParameter("reseller_address");
          String phone = iwc.getParameter("reseller_phone");
          String fax = iwc.getParameter("reseller_fax");
          String email = iwc.getParameter("reseller_email");

          String userName = iwc.getParameter("reseller_user_name");
          String passOne = iwc.getParameter("reseller_password_one");
          String passTwo = iwc.getParameter("reseller_password_one");
//                  tm.begin();
          boolean isUpdate = false;
          if (resellerId != -1) isUpdate = true;


          if (isUpdate) {
              Vector phoneIDS = new Vector();
              Reseller reseller = new Reseller(resellerId);

              Phone ph;
              List phones = reseller.getPhones(Phone.getHomeNumberID());
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

              phones = reseller.getPhones(Phone.getFaxNumberID());
              if (phones != null) {
                if (phones.size() > 0 ) {
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


              Address addr = reseller.getAddress();
                addr.setStreetName(address);
              addr.update();

              int[] addressIds = new int[1];
              addressIds[0] = addr.getID();


              Email eml = reseller.getEmail();
                eml.setEmailAddress(email);
              eml.update();

              int[] emailIds = new int[1];
              emailIds[0] = eml.getID();

              ResellerManager resMan = new ResellerManager();
              reseller = resMan.updateReseller(resellerId,name, description, addressIds, phoneIds, emailIds);


              add(iwrb.getLocalizedString("travel.information_updated","Information updated"));
              resellerCreation(resellerId);

          }else {
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

                //add(iwrb.getLocalizedString("travel.reseller_created","Reseller was created"));
                resellers = null;
                this.mainMenu(iwc);
            }else {
                add("TEMP - PASSWORDS not the same");
            }
          }

      }
      catch (Exception sql) {
          add(iwrb.getLocalizedString("travel.supplier_not_created","Supplier was not created"));
        sql.printStackTrace(System.err);
      }
  }

  private void assignReseller(IWContext iwc) throws SQLException {
    String sResellerId = iwc.getParameter(this.parameterResellerId);
    String sProductId = iwc.getParameter(this.parameterProductId);
    int productId = -1;
    if (sProductId != null) productId = Integer.parseInt(sProductId);
    int resellerId = -1;
    if (sResellerId != null) resellerId = Integer.parseInt(sResellerId);
    Reseller reseller = new Reseller(Integer.parseInt(sResellerId));

    Contract[] contracts = (Contract[]) (Contract.getStaticInstance(Contract.class)).findAllByColumn(Contract.getColumnNameResellerId(), Integer.toString(resellerId), Contract.getColumnNameServiceId(), Integer.toString(productId));
    Contract contract = null;
    if (contracts.length > 0) {
      contract = contracts[0];
    }

/*
    ShadowBox sb = new ShadowBox();
      sb.setWidth("90%");
*/
    Form form = new Form();
//      sb.add(form);
    Table table = new Table();
      table.setAlignment("center");
      table.setWidth("90%");
      table.setBorder(0);
      table.setCellspacing(0);
      form.add(table);

    Product[] products = tsb.getProducts(supplier.getID());
    int[] serviceDays;
    int row = 1;


    Text header = (Text) theBoldText.clone();
      header.setText(reseller.getName());
      header.setFontSize(Text.FONT_SIZE_14_HTML_4 );
    table.add(header,1,row);
    table.mergeCells(1,row,4,row);

    Text tNumberOfSeatsPerTour = (Text) theText.clone();
      tNumberOfSeatsPerTour.setText(iwrb.getLocalizedString("travel.number_of_seats_per_tour","Number of seats per tour"));
      tNumberOfSeatsPerTour.addToText("&nbsp;&nbsp;");
    Text tWeekdays = (Text) theText.clone();
      tWeekdays.setText(iwrb.getLocalizedString("travel.weekdays","Weekdays"));
    Text tDiscount = (Text) theText.clone();
      tDiscount.setText(iwrb.getLocalizedString("travel.discount","Discount"));
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
    TextInput pDiscount;

    Timeframe timeframe;

    Link closerLook = new Link("T- Nánar");
      closerLook.setFontColor(super.textColor);
      closerLook.addParameter(this.sAction, this.parameterAssignReseller);
      closerLook.addParameter(this.parameterResellerId , reseller.getID());

    Link temp;

    ++row;
    table.setAlignment(4,row,"left");
    for (int i = 0; i < products.length; i++) {
        ++row;
        pName = (Text) theBoldText.clone();
          pName.setText(products[i].getName());
          pName.setFontColor(super.textColor);

        table.add(pName,1,row);
        table.setRowColor(row, super.backgroundColor);
        table.mergeCells(1,row,3,row);

        if (products[i].getID() == productId) {

            form.add(new HiddenInput(this.parameterResellerId , Integer.toString(reseller.getID())));
            form.add(new HiddenInput(this.parameterProductId , Integer.toString(products[i].getID())));

            table.setAlignment(4,row,"right");
            temp = (Link) closerLook.clone();
              temp.setText("T-Loka");
              temp.setFontColor(super.backgroundColor);
            table.add(temp,4,row);

            pAlot = new TextInput("alotment");
              pAlot.setSize(3);
            pDays = new TextInput("valid");
              pDays.setSize(3);
            pDiscount = new TextInput("discount");
              pDiscount.setSize(3);

            serviceDays  = ServiceDay.getDaysOfWeek(products[i].getID());

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

            if (ServiceDay.getIfDay(productId,ServiceDay.MONDAY))
              weekdayFixTable.add(mondays,3,2);
            if (ServiceDay.getIfDay(productId,ServiceDay.TUESDAY))
            weekdayFixTable.add(tuesdays,4,2);
            if (ServiceDay.getIfDay(productId,ServiceDay.WEDNESDAY))
            weekdayFixTable.add(wednesdays,5,2);
            if (ServiceDay.getIfDay(productId,ServiceDay.THURSDAY))
            weekdayFixTable.add(thursdays,6,2);
            if (ServiceDay.getIfDay(productId,ServiceDay.FRIDAY))
            weekdayFixTable.add(fridays,7,2);
            if (ServiceDay.getIfDay(productId,ServiceDay.SATURDAY))
            weekdayFixTable.add(saturdays,8,2);
            if (ServiceDay.getIfDay(productId,ServiceDay.SUNDAY))
            weekdayFixTable.add(sundays,9,2);

            pFrom = new DateInput("from");
            pTo = new DateInput("to");

            if (contract != null) {
                pAlot.setContent(Integer.toString(contract.getAlotment()));
                pDays.setContent(Integer.toString(contract.getExpireDays()) );
                pDiscount.setContent(contract.getDiscount());
                int[] days = ResellerDay.getDaysOfWeek(resellerId, productId);
                for (int j = 0; j < days.length; j++) {
                  if (days[j] == ResellerDay.MONDAY) mondays.setChecked(true);
                  if (days[j] == ResellerDay.TUESDAY) tuesdays.setChecked(true);
                  if (days[j] == ResellerDay.WEDNESDAY) wednesdays.setChecked(true);
                  if (days[j] == ResellerDay.THURSDAY) thursdays.setChecked(true);
                  if (days[j] == ResellerDay.FRIDAY) fridays.setChecked(true);
                  if (days[j] == ResellerDay.SATURDAY) saturdays.setChecked(true);
                  if (days[j] == ResellerDay.SUNDAY) sundays.setChecked(true);
                }

                form.add(new HiddenInput(this.parameterContractId,Integer.toString(contract.getID())));
            }


            try {
              if (contract == null) {
                timeframe = tsb.getTimeframe(products[i]);
                pFrom.setDate(new idegaTimestamp(timeframe.getFrom()).getSQLDate());
                pTo.setDate(new idegaTimestamp(timeframe.getTo()).getSQLDate());
              }else {
                pFrom.setDate(new idegaTimestamp(contract.getFrom()).getSQLDate());
                pTo.setDate(new idegaTimestamp(contract.getTo()).getSQLDate());
              }
            }catch (TravelStockroomBusiness.TimeframeNotFoundException tnfe) {
              tnfe.printStackTrace(System.err);
              timeframe = null;
            }catch (TravelStockroomBusiness.ServiceNotFoundException snfe) {
              snfe.printStackTrace(System.err);
              timeframe = null;
            }


            pName.setFontColor(super.backgroundColor);
            table.setRowColor(row, super.textColor);

            ++row;
            table.add(tDiscount,1,row);
            table.add(pDiscount,3,row);
            table.setRowColor(row, super.backgroundColor);

            ++row;
            table.add(tNumberOfSeatsPerTour,1,row);
            table.add(pAlot,3,row);
            table.setRowColor(row, super.backgroundColor);

            ++row;
            table.setRowColor(row, super.backgroundColor);
            table.add(tWeekdays,1,row);
            table.add(weekdayFixTable,3,row);
            table.mergeCells(3,row,4,row);

            ++row;
            table.setRowColor(row, super.backgroundColor);
            table.add(tTimeframe,1,row);
            table.mergeCells(3,row,4,row);
            table.add(tfFromText,2,row);
            table.add(pFrom,3,row);
            table.add(tfToText,3,row);
            table.add(pTo,3,row);

            ++row;
            table.setRowColor(row, super.backgroundColor);
            table.add(tValidUntil,1,row);
            table.mergeCells(3,row,4,row);
            table.add(pDays,3,row);
            table.add(tDaysBefore,3,row);

            ++row;
            table.setRowColor(row, super.textColor);
            SubmitButton submit = new SubmitButton("T-SAVE",this.sAction,this.parameterSaveProductInfo);
            table.add(submit,4,row);
            table.setAlignment(4,row,"right");

        }else {
          temp = (Link) closerLook.clone();
            temp.addParameter(this.parameterProductId, products[i].getID());
          table.setAlignment(4,row,"right");
          table.add(temp,4,row);
        }
        //table.setRowColor(row, super.DARKBLUE);
    }





    add(Text.getBreak());
    add(form);
  }

  private void saveProductInfo(IWContext iwc) {
    String sResellerId = iwc.getParameter(this.parameterResellerId);
    String sProductId = iwc.getParameter(this.parameterProductId);
    String sContractId = iwc.getParameter(this.parameterContractId);

    String discount = iwc.getParameter("discount");
    String alotment = iwc.getParameter("alotment");

    String allDays = iwc.getParameter("all_days");
    String mondays = iwc.getParameter("mondays");
    String tuesdays = iwc.getParameter("tuesdays");
    String wednesdays = iwc.getParameter("wednesdays");
    String thursdays = iwc.getParameter("thursdays");
    String fridays = iwc.getParameter("fridays");
    String saturdays = iwc.getParameter("saturdays");
    String sundays = iwc.getParameter("sundays");

    String activeFrom = iwc.getParameter("from");
    String activeTo = iwc.getParameter("to");
    String valid = iwc.getParameter("valid");

    javax.transaction.TransactionManager tm = com.idega.transaction.IdegaTransactionManager.getInstance();

    try {
        tm.begin();
        int resellerId = Integer.parseInt(sResellerId);
        int productId = Integer.parseInt(sProductId);
        int contractId = -1;
        if (sContractId != null) contractId = Integer.parseInt(sContractId);


        Service service = new Service(productId);
        Reseller reseller = new Reseller(resellerId);

        idegaTimestamp from = new idegaTimestamp(activeFrom);
        idegaTimestamp to = new idegaTimestamp(activeTo);


        Contract contract;
        if (contractId != -1) {
          contract = new Contract(contractId);
        }else {
          contract = new Contract();
        }
          contract.setAlotment(Integer.parseInt(alotment));
          contract.setDiscount(discount);
          contract.setServiceId(productId);
          contract.setResellerId(resellerId);
          contract.setFrom(from.getTimestamp());
          contract.setTo(to.getTimestamp());
          contract.setExpireDays(Integer.parseInt(valid));

        if (contractId != -1) {
          contract.update();
          TravelStockroomBusiness.removeResellerHashtables(iwc);
        }else {
          contract.insert();
        }


        int[] tempDays = new int[7];
        int counter = 0;
          if (allDays != null) {
            tempDays = ServiceDay.getDaysOfWeek(productId);
            counter = tempDays.length;
          }else {
            if (sundays != null) tempDays[counter++] = java.util.GregorianCalendar.SUNDAY;
            if (mondays != null) tempDays[counter++] = java.util.GregorianCalendar.MONDAY;
            if (tuesdays != null) tempDays[counter++] = java.util.GregorianCalendar.TUESDAY;
            if (wednesdays != null) tempDays[counter++] = java.util.GregorianCalendar.WEDNESDAY;
            if (thursdays != null) tempDays[counter++] = java.util.GregorianCalendar.THURSDAY;
            if (fridays != null) tempDays[counter++] = java.util.GregorianCalendar.FRIDAY;
            if (saturdays != null) tempDays[counter++] = java.util.GregorianCalendar.SATURDAY;
          }

        ResellerDay resDay = (ResellerDay) ResellerDay.getStaticInstance(ResellerDay.class);

        ResellerDay[] oldToDelete = (ResellerDay[]) resDay.findAllByColumn(ResellerDay.getColumnNameResellerId() , Integer.toString(resellerId), ResellerDay.getColumnNameServiceId(), Integer.toString(productId));
        for (int i = 0; i < oldToDelete.length; i++) {
          oldToDelete[i].delete();
        }

        int[] activeDays = new int[counter];
        System.arraycopy(tempDays,0,activeDays,0,counter);
        for (int i = 0; i < activeDays.length; i++) {
          resDay = new ResellerDay();
            resDay.setDayOfWeek(resellerId, productId, activeDays[i]);
          resDay.insert();
        }

        tm.commit();
    }catch (Exception e) {
        e.printStackTrace(System.err);
        try {
          tm.rollback();
        }catch (javax.transaction.SystemException se) {
          se.printStackTrace(System.err);
        }
    }

  }

}