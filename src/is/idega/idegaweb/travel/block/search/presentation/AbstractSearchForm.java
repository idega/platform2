package is.idega.idegaweb.travel.block.search.presentation;

import is.idega.idegaweb.travel.block.search.business.InvalidSearchException;
import is.idega.idegaweb.travel.block.search.business.ServiceSearchBusiness;
import is.idega.idegaweb.travel.block.search.data.ServiceSearchEngine;
import is.idega.idegaweb.travel.business.TravelStockroomBusiness;
import is.idega.idegaweb.travel.data.GeneralBooking;
import is.idega.idegaweb.travel.data.GeneralBookingHome;
import is.idega.idegaweb.travel.presentation.LinkGenerator;
import is.idega.idegaweb.travel.presentation.PublicBooking;
import is.idega.idegaweb.travel.presentation.TravelBlock;
import is.idega.idegaweb.travel.presentation.TravelCurrencyCalculatorWindow;
import is.idega.idegaweb.travel.presentation.TravelWindow;
import is.idega.idegaweb.travel.presentation.VoucherWindow;
import is.idega.idegaweb.travel.service.presentation.BookingForm;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ejb.FinderException;
import javax.mail.MessagingException;

import com.idega.block.creditcard.business.CreditCardBusiness;
import com.idega.block.creditcard.business.TPosException;
import com.idega.block.text.data.TxText;
import com.idega.block.text.presentation.TextReader;
import com.idega.block.trade.data.Currency;
import com.idega.block.trade.stockroom.business.ProductComparator;
import com.idega.block.trade.stockroom.data.PriceCategory;
import com.idega.block.trade.stockroom.data.PriceCategoryBMPBean;
import com.idega.block.trade.stockroom.data.PriceCategoryHome;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.ProductHome;
import com.idega.block.trade.stockroom.data.ProductPrice;
import com.idega.block.trade.stockroom.data.ProductPriceBMPBean;
import com.idega.block.trade.stockroom.data.ProductPriceHome;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.block.trade.stockroom.data.SupplierHome;
import com.idega.block.trade.stockroom.data.Timeframe;
import com.idega.block.trade.stockroom.data.TravelAddress;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.builder.business.BuilderService;
import com.idega.core.builder.business.BuilderServiceFactory;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.InterfaceObject;
import com.idega.presentation.ui.SelectPanel;
import com.idega.presentation.ui.SelectionBox;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWTimestamp;
import com.idega.util.SendMail;
import com.idega.util.text.TextSoap;

/**
 * @author gimmi
 */
public abstract class AbstractSearchForm extends TravelBlock{

	protected String ACTION = "bsf_a";
	protected String ACTION_SEARCH = "bsf_as";
	protected String ACTION_BOOKING_FORM = "bsf_bf";
	protected String ACTION_CONFIRM = "bsf_cm";

	protected static final int STATE_SHOW_SEARCH_FORM = 0;
	protected static final int STATE_SHOW_SEARCH_RESULTS = 1;
	protected static final int STATE_SHOW_BOOKING_FORM = 2;
	protected static final int STATE_CHECK_BOOKING = 3;
	protected int STATE = STATE_SHOW_SEARCH_FORM;

	protected String PARAMETER_TYPE = "hs_pt";
	protected String PARAMETER_TYPE_COUNT = "hs_ptc";

	public static String PARAMETER_POSTAL_CODE_NAME = "hs_pcn";
	public static String PARAMETER_FROM_DATE = BookingForm.parameterFromDate;//"hs_fd";
	public static String ERROR_NO_BOOKING_COUNT = "ErrorNoBookingCount";
	/** Used for checkbooking */
	public static String PARAMETER_MANY_DAYS = BookingForm.parameterManyDays;
	public static String PARAMETER_ONLINE = BookingForm.parameterOnlineBooking; // false or true
	
	public static String PARAMETER_ADDRESS_ID = BookingForm.parameterDepartureAddressId;//"hs_aid";
	public static String PARAMETER_PRODUCT_ID = "hs_pid";
	public static String PARAMETER_PRODUCT_PRICE_ID = "hs_ppid";
	
	public static String PARAMETER_FIRST_NAME = BookingForm.PARAMETER_FIRST_NAME;//"hs_fna";
	public static String PARAMETER_LAST_NAME = BookingForm.PARAMETER_LAST_NAME;//"hs_lna";
	public static String PARAMETER_STREET = BookingForm.PARAMETER_ADDRESS;//"hs_st";
	public static String PARAMETER_POSTAL_CODE = BookingForm.PARAMETER_AREA_CODE;//"hs_pc";
	public static String PARAMETER_CITY = BookingForm.PARAMETER_CITY;//"hs_cit";
	public static String PARAMETER_COUNTRY = BookingForm.PARAMETER_COUNTRY;//"hs_cnt";
	public static String PARAMETER_EMAIL = BookingForm.PARAMETER_EMAIL;//"hs_em";
	public static String PARAMETER_CC_NUMBER = BookingForm.parameterCCNumber;//"hs_ccn";
	public static String PARAMETER_CC_MONTH = BookingForm.parameterCCMonth;//"hs_ccm";
	public static String PARAMETER_CC_YEAR = BookingForm.parameterCCYear;//"hs_ccy";
	public static String PARAMETER_CC_CVC = BookingForm.parameterCCCVC;
	public static String PARAMETER_COMMENT = BookingForm.PARAMETER_COMMENT;//"hs_comm";
	public static String PARAMETER_REFERER_URL = PublicBooking.PARAMETER_REFERRAL_URL;

	private IWContext iwc;

