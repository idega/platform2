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
import com.idega.util.text.TextSoap;
import com.idega.core.accesscontrol.business.AccessControl;
import com.idega.projects.nat.business.NatBusiness;
import is.idega.travel.business.TravelStockroomBusiness;
import java.sql.SQLException;

import is.idega.travel.data.*;
import com.idega.core.data.*;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class ServiceOverview extends TravelManager {

  private IWBundle bundle;
  private IWResourceBundle iwrb;

  String tableBackgroundColor = "#FFFFFF";
  int numberOfTripsToDiplay = 3;

  public ServiceOverview() {
  }

  public void add(ModuleObject mo) {
    super.add(mo);
  }


  public void main(ModuleInfo modinfo) throws SQLException{
      super.main(modinfo);
      bundle = super.getBundle();
      iwrb = super.getResourceBundle();

      String action = modinfo.getParameter("action");
      if (action == null) {action = "";}

      if (action.equals("")) {
          displayForm(modinfo);
      }

      super.addBreak();
      //super.add(tm);
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

      Supplier supplier = super.getSupplier();
      if (supplier != null) {
        TravelStockroomBusiness tsb = TravelStockroomBusiness.getNewInstance();
        Product[] products = tsb.getProducts(supplier.getID(), stamp);

        Service service;
        Timeframe timeframe;
        Address address;

//        Tour tour;

        for (int i = 0; i < products.length; i++) {
          try {
            service = TravelStockroomBusiness.getService(products[i]);
            timeframe = TravelStockroomBusiness.getTimeframe(products[i]);
            address = service.getAddress();
//            tour = TravelStockroomBusiness.getTour(products[i]);


            Text prodName = (Text) theBoldText.clone();
                prodName.setText(service.getName());

            Text timeframeTxt = (Text) theBoldText.clone();
                timeframeTxt.setText(new idegaTimestamp(timeframe.getFrom()).getLocaleDate(modinfo) + " - " +new idegaTimestamp(timeframe.getTo()).getLocaleDate(modinfo));

            Text depFrom = (Text) theBoldText.clone();
                depFrom.setText(address.getStreetName());

            idegaTimestamp depTimeStamp = new idegaTimestamp(service.getDepartureTime());
            Text depTime = (Text) theBoldText.clone();
                depTime.setText(TextSoap.addZero(depTimeStamp.getHour())+":"+TextSoap.addZero(depTimeStamp.getMinute()));


            ++row;
            table.mergeCells(1,row,1,row+3);
            table.add(image,1,row);
            table.add(nameText,2,row);
            table.add(timeframeText,4,row);
            table.setVerticalAlignment(2,row,"top");
            table.setVerticalAlignment(3,row,"top");
            table.setVerticalAlignment(4,row,"top");
            table.setVerticalAlignment(5,row,"top");
            table.setAlignment(2,row,"right");
            table.setAlignment(3,row,"left");
            table.setAlignment(4,row,"right");
            table.setAlignment(5,row,"left");
            table.add(prodName,3,row);
            table.add(timeframeTxt,5,row);

            ++row;
            table.setVerticalAlignment(2,row,"top");
            table.setVerticalAlignment(3,row,"top");
            table.setVerticalAlignment(4,row,"top");
            table.setVerticalAlignment(5,row,"top");
            table.add(departureFromText,2,row);
            table.add(departureTimeText,4,row);
            table.setAlignment(2,row,"right");
            table.setAlignment(3,row,"left");
            table.setAlignment(4,row,"right");
            table.setAlignment(5,row,"left");
            table.add(depFrom,3,row);
            table.add(depTime,5,row);

            ++row;

            ++row;
            table.mergeCells(2,row,5,row);
            table.setAlignment(2,row,"right");
            table.add("Takkar hér",2,row);
            table.setColor(2,row,NatBusiness.backgroundColor);

            ++row;
            table.mergeCells(1,row,5,row);
          }catch (TravelStockroomBusiness.ServiceNotFoundException snf) {
            snf.printStackTrace(System.err);
          }catch (TravelStockroomBusiness.TimeframeNotFoundException tnf) {
            tnf.printStackTrace(System.err);
          }catch (SQLException sql) {
            sql.printStackTrace(System.err);
          }
        }

      }


      topTable.setAlignment(1,1,"right");
      topTable.add(tframeText);
      topTable.add(year);
      topTable.add(new SubmitButton("TEMP-Sækja"));


      add(form);
  }



}