package is.idega.idegaweb.travel.presentation;

import is.idega.idegaweb.travel.data.BookingEntry;
import is.idega.idegaweb.travel.data.GeneralBooking;
import is.idega.idegaweb.travel.data.Service;
import is.idega.idegaweb.travel.interfaces.Booking;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.block.creditcard.business.CreditCardBusiness;
import com.idega.block.creditcard.data.CreditCardAuthorizationEntry;
import com.idega.block.trade.stockroom.business.ResellerManager;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.Reseller;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.block.trade.stockroom.data.Timeframe;
import com.idega.block.trade.stockroom.data.TravelAddress;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.core.user.data.User;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.util.IWTimestamp;
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

  protected GeneralBooking _booking;
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
  private Table _table;

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

	public Voucher(GeneralBooking booking) throws Exception {
		_booking = booking;
	}

  public Voucher(Booking booking) throws Exception{
    _booking = ((is.idega.idegaweb.travel.data.GeneralBookingHome)com.idega.data.IDOLookup.getHome(GeneralBooking.class)).findByPrimaryKey(new Integer(booking.getID()));
  }


  public void add(PresentationObject po) {
    super.addToBlock(po);
  }

  public void main(IWContext iwc) throws Exception{
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
      add(getVoucher());
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }
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

  private Table getVoucher() throws RemoteException{
    Table bigTable = new Table();
      bigTable.setColor(BLACK);
      bigTable.setCellspacing(0);
      bigTable.setCellpadding(0);
      bigTable.setWidth(width);
      bigTable.setBorder(1);
      bigTable.setBorderColor(BLACK);

    _table = new Table(3,2);
      bigTable.add(_table);
      _table.setColor(WHITE);
      _table.setWidth("100%");
      _table.setWidth(1,"33%");
      _table.setWidth(2,"34%");
      _table.setWidth(3,"33%");
      _table.setBorder(0);
      _table.setCellspacing(10);
      _table.mergeCells(1,2,3,2);

    boolean error = false;

    if (_booking == null) {
      _table.add(_iwrb.getLocalizedString("travel.no_booking_specified","No booking specified"),2,1);
    }else {
      try {
        Table leftHeader = new Table();
          leftHeader.add(getSmallText(_iwrb.getLocalizedString("travel.NR","NR")),1,1);
          leftHeader.add(getSmallText(" : "),1,1);
          leftHeader.add(getSmallText(Integer.toString(getVoucherNumber())),1,1);
          leftHeader.add(getSmallText(_iwrb.getLocalizedString("travel.reference_number_show","Reference nr. ")),1,2);
          leftHeader.add(getSmallText(_booking.getReferenceNumber()),1,2);
        _table.add(leftHeader,1,1);
        _table.setAlignment(1,1,"left");

        Table centerHeader = new Table();
          centerHeader.add(getBigText(_iwrb.getLocalizedString("travel.voucher","Voucher")),1,1);
          if (_reseller != null) {
            centerHeader.add(getText(_reseller.getName()),1,2);
          }else {
            centerHeader.add(getText(_supplier.getName()),1,2);
          }
          centerHeader.setAlignment(1,1,"center");
          centerHeader.setAlignment(1,2,"center");
        _table.add(centerHeader,2,1);
        _table.setAlignment(2,1,"center");

        Table rightHeader = new Table();
          rightHeader.add(getSmallText(_iwrb.getLocalizedString("travel.date_of_issue","Date of issue")),1,1);
          rightHeader.add(getSmallText(":"),1,1);
          rightHeader.add(getSmallText(new IWTimestamp(_booking.getDateOfBooking()).getLocaleDate(_iwc)),1,2);
          rightHeader.setAlignment(1,1,"right");
          rightHeader.setAlignment(1,2,"right");
        _table.add(rightHeader,3,1);
        _table.setAlignment(3,1,"right");


        String strng;
        int size;
        // SECTION ONE BEGINS
        if (sectOne != null) {
          size = sectOne.size();
          for (int i = 0 ; i < size ; i++) {
            strng = (String) sectOne.get(i);
            _table.add(strng, 1, 2);
            _table.add(Text.BREAK,1,2);
          }
        }
        // SECTION ONE ENDS

        _table.add(Text.BREAK,1,2);

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

        _table.add(getText(_iwrb.getLocalizedString("travel.to_lg","TO")+" : "),1,2);
        _table.add(getText(name),1,2);
        _table.add(Text.BREAK,1,2);

        _table.add(getText(_iwrb.getLocalizedString("travel.address_lg","ADDRESS")+" : "),1,2);
        _table.add(getText(address.getStreetName()),1,2);
        _table.add(Text.BREAK,1,2);

        Phone phone;

        _table.add(getText(_iwrb.getLocalizedString("travel.telephone_number_lg","PHONE")+" : "), 1, 2);
        if (hPhone != null)
        for (int i = 0; i < hPhone.size(); i++) {
          if (i != 0) _table.add(getText(", "), 1, 2);
          phone = (Phone) hPhone.get(i);
          _table.add(getText(phone.getNumber()), 1,2);
        }
        _table.add(Text.BREAK,1,2);

        _table.add(getText(_iwrb.getLocalizedString("travel.fax_lg","FAX")+" : "), 1, 2);
        if (fPhone != null)
        for (int i = 0; i < fPhone.size(); i++) {
          if (i != 0) _table.add(getText(", "), 1, 2);
          phone = (Phone) fPhone.get(i);
          _table.add(getText(phone.getNumber()), 1,2);
        }
        _table.add(Text.BREAK,1,2);

        _table.add(getText(_iwrb.getLocalizedString("travel.email_lg","E-MAIL")+" : "), 1, 2);
        Email email;
        if (emails != null)
        for (int i = 0; i < emails.size(); i++) {
          if (i != 0) _table.add(getText(", "), 1, 2);
          email = (Email) emails.get(i);
          _table.add(getText(email.getEmailAddress()), 1,2);
        }
        _table.add(Text.BREAK,1,2);

        // SECTION TWO BEGINS
        if (sectTwo != null) {
          size = sectTwo.size();
          for (int i = 0 ; i < size ; i++) {
            strng = (String) sectTwo.get(i);
            _table.add(strng, 1, 2);
            _table.add(Text.BREAK,1,2);
          }
        }
        // SECTION TWO ENDS

        _table.add(Text.BREAK,1,2);

        _table.add(getText(_iwrb.getLocalizedString("travel.this_order_to_be_accepted","This order to be accepted at amount shown as part or full payment for the following services")),1,2);
        // SECTION THREE BEGINS
        if (sectThree != null) {
          size = sectThree.size();
          for (int i = 0 ; i < size ; i++) {
            strng = (String) sectThree.get(i);
            _table.add(strng, 1, 2);
            _table.add(Text.BREAK,1,2);
          }
        }
        // SECTION THREE ENDS
        _table.add(Text.BREAK,1,2);

        _table.add(Text.BREAK,1,2);
        _table.add(getText(getProductBusiness(_iwc).getProductNameWithNumber(_product, true, _localeId)),1,2);
        _table.add(Text.BREAK,1,2);
				addBookingDates(_table, _bookings, _iwc);
        _table.add(Text.BREAK,1,2);
        if (_address != null) {
          _table.add(_iwrb.getLocalizedString("travel.departure_place","Departure place"), 1,2);
          _table.add(getText(" : "),1,2);
          _table.add(getText(_address.getName()),1,2);
				  _table.add(Text.BREAK,1,2);
        }
        // SECTION FOUR BEGINS
        if (sectFour != null) {
          size = sectFour.size();
          for (int i = 0 ; i < size ; i++) {
            strng = (String) sectFour.get(i);
            _table.add(strng, 1, 2);
            _table.add(Text.BREAK,1,2);
          }
        }
        // SECTION FOUR ENDS

        _table.add(Text.BREAK,1,2);
        _table.add(Text.BREAK,1,2);

        _table.add(getText(_iwrb.getLocalizedString("travel.client_name_lg","CLIENT NAME")),1,2);
        _table.add(getText(" : "),1,2);
        _table.add(getText(_booking.getName()),1,2);
        _table.add(Text.BREAK,1,2);
        // SECTION CLIENT_INFO BEGINS
        if (clientInfo != null) {
          size = clientInfo.size();
          for (int i = 0 ; i < size ; i++) {
            strng = (String) clientInfo.get(i);
            _table.add(strng, 1, 2);
            _table.add(Text.BREAK,1,2);
          }
        }
        // SECTION CLIENT_INFO ENDS

        _table.add(Text.BREAK,1,2);
        _table.add(getText(_iwrb.getLocalizedString("travel.party_of_lg","PARTY OF")),1,2);
        _table.add(getText(" : "),1,2);
        _table.add(getText(Integer.toString(_booking.getTotalCount())),1,2);
        _table.add(Text.BREAK,1,2);
        for (int i = 0; i < _entries.length; i++) {
          _table.add(getText(Text.NON_BREAKING_SPACE+ Text.NON_BREAKING_SPACE),1,2);
          _table.add(getText(_entries[i].getProductPrice().getPriceCategory().getName()),1,2);
          _table.add(getText(" : "),1,2);
          _table.add(getText(Integer.toString(_entries[i].getCount())),1,2);
          _table.add(Text.BREAK,1,2);
        }



        _table.add(Text.BREAK,1,2);
        _table.add(getText(_iwrb.getLocalizedString("travel.amount_lg","AMOUNT")),1,2);
        _table.add(getText(" : "),1,2);
//        _table.add(getText(df.format(Booker.getBookingPrice(iwc, _booking))),1,2);
        _table.add(getText(df.format(getBooker(_iwc).getBookingPrice(_bookings))),1,2);
        _table.add(getText(" "),1,2);
        com.idega.block.trade.data.Currency currency = getBooker(_iwc).getCurrency(_booking);
        if (currency != null) {
					_table.add(getText(currency.getCurrencyAbbreviation()),1,2);
        }
        _table.add(Text.BREAK,1,2);

				String ccAuthNumber =  _booking.getCreditcardAuthorizationNumber();
				String cardType = null;
				if (ccAuthNumber != null) {
					CreditCardAuthorizationEntry entry = this.getCreditCardBusiness(_iwc).getAuthorizationEntry(_supplier, ccAuthNumber, new IWTimestamp(_booking.getDateOfBooking()));
					cardType = entry.getBrandName();
					_table.add(getText(_iwrb.getLocalizedString("travel.amount_paid_lg","AMOUNT PAID")),1,2);
					_table.add(getText(" : "),1,2);
					double fAmount = entry.getAmount() / CreditCardAuthorizationEntry.amountMultiplier;
					_table.add(getText(df.format(fAmount)+" "+entry.getCurrency()), 1, 2);
					_table.add(Text.BREAK,1,2);
				}
				_table.add(getText(_iwrb.getLocalizedString("travel.payment_type_lg","PAYMENT TYPE")),1,2);
				_table.add(getText(" : "),1,2);
				if (cardType == null) {
					_table.add(getText(getBooker(_iwc).getPaymentType(_iwrb, _booking.getPaymentTypeId())), 1, 2);
				}else {
					_table.add(getText(cardType), 1, 2);
				}
				_table.add(Text.BREAK, 1, 2);
				
		_table.add(Text.BREAK, 1, 2);


				_table.add(getText(_iwrb.getLocalizedString("travel.comment_lg","COMMENT")),1,2);
				_table.add(getText(" : "+Text.BREAK),1,2);
				_table.add(getText(_booking.getComment()), 1, 2);
				_table.add(getText(Text.BREAK), 1, 2);


        // SECTION FIVE BEGINS
        if (sectFive != null) {
          size = sectFive.size();
          for (int i = 0 ; i < size ; i++) {
            strng = (String) sectFive.get(i);
            _table.add(strng, 1, 2);
            _table.add(Text.BREAK,1,2);
          }
        }
        // SECTION FIVE ENDS


        _table.add(Text.BREAK,1,2);

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
        _table.add(getText(_iwrb.getLocalizedString("travel.voucher_error","Voucher could not be created, please write down your reference number")),1,2);
        _table.add(Text.BREAK,1,2);
        _table.add(getSmallText(_iwrb.getLocalizedString("travel.reference_number_show","Reference nr. ")),1,2);
        _table.add(getSmallText(_booking.getReferenceNumber()),1,2);
      }
    }


    return bigTable;
  }

  protected void addBookingDates(Table table, List bookings, IWContext iwc) throws RemoteException {
		if (bookings.size() > 0) {
		  IWTimestamp fromStamp = new IWTimestamp(((Booking)bookings.get(0)).getBookingDate());
		  if (bookings.size() < 2) {
		    table.add(getText(fromStamp.getLocaleDate(iwc)),1,2);
		  }else {
		    IWTimestamp toStamp = new IWTimestamp(((Booking)bookings.get(bookings.size()-1)).getBookingDate());
		    table.add(getText(fromStamp.getLocaleDate(iwc)+" - "+toStamp.getLocaleDate(iwc)),1,2);
		  }
		}
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
