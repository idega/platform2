package is.idega.travel.presentation;

import com.idega.jmodule.object.JModuleObject;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.block.trade.stockroom.data.*;
import com.idega.jmodule.calendar.presentation.SmallCalendar;
import com.idega.util.idegaTimestamp;
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

public class Booking extends TravelManager {

  private IWBundle bundle;
  private IWResourceBundle iwrb;

  String tableBackgroundColor = "#FFFFFF";
  int numberOfTripsToDiplay = 6;

  String textColor = "#666699";


  public Booking() {
  }


  public void add(ModuleObject mo) {
    super.add(mo);
  }

  public void main(ModuleInfo modinfo) throws SQLException {
      super.main(modinfo);
      initialize(modinfo);


      String action = modinfo.getParameter("action");
      if (action == null) {action = "";}

      if (action.equals("")) {
          displayForm(modinfo);
      }

      super.addBreak();
  }

  public void initialize(ModuleInfo modinfo) {
      bundle = super.getBundle();
      iwrb = super.getResourceBundle();
  }

  public void displayForm(ModuleInfo modinfo) {

      Form form = new Form();
      Table topTable = getTopTable(modinfo);
        form.add(topTable);

      Table contentTable = new Table(1,1);
          contentTable.setBorder(1);
          contentTable.add(getContentHeader(modinfo));
          contentTable.add(getTotalTable(modinfo));
          contentTable.add(getContentTable(modinfo));
          contentTable.setWidth("95%");
          contentTable.setCellspacing(0);
          contentTable.setCellpadding(0);
          contentTable.setBorderColor(NatBusiness.textColor);

      ShadowBox sb = new ShadowBox();
        form.add(sb);
        sb.setWidth("90%");
        sb.setAlignment("center");
        sb.add(contentTable);



      int row = 0;
      add(Text.getBreak());
      add(form);
  }


  public Table getTopTable(ModuleInfo modinfo) {
      Table topTable = new Table(5,1);
        topTable.setBorder(0);
        topTable.setWidth("90%");

      Text tframeText = (Text) theText.clone();
          tframeText.setText(iwrb.getLocalizedString("travel.timeframe_only","Timeframe"));
          tframeText.addToText(":");

      DropdownMenu trip = new DropdownMenu("trip");
          trip.addMenuElement("1","Dropdown af ferðum sem eru til :)");
          trip.addMenuElement("2","Annað dropdown hér ;)");

          String parTrip = modinfo.getParameter("trip");
          if (parTrip != null) {
              trip.setSelectedElement(parTrip);
          }

      idegaTimestamp stamp = idegaTimestamp.RightNow();
      DropdownMenu year = new DropdownMenu("year");
          for (int i = 2000; i < ( stamp.getYear() +4 ); i++) {
              year.addMenuElement(i,""+i);
          }

          String parYear = modinfo.getParameter("year");
          if (parYear != null) {
              year.setSelectedElement(parYear);
          }else {
              year.setSelectedElement(Integer.toString(stamp.getYear()));
          }
          String parMonth = modinfo.getParameter("month");
          String parDay = modinfo.getParameter("day");
          if (parMonth != null) topTable.add(new HiddenInput("month",parMonth));
          if (parDay != null) topTable.add(new HiddenInput("day",parDay));

      Text nameText = (Text) theText.clone();
          nameText.setText(iwrb.getLocalizedString("travel.trip_name_lg","Name of trip"));
          nameText.addToText(":");

      topTable.setColumnAlignment(1,"right");
      topTable.setColumnAlignment(2,"left");
      topTable.add(nameText,1,1);
      topTable.add(trip,2,1);
      topTable.add(tframeText,3,1);
      topTable.add(year,4,1);

      topTable.setAlignment(5,1,"right");
      topTable.add(new SubmitButton("TEMP-Sækja"),5,1);

      return topTable;
  }

