package is.idega.idegaweb.travel.presentation;

import is.idega.idegaweb.travel.business.Booker;
import is.idega.idegaweb.travel.data.GeneralBooking;
import is.idega.idegaweb.travel.data.GeneralBookingHome;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.FinderException;
import com.idega.block.creditcard.business.CreditCardAuthorizationException;
import com.idega.block.creditcard.business.CreditCardBusiness;
import com.idega.block.creditcard.business.CreditCardClient;
import com.idega.block.creditcard.data.CreditCardAuthorizationEntry;
import com.idega.block.trade.stockroom.business.ProductBusiness;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWTimestamp;
import com.idega.util.text.TextSoap;

/**
 * @author gimmi
 */
public class BookingRefunder extends TravelBlock {
	
	private static final String PARAMETER_REFERENCE_NUMBER = "rn";
	public static final String PARAMETER_EMAILED_REFERENCE_NUMBER = "rno";
	
	private static final String PARAMETER_CC_NUMBER = "ccn";
	private static final String PARAMETER_CC_MONTH = "ccm";
	private static final String PARAMETER_CC_YEAR = "ccy";
	private static final String PARAMETER_CC_CVC = "cccvc";
	private static final String PARAMETER_AMOUNT = "cca";

	private static final String ACTION = "p_a";
	private static final String PARAMETER_VERIFY = "p_v";
	private static final String PARAMETER_CONFIRMED = "p_c";
	
	public static final int HOURS_BEFORE_REFUND_EXPIRES = 48;
  private DecimalFormat df = new DecimalFormat("0.00");
  
	private int dateInputSize = 3;
  private int ccInputSize = 19;
  private int amountInputSize = 10;
  private int tableWidth = 400;

  private GeneralBooking booking = null;
  private Product product = null;
	private CreditCardAuthorizationEntry ccAuthEntry = null;
	private CreditCardClient ccClient = null;
	private IWResourceBundle iwrb = null;
	private Supplier bookingSupplier = null;
	
