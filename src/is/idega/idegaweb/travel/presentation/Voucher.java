package is.idega.idegaweb.travel.presentation;

import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.idegaweb.*;
import is.idega.idegaweb.travel.interfaces.Booking;
import com.idega.block.trade.stockroom.data.*;
import com.idega.util.idegaTimestamp;
import is.idega.idegaweb.travel.data.*;

import java.sql.SQLException;
/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class Voucher extends TravelManager {

  private IWContext _iwc;
  private IWResourceBundle _iwrb;
  private IWBundle _bundle;

  private is.idega.idegaweb.travel.interfaces.Booking _booking;
  private Service _service;
  private Product _product;
  private Supplier _supplier;

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
      _supplier = new Supplier(_product.getSupplierId());
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

  private Text getBoldText(String content) {
    Text text = getText(content);
      text.setBold();
    return text;
  }

  public Table getVoucher() {
    Table bigTable = new Table();
      bigTable.setColor(BLACK);
      bigTable.setCellspacing(1);
      bigTable.setCellpadding(0);
      bigTable.setWidth(580);

    Table table = new Table(3,2);
      bigTable.add(table);
      table.setColor(WHITE);
      table.setWidth("100%");
      table.setWidth(1,"33%");
      table.setWidth(2,"34%");
      table.setWidth(3,"33%");
      table.setBorder(1);
      table.mergeCells(1,2,3,2);

    if (_booking == null) {
      table.add(_iwrb.getLocalizedString("travel.no_booking_specified","No booking specified"),2,1);
    }else {
      Table leftHeader = new Table();
        leftHeader.add(getSmallText(_iwrb.getLocalizedString("travel.NR","NR")),1,1);
        leftHeader.add(getSmallText(":"),1,1);
        leftHeader.add(getSmallText(_iwrb.getLocalizedString("travel.reference_number_show","Reference nr. ")),1,2);
        leftHeader.add(getSmallText(_booking.getReferenceNumber()),1,2);
      table.add(leftHeader,1,1);
      table.setAlignment(1,1,"left");

      Text voucherText = getBigText(_iwrb.getLocalizedString("travel.voucher","Voucher"));
      table.add(voucherText,2,1);
      table.setAlignment(2,1,"center");

      Table rightHeader = new Table();
        rightHeader.add(getSmallText(_iwrb.getLocalizedString("travel.date_of_issue","Date of issue")),1,1);
        rightHeader.add(getSmallText(":"),1,1);
        rightHeader.add(getSmallText(idegaTimestamp.RightNow().getLocaleDate(_iwc)),1,2);
        rightHeader.setAlignment(1,1,"right");
        rightHeader.setAlignment(1,2,"right");
      table.add(rightHeader,3,1);
      table.setAlignment(3,1,"right");



    }


    return bigTable;
  }

}