  public Table getContentHeader(ModuleInfo modinfo) {
      Table table = new Table(3,4);
      table.setBorder(0);
      table.setWidth("100%");

      Text nameText = (Text) theBoldText.clone();
          nameText.setText(iwrb.getLocalizedString("travel.trip","Trip"));
      Text timeText = (Text) theBoldText.clone();
          timeText.setText(iwrb.getLocalizedString("travel.timeframe","Timeframe"));

      Image image = new Image("/pics/mynd.gif");


      Text departureFromText = (Text) theBoldText.clone();
          departureFromText.setText(iwrb.getLocalizedString("travel.departure_from","Departure from"));
      Text departureTimeText = (Text) theBoldText.clone();
          departureTimeText.setText(iwrb.getLocalizedString("travel.departure_time","Departure time"));

      table.mergeCells(1,1,1,4);

      table.add(image,1,1);
      table.setWidth(1,"150");
      table.setColumnAlignment(2,"left");
      table.setColumnAlignment(3,"left");
      table.add(nameText,2,1);
      table.add(timeText,3,1);
      table.add(departureFromText,2,3);
      table.add(departureTimeText,3,3);

      return table;
  }

  public Table getContentTable(ModuleInfo modinfo) {
      Table table = new Table();
        table.setWidth("100%");
        table.setBorder(0);
        table.setCellspacing(0);
        table.setCellpadding(2);
        table.setWidth(6,"200");

      int row = 1;

      idegaTimestamp stamp = getIdegaTimestamp(modinfo);


      table.mergeCells(1,row,5,row);
      table.add(getInqueries(),1,row);
      table.setColor(1,row,NatBusiness.YELLOW);

      table.setColor(6,row,NatBusiness.backgroundColor);
      table.setVerticalAlignment(6,row,"top");
      table.add(getCalendar(modinfo),6,row);

      ++row;
      table.mergeCells(1,row,5,row);
      table.setColor(1,row,NatBusiness.backgroundColor );
      table.mergeCells(6,1,6,row);
      table.add(getBookingFormTable(),1,row);

      return table;

  }


