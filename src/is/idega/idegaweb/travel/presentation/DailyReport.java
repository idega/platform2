package is.idega.idegaweb.travel.presentation;

import com.idega.data.IDORelationshipException;
import javax.ejb.FinderException;
import java.rmi.RemoteException;
import com.idega.presentation.Block;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.SmallCalendar;
import com.idega.presentation.text.*;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.block.trade.stockroom.data.*;
import com.idega.block.trade.stockroom.business.*;
import com.idega.util.text.TextSoap;
import com.idega.util.*;
import com.idega.core.accesscontrol.business.AccessControl;
import java.sql.SQLException;
import java.util.*;

import com.idega.core.location.data.Address;

import is.idega.idegaweb.travel.business.*;
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

public class DailyReport extends TravelManager implements Report{

  private IWBundle bundle;
  private IWResourceBundle iwrb;

  private String sAction = "dailyReportAction";
  private String parameterUpdate = "dailyReportUpdate";
  private String parameterToggleCloser = "dailyReportCloser";
  private String parameterYes = "yes";
  private String parameterNo = "no";
  private String parameterBookingReport ="dailyBookingReport";
  private String parameterBookingReportType ="dailyBookingReportType";
  private String parameterHotelPickupPlaceReport ="dailyHotelPickupPlaceReport";
  private String parameterUserReport ="dailyUserReport";
  private String sessionParameterToggleCloser = "sessDailyToggler";

  private boolean viewAllProducts = false;
  private boolean hotelPickupReport = false;
  private boolean userReport = false;

  private boolean closerLook = false;


  public DailyReport(IWContext iwc) throws Exception {
    super.main(iwc);
    initialize(iwc);
  }

  public boolean useTwoDates() {
    return false;
  }

  public String getReportName() {
    return iwrb.getLocalizedString("travel.daily_report","Daily report");
  }

  public String getReportDescription() {
    return iwrb.getLocalizedString("travel.daily_description","Daily report");
  }

  public void initialize(IWContext iwc) throws RemoteException {
    bundle = super.getBundle();
    iwrb = super.getResourceBundle();
  }

