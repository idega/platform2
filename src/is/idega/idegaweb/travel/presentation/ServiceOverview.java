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

public class ServiceOverview extends JModuleObject {

  private  TravelManager tm;

  private IWBundle bundle;
  private IWResourceBundle iwrb;

  String tableBackgroundColor = "#FFFFFF";
  int numberOfTripsToDiplay = 3;

  public ServiceOverview() {
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
      Table topTable = new Table();
        form.add(topTable);
        topTable.setBorder(0);
        topTable.setWidth("90%");
      Table table = new Table();
        table.setBorder(0);
        table.setWidth(1,"150");
      ShadowBox sb = new ShadowBox();
        form.add(sb);
        sb.setWidth("90%");
        sb.setAlignment("center");
        sb.add(table);

      table.setWidth("95%");
      String sYear = modinfo.getParameter("year");
      if (sYear == null) {
          sYear = Text.emptyString().toString();
      }

      int row = 0;
      idegaTimestamp stamp = idegaTimestamp.RightNow();

      Text theText = new Text();
          theText.setFontSize(Text.FONT_SIZE_10_HTML_2);
          theText.setFontFace(Text.FONT_FACE_VERDANA);

      Text theBoldText = new Text();
          theBoldText.setFontSize(Text.FONT_SIZE_10_HTML_2);
          theBoldText.setFontFace(Text.FONT_FACE_VERDANA);
          theBoldText.setBold();

      Text smallText = new Text();
          smallText.setFontSize(Text.FONT_SIZE_7_HTML_1);
          smallText.setFontFace(Text.FONT_FACE_VERDANA);

      Text tframeText = (Text) theText.clone();
          tframeText.setText(iwrb.getLocalizedString("travel.timeframe_only","Timeframe"));
          tframeText.addToText(":");
      DropdownMenu year = new DropdownMenu("year");
          year.addMenuElement((stamp.getYear()-1),""+(stamp.getYear()-1));
          year.addMenuElement(stamp.getYear(),""+stamp.getYear());
          year.addMenuElement((stamp.getYear()+1),""+(stamp.getYear()+1));
          year.addMenuElement((stamp.getYear()+2),""+(stamp.getYear()+2));
          year.addMenuElement((stamp.getYear()+3),""+(stamp.getYear()+3));
          if (!sYear.equals(Text.emptyString().toString())) {
              year.setSelectedElement(sYear);
          }else {
              year.setSelectedElement(""+stamp.getYear());
          }

      Text nameText = (Text) theText.clone();
          nameText.setText(iwrb.getLocalizedString("travel.name_of_trip","Name of trip"));
          nameText.addToText(":");
      Text timeframeText = (Text) theText.clone();
          timeframeText.setText(iwrb.getLocalizedString("travel.timeframe_only","Timeframe"));
          timeframeText.addToText(":");
      Text departureFromText = (Text) theText.clone();
          departureFromText.setText(iwrb.getLocalizedString("travel.departure_from","Departure from"));
          departureFromText.addToText(":");
      Text departureTimeText = (Text) theText.clone();
          departureTimeText.setText(iwrb.getLocalizedString("travel.departure_time","Departure time"));
          departureTimeText.addToText(":");

      Image image = new Image("/pics/mynd.gif");

      for (int i = 0; i < numberOfTripsToDiplay; i++) {
          Text flipp = (Text) theBoldText.clone();
              flipp.setText("Flipp");

          ++row;
          table.mergeCells(1,row,1,row+3);
          table.add(image,1,row);
          table.add(nameText,2,row);
          table.add(timeframeText,4,row);
          table.setAlignment(2,row,"right");
          table.setAlignment(3,row,"left");
          table.setAlignment(4,row,"right");
          table.setAlignment(5,row,"left");
          table.add(flipp,3,row);
          table.add(flipp,5,row);

          ++row;
          table.add(departureFromText,2,row);
          table.add(departureTimeText,4,row);
          table.setAlignment(2,row,"right");
          table.setAlignment(3,row,"left");
          table.setAlignment(4,row,"right");
          table.setAlignment(5,row,"left");
          table.add(flipp,3,row);
          table.add(flipp,5,row);

          ++row;

          ++row;
          table.mergeCells(2,row,5,row);
          table.setAlignment(2,row,"right");
          table.add("Takkar hér",2,row);
          table.setColor(2,row,NatBusiness.backgroundColor);

          ++row;
          table.mergeCells(1,row,5,row);

      }

      topTable.setAlignment(1,1,"right");
      topTable.add(tframeText);
      topTable.add(year);
      topTable.add(new SubmitButton("TEMP-Sækja"));


      add(form);
  }



}