  public Table getCalendar(ModuleInfo modinfo) {
      Table table = new Table(4,5);
          table.setBorder(0);
          table.setColor(NatBusiness.backgroundColor);
          table.setAlignment("center");


      idegaCalendar cal = new idegaCalendar();
      idegaTimestamp stamp = getIdegaTimestamp(modinfo);

      Text jan = (Text) theText.clone();
        jan.setText(cal.getShortNameOfMonth(1,modinfo));
      Text feb = (Text) theText.clone();
        feb.setText(cal.getShortNameOfMonth(2,modinfo));
      Text mar = (Text) theText.clone();
        mar.setText(cal.getShortNameOfMonth(3,modinfo));
      Text apr = (Text) theText.clone();
        apr.setText(cal.getShortNameOfMonth(4,modinfo));
      Text may = (Text) theText.clone();
        may.setText(cal.getShortNameOfMonth(5,modinfo));
      Text jun = (Text) theText.clone();
        jun.setText(cal.getShortNameOfMonth(6,modinfo));
      Text jul = (Text) theText.clone();
        jul.setText(cal.getShortNameOfMonth(7,modinfo));
      Text aug = (Text) theText.clone();
        aug.setText(cal.getShortNameOfMonth(8,modinfo));
      Text sep = (Text) theText.clone();
        sep.setText(cal.getShortNameOfMonth(9,modinfo));
      Text oct = (Text) theText.clone();
        oct.setText(cal.getShortNameOfMonth(10,modinfo));
      Text nov = (Text) theText.clone();
        nov.setText(cal.getShortNameOfMonth(11,modinfo));
      Text dec = (Text) theText.clone();
        dec.setText(cal.getShortNameOfMonth(12,modinfo));

      Link lJan = new Link(jan,Booking.class);
        lJan.addParameter("year",stamp.getYear());
        lJan.addParameter("month",1);
        lJan.addParameter("day",stamp.getDate());
      Link lFeb = new Link(feb,Booking.class);
        lFeb.addParameter("year",stamp.getYear());
        lFeb.addParameter("month",2);
        lFeb.addParameter("day",stamp.getDate());
      Link lMar = new Link(mar,Booking.class);
        lMar.addParameter("year",stamp.getYear());
        lMar.addParameter("month",3);
        lMar.addParameter("day",stamp.getDate());
      Link lApr = new Link(apr,Booking.class);
        lApr.addParameter("year",stamp.getYear());
        lApr.addParameter("month",4);
        lApr.addParameter("day",stamp.getDate());
      Link lMay = new Link(may,Booking.class);
        lMay.addParameter("year",stamp.getYear());
        lMay.addParameter("month",5);
        lMay.addParameter("day",stamp.getDate());
      Link lJun = new Link(jun,Booking.class);
        lJun.addParameter("year",stamp.getYear());
        lJun.addParameter("month",6);
        lJun.addParameter("day",stamp.getDate());
      Link lJul = new Link(jul,Booking.class);
        lJul.addParameter("year",stamp.getYear());
        lJul.addParameter("month",7);
        lJul.addParameter("day",stamp.getDate());
      Link lAug = new Link(aug,Booking.class);
        lAug.addParameter("year",stamp.getYear());
        lAug.addParameter("month",8);
        lAug.addParameter("day",stamp.getDate());
      Link lSep = new Link(sep,Booking.class);
        lSep.addParameter("year",stamp.getYear());
        lSep.addParameter("month",9);
        lSep.addParameter("day",stamp.getDate());
      Link lOct = new Link(oct,Booking.class);
        lOct.addParameter("year",stamp.getYear());
        lOct.addParameter("month",10);
        lOct.addParameter("day",stamp.getDate());
      Link lNov = new Link(nov,Booking.class);
        lNov.addParameter("year",stamp.getYear());
        lNov.addParameter("month",11);
        lNov.addParameter("day",stamp.getDate());
      Link lDec = new Link(dec,Booking.class);
        lDec.addParameter("year",stamp.getYear());
        lDec.addParameter("month",12);
        lDec.addParameter("day",stamp.getDate());

      table.add(lJan,1,1);
      table.add(lFeb,2,1);
      table.add(lMar,3,1);
      table.add(lApr,4,1);
      table.add(lMay,1,2);
      table.add(lJun,2,2);
      table.add(lJul,3,2);
      table.add(lAug,4,2);
      table.add(lSep,1,3);
      table.add(lOct,2,3);
      table.add(lNov,3,3);
      table.add(lDec,4,3);

//        table.add(new SmallCalendar(),1,5);

      SmallCalendar sm = new SmallCalendar(stamp);
          sm.T.setBorder(1);
          sm.T.setCellpadding(2);
          sm.T.setCellspacing(0);
          sm.T.setBorderColor(NatBusiness.backgroundColor);
          sm.useNextAndPreviousLinks(false);
          sm.setBackgroundColor(NatBusiness.backgroundColor);
          sm.setTextColor("WHITE");
          sm.setDaysAsLink(true);
          sm.showNameOfDays(false);
          sm.setHeaderTextColor("#666699");
          sm.setHeaderColor(NatBusiness.textColor);
          sm.setBodyColor(NatBusiness.textColor);
          sm.setInActiveCellColor(NatBusiness.BLUE);
          sm.setDayColor(2001,7,1,NatBusiness.RED);
          sm.setDayColor(2001,7,4,NatBusiness.RED);
          sm.setDayColor(2001,8,11,NatBusiness.RED);
          sm.setDayColor(2001,8,14,NatBusiness.RED);
          sm.setDayColor(2001,7,21,NatBusiness.RED);
          sm.setDayColor(2001,7,24,NatBusiness.RED);

      table.mergeCells(1,5,4,5);
      table.add(sm,1,5);

      return table;
  }


