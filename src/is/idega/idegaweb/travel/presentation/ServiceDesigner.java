package is.idega.travel.presentation;

import com.idega.jmodule.object.JModuleObject;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.block.trade.stockroom.data.*;
import com.idega.util.idegaTimestamp;
import com.idega.block.trade.stockroom.business.*;
import is.idega.travel.business.TravelStockroomBusiness;
import com.idega.util.idegaCalendar;
import com.idega.core.accesscontrol.business.AccessControl;
import com.idega.projects.nat.business.NatBusiness;
import java.sql.SQLException;

import is.idega.travel.data.Service;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class ServiceDesigner extends TravelManager {

  private IWBundle bundle;
  private IWResourceBundle iwrb;

  private Supplier supplier;
  private Service service;

  private String ServiceAction = "service_action";
  private String PriceCategoryRefresh = "refresh_categories";
  private String PriceCategorySave = "save_categories";
  private String ServiceSessionAttribute = "service_designer_service_id";


  public ServiceDesigner() {
  }

  public void add(ModuleObject mo) {
    super.add(mo);
  }


  public void main(ModuleInfo modinfo) throws SQLException{
      super.main(modinfo);
      bundle = super.getBundle();
      iwrb = super.getResourceBundle();
      supplier = super.getSupplier();

      Window window = new Window("gimmi",PriceCategoryDesigner.class,TravelWindow.class);

      Link link = new Link(window);
        link.setText("gimmi");

        add(link);

      if (supplier != null) {

        String action = modinfo.getParameter(ServiceAction);
        if (action == null) {action = "";}

        if (action.equals("")) {
            displayForm(modinfo);
        }else if (action.equals("create")) {
            createService(modinfo);
        }else if (action.equals(this.PriceCategoryRefresh) ) {
            priceCategoryCreation(modinfo);
        }else if (action.equals(this.PriceCategorySave)) {
            priceCategorySave(modinfo);
        }


      }else {
        add("TEMP - Enginn supplier");
      }

      super.addBreak();
  }


  private void displayForm(ModuleInfo modinfo) {

      Form form = new Form();
      Table table = new Table();

      ShadowBox sb = new ShadowBox();
        form.add(sb);
        sb.setWidth("90%");
        sb.setAlignment("center");
        sb.add(table);

      table.setWidth("95%");

      int row = 0;
      idegaTimestamp stamp = idegaTimestamp.RightNow();

      TextInput name = new TextInput("name_of_trip");
          name.setSize(40);
      TextArea description = new TextArea("description");
          description.setWidth(50);
          description.setHeight(12);
      DateInput active_from = new DateInput("active_from");
          active_from.setDate(stamp.getSQLDate());
      DateInput active_to = new DateInput("active_to");
          stamp.addDays(92);
          active_to.setDate(stamp.getSQLDate());
      BooleanInput active_yearly = new BooleanInput("active_yearly");
        active_yearly.setSelected(false);


      CheckBox allDays = new CheckBox("all_days");
      CheckBox mondays = new CheckBox("mondays");
      CheckBox tuesdays = new CheckBox("tuesdays");
      CheckBox wednesdays = new CheckBox("wednesdays");
      CheckBox thursdays = new CheckBox("thursdays");
      CheckBox fridays = new CheckBox("fridays");
      CheckBox saturdays = new CheckBox("saturdays");
      CheckBox sundays = new CheckBox("sundays");

      /*
        allDays.setOnClick("this.form."+mondays.getName()+".disabled=true");
        allDays.setOnClick("this.form."+tuesdays.getName()+".disabled=true");
        allDays.setOnClick("this.form."+wednesdays.getName()+".disabled=true");
        allDays.setOnClick("this.form."+thursdays.getName()+".disabled=true");
        allDays.setOnClick("this.form."+fridays.getName()+".disabled=true");
        allDays.setOnClick("this.form."+saturdays.getName()+".disabled=true");
        allDays.setOnClick("this.form."+sundays.getName()+".disabled=true");
      */


      TextInput departure_from = new TextInput("departure_from");
          departure_from.setSize(40);
      TimeInput departure_time = new TimeInput("departure_time");
          departure_time.setHour(8);
          departure_time.setMinute(0);
      TextInput arrival_at = new TextInput("arrival_at");
          arrival_at.setSize(40);
      TimeInput arrival_time = new TimeInput("arrival_time");
          arrival_time.setHour(8);
          arrival_time.setMinute(0);

      RadioButton hotelPickupYes = new RadioButton("hotel_pickup","yes");
          hotelPickupYes.setSelected();
      RadioButton hotelPickupNo = new RadioButton("hotel_pickup","no");
      TimeInput hotelPickupTime = new TimeInput("hotel_pickup_time");
          hotelPickupTime.setHour(8);
          hotelPickupTime.setMinute(0);
      TextInput hotelPickup = new TextInput("hotel_pickup_address");

        hotelPickup.setSize(40);
        hotelPickupYes.setOnClick("this.form."+hotelPickup.getName()+".disabled=false");
        hotelPickupYes.setOnClick("this.form."+hotelPickupTime.getHourName()+".disabled=false");
        hotelPickupYes.setOnClick("this.form."+hotelPickupTime.getMinuteName()+".disabled=false");
        hotelPickupNo.setOnClick("this.form."+hotelPickup.getName()+".disabled=true");
        hotelPickupNo.setOnClick("this.form."+hotelPickupTime.getHourName()+".disabled=true");
        hotelPickupNo.setOnClick("this.form."+hotelPickupTime.getMinuteName()+".disabled=true");

      TextInput numberOfSeats = new TextInput("number_of_seats");
        numberOfSeats.setAsIntegers("TEMP _ Must be numbers");
        numberOfSeats.setAsNotEmpty("TEMP _ Must not be empty");


      ++row;
      Text nameText = (Text) theBoldText.clone();
          nameText.setText(iwrb.getLocalizedString("travel.name_of_trip","Name of trip"));
      table.add(nameText,1,row);
      table.add(name,2,row);

      ++row;

      Text descText = (Text) theBoldText.clone();
          descText.setText(iwrb.getLocalizedString("travel.description","Description"));
      Table descFixTable = new Table(3,1);
        descFixTable.setCellpadding(0);
        descFixTable.setCellspacing(0);
        descFixTable.setBorder(0);
        descFixTable.setAlignment("left");
        descFixTable.setAlignment(1,1,"left");
        descFixTable.setColumnAlignment(1,"center");
        descFixTable.setColumnAlignment(2,"center");
        descFixTable.setColumnAlignment(3,"center");


      descFixTable.add(description,1,1);
      com.idega.jmodule.image.presentation.ImageInserter imageInserter = new com.idega.jmodule.image.presentation.ImageInserter("design_image_id");
      descFixTable.add(imageInserter,3,1);

      table.setVerticalAlignment(1,row,"top");
      table.setVerticalAlignment(2,row,"top");
      table.add(descText,1,row);
      table.add(descFixTable,2,row);


      ++row;
      Text timeframeText = (Text) theBoldText.clone();
        timeframeText.setText(iwrb.getLocalizedString("travel.timeframe","Timeframe"));
      Text tfFromText = (Text) smallText.clone();
        tfFromText.setText(iwrb.getLocalizedString("travel.from","from"));
      Text tfToText = (Text) smallText.clone();
        tfToText.setText(iwrb.getLocalizedString("travel.to","to"));
      Text tfYearlyText = (Text) smallText.clone();
        tfYearlyText.setText(iwrb.getLocalizedString("travel.yearly","yearly"));

      Table activeTable = new Table(5,2);

        activeTable.add(tfFromText,1,1);
        activeTable.add(active_from,1,2);
        activeTable.add(tfToText,3,1 );
        activeTable.add(active_to,3,2);
        activeTable.add(tfYearlyText,5,1 );
        activeTable.add(active_yearly,5,2);

        activeTable.setVerticalAlignment(1,1,"bottom");
        activeTable.setVerticalAlignment(3,1,"bottom");
        activeTable.setVerticalAlignment(5,1,"bottom");


      table.add(timeframeText,1,row );
      table.add(activeTable,2,row);



      ++row;
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

      ++row;
      Text departureFromText = (Text) theBoldText.clone();
          departureFromText.setText(iwrb.getLocalizedString("travel.departure_from","Departure from"));
      table.add(departureFromText,1,row);
      table.add(departure_from,2,row);

      ++row;
      Text departureTimeText = (Text) theBoldText.clone();
          departureTimeText.setText(iwrb.getLocalizedString("travel.departure_time","Departure time"));
      table.add(departureTimeText,1,row);
      table.add(departure_time,2,row);

      ++row;
      Text arrivalAtText = (Text) theBoldText.clone();
          arrivalAtText.setText(iwrb.getLocalizedString("travel.arrival_at","Arrival at"));
      table.add(arrivalAtText,1,row);
      table.add(departure_from,2,row);

      ++row;
      Text arrivalTimeText = (Text) theBoldText.clone();
          arrivalTimeText.setText(iwrb.getLocalizedString("travel.arrival_time","Arrival time"));
      table.add(arrivalTimeText,1,row);
      table.add(arrival_time,2,row);

      ++row;
      Text hotelPickupText = (Text) theBoldText.clone();
          hotelPickupText.setText(iwrb.getLocalizedString("travel.hotel_pickup","Hotel pick-up"));

      Table hotelPickupFixTable = new Table(7,2);
        hotelPickupFixTable.setCellpadding(0);
        hotelPickupFixTable.setCellspacing(1);
        hotelPickupFixTable.setColumnAlignment(1,"center");
        hotelPickupFixTable.setColumnAlignment(2,"center");
        hotelPickupFixTable.setColumnAlignment(3,"center");


      Text yesText = (Text) smallText.clone();
        yesText.setText(iwrb.getLocalizedString("travel.yes","yes"));
      Text noText = (Text) smallText.clone();
        noText.setText(iwrb.getLocalizedString("travel.no","no"));
      Text addressText = (Text) smallText.clone();
        addressText.setText(iwrb.getLocalizedString("travel.address_long","Address"));
      Text timeText = (Text) smallText.clone();
        timeText.setText(iwrb.getLocalizedString("travel.time","Time"));



      hotelPickupFixTable.add(yesText,1,1);
      hotelPickupFixTable.add(noText,3,1);
      hotelPickupFixTable.add(hotelPickupYes,1,2);
      hotelPickupFixTable.add(hotelPickupNo,3,2);

      table.add(hotelPickupText,1,row);
      table.add(hotelPickupFixTable,2,row);

      hotelPickupFixTable.add(addressText,5,1);
      hotelPickupFixTable.add(hotelPickup,5,2);
      hotelPickupFixTable.add(timeText,7,1);
      hotelPickupFixTable.add(hotelPickupTime,7,2);

      hotelPickupFixTable.setVerticalAlignment(1,1,"bottom");
      hotelPickupFixTable.setVerticalAlignment(3,1,"bottom");
      hotelPickupFixTable.setVerticalAlignment(5,1,"bottom");
      hotelPickupFixTable.setVerticalAlignment(7,1,"bottom");


      ++row;

      Text nOSText = (Text) theBoldText.clone();
        nOSText.setText("TEMP-Number of seats");
      table.add(nOSText,1,row);
      table.add(numberOfSeats,2,row);

      ++row;
      table.mergeCells(1,row,2,row);
      table.setAlignment(1,row,"right");
      SubmitButton submit = new SubmitButton(iwrb.getImage("buttons/save.gif"),ServiceAction,"create");
      table.add(submit,1,row);


      table.setColumnAlignment(1,"right");
      table.setColumnAlignment(2,"left");

      add(form);
  }


  private void createService(ModuleInfo modinfo) {
      String name = modinfo.getParameter("name_of_trip");
      String description = modinfo.getParameter("description");
      String imageId = modinfo.getParameter("design_image_id");
      String activeFrom = modinfo.getParameter("active_from");
      String activeTo = modinfo.getParameter("active_to");
      String activeYearly = modinfo.getParameter("active_yearly");

      String allDays = modinfo.getParameter("all_days");
      String mondays = modinfo.getParameter("mondays");
      String tuesdays = modinfo.getParameter("tuesdays");
      String wednesdays = modinfo.getParameter("wednesdays");
      String thursdays = modinfo.getParameter("thursdays");
      String fridays = modinfo.getParameter("fridays");
      String saturdays = modinfo.getParameter("saturdays");
      String sundays = modinfo.getParameter("sundays");

      String departureFrom = modinfo.getParameter("departure_from");
      String departureTime = modinfo.getParameter("departure_time");
      String arrivalAt = modinfo.getParameter("arrival_at");
      String arrivalTime = modinfo.getParameter("arrival_time");
      String hotelPickup = modinfo.getParameter("hotel_pickup");
      String hotelPickupAddress = modinfo.getParameter("hotel_pickup_address");
      String hotelPickupTime = modinfo.getParameter("hotel_pickup_time");

      String numberOfSeats = modinfo.getParameter("number_of_seats");

      boolean yearly = false;
      if (activeYearly != null) {
        if (activeYearly.equals("Y")) yearly = true;
      }


      Integer iImageId = null;
      if (imageId != null) {
        if (!imageId.equals("-1")) {
          iImageId = new Integer(imageId);
        }
      }

      Integer iNumberOfSeats = null;
      if (numberOfSeats != null) {
        iNumberOfSeats = new Integer(numberOfSeats);
      }

      idegaTimestamp activeFromStamp = null;
      if (activeFrom != null) {
        activeFromStamp = new idegaTimestamp(activeFrom);
      }

      idegaTimestamp activeToStamp = null;
      if (activeTo != null) {
        activeToStamp = new idegaTimestamp(activeTo);
      }

      idegaTimestamp departureStamp = null;
      if (departureTime != null) {
        departureStamp = new idegaTimestamp("2001-01-01 "+departureTime);
      }

      idegaTimestamp arrivalStamp = null;
      if (arrivalTime != null) {
        arrivalStamp = new idegaTimestamp("2001-01-01 "+arrivalTime);
      }

      idegaTimestamp hotelPickupTimeStamp = null;
      if (hotelPickupTime != null) {
        hotelPickupTimeStamp = new idegaTimestamp("2001-01-01 "+hotelPickupTime);
      }

      int[] tempDays = new int[7];
      int counter = 0;
        if (allDays != null) {
          tempDays[counter++] = java.util.GregorianCalendar.SUNDAY;
          tempDays[counter++] = java.util.GregorianCalendar.MONDAY;
          tempDays[counter++] = java.util.GregorianCalendar.TUESDAY;
          tempDays[counter++] = java.util.GregorianCalendar.WEDNESDAY;
          tempDays[counter++] = java.util.GregorianCalendar.THURSDAY;
          tempDays[counter++] = java.util.GregorianCalendar.FRIDAY;
          tempDays[counter++] = java.util.GregorianCalendar.SATURDAY;
        }else {
          if (sundays != null) tempDays[counter++] = java.util.GregorianCalendar.SUNDAY;
          if (mondays != null) tempDays[counter++] = java.util.GregorianCalendar.MONDAY;
          if (tuesdays != null) tempDays[counter++] = java.util.GregorianCalendar.TUESDAY;
          if (wednesdays != null) tempDays[counter++] = java.util.GregorianCalendar.WEDNESDAY;
          if (thursdays != null) tempDays[counter++] = java.util.GregorianCalendar.THURSDAY;
          if (fridays != null) tempDays[counter++] = java.util.GregorianCalendar.FRIDAY;
          if (saturdays != null) tempDays[counter++] = java.util.GregorianCalendar.SATURDAY;
        }

      int[] activeDays = new int[counter];
      System.arraycopy(tempDays,0,activeDays,0,counter);

      try {
        TravelStockroomBusiness tsb = TravelStockroomBusiness.getNewInstance();
          tsb.setTimeframe(activeFromStamp, activeToStamp, yearly);
          int serviceId = tsb.createTourService(supplier.getID(),iImageId,name,description,true, departureFrom,departureStamp, arrivalAt, arrivalStamp, hotelPickupAddress, hotelPickupTimeStamp, activeDays, iNumberOfSeats);
          setService(modinfo,serviceId);
        priceCategoryCreation(modinfo);


      }catch (Exception e) {
        e.printStackTrace(System.err);
        add("TEMP - Service EKKI smíðuð");
      }



  }

  private void setService(ModuleInfo modinfo,int serviceId) throws SQLException{
      service = new Service(serviceId);
      modinfo.setSessionAttribute(this.ServiceSessionAttribute, service);
  }

  private Service getService(ModuleInfo modinfo) {
    if (service == null) {
      service = (Service) modinfo.getSessionAttribute(this.ServiceSessionAttribute);
    }
    return service;

  }

  private void removeService(ModuleInfo modinfo) {
      service = null;
      modinfo.removeSessionAttribute(this.ServiceSessionAttribute);
  }


  private void priceCategoryCreation(ModuleInfo modinfo) {

      if (this.getService(modinfo) != null) {

          ShadowBox sb = new ShadowBox();
            sb.setWidth("90%");

          String sHowMany = modinfo.getParameter("how_many");
          if (sHowMany == null) {
            sHowMany = "2";
          }
          int iHowMany = Integer.parseInt(sHowMany);

          Form howManyForm = new Form();
              sb.add(howManyForm);
              howManyForm.addParameter(this.ServiceAction ,this.PriceCategoryRefresh);

          Table tableHowMany = new Table();
            howManyForm.add(tableHowMany);

          Text howManyText = (Text) theText.clone();
            howManyText.setText("T - Hve marga verðliði");

          TextInput howMany = new TextInput("how_many",sHowMany);
            howMany.setAsIntegers("Temp - bara tölur takk");
            howMany.setAsNotEmpty("Temp - selja e-ð ");

          SubmitButton howManySubmit = new SubmitButton("T - áfram");

          tableHowMany.add(howManyText,1,1);
          tableHowMany.add(howMany,2,1);
          tableHowMany.add(howManySubmit,3,1);


          Form form = new Form();
            sb.add(form);

          Table table = new Table();
            form.add(table);
            table.setAlignment("center");
            table.setWidth("95%");
            int row = 1;

          TextInput name;
          TextInput description;
          DropdownMenu type;
          TextArea extraInfo;
          BooleanInput onlineCategory;
          TextInput priceDiscount;
          DropdownMenu discountOfMenu;



          Text counter;

          Text catName = (Text) theText.clone();
            catName.setText("T - nafn");
          Text catDesc = (Text) theText.clone();
            catDesc.setText("T - description");
          Text catOnline = (Text) theText.clone();
            catOnline.setText("T - netbokun");
          Text catType = (Text) theText.clone();
            catType.setText("T - type");
          Text catExtraInfo = (Text) theText.clone();
            catExtraInfo.setText("T - exrtaInfo");
          Text priceDiscountText = (Text) theText.clone();
            priceDiscountText.setText("T - Price / Discount");
          Text discountOfText = (Text) theText.clone();
            discountOfText.setText("T - Discount of");

          table.setColor(1,row,NatBusiness.backgroundColor);
          table.mergeCells(1,row,5,row);

          for (int i = 1; i <= iHowMany; i++) {
              counter = (Text) theBoldText.clone();
                counter.setText("t - verðliður #"+i);
              name = new TextInput("price_name");
              description = new TextInput("price_description");
              type = new DropdownMenu("price_type");
                type.addMenuElement(PriceCategory.PRICETYPE_PRICE,"Verð");
                type.addMenuElement(PriceCategory.PRICETYPE_DISCOUNT,"Afsláttur");
              extraInfo = new TextArea("price_extra_info");
                extraInfo.setWidth(60);
                extraInfo.setHeight(5);
              onlineCategory = new BooleanInput("price_online");
              priceDiscount = new TextInput("price_discount");
                priceDiscount.setAsNotEmpty("T - verður að skrá verð eða afslátt á allt verðliði");

              discountOfMenu = new DropdownMenu("discount_of");
                  for (int j = 1; j < i; j++) {
                    discountOfMenu.addMenuElement(j,"T-Verðliði "+j);
                  }

              ++row;
              table.mergeCells(1,row,1,row+2);
              table.add(counter,1,row);
              table.setVerticalAlignment(1,row,"top");

              table.add(catName,2,row);
              table.add(name,3,row);
              table.add(catOnline,4,row);
              table.add(onlineCategory,5,row);

              ++row;
              table.add(catDesc,2,row);
              table.add(description,3,row);
              table.add(catType,4,row);
              if (i != 1) {
                table.add(type,5,row);
              }else {
                table.add("T - Verð",5,row);
                table.add(new HiddenInput("price_type",PriceCategory.PRICETYPE_PRICE),5,row);
              }

              ++row;
              table.add(catExtraInfo,2,row);
              table.add(Text.getBreak(),2,row);
              table.add(extraInfo,2,row);
              table.mergeCells(2,row,5,row);

              ++row;
              table.add(priceDiscountText,2,row);
              table.add(priceDiscount,3,row);
              if (i != 1) {
                table.add(discountOfText,4,row);
                table.add(discountOfMenu,5,row);
              }else {
                table.add(new HiddenInput("discount_of","0"),5,row);
              }

              ++row;
              table.setColor(1,row,NatBusiness.backgroundColor);
              table.mergeCells(1,row,5,row);

          }

          table.add(new SubmitButton("T - save",this.ServiceAction, this.PriceCategorySave),1,row);

          if (iHowMany > 0) {
            SubmitButton savePrice = new SubmitButton(this.ServiceAction, this.PriceCategorySave);

          }

          add(Text.getBreak());
          add(sb);
      }else {
        add("TEMP SERVICE ER NULL");
      }

  }

  private void priceCategorySave(ModuleInfo modinfo) {
      String[] name =   (String[]) modinfo.getParameterValues("price_name");
      String[] desc =   (String[]) modinfo.getParameterValues("price_description");
      String[] type =   (String[]) modinfo.getParameterValues("price_type");
      String[] info =   (String[]) modinfo.getParameterValues("price_extra_info");
      String[] online = (String[]) modinfo.getParameterValues("price_online");

      String[] priceDiscount = (String[]) modinfo.getParameterValues("price_discount");
      String[] discountOf = (String[]) modinfo.getParameterValues("discount_of");

      Service service = this.getService(modinfo);
      TravelStockroomBusiness sb = TravelStockroomBusiness.getNewInstance();

      try {
        if (name != null) {
          int priceCategoryId = 0;
          int[] categoryIds = new int[name.length];
          int parentId;
          boolean bOnline;
          for (int i = 0; i < name.length; i++) {
              if (online[i].equals("Y")) {
                  bOnline = true;
              }else {
                bOnline = false;
              }

              if (type[i].equals(PriceCategory.PRICETYPE_DISCOUNT)) {
                parentId = categoryIds[Integer.parseInt(discountOf[i])-1];
                priceCategoryId = sb.createPriceCategory(supplier.getID(), name[i], desc[i],type[i], info[i], bOnline, parentId);
                sb.setPrice(service.getID() , priceCategoryId, TravelStockroomBusiness.getCurrencyIdForIceland(),idegaTimestamp.getTimestampRightNow(), Float.parseFloat(priceDiscount[i]), ProductPrice.PRICETYPE_DISCOUNT);
              }else if (type[i].equals(PriceCategory.PRICETYPE_PRICE)) {
                priceCategoryId = sb.createPriceCategory(supplier.getID(), name[i], desc[i],type[i], info[i], bOnline);
                sb.setPrice(service.getID() , priceCategoryId, TravelStockroomBusiness.getCurrencyIdForIceland(),idegaTimestamp.getTimestampRightNow(), Float.parseFloat(priceDiscount[i]), ProductPrice.PRICETYPE_PRICE);
              }

              categoryIds[i] = priceCategoryId;
          }
        }
        this.removeService(modinfo);

      }catch (Exception e) {
        e.printStackTrace(System.err);
      }


  }


}