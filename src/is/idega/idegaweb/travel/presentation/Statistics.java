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

public class Statistics extends JModuleObject {

  private  TravelManager tm;

  private IWBundle bundle;
  private IWResourceBundle iwrb;

  String tableBackgroundColor = "#FFFFFF";
  int numberOfTripsToDiplay = 6;

  Text theText = new Text();
  Text theBoldText = new Text();
  Text smallText = new Text();
  Text theSmallBoldText = new Text();

  public Statistics() {
  }

  public void add(ModuleObject mo) {
    tm.add(mo);
  }

  public String getBundleIdentifier(){
    return TravelManager.IW_BUNDLE_IDENTIFIER;
  }



  public void main(ModuleInfo modinfo) {
      initialize(modinfo);

      String action = modinfo.getParameter("action");
      if (action == null) {action = "";}

      if (action.equals("")) {
          displayForm(modinfo);
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
      theBoldText.setFontSize(Text.FONT_SIZE_10_HTML_2);
      theBoldText.setFontFace(Text.FONT_FACE_VERDANA);
      theBoldText.setBold();
      smallText.setFontSize(Text.FONT_SIZE_7_HTML_1);
      smallText.setFontFace(Text.FONT_FACE_VERDANA);
      theSmallBoldText.setFontFace(Text.FONT_FACE_VERDANA);
      theSmallBoldText.setFontSize(Text.FONT_SIZE_7_HTML_1);
      theSmallBoldText.setBold();
  }

  public void displayForm(ModuleInfo modinfo) {

      Form form = new Form();
      Table topTable = getTopTable(modinfo);
        form.add(topTable);
      Table table = getContentTable(modinfo);
      ShadowBox sb = new ShadowBox();
        form.add(sb);
        sb.setWidth("90%");
        sb.setAlignment("center");
        sb.add(getContentHeader(modinfo));
        sb.add(table);

      Paragraph par = new Paragraph();
        par.setAlign("right");
        par.add(new PrintButton("TEMP-PRENTA"));
        sb.add(par);


      int row = 0;
      add(Text.getBreak());
      add(form);
  }


  // BUSINESS
  public idegaTimestamp getFromIdegaTimestamp(ModuleInfo modinfo) {
      idegaTimestamp stamp = null;
      String from_time = modinfo.getParameter("active_from");
      if (from_time!= null) {
          try {
              stamp = new idegaTimestamp(from_time);
          }
          catch (Exception e) {
              stamp = idegaTimestamp.RightNow();
          }
      }
      else {
          stamp = idegaTimestamp.RightNow();
      }
      return stamp;
  }

  // BUSINESS
  public idegaTimestamp getToIdegaTimestamp(ModuleInfo modinfo) {
      idegaTimestamp stamp = null;
      String from_time = modinfo.getParameter("active_to");
      if (from_time!= null) {
          try {
              stamp = new idegaTimestamp(from_time);
          }
          catch (Exception e) {
              stamp = idegaTimestamp.RightNow();
              stamp.addDays(15);
          }
      }
      else {
          stamp = idegaTimestamp.RightNow();
          stamp.addDays(15);
      }
      return stamp;
  }


  public Table getTopTable(ModuleInfo modinfo) {
      Table topTable = new Table(4,3);
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


      DateInput active_from = new DateInput("active_from");
          idegaTimestamp fromStamp = getFromIdegaTimestamp(modinfo);
          active_from.setDate(fromStamp.getSQLDate());
      DateInput active_to = new DateInput("active_to");
          idegaTimestamp toStamp = getToIdegaTimestamp(modinfo);
          active_to.setDate(toStamp.getSQLDate());

      Text tfFromText = (Text) theText.clone();
          tfFromText.setText(iwrb.getLocalizedString("travel.from","from"));
      Text tfToText = (Text) theText.clone();
          tfToText.setText(iwrb.getLocalizedString("travel.to","to"));


      Text nameText = (Text) theText.clone();
          nameText.setText(iwrb.getLocalizedString("travel.trip_name_lg","Name of trip"));
          nameText.addToText(":");
      Text timeframeText = (Text) theText.clone();
          timeframeText.setText(iwrb.getLocalizedString("travel.timeframe_only","Timeframe"));
          timeframeText.addToText(":");



      topTable.setColumnAlignment(1,"right");
      topTable.setColumnAlignment(2,"left");
      topTable.add(nameText,1,1);
      topTable.add(trip,2,1);
      topTable.add(timeframeText,1,2);
      topTable.add(tfFromText,1,2);
      topTable.add(active_from,2,2);
      topTable.add(tfToText,2,2);
      topTable.add(active_to,2,2);
      topTable.mergeCells(2,1,4,1);
      topTable.mergeCells(2,2,4,2);

      topTable.setAlignment(4,3,"right");
      topTable.add(new SubmitButton("TEMP-Sækja"),4,3);

      return topTable;
  }

  public Table getContentHeader(ModuleInfo modinfo) {
      Table table = new Table(2,3);
      table.setWidth("95%");


      idegaTimestamp fromStamp = getFromIdegaTimestamp(modinfo);
      idegaTimestamp toStamp = getToIdegaTimestamp(modinfo);

      String mode = modinfo.getParameter("mode");
      if (mode== null) mode="";


      Text headerText = (Text) theBoldText.clone();
          headerText.setFontColor(NatBusiness.textColor);
          headerText.setText(iwrb.getLocalizedString("travel.name_of_trip_lg","Name of trip"));
          headerText.addToText(" : ");

      Text timeframeText = (Text) theBoldText.clone();
          timeframeText.setFontColor(NatBusiness.textColor);
          timeframeText.setText(iwrb.getLocalizedString("travel.timeframe","Timeframe"));
          timeframeText.addToText(" : ");

      Text timeText = (Text) theText.clone();
          timeText.setText(fromStamp.getLocaleDate(modinfo)+" - "+toStamp.getLocaleDate(modinfo));
      Text toTimeText = (Text) theText.clone();
          toTimeText.setText(toStamp.getLocaleDate(modinfo));
      Text nameText = (Text) theText.clone();
          nameText.setText("Flippedý flopp");
      Text statusText = (Text) theBoldText.clone();
          statusText.setFontColor(NatBusiness.textColor);
          statusText.setText(iwrb.getLocalizedString("travel.status","Status"));
          statusText.addToText(" : ");


      table.setColumnAlignment(1,"left");
      table.add(headerText,1,1);
      table.add(nameText,1,1);
      table.setAlignment(2,1,"right");
      table.add(timeframeText,2,1);
      table.add(timeText,2,1);
      table.add(statusText,1,2);
      table.add(toTimeText,1,2);


      return table;
  }

  public Table getContentTable(ModuleInfo modinfo) {
      Table table = new Table();
        table.setWidth("95%");
        table.setBorder(1);
        table.setCellspacing(0);
        table.setCellpadding(2);

      int row = 0;
      idegaTimestamp fromStamp = getFromIdegaTimestamp(modinfo);
      idegaTimestamp toStamp = getToIdegaTimestamp(modinfo);
          toStamp.addDays(1);

      Text netBookText = (Text) smallText.clone();
          netBookText.setText(iwrb.getLocalizedString("travel.bookings_on_the_net","Bookings on the net"));

      Text inqText = (Text) smallText.clone();
          inqText.setText(iwrb.getLocalizedString("travel.bookings_from_inqueries","Bookings from inqueries"));

      Text supplText = (Text) smallText.clone();
          supplText.setText(iwrb.getLocalizedString("travel.bookings_from_supplier","Booked by supplier"));

      Text travelText = (Text) smallText.clone();
          travelText.setText(iwrb.getLocalizedString("travel.bookings_from_travel_agencies","Booked by travel agencies"));

      Text availText = (Text) smallText.clone();
          availText.setText(iwrb.getLocalizedString("travel.available","Available"));

      Text passText = (Text) smallText.clone();
          passText.setText(iwrb.getLocalizedString("travel.number_of_passengers","Number of passengers"));

      Text seatText = (Text) smallText.clone();
          seatText.setText(iwrb.getLocalizedString("travel.number_of_seats","Number of seats"));

      Text usageText = (Text) smallText.clone();
          usageText.setText(iwrb.getLocalizedString("travel.seat_usage","Seat usage"));



      Text netBookNrText = (Text) smallText.clone();
      Text inqNrText = (Text) smallText.clone();
      Text supplNrText = (Text) smallText.clone();
      Text travelNrText = (Text) smallText.clone();
      Text availNrText = (Text) smallText.clone();
      Text passNrText = (Text) smallText.clone();
      Text seatNrText = (Text) smallText.clone();
      Text usageNrText = (Text) smallText.clone();

          netBookNrText.setText("460");
          inqNrText.setText("260");
          supplNrText.setText("500");
          travelNrText.setText("150");
          availNrText.setText("90");
          passNrText.setText("1220");
          seatNrText.setText("1310");
          usageNrText.setText("83,6%");




      table.add(netBookText,1,1);
      table.add(inqText,1,2);
      table.add(supplText,1,3);
      table.add(travelText,1,4);
      table.add(availText,1,5);
      table.add(passText,1,6);
      table.add(seatText,1,7);
      table.add(usageText,1,8);

      table.add(netBookNrText,2,1);
      table.add(inqNrText,2,2);
      table.add(supplNrText,2,3);
      table.add(travelNrText,2,4);
      table.add(availNrText,2,5);
      table.add(passNrText,2,6);
      table.add(seatNrText,2,7);
      table.add(usageNrText,2,8);


      table.setColumnAlignment(1,"left");
      table.setColumnAlignment(2,"center");
      table.setWidth(2,"100");

      table.setBorderColor(NatBusiness.textColor);
      table.setRowColor(1,NatBusiness.backgroundColor);
      table.setRowColor(2,NatBusiness.YELLOW);
      table.setRowColor(3,NatBusiness.ORANGE);
      table.setRowColor(4,NatBusiness.BLUE);
      table.setRowColor(5,NatBusiness.GREEN);
      table.setRowColor(6,NatBusiness.LIGHTORANGE);
      table.setRowColor(7,NatBusiness.RED);
      table.setRowColor(8,NatBusiness.LIGHTGREEN);


      return table;

  }

}