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

  private String ServiceAction = "service_action";

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

      if (supplier != null) {

        String action = modinfo.getParameter(ServiceAction);
        if (action == null) {action = "";}

        if (action.equals("")) {
            displayForm(modinfo);
        }else if (action.equals("create")) {
            createService(modinfo);
        }

      }else {
        add("TEMP - Enginn supplier");
      }

      super.addBreak();
      //super.add(tm);
  }


  public void displayForm(ModuleInfo modinfo) {

      Form form = new Form();
      Table table = new Table();
        table.setBorder(1);
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
      Text tfFromText = (Text) theBoldText.clone();
        tfFromText.setText(iwrb.getLocalizedString("travel.from","from"));
      Text tfToText = (Text) theBoldText.clone();
        tfToText.setText(iwrb.getLocalizedString("travel.to","to"));

      table.add(timeframeText,1,row );
      table.add(tfFromText,2,row );
      table.add(active_from,2,row);
      table.add(tfToText,2,row );
      table.add(active_to,2,row);


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

//      ++row;
      hotelPickupFixTable.add(addressText,5,1);
      hotelPickupFixTable.add(hotelPickup,5,2);
      hotelPickupFixTable.add(timeText,7,1);
      hotelPickupFixTable.add(hotelPickupTime,7,2);

      hotelPickupFixTable.setVerticalAlignment(1,1,"bottom");
      hotelPickupFixTable.setVerticalAlignment(3,1,"bottom");
      hotelPickupFixTable.setVerticalAlignment(5,1,"bottom");
      hotelPickupFixTable.setVerticalAlignment(7,1,"bottom");


      ++row;
      table.mergeCells(1,row,2,row);
      table.setAlignment(1,row,"right");
      SubmitButton submit = new SubmitButton(iwrb.getImage("buttons/save.gif"),ServiceAction,"create");
      table.add(submit,1,row);


      table.setColumnAlignment(1,"right");
      table.setColumnAlignment(2,"left");

      add(form);
  }


  public void createService(ModuleInfo modinfo) {
      String name = modinfo.getParameter("name_of_trip");
      String description = modinfo.getParameter("description");
      String imageId = modinfo.getParameter("design_image_id");
      String activeFrom = modinfo.getParameter("active_from");
      String activeTo = modinfo.getParameter("active_to");

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
      String departureAt = modinfo.getParameter("arrival_at");
      String arrivalTime = modinfo.getParameter("arrival_time");
      String hotelPickup = modinfo.getParameter("hotel_pickup");
      String hotelPickupAddress = modinfo.getParameter("hotel_pickup_address");
      String hotelPickupTime = modinfo.getParameter("hotel_pickup_time");

      Integer iImageId = null;
      if (imageId != null) {
        iImageId = new Integer(imageId);
      }

      idegaTimestamp activeFromStamp;
      if (activeFrom != null) {
        activeFromStamp = new idegaTimestamp(activeFrom);
      }

      idegaTimestamp activeToStamp;
      if (activeTo != null) {
        activeToStamp = new idegaTimestamp(activeTo);
      }

      idegaTimestamp departureStamp;
      if (departureTime != null) {
        departureStamp = new idegaTimestamp("2001-01-01 "+departureTime);
      }

      idegaTimestamp arrivalStamp;
      if (arrivalTime != null) {
        arrivalStamp = new idegaTimestamp("2001-01-01 "+arrivalTime);
      }

      idegaTimestamp hotelPickupTimeStamp;
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



      TravelStockroomBusiness tsb = new TravelStockroomBusiness();
//        tsb.createTripService(supplier.getID(),iImageId,name,description,true, departureFrom,departureStamp, arrivalStamp, hotelPickupAddress, hotelPickupTimeStamp, activeDays);

      add("<br>"+name);
      add("<br>"+description);
      add("<br>"+imageId);
      add("<br>"+activeFrom);
      add("<br>"+activeTo);
      add("<br>A"+allDays);
      add("<br>M"+mondays);
      add("<br>T"+tuesdays);
      add("<br>W"+wednesdays);
      add("<br>T"+thursdays);
      add("<br>F"+fridays);
      add("<br>S"+saturdays);
      add("<br>S"+sundays);
      add("<br>"+departureFrom);
      add("<br>"+departureTime);
      add("<br>"+hotelPickup);

  }



  public void addScript(ModuleInfo modinfo) {


  }

}