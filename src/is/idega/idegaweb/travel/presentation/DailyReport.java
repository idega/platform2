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

public class DailyReport extends TravelManager {

  private IWBundle bundle;
  private IWResourceBundle iwrb;

  public DailyReport() {
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


      Text nameText = (Text) theText.clone();
          nameText.setText(iwrb.getLocalizedString("travel.trip_name_lg","Name of trip"));
          nameText.addToText(":");
      Text timeframeText = (Text) theText.clone();
          timeframeText.setText(iwrb.getLocalizedString("travel.date","Date"));
          timeframeText.addToText(":");



      topTable.setColumnAlignment(1,"right");
      topTable.setColumnAlignment(2,"left");
      topTable.add(nameText,1,1);
      topTable.add(trip,2,1);
      topTable.add(timeframeText,1,2);
      topTable.add(active_from,2,2);
      topTable.mergeCells(2,1,4,1);
      topTable.mergeCells(2,2,4,2);

      topTable.setAlignment(4,3,"right");
      topTable.add(new SubmitButton("TEMP-Sækja"),4,3);

      return topTable;
  }

  public Table getContentHeader(ModuleInfo modinfo) {
      Table table = new Table(2,2);
      table.setWidth("95%");


      idegaTimestamp fromStamp = getFromIdegaTimestamp(modinfo);

      String mode = modinfo.getParameter("mode");
      if (mode== null) mode="";


      Text headerText = (Text) theBoldText.clone();
          headerText.setFontColor(NatBusiness.textColor);
          headerText.setText(iwrb.getLocalizedString("travel.daily_report","Daily report"));
          headerText.addToText(" : ");

      Text timeText = (Text) theBoldText.clone();
          timeText.setText(fromStamp.getLocaleDate(modinfo));
          timeText.setFontColor(NatBusiness.textColor);
      Text nameText = (Text) theBoldText.clone();
          nameText.setText("Flippedý flopp");


      table.setColumnAlignment(1,"left");
      table.add(headerText,1,1);
      table.add(nameText,1,1);
      table.setAlignment(2,1,"right");
      table.add(timeText,2,1);


      return table;
  }

  public Table getContentTable(ModuleInfo modinfo) {
      Table theTable = new Table();
          theTable.setBorder(0);
          theTable.setWidth("95%");

      Table table = new Table();
        table.setWidth("100%");
        table.setBorder(1);
        table.setCellspacing(0);
        table.setCellpadding(2);

      int row = 1;

      String twoWidth = "150";
      String threeWidth = "70";
      String fourWidth = "90";
      String fiveWidth = "100";


      idegaTimestamp fromStamp = getFromIdegaTimestamp(modinfo);

      Text nameHText = (Text) theSmallBoldText.clone();
          nameHText.setText(iwrb.getLocalizedString("travel.name","Name"));

      Text payTypeHText = (Text) theSmallBoldText.clone();
          payTypeHText.setText(iwrb.getLocalizedString("travel.payment_type","Payment type"));

      Text bookedHText = (Text) theSmallBoldText.clone();
          bookedHText.setText(iwrb.getLocalizedString("travel.booked_lg","Booked"));

      Text attHText = (Text) theSmallBoldText.clone();
          attHText.setText(iwrb.getLocalizedString("travel.attendance","Attendance"));

      Text amountHText = (Text) theSmallBoldText.clone();
          amountHText.setText(iwrb.getLocalizedString("travel.amount","Amount"));

      Text additionHText = (Text) theSmallBoldText.clone();
          additionHText.setText(iwrb.getLocalizedString("travel.addition","Addition"));

      Text totalHText = (Text) theSmallBoldText.clone();
          totalHText.setText(iwrb.getLocalizedString("travel.total","Total"));

      TextInput textBoxToClone = new TextInput();
          textBoxToClone.setSize(3);
          textBoxToClone.setAttribute("style","font-size: 8pt");
      TextInput attTextBox = new TextInput();


      Text nameText = (Text) smallText.clone();
      Text payTypeText = (Text) smallText.clone();
      Text bookedText = (Text) smallText.clone();
      Text attText = (Text) smallText.clone();
      Text amountText = (Text) smallText.clone();
      Text additionText = (Text) smallText.clone();
      Text totalText = (Text) smallText.clone();


      table.add(nameHText,1,1);
      table.add(payTypeHText,2,1);
      table.add(bookedHText,3,1);
      table.add(attHText,4,1);
      table.add(amountHText,5,1);

      table.setWidth(2,twoWidth);
      table.setWidth(3,threeWidth);
      table.setWidth(4,fourWidth);
      table.setWidth(5,fiveWidth);

      table.setBorderColor(NatBusiness.textColor);

      for (int i = 0; i < 6; i++) {
          row++;
          table.setRowColor(row,NatBusiness.backgroundColor);
          nameText = (Text) smallText.clone();
            nameText.setText("Whacky D");

          attTextBox = (TextInput) textBoxToClone.clone();
            attTextBox.setSize(3);



          table.add(nameText,1,row);

          table.add(attTextBox,4,row);

      }

      table.setColumnAlignment(1,"left");
      table.setColumnAlignment(2,"center");
      table.setColumnAlignment(3,"center");
      table.setColumnAlignment(4,"center");
      table.setColumnAlignment(5,"center");



      Table addTable = new Table();
          addTable.setWidth("100%");
          addTable.setBorder(1);
          addTable.setCellspacing(0);
          addTable.setBorderColor(NatBusiness.textColor);
          addTable.setWidth(2,twoWidth);
          addTable.setWidth(3,threeWidth);
          addTable.setWidth(4,fourWidth);
          addTable.setWidth(5,fiveWidth);
          addTable.setColumnAlignment(1,"left");
          addTable.setColumnAlignment(2,"center");
          addTable.setColumnAlignment(3,"center");
          addTable.setColumnAlignment(4,"center");
          addTable.setColumnAlignment(5,"center");

      Table totalTable = new Table();
          totalTable.setWidth("100%");
          totalTable.setBorder(1);
          totalTable.setCellspacing(0);
          totalTable.setBorderColor(NatBusiness.textColor);
          totalTable.setWidth(2,twoWidth);
          totalTable.setWidth(3,threeWidth);
          totalTable.setWidth(4,fourWidth);
          totalTable.setWidth(5,fiveWidth);
          totalTable.setColumnAlignment(1,"left");
          totalTable.setColumnAlignment(2,"center");
          totalTable.setColumnAlignment(3,"center");
          totalTable.setColumnAlignment(4,"center");
          totalTable.setColumnAlignment(5,"center");

          totalTable.add(totalHText,1,1);















      theTable.add(table);
      theTable.add(additionHText,1,2);
      theTable.setAlignment(1,2,"left");
      theTable.add(addTable,1,3);
      theTable.add(totalTable,1,5);



      return theTable;

  }

}