/*
 * Created on Jun 29, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package is.idega.idegaweb.travel.block.search.presentation;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ejb.FinderException;

import is.idega.idegaweb.travel.business.TravelSessionManager;
import is.idega.idegaweb.travel.business.TravelStockroomBusiness;
import is.idega.idegaweb.travel.data.GeneralBooking;
import is.idega.idegaweb.travel.data.GeneralBookingHome;
import is.idega.idegaweb.travel.presentation.LinkGenerator;
import is.idega.idegaweb.travel.presentation.PublicBooking;
import is.idega.idegaweb.travel.presentation.VoucherWindow;
import is.idega.idegaweb.travel.service.business.ServiceHandler;
import is.idega.idegaweb.travel.service.presentation.BookingForm;
import is.idega.idegaweb.travel.service.presentation.ServiceOverview;

import com.idega.block.text.data.TxText;
import com.idega.block.text.presentation.TextReader;
import com.idega.block.tpos.presentation.ReceiptWindow;
import com.idega.block.trade.data.Currency;
import com.idega.block.trade.stockroom.business.ProductBusiness;
import com.idega.block.trade.stockroom.data.PriceCategory;
import com.idega.block.trade.stockroom.data.PriceCategoryBMPBean;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.ProductHome;
import com.idega.block.trade.stockroom.data.ProductPrice;
import com.idega.block.trade.stockroom.data.ProductPriceBMPBean;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.block.trade.stockroom.data.SupplierHome;
import com.idega.block.trade.stockroom.data.Timeframe;
import com.idega.block.trade.stockroom.data.TravelAddress;
import com.idega.business.IBOLookup;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.InterfaceObject;
import com.idega.presentation.ui.ResultOutput;
import com.idega.presentation.ui.SelectionBox;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.TimeInput;
import com.idega.presentation.ui.TimestampInput;
import com.idega.util.IWTimestamp;

/**
 * @author gimmi
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public abstract class AbstractSearchForm extends Block{

	protected String ACTION = "bsf_a";
	protected String ACTION_SEARCH = "bsf_as";
	protected String ACTION_BOOKING_FORM = "bsf_bf";
	protected String ACTION_CONFIRM = "bsf_cm";

	protected static final int STATE_SHOW_SEARCH_FORM = 0;
	protected static final int STATE_SHOW_SEARCH_RESULTS = 1;
	protected static final int STATE_SHOW_BOOKING_FORM = 2;
	protected static final int STATE_CHECK_BOOKING = 3;
	protected int STATE = STATE_SHOW_SEARCH_FORM;

	protected String PARAMETER_POSTAL_CODE_NAME = "hs_pcn";
	protected String PARAMETER_FROM_DATE = BookingForm.parameterFromDate;//"hs_fd";
	protected String PARAMETER_TO_DATE = "hs_td";
	protected String ERROR_NO_BOOKING_COUNT = "ErrorNoBookingCount";
	/** Used for checkbooking */
	protected String PARAMETER_MANY_DAYS = BookingForm.parameterManyDays;
	protected String PARAMETER_ONLINE = BookingForm.parameterOnlineBooking; // false or true
	
	protected String PARAMETER_ADDRESS_ID = BookingForm.parameterDepartureAddressId;//"hs_aid";
	//protected String PARAMETER_TIMEFRAME_ID = "hs_tfid";
	protected String PARAMETER_PRODUCT_ID = "hs_pid";
	
	protected String PARAMETER_FIRST_NAME = BookingForm.PARAMETER_FIRST_NAME;//"hs_fna";
	protected String PARAMETER_LAST_NAME = BookingForm.PARAMETER_LAST_NAME;//"hs_lna";
	protected String PARAMETER_STREET = BookingForm.PARAMETER_ADDRESS;//"hs_st";
	protected String PARAMETER_POSTAL_CODE = BookingForm.PARAMETER_AREA_CODE;//"hs_pc";
	protected String PARAMETER_CITY = BookingForm.PARAMETER_CITY;//"hs_cit";
	protected String PARAMETER_COUNTRY = BookingForm.PARAMETER_COUNTRY;//"hs_cnt";
	protected String PARAMETER_EMAIL = BookingForm.PARAMETER_EMAIL;//"hs_em";
	protected String PARAMETER_CC_NUMBER = BookingForm.parameterCCNumber;//"hs_ccn";
	protected String PARAMETER_CC_MONTH = BookingForm.parameterCCMonth;//"hs_ccm";
	protected String PARAMETER_CC_YEAR = BookingForm.parameterCCYear;//"hs_ccy";
	protected String PARAMETER_COMMENT = BookingForm.PARAMETER_COMMENT;//"hs_comm";

	private IWContext iwc;

	protected String textFontStyle;
	protected String headerFontStyle;
	protected String linkFontStyle;
	protected String errorFontStyle;
	protected String headerBackgroundColor;
	protected String linkBackgroundColor;
	protected String backgroundColor;
	protected String width;
	protected String formInputStyle;
	
	protected IWResourceBundle iwrb;

	protected Image headerImage;
	protected Table formTable = new Table();
	int row = 1;
	
	private List errorFields = null;
	
	public AbstractSearchForm() {
		super();
	}
	
	protected abstract String getServiceName(IWResourceBundle iwrb);
	protected abstract void setupSearchForm();
	protected abstract void getResults() throws RemoteException;
	protected abstract Image getHeaderImage(IWResourceBundle iwrb);

	public void main(IWContext iwc) throws Exception {
		this.iwc = iwc;
		iwrb = getTravelSessionManager(iwc).getIWResourceBundle();

		formTable.setWidth("100%");
		formTable.setCellpadding(0);
		formTable.setCellspacing(3);
		if (backgroundColor != null) {
			formTable.setColor(backgroundColor);
		}
		
		Table outTable = new Table();
		if (width != null) {
			outTable.setWidth(width);
		}
		//outTable.setBorder(1);
		
		handleSubmit(iwc);
		
		Form form = new Form();
		form.maintainParameter(ServiceSearch.PARAMETER_SERVICE_SEARCH_FORM);
		form.add(getHeader());
		form.add(getLinks());
		setupPresentation(formTable);
		form.add(formTable);
		form.add(getButtons());
		outTable.add(form);
		
		super.add(outTable);
	}
	
	public void add(Object object) {
		formTable.add(object);
	}

	public void add(String string) {
		formTable.add(string);
	}
	
	public void add(PresentationObject po) {
		formTable.add(po);
	}

	protected Table getButtons() {

		Table table = new Table();
		table.setWidth("100%");
		
		if (STATE == STATE_SHOW_SEARCH_FORM) {
			
			SubmitButton search = new SubmitButton(iwrb.getLocalizedImageButton("search","Search"), ACTION, ACTION_SEARCH);
			
			table.add(search);
			table.setAlignment(1, 1, Table.HORIZONTAL_ALIGN_RIGHT);
		}
		
		return table;	
	}

	protected Table getLinks() {
		List searchForms = ServiceSearch.searchForms;
		
		Table table = new Table();
		table.setWidth("100%");
		table.setCellpadding(3);
		int column = 0;
		
		Link link;

		if (searchForms != null && !searchForms.isEmpty() ) {
			Iterator iter = searchForms.iterator();
			AbstractSearchForm bsf;
			while (iter.hasNext()) {
				bsf = (AbstractSearchForm) iter.next();
				link = new Link(getLinkText(bsf.getServiceName(iwrb)));
				link.addParameter(ServiceSearch.PARAMETER_SERVICE_SEARCH_FORM, bsf.getClassName());
				table.add(link, ++column, 1);
			}
		}	else {
			System.out.println(" no extra searchForms found" );
			link = new Link(getLinkText(this.getServiceName(iwrb)));
			link.addParameter(ServiceSearch.PARAMETER_SERVICE_SEARCH_FORM, this.getClassName());
			table.add(link, ++column, 1);
		}
		if (linkBackgroundColor != null) {
			table.setColor(linkBackgroundColor);				
		}
		return table;
	}

	protected Table getHeader() {
		Table table = new Table();
		table.setWidth("100%");
		table.setCellpaddingAndCellspacing(0);
		if (headerImage != null) {
			table.add(headerImage);
	//	if (getHeaderImage(iwrb) != null) {
	//		table.add(getHeaderImage(iwrb));
		} else {
			table.add(getHeaderText(getServiceName(iwrb)));
		}
		if (headerBackgroundColor != null) {
			table.setColor(headerBackgroundColor);				
		}
		return table;
	}

	protected void handleSubmit(IWContext iwc) {
		String action = iwc.getParameter(this.ACTION);
		if (action == null) {
			STATE = STATE_SHOW_SEARCH_FORM;
		} else if ( action.equals(this.ACTION_SEARCH)) {
			STATE = STATE_SHOW_SEARCH_RESULTS;
		} else if ( action.equals(this.ACTION_BOOKING_FORM)) {
			STATE = STATE_SHOW_BOOKING_FORM;
		} else if (action.equals(ACTION_CONFIRM)) {
			errorFields = getErrorFormFields();
			if (errorFields == null || errorFields.isEmpty() ) {
				STATE = STATE_CHECK_BOOKING;
			} else {
				STATE = STATE_SHOW_BOOKING_FORM;
			}
		}
	}

	protected void setupPresentation(Table table) throws RemoteException {
		switch (STATE) {
			case 0 :
				setupSearchForm();
				break;
			case STATE_SHOW_SEARCH_RESULTS :
				getResults();
				break;
			case STATE_SHOW_BOOKING_FORM :
					setupBookingForm();
				break;
			case STATE_CHECK_BOOKING :
				checkBooking();
				break;
		}
	}
			
	protected void setupBookingForm() {
		if (errorFields != null && !errorFields.isEmpty()) {
			Text error = getErrorText(iwrb.getLocalizedString("travek.search.fields_must_be_filled","Fields marked with * must be filled"));
			formTable.add(error, 1, row);
			formTable.mergeCells(1, row, 3, row);
			++row;
		}
		addInputLine(new String[]{iwrb.getLocalizedString("travel.search.first_name","First name"), iwrb.getLocalizedString("travel.search.last_name","Last name")}, new PresentationObject[]{new TextInput(PARAMETER_FIRST_NAME), new TextInput(PARAMETER_LAST_NAME)});
		formTable.mergeCells(2, (row-1), 3, (row-1));

		TextInput postalC = new TextInput(PARAMETER_POSTAL_CODE);
		postalC.setSize(6);
		addInputLine(new String[]{iwrb.getLocalizedString("travel.search.street","Street"),iwrb.getLocalizedString("travel.search.postal_code","Postal Code"), iwrb.getLocalizedString("travel.search.city","City")}, new PresentationObject[]{new TextInput(PARAMETER_STREET), postalC,new TextInput(PARAMETER_CITY)});

		addInputLine(new String[]{iwrb.getLocalizedString("travel.search.country","Country"), iwrb.getLocalizedString("travel.search.email_address","Email address")}, new PresentationObject[]{new TextInput(PARAMETER_COUNTRY), new TextInput(PARAMETER_EMAIL)});
		formTable.mergeCells(2, (row-1), 3, (row-1));

		TextInput expMonth = new TextInput(PARAMETER_CC_MONTH);
		expMonth.setSize(3);
		expMonth.setMaxlength(2);
		TextInput expYear = new TextInput(PARAMETER_CC_YEAR);
		expYear.setSize(3);
		expYear.setMaxlength(2);
		addInputLine(new String[]{iwrb.getLocalizedString("travel.search.credit_card_number","Credit card number"),iwrb.getLocalizedString("travel.search.expires_month","Expires month"), iwrb.getLocalizedString("travel.search.expires_year","Expires year")}, new PresentationObject[]{new TextInput(PARAMETER_CC_NUMBER), expMonth,expYear});

		TextArea comment = new TextArea(PARAMETER_COMMENT);
		comment.setWidth("350");
		comment.setHeight("50");
		addInputLine(new String[]{iwrb.getLocalizedString("travel.search.comment","Comment")}, new PresentationObject[]{comment});
		formTable.mergeCells(1, (row-1), 3, (row-1));

		IWTimestamp from = new IWTimestamp(iwc.getParameter(PARAMETER_FROM_DATE));
		IWTimestamp to = new IWTimestamp(iwc.getParameter(PARAMETER_TO_DATE));
		int betw = IWTimestamp.getDaysBetween(from, to);

		formTable.add(new HiddenInput(PARAMETER_ADDRESS_ID, iwc.getParameter(PARAMETER_ADDRESS_ID)));
		formTable.add(new HiddenInput(PARAMETER_PRODUCT_ID, iwc.getParameter(PARAMETER_PRODUCT_ID)));
		formTable.add(new HiddenInput(PARAMETER_ONLINE, "true"));
		formTable.add(new HiddenInput(PARAMETER_FROM_DATE, iwc.getParameter(PARAMETER_FROM_DATE)));
		formTable.add(new HiddenInput(PARAMETER_TO_DATE, iwc.getParameter(PARAMETER_TO_DATE)));
		formTable.add(new HiddenInput(PARAMETER_MANY_DAYS, Integer.toString(betw)));
		
		/** PriceCategories Begin*/

		try {
			ProductHome productHome = (ProductHome) IDOLookup.getHome(Product.class);
			Product product = productHome.findByPrimaryKey( new Integer(iwc.getParameter(PARAMETER_PRODUCT_ID)) );
	
			int addressId = -1;
			try {
				addressId = Integer.parseInt(iwc.getParameter(PARAMETER_ADDRESS_ID));
			} catch (Exception e) {}

			Timeframe tFrame = getServiceHandler(iwc).getProductBusiness().getTimeframe(product, from, addressId);
			int timeframeId = -1;
			ProductPrice[] prices = {};
			ProductPrice[] misc = {};
			if (tFrame != null) {
			  timeframeId = tFrame.getID();
			  prices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(product.getID(), tFrame.getID(), addressId, true);
			  misc = ProductPriceBMPBean.getMiscellaneousPrices(product.getID(), tFrame.getID(), addressId, true);
			}else {
			  prices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(product.getID(), -1, addressId, true);
			  misc = ProductPriceBMPBean.getMiscellaneousPrices(product.getID(), -1, addressId, true);
			}

			int pricesLength = prices.length;
			int miscLength = misc.length;
			ProductPrice[] pPrices = new ProductPrice[pricesLength+miscLength];
			for (int i = 0; i < pricesLength; i++) {
			  pPrices[i] = prices[i];
			}
			for (int i = 0; i < miscLength; i++) {
			  pPrices[i+pricesLength] = misc[i];
			}

			Table pTable;
			PriceCategory category;
			ResultOutput pPriceText;
		  ResultOutput TotalPassTextInput = new ResultOutput("total_pass","0");
			TotalPassTextInput.setSize(5);
		  ResultOutput TotalTextInput = new ResultOutput("total","0");
			TotalTextInput.setSize(8);
			TextInput pPriceMany;
			int numberOfDays = betw;
			
			for (int i = 0; i < pPrices.length; i++) {
				try {
					++row;
					category = pPrices[i].getPriceCategory();
					int price = (int) getServiceHandler(iwc).getServiceBusiness(product).getPrice(pPrices[i].getID(), product.getID(),pPrices[i].getPriceCategoryID(),pPrices[i].getCurrencyId(),IWTimestamp.getTimestampRightNow(), timeframeId, addressId);

					pPriceText = new ResultOutput("thePrice"+pPrices[i].getID(),"0");
					  pPriceText.setSize(8);

					pPriceMany = new TextInput("priceCategory"+pPrices[i].getID() ,"0");
					  pPriceMany.setSize(5);

					if (i == pricesLength) {
//					  Text tempTexti = (Text) theBoldText.clone();
//						tempTexti.setText(iwrb.getLocalizedString("travel.miscellaneous_services","Miscellaneous services"));
//						table.mergeCells(1, row, 2, row);
					  formTable.add(getText(iwrb.getLocalizedString("travel.miscellaneous_services","Miscellaneous services")), 1, row);
					  formTable.mergeCells(1, row, 3, row);
					  ++row;
					}else if (i == 0) {
					  //Text tempTexti = (Text) theBoldText.clone();
						//tempTexti.setText(iwrb.getLocalizedString("travel.basic_prices","Basic prices"));
						//tempTexti.setUnderline(true);
//						table.mergeCells(1, row, 2, row);
						if (errorFields != null && errorFields.contains(ERROR_NO_BOOKING_COUNT)) {
							formTable.add(getErrorText("* "), 1, row);
						}
					  formTable.add(getText(iwrb.getLocalizedString("travel.basic_prices","Basic prices")), 1, row);
					  formTable.mergeCells(1, row, 3, row);
					  ++row;
					}
					if (i >= pricesLength) {
					  pPriceMany.setName("miscPriceCategory"+pPrices[i].getID());
					}

					pPriceText.add(pPriceMany,ResultOutput.OPERATOR_MULTIPLY+price+ResultOutput.OPERATOR_MULTIPLY+numberOfDays);
					TotalPassTextInput.add(pPriceMany);
					TotalTextInput.add(pPriceMany,ResultOutput.OPERATOR_MULTIPLY+price+ResultOutput.OPERATOR_MULTIPLY+numberOfDays);


					formTable.add(getText(category.getName()), 1,row);

		//                  table.add(Text.NON_BREAKING_SPACE,2,row);

					pTable = new Table(4,1);
					  pTable.setWidth(1, Integer.toString(60));
					  pTable.setWidth(2, Integer.toString(90));
					  pTable.setWidth(3, Integer.toString(75));
					  pTable.setCellpaddingAndCellspacing(0);
					  pTable.add(pPriceMany,1,1);
					  if (numberOfDays > 1) {
							pTable.add(getText(Integer.toString(price)+" (* "+numberOfDays+" "+iwrb.getLocalizedString("travel.search.days","days")+")"),2,1);
					  } else {
					  	pTable.add(getText(Integer.toString(price)),2,1);
					  }
					  pTable.add(pPriceText, 3,1);


		//                    pTable.add();
					formTable.add(pTable, 2, row);
					formTable.mergeCells(2, row, 3, row);

				}catch (SQLException sql) {
				  sql.printStackTrace(System.err);
				}catch (FinderException fe) {
				  fe.printStackTrace(System.err);
				}
			}			

			++row;
			formTable.mergeCells(1,row,3,row);
			++row;

			formTable.add(getText(iwrb.getLocalizedString("travel.total","Total")),1,row);

			pTable = new Table(3,1);
			  pTable.setWidth(1, Integer.toString(60));
			  pTable.setWidth(2, Integer.toString(90));
			  pTable.setWidth(3, Integer.toString(75));
			  pTable.setCellpaddingAndCellspacing(0);

			pTable.add(TotalPassTextInput,1,1);
			pTable.add(TotalTextInput,3,1);
			formTable.add(pTable, 2, row);			
			formTable.mergeCells(2, row, 3, row);
		}catch (Exception e) {
			e.printStackTrace();
		}

		/** PriceCategories End*/
		++row;
		SubmitButton submit = new SubmitButton(iwrb.getLocalizedImageButton("travel.search.confirm","Confirm"), ACTION, ACTION_CONFIRM);
		formTable.setAlignment(3, row, Table.HORIZONTAL_ALIGN_RIGHT);
		formTable.add(submit, 3, row);
		//formTable.setBorder(1);
	}
	
	protected void checkBooking() throws RemoteException {
		ProductHome productHome = (ProductHome) IDOLookup.getHome(Product.class);
		try {
			Product product = productHome.findByPrimaryKey( new Integer(iwc.getParameter(PARAMETER_PRODUCT_ID)) );
			BookingForm bf = this.getServiceHandler(iwc).getBookingForm(iwc, product);
			int bookingId = bf.checkBooking(iwc, true);
			GeneralBookingHome gBookingHome = (GeneralBookingHome) IDOLookup.getHome(GeneralBooking.class);
			GeneralBooking gBooking = null;
			boolean inquirySent = (bookingId == BookingForm.inquirySent); 
			
			if (bookingId > 0) {
				gBooking = gBookingHome.findByPrimaryKey(new Integer(bookingId));	
			}
			
			if (gBooking != null) {
			  boolean sendEmail = PublicBooking.sendEmails(iwc, gBooking, iwrb);

			  formTable.add(getText(gBooking.getName()));
			  formTable.add(getText(", "));
			  formTable.add(getText(iwrb.getLocalizedString("travel.you_booking_has_been_confirmed","your booking has been confirmed.")));
			  formTable.add(Text.BREAK);
			  formTable.add(Text.BREAK);
			  if (sendEmail) {
				formTable.add(getText(iwrb.getLocalizedString("travel.you_will_reveice_an_email_shortly","You will receive an email shortly confirming your booking.")));
				formTable.add(Text.BREAK);
				formTable.add(Text.BREAK);
			  }
			  formTable.add(getText(iwrb.getLocalizedString("travel.your_credidcard_authorization_number_is","Your creditcard authorization number is")));
			  formTable.add(getText(" : "));
			  formTable.add(getText(gBooking.getCreditcardAuthorizationNumber()));
			  formTable.add(Text.BREAK);
			  formTable.add(getText(iwrb.getLocalizedString("travel.your_reference_number_is","Your reference number is")));
			  formTable.add(getText(" : "));
			  formTable.add(getText(gBooking.getReferenceNumber()));
			  formTable.add(Text.BREAK);
			  //table.add(getText(gBooking.getReferenceNumber()));
			  //table.add(Text.BREAK);
			  formTable.add(Text.BREAK);
			  formTable.add(getText(iwrb.getLocalizedString("travel.if_unable_to_print","If you are unable to print the voucher, write the reference number down else proceed to printing the voucher.")));



			  Link printVoucher = new Link(getText(iwrb.getLocalizedString("travel.print_voucher","Print voucher")));
				printVoucher.addParameter(VoucherWindow.parameterBookingId, gBooking.getID());
				printVoucher.setWindowToOpen(VoucherWindow.class);

			  if (bf._TPosClient != null) {
			  	Supplier supplier = ( (SupplierHome) IDOLookup.getHome(Supplier.class)).findByPrimaryKey(new Integer(gBooking.getService().getProduct().getSupplierId()));
					com.idega.block.tpos.presentation.Receipt r = new com.idega.block.tpos.presentation.Receipt(bf._TPosClient, supplier);
					iwc.setSessionAttribute(ReceiptWindow.RECEIPT_SESSION_NAME, r);

					Link printCCReceipt = new Link(getText(iwrb.getLocalizedString("travel.print_cc_receipt","Print creditcard receipt")));
				  printCCReceipt.setWindowToOpen(ReceiptWindow.class);
					formTable.add(Text.NON_BREAKING_SPACE+Text.NON_BREAKING_SPACE, 1,2);
					formTable.add(printCCReceipt, 1, 2);
			  }

			  formTable.add(printVoucher,1,3);
			  formTable.setAlignment(1,1,"left");
			  formTable.setAlignment(1,2,"right");
			  formTable.setAlignment(1,3,"right");
			}else if (inquirySent) {
				formTable.add(getText(iwrb.getLocalizedString("travel.inquiry_has_been_sent","Inquiry has been sent")));
				formTable.add(Text.BREAK);
				formTable.add(getText(iwrb.getLocalizedString("travel.you_will_reveice_an_confirmation_email_shortly","You will receive an confirmation email shortly.")));
			}else {
				formTable.add(getText(iwrb.getLocalizedString("travek.booking_failed","Booking failed")));
			  if (gBooking == null) {
					debug("gBooking == null");
			  }
			}
		} catch (Exception e) {
			formTable.add(getText(iwrb.getLocalizedString("travek.booking_failed","Booking failed")+" ( "+e.getMessage()+" )"));
			e.printStackTrace(System.err);
		}
	}
		
	protected Collection checkResults(IWContext iwc, Collection results) throws RemoteException {
		if (results != null && !results.isEmpty()) {
			Collection coll = new Vector();
			ProductHome pHome = (ProductHome) IDOLookup.getHome(Product.class);
			Product product;
			IWTimestamp from = null;
			IWTimestamp to = null;
			IWTimestamp tmp;
			try {
				from = new IWTimestamp(iwc.getParameter(PARAMETER_FROM_DATE));
				to = new IWTimestamp(iwc.getParameter(PARAMETER_TO_DATE));
			}catch (Exception e) {
				System.out.println("error getting stamps : "+e.getMessage());
			}
			TravelStockroomBusiness bus;
			Iterator iter = results.iterator();
			boolean productIsValid = true;
			while (iter.hasNext() && from != null && to != null) {
				try {
					product = pHome.findByPrimaryKey(iter.next());
					bus = getBusiness(iwc, product);
					tmp = new IWTimestamp(from);
					productIsValid = true;
					while ( tmp.isEarlierThan(to) && productIsValid) {
						productIsValid = bus.getIfDay(iwc, product, tmp);
						tmp.addDays(1);
					}
					if (productIsValid) {
						coll.add(product.getPrimaryKey());
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			return coll;
		}
		return results;
	}
	
	protected List getErrorFormFields() {
		List list = new Vector();
		String firstName = iwc.getParameter(PARAMETER_FIRST_NAME);
		String lastName = iwc.getParameter(PARAMETER_LAST_NAME);
		String street = iwc.getParameter(PARAMETER_STREET);
		String pc = iwc.getParameter(PARAMETER_POSTAL_CODE);
		String city = iwc.getParameter(PARAMETER_CITY);
		String country = iwc.getParameter(PARAMETER_COUNTRY);
		String email = iwc.getParameter(PARAMETER_EMAIL);
		String ccNum = iwc.getParameter(PARAMETER_CC_NUMBER);
		String ccMon = iwc.getParameter(PARAMETER_CC_MONTH);
		String ccYear = iwc.getParameter(PARAMETER_CC_YEAR);
		
		if (firstName == null || firstName.equals("")) {
			list.add(PARAMETER_FIRST_NAME);
		}
		if (lastName == null || lastName.equals("")) {
			list.add(PARAMETER_LAST_NAME);
		}
		if (street == null || street.equals("")) {
			list.add(PARAMETER_STREET);
		}
		if (pc == null || pc.equals("")) {
			list.add(PARAMETER_POSTAL_CODE);
		}
		if (city == null || city.equals("")) {
			list.add(PARAMETER_CITY);
		}
		if (country == null || country.equals("")) {
			list.add(PARAMETER_COUNTRY);
		}
		if (email == null || email.equals("")) {
			list.add(PARAMETER_EMAIL);
		}
		if (ccNum == null || ccNum.equals("")) {
			list.add(PARAMETER_CC_NUMBER);
		}
		if (ccMon == null || ccMon.equals("")) {
			list.add(PARAMETER_CC_MONTH);
		}
		if (ccYear == null || ccYear.equals("")) {
			list.add(PARAMETER_CC_YEAR);
		}
		
		String productId = iwc.getParameter(PARAMETER_PRODUCT_ID);
		
		ProductPrice[] pPrices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(Integer.parseInt(productId), -1, -1, true);
		int iMany = 0;
		for (int i = 0; i < pPrices.length; i++) {
		  try {
			iMany += Integer.parseInt(iwc.getParameter("priceCategory"+pPrices[i].getID()));
		  }catch (NumberFormatException n) {
		  }
		}		
		
		if (iMany < 1) {
			list.add(ERROR_NO_BOOKING_COUNT);
		}
		
		return list;
	}
	
	protected void listResults(IWContext iwc, Collection results) throws RemoteException{
		if (results != null && !results.isEmpty()) {
			ProductHome pHome = (ProductHome) IDOLookup.getHome(Product.class);
			// TODO move to a better location
			IWTimestamp stamp = new IWTimestamp(iwc.getParameter(PARAMETER_FROM_DATE));

			TravelStockroomBusiness bus;
			Product product;
			Link link;
			List addresses;
			Timeframe timeframe;
			ProductPrice[] prices;
			Currency currency;
			Timeframe[] timeframes;
			Iterator iter = results.iterator();
			while (iter.hasNext()) {
				try {
					product = pHome.findByPrimaryKey(iter.next());
					bus = getServiceHandler(iwc).getServiceBusiness(product);
					addresses = getServiceHandler(iwc).getProductBusiness().getDepartureAddresses(product, stamp, true);

					Table table = new Table();
					int row = 1;
					table.add(product.getProductName(iwc.getCurrentLocaleId()), 1, row);
					//description
					++row;
					TxText descriptionText = product.getText();
					if (descriptionText != null) {
						System.out.println("product.getText().getID() = "+descriptionText.getID());
					  TextReader textReader = new TextReader(descriptionText.getID());
						textReader.setHeadlineStyle("font-family:Arial,Helvetica,sans-serif;font-size:12pt;font-weight:bold;");
						textReader.setTextStyle("font-family:Arial,Helvetica,sans-serif;font-size:8pt;");
						textReader.setCacheable(false);
					  table.add(textReader,1,row);//insert a textreader
					  //content.mergeCells(1,y,2,y);
					} else {
						System.out.println("product.getText() == null");
					}
					
					if (addresses == null || addresses.isEmpty()) {
						++row;
						int addressId = -1;
						timeframe = getServiceHandler(iwc).getProductBusiness().getTimeframe(product, stamp, addressId);
						int timeframeId = -1;
						if (timeframe != null) {
							timeframeId = timeframe.getID();
						}
						row = getPrices(iwc, stamp, bus, product, table, row, addressId, timeframeId);
						link = getBookingLink(product.getID());
						link.addParameter(PARAMETER_ADDRESS_ID, -1);
						//link.addParameter(PARAMETER_TIMEFRAME_ID, timeframeId);
						
						table.add(link, 1, row);
					} else {
						TravelAddress address;
						Iterator addressesIter = addresses.iterator();
						while (addressesIter.hasNext()) {
							address = (TravelAddress) addressesIter.next();
							timeframe = getServiceHandler(iwc).getProductBusiness().getTimeframe(product, stamp, address.getAddressId());
							int timeframeId = -1;
							if (timeframe != null) {
								timeframeId = timeframe.getID();
							}

							++row;
							table.add(getText(address.getName()), 1, row);
							link = getBookingLink(product.getID());
							link.addParameter(PARAMETER_ADDRESS_ID, address.getAddressId());
							//link.addParameter(PARAMETER_TIMEFRAME_ID, timeframeId);
							table.add(link, 2, row);
							row =getPrices(iwc, stamp, bus, product, table, row, address.getAddressId(), timeframeId);
						}
						
					}
					
					add(table);

				}catch(Exception e) {
					e.printStackTrace();
				} 
			}
				
		}
	}
	
	protected Link getBookingLink(int productId) {
		Link link = new Link(iwrb.getLocalizedImageButton("travel.book","Book"));
//		Link link = new Link(getLinkText(iwrb.getLocalizedString("travel.book","Book")));
		link.maintainParameter(ServiceSearch.PARAMETER_SERVICE_SEARCH_FORM, iwc);
		link.maintainParameter(PARAMETER_FROM_DATE, iwc);
		link.maintainParameter(PARAMETER_TO_DATE, iwc);
		link.addParameter(ACTION, ACTION_BOOKING_FORM);
		link.addParameter(PARAMETER_PRODUCT_ID, productId);
		return link;
	}

	private int getPrices(IWContext iwc, IWTimestamp stamp, TravelStockroomBusiness bus, Product product, Table table, int row, int addressId, int timeframeId) throws RemoteException, SQLException {
		ProductPrice[] prices;
		Currency currency;
		prices = ProductPriceBMPBean.getProductPrices(product.getID(), timeframeId, addressId, new int[] {PriceCategoryBMPBean.PRICE_VISIBILITY_PUBLIC, PriceCategoryBMPBean.PRICE_VISIBILITY_BOTH_PRIVATE_AND_PUBLIC});
		
		table.add(getText(iwrb.getLocalizedString("travel.prices","Prices")+":"), 1, row);
		++row;
		
		for (int i = 0; i < prices.length; i++) {
			try {
				currency = ((com.idega.block.trade.data.CurrencyHome)com.idega.data.IDOLookup.getHomeLegacy(Currency.class)).findByPrimaryKeyLegacy(prices[i].getCurrencyId());
			}catch (Exception e) {
				currency = null;
			}
			table.add(getText(prices[i].getPriceCategory().getName()+" : "+bus.getPrice(prices[i].getID(),((Integer) product.getPrimaryKey()).intValue(),prices[i].getPriceCategoryID() , prices[i].getCurrencyId(), IWTimestamp.getTimestampRightNow(), timeframeId, -1 )+Text.NON_BREAKING_SPACE+currency.getCurrencyAbbreviation()), 1, row++);
		}
		return row;
	}

	protected void addInputLine(String[] text, PresentationObject[] object) {
		for (int i = 0; i < text.length; i++) {
			formTable.add(getText(text[i]), i+1, row);
		}
		++row;
		String value;
		for (int i = 0; i < object.length; i++) {
			value = iwc.getParameter(object[i].getName());
			if (value != null) {
				if (object[i] instanceof DropdownMenu) {
					((DropdownMenu)object[i]).setSelectedElement(value);
				} else if (object[i] instanceof SelectionBox) {
					String values[] = iwc.getParameterValues(object[i].getName());
					((SelectionBox)object[i]).setSelectedElements(values);
				} else if (object[i] instanceof DateInput) {
					try {
						String year = value.substring(0, 4);
						String month = value.substring(5, 7);
						String day = value.substring(8, 10);
						((DateInput)object[i]).setYear(year);
						((DateInput)object[i]).setMonth(month);
						((DateInput)object[i]).setDay(day);
					}catch (Exception e) {
						System.out.println("Error changing setting dateinputs");
					}
				} else {
					try {
						((InterfaceObject)object[i]).setContent(value);
					}catch (Exception e) {
						System.out.println("Error changing po to io");
					}
				}
			}

			if ( errorFields != null && errorFields.contains(object[i].getName())) {
				formTable.add(getErrorText("*"), i+1, row);
			}
			if (formInputStyle != null) {
				object[i].setStyleAttribute(formInputStyle);
			}
			formTable.add(object[i], i+1, row);
		}
		++row;
	}
	
	
	protected Text getText(String content) {
		Text text = new Text(content);
		if (textFontStyle != null) {
			text.setFontStyle(textFontStyle);
		}
		return text;
	}

	protected Text getHeaderText(String content) {
		Text text = new Text(content);
		if (headerFontStyle != null) {
			text.setFontStyle(headerFontStyle);
		}
		return text;
	}
	
	protected Text getLinkText(String content) {
		Text text = new Text(content);
		if (linkFontStyle != null) {
			text.setFontStyle(linkFontStyle);
		}
		return text;
	}

	protected Text getErrorText(String content) {
		Text text = new Text(content);
		if (errorFontStyle != null) {
			text.setFontStyle(errorFontStyle);
		}
		return text;
	}
	
	public void setTextFontStyle(String fontStyle) {
		this.textFontStyle = fontStyle;
	}
	
	public void setHeaderFontStyle(String fontStyle) {
		this.headerFontStyle = fontStyle;
	}
	
	public void setLinkFontStyle(String fontStyle) {
		this.linkFontStyle = fontStyle;
	}

	public void setErrorFontStyle(String fontStyle) {
		this.errorFontStyle = fontStyle;
	}
		
	public void setHeaderImage(Image image) {
		this.headerImage = image;
	}
	
	public void setHeaderBackgroundColor(String color) {
		this.headerBackgroundColor = color;
	}
	
	public void setLinksBackgroundColor(String color) {
		this.linkBackgroundColor = color;
	}
	
	public void setBackgroundColor(String color) {
		this.backgroundColor = color;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public void setFormInputStyle(String style) {
		this.formInputStyle = style;
	}

	protected TravelSessionManager getTravelSessionManager(IWUserContext iwuc) throws RemoteException {
		return (TravelSessionManager) IBOLookup.getSessionInstance(iwuc, TravelSessionManager.class);
	}
	
	protected TravelStockroomBusiness getBusiness(IWContext iwc, Product product) throws RemoteException, FinderException {
		return getServiceHandler(iwc).getServiceBusiness(product);
	}
	
	protected ServiceHandler getServiceHandler(IWApplicationContext iwac) throws RemoteException {
	return (ServiceHandler) IBOLookup.getServiceInstance(iwac, ServiceHandler.class);
}
}
