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

public class ServiceDesigner extends JModuleObject {

  private  TravelManager tm;

  private IWBundle bundle;
  private IWResourceBundle iwrb;

  String tableBackgroundColor = "#FFFFFF";


  public ServiceDesigner() {
  }

  public void add(ModuleObject mo) {
    tm.add(mo);
  }

  public String getBundleIdentifier(){
    return TravelManager.IW_BUNDLE_IDENTIFIER;
  }



  public void main(ModuleInfo modinfo) {
      tm = new TravelManager();
      bundle = getBundle(modinfo);
      iwrb = bundle.getResourceBundle(modinfo.getCurrentLocale());

      String action = modinfo.getParameter("action");
      if (action == null) {action = "";}

      if (action.equals("")) {
          displayForm(modinfo);
      }

      tm.addBreak();
      super.add(tm);
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

      Text theText = new Text();
          theText.setFontSize(Text.FONT_SIZE_10_HTML_2);
          theText.setFontFace(Text.FONT_FACE_VERDANA);

      Text smallText = new Text();
          smallText.setFontSize(Text.FONT_SIZE_7_HTML_1);
          smallText.setFontFace(Text.FONT_FACE_VERDANA);


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


      TextInput departure_from = new TextInput("departure_from");
          departure_from.setSize(40);
      TimeInput departure_time = new TimeInput("departure_time");
          departure_time.setHour(8);
          departure_time.setMinute(0);
      RadioButton hotelPickupYes = new RadioButton("hotel_pickup","yes");
          hotelPickupYes.setSelected();
      RadioButton hotelPickupNo = new RadioButton("hotel_pickup","no");





      ++row;
      Text nameText = (Text) theText.clone();
          nameText.setText(iwrb.getLocalizedString("travel.name_of_trip","Name of trip"));
      table.add(nameText,1,row);
      table.add(name,2,row);

      ++row;

      Text descText = (Text) theText.clone();
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
      Text timeframeText = (Text) theText.clone();
        timeframeText.setText(iwrb.getLocalizedString("travel.timeframe","Timeframe"));
      Text tfFromText = (Text) theText.clone();
        tfFromText.setText(iwrb.getLocalizedString("travel.from","from"));
      Text tfToText = (Text) theText.clone();
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

      Text weekdaysText = (Text) theText.clone();
          weekdaysText.setText(iwrb.getLocalizedString("travel.weekdays","Weekdays"));
      table.add(weekdaysText,1,row);
      table.add(weekdayFixTable,2,row);

      ++row;
      Text departureFromText = (Text) theText.clone();
          departureFromText.setText(iwrb.getLocalizedString("travel.departure_from","Departure from"));
      table.add(departureFromText,1,row);
      table.add(departure_from,2,row);

      ++row;
      Text departureTimeText = (Text) theText.clone();
          departureTimeText.setText(iwrb.getLocalizedString("travel.departure_time","Departure time"));
      table.add(departureTimeText,1,row);
      table.add(departure_time,2,row);

      ++row;
      Text hotelPickupText = (Text) theText.clone();
          hotelPickupText.setText(iwrb.getLocalizedString("travel.hotel_pickup","Hotel pick-up"));

      Table hotelPickupFixTable = new Table(3,2);
        hotelPickupFixTable.setCellpadding(0);
        hotelPickupFixTable.setCellspacing(1);
        hotelPickupFixTable.setColumnAlignment(1,"center");
        hotelPickupFixTable.setColumnAlignment(2,"center");
        hotelPickupFixTable.setColumnAlignment(3,"center");


      Text yesText = (Text) smallText.clone();
        yesText.setText(iwrb.getLocalizedString("travel.yes","yes"));
      Text noText = (Text) smallText.clone();
        noText.setText(iwrb.getLocalizedString("travel.no","no"));


      hotelPickupFixTable.add(yesText,1,1);
      hotelPickupFixTable.add(noText,3,1);
      hotelPickupFixTable.add(hotelPickupYes,1,2);
      hotelPickupFixTable.add(hotelPickupNo,3,2);

      table.add(hotelPickupText,1,row);
      table.add(hotelPickupFixTable,2,row);

      table.setColumnAlignment(1,"right");
      table.setColumnAlignment(2,"left");

      add(form);
  }




}