  public Table getBookingTable(IWContext iwc, Product product, IWTimestamp stamp) throws RemoteException, FinderException {
      int totalBookings = 0;
      int totalAttendance = 0;
      int totalAmount = 0;

      Table theTable = new Table();
	  theTable.setBorder(0);
	  theTable.setWidth("100%");

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


      Text nameHText = getHeaderText(iwrb.getLocalizedString("travel.name","Name"));

      Text payTypeHText = getHeaderText(iwrb.getLocalizedString("travel.payment_type","Payment type"));

      Text bookedHText = getHeaderText(iwrb.getLocalizedString("travel.booked_lg","Booked"));

      Text attHText = getHeaderText(iwrb.getLocalizedString("travel.attendance","Attendance"));

      Text amountHText = getHeaderText(iwrb.getLocalizedString("travel.amount","Amount"));

      Text additionHText = getHeaderText(iwrb.getLocalizedString("travel.addition","Addition"));

      Text correctionHText = getHeaderText(iwrb.getLocalizedString("travel.correction","Correction"));

      Text totalHText = (Text) theBoldText.clone();
	  totalHText.setText(iwrb.getLocalizedString("travel.total","Total"));

      TextInput textBoxToClone = new TextInput("attendance");
	  textBoxToClone.setSize(3);
	  textBoxToClone.setMarkupAttribute("style","font-size: 8pt");
      TextInput attTextBox = new TextInput();


      Text nameText = (Text) smallText.clone();
      Text payTypeText = (Text) smallText.clone();
      Text bookedText = (Text) smallText.clone();
      Text attText = (Text) smallText.clone();
      Text amountText = (Text) smallText.clone();
      Text additionText = (Text) smallText.clone();
      Text totalText = (Text) smallText.clone();
      Text addressText = (Text) smallText.clone();

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
      Timeframe tframe;
      List addresses = product.getDepartureAddresses(true);
      TravelAddress address;
      int addressesSize = addresses.size();
      ProductPrice[] prices = {};
      ProductPrice[] misc = {};
//      TravelAddress[] addresses = {};
//      try {
//	addresses = ProductBusiness.getDepartureAddresses(product);
//      }catch (SQLException sql) {
//	sql.printStackTrace(System.err);
//      }




      ProductPrice price;
      Integer entryCount;
      int iEntryCount;

      Map map = new Hashtable();

      Booking[] bookings;
      TravelAddress[] bookingAddresses;
      String theColor = super.GRAY;
      DropdownMenu payType;
      BookingEntry[] entries;
      Collection coll;
      Text travelAddressText;
      int iBookingId = 0;
      table.setRowColor(row,super.backgroundColor);

      for (int ta = 0; ta < addressesSize; ta++) {
        address = (TravelAddress) addresses.get(ta);
        tframe = getProductBusiness(iwc).getTimeframe(product, stamp, address.getID());
        if (tframe != null) {
          prices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(product.getID(), tframe.getID(), address.getID(), false);
          misc = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getMiscellaneousPrices(product.getID(), tframe.getID(),address.getID(), false);
        }

        for (int i = 0; i < prices.length; i++) {
          map.put(prices[i].getPriceCategoryIDInteger()+"_"+address.getID(),new Integer(0));
        }
        for (int i = 0; i < misc.length; i++) {
          map.put(misc[i].getPriceCategoryIDInteger()+"_"+address.getID(),new Integer(0));
        }

        bookings = getBooker(iwc).getBookings(new int[] {product.getID()},stamp,null, bookingTypeIds, address);
        ++row;
        table.setRowColor(row,super.backgroundColor);
        table.mergeCells(1, row, 5, row);
        travelAddressText = super.getText(address.getName());
        travelAddressText.setFontColor(super.WHITE);
        table.add(travelAddressText, 1, row);
        for (int i = 0; i < bookings.length; i++) {
            row++;

            attendance = 0;
            ibookings = 0;
            amount = 0;

            ibookings = bookings[i].getTotalCount();
            attendance = bookings[i].getAttendance();
            amount = getBooker(iwc).getBookingPrice(bookings[i]);

            totalBookings += ibookings;
            if (attendance != -1000)
            totalAttendance += attendance;
            totalAmount += amount;

            table.setRowColor(row,super.backgroundColor);
            nameText = (Text) smallText.clone();
              nameText.setText(bookings[i].getName());

            payTypeText = (Text) smallText.clone();
            payType = (DropdownMenu) getBooker(iwc).getPaymentTypes(iwrb).clone();
              payType.setSelectedElement(Integer.toString(bookings[i].getPaymentTypeId()));
            iBookingId = bookings[i].getPaymentTypeId();

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
              coll = bookings[i].getTravelAddresses();
              bookingAddresses = (TravelAddress[]) coll.toArray(new TravelAddress[]{});
              entries = bookings[i].getBookingEntries();
              //bookingAddresses = (TravelAddress[]) bookings[i].findRelated((TravelAddress)com.idega.block.trade.stockroom.data.TravelAddressBMPBean.getStaticInstance(TravelAddress.class));
/*              if (bookingAddresses.length > 0) {
                addressText = (Text) smallText.clone();
                addressText.setText(bookingAddresses[0].getName()+Text.NON_BREAKING_SPACE+Text.NON_BREAKING_SPACE);
                addressText.setFontColor(super.BLACK);
                table.add(addressText, 1, row+1);
                table.setAlignment(1,row+1, "right");
              }
*/
              for (int j = 0; j < entries.length; j++) {
                ++row;
                table.setRowColor(row, theColor);
                price = entries[j].getProductPrice();
                iEntryCount = (int) getBooker(iwc).getBookingEntryPrice(entries[j], bookings[i]);

                nameText = (Text) smallText.clone();
                  nameText.setText(Text.NON_BREAKING_SPACE + Text.NON_BREAKING_SPACE + price.getPriceCategory().getName());
                bookedText = (Text) smallText.clone();
                  bookedText.setText(Integer.toString(entries[j].getCount()));
                amountText = (Text) smallText.clone();
                  amountText.setText(Integer.toString(iEntryCount));

                nameText.setFontColor(super.BLACK);
                bookedText.setFontColor(super.BLACK);
                amountText.setFontColor(super.BLACK);


                entryCount = (Integer) map.get(price.getPriceCategoryIDInteger()+"_"+bookingAddresses[0].getID());
                if (entryCount == null) {
                  entryCount = new Integer(0);
                }
                entryCount = new Integer(entryCount.intValue() + entries[j].getCount());
                map.put(price.getPriceCategoryIDInteger()+"_"+bookingAddresses[0].getID(),entryCount);

                table.add(nameText,2,row);
                table.add(bookedText,3,row);
                table.add(amountText,4,row);
                table.setAlignment(2,row, "LEFT");
              }


            }catch (FinderException fe) {
              fe.printStackTrace(System.err);
            }catch (IDORelationshipException re) {
              re.printStackTrace(System.err);
            }

        }
      }

//      table.setColumnAlignment(1,"left");
      table.setColumnAlignment(3,"center");
      table.setColumnAlignment(4,"center");
      table.setColumnAlignment(5,"center");