	protected String textFontStyle;
	protected String headerFontStyle;
	protected String linkFontStyle;
	protected String clickedLinkFontStyle;
	protected String errorFontStyle;
	protected String headerBackgroundColor;
	protected String linkBackgroundColor;
	protected String backgroundColor;
	protected String width;
	protected String formInputStyle;
	protected Image windowHeaderImage;
	protected boolean cvcIsUsed = true;
	protected IWResourceBundle iwrb;
	protected IWBundle bundle;
	protected ServiceSearchEngine engine = null;
	
	protected Image headerImage;
	protected Table formTable = new Table();
	protected int row = 1;
	protected boolean useSecureServer = true;
	int tmpPriceID;
	
	protected Product definedProduct;
	
	private List errorFields = null;
	private List searchForms = null;
	
	public AbstractSearchForm() {
		super();
	}

	public synchronized Object clone() {
		AbstractSearchForm obj = (AbstractSearchForm) super.clone();
		obj.searchForms = searchForms;

		return obj;
	}
	
	protected abstract String getServiceName(IWResourceBundle iwrb);
	protected abstract void setupSearchForm();
	protected abstract void getResults() throws RemoteException, InvalidSearchException;
	protected abstract Image getHeaderImage(IWResourceBundle iwrb);
	protected abstract String getPriceCategoryKey();
	protected abstract String getUnitName();
	
	
	protected abstract String getParameterTypeCountName();

	private void init(IWContext iwc) throws RemoteException {
		this.iwc = iwc;
		iwrb = getSearchBusiness(iwc).getTravelSessionManager(iwc).getIWResourceBundle();
		bundle = getSearchBusiness(iwc).getTravelSessionManager(iwc).getIWBundle();
		formTable.setWidth("100%");
		formTable.setCellpadding(0);
		formTable.setCellspacing(3);
		if (backgroundColor != null) {
			formTable.setColor(backgroundColor);
		}
		
		definedProduct = getProduct();
		if (definedProduct != null) {
			cvcIsUsed = getCreditCardBusiness(iwc).getUseCVC(definedProduct.getSupplier(), IWTimestamp.RightNow());
		}
	}
	
	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		init(iwc);
		
		Table outTable = new Table();
		if (width != null) {
			outTable.setWidth(width);
		}
		//outTable.setBorder(1);
		
		handleSubmit(iwc);
		
		Form form = new Form();
		form.maintainParameter(ServiceSearch.PARAMETER_SERVICE_SEARCH_FORM);
		form.addParameter(BookingForm.PARAMETER_CODE, engine.getCode());
		form.addParameter(BookingForm.parameterPriceCategoryKey, getPriceCategoryKey());
		form.add(getHeader());
		form.add(getLinks());
		form.add(getText());
		formTable.add(Text.NON_BREAKING_SPACE, 1, row);
		++row;
		
		if (useSecureServer && !iwc.isSecure()) {
			String URL = iwc.getRequest().getRequestURL().toString();
			String serverName = bundle.getProperty(LinkGenerator.PROPERTY_SERVER_NAME);
			if (URL.indexOf("nat.sidan.is") >= 0 && serverName != null) {
				URL = URL.replaceFirst("nat.sidan.is",  serverName);
			}
			URL = URL.replaceFirst("http", "https");
			
			Link secureLink = new Link(getErrorText(iwrb.getLocalizedString("travel.click_here", "CLICK HERE")), URL+"?"+iwc.getQueryString());
			form.add(getErrorText(iwrb.getLocalizedString("travel.click_here_to_switch_to_secure_mode","You are not using our secure form. To switch to secure mode please")+" "));
			form.add(secureLink);
		}

		setupPresentation();
		form.add(formTable);
		form.add(getButtons());
		outTable.add(form);
		outTable.add(Text.BREAK);
		outTable.add(addTermsAndConditionsAndVerisign());
		if (definedProduct != null && (isInPermissionGroup(iwc) || isAdministrator(iwc))) {
			Link link = getDirectBookingLink();
			outTable.add(link);
		} 
		
