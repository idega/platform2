package is.idega.idegaweb.travel.presentation;

import javax.ejb.FinderException;
import com.idega.business.IBOLookup;
import com.idega.idegaweb.*;
import com.idega.idegaweb.presentation.SmallCalendar;

import java.rmi.RemoteException;
import com.idega.presentation.Block;
import com.idega.presentation.text.*;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.block.trade.stockroom.data.*;
import com.idega.util.IWTimestamp;
import com.idega.util.IWCalendar;
import com.idega.core.accesscontrol.business.AccessControl;
import com.idega.data.*;
import java.sql.SQLException;
import is.idega.idegaweb.travel.business.*;
import is.idega.idegaweb.travel.data.*;
import com.idega.block.trade.stockroom.business.*;
import is.idega.idegaweb.travel.service.tour.data.*;
import is.idega.idegaweb.travel.service.tour.business.*;
import is.idega.idegaweb.travel.service.tour.presentation.*;
import is.idega.idegaweb.travel.interfaces.Booking;
import is.idega.idegaweb.travel.business.Booker;


/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class Statistics extends TravelManager {

  private IWBundle bundle;
  private IWResourceBundle iwrb;

  private Supplier supplier;
  private Product product;
  private TravelStockroomBusiness tsb;
  private Service service;
  private Timeframe timeframe;

  private IWTimestamp fromStamp;
  private IWTimestamp toStamp;

  public Statistics() {
  }

  public void add(PresentationObject mo) {
    super.add(mo);
  }


  public void main(IWContext iwc) throws Exception {
      super.main(iwc);
      initialize(iwc);

      if (super.isLoggedOn(iwc) ) {
          String action = iwc.getParameter("action");
          if (action == null) {action = "";}

          if (action.equals("")) {
              displayForm(iwc);
          }
        super.addBreak();
      }else {
        add(super.getLoggedOffTable(iwc));
      }
  }

  public void initialize(IWContext iwc) throws RemoteException{
      bundle = super.getBundle();
      iwrb = super.getResourceBundle();
      supplier = super.getSupplier();
      tsb = getTravelStockroomBusiness(iwc);
      String productId = iwc.getParameter(com.idega.block.trade.stockroom.data.ProductBMPBean.getProductEntityName());
      try {
        if (productId == null) {
          productId = (String) iwc.getSessionAttribute("TB_BOOKING_PRODUCT_ID");
        }else {
          iwc.setSessionAttribute("TB_BOOKING_PRODUCT_ID",productId);
        }
        if (productId != null && !productId.equals("-1")) {
          product = getProductBusiness(iwc).getProduct(Integer.parseInt(productId));
          service = tsb.getService(product);
          timeframe = tsb.getTimeframe(product);
        }
      }catch (ServiceNotFoundException snfe) {
          snfe.printStackTrace(System.err);
      }catch (TimeframeNotFoundException tfnfe) {
          tfnfe.printStackTrace(System.err);
      }catch (FinderException sql) {sql.printStackTrace(System.err);}

      fromStamp = getFromIdegaTimestamp(iwc);
      toStamp = getToIdegaTimestamp(iwc);

  }

  public void displayForm(IWContext iwc) throws RemoteException {

      Form form = new Form();
      Table topTable = getTopTable(iwc);
        form.add(topTable);
      if (service != null) {
          form.add(Text.BREAK);
          form.add(Text.BREAK);
          Table table = getContentTable(iwc);
          form.add(table);
          form.add(Text.BREAK);
          Table par = new Table();
            par.setAlignment(1,1,"right");
            par.setAlignment("center");
            par.setWidth("90%");
            par.add(new PrintButton(iwrb.getImage("buttons/print.gif")));
          form.add(par);
            //sb.add(par);
      }

      int row = 0;
      add(Text.getBreak());
      add(form);
  }


  // BUSINESS
  public IWTimestamp getFromIdegaTimestamp(IWContext iwc) {
      IWTimestamp stamp = null;
      String from_time = iwc.getParameter("active_from");
      if (from_time!= null) {
          try {
              stamp = new IWTimestamp(from_time);
          }
          catch (RuntimeException e) {
              stamp = IWTimestamp.RightNow();
          }
      }
      else {
          stamp = IWTimestamp.RightNow();
      }
      return stamp;
  }

  // BUSINESS
  public IWTimestamp getToIdegaTimestamp(IWContext iwc) {
      IWTimestamp stamp = null;
      String from_time = iwc.getParameter("active_to");
      if (from_time!= null) {
          try {
              stamp = new IWTimestamp(from_time);
          }
          catch (RuntimeException e) {
              stamp = IWTimestamp.RightNow();
              stamp.addDays(15);
          }
      }
      else {
          stamp = IWTimestamp.RightNow();
          stamp.addDays(15);
      }
      return stamp;
  }


  public Table getTopTable(IWContext iwc) throws RemoteException{
      Table topTable = new Table(5,2);
        topTable.setBorder(0);
        topTable.setWidth("90%");



      Text tframeText = (Text) theText.clone();
          tframeText.setText(iwrb.getLocalizedString("travel.timeframe_only","Timeframe"));
          tframeText.addToText(":");


      DropdownMenu trip = null;
        trip = getProductBusiness(iwc).getDropdownMenuWithProducts(iwc, supplier.getID());
//        trip = new DropdownMenu(tsb.getProducts(supplier.getID()));

      if (product != null) {
          trip.setSelectedElement(Integer.toString(product.getID()));
      }

			IWTimestamp now = IWTimestamp.RightNow();

      DateInput active_from = new DateInput("active_from");
	    active_from.setDate(fromStamp.getSQLDate());
			active_from.setYearRange(2001, now.getYear()+4);
	          
      DateInput active_to = new DateInput("active_to");
	    active_to.setDate(toStamp.getSQLDate());
			active_to.setYearRange(2001, now.getYear()+4);

      Text tfFromText = (Text) theText.clone();
          tfFromText.setText(iwrb.getLocalizedString("travel.from","from"));
      Text tfToText = (Text) theText.clone();
          tfToText.setText(iwrb.getLocalizedString("travel.to","to"));


      Text nameText = (Text) theText.clone();
          nameText.setText(iwrb.getLocalizedString("travel.product_name_lg","Name of product"));
          nameText.addToText(":");
      Text timeframeText = (Text) theText.clone();
          timeframeText.setText(iwrb.getLocalizedString("travel.timeframe_only","Timeframe"));
          timeframeText.addToText(":");



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

      topTable.setAlignment(5,2,"right");
      topTable.add(new SubmitButton(iwrb.getImage("buttons/get.gif")),5,2);

      return topTable;
  }

  public Table getContentHeader(IWContext iwc) throws RemoteException{
      Table table = new Table(2,3);
      table.setWidth("90%");


      String mode = iwc.getParameter("mode");
      if (mode== null) mode="";


      Text headerText = (Text) theBoldText.clone();
          headerText.setFontColor(super.textColor);
          headerText.setText(iwrb.getLocalizedString("travel.name_of_product_lg","Name of product"));
          headerText.addToText(" : ");

      Text timeframeText = (Text) theBoldText.clone();
          timeframeText.setFontColor(super.textColor);
          timeframeText.setText(iwrb.getLocalizedString("travel.timeframe","Timeframe"));
          timeframeText.addToText(" : ");

      Text timeText = (Text) theText.clone();
          timeText.setText(fromStamp.getLocaleDate(iwc)+" - "+toStamp.getLocaleDate(iwc));
      Text toTimeText = (Text) theText.clone();
          toTimeText.setText(toStamp.getLocaleDate(iwc));
      Text nameText = (Text) theText.clone();
          nameText.setText(service.getName(super.getTravelSessionManager(iwc).getLocaleId()));
      Text statusText = (Text) theBoldText.clone();
          statusText.setFontColor(super.textColor);
          statusText.setText(iwrb.getLocalizedString("travel.status","Status"));
          statusText.addToText(" : ");


      table.setColumnAlignment(1,"left");
      table.add(headerText,1,1);
      table.add(nameText,1,1);
      table.setAlignment(2,1,"right");
      table.add(timeframeText,2,1);
      table.add(timeText,2,1);
      table.add(statusText,1,2);
      table.add(toTimeText,1,2);


      return table;
  }

  public Table getContentTable(IWContext iwc) throws RemoteException{
      Table table = new Table();
        table.setWidth("90%");
        table.setBorder(0);
        table.setCellspacing(1);
        table.setCellpadding(2);
        table.setColor(super.WHITE);

      int row = 0;

      Text smallText = (Text) super.smallText.clone();
        smallText.setFontColor(super.backgroundColor);

      Text netBookText = (Text) smallText.clone();
          netBookText.setText(iwrb.getLocalizedString("travel.bookings_on_the_net","Bookings on the net"));
          netBookText.setFontColor(super.BLACK);

      Text inqText = (Text) smallText.clone();
          inqText.setText(iwrb.getLocalizedString("travel.bookings_from_inqueries","Bookings from inqueries"));
          inqText.setFontColor(super.BLACK);

      Text supplText = (Text) smallText.clone();
          supplText.setText(iwrb.getLocalizedString("travel.bookings_from_supplier","Booked by supplier"));
          supplText.setFontColor(super.BLACK);

      Text travelText = (Text) smallText.clone();
          travelText.setText(iwrb.getLocalizedString("travel.bookings_from_travel_agencies","Booked by travel agencies"));
          travelText.setFontColor(super.BLACK);

      Text availText = (Text) smallText.clone();
          availText.setText(iwrb.getLocalizedString("travel.available","Available"));
          availText.setFontColor(super.BLACK);

      Text passText = (Text) smallText.clone();
          passText.setText(iwrb.getLocalizedString("travel.number_of_passengers","Number of passengers"));
          passText.setFontColor(super.BLACK);

      Text seatText = (Text) smallText.clone();
          seatText.setText(iwrb.getLocalizedString("travel.number_of_seats","Number of seats"));
          seatText.setFontColor(super.BLACK);

      Text usageText = (Text) smallText.clone();
          usageText.setText(iwrb.getLocalizedString("travel.seat_usage","Seat usage"));
          usageText.setFontColor(super.BLACK);


      Text netBookNrText = (Text) smallText.clone();
      Text inqNrText = (Text) smallText.clone();
      Text supplNrText = (Text) smallText.clone();
      Text travelNrText = (Text) smallText.clone();
      Text availNrText = (Text) smallText.clone();
      Text passNrText = (Text) smallText.clone();
      Text seatNrText = (Text) smallText.clone();
      Text usageNrText = (Text) smallText.clone();
          netBookNrText.setFontColor(super.BLACK);
          inqNrText.setFontColor(super.BLACK);
          supplNrText.setFontColor(super.BLACK);
          travelNrText.setFontColor(super.BLACK);
          availNrText.setFontColor(super.BLACK);
          passNrText.setFontColor(super.BLACK);
          seatNrText.setFontColor(super.BLACK);
          usageNrText.setFontColor(super.BLACK);

      int iNetBooking = getBooker(iwc).getBookingsTotalCount(service.getID() ,fromStamp, toStamp, Booking.BOOKING_TYPE_ID_ONLINE_BOOKING, null);
      int iInqBooking = getBooker(iwc).getBookingsTotalCount(service.getID() ,fromStamp, toStamp, Booking.BOOKING_TYPE_ID_INQUERY_BOOKING, null);
      int iSupBooking = getBooker(iwc).getBookingsTotalCount(service.getID() ,fromStamp, toStamp, Booking.BOOKING_TYPE_ID_SUPPLIER_BOOKING, null);
      int i3rdBooking = getBooker(iwc).getBookingsTotalCount(service.getID() ,fromStamp, toStamp, Booking.BOOKING_TYPE_ID_THIRD_PARTY_BOOKING, null);


      int total = iNetBooking + iInqBooking + iSupBooking + i3rdBooking;

      /**
       * @todo smiða fall sem skila number of seats for - to
       */
      int numberOfSeats = 0;
      //      int numberOfSeats = tour.getTotalSeats() * getTourBusiness(iwc).getNumberOfTours(service.getID(), fromStamp, toStamp);

      int iAvail = numberOfSeats - total;

      float usage = 100 * (float)total / (float)numberOfSeats ;
      java.text.DecimalFormat format = new java.text.DecimalFormat("0.00");
      String sUsage = format.format(usage);

          netBookNrText.setText(Integer.toString(iNetBooking));
          inqNrText.setText(Integer.toString(iInqBooking));
          supplNrText.setText(Integer.toString(iSupBooking));
          travelNrText.setText(Integer.toString(i3rdBooking));
          passNrText.setText(Integer.toString(total));
          if (numberOfSeats > 0) {
            availNrText.setText(Integer.toString(iAvail));
            seatNrText.setText(Integer.toString(numberOfSeats));
            usageNrText.setText(sUsage+"%");
          }else {
            availNrText.setText("-");
            seatNrText.setText("-");
            usageNrText.setText("-");
          }

      table.add(netBookText,1,1);
      table.add(inqText,1,2);
      table.add(supplText,1,3);
      table.add(travelText,1,4);
      table.add(availText,1,5);
      table.add(passText,1,6);
      table.add(seatText,1,7);
      table.add(usageText,1,8);

      table.add(netBookNrText,2,1);
      table.add(inqNrText,2,2);
      table.add(supplNrText,2,3);
      table.add(travelNrText,2,4);
      table.add(availNrText,2,5);
      table.add(passNrText,2,6);
      table.add(seatNrText,2,7);
      table.add(usageNrText,2,8);



      table.setColumnAlignment(1,"left");
      table.setColumnAlignment(2,"center");
      table.setWidth(2,"100");

      /*table.setBorderColor(super.backgroundColor);
      table.setRowColor(1,super.LIGHTBLUE);
      table.setRowColor(2,super.YELLOW);
      table.setRowColor(3,super.ORANGE);
      table.setRowColor(4,super.BLUE);
      table.setRowColor(5,super.GREEN);
      table.setRowColor(6,super.LIGHTORANGE);
      table.setRowColor(7,super.RED);
      table.setRowColor(8,super.LIGHTGREEN);
      */
      table.setRowColor(1,super.GRAY);
      table.setRowColor(2,super.GRAY);
      table.setRowColor(3,super.GRAY);
      table.setRowColor(4,super.GRAY);
      table.setRowColor(5,super.GRAY);
      table.setRowColor(6,super.GRAY);
      table.setRowColor(7,super.GRAY);
      table.setRowColor(8,super.GRAY);


      return table;

  }

  private TourBusiness getTourBusiness(IWApplicationContext iwac) throws RemoteException {
    return (TourBusiness) IBOLookup.getServiceInstance(iwac, TourBusiness.class);
  }

}
