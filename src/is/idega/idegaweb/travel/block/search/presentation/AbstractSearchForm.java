package is.idega.idegaweb.travel.block.search.presentation;

import is.idega.idegaweb.travel.block.search.business.InvalidSearchException;
import is.idega.idegaweb.travel.block.search.business.ServiceSearchBusiness;
import is.idega.idegaweb.travel.block.search.business.ServiceSearchBusinessBean;
import is.idega.idegaweb.travel.block.search.business.ServiceSearchSession;
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
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.ejb.FinderException;
import javax.mail.MessagingException;
import com.idega.block.creditcard.business.CreditCardBusiness;
import com.idega.block.creditcard.business.TPosException;
import com.idega.block.text.data.LocalizedText;
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
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.block.trade.stockroom.data.SupplierHome;
import com.idega.block.trade.stockroom.data.Timeframe;
import com.idega.block.trade.stockroom.data.TravelAddress;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.builder.business.BuilderService;
import com.idega.core.builder.business.BuilderServiceFactory;
import com.idega.core.builder.data.ICPage;
import com.idega.core.file.data.ICFile;
import com.idega.data.IDOFinderException;
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
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.Window;
import com.idega.util.IWTimestamp;
import com.idega.util.SendMail;

/**
 * @author gimmi
 */
public abstract class AbstractSearchForm extends TravelBlock{

	private boolean debug = false;
	
	protected String ACTION = "bsf_a";
	protected String ACTION_SEARCH = "bsf_as";
	protected String ACTION_BOOKING_FORM = "bsf_bf";
	protected String ACTION_PRODUCT_DETAILS = "bsf_pd";
	protected String ACTION_CONFIRM = "bsf_cm";

	protected static final int STATE_SHOW_SEARCH_FORM = 0;
	protected static final int STATE_SHOW_SEARCH_RESULTS = 1;
	protected static final int STATE_SHOW_BOOKING_FORM = 2;
	protected static final int STATE_CHECK_BOOKING = 3;
	protected static final int STATE_SHOW_DETAILED_PRODUCT = 4;
	protected static final int STATE_DEFINED_PRODUCT = 5;

	protected String PARAMETER_TYPE = "hs_pt";
	protected String PARAMETER_TYPE_COUNT = "hs_ptc";

	public static String PARAMETER_POSTAL_CODE_NAME = "hs_pcn";
	public static String PARAMETER_FROM_DATE = BookingForm.parameterFromDate;//"hs_fd";
	public static String PARAMETER_TO_DATE = BookingForm.parameterToDate;
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
	public static String PARAMETER_PHONE_NUMBER = BookingForm.PARAMETER_PHONE;
	public static String PARAMETER_NAME_ON_CARD = BookingForm.PARAMETER_NAME_ON_CARD; //"hs_noc";
	public static String PARAMETER_SORT_BY = "asf_p_sb"; 

	public static String PARAMETER_NEW_SEARCH = "asf_p_ns";
	public static String PARAMETER_PAGE_NR = "asf_p_nr";
	
	public static String PARAMETER_SUPPLIER_NAME = "hs_suppn";
	
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
	protected String searchPartTopBorderColor = null;
	protected String searchPartTopBorderWidth = null;
	protected String searchPartBottomBorderColor = null;
	protected String searchPartBottomBorderWidth = null;
	protected String searchPartColor = null;
	protected Image windowHeaderImage;
	protected IWResourceBundle iwrb;
	protected IWBundle bundle;
	protected ServiceSearchEngine engine = null;
	protected int resultsPerPage = 5;
	private int currentPageNumber = 1;
	
	protected Image headerImage;
	protected Image headerImageTiler;
	protected int row = 1;
	protected boolean useSecureServer = true;
	int tmpPriceID;
	
	protected Product definedProduct;
	private boolean isAlwaysSearchForm = false;
	protected ICPage targetPage = null;
	
	private List errorFields = null;
	private List searchForms = null;
	protected DecimalFormat currencyFormat;
	protected int localeID = -1;
	private BookingForm bf;
	
	protected HashMap frames = new HashMap();
	
	public AbstractSearchForm() {
		setCacheable(getCacheKey(),0);
	}


	protected abstract String 			getServiceName(IWResourceBundle iwrb);
	protected abstract void 				setupSearchForm() throws RemoteException ;
	protected abstract void 				setupSpecialFieldsForBookingForm(Table table, int row, List errorFields);
	protected abstract Collection 	getResults() throws RemoteException, InvalidSearchException;
	protected abstract Image 				getHeaderImage(IWResourceBundle iwrb);
	protected abstract String 			getPriceCategoryKey();
	protected abstract List 				getErrorFormFields();
	protected abstract Collection 	getParametersInUse();
	
	protected Table getProductInfoDetailed(Product product) {return null;}
	protected void addProductInfo(Product product, Table table, int column, int row) {}

	private void init(IWContext iwc) throws RemoteException {
		this.iwc = iwc;
		getBooker(iwc).addCacheKeyToInvalidateOnSave(getCacheKey());
		
		iwrb = getSearchBusiness(iwc).getTravelSessionManager(iwc).getIWResourceBundle();
		bundle = getSearchBusiness(iwc).getTravelSessionManager(iwc).getIWBundle();
		localeID = iwc.getCurrentLocaleId();
		currencyFormat = new DecimalFormat("0.00");
		
		definedProduct = getProduct();
		try {
			currentPageNumber = Integer.parseInt(iwc.getParameter(PARAMETER_PAGE_NR));
		} catch (NumberFormatException ignore) {}
		bf = getBookingForm();
	}
	
	public void _main(IWContext iwc) throws Exception {
		this.iwc = iwc;
		handleSubmit(iwc);
		super._main(iwc);
	}
	
	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		init(iwc);
		handleSubmit(iwc);
//		System.out.println("[AbstractSearchFrom] Engine code = "+this.engine.getCode());
		
		Table outTable = new Table();
		if (width != null) {
			outTable.setWidth(width);
		}
		//outTable.setBorder(1);
		//outTable.setBorderColor("GREEN");
		outTable.setCellpaddingAndCellspacing(0);
		
		
		Form form = new Form();
		form.setMethod("GET");
		int productId = -1;
		if (definedProduct != null) {
			productId = ((Integer) definedProduct.getPrimaryKey()).intValue();
		}

		form = (Form) addParameters(form, productId, this.isAlwaysSearchForm);

