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
import is.idega.travel.business.TravelStockroomBusiness;
import java.sql.SQLException;

import is.idega.travel.data.Service;
import is.idega.travel.data.Tour;
/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class BookingOverview extends TravelManager {

  private IWBundle bundle;
  private IWResourceBundle iwrb;

  private Supplier supplier;

  String tableBackgroundColor = "#FFFFFF";
  int numberOfTripsToDiplay = 6;

  public BookingOverview() {
  }

  public void add(ModuleObject mo) {
    super.add(mo);
  }


  public void main(ModuleInfo modinfo) throws SQLException {
      super.main(modinfo);
      initialize(modinfo);
      supplier = super.getSupplier();

      if (supplier != null) {
        String action = modinfo.getParameter("action");
        if (action == null) {action = "";}

        if (action.equals("")) {
            displayForm(modinfo);
        }

        super.addBreak();
      }else {
        add("TEMP");
        add(new com.idega.block.login.presentation.Login());
      }
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
          stamp = new idegaTimestamp(from_time);
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
          stamp = new idegaTimestamp(from_time);
      }
      else {
          stamp = idegaTimestamp.RightNow();
          stamp.addDays(15);
      }
      return stamp;
  }


  public Table getTopTable(ModuleInfo modinfo) {
      Table topTable = new Table(4,4);
        topTable.setBorder(0);
        topTable.setWidth("90%");



      Text tframeText = (Text) theText.clone();
          tframeText.setText(iwrb.getLocalizedString("travel.timeframe_only","Timeframe"));
          tframeText.addToText(":");

      DropdownMenu trip = null;
      try {
        trip = new DropdownMenu(Product.getStaticInstance(Product.class).findAllByColumnOrdered(Supplier.getStaticInstance(Supplier.class).getIDColumnName() , Integer.toString(supplier.getID()), Product.getColumnNameProductName()));
      }catch (SQLException sql) {
        sql.printStackTrace(System.err);
        trip = new DropdownMenu(Product.getProductEntityName());
      }
      trip.addMenuElementFirst("-10","TEMP - ALLAR FERÐIR");

          String parTrip = modinfo.getParameter(Product.getProductEntityName());
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
      Text generalOverviewText = (Text) theText.clone();
          generalOverviewText.setText(iwrb.getLocalizedString("travel.general_overview","General overview"));
          generalOverviewText.addToText(":");
      Text inqOnlyText = (Text) theText.clone();
          inqOnlyText.setText(iwrb.getLocalizedString("travel.inqueries_only","Inqueries only"));
          inqOnlyText.addToText(":");


      String parMode = modinfo.getParameter("mode");
      if (parMode == null) {parMode = Text.emptyString().toString();}
      RadioButton generalRadio = new RadioButton("mode","general_overview");
        if (parMode.equals(Text.emptyString().toString())) {
            generalRadio.setSelected();
        }else if (parMode.equals("general_overview")){
            generalRadio.setSelected();
        }
      RadioButton inqRadio = new RadioButton("mode","inqueries_only");
        if (parMode.equals("inqueries_only")){
            inqRadio.setSelected();
        }



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


      topTable.add(generalOverviewText,1,3);
      topTable.add(generalRadio,2,3);
      topTable.add(inqOnlyText,3,3);
      topTable.add(inqRadio,4,3);
      topTable.setAlignment(3,3,"right");
      topTable.setAlignment(4,3,"left");
      topTable.setWidth(4,3,"500");

      topTable.setAlignment(4,4,"right");
      topTable.add(new SubmitButton("TEMP-Sækja"),4,4);

      return topTable;
  }

  public Table getContentHeader(ModuleInfo modinfo) {
      Table table = new Table(2,1);
      table.setWidth("95%");


      idegaTimestamp fromStamp = getFromIdegaTimestamp(modinfo);
      idegaTimestamp toStamp = getToIdegaTimestamp(modinfo);

      String mode = modinfo.getParameter("mode");
      if (mode== null) mode="";


      Text headerText = (Text) theBoldText.clone();
          if (mode.equals("inqueries_only")) {
              headerText.setText(iwrb.getLocalizedString("travel.inqueries","Inqueries"));
          }else {
              headerText.setText(iwrb.getLocalizedString("travel.overview","Overview"));
          }
      Text timeText = (Text) theBoldText.clone();
          timeText.setText(fromStamp.getLocaleDate(modinfo)+" - "+toStamp.getLocaleDate(modinfo));

      table.setAlignment(1,1,"left");
      table.add(headerText,1,1);
      table.setAlignment(2,1,"right");
      table.add(timeText,2,1);

      return table;
  }

  public Table getContentTable(ModuleInfo modinfo) {
      Table table = new Table();
        table.setWidth("95%");
        table.setBorder(1);
        table.setCellspacing(0);
        table.setCellpadding(2);

      int row = 1;
      String sProductId = modinfo.getParameter(Product.getProductEntityName());
      if (sProductId != null) {

          boolean viewAll = false;
          idegaTimestamp fromStamp = getFromIdegaTimestamp(modinfo);
          idegaTimestamp toStamp = getToIdegaTimestamp(modinfo);
              toStamp.addDays(1);
          int productId = Integer.parseInt(sProductId);
          if (productId == -10 ) viewAll = true;


          Text dateText = (Text) theText.clone();
              dateText.setText(iwrb.getLocalizedString("travel.date_sm","date"));
          Text nameText = (Text) theText.clone();
              nameText.setText(iwrb.getLocalizedString("travel.trip_name_sm","name of trip"));
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


          Text dateTextBold = (Text) theSmallBoldText.clone();
          Text nameTextBold = (Text) theSmallBoldText.clone();
          Text countTextBold = (Text) theSmallBoldText.clone();
          Text assignedTextBold = (Text) theSmallBoldText.clone();
          Text inqTextBold = (Text) theSmallBoldText.clone();
          Text bookedTextBold = (Text) theSmallBoldText.clone();
          Text availableTextBold = (Text) theSmallBoldText.clone();



          table.add(dateText,1,row);
          table.add(nameText,2,row);
          table.add(countText,3,row);
          table.add(assignedText,4,row);
          table.add(inqText,5,row);
          table.add(bookedText,6,row);
          table.add(availableText,7,row);
          table.add("&nbsp;",8,row);


          TravelStockroomBusiness tsb = TravelStockroomBusiness.getNewInstance();
          Product[] products;
          int supplierId = supplier.getID();
          idegaCalendar cal = new idegaCalendar();
          int dayOfWeek;
          boolean upALine = false;

          Service service;
          Tour tour;

          int iCount = 0;
          int iBooked =0;
          int iInquery=0;
          int iAvailable=0;

          while (toStamp.isLaterThan(fromStamp)) {
              dayOfWeek = cal.getDayOfWeek(fromStamp.getYear(), fromStamp.getMonth(), fromStamp.getDay());
              try {
                  if (viewAll) {
                    products = tsb.getProducts(supplierId, fromStamp);
                  }
                  else {
                    products = new Product[1];
                      products[0] = new Product(productId);
                  }
              }catch (SQLException sql) {
                sql.printStackTrace(System.err);
                products = tsb.getProducts(supplierId, fromStamp);
              }
              upALine = false;
              ++row;
              dateTextBold = (Text) theSmallBoldText.clone();
                  dateTextBold.setText(fromStamp.getLocaleDate(modinfo));
              table.add(dateTextBold,1,row);

              for (int i = 0; i < products.length; i++) {
                  if (TravelStockroomBusiness.getIfDay(products[i].getID(),dayOfWeek) ) {
                      try {
                          iCount = 0;
                          iBooked =0;
                          iInquery=0;
                          iAvailable=0;
                          service = tsb.getService(products[i]);
                          tour = tsb.getTour(products[i]);

                          iCount = tour.getTotalSeats();
                          countTextBold.setText(Integer.toString(iCount));

                          iBooked = tsb.getNumberOfBookings(service.getID(), fromStamp);

                          iInquery = 0;

                          iAvailable = iCount - iBooked;


                          nameTextBold  = (Text) theSmallBoldText.clone();
                              nameTextBold.setText(service.getName());
                          countTextBold = (Text) theSmallBoldText.clone();
                              countTextBold.setText(Integer.toString(iCount));
                          assignedTextBold = (Text) theSmallBoldText.clone();
                              assignedTextBold.setText("0");
                          inqTextBold = (Text) theSmallBoldText.clone();
                              inqTextBold.setText(Integer.toString(iInquery));
                          bookedTextBold = (Text) theSmallBoldText.clone();
                              bookedTextBold.setText(Integer.toString(iBooked));
                          availableTextBold = (Text) theSmallBoldText.clone();
                              availableTextBold.setText(Integer.toString(iAvailable));

                          SubmitButton btnNanar = new SubmitButton("Nanar");
                          SubmitButton btnBook = new SubmitButton("Breyta");


                          table.add(nameTextBold,2,row);
                          table.add(countTextBold,3,row);
                          table.add(assignedTextBold,4,row);
                          table.add(inqTextBold,5,row);
                          table.add(bookedTextBold,6,row);
                          table.add(availableTextBold,7,row);

                          table.setColor(3,row,NatBusiness.backgroundColor);
                          table.setColor(4,row,NatBusiness.ORANGE);
                          table.setColor(5,row,NatBusiness.YELLOW);
                          table.setColor(6,row,NatBusiness.RED);
                          table.setColor(7,row,NatBusiness.LIGHTGREEN);

                          table.add(btnNanar,8,row);
                          table.add(btnBook,8,row);
                          ++row;
                          upALine = true;
                      }catch (TravelStockroomBusiness.ServiceNotFoundException snfe) {
                        snfe.printStackTrace(System.err);
                      }catch (TravelStockroomBusiness.TourNotFoundException tnfe) {
                        tnfe.printStackTrace(System.err);
                      }
                  }
              }
              if (upALine) --row;

              fromStamp.addDays(1);
          }


          String cellWidth = "50";

          table.setWidth(3,cellWidth);
          table.setWidth(4,cellWidth);
          table.setWidth(5,cellWidth);
          table.setWidth(6,cellWidth);
          table.setWidth(7,cellWidth);

          table.setColumnAlignment(1,"left");
          table.setColumnAlignment(2,"left");
          table.setColumnAlignment(3,"center");
          table.setColumnAlignment(4,"center");
          table.setColumnAlignment(5,"center");
          table.setColumnAlignment(6,"center");
          table.setColumnAlignment(7,"center");
          table.setColumnAlignment(8,"right");


      }
      else {
        table.add("TEMP EKKERT VALIÐ");
      }
      return table;

  }

  }