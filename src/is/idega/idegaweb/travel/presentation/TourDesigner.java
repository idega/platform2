package is.idega.travel.presentation;

import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.util.idegaTimestamp;
import com.idega.idegaweb.*;
import is.idega.travel.business.*;
import com.idega.block.trade.stockroom.data.*;
import java.sql.SQLException;

import com.idega.core.data.Address;
import com.idega.core.data.AddressType;
import is.idega.travel.data.*;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class TourDesigner extends TravelManager {
  IWResourceBundle iwrb;
  IWBundle iwb;
  Supplier supplier;
  TourBusiness tb = new TourBusiness();

  String NAME_OF_FORM = ServiceDesigner.NAME_OF_FORM;
  String ServiceAction = ServiceDesigner.ServiceAction;

  Service service;
  Tour tour;
  Timeframe timeframe;
  Address depAddress;
  Address arrAddress;


  private String parameterIsUpdate = "isTourUpdate";
  private String parameterTimeframeId = "td_timeframeId";

  public TourDesigner(IWContext iwc) throws Exception{
    init(iwc);
  }

  public void init(IWContext iwc) throws Exception{
    super.main(iwc);
    iwrb = super.getResourceBundle();
    iwb = super.getBundle();
    supplier = super.getSupplier();
  }

  private boolean setupData(int tourId) {
    try {
      service = new Service(tourId);
      tour = new Tour(tourId);
      timeframe = service.getTimeframe();

      int addressTypeId = AddressType.getId(tb.uniqueArrivalAddressType);
      Address[] tempAddresses = (Address[]) (service.findRelated( (Address) Address.getStaticInstance(Address.class), Address.getColumnNameAddressTypeId(), Integer.toString(addressTypeId)));
      if (tempAddresses.length > 0) {
        arrAddress = new Address(tempAddresses[tempAddresses.length -1].getID());
      }
      addressTypeId = AddressType.getId(tb.uniqueDepartureAddressType);

      tempAddresses = (Address[]) (service.findRelated( (Address) Address.getStaticInstance(Address.class), Address.getColumnNameAddressTypeId(), Integer.toString(addressTypeId)));
      if (tempAddresses.length > 0) {
        depAddress = new Address(tempAddresses[tempAddresses.length -1].getID());
      }

      return true;
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
      return false;
    }
  }


  public Form getTourDesignerForm(IWContext iwc) {
    return getTourDesignerForm(iwc,-1);
  }


  public Form getTourDesignerForm(IWContext iwc,int tourId) {
//    System.err.println("TOUR_ID = "+tourId);


    boolean isDataValid = true;

    if (tourId != -1) {
      isDataValid = setupData(tourId);
    }

      Form form = new Form();
        form.setName(NAME_OF_FORM);
      Table table = new Table();
        form.add(table);
/*
      ShadowBox sb = new ShadowBox();
        form.add(sb);
        sb.setWidth("90%");
        sb.setAlignment("center");
        sb.add(table);
*/
    if (isDataValid) {

      table.setWidth("95%");

      int row = 0;
      idegaTimestamp stamp = idegaTimestamp.RightNow();

      TextInput name = new TextInput("name_of_trip");
          name.setSize(40);
          name.keepStatusOnAction();
      TextArea description = new TextArea("description");
          description.setWidth(50);
          description.setHeight(12);
          description.keepStatusOnAction();
      DateInput active_from = new DateInput("active_from");
          active_from.setDate(stamp.getSQLDate());
         active_from.keepStatusOnAction();
      DateInput active_to = new DateInput("active_to");
          stamp.addDays(92);
          active_to.setDate(stamp.getSQLDate());
          active_to.keepStatusOnAction();
      BooleanInput active_yearly = new BooleanInput("active_yearly");
        active_yearly.setSelected(false);
        active_yearly.keepStatusOnAction();


      CheckBox allDays = new CheckBox("all_days");
      CheckBox mondays = new CheckBox("mondays");
      CheckBox tuesdays = new CheckBox("tuesdays");
      CheckBox wednesdays = new CheckBox("wednesdays");
      CheckBox thursdays = new CheckBox("thursdays");
      CheckBox fridays = new CheckBox("fridays");
      CheckBox saturdays = new CheckBox("saturdays");
      CheckBox sundays = new CheckBox("sundays");
        allDays.keepStatusOnAction();
        mondays.keepStatusOnAction();
        tuesdays.keepStatusOnAction();
        wednesdays.keepStatusOnAction();
        thursdays.keepStatusOnAction();
        fridays.keepStatusOnAction();
        saturdays.keepStatusOnAction();
        sundays.keepStatusOnAction();


      TextInput departure_from = new TextInput("departure_from");
          departure_from.setSize(40);
          departure_from.keepStatusOnAction();
      TimeInput departure_time = new TimeInput("departure_time");
          departure_time.setHour(8);
          departure_time.setMinute(0);
          departure_time.keepStatusOnAction();
      TextInput arrival_at = new TextInput("arrival_at");
          arrival_at.setSize(40);
          arrival_at.keepStatusOnAction();
      TimeInput arrival_time = new TimeInput("arrival_time");
          arrival_time.setHour(8);
          arrival_time.setMinute(0);
          arrival_time.keepStatusOnAction();

      RadioButton hotelPickupYes = new RadioButton("hotel_pickup","yes");
          hotelPickupYes.setSelected();
          hotelPickupYes.keepStatusOnAction();
      RadioButton hotelPickupNo = new RadioButton("hotel_pickup","no");
        hotelPickupYes.keepStatusOnAction();
      TimeInput hotelPickupTime = new TimeInput("hotel_pickup_time");
          hotelPickupTime.setHour(8);
          hotelPickupTime.setMinute(0);
          hotelPickupTime.keepStatusOnAction();
      TextInput hotelPickup = new TextInput("hotel_pickup_address");
        hotelPickup.keepStatusOnAction();

        hotelPickup.setSize(40);
        hotelPickupYes.setOnClick("this.form."+hotelPickup.getName()+".disabled=false");
        hotelPickupYes.setOnClick("this.form."+hotelPickupTime.getHourName()+".disabled=false");
        hotelPickupYes.setOnClick("this.form."+hotelPickupTime.getMinuteName()+".disabled=false");
        hotelPickupNo.setOnClick("this.form."+hotelPickup.getName()+".disabled=true");
        hotelPickupNo.setOnClick("this.form."+hotelPickupTime.getHourName()+".disabled=true");
        hotelPickupNo.setOnClick("this.form."+hotelPickupTime.getMinuteName()+".disabled=true");

      TextInput numberOfSeats = new TextInput("number_of_seats");
        numberOfSeats.keepStatusOnAction();


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
      table.add(arrival_at,2,row);

      ++row;
      Text arrivalTimeText = (Text) theBoldText.clone();
          arrivalTimeText.setText(iwrb.getLocalizedString("travel.arrival_time","Arrival time"));
      table.add(arrivalTimeText,1,row);
      table.add(arrival_time,2,row);

      ++row;
      Text hotelPickupText = (Text) theBoldText.clone();
          hotelPickupText.setText(iwrb.getLocalizedString("travel.hotel_pickup","Hotel pick-up"));
      SelectionBox hotels = new SelectionBox(tb.getHotelPickupPlaces(this.supplier));
        hotels.setName("hotelPickupId");
        hotels.keepStatusOnAction();

      Link alink = new Link();
        alink.setText("T-pickupPlaceDesigner");
        alink.setWindowToOpen(HotelPickupPlaceDesigner.class);

      table.add(hotelPickupText,1,row);
      table.add(hotels,2,row);
      table.add(alink,2,row);

      table.setVerticalAlignment(1,row,"top");
      table.setVerticalAlignment(2,row,"top");



      ++row;

      Text nOSText = (Text) theBoldText.clone();
        nOSText.setText("TEMP-Number of seats");
      table.add(nOSText,1,row);
      table.add(numberOfSeats,2,row);

      ++row;
      table.mergeCells(1,row,2,row);
      table.setAlignment(1,row,"right");
      SubmitButton submit = new SubmitButton(iwrb.getImage("buttons/save.gif"),ServiceDesigner.ServiceAction,ServiceDesigner.parameterCreate);
      table.add(submit,1,row);


      table.setColumnAlignment(1,"right");
      table.setColumnAlignment(2,"left");

      if (service != null) {
          Parameter par1 = new Parameter(this.parameterIsUpdate, Integer.toString(tourId));
            par1.keepStatusOnAction();
          table.add(par1);
          Parameter par2 = new Parameter(this.parameterTimeframeId, Integer.toString(timeframe.getID()));
            par2.keepStatusOnAction();
          table.add(par2);

          name.setContent(service.getName());
          description.setContent(service.getDescription());
          active_from.setDate(new idegaTimestamp(timeframe.getFrom()).getSQLDate());
          active_to.setDate(new idegaTimestamp(timeframe.getTo()).getSQLDate());
          active_yearly.setSelected(timeframe.getIfYearly());

          int[] days = ServiceDay.getDaysOfWeek(service.getID());
          for (int i = 0; i < days.length; i++) {
              if (days[i] == ServiceDay.SUNDAY) sundays.setChecked(true);
              else if (days[i] == ServiceDay.MONDAY) mondays.setChecked(true);
              else if (days[i] == ServiceDay.TUESDAY) tuesdays.setChecked(true);
              else if (days[i] == ServiceDay.WEDNESDAY) wednesdays.setChecked(true);
              else if (days[i] == ServiceDay.THURSDAY) thursdays.setChecked(true);
              else if (days[i] == ServiceDay.FRIDAY) fridays.setChecked(true);
              else if (days[i] == ServiceDay.SATURDAY) saturdays.setChecked(true);
          }

          if (depAddress != null)
          departure_from.setContent(depAddress.getStreetName());
          idegaTimestamp tempStamp = new idegaTimestamp(service.getDepartureTime());
          departure_time.setHour(tempStamp.getHour());
          departure_time.setMinute(tempStamp.getMinute());

          if (arrAddress != null)
          arrival_at.setContent(arrAddress.getStreetName());
          tempStamp = new idegaTimestamp(service.getArrivalTime());
          arrival_time.setHour(tempStamp.getHour());
          arrival_time.setMinute(tempStamp.getMinute());

          HotelPickupPlace[] places = tb.getHotelPickupPlaces(service);
          for (int i = 0; i < places.length; i++) {
            hotels.setSelectedElement(Integer.toString(places[i].getID()));
          }


          if (tour.getIsHotelPickup()) {
            hotelPickupYes.setSelected();
          }else {
            hotelPickupNo.setSelected();
          }

          numberOfSeats.setContent(Integer.toString(tour.getTotalSeats()));


      }


      }else {
        table.add("Gögn eru ósamræmd");
      }
      return form;

  }

  public int createTour(IWContext iwc) {
      String sTourId = iwc.getParameter(this.parameterIsUpdate);
      int tourId = -1;
      if (sTourId != null) tourId = Integer.parseInt(sTourId);


      String name = iwc.getParameter("name_of_trip");
      String description = iwc.getParameter("description");
      String imageId = iwc.getParameter("design_image_id");
      String activeFrom = iwc.getParameter("active_from");
      String activeTo = iwc.getParameter("active_to");
      String activeYearly = iwc.getParameter("active_yearly");

      String allDays = iwc.getParameter("all_days");
      String mondays = iwc.getParameter("mondays");
      String tuesdays = iwc.getParameter("tuesdays");
      String wednesdays = iwc.getParameter("wednesdays");
      String thursdays = iwc.getParameter("thursdays");
      String fridays = iwc.getParameter("fridays");
      String saturdays = iwc.getParameter("saturdays");
      String sundays = iwc.getParameter("sundays");

      String departureFrom = iwc.getParameter("departure_from");
      String departureTime = iwc.getParameter("departure_time");
      String arrivalAt = iwc.getParameter("arrival_at");
      String arrivalTime = iwc.getParameter("arrival_time");
      String[] hotelPickup = iwc.getParameterValues("hotelPickupId");

      String numberOfSeats = iwc.getParameter("number_of_seats");
/*
      if (hotelPickup != null) {
        if (hotelPickup.equals("N")) hotelPickupAddress = "";
      }
*/
      int serviceId = -1;

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
        try {
        iNumberOfSeats = new Integer(numberOfSeats);
        }catch (NumberFormatException n) {
          iNumberOfSeats = new Integer(0);
        }
      }else {
        iNumberOfSeats = new Integer(0);
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
/*
      idegaTimestamp hotelPickupTimeStamp = null;
      if (hotelPickupTime != null) {
        hotelPickupTimeStamp = new idegaTimestamp("2001-01-01 "+hotelPickupTime);
      }
*/
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

        if (tourId == -1) {
            tb.setTimeframe(activeFromStamp, activeToStamp, yearly);
            serviceId = tb.createTourService(supplier.getID(),iImageId,name,description,true, departureFrom,departureStamp, arrivalAt, arrivalStamp, hotelPickup,  activeDays, iNumberOfSeats);
        } else {
            String timeframeId = iwc.getParameter(this.parameterTimeframeId);
            tb.setTimeframe(Integer.parseInt(timeframeId), activeFromStamp, activeToStamp, yearly);
            serviceId = tb.updateTourService(tourId,supplier.getID(),iImageId,name,description,true, departureFrom,departureStamp, arrivalAt, arrivalStamp, hotelPickup,  activeDays, iNumberOfSeats);
        }

        /**
         * @todo TravelStockroomBusiness.removeServiceDayHashtable....
         */



      }catch (Exception e) {
        e.printStackTrace(System.err);
        //add("TEMP - Service EKKI smíðuð");
      }


      return serviceId;

  }

}