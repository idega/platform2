package is.idega.idegaweb.travel.presentation;

import is.idega.idegaweb.travel.data.GeneralBooking;
import is.idega.idegaweb.travel.service.presentation.BookingForm;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

import javax.ejb.FinderException;
import javax.mail.MessagingException;
import javax.transaction.TransactionManager;

import com.idega.block.creditcard.business.CreditCardBusiness;
import com.idega.block.creditcard.data.CreditCardAuthorizationEntry;
import com.idega.block.creditcard.presentation.Receipt;
import com.idega.block.creditcard.presentation.ReceiptWindow;
import com.idega.block.trade.stockroom.business.ProductBusiness;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.ProductHome;
import com.idega.block.trade.stockroom.data.Settings;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.block.trade.stockroom.data.SupplierHome;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.CalendarParameters;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.transaction.IdegaTransactionManager;
import com.idega.util.IWCalendar;
import com.idega.util.IWTimestamp;
import com.idega.util.SendMail;
/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class PublicBooking extends TravelBlock  {

  public static String IW_BUNDLE_IDENTIFIER="is.idega.travel";
  
  IWResourceBundle iwrb;
  IWBundle bundle;
  Product product;
//  Service service;
//  Timeframe[] timeframes;
  Supplier supplier;
  int productId = -1;

	public static String PARAMETER_REFERRAL_URL = "pb_spm_ru";
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
  		super.main(iwc);
    init(iwc);

    if (productId != -1 ) {
      displayForm(iwc);
    }else {
    }
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

	public static String getRefererUrl(IWContext iwc) {
		String tmpUrl = iwc.getParameter(PARAMETER_REFERRAL_URL);
		if (tmpUrl == null){
			tmpUrl = (String) iwc.getSessionAttribute(PARAMETER_REFERRAL_URL);
			if (tmpUrl == null) {
				tmpUrl = iwc.getReferer();
				System.out.println("URI : "+iwc.getRequest().getServerName());
				if (tmpUrl != null && (tmpUrl.indexOf(iwc.getRequest().getServerName()) > -1)) {
					return null;
				} 
			}
		}
		iwc.setSessionAttribute(PARAMETER_REFERRAL_URL, tmpUrl);
		return tmpUrl;
	}


  private void init(IWContext iwc) throws RemoteException{
  	System.out.println("ReferalUrl = "+getRefererUrl(iwc));
    bundle = getBundle(iwc);
    iwrb = bundle.getResourceBundle(iwc.getCurrentLocale());
    super.getParentPage().setExpiryDate("Tue, 20 Aug 1996 14:25:27 GMT");
    iwc.getResponse().addHeader("Expires","Tue, 20 Aug 1996 14:25:27 GMT");

    String year = iwc.getParameter(CalendarParameters.PARAMETER_YEAR);
    String month = iwc.getParameter(CalendarParameters.PARAMETER_MONTH);
    String day = iwc.getParameter(CalendarParameters.PARAMETER_DAY);
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
      table.add(getTermsAndConditions(), 1, 4);
      Image image = bundle.getImage("verisignseals/verisign_logo.gif");
        image.setWidth(100);
        image.setHeight(42);
			String verisignUrl = bundle.getProperty("verisign_url");
			if (verisignUrl == null) { 
				verisignUrl = "https://digitalid.verisign.com/as2/a83d13ff1653ab8baf084d646faab5c9";
			}
			
			Link verisign = new Link(image, verisignUrl);
        verisign.setTarget(Link.TARGET_NEW_WINDOW);
        verisign.setOutgoing(true);
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
        ch.addParameterToLink(PARAMETER_REFERRAL_URL, getRefererUrl(iwc));
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
    	Form form = new Form();
    	form.maintainParameter(PARAMETER_REFERRAL_URL);
    	if (product.getIsValid() ) {
	      BookingForm bf = getServiceHandler(iwc).getBookingForm(iwc, product);
	//      TourBookingForm tbf = new TourBookingForm(iwc, product);
	      CalendarHandler ch  = new CalendarHandler(iwc);
	        ch.setProduct(product);
	
	      boolean legalDay = bf.getIsDayVisible(iwc);
	      boolean fullyBooked = bf.isFullyBooked( iwc, product, stamp);
	//      legalDay = getTravelStockroomBusiness(iwc).getIfDay(iwc, product, stamp);
	
	
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
	            form = bf.getFormMaintainingAllParameters(iwc, true, false);
	            form.maintainParameter(this.parameterProductId);
	//            form.addParameter( BookingForm.sAction, this.parameterBookingVerified);
	            form.add(bf.getVerifyBookingTable(iwc, product));
	        }else if (action.equals(this.parameterBookingVerified)) {
	            form = bf.getFormMaintainingAllParameters(iwc, true, false);
	            form.maintainParameter(this.parameterProductId);
	            form.add(doBooking(iwc));
	        }
	      }else {
	          form.add(getNoSeatsAvailable(iwc));
	      }
    	} else {
    		form.add(getDisabledProduct(iwc));
    	}

      return form;
    }catch (Exception e) {
      e.printStackTrace(System.err);
      return new Form();
    }
  }


  public Table getDisabledProduct(IWContext iwc) {
  	Table table = new Table();
  	table.setCellpadding(0);
  	table.setCellspacing(6);

  	Text notAvailSeats = new Text();
  	notAvailSeats.setFontStyle(TravelManager.theTextStyle);
  	notAvailSeats.setFontColor(TravelManager.WHITE);
  	notAvailSeats.setText(iwrb.getLocalizedString("travel.product_is_disabled ","This product has been disabled."));

  	table.add(notAvailSeats);

  	return table;
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

//      com.idega.block.tpos.business.TPosClient t = null;
      GeneralBooking  gBooking = null;
			BookingForm bf = null; 
			try {
				bf = getServiceHandler(iwc).getBookingForm(iwc, product);
			}catch (Exception e) {
				e.printStackTrace(System.out);	
			}

      TransactionManager tm = IdegaTransactionManager.getInstance();
      try {
        tm.begin();

//				float price = bf.getOrderPrice(iwc, product, stamp);

//        TourBookingForm tbf = new TourBookingForm(iwc,product);
//        int bookingId = bf.saveBooking(iwc); // WAS handleInsert(iwc), changed 14.10.2002, because Booking has already been checked, and verified
        int bookingId = bf.handleInsert(iwc); // WAS handleInsert(iwc), changed 14.10.2002, because Booking has already been checked, and verified

        if (bookingId == BookingForm.inquirySent) {
          inquirySent = true;
          tm.commit();
        }else {
          gBooking = ((is.idega.idegaweb.travel.data.GeneralBookingHome)com.idega.data.IDOLookup.getHome(GeneralBooking.class)).findByPrimaryKey(new Integer(bookingId));
          gBooking.setRefererUrl(getRefererUrl(iwc));
          gBooking.store();
          tm.commit();
          success = true;
        }

      }catch(com.idega.block.creditcard.business.TPosException e) {
      	if (!e.getMessage().equals("")) {
	        display.addToText(iwrb.getLocalizedString("travel.booking_failed","Booking failed")+" ( "+e.getMessage()+" )");
      	}
        //e.printStackTrace(System.err);
//        gBooking.setIsValid(false);
//        gBooking.store();
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
        gBooking = null;
        try {
          tm.rollback();
        }catch (javax.transaction.SystemException se) {
          se.printStackTrace(System.err);
        }
      }

      if (gBooking != null) {
        boolean sendEmail = sendEmails(iwc, gBooking, iwrb);

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
        table.add(getBoldTextWhite(gBooking.getCreditcardAuthorizationNumber()));
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

        try {
        CreditCardAuthorizationEntry entry = this.getCreditCardBusiness(iwc).getAuthorizationEntry(supplier, gBooking.getCreditcardAuthorizationNumber(),  new IWTimestamp(gBooking.getDateOfBooking()));
	        if (entry != null) {
	          Receipt r = new Receipt(entry, supplier);
	          iwc.setSessionAttribute(ReceiptWindow.RECEIPT_SESSION_NAME, r);
	
	          Link printCCReceipt = new Link(getBoldTextWhite(iwrb.getLocalizedString("travel.print_cc_receipt","Print creditcard receipt")));
	            printCCReceipt.setWindowToOpen(ReceiptWindow.class);
	          table.add(Text.NON_BREAKING_SPACE+Text.NON_BREAKING_SPACE, 1,2);
	          table.add(printCCReceipt, 1, 2);
	        }
        } catch (Exception e) {
        		e.printStackTrace(System.err);
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




  public static boolean sendEmails(IWContext iwc, GeneralBooking gBooking,IWResourceBundle iwrb) {
	boolean sendEmail = false;
	try {
	  DecimalFormat df = new DecimalFormat("0.00");

	  ProductHome pHome = (ProductHome)com.idega.data.IDOLookup.getHome(Product.class);
	  Product prod = pHome.findByPrimaryKey(new Integer(gBooking.getServiceID()));
    ProductBusiness pBus =  (ProductBusiness) IBOLookup.getServiceInstance(iwc, ProductBusiness.class);
    CreditCardBusiness ccBus = (CreditCardBusiness) IBOLookup.getServiceInstance(iwc, CreditCardBusiness.class);

	  Supplier suppl = ((SupplierHome) IDOLookup.getHomeLegacy(Supplier.class)).findByPrimaryKeyLegacy(prod.getSupplierId());
	  List addresses = suppl.getAddresses();
	  List phones = suppl.getPhones();
	  Settings settings = suppl.getSettings();
	  Email sEmail = suppl.getEmail();
	  String suppEmail = "";
	  if (sEmail != null) {
	    suppEmail = sEmail.getEmailAddress();
	  }
	  String bookEmail = gBooking.getEmail();
	  boolean doubleSendSuccessful = false;
	  IWBundle bundle = iwrb.getIWBundleParent();
	
	  if (settings.getIfDoubleConfirmation()) {
	    try {
	      sendEmail = true;
	      StringBuffer mailText = new StringBuffer();
	      mailText.append(iwrb.getLocalizedString("travel.email_double_confirmation","This email is to confirm that your booking has been received, and confirmed."));
	      mailText.append("\n").append(iwrb.getLocalizedString("travel.supplier",   "Supplier    ")).append(" : ").append(suppl.getName());
	      if (addresses != null) {
	      		Address addr;
	      		Iterator iter = addresses.iterator();
	      		while (iter.hasNext()) {
	      			addr = (Address) iter.next();
	    	      mailText.append("\n").append(iwrb.getLocalizedString("travel.address","Address    ")).append(" : ").append(addr.getStreetName());
	    	      if (addr.getStreetNumber() != null) {
	    	      		mailText.append(" "+addr.getStreetNumber());
	    	      }
	      		}
	      }
	      if (suppEmail != null) {
	      		mailText.append("\n").append(iwrb.getLocalizedString("travel.email",   "Email    ")).append(" : ").append(suppEmail);
	      }
	      if (phones != null) {
	      		Phone phone;
	      		Iterator iter = phones.iterator();
	      		while (iter.hasNext()) {
	      			phone = (Phone) iter.next();
	      			if (phone != null && phone.getNumber() != null && !"".equals(phone.getNumber())) {
	      				mailText.append("\n").append(iwrb.getLocalizedString("travel.phone","Phone    ")).append(" : ").append(phone.getNumber());
	      			}
	      		}
	      }
	      	      
	      mailText.append("\n\n").append(iwrb.getLocalizedString("travel.name",   "Name    ")).append(" : ").append(gBooking.getName());
	      mailText.append("\n").append(iwrb.getLocalizedString("travel.service","Service ")).append(" : ").append(pBus.getProductNameWithNumber(prod, true, iwc.getCurrentLocaleId()));
	      mailText.append("\n").append(iwrb.getLocalizedString("travel.date",   "Date    ")).append(" : ").append(getLocaleDate(new IWTimestamp(gBooking.getBookingDate())));
	      mailText.append("\n").append(iwrb.getLocalizedString("travel.seats",  "Seats   ")).append(" : ").append(gBooking.getTotalCount());

	      mailText.append("\n\n").append(iwrb.getLocalizedString("travel.order_number",  "Order number   ")).append(" : ").append(Voucher.getVoucherNumber(gBooking.getID()));
	      String ccAuthNumber =  gBooking.getCreditcardAuthorizationNumber();
				String cardType = null;
				if (ccAuthNumber != null) {
					CreditCardAuthorizationEntry entry = ccBus.getAuthorizationEntry(suppl, ccAuthNumber, new IWTimestamp(gBooking.getDateOfBooking()));
					cardType = entry.getBrandName();
					double fAmount = entry.getAmount() / CreditCardAuthorizationEntry.amountMultiplier;
		      mailText.append("\n").append(iwrb.getLocalizedString("travel.amount_paid","Amount paid   ")).append(" : ").append(df.format(fAmount)+" "+entry.getCurrency()+" ("+cardType+")");
				}
	      mailText.append("\n\n").append(iwrb.getLocalizedString("travel.comment",  "Comment   ")).append(" : ").append(gBooking.getComment());
	      
	      mailText.append("\n\n").append(iwrb.getLocalizedString("travel.if_you_want_to_cancel",  "If you for any reason would like to cancel your booking please follow this link ")).append(" : ").append(LinkGenerator.getUrlToRefunderPage(iwc, gBooking.getReferenceNumber()));
	      mailText.append("\n").append(iwrb.getLocalizedString("travel.refund_must_happen_before_48_hours",  "Please note that you can not cancel your booking if 48 hours have passed since your booking was made."));
	      
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
	      mailText.append("\n").append(iwrb.getLocalizedString("travel.service","Service ")).append(" : ").append(pBus.getProductNameWithNumber(prod, true, iwc.getCurrentLocaleId()));
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
	return sendEmail;
}
  
  
  protected Table getTermsAndConditions() throws RemoteException {
  	Link terms = new Link(getTextWhite(iwrb.getLocalizedString("travel.search.terms_and_conditions", "Terms and conditions")));
  	terms.setWindowToOpen(TravelWindow.class, "700", "400", true, true);
  	terms.addParameter(TravelWindow.LOCALIZATION_KEY_FOR_HEADER, "travel.search.terms_and_conditions");
  	terms.addParameter(TravelWindow.LOCALIZATION_KEY, "travel.search.terms_and_conditions_text");

  	Link privacyStatement = new Link(getTextWhite(iwrb.getLocalizedString("travel.search.privacy_statement", "Privacy statement")));
  	privacyStatement.setWindowToOpen(TravelWindow.class, "700", "400", true, true);
  	privacyStatement.addParameter(TravelWindow.LOCALIZATION_KEY_FOR_HEADER, "travel.search.privacy_statement");
  	privacyStatement.addParameter(TravelWindow.LOCALIZATION_KEY, "travel.search.privacy_statement_text");

  	Table table = new Table(1, 1);
  	table.setCellpaddingAndCellspacing(0);
  	table.setWidth("100%");
  	table.setAlignment(1, 1, Table.HORIZONTAL_ALIGN_RIGHT);
  	table.add(terms, 1, 1);

  	table.add(Text.NON_BREAKING_SPACE, 1, 1);
  	table.add(getTextWhite("-"), 1, 1);
  	table.add(Text.NON_BREAKING_SPACE, 1, 1);
  	
  	table.setAlignment(1, 1, Table.HORIZONTAL_ALIGN_RIGHT);
  	table.add(privacyStatement, 1, 1);

  	return table;
  }  

protected static String getLocaleDate(IWTimestamp stamp) {
    return  (new IWCalendar(stamp)).getLocaleDate();
  }

/*
  protected static TravelStockroomBusiness getTravelStockroomBusiness(IWApplicationContext iwac) throws RemoteException {
    return (TravelStockroomBusiness) IBOLookup.getServiceInstance(iwac, TravelStockroomBusiness.class);
  }
  protected static ServiceHandler getServiceHandler(IWApplicationContext iwac) throws RemoteException {
    return (ServiceHandler) IBOLookup.getServiceInstance(iwac, ServiceHandler.class);
  }

  protected static ProductBusiness getProductBusiness(IWApplicationContext iwac) throws RemoteException {
    return (ProductBusiness) IBOLookup.getServiceInstance(iwac, ProductBusiness.class);
  }
  */
  protected CreditCardBusiness getCreditCardBusiness(IWContext iwc) {
	  	try {
	  		return (CreditCardBusiness) IBOLookup.getServiceInstance(iwc, CreditCardBusiness.class);
	  	} catch (IBOLookupException rt) {
	  		throw new IBORuntimeException();
	  	}
	}

}
