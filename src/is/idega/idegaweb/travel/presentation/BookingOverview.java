package is.idega.travel.presentation;

import com.idega.presentation.Block;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.text.*;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.block.trade.stockroom.data.*;
import com.idega.block.trade.stockroom.business.*;
import com.idega.jmodule.calendar.presentation.SmallCalendar;
import com.idega.util.idegaTimestamp;
import com.idega.util.idegaCalendar;
import com.idega.core.accesscontrol.business.AccessControl;
import is.idega.travel.business.*;
import java.sql.SQLException;

import is.idega.travel.business.Assigner;
import is.idega.travel.business.Booker;
import is.idega.travel.business.Inquirer;
import is.idega.travel.data.*;
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
  private Reseller reseller;
  private Contract contract;

  private Product product;
  private TravelStockroomBusiness tsb = TravelStockroomBusiness.getNewInstance();

  private Service service;
  private Timeframe timeframe;
  private Tour tour;

  private idegaTimestamp fromStamp;
  private idegaTimestamp toStamp;

  private String cellWidth = "50";

  private String closerLookIdParameter = "viewServiceId";
  private String closerLookDateParameter = "viewServiceDate";
  private String bookParameter = "bookService";

  private String parameterDeleteBooking = "bookingOverviewDeleteBooking";
  private String bookingOverviewAction = "bookingOverviewAction";
  private String parameterBookingId = "bookingOverviewBookingId";

  public BookingOverview() {
  }

  public void add(PresentationObject mo) {
    super.add(mo);
  }


  public void main(IWContext iwc) throws Exception {
      super.main(iwc);
      initialize(iwc);
      supplier = super.getSupplier();

      if ( (supplier != null) || (reseller != null) ) {
        if (reseller != null && contract == null) {
          product = null;
        }
        displayForm(iwc);
        super.addBreak();
      }else {
        add("TEMP");
        add(new com.idega.block.login.presentation.Login());
      }
  }

  public void initialize(IWContext iwc) {
      bundle = super.getBundle();
      iwrb = super.getResourceBundle();

      supplier = super.getSupplier();
      reseller = super.getReseller();

      String productId = iwc.getParameter(Product.getProductEntityName());
      try {
        if (productId == null) {
          productId = (String) iwc.getSessionAttribute("TB_BOOKING_PRODUCT_ID");
        }else {
          iwc.setSessionAttribute("TB_BOOKING_PRODUCT_ID",productId);
        }
        if (productId != null) {
          product = new Product(Integer.parseInt(productId));
          service = tsb.getService(product);
          tour = TourBusiness.getTour(product);
          timeframe = tsb.getTimeframe(product);
        }
      }catch (TravelStockroomBusiness.ServiceNotFoundException snfe) {
          snfe.printStackTrace(System.err);
      }catch (TravelStockroomBusiness.TimeframeNotFoundException tfnfe) {
          tfnfe.printStackTrace(System.err);
      }catch (TourBusiness.TourNotFoundException tnfe) {
          tnfe.printStackTrace(System.err);
      }catch (SQLException sql) {sql.printStackTrace(System.err);}


      if ((reseller != null) && (product != null)){
        try {
            Contract[] contracts = (Contract[]) (Contract.getStaticInstance(Contract.class)).findAllByColumn(Contract.getColumnNameResellerId(), Integer.toString(reseller.getID()), Contract.getColumnNameServiceId(), Integer.toString(product.getID()) );
            if (contracts.length > 0) {
              contract = contracts[0];
            }
        }catch (SQLException sql) {
            sql.printStackTrace(System.err);
        }

      }

      fromStamp = getFromIdegaTimestamp(iwc);
      toStamp = getToIdegaTimestamp(iwc);

  }

  public void displayForm(IWContext iwc) {

      Form form = new Form();
      Table topTable = getTopTable(iwc);
        form.add(topTable);
      Table table = new Table();

      String action = iwc.getParameter(this.bookingOverviewAction);
      if (action == null) action = "";

      String view = iwc.getParameter(closerLookDateParameter);
      if (action.equals(""))
      if (view != null) action = "view";

      if (action.equals("")) {
         table = getContentTable(iwc);
      }else if (action.equals("view")) {
         table = getViewService(iwc);
      }else if (action.equals(this.parameterDeleteBooking)) {
         deleteBooking(iwc);
         table = getViewService(iwc);
      }


      form.add(table);
      /*
      ShadowBox sb = new ShadowBox();
        form.add(sb);
        sb.setWidth("90%");
        sb.setAlignment("center");
        sb.add(getContentHeader(iwc));
        sb.add(table);
*/
      Paragraph par = new Paragraph();
        par.setAlign("right");
        par.add(new PrintButton("TEMP-PRENTA"));
        form.add(par);
//        sb.add(par);


      int row = 0;
      add(Text.getBreak());
      add(form);
  }


  // BUSINESS
  public idegaTimestamp getFromIdegaTimestamp(IWContext iwc) {
      idegaTimestamp stamp = null;
      String from_time = iwc.getParameter("active_from");
      if (from_time!= null) {
          stamp = new idegaTimestamp(from_time);
      }
      else {
          stamp = idegaTimestamp.RightNow();
      }
      return stamp;
  }

  // BUSINESS
  public idegaTimestamp getToIdegaTimestamp(IWContext iwc) {
      idegaTimestamp stamp = null;
      String from_time = iwc.getParameter("active_to");
      if (from_time!= null) {
          stamp = new idegaTimestamp(from_time);
      }
      else {
          stamp = idegaTimestamp.RightNow();
          stamp.addDays(15);
      }
      return stamp;
  }


  public Table getTopTable(IWContext iwc) {
      Table topTable = new Table(4,4);
        topTable.setBorder(0);
        topTable.setWidth("90%");



      Text tframeText = (Text) theText.clone();
          tframeText.setText(iwrb.getLocalizedString("travel.timeframe_only","Timeframe"));
          tframeText.addToText(":");

      DropdownMenu trip = null;
        if (supplier != null) {
          trip = new DropdownMenu(tsb.getProducts(supplier.getID()));
        }else if (reseller != null){
          try {
            trip = new DropdownMenu(ResellerManager.getProductsForReseller(reseller.getID()));
          }catch (SQLException sql) {
            sql.printStackTrace(System.err);
            trip = new DropdownMenu();
          }
        }


          if (product != null) {
              trip.setSelectedElement(Integer.toString(product.getID()));
          }


      DateInput active_from = new DateInput("active_from");
          active_from.setDate(fromStamp.getSQLDate());
      DateInput active_to = new DateInput("active_to");
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


      String parMode = iwc.getParameter("mode");
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

  public Table getContentHeader(IWContext iwc) {
      Table table = new Table(2,1);
      table.setWidth("95%");

      String mode = iwc.getParameter("mode");
      if (mode== null) mode="";


      Text headerText = (Text) theBoldText.clone();
          if (mode.equals("inqueries_only")) {
              headerText.setText(iwrb.getLocalizedString("travel.inqueries","Inqueries"));
          }else {
              headerText.setText(iwrb.getLocalizedString("travel.overview","Overview"));
          }
      Text timeText = (Text) theBoldText.clone();
          timeText.setText(fromStamp.getLocaleDate(iwc)+" - "+toStamp.getLocaleDate(iwc));

      table.setAlignment(1,1,"left");
      table.add(headerText,1,1);
      table.setAlignment(2,1,"right");
      table.add(timeText,2,1);

      return table;
  }

  public Table getContentTable(IWContext iwc) {
      Table table = new Table();
        table.setWidth("95%");
        table.setBorder(1);
        table.setCellspacing(0);
        table.setCellpadding(2);

      int row = 1;
      if (product != null) {

          boolean viewAll = false;
          int productId = product.getID();
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


          Product[] products;
          int supplierId = 0;
          if (supplier != null)
            supplierId = supplier.getID();
          idegaCalendar cal = new idegaCalendar();
          int dayOfWeek;
          boolean upALine = false;

          Service service;
          Tour tour;

          int iCount = 0;
          int iBooked =0;
          int iInquery=0;
          int iAvailable=0;
          int iAssigned=0;

          idegaTimestamp tempStamp = new idegaTimestamp(fromStamp);

          toStamp.addDays(1);
          while (toStamp.isLaterThan(tempStamp)) {
              dayOfWeek = cal.getDayOfWeek(tempStamp.getYear(), tempStamp.getMonth(), tempStamp.getDay());
              try {
                  if (viewAll) {
                    products = tsb.getProducts(supplierId, tempStamp);
                  }
                  else {
                    products = new Product[1];
                      products[0] = new Product(productId);
                  }
              }catch (SQLException sql) {
                sql.printStackTrace(System.err);
                products = tsb.getProducts(supplierId, tempStamp);
              }
              upALine = false;
              ++row;
              dateTextBold = (Text) theSmallBoldText.clone();
                  dateTextBold.setText(tempStamp.getLocaleDate(iwc));
              table.add(dateTextBold,1,row);

              boolean bContinue= false;

              for (int i = 0; i < products.length; i++) {
                  try {
                      if (supplier != null) {
                        bContinue =TravelStockroomBusiness.getIfDay(iwc,products[i],tempStamp);
                      }else if (reseller != null) {
                        bContinue = TravelStockroomBusiness.getIfDay(iwc,contract,product,tempStamp);
                      }
                      if (bContinue) {
                          iCount = 0;
                          iBooked =0;
                          iInquery=0;
                          iAvailable=0;
                          iAssigned=0;
                          service = tsb.getService(products[i]);
                          tour = TourBusiness.getTour(products[i]);

                          if (supplier != null) {
                              iCount = tour.getTotalSeats();
                              iBooked = Booker.getNumberOfBookings(service.getID(), tempStamp);
                              iAssigned = Assigner.getNumberOfAssignedSeats(service.getID(), tempStamp);

                              iInquery = Inquirer.getInqueredSeats(service.getID(), tempStamp, true);//getInqueredSeats(service.getID() ,tempStamp, true);
                              //iInquery = Inquirer.getInqueredSeats(service.getID() ,tempStamp, true);
                              iAvailable = iCount - iBooked - iAssigned;
                          }else if (reseller != null) {
                              iCount = contract.getAlotment();
                              iBooked = Booker.getNumberOfBookings(reseller.getID() ,service.getID(), tempStamp);
                              iAssigned = 0;

                              iInquery = Inquirer.getInqueredSeats(service.getID(),tempStamp,reseller.getID(), true);
                              iAvailable = iCount - iBooked - iAssigned -iInquery;
                          }
                          countTextBold.setText(Integer.toString(iCount));


                          nameTextBold  = (Text) theSmallBoldText.clone();
                              nameTextBold.setText(service.getName());
                          countTextBold = (Text) theSmallBoldText.clone();
                              countTextBold.setText(Integer.toString(iCount));
                          assignedTextBold = (Text) theSmallBoldText.clone();
                              assignedTextBold.setText(Integer.toString(iAssigned));
                          inqTextBold = (Text) theSmallBoldText.clone();
                              inqTextBold.setText(Integer.toString(iInquery));
                          bookedTextBold = (Text) theSmallBoldText.clone();
                              bookedTextBold.setText(Integer.toString(iBooked));
                          availableTextBold = (Text) theSmallBoldText.clone();
                              availableTextBold.setText(Integer.toString(iAvailable));

                          Link btnNanar = new Link(iwrb.getImage("/buttons/closer.gif"));
                              btnNanar.addParameter(closerLookDateParameter,tempStamp.toSQLDateString());
                          Link btnBook = new Link(iwrb.getImage("/buttons/book.gif"), Booking.class);
                              btnBook.addParameter(Booking.parameterProductId, this.product.getID());
                              btnBook.addParameter("year",tempStamp.getYear());
                              btnBook.addParameter("month",tempStamp.getMonth());
                              btnBook.addParameter("day",tempStamp.getDay());

                          table.add(nameTextBold,2,row);
                          table.add(assignedTextBold,4,row);
                          table.add(inqTextBold,5,row);
                          table.add(bookedTextBold,6,row);
                          if (iCount > 0) {
                            table.add(countTextBold,3,row);
                            table.add(availableTextBold,7,row);
                          }

                          table.setColor(3,row,super.backgroundColor);
                          table.setColor(4,row,super.ORANGE);
                          table.setColor(5,row,super.YELLOW);
                          table.setColor(6,row,super.RED);
                          table.setColor(7,row,super.LIGHTGREEN);

                          table.add(btnNanar,8,row);
                          if (supplier != null) {
                            table.add(btnBook,8,row);
                          } else if (reseller != null) {
                            if (!TravelStockroomBusiness.getIfExpired(contract, tempStamp))
                              table.add(btnBook,8,row);
                          }
                          ++row;
                          upALine = true;
                      }
                  }catch (TravelStockroomBusiness.ServiceNotFoundException snfe) {
                    snfe.printStackTrace(System.err);
                  }catch (TourBusiness.TourNotFoundException tnfe) {
                    tnfe.printStackTrace(System.err);
                  }catch (TravelStockroomBusiness.TimeframeNotFoundException tfnfe) {
                    tfnfe.printStackTrace(System.err);
                  }
              }
              if (upALine) --row;

              tempStamp.addDays(1);
          }
          toStamp.addDays(-1);



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


  public Table getViewService(IWContext iwc)  {
    String view_id = iwc.getParameter(this.closerLookIdParameter);
    String view_date = iwc.getParameter(this.closerLookDateParameter);


      Table table = new Table();
        table.setWidth("95%");
        table.setBorder(0);
        table.setCellspacing(0);
        table.setCellpadding(2);

        int row = 1;

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
          Text hotelPickupText = (Text) theText.clone();
              hotelPickupText.setText(iwrb.getLocalizedString("travel.hotel_pickup","Hotel pick-up"));


          Text dateTextBold = (Text) theSmallBoldText.clone();
          Text nameTextBold = (Text) theSmallBoldText.clone();
          Text countTextBold = (Text) theSmallBoldText.clone();
          Text assignedTextBold = (Text) theSmallBoldText.clone();
          Text inqTextBold = (Text) theSmallBoldText.clone();
          Text bookedTextBold = (Text) theSmallBoldText.clone();
          Text availableTextBold = (Text) theSmallBoldText.clone();
          Text hotelPickupTextBold = (Text) theSmallBoldText.clone();

          table.add(dateText,1,row);
          table.add(nameText,2,row);
          table.add(countText,3,row);
          table.add(assignedText,4,row);
          table.add(inqText,5,row);
          table.add(bookedText,6,row);
          table.add(availableText,7,row);
          table.add(hotelPickupText,8,row);

          idegaTimestamp currentStamp = new idegaTimestamp(view_date);
          int seats = 0;
          int assigned = 0;
          int iInqueries = 0;
          int booked = 0;
          int available = 0;

          if (supplier != null) {
            seats = tour.getTotalSeats();
            assigned = Assigner.getNumberOfAssignedSeats(service.getID(), currentStamp);
            iInqueries = Inquirer.getInqueredSeats(service.getID() , currentStamp, true);
            booked = Booker.getNumberOfBookings(service.getID(), currentStamp);
            available = seats - booked;
          }else if (reseller != null) {
            seats = contract.getAlotment();
            assigned = 0;
            iInqueries = Inquirer.getInqueredSeats(service.getID() , currentStamp, reseller.getID(), true);
            booked = Booker.getNumberOfBookings(reseller.getID(),service.getID(), currentStamp);
            available = seats - booked - iInqueries;
          }

          dateTextBold.setText(currentStamp.getLocaleDate(iwc));
          nameTextBold.setText(service.getName());
          countTextBold.setText(Integer.toString(seats));
          availableTextBold.setText(Integer.toString(available));
          assignedTextBold.setText(Integer.toString(assigned));
          inqTextBold.setText(Integer.toString(iInqueries));
          bookedTextBold.setText(Integer.toString(booked));

          ++row;
          table.add(dateTextBold,1,row);
          table.add(nameTextBold,2,row);
          table.setAlignment(3,row,"center");
          table.add(assignedTextBold,4,row);
          table.add(inqTextBold,5,row);
          table.add(bookedTextBold,6,row);
          if (seats > 0) {
            table.add(countTextBold,3,row);
            table.add(availableTextBold,7,row);
          }

          table.setColor(3,row,super.backgroundColor);
          table.setColor(4,row,super.ORANGE);
          table.setColor(5,row,super.YELLOW);
          table.setColor(6,row,super.RED);
          table.setColor(7,row,super.LIGHTGREEN);
          table.setColor(8,row,super.backgroundColor);


          Text Tname;
          Text Temail;
          Text Thotel = (Text) super.theSmallBoldText.clone();
          Text Tbooked;

          java.util.List emails;
          // ------------------ ASSIGNED -----------------------
          if (assigned > 0) {
            Reseller[] resellers = ResellerManager.getResellers(service.getID(), currentStamp);
            for (int i = 0; i < resellers.length; i++) {
              ++row;

              Tname = (Text) super.theSmallBoldText.clone();
                Tname.setText("&nbsp;&nbsp;"+resellers[i].getName());
              Temail = (Text) super.theSmallBoldText.clone();

              try {
                emails = resellers[i].getEmails();
                if (emails != null) {
                  for (int j = 0; j < emails.size(); j++) {
                    if (j > 0) Temail.addToText(", ");
                    Temail.addToText( ((com.idega.core.data.Email) emails.get(j)).getEmailAddress());
                  }
                }
              }catch (SQLException sql) {sql.printStackTrace(System.err);}
              //                Temail.setText(reseller[i].getEmail());
              Tbooked = (Text) super.theSmallBoldText.clone();
                Tbooked.setText(Integer.toString(Assigner.getNumberOfAssignedSeats(service.getID(), resellers[i].getID() ,currentStamp)));

              table.mergeCells(2,row,3, row);
              table.add(Tname,1,row);
              table.add(Temail,3,row);
              table.setAlignment(3,row,"left");
              table.add(Tbooked,4,row);


              table.setColor(1,row,super.ORANGE);
              table.setColor(2,row,super.ORANGE);
              table.setColor(4,row,super.ORANGE);
              table.setColor(5,row,super.YELLOW);
              table.setColor(6,row,super.RED);
              table.setColor(7,row,super.LIGHTGREEN);
              table.setColor(8,row,super.backgroundColor);
            }
          }


          // ------------------ INQUERIES ------------------------
          Inquery[] inqueries = null;
          if (supplier != null) inqueries = Inquirer.getInqueries(service.getID(), currentStamp, true, Inquery.getNameColumnName());
          if (reseller != null) inqueries = Inquirer.getInqueries(service.getID(), currentStamp, reseller.getID(),true, Inquery.getNameColumnName());
          for (int i = 0; i < inqueries.length; i++) {
            ++row;
            Tname = (Text) super.theSmallBoldText.clone();
              Tname.setText("&nbsp;&nbsp;"+inqueries[i].getName());
            Temail = (Text) super.theSmallBoldText.clone();
              Temail.setText(inqueries[i].getEmail());
            Tbooked = (Text) super.theSmallBoldText.clone();
              Tbooked.setText(Integer.toString(inqueries[i].getNumberOfSeats()));

            table.mergeCells(2,row,4, row);
            table.add(Tname,1,row);
            table.add(Temail,2,row);
            table.setAlignment(3,row,"left");
            table.add(Tbooked,5,row);


            table.setColor(1,row,super.YELLOW);
            table.setColor(2,row,super.YELLOW);
            table.setColor(4,row,super.YELLOW);
            table.setColor(5,row,super.YELLOW);
            table.setColor(6,row,super.RED);
            table.setColor(7,row,super.LIGHTGREEN);
            table.setColor(8,row,super.backgroundColor);
          }


          // ------------------ BOOKINGS ------------------------
          Link changeLink = new Link(iwrb.getImage("buttons/change.gif"),Booking.class);
          Link deleteLink = new Link(iwrb.getImage("buttons/delete.gif"));
            deleteLink.addParameter(this.bookingOverviewAction,this.parameterDeleteBooking);
            deleteLink.addParameter(this.closerLookDateParameter, view_date);
            deleteLink.addParameter(this.closerLookIdParameter, view_id);
          Link link;
          is.idega.travel.data.Booking[] bookings = Booker.getBookings(this.service.getID(), currentStamp);
          for (int i = 0; i < bookings.length; i++) {
              ++row;
              if (tour.getHotelPickup()) {
                  try {

                      HotelPickupPlace place = new HotelPickupPlace(bookings[i].getHotelPickupPlaceID());
                      Thotel = (Text) super.theSmallBoldText.clone();
                      Thotel.setText("&nbsp;&nbsp;"+place.getName());
                  }catch (Exception e){Thotel.setText("");}
              }else {
                  Thotel.setText("");
              }

              Tname = (Text) super.theSmallBoldText.clone();
                Tname.setText("&nbsp;&nbsp;"+bookings[i].getName());
              Temail = (Text) super.theSmallBoldText.clone();
                Temail.setText(bookings[i].getEmail());
              Tbooked = (Text) super.theSmallBoldText.clone();
                Tbooked.setText(Integer.toString(bookings[i].getTotalCount()));

              table.mergeCells(2, row, 5, row);
              table.add(Tname, 1, row);
              table.add(Temail, 2, row);
              table.setAlignment(3, row, "left");
              table.add(Tbooked, 6, row);
              table.add(Thotel, 8, row);

              table.setColor(1, row, super.RED);
              table.setColor(2, row, super.RED);
              table.setColor(6, row, super.RED);
              table.setColor(7, row, super.LIGHTGREEN);
              table.setColor(8, row, super.backgroundColor);

              link = (Link) changeLink.clone();
                link.addParameter(Booking.BookingAction,Booking.parameterUpdateBooking);
                link.addParameter(Booking.parameterBookingId,bookings[i].getID());
              table.add(link, 8, row);

              link = (Link) deleteLink.clone();
                link.addParameter(this.parameterBookingId,bookings[i].getID());
              table.add(link, 8, row);

          }

        ++row;
        table.mergeCells(1,row,6,row);
        availableTextBold.setText(Integer.toString(available));
        table.setColor(1, row, super.LIGHTGREEN);
        table.setColor(7, row, super.LIGHTGREEN);
        table.setColor(8, row, super.backgroundColor);
        Text Tavail = (Text) super.theSmallBoldText.clone();
          Tavail.setText(iwrb.getLocalizedString("travel.available_seats","Available seats"));
        if (seats > 0) {
          table.add(Tavail, 1, row);
          table.add(availableTextBold, 7, row);
        }



    table.setColumnAlignment(1,"left");
    table.setColumnAlignment(2,"left");
    table.setColumnAlignment(4,"center");
    table.setColumnAlignment(5,"center");
    table.setColumnAlignment(6,"center");
    table.setColumnAlignment(7,"center");
    table.setColumnAlignment(8,"left");

    table.setWidth(3,cellWidth);
    table.setWidth(4,cellWidth);
    table.setWidth(5,cellWidth);
    table.setWidth(6,cellWidth);
    table.setWidth(7,cellWidth);

    return table;

  }

  public void deleteBooking(IWContext iwc) {
    String lBookingId = iwc.getParameter(this.parameterBookingId);
    if (lBookingId != null) {
      Booker.deleteBooking(Integer.parseInt(lBookingId));
    }
  }

  }