package is.idega.idegaweb.travel.presentation;

import is.idega.idegaweb.travel.block.search.presentation.ProductDetailFrame;
import is.idega.idegaweb.travel.service.presentation.BookingForm;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.FinderException;
import com.idega.block.creditcard.business.CreditCardBusiness;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.localisation.business.LocaleSwitcher;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.CalendarParameters;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.util.IWTimestamp;
/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class PublicBooking extends TravelBlock  {

  IWResourceBundle iwrb;
  IWBundle bundle;
  Product product;
//  Service service;
//  Timeframe[] timeframes;
  Supplier supplier;
  int productId = -1;
  
  private static final String STYLENAME_WHITE_LINK = "WhiteLink";

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

  private HashMap frames = new HashMap();
  BookingForm bf;
  boolean legalDay;
  boolean fullyBooked;

  public PublicBooking() {
  }

  public void main(IWContext iwc)throws Exception {
    if (iwc.isParameterSet(LocaleSwitcher.languageParameterString)) {
  			LocaleSwitcher ls = new LocaleSwitcher();
  			ls.actionPerformed(iwc);
    }
  		super.main(iwc);
    init(iwc);

    if (productId != -1 ) {
      displayForm(iwc);
    }else {
    }
  }

	public Map getStyleNames() {
		Map map = super.getStyleNames();
		if (map == null) {
			map = new HashMap();
		}
		map.put(STYLENAME_WHITE_LINK, "");
		return map;
	}  
	


  private void init(IWContext iwc) throws RemoteException{
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
  
  private Text getWhiteText(String content) {
  		Text text = new Text(content);
  		text.setStyleClass(getStyleName(STYLENAME_WHITE_LINK));
  		return text;
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
  private Text getBoldTextWhite(String content) {
    Text temp = (Text) boldText.clone();
      temp.setText(content);
      temp.setFontColor("#FFFFFF");
    return temp;
  }


  private void displayForm(IWContext iwc) throws RemoteException, FinderException{
      Table table = new Table(3,6);
	    table.setWidth("800");
	    table.setAlignment("center");
	    table.setCellspacing(0);
	    table.setCellpadding(0);
	    table.setBorder(0);
	    table.setWidth(1, "175");
	    table.setWidth(2, "5");

	    	if (product.getIsValid() && supplier.getIsValid()) {
					try {
						bf = getServiceHandler(iwc).getBookingForm(iwc, product);
						legalDay = bf.getIsDayVisible(iwc);
			      fullyBooked = bf.isFullyBooked( iwc, product, stamp);
			      table.add(rightBottom(iwc),3,3);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
	    	} else {
	    		table.add(bf.getDisabledProduct(iwc), 3, 3);
	    	} 

      table.setVerticalAlignment(1,3,"top");
      table.setVerticalAlignment(3,3,"top");
      //table.setAlignment(2,3,"center");
     // table.setWidth(1, "20");

      table.mergeCells(1, 1, 3, 1);
      table.mergeCells(1, 2, 3, 2);

      table.add(topHeader(iwc), 1, 1);
      table.setCellpaddingBottom(1, 1, 2);
      table.add(header(iwc), 1, 2);
      table.setCellpaddingBottom(1, 2, 4);
      table.add(leftBottom(iwc),1,3);

      table.setRowHeight(4, "1");
      table.setStyleClass(1, 4, getStyleName(BookingForm.STYLENAME_HEADER_BACKGROUND_COLOR));
      table.setStyleClass(3, 4, getStyleName(BookingForm.STYLENAME_HEADER_BACKGROUND_COLOR));
      
      table.setRowHeight(5, "1");
      Image idegaweb = bundle.getImage("/images/idegaweb.png");
      //table.setCellpaddingLeft(1, 6, 10);
      table.add(idegaweb, 1, 6);
      table.setRowHeight(6, "25");
      table.setCellpaddingLeft(1, 6, 10);
      table.setStyleClass(1, 6, getStyleName(BookingForm.STYLENAME_BLUE_BACKGROUND_COLOR));
      table.setStyleClass(3, 6, getStyleName(BookingForm.STYLENAME_BLUE_BACKGROUND_COLOR));
      table.add(getTermsAndConditions(iwc), 3, 6);
      table.setCellpaddingRight(3, 6, 10);

      //ProductDetailFrame frame = getProductDetailFrame(product, iwc, 2);
      //frame.add(leftBottom(iwc));
      //add(frame);
      add(table);
  }

  private Table topHeader(IWContext iwc) {
  		Table table = new Table();
  		table.setWidth("100%");
  		table.setBorder(0);
  		table.setCellpaddingAndCellspacing(0);
  		table.setStyleClass(1, 1, getStyleName(BookingForm.STYLENAME_BLUE_BACKGROUND_COLOR));
  		table.setStyleClass(2, 1, getStyleName(BookingForm.STYLENAME_BLUE_BACKGROUND_COLOR));
  		
  		Image etravel = this.bundle.getImage("images/etravel.gif");
  		Image idega = this.bundle.getImage("images/idega.gif");
  		table.add(etravel, 1, 1);
  		table.add(idega, 2, 1);
  		table.setAlignment(2, 1, Table.HORIZONTAL_ALIGN_RIGHT);
  		table.setCellpaddingLeft(1, 1, 5);
  		table.setCellpaddingRight(2, 1, 5);
  		table.setVerticalAlignment(1, 1, Table.VERTICAL_ALIGN_BOTTOM);
  		table.setVerticalAlignment(2, 1, Table.VERTICAL_ALIGN_BOTTOM);
  		table.setCellpaddingBottom(1, 1, 8);
  		table.setCellpaddingBottom(2, 1, 8);
  		table.setHeight(1, 1, 50);
  		
  		table.mergeCells(1, 3, 2, 3);
  		table.setStyleClass(1, 3, getStyleName(BookingForm.STYLENAME_BLUE_BACKGROUND_COLOR));
  		
  		return table;
  }
  
  private Table header(IWContext iwc) {
		Table table = new Table();
		table.setWidth("100%");
		table.setBorder(0);
		table.setCellpaddingAndCellspacing(0);
		table.setStyleClass(1, 1, getStyleName(BookingForm.STYLENAME_HEADER_BACKGROUND_COLOR));
		table.setStyleClass(2, 1, getStyleName(BookingForm.STYLENAME_HEADER_BACKGROUND_COLOR));
		table.setAlignment(2, 1, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setCellpaddingLeft(1, 1, 10);
		table.setCellpaddingRight(2, 1, 10);
		Text suppName = new Text(supplier.getName());
		suppName.setStyleClass(getStyleName(BookingForm.STYLENAME_HEADER_TEXT));		
		Text availText = null;
		if (legalDay && !fullyBooked) {
			availText = new Text(iwrb.getLocalizedString("travel.there_is_availability","There is availability "));
			availText.addToText(stamp.getLocaleDate(iwc)+". "+iwrb.getLocalizedString("travel.please_book","Please book"));
		} else {
			availText = new Text(iwrb.getLocalizedString("travel.there_is_no_availability","There is no availability "));
			availText.addToText(stamp.getLocaleDate(iwc)+". "+iwrb.getLocalizedString("travel.please_find_another_day","Please find another day"));
		}
		availText.setStyleClass(getStyleName(BookingForm.STYLENAME_HEADER_TEXT));
		
		table.add(availText, 1, 1);
		table.add(suppName, 2, 1);
		table.setHeight(1, 1, 50);
		table.setHeight(1, 2, 2);
		table.setHeight(1, 3, 3);
		table.mergeCells(1, 3, 2, 3);
		table.setStyleClass(1, 3, getStyleName(BookingForm.STYLENAME_BLUE_BACKGROUND_COLOR));
		return table;
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

  private Table leftBottom(IWContext iwc)  {
    Table table = new Table(1,9);
//	  table.setBorder(1);
//	  table.setBorderColor("BLUE");
	  table.setCellpaddingAndCellspacing(0);
	  table.setWidth(180);
	  
	  int row = 1;
	  
	  table.setHeight(1, row, 30);
	  Text dayRequest = new Text(iwrb.getLocalizedString("travel.day_requested", "Day requested"));
	  dayRequest.setStyleClass(getStyleName(BookingForm.STYLENAME_HEADER_TEXT));
	  table.add(dayRequest, 1, row);
	  table.setCellpaddingLeft(1, row, 10);
	  table.setStyleClass(1, row, getStyleName(BookingForm.STYLENAME_HEADER_BACKGROUND_COLOR));
	  ++row;
	  table.setHeight(1, row, 1);
	  ++row;
	  table.setStyleClass(1, row, getStyleName(BookingForm.STYLENAME_BLUE_BACKGROUND_COLOR));
	  table.setHeight(1, row, 1);
	  ++row;
	  table.setHeight(1, row, 4);
	  ++row;
	  
	  table.setCellpaddingLeft(1, row, 10);
	  table.setCellpaddingRight(1, row, 10);
    table.setStyleClass(1, row, getStyleName(BookingForm.STYLENAME_BACKGROUND_COLOR));

    try {
      CalendarHandler ch = new CalendarHandler(iwc);
      ch.setProduct(product);
      
      ch.setTextStyle(getStyleName(BookingForm.STYLENAME_TEXT));
      ch.setHeaderStyle(getStyleName(BookingForm.STYLENAME_HEADER_TEXT));
      ch.setLinkStyle(getStyleName(BookingForm.STYLENAME_LINK));
      ch.setBackgroundStyle(getStyleName(BookingForm.STYLENAME_BACKGROUND_COLOR));
      ch.setAvailableDayFontStyle(getStyleName(BookingForm.STYLENAME_TEXT));
      ch.setAvailableDayStyle(getStyleName(BookingForm.STYLENAME_AVAILABLE_DAY));
      ch.setFullyBookedStyle(getStyleName(BookingForm.STYLENAME_FULLY_BOOKED));
      ch.setTodayStyle(getStyleName(BookingForm.STYLENAME_TODAY));
      ch.setInquiryStyle(getStyleName(BookingForm.STYLENAME_INQUIRY));
      ch.setInActiveCellStyle(getStyleName(BookingForm.STYLENAME_HEADER_BACKGROUND_COLOR));
      ch.setSmallStyle(getStyleName(BookingForm.STYLENAME_SMALL_TEXT));

      ch.showInquiries(false);
	    ch.setTimestamp(stamp);
	    ch.addParameterToLink(this.parameterProductId, productId);
	    ch.setClassToLinkTo(PublicBooking.class);
	    ch.addParameterToLink(PARAMETER_REFERRAL_URL, BookingForm.getRefererUrl(iwc));

	    table.add(ch.getCalendarTable(iwc),1,row);
	    ++row;
      table.setStyleClass(1, row, getStyleName(BookingForm.STYLENAME_HEADER_BACKGROUND_COLOR));
      table.setHeight(1, row, 1);
      ++row;
      table.setHeight(1, row, 2);
      ++row;
      table.setHeight(1, row, 3);
      table.setStyleClass(1, row, getStyleName(BookingForm.STYLENAME_HEADER_BACKGROUND_COLOR));
      Image image = bundle.getImage("verisignseals/verisign_logo.gif");
      //image.setWidth(100);
      //image.setHeight(42);
      String verisignUrl = bundle.getProperty("verisign_url");
      if (verisignUrl == null) { 
      		verisignUrl = "https://digitalid.verisign.com/as2/a83d13ff1653ab8baf084d646faab5c9";
      }
		
      ++row;
      Link verisign = new Link(image, verisignUrl);
      verisign.setTarget(Link.TARGET_NEW_WINDOW);
      verisign.setOutgoing(true);
      table.setCellpaddingTop(1, row, 5);
      table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_CENTER);
      table.add(verisign, 1,row);

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


  private Form rightBottom(IWContext iwc) {
    try {
    	Form form = new Form();
    	form.maintainParameter(PARAMETER_REFERRAL_URL);
	
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
		        form.maintainParameter(BookingForm.PARAMETER_REFERENCE_NUMBER);
		//            form.setOnSubmit("this.form."+BookingForm.sAction+".value = \""+BookingForm.parameterSaveBooking+"\"");
		//            form.addParameter(BookingForm.sAction,BookingForm.parameterSaveBooking);
		    }else if (action.equals(BookingForm.parameterSaveBooking) || action.equals(BookingForm.parameterSendInquery)) {
		        form = bf.getFormMaintainingAllParameters(iwc, true, false);
		        form.maintainParameter(this.parameterProductId);
		//            form.addParameter( BookingForm.sAction, this.parameterBookingVerified);
		        form.add(bf.getVerifyBookingTable(iwc, product));
		    }else if (action.equals(this.parameterBookingVerified)) {
		        form = bf.getFormMaintainingAllParameters(iwc, true, false);
		        form.maintainParameter(this.parameterProductId);
		        form.add(bf.handlePublicTransaction());
		    }
//		    else if (action.equals(BookingForm.parameterSendInquery)) {
//	        form = bf.getFormMaintainingAllParameters(iwc, true, false);
//	        form.maintainParameter(this.parameterProductId);
//	        form.add(bf.handlePublicTransaction());
//		    }
		  }else {
		      form.add(bf.getNoSeatsAvailable(iwc, stamp));
		  }

      return form;
    }catch (Exception e) {
      e.printStackTrace(System.err);
      return new Form();
    }
  }

//  private Table doBooking(IWContext iwc) throws RemoteException{
//    Table table = new Table();
//      String ccNumber = iwc.getParameter(BookingForm.parameterCCNumber);
//      String ccMonth  = iwc.getParameter(BookingForm.parameterCCMonth);
//      String ccYear   = iwc.getParameter(BookingForm.parameterCCYear);
//
//      Text display = getBoldTextWhite("");
//      boolean success = false;
//      boolean inquirySent = false;
//
////      com.idega.block.tpos.business.TPosClient t = null;
//      GeneralBooking  gBooking = null;
//			BookingForm bf = null; 
//			try {
//				bf = getServiceHandler(iwc).getBookingForm(iwc, product);
//			}catch (Exception e) {
//				e.printStackTrace(System.out);	
//			}
//
//      TransactionManager tm = IdegaTransactionManager.getInstance();
//      try {
//        tm.begin();
//
////				float price = bf.getOrderPrice(iwc, product, stamp);
//
////        TourBookingForm tbf = new TourBookingForm(iwc,product);
////        int bookingId = bf.saveBooking(iwc); // WAS handleInsert(iwc), changed 14.10.2002, because Booking has already been checked, and verified
//        int bookingId = bf.handleInsert(iwc); // WAS handleInsert(iwc), changed 14.10.2002, because Booking has already been checked, and verified
//
//        if (bookingId == BookingForm.inquirySent) {
//          inquirySent = true;
//          tm.commit();
//        } else if (bookingId == BookingForm.errorFieldsEmpty) {
//        		List list = bf.errorFields;
//        		display.addToText(iwrb.getLocalizedString("travel.fields_must_be_filled", "The following fields must be filled")+Text.BREAK);
//        		Iterator iter = list.iterator();
//        		while (iter.hasNext()) {
//        			display.addToText(" "+iter.next().toString()+Text.BREAK);
//        		}
//        		success = false;
//        }else {
//          gBooking = ((is.idega.idegaweb.travel.data.GeneralBookingHome)com.idega.data.IDOLookup.getHome(GeneralBooking.class)).findByPrimaryKey(new Integer(bookingId));
//          gBooking.setRefererUrl(getRefererUrl(iwc));
//          gBooking.store();
//          tm.commit();
//          success = true;
//        }
//
//      }catch(CreditCardAuthorizationException e) {
//      	if (!e.getMessage().equals("")) {
//	        display.addToText(iwrb.getLocalizedString("travel.booking_failed","Booking failed")+" ( "+e.getMessage()+" )");
//      	}
//        //e.printStackTrace(System.err);
////        gBooking.setIsValid(false);
////        gBooking.store();
//        try {
//          tm.commit();
//        }catch(Exception ex) {
//          debug("commit failed");
//          ex.printStackTrace(System.err);
//          try {
//            tm.rollback();
//          }catch (javax.transaction.SystemException se) {
//            se.printStackTrace(System.err);
//          }
//        }
//
//        gBooking = null;
//        success = false;
//      }catch (Exception e) {
//        display.addToText(" ( "+e.getMessage()+" )");
//        e.printStackTrace(System.err);
//        gBooking = null;
//        try {
//          tm.rollback();
//        }catch (javax.transaction.SystemException se) {
//          se.printStackTrace(System.err);
//        }
//      }
//
//      if (gBooking != null) {
//        boolean sendEmail = sendEmails(iwc, gBooking, iwrb);
//
//        table.add(getBoldTextWhite(gBooking.getName()));
//        table.add(getBoldTextWhite(", "));
//        table.add(getBoldTextWhite(iwrb.getLocalizedString("travel.you_booking_has_been_confirmed","your booking has been confirmed.")));
//        table.add(Text.BREAK);
//        table.add(Text.BREAK);
//        if (sendEmail) {
//          table.add(getBoldTextWhite(iwrb.getLocalizedString("travel.you_will_reveice_an_email_shortly","You will receive an email shortly confirming your booking.")));
//          table.add(Text.BREAK);
//          table.add(Text.BREAK);
//        }
//        table.add(getBoldTextWhite(iwrb.getLocalizedString("travel.your_credidcard_authorization_number_is","Your creditcard authorization number is")));
//        table.add(getBoldTextWhite(" : "));
//        table.add(getBoldTextWhite(gBooking.getCreditcardAuthorizationNumber()));
//        table.add(Text.BREAK);
//        table.add(getBoldTextWhite(iwrb.getLocalizedString("travel.your_reference_number_is","Your reference number is")));
//        table.add(getBoldTextWhite(" : "));
//        table.add(getBoldTextWhite(gBooking.getReferenceNumber()));
//        table.add(Text.BREAK);
//        //table.add(getBoldTextWhite(gBooking.getReferenceNumber()));
//        //table.add(Text.BREAK);
//        table.add(Text.BREAK);
//        table.add(getBoldTextWhite(iwrb.getLocalizedString("travel.if_unable_to_print","If you are unable to print the voucher, write the reference number down else proceed to printing the voucher.")));
//
//
//
//        Link printVoucher = new Link(getBoldTextWhite(iwrb.getLocalizedString("travel.print_voucher","Print voucher")));
//          printVoucher.addParameter(VoucherWindow.parameterBookingId, gBooking.getID());
//          printVoucher.setWindowToOpen(VoucherWindow.class);
//
//        try {
//        CreditCardAuthorizationEntry entry = this.getCreditCardBusiness(iwc).getAuthorizationEntry(supplier, gBooking.getCreditcardAuthorizationNumber(),  new IWTimestamp(gBooking.getDateOfBooking()));
//	        if (entry != null) {
//	          Receipt r = new Receipt(entry, supplier);
//	          iwc.setSessionAttribute(ReceiptWindow.RECEIPT_SESSION_NAME, r);
//	
//	          Link printCCReceipt = new Link(getBoldTextWhite(iwrb.getLocalizedString("travel.print_cc_receipt","Print creditcard receipt")));
//	            printCCReceipt.setWindowToOpen(ReceiptWindow.class);
//	          table.add(Text.NON_BREAKING_SPACE+Text.NON_BREAKING_SPACE, 1,2);
//	          table.add(printCCReceipt, 1, 2);
//	        }
//        } catch (Exception e) {
//        		e.printStackTrace(System.err);
//        }
//
//        table.add(printVoucher,1,3);
//        table.setAlignment(1,1,"left");
//        table.setAlignment(1,2,"right");
//        table.setAlignment(1,3,"right");
//      }else if (inquirySent) {
//        table.add(getBoldTextWhite(iwrb.getLocalizedString("travel.inquiry_has_been_sent","Inquiry has been sent")));
//        table.add(Text.BREAK);
//        table.add(getBoldTextWhite(iwrb.getLocalizedString("travel.you_will_reveice_an_confirmation_email_shortly","You will receive an confirmation email shortly.")));
//      }else {
//        table.add(display);
//        if (gBooking == null) {
//          debug("gBooking == null");
//        }
//      }
//
//    return table;
//  }


  
  
  protected Table getTermsAndConditions(IWContext iwc) throws RemoteException {
  	
  	Link terms = LinkGenerator.getLinkToTermsAndContition(iwc, getWhiteText(iwrb.getLocalizedString("travel.search.terms_and_conditions", "Terms and conditions")));
  	Link privacyStatement = LinkGenerator.getLinkToPrivacyStatement(iwc, getWhiteText(iwrb.getLocalizedString("travel.search.privacy_statement", "Privacy statement")));

  	Table table = new Table(1, 1);
  	table.setCellpaddingAndCellspacing(0);
  	table.setWidth("100%");
  	table.setAlignment(1, 1, Table.HORIZONTAL_ALIGN_RIGHT);
  	table.add(terms, 1, 1);

  	table.add(Text.NON_BREAKING_SPACE, 1, 1);
  	table.add(getWhiteText("-"), 1, 1);
  	table.add(Text.NON_BREAKING_SPACE, 1, 1);
  	
  	table.setAlignment(1, 1, Table.HORIZONTAL_ALIGN_RIGHT);
  	table.add(privacyStatement, 1, 1);

  	return table;
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

	protected ProductDetailFrame getProductDetailFrame(Product product, IWContext iwc, int columns) throws RemoteException {
		ProductDetailFrame frame = (ProductDetailFrame) frames.get(new Integer(columns));
		if (frame == null) { 
			frame = new ProductDetailFrame(iwc, columns);
			//frame.setPriceCategoryKey(getPriceCategoryKey());
			//frame.setCount(this.getCount());
			//frame.setProductInfoDetailed(getProductInfoDetailed(getProduct()));
			frames.put(new Integer(columns), frame);
		}
		return frame;
	}
 
  
}