		setupPresentation(form);
//		form.add(getBookingForm().formTable);
		form.add(getButtons(form));
		outTable.add(form);
/*		outTable.add(Text.BREAK);
		outTable.add(addTermsAndConditionsAndVerisign());
		*/
		if (definedProduct != null && !this.isAlwaysSearchForm && (isInPermissionGroup(iwc) || isAdministrator(iwc))) {
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

		//String url = iwc.getIWMainApplication().getBuilderServletURI();//+"&"+PARAMETER_PRODUCT_ID+"="+definedProduct.getPrimaryKey().toString();
		
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
	/*
	public void add(Object object) {
		getBookingForm().add(object);
	}

	public void add(String string) {
		getBookingForm().add(string);
	}
	
	public void add(PresentationObject po) {
		getBookingForm().add(po);
	}*/

	protected Table getButtons(Form form) throws RemoteException {

		Table table = new Table();
		table.setWidth("100%");
		
		if (isAlwaysSearchForm) {

//			if (definedProduct == null) {
				Link resetLink = new Link(getLinkText(iwrb.getLocalizedString("reset", "Reset"), false));
				resetLink.setToFormReset(form);
				
				Link searchLink = new Link(getLinkText(iwrb.getLocalizedString("search","Search"), false));
				if (hasDefinedProduct()) {
					form.addParameter(ACTION, ACTION_PRODUCT_DETAILS);
				} else {
					form.addParameter(ACTION, ACTION_SEARCH);
					form.addParameter(PARAMETER_NEW_SEARCH,"true");
				}
				searchLink.setToFormSubmit(form);
				table.add(resetLink, 1, 1);
				table.setAlignment(2, 1, Table.HORIZONTAL_ALIGN_RIGHT);
				table.add(searchLink, 2, 1);
				//SubmitButton search = new SubmitButton(iwrb.getLocalizedImageButton("search","Search"), ACTION, ACTION_SEARCH);
//				table.add(search, 2, 1);
/*			} else {
				table.add(new HiddenInput(PARAMETER_PRODUCT_ID, definedProduct.getPrimaryKey().toString()));
				SubmitButton book = new SubmitButton(iwrb.getLocalizedImageButton("book","Book"), ACTION, ACTION_BOOKING_FORM);
				table.setAlignment(1, 1, Table.HORIZONTAL_ALIGN_RIGHT);
				table.add(book);
			}
*/		
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
		} else {
			table.add(getHeaderText(getServiceName(iwrb)));
		}
		if (headerImageTiler != null) {
			table.setBackgroundImage(headerImageTiler);
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
	
	protected boolean isCacheable(IWContext iwc) {
		if (debug) {
			return false;
		}
		
		try {
			handleSubmit(iwc);
			return (STATE_CHECK_BOOKING != getSession(iwc).getState());
		} catch (Exception e) {
			return super.isCacheable(iwc);
		}
	}

	protected void handleSubmit(IWContext iwc) throws RemoteException {
		String action = iwc.getParameter(this.ACTION);
		String pId = iwc.getParameter(this.PARAMETER_PRODUCT_ID);
		int STATE = -1;
		if (action == null) {
			STATE = STATE_SHOW_SEARCH_FORM;
			if (pId != null) {
				STATE = STATE_DEFINED_PRODUCT;
			}
		} else if ( action.equals(this.ACTION_SEARCH)) {
			STATE = STATE_SHOW_SEARCH_RESULTS;
		} else if ( action.equals(this.ACTION_BOOKING_FORM)) {
			STATE = STATE_SHOW_BOOKING_FORM;
		} else if (action.equals(ACTION_CONFIRM)) {
			bf = getBookingForm();
			errorFields = getSearchBusiness(iwc).getErrorFormFields(iwc, getPriceCategoryKey(), bf.useCVC);
			bf.setErrorFields(errorFields);
			List tmp = getErrorFormFields();
			if (tmp != null) {
				errorFields.addAll(tmp);
			}
			//errorFields.addAll(getErrorFormFields());
			if (errorFields == null || errorFields.isEmpty() ) {
				STATE = STATE_CHECK_BOOKING;
			} else {
				STATE = STATE_SHOW_BOOKING_FORM;
			}
		} else if (action.equals(ACTION_PRODUCT_DETAILS)) {
			STATE = STATE_SHOW_DETAILED_PRODUCT;
		}
		
		getSession(iwc).setState(STATE);
	}
	
	protected void setupPresentation(Form form) throws RemoteException {

		if (isAlwaysSearchForm) {
			form.add(getBookingForm().formTable);
			if (getSession(iwc).getState() != STATE_DEFINED_PRODUCT && getSession(iwc).getState() != STATE_SHOW_SEARCH_RESULTS) {
				getSession(iwc).setState(STATE_SHOW_SEARCH_FORM);
			}
//			else {
//				System.out.println("Not setting state as search");
//			}
		} 
		/*
		else if ( (getSession(iwc).getState() == STATE_SHOW_SEARCH_FORM || getSession(iwc).getState() == 0) && definedProduct != null) {
			getSession(iwc).setState(STATE_SHOW_DETAILED_PRODUCT);
		}
		*/
		switch (getSession(iwc).getState()) {
			case STATE_DEFINED_PRODUCT :
				if (isAlwaysSearchForm) {
					setupSearchForm();
					getBookingForm().addHiddenInput(PARAMETER_PRODUCT_ID, definedProduct.getPrimaryKey().toString());
				} else {
					definedProductInformation();
				}
				break;
			case STATE_SHOW_SEARCH_FORM :
				if (isAlwaysSearchForm) {
					setupSearchForm();
				} else {
					unsearched();
				}
				break;
			case STATE_SHOW_SEARCH_RESULTS :
				try {
					if (!isAlwaysSearchForm) {
						Collection coll = getAndHandleResults();
						handleResults(coll);
					} else {
						setupSearchForm();
					}
				} catch (InvalidSearchException i) {
					//System.out.println("AbstractSearchForm : InvalidSearchException : "+i.getMessage());
					if (isAlwaysSearchForm) {
						errorFields = i.getErrorFields();
						getSession(iwc).setState(STATE_SHOW_SEARCH_FORM);
						getBookingForm().addErrorWarning(getBookingForm().formTable, row, true, false);
						setupSearchForm();
					} else {
						unsearched();
					}
				} catch (FinderException f) {
					getSession(iwc).setState(STATE_SHOW_SEARCH_FORM);
					getBookingForm().addErrorWarning(getBookingForm().formTable, row, true, false);
					setupSearchForm();
				}
				break;
			case STATE_SHOW_BOOKING_FORM :
				try {
					getBookingForm(form);
//					add(getBookingForm(form));
				}catch (Exception e) {
					e.printStackTrace(System.err);
				}
				break;
			case STATE_CHECK_BOOKING :
				checkBooking();
				break;
			case STATE_SHOW_DETAILED_PRODUCT :
				add(getProductDetails(iwc));
				break;
		}
	}	
	
	protected void unsearched() {
		Table table = new Table(1, 2);
		table.add(getText(iwrb.getLocalizedString("travel.search.please_search", "Please fill in the form on the left.")), 1, 1);
		table.add(getSmallText(iwrb.getLocalizedString("travel.search.please_search_detailed", "To execute a search you must fill in the form on the left and click search.")), 1, 2);
		this.add(table);
	}
	
	protected void definedProductInformation() throws RemoteException {
		ProductDetailFrame frame = getProductDetailFrame(getProduct(), 2);
				
		Table table = new Table(1, 2);
		table.add(getText(iwrb.getLocalizedString("travel.search.defined_product_explained_header", "Check availability.")), 1, 1);
		table.add(getSmallText(iwrb.getLocalizedString("travel.search.defined_product_explained", "In order to check availability for the desired product, you must fill in the form on the left and click search.")), 1, 2);

		frame.add(table);
		
		this.add(frame);
	}
	
	/**
	 * @param iwc
	 * @param cacheStatePrefix
	 * @return
	 */
	private StringBuffer getUniqueKey(IWContext iwc, boolean includeSortAndPageNR) {
		StringBuffer key = new StringBuffer();
		Collection params = getParametersInUse();
		key.append("asf_")
		.append(iwc.getParameter(ServiceSearch.PARAMETER_SERVICE_SEARCH_FORM)).append("_")
		.append(iwc.getParameter(AbstractSearchForm.PARAMETER_POSTAL_CODE_NAME)).append("_")
		.append(iwc.getParameter(PARAMETER_FROM_DATE)).append("_")
		.append(iwc.getParameter(PARAMETER_TO_DATE)).append("_")
		.append(iwc.getParameter(PARAMETER_MANY_DAYS)).append("_")
		.append(iwc.getParameter(PARAMETER_PRODUCT_ID)).append("_")
		.append(iwc.getParameter(PARAMETER_POSTAL_CODE)).append("_")
		.append(iwc.getParameter(ACTION)).append("_");
		if (includeSortAndPageNR) {
			key.append(iwc.getParameter(PARAMETER_SORT_BY)).append("_")
			.append(iwc.getParameter(PARAMETER_PAGE_NR));
		}
		key.append(isAlwaysSearchForm);
		try {
			key.append(getSession(iwc).getState()).append("_");
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		if (params != null) {
			Iterator iter = params.iterator();
			while (iter.hasNext()) {
				key.append(iwc.getParameter((String) iter.next())).append("_");
			}
		}
		return key;
	}

	protected Collection getAndHandleResults() throws RemoteException, InvalidSearchException, FinderException {
		String key = getUniqueKey(iwc, false).toString();
		String keyWithSort = getUniqueKey(iwc, true).toString();
		boolean sorted = false;
		
		//System.out.println("resultsKey+S = "+keyWithSort);
		Collection coll = getSearchBusiness(iwc).getSearchResults(keyWithSort);
		if (coll == null) {
			//System.out.println("resultsKey   = "+key);
			coll = getSearchBusiness(iwc).getSearchResults(key);
		} 
		else {
			sorted = true;
		}
		
		if (coll == null) {
			System.out.println("Getting new results");
			coll = getResults();
			coll = getSearchBusiness(iwc).checkResults(iwc, coll);
			getSearchBusiness(iwc).addSearchResults(key, coll);
		} else {
			System.out.println("Getting cached results");
		}
		
		if (!sorted){
			coll = filterResults(iwc, coll);
			getSearchBusiness(iwc).addSearchResults(keyWithSort, coll);
			System.out.println("Sorting results...");
		} else {
			System.out.println("Getting old sorted results");
		}
		return coll;
	}

	public String getCacheKey() {
		return ServiceSearchBusinessBean.SEARCH_FORM_CACHE_KEY;
	}

	 protected String getCacheState(IWContext iwc, String cacheStatePrefix){
		StringBuffer key = getUniqueKey(iwc, true);
		//System.out.println("cacheState = "+cacheStatePrefix+key.toString());
		return  cacheStatePrefix+key.toString();
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
	
	
	protected TravelBlock getBookingForm(Form form) throws RemoteException {
		ProductDetailFrame frame = getProductDetailFrame(getProduct(), 2);

		Table table = new Table();
		table.setBorder(0);
		table.setCellpaddingAndCellspacing(0);
		table.setWidth("100%");
//		form.add(table);
		form.add(frame);
		int row = 1;
		
		IWTimestamp from = new IWTimestamp(iwc.getParameter(PARAMETER_FROM_DATE));
		int betw = getBookingForm().getNumberOfDays(from);
		IWTimestamp to = new IWTimestamp(from);
		to.addDays(betw);

		Product product = getProduct();
		Supplier supplier = null;
		try {
			SupplierHome sHome = (SupplierHome) IDOLookup.getHome(Supplier.class);
			supplier = sHome.findByPrimaryKey(product.getSupplierId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		boolean isProductValid = false;
		try {
			isProductValid = getBookingBusiness(iwc).getIsProductValid(iwc, product, from, to);
		}
		catch (Exception e2) {
			e2.printStackTrace();
		}

		getBookingForm().setSearchPart(table, row, false, false);
		if (errorFields != null && !errorFields.isEmpty()) {
			getBookingForm().addErrorWarning(getBookingForm().getCurrentBookingPart(), getBookingForm().getCurrentBookingPartRow(), true, false);
			getBookingForm().getCurrentBookingPart().setCellpaddingBottom(1, getBookingForm().getCurrentBookingPartRow(), 8);
			++row;
			getBookingForm().setCurrentBookingPartRow(getBookingForm().getCurrentBookingPartRow()+1);
		}

		if (isProductValid) {
			getBookingForm().addInputLine(new String[]{iwrb.getLocalizedString("travel.search.first_name","First name"), iwrb.getLocalizedString("travel.search.last_name","Last name")}, new PresentationObject[]{new TextInput(PARAMETER_FIRST_NAME), new TextInput(PARAMETER_LAST_NAME)}, false, false, table, row);
			//table.mergeCells(2, (row-1), 3, (row-1));
	
			TextInput postalC = new TextInput(PARAMETER_POSTAL_CODE);
			postalC.setSize(6);
			TextInput city = new TextInput(PARAMETER_CITY);
			//city.setSize(18);
			getBookingForm().addInputLine(new String[]{iwrb.getLocalizedString("travel.search.address","Address"),iwrb.getLocalizedString("travel.search.postal_code","Postal Code")}, new PresentationObject[]{new TextInput(PARAMETER_STREET), postalC}, false, false, table, row);
	
			getBookingForm().addInputLine(new String[]{iwrb.getLocalizedString("travel.search.city","City"), iwrb.getLocalizedString("travel.search.country","Country")}, new PresentationObject[]{city, new TextInput(PARAMETER_COUNTRY)}, false, false, table, row);
			getBookingForm().addInputLine(new String[]{iwrb.getLocalizedString("travel.search.email","Email"), iwrb.getLocalizedString("travel.search.phone", "Telephone number")}, new PresentationObject[]{new TextInput(PARAMETER_EMAIL), new TextInput(PARAMETER_PHONE_NUMBER)}, false, false, table, row);
//			table.mergeCells(2, (row-1), 3, (row-1));
	
			setupSpecialFieldsForBookingForm(table, row, errorFields);
			
			TextInput expMonth = new TextInput(PARAMETER_CC_MONTH);
			expMonth.setSize(3);
			expMonth.setMaxlength(2);
			TextInput expYear = new TextInput(PARAMETER_CC_YEAR);
			expYear.setSize(3);
			expYear.setMaxlength(2);
			TextInput expCVC = new TextInput(PARAMETER_CC_CVC);
			expCVC.setSize(5);
			expCVC.setMaxlength(4);
	
			//if ( errorFields != null && errorFields.contains(PARAMETER_CC_NUMBER)) {
			//	table.add(getErrorText("* "), 1, row);
			//}
			//table.add(getText(iwrb.getLocalizedString("travel.search.credit_card_number","Credit card number")), 1, row);
			//++row;
			//table.add(new TextInput(PARAMETER_CC_NUMBER), 1, row);

			TextArea comment = new TextArea(PARAMETER_COMMENT);
			comment.setWidth("300");
			comment.setHeight("50");
			getBookingForm().addInputLine(new String[]{iwrb.getLocalizedString("travel.search.comment","Comment")}, new PresentationObject[]{comment}, false, false, table, row);
			getBookingForm().getCurrentBookingPart().mergeCells(1, getBookingForm().getCurrentBookingPartRow()-1, 2, getBookingForm().getCurrentBookingPartRow()-1);


			getBookingForm().addInputLine(new String[]{iwrb.getLocalizedString("travel.search.credit_card_number","Credit card number"), iwrb.getLocalizedString("travel.search.name_on_card", "Name as it appears on card")}, new PresentationObject[]{new TextInput(PARAMETER_CC_NUMBER), new TextInput(PARAMETER_NAME_ON_CARD)}, false, false, table, row);

			++row;
			Table ccTable = new Table();
			ccTable.setCellpaddingAndCellspacing(0);
			if ( errorFields != null && errorFields.contains(PARAMETER_CC_MONTH)) {
				ccTable.add(getErrorText("* "), 1, 1);
			}
			ccTable.add(getText(iwrb.getLocalizedString("travel.search.month","Month")), 1, 1);
			ccTable.add(getText("/"), 2, 1);
			if ( errorFields != null && errorFields.contains(PARAMETER_CC_YEAR)) {
				ccTable.add(getErrorText("* "), 3, 1);
			}
			ccTable.add(getText(iwrb.getLocalizedString("travel.search.year","Year")), 3, 1);
			ccTable.add(expMonth, 1, 2);
			ccTable.add(getText("/"), 2, 2);
			ccTable.add(expYear, 3, 2);
			ccTable.setColumnWidth(2, "8");
			ccTable.setBorder(0);
			ccTable.setCellpaddingLeft(2, 1, 5);
			ccTable.setCellpaddingLeft(2, 2, 5);
			//table.add(ccTable, 1, row);
			getBookingForm().getCurrentBookingPart().add(ccTable, 1, getBookingForm().getCurrentBookingPartRow());
			//currentSearchPart.setCellpaddingTop(1, currentSearchPartRow, 6);
			getBookingForm().getCurrentBookingPart().setCellpaddingLeft(1, getBookingForm().getCurrentBookingPartRow(), 10);
			getBookingForm().getCurrentBookingPart().setCellpaddingBottom(1, getBookingForm().getCurrentBookingPartRow(), 9);
			//currentSearchPart.setCellpaddingTop(2, currentSearchPartRow, 6);
			getBookingForm().getCurrentBookingPart().setCellpaddingLeft(2, getBookingForm().getCurrentBookingPartRow(), 10);
			getBookingForm().getCurrentBookingPart().setCellpaddingBottom(2, getBookingForm().getCurrentBookingPartRow(), 9);
			
			
			if (getBookingForm().useCVC) {
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
				//table.mergeCells(2, row, 3, row);
				
				//ccTable.add(ccTable2, 4, 1);
				getBookingForm().getCurrentBookingPart().add(ccTable2, 2, getBookingForm().getCurrentBookingPartRow());
			}
			++row;
			
	

			//addInputLine(new String[]{iwrb.getLocalizedString("travel.search.expires_month","Expires month"), iwrb.getLocalizedString("travel.search.expires_year","Expires year"), iwrb.getLocalizedString("travel.cc.cvc","Cardholder Verification Code (CVC)")}, new PresentationObject[]{expMonth,expYear, expCVC}, false, false, table, row);
			//table.mergeCells(1, (row-1), 3, (row-1));
	
			String productPriceId = iwc.getParameter(PARAMETER_PRODUCT_PRICE_ID);
			String sAddressId = iwc.getParameter(PARAMETER_ADDRESS_ID);
			if (sAddressId == null) {
				sAddressId = "-1";
			}
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
			
			table.add(new HiddenInput(PARAMETER_ADDRESS_ID, sAddressId));
			table.add(new HiddenInput(PARAMETER_PRODUCT_ID, iwc.getParameter(PARAMETER_PRODUCT_ID)));
			table.add(new HiddenInput(PARAMETER_ONLINE, "true"));
			table.add(new HiddenInput(PARAMETER_FROM_DATE, iwc.getParameter(PARAMETER_FROM_DATE)));
			table.add(new HiddenInput(PARAMETER_TO_DATE, iwc.getParameter(PARAMETER_TO_DATE)));
			table.add(new HiddenInput(PARAMETER_MANY_DAYS, iwc.getParameter(PARAMETER_MANY_DAYS)));
			table.add(new HiddenInput(PARAMETER_PRODUCT_PRICE_ID, productPriceId));
			table.add(new HiddenInput(PARAMETER_PRODUCT_ID, product.getPrimaryKey().toString()));
			table.add(new HiddenInput(getBookingForm().getParameterTypeCountName(), iwc.getParameter(getBookingForm().getParameterTypeCountName())));
			
	//		String productPriceId = iwc.getParameter(PARAMETER_PRODUCT_PRICE_ID);
			table.add(new HiddenInput("priceCategory"+productPriceId, iwc.getParameter(getBookingForm().getParameterTypeCountName())));
			
			/*
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
				table.mergeCells(1, row, 3, row);
				table.add(getHeaderText(oldGetPriceString(getSearchBusiness(iwc).getBusiness(product), product.getID(), tFrameID, pPrice, betw)), 1, row);
			} catch (Exception e) {
				e.printStackTrace();
			}*/
			
			++row;
			Link backLink = new Link(getLinkText(iwrb.getLocalizedString("travel.search.back", "Back"), false));
			backLink.setAsBackLink();

			Link submitLink = new Link(getLinkText(iwrb.getLocalizedString("travel.search.proceed_to_check_out", "Proceed to check out"), false));
			submitLink.setToFormSubmit(form);
			form.addParameter(ACTION, ACTION_CONFIRM);

			Table linkTable = new Table();
			linkTable.setCellpaddingAndCellspacing(0);
			linkTable.setCellpaddingRight(1, 1, 5);
			linkTable.setCellpaddingLeft(2, 1, 5);
			linkTable.setWidth("100%");
			linkTable.add(backLink, 1, 1);
			linkTable.add(submitLink, 2, 1);
			linkTable.setAlignment(2, 1, Table.HORIZONTAL_ALIGN_RIGHT);
			frame.addBottom(linkTable);
			
			
//			currentSearchPart.setRowHeight(currentSearchPartRow, "10");
//			++currentSearchPartRow;
			
//			currentSearchPart.add(backLink, 1, currentSearchPartRow);
//			currentSearchPart.add(submitLink, 2, currentSearchPartRow);
//			currentSearchPart.setAlignment(2, currentSearchPartRow, Table.HORIZONTAL_ALIGN_RIGHT);

//			currentSearchPart.setCellpaddingLeft(1, currentSearchPartRow, 10);
//			currentSearchPart.setCellpaddingLeft(2, currentSearchPartRow, 10);
//			currentSearchPart.setBorderColor("BLUE");
//			currentSearchPart.setBorder(1);
			
//			SubmitButton submit = new SubmitButton(iwrb.getLocalizedImageButton("travel.search.confirm","Confirm"), ACTION, ACTION_CONFIRM);
//			table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
//			table.add(submit, 1, row);
//			//formTable.setBorder(1);
	
			++row;
			Table logoTable = new Table();
			logoTable.setCellpaddingAndCellspacing(0);
			Collection imgs = null;
			try {
				imgs = getCreditCardBusiness(iwc).getCreditCardTypeImages(getCreditCardBusiness(iwc).getCreditCardClient(supplier, IWTimestamp.RightNow()));
				if (imgs != null && !imgs.isEmpty()) {
					Iterator iter = imgs.iterator();
					int col = 0;
					while (iter.hasNext()) {
						logoTable.add((Image)iter.next(), ++col, 1);
						logoTable.setCellpaddingTop(col, 1, 10);
						logoTable.setCellpaddingRight(col, 1, 5);
					}
					//addInputLine(new String[]{"", "", ""}, new PresentationObject[]{null, null, logoTable});
					//table.add(logoTable, 1, row);
					frame.addLeft(logoTable);
				}
			}catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		else {
			table.mergeCells(1, row, 3, row);
			table.add(getErrorText(iwrb.getLocalizedString("search_product_not_available", "This product is not available on the selected days.")), 1, row);
			++row;
			++row;
			BackButton back = new BackButton(iwrb.getLocalizedImageButton("travelSearch.try_again", "Try again"));
			table.add(back, 1, row);
		}
		getBookingForm().formTable.setBorder(1);

		frame.add(table);
		return frame;
	}
	
	protected void checkBooking() throws RemoteException {
		ProductDetailFrame frame = getProductDetailFrame(getProduct(), 2);
	  Table table = new Table();
	  table.setCellpaddingAndCellspacing(0);
	  table.setCellpaddingLeft(1, 1, 5);
	  
	  frame.add(table);
		add(frame);
		
		ProductHome productHome = (ProductHome) IDOLookup.getHome(Product.class);
		try {
			Product product = getProduct();
			int bookingId = getBookingForm().checkBooking(iwc, true);
			GeneralBookingHome gBookingHome = (GeneralBookingHome) IDOLookup.getHome(GeneralBooking.class);
			GeneralBooking gBooking = null;
			boolean inquirySent = (bookingId == BookingForm.inquirySent); 
			
			if (bookingId > 0) {
				gBooking = gBookingHome.findByPrimaryKey(new Integer(bookingId));	
				gBooking.setCode(this.engine.getCode());
			}
			

			if (gBooking != null) {
			  boolean sendEmail = bf.sendEmails(iwc, gBooking, iwrb);
			  
			  table.add(getText(gBooking.getName()));
			  table.add(getText(", "));
			  table.add(getText(iwrb.getLocalizedString("travel.you_booking_has_been_confirmed","your booking has been confirmed.")));
			  table.add(Text.BREAK);
			  table.add(Text.BREAK);
			  if (sendEmail) {
					table.add(getText(iwrb.getLocalizedString("travel.you_will_reveice_an_email_shortly","You will receive an email shortly confirming your booking.")));
					table.add(Text.BREAK);
					table.add(Text.BREAK);
			  }
			  table.add(getText(iwrb.getLocalizedString("travel.your_credidcard_authorization_number_is","Your creditcard authorization number is")));
			  table.add(getText(" : "));
			  table.add(getText(gBooking.getCreditcardAuthorizationNumber()));
			  table.add(Text.BREAK);
			  table.add(getText(iwrb.getLocalizedString("travel.your_reference_number_is","Your reference number is")));
			  table.add(getText(" : "));
			  table.add(getText(gBooking.getReferenceNumber()));
			  table.add(Text.BREAK);
			  //table.add(getText(gBooking.getReferenceNumber()));
			  //table.add(Text.BREAK);
			  table.add(Text.BREAK);
			  table.add(getText(iwrb.getLocalizedString("travel.if_unable_to_print","If you are unable to print the voucher, write the reference number down else proceed to printing the voucher.")));

			  Link printVoucher = new Link(getText(iwrb.getLocalizedString("travel.print_voucher","Print voucher")));
				printVoucher.addParameter(VoucherWindow.parameterBookingId, gBooking.getID());
				printVoucher.setWindowToOpen(VoucherWindow.class);

			  table.add(printVoucher,1,3);
			  table.setAlignment(1,1,"left");
			  table.setAlignment(1,2,"right");
			  table.setAlignment(1,3,"right");
			}else if (inquirySent) {
				table.add(getText(iwrb.getLocalizedString("travel.inquiry_has_been_sent","Inquiry has been sent")));
				table.add(Text.BREAK);
				table.add(getText(iwrb.getLocalizedString("travel.you_will_reveice_an_confirmation_email_shortly","You will receive an confirmation email shortly.")));
			}else {
				table.add(getText(iwrb.getLocalizedString("travel.booking_failed","Booking failed")));
				table.add(getText(Text.BREAK));
				if (bookingId == BookingForm.errorTooMany) {
					table.add(getText(iwrb.getLocalizedString("travel.there_is_no_availability","There is no availability")));
				} else	if (bookingId == BookingForm.errorTooFew) {
					table.add(getText(iwrb.getLocalizedString("travel.there_is_no_availability","There is no availability")));
				}
			  if (gBooking == null) {
					debug("gBooking == null");
			  }
			}
		} catch (Exception e) {
			table.add(getText(iwrb.getLocalizedString("travek.booking_failed","Booking failed")+" ( "+e.getMessage()+" )"));
			Link backLink = new Link(getLinkText(iwrb.getLocalizedString("travel.back", "Back"), false));
			backLink.setAsBackLink(1);
			frame.addBottom(backLink);
			//table.add(Text.BREAK);
			//table.add(Text.BREAK);
			//table.add(new BackButton(iwrb.getLocalizedImageButton("travel.back", "Back")));
			//e.printStackTrace(System.err);
		}
		
	}

	
	protected void listResults(IWContext iwc, Collection products) throws RemoteException{
		//Collection products = availability.keySet();
		if (products != null && !products.isEmpty()) {
			ProductHome pHome = (ProductHome) IDOLookup.getHome(Product.class);
			SupplierHome sHome = (SupplierHome) IDOLookup.getHome(Supplier.class);
			// TODO move to a better location
			IWTimestamp stamp = new IWTimestamp(iwc.getParameter(PARAMETER_FROM_DATE));
			int days = getBookingForm().getNumberOfDays(stamp);
			TravelStockroomBusiness bus;
			Product product;
			int productId;
			Supplier supplier;
			Link link;
			List addresses;
			Timeframe timeframe;
			ProductPrice[] prices;
			Currency currency;
			Timeframe[] timeframes;
			Image image;
			boolean available;
			Iterator iter = products.iterator();
			int productsSize = products.size();
			//int mapSize = availability.size();
			Vector tmp = new Vector(products);
			Table table = new Table();
			table.setWidth("100%");
			table.setCellpaddingAndCellspacing(0);
			table.setBorder(0);
			add(table);
			
			Table innerTable;
			int resultsRow = 1;
//			int topProduct = productsSize -1;
//			int bottomProduct = 0;
			int topProduct = 0;
			int bottomProduct = productsSize -1;
			if (currentPageNumber > 0) {
				topProduct = topProduct + ( (currentPageNumber -1) * resultsPerPage);
//				topProduct = topProduct - ( (currentPageNumber -1) * resultsPerPage);
				
				if ( (topProduct + resultsPerPage -1) < bottomProduct) {
					bottomProduct= topProduct + resultsPerPage - 1;
				}
//				if ( (topProduct + resultsPerPage) >= (bottomProduct) ) {
//					bottomProduct= topProduct + resultsPerPage - 1;
//				}
			}
			for (int i = (topProduct); i <= bottomProduct ; i++) {
				try {
					product = (Product)tmp.get(i);
					productId = product.getID();
					supplier = sHome.findByPrimaryKey(product.getSupplierId());
					bus = getSearchBusiness(iwc).getServiceHandler().getServiceBusiness(product);
					addresses = getSearchBusiness(iwc).getServiceHandler().getProductBusiness().getDepartureAddresses(product, stamp, true, getPriceCategoryKey());

					//Boolean productAvailability = ((Boolean)availability.get(product.getPrimaryKey()));
					
					//new result listing
					//if (productAvailability != null && productAvailability.booleanValue()) {
						
						resultsRow = addResultProductHeader(product, supplier, table, resultsRow);

						innerTable = new Table(4, 1);
						++resultsRow;
						table.add(innerTable, 1, resultsRow);
						//table.setBorder(0);
						table.mergeCells(1, resultsRow, 3, resultsRow);
						innerTable.setWidth("100%");
						innerTable.setWidth(1, 1, 50);
						innerTable.setVerticalAlignment(2, 1, Table.VERTICAL_ALIGN_TOP);
						innerTable.setVerticalAlignment(3, 1, Table.VERTICAL_ALIGN_TOP);
						innerTable.setAlignment(3, 1, Table.HORIZONTAL_ALIGN_RIGHT);
						innerTable.setWidth(4, 1, 70);
						innerTable.setVerticalAlignment(4, 1, Table.VERTICAL_ALIGN_TOP);
						innerTable.setAlignment(4, 1, Table.HORIZONTAL_ALIGN_RIGHT);

						if (product.getFileId() != -1) {
				      image = new Image(product.getFileId());
				      image.setMaxImageWidth(50);
				      innerTable.add(image, 1, 1);
				      innerTable.setVerticalAlignment(1, 1, Table.VERTICAL_ALIGN_TOP);
				    }
				    int addressCount = 0;
						if (addresses == null || addresses.isEmpty()) {
							int addressId = -1;
							timeframe = getSearchBusiness(iwc).getServiceHandler().getProductBusiness().getTimeframe(product, stamp, addressId);
							int timeframeId = -1;
							if (timeframe != null) {
								timeframeId = timeframe.getID();
							}
							tmpPriceID= getProductDetailFrame(product, 2).addPrices(innerTable, 2, 1, bus, product, timeframeId, addressId, days, Text.NON_BREAKING_SPACE);
						}
						else {
							TravelAddress address;
							Iterator addressesIter = addresses.iterator();
							while (addressesIter.hasNext()) {
								++addressCount;
								address = (TravelAddress) addressesIter.next();
								timeframe = getSearchBusiness(iwc).getServiceHandler().getProductBusiness().getTimeframe(product, stamp, address.getID());
								int timeframeId = -1;
								if (timeframe != null) {
									timeframeId = timeframe.getID();
								}
								tmpPriceID = getProductDetailFrame(product, 2).addPrices(innerTable, 2, 1, bus, product, timeframeId, address.getID(), days, Text.NON_BREAKING_SPACE);
							}
						}
						
						addProductInfo(product, innerTable, 3, 1);
				    innerTable.add(getDetailsLink(productId), 4, 1);
				    if (addressCount <= 1) {
				    		innerTable.add(Text.BREAK, 4, 1);
				    		Link bLink = getBookingLink(productId);
				    		if (addressCount == 1) {
				    			bLink.addParameter(PARAMETER_ADDRESS_ID, ((TravelAddress) addresses.get(0)).getPrimaryKey().toString());
				    		}
				    		innerTable.add(bLink, 4, 1);
				    }
				    
						++resultsRow;
						table.setRowStyleClass(resultsRow, getStyleName(BookingForm.STYLENAME_BLUE_BACKGROUND_COLOR));
						table.setHeight(1, resultsRow++,2);
						
					
					//}
					
					//oldResultListing(iwc, sHome, stamp, days, product, productAvailability);
				}catch(Exception e) {
					e.printStackTrace();
				} 
			}
				
		}
	}
	
	private int addResultProductHeader(Product product, Supplier supplier, Table table, int resultsRow) throws RemoteException {
		table.setHeight(1, resultsRow, 1);
		table.setRowStyleClass(resultsRow, getStyleName(BookingForm.STYLENAME_BACKGROUND_COLOR));
		++resultsRow;
		table.setHeight(1, resultsRow, 1);
		++resultsRow;
		table.add(getHeaderText(supplier.getName()), 1, resultsRow);
		table.add(getSmallText(product.getProductName(localeID)), 3, resultsRow);
		table.setAlignment(3, resultsRow, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setCellpaddingLeft(1, resultsRow, 10);
		//table.setCellpadding(2, resultsRow, 5);
		table.setCellpaddingRight(3, resultsRow, 10);
		table.setHeight(resultsRow, 28);
		table.setRowStyleClass(resultsRow, super.getStyleName(BookingForm.STYLENAME_BACKGROUND_COLOR));
		++resultsRow;
		table.setRowStyleClass(resultsRow, super.getStyleName(BookingForm.STYLENAME_HEADER_BACKGROUND_COLOR));
		table.setHeight(1, resultsRow, 1);
		++resultsRow;
		table.setHeight(1, resultsRow, 1);
		++resultsRow;
		table.setRowStyleClass(resultsRow, super.getStyleName(BookingForm.STYLENAME_BLUE_BACKGROUND_COLOR));
		table.setHeight(1, resultsRow, 1);

//		++resultsRow;
//		table.setHeight(1, resultsRow, 3);

//		table.setRowStyleClass(++resultsRow, super.getStyleName(ServiceSearch.STYLENAME_BLUE_BACKGROUND_COLOR));
//		table.setHeight(1, resultsRow, 3);
		
		return ++resultsRow;
	}

	/**
	 * @param iwc
	 * @param sHome
	 * @param stamp
	 * @param days
	 * @param product
	 * @param productAvailability
	 * @throws FinderException
	 * @throws RemoteException
	 * @throws IDOFinderException
	 * @throws SQLException
	 */
	private void oldResultListing(IWContext iwc, SupplierHome sHome, IWTimestamp stamp, int days, Product product, Boolean productAvailability) throws FinderException, RemoteException, IDOFinderException, SQLException {
		TravelStockroomBusiness bus;
		Supplier supplier;
		Link link;
		List addresses;
		Timeframe timeframe;
		boolean available;
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
				row = oldGetPrices(iwc, stamp, bus, product, days, table, row, addressId, timeframeId);
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
					row =oldGetPrices(iwc, stamp, bus, product, days, table, row, address.getID(), timeframeId);
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
	}
	
	protected Link addParameters(Link po, int productId) {
		if (po instanceof Link) {
			((Link)po).maintainParameter(ServiceSearch.PARAMETER_SERVICE_SEARCH_FORM, iwc);
			((Link)po).addParameter(ACTION, ACTION_BOOKING_FORM);
			((Link)po).maintainParameter(PARAMETER_FROM_DATE, iwc);
			((Link)po).maintainParameter(PARAMETER_MANY_DAYS, iwc);
			((Link)po).maintainParameter(PARAMETER_TO_DATE, iwc);
			((Link)po).addParameter(getBookingForm().getParameterTypeCountName(), getCount());
			if (productId > 0) {
				((Link)po).addParameter(PARAMETER_PRODUCT_ID, productId);
			}
			//((Link)po).addParameter(PARAMETER_PRODUCT_PRICE_ID, tmpPriceID);
			((Link)po).addParameter(BookingForm.PARAMETER_CODE, engine.getCode());
			((Link)po).addParameter(BookingForm.parameterPriceCategoryKey, getPriceCategoryKey());
			maintainEngineSpecificParameters(((Link)po));
		}
		
		return po;
	}
	protected Form addParameters(Form po, int productId, boolean isSearchForm) {
		if ( po instanceof Form ) {

			if (targetPage != null) {
				((Form)po).setPageToSubmitTo(targetPage);
			}
			((Form)po).maintainParameter(ServiceSearch.PARAMETER_SERVICE_SEARCH_FORM);
			((Form)po).addParameter(BookingForm.PARAMETER_CODE, engine.getCode());
			((Form)po).addParameter(BookingForm.parameterPriceCategoryKey, getPriceCategoryKey());
			//((Form)po).addParameter(ACTION, ACTION_BOOKING_FORM);
			
			if (!isSearchForm) {
				po.maintainParameter(ACTION);
				po.maintainParameter(AbstractSearchForm.PARAMETER_POSTAL_CODE_NAME);
				((Form)po).maintainParameter(PARAMETER_FROM_DATE);
				((Form)po).maintainParameter(PARAMETER_MANY_DAYS);
				((Form)po).maintainParameter(PARAMETER_TO_DATE);
				((Form)po).addParameter(getBookingForm().getParameterTypeCountName(), getCount());
				if (iwc.isParameterSet(PARAMETER_ADDRESS_ID)) {
					po.maintainParameter(PARAMETER_ADDRESS_ID);
				}
				if (productId > 0) {
					((Form)po).addParameter(PARAMETER_PRODUCT_ID, productId);
				}
				//((Form)po).addParameter(PARAMETER_PRODUCT_PRICE_ID, tmpPriceID);
				po.maintainParameter(PARAMETER_POSTAL_CODE);
				
				Collection coll = getParametersInUse();
				if (coll != null && !coll.isEmpty()) {
					Iterator iter = coll.iterator();
					while (iter.hasNext()) {
						po.maintainParameter(iter.next().toString());
					}
				}
			}
			
		}
		
		return po;
	}

	protected Link getBookingLink(int productId) {
		Link link = new Link(getLinkText( iwrb.getLocalizedString("travel.book","Book"), false ));
		return addParameters(link, productId);
	}
	
	protected Link getNextOrPreviousLink(boolean next) {
		Link link = new Link();
		if (next) {
			return getPageLink(iwrb.getLocalizedString("travel.search.next", "Next"), currentPageNumber+1);
		} else {
			return getPageLink(iwrb.getLocalizedString("travel.search.previous", "Previous"), currentPageNumber-1);
		}
	}

	protected Link getPageLink(String content, int pageNumber) {
		Link link = new Link(getLinkText(content, false));
		link.maintainParameter(ServiceSearch.PARAMETER_SERVICE_SEARCH_FORM, iwc);
		link.maintainParameter(PARAMETER_FROM_DATE, iwc);
		link.maintainParameter(ACTION, iwc);
		link.maintainParameter(PARAMETER_POSTAL_CODE_NAME, iwc);
		link.maintainParameter(PARAMETER_MANY_DAYS, iwc);
		link.maintainParameter(PARAMETER_TO_DATE, iwc);
		link.maintainParameter(PARAMETER_SORT_BY, iwc);
		link.maintainParameter(PARAMETER_MANY_DAYS, iwc);
		link.addParameter(PARAMETER_PAGE_NR, pageNumber);
		
		maintainEngineSpecificParameters(link);
		return link;
	}
	
	protected Link getDetailsLink(int productId) {
		Link link = new Link(getLinkText(iwrb.getLocalizedString("travel.search.view_details","View details"), false));
		link.maintainParameter(ServiceSearch.PARAMETER_SERVICE_SEARCH_FORM, iwc);
		link.addParameter(ACTION, ACTION_PRODUCT_DETAILS);
		link.maintainParameter(PARAMETER_FROM_DATE, iwc);
		link.maintainParameter(PARAMETER_MANY_DAYS, iwc);
		link.maintainParameter(PARAMETER_TO_DATE, iwc);
		link.addParameter(getBookingForm().getParameterTypeCountName(), getCount());
		link.addParameter(PARAMETER_PRODUCT_ID, productId);
		//link.addParameter(PARAMETER_PRODUCT_PRICE_ID, tmpPriceID);
		link.maintainParameter(PARAMETER_ADDRESS_ID, iwc);
		maintainEngineSpecificParameters(link);
		return link;
	}
	
	private void maintainEngineSpecificParameters(Link link) {
		Collection coll = this.getParametersInUse();
		if (coll != null && !coll.isEmpty()) {
			Iterator iter = coll.iterator();
			while (iter.hasNext() ) {
				link.maintainParameter((String) iter.next(), iwc);
			}
		}
	}

	private int oldGetPrices(IWContext iwc, IWTimestamp stamp, TravelStockroomBusiness bus, Product usedProduct, int days, Table table, int row, int addressId, int timeframeId) throws RemoteException, SQLException {
		ProductPrice[] prices;
		Currency currency;
		prices = getProductPrices(usedProduct, addressId, timeframeId);
		//prices = ProductPriceBMPBean.getProductPrices(usedProduct.getID(), timeframeId, addressId, new int[] {PriceCategoryBMPBean.PRICE_VISIBILITY_PUBLIC, PriceCategoryBMPBean.PRICE_VISIBILITY_BOTH_PRIVATE_AND_PUBLIC}, getPriceCategoryKey());
		for (int i = 0; i < prices.length; i++) {
			tmpPriceID = prices[i].getID();
			table.add(getText(oldGetPriceString(bus, usedProduct.getID(), timeframeId, prices[i], days)), 1, row++);
		}
		return row;
	}

	private ProductPrice[] getProductPrices(Product usedProduct, int addressId, int timeframeId) throws RemoteException {
		return ProductPriceBMPBean.getProductPrices(usedProduct.getID(), timeframeId, addressId, new int[] {PriceCategoryBMPBean.PRICE_VISIBILITY_PUBLIC, PriceCategoryBMPBean.PRICE_VISIBILITY_BOTH_PRIVATE_AND_PUBLIC}, getPriceCategoryKey());
	}
	/* TODO trespps 
	void addPrices(Table table, int column, int row, TravelStockroomBusiness bus, Product product, int timeframeId, int addressId, int days, String seperator) throws SQLException, RemoteException {
		ProductPrice[] prices = getProductPrices(product, addressId, timeframeId);
		for (int i = 0; i < prices.length; i++) {
			tmpPriceID = prices[i].getID();
			addPrices(table, column, row, bus, product.getID(), timeframeId, addressId, prices[i], days, seperator);
		}
	}
*/
	/*
	
	private void addPrices(Table table, int column, int row, TravelStockroomBusiness bus, int productId, int timeframeId, int travelAddressId, ProductPrice pPrice, int days, String seperator) throws SQLException, RemoteException {
		float price = bus.getPrice(pPrice.getID(), productId ,pPrice.getPriceCategoryID() , pPrice.getCurrencyId(), IWTimestamp.getTimestampRightNow(), timeframeId, -1 );
		float total = -1;
		String returner = "";

		int count = getCount();
		Currency currency;
		String currAbbr = "";
		try {
			currency = ((com.idega.block.trade.data.CurrencyHome)com.idega.data.IDOLookup.getHome(Currency.class)).findByPrimaryKeyLegacy(pPrice.getCurrencyId());
			currAbbr = currency.getCurrencyAbbreviation();
		}catch (Exception e) {
			currency = null;
		}
		TravelAddress tAddress = null;
		try {
			if (travelAddressId > 0) {
				tAddress = ((TravelAddressHome) IDOLookup.getHome(TravelAddress.class)).findByPrimaryKey(travelAddressId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		if (days == 0) {
			days = 1;
		}
		if (count == 0) {
			count = 1;
		}
		
		total = price * days * count;
		if (tAddress != null) {
			table.add(getText(tAddress.getName()+Text.BREAK), column, row);
		}
		table.add(getText(iwrb.getLocalizedString("travel.price","Price")+":"+seperator),column, row);
		table.add(getSmallText(currencyFormat.format(price*count)+Text.NON_BREAKING_SPACE+currAbbr), column, row);
		if (days > 1) {
			table.add(getSmallText(Text.NON_BREAKING_SPACE+iwrb.getLocalizedString("travel.search.per_night","per night")), column, row);
		}
		if (count > 1) {
			table.add(getSmallText(" ("+currencyFormat.format(price)+Text.NON_BREAKING_SPACE+currAbbr+" per "+getUnitName()+")"), column, row);
		}
		table.addBreak(column, row);
		table.add(getText(iwrb.getLocalizedString("travel.search.total","Total")+":"+seperator), column, row);
		table.add(getSmallText(currencyFormat.format(total)+Text.NON_BREAKING_SPACE+currAbbr+seperator), column, row);
		table.addBreak(column, row);
	
	}
	*/
	private String oldGetPriceString(TravelStockroomBusiness bus, int productId, int timeframeId, ProductPrice pPrice, int days) throws SQLException, RemoteException {
		float price = bus.getPrice(pPrice.getID(), productId ,pPrice.getPriceCategoryID() , pPrice.getCurrencyId(), IWTimestamp.getTimestampRightNow(), timeframeId, -1 );
/*
		Currency currency;
		try {
			currency = ((com.idega.block.trade.data.CurrencyHome)com.idega.data.IDOLookup.getHomeLegacy(Currency.class)).findByPrimaryKeyLegacy(pPrice.getCurrencyId());
		}catch (Exception e) {
			currency = null;
		}*/
		float total = -1;
		String returner = "";

		int count = getCount();
		/*
		int days = 1;
		try {
			days = Integer.parseInt(iwc.getParameter(PARAMETER_MANY_DAYS));
		} catch (NumberFormatException n) {
			logDebug("SearchForm : days set to 1");
		}*/
		
		total = price * days * count;
		returner += iwrb.getLocalizedString("travel.price","Price")+":"+Text.NON_BREAKING_SPACE+currencyFormat.format(price*count);
//		returner += iwrb.getLocalizedString("travel.price","Price")+":"+Text.NON_BREAKING_SPACE+(price*count)+Text.NON_BREAKING_SPACE+currency.getCurrencyAbbreviation();
		if (days > 1) {
			returner += Text.NON_BREAKING_SPACE+iwrb.getLocalizedString("travel.search.per_nigth","per night");
		}
		if (count > 1) {
			returner += " ("+currencyFormat.format(price)+" per "+getUnitName()+")";
		}
		returner += Text.BREAK+iwrb.getLocalizedString("travel.search.total","Total")+":"+Text.NON_BREAKING_SPACE+currencyFormat.format(total);
		return returner;
	}

	private String getUnitName() {
		return bf.getUnitName();
	}
	
	protected int getCount() {
		String sCount = iwc.getParameter(getBookingForm().getParameterTypeCountName());
		int count = 0;
		try {
			count = Integer.parseInt(sCount);
		} catch (NumberFormatException ignore){}
		return count;
	}

	protected Text getText(String content) {
		Text text = new Text(content);
		if (getStyleName(BookingForm.STYLENAME_TEXT) != null) {
			text = getStyleText(text, BookingForm.STYLENAME_TEXT);
		}
		else if (textFontStyle != null) {
			text.setFontStyle(textFontStyle);
		}
		return text;
	}

	protected Text getHeaderText(String content) {
		Text text = new Text(content);
		if (getStyleName(BookingForm.STYLENAME_HEADER_TEXT) != null) {
			text = getStyleText(text, BookingForm.STYLENAME_HEADER_TEXT);
		}
		else if (headerFontStyle != null) {
			text.setFontStyle(headerFontStyle);
		}
		
		return text;
	}
	
	protected Text getSmallText(String content) {
		Text text = new Text(content);
		if (getStyleName(BookingForm.STYLENAME_SMALL_TEXT) != null) {
			text = getStyleText(text, BookingForm.STYLENAME_SMALL_TEXT);
		}
		return text;
	}
	
	protected Text getOrangeText(String content) {
		Text text = new Text(content);
		if (getStyleName(BookingForm.STYLENAME_ORANGE_TEXT) != null) {
			text = getStyleText(text, BookingForm.STYLENAME_ORANGE_TEXT);
		}
		return text;
	}
	
	protected Text getLinkText(String content, boolean clicked) {
		Text text = new Text(content);
		if (clicked) {
			if (getStyleName(BookingForm.STYLENAME_CLICKED_LINK) != null) {
				text = getStyleText(text, BookingForm.STYLENAME_CLICKED_LINK);
			}
			else if (clickedLinkFontStyle != null) {
				text.setFontStyle(clickedLinkFontStyle);
			}
		}
		else {
			if (getStyleName(BookingForm.STYLENAME_LINK) != null) {
				text = getStyleText(text, BookingForm.STYLENAME_LINK);
			}
			else if (linkFontStyle != null) {
				text.setFontStyle(linkFontStyle);
			}
		}
		
		return text;
	}

	protected Text getErrorText(String content) {
		Text text = new Text(content);
		if (getStyleName(BookingForm.STYLENAME_ERROR_TEXT) != null) {
			text = getStyleText(text, BookingForm.STYLENAME_ERROR_TEXT);
		}
		else if (errorFontStyle != null) {
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
	
	public void setHeaderImageTiler(Image image) {
		this.headerImageTiler = image;
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
	
	public void setSearchPartBottomBorderColor(String color) {
		searchPartBottomBorderColor = color;
	}
	
	public void setSearchPartBottomBorderWidth(String width) {
		searchPartBottomBorderWidth = width;
	}
	
	public void setSearchPartTopBorderColor(String color) {
		searchPartTopBorderColor = color;
	}
	
	public void setSearchPartTopBorderWidth(String width) {
		searchPartTopBorderWidth = width;
	}
	
	public void setSearchPartColor(String color) {
		searchPartColor = color;
	}
	
	public void setTargetPage(ICPage page) {
		if (page != null) {
			this.targetPage = page;
			this.isAlwaysSearchForm = true;
		}
	}

	
	protected Product getProduct() {
		try {
			ProductHome home = (ProductHome) IDOLookup.getHome(Product.class);
			String tmp = iwc.getParameter(PARAMETER_PRODUCT_ID);
			return home.findByPrimaryKey(new Integer(iwc.getParameter(PARAMETER_PRODUCT_ID)));
		}catch (Exception e) {
			return null;
		}
	}
	
	protected boolean hasDefinedProduct() throws RemoteException {
		return (getSession(iwc).getState() == this.STATE_DEFINED_PRODUCT && definedProduct != null);
	}

	protected DropdownMenu getDropdownWithNumbers(String name, int startNumber, int endNumber) {
		DropdownMenu menu = new DropdownMenu(name);
		for (int i = startNumber; i <= endNumber; i++) {
			menu.addMenuElement(i, Integer.toString(i));
		}		
		menu.setSelectedElement(startNumber);
		return menu;
	}


	public void setServiceSearchEngine(ServiceSearchEngine engine) {
		this.engine = engine;
	}
	
	public void setResultsPerPage(int resultsPerPage) {
		this.resultsPerPage = resultsPerPage;
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

	private void handleResults(Collection coll) throws IDOLookupException, FinderException, RemoteException {

		int collSize = coll.size();
		
		String results = iwrb.getLocalizedString("travel.search.results", "results");
		if (collSize == 1) {
			results = iwrb.getLocalizedString("travel.search.result", "result");
		}
		Form form = new Form();
		form = (Form) addParameters(form, -1, false);
		Table table = new Table(3, 4);
		table.setWidth("100%");
		table.setCellspacing(0);
		table.setCellpadding(0);
		table.setWidth(1, "33%");
		table.setWidth(2, "34%");
		table.setWidth(3, "33%");
		table.setCellpaddingLeft(1, 1, 10);
		table.setCellpaddingRight(3, 1, 10);
		table.setHeight(33);
		table.setRowStyleClass(1, getStyleName(BookingForm.STYLENAME_HEADER_BACKGROUND_COLOR));
		table.setHeight(2, 1);
		table.setHeight(3, 1);
		table.setHeight(4, 3);
		table.setRowStyleClass(3, getStyleName(BookingForm.STYLENAME_BLUE_BACKGROUND_COLOR));
		int totalPages = (int )Math.ceil((double) collSize / (double) resultsPerPage); 
		
		if (currentPageNumber > 1) {
			table.add(getNextOrPreviousLink(false), 2, 1);
		}
		for (int i = 1; i <= totalPages; i++) {
			table.add(getPageLink(" "+Integer.toString(i)+" ", i), 2, 1);
		}
		//table.add(""+totalPages, 2, 1);
		if (currentPageNumber < totalPages) {
			table.add(getNextOrPreviousLink(true), 2, 1);
		}
		table.setAlignment(2, 1, Table.HORIZONTAL_ALIGN_CENTER);
		
		Table bottomTable = (Table) table.clone();
		
		table.add(getHeaderText(iwrb.getLocalizedString("travel.search.sort_by", "Sort by")+":"+Text.NON_BREAKING_SPACE), 1, 1);
		table.add(getSortMenu(), 1, 1);
		
		table.add(getHeaderText(collSize+Text.NON_BREAKING_SPACE+results), 3, 1);
		table.setAlignment(3, 1, Table.HORIZONTAL_ALIGN_RIGHT);
//		table.add("gimmi", 1, 1);
		form.add(table);
		add(form);
		
		listResults(iwc, coll);
	
		add(bottomTable);

	}
	
	
	private DropdownMenu getSortMenu() {
		DropdownMenu menu = new DropdownMenu(PARAMETER_SORT_BY);
		menu.addMenuElement(ProductComparator.NAME, iwrb.getLocalizedString("travel.name", "Name"));
		menu.addMenuElement(ProductComparator.PRICE, iwrb.getLocalizedString("travel.price", "Price"));
		menu.addMenuElement(ProductComparator.SUPPLIER, iwrb.getLocalizedString("travel.supplier", "Supplier"));
		menu.setSelectedElement(getSortMethod());
		menu.setToSubmit(true);
		if (formInputStyle != null) {
			menu.setStyleAttribute(formInputStyle);
		} else {
			menu.setStyleClass(getStyleName(BookingForm.STYLENAME_INTERFACE));
		}
		return menu;
	}
	/**
	 * @param coll
	 * @return
	 * @throws IDOLookupException
	 * @throws FinderException
	 * @throws RemoteException
	 */
	private Collection filterResults(IWContext iwc, Collection coll) throws IDOLookupException, FinderException, RemoteException {
		String sFromDate = iwc.getParameter(PARAMETER_FROM_DATE);
		PriceCategoryHome pcHome = (PriceCategoryHome) IDOLookup.getHome(PriceCategory.class);
		PriceCategory priceCat = pcHome.findByKey(getPriceCategoryKey());
		coll = getSearchBusiness(iwc).sortProducts(iwc, coll, priceCat, new IWTimestamp(sFromDate), getSortMethod());
		return coll;
	}
	
	private int getSortMethod() {
		String sSortBy = this.iwc.getParameter(PARAMETER_SORT_BY);
		try {
			if (sSortBy != null) {
				return Integer.parseInt(sSortBy);
			}
		} catch (NumberFormatException ignore) {}
		
		return getDefaultSortMethod();
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
	
	
	protected ServiceSearchSession getSession(IWContext iwc) {
		try {
			return (ServiceSearchSession) IBOLookup.getSessionInstance(iwc, ServiceSearchSession.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException();
		}
	}
	
	
	protected TravelBlock getProductDetails(IWContext iwc) throws RemoteException {
		ProductDetailFrame frame = getProductDetailFrame(getProduct(), 3);

		Vector vector = new Vector();
		vector.add(definedProduct);
		Collection coll = getSearchBusiness(iwc).checkResults(iwc, vector);
		
		if ( coll != null && coll.contains(definedProduct) ) {
			Table table = new Table(1, 4);
			int row = 1;
			if (frame.ch != null) {
				LocalizedText text = frame.ch.getLocalizedText();
				List images = frame.ch.getFiles();
				if (text.getHeadline() != null) {
					table.setCellpaddingLeft(1, row, 5);
					table.setCellpaddingTop(1, row, 5);
					table.add(this.getText(text.getHeadline()), 1, row);
				}
				++row;
				if (text.getBody() != null) {
					table.setCellpaddingLeft(1, row, 5);
					table.setCellpaddingBottom(1, row, 20);
					table.add(this.getSmallText(text.getBody()), 1, row);
				}
				if (images != null && !images.isEmpty()) {
					++row;
					table.setCellpaddingLeft(1, row, 5);
					table.setCellpaddingBottom(1, row, 5);
					table.add(getText(iwrb.getLocalizedString("travel.search.view_more_photos", "View more photos")), 1, row);

					++row;
					table.setCellpaddingLeft(1, row, 5);

					Iterator iter = images.iterator();
					ICFile file;
					Image image;
					Window window;
					Link link;
					while (iter.hasNext()) {
						file = (ICFile) iter.next();
						try {
							image = new Image(Integer.parseInt(file.getPrimaryKey().toString()));
							image.setMaxImageWidth(35);
							window = new Window(new Image(Integer.parseInt(file.getPrimaryKey().toString())));
							link = new Link(image);
							link.setWindow(window);
							table.add(link, 1, row);
							table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			} else {
				table.setCellpaddingLeft(1, row, 5);
				table.add(getText(iwrb.getLocalizedString("travel.search.no_info_available", "No information is available for the selected product.")), 1, row);
			}
			table.setCellpaddingAndCellspacing(0);
			table.setBorder(0);
			table.setWidth("100%");
			table.setHeight(1, 2, "100%");
			
			frame.add(table);
			frame.addBottom(getDetailLinks(frame.product, frame.depAddresses));
		} 
		else {
			Table table = new Table(1, 1);
			table.add(iwrb.getLocalizedString("travel.search.product_unavailable", "The product you have selected is unavailable for the selected days."));
			frame.add(table);
		}
			
		
		return frame;
	}
	
	private Form getDetailLinks(Product product, Collection departureAddresses) throws RemoteException {
		Form form = new Form();
		form = addParameters(form, product.getID(), false);
		Table linkTable = new Table(3, 1);
		form.add(linkTable);
		try {
			if (departureAddresses != null && !departureAddresses.isEmpty()) {
				DropdownMenu addresses = new DropdownMenu(PARAMETER_ADDRESS_ID);
				addresses.addMenuElements(departureAddresses);
				addresses.setStyleClass(getStyleName(BookingForm.STYLENAME_INTERFACE));
				//linkTable.add( getSmallText(iwrb.getLocalizedString("travel.search.departure_from", "Departure from")+" : "), 3 ,1);
				linkTable.add( addresses, 2, 1);
				linkTable.add( Text.NON_BREAKING_SPACE + Text.NON_BREAKING_SPACE);
				/*Iterator iter = depAddresses.iterator();
				TravelAddress ta;
				while (iter.hasNext()) {
					ta = (TravelAddress) iter.next();
				}*/
			}
		} catch (Exception e) {
			
		}
		linkTable.setCellpaddingAndCellspacing(0);
		linkTable.setCellpaddingLeft(1, 1, 5);
		linkTable.setCellpaddingRight(2, 1, 5);
		linkTable.setCellpaddingRight(3, 1, 5);
		linkTable.setVerticalAlignment(1, 1, Table.VERTICAL_ALIGN_MIDDLE);
		linkTable.setVerticalAlignment(2, 1, Table.VERTICAL_ALIGN_MIDDLE);
		linkTable.setVerticalAlignment(3, 1, Table.VERTICAL_ALIGN_MIDDLE);
		linkTable.setWidth("100%");
		linkTable.setAlignment(2, 1, Table.HORIZONTAL_ALIGN_RIGHT);
		linkTable.setAlignment(3, 1, Table.HORIZONTAL_ALIGN_RIGHT);
		linkTable.setWidth(3, 1, "20");
		Link back = new Link(getLinkText(iwrb.getLocalizedString("travel.search.back_to_results","Back to results"), false));
		back.setAsBackLink();
		linkTable.add(back, 1, 1);
		Link bookingLink = getBookingLink(product.getID());
		//bookingLink.setImage(bundle.getImage("images/book_01.jpg"));
		SubmitButton button = new SubmitButton(bundle.getImage("images/book_01.jpg"));
		form.addParameter(ACTION, ACTION_BOOKING_FORM);
		//bookingLink.setToFormSubmit(form);
		//linkTable.add(bookingLink, 3, 1);
		linkTable.add(button, 3, 1);
		return form;
	}	
	
	protected ProductDetailFrame getProductDetailFrame(Product product, int columns) throws RemoteException {
		ProductDetailFrame frame = (ProductDetailFrame) frames.get(columns+""+product);
		if (frame == null) { 
			frame = new ProductDetailFrame(iwc, product);
			frame.setPriceCategoryKey(getPriceCategoryKey());
			frame.setCount(this.getCount());
			frame.setProductInfoDetailed(getProductInfoDetailed(product));
			frames.put(columns+""+product, frame);
		}
		return frame;
	}
	
	protected BookingForm getBookingForm() {
		if (bf == null) {
			try {
				bf = getSearchBusiness(iwc).getServiceHandler().getBookingForm(iwc, getProduct());
				if (backgroundColor != null) {
					bf.formTable.setColor(backgroundColor);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return bf;
	}
	
}
