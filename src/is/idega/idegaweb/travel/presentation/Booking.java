package is.idega.travel.presentation;

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
import is.idega.travel.business.*;
import com.idega.util.text.*;
import java.sql.SQLException;
import java.util.*;
import is.idega.travel.business.Booker;
import is.idega.travel.business.Inquirer;
import is.idega.travel.data.*;
//import javax.swing.*;
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
  private is.idega.travel.data.Booking booking;
    private int bookingId;

  private Product product;
    private int productId;
  private TravelStockroomBusiness tsb = TravelStockroomBusiness.getNewInstance();

  public static String BookingAction = "booking_action";
  private String BookingParameter = "booking";

  private Service service;
  private Timeframe timeframe;
  private Tour tour;

  public static String parameterProductId = Product.getProductEntityName();
  private static String parameterBookAnyway = "bookingBookAnyway";
  private static String parameterSendInquery = "bookingSendInquery";
  private static String parameterInqueryId = "bookInqueryId";
  private static String parameterRespondInquery = "bookingRespondInquery";
  private static String parameterRespondYes = "bookingYes";
  private static String parameterRespondNo = "bookingNo";

  public static String parameterUpdateBooking = "bookinUpdateBooking";
  public static String parameterBookingId = "bookingBookingId";

  private idegaTimestamp stamp;

  int available = -1234;

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
          checkBooking(iwc);
        }else if (action.equals(this.parameterBookAnyway)) {
          saveBooking(iwc, true);
        }else if (action.equals(this.parameterSendInquery)) {
          sendInquery(iwc);
        }else if (action.equals(this.parameterRespondInquery)) {
          inqueryResponse(iwc);
          displayForm(iwc);
        }else if (action.equals(this.parameterUpdateBooking)) {
          updateBooking(iwc);
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

  public void displayForm(IWContext iwc) {

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

          if (tour.getNumberOfDays() > 1) {
            List depDays = TourBusiness.getDepartureDays(iwc, tour);
            String dateStr = iwc.getParameter("booking_date");
            DropdownMenu menu = TourBusiness.getDepartureDaysDropdownMenu(iwc, depDays, "booking_date");
              if (dateStr != null) menu.setSelectedElement(dateStr);
              menu.setToSubmit();
            table.add(menu,3,4);
          }

      }catch (SQLException sql) {
          sql.printStackTrace(System.err);
      }

      return form;
  }

  public Table getContentTable(IWContext iwc) {
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
          table.add(getBookingForm(),1,row);

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
      String colorForAvailableDay = super.ORANGE;
      String colorForAvailableDayText = super.backgroundColor;
      String colorForInquery = super.YELLOW;
      String colorForFullyBooked = super.RED;
      String colorForFullyBookedText = super.WHITE;
      String colorForToday = "#71CBFB";

      Table table = new Table(4,6);
          table.setBorder(0);
          table.setColor(super.backgroundColor);
          table.setAlignment("center");


      idegaCalendar cal = new idegaCalendar();

      Text jan = (Text) theText.clone();
        jan.setText(cal.getShortNameOfMonth(1,iwc).substring(0,3));
      Text feb = (Text) theText.clone();
        feb.setText(cal.getShortNameOfMonth(2,iwc).substring(0,3));
      Text mar = (Text) theText.clone();
        mar.setText(cal.getShortNameOfMonth(3,iwc).substring(0,3));
      Text apr = (Text) theText.clone();
        apr.setText(cal.getShortNameOfMonth(4,iwc).substring(0,3));
      Text may = (Text) theText.clone();
        may.setText(cal.getShortNameOfMonth(5,iwc).substring(0,3));
      Text jun = (Text) theText.clone();
        jun.setText(cal.getShortNameOfMonth(6,iwc).substring(0,3));
      Text jul = (Text) theText.clone();
        jul.setText(cal.getShortNameOfMonth(7,iwc).substring(0,3));
      Text aug = (Text) theText.clone();
        aug.setText(cal.getShortNameOfMonth(8,iwc).substring(0,3));
      Text sep = (Text) theText.clone();
        sep.setText(cal.getShortNameOfMonth(9,iwc).substring(0,3));
      Text oct = (Text) theText.clone();
        oct.setText(cal.getShortNameOfMonth(10,iwc).substring(0,3));
      Text nov = (Text) theText.clone();
        nov.setText(cal.getShortNameOfMonth(11,iwc).substring(0,3));
      Text dec = (Text) theText.clone();
        dec.setText(cal.getShortNameOfMonth(12,iwc).substring(0,3));

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


      SmallCalendar sm = new SmallCalendar(stamp);
          sm.T.setBorder(1);
          sm.T.setCellpadding(2);
          sm.T.setBorderColor(super.backgroundColor);
          sm.useNextAndPreviousLinks(true);
          sm.setBackgroundColor(super.backgroundColor);
          sm.setTextColor("WHITE");
          sm.setDaysAsLink(true);
          sm.showNameOfDays(true);
          sm.setHeaderTextColor(super.textColor);
          sm.setDayTextColor(super.textColor);
          sm.setHeaderColor(super.backgroundColor);
          sm.setDayCellColor(super.backgroundColor);
          sm.setBodyColor("#8484D6");
          sm.setInActiveCellColor("#666666");
//          sm.setInActiveCellColor("#B1B1E5");
//          sm.useColorToday(true);
          sm.setColorToday(colorForToday);
          sm.setSelectedHighlighted(false);

          sm.setDayFontColor(idegaTimestamp.RightNow(),super.backgroundColor);


      int month = stamp.getMonth();
      int year = stamp.getYear();
      int lengthOfMonth = cal.getLengthOfMonth(month, year);

      idegaTimestamp temp = new idegaTimestamp(1, month , year);
      int iBookings = 0;
      int seats = tour.getTotalSeats();

      List depDays = new Vector();
      if (tour.getNumberOfDays() > 1) {
        depDays = TourBusiness.getDepartureDays(iwc, tour);
      }else {
        depDays = TourBusiness.getDepartureDays(iwc,tour, new idegaTimestamp(1,stamp.getMonth(), stamp.getYear()), new idegaTimestamp(lengthOfMonth ,stamp.getMonth(), stamp.getYear()));
      }

      try {
        if (contract != null) {
          for (int i = 0; i < depDays.size(); i++) {
            temp = (idegaTimestamp) depDays.get(i);
            if (!TravelStockroomBusiness.getIfExpired(contract, temp))
            if (TravelStockroomBusiness.getIfDay(iwc,contract,product,temp)) {
              if (seats > 0 && seats <= Booker.getNumberOfBookings(productId, temp) ) {
                sm.setDayColor(temp, colorForFullyBooked);
                sm.setDayFontColor(temp, colorForFullyBookedText);
              }else {
                sm.setDayColor(temp, colorForAvailableDay);
                sm.setDayFontColor(temp, colorForAvailableDayText);
              }
            }
          }
        }
        else if (supplier != null) {
          for (int i = 0; i < depDays.size(); i++) {
            temp = (idegaTimestamp) depDays.get(i);
            if (seats > 0 && seats <= Booker.getNumberOfBookings(productId, temp) ) {
              sm.setDayColor(temp, colorForFullyBooked);
              sm.setDayFontColor(temp, colorForFullyBookedText);
            }else {
              sm.setDayColor(temp, colorForAvailableDay);
              sm.setDayFontColor(temp,colorForAvailableDayText);
            }
          }
          for (int i = 1; i <= lengthOfMonth; i++) {
            if (Inquirer.getInqueredSeats(productId, temp, true) > 0) {
              sm.setDayColor(temp, colorForInquery);
              sm.setDayFontColor(temp,colorForAvailableDayText);
            }
            temp.addDays(1);
          }
        }
        else {
          for (int i = 0; i < depDays.size(); i++) {
            temp = (idegaTimestamp) depDays.get(i);
            iBookings = Booker.getNumberOfBookings(productId, temp);
            if (seats > 0 && seats <= iBookings ) {
              sm.setDayColor(temp, colorForFullyBooked);
              sm.setDayFontColor(temp, colorForFullyBookedText);
            }else if (iBookings >= tour.getMinimumSeats() && iBookings <= tour.getTotalSeats()) {
              sm.setDayColor(temp, colorForAvailableDay);
              sm.setDayFontColor(temp,colorForAvailableDayText);
            }
          }
          for (int i = 1; i <= lengthOfMonth; i++) {
            if (Inquirer.getInqueredSeats(productId, temp,resellerId, true) > 0) {
              sm.setDayColor(temp, colorForInquery);
              sm.setDayFontColor(temp,colorForAvailableDayText);
            }
            temp.addDays(1);
          }
        }
      }catch (TravelStockroomBusiness.ServiceNotFoundException snfe) {
        snfe.printStackTrace(System.err);
      }catch (TravelStockroomBusiness.TimeframeNotFoundException tfnfe) {
        tfnfe.printStackTrace(System.err);
      }

      Table legend = new Table();
        legend.setBorder(0);
        legend.setCellpadding(0);
        legend.setCellspacing(2);

        Text avail = (Text) theText.clone();
          avail.setText(iwrb.getLocalizedString("travel.colorForAvailableDay","Available"));
        Text inq = (Text) theText.clone();
          inq.setText(iwrb.getLocalizedString("travel.colorForInquiry","Inquiry"));
        Text today = (Text) theText.clone();
          today.setText(iwrb.getLocalizedString("travel.today","Today"));
        Text full = (Text) theText.clone();
          full.setText(iwrb.getLocalizedString("travel.fully_booked","Fully booked"));

        legend.add(avail,1,1);
        legend.setColor(3,1,colorForAvailableDay);
        legend.setWidth(3,1,"18");
        legend.setHeight(1,"14");
        legend.add(inq,1,2);
        legend.setColor(3,2,colorForInquery);
        legend.setWidth(3,2,"18");
        legend.setHeight(2,"14");
        legend.add(today,1,3);
        legend.setColor(3,3,colorForToday);
        legend.setWidth(3,3,"18");
        legend.setHeight(3,"14");
        legend.add(full,1,4);
        legend.setColor(3,4,colorForFullyBooked);
        legend.setWidth(3,4,"18");
        legend.setHeight(4,"14");
      table.setAlignment(1,6,"center");

      table.mergeCells(1,5,4,5);
      table.add(sm,1,5);
      table.mergeCells(1,6,4,6);
      table.add(legend,1,6);

      return table;
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


  public Table getTotalTable(IWContext iwc) {
      Table table = new Table();
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
          calendarForBooking.setText(iwrb.getLocalizedString("travel.calendar_for_booking","Calendar for booking"));
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
//      table.setColor(3,row,super.RED);
//      table.setColor(4,row,super.YELLOW);
//      table.setColor(5,row,super.LIGHTGREEN);

      table.add(countTextBold,2,row);
      table.add(bookedTextBold,3,row);
      table.add(inqTextBold,4,row);
      table.add(availableTextBold,5,row);

      table.setColumnAlignment(1,"left");
      table.setColumnAlignment(2,"center");
      table.setColumnAlignment(3,"center");
      table.setColumnAlignment(4,"center");
      table.setColumnAlignment(5,"center");
      table.setColumnAlignment(6,"center");
      table.setRowColor(row, super.GRAY);

      return table;
  }

  public Form getBookingForm() {
      Form form = new Form();
      Table table = new Table();
        form.add(table);
        form.addParameter("year",Integer.toString(stamp.getYear()));
        form.addParameter("month",Integer.toString(stamp.getMonth()));
        form.addParameter("day",Integer.toString(stamp.getDay()));
        table.setWidth("100%");

      table.setColumnAlignment(1,"right");
      table.setColumnAlignment(2,"left");
      table.setColumnAlignment(3,"right");
      table.setColumnAlignment(4,"left");

      ProductPrice[] pPrices = ProductPrice.getProductPrices(service.getID(), true);

      if (pPrices.length > 0) {
          int row = 1;
          int textInputSizeLg = 38;
          int textInputSizeMd = 18;
          int textInputSizeSm = 5;

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
          Text cityText = (Text) theText.clone();
              cityText.setText(iwrb.getLocalizedString("travel.city_sm","city"));
          Text countryText = (Text) theText.clone();
              countryText.setText(iwrb.getLocalizedString("travel.country_sm","country"));

          DropdownMenu pickupMenu = null;
          TextInput roomNumber = null;
          Text tReferenceNumber = (Text) theText.clone();
            tReferenceNumber.setText(iwrb.getLocalizedString("travel.reference_number","Reference number"));
          TextInput tiReferenceNumber = new TextInput("reference_number");
            tiReferenceNumber.setSize(10);

          TextInput surname = new TextInput("surname");
              surname.setSize(textInputSizeLg);
          TextInput lastname = new TextInput("lastname");
              lastname.setSize(textInputSizeLg);
          TextInput address = new TextInput("address");
              address.setSize(textInputSizeLg);
          TextInput areaCode = new TextInput("area_code");
              areaCode.setSize(textInputSizeSm);
          TextInput email = new TextInput("e-mail");
              email.setSize(textInputSizeMd);
          TextInput telNumber = new TextInput("telephone_number");
              telNumber.setSize(textInputSizeMd);

          TextInput city = new TextInput("city");
              city.setSize(textInputSizeLg);
          TextInput country = new TextInput("country");
              country.setSize(textInputSizeMd);


          ++row;
          table.mergeCells(2,row,4,row);
          table.add(surnameText,1,row);
          table.add(surname,2,row);

          ++row;
          table.mergeCells(2,row,4,row);
          table.add(lastnameText,1,row);
          table.add(lastname,2,row);

          ++row;
          table.mergeCells(2,row,4,row);
          table.add(addressText,1,row);
          table.add(address,2,row);

          ++row;
          table.mergeCells(2,row,4,row);
          table.add(cityText,1,row);
          table.add(city,2,row);

          ++row;
          table.mergeCells(2,row,4,row);
          table.add(areaCodeText,1,row);
          table.add(areaCode,2,row);

          ++row;
          table.mergeCells(2,row,4,row);
          table.add(countryText,1,row);
          table.add(country,2,row);

          ++row;
          table.mergeCells(2,row,4,row);
          table.add(emailText,1,row);
          table.add(email,2,row);

          ++row;
          table.mergeCells(2,row,4,row);
          table.add(telNumberText,1,row);
          table.add(telNumber,2,row);

          if (tour.getIsHotelPickup()) {
              ++row;
              table.mergeCells(2,row,4,row);

              Text hotelText = (Text) theText.clone();
                hotelText.setText(iwrb.getLocalizedString("travel.hotel_pickup_sm","hotel pickup"));
              HotelPickupPlace[] hotelPickup = tsb.getHotelPickupPlaces(this.service);
              pickupMenu = new DropdownMenu(hotelPickup, HotelPickupPlace.getHotelPickupPlaceTableName());
                pickupMenu.addMenuElementFirst("-1",iwrb.getLocalizedString("travel.no_hotel_pickup","No hotel pickup"));

              Text roomNumberText = (Text) theText.clone();
                roomNumberText.setText(iwrb.getLocalizedString("travel.room_number","room number"));
              roomNumber = new TextInput("room_number");
                roomNumber.setSize(textInputSizeSm);

              table.add(hotelText,1,row);
              table.add(pickupMenu,2,row);
              table.add(Text.NON_BREAKING_SPACE+Text.NON_BREAKING_SPACE,2,row);
              table.add(roomNumberText,2,row);
              table.add(Text.NON_BREAKING_SPACE+Text.NON_BREAKING_SPACE,2,row);
              table.add(roomNumber,2,row);
          }

          Text pPriceCatNameText;
          ResultOutput pPriceText;
          TextInput pPriceMany;
          PriceCategory category;
          Text txtPrice;
          Text txtPerPerson = (Text) theText.clone();
            txtPerPerson.setText(iwrb.getLocalizedString("travel.per_person","per person"));

          Text totalText = (Text) theBoldText.clone();
            totalText.setText(iwrb.getLocalizedString("travel.total","Total"));
          ResultOutput TotalPassTextInput = new ResultOutput("total_pass","0");
            TotalPassTextInput.setSize(5);
          ResultOutput TotalTextInput = new ResultOutput("total","0");
            TotalTextInput.setSize(8);

          ++row;

          BookingEntry[] entries = null;
          ProductPrice pPri = null;
          int totalCount = 0;
          int totalSum = 0;
          int currentSum = 0;
          int currentCount = 0;
          if (booking != null) {
            entries = Booker.getBookingEntries(booking);
          }


          for (int i = 0; i < pPrices.length; i++) {
              try {
                  ++row;
                  category = pPrices[i].getPriceCategory();
                  int price = (int) tsb.getPrice(service.getID(),pPrices[i].getPriceCategoryID(),pPrices[i].getCurrencyId(),idegaTimestamp.getTimestampRightNow());
    //              pPrices[i].getPrice();
                  pPriceCatNameText = (Text) theText.clone();
                    pPriceCatNameText.setText(category.getName());

                  pPriceText = new ResultOutput("thePrice"+i,"0");
                    pPriceText.setSize(8);

                  pPriceMany = new TextInput("priceCategory"+i ,"0");
                    pPriceMany.setSize(5);
                    pPriceMany.setAsNotEmpty("T - Ekki tómt");
                    pPriceMany.setAsIntegers("T - Bara tölur takk");

                  if (booking != null) {
                    if (entries != null) {
                      for (int j = 0; j < entries.length; j++) {
                        if (entries[j].getProductPriceId() == pPrices[i].getID()) {
                          pPri = entries[j].getProductPrice();
                          currentCount = entries[j].getCount();
                          currentSum = (int) (currentCount * tsb.getPrice(productId,pPri.getPriceCategoryID(),pPri.getCurrencyId(),idegaTimestamp.getTimestampRightNow()));

                          totalCount += currentCount;
                          totalSum += currentSum;
                          pPriceMany.setContent(Integer.toString(currentCount));
                          pPriceText = new ResultOutput("thePrice"+i,Integer.toString(currentSum));
                            pPriceText.setSize(8);
                        }
                      }
                    }
                  }


                  pPriceText.add(pPriceMany,"*"+price);
                  TotalPassTextInput.add(pPriceMany);
                  TotalTextInput.add(pPriceMany,"*"+price);


                  table.add(pPriceCatNameText, 1,row);
                  table.add(pPriceMany,2,row);
                  table.add(pPriceText, 2,row);


                  txtPrice = (Text) theText.clone();
                    txtPrice.setText(Integer.toString(price));
                  //table.add(txtPrice,3,row);
                  //table.add(txtPerPerson,4,row);


              }catch (SQLException sql) {
                sql.printStackTrace(System.err);
              }
          }

          ++row;

          table.add(totalText,1,row);
          if (booking != null) {
            TotalPassTextInput.setContent(Integer.toString(totalCount));
            TotalTextInput.setContent(Integer.toString(totalSum));
          }
          table.add(TotalPassTextInput,2,row);
          table.add(TotalTextInput,2,row);
           table.add(new HiddenInput("available",Integer.toString(available)),2,row);

          ++row;
          if (reseller != null) {
            table.setAlignment(2,row,"right");
            table.add(tReferenceNumber,2,row);
            table.add(tiReferenceNumber,3,row);
          }else if ( (reseller == null) && (supplier == null) ) {
            TextInput ccNumber = new TextInput("ccNumber");
              ccNumber.setMaxlength(16);
              ccNumber.setLength(20);
              ccNumber.setAsNotEmpty("T - vantar cc númer");
              ccNumber.setAsIntegers("T - cc númer rangt");
            TextInput ccMonth = new TextInput("ccMonth");
              ccMonth.setMaxlength(2);
              ccMonth.setLength(3);
              ccMonth.setAsNotEmpty("T - vantar cc manuð");
              ccMonth.setAsIntegers("T - cc manuður rangur");
            TextInput ccYear = new TextInput("ccYear");
              ccYear.setMaxlength(2);
              ccYear.setLength(3);
              ccYear.setAsNotEmpty("T - vantar cc ár");
              ccYear.setAsIntegers("T - cc ár rangt");

            Text ccText = (Text) theText.clone();
              ccText.setText(iwrb.getLocalizedString("travel.credidcard_number","Creditcard number"));

            Text ccMY = (Text) theText.clone();
              ccMY.setText(iwrb.getLocalizedString("travel.month_year","month / year"));

            Text ccSlash = (Text) theText.clone();
              ccSlash.setText(" / ");

            ++row;
            table.add(ccText,1,row);
            table.add(ccNumber,2,row);

            ++row;
            table.add(ccMY,1,row);
            table.add(ccMonth,2,row);
            table.add(ccSlash,2,row);
            table.add(ccYear,2,row);


          }

          if (booking != null) {
            form.addParameter(this.parameterBookingId,booking.getID());
            surname.setContent(booking.getName());
            address.setContent(booking.getAddress());
            city.setContent(booking.getCity());
            areaCode.setContent(booking.getPostalCode());
            country.setContent(booking.getCountry());
            email.setContent(booking.getEmail());
            telNumber.setContent(booking.getTelephoneNumber());

            if (pickupMenu != null) {
              try {
                pickupMenu.setSelectedElement(Integer.toString(booking.getHotelPickupPlaceID()));
                roomNumber.setContent(booking.getRoomNumber());
              }catch (NullPointerException n) {
                n.printStackTrace(System.err);
              }
            }

          }

          ++row;
          if (booking != null) {
            table.add(new SubmitButton(iwrb.getImage("buttons/update.gif")),4,row);
          }else {
            table.add(new SubmitButton(iwrb.getImage("buttons/book.gif")),4,row);
          }
          table.add(new HiddenInput(this.BookingAction,this.BookingParameter),4,row);
          //table.setAlignment(4,row,"right");
      }else {
        if (supplier != null || reseller != null)
          table.add(iwrb.getLocalizedString("travel.pricecategories_not_set_up_right","Pricecategories not set up right"));
      }


      return form;
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

  private void checkBooking(IWContext iwc) {
    Form form = new Form();
      form.maintainParameter("surname");
      form.maintainParameter("lastname");
      form.maintainParameter("address");
      form.maintainParameter("area_code");
      form.maintainParameter("e-mail");
      form.maintainParameter("telephone_number");
      form.maintainParameter("city");
      form.maintainParameter("country");
      form.maintainParameter(HotelPickupPlace.getHotelPickupPlaceTableName());
      form.maintainParameter("room_number");
      form.maintainParameter("reference_number");
      form.maintainParameter("year");
      form.maintainParameter("month");
      form.maintainParameter("day");
      form.maintainParameter(this.parameterBookingId);

    boolean tooMany = false;
    String sAvailable = iwc.getParameter("available");


    int iAvailable = Integer.parseInt(sAvailable);

    if (iAvailable != available) {
      String many;
      int iMany = 0;
      ProductPrice[] pPrices = ProductPrice.getProductPrices(service.getID(), false);
        int[] manys = new int[pPrices.length];
        for (int i = 0; i < manys.length; i++) {
            form.maintainParameter("priceCategory"+i);
            many = iwc.getParameter("priceCategory"+i);
            if ( (many != null) && (!many.equals("")) && (!many.equals("0"))) {
                manys[i] = Integer.parseInt(many);
                iMany += Integer.parseInt(many);
            }else {
                manys[i] = 0;
            }
        }
        form.add(new HiddenInput("numberOfSeats", Integer.toString(iMany)));

      if (iMany > iAvailable) {
          tooMany = true;
      }
    }


    if (tooMany) {
      Table table = new Table();
        form.add(table);

      if (supplier != null) {
        table.add(iwrb.getLocalizedString("travel.too_many_book_anyway","Too many, book anyway ?"));
        table.add(new SubmitButton(iwrb.getImage("buttons/yes.gif"),this.BookingAction, this.parameterBookAnyway));
        table.add(new BackButton(iwrb.getImage("buttons/no.gif")));
      }else if (reseller != null) {
        table.add(iwrb.getLocalizedString("travel.too_many_send_inquiry","Too many, send inquiry ?"));
        table.add(new SubmitButton(iwrb.getImage("buttons/yes.gif"),this.BookingAction, this.parameterSendInquery));
        table.add(new BackButton(iwrb.getImage("buttons/no.gif")));
      }

      add(form);
    }else {
      saveBooking(iwc, true);
    }

  }


  private int saveBooking(IWContext iwc, boolean displayForm) {
      String surname = iwc.getParameter("surname");
      String lastname = iwc.getParameter("lastname");
      String address = iwc.getParameter("address");
      String areaCode = iwc.getParameter("area_code");
      String email = iwc.getParameter("e-mail");
      String phone = iwc.getParameter("telephone_number");

      String city = iwc.getParameter("city");
      String country = iwc.getParameter("country");
      String hotelPickupPlaceId = iwc.getParameter(HotelPickupPlace.getHotelPickupPlaceTableName());
      String roomNumber = iwc.getParameter("room_number");
      String referenceNumber = iwc.getParameter("reference_number");

      String ccNumber = iwc.getParameter("ccNumber");
      String ccMonth = iwc.getParameter("ccMonth");
      String ccYear = iwc.getParameter("ccYear");

      String sBookingId = iwc.getParameter(this.parameterBookingId);
      int iBookingId = -1;
      if (sBookingId != null) iBookingId = Integer.parseInt(sBookingId);

      int returner = 0;

      String many;
      int iMany = 0;
      int iHotelId;

      ProductPrice[] pPrices = ProductPrice.getProductPrices(service.getID(), false);
      int lbookingId = -1;

      boolean displayFormInternal = false;

      try {
        int[] manys = new int[pPrices.length];
        for (int i = 0; i < manys.length; i++) {
            many = iwc.getParameter("priceCategory"+i);
            if ( (many != null) && (!many.equals("")) && (!many.equals("0"))) {
                manys[i] = Integer.parseInt(many);
                iMany += Integer.parseInt(many);
            }else {
                manys[i] = 0;
            }
        }

        try {
          iHotelId = Integer.parseInt(hotelPickupPlaceId);
        }catch (NumberFormatException n) {
          iHotelId = -1;
        }

        if (supplier != null) {
          if (iBookingId == -1) {
            lbookingId = Booker.BookBySupplier(service.getID(), iHotelId, roomNumber, country, surname+" "+lastname, address, city, phone, email, stamp, iMany, areaCode);
          }else {
            lbookingId = Booker.updateBooking(iBookingId, service.getID(), iHotelId, roomNumber, country, surname+" "+lastname, address, city, phone, email, stamp, iMany, areaCode);
          }
            displayFormInternal = true;
        }else if (reseller != null) {
            if (reseller.getReferenceNumber().equals(referenceNumber)) {
              if (iBookingId == -1) {
                lbookingId = Booker.Book(service.getID(), iHotelId, roomNumber, country, surname+" "+lastname, address, city, phone, email, stamp, iMany, is.idega.travel.data.Booking.BOOKING_TYPE_ID_THIRD_PARTY_BOOKING ,areaCode);
              }else {
                lbookingId = Booker.updateBooking(iBookingId, service.getID(), iHotelId, roomNumber, country, surname+" "+lastname, address, city, phone, email, stamp, iMany, areaCode);
              }
              reseller.addTo(is.idega.travel.data.Booking.class, bookingId);
              displayFormInternal= true;
            }
        }else if ((supplier == null) && (reseller == null) ) {
            // if (Median.isCCValid(ccNumber,ccMonth, ccYear));
            bookingId = Booker.Book(service.getID(), iHotelId, roomNumber, country, surname+" "+lastname, address, city, phone, email, stamp, iMany, is.idega.travel.data.Booking.BOOKING_TYPE_ID_ONLINE_BOOKING ,areaCode);
        }

        returner = bookingId;

        if (lbookingId != -1) {
          if (iBookingId == -1) {
            BookingEntry bEntry;
            for (int i = 0; i < pPrices.length; i++) {
              if (manys[i] != 0) {
                bEntry = new BookingEntry();
                  bEntry.setProductPriceId(pPrices[i].getID());
                  bEntry.setBookingId(lbookingId);
                  bEntry.setCount(manys[i]);
                bEntry.insert();
              }
            }
          }else {
            BookingEntry bEntry;
            ProductPrice price;
            boolean done = false;
            BookingEntry[] entries = Booker.getBookingEntries(new is.idega.travel.data.Booking(iBookingId));
              if (entries == null) entries = new BookingEntry[]{};
            for (int i = 0; i < pPrices.length; i++) {
              done = false;
              for (int j = 0; j < entries.length; j++) {
                if (pPrices[i].getID() == entries[j].getProductPriceId()) {
                  done = true;
                  entries[j].setCount(manys[i]);
                  entries[j].update();
                  break;
                }
              }
              if (!done) {
                bEntry = new BookingEntry();
                  bEntry.setProductPriceId(pPrices[i].getID());
                  bEntry.setBookingId(lbookingId);
                  bEntry.setCount(manys[i]);
                bEntry.insert();
              }
            }
          }
        }



      }catch (NumberFormatException n) {
        n.printStackTrace(System.err);
      }catch (SQLException sql) {
        sql.printStackTrace(System.err);
      }

      if (displayForm)
      if (displayFormInternal)
      displayForm(iwc);


      return returner;
  }

  private void sendInquery(IWContext iwc) {
    String surname = iwc.getParameter("surname");
    String lastname = iwc.getParameter("lastname");
    String address = iwc.getParameter("address");
    String areaCode = iwc.getParameter("area_code");
    String email = iwc.getParameter("e-mail");
    String phone = iwc.getParameter("telephone_number");

    String city = iwc.getParameter("city");
    String country = iwc.getParameter("country");
    String hotelPickupPlaceId = iwc.getParameter(HotelPickupPlace.getHotelPickupPlaceTableName());
    String numberOfSeats = iwc.getParameter("numberOfSeats");

    String referenceNumber = iwc.getParameter("reference_number");

    try {
        int bookingId = saveBooking(iwc, false);
        is.idega.travel.data.Booking booking = new is.idega.travel.data.Booking(bookingId);
          booking.setIsValid(false);
          booking.update();


        Inquirer.sendInquery(surname+" "+lastname, email, stamp, product.getID() , Integer.parseInt(numberOfSeats), bookingId, reseller);

    }catch (SQLException sql) {
      sql.printStackTrace();
    }
  }

  private void inqueryResponse(IWContext iwc) {
    String yesNo = iwc.getParameter(this.parameterRespondInquery);
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
        is.idega.travel.data.Booking booking = inquery.getBooking();
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



  }

  private void updateBooking(IWContext iwc) throws SQLException{
    String sBookingId = iwc.getParameter(this.parameterBookingId);
    if (sBookingId != null) {
        this.bookingId = Integer.parseInt(sBookingId);
        booking = new is.idega.travel.data.Booking(bookingId);
        this.stamp = new idegaTimestamp(booking.getBookingDate());
        displayForm(iwc);
    }
  }

}