      //---------------------ADDITION----------------------

      theColor = super.GRAY;
      Table addTable = new Table();
	int addRow = 0;
	  addTable.setWidth("100%");
	  addTable.setBorder(0);
	  addTable.setCellspacing(1);
	  addTable.setColor(super.WHITE);
	  addTable.setBorderColor(super.textColor);

//      bookings = getBooker(iwc).getBookings(product.getID(),stamp,Booking.BOOKING_TYPE_ID_ADDITIONAL_BOOKING);
//      if (bookings.length == 0) {
//	addRow++;
//	addTable.setRowColor(addRow,theColor);
//      }
      for (int ta = 0; ta < addressesSize; ta++) {
        address = (TravelAddress) addresses.get(ta);
        bookings = getBooker(iwc).getBookings(new int[] {product.getID()},stamp,null, new int[]{Booking.BOOKING_TYPE_ID_ADDITIONAL_BOOKING}, address);
        ++addRow;
        addTable.setRowColor(addRow,super.backgroundColor);
        addTable.mergeCells(1, addRow, 5, addRow);
        travelAddressText = super.getText(address.getName());
        travelAddressText.setFontColor(super.WHITE);
        addTable.add(travelAddressText, 1, addRow);

        for (int i = 0; i < bookings.length; i++) {
            ++addRow;
            addTable.setRowColor(addRow,super.backgroundColor);
            addTable.setAlignment(2,addRow,"center");
            ibookings = bookings[i].getTotalCount();
            attendance = bookings[i].getAttendance();
            amount = getBooker(iwc).getBookingPrice(bookings[i]);

            totalBookings += ibookings;
            if (attendance != -1000)
            totalAttendance += attendance;
            totalAmount += amount;

            nameText = (Text) smallText.clone();
              nameText.setText(bookings[i].getName());

            payType = (DropdownMenu) getBooker(iwc).getPaymentTypes(iwrb).clone();
              payType.setSelectedElement(Integer.toString(bookings[i].getPaymentTypeId()));

  //          payTypeText = (Text) smallText.clone();
  //            payTypeText.setText(iwrb.getLocalizedString("travel.paid_on_location","Paid on loaction"));


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
            addTable.add(payType,2,addRow);
            addTable.add(bookedText,3,addRow);
            addTable.add(attTextBox,4,addRow);
            addTable.add(amountText,5,addRow);
            addTable.setRowColor(addRow, theColor);

            if (closerLook)
            try {
              entries = bookings[i].getBookingEntries();
              coll = bookings[i].getTravelAddresses();
              bookingAddresses = (TravelAddress[]) coll.toArray(new TravelAddress[]{});
  //            bookingAddresses = (TravelAddress[]) bookings[i].findRelated((TravelAddress)com.idega.block.trade.stockroom.data.TravelAddressBMPBean.getStaticInstance(TravelAddress.class));
              /*if (bookingAddresses.length > 0) {
                addressText = (Text) smallText.clone();
                addressText.setText(bookingAddresses[0].getName()+Text.NON_BREAKING_SPACE+Text.NON_BREAKING_SPACE);
                addressText.setFontColor(super.BLACK);
                addTable.add(addressText, 1, addRow+1);
                addTable.setAlignment(1,addRow+1, "right");
              }*/
              for (int j = 0; j < entries.length; j++) {
                ++addRow;
                addTable.setRowColor(addRow, theColor);
                price = entries[j].getProductPrice();
                iEntryCount = (int) getBooker(iwc).getBookingEntryPrice(entries[j], bookings[i]);

                nameText = (Text) smallText.clone();
                  nameText.setText(Text.NON_BREAKING_SPACE + Text.NON_BREAKING_SPACE + price.getPriceCategory().getName());
                bookedText = (Text) smallText.clone();
                  bookedText.setText(Integer.toString(entries[j].getCount()));
                amountText = (Text) smallText.clone();
                  amountText.setText(Integer.toString(iEntryCount));

                nameText.setFontColor(super.BLACK);
                bookedText.setFontColor(super.BLACK);
                amountText.setFontColor(super.BLACK);

                entryCount = (Integer) map.get(price.getPriceCategoryIDInteger()+"_"+bookingAddresses[0].getID());
                entryCount = new Integer(entryCount.intValue() + entries[j].getCount());
                map.put(price.getPriceCategoryIDInteger()+"_"+bookingAddresses[0].getID(),entryCount);

                addTable.add(nameText,2,addRow);
                addTable.add(bookedText,3,addRow);
                addTable.add(amountText,4,addRow);
                addTable.setAlignment(2,addRow, "LEFT");
              }


            }catch (FinderException fe) {
              fe.printStackTrace(System.err);
            }catch (IDORelationshipException re) {
              re.printStackTrace(System.err);
            }

        }
      }
      addTable.setWidth(2,twoWidth);
      addTable.setWidth(3,threeWidth);
      addTable.setWidth(4,fourWidth);
      addTable.setWidth(5,fiveWidth);
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

//      bookings = getBooker(iwc).getBookings(product.getID(),stamp,Booking.BOOKING_TYPE_ID_CORRECTION);
//      if (bookings.length == 0) {
//	corrRow++;
//	correctionTable.setRowColor(corrRow,theColor);
//      }
      for (int ta = 0; ta < addressesSize; ta++) {
        address = (TravelAddress) addresses.get(ta);
        bookings = getBooker(iwc).getBookings(new int[] {product.getID()},stamp,null, new int[]{Booking.BOOKING_TYPE_ID_CORRECTION}, address);
        ++corrRow;
        correctionTable.setRowColor(corrRow,super.backgroundColor);
        correctionTable.mergeCells(1, corrRow, 5, corrRow);
        travelAddressText = super.getText(address.getName());
        travelAddressText.setFontColor(super.WHITE);
        correctionTable.add(travelAddressText, 1, corrRow);

        for (int i = 0; i < bookings.length; i++) {
            ++corrRow;
            correctionTable.setRowColor(corrRow,super.backgroundColor);
            correctionTable.setAlignment(2,corrRow,"center");
            ibookings = bookings[i].getTotalCount();
            attendance = bookings[i].getAttendance();
            amount = getBooker(iwc).getBookingPrice(bookings[i]);

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
            correctionTable.add(new HiddenInput("payment_type",Integer.toString(bookings[i].getPaymentTypeId())),1,corrRow);

            correctionTable.add(nameText,1,corrRow);

            correctionTable.add(bookedText,3,corrRow);
            correctionTable.add(attTextBox,4,corrRow);
            correctionTable.add(amountText,5,corrRow);
            correctionTable.setRowColor(corrRow, theColor);

            if (closerLook)
            try {
              entries = bookings[i].getBookingEntries();
              coll = bookings[i].getTravelAddresses();
              bookingAddresses = (TravelAddress[]) coll.toArray(new TravelAddress[]{});
  //            bookingAddresses = (TravelAddress[]) bookings[i].findRelated((TravelAddress)com.idega.block.trade.stockroom.data.TravelAddressBMPBean.getStaticInstance(TravelAddress.class));
/*              if (bookingAddresses.length > 0) {
                addressText = (Text) smallText.clone();
                addressText.setText(bookingAddresses[0].getName()+Text.NON_BREAKING_SPACE+Text.NON_BREAKING_SPACE);
                addressText.setFontColor(super.BLACK);
                correctionTable.add(addressText, 1, corrRow+1);
                correctionTable.setAlignment(1,corrRow+1, "right");
              }*/
              for (int j = 0; j < entries.length; j++) {
                ++corrRow;
                correctionTable.setRowColor(corrRow, theColor);
                price = entries[j].getProductPrice();
                iEntryCount = (int) getBooker(iwc).getBookingEntryPrice(entries[j], bookings[i]);

                nameText = (Text) smallText.clone();
                  nameText.setText(Text.NON_BREAKING_SPACE + Text.NON_BREAKING_SPACE + price.getPriceCategory().getName());
                bookedText = (Text) smallText.clone();
                  bookedText.setText(Integer.toString(entries[j].getCount()));
                amountText = (Text) smallText.clone();
                  amountText.setText(Integer.toString(iEntryCount));

                nameText.setFontColor(super.BLACK);
                bookedText.setFontColor(super.BLACK);
                amountText.setFontColor(super.BLACK);

                entryCount = (Integer) map.get(price.getPriceCategoryIDInteger()+"_"+bookingAddresses[0].getID());
                entryCount = new Integer(entryCount.intValue() + entries[j].getCount());
                map.put(price.getPriceCategoryIDInteger()+"_"+bookingAddresses[0].getID(),entryCount);

                correctionTable.add(nameText,2,corrRow);
                correctionTable.add(bookedText,3,corrRow);
                correctionTable.add(amountText,4,corrRow);
                correctionTable.setAlignment(2,corrRow, "LEFT");
              }


            }catch (FinderException fe) {
              fe.printStackTrace(System.err);
            }catch (IDORelationshipException re) {
              re.printStackTrace(System.err);
            }

        }
      }
      correctionTable.setWidth(2,twoWidth);
      correctionTable.setWidth(3,threeWidth);
      correctionTable.setWidth(4,fourWidth);
      correctionTable.setWidth(5,fiveWidth);
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
	    attTextBox.setDisabled(true);
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
	  for (int k = 0; k < addressesSize; k++) {
            address = (TravelAddress) addresses.get(k);
            tframe = getProductBusiness(iwc).getTimeframe(product, stamp, address.getID());
	      prices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(product.getID(), tframe.getID(), address.getID(), false);
	      misc = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getMiscellaneousPrices(product.getID(), tframe.getID(), address.getID(), false);
	      addressText = (Text) smallText.clone();
		addressText.setText(address.getName()+Text.NON_BREAKING_SPACE + Text.NON_BREAKING_SPACE);
		addressText.setFontColor(super.BLACK);
	      totalTable.add(addressText, 1, tRow+1);
	      totalTable.setAlignment(1,tRow+1,"right");

	    for (int i = 0; i < prices.length; i++) {
	      try {
		++tRow;
		totalTable.setRowColor(tRow, theColor);
		many = ((Integer) map.get(prices[i].getPriceCategoryIDInteger()+"_"+address.getID())).intValue();
		nameText = (Text) smallText.clone();
		  nameText.setText(Text.NON_BREAKING_SPACE + Text.NON_BREAKING_SPACE+prices[i].getPriceCategory().getName());
		bookedText = (Text) smallText.clone();
		  bookedText.setText(Integer.toString(many));
		amountText = (Text) smallText.clone();
		  amountText.setText(Integer.toString(many * ((int) getTravelStockroomBusiness(iwc).getPrice(prices[i].getID(), product.getID(), prices[i].getPriceCategoryID(), prices[i].getCurrencyId(), IWTimestamp.getTimestampRightNow(), tframe.getID(), address.getID()))));

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

	    for (int i = 0; i < misc.length; i++) {
	      try {
		++tRow;
		totalTable.setRowColor(tRow, theColor);
		many = ((Integer) map.get(misc[i].getPriceCategoryIDInteger()+"_"+address.getID())).intValue();
		nameText = (Text) smallText.clone();
		  nameText.setText(Text.NON_BREAKING_SPACE + Text.NON_BREAKING_SPACE+misc[i].getPriceCategory().getName());
		bookedText = (Text) smallText.clone();
		  bookedText.setText(Integer.toString(many));
		amountText = (Text) smallText.clone();
		  amountText.setText(Integer.toString(many * ((int) getTravelStockroomBusiness(iwc).getPrice(misc[i].getID(), product.getID(), misc[i].getPriceCategoryID(), misc[i].getCurrencyId(), IWTimestamp.getTimestampRightNow(), tframe.getID(), address.getID()))));

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

	  }

//          totalTable.setColumnAlignment(1,"left");
	  totalTable.setColumnAlignment(3,"center");
	  totalTable.setColumnAlignment(4,"center");
	  totalTable.setColumnAlignment(5,"center");


      Link link = new Link(iwrb.getImage("buttons/add.gif"));
	link.setFontColor(super.textColor);
	link.addParameter(AdditionalBooking.parameterServiceId,product.getID());
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
	open = new SubmitButton(iwrb.getImage("buttons/close.gif"),this.parameterToggleCloser, this.parameterNo);
      }else {
	open = new SubmitButton(iwrb.getImage("buttons/closer.gif"),this.parameterToggleCloser, this.parameterYes);
      }

      theTable.setAlignment(1,8,"right");
      theTable.add(open,1,8);
      theTable.add(submit,1,8);


      return theTable;

  }


  private void update(IWContext iwc) throws RemoteException{
    String[] booking_ids = (String[]) iwc.getParameterValues("booking_id");
    String[] attendance  = (String[]) iwc.getParameterValues("attendance");
    String[] pay_type    = (String[]) iwc.getParameterValues("payment_type");

    Booking booking;
    if (booking_ids != null)
    for (int i = 0; i < booking_ids.length; i++) {
      try {
	booking = ((is.idega.idegaweb.travel.data.GeneralBookingHome)com.idega.data.IDOLookup.getHome(GeneralBooking.class)).findByPrimaryKey(booking_ids[i]);
	try {
	  booking.setAttendance(Integer.parseInt(attendance[i]));
	}catch (NumberFormatException n) {
	  booking.setAttendance(0);
	}
	try {
	  booking.setPaymentTypeId(Integer.parseInt(pay_type[i]));
	}catch (NumberFormatException n) {}
	booking.store();
      }catch (FinderException fe) {
	fe.printStackTrace(System.err);
      }
    }
  }

  public PresentationObject getReport(IWContext iwc, List products, IWTimestamp stamp, IWTimestamp toStamp) {
    /**
     * unsupported
     */
    return new Table();
  }

  public PresentationObject getReport(IWContext iwc, List products, IWTimestamp stamp) throws RemoteException, FinderException {
    handleInsert(iwc);
    if (products.size() == 1) {
      return getBookingTable(iwc, (Product) products.get(0), stamp);
    }else {
      return getDailyReportSimple(iwc, products, stamp);
    }
  }

  private void handleInsert(IWContext iwc) throws RemoteException {
    String toggler = iwc.getParameter(this.parameterToggleCloser);
    Boolean sessionToggler = (Boolean) iwc.getSessionAttribute(this.sessionParameterToggleCloser);

    if (toggler != null && !toggler.equals("")) {
      if (toggler.equals(this.parameterNo)) {
	this.closerLook = false;
      }else if (toggler.equals(this.parameterYes)) {
	this.closerLook = true;
      }
      iwc.setSessionAttribute(this.sessionParameterToggleCloser, new Boolean(closerLook));
    }else if (sessionToggler != null) {
      if (sessionToggler.booleanValue()) {
	this.closerLook = true;
      }else {
	this.closerLook = false;
      }
    }

    String action = iwc.getParameter(this.sAction);
    if (action != null && action.equals(this.parameterUpdate)) {
      this.update(iwc);
    }
  }

  public Table getDailyReportSimple(IWContext iwc, List products, IWTimestamp stamp) throws RemoteException, FinderException {
      Table table = new Table();
	table.setColor(super.WHITE);
	table.setCellspacing(1);
	table.setCellpadding(2);
	table.setWidth("100%");
      int row = 1;
      int count = 0;
      int totalCount = 0;
      float price = 0;
      float totalPrice = 0;

      Product prod;
      Booking[] bookings;
      IWTimestamp depTime;


      String sOrderBy = iwc.getParameter("dayRepOrderBy");
      int iOrderBy = ProductComparator.NUMBER;
      if (sOrderBy != null) iOrderBy = Integer.parseInt(sOrderBy);


      Text servNameTxt = (Text) super.theBoldText.clone();
	 servNameTxt.setText(iwrb.getLocalizedString("travel.service_name","Service name"));
      Text servNumTxt = (Text) super.theBoldText.clone();
	servNumTxt.setText(iwrb.getLocalizedString("travel.nr","Nr."));
      Text timeTxt = (Text) super.theBoldText.clone();;
	timeTxt.setText(iwrb.getLocalizedString("travel.time","Time"));
      Text priceTxt = (Text) super.theBoldText.clone();
	priceTxt.setText(iwrb.getLocalizedString("travel.price","Price"));
      Text countTxt = (Text) super.theBoldText.clone();
	countTxt.setText(iwrb.getLocalizedString("travel.count","Count"));
      Text productCountTxt;
      Text productPriceTxt;

	servNameTxt.setFontColor(super.WHITE);
	servNumTxt.setFontColor(super.WHITE);
	timeTxt.setFontColor(super.WHITE);
	priceTxt.setFontColor(super.WHITE);
	countTxt.setFontColor(super.WHITE);


      Link servNameLnk = Reports.getReportLink(servNameTxt);
	servNameLnk.addParameter("dayRepOrderBy",ProductComparator.NAME);
      Link servNumLnk = Reports.getReportLink(servNumTxt);
	servNumLnk.addParameter("dayRepOrderBy",ProductComparator.NUMBER);
//      Link timeLnk = Reports.getReportLink(timeTxt);
//	timeLnk.addParameter("dayRepOrderBy",ProductComparator.DEPARTURETIME_NAME);

//      addParameters(servNameLnk);
//      addParameters(servNumLnk);
//      addParameters(timeLnk);


      table.add(servNumLnk, 1, row);
      table.add(servNameLnk, 2, row);
      table.add(timeTxt, 3, row);
      table.add(countTxt, 4, row);
      table.add(priceTxt, 5, row);
      table.setRowColor(row, super.backgroundColor);


      Text pNumberTxt;
      Text pNameTxt;
      Text pTimeTxt;
      Text pCountTxt;
      Text pPriceTxt;

      Collections.sort(products, new ProductComparator(iOrderBy));
      for (int i = 0; i < products.size(); i++) {
	try {
	++row;
	table.setRowColor(row, super.GRAY);
	prod = (Product) products.get(i);
	bookings = getBooker(iwc).getBookings(prod.getID(), stamp);
	count = getBooker(iwc).getBookingsTotalCount(prod.getID(), stamp,-1);
	price =  getBooker(iwc).getBookingPrice(bookings);
	depTime = getServiceHandler(iwc).getDepartureTime(prod);
	totalCount += count;
	totalPrice += price;

	pNumberTxt = (Text) super.theText.clone();
	pNameTxt = (Text) super.theText.clone();
	pTimeTxt = (Text) super.theText.clone();
	pCountTxt = (Text) super.theText.clone();
	pPriceTxt = (Text) super.theText.clone();

	pNumberTxt.setFontColor(super.BLACK);
	pNameTxt.setFontColor(super.BLACK);
	pTimeTxt.setFontColor(super.BLACK);
	pCountTxt.setFontColor(super.BLACK);
	pPriceTxt.setFontColor(super.BLACK);

	pNumberTxt.setText(prod.getNumber());
	pNameTxt.setText(prod.getProductName(getLocaleId()));
	pTimeTxt.setText(TextSoap.addZero(depTime.getHour())+":"+TextSoap.addZero(depTime.getMinute()));
	pCountTxt.setText(Integer.toString(count));
	pPriceTxt.setText(TextSoap.decimalFormat(price, 2));

	table.add(pNumberTxt, 1,row);
	Link pNameLink = Reports.getReportLink(pNameTxt);
	  pNameLink.addParameter(Reports.PARAMETER_PRODUCT_ID, prod.getID());
	table.add(pNameLink,2, row);
	table.add(pTimeTxt, 3, row);
	table.add(pCountTxt, 4, row);
	table.add(pPriceTxt, 5, row);

	}catch (SQLException sql) {
	  sql.printStackTrace(System.err);
	}
      }

      ++row;
      table.setRowColor(row, super.backgroundColor);
      Text totalCountTxt = (Text) super.theBoldText.clone();
	totalCountTxt.setText(Integer.toString(totalCount));
      Text totalPriceTxt = (Text) super.theBoldText.clone();
	totalPriceTxt.setText(TextSoap.decimalFormat(totalPrice, 2));
      table.add(totalCountTxt, 4, row);
      table.add(totalPriceTxt, 5, row);
      table.setColumnAlignment(1, "center");
      table.setColumnAlignment(3, "center");
      table.setColumnAlignment(4, "center");
      table.setColumnAlignment(5, "right");

      table.setWidth(1, "70");
      table.setWidth(3, "70");
      table.setWidth(4, "70");
      table.setWidth(5, "70");
      return table;
  }
}
