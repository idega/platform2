package is.idega.idegaweb.travel.presentation;

import com.idega.block.calendar.business.CalendarBusiness;
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
  public static String BookingParameter = "booking";
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

  public static final int availableIfNoLimit = -1234;
  public static int available = availableIfNoLimit;

  private idegaTimestamp stamp;

  public Booking() {
  }

  public void main(IWContext iwc) throws Exception {
      super.main(iwc);
      initialize(iwc);


        if (reseller != null && contract == null) {
          product = null;
        }

        String action = iwc.getParameter(this.BookingAction);
        if (action == null) {action = "";}

        if (action.equals("")) {
          displayForm(iwc);
        }else if (action.equals(parameterRespondInquery)) {
          inqueryResponse(iwc);
        }else if (action.equals(this.parameterUpdateBooking)){
          this.updateBooking(iwc);
        }else {
          handleInsert(iwc, true);
        }

        super.addBreak();
  }

  private void initialize(IWContext iwc) {
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
        if (sProductId != null && !sProductId.equals("-1")) {
          productId = Integer.parseInt(sProductId);
          product = ProductBusiness.getProduct(productId);
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

  private void displayForm(IWContext iwc) throws Exception {
    displayForm(iwc , null);
  }

  private void displayForm(IWContext iwc, PresentationObject contentTableForm) throws Exception{

      Form topTable = getTopTable(iwc);
          add(Text.BREAK);

      if ((supplier != null) || (reseller != null) || ((supplier == null) && (reseller == null) && (product == null)) ) {
          add(topTable);
          add(Text.BREAK);
      }

        if (product != null) {
          Table contentTable = new Table(1,1);
              contentTable.setBorder(1);
              contentTable.add(getContentHeader(iwc));
              contentTable.add(Text.BREAK);
              contentTable.add(getTotalTable(iwc));
              if (contentTableForm == null) {
                contentTable.add(getContentTable(iwc));
              }else {
                contentTable.add(contentTableForm);
              }
              contentTable.setWidth("90%");
              contentTable.setCellspacing(0);
              contentTable.setCellpadding(0);
              contentTable.setBorderColor(super.textColor);
            add(contentTable);
        }
        else {
          add(iwrb.getLocalizedString("travel.select_a_product","Select a product"));
        }



      int row = 0;
      add(Text.getBreak());
  }


  private Form getTopTable(IWContext iwc) {
    Form form = new Form();
      Table topTable = new Table(5,1);
        form.add(topTable);
        topTable.setBorder(0);
        topTable.setWidth("90%");

      Text dateText = (Text) theText.clone();
          dateText.setText(iwrb.getLocalizedString("travel.date","Date"));
          dateText.addToText(":");

      DropdownMenu trip = null;
      if (supplier != null) {
        trip = ProductBusiness.getDropdownMenuWithProducts(iwc, supplierId);
      }else if (reseller != null) {
        trip = ResellerManager.getDropdownMenuWithProducts(iwc, resellerId);
      }else if (product == null) {
        trip = new DropdownMenu(ProductBusiness.getProducts(iwc, -1));
      }

      if (trip != null)
          if (product != null) {
              trip.setSelectedElement(Integer.toString(product.getID()));
          }

      Text nameText = (Text) theText.clone();
          nameText.setText(iwrb.getLocalizedString("travel.product_name_lg","Name of product"));
          nameText.addToText(":");


      DateInput dateInp = new DateInput("IWCalendar");
        dateInp.setDate(stamp.getSQLDate());

      topTable.setColumnAlignment(1,"right");
      topTable.setColumnAlignment(2,"left");
      topTable.add(nameText,1,1);
      topTable.add(trip,2,1);
      topTable.add(dateText,3,1);
      topTable.add(dateInp,4,1);

      topTable.setAlignment(5,1,"right");
      topTable.add(new SubmitButton(iwrb.getImage("buttons/get.gif"),"", ""),5,1);

//      topTable.add(new HiddenInput("month",Integer.toString(stamp.getMonth()) ));
//      topTable.add(new HiddenInput("day",Integer.toString(stamp.getDay())));
//      topTable.add(new HiddenInput("year",Integer.toString(stamp.getYear())));

      return form;
  }

  private Table getContentHeader(IWContext iwc) throws Exception{
    ServiceOverview so = new ServiceOverview(iwc);
    Table table = so.getProductInfoTable(iwc, iwrb, this.product);

    return table;
  }

  private Table getContentTable(IWContext iwc) throws Exception{
      Table table = new Table();
        table.setWidth("100%");
        table.setBorder(0);
        table.setCellspacing(0);
        table.setCellpadding(2);
        table.setWidth(6,"200");

        /**
         * @todo minnka endurtekningar
         */

      int row = 1;
      boolean isDayVisible = false;
      boolean isExpired = false;
      int iBookings = 0;
      try {
        if (reseller != null) {
          isExpired = TravelStockroomBusiness.getIfExpired(contract, stamp);
          if (!isExpired) {
            if (this.tour != null) {
              isDayVisible = TourBusiness.getIfDay(iwc, contract, tour, stamp);
            }else {
              isDayVisible = TravelStockroomBusiness.getIfDay(iwc, contract, product, stamp);
            }
          }
        }
        else {
          if (this.tour != null) {
            isDayVisible = TourBusiness.getIfDay(iwc, tour, stamp, false);
          }else {
            isDayVisible = TravelStockroomBusiness.getIfDay(iwc,this.product, this.stamp);
          }
          if (supplier == null) {
            if (isDayVisible) {
              iBookings = Booker.getNumberOfBookings(productId, stamp);
              /*if (iBookings < tour.getMinimumSeats() || iBookings > tour.getTotalSeats()) {
                isDayVisible = false;
              }*/ /** @todo setja í calendarHandler */
            }
          }
        }
      }catch (TravelStockroomBusiness.ServiceNotFoundException snfe) {
            snfe.printStackTrace(System.err);
      }catch (TravelStockroomBusiness.TimeframeNotFoundException tfnfe) {
            tfnfe.printStackTrace(System.err);
      }


      boolean yearly = timeframe.getIfYearly();
      List depDays = null;


      if (isDayVisible) {
          table.setColor(6,row,super.backgroundColor);
          table.add(Text.BREAK ,6,row);
          table.add(getCalendar(iwc),6,row);
          table.setVerticalAlignment(6,row,"top");

          table.mergeCells(1,row,5,row);
          if (supplier != null) {
            Inquery[] inqueries = Inquirer.getInqueries(product.getID(), stamp , true, Inquery.getInqueryPostDateColumnName());

            if (inqueries.length > 0) {
              table.add(getInqueries(iwc, inqueries),1,row);
              table.setColor(1,row,super.YELLOW);
          ++row;
            }else {
              table.setColor(1,row,super.backgroundColor);
            }
          }
          else if (reseller != null) {
            Inquery[] inqueries = Inquirer.getInqueries(product.getID(), stamp ,reseller.getID(), true, Inquery.getInqueryPostDateColumnName());

            if (inqueries.length > 0) {
              table.add(getInqueries(iwc, inqueries),1,row);
              table.setColor(1,row,super.YELLOW);
          ++row;
            }else {
              table.setColor(1,row,super.backgroundColor);
            }
          }


          table.mergeCells(1,row,5,row);
          table.setColor(1,row,super.backgroundColor );
          table.mergeCells(6,1,6,row);
          table.add(Text.BREAK ,1,row);
          table.add(Text.BREAK ,1,row);
          table.add(getBookingForm(iwc),1,row);

      }else {
        if (isExpired) {
          if (supplier != null) {
            Inquery[] inqueries = Inquirer.getInqueries(product.getID(), stamp , true, Inquery.getInqueryPostDateColumnName());

            if (inqueries.length > 0) {
              table.add(getInqueries(iwc, inqueries),1,row);
              table.setColor(1,row,super.YELLOW);
          ++row;
            }else {
              table.setColor(1,row,super.backgroundColor);
            }
          }
          else if (reseller != null) {
            Inquery[] inqueries = Inquirer.getInqueries(product.getID(), stamp ,reseller.getID(), true, Inquery.getInqueryPostDateColumnName());

            if (inqueries.length > 0) {
              table.add(getInqueries(iwc, inqueries),1,row);
              table.setColor(1,row,super.YELLOW);
          ++row;
            }else {
              table.setColor(1,row,super.backgroundColor);
            }
          }
          table.add(iwrb.getLocalizedString("travel.time_for_booking_has_passed","Time for booking has passed"),1,row+2);
        }else {
          if (supplier != null) {
            Inquery[] inqueries = Inquirer.getInqueries(product.getID(), stamp , true, Inquery.getInqueryPostDateColumnName());

            if (inqueries.length > 0) {
              table.add(getInqueries(iwc, inqueries),1,row);
              table.setColor(1,row,super.YELLOW);
          ++row;
            }else {
              table.setColor(1,row,super.backgroundColor);
            }
          }
          else if (reseller != null) {
            Inquery[] inqueries = Inquirer.getInqueries(product.getID(), stamp ,reseller.getID(), true, Inquery.getInqueryPostDateColumnName());

            if (inqueries.length > 0) {
              table.add(getInqueries(iwc, inqueries),1,row);
              table.setColor(1,row,super.YELLOW);
          ++row;
            }else {
              table.setColor(1,row,super.backgroundColor);
            }
          }
          table.add(iwrb.getLocalizedString("travel.trip_is_not_scheduled_this_day","Trip is not scheduled this day")+" : "+stamp.getLocaleDate(iwc));
        }
          table.mergeCells(1,row,5,row);
          table.setAlignment(1,row, "center");

          table.setColor(6,row,super.backgroundColor);
          table.add(Text.BREAK,6,row);
          table.add(getCalendar(iwc),6,row);
          table.setVerticalAlignment(6,row,"top");

          ++row;
          table.mergeCells(1,row,5,row);
          table.setColor(1,row,super.backgroundColor );
          table.mergeCells(6,1,6,row);
      }

      return table;

  }

  private Table getCalendar(IWContext iwc) {
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


  private Table getInqueries(IWContext iwc,Inquery[] inqueries) {
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

      List groupInq;
      idegaTimestamp tempStamp;

      for (int i = 0; i < inqueries.length; i++) {
          theStamp = new idegaTimestamp(inqueries[i].getInqueryDate());

          groupInq = Inquirer.getMultibleInquiries(inqueries[i]);
          dateText = (Text) theSmallBoldText.clone();
            dateText.setFontColor(BLACK);
          if (groupInq == null || groupInq.size() <= 1) {
            dateText.setText(theStamp.getLocaleDate(iwc));
          }else {
            theStamp = new idegaTimestamp(((Inquery) groupInq.get(0)).getInqueryDate());
            tempStamp = new idegaTimestamp(((Inquery) groupInq.get(groupInq.size()-1)).getInqueryDate());
            dateText.setText(theStamp.getLocaleDate(iwc)+" - "+tempStamp.getLocaleDate(iwc));
          }
          nameText = (Text) theSmallBoldText.clone();
              nameText.setFontColor(BLACK);
              nameText.setText(inqueries[i].getName());
          countText = (Text) theSmallBoldText.clone();
              countText.setFontColor(BLACK);
              countText.setText(Integer.toString(inqueries[i].getNumberOfSeats()));
          contentText = (Text) theSmallBoldText.clone();
              contentText.setFontColor(BLACK);
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
              answerYes = new Link(iwrb.getLocalizedImageButton("travel.confirm_booking","Confirm_booking"));
                answerYes.addParameter(this.parameterInqueryId,inqueries[i].getID());
                answerYes.addParameter(this.parameterRespondInquery, this.parameterRespondYes);
                answerYes.addParameter(this.BookingAction, this.parameterRespondInquery);
                answerYes.addParameter("year",this.stamp.getYear());
                answerYes.addParameter("month",this.stamp.getMonth());
                answerYes.addParameter("day",this.stamp.getDay());

              answerNo = new Link(iwrb.getLocalizedImageButton("travel.reject_booking","Reject_booking"));
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
              answerNo = new Link(iwrb.getLocalizedImageButton("travel.cancel_inquiry","Cancel_inquiry"));
                answerNo.addParameter(this.parameterInqueryId,inqueries[i].getID());
                answerNo.addParameter(this.parameterRespondInquery, this.parameterRespondNo);
                answerNo.addParameter(this.BookingAction, this.parameterRespondInquery);

              table.add(answerNo,2,row);
          }
      }

      return table;
  }


  private Form getTotalTable(IWContext iwc) {
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

            iBooked = Booker.getNumberOfBookingsByReseller(resellerId, service.getID(), this.stamp);
            bookedTextBold.setText(Integer.toString(iBooked));

            iInquery = Inquirer.getInqueredSeats(service.getID(), this.stamp, reseller.getID(), true);
            inqTextBold.setText(Integer.toString(iInquery));

            if (iCount >0) {
              iAvailable = iCount - iBooked -iInquery;
              available = iAvailable;
              availableTextBold.setText(Integer.toString(iAvailable));
            }
            iCount = iCount - iBooked;

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

  private Form getBookingForm(IWContext iwc) throws Exception{
    TourBookingForm tbf = new TourBookingForm(iwc,product);
    try {
      if (reseller != null) tbf.setReseller(reseller);
      tbf.setTimestamp(stamp);
      if (booking != null)  tbf.setBooking(booking);
      return tbf.getBookingForm(iwc);
    }catch (Exception e) {
      return new Form();
    }
  }


  // BUSINESS
  private idegaTimestamp getIdegaTimestamp(IWContext iwc) {
      idegaTimestamp stamp = null;

      String year = iwc.getParameter("year");
      String month = iwc.getParameter("month");
      String day = iwc.getParameter("day");

      String IWCalendar_year = iwc.getParameter(CalendarBusiness.PARAMETER_YEAR);
      String IWCalendar_month = iwc.getParameter(CalendarBusiness.PARAMETER_MONTH);
      String IWCalendar_day = iwc.getParameter(CalendarBusiness.PARAMETER_DAY);
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

    TourBookingForm tbf = new TourBookingForm(iwc, product);
    try {
      if (reseller != null) tbf.setReseller(reseller);
      tbf.setTimestamp(stamp);

      int returner = tbf.handleInsert(iwc);

      if (returner < 1) {
        displayForm(iwc, tbf.getErrorForm(iwc, returner));
      }else if (displayForm) {
        add(Text.BREAK);
        add(bookingInformation(iwc, returner));
//        displayForm(iwc, );
      }

      return returner;
    }catch (Exception e) {
      e.printStackTrace(System.err);

      return -1;
    }
  }

  private Table bookingInformation(IWContext iwc, int bookingId) {
    try {
      GeneralBooking booking = new GeneralBooking(bookingId);
      int voucherNumber = Voucher.getVoucherNumber(bookingId);
      String referenceNumber = booking.getReferenceNumber();

      Text bookingSuccessful = (Text) super.theBoldText.clone();
        bookingSuccessful.setText(iwrb.getLocalizedString("travel.booking_was_successful","Booking was successful"));

      Text refNumTxt = (Text) super.theText.clone();
        refNumTxt.setText(iwrb.getLocalizedString("travel.reference_number","Reference number"));
        refNumTxt.setFontColor(super.BLACK);
      Text voucherNumTxt = (Text) super.theText.clone();
        voucherNumTxt.setText(iwrb.getLocalizedString("travel.voucher_number","Voucher number"));
        voucherNumTxt.setFontColor(super.BLACK);
      Text voucher = (Text) super.theBoldText.clone();
        voucher.setText(iwrb.getLocalizedString("travel.voucher","Voucher"));
        voucher.setFontColor(super.BLACK);
      Link voucherLink = new Link(voucher);
        voucherLink.setWindowToOpen(VoucherWindow.class);
        voucherLink.addParameter(VoucherWindow.parameterBookingId, bookingId);

      Text refNum = (Text) super.theBoldText.clone();
        refNum.setText(referenceNumber);
        refNum.setFontColor(super.BLACK);
      Text voucherNum = (Text) super.theBoldText.clone();
        voucherNum.setText(Integer.toString(voucherNumber));
        voucherNum.setFontColor(super.BLACK);

      Link backLink = new Link(iwrb.getImage("buttons/back.gif"));
        backLink.addParameter(this.parameterProductId, this.productId);
        backLink.addParameter(CalendarBusiness.PARAMETER_DAY, stamp.getDay());
        backLink.addParameter(CalendarBusiness.PARAMETER_MONTH, stamp.getMonth());
        backLink.addParameter(CalendarBusiness.PARAMETER_YEAR, stamp.getYear());

      Table table = new Table();
        table.setColor(super.WHITE);
        table.add(bookingSuccessful,1,1);
        table.add(refNumTxt, 1, 2);
        table.add(refNum, 2, 2);
        table.add(voucherNumTxt, 1, 3);
        table.add(voucherNum, 2, 3);
        table.add(voucherLink, 1, 4);
        table.add(Text.NON_BREAKING_SPACE, 1, 5);
        table.add(backLink, 1, 6);

        table.mergeCells(1,1,2,1);
        table.mergeCells(1,4,2,4);
        table.mergeCells(1,5,2,5);
        table.mergeCells(1,6,2,6);

        table.setRowColor(1, super.backgroundColor);
        table.setRowColor(2, super.GRAY);
        table.setRowColor(3, super.GRAY);
        table.setRowColor(4, super.GRAY);
        table.setRowColor(5, super.GRAY);
        table.setRowColor(6, super.GRAY);

        table.setAlignment(1, 6, "center");

      return table;
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
      return new Table();
    }
  }


  private void inqueryResponse(IWContext iwc) throws Exception{
    String yesNo = iwc.getParameter(this.parameterRespondInquery);
    String sInqueryId = iwc.getParameter(this.parameterInqueryId);
    Boolean book = null;


    if (yesNo != null) {
      if (yesNo.equals(this.parameterRespondYes)) book = new Boolean(true);
      if (yesNo.equals(this.parameterRespondNo)) book = new Boolean(false);
    }

    if (book != null && sInqueryId != null) {
        int inqueryId = Integer.parseInt(sInqueryId);
        int errorMessage = Inquirer.inquiryResponse(iwc, iwrb, inqueryId, book.booleanValue(), this.supplier);

        switch (errorMessage) {
          case 0 : displayForm(iwc);
            break;
          case 1 : displayForm(iwc, Inquirer.getInquiryResponseError(iwrb));
            break;
        }
    }

  }


  private void updateBooking(IWContext iwc) throws Exception{
    String sBookingId = iwc.getParameter(this.parameterBookingId);
    if (sBookingId != null) {
        this.bookingId = Integer.parseInt(sBookingId);
        booking = new GeneralBooking(bookingId);
        this.stamp = new idegaTimestamp(booking.getBookingDate());
        displayForm(iwc);
    }else {
      displayForm(iwc, getErrorUpdateBookingTable());
    }
  }


  private Table getErrorUpdateBookingTable() {
    Table table = new Table();
      Text text = (Text) theBoldText.clone();
        text.setFontColor(super.WHITE);
        text.setText(iwrb.getLocalizedString("travel.cannot_edit_booking","Cannot edit booking"));
      table.add(text);
    return table;
  }

}