  public Table getInqueries() {
      Table table = new Table();
          table.setWidth("100%");
          table.setBorder(0);

      int row = 0;

      table.setWidth(1,"100");

      Text dateText;
      Text nameText;
      Text countText;
      Text contentText;

      for (int i = 0; i < 2; i++) {
          dateText = (Text) theSmallBoldText.clone();
              dateText.setText("1. júlí 2001");
          nameText = (Text) theSmallBoldText.clone();
              nameText.setText("Mökkur Brick Skorsteinn");
          countText = (Text) theSmallBoldText.clone();
              countText.setText("5");
          contentText = (Text) theSmallBoldText.clone();
              contentText.setText("Er kannski laus ferð hér ?");


          ++row;
          table.setAlignment(1,row,"left");
          table.setAlignment(2,row,"left");
          table.setAlignment(3,row,"right");
          table.add(dateText,1,row);
          table.add(nameText,2,row);
          table.add(countText,3,row);

          ++row;
          ++row;
          table.mergeCells(2,row,3,row);
          table.setAlignment(2,row,"left");
          table.add(contentText,2,row);

          ++row;
          ++row;
          table.mergeCells(2,row,3,row);
          table.setAlignment(2,row,"right");
          table.add(new SubmitButton("TEMP-SVARA"),2,row);

      }



      return table;
  }


  public Table getTotalTable(ModuleInfo modinfo) {
      Table table = new Table();
        table.setFrameVoid();
        table.setWidth("100%");
        table.setCellspacing(0);
        table.setBorder(1);
        table.setBorderColor(NatBusiness.textColor);
        int row = 1;

      String cellWidth = "60";
      table.setWidth(2,cellWidth);
      table.setWidth(3,cellWidth);
      table.setWidth(4,cellWidth);
      table.setWidth(5,cellWidth);
      table.setWidth(6,"200");

      idegaTimestamp stamp = getIdegaTimestamp(modinfo);

      Text dateText = (Text) theBoldText.clone();
          dateText.setText(stamp.getLocaleDate(modinfo));
      Text countText = (Text) theText.clone();
          countText.setText(iwrb.getLocalizedString("travel.count_sm","count"));
      Text assignedText = (Text) theText.clone();
          assignedText.setText(iwrb.getLocalizedString("travel.assigned_small_sm","assigned"));
      Text inqText = (Text) theText.clone();
          inqText.setText(iwrb.getLocalizedString("travel.inqueries_small_sm","inq."));
      Text bookedText = (Text) theText.clone();
          bookedText.setText(iwrb.getLocalizedString("travel.booked_sm","booked"));
      Text availableText = (Text) theText.clone();
          availableText.setText(iwrb.getLocalizedString("travel.available_small_sm","avail."));
      Text bookingStatusText = (Text) theBoldText.clone();
          bookingStatusText.setText(iwrb.getLocalizedString("travel.booking_status","Booking status"));
      Text calendarForBooking = (Text) theText.clone();
          calendarForBooking.setText(iwrb.getLocalizedString("travel.calendar_for_booking","Calendar for booking"));


      Text dateTextBold = (Text) theSmallBoldText.clone();
      Text nameTextBold = (Text) theSmallBoldText.clone();
      Text countTextBold = (Text) theSmallBoldText.clone();
      Text assignedTextBold = (Text) theSmallBoldText.clone();
      Text inqTextBold = (Text) theSmallBoldText.clone();
      Text bookedTextBold = (Text) theSmallBoldText.clone();
      Text availableTextBold = (Text) theSmallBoldText.clone();



      table.add(dateText,1,row);

      table.add(countText,2,row);
      table.add(bookedText,3,row);
      table.add(inqText,4,row);
      table.add(availableText,5,row);
      table.add(calendarForBooking,6,row);

      ++row;
      table.add(bookingStatusText,1,row);
      table.setColor(2,row,NatBusiness.backgroundColor);
      table.setColor(3,row,NatBusiness.RED);
      table.setColor(4,row,NatBusiness.YELLOW);
      table.setColor(5,row,NatBusiness.LIGHTGREEN);


      table.setColumnAlignment(1,"left");
      table.setColumnAlignment(2,"center");
      table.setColumnAlignment(3,"center");
      table.setColumnAlignment(4,"center");
      table.setColumnAlignment(5,"center");
      table.setColumnAlignment(6,"center");

      return table;
  }

