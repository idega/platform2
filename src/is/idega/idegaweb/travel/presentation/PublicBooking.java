package is.idega.idegaweb.travel.presentation;

import com.idega.block.tpos.data.TPosMerchant;
import javax.ejb.FinderException;
import java.rmi.RemoteException;
import com.idega.business.IBOLookup;
import javax.mail.MessagingException;
import com.idega.core.data.Email;
import com.idega.data.IDOLookup;
import com.idega.transaction.IdegaTransactionManager;
import javax.transaction.TransactionManager;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.idegaweb.*;
import com.idega.util.*;
import com.idega.util.text.*;
import is.idega.idegaweb.travel.business.*;
import java.text.DecimalFormat;
import java.util.*;
import com.idega.block.calendar.business.CalendarBusiness;
import com.idega.core.data.Address;
import com.idega.block.trade.stockroom.data.*;
import com.idega.block.trade.stockroom.business.*;
import is.idega.idegaweb.travel.data.*;
import com.idega.block.trade.data.Currency;

import is.idega.idegaweb.travel.service.presentation.BookingForm;
import is.idega.idegaweb.travel.service.business.ServiceHandler;

import com.idega.block.trade.stockroom.business.ProductPriceException;
import com.idega.block.tpos.presentation.*;
import java.sql.SQLException;
/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class PublicBooking extends Block  {

  public static String IW_BUNDLE_IDENTIFIER="is.idega.travel";
  IWResourceBundle iwrb;
  IWBundle bundle;
  Product product;
//  Service service;
//  Timeframe[] timeframes;
  Supplier supplier;
  int productId = -1;

  private IWTimestamp stamp;
  private String parameterProductId = LinkGenerator.parameterProductId;
  private DecimalFormat df = new DecimalFormat("0.00");
  private Text text = new Text("");
  private Text boldText = new Text("");
  private String backgroundColor = "#1A4B8E";

  private String sAction = "publicBookingAction";
  private String parameterSubmitBooking = "publicBookingSubmitBooking";
  public static String parameterBookingVerified = "publicBookingBookingVerified";


  public PublicBooking() {
  }

  public void main(IWContext iwc)throws Exception {
    init(iwc);

    if (productId != -1 ) {
      displayForm(iwc);
    }else {
    }
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  private void init(IWContext iwc) throws RemoteException{
    bundle = getBundle(iwc);
    iwrb = bundle.getResourceBundle(iwc.getCurrentLocale());
    super.getParentPage().setExpiryDate("Tue, 20 Aug 1996 14:25:27 GMT");
    iwc.getResponse().addHeader("Expires","Tue, 20 Aug 1996 14:25:27 GMT");

    String year = iwc.getParameter(CalendarBusiness.PARAMETER_YEAR);
    String month = iwc.getParameter(CalendarBusiness.PARAMETER_MONTH);
    String day = iwc.getParameter(CalendarBusiness.PARAMETER_DAY);
	  stamp = new IWTimestamp(IWTimestamp.RightNow());
	  if (year != null) {
	  	stamp.setYear(Integer.parseInt(year));	
	  }
    if (month != null) {
    	stamp.setMonth(Integer.parseInt(month));	
    }
    if (day != null) {
    	stamp.setDay(Integer.parseInt(day));	
    }

    String sProductId = iwc.getParameter(this.parameterProductId);
    if (sProductId != null) {
      try {

        productId = Integer.parseInt(sProductId);
        product = getProductBusiness(iwc).getProduct(productId);
        if (!product.getIsValid()) {
          throw new SQLException("Product not valid");
        }
        supplier = ((com.idega.block.trade.stockroom.data.SupplierHome)com.idega.data.IDOLookup.getHomeLegacy(Supplier.class)).findByPrimaryKeyLegacy(product.getSupplierId());

      }catch (SQLException s) {
        s.printStackTrace(System.err);
      }catch (FinderException f) {
        f.printStackTrace(System.err);
      }
    }
    boldText.setFontStyle("font-face: Verdana, Helvetica, sans-serif; font-size: "+Text.FONT_SIZE_10_STYLE_TAG+"; font-weight: bold;");
      boldText.setFontColor("#000000");
    text.setFontStyle("font-face: Verdana, Helvetica, sans-serif; font-size: "+Text.FONT_SIZE_10_STYLE_TAG+";");
      text.setFontColor("#000000");
  }

  private Text getText(String content) {
    Text temp = (Text) text.clone();
      temp.setText(content);
    return temp;
  }
  private Text getBoldText(String content) {
    Text temp = (Text) boldText.clone();
      temp.setText(content);
    return temp;
  }
  private Text getTextWhite(String content) {
    Text temp = (Text) text.clone();
      temp.setText(content);
      temp.setFontColor("#FFFFFF");
    return temp;
  }
  private Text getBoldTextWhite(String content) {
    Text temp = (Text) boldText.clone();
      temp.setText(content);
      temp.setFontColor("#FFFFFF");
    return temp;
  }


  private void displayForm(IWContext iwc) throws RemoteException, FinderException{
      Table table = new Table(2,4);
        table.setWidth("90%");
        table.setAlignment("center");
        table.setCellspacing(1);
        table.setColor(TravelManager.WHITE);
        table.setBorder(0);

      Image background = bundle.getImage("images/sb_background.gif");
      Image seeAndBuy = iwrb.getImage("images/see_and_buy.gif");

      table.setBackgroundImage(1, 3, background);
      table.setColor(1, 4, backgroundColor);
      table.setColor(2, 2, "#CCCCCC");
      table.setColor(2, 3, "#CCCCCC");
      table.setColor(2, 4, "#CCCCCC");
      table.setAlignment(1,1,"left");
      table.setVerticalAlignment(1,2,"top");
      table.setVerticalAlignment(1,3,"top");
      table.setVerticalAlignment(2,2,"top");
      table.setVerticalAlignment(2,3,"top");
      table.setAlignment(2,2,"center");
      table.setAlignment(2,3,"center");
      table.setHeight(2, "20");
      table.setHeight(4, "20");
      table.setWidth(2, "20");


      //table.add(seeAndBuy,1,1);
      table.add(leftTop(iwc),1,2);
      table.add(leftBottom(iwc),1,3);
      table.add(rightTop(iwc),2,2);
      table.add(rightBottom(iwc),2,3);
      Image image = bundle.getImage("verisignseals/verisign_logo.gif");
        image.setWidth(100);
        image.setHeight(42);
      Link verisign = new Link(image, "https://digitalid.verisign.com/as2/3537e1357d56f9db899a65d84e97d2c9");
        verisign.setTarget(Link.TARGET_NEW_WINDOW);
      table.add(verisign, 2,4);
      table.setAlignment(2,4, "center");
      table.setVerticalAlignment(2, 4, "bottom");

      add(table);
  }


  private Table rightTop(IWContext iwc) {
    Table table = new Table(1,3);
      table.setVerticalAlignment(1,1,"top");
      table.setVerticalAlignment(1,2,"top");
      table.setVerticalAlignment(1,3,"top");
      table.setAlignment(1,1,"center");
      table.setAlignment(1,2,"center");
      table.setAlignment(1,3,"center");

    Image arrow = bundle.getImage("images/white_arrow.gif");
    Image bookNow = iwrb.getImage("images/day_requested.gif");
    Text checkAvail = getBoldText(iwrb.getLocalizedString("travel.check_availability","Check availability and select date by the calendar below"));

    table.add(bookNow,1,1);
    table.add(checkAvail,1,2);
    table.add(arrow,1,3);

    return table;
  }

  private Table rightBottom(IWContext iwc)  {
    Table table = new Table(1,2);
      table.setAlignment(1,1,"center");
      table.setVerticalAlignment(1,1,"top");

    try {
      CalendarHandler ch = new CalendarHandler(iwc);
        ch.setProduct(product);
        ch.setBackgroundColor("#CCCCCC");
        ch.setInActiveCellColor("#666666");
        ch.setFontColor("#000000");
        ch.setTodayColor("#99CCFF");
        ch.setFullyBookedColor("#CC3333");
        ch.setAvailableDayColor("#FFFFFF");
        ch.addParameterToLink(this.parameterProductId, productId);
        ch.setClassToLinkTo(PublicBooking.class);
        ch.setTimestamp(stamp);
        ch.showInquiries(false);
        ch.sm.T.setBorder(0);
        ch.sm.T.setCellspacing(2);
      table.add(ch.getCalendarTable(iwc),1,1);

    }catch (Exception e) {
      e.printStackTrace(System.err);
    }

    return table;
  }

  private Table leftTop(IWContext iwc) throws RemoteException, FinderException{
    is.idega.idegaweb.travel.service.presentation.ServiceOverview so = getServiceHandler(iwc).getServiceOverview(iwc, product);
    return so.getPublicServiceInfoTable(iwc, product);
//    return getPublicServiceInfoTable(iwc);
  }


  private Form leftBottom(IWContext iwc) {
    try {
      BookingForm bf = getServiceHandler(iwc).getBookingForm(iwc, product);
//      TourBookingForm tbf = new TourBookingForm(iwc, product);
      CalendarHandler ch  = new CalendarHandler(iwc);
        ch.setProduct(product);

      boolean legalDay = bf.getIsDayVisible(iwc);
      boolean fullyBooked = bf.isFullyBooked( iwc, product, stamp);
//      legalDay = getTravelStockroomBusiness(iwc).getIfDay(iwc, product, stamp);

      Form form = new Form();


      if (legalDay && !fullyBooked) {
        String action = iwc.getParameter(BookingForm.sAction);
//        String tbfAction = iwc.getParameter(BookingForm.sAction);
//        if (tbfAction == null || !tbfAction.equals(BookingForm.parameterSaveBooking)) {//
//	        System.out.println("action a = '"+action+"'");
//          action = "";
//        }
        if (action == null || action.equals("")) {
            form = bf.getPublicBookingForm(iwc, product);
            form.maintainParameter(this.parameterProductId);
//            form.setOnSubmit("this.form."+BookingForm.sAction+".value = \""+BookingForm.parameterSaveBooking+"\"");
//            form.addParameter(BookingForm.sAction,BookingForm.parameterSaveBooking);
        }else if (action.equals(BookingForm.parameterSaveBooking)) {
            form = bf.getFormMaintainingAllParameters(true, false);
            form.maintainParameter(this.parameterProductId);
//            form.addParameter( BookingForm.sAction, this.parameterBookingVerified);
            form.add(bf.getVerifyBookingTable(iwc, product));
        }else if (action.equals(this.parameterBookingVerified)) {
            form = bf.getFormMaintainingAllParameters(true, false);
            form.maintainParameter(this.parameterProductId);
            form.add(doBooking(iwc));
        }
      }else {
          form.add(getNoSeatsAvailable(iwc));
      }

      return form;
    }catch (Exception e) {
      e.printStackTrace(System.err);
      return new Form();
    }
  }


  public Table getNoSeatsAvailable(IWContext iwc) {
    Table table = new Table();
      table.setCellpadding(0);
      table.setCellspacing(6);

          Text notAvailSeats = new Text();
            notAvailSeats.setFontStyle(TravelManager.theTextStyle);
            notAvailSeats.setFontColor(TravelManager.WHITE);
            notAvailSeats.setText(iwrb.getLocalizedString("travel.there_are_no_available_seats ","There are no available seats "));

          Text dateText = new Text();
            dateText.setFontStyle(TravelManager.theBoldTextStyle);
            dateText.setFontColor(TravelManager.WHITE);
            dateText.setText(getLocaleDate(stamp));
            dateText.addToText("."+Text.NON_BREAKING_SPACE);

          Text pleaseFindAnotherDay = new Text();
            pleaseFindAnotherDay.setFontStyle(TravelManager.theTextStyle);
            pleaseFindAnotherDay.setFontColor(TravelManager.WHITE);
            pleaseFindAnotherDay.setText(iwrb.getLocalizedString("travel.please_find_another_day","Please find another day"));

      table.add(notAvailSeats);
      table.add(dateText);
      table.add(pleaseFindAnotherDay);

    return table;
  }

  private Table doBooking(IWContext iwc) throws RemoteException{
    Table table = new Table();
      String ccNumber = iwc.getParameter(BookingForm.parameterCCNumber);
      String ccMonth  = iwc.getParameter(BookingForm.parameterCCMonth);
      String ccYear   = iwc.getParameter(BookingForm.parameterCCYear);

      Text display = getBoldTextWhite("");
      boolean success = false;
      boolean inquirySent = false;
      String heimild = "";

      com.idega.block.tpos.business.TPosClient t = null;
      GeneralBooking  gBooking = null;

      TransactionManager tm = IdegaTransactionManager.getInstance();
      try {
		    BookingForm bf = getServiceHandler(iwc).getBookingForm(iwc, product);
        tm.begin();

				float price = bf.getOrderPrice(iwc, product, stamp);

//        TourBookingForm tbf = new TourBookingForm(iwc,product);
        int bookingId = bf.saveBooking(iwc); // WAS handleInsert(iwc), changed 14.10.2002, because Booking has already been checked, and verified
        gBooking = ((is.idega.idegaweb.travel.data.GeneralBookingHome)com.idega.data.IDOLookup.getHome(GeneralBooking.class)).findByPrimaryKey(new Integer(bookingId));

        if (bookingId == BookingForm.inquirySent) {
          inquirySent = true;
          tm.commit();
        }else {
           try {
            System.out.println("Starting TPOS test : "+IWTimestamp.RightNow().toString());
            TPosMerchant merchant = null;
            try {
              int productSupplierId = gBooking.getService().getProduct().getSupplierId();
              Supplier suppTemp = ((SupplierHome) IDOLookup.getHomeLegacy(Supplier.class)).findByPrimaryKeyLegacy(productSupplierId);
              merchant = suppTemp.getTPosMerchant();
              System.out.println("TPosMerchant found");
            }catch (Exception e) {
              System.out.println("TPosMerchant NOT found for supplier, using system default");
            }
            if (merchant == null) {
              t = new com.idega.block.tpos.business.TPosClient(iwc);
            }else {
              t = new com.idega.block.tpos.business.TPosClient(iwc, merchant);
            }
            heimild = t.doSale(ccNumber,ccMonth,ccYear,price,"ISK");
            //System.out.println("heimild = " + heimild);
            System.out.println("Ending TPOS test : "+IWTimestamp.RightNow().toString());
          }catch(com.idega.block.tpos.business.TPosException e) {
            System.out.println("TPOS errormessage = " + e.getErrorMessage());
            System.out.println("number = " + e.getErrorNumber());
            System.out.println("display = " + e.getDisplayError());
            int number = Integer.parseInt(e.getErrorNumber());
            switch (number) {
              case 6:
              case 12:
              case 19:
                display.setText(iwrb.getLocalizedString("travel.creditcard_number_incorrect","Creditcard number incorrect"));
                break;
              case 10:
              case 22:
              case 74:
                display.setText(iwrb.getLocalizedString("travel.creditcard_type_not_accepted","Creditcard type not accepted"));
                break;
              case 17:
              case 18:
                display.setText(iwrb.getLocalizedString("travel.creditcard_is_expired","Creditcard is expired"));
                break;
              case 48:
              case 49:
              case 50:
              case 51:
              case 56:
              case 57:
              case 76:
              case 79:
              case 2002:
              case 2010:
                display.setText(iwrb.getLocalizedString("travel.cannot_connect_to_cps","Could not connect to Central Payment Server"));
                break;
              case 7:
              case 37:
              case 69:
              case 75:
                display.setText(iwrb.getLocalizedString("travel.creditcard_autorization_failed","Authorization failed"));
                break;
              /*case 69:
                display.setText(e.getErrorMessage());
                break;*/
              case 20:
              case 31:
                display.setText(iwrb.getLocalizedString("travel.transaction_not_permitted","Transaction not permitted"));
                break;
              case 99999:
                display.setText(iwrb.getLocalizedString("travel.booking_was_not_confirmed_try_again_later","Booking was not confirmed. Please try again later"));
                break;
              default:
                display.setText(iwrb.getLocalizedString("travel.cannot_connect","Cannot communicate with server"));
                break;
            }

            throw e;
          }

          gBooking.setCreditcardAuthorizationNumber(heimild);
          gBooking.store();

          debug("commiting");
          tm.commit();
          success = true;
        }

      }catch(com.idega.block.tpos.business.TPosException e) {
        display.addToText(" ( "+e.getMessage()+" )");
        //e.printStackTrace(System.err);
        gBooking.setIsValid(false);
        gBooking.store();
        try {
          tm.commit();
        }catch(Exception ex) {
          debug("commit failed");
          ex.printStackTrace(System.err);
          try {
            tm.rollback();
          }catch (javax.transaction.SystemException se) {
            se.printStackTrace(System.err);
          }
        }

        gBooking = null;
        success = false;
      }catch (Exception e) {
        display.addToText(" ( "+e.getMessage()+" )");
        e.printStackTrace(System.err);
        try {
          tm.rollback();
        }catch (javax.transaction.SystemException se) {
          se.printStackTrace(System.err);
        }
      }

      if (success && gBooking != null) {
        boolean sendEmail = false;
        try {
          ProductHome pHome = (ProductHome)com.idega.data.IDOLookup.getHome(Product.class);
          Product prod = pHome.findByPrimaryKey(new Integer(gBooking.getServiceID()));
          Supplier suppl = ((SupplierHome) IDOLookup.getHomeLegacy(Supplier.class)).findByPrimaryKeyLegacy(prod.getSupplierId());
          Settings settings = suppl.getSettings();
          Email sEmail = suppl.getEmail();
          String suppEmail = "";
          if (sEmail != null) {
            suppEmail = sEmail.getEmailAddress();
          }
          String bookEmail = gBooking.getEmail();
          boolean doubleSendSuccessful = false;

          if (settings.getIfDoubleConfirmation()) {
            try {
              sendEmail = true;
              StringBuffer mailText = new StringBuffer();
              mailText.append(iwrb.getLocalizedString("travel.email_double_confirmation","This email is to confirm that your booking has been received, and confirmed."));
              mailText.append("\n").append(iwrb.getLocalizedString("travel.name",   "Name    ")).append(" : ").append(gBooking.getName());
              mailText.append("\n").append(iwrb.getLocalizedString("travel.service","Service ")).append(" : ").append(getProductBusiness(iwc).getProductNameWithNumber(prod, true, iwc.getCurrentLocaleId()));
              mailText.append("\n").append(iwrb.getLocalizedString("travel.date",   "Date    ")).append(" : ").append(getLocaleDate(new IWTimestamp(gBooking.getBookingDate())));
              mailText.append("\n").append(iwrb.getLocalizedString("travel.seats",  "Seats   ")).append(" : ").append(gBooking.getTotalCount());

              SendMail sm = new SendMail();
                sm.send(suppEmail, bookEmail, "", "", "mail.idega.is", "Booking",mailText.toString());
              doubleSendSuccessful = true;
            }catch (MessagingException me) {
              doubleSendSuccessful = false;
              me.printStackTrace(System.err);
            }
          }

          if (settings.getIfEmailAfterOnlineBooking()) {
            try {
              String subject = "Booking";

              StringBuffer mailText = new StringBuffer();
              mailText.append(iwrb.getLocalizedString("travel.email_after_online_booking","You have just received a booking through nat.sidan.is."));
              mailText.append("\n").append(iwrb.getLocalizedString("travel.name",   "Name    ")).append(" : ").append(gBooking.getName());
              mailText.append("\n").append(iwrb.getLocalizedString("travel.service","Service ")).append(" : ").append(getProductBusiness(iwc).getProductNameWithNumber(prod, true, iwc.getCurrentLocaleId()));
              mailText.append("\n").append(iwrb.getLocalizedString("travel.date",   "Date    ")).append(" : ").append(getLocaleDate(new IWTimestamp(gBooking.getBookingDate())));
              mailText.append("\n").append(iwrb.getLocalizedString("travel.seats",  "Seats   ")).append(" : ").append(gBooking.getTotalCount());
              if (doubleSendSuccessful) {
                mailText.append("\n\n").append(iwrb.getLocalizedString("travel.double_confirmation_has_been_sent","Double confirmation has been sent."));
              }else {
                mailText.append("\n\n").append(iwrb.getLocalizedString("travel.double_confirmation_has_not_been_sent","Double confirmation has NOT been sent."));
                mailText.append("\n").append("   - ").append(iwrb.getLocalizedString("travel.email_was_probably_incorrect","E-mail was probably incorrect."));
                subject = "Booking - double confirmation failed!";
              }


              SendMail sm = new SendMail();
                sm.send(suppEmail, suppEmail, "", "", "mail.idega.is", subject,mailText.toString());
            }catch (MessagingException me) {
              me.printStackTrace(System.err);
            }
          }

        }catch (Exception e) {
          e.printStackTrace(System.err);
        }

        table.add(getBoldTextWhite(gBooking.getName()));
        table.add(getBoldTextWhite(", "));
        table.add(getBoldTextWhite(iwrb.getLocalizedString("travel.you_booking_has_been_confirmed","your booking has been confirmed.")));
        table.add(Text.BREAK);
        table.add(Text.BREAK);
        if (sendEmail) {
          table.add(getBoldTextWhite(iwrb.getLocalizedString("travel.you_will_reveice_an_email_shortly","You will receive an email shortly confirming your booking.")));
          table.add(Text.BREAK);
          table.add(Text.BREAK);
        }
        table.add(getBoldTextWhite(iwrb.getLocalizedString("travel.your_credidcard_authorization_number_is","Your creditcard authorization number is")));
        table.add(getBoldTextWhite(" : "));
        table.add(getBoldTextWhite(heimild));
        table.add(Text.BREAK);
        table.add(getBoldTextWhite(iwrb.getLocalizedString("travel.your_reference_number_is","Your reference number is")));
        table.add(getBoldTextWhite(" : "));
        table.add(getBoldTextWhite(gBooking.getReferenceNumber()));
        table.add(Text.BREAK);
        //table.add(getBoldTextWhite(gBooking.getReferenceNumber()));
        //table.add(Text.BREAK);
        table.add(Text.BREAK);
        table.add(getBoldTextWhite(iwrb.getLocalizedString("travel.if_unable_to_print","If you are unable to print the voucher, write the reference number down else proceed to printing the voucher.")));



        Link printVoucher = new Link(getBoldTextWhite(iwrb.getLocalizedString("travel.print_voucher","Print voucher")));
          printVoucher.addParameter(VoucherWindow.parameterBookingId, gBooking.getID());
          printVoucher.setWindowToOpen(VoucherWindow.class);

        if (t != null) {
          com.idega.block.tpos.presentation.Receipt r = new com.idega.block.tpos.presentation.Receipt(t, supplier);
          iwc.setSessionAttribute(ReceiptWindow.RECEIPT_SESSION_NAME, r);

          Link printCCReceipt = new Link(getBoldTextWhite(iwrb.getLocalizedString("travel.print_cc_receipt","Print creditcard receipt")));
            printCCReceipt.setWindowToOpen(ReceiptWindow.class);
          table.add(Text.NON_BREAKING_SPACE+Text.NON_BREAKING_SPACE, 1,2);
          table.add(printCCReceipt, 1, 2);
        }

        table.add(printVoucher,1,3);
        table.setAlignment(1,1,"left");
        table.setAlignment(1,2,"right");
        table.setAlignment(1,3,"right");
      }else if (inquirySent) {
        table.add(getBoldTextWhite(iwrb.getLocalizedString("travel.inquiry_has_been_sent","Inquiry has been sent")));
        table.add(Text.BREAK);
        table.add(getBoldTextWhite(iwrb.getLocalizedString("travel.you_will_reveice_an_confirmation_email_shortly","You will receive an confirmation email shortly.")));
      }else {
        table.add(display);
        if (gBooking == null) {
          debug("gBooking == null");
        }
      }

    return table;
  }

  protected String getLocaleDate(IWTimestamp stamp) {
    return  (new IWCalendar(stamp)).getLocaleDate();
  }

  protected TravelStockroomBusiness getTravelStockroomBusiness(IWApplicationContext iwac) throws RemoteException {
    return (TravelStockroomBusiness) IBOLookup.getServiceInstance(iwac, TravelStockroomBusiness.class);
  }
  protected ServiceHandler getServiceHandler(IWApplicationContext iwac) throws RemoteException {
    return (ServiceHandler) IBOLookup.getServiceInstance(iwac, ServiceHandler.class);
  }

  protected ProductBusiness getProductBusiness(IWApplicationContext iwac) throws RemoteException {
    return (ProductBusiness) IBOLookup.getServiceInstance(iwac, ProductBusiness.class);
  }

}
