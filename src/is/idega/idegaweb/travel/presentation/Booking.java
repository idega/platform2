package is.idega.idegaweb.travel.presentation;

import com.idega.presentation.Block;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.text.*;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.block.trade.stockroom.data.*;
import com.idega.block.trade.stockroom.business.*;
import com.idega.block.calendar.presentation.SmallCalendar;
import com.idega.util.idegaTimestamp;
import com.idega.util.idegaCalendar;
import com.idega.core.accesscontrol.business.AccessControl;
import is.idega.idegaweb.travel.business.*;
import com.idega.util.text.*;
import java.sql.SQLException;
import java.util.*;
import is.idega.idegaweb.travel.business.Booker;
import is.idega.idegaweb.travel.business.Inquirer;
import is.idega.idegaweb.travel.data.*;
import is.idega.idegaweb.travel.service.tour.presentation.*;
import is.idega.idegaweb.travel.service.tour.data.*;
import is.idega.idegaweb.travel.service.tour.business.*;

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

  private Supplier supplier;
    private int supplierId;
  private Reseller reseller;
    private int resellerId;
  private Contract contract;
    private int contractId;
  private GeneralBooking booking;
    private int bookingId;

  private Product product;
    private int productId;
  private TravelStockroomBusiness tsb = TravelStockroomBusiness.getNewInstance();

  public static String BookingAction = "booking_action";
  private String BookingParameter = "booking";
  public static String parameterBookingId = "bookingBookingId";

  private Service service;
  private Timeframe timeframe;
  private Tour tour;

  public static String parameterProductId = Product.getProductEntityName();
  private static String parameterInqueryId = "bookInqueryId";
  private static String parameterRespondInquery = "bookingRespondInquery";
  private static String parameterRespondYes = "bookingYes";
  private static String parameterRespondNo = "bookingNo";

  public static String parameterUpdateBooking = "bookinUpdateBooking";

  public static int available = -1234;

  private idegaTimestamp stamp;

  public Booking() {
  }

  public void main(IWContext iwc) throws Exception {
      super.main(iwc);
      initialize(iwc);


      //if (super.isLoggedOn(iwc)) {
        if (reseller != null && contract == null) {
          product = null;
        }

        String action = iwc.getParameter(this.BookingAction);
        if (action == null) {action = "";}

        if (action.equals("")) {
          displayForm(iwc);
        }else if (action.equals(this.BookingParameter)) {
          handleInsert(iwc, true);
        }else if (action.equals(this.parameterRespondInquery)) {
          inqueryResponse(iwc);
          displayForm(iwc);
        }else if (action.equals(this.parameterUpdateBooking)) {
          updateBooking(iwc);
          //add("REIMPLEMENTA");
        }

        super.addBreak();
      //}else {
      //  add(super.getLoggedOffTable(iwc));
      //}
  }

  public void initialize(IWContext iwc) {
      bundle = super.getBundle();
      iwrb = super.getResourceBundle();

      supplier = super.getSupplier();
      reseller = super.getReseller();
      if (supplier != null) supplierId = supplier.getID();
      if (reseller != null) resellerId = reseller.getID();

      String sProductId = iwc.getParameter(parameterProductId);
      try {
        if (sProductId == null) {
          sProductId = (String) iwc.getSessionAttribute("TB_BOOKING_PRODUCT_ID");
        }else {
          iwc.setSessionAttribute("TB_BOOKING_PRODUCT_ID",sProductId);
        }
        if (sProductId != null) {
          productId = Integer.parseInt(sProductId);
          product = new Product(productId);
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
              contractId = contract.getID();
            }
        }catch (SQLException sql) {
            sql.printStackTrace(System.err);
        }

      }
      stamp = getIdegaTimestamp(iwc);

  }

  public void displayForm(IWContext iwc) throws Exception{

      Form form = new Form();
      Table topTable = getTopTable(iwc);

      if ((supplier != null) || (reseller != null) || ((supplier == null) && (reseller == null) && (product == null)) ) {
        form.add(topTable);
        form.add(Text.BREAK);
      }

        if (product != null) {
          Table contentTable = new Table(1,1);
              contentTable.setBorder(1);
              contentTable.add(getContentHeader(iwc));
              contentTable.add(Text.BREAK);
              contentTable.add(getTotalTable(iwc));
              contentTable.add(getContentTable(iwc));
              contentTable.setWidth("90%");
              contentTable.setCellspacing(0);
              contentTable.setCellpadding(0);
              contentTable.setBorderColor(super.textColor);
          form.add(contentTable);
        }
        else {
          form.add("TEMP - Veldu ferð");
        }



      int row = 0;
      add(Text.getBreak());
      add(form);
  }


  public Table getTopTable(IWContext iwc) {
      Table topTable = new Table(5,1);
        topTable.setBorder(0);
        topTable.setWidth("90%");

      Text tframeText = (Text) theText.clone();
          tframeText.setText(iwrb.getLocalizedString("travel.timeframe_only","Timeframe"));
          tframeText.addToText(":");

      DropdownMenu trip = null;
      try {
        if (supplier != null) {
          trip = new DropdownMenu(tsb.getProducts(supplierId));
        }else if (reseller != null) {
          trip = new DropdownMenu(ResellerManager.getProductsForReseller(resellerId ));
        }else if (product == null) {
          trip = new DropdownMenu(tsb.getProducts(-1));
        }
      }catch (SQLException sql) {
        sql.printStackTrace(System.err);
        trip = new DropdownMenu(Product.getProductEntityName());
      }

      if (trip != null)
          if (product != null) {
              trip.setSelectedElement(Integer.toString(product.getID()));
          }

      idegaTimestamp temp = idegaTimestamp.RightNow();
      DropdownMenu year = new DropdownMenu("chosen_year");
          for (int i = 2000; i < ( temp.getYear() +4 ); i++) {
              year.addMenuElement(i,""+i);
          }

          String parYear = iwc.getParameter("chosen_year");
          if (parYear != null) {
              year.setSelectedElement(parYear);
          }else {
              year.setSelectedElement(Integer.toString(temp.getYear()));
          }

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
      topTable.add(new SubmitButton(iwrb.getImage("buttons/get.gif"),this.BookingAction, ""),5,1);

      topTable.add(new HiddenInput("month",Integer.toString(stamp.getMonth()) ));
      topTable.add(new HiddenInput("day",Integer.toString(stamp.getDay())));
      topTable.add(new HiddenInput("year",Integer.toString(stamp.getYear())));

      return topTable;
  }

  public Form getContentHeader(IWContext iwc) {
    Form form = new Form();
      Table table = new Table(3,4);
      form.add(table);
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


      try {

          Text nameTextC = (Text) theText.clone();
            nameTextC.setText(service.getName());

          Text timeTextC = (Text) theText.clone();
            timeTextC.setText(new idegaTimestamp(timeframe.getFrom()).getLocaleDate(iwc)+" - "+new idegaTimestamp(timeframe.getTo()).getLocaleDate(iwc) );

          Text depFrom = (Text) theText.clone();
            depFrom.setText(service.getAddress().getStreetName());

          idegaTimestamp temp = new idegaTimestamp(service.getDepartureTime());
          Text depAt = (Text) theText.clone();
            depAt.setText(TextSoap.addZero(temp.getHour())+":"+TextSoap.addZero(temp.getMinute()));

          table.add(nameTextC,2,2);
          table.add(timeTextC,3,2);
          table.add(depFrom,2,4);
          table.add(depAt,3,4);


      }catch (SQLException sql) {
          sql.printStackTrace(System.err);
      }

      return form;
  }

  public Table getContentTable(IWContext iwc) throws Exception{
      Table table = new Table();
        table.setWidth("100%");
        table.setBorder(0);
        table.setCellspacing(0);
        table.setCellpadding(2);
        table.setWidth(6,"200");

      int row = 1;
      boolean isDayVisible = false;
      boolean isExpired = false;
      int iBookings = 0;
      try {
        if (reseller != null) {
          isExpired = TravelStockroomBusiness.getIfExpired(contract, stamp);
          if (!isExpired) {
            isDayVisible = TravelStockroomBusiness.getIfDay(iwc, contract, product, stamp);
          }
        }
        else {
          isDayVisible = TravelStockroomBusiness.getIfDay(iwc,this.product, this.stamp);
          if (supplier == null) {
            if (isDayVisible) {
              iBookings = Booker.getNumberOfBookings(productId, stamp);
              if (iBookings < tour.getMinimumSeats() || iBookings > tour.getTotalSeats()) {
                isDayVisible = false;
              }
            }
          }
        }
      }catch (TravelStockroomBusiness.ServiceNotFoundException snfe) {
            snfe.printStackTrace(System.err);
      }catch (TravelStockroomBusiness.TimeframeNotFoundException tfnfe) {
            tfnfe.printStackTrace(System.err);
      }



      if (isDayVisible) {
          table.mergeCells(1,row,5,row);
          if (supplier != null) {
            Inquery[] inqueries = Inquirer.getInqueries(product.getID(), stamp , true, Inquery.getInqueryPostDateColumnName());

            if (inqueries.length > 0) {
              table.add(getInqueries(iwc, inqueries),1,row);
              table.setColor(1,row,super.YELLOW);
            }else {
              table.setColor(1,row,super.backgroundColor);
            }
          }
          else if (reseller != null) {
            Inquery[] inqueries = Inquirer.getInqueries(product.getID(), stamp ,reseller.getID(), true, Inquery.getInqueryPostDateColumnName());

            if (inqueries.length > 0) {
              table.add(getInqueries(iwc, inqueries),1,row);
              table.setColor(1,row,super.YELLOW);
            }else {
              table.setColor(1,row,super.backgroundColor);
            }
          }

          table.setColor(6,row,super.backgroundColor);
          table.setVerticalAlignment(6,row,"top");
          table.add(Text.BREAK ,6,row);
          table.add(Text.BREAK ,6,row);
          table.add(getCalendar(iwc),6,row);

          ++row;
          table.mergeCells(1,row,5,row);
          table.setColor(1,row,super.backgroundColor );
          table.mergeCells(6,1,6,row);
          table.add(Text.BREAK ,1,row);
          table.add(Text.BREAK ,1,row);
          table.add(getBookingForm(iwc),1,row);

      }else {
        if (isExpired) {
          table.add(iwrb.getLocalizedString("travel.time_for_booking_has_passed","Time for booking has passed"));
        }else {
          table.add(iwrb.getLocalizedString("travel.trip_is_not_scheduled_this_day","Trip is not scheduled this day")+" : "+stamp.getLocaleDate(iwc));
        }
          table.mergeCells(1,row,5,row);
          table.setAlignment(1,row, "center");

          table.setColor(6,row,super.backgroundColor);
          table.setVerticalAlignment(6,row,"top");
          table.add(getCalendar(iwc),6,row);

          ++row;
          table.mergeCells(1,row,5,row);
          table.setColor(1,row,super.backgroundColor );
          table.mergeCells(6,1,6,row);
      }

      return table;

  }

  public Table getCalendar(IWContext iwc) {
    try {
    CalendarHandler ch = new CalendarHandler(iwc);
      if (contract != null) ch.setContract(contract);
      if (product != null)  ch.setProduct(product);
      if (reseller != null) ch.setReseller(reseller);
      if (tour != null)     ch.setTour(tour);
      ch.setTimestamp(stamp);

      return ch.getCalendarTable(iwc);
    }catch (Exception e) {
      return new Table();
    }
  }


  public Table getInqueries(IWContext iwc,Inquery[] inqueries) {
      Table table = new Table();
          table.setWidth("100%");
          table.setBorder(0);
          table.setCellspacing(0);

      int row = 0;

      table.setWidth(1,"100");

      Text dateText;
      Text nameText;
      Text countText;
      Text contentText;

      idegaTimestamp theStamp;
      Link answerYes;
      Link answerNo;


      for (int i = 0; i < inqueries.length; i++) {
          theStamp = new idegaTimestamp(inqueries[i].getInqueryDate());

          dateText = (Text) theSmallBoldText.clone();
              dateText.setText(theStamp.getLocaleDate(iwc));
          nameText = (Text) theSmallBoldText.clone();
              nameText.setText(inqueries[i].getName());
          countText = (Text) theSmallBoldText.clone();
              countText.setText(Integer.toString(inqueries[i].getNumberOfSeats()));
          contentText = (Text) theSmallBoldText.clone();
              contentText.setText(inqueries[i].getInquery());


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

          if (supplier != null) {
              answerYes = new Link("T - Staðfesta bókun");
                answerYes.addParameter(this.parameterInqueryId,inqueries[i].getID());
                answerYes.addParameter(this.parameterRespondInquery, this.parameterRespondYes);
                answerYes.addParameter(this.BookingAction, this.parameterRespondInquery);
                answerYes.addParameter("year",this.stamp.getYear());
                answerYes.addParameter("month",this.stamp.getMonth());
                answerYes.addParameter("day",this.stamp.getDay());

              answerNo = new Link("T - Hafna bókun");
                answerNo.addParameter(this.parameterInqueryId,inqueries[i].getID());
                answerNo.addParameter(this.parameterRespondInquery, this.parameterRespondNo);
                answerNo.addParameter(this.BookingAction, this.parameterRespondInquery);
                answerNo.addParameter("year",this.stamp.getYear());
                answerNo.addParameter("month",this.stamp.getMonth());
                answerNo.addParameter("day",this.stamp.getDay());

              table.add(answerYes,2,row);
              table.add("&nbsp;&nbsp;",2,row);
              table.add(answerNo,2,row);
          }else if (reseller != null) {
              answerNo = new Link("T - Ógilda fyrirspurn");
                answerNo.addParameter(this.parameterInqueryId,inqueries[i].getID());
                answerNo.addParameter(this.parameterRespondInquery, this.parameterRespondNo);
                answerNo.addParameter(this.BookingAction, this.parameterRespondInquery);

              table.add(answerNo,2,row);
          }
      }

      return table;
  }


  public Form getTotalTable(IWContext iwc) {
    Form form = new Form();
      Table table = new Table();
        form.add(table);
        table.setFrameHsides();
        table.setWidth("100%");
        table.setCellspacing(0);
        table.setColor(super.WHITE);
        table.setBorder(1);
        table.setBorderColor(super.textColor);
        int row = 1;

      String cellWidth = "60";
      table.setWidth(2,cellWidth);
      table.setWidth(3,cellWidth);
      table.setWidth(4,cellWidth);
      table.setWidth(5,cellWidth);
      table.setWidth(6,"200");

      int iCount = 0;
      int iBooked =0;
      int iInquery=0;
      int iAvailable=0;


      Text dateText = (Text) theBoldText.clone();
          dateText.setText(stamp.getLocaleDate(iwc));
          dateText.setFontColor(super.BLACK);
      Text countText = (Text) theText.clone();
          countText.setText(iwrb.getLocalizedString("travel.count_sm","count"));
          countText.setFontColor(super.BLACK);
      Text assignedText = (Text) theText.clone();
          assignedText.setText(iwrb.getLocalizedString("travel.assigned_small_sm","assigned"));
          assignedText.setFontColor(super.BLACK);
      Text inqText = (Text) theText.clone();
          inqText.setText(iwrb.getLocalizedString("travel.inqueries_small_sm","inq."));
          inqText.setFontColor(super.BLACK);
      Text bookedText = (Text) theText.clone();
          bookedText.setText(iwrb.getLocalizedString("travel.booked_sm","booked"));
          bookedText.setFontColor(super.BLACK);
      Text availableText = (Text) theText.clone();
          availableText.setText(iwrb.getLocalizedString("travel.available_small_sm","avail."));
          availableText.setFontColor(super.BLACK);
      Text bookingStatusText = (Text) theBoldText.clone();
          bookingStatusText.setText(iwrb.getLocalizedString("travel.booking_status","Booking status"));
          bookingStatusText.setFontColor(super.BLACK);
      Text calendarForBooking = (Text) theText.clone();
          calendarForBooking.setText(iwrb.getLocalizedString("travel.booking_status","Booking status"));
          calendarForBooking.setFontColor(super.BLACK);


      Text dateTextBold = (Text) theSmallBoldText.clone();
        dateTextBold.setText("");
      Text nameTextBold = (Text) theSmallBoldText.clone();
        nameTextBold.setText("");
      Text countTextBold = (Text) theSmallBoldText.clone();
        countTextBold.setText("");
      Text assignedTextBold = (Text) theSmallBoldText.clone();
        assignedTextBold.setText("");
      Text inqTextBold = (Text) theSmallBoldText.clone();
        inqTextBold.setText("");
      Text bookedTextBold = (Text) theSmallBoldText.clone();
        bookedTextBold.setText("");
      Text availableTextBold = (Text) theSmallBoldText.clone();
        availableTextBold.setText("");
          dateTextBold.setFontColor(super.BLACK);
          nameTextBold.setFontColor(super.BLACK);
          countTextBold.setFontColor(super.BLACK);
          assignedTextBold.setFontColor(super.BLACK);
          inqTextBold.setFontColor(super.BLACK);
          bookedTextBold.setFontColor(super.BLACK);
          availableTextBold.setFontColor(super.BLACK);

      try {
        if (reseller != null) {
          if (TravelStockroomBusiness.getIfDay(iwc, contract,product, this.stamp)) {
            iCount = contract.getAlotment();

            if (iCount >0) {
              countTextBold.setText(Integer.toString(iCount));
            }

            iBooked = Booker.getNumberOfBookings(resellerId, service.getID(), this.stamp);
            bookedTextBold.setText(Integer.toString(iBooked));

            iInquery = Inquirer.getInqueredSeats(service.getID(), this.stamp, reseller.getID(), true);
            inqTextBold.setText(Integer.toString(iInquery));

            if (iCount >0) {
              iAvailable = iCount - iBooked -iInquery;
              available = iAvailable;
              availableTextBold.setText(Integer.toString(iAvailable));
            }

          }
        }
        else {
          if (TravelStockroomBusiness.getIfDay(iwc, this.product, this.stamp)) {
            iCount = tour.getTotalSeats();
            iBooked = Booker.getNumberOfBookings(service.getID(), this.stamp);
            iInquery = Inquirer.getInqueredSeats(service.getID(), this.stamp, true);

            if (supplier != null) {
              if (iCount >0) {
                countTextBold.setText(Integer.toString(iCount));
              }
              bookedTextBold.setText(Integer.toString(iBooked));
              inqTextBold.setText(Integer.toString(iInquery));
            }

            if (iCount >0) {
              iAvailable = iCount - iBooked;
              available = iAvailable;
              availableTextBold.setText(Integer.toString(iAvailable));
            }

          }
        }

      }catch (TravelStockroomBusiness.ServiceNotFoundException snfe) {
            snfe.printStackTrace(System.err);
      }catch (TravelStockroomBusiness.TimeframeNotFoundException tfnfe) {
            tfnfe.printStackTrace(System.err);
      }


      table.add(dateText,1,row);

      table.add(countText,2,row);
      table.add(bookedText,3,row);
      table.add(inqText,4,row);
      table.add(availableText,5,row);
      table.add(calendarForBooking,6,row);
      table.setRowColor(row, super.GRAY);


      ++row;
      table.add(bookingStatusText,1,row);
      table.setColor(2,row,super.backgroundColor);

      table.add(countTextBold,2,row);
      table.add(bookedTextBold,3,row);
      table.add(inqTextBold,4,row);
      table.add(availableTextBold,5,row);
      if (tour != null)
      if (tour.getNumberOfDays() > 1) {
        List depDays = TourBusiness.getDepartureDays(iwc, tour);
        String dateStr = iwc.getParameter("booking_date");
        DropdownMenu menu = TourBusiness.getDepartureDaysDropdownMenu(iwc, depDays, "booking_date");
          menu.setStyle("font-family: Verdana; font-size: 8pt; border: 1 solid #000000");
          if (dateStr != null) menu.setSelectedElement(dateStr);
          menu.setToSubmit();
        table.add(menu,6,row);
      }

      table.setColumnAlignment(1,"left");
      table.setColumnAlignment(2,"center");
      table.setColumnAlignment(3,"center");
      table.setColumnAlignment(4,"center");
      table.setColumnAlignment(5,"center");
      table.setColumnAlignment(6,"center");
      table.setRowColor(row, super.GRAY);

      return form;
  }

  public Form getBookingForm(IWContext iwc) throws Exception{
    TourBookingForm tbf = new TourBookingForm(iwc);
    try {
      if (product != null)  tbf.setProduct(product);
      if (reseller != null) tbf.setReseller(reseller);
      if (tour != null)     tbf.setTour(tour);
      tbf.setTimestamp(stamp);
      if (booking != null)  tbf.setBooking(booking);
      return tbf.getBookingForm();
    }catch (Exception e) {
      return new Form();
    }
    //return null;
  }


  // BUSINESS
  public idegaTimestamp getIdegaTimestamp(IWContext iwc) {
      idegaTimestamp stamp = null;

      String year = iwc.getParameter("year");
      String month = iwc.getParameter("month");
      String day = iwc.getParameter("day");

      String IWCalendar_year = iwc.getParameter("IWCalendar_year");
      String IWCalendar_month = iwc.getParameter("IWCalendar_month");
      String IWCalendar_day = iwc.getParameter("IWCalendar_day");
      if (IWCalendar_year != null) year = IWCalendar_year;
      if (IWCalendar_month != null) month = IWCalendar_month;
      if (IWCalendar_day != null) day = IWCalendar_day;

      String dateStr = iwc.getParameter("booking_date");

      if (dateStr == null) {
          String chYear = iwc.getParameter("chosen_year");
          if ((chYear != null) && (year != null)) year = chYear;

          if (stamp == null)

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
      }else {
        stamp = new idegaTimestamp(dateStr);
      }

      return stamp;

  }



  private int handleInsert(IWContext iwc, boolean displayForm) throws Exception {

    TourBookingForm tbf = new TourBookingForm(iwc);
    try {
      if (product != null)  tbf.setProduct(product);
      if (reseller != null) tbf.setReseller(reseller);
      if (tour != null)     tbf.setTour(tour);
      tbf.setTimestamp(stamp);

      if (displayForm) {
        displayForm(iwc);
      }

      return tbf.handleInsert(iwc);
    }catch (Exception e) {
      e.printStackTrace(System.err);

      return -1;
    }
  }


  private void inqueryResponse(IWContext iwc) {
/*    String yesNo = iwc.getParameter(this.parameterRespondInquery);
    String sInqueryId = iwc.getParameter(this.parameterInqueryId);
    Boolean book = null;

    String mailHost = "mail.idega.is";

    String mailSubject = "NAT "+iwrb.getLocalizedString("travel.idega.inquiry","Inquiry");
    StringBuffer responseString = new StringBuffer();

    if (yesNo != null) {
      if (yesNo.equals(this.parameterRespondYes)) book = new Boolean(true);
      if (yesNo.equals(this.parameterRespondNo)) book = new Boolean(false);
    }

    javax.transaction.TransactionManager tm = com.idega.transaction.IdegaTransactionManager.getInstance();
    if (book != null) {
      try {
        tm.begin();
        com.idega.util.SendMail sm = new com.idega.util.SendMail();
        int inqueryId = Integer.parseInt(sInqueryId);
        Inquery inquery = new Inquery(inqueryId);
        is.idega.idegaweb.travel.data.Booking booking = inquery.getBooking();
        Service tempService = booking.getService();


        responseString.append("T - Svar við fyrirspurn þinni varðandi "+inquery.getNumberOfSeats()+" sæti í ferðina \""+tempService.getName()+"\" þann "+new idegaTimestamp(booking.getBookingDate()).getLocaleDate(iwc)+"\n");

        if (book.booleanValue() == false) {
            responseString.append("Beiðninni er hafnað");
        }else if (book.booleanValue() == true) {
            responseString.append("Beiðnin er samþykkt, búið er að bóka");
              booking.setIsValid(true);
            booking.update();
        }

        inquery.setAnswered(true);
        inquery.setAnswerDate(idegaTimestamp.getTimestampRightNow());
        inquery.update();

        Reseller[] resellers = (Reseller[]) inquery.findRelated((Reseller) Reseller.getStaticInstance(Reseller.class));
        try {
          sm.send(supplier.getEmail().getEmailAddress(),inquery.getEmail(), "","",mailHost,mailSubject,responseString.toString());
          if (reseller != null) {  // if this is not a reseller deleting his own inquiry
            if (resellers != null) { // if there was a reseller who send the inquiry
              responseString = new StringBuffer();
              responseString.append("T - Svar við fyrirspurn varðandi "+inquery.getNumberOfSeats()+" sæti fyrir \""+inquery.getName()+"\" í ferðina \""+tempService.getName()+"\" þann "+new idegaTimestamp(booking.getBookingDate()).getLocaleDate(iwc)+"\n");
              for (int i = 0; i < resellers.length; i++) {
                if (resellers[i].getEmail() != null)
                sm.send(supplier.getEmail().getEmailAddress(),resellers[i].getEmail().getEmailAddress(), "","",mailHost,mailSubject,responseString.toString());

              }
            }
          }
          tm.commit();
        }catch (javax.mail.internet.AddressException ae) {
          throw ae;
        }

      }catch (Exception e) {
        e.printStackTrace(System.err);
        try {
          tm.rollback();
        }catch (javax.transaction.SystemException sy) {
          sy.printStackTrace(System.err);
        }
      }
    }


*/
  add("REIMPLEMENTA");

  }

  private void updateBooking(IWContext iwc) throws Exception{
    String sBookingId = iwc.getParameter(this.parameterBookingId);
    if (sBookingId != null) {
        this.bookingId = Integer.parseInt(sBookingId);
        booking = new GeneralBooking(bookingId);
        this.stamp = new idegaTimestamp(booking.getBookingDate());
        displayForm(iwc);
    }
  }

}
