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

import is.idega.travel.business.TravelStockroomBusiness;
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


  public DailyReport() {
  }

  public void add(ModuleObject mo) {
    super.add(mo);
  }

  public void main(ModuleInfo modinfo) throws SQLException {
      super.main(modinfo);
      initialize(modinfo);

      String action = modinfo.getParameter(sAction);
      if (action == null) {action = "";}

      if (action.equals(this.parameterUpdate)) {
          update(modinfo);
      }
      displayForm(modinfo);

      super.addBreak();
  }

  public void initialize(ModuleInfo modinfo) {
      bundle = super.getBundle();
      iwrb = super.getResourceBundle();

      supplier = super.getSupplier();

      String productId = modinfo.getParameter(Product.getProductEntityName());
      try {
        if (productId == null) {
          productId = (String) modinfo.getSessionAttribute("TB_BOOKING_PRODUCT_ID");
        }else {
          modinfo.setSessionAttribute("TB_BOOKING_PRODUCT_ID",productId);
        }
        if (productId != null) {
          product = new Product(Integer.parseInt(productId));
          service = tsb.getService(product);
          tour = tsb.getTour(product);
          timeframe = tsb.getTimeframe(product);
        }
      }catch (TravelStockroomBusiness.ServiceNotFoundException snfe) {
          snfe.printStackTrace(System.err);
      }catch (TravelStockroomBusiness.TimeframeNotFoundException tfnfe) {
          tfnfe.printStackTrace(System.err);
      }catch (TravelStockroomBusiness.TourNotFoundException tnfe) {
          tnfe.printStackTrace(System.err);
      }catch (SQLException sql) {sql.printStackTrace(System.err);}

      stamp = getFromIdegaTimestamp(modinfo);
  }

  public void displayForm(ModuleInfo modinfo) {

      Form form = new Form();
      Table topTable = getTopTable(modinfo);
        form.add(topTable);
      ShadowBox sb = new ShadowBox();
            sb.setWidth("90%");
        form.add(sb);
        if (product != null) {
            sb.setAlignment("center");
            sb.add(getContentHeader(modinfo));
          Table table = getContentTable(modinfo);
            sb.add(table);

          Paragraph par = new Paragraph();
            par.setAlign("right");
            par.add(new PrintButton("TEMP-PRENTA"));
            sb.add(par);

        }
        else {
          sb.add("T ekkert product valið");
        }

      int row = 0;
      add(Text.getBreak());
      add(form);
  }


  // BUSINESS
  public idegaTimestamp getFromIdegaTimestamp(ModuleInfo modinfo) {
      idegaTimestamp stamp = null;
      String from_time = modinfo.getParameter("active_from");
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


  public Table getTopTable(ModuleInfo modinfo) {
      Table topTable = new Table(4,3);
        topTable.setBorder(0);
        topTable.setWidth("90%");



      Text tframeText = (Text) theText.clone();
          tframeText.setText(iwrb.getLocalizedString("travel.timeframe_only","Timeframe"));
          tframeText.addToText(":");


      DropdownMenu trip = null;
      try {
        trip = new DropdownMenu(tsb.getProductsForDrowdown(supplier.getID()));
      }catch (SQLException sql) {
        sql.printStackTrace(System.err);
        trip = new DropdownMenu(Product.getProductEntityName());
      }

          if (product != null) {
              trip.setSelectedElement(Integer.toString(product.getID()));
          }



      DateInput active_from = new DateInput("active_from");
          idegaTimestamp fromStamp = getFromIdegaTimestamp(modinfo);
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

  public Table getContentHeader(ModuleInfo modinfo) {
      Table table = new Table(2,2);
      table.setWidth("95%");



      String mode = modinfo.getParameter("mode");
      if (mode== null) mode="";


      Text headerText = (Text) theBoldText.clone();
          headerText.setFontColor(NatBusiness.textColor);
          headerText.setText(iwrb.getLocalizedString("travel.daily_report","Daily report"));
          headerText.addToText(" : ");

      Text timeText = (Text) theBoldText.clone();
          timeText.setText(stamp.getLocaleDate(modinfo));
          timeText.setFontColor(NatBusiness.textColor);
      Text nameText = (Text) theBoldText.clone();
          nameText.setText(product.getName());


      table.setColumnAlignment(1,"left");
      table.add(headerText,1,1);
      table.add(nameText,1,1);
      table.setAlignment(2,1,"right");
      table.add(timeText,2,1);


      return table;
  }

  public Table getContentTable(ModuleInfo modinfo) {

      int totalBookings = 0;
      int totalAttendance = 0;
      int totalAmount = 0;

      Table theTable = new Table();
          theTable.setBorder(0);
          theTable.setWidth("95%");

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

      Text totalHText = (Text) theSmallBoldText.clone();
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

      table.setWidth(2,twoWidth);
      table.setWidth(3,threeWidth);
      table.setWidth(4,fourWidth);
      table.setWidth(5,fiveWidth);

      table.setBorderColor(NatBusiness.textColor);

      int attendance;
      int ibookings;
      float amount;

      int[] bookingTypeIds = {is.idega.travel.data.Booking.BOOKING_TYPE_ID_INQUERY_BOOKING, is.idega.travel.data.Booking.BOOKING_TYPE_ID_ONLINE_BOOKING , is.idega.travel.data.Booking.BOOKING_TYPE_ID_SUPPLIER_BOOKING , is.idega.travel.data.Booking.BOOKING_TYPE_ID_THIRD_PARTY_BOOKING };


      is.idega.travel.data.Booking[] bookings = tsb.getBookings(product.getID(),stamp,bookingTypeIds);
      for (int i = 0; i < bookings.length; i++) {
          row++;
          attendance = 0;
          ibookings = 0;
          amount = 0;

          ibookings = bookings[i].getTotalCount();
          attendance = bookings[i].getAttendance();
          amount = tsb.getBookingPrice(bookings[i]);

          totalBookings += ibookings;
          totalAttendance += attendance;
          totalAmount += amount;

          table.setRowColor(row,NatBusiness.backgroundColor);
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

      }

      table.setColumnAlignment(1,"left");
      table.setColumnAlignment(2,"center");
      table.setColumnAlignment(3,"center");
      table.setColumnAlignment(4,"center");
      table.setColumnAlignment(5,"center");



      Table addTable = new Table();
        int addRow = 0;
          addTable.setWidth("100%");
          addTable.setBorder(1);
          addTable.setCellspacing(0);
          addTable.setBorderColor(NatBusiness.textColor);
          addTable.setWidth(2,twoWidth);
          addTable.setWidth(3,threeWidth);
          addTable.setWidth(4,fourWidth);
          addTable.setWidth(5,fiveWidth);

      bookings = tsb.getBookings(product.getID(),stamp,is.idega.travel.data.Booking.BOOKING_TYPE_ID_ADDITIONAL_BOOKING);
      for (int i = 0; i < bookings.length; i++) {
          ++addRow;
          ibookings = bookings[i].getTotalCount();
          attendance = bookings[i].getAttendance();
          amount = tsb.getBookingPrice(bookings[i]);

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
      }
      addTable.setColumnAlignment(1,"left");
      addTable.setColumnAlignment(2,"center");
      addTable.setColumnAlignment(3,"center");
      addTable.setColumnAlignment(4,"center");
      addTable.setColumnAlignment(5,"center");



      Table totalTable = new Table();
          totalTable.setWidth("100%");
          totalTable.setBorder(1);
          totalTable.setCellspacing(0);
          totalTable.setBorderColor(NatBusiness.textColor);
          totalTable.setWidth(2,twoWidth);
          totalTable.setWidth(3,threeWidth);
          totalTable.setWidth(4,fourWidth);
          totalTable.setWidth(5,fiveWidth);
          totalTable.setColumnAlignment(1,"left");
          totalTable.setColumnAlignment(2,"center");
          totalTable.setColumnAlignment(3,"center");
          totalTable.setColumnAlignment(4,"center");
          totalTable.setColumnAlignment(5,"center");


          bookedText = (Text) smallText.clone();
            bookedText.setText(Integer.toString(totalBookings));
          attTextBox = (TextInput) textBoxToClone.clone();
            attTextBox.setSize(3);
            attTextBox.setContent(Integer.toString(totalAttendance));
          amountText = (Text) smallText.clone();
            amountText.setText(Integer.toString((int) totalAmount));

          totalTable.add(totalHText,1,1);
          totalTable.add(bookedText,3,1);
          totalTable.add(attTextBox,4,1);
          totalTable.add(amountText,5,1);


      Link link = new Link();
        link.setText(" t - add new");
        link.addParameter(AdditionalBooking.parameterServiceId,service.getID());
        link.addParameter(AdditionalBooking.parameterDate, stamp.toSQLDateString());
        link.setWindowToOpen(AdditionalBooking.class);

      theTable.add(table);
      theTable.add(additionHText,1,2);
      theTable.add(link,1,2);
      theTable.setAlignment(1,2,"left");
      theTable.add(addTable,1,3);
      theTable.add(totalTable,1,5);

      SubmitButton submit = new SubmitButton("T update",this.sAction, this.parameterUpdate);

      theTable.setAlignment(1,6,"right");
      theTable.add(submit,1,6);

      return theTable;

  }

  private void update(ModuleInfo modinfo) {
    String[] booking_ids = (String[]) modinfo.getParameterValues("booking_id");
    String[] attendance  = (String[]) modinfo.getParameterValues("attendance");

    is.idega.travel.data.Booking booking;
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