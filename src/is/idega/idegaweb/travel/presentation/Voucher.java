package is.idega.idegaweb.travel.presentation;

import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.idegaweb.*;
import is.idega.idegaweb.travel.interfaces.Booking;
import com.idega.core.data.*;
import com.idega.block.trade.stockroom.data.*;
import com.idega.block.trade.stockroom.business.*;
import com.idega.block.trade.data.*;
import com.idega.util.idegaTimestamp;
import is.idega.idegaweb.travel.data.*;
import is.idega.idegaweb.travel.business.TravelStockroomBusiness;
import is.idega.idegaweb.travel.business.Booker;

import java.text.DecimalFormat;
import java.sql.SQLException;
import java.util.List;
/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class Voucher extends TravelManager {

  public static int width = 580;
  public static final int voucherNumberChanger = 4098;

  private IWContext _iwc;
  private IWResourceBundle _iwrb;
  private IWBundle _bundle;

  private Booking _booking;
  private BookingEntry[] _entries;
  private Service _service;
  private Product _product;
  private Supplier _supplier;
  private Timeframe _timeframe;
  private Address _address;

  private DecimalFormat df = new DecimalFormat("0.00");

  public Voucher(IWContext iwc, int bookingId) throws Exception{
    this(iwc, new GeneralBooking(bookingId));
  }

  public Voucher(IWContext iwc, Booking booking) throws Exception{
    super.main(iwc);
    _bundle = super.getBundle();
    _iwrb = super.getResourceBundle();
    _iwc = iwc;
    try {
      _booking = booking;
      _service = _booking.getService();
      _product = _service.getProduct();
      _entries = _booking.getBookingEntries();
      _supplier = new Supplier(_product.getSupplierId());
      _timeframe = ProductBusiness.getTimeframe(_product, new idegaTimestamp(_booking.getBookingDate()));
      GeneralBooking gBooking = new GeneralBooking(_booking.getID());
      /*
      Address[] addresses = (Address[]) gBooking.findRelated(Address.getStaticInstance(Address.class));
      _address = addresses[addresses.length - 1];*/
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

  private Text getText(String content) {
    Text text = (Text) theText.clone();
      text.setFontColor(BLACK);
      text.setText(content);
    return text;
  }

  private int getVoucherNumber() {
    return _booking.getID() + voucherNumberChanger;
  }

  public Table getVoucher(IWContext iwc) {
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
          centerHeader.add(getText(_supplier.getName()),1,2);
          centerHeader.setAlignment(1,1,"center");
          centerHeader.setAlignment(1,2,"center");
        table.add(centerHeader,2,1);
        table.setAlignment(2,1,"center");

        Table rightHeader = new Table();
          rightHeader.add(getSmallText(_iwrb.getLocalizedString("travel.date_of_issue","Date of issue")),1,1);
          rightHeader.add(getSmallText(":"),1,1);
          rightHeader.add(getSmallText(idegaTimestamp.RightNow().getLocaleDate(_iwc)),1,2);
          rightHeader.setAlignment(1,1,"right");
          rightHeader.setAlignment(1,2,"right");
        table.add(rightHeader,3,1);
        table.setAlignment(3,1,"right");

        table.add(Text.BREAK,1,2);
        table.add(Text.BREAK,1,2);

        table.add(getText(_iwrb.getLocalizedString("travel.to_lg","TO :")),1,2);
        table.add(getText(_supplier.getName()),1,2);
        table.add(Text.BREAK,1,2);

        table.add(getText(_iwrb.getLocalizedString("travel.address_lg","ADDRESS :")),1,2);
        Address address = _supplier.getAddress();
        table.add(getText(address.getStreetName()),1,2);
        table.add(Text.BREAK,1,2);

        table.add(Text.BREAK,1,2);

        table.add(getText(_iwrb.getLocalizedString("travel.this_order_to_be_accepted","This order to be accepted at amount shown as part or full payment for the following services")),1,2);
        table.add(Text.BREAK,1,2);

        table.add(Text.BREAK,1,2);
        table.add(getText(ProductBusiness.getProductName(_product)),1,2);
        table.add(Text.BREAK,1,2);
        table.add(getText(new idegaTimestamp(_booking.getBookingDate()).getLocaleDate(_iwc)),1,2);
        table.add(getText(" "+_iwrb.getLocalizedString("travel.at","at"))+" ",1,2);
        table.add(getText(new idegaTimestamp(_service.getDepartureTime()).toSQLTimeString().substring(0,5)),1,2);
        table.add(Text.BREAK,1,2);

        table.add(Text.BREAK,1,2);
        table.add(Text.BREAK,1,2);

        table.add(getText(_iwrb.getLocalizedString("travel.client_name_lg","CLIENT NAME")),1,2);
        table.add(getText(" : "),1,2);
        table.add(getText(_booking.getName()),1,2);
        table.add(Text.BREAK,1,2);

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
        table.add(getText(df.format(Booker.getBookingPrice(iwc, _booking))),1,2);
        table.add(getText(" "),1,2);
        Currency currency = Booker.getCurrency(_booking);
        if (currency != null)
        table.add(getText(currency.getCurrencyAbbreviation()),1,2);
        table.add(Text.BREAK,1,2);


        table.add(Text.BREAK,1,2);

      }catch (SQLException sql) {
        table.add(getText(_iwrb.getLocalizedString("travel.voucher_error","Voucher could not be created, please write down your reference number")),1,2);
        table.add(Text.BREAK,1,2);
        table.add(getSmallText(_iwrb.getLocalizedString("travel.reference_number_show","Reference nr. ")),1,2);
        table.add(getSmallText(_booking.getReferenceNumber()),1,2);
      }
    }


    return bigTable;
  }

}