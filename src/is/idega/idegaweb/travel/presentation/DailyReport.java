package is.idega.travel.presentation;

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

import is.idega.travel.business.Booker;
import is.idega.travel.business.TravelStockroomBusiness;
import is.idega.travel.business.TourBusiness;
import is.idega.travel.data.*;
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

          Paragraph par = new Paragraph();
            par.setAlign("right");
            par.add(new PrintButton("TEMP-PRENTA"));
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
      Table topTable = new Table(4,3);
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

  public Table getContentHeader(IWContext iwc) {
      Table table = new Table(2,2);
      table.setWidth("95%");



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
          theTable.setWidth("95%");
          if (closerLook) {
            theTable.add(new HiddenInput(this.parameterToggleCloser , this.parameterYes));
          }else {
            theTable.add(new HiddenInput(this.parameterToggleCloser , this.parameterNo));
          }

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

      int[] bookingTypeIds = {is.idega.travel.data.Booking.BOOKING_TYPE_ID_INQUERY_BOOKING, is.idega.travel.data.Booking.BOOKING_TYPE_ID_ONLINE_BOOKING , is.idega.travel.data.Booking.BOOKING_TYPE_ID_SUPPLIER_BOOKING , is.idega.travel.data.Booking.BOOKING_TYPE_ID_THIRD_PARTY_BOOKING };
      ProductPrice[] prices = ProductPrice.getProductPrices(service.getID(), false);
      ProductPrice price;
      Integer entryCount;
      int iEntryCount;

      Map map = new Hashtable();
      for (int i = 0; i < prices.length; i++) {
        map.put(prices[i].getPriceCategoryIDInteger(),new Integer(0));
      }


      is.idega.travel.data.Booking[] bookings = Booker.getBookings(product.getID(),stamp,bookingTypeIds);

      BookingEntry[] entries;
      for (int i = 0; i < bookings.length; i++) {
          row++;
          attendance = 0;
          ibookings = 0;
          amount = 0;

          ibookings = bookings[i].getTotalCount();
          attendance = bookings[i].getAttendance();
          amount = Booker.getBookingPrice(bookings[i]);

          totalBookings += ibookings;
          totalAttendance += attendance;
          totalAmount += amount;

          table.setRowColor(row,super.backgroundColor);
          nameText = (Text) smallText.clone();
            nameText.setText(bookings[i].getName());

          bookedText = (Text) smallText.clone();
            bookedText.setText(Integer.toString(ibookings));

          attTextBox = (TextInput) textBoxToClone.clone();
            attTextBox.setSize(3);
          if (attendance != 0) {
            attTextBox.setContent(Integer.toString(attendance));
          }
          amountText = (Text) smallText.clone();
            amountText.setText(Integer.toString((int) amount));

          table.add(new HiddenInput("booking_id",Integer.toString(bookings[i].getID())),1,row);
          table.add(nameText,1,row);

          table.add(bookedText,3,row);
          table.add(attTextBox,4,row);
          table.add(amountText,5,row);
          table.setAlignment(2,row, "center");

          if (closerLook)
          try {
            entries = bookings[i].getBookingEntries();
            for (int j = 0; j < entries.length; j++) {
              ++row;
              price = entries[j].getProductPrice();
              iEntryCount = (int) Booker.getBookingEntryPrice(entries[j], bookings[i]);

              nameText = (Text) smallText.clone();
                nameText.setText(Text.NON_BREAKING_SPACE + Text.NON_BREAKING_SPACE + price.getPriceCategory().getName());
              bookedText = (Text) smallText.clone();
                bookedText.setText(Integer.toString(entries[j].getCount()));
              amountText = (Text) smallText.clone();
                amountText.setText(Integer.toString(iEntryCount));

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



      Table addTable = new Table();
        int addRow = 0;
          addTable.setWidth("100%");
          addTable.setBorder(1);
          addTable.setCellspacing(0);
          addTable.setBorderColor(super.textColor);
          addTable.setWidth(2,twoWidth);
          addTable.setWidth(3,threeWidth);
          addTable.setWidth(4,fourWidth);
          addTable.setWidth(5,fiveWidth);

      bookings = Booker.getBookings(product.getID(),stamp,is.idega.travel.data.Booking.BOOKING_TYPE_ID_ADDITIONAL_BOOKING);
      for (int i = 0; i < bookings.length; i++) {
          ++addRow;
          addTable.setRowColor(addRow,super.backgroundColor);
          addTable.setAlignment(2,addRow,"center");
          ibookings = bookings[i].getTotalCount();
          attendance = bookings[i].getAttendance();
          amount = Booker.getBookingPrice(bookings[i]);

          totalBookings += ibookings;
          totalAttendance += attendance;
          totalAmount += amount;

          nameText = (Text) smallText.clone();
            nameText.setText(bookings[i].getName());

          bookedText = (Text) smallText.clone();
            bookedText.setText(Integer.toString(ibookings));

          attTextBox = (TextInput) textBoxToClone.clone();
            attTextBox.setSize(3);
          if (attendance != 0) {
            attTextBox.setContent(Integer.toString(attendance));
          }
          amountText = (Text) smallText.clone();
            amountText.setText(Integer.toString((int) amount));

          addTable.add(new HiddenInput("booking_id",Integer.toString(bookings[i].getID())),1,addRow);
          addTable.add(nameText,1,addRow);

          addTable.add(bookedText,3,addRow);
          addTable.add(attTextBox,4,addRow);
          addTable.add(amountText,5,addRow);

          if (closerLook)
          try {
            entries = bookings[i].getBookingEntries();
            for (int j = 0; j < entries.length; j++) {
              ++addRow;
              price = entries[j].getProductPrice();
              iEntryCount = (int) Booker.getBookingEntryPrice(entries[j], bookings[i]);

              nameText = (Text) smallText.clone();
                nameText.setText(Text.NON_BREAKING_SPACE + Text.NON_BREAKING_SPACE + price.getPriceCategory().getName());
              bookedText = (Text) smallText.clone();
                bookedText.setText(Integer.toString(entries[j].getCount()));
              amountText = (Text) smallText.clone();
                amountText.setText(Integer.toString(iEntryCount));

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
      Table correctionTable = new Table();
        int corrRow = 0;
          correctionTable.setWidth("100%");
          correctionTable.setBorder(1);
          correctionTable.setCellspacing(0);
          correctionTable.setBorderColor(super.textColor);
          correctionTable.setWidth(2,twoWidth);
          correctionTable.setWidth(3,threeWidth);
          correctionTable.setWidth(4,fourWidth);
          correctionTable.setWidth(5,fiveWidth);

      bookings = Booker.getBookings(product.getID(),stamp,is.idega.travel.data.Booking.BOOKING_TYPE_ID_CORRECTION);
      for (int i = 0; i < bookings.length; i++) {
          ++corrRow;
          correctionTable.setRowColor(corrRow,super.backgroundColor);
          correctionTable.setAlignment(2,corrRow,"center");
          ibookings = bookings[i].getTotalCount();
          attendance = bookings[i].getAttendance();
          amount = Booker.getBookingPrice(bookings[i]);

          totalBookings += ibookings;
          totalAttendance += attendance;
          totalAmount += amount;

          nameText = (Text) smallText.clone();
            nameText.setText(bookings[i].getName());

          bookedText = (Text) smallText.clone();
            bookedText.setText(Integer.toString(ibookings));

          attTextBox = (TextInput) textBoxToClone.clone();
            attTextBox.setSize(3);
          if (attendance != 0) {
            attTextBox.setContent(Integer.toString(attendance));
          }
          amountText = (Text) smallText.clone();
            amountText.setText(Integer.toString((int) amount));

          correctionTable.add(new HiddenInput("booking_id",Integer.toString(bookings[i].getID())),1,corrRow);
          correctionTable.add(nameText,1,corrRow);

          correctionTable.add(bookedText,3,corrRow);
          correctionTable.add(attTextBox,4,corrRow);
          correctionTable.add(amountText,5,corrRow);

          if (closerLook)
          try {
            entries = bookings[i].getBookingEntries();
            for (int j = 0; j < entries.length; j++) {
              ++corrRow;
              price = entries[j].getProductPrice();
              iEntryCount = (int) Booker.getBookingEntryPrice(entries[j], bookings[i]);

              nameText = (Text) smallText.clone();
                nameText.setText(Text.NON_BREAKING_SPACE + Text.NON_BREAKING_SPACE + price.getPriceCategory().getName());
              bookedText = (Text) smallText.clone();
                bookedText.setText(Integer.toString(entries[j].getCount()));
              amountText = (Text) smallText.clone();
                amountText.setText(Integer.toString(iEntryCount));

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


      Table totalTable = new Table();
          totalTable.setWidth("100%");
          totalTable.setBorder(1);
          totalTable.setCellspacing(0);
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

          totalTable.add(totalHText,1,1);
          totalTable.add(bookedText,3,1);
          totalTable.add(attTextBox,4,1);
          totalTable.add(amountText,5,1);
          totalTable.setRowColor(1,super.backgroundColor);

          int tRow = 1;
          int many;


          if (closerLook)
          for (int i = 0; i < prices.length; i++) {
            try {
              ++tRow;
              many = ((Integer) map.get(prices[i].getPriceCategoryIDInteger())).intValue();
              nameText = (Text) smallText.clone();
                nameText.setText(Text.NON_BREAKING_SPACE + Text.NON_BREAKING_SPACE+prices[i].getPriceCategory().getName());
              bookedText = (Text) smallText.clone();
                bookedText.setText(Integer.toString(many));
              amountText = (Text) smallText.clone();
                amountText.setText(Integer.toString(many * ((int) tsb.getPrice(service.getID(), prices[i].getPriceCategoryID(), prices[i].getCurrencyId(), idegaTimestamp.getTimestampRightNow()))));

              totalTable.setAlignment(2,tRow,"left");
              totalTable.add(nameText,2,tRow);
              totalTable.add(bookedText,3,tRow);
              totalTable.add(amountText,4,tRow);
//              totalTable.add(prices[i].getPriceCategory().getName() +" , "+ );
            }catch (SQLException sql) {
              sql.printStackTrace(System.err);
            }
          }
          totalTable.setColumnAlignment(1,"left");
          totalTable.setColumnAlignment(3,"center");
          totalTable.setColumnAlignment(4,"center");
          totalTable.setColumnAlignment(5,"center");


      Link link = new Link();
        link.setText(" t - add new");
        link.setFontColor(super.textColor);
        link.addParameter(AdditionalBooking.parameterServiceId,service.getID());
        link.addParameter(AdditionalBooking.parameterDate, stamp.toSQLDateString());
        link.setWindowToOpen(AdditionalBooking.class);

      Link correctionLink = (Link) link.clone();
        correctionLink.addParameter(AdditionalBooking.correction,"true");

      theTable.add(table);
      theTable.add(additionHText,1,2);
      theTable.add(link,1,2);
      theTable.setAlignment(1,2,"left");
      theTable.add(addTable,1,3);
      theTable.add(correctionHText,1,4);
      theTable.add(correctionLink,1,4);
      theTable.add(correctionTable,1,5);
      theTable.add(totalTable,1,7);

      SubmitButton submit = new SubmitButton("T update",this.sAction, this.parameterUpdate);

      SubmitButton open = null;
      if (this.closerLook) {
        open = new SubmitButton("T close extra info",this.sAction, this.parameterNo);
      }else {
        open = new SubmitButton("T extra info",this.sAction, this.parameterYes);
      }

      theTable.setAlignment(1,8,"right");
      theTable.add(open,1,8);
      theTable.add(submit,1,8);


      return theTable;

  }


  private void update(IWContext iwc) {
    String[] booking_ids = (String[]) iwc.getParameterValues("booking_id");
    String[] attendance  = (String[]) iwc.getParameterValues("attendance");

    is.idega.travel.data.Booking booking;
    if (booking_ids != null)
    for (int i = 0; i < booking_ids.length; i++) {
      try {
        booking = new is.idega.travel.data.Booking(Integer.parseInt(booking_ids[i]));
        booking.setAttendance(Integer.parseInt(attendance[i]));
        booking.update();
      }catch (SQLException sql) {
        sql.printStackTrace(System.err);
      }catch (NumberFormatException n) {
      }
    }


  }

}