package is.idega.idegaweb.travel.presentation;

import com.idega.presentation.Block;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.text.*;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.block.trade.stockroom.data.*;
import com.idega.jmodule.calendar.presentation.SmallCalendar;
import com.idega.util.idegaTimestamp;
import com.idega.util.idegaCalendar;
import com.idega.core.accesscontrol.business.AccessControl;
import java.sql.SQLException;
import java.util.Map;
import java.util.Hashtable;

import is.idega.idegaweb.travel.business.Booker;
import is.idega.idegaweb.travel.business.TravelStockroomBusiness;
import is.idega.idegaweb.travel.data.*;
import is.idega.idegaweb.travel.service.tour.data.*;
import is.idega.idegaweb.travel.service.tour.business.*;
import is.idega.idegaweb.travel.service.tour.presentation.*;
import is.idega.idegaweb.travel.interfaces.Booking;

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

  private Supplier supplier;
  private Product product;
  private Service service;
  private Tour tour;
  private Timeframe timeframe;

  private idegaTimestamp stamp;

  private TravelStockroomBusiness tsb = TravelStockroomBusiness.getNewInstance();

  private String sAction = "dailyReportAction";
  private String parameterUpdate = "dailyReportUpdate";
  private String parameterToggleCloser = "dailyReportCloser";
  private String parameterYes = "yes";
  private String parameterNo = "no";

  private boolean closerLook = false;


  public DailyReport() {
  }

  public void add(PresentationObject mo) {
    super.add(mo);
  }

  public void main(IWContext iwc) throws Exception {
      super.main(iwc);
      initialize(iwc);

      if (super.isLoggedOn(iwc)) {
        String action = iwc.getParameter(sAction);
        if (action == null) {action = "";}

        if (action.equals(this.parameterUpdate)) {
          update(iwc);
        }else if (action.equals(this.parameterYes)) {
          this.closerLook = true;
        }else if (action.equals(this.parameterNo)) {
          this.closerLook = false;
        }
        displayForm(iwc);

        super.addBreak();
      }else {
        add(super.getLoggedOffTable(iwc));
      }
  }

  public void initialize(IWContext iwc) {
      bundle = super.getBundle();
      iwrb = super.getResourceBundle();

      supplier = super.getSupplier();

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

      String toggler = iwc.getParameter(this.parameterToggleCloser);
      if (toggler != null) {
        if (toggler.equals(this.parameterYes)) {
          this.closerLook = true;
        }else if (toggler.equals(this.parameterNo) ) {
          this.closerLook = false;
        }
      }

      stamp = getFromIdegaTimestamp(iwc);
  }

  public void displayForm(IWContext iwc) {

      Form form = new Form();
      Table topTable = getTopTable(iwc);
        form.add(topTable);

        if (product != null) {
            form.add(getContentHeader(iwc));
          Table table = getContentTable(iwc);
            form.add(table);

          form.add(Text.BREAK);
          Table par = new Table();
            par.setAlignment(1,1,"right");
            par.setAlignment("center");
            par.setWidth("90%");
            par.add(new PrintButton(iwrb.getImage("buttons/print.gif")));
          form.add(par);

        }
        else {
          form.add("T ekkert product valið");
        }

      int row = 0;
      add(Text.getBreak());
      add(form);
  }


  // BUSINESS
  public idegaTimestamp getFromIdegaTimestamp(IWContext iwc) {
      idegaTimestamp stamp = null;
      String from_time = iwc.getParameter("active_from");
      if (from_time!= null) {
          try {
              stamp = new idegaTimestamp(from_time);
          }
          catch (RuntimeException e) {
              stamp = idegaTimestamp.RightNow();
          }
      }
      else {
          stamp = idegaTimestamp.RightNow();
      }
      return stamp;
  }


  public Table getTopTable(IWContext iwc) {
      Table topTable = new Table(5,2);
        topTable.setBorder(0);
        topTable.setWidth("90%");

      Text tframeText = (Text) theText.clone();
          tframeText.setText(iwrb.getLocalizedString("travel.timeframe_only","Timeframe"));
          tframeText.addToText(":");

      DropdownMenu trip = null;
        trip = new DropdownMenu(tsb.getProducts(supplier.getID()));
          if (product != null) {
              trip.setSelectedElement(Integer.toString(product.getID()));
          }

      DateInput active_from = new DateInput("active_from");
          idegaTimestamp fromStamp = getFromIdegaTimestamp(iwc);
          active_from.setDate(fromStamp.getSQLDate());

      Text nameText = (Text) theText.clone();
          nameText.setText(iwrb.getLocalizedString("travel.trip_name_lg","Name of trip"));
          nameText.addToText(":");
      Text timeframeText = (Text) theText.clone();
          timeframeText.setText(iwrb.getLocalizedString("travel.date","Date"));
          timeframeText.addToText(":");



      topTable.setColumnAlignment(1,"right");
      topTable.setColumnAlignment(2,"left");
      topTable.setColumnAlignment(3,"right");
      topTable.setColumnAlignment(4,"left");
      topTable.add(nameText,1,1);
      topTable.add(trip,2,1);
      topTable.add(timeframeText,3,1);
      topTable.add(active_from,4,1);
//      topTable.mergeCells(2,1,4,1);
//     topTable.mergeCells(2,2,4,2);

      topTable.setAlignment(5,1,"right");
      topTable.add(new SubmitButton(iwrb.getImage("/buttons/get.gif")),5,1);

      return topTable;
  }

  public Table getContentHeader(IWContext iwc) {
      Table table = new Table(2,2);
      table.setWidth("90%");



      String mode = iwc.getParameter("mode");
      if (mode== null) mode="";


      Text headerText = (Text) theBoldText.clone();
          headerText.setFontColor(super.textColor);
          headerText.setText(iwrb.getLocalizedString("travel.daily_report","Daily report"));
          headerText.addToText(" : ");

      Text timeText = (Text) theBoldText.clone();
          timeText.setText(stamp.getLocaleDate(iwc));
          timeText.setFontColor(super.textColor);
      Text nameText = (Text) theBoldText.clone();
          nameText.setText(product.getName());


      table.setColumnAlignment(1,"left");
      table.add(headerText,1,1);
      table.add(nameText,1,1);
      table.setAlignment(2,1,"right");
      table.add(timeText,2,1);


      return table;
  }

  public Table getContentTable(IWContext iwc) {

      int totalBookings = 0;
      int totalAttendance = 0;
      int totalAmount = 0;

      Table theTable = new Table();
          theTable.setBorder(0);
          theTable.setWidth("90%");
          if (closerLook) {
            theTable.add(new HiddenInput(this.parameterToggleCloser , this.parameterYes));
          }else {
            theTable.add(new HiddenInput(this.parameterToggleCloser , this.parameterNo));
          }

      Table table = new Table();
        table.setWidth("100%");
        table.setBorder(0);
        table.setCellspacing(1);
        table.setColor(super.WHITE);
        table.setCellpadding(2);

      int row = 1;

      String twoWidth = "150";
      String threeWidth = "70";
      String fourWidth = "90";
      String fiveWidth = "100";


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

      Text correctionHText = (Text) theSmallBoldText.clone();
          correctionHText.setText(iwrb.getLocalizedString("travel.correction","Correction"));

      Text totalHText = (Text) theBoldText.clone();
          totalHText.setText(iwrb.getLocalizedString("travel.total","Total"));

      TextInput textBoxToClone = new TextInput("attendance");
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
      table.setAlignment(2,1, "center");

      table.setWidth(2,twoWidth);
      table.setWidth(3,threeWidth);
      table.setWidth(4,fourWidth);
      table.setWidth(5,fiveWidth);

      table.setBorderColor(super.textColor);

      int attendance;
      int ibookings;
      float amount;

      int[] bookingTypeIds = {Booking.BOOKING_TYPE_ID_INQUERY_BOOKING, Booking.BOOKING_TYPE_ID_ONLINE_BOOKING , Booking.BOOKING_TYPE_ID_SUPPLIER_BOOKING ,Booking.BOOKING_TYPE_ID_THIRD_PARTY_BOOKING };
      ProductPrice[] prices = ProductPrice.getProductPrices(service.getID(), false);
      ProductPrice price;
      Integer entryCount;
      int iEntryCount;

      Map map = new Hashtable();
      for (int i = 0; i < prices.length; i++) {
        map.put(prices[i].getPriceCategoryIDInteger(),new Integer(0));
      }


      Booking[] bookings = TourBooker.getBookings(product.getID(),stamp,bookingTypeIds);

      String theColor = super.GRAY;
      DropdownMenu payType;
      BookingEntry[] entries;
      int iBookingId = 0;
      table.setRowColor(row,super.backgroundColor);
      for (int i = 0; i < bookings.length; i++) {
          row++;

          attendance = 0;
          ibookings = 0;
          amount = 0;

          ibookings = bookings[i].getTotalCount();
          attendance = bookings[i].getAttendance();
          amount = Booker.getBookingPrice(bookings[i]);

          totalBookings += ibookings;
          if (attendance != -1000)
          totalAttendance += attendance;
          totalAmount += amount;

          table.setRowColor(row,super.backgroundColor);
          nameText = (Text) smallText.clone();
            nameText.setText(bookings[i].getName());

          payTypeText = (Text) smallText.clone();
          payType = (DropdownMenu) Booker.getPaymentTypes(iwrb).clone();
            payType.setSelectedElement(Integer.toString(bookings[i].getPaymentTypeId()));
          iBookingId = bookings[i].getPaymentTypeId();
/*
          switch (iBookingId) {
            case Booking.PAYMENT_TYPE_ID_CREDIT_CARD :
                payTypeText.setText(iwrb.getLocalizedString("travel.credit_card","Credit card"));
              break;
            case Booking.PAYMENT_TYPE_ID_CASH :
                payTypeText.setText(iwrb.getLocalizedString("travel.cash","Cash"));
            break;
            case Booking.PAYMENT_TYPE_ID_NO_PAYMENT :
                payTypeText.setText(iwrb.getLocalizedString("travel.unpaid","Unpaid"));
            break;
            case Booking.BOOKING_TYPE_ID_INQUERY_BOOKING :
                payTypeText.setText(iwrb.getLocalizedString("travel.bookings_from_supplier","Booked by supplier"));
            break;
            default:
                payTypeText.setText("");
            break;
          }
*/

          bookedText = (Text) smallText.clone();
            bookedText.setText(Integer.toString(ibookings));

          attTextBox = (TextInput) textBoxToClone.clone();
            attTextBox.setSize(3);
          if (attendance != -1000) {
            attTextBox.setContent(Integer.toString(attendance));
            }
          amountText = (Text) smallText.clone();
            amountText.setText(Integer.toString((int) amount));

          nameText.setFontColor(super.BLACK);
          payTypeText.setFontColor(super.BLACK);
          bookedText.setFontColor(super.BLACK);
          amountText.setFontColor(super.BLACK);

          table.add(new HiddenInput("booking_id",Integer.toString(bookings[i].getID())),1,row);
          table.add(nameText,1,row);
          table.add(payType,2,row);
          table.add(bookedText,3,row);
          table.add(attTextBox,4,row);
          table.add(amountText,5,row);
          table.setAlignment(2,row, "center");
          table.setRowColor(row, theColor);

          if (closerLook)
          try {
            entries = bookings[i].getBookingEntries();
            for (int j = 0; j < entries.length; j++) {
              ++row;
              table.setRowColor(row, theColor);
              price = entries[j].getProductPrice();
              iEntryCount = (int) Booker.getBookingEntryPrice(entries[j], bookings[i]);

              nameText = (Text) smallText.clone();
                nameText.setText(Text.NON_BREAKING_SPACE + Text.NON_BREAKING_SPACE + price.getPriceCategory().getName());
              bookedText = (Text) smallText.clone();
                bookedText.setText(Integer.toString(entries[j].getCount()));
              amountText = (Text) smallText.clone();
                amountText.setText(Integer.toString(iEntryCount));

              nameText.setFontColor(super.BLACK);
              bookedText.setFontColor(super.BLACK);
              amountText.setFontColor(super.BLACK);


              entryCount = (Integer) map.get(price.getPriceCategoryIDInteger());
              entryCount = new Integer(entryCount.intValue() + entries[j].getCount());
              map.put(price.getPriceCategoryIDInteger(),entryCount);

              table.add(nameText,2,row);
              table.add(bookedText,3,row);
              table.add(amountText,4,row);
              table.setAlignment(2,row, "LEFT");
            }


          }catch (SQLException sql) {
            sql.printStackTrace(System.err);
          }

      }

      table.setColumnAlignment(1,"left");
      table.setColumnAlignment(3,"center");
      table.setColumnAlignment(4,"center");
      table.setColumnAlignment(5,"center");


      theColor = super.GRAY;
      Table addTable = new Table();
        int addRow = 0;
          addTable.setWidth("100%");
          addTable.setBorder(0);
          addTable.setCellspacing(1);
          addTable.setColor(super.WHITE);
          addTable.setBorderColor(super.textColor);
          addTable.setWidth(2,twoWidth);
          addTable.setWidth(3,threeWidth);
          addTable.setWidth(4,fourWidth);
          addTable.setWidth(5,fiveWidth);

      bookings = Booker.getBookings(product.getID(),stamp,Booking.BOOKING_TYPE_ID_ADDITIONAL_BOOKING);
      if (bookings.length == 0) {
        addRow++;
        addTable.setRowColor(addRow,theColor);
      }
      for (int i = 0; i < bookings.length; i++) {
          ++addRow;
          addTable.setRowColor(addRow,super.backgroundColor);
          addTable.setAlignment(2,addRow,"center");
          ibookings = bookings[i].getTotalCount();
          attendance = bookings[i].getAttendance();
          amount = Booker.getBookingPrice(bookings[i]);

          totalBookings += ibookings;
          if (attendance != -1000)
          totalAttendance += attendance;
          totalAmount += amount;

          nameText = (Text) smallText.clone();
            nameText.setText(bookings[i].getName());

          payTypeText = (Text) smallText.clone();
            payTypeText.setText(iwrb.getLocalizedString("travel.paid_on_location","Paid on loaction"));


          bookedText = (Text) smallText.clone();
            bookedText.setText(Integer.toString(ibookings));

          attTextBox = (TextInput) textBoxToClone.clone();
            attTextBox.setSize(3);
          if (attendance != -1000) {
            attTextBox.setContent(Integer.toString(attendance));
          }
          amountText = (Text) smallText.clone();
            amountText.setText(Integer.toString((int) amount));


          nameText.setFontColor(super.BLACK);
          payTypeText.setFontColor(super.BLACK);
          bookedText.setFontColor(super.BLACK);
          amountText.setFontColor(super.BLACK);

          addTable.add(new HiddenInput("booking_id",Integer.toString(bookings[i].getID())),1,addRow);
          addTable.add(nameText,1,addRow);
          addTable.add(payTypeText,2,addRow);
          addTable.add(bookedText,3,addRow);
          addTable.add(attTextBox,4,addRow);
          addTable.add(amountText,5,addRow);
          addTable.setRowColor(addRow, theColor);

          if (closerLook)
          try {
            entries = bookings[i].getBookingEntries();
            for (int j = 0; j < entries.length; j++) {
              ++addRow;
              addTable.setRowColor(addRow, theColor);
              price = entries[j].getProductPrice();
              iEntryCount = (int) Booker.getBookingEntryPrice(entries[j], bookings[i]);

              nameText = (Text) smallText.clone();
                nameText.setText(Text.NON_BREAKING_SPACE + Text.NON_BREAKING_SPACE + price.getPriceCategory().getName());
              bookedText = (Text) smallText.clone();
                bookedText.setText(Integer.toString(entries[j].getCount()));
              amountText = (Text) smallText.clone();
                amountText.setText(Integer.toString(iEntryCount));

              nameText.setFontColor(super.BLACK);
              bookedText.setFontColor(super.BLACK);
              amountText.setFontColor(super.BLACK);

              entryCount = (Integer) map.get(price.getPriceCategoryIDInteger());
              entryCount = new Integer(entryCount.intValue() + entries[j].getCount());
              map.put(price.getPriceCategoryIDInteger(),entryCount);

              addTable.add(nameText,2,addRow);
              addTable.add(bookedText,3,addRow);
              addTable.add(amountText,4,addRow);
              addTable.setAlignment(2,addRow, "LEFT");
            }


          }catch (SQLException sql) {
            sql.printStackTrace(System.err);
          }

      }
      addTable.setColumnAlignment(1,"left");
      addTable.setColumnAlignment(3,"center");
      addTable.setColumnAlignment(4,"center");
      addTable.setColumnAlignment(5,"center");


      //---------------------CORRECTION----------------------

      theColor = super.GRAY;
      Table correctionTable = new Table();
        int corrRow = 0;
          correctionTable.setWidth("100%");
          correctionTable.setBorder(0);
          correctionTable.setCellspacing(1);
          correctionTable.setColor(super.WHITE);
          correctionTable.setBorderColor(super.textColor);
          correctionTable.setWidth(2,twoWidth);
          correctionTable.setWidth(3,threeWidth);
          correctionTable.setWidth(4,fourWidth);
          correctionTable.setWidth(5,fiveWidth);

      bookings = Booker.getBookings(product.getID(),stamp,Booking.BOOKING_TYPE_ID_CORRECTION);
      if (bookings.length == 0) {
        corrRow++;
        correctionTable.setRowColor(corrRow,theColor);
      }
      for (int i = 0; i < bookings.length; i++) {
          ++corrRow;
          correctionTable.setRowColor(corrRow,super.backgroundColor);
          correctionTable.setAlignment(2,corrRow,"center");
          ibookings = bookings[i].getTotalCount();
          attendance = bookings[i].getAttendance();
          amount = Booker.getBookingPrice(bookings[i]);

          totalBookings += ibookings;
          if (attendance != -1000)
          totalAttendance += attendance;
          totalAmount += amount;

          nameText = (Text) smallText.clone();
            nameText.setText(bookings[i].getName());

          bookedText = (Text) smallText.clone();
            bookedText.setText(Integer.toString(ibookings));

          attTextBox = (TextInput) textBoxToClone.clone();
            attTextBox.setSize(3);
          if (attendance != -1000) {
            attTextBox.setContent(Integer.toString(attendance));
          }
          amountText = (Text) smallText.clone();
            amountText.setText(Integer.toString((int) amount));

          nameText.setFontColor(super.BLACK);
          bookedText.setFontColor(super.BLACK);
          amountText.setFontColor(super.BLACK);

          correctionTable.add(new HiddenInput("booking_id",Integer.toString(bookings[i].getID())),1,corrRow);
          correctionTable.add(nameText,1,corrRow);

          correctionTable.add(bookedText,3,corrRow);
          correctionTable.add(attTextBox,4,corrRow);
          correctionTable.add(amountText,5,corrRow);
          correctionTable.setRowColor(corrRow, theColor);

          if (closerLook)
          try {
            entries = bookings[i].getBookingEntries();
            for (int j = 0; j < entries.length; j++) {
              ++corrRow;
              correctionTable.setRowColor(corrRow, theColor);
              price = entries[j].getProductPrice();
              iEntryCount = (int) Booker.getBookingEntryPrice(entries[j], bookings[i]);

              nameText = (Text) smallText.clone();
                nameText.setText(Text.NON_BREAKING_SPACE + Text.NON_BREAKING_SPACE + price.getPriceCategory().getName());
              bookedText = (Text) smallText.clone();
                bookedText.setText(Integer.toString(entries[j].getCount()));
              amountText = (Text) smallText.clone();
                amountText.setText(Integer.toString(iEntryCount));

              nameText.setFontColor(super.BLACK);
              bookedText.setFontColor(super.BLACK);
              amountText.setFontColor(super.BLACK);

              entryCount = (Integer) map.get(price.getPriceCategoryIDInteger());
              entryCount = new Integer(entryCount.intValue() + entries[j].getCount());
              map.put(price.getPriceCategoryIDInteger(),entryCount);

              correctionTable.add(nameText,2,corrRow);
              correctionTable.add(bookedText,3,corrRow);
              correctionTable.add(amountText,4,corrRow);
              correctionTable.setAlignment(2,corrRow, "LEFT");
            }


          }catch (SQLException sql) {
            sql.printStackTrace(System.err);
          }

      }
      correctionTable.setColumnAlignment(1,"left");
      correctionTable.setColumnAlignment(3,"center");
      correctionTable.setColumnAlignment(4,"center");
      correctionTable.setColumnAlignment(5,"center");


      theColor = super.GRAY;
      Table totalTable = new Table();
          totalTable.setWidth("100%");
          totalTable.setBorder(0);
          totalTable.setCellspacing(1);
          totalTable.setColor(super.WHITE);
          totalTable.setBorderColor(super.textColor);
          totalTable.setWidth(2,twoWidth);
          totalTable.setWidth(3,threeWidth);
          totalTable.setWidth(4,fourWidth);
          totalTable.setWidth(5,fiveWidth);
          totalTable.setColumnAlignment(1,"left");
          totalTable.setColumnAlignment(3,"center");
          totalTable.setColumnAlignment(4,"center");
          totalTable.setColumnAlignment(5,"center");


          bookedText = (Text) theSmallBoldText.clone();
            bookedText.setText(Integer.toString(totalBookings));
          attTextBox = (TextInput) textBoxToClone.clone();
            attTextBox.setSize(3);
            attTextBox.setContent(Integer.toString(totalAttendance));
          amountText = (Text) theSmallBoldText.clone();
            amountText.setText(Integer.toString((int) totalAmount));

          nameText.setFontColor(super.BLACK);
          bookedText.setFontColor(super.BLACK);
          amountText.setFontColor(super.BLACK);

          totalHText.setFontColor(super.BLACK);

          totalTable.add(totalHText,1,1);
          totalTable.add(bookedText,3,1);
          totalTable.add(attTextBox,4,1);
          totalTable.add(amountText,5,1);
          //totalTable.setRowColor(1,super.backgroundColor);

          int tRow = 1;
          int many;

          totalTable.setRowColor(tRow, theColor);

          if (closerLook)
          for (int i = 0; i < prices.length; i++) {
            try {
              ++tRow;
              totalTable.setRowColor(tRow, theColor);
              many = ((Integer) map.get(prices[i].getPriceCategoryIDInteger())).intValue();
              nameText = (Text) smallText.clone();
                nameText.setText(Text.NON_BREAKING_SPACE + Text.NON_BREAKING_SPACE+prices[i].getPriceCategory().getName());
              bookedText = (Text) smallText.clone();
                bookedText.setText(Integer.toString(many));
              amountText = (Text) smallText.clone();
                amountText.setText(Integer.toString(many * ((int) tsb.getPrice(service.getID(), prices[i].getPriceCategoryID(), prices[i].getCurrencyId(), idegaTimestamp.getTimestampRightNow()))));

              nameText.setFontColor(super.BLACK);
              bookedText.setFontColor(super.BLACK);
              amountText.setFontColor(super.BLACK);

              totalTable.setAlignment(2,tRow,"left");
              totalTable.add(nameText,2,tRow);
              totalTable.add(bookedText,3,tRow);
              totalTable.add(amountText,4,tRow);
            }catch (SQLException sql) {
              sql.printStackTrace(System.err);
            }
          }
          totalTable.setColumnAlignment(1,"left");
          totalTable.setColumnAlignment(3,"center");
          totalTable.setColumnAlignment(4,"center");
          totalTable.setColumnAlignment(5,"center");


      Link link = new Link(iwrb.getImage("buttons/add.gif"));
        link.setFontColor(super.textColor);
        link.addParameter(AdditionalBooking.parameterServiceId,service.getID());
        link.addParameter(AdditionalBooking.parameterDate, stamp.toSQLDateString());
        link.setWindowToOpen(AdditionalBooking.class);

      Link correctionLink = (Link) link.clone();
        correctionLink.addParameter(AdditionalBooking.correction,"true");

      theTable.add(table);
      theTable.add(additionHText,1,2);
      theTable.add(link,1,4);
      theTable.setAlignment(1,2,"left");
      theTable.add(addTable,1,3);
      theTable.add(correctionHText,1,5);
      theTable.add(correctionLink,1,7);
      theTable.add(correctionTable,1,6);
      theTable.add(totalTable,1,8);

      SubmitButton submit = new SubmitButton(iwrb.getImage("buttons/save.gif"),this.sAction, this.parameterUpdate);

      SubmitButton open = null;
      if (this.closerLook) {
        open = new SubmitButton(iwrb.getImage("buttons/close.gif"),this.sAction, this.parameterNo);
      }else {
        open = new SubmitButton(iwrb.getImage("buttons/closer.gif"),this.sAction, this.parameterYes);
      }

      theTable.setAlignment(1,8,"right");
      theTable.add(open,1,8);
      theTable.add(submit,1,8);


      return theTable;

  }


  private void update(IWContext iwc) {
    String[] booking_ids = (String[]) iwc.getParameterValues("booking_id");
    String[] attendance  = (String[]) iwc.getParameterValues("attendance");
    String[] pay_type    = (String[]) iwc.getParameterValues("payment_type");

    Booking booking;
    if (booking_ids != null)
    for (int i = 0; i < booking_ids.length; i++) {
      try {
        booking = new GeneralBooking(Integer.parseInt(booking_ids[i]));
        try {
          booking.setAttendance(Integer.parseInt(attendance[i]));
        }catch (NumberFormatException n) {
          booking.setAttendance(0);
        }
        try {
          booking.setPaymentTypeId(Integer.parseInt(pay_type[i]));
        }catch (NumberFormatException n) {}
        booking.update();
      }catch (SQLException sql) {
        sql.printStackTrace(System.err);
      }
    }


  }

}
