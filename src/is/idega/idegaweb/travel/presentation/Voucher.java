package is.idega.idegaweb.travel.presentation;

import com.idega.core.user.data.User;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.idegaweb.*;
import com.idega.core.data.*;
import com.idega.block.trade.stockroom.data.*;
import com.idega.block.trade.stockroom.business.*;
import com.idega.block.trade.data.*;
import com.idega.util.IWTimestamp;
import is.idega.idegaweb.travel.interfaces.Booking;
import is.idega.idegaweb.travel.data.*;
import is.idega.idegaweb.travel.business.TravelStockroomBusiness;
import is.idega.idegaweb.travel.business.Booker;

import java.text.DecimalFormat;
import java.sql.SQLException;
import java.util.*;
import java.util.Collection;
import java.rmi.RemoteException;
import javax.ejb.FinderException;
/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public abstract class Voucher extends TravelManager {

  public static int width = 580;
  public static final int voucherNumberChanger = 4098;

  private IWContext _iwc;
  private IWResourceBundle _iwrb;
  private IWBundle _bundle;

  protected Booking _booking;
  private List _bookings;
  private BookingEntry[] _entries;
  private Service _service;
  private Product _product;
  private Supplier _supplier;
  private Timeframe _timeframe;
  private TravelAddress _address;
  private User _user;
  private Reseller _reseller;
  private int _localeId = -1;

  private DecimalFormat df = new DecimalFormat("0.00");
  private List sectOne;
  private List sectTwo;
  private List sectThree;
  private List sectFour;
  private List clientInfo;
  private List sectFive;

  public Voucher(){}

  public Voucher(int bookingId) throws Exception{
    this(((is.idega.idegaweb.travel.data.GeneralBookingHome)com.idega.data.IDOLookup.getHome(GeneralBooking.class)).findByPrimaryKey(new Integer(bookingId)));
  }

  public Voucher(Booking booking) throws Exception{
    _booking = booking;
  }


  public void add(PresentationObject po) {
    super.addToBlock(po);
  }

  public void main(IWContext iwc) throws Exception{
  	System.out.println("[Voucher] starting voucher for bookingId = "+_booking.getID());
    super.initializer(iwc);
		setupVoucher(iwc);
    _bundle = super.getBundle();
    _iwrb = super.getResourceBundle();
    _iwc = iwc;
    _localeId = iwc.getCurrentLocaleId();
    try {
      GeneralBooking gBooking = ((is.idega.idegaweb.travel.data.GeneralBookingHome)com.idega.data.IDOLookup.getHome(GeneralBooking.class)).findByPrimaryKey(_booking.getPrimaryKey());
      _bookings = getBooker(iwc).getMultibleBookings(gBooking);
      _service = _booking.getService();
      _product = _service.getProduct();
      _entries = _booking.getBookingEntries();
      _supplier = ((com.idega.block.trade.stockroom.data.SupplierHome)com.idega.data.IDOLookup.getHomeLegacy(Supplier.class)).findByPrimaryKeyLegacy(_product.getSupplierId());
      if (_booking.getUserId() != -1) {
        _user = ((com.idega.core.user.data.UserHome)com.idega.data.IDOLookup.getHomeLegacy(User.class)).findByPrimaryKeyLegacy(_booking.getUserId());
        _reseller = ResellerManager.getReseller(_user);
      }
      _timeframe = getProductBusiness(iwc).getTimeframe(_product, new IWTimestamp(_booking.getBookingDate()));
      Collection coll = gBooking.getTravelAddresses();
      TravelAddress[] addresses = (TravelAddress[]) coll.toArray(new TravelAddress[]{});
      if (addresses != null && addresses.length > 0 ) {
        _address = addresses[addresses.length - 1];
      }
      add(getVoucher(iwc));
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }
		System.out.println("[Voucher] finished voucher for bookingId = "+_booking.getID());
  }

  private Text getBigText(String content) {
    Text text = (Text) super.theBigBoldText.clone();
      text.setFontColor(BLACK);
      text.setText(content);
    return text;
  }

  private Text getBoldText(String content) {
    Text text = getText(content);
      text.setBold();
    return text;
  }

  private Text getSmallText(String content) {
    Text text = (Text) super.theSmallBoldText.clone();
      text.setFontColor(BLACK);
      text.setText(content);
    return text;
  }

  private int getVoucherNumber() throws RemoteException{
    return _booking.getID() + voucherNumberChanger;
  }

  public static int getVoucherNumber(int bookingId) {
    return bookingId + voucherNumberChanger;
  }

  private Table getVoucher(IWContext iwc) throws RemoteException{
    Table bigTable = new Table();
      bigTable.setColor(BLACK);
      bigTable.setCellspacing(0);
      bigTable.setCellpadding(0);
      bigTable.setWidth(width);
      bigTable.setBorder(1);
      bigTable.setBorderColor(BLACK);

    Table table = new Table(3,2);
      bigTable.add(table);
      table.setColor(WHITE);
      table.setWidth("100%");
      table.setWidth(1,"33%");
      table.setWidth(2,"34%");
      table.setWidth(3,"33%");
      table.setBorder(0);
      table.setCellspacing(10);
      table.mergeCells(1,2,3,2);

    boolean error = false;

    if (_booking == null) {
      table.add(_iwrb.getLocalizedString("travel.no_booking_specified","No booking specified"),2,1);
    }else {
      try {
        Table leftHeader = new Table();
          leftHeader.add(getSmallText(_iwrb.getLocalizedString("travel.NR","NR")),1,1);
          leftHeader.add(getSmallText(" : "),1,1);
          leftHeader.add(getSmallText(Integer.toString(getVoucherNumber())),1,1);
          leftHeader.add(getSmallText(_iwrb.getLocalizedString("travel.reference_number_show","Reference nr. ")),1,2);
          leftHeader.add(getSmallText(_booking.getReferenceNumber()),1,2);
        table.add(leftHeader,1,1);
        table.setAlignment(1,1,"left");

        Table centerHeader = new Table();
          centerHeader.add(getBigText(_iwrb.getLocalizedString("travel.voucher","Voucher")),1,1);
          if (_reseller != null) {
            centerHeader.add(getText(_reseller.getName()),1,2);
          }else {
            centerHeader.add(getText(_supplier.getName()),1,2);
          }
          centerHeader.setAlignment(1,1,"center");
          centerHeader.setAlignment(1,2,"center");
        table.add(centerHeader,2,1);
        table.setAlignment(2,1,"center");

        Table rightHeader = new Table();
          rightHeader.add(getSmallText(_iwrb.getLocalizedString("travel.date_of_issue","Date of issue")),1,1);
          rightHeader.add(getSmallText(":"),1,1);
//          rightHeader.add(getSmallText(IWTimestamp.RightNow().getLocaleDate(_iwc)),1,2);
          rightHeader.add(getSmallText(new IWTimestamp(_booking.getDateOfBooking()).getLocaleDate(_iwc)),1,2);
          rightHeader.setAlignment(1,1,"right");
          rightHeader.setAlignment(1,2,"right");
        table.add(rightHeader,3,1);
        table.setAlignment(3,1,"right");


        String strng;
        int size;
        // SECTION ONE BEGINS
        if (sectOne != null) {
          size = sectOne.size();
          for (int i = 0 ; i < size ; i++) {
            strng = (String) sectOne.get(i);
            table.add(strng, 1, 2);
            table.add(Text.BREAK,1,2);
          }
        }
        // SECTION ONE ENDS

        table.add(Text.BREAK,1,2);

        Address address = null;
        List hPhone = null;
        List fPhone = null;
        List emails = null;
        String name = "";

        name = _supplier.getName();
        address = _supplier.getAddress();
        hPhone = _supplier.getHomePhone();
        fPhone = _supplier.getFaxPhone();
        emails = _supplier.getEmails();

        table.add(getText(_iwrb.getLocalizedString("travel.to_lg","TO")+" : "),1,2);
        table.add(getText(name),1,2);
        table.add(Text.BREAK,1,2);

        table.add(getText(_iwrb.getLocalizedString("travel.address_lg","ADDRESS")+" : "),1,2);
        table.add(getText(address.getStreetName()),1,2);
        table.add(Text.BREAK,1,2);

        Phone phone;

        table.add(getText(_iwrb.getLocalizedString("travel.telephone_number_lg","PHONE")+" : "), 1, 2);
        if (hPhone != null)
        for (int i = 0; i < hPhone.size(); i++) {
          if (i != 0) table.add(getText(", "), 1, 2);
          phone = (Phone) hPhone.get(i);
          table.add(getText(phone.getNumber()), 1,2);
        }
        table.add(Text.BREAK,1,2);

        table.add(getText(_iwrb.getLocalizedString("travel.fax_lg","FAX")+" : "), 1, 2);
        if (fPhone != null)
        for (int i = 0; i < fPhone.size(); i++) {
          if (i != 0) table.add(getText(", "), 1, 2);
          phone = (Phone) fPhone.get(i);
          table.add(getText(phone.getNumber()), 1,2);
        }
        table.add(Text.BREAK,1,2);

        table.add(getText(_iwrb.getLocalizedString("travel.email_lg","E-MAIL")+" : "), 1, 2);
        Email email;
        if (emails != null)
        for (int i = 0; i < emails.size(); i++) {
          if (i != 0) table.add(getText(", "), 1, 2);
          email = (Email) emails.get(i);
          table.add(getText(email.getEmailAddress()), 1,2);
        }
        table.add(Text.BREAK,1,2);

        // SECTION TWO BEGINS
        if (sectTwo != null) {
          size = sectTwo.size();
          for (int i = 0 ; i < size ; i++) {
            strng = (String) sectTwo.get(i);
            table.add(strng, 1, 2);
            table.add(Text.BREAK,1,2);
          }
        }
        // SECTION TWO ENDS

        table.add(Text.BREAK,1,2);

        table.add(getText(_iwrb.getLocalizedString("travel.this_order_to_be_accepted","This order to be accepted at amount shown as part or full payment for the following services")),1,2);
        // SECTION THREE BEGINS
        if (sectThree != null) {
          size = sectThree.size();
          for (int i = 0 ; i < size ; i++) {
            strng = (String) sectThree.get(i);
            table.add(strng, 1, 2);
            table.add(Text.BREAK,1,2);
          }
        }
        // SECTION THREE ENDS
        table.add(Text.BREAK,1,2);

        table.add(Text.BREAK,1,2);
        table.add(getText(getProductBusiness(iwc).getProductNameWithNumber(_product, true, _localeId)),1,2);
        table.add(Text.BREAK,1,2);
        if (_bookings.size() > 0) {
          IWTimestamp fromStamp = new IWTimestamp(((Booking)_bookings.get(0)).getBookingDate());
          if (_bookings.size() < 2) {
            table.add(getText(fromStamp.getLocaleDate(_iwc)),1,2);
          }else {
            IWTimestamp toStamp = new IWTimestamp(((Booking)_bookings.get(_bookings.size()-1)).getBookingDate());
            table.add(getText(fromStamp.getLocaleDate(_iwc)+" - "+toStamp.getLocaleDate(iwc)),1,2);
          }
        }
        table.add(Text.BREAK,1,2);
        if (_address != null) {
          table.add(_iwrb.getLocalizedString("travel.departure_place","Departure place"), 1,2);
          table.add(getText(" : "),1,2);
          table.add(getText(_address.getName()),1,2);
        }
        // SECTION FOUR BEGINS
        if (sectFour != null) {
          size = sectFour.size();
          for (int i = 0 ; i < size ; i++) {
            strng = (String) sectFour.get(i);
            table.add(strng, 1, 2);
            table.add(Text.BREAK,1,2);
          }
        }
        // SECTION FOUR ENDS

        table.add(Text.BREAK,1,2);

        table.add(Text.BREAK,1,2);
        table.add(Text.BREAK,1,2);

        table.add(getText(_iwrb.getLocalizedString("travel.client_name_lg","CLIENT NAME")),1,2);
        table.add(getText(" : "),1,2);
        table.add(getText(_booking.getName()),1,2);
        table.add(Text.BREAK,1,2);
        // SECTION CLIENT_INFO BEGINS
        if (clientInfo != null) {
          size = clientInfo.size();
          for (int i = 0 ; i < size ; i++) {
            strng = (String) clientInfo.get(i);
            table.add(strng, 1, 2);
            table.add(Text.BREAK,1,2);
          }
        }
        // SECTION CLIENT_INFO ENDS

        table.add(Text.BREAK,1,2);
        table.add(getText(_iwrb.getLocalizedString("travel.party_of_lg","PARTY OF")),1,2);
        table.add(getText(" : "),1,2);
        table.add(getText(Integer.toString(_booking.getTotalCount())),1,2);
        table.add(Text.BREAK,1,2);
        for (int i = 0; i < _entries.length; i++) {
          table.add(getText(Text.NON_BREAKING_SPACE+ Text.NON_BREAKING_SPACE),1,2);
          table.add(getText(_entries[i].getProductPrice().getPriceCategory().getName()),1,2);
          table.add(getText(" : "),1,2);
          table.add(getText(Integer.toString(_entries[i].getCount())),1,2);
          table.add(Text.BREAK,1,2);
        }


        table.add(Text.BREAK,1,2);
        table.add(getText(_iwrb.getLocalizedString("travel.amount_paid_lg","AMOUNT PAID")),1,2);
        table.add(getText(" : "),1,2);
//        table.add(getText(df.format(Booker.getBookingPrice(iwc, _booking))),1,2);
        table.add(getText(df.format(getBooker(iwc).getBookingPrice(_bookings))),1,2);
        table.add(getText(" "),1,2);
        com.idega.block.trade.data.Currency currency = getBooker(iwc).getCurrency(_booking);
        if (currency != null)
        table.add(getText(currency.getCurrencyAbbreviation()),1,2);
        table.add(Text.BREAK,1,2);


        // SECTION FIVE BEGINS
        if (sectFive != null) {
          size = sectFive.size();
          for (int i = 0 ; i < size ; i++) {
            strng = (String) sectFive.get(i);
            table.add(strng, 1, 2);
            table.add(Text.BREAK,1,2);
          }
        }
        // SECTION FIVE ENDS


        table.add(Text.BREAK,1,2);

      }catch (FinderException fe) {
        error = true;
        fe.printStackTrace(System.err);
      }catch (SQLException sql) {
        error = true;
        sql.printStackTrace(System.err);
      }catch (Exception e) {
      	error = true;
      	e.printStackTrace(System.err);
      }

      if (error) {
        table.add(getText(_iwrb.getLocalizedString("travel.voucher_error","Voucher could not be created, please write down your reference number")),1,2);
        table.add(Text.BREAK,1,2);
        table.add(getSmallText(_iwrb.getLocalizedString("travel.reference_number_show","Reference nr. ")),1,2);
        table.add(getSmallText(_booking.getReferenceNumber()),1,2);
      }
    }


    return bigTable;
  }

  protected void addToSectionOne(String lineToAdd) {
    if (sectOne == null) {
      sectOne = new Vector();
    }
    sectOne.add(lineToAdd);
  }

  protected void addToSectionTwo(String lineToAdd) {
    if (sectTwo == null) {
      sectTwo = new Vector();
    }
    sectTwo.add(lineToAdd);
  }

  protected void addToSectionThree(String lineToAdd) {
    if (sectThree == null) {
      sectThree = new Vector();
    }
    sectThree.add(lineToAdd);
  }

  protected void addToSectionFour(String lineToAdd) {
    if (sectFour == null) {
      sectFour = new Vector();
    }
    sectFour.add(lineToAdd);
  }

  protected void addToSectionFive(String lineToAdd) {
    if (sectFive == null) {
      sectFive = new Vector();
    }
    sectFive.add(lineToAdd);
  }

  protected void addToClientInfo(String lineToAdd) {
    if (clientInfo == null) {
      clientInfo = new Vector();
    }
    clientInfo.add(lineToAdd);
  }

  protected abstract void setupVoucher(IWContext iwc)throws RemoteException;

}