		super.add(outTable);
	}
	
	protected Link getVerisign() {
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

		return verisign;
	}
	
	protected Link getDirectBookingLink() {
		String http = "http";
		if (LinkGenerator.getIsHttps()) {
			http = "https";
		}
		StringBuffer text = new StringBuffer(http+"://"+iwc.getServerName());
		if (!http.equals("https")) {
			text.append(":"+iwc.getServerPort());
		}

		String url = iwc.getIWMainApplication().getBuilderServletURI();//+"&"+PARAMETER_PRODUCT_ID+"="+definedProduct.getPrimaryKey().toString();
		
		try {
			BuilderService bs = BuilderServiceFactory.getBuilderService(iwc);
			text.append(bs.getPageURI(iwc.getCurrentIBPageID()));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		Link link = new Link(text.toString()+"&"+PARAMETER_PRODUCT_ID+"="+definedProduct.getPrimaryKey().toString(), text.toString());
		link.addParameter(PARAMETER_PRODUCT_ID, definedProduct.getPrimaryKey().toString());
		link.addParameter(ServiceSearch.PARAMETER_SERVICE_SEARCH_FORM, IWMainApplication.getEncryptedClassName(this.getClassName()));
		return link;
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

			if (definedProduct == null) {
				SubmitButton search = new SubmitButton(iwrb.getLocalizedImageButton("search","Search"), ACTION, ACTION_SEARCH);
				table.add(search);
			} else {
				table.add(new HiddenInput(PARAMETER_PRODUCT_ID, definedProduct.getPrimaryKey().toString()));
				SubmitButton book = new SubmitButton(iwrb.getLocalizedImageButton("book","Book"), ACTION, ACTION_BOOKING_FORM);
				table.add(book);
			}
			
			table.setAlignment(1, 1, Table.HORIZONTAL_ALIGN_RIGHT);
		}
		
		return table;	
	}

	protected Table getLinks() {
//		List searchForms = ServiceSearch.searchForms;
		
		Table table = new Table();
		table.setWidth("100%");
		table.setCellpadding(3);
		int column = 0;
		
		Link link;

		if (searchForms != null && !searchForms.isEmpty() ) {
			Iterator iter = searchForms.iterator();
			AbstractSearchForm bsf;
			String currentSearchFormName = getClassName();
			while (iter.hasNext()) {
				bsf = (AbstractSearchForm) iter.next();
				if ( bsf.getClassName().equals(currentSearchFormName) ){
					link = new Link(getLinkText(bsf.getServiceName(iwrb), true));
				} else {
					link = new Link(getLinkText(bsf.getServiceName(iwrb), false));
				}
				link.addParameter(ServiceSearch.PARAMETER_SERVICE_SEARCH_FORM, IWMainApplication.getEncryptedClassName(bsf.getClassName()));
				table.add(link, ++column, 1);
				table.setNoWrap(column, 1);
				table.setAlignment(column, 1, Table.HORIZONTAL_ALIGN_LEFT);
				table.setWidth(column, 1);
				table.setWidth(++column, 1);
			}
		}	else {
			System.out.println(" no extra searchForms found" );
			link = new Link(getLinkText(this.getServiceName(iwrb), true));
			link.addParameter(ServiceSearch.PARAMETER_SERVICE_SEARCH_FORM, this.getClassName());
			table.add(link, ++column, 1);
		}
		Link currLink = new Link(getLinkText(iwrb.getLocalizedString("travel.search.curreny_calculator","Currencies"), false));
		currLink.setWindowToOpen(TravelCurrencyCalculatorWindow.class);
		if (windowHeaderImage != null) {
			TravelCurrencyCalculatorWindow.setHeaderImage(currLink, windowHeaderImage.getDefaultImageID());
		}
		table.add(currLink, ++column, 1);
		table.setAlignment(column, 1, Table.HORIZONTAL_ALIGN_RIGHT);
		
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
	protected Table getText(){
		Text text = getText("All bookings directly from suppliers");
		Table table = new Table();
		table.setWidth("100%");
		table.setCellpaddingAndCellspacing(0);
		table.add(text, 1, 1);
		table.setAlignment(1, 1, Table.HORIZONTAL_ALIGN_RIGHT);
		
		return table;
		
	}

	protected void handleSubmit(IWContext iwc) throws RemoteException {
		String action = iwc.getParameter(this.ACTION);
		if (action == null) {
			STATE = STATE_SHOW_SEARCH_FORM;
		} else if ( action.equals(this.ACTION_SEARCH)) {
			STATE = STATE_SHOW_SEARCH_RESULTS;
		} else if ( action.equals(this.ACTION_BOOKING_FORM)) {
			STATE = STATE_SHOW_BOOKING_FORM;
		} else if (action.equals(ACTION_CONFIRM)) {
			errorFields = getSearchBusiness(iwc).getErrorFormFields(iwc, getPriceCategoryKey(), cvcIsUsed);
			if (errorFields == null || errorFields.isEmpty() ) {
				STATE = STATE_CHECK_BOOKING;
			} else {
				STATE = STATE_SHOW_BOOKING_FORM;
			}
		}
	}

	protected void setupPresentation() throws RemoteException {
		switch (STATE) {
			case 0 :
				setupSearchForm();
				break;
			case STATE_SHOW_SEARCH_RESULTS :
				try {
					getResults();
				} catch (InvalidSearchException i) {
					System.out.println("AbstractSearchForm : InvalidSearchException : "+i.getMessage());
					errorFields = i.getErrorFields();
					STATE = STATE_SHOW_SEARCH_FORM;
					addErrorWarning();
					setupSearchForm();
				}
				break;
			case STATE_SHOW_BOOKING_FORM :
				try {
					setupBookingForm();
				}catch (Exception e) {
					e.printStackTrace(System.err);
				}
				break;
			case STATE_CHECK_BOOKING :
				checkBooking();
				break;
		}
	}
	
	
	private void addErrorWarning() {
		if (errorFields != null && !errorFields.isEmpty()) {
			Text error = getErrorText(iwrb.getLocalizedString("travek.search.fields_must_be_filled","Fields marked with * must be filled"));
			formTable.add(error, 1, row);
			formTable.mergeCells(1, row, 3, row);
			++row;
		}
	}
	
	private Table addTermsAndConditionsAndVerisign() throws RemoteException {
		Link terms = new Link(getText(iwrb.getLocalizedString("travel.search.terms_and_conditions", "Terms and conditions")));
		terms.setWindowToOpen(TravelWindow.class, "700", "400", true, true);
		terms.addParameter(TravelWindow.LOCALIZATION_KEY_FOR_HEADER, "travel.search.terms_and_conditions");
		terms.addParameter(TravelWindow.LOCALIZATION_KEY, "travel.search.terms_and_conditions_text");

		Link privacyStatement = new Link(getText(iwrb.getLocalizedString("travel.search.privacy_statement", "Privacy statement")));
		privacyStatement.setWindowToOpen(TravelWindow.class, "700", "400", true, true);
		privacyStatement.addParameter(TravelWindow.LOCALIZATION_KEY_FOR_HEADER, "travel.search.privacy_statement");
		privacyStatement.addParameter(TravelWindow.LOCALIZATION_KEY, "travel.search.privacy_statement_text");

		Table table = new Table();
		table.setCellpaddingAndCellspacing(0);
		table.setWidth("100%");
		table.setAlignment(1, 1, Table.HORIZONTAL_ALIGN_RIGHT);
		table.add(terms, 1, 1);

		table.setAlignment(1, 2, Table.HORIZONTAL_ALIGN_RIGHT);
		table.add(privacyStatement, 1, 2);
		
		table.setAlignment(1, 3, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setRowHeight(3, "5");
		
		table.setAlignment(1, 4, Table.HORIZONTAL_ALIGN_RIGHT);
		table.add(getVerisign(), 1, 4);

		return table;
	}
			
	protected void setupBookingForm() throws RemoteException {
		
		IWTimestamp from = new IWTimestamp(iwc.getParameter(PARAMETER_FROM_DATE));
		int	betw = 0;
		try {
			betw = Integer.parseInt(iwc.getParameter(PARAMETER_MANY_DAYS));
		} catch (NumberFormatException n) {
			logDebug("SearchForm : days set to 0");
		}

		Product product = getProduct();
		Supplier supplier = null;
		try {
			SupplierHome sHome = (SupplierHome) IDOLookup.getHome(Supplier.class);
			supplier = sHome.findByPrimaryKey(product.getSupplierId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		formTable.mergeCells(1, row, 3, row);
		formTable.add(getHeaderText(supplier.getName()), 1, row);
		++row;
		formTable.mergeCells(1, row, 3, row);
		formTable.add(getHeaderText(product.getProductName(iwc.getCurrentLocaleId())), 1, row);
		++row;
		formTable.mergeCells(1, row, 3, row);
		if (betw > 0) {
			IWTimestamp to = new IWTimestamp(from);
			to.addDays(betw);
			formTable.add(getHeaderText(from.getLocaleDate(iwc)+" - "+to.getLocaleDate(iwc)), 1, row);
		} else {
			formTable.add(getHeaderText(from.getLocaleDate(iwc)), 1, row);
		}
		++row;
		++row;
		addErrorWarning();
		/*
		if (errorFields != null && !errorFields.isEmpty()) {
			Text error = getErrorText(iwrb.getLocalizedString("travek.search.fields_must_be_filled","Fields marked with * must be filled"));
			formTable.add(error, 1, row);
			formTable.mergeCells(1, row, 3, row);
			++row;
		}*/
		addInputLine(new String[]{iwrb.getLocalizedString("travel.search.first_name","First name"), iwrb.getLocalizedString("travel.search.last_name","Last name")}, new PresentationObject[]{new TextInput(PARAMETER_FIRST_NAME), new TextInput(PARAMETER_LAST_NAME)});
		formTable.mergeCells(2, (row-1), 3, (row-1));

		TextInput postalC = new TextInput(PARAMETER_POSTAL_CODE);
		postalC.setSize(6);
		TextInput city = new TextInput(PARAMETER_CITY);
		city.setSize(18);
		addInputLine(new String[]{iwrb.getLocalizedString("travel.search.street","Street"),iwrb.getLocalizedString("travel.search.postal_code","Postal Code"), iwrb.getLocalizedString("travel.search.city","City")}, new PresentationObject[]{new TextInput(PARAMETER_STREET), postalC,city});

		addInputLine(new String[]{iwrb.getLocalizedString("travel.search.country","Country"), iwrb.getLocalizedString("travel.search.email","Email")}, new PresentationObject[]{new TextInput(PARAMETER_COUNTRY), new TextInput(PARAMETER_EMAIL)});
		formTable.mergeCells(2, (row-1), 3, (row-1));

		TextInput expMonth = new TextInput(PARAMETER_CC_MONTH);
		expMonth.setSize(3);
		expMonth.setMaxlength(2);
		TextInput expYear = new TextInput(PARAMETER_CC_YEAR);
		expYear.setSize(3);
		expYear.setMaxlength(2);
		TextInput expCVC = new TextInput(PARAMETER_CC_CVC);
		expCVC.setSize(5);
		expCVC.setMaxlength(4);

		if ( errorFields != null && errorFields.contains(PARAMETER_CC_NUMBER)) {
			formTable.add(getErrorText("* "), 1, row);
		}
		formTable.add(getText(iwrb.getLocalizedString("travel.search.credit_card_number","Credit card number")), 1, row);
		++row;
		formTable.add(new TextInput(PARAMETER_CC_NUMBER), 1, row);
		
		++row;
		Table ccTable = new Table();
		ccTable.setCellpaddingAndCellspacing(0);
		if ( errorFields != null && errorFields.contains(PARAMETER_CC_MONTH)) {
			ccTable.add(getErrorText("* "), 1, 1);
		}
		ccTable.add(getText(iwrb.getLocalizedString("travel.search.expires_month","Expires month")), 1, 1);
		if ( errorFields != null && errorFields.contains(PARAMETER_CC_YEAR)) {
			ccTable.add(getErrorText("* "), 3, 1);
		}
		ccTable.add(getText(iwrb.getLocalizedString("travel.search.expires_year","Expires year")), 3, 1);
		ccTable.add(expMonth, 1, 2);
		ccTable.add(expYear, 3, 2);
		ccTable.setColumnWidth(2, "8");
		formTable.add(ccTable, 1, row);
		
		
		if (cvcIsUsed) {
			Table ccTable2 = new Table();
			ccTable2.setCellpaddingAndCellspacing(0);
			if ( errorFields != null && errorFields.contains(PARAMETER_CC_CVC)) {
				ccTable2.add(getErrorText("* "), 1, 1);
			}
			ccTable2.add(getText(iwrb.getLocalizedString("travel.cc.cvc","Cardholder Verification Code (CVC)")), 1, 1);
			ccTable2.add(expCVC, 1, 2);
			Link cvcLink = LinkGenerator.getLinkCVCExplanationPage(iwc, getText(iwrb.getLocalizedString("cc.what_is_cvc","What is CVC?")));
			if (cvcLink != null) {
				ccTable2.add(cvcLink, 1, 2);
			}
			formTable.mergeCells(2, row, 3, row);
			formTable.add(ccTable2, 2, row);
		}
		++row;
		
		//addInputLine(new String[]{iwrb.getLocalizedString("travel.search.credit_card_number","Credit card number")}, new PresentationObject[]{new TextInput(PARAMETER_CC_NUMBER)});
		//addInputLine(new String[]{iwrb.getLocalizedString("travel.search.expires_month","Expires month"), iwrb.getLocalizedString("travel.search.expires_year","Expires year"), iwrb.getLocalizedString("travel.cc.cvc","Cardholder Verification Code (CVC)")}, new PresentationObject[]{expMonth,expYear, expCVC});

		TextArea comment = new TextArea(PARAMETER_COMMENT);
		comment.setWidth("350");
		comment.setHeight("50");
		addInputLine(new String[]{iwrb.getLocalizedString("travel.search.comment","Comment")}, new PresentationObject[]{comment});
		formTable.mergeCells(1, (row-1), 3, (row-1));

		String productPriceId = iwc.getParameter(PARAMETER_PRODUCT_PRICE_ID);
		String sAddressId = "-1";
		String sTimeframeId = "-1";
		
		if (productPriceId == null) {
			Timeframe timeframe = getSearchBusiness(iwc).getServiceHandler().getProductBusiness().getTimeframe(product, from, Integer.parseInt(sAddressId));
			if (timeframe != null) {
				sTimeframeId = timeframe.getPrimaryKey().toString();
			}
			ProductPrice[] prices = getProductPrices(product, Integer.parseInt(sAddressId), Integer.parseInt(sTimeframeId));
			if (prices != null && prices.length > 0) {
				productPriceId = prices[prices.length-1].getPrimaryKey().toString();
			}
		}
		
		formTable.add(new HiddenInput(PARAMETER_ADDRESS_ID, sAddressId));
		formTable.add(new HiddenInput(PARAMETER_PRODUCT_ID, iwc.getParameter(PARAMETER_PRODUCT_ID)));
		formTable.add(new HiddenInput(PARAMETER_ONLINE, "true"));
		formTable.add(new HiddenInput(PARAMETER_FROM_DATE, iwc.getParameter(PARAMETER_FROM_DATE)));
		//formTable.add(new HiddenInput(PARAMETER_TO_DATE, iwc.getParameter(PARAMETER_TO_DATE)));
		formTable.add(new HiddenInput(PARAMETER_MANY_DAYS, iwc.getParameter(PARAMETER_MANY_DAYS)));
		formTable.add(new HiddenInput(PARAMETER_PRODUCT_PRICE_ID, productPriceId));
		formTable.add(new HiddenInput(getParameterTypeCountName(), iwc.getParameter(getParameterTypeCountName())));
		
//		String productPriceId = iwc.getParameter(PARAMETER_PRODUCT_PRICE_ID);
		formTable.add(new HiddenInput("priceCategory"+productPriceId, iwc.getParameter(getParameterTypeCountName())));
		
		try {
			ProductPrice pPrice = ((ProductPriceHome) IDOLookup.getHome(ProductPrice.class)).findByPrimaryKey(new Integer(productPriceId));
			int addressId = -1;
			try {
				addressId = Integer.parseInt(sAddressId);
			} catch (Exception e) {}
			//Timeframe tFrame = getSearchBusiness(iwc).getServiceHandler().getProductBusiness().getTimeframe(product, from, addressId);
			int tFrameID = -1;
			if (sTimeframeId != null) {
				tFrameID = Integer.parseInt(sTimeframeId);
			}
			formTable.mergeCells(1, row, 3, row);
			formTable.add(getHeaderText(getPriceString(getSearchBusiness(iwc).getBusiness(product), product.getID(), tFrameID, pPrice)), 1, row);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		++row;
		SubmitButton submit = new SubmitButton(iwrb.getLocalizedImageButton("travel.search.confirm","Confirm"), ACTION, ACTION_CONFIRM);
		formTable.setAlignment(3, row, Table.HORIZONTAL_ALIGN_RIGHT);
		formTable.add(submit, 3, row);
		//formTable.setBorder(1);

		++row;
		Table logoTable = new Table();
		Collection imgs = null;
		try {
			imgs = getCreditCardBusiness(iwc).getCreditCardTypeImages(getCreditCardBusiness(iwc).getCreditCardClient(supplier, IWTimestamp.RightNow()));
			if (imgs != null && !imgs.isEmpty()) {
				Iterator iter = imgs.iterator();
				int col = 0;
				while (iter.hasNext()) {
					logoTable.add((Image)iter.next(), ++col, 1);
				}
				//addInputLine(new String[]{"", "", ""}, new PresentationObject[]{null, null, logoTable});
				formTable.add(logoTable, 1, row);
			}
		}catch (Exception e1) {
			e1.printStackTrace();
		}
		
	}
	
	protected void checkBooking() throws RemoteException {
		ProductHome productHome = (ProductHome) IDOLookup.getHome(Product.class);
		try {
			Product product = productHome.findByPrimaryKey( new Integer(iwc.getParameter(PARAMETER_PRODUCT_ID)) );
			BookingForm bf = getSearchBusiness(iwc).getServiceHandler().getBookingForm(iwc, product);
			int bookingId = bf.checkBooking(iwc, true);
			GeneralBookingHome gBookingHome = (GeneralBookingHome) IDOLookup.getHome(GeneralBooking.class);
			GeneralBooking gBooking = null;
			boolean inquirySent = (bookingId == BookingForm.inquirySent); 
			
			if (bookingId > 0) {
				gBooking = gBookingHome.findByPrimaryKey(new Integer(bookingId));	
				gBooking.setCode(this.engine.getCode());
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

			  formTable.add(printVoucher,1,3);
			  formTable.setAlignment(1,1,"left");
			  formTable.setAlignment(1,2,"right");
			  formTable.setAlignment(1,3,"right");
			}else if (inquirySent) {
				formTable.add(getText(iwrb.getLocalizedString("travel.inquiry_has_been_sent","Inquiry has been sent")));
				formTable.add(Text.BREAK);
				formTable.add(getText(iwrb.getLocalizedString("travel.you_will_reveice_an_confirmation_email_shortly","You will receive an confirmation email shortly.")));
			}else {
				formTable.add(getText(iwrb.getLocalizedString("travel.booking_failed","Booking failed")));
				formTable.add(getText(Text.BREAK));
				if (bookingId == BookingForm.errorTooMany) {
					formTable.add(getText(iwrb.getLocalizedString("travel.there_is_no_availability","There is no availability")));
				} else	if (bookingId == BookingForm.errorTooFew) {
					formTable.add(getText(iwrb.getLocalizedString("travel.there_is_no_availability","There is no availability")));
				}
			  if (gBooking == null) {
					debug("gBooking == null");
			  }
			}
		} catch (Exception e) {
			formTable.add(getText(iwrb.getLocalizedString("travek.booking_failed","Booking failed")+" ( "+e.getMessage()+" )"));
			formTable.add(Text.BREAK);
			formTable.add(Text.BREAK);
			formTable.add(new BackButton(iwrb.getLocalizedImageButton("travel.back", "Back")));
			e.printStackTrace(System.err);
		}
	}
		
	protected void listResults(IWContext iwc, Collection products, HashMap availability) throws RemoteException{
		if (products != null && !products.isEmpty()) {
			ProductHome pHome = (ProductHome) IDOLookup.getHome(Product.class);
			SupplierHome sHome = (SupplierHome) IDOLookup.getHome(Supplier.class);
			// TODO move to a better location
			IWTimestamp stamp = new IWTimestamp(iwc.getParameter(PARAMETER_FROM_DATE));

			TravelStockroomBusiness bus;
			Product product;
			Supplier supplier;
			Link link;
			List addresses;
			Timeframe timeframe;
			ProductPrice[] prices;
			Currency currency;
			Timeframe[] timeframes;
			boolean available;
			Iterator iter = products.iterator();
			int productsSize = products.size();
			Vector tmp = new Vector(products);
			for (int i = (productsSize-1); i >= 0 ; i--) {
			//while (iter.hasNext()) {
				try {
					product = pHome.findByPrimaryKey(tmp.get(i));
					Boolean productAvailability = ((Boolean)availability.get(product.getPrimaryKey()));
					if (productAvailability != null) {
						supplier = sHome.findByPrimaryKey(product.getSupplierId());
						bus = getSearchBusiness(iwc).getServiceHandler().getServiceBusiness(product);
						addresses = getSearchBusiness(iwc).getServiceHandler().getProductBusiness().getDepartureAddresses(product, stamp, true, getPriceCategoryKey());
	
						Table table = new Table();
						table.setWidth("100%");
						int row = 1;
						table.add(getHeaderText(supplier.getName()), 1, row);
						table.mergeCells(1, row, 2, row);
						++row;
						available = productAvailability.booleanValue();
						table.add(getHeaderText(product.getProductName(iwc.getCurrentLocaleId())), 1, row);
						table.setAlignment(2, row, Table.HORIZONTAL_ALIGN_RIGHT);
						if (available) {
							table.add(getText(iwrb.getLocalizedString("travel.search.available","Available")), 2, row);
						} else {
							table.add(getText(iwrb.getLocalizedString("travel.search.not_available","Not available")), 2, row);
						}
						//description
						++row;
						TxText descriptionText = product.getText();
						if (descriptionText != null) {
						  TextReader textReader = new TextReader(descriptionText.getID());
						  if (headerFontStyle != null) {
							textReader.setHeadlineStyle(headerFontStyle);
						  }
						  
						  if (textFontStyle != null) {
							textReader.setTextStyle(textFontStyle);
						  }
							textReader.setCacheable(false);
						  table.add(textReader,1,row);
						  table.mergeCells(1, row, 2, row);
						} else {
							/*
							try {
								sendErrorEmail(product);
							} catch (TPosException t) {
								System.out.println("[ServiceSearch] Product \""+product.getProductName(iwc.getCurrentLocaleId())+"\" has no Text to use with the search (mail NOT sent : error = "+t.getMessage()+")");
							}
							System.out.println("[ServiceSearch] Product \""+product.getProductName(iwc.getCurrentLocaleId())+"\" has no Text to use with the search (mail sent)");
							*/
						}
						
						if (addresses == null || addresses.isEmpty()) {
							++row;
							int addressId = -1;
							timeframe = getSearchBusiness(iwc).getServiceHandler().getProductBusiness().getTimeframe(product, stamp, addressId);
							int timeframeId = -1;
							if (timeframe != null) {
								timeframeId = timeframe.getID();
							}
							row = getPrices(iwc, stamp, bus, product, table, row, addressId, timeframeId);
							link = getBookingLink(product.getID());
							link.addParameter(PARAMETER_ADDRESS_ID, -1);
							//link.addParameter(PARAMETER_TIMEFRAME_ID, timeframeId);
							if (available) {
								table.add(link, 1, row);
							} else {
								table.add(getText(iwrb.getLocalizedString("travel.search.not_available","Not available")), 1, row);
							}
						} else {
							TravelAddress address;
							Iterator addressesIter = addresses.iterator();
							while (addressesIter.hasNext()) {
								address = (TravelAddress) addressesIter.next();
								timeframe = getSearchBusiness(iwc).getServiceHandler().getProductBusiness().getTimeframe(product, stamp, address.getID());
								int timeframeId = -1;
								if (timeframe != null) {
									timeframeId = timeframe.getID();
								}
								
								++row;
								table.add(getText(address.getName()), 1, row);
								++row;
								row =getPrices(iwc, stamp, bus, product, table, row, address.getID(), timeframeId);
								link = getBookingLink(product.getID());
								link.addParameter(PARAMETER_ADDRESS_ID, address.getAddressId());
								if (available) {
									table.add(link, 1, row);
								} else {
									table.add(getText(iwrb.getLocalizedString("travel.search.not_available","Not available")), 1, row);
								}
							}
							
						}
						
						add(table);
						add(Text.BREAK);
					}
				}catch(Exception e) {
					e.printStackTrace();
				} 
			}
				
		}
	}
	
	protected Link getBookingLink(int productId) {
		Link link = new Link(iwrb.getLocalizedImageButton("travel.book","Book"));
		link.maintainParameter(ServiceSearch.PARAMETER_SERVICE_SEARCH_FORM, iwc);
		link.addParameter(ACTION, ACTION_BOOKING_FORM);
		link.maintainParameter(PARAMETER_FROM_DATE, iwc);
		link.maintainParameter(PARAMETER_MANY_DAYS, iwc);
		link.maintainParameter(getParameterTypeCountName(), iwc);
		link.addParameter(PARAMETER_PRODUCT_ID, productId);
		link.addParameter(PARAMETER_PRODUCT_PRICE_ID, tmpPriceID);

		return link;
	}

	private int getPrices(IWContext iwc, IWTimestamp stamp, TravelStockroomBusiness bus, Product usedProduct, Table table, int row, int addressId, int timeframeId) throws RemoteException, SQLException {
		ProductPrice[] prices;
		Currency currency;
		prices = getProductPrices(usedProduct, addressId, timeframeId);
		//prices = ProductPriceBMPBean.getProductPrices(usedProduct.getID(), timeframeId, addressId, new int[] {PriceCategoryBMPBean.PRICE_VISIBILITY_PUBLIC, PriceCategoryBMPBean.PRICE_VISIBILITY_BOTH_PRIVATE_AND_PUBLIC}, getPriceCategoryKey());
		for (int i = 0; i < prices.length; i++) {
			tmpPriceID = prices[i].getID();
			table.add(getText(getPriceString(bus, usedProduct.getID(), timeframeId, prices[i])), 1, row++);
		}
		return row;
	}

	private ProductPrice[] getProductPrices(Product usedProduct, int addressId, int timeframeId) throws RemoteException {
		return ProductPriceBMPBean.getProductPrices(usedProduct.getID(), timeframeId, addressId, new int[] {PriceCategoryBMPBean.PRICE_VISIBILITY_PUBLIC, PriceCategoryBMPBean.PRICE_VISIBILITY_BOTH_PRIVATE_AND_PUBLIC}, getPriceCategoryKey());
	}
	
	private String getPriceString(TravelStockroomBusiness bus, int productId, int timeframeId, ProductPrice pPrice) throws SQLException, RemoteException {
		float price = bus.getPrice(pPrice.getID(), productId ,pPrice.getPriceCategoryID() , pPrice.getCurrencyId(), IWTimestamp.getTimestampRightNow(), timeframeId, -1 );

		Currency currency;
		try {
			currency = ((com.idega.block.trade.data.CurrencyHome)com.idega.data.IDOLookup.getHomeLegacy(Currency.class)).findByPrimaryKeyLegacy(pPrice.getCurrencyId());
		}catch (Exception e) {
			currency = null;
		}
		float total = -1;
		String returner = "";

		String sCount = iwc.getParameter(getParameterTypeCountName());
		int count = Integer.parseInt(sCount);
		int days = 1;
		try {
			days = Integer.parseInt(iwc.getParameter(PARAMETER_MANY_DAYS));
		} catch (NumberFormatException n) {
			logDebug("SearchForm : days set to 1");
		}
		
		total = price * days * count;
		returner += iwrb.getLocalizedString("travel.price","Price")+":"+Text.NON_BREAKING_SPACE+(price*count)+Text.NON_BREAKING_SPACE+currency.getCurrencyAbbreviation();
		if (days > 1) {
			returner += Text.NON_BREAKING_SPACE+iwrb.getLocalizedString("travel.search.per_nigth","per night");
		}
		if (count > 1) {
			returner += " ("+price+" per "+getUnitName()+")";
		}
		returner += Text.BREAK+iwrb.getLocalizedString("travel.search.total","Total")+":"+Text.NON_BREAKING_SPACE+total+Text.NON_BREAKING_SPACE+currency.getCurrencyAbbreviation();
		return returner;
	}

	protected void addInputLine(String[] text, PresentationObject[] object) {
		addInputLine(text, object, false);
	}	
	protected void addInputLine(String[] text, PresentationObject[] object, boolean useHeaderText) {
		for (int i = 0; i < text.length; i++) {
			if ( errorFields != null && errorFields.contains(object[i].getName())) {
				formTable.add(getErrorText("* "), i+1, row);
			}
			if (useHeaderText) {
				formTable.add(getHeaderText(text[i]), i+1, row);
			} else {
				formTable.add(getText(text[i]), i+1, row);
			}
			formTable.setNoWrap(i+1, row);
		}
		++row;
		String value;
		for (int i = 0; i < object.length; i++) {
			if ( object[i] != null) {
				if (object[i]  instanceof Table ) {
					value = null;
				} else {
					value = iwc.getParameter(object[i].getName());
				}
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
					} else if (object[i] instanceof SelectPanel) {
						String values[] = iwc.getParameterValues(object[i].getName());
						((SelectPanel)object[i]).setSelectedElements(values);
					} else {
						try {
							((InterfaceObject)object[i]).setContent(value);
						}catch (Exception e) {
							System.out.println("Error changing presentationObject to interfaceObject");
						}
					} 
				}
	
				if (formInputStyle != null) {
					object[i].setStyleAttribute(formInputStyle);
				}
				formTable.add(object[i], i+1, row);
			}
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
	
	protected Text getLinkText(String content, boolean clicked) {
		Text text = new Text(content);
		if (clicked && clickedLinkFontStyle != null) {
			text.setFontStyle(clickedLinkFontStyle);
		} else if (linkFontStyle != null) {
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
	
	public void setClickedLinkFontStyle(String fontStyle) {
		this.clickedLinkFontStyle = fontStyle;
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
	
	public void setWindowHeaderImage(Image image) {
		this.windowHeaderImage = image;
	}
	
	public void setSearchForms(List searchForms) {
		this.searchForms = searchForms;
	}
	
	protected Product getProduct() {
		try {
			ProductHome home = (ProductHome) IDOLookup.getHome(Product.class);
			return home.findByPrimaryKey(new Integer(iwc.getParameter(PARAMETER_PRODUCT_ID)));
		}catch (Exception e) {
			return null;
		}
	}
	
	
	public void addAreaCodeInput() {
		try {
			DropdownMenu menu = getSearchBusiness(iwc).getPostalCodeDropdown(iwrb);
			addInputLine(new String[]{iwrb.getLocalizedString("travel.search.location","Location")}, new PresentationObject[]{menu});
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	protected DropdownMenu getDropdownWithNumbers(String name, int startNumber, int endNumber) {
		DropdownMenu menu = new DropdownMenu(name);
		for (int i = startNumber; i <= endNumber; i++) {
			menu.addMenuElement(i, Integer.toString(i));
		}		
		return menu;
	}


	public void setServiceSearchEngine(ServiceSearchEngine engine) {
		this.engine = engine;
	}
	
	public ServiceSearchBusiness getSearchBusiness(IWApplicationContext iwac) throws RemoteException {
		return (ServiceSearchBusiness) IBOLookup.getServiceInstance(iwac, ServiceSearchBusiness.class);
	}
	
	public TravelStockroomBusiness getTravelStockroomBusiness(IWApplicationContext iwac) throws RemoteException {
		return (TravelStockroomBusiness) IBOLookup.getServiceInstance(iwac, TravelStockroomBusiness.class);
	}
	
	protected void sendErrorEmail(Product product) throws TPosException {
		String error_notify_email = this.bundle.getProperty(BookingForm.PARAMETER_EMAIL_FOR_ERROR_NOTIFICATION);
		if (error_notify_email != null) {
			try {
				String cc_error_notify_email = this.bundle.getProperty(BookingForm.PARAMETER_CC_EMAIL_FOR_ERROR_NOTIFICATION);
				if (cc_error_notify_email == null) {
					cc_error_notify_email = "";	
				}

				Supplier supp = null;
				try {
					SupplierHome sHome = (SupplierHome) IDOLookup.getHome(Supplier.class);
					supp = sHome.findByPrimaryKey(product.getSupplierId());
				} catch (FinderException e) {
					e.printStackTrace();
				} 
				
				String subject = iwrb.getLocalizedString("travel.product_description_text_not_found", "Text for Search not found for product = "+product.getProductName(iwc.getCurrentLocaleId()));
				
				SendMail mail = new SendMail();

				product.getSupplierId();
				StringBuffer msg = new StringBuffer();
				msg.append(subject+"\n\n ")
				.append(product.getProductName(iwc.getCurrentLocaleId()))
				.append(" "+iwrb.getLocalizedString("travel.belongs_to_supplier", "belongs to supplier")+ " : ");
				if (supp != null) {
					msg.append(supp.getName());
				}

				mail.send("gimmi@idega.is", error_notify_email, cc_error_notify_email, "", "mail.idega.is", subject, msg.toString());
			} catch (MessagingException e1) {
				e1.printStackTrace(System.err);
				throw new TPosException(iwrb.getLocalizedString("travel.unknown_error","Unknown error"));
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected int getDefaultSortMethod() {
		return ProductComparator.PRICE;
	}

	protected void handleResults(Collection coll) throws IDOLookupException, FinderException, RemoteException {
		String sFromDate = iwc.getParameter(PARAMETER_FROM_DATE);
		PriceCategoryHome pcHome = (PriceCategoryHome) IDOLookup.getHome(PriceCategory.class);
		PriceCategory priceCat = pcHome.findByKey(getPriceCategoryKey());
		coll = getSearchBusiness(iwc).sortProducts(coll, priceCat, new IWTimestamp(sFromDate), getDefaultSortMethod());

		HashMap map = getSearchBusiness(iwc).checkResults(iwc, coll);
		int mapSize = map.size();
		String foundString = "";
		if (map != null) {
			foundString = "Found "+mapSize+" match";
			if (mapSize != 1) foundString += "es !<br>";
		} else {
			foundString = getText(iwrb.getLocalizedString("travel.search.no_matches","No matches"))+"<BR>";
		}
		
		if (mapSize > 0) {
			add(foundString);
		}
		listResults(iwc, coll, map);
		add(foundString);

		if (coll != null) {
			if (coll.isEmpty()) {
				STATE = 0;
				setupSearchForm();
			}
		} else {
			STATE = 0;
			setupSearchForm();
		}
	}
	
	protected Object[] getSupplierIDs() {
		Object[] suppIds = new Object[]{};
		String sPostalCode[] = iwc.getParameterValues(PARAMETER_POSTAL_CODE_NAME);
		try {
			Collection supps = engine.getSuppliers();
			if (supps != null && !supps.isEmpty()) {
				Iterator iter =supps.iterator();
				int i = 0;
				suppIds = new Object[supps.size()];
				while (iter.hasNext()) {
					suppIds[i] = ((Supplier) iter.next()).getPrimaryKey();
					i++;
				}
			}
		}catch (Exception e) {
			e.printStackTrace(System.err);
		}
		return suppIds;
	}
	
	protected CreditCardBusiness getCreditCardBusiness(IWContext iwc) {
		try {
				return (CreditCardBusiness) IBOLookup.getServiceInstance(iwc, CreditCardBusiness.class);
			} catch (IBOLookupException e) {
			throw new IBORuntimeException();
			}
	}
	
}