	private String headerStyle = null;
	private String textStyle = null;
	private String errorTextStyle = null;
		
	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		init(iwc);
		if (booking == null) {
			getReferenceNumberForm(iwc);
		} else {
			getRefundForm(iwc);
		}
		
	}
	
	private void init(IWContext iwc) throws RemoteException {
		iwrb = getResourceBundle(iwc);
		String refNum = iwc.getParameter(PARAMETER_REFERENCE_NUMBER);
		if (refNum != null) {
			try {
				GeneralBookingHome bookingHome = (GeneralBookingHome) IDOLookup.getHome(GeneralBooking.class);
				Collection coll = bookingHome.findAllByReferenceNumber(refNum);
				if ( coll != null && !coll.isEmpty()) {
					Iterator iter = coll.iterator();
					booking = (GeneralBooking) iter.next();
					product = booking.getService().getProduct();
					System.out.println("[BookingRefunder] is product ("+product.getID()+") refundable = "+product.getRefundable());
					bookingSupplier = product.getSupplier();
				}
			} catch (FinderException e) {
			}

			try {
				ccAuthEntry = getCreditCardBusiness(iwc).getAuthorizationEntry(bookingSupplier, booking.getCreditcardAuthorizationNumber(), new IWTimestamp(booking.getDateOfBooking()));
				ccClient = getCreditCardBusiness(iwc).getCreditCardClient(bookingSupplier, new IWTimestamp(booking.getDateOfBooking()));
			} catch (Exception e) {
			}
			
		}
	}
	
	private void getRefundForm(IWContext iwc) throws RemoteException, FinderException {
		Form form = new Form();
		Table table = new Table();
		table.setWidth(tableWidth);
		form.add(table);
		form.maintainParameter(PARAMETER_REFERENCE_NUMBER);
		int row = 1;
		
		String action  = iwc.getParameter(ACTION);

		if (bookingSupplier.equals(super.getSupplier()) || super.isSuperAdmin ) {
			if (ccAuthEntry == null) {
				table.add(getHeaderText(iwrb.getLocalizedString("travel.not_booked_with_a_creditcard", "Not booked with a creditcard")), 1, row);
				++row;
				table.add(getErrorText(iwrb.getLocalizedString("travel.not_booked_with_a_creditcard_long", "This booking was not booked using a creditcard. Please contact the supplier if this is incorrect.")), 1, row);
			} else if (action == null){
		    row = creditcardInputForm(iwc, table, row);
			} else if (action.equals(PARAMETER_VERIFY)) {
				row = verificationForm(iwc, table, row);
			} else if (action.equals(PARAMETER_CONFIRMED)) {
				complete(iwc, table, row);
			}
		} else {
			if (!product.getRefundable()) { 
				table.add(getHeaderText(iwrb.getLocalizedString("travel.cannot_refund", "Can not refund")), 1, row);
				++row;
				table.add(getErrorText(iwrb.getLocalizedString("travel.product_not_refundable", "This booking can not be refunded, please contact the supplier.")), 1, row);
			} else if (isTooLateToRefund()) {
				table.add(getHeaderText(iwrb.getLocalizedString("travel.time_has_passed", "Time has passed")), 1, row);
				++row;
				table.add(getErrorText(iwrb.getLocalizedString("travel.time_has_passed_long", "Too much time (more than "+HOURS_BEFORE_REFUND_EXPIRES+" hours) has passed since you made your booking.  In order to cancel your booking you must contact the supplier.")), 1, row);
			} else if (isPastBookingDate()) { 
				table.add(getHeaderText(iwrb.getLocalizedString("travel.booking_date_has_passed", "Booking date passed")), 1, row);
				++row;
				table.add(getErrorText(iwrb.getLocalizedString("travel.booking_date_has_passed_long", "You can no longer cancel this booking.  In order to cancel this booking you must contact the supplier.")), 1, row);
			} else if (ccAuthEntry == null) {
				table.add(getHeaderText(iwrb.getLocalizedString("travel.not_booked_with_a_creditcard", "Not booked with a creditcard")), 1, row);
				++row;
				table.add(getErrorText(iwrb.getLocalizedString("travel.not_booked_with_a_creditcard_long", "This booking was not booked using a creditcard. Please contact the supplier if this is incorrect.")), 1, row);
			} else if (ccAuthEntry.getCardNumber() == null) {
				table.add(getHeaderText(iwrb.getLocalizedString("travel.cannot_refund", "Can not refund")), 1, row);
				++row;
				table.add(getErrorText(iwrb.getLocalizedString("travel.cannot_refund_long", "This booking can not be refunded here, please contact the supplier.")), 1, row);
			} else if (action == null){
		    row = creditcardInputForm(iwc, table, row);
			} else if (action.equals(PARAMETER_VERIFY)) {
				row = verificationForm(iwc, table, row);
			} else if (action.equals(PARAMETER_CONFIRMED)) {
				complete(iwc, table, row);
			}
		}
		
		add(form);
	}
	
	private int verificationForm(IWContext iwc, Table table, int row) throws RemoteException{
    String number = iwc.getParameter(PARAMETER_CC_NUMBER);
    String year   = iwc.getParameter(PARAMETER_CC_YEAR);
    String month  = iwc.getParameter(PARAMETER_CC_MONTH);
    String cvc  = iwc.getParameter(PARAMETER_CC_CVC);
    String amount = iwc.getParameter(PARAMETER_AMOUNT);
	  amount = TextSoap.findAndReplace(amount,',','.');

    Text ccNumber = getText(iwrb.getLocalizedString("travel.credidcard_number","Creditcard number"));
    Text ccYear   = getText(iwrb.getLocalizedString("travel.year","Year"));
    Text ccMonth  = getText(iwrb.getLocalizedString("travel.month","Month"));
    Text ccCVC = getText(iwrb.getLocalizedString("travel.cc.cvc","Cardholder Verification Code (CVC)"));
    Text ccAmount   = getText(iwrb.getLocalizedString("travel.amount","Amount"));
    Text notANumber = getErrorText("X");
	  notANumber.setFontColor("RED");

    boolean error = false;
    boolean incorrectCCNum = false;
    boolean incorrectCCDate = false;
    boolean cardNumberWarning = false;
    table.mergeCells(1,row,3,row);
    table.add(getHeaderText(iwrb.getLocalizedString("travel.is_information_correct","Is the following information correct ?")), 1, row);
    //table.setRowColor(row, backgroundColor);

    ++row;
    table.add(ccNumber,2,row);
    table.add(number,3,row);
    table.setAlignment(3, row, "right");
    try {
    		if (this.ccAuthEntry.getCardNumber() != null) {
	    		incorrectCCNum = !getCreditCardBusiness(iwc).verifyCreditCardNumber(number,this.ccAuthEntry);
	    		error = incorrectCCNum;
	      Long.parseLong(number);
    		} else {
    			if (bookingSupplier.equals(super.getSupplier()) || super.isSuperAdmin ) {
    				cardNumberWarning = true;
    			} else {
    				incorrectCCNum = true;
    				error = true;
    			}
    		}
    }catch (NumberFormatException n) {
      table.add(notANumber,4,row);
      error = true;
    } catch (IllegalArgumentException e) {
      error = true;
    		e.printStackTrace();
    }
    incorrectCCDate = !ccAuthEntry.getCardExpires().equals(month+year);
    if (incorrectCCDate) {
    		System.out.println("BookingRefunder : ExpireDate is incorrect ("+month+year+")");
    		error = true;
    }
    
    ++row;
    table.add(ccMonth,2,row);
    table.add(month,3,row);
    table.setAlignment(3, row, "right");
    try {
      if (Integer.parseInt(month) < 1 || Integer.parseInt(month) > 12) {
        throw new NumberFormatException();
      }
    }catch (NumberFormatException n) {
      table.add(notANumber,4,row);
      error = true;
    }

    ++row;
    table.add(ccYear,2,row);
    table.add(year,3,row);
    table.setAlignment(3, row, "right");
    try {
      Integer.parseInt(year);
    }catch (NumberFormatException n) {
      table.add(notANumber,4,row);
      error = true;
    }
    
    if (getCreditCardBusiness(iwc).getUseCVC(ccClient)) {
	    ++row;
	    table.add(ccCVC, 2, row);
	    table.add(cvc, 3, row);
	    table.setAlignment(3, row, "right");
	    try {
	    		Integer.parseInt(cvc);
	    } catch (NumberFormatException e) {
	    		table.add(notANumber, 4, row);
	    		error = true;
	    }
    }


    ++row;
    ++row;
    table.add(ccAmount,2,row);
    table.add(amount,3,row);
    table.setAlignment(3, row, "right");
    try {
      Float.parseFloat(amount);
    }catch (NumberFormatException n) {
      table.add(notANumber,4,row);
      error = true;
    }

	  if (incorrectCCNum || incorrectCCDate) {
			++row;
			table.mergeCells(1, row, 3, row);
			table.add(getErrorText(iwrb.getLocalizedString("creditcard.credicard_info_not_the_same_as_on_booking", "Creditcard information is not the same as the one used when booking.")), 1, row);
	  } 
	  else if (cardNumberWarning) {
			++row;
			table.mergeCells(1, row, 3, row);
			table.add(getErrorText(iwrb.getLocalizedString("creditcard.warning_credicard_info_can_not_be_verified", "WARNING !!! Creditcard number can not be verified as being the same as used when booking.")), 1, row);
	  }

    ++row;
    ++row;
    table.mergeCells(1, row, 2, row);
    table.setAlignment(3, row, "right");
    table.add(new BackButton(iwrb.getLocalizedImageButton("creditcard.no", "No")),1 ,row);
    SubmitButton sub  = new SubmitButton(iwrb.getLocalizedImageButton("creditcard.yes", "Yes"), ACTION, PARAMETER_CONFIRMED);
	  table.add(new HiddenInput(this.PARAMETER_CC_NUMBER, number), 1, row);
	  table.add(new HiddenInput(this.PARAMETER_CC_YEAR, year), 1, row);
	  table.add(new HiddenInput(this.PARAMETER_CC_MONTH, month), 1, row);
	  table.add(new HiddenInput(this.PARAMETER_CC_CVC), 1, row);
	  table.add(new HiddenInput(this.PARAMETER_AMOUNT, amount), 1, row);
	  
    if (!error)
    		table.add(sub, 3, row);

    return row;
	}
	
	private int creditcardInputForm(IWContext iwc, Table table, int row) throws RemoteException, FinderException {
		TextInput ccNum = new TextInput(PARAMETER_CC_NUMBER);
			ccNum.setSize(ccInputSize);
			TextInput ccMonth = new TextInput(PARAMETER_CC_MONTH);
			ccMonth.setSize(dateInputSize);
			TextInput ccYear = new TextInput(PARAMETER_CC_YEAR);
			ccYear.setSize(dateInputSize);
			TextInput amount = new TextInput(PARAMETER_AMOUNT);
			amount.setSize(amountInputSize);
			TextInput cvc = new TextInput(PARAMETER_CC_CVC);
			cvc.setSize(5);
			
			table.add(getHeaderText(iwrb.getLocalizedString("travel.booking_refund", "Booking refund")), 1, row);
			
			++row;
			table.add(getText(iwrb.getLocalizedString("travel.credidcard_number","Creditcard number")), 1, row);
	    table.add(ccNum, 2, row);
	    table.mergeCells(2, row, 3, row);
	    //table.setRowColor(row, GRAY);
	    
	    ++row;
	    table.add(getText(iwrb.getLocalizedString("travel.month","Month")), 1, row);
	    table.add(getText(" / "), 1, row);
	    table.add(getText(iwrb.getLocalizedString("travel.year","Year")), 1, row);
	    table.add(ccMonth, 2, row);
	    table.add(getText(" / "), 2, row);
	    table.add(ccYear, 2, row);
	    //table.setRowColor(row, GRAY);

	    ++row;
	    //table.setRowColor(row, GRAY);
	    table.add(getText(iwrb.getLocalizedString("travel.amount","Amount")), 1, row);
	    String sAmount = df.format(getBooker(iwc).getBookingPrice(getBooker(iwc).getMultibleBookings(booking)));
	    table.add(getText(sAmount), 2, row);
	    table.add(new HiddenInput(PARAMETER_AMOUNT, sAmount), 2, row);
	    //table.add(amount, 2, row);
	    //table.setRowColor(row, GRAY);

	    if (getCreditCardBusiness(iwc).getUseCVC(ccClient)) {
		    ++row;
		    table.add(getText(iwrb.getLocalizedString("travel.cc.cvc","Cardholder Verification Code (CVC)")), 1,row);
		    table.add(cvc, 2,row);
				Link cvcLink = LinkGenerator.getLinkCVCExplanationPage(iwc, getText(iwrb.getLocalizedString("cc.what_is_cvc","What is CVC?")));
				if (cvcLink != null) {
					table.add(cvcLink, 2, row);
				}
	    }
	    //table.setRowColor(row, GRAY);
	    
	    ++row;
	    table.mergeCells(2, row, 3, row);
	    table.setAlignment(2, row, "right");
	    //table.setRowColor(row, GRAY);
	    table.add(new SubmitButton(iwrb.getLocalizedImageButton("creditcard.save", "Save"), ACTION, PARAMETER_VERIFY),2 ,row);

	    //table.setRowColor(1, backgroundColor);
	    
	    return row;
	}
	
  private void complete(IWContext iwc, Table table, int row) {
    String number = iwc.getParameter(PARAMETER_CC_NUMBER);
    String year   = iwc.getParameter(PARAMETER_CC_YEAR);
    String month  = iwc.getParameter(PARAMETER_CC_MONTH);
    String amount = iwc.getParameter(PARAMETER_AMOUNT);
    String cvc = iwc.getParameter(PARAMETER_CC_CVC);


	  try{
	  		if (!ccAuthEntry.getCardExpires().equals(month+year)) {
	  			CreditCardAuthorizationException e = new CreditCardAuthorizationException();
	  			e.setDisplayError(iwrb.getLocalizedString("travel.card_expire_date_wrong", "Creditcard expire date is incorrect."));
	  			throw e;
	  		}
	    System.out.println("Starting CreditCard test : "+IWTimestamp.RightNow().toString());
      number = number.replaceAll(" ", "");
      number = number.replaceAll("-", "");
	    String heimild = ccClient.doRefund(number,month,year,cvc,Float.parseFloat(amount),ccAuthEntry.getCurrency(), ccAuthEntry.getPrimaryKey(), ccAuthEntry.getExtraField());
	    //booking.setCreditcardAuthorizationNumber(heimild);
//	    booking.setIsValid(false);
	    getBooker(iwc).deleteBooking(booking, true);
//	    booking.store();
	    System.out.println("Ending CreditCard test : "+IWTimestamp.RightNow().toString());
	
	    table.add(getText(iwrb.getLocalizedString("travel.success","Success")),1,row);
	    table.mergeCells(1,row,2,row);
	    //table.setRowColor(row, backgroundColor);
	    ++row;
	    table.add(getText(iwrb.getLocalizedString("travel.credidcard_authorization_number","Creditcard authorization number")),1,row);
	    table.add(getText(heimild),2, row);
	    table.setAlignment(2, row, "right");

	  }
	  catch(CreditCardAuthorizationException e) {
	    String errMsge = e.getErrorMessage();
	    String errNumb = e.getErrorNumber();
	    String display = e.getDisplayError();
	
	    ++row;
	    table.add(getErrorText(iwrb.getLocalizedString("travel.error","Error")),1,row);
	    table.add(getErrorText(display),2, row);
	    
	    ++row;
	    table.setRowHeight(row, "12");
	    ++row;
	    table.add(new BackButton(iwrb.getLocalizedImageButton("travel.back", "Back")), 1, row);
	
	  }
	  catch (Exception e) {
	    table.add(getErrorText(iwrb.getLocalizedString("travel.error","Error")),1,row);
	    table.mergeCells(1,row,2,row);
	    //table.setRowColor(row, backgroundColor);
	
	    ++row;
	    table.add(getErrorText(iwrb.getLocalizedString("travel.unknown_error","Unknown error")),1,row);
			table.mergeCells(1, row, 2, row);

	    ++row;
	    table.setRowHeight(row, "12");
	    ++row;
	    table.add(new BackButton(iwrb.getLocalizedImageButton("travel.back", "Back")), 1, row);

	    e.printStackTrace(System.err);
	  }


  }

	private boolean isTooLateToRefund() {
		IWTimestamp dateOfBooking = new IWTimestamp(booking.getDateOfBooking());
		int hoursBetween = IWTimestamp.getHoursBetween(dateOfBooking, IWTimestamp.RightNow());
		
		return (hoursBetween > HOURS_BEFORE_REFUND_EXPIRES);
	}

	private boolean isPastBookingDate() {
		return IWTimestamp.RightNow().isLaterThan(new IWTimestamp(booking.getBookingDate()));
	}
	
	private void getReferenceNumberForm(IWContext iwc) {
		Form form = new Form();
		Table table = new Table();
		int row = 1;
		
		boolean notFound = iwc.isParameterSet(PARAMETER_REFERENCE_NUMBER);

		if (notFound) {
			form.add(getErrorText(iwrb.getLocalizedString("travel.booking_not_found","Booking not found")));
		}
		
		form.add(table);
		
		
		table.mergeCells(1, row, 2, row);
		//table.setRowColor(row, backgroundColor);
		table.add(getHeaderText(iwrb.getLocalizedString("travel.please_type_in_your_reference_number", "Please type in your reference number")), 1, row);
		
		
		++row;
		TextInput refNum = new TextInput(PARAMETER_REFERENCE_NUMBER);
		String emailedRefNum = iwc.getParameter(PARAMETER_EMAILED_REFERENCE_NUMBER);
		if (emailedRefNum != null) {
			refNum.setContent(emailedRefNum);
		}
		table.add(getText(iwrb.getLocalizedString("travel.reference_number", "Reference number")), 1, row);
		table.add(refNum, 2, row);
		//table.setRowColor(row, GRAY);
		
		++row;
		SubmitButton submit = new SubmitButton(iwrb.getLocalizedImageButton("travel.continue","Continue"));
		table.mergeCells(1, row, 2, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
		//table.setRowColor(row, GRAY);
		table.add(submit, 1, row);
		
		
		add(form);
	}
	
	public Text getHeaderText(String content) {
		Text text= new Text(content);
		if (headerStyle != null) {
			text.setFontStyle(headerStyle);
		}
		return text;
	}
	
	public Text getText(String content) {
		Text text = new Text(content);
		if (textStyle != null) {
			text.setFontStyle(textStyle);
		}
		return text;
	}
	
	public Text getErrorText(String content) {
		Text text = new Text(content);
		if (errorTextStyle != null) {
			text.setFontStyle(errorTextStyle);
		}
		return text;
	}
	
	public void setHeaderFontStyle(String style) {
		this.headerStyle = style;
	}

	public void setFontStyle(String style) {
		this.textStyle = style;
	}
	
	public void setErrorFontStyle(String style) {
		this.errorTextStyle = style;
	}
		
  public CreditCardBusiness getCreditCardBusiness(IWApplicationContext iwac) {
		try {
		return (CreditCardBusiness) IBOLookup.getServiceInstance(iwac, CreditCardBusiness.class);
		} catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}

  protected ProductBusiness getProductBusiness(IWApplicationContext iwac) {
  		try {
	    return (ProductBusiness) IBOLookup.getServiceInstance(iwac, ProductBusiness.class);
		} catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
  }
 
  protected Booker getBooker(IWApplicationContext iwac) {
  		try {
  			return (Booker) IBOLookup.getServiceInstance(iwac, Booker.class);
  		} catch (IBOLookupException e) {
  			throw new IBORuntimeException(e);
  		}
  }
  
}