  public Table getBookingFormTable() {
      Table table = new Table(6,7);
          table.setWidth("100%");

      table.setColumnAlignment(1,"right");
      table.setColumnAlignment(2,"left");
      table.setColumnAlignment(3,"right");
      table.setColumnAlignment(4,"left");


      int row = 1;
      int textInputSizeLg = 18;
      int textInputSizeSm = 2;

      Text adultsText = (Text) theText.clone();
          adultsText.setText(iwrb.getLocalizedString("travel.adults","adults"));
      Text childrenText = (Text) theText.clone();
          childrenText.setText(iwrb.getLocalizedString("travel.children","children"));
          childrenText.addToText(" : ");
      Text age03Text = (Text) theText.clone();
          age03Text.setText(iwrb.getLocalizedString("travel.age_0_3","age 0-3"));
      Text age411Text = (Text) theText.clone();
          age411Text.setText(iwrb.getLocalizedString("travel.age_4_11","age 4-11"));
      Text surnameText = (Text) theText.clone();
          surnameText.setText(iwrb.getLocalizedString("travel.surname","surname"));
      Text lastnameText = (Text) theText.clone();
          lastnameText.setText(iwrb.getLocalizedString("travel.last_name","last name"));
      Text addressText = (Text) theText.clone();
          addressText.setText(iwrb.getLocalizedString("travel.address","address"));
      Text areaCodeText = (Text) theText.clone();
          areaCodeText.setText(iwrb.getLocalizedString("travel.area_code","area code"));
      Text emailText = (Text) theText.clone();
          emailText.setText(iwrb.getLocalizedString("travel.email","e-mail"));
      Text telNumberText = (Text) theText.clone();
          telNumberText.setText(iwrb.getLocalizedString("travel.telephone_number","telephone number"));


      TextInput adults = new TextInput("adults");
          adults.setSize(textInputSizeSm);
      TextInput age03 = new TextInput("age_0_3");
          age03.setSize(textInputSizeSm);
      TextInput age411 = new TextInput("age_4_11");
          age411.setSize(textInputSizeSm);
      TextInput surname = new TextInput("surname");
          surname.setSize(textInputSizeLg);
      TextInput lastname = new TextInput("lastname");
          lastname.setSize(textInputSizeLg);
      TextInput address = new TextInput("address");
          address.setSize(textInputSizeLg);
      TextInput areaCode = new TextInput("area_code");
          areaCode.setSize(textInputSizeLg);
      TextInput email = new TextInput("e-mail");
          email.setSize(textInputSizeLg);
      TextInput telNumber = new TextInput("telephone_number");
          telNumber.setSize(textInputSizeLg);



      ++row;
      table.add(adultsText,1,row);
      table.add(adults,2,row);
      table.add(childrenText,3,row);
      table.add(age03Text,3,row);
      table.add(age03,4,row);
      table.add(age411Text,4,row);
      table.add(age411,4,row);

      ++row;
      table.add(surnameText,1,row);
      table.add(surname,2,row);
      table.add(lastnameText,3,row);
      table.add(lastname,4,row);

      ++row;
      table.add(addressText,1,row);
      table.add(address,2,row);
      table.add(areaCodeText,3,row);
      table.add(areaCode,4,row);

      ++row;
      table.add(emailText,1,row);
      table.add(email,2,row);
      table.add(telNumberText,3,row);
      table.add(telNumber,4,row);


      ++row;
      ++row;
      table.add(new SubmitButton("TEMP BÓKA"),4,row);
      table.setAlignment(4,row,"right");



      return table;
  }


  // BUSINESS
  public idegaTimestamp getIdegaTimestamp(ModuleInfo modinfo) {
      idegaTimestamp stamp = null;
      String year = modinfo.getParameter("year");
      String month = modinfo.getParameter("month");
      String day = modinfo.getParameter("day");

      try {
          if ( (day != null) && (month != null) && (year != null)) {
              stamp = new idegaTimestamp(day,month,year);
          }
          else if ((day == null) && (month == null) && (year != null)) {
              stamp = new idegaTimestamp(1,idegaTimestamp.RightNow().getMonth(),Integer.parseInt(year));
          }
          else if ((day == null) && (month != null) && (year != null)) {
              stamp = new idegaTimestamp(1,Integer.parseInt(month),Integer.parseInt(year));
          }
          else {
              stamp = idegaTimestamp.RightNow();
          }
      }
      catch (Exception e) {
          stamp = idegaTimestamp.RightNow();
      }

      return stamp;
  }





}