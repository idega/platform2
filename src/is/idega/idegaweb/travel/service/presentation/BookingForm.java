package is.idega.idegaweb.travel.service.presentation;

import is.idega.idegaweb.travel.business.ServiceNotFoundException;
import is.idega.idegaweb.travel.business.TimeframeNotFoundException;
import is.idega.idegaweb.travel.data.BookingEntry;
import is.idega.idegaweb.travel.data.BookingEntryHome;
import is.idega.idegaweb.travel.data.Contract;
import is.idega.idegaweb.travel.data.GeneralBooking;
import is.idega.idegaweb.travel.data.GeneralBookingHome;
import is.idega.idegaweb.travel.data.Service;
import is.idega.idegaweb.travel.data.ServiceDay;
import is.idega.idegaweb.travel.data.ServiceDayHome;
import is.idega.idegaweb.travel.interfaces.Booking;
import is.idega.idegaweb.travel.presentation.LinkGenerator;
import is.idega.idegaweb.travel.presentation.PublicBooking;
import is.idega.idegaweb.travel.presentation.TravelManager;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.mail.MessagingException;

import com.idega.block.creditcard.business.CreditCardAuthorizationException;
import com.idega.block.creditcard.business.CreditCardClient;
import com.idega.block.creditcard.data.CreditCardMerchant;
import com.idega.block.trade.data.Currency;
import com.idega.block.trade.data.CurrencyHome;
import com.idega.block.trade.stockroom.business.ProductPriceException;
import com.idega.block.trade.stockroom.business.ResellerManager;
import com.idega.block.trade.stockroom.business.SupplierManager;
import com.idega.block.trade.stockroom.data.PriceCategory;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.ProductPrice;
import com.idega.block.trade.stockroom.data.ProductPriceBMPBean;
import com.idega.block.trade.stockroom.data.ProductPriceHome;
import com.idega.block.trade.stockroom.data.Reseller;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.block.trade.stockroom.data.SupplierHome;
import com.idega.block.trade.stockroom.data.Timeframe;
import com.idega.block.trade.stockroom.data.TravelAddress;
import com.idega.core.user.data.User;
import com.idega.data.IDOException;
import com.idega.data.IDOFinderException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.CalendarParameters;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.HorizontalRule;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.ResultOutput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWCalendar;
import com.idega.util.IWTimestamp;
import com.idega.util.SendMail;
/**
 * <p>Title: idega</p>
 * <p>Description: software</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: idega software</p>
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public abstract class BookingForm extends TravelManager{

	public static final int errorTooMany = -1;
	public List errorDays = new Vector();
	public static final int errorFieldsEmpty = -2;
	public List errorFields = new Vector();
	public static final int errorTooFew = -3;
  public static final int inquirySent = -10;
  public static String parameterDepartureAddressId = "depAddrId";

  protected IWResourceBundle iwrb;
  protected IWBundle bundle;
  protected Supplier supplier;
  protected Product _product;
  protected Service _service;
  protected Reseller _reseller;
  protected Contract _contract;
  protected int _productId;
  protected int _resellerId;
  protected int _contractId;
  protected IWTimestamp _stamp;
  protected Booking _booking;
  protected int[] _multipleBookingNumber = new  int[] {0, 0, 0};
  protected boolean _multipleBookings = false;
  protected boolean useCVC = true;
  protected CreditCardMerchant ccMerchant = null;

	public static final String PARAMETER_EMAIL_FOR_ERROR_NOTIFICATION = "error_email";
	public static final String PARAMETER_CC_EMAIL_FOR_ERROR_NOTIFICATION = "error_email_cc";
  protected int available = is.idega.idegaweb.travel.presentation.Booking.available;
  protected int availableIfNoLimit = is.idega.idegaweb.travel.presentation.Booking.availableIfNoLimit;
  protected DecimalFormat df = new DecimalFormat("0.00");

  public static String BookingAction = is.idega.idegaweb.travel.presentation.Booking.BookingAction;
  protected String BookingParameter = is.idega.idegaweb.travel.presentation.Booking.BookingParameter;
  public static String parameterBookingId = is.idega.idegaweb.travel.presentation.Booking.parameterBookingId;
  public static String parameterUpdateBooking = "bookingUpdateBooking";
  protected static String parameterBookAnyway = "bookingBookAnyway";
  public static String parameterInquiry = "bookingInquiry";
  protected static String parameterSendInquery = "bookingSendInquery";
  protected static String parameterSupplierId = "bookingSupplierId";
  public static String parameterCCNumber = "CCNumber";
  public static String parameterCCMonth  = "CCMonth";
  public static String parameterCCYear   = "CCYear";
  public static String parameterCCCVC = "CCCVC";
  public static String parameterPickupId = "bookingPicId";
  public static String parameterPickupInf= "bookingPicInf";
  public static String parameterPriceCategoryKey = "pcatkey";
  public static  String parameterCountToCheck = "bf_c2chk";

  public static String sAction = "bookingFormAction";
  public static String parameterSaveBooking = "bookingFormSaveBooking";

  public static String parameterFromDate = "bookingFromDate";
  public static String parameterManyDays = "bookingManyDays";
  public static String parameterOnlineBooking = "pr_onl_bking";

  public static final String PARAMETER_FIRST_NAME = "surname";
  public static final String PARAMETER_LAST_NAME = "lastname";
  public static final String PARAMETER_ADDRESS = "address";
  public static final String PARAMETER_AREA_CODE = "area_code";
  public static final String PARAMETER_EMAIL = "e-mail";
  public static final String PARAMETER_PHONE = "telephone_number";
  public static final String PARAMETER_CITY = "city";
  public static final String PARAMETER_COUNTRY = "country";
  public static final String PARAMETER_COMMENT = "comment";
  public static final String PARAMETER_CODE = "code";
  
  
	protected int pWidthLeft = 60;
	protected int pWidthCenter = 60;
	protected int pWidthRight = 75;
	// TODO see if this is used somewhere 
	//public CreditCardClient creditCardClient;
	protected boolean orderAddresses = false;

  protected boolean _useInquiryForm = false;


  public BookingForm(IWContext iwc, Product product) throws Exception{
    super.initializer(iwc);
    setProduct(iwc, product);
    iwrb = super.getResourceBundle();
    bundle = super.getBundle();
    supplier = super.getSupplier();
    _reseller = super.getReseller();
    if ((_reseller != null) && (product != null)){
    		_contract = getContractBusiness(iwc).getContract(_reseller, product);
	    _contractId = ((Integer) _contract.getPrimaryKey()).intValue();
    }
    String sBookingId = iwc.getParameter(this.parameterBookingId);
    if (sBookingId != null) {
	    	try {
	        int bookingId = Integer.parseInt(sBookingId);
	        _booking = ((is.idega.idegaweb.travel.data.GeneralBookingHome)com.idega.data.IDOLookup.getHome(GeneralBooking.class)).findByPrimaryKey(new Integer(bookingId));
	        _multipleBookingNumber = getBooker(iwc).getMultipleBookingNumber((GeneralBooking)_booking);
	        if (_multipleBookingNumber[1] > 1 ) {
	        		_multipleBookings = true;
	        }
	    	}catch (FinderException fe) {
	    		/** not handled */	
	    	}
    }  
    
		Supplier supp = product.getSupplier();
		ccMerchant = getCreditCardBusiness(iwc).getCreditCardMerchant(supp, IWTimestamp.RightNow());
		useCVC = getCreditCardBusiness(iwc).getUseCVC(ccMerchant);

    setTimestamp(iwc);
  }

  protected Form getExpiredForm(IWContext iwc) {
    Form form = new Form();
    Table table = new Table();
    form.add(table);
    table.add(iwrb.getLocalizedString("travel.time_for_booking_has_passed","Time for booking has passed"));
    return form;
  }

  public void main(IWContext iwc)throws Exception {
    super.main(iwc);
    
  }


  public void setTimestamp(IWTimestamp stamp) {
    _stamp = new IWTimestamp(stamp);
  }

  public void setBooking(Booking booking) throws RemoteException, FinderException {
    _booking = booking;
  }


  // IMPLEMENTA


  public Form getBookingForm(IWContext iwc) throws RemoteException, FinderException {
    Form form = new Form();
    Table table = new Table();
      form.add(table);
      form.addParameter(CalendarParameters.PARAMETER_YEAR,_stamp.getYear());
      form.addParameter(CalendarParameters.PARAMETER_MONTH,_stamp.getMonth());
      form.addParameter(CalendarParameters.PARAMETER_DAY,_stamp.getDay());
      if (supplier != null) {
        form.addParameter(this.parameterSupplierId, supplier.getID());
      }
      table.setWidth("100%");

    table.setColumnAlignment(1,"right");
    table.setColumnAlignment(2,"left");
    table.setColumnAlignment(3,"right");
    table.setColumnAlignment(4,"left");

//      ProductPrice[] pPrices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(_service.getID(), false);
    List addresses;
    try {
			addresses = super.getProductBusiness(iwc).getDepartureAddresses(_product, _stamp, orderAddresses);
//      addresses = _product.getDepartureAddresses(false);
    }catch (IDOFinderException ido) {
      ido.printStackTrace(System.err);
      addresses = new Vector();
    }
    TravelAddress tAddress;
    int addressId = -1;
    String sAddressId = iwc.getParameter(parameterDepartureAddressId);
    if (sAddressId != null) {
      addressId = Integer.parseInt(sAddressId);
    }else if (addresses.size() > 0) {
      addressId = ((TravelAddress) addresses.get(0)).getID();
    }


		int bookingDays = 1;
    ProductPrice[] prices = {};
    ProductPrice[] misc = {};
    Timeframe tFrame = getProductBusiness(iwc).getTimeframe(_product, _stamp, addressId);
    int timeframeId = -1;
    if (tFrame != null) {
      timeframeId = tFrame.getID();
      prices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(_service.getID(), tFrame.getID(), addressId, false);
      misc = ProductPriceBMPBean.getMiscellaneousPrices(_service.getID(), tFrame.getID(), addressId, false);
    }else {
      prices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(_service.getID(), -1, -1, false);
      misc = ProductPriceBMPBean.getMiscellaneousPrices(_service.getID(), -1, -1, false);
    }

    if (prices.length > 0) {

        int row = 1;
        int textInputSizeLg = 38;
        int textInputSizeMd = 18;
        int textInputSizeSm = 5;

          DateInput fromDate = new DateInput(parameterFromDate);
            fromDate.setDay(_stamp.getDay());
            fromDate.setMonth(_stamp.getMonth());
            fromDate.setYear(_stamp.getYear());
            fromDate.setDisabled(true);

          TextInput manyDays = new TextInput(parameterManyDays);
            manyDays.setSize(5);
            manyDays.setContent("1");

        Text surnameText = (Text) theText.clone();
            surnameText.setText(iwrb.getLocalizedString("travel.surname","surname"));
        Text lastnameText = (Text) theText.clone();
            lastnameText.setText(iwrb.getLocalizedString("travel.last_name","last name"));
        Text addressText = (Text) theText.clone();
            addressText.setText(iwrb.getLocalizedString("travel.address","address"));
        Text areaCodeText = (Text) theText.clone();
            areaCodeText.setText(iwrb.getLocalizedString("travel.area_code","area code"));
        Text emailText = (Text) theText.clone();
            emailText.setText(iwrb.getLocalizedString("travel.email","e-mail"));
        Text telNumberText = (Text) theText.clone();
            telNumberText.setText(iwrb.getLocalizedString("travel.telephone_number","telephone number"));
        Text cityText = (Text) theText.clone();
            cityText.setText(iwrb.getLocalizedString("travel.city_sm","city"));
        Text countryText = (Text) theText.clone();
            countryText.setText(iwrb.getLocalizedString("travel.country_sm","country"));
        Text depPlaceText = (Text) theText.clone();
            depPlaceText.setText(iwrb.getLocalizedString("travel.departure_place","Departure place"));
        Text fromText = (Text) theText.clone();
            fromText.setText(iwrb.getLocalizedString("travel.from","From"));
        Text manyDaysText = (Text) theText.clone();
            manyDaysText.setText(iwrb.getLocalizedString("travel.number_of_days","Number of days"));
        Text commentText = (Text) theText.clone();
            commentText.setText(iwrb.getLocalizedString("travel.comment","Comment"));

        DropdownMenu depAddr = new DropdownMenu(addresses, this.parameterDepartureAddressId);
          depAddr.setToSubmit();
          depAddr.setSelectedElement(Integer.toString(addressId));

        TextInput surname = new TextInput(PARAMETER_FIRST_NAME);
            surname.setSize(textInputSizeLg);
            surname.keepStatusOnAction();
        TextInput lastname = new TextInput(PARAMETER_LAST_NAME);
            lastname.setSize(textInputSizeLg);
            lastname.keepStatusOnAction();
        TextInput address = new TextInput(PARAMETER_ADDRESS);
            address.setSize(textInputSizeLg);
            address.keepStatusOnAction();
        TextInput areaCode = new TextInput(PARAMETER_AREA_CODE);
            areaCode.setSize(textInputSizeSm);
            areaCode.keepStatusOnAction();
        TextInput email = new TextInput(PARAMETER_EMAIL);
            email.setSize(textInputSizeMd);
            email.keepStatusOnAction();
        TextInput telNumber = new TextInput(PARAMETER_PHONE);
            telNumber.setSize(textInputSizeMd);
            telNumber.keepStatusOnAction();
        TextInput city = new TextInput(PARAMETER_CITY);
            city.setSize(textInputSizeLg);
            city.keepStatusOnAction();
        TextInput country = new TextInput(PARAMETER_COUNTRY);
            country.setSize(textInputSizeMd);
            country.keepStatusOnAction();
        TextArea comment = new TextArea(PARAMETER_COMMENT);
	          comment.setWidth("350");
	          comment.setHeight("60");
            comment.keepStatusOnAction();

        DropdownMenu usersDrop = null;
        DropdownMenu payType = getBooker(iwc).getPaymentTypeDropdown(iwrb, "payment_type");

        ++row;
        table.mergeCells(2,row,4,row);
        table.add(surnameText,1,row);
        table.add(surname,2,row);

        ++row;
        table.mergeCells(2,row,4,row);
        table.add(lastnameText,1,row);
        table.add(lastname,2,row);

        ++row;
        table.mergeCells(2,row,4,row);
        table.add(addressText,1,row);
        table.add(address,2,row);

        ++row;
        table.mergeCells(2,row,4,row);
        table.add(cityText,1,row);
        table.add(city,2,row);

        ++row;
        table.mergeCells(2,row,4,row);
        table.add(areaCodeText,1,row);
        table.add(areaCode,2,row);

        ++row;
        table.mergeCells(2,row,4,row);
        table.add(countryText,1,row);
        table.add(country,2,row);

        ++row;
        table.mergeCells(2,row,4,row);
        table.add(emailText,1,row);
        table.add(email,2,row);

        ++row;
        table.mergeCells(2,row,4,row);
        table.add(telNumberText,1,row);
        table.add(telNumber,2,row);

        if (addresses.size() > 1) {
          ++row;
          table.mergeCells(2,row,4,row);
          table.add(depPlaceText, 1, row);
          table.add(depAddr, 2,row);
        }else {
          table.add(new HiddenInput(this.parameterDepartureAddressId, Integer.toString(addressId)));
        }

				++row;
				table.add(fromText, 1, row);
				table.add(fromDate, 2, row);
				++row;
				table.add(manyDaysText, 1, row);
				table.add(manyDays, 2, row);
	
				if (_booking != null) {
	//				fromDate.setDate(_booking.getBookingDate());
						fromDate.setDisabled(false);
						if (this._multipleBookings) {
							bookingDays = _multipleBookingNumber[1];
							manyDays.setContent(Integer.toString(bookingDays));	
						}
				}
				/*
        if (_booking == null) {
          ++row;
          table.add(fromText, 1, row);
          table.add(fromDate, 2, row);

          ++row;
          table.add(manyDaysText, 1, row);
          table.add(manyDays, 2, row);
        }else {
          table.add(new HiddenInput(parameterFromDate, new IWTimestamp(_booking.getBookingDate()).toSQLDateString()), 1, row);
          GeneralBookingHome gbHome = (GeneralBookingHome) IDOLookup.getHome(GeneralBooking.class);
          GeneralBooking tempBooking = gbHome.findByPrimaryKey(_booking.getPrimaryKey());
          List bookingsJa = gbHome.getMultibleBookings(tempBooking);
          table.add(new HiddenInput(parameterManyDays, Integer.toString(bookingsJa.size())), 1, row);
        }*/

        Text pPriceCatNameText;
        ResultOutput pPriceText;
        TextInput pPriceMany;
        PriceCategory category;
        Text txtPrice;
        Text txtPerPerson = (Text) theText.clone();
          txtPerPerson.setText(iwrb.getLocalizedString("travel.per_person","per person"));

        Text totalText = (Text) theBoldText.clone();
          totalText.setText(iwrb.getLocalizedString("travel.total","Total"));
        ResultOutput TotalPassTextInput = new ResultOutput("total_pass","0");
          TotalPassTextInput.setSize(5);
        ResultOutput TotalTextInput = new ResultOutput("total","0");
          TotalTextInput.setSize(8);

        ++row;
        table.add(Text.NON_BREAKING_SPACE, 1,row);

        BookingEntry[] entries = null;
        ProductPrice pPri = null;
        int totalCount = 0;
        int totalSum = 0;
        int currentSum = 0;
        int currentCount = 0;
        if (_booking != null) {
          entries = getBooker(iwc).getBookingEntries(_booking);
        }

        ++row;
        Table pTable = new Table();
          pTable.setBorder(1);
        int pWidthLeft = 60;
        int pWidthCenter = 60;
        int pWidthRight = 75;

        pTable = new Table(3,1);
          pTable.setWidth(1, Integer.toString(pWidthLeft));
          pTable.setWidth(2, Integer.toString(pWidthCenter));
          pTable.setWidth(3, Integer.toString(pWidthRight));
          pTable.setCellpaddingAndCellspacing(0);
        table.add(pTable, 2, row+1);

        Text count = (Text) super.theSmallBoldText.clone();
          count.setText(iwrb.getLocalizedString("travel.number_of_units","Units"));
        Text unitPrice = (Text) super.theSmallBoldText.clone();
          unitPrice.setText(iwrb.getLocalizedString("travel.unit_price","Unit price"));
        Text amount = (Text) super.theSmallBoldText.clone();
          amount.setText(iwrb.getLocalizedString("travel.total_amount","Total amount"));

        pTable.add(count, 1, 1);
        pTable.add(unitPrice, 2, 1);
        pTable.add(amount, 3, 1);

        int pricesLength = prices.length;
        int miscLength = misc.length;
        ProductPrice[] pPrices = new ProductPrice[pricesLength+miscLength];
        for (int i = 0; i < pricesLength; i++) {
          pPrices[i] = prices[i];
        }
        for (int i = 0; i < miscLength; i++) {
          pPrices[i+pricesLength] = misc[i];
        }

        for (int i = 0; i < pPrices.length; i++) {
            try {
                ++row;
                category = pPrices[i].getPriceCategory();
                int price = (int) getTravelStockroomBusiness(iwc).getPrice(pPrices[i].getID(), _service.getID(),pPrices[i].getPriceCategoryID(),pPrices[i].getCurrencyId(),IWTimestamp.getTimestampRightNow(), timeframeId, addressId);
    //              pPrices[i].getPrice();
                pPriceCatNameText = (Text) theText.clone();
                  pPriceCatNameText.setText(category.getName());

                pPriceText = new ResultOutput("thePrice"+pPrices[i].getID(),"0");
                  pPriceText.setSize(8);

                pPriceMany = new TextInput("priceCategory"+pPrices[i].getID() ,"0");
                  pPriceMany.setSize(5);

                if (i == pricesLength) {
                  Text tempTexti = (Text) theBoldText.clone();
                    tempTexti.setText(iwrb.getLocalizedString("travel.miscellaneous_services","Miscellaneous services"));
//                  table.mergeCells(1, row, 2, row);
                  table.add(tempTexti, 1, row);
                  ++row;
                }else if (i == 0) {
                  Text tempTexti = (Text) theBoldText.clone();
                    tempTexti.setText(iwrb.getLocalizedString("travel.basic_prices","Basic prices"));
                    tempTexti.setUnderline(true);
//                  table.mergeCells(1, row, 2, row);
                  table.add(tempTexti, 1, row);
                  ++row;
                }
                if (i >= pricesLength) {
                  pPriceMany.setName("miscPriceCategory"+pPrices[i].getID());
                }

                if (_booking != null) {
                  if (entries != null) {
                    for (int j = 0; j < entries.length; j++) {
                      if (entries[j].getProductPrice().getPriceCategoryID() == pPrices[i].getPriceCategoryID()) {
                        pPri = entries[j].getProductPrice();
                        currentCount = entries[j].getCount();
                        price = (int) getTravelStockroomBusiness(iwc).getPrice(pPri.getID(), _productId,pPri.getPriceCategoryID(),pPri.getCurrencyId(),IWTimestamp.getTimestampRightNow(), tFrame.getID(), addressId);
                        currentSum = (int) (currentCount * price);

                        totalCount += currentCount;
                        totalSum += currentSum;
                        pPriceMany.setContent(Integer.toString(currentCount));
                        pPriceText = new ResultOutput("thePrice"+pPrices[i].getID(),Integer.toString(currentSum));
                          pPriceText.setSize(8);
                      }
                    }
                  }
                }

                pPriceText.add(pPriceMany,ResultOutput.OPERATOR_MULTIPLY+price);
                pPriceText.add(manyDays, ResultOutput.OPERATOR_MULTIPLY, null);
                TotalPassTextInput.add(pPriceMany);
                TotalTextInput.add(pPriceMany,ResultOutput.OPERATOR_MULTIPLY+price);
                TotalTextInput.add(manyDays, ResultOutput.OPERATOR_MULTIPLY, null);



                table.add(pPriceCatNameText, 1,row);

                txtPrice = (Text) theText.clone();
                  txtPrice.setText(Integer.toString(price));
    //                  table.add(Text.NON_BREAKING_SPACE,2,row);

                pTable = new Table(4,1);
                  pTable.setWidth(1, Integer.toString(pWidthLeft));
                  pTable.setWidth(2, Integer.toString(pWidthCenter));
                  pTable.setWidth(3, Integer.toString(pWidthRight));
                  pTable.setCellpaddingAndCellspacing(0);
                  pTable.add(pPriceMany,1,1);
                  pTable.add(txtPrice,2,1);
                  pTable.add(pPriceText, 3,1);


    //                    pTable.add();
                table.add(pTable, 2, row);

            }catch (SQLException sql) {
              sql.printStackTrace(System.err);
            }catch (FinderException fe) {
              fe.printStackTrace(System.err);
            }
        }

        ++row;
        table.mergeCells(1,row,4,row);
        ++row;

        table.add(totalText,1,row);

        if (_booking != null) {
          TotalPassTextInput.setContent(Integer.toString(totalCount));
          TotalTextInput.setContent(Integer.toString(totalSum));
        }
        pTable = new Table(3,1);
          pTable.setWidth(1, Integer.toString(pWidthLeft));
          pTable.setWidth(2, Integer.toString(pWidthCenter));
          pTable.setWidth(3, Integer.toString(pWidthRight));
          pTable.setCellpaddingAndCellspacing(0);

        pTable.add(TotalPassTextInput,1,1);
        pTable.add(TotalTextInput,3,1);
        table.add(pTable, 2, row);
         table.add(new HiddenInput("available",Integer.toString(available)),2,row);

        ++row;
        table.add(Text.NON_BREAKING_SPACE,1, row);

        if (super.getUser() != null) {
          ++row;
          List users = null;
          if ( this.supplier != null) users = SupplierManager.getUsersIncludingResellers(supplier);
          if ( _reseller != null) users = ResellerManager.getUsersIncludingSubResellers(_reseller);
          if (users == null) users = com.idega.util.ListUtil.getEmptyList();
          usersDrop = this.getDropdownMenuWithUsers(users, "ic_user");
          usersDrop.setSelectedElement(Integer.toString(super.getUserId()));
          usersDrop.keepStatusOnAction();

          Text tUser = (Text) theText.clone();
            tUser.setFontColor(WHITE);
            tUser.setText(iwrb.getLocalizedString("travel.user","User"));
          table.add(tUser, 1, row);
          table.add(usersDrop, 2 ,row);
        }

        ++row;
        Text payText = (Text) theText.clone();
          payText.setText(iwrb.getLocalizedString("travel.payment_type","Payment type"));
        table.add(payText, 1, row);
        table.add(payType, 2, row);


          ++row;
          table.add(commentText, 1, row);
          table.setVerticalAlignment(1, row, Table.VERTICAL_ALIGN_TOP);
          table.add(comment, 2, row);

				row = addCreditcardInputForm(iwc, table, row);
        



        if (_booking != null) {
          form.addParameter(this.parameterBookingId,_booking.getID());
          surname.setContent(_booking.getName());
          address.setContent(_booking.getAddress());
          city.setContent(_booking.getCity());
          areaCode.setContent(_booking.getPostalCode());
          country.setContent(_booking.getCountry());
          email.setContent(_booking.getEmail());
          telNumber.setContent(_booking.getTelephoneNumber());

          if (usersDrop != null) {
            usersDrop.setSelectedElement(Integer.toString(_booking.getUserId()));
          }
          payType.setSelectedElement(Integer.toString(_booking.getPaymentTypeId()));
          if (_booking.getComment() != null) {
            comment.setContent(_booking.getComment());
          }

        }

        ++row;
        if (_booking != null) {
          table.add(new SubmitButton(iwrb.getImage("buttons/update.gif"), this.sAction, this.parameterSaveBooking),4,row);
        }else {
          table.add(new SubmitButton(iwrb.getImage("buttons/book.gif"), this.sAction, this.parameterSaveBooking),4,row);
        }
        table.add(new HiddenInput(this.BookingAction,this.BookingParameter),4,row);
    }else {
      if (supplier != null || _reseller != null)
        table.add(iwrb.getLocalizedString("travel.pricecategories_not_set_up_right","Pricecategories not set up right"));
    }

    return form;
  }

	protected int addCreditcardInputForm(IWContext iwc, Table table, int row) {
		// Virkar, vantar HTTPS
		
		if (this._useInquiryForm) {
		  table.add(new HiddenInput(this.parameterInquiry,"true"), 1, row);
		}else {
			TextInput ccNumber = new TextInput(this.parameterCCNumber);
		    ccNumber.setMaxlength(16);
		    ccNumber.setLength(20);
		  TextInput ccMonth = new TextInput(this.parameterCCMonth);
		    ccMonth.setMaxlength(2);
		    ccMonth.setLength(3);
		  TextInput ccYear = new TextInput(this.parameterCCYear);
		    ccYear.setMaxlength(2);
		    ccYear.setLength(3);
		  TextInput ccCVC = new TextInput(this.parameterCCCVC);
		  ccCVC.setMaxlength(4);
		  ccCVC.setLength(5);
		  Text ccText = (Text) theText.clone();
		    ccText.setText(iwrb.getLocalizedString("travel.credidcard_number","Creditcard number"));
		
		  Text ccMY = (Text) theText.clone();
		    ccMY.setText(iwrb.getLocalizedString("travel.month_year","month / year"));
		
		  Text ccCV = (Text) theText.clone();
		  ccCV.setText(iwrb.getLocalizedString("travel.cc.cvc","Cardholder Verification Code (CVC)"));
		  
		  Text ccExp = (Text) theText.clone();
		  ccExp.setText(iwrb.getLocalizedString("cc.what_is_cvc","What is CVC?"));

		    
		  Text ccSlash = (Text) theText.clone();
		    ccSlash.setText(" / ");
		
		  ++row;
		  table.add(ccText,1,row);
		  table.add(ccNumber,2,row);
		
		  ++row;
		  table.add(ccMY,1,row);
		  table.add(ccMonth,2,row);
		  table.add(ccSlash,2,row);
		  table.add(ccYear,2,row);
		  
		  if (useCVC) {
			  ++row;
			  table.add(ccCV, 1, row);
			  table.add(ccCVC, 2, row);
				Link cvcLink = LinkGenerator.getLinkCVCExplanationPage(iwc, ccExp);
				if (cvcLink != null) {
					table.add(cvcLink, 2, row);
				}
		  }
		} 
		return row;
	}
  
  protected String getLocaleDate(IWTimestamp stamp) {
    return  (new IWCalendar(stamp)).getLocaleDate();
  }

  public Form getPublicBookingForm(IWContext iwc, Product product) throws RemoteException, FinderException {
    List addresses;
    try {
      addresses = product.getDepartureAddresses(false);
    }catch (IDOFinderException ido) {
      ido.printStackTrace(System.err);
      addresses = new Vector();
    }
    int addressId = getAddressIDToUse(iwc, addresses);

    int bookings = getBooker(iwc).getBookingsTotalCount(_productId, this._stamp, addressId);
    int max = 0;
    int min = 0;

    try {
      ServiceDayHome sDayHome = (ServiceDayHome) IDOLookup.getHome(ServiceDay.class);
      ServiceDay sDay;// = sDayHome.create();
        sDay = sDayHome.findByServiceAndDay(this._productId, _stamp.getDayOfWeek());
      if (sDay != null) {
        max = sDay.getMax();
        min = sDay.getMin();
      }
    }catch (Exception e) {
      e.printStackTrace(System.err);
    }

    /** ef ferd er fullbokud eda ef ferd er vanbokud */
    if ((max > 0 && max <= bookings) || (min > 0 && min > bookings) ){
      _useInquiryForm = true;
    }
    try {
      return getPublicBookingFormPrivate(iwc, product);
    }catch (ServiceNotFoundException snfe) {
      throw new FinderException(snfe.getMessage());
    }catch (TimeframeNotFoundException tnfe) {
      throw new FinderException(tnfe.getMessage());
    }
  }


  private Form getPublicBookingFormPrivate(IWContext iwc, Product product) throws RemoteException, ServiceNotFoundException, TimeframeNotFoundException, FinderException {
    Form form = new Form();
      form.addParameter(this.parameterOnlineBooking, "true");
    Table table = new Table();
      table.setCellpadding(0);
      table.setCellspacing(6);
      table.setBorder(0);
      table.setWidth("100%");
      form.add(table);

      if (_stamp != null) {
        form.addParameter(CalendarParameters.PARAMETER_YEAR,_stamp.getYear());
        form.addParameter(CalendarParameters.PARAMETER_MONTH,_stamp.getMonth());
        form.addParameter(CalendarParameters.PARAMETER_DAY,_stamp.getDay());
      }

      boolean isDay = true;

      try {
        isDay = getTravelStockroomBusiness(iwc).getIfDay(iwc, this._product, _stamp);
      }catch (SQLException sql) {
        throw new FinderException(sql.getMessage());
      }

      List addresses;
      try {
        addresses = product.getDepartureAddresses(false);
      }catch (IDOFinderException ido) {
        ido.printStackTrace(System.err);
        addresses = new Vector();
      }
      int addressId = getAddressIDToUse(iwc, addresses);

      ProductPrice[] prices = {};
      ProductPrice[] misc = {};
      Timeframe tFrame = getProductBusiness(iwc).getTimeframe(_product, _stamp, addressId);
      if (tFrame != null) {
        prices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(_service.getID(), tFrame.getID(), addressId, true);
        misc = ProductPriceBMPBean.getMiscellaneousPrices(_service.getID(), tFrame.getID(), addressId, true);
      }

      Text availSeats = (Text) theText.clone();
        availSeats.setText(iwrb.getLocalizedString("travel.there_are_available_seats","There are available seats "));

      Text notAvailSeats = (Text) theText.clone();
        notAvailSeats.setText(iwrb.getLocalizedString("travel.there_are_no_available_seats","There are no available seats "));

      Text inquiryText = (Text) theBoldText.clone();
        inquiryText.setText(iwrb.getLocalizedString("travel.attention","Attention!"));
        //inquiryText.setText(iwrb.getLocalizedString("travel.please_fill_out_inquiry_form","An inquiry must be sent. Please fill out the inquiry form, or select another day."));
      Text inquiryExplain = (Text) theText.clone();
        inquiryExplain.setText(iwrb.getLocalizedString("travel.inquiry_explain","A departure on the selected day cannot be guarenteed. By filling out this form you will send us your request and we will try to meet your requirements.\nYou can also select another day from the calendar."));

      Text dateText = (Text) theBoldText.clone();
        dateText.setText(getLocaleDate(_stamp));
        dateText.addToText("."+Text.NON_BREAKING_SPACE);

      Text pleaseBook = (Text) theText.clone();
        pleaseBook.setText(iwrb.getLocalizedString("travel.please_book","Please book"));

      Text pleaseFindAnotherDay = (Text) theText.clone();
        pleaseFindAnotherDay.setText(iwrb.getLocalizedString("travel.please_find_another_day","Please find another day"));

      if (prices.length > 0) {
          int row = 1;
          int textInputSizeLg = 28;
          int textInputSizeMd = 28;//18;
          int textInputSizeSm = 28;//5;

          Table pTable;
          Table pTableToClone = new Table();
          int pWidthLeft = 60;
          int pWidthCenter = 60;
          int pWidthRight = 75;
            pTableToClone.setWidth(1, Integer.toString(pWidthLeft));
            pTableToClone.setWidth(2, Integer.toString(pWidthCenter));
            pTableToClone.setWidth(3, Integer.toString(pWidthRight));
            pTableToClone.setCellpaddingAndCellspacing(0);

          HorizontalRule hr = new HorizontalRule("100%");
            hr.setColor(WHITE);

          Text subHeader;


          table.mergeCells(1,row,6,row);


          if (isDay) {
            if (_useInquiryForm) {
              table.add(inquiryText, 1, row);
              table.add(Text.BREAK, 1, row);
              table.add(inquiryExplain, 1, row);
            }else {
              table.add(availSeats,1,row);
              table.add(dateText,1,row);
              table.add(pleaseBook,1,row);
            }
            ++row;

          String star = " * ";

          Text surnameText = (Text) theText.clone();
              surnameText.setText(star);
              surnameText.addToText(iwrb.getLocalizedString("travel.surname","surname"));
          Text lastnameText = (Text) theText.clone();
              lastnameText.setText(star);
              lastnameText.addToText(iwrb.getLocalizedString("travel.last_name","last name"));
          Text addressText = (Text) theText.clone();
              addressText.setText(star);
              addressText.addToText(iwrb.getLocalizedString("travel.address","address"));
          Text areaCodeText = (Text) theText.clone();
              areaCodeText.setText(star);
              areaCodeText.addToText(iwrb.getLocalizedString("travel.area_code","area code"));
          Text emailText = (Text) theText.clone();
              emailText.setText(star);
              emailText.addToText(iwrb.getLocalizedString("travel.email","e-mail"));
          Text telNumberText = (Text) theText.clone();
              telNumberText.setText(iwrb.getLocalizedString("travel.telephone_number","telephone number"));
          Text cityText = (Text) theText.clone();
              cityText.setText(star);
              cityText.addToText(iwrb.getLocalizedString("travel.city_sm","city"));
          Text countryText = (Text) theText.clone();
              countryText.setText(star);
              countryText.addToText(iwrb.getLocalizedString("travel.country_sm","country"));
          Text depPlaceText = (Text) theText.clone();
              depPlaceText.setText(iwrb.getLocalizedString("travel.departure_place","Departure place"));
          Text fromText = (Text) theText.clone();
              fromText.setText(iwrb.getLocalizedString("travel.from","From"));
          Text manyDaysText = (Text) theText.clone();
              manyDaysText.setText(iwrb.getLocalizedString("travel.number_of_days","Number of days"));
          Text commentText = (Text) theText.clone();
              commentText.setText(iwrb.getLocalizedString("travel.comment","Comment"));

          DropdownMenu depAddr = new DropdownMenu(addresses, this.parameterDepartureAddressId);
            depAddr.setToSubmit();
            depAddr.setSelectedElement(Integer.toString(addressId));

          TextInput surname = new TextInput("surname");
              surname.setSize(textInputSizeLg);
          TextInput lastname = new TextInput("lastname");
              lastname.setSize(textInputSizeLg);
          TextInput address = new TextInput("address");
              address.setSize(textInputSizeLg);
          TextInput areaCode = new TextInput("area_code");
              areaCode.setSize(textInputSizeSm);
          TextInput email = new TextInput("e-mail");
              email.setSize(textInputSizeMd);
          TextInput telNumber = new TextInput("telephone_number");
              telNumber.setSize(textInputSizeMd);
          TextInput city = new TextInput("city");
              city.setSize(textInputSizeLg);
          TextInput country = new TextInput("country");
              country.setSize(textInputSizeMd);
/*
          DateInput fromDate = new DateInput(parameterFromDate);
            fromDate.setDay(_stamp.getDay());
            fromDate.setMonth(_stamp.getMonth());
            fromDate.setYear(_stamp.getYear());
            fromDate.setDisabled(true);
*/
          TextInput manyDays = new TextInput(parameterManyDays);
            manyDays.setContent("1");
            manyDays.setSize(5);

          TextArea comment = new TextArea("comment");
	          comment.setWidth("350");
	          comment.setHeight("60");

          ++row;
          table.mergeCells(1,row,6,row);
          table.add(hr,1,row);
          ++row;
          subHeader = (Text) theBoldText.clone();
            subHeader.setFontColor(WHITE);
            subHeader.setText(iwrb.getLocalizedString("travel.booking_information","Booking information"));
          table.add(subHeader, 1, row);
          table.mergeCells(1, row, 6 ,row);

          if (addresses.size() > 1) {
            ++row;
            table.add(depPlaceText, 1, row);
            table.add(depAddr, 2,row);
            table.setAlignment(1,row,"right");
            table.setAlignment(1,row,"right");
            table.setAlignment(2,row,"left");
            table.setAlignment(3,row,"right");
            table.setAlignment(4,row,"left");
          }else {
            table.add(new HiddenInput(this.parameterDepartureAddressId, Integer.toString(addressId)));
          }
          ++row;
          table.add(fromText, 1, row);
          table.add(new HiddenInput(parameterFromDate, _stamp.toSQLString()));
          Text currDate = (Text) theText.clone();
          currDate.setText(_stamp.getLocaleDate(iwc.getCurrentLocale()));
          table.add(currDate,  2, row);//fromDate, 2, row);
          //table.add(fromDate, 2, row);
          table.setAlignment(1,row,"right");
          table.setAlignment(2,row,"left");
          table.mergeCells(2,row,6,row);
          ++row;
          table.add(manyDaysText, 1, row);
          table.add(manyDays, 2, row);
          table.setAlignment(1,row,"right");
          table.setAlignment(2,row,"left");
          table.mergeCells(2,row,6,row);

          Text pPriceCatNameText;
          ResultOutput pPriceText;
          TextInput pPriceMany;
          PriceCategory category;
          Text txtPrice;
          Text txtPerPerson = (Text) theBoldText.clone();
            txtPerPerson.setText(iwrb.getLocalizedString("travel.per_person","per person"));

          Text totalText = (Text) theBoldText.clone();
            totalText.setText(iwrb.getLocalizedString("travel.total","Total"));
          ResultOutput TotalPassTextInput = new ResultOutput("total_pass","0");
            TotalPassTextInput.setSize(5);
          ResultOutput TotalTextInput = new ResultOutput("total","0");
            TotalTextInput.setSize(8);

          ++row;

          Text count = (Text) super.theSmallBoldText.clone();
            count.setText(iwrb.getLocalizedString("travel.number_of_units","Units"));
          Text unitPrice = (Text) super.theSmallBoldText.clone();
            unitPrice.setText(iwrb.getLocalizedString("travel.unit_price","Unit price"));
          Text amount = (Text) super.theSmallBoldText.clone();
            amount.setText(iwrb.getLocalizedString("travel.total_amount","Total amount"));
          Text space = (Text) super.theSmallBoldText.clone();
            space.setText(Text.NON_BREAKING_SPACE);

//          Table priceTable = new Table();
//            priceTable.setBorder(0);
//            priceTable.setCellpadding(0);
//            priceTable.setCellspacing(6);
//          int pRow = 1;

          pTable = (Table) pTableToClone.clone();
          pTable.add(count, 1, 1);
          pTable.add(unitPrice, 2, 1);
          pTable.add(amount, 3, 1);

//          priceTable.add(count, 1, pRow);
//          priceTable.add(unitPrice, 2, pRow);
//          priceTable.add(amount, 3, pRow);

//          table.add(space, 1, row);
          table.add(pTable, 2, row+1);
//          table.mergeCells(2, row, 2, row + prices.length + misc.length + 1);


          BookingEntry[] entries = null;
          ProductPrice pPri = null;
          int totalCount = 0;
          int totalSum = 0;
          int currentSum = 0;
          int currentCount = 0;
          if (_booking != null) {
            entries = getBooker(iwc).getBookingEntries(_booking);
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

          for (int i = 0; i < pPrices.length; i++) {
              try {
                  ++row;
                  pTable = (Table) pTableToClone.clone();
//                  ++pRow;
                  category = pPrices[i].getPriceCategory();
                  int price = (int) getTravelStockroomBusiness(iwc).getPrice(pPrices[i].getID() ,_product.getID(),pPrices[i].getPriceCategoryID(),pPrices[i].getCurrencyId(),IWTimestamp.getTimestampRightNow(), tFrame.getID(), addressId);
    //              pPrices[i].getPrice();
                  pPriceCatNameText = (Text) theText.clone();
                    pPriceCatNameText.setText(category.getName());

                  pPriceText = new ResultOutput("thePrice"+pPrices[i].getID(),"0");
                    pPriceText.setSize(8);

                  pPriceMany = new TextInput("priceCategory"+pPrices[i].getID() ,"0");
                    pPriceMany.setSize(5);

                  if (i == pricesLength) {
                    Text tempTexti = (Text) theBoldText.clone();
                      tempTexti.setText(iwrb.getLocalizedString("travel.miscellaneous_services","Miscellaneous services"));
                    //table.mergeCells(1, row, 2, row);
                    table.setAlignment(1, row, "RIGHT");
                    table.add(tempTexti, 1, row);
                    ++row;
                  }else if (i == 0) {
                    Text tempTexti = (Text) theBoldText.clone();
                      tempTexti.setText(iwrb.getLocalizedString("travel.basic_prices","Basic prices"));
                      tempTexti.setUnderline(true);
                    //table.mergeCells(1, row, 2, row);
                    table.setAlignment(1, row, "RIGHT");
                    table.add(tempTexti, 1, row);
                    ++row;
                  }
                  if (i >= pricesLength) {
                    pPriceMany.setName("miscPriceCategory"+pPrices[i].getID());
                  }

                  if (_booking != null) {
                    if (entries != null) {
                      for (int j = 0; j < entries.length; j++) {
                        if (entries[j].getProductPriceId() == pPrices[i].getID()) {
                          pPri = entries[j].getProductPrice();
                          currentCount = entries[j].getCount();
                          currentSum = (int) (currentCount * getTravelStockroomBusiness(iwc).getPrice(pPri.getID(), _productId,pPri.getPriceCategoryID(),pPri.getCurrencyId(),IWTimestamp.getTimestampRightNow(), tFrame.getID(), addressId));

                          totalCount += currentCount;
                          totalSum += currentSum;
                          pPriceMany.setContent(Integer.toString(currentCount));
                          pPriceText = new ResultOutput("thePrice"+pPrices[i].getID(),Integer.toString(currentSum));
                            pPriceText.setSize(8);
                        }
                      }
                    }
                  }


                  pPriceText.add(pPriceMany,ResultOutput.OPERATOR_MULTIPLY+price);
                  pPriceText.add(manyDays, ResultOutput.OPERATOR_MULTIPLY, null);
                  TotalPassTextInput.add(pPriceMany);
                  TotalTextInput.add(pPriceMany,ResultOutput.OPERATOR_MULTIPLY+price);
                  TotalTextInput.add(manyDays, ResultOutput.OPERATOR_MULTIPLY, null);


                  table.add(pPriceCatNameText, 1,row);
                  pTable.add(pPriceMany,1,1);
                  pTable.add(pPriceText, 3,1);

                  txtPrice = (Text) theText.clone();
                    txtPrice.setText(Integer.toString(price));
                  pTable.add(txtPrice, 2,1);
//                  table.add(txtPerPerson,3,row);

                  table.add(pTable, 2, row);
                  table.setAlignment(1,row,"right");
                  table.setAlignment(2,row,"left");
                  table.setAlignment(3,row,"left");

              }catch (SQLException sql) {
                sql.printStackTrace(System.err);
              }catch (FinderException fe) {
                fe.printStackTrace(System.err);
              }
          }

          ++row;
//          ++pRow;

          table.add(totalText,1,row);
          if (_booking != null) {
            TotalPassTextInput.setContent(Integer.toString(totalCount));
            TotalTextInput.setContent(Integer.toString(totalSum));
          }
          pTable = (Table) pTableToClone.clone();
          pTable.add(TotalPassTextInput,1,1);
          pTable.add(TotalTextInput,3,1);
          pTable.setColumnAlignment(2, "right");
          table.setAlignment(1,row,"right");
          table.setAlignment(2,row,"left");
          table.add(pTable, 2, row);

          //priceTable.setBorder(1);

          ++row;
          table.mergeCells(1,row,6,row);
          table.add(hr,1,row);
          ++row;
          table.mergeCells(1,row,6,row);
          subHeader = (Text) theBoldText.clone();
            subHeader.setFontColor(WHITE);
            subHeader.setText(iwrb.getLocalizedString("travel.personal_information","Personal information"));
          table.add(subHeader,1,row);
          table.setAlignment(1,row,"left");
          ++row;

          ++row;
          table.add(surnameText,1,row);
          table.add(surname,2,row);
          table.add(lastnameText,3,row);
          table.add(lastname,4,row);
          table.mergeCells(4,row,6,row);

          table.setAlignment(1,row,"right");
          table.setAlignment(2,row,"left");
          table.setAlignment(3,row,"right");
          table.setAlignment(4,row,"left");

          ++row;
          table.add(addressText,1,row);
          table.add(address,2,row);
          table.add(areaCodeText,3,row);
          table.add(areaCode,4,row);

          table.setAlignment(1,row,"right");
          table.setAlignment(2,row,"left");
          table.setAlignment(3,row,"right");
          table.setAlignment(4,row,"left");
          table.mergeCells(4,row,6,row);

          ++row;
          table.add(cityText,1,row);
          table.add(city,2,row);
          table.add(countryText,3,row);
          table.add(country,4,row);

          table.setAlignment(1,row,"right");
          table.setAlignment(2,row,"left");
          table.setAlignment(3,row,"right");
          table.setAlignment(4,row,"left");
          table.mergeCells(4,row,6,row);

          ++row;
          table.add(emailText,1,row);
          table.add(email,2,row);
          table.add(telNumberText,3,row);
          table.add(telNumber,4,row);

          table.setAlignment(1,row,"right");
          table.setAlignment(2,row,"left");
          table.setAlignment(3,row,"right");
          table.setAlignment(4,row,"left");
          table.mergeCells(4,row,6,row);

          ++row;
          table.add(commentText,1,row);
          table.add(comment,2,row);
          table.mergeCells(2, row, 6, row);

          table.setAlignment(1,row,"right");
          table.setVerticalAlignment(1,row,"top");
          table.setAlignment(2,row,"left");


           table.add(new HiddenInput("available",Integer.toString(available)),2,row);

           row = addCreditCardFormElements(iwc, product, table, row, hr, star);


            if (super.getUser() != null) {
              ++row;
              table.mergeCells(1,row,6,row);
              table.add(hr,1,row);

              ++row;
              List users = null;
              if ( this.supplier != null) {
                users = SupplierManager.getUsersIncludingResellers(supplier);
              }else if ( _reseller != null) {
                users = ResellerManager.getUsersIncludingSubResellers(_reseller);
              }
              if (users == null) users = new Vector();
//              DropdownMenu usersDrop = new DropdownMenu(users, "ic_user");
              DropdownMenu usersDrop = this.getDropdownMenuWithUsers(users, "ic_user");
              usersDrop.setSelectedElement(Integer.toString(super.getUserId()));

              Text tUser = (Text) theBoldText.clone();
                tUser.setFontColor(WHITE);
                tUser.setText(iwrb.getLocalizedString("travel.user","User"));
              table.setAlignment(1,row, "right");
              table.add(tUser, 1, row);
              table.add(usersDrop, 2 ,row);
            }

            ++row;
            table.mergeCells(1,row,6,row);
            table.add(hr,1,row);

            ++row;
            if (_booking != null) {
              table.add(new SubmitButton(iwrb.getImage("buttons/update.gif"), this.sAction, this.parameterSaveBooking),6,row);
            }else {
              if (this._useInquiryForm) {
                table.add(new SubmitButton(iwrb.getLocalizedImageButton("travel.send_inquiry","Semd Inquiry"), this.sAction, this.parameterSaveBooking),6,row);
              }else {
                table.add(new SubmitButton(iwrb.getImage("buttons/book.gif"), this.sAction, this.parameterSaveBooking),6,row);
              }
            }
            table.add(new HiddenInput(this.BookingAction,this.BookingParameter),6,row);

            Text starTextOne = (Text) theText.clone();
              starTextOne.setFontColor(WHITE);
              starTextOne.setText(iwrb.getLocalizedString("travel.fields_marked_with_a_star","* Fields marked with a star must be filled."));

            table.mergeCells(1,row,5,row);
            table.add(starTextOne,1,row);
//            ++row;
//            table.mergeCells(1,row,5,row);
//            table.add(starTextTwo,1,row);
            table.setAlignment(6,row,"right");


          }
          else {
            table.add(notAvailSeats,1,row);
            table.add(dateText,1,row);
            table.add(pleaseFindAnotherDay,1,row);
          }
        }else {
            table.add(notAvailSeats,1,1);
            table.add(dateText,1,1);
            table.add(pleaseFindAnotherDay,1,1);
        }
        table.setAlignment(1,1,"left");
                  //table.setBorder(1);
    return form;
  }

 /**
 * @param iwc
 * @param addresses
 * @return
 */
protected int getAddressIDToUse(IWContext iwc, List addresses) {
	int addressId = -1;
      String sAddressId = iwc.getParameter(parameterDepartureAddressId);
      if (sAddressId != null) {
        addressId = Integer.parseInt(sAddressId);
      }else if (addresses.size() > 0) {
        addressId = ((TravelAddress) addresses.get(0)).getID();
      }
	return addressId;
}

public Form getFormMaintainingAllParameters(IWContext iwc) {
    return getFormMaintainingAllParameters(iwc, true);
 }
 public Form getFormMaintainingAllParameters(IWContext iwc, boolean withBookingAction) {
		return getFormMaintainingAllParameters(iwc, withBookingAction, false);
 }
 public Form getFormMaintainingAllParameters(IWContext iwc, boolean withBookingAction, boolean withSAction) {
    Form form = new Form();
      form.maintainParameter("surname");
      form.maintainParameter("lastname");
      form.maintainParameter("address");
      form.maintainParameter("area_code");
      form.maintainParameter("e-mail");
      form.maintainParameter("telephone_number");
      form.maintainParameter("city");
      form.maintainParameter("country");
      form.maintainParameter(this.parameterPickupId);
      form.maintainParameter(this.parameterPickupInf);
//      form.maintainParameter(is.idega.idegaweb.travel.data.HotelPickupPlaceBMPBean.getHotelPickupPlaceTableName());
//      form.maintainParameter("room_number");
//      form.maintainParameter("reference_number");
      form.maintainParameter(CalendarParameters.PARAMETER_YEAR);
      form.maintainParameter(CalendarParameters.PARAMETER_MONTH);
      form.maintainParameter(CalendarParameters.PARAMETER_DAY);
      form.maintainParameter(this.parameterBookingId);
      form.maintainParameter(this.parameterSupplierId);
      form.maintainParameter(this.parameterCCNumber);
      form.maintainParameter(this.parameterCCMonth);
      form.maintainParameter(this.parameterCCYear);
      form.maintainParameter(this.parameterCCCVC);
      form.maintainParameter(this.parameterDepartureAddressId);
      form.maintainParameter(this.parameterInquiry);
      form.maintainParameter(parameterFromDate);
      form.maintainParameter(this.parameterOnlineBooking);
      form.maintainParameter(PARAMETER_CODE);
			if (withSAction) {
	      form.maintainParameter(this.sAction);
			}
      if (withBookingAction) {
        form.maintainParameter(this.BookingAction);
      }

			String sOnline = iwc.getParameter(this.parameterOnlineBooking);
			boolean onlineOnly = false;
			if (sOnline != null && sOnline.equals("true")) {
				onlineOnly = true;
			}else if (sOnline != null && sOnline.equals("false")) {
				onlineOnly = false;
			}

      ProductPrice[] pPrices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(this._productId, onlineOnly);
      for (int i = 0; i < pPrices.length; i++) {
        form.maintainParameter("priceCategory"+pPrices[i].getID());
      }
      ProductPrice[] misc = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getMiscellaneousPrices(this._productId, -1, -1, onlineOnly);
      for (int i = 0; i < misc.length; i++) {
        form.maintainParameter("miscPriceCategory"+misc[i].getID());
      }
      form.maintainParameter(this.parameterFromDate);
      form.maintainParameter(this.parameterManyDays);
      form.maintainParameter("ic_user");

    return form;
  }







  /**
   * return bookingId, 0 if nothing is done,  -10 if inquiry is sent
   */

  public int handleInsert(IWContext iwc) throws Exception {
    String check = iwc.getParameter(sAction);
    String action = iwc.getParameter(this.BookingAction);
    String inquiry = iwc.getParameter(this.parameterInquiry);
      if (action != null) {
        if (action.equals(this.BookingParameter)) {
        	int fields = checkFormFields(iwc);
        	if (fields != 0) {
        		return fields;
        	}else { 
	          if (inquiry == null) {
	            return checkBooking(iwc, true);
	          }else {
	            int checkInt = checkBooking(iwc, true, true);
	            ///// INquiry STUFF JAMMS
	            if (checkInt > 0) {
	              int inqId = this.sendInquery(iwc, checkInt, true);
	              int resp = getInquirer(iwc).sendInquiryEmails(iwc, iwrb, inqId);
	              /** @todo senda email....grrrr */
	              if (resp == 0) {
	                return this.inquirySent;
	              }else {
	                throw new Exception(iwrb.getLocalizedString("travel.inquiry_failed","Inquiry failed"));
	              }
	            }else {
	              throw new Exception(iwrb.getLocalizedString("travel.inquiry_failed","Inquiry failed"));
	            }
	          }
        	}
        }else if (action.equals(this.parameterBookAnyway)) {
          return saveBooking(iwc);
        }else if (action.equals(this.parameterSendInquery)) {
          if (sendInquery(iwc) > 0) {
            return this.inquirySent;
          }else {
            return -1;
          }
        }else {
          return -1;
        }
      }else {
        return -1;
      }
//    }else {
//      return 0;
//    }
  }

	protected int checkFormFields(IWContext iwc) {
		int returner = 0;
		
		String fName = iwc.getParameter(PARAMETER_FIRST_NAME);
		//String lName = iwc.getParameter(PARAMETER_LAST_NAME);
		String phone = iwc.getParameter(PARAMETER_PHONE);

		if (fName == null || fName.equals("")) {
			errorFields.add(PARAMETER_FIRST_NAME);
			returner = errorFieldsEmpty;
		}
/*
		if (lName == null || lName.equals("")) {
			errorFields.add(PARAMETER_LAST_NAME);
			returner = errorFieldsEmpty;
		}
*/
		if (phone == null || phone.equals("")) {
			errorFields.add(PARAMETER_PHONE);
			returner = errorFieldsEmpty;
		}

		return returner;
	}


  public int checkBooking(IWContext iwc, boolean saveBookingIfValid) throws Exception {
    return checkBooking(iwc, saveBookingIfValid, false);
  }

  public int checkBooking(IWContext iwc, boolean saveBookingIfValid, boolean bookIfTooMany) throws Exception {
		return checkBooking(iwc, saveBookingIfValid, bookIfTooMany, false);
  }
  
  public int checkBooking(IWContext iwc, boolean saveBookingIfValid, boolean bookIfTooMany, boolean bookIfTooFew) throws Exception {
    boolean tooMany = false;

    int iMany = 0;

		String key = iwc.getParameter(parameterPriceCategoryKey);
		String count2Chk = iwc.getParameter(parameterCountToCheck);
		if (count2Chk != null) {
			try {
				iMany = Integer.parseInt(count2Chk);
			}catch (Exception e){}
		}
    String sAddressId = iwc.getParameter(this.parameterDepartureAddressId);
    int iAddressId = -1;
    int timeframeId = -1;
    try {
      iAddressId = Integer.parseInt(sAddressId);
    }catch (Exception e) {}

    Collection addressIds = getTravelStockroomBusiness(iwc).getTravelAddressIdsFromRefill(getProductBusiness(iwc).getProduct(_service.getID()), iAddressId);
    Timeframe tFrame = getProductBusiness(iwc).getTimeframe(_product, _stamp, iAddressId);
    if (tFrame != null) {
      timeframeId = tFrame.getID();
    }
    String sBookingId = iwc.getParameter(this.parameterBookingId);
    int iBookingId = -1;

    int previousBookings = 0;
    if (sBookingId != null){
      iBookingId = Integer.parseInt(sBookingId);
      try {
        GeneralBooking gBook = ((is.idega.idegaweb.travel.data.GeneralBookingHome)com.idega.data.IDOLookup.getHome(GeneralBooking.class)).findByPrimaryKey(new Integer(iBookingId));
        previousBookings = gBook.getTotalCount();
      }catch (FinderException sql) {
        sql.printStackTrace(System.err);
      }
    }

    String sOnline = iwc.getParameter(this.parameterOnlineBooking);
    boolean onlineOnly = false;
    if (sOnline != null && sOnline.equals("true")) {
      onlineOnly = true;
    }else if (sOnline != null && sOnline.equals("false")) {
      onlineOnly = false;
    }

		if (iMany == 0) {
			ProductPrice[] pPrices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(_service.getID(), -1, -1, onlineOnly, key);
	//    ProductPrice[] pPrices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(_service.getID(), timeframeId, iAddressId, onlineOnly);
	    int current = 0;
	    for (int i = 0; i < pPrices.length; i++) {
	      try {
	        current = Integer.parseInt(iwc.getParameter("priceCategory"+pPrices[i].getID()));
	      }catch (NumberFormatException n) {
	        current = 0;
	      }
	      iMany += current;
	    }
		}

		if (!bookIfTooFew && iMany < 1) {
			return errorTooFew;
		}

    int serviceId = _service.getID();
    String fromDate = iwc.getParameter(this.parameterFromDate);
    String manyDays = iwc.getParameter(this.parameterManyDays);
    IWTimestamp fromStamp = null;
    IWTimestamp toStamp = null;
    //int betw = 1;
    int totalSeats = 0;
/*
    try {
      fromStamp = new IWTimestamp(fromDate);
      int iManyDays = Integer.parseInt(manyDays);
      if (iManyDays < 1) betw = 1;
      else betw = iManyDays;
    }catch (Exception e) {
      debug(e.getMessage());
    }
*/
		int bookingTotal = 0;
		int iManyDays = 1;
		try {
			iManyDays = Integer.parseInt(manyDays);
			if (iManyDays < 1) {
				iManyDays = 1;	
			}
		}catch (NumberFormatException n) {}
		    
		try {
			fromStamp = new IWTimestamp(fromDate);
		//	heildarbokanir = getHotelBooker(iwc).getNumberOfReservedRooms(product.getID(), stamp, null);
		//	heildarbokanir = getHotelBooker(iwc).getNumberOfReservedRooms(serviceId, fromStamp, null);
		//	heildarbokanir = getHotelBooker(iwc).getBookingsTotalCount(serviceId, fromStamp);
			if (_booking != null) {
				bookingTotal = _booking.getTotalCount();
		//		heildarbokanir -= bookingTotal;	
			}
		
		//	if (iManyDays < 1) betw = 1;
		//	else betw = iManyDays;
		}catch (Exception e) {
			e.printStackTrace(System.err);
		}

     if (_reseller != null) {
			Contract cont = super.getContractBusiness(iwc).getContract(_reseller, _product);
			if (cont != null) {
				totalSeats = cont.getAlotment();
			}	
    } else {
			ServiceDayHome sDayHome = (ServiceDayHome) IDOLookup.getHome(ServiceDay.class);
			ServiceDay sDay;// = sDayHome.create();
			
			sDay = sDayHome.findByServiceAndDay(serviceId, fromStamp.getDayOfWeek());
			if (sDay != null) {
			  totalSeats = sDay.getMax();
			}
		}



    iMany -= previousBookings;


    int iAvailable;
    if (totalSeats > 0) {
      if (iManyDays == 1) {
        iAvailable = totalSeats + bookingTotal - getBooker(iwc).getGeneralBookingHome().getBookingsTotalCount(( (Integer) _service.getPrimaryKey()).intValue(), this._stamp, null, -1, new int[]{}, addressIds );
        if (iMany > iAvailable) {
          tooMany = true;
          errorDays.add(fromStamp);
        }
      }else {
        for (int r = 0; r < iManyDays ; r++) {
          if (r != 0)
          fromStamp.addDays(1);
          iAvailable = totalSeats + bookingTotal - getBooker(iwc).getGeneralBookingHome().getBookingsTotalCount(( (Integer) _service.getPrimaryKey()).intValue(), fromStamp, null, -1, new int[]{}, addressIds );
          if (iMany > iAvailable) {
              tooMany = true;
              errorDays.add(fromStamp);
          }
        }
      }
    }

    if (tooMany && !bookIfTooMany) {
      return this.errorTooMany;
    }else {
      if (saveBookingIfValid) {
        return saveBooking(iwc);
      }else {
        return 0;
      }
    }
  }

	public abstract void saveServiceBooking(IWContext iwc, int bookingId, IWTimestamp stamp) throws RemoteException, IDOException;
	
	/** This is here so that Tour can override it... */
	public IWTimestamp getNextAvailableDay(IWContext iwc, IWTimestamp stamp) throws RemoteException, SQLException{
		stamp.addDays(1);
		return stamp;
	}

  public int saveBooking(IWContext iwc) throws CreateException, RemoveException, FinderException, SQLException, CreditCardAuthorizationException, RemoteException, IDOException{

	
	     String surname = iwc.getParameter(PARAMETER_FIRST_NAME);
      String lastname = iwc.getParameter(PARAMETER_LAST_NAME);
      String address = iwc.getParameter(PARAMETER_ADDRESS);
      String areaCode = iwc.getParameter(PARAMETER_AREA_CODE);
      String email = iwc.getParameter(PARAMETER_EMAIL);
      String phone = iwc.getParameter(PARAMETER_PHONE);

      String city = iwc.getParameter(PARAMETER_CITY);
      String country = iwc.getParameter(PARAMETER_COUNTRY);
      String pickupId = iwc.getParameter(parameterPickupId);
      String pickupInfo = iwc.getParameter(parameterPickupInf);
      String sPaymentType = iwc.getParameter("payment_type");
      String comment = iwc.getParameter(PARAMETER_COMMENT);
      String code = iwc.getParameter(PARAMETER_CODE);
      String key = iwc.getParameter(parameterPriceCategoryKey);

			if (phone == null) {
				phone = "";
			}

      String sAddressId = iwc.getParameter(this.parameterDepartureAddressId);
      int iAddressId = -1;
      try {
      	if (sAddressId != null) {
	      	iAddressId = Integer.parseInt(sAddressId);
      	}
      }catch(NumberFormatException n) {}

      String sUserId = iwc.getParameter("ic_user");
      if (sUserId == null) sUserId = "-1";


      String ccNumber = iwc.getParameter(this.parameterCCNumber);
      String ccMonth = iwc.getParameter(this.parameterCCMonth);
      String ccYear = iwc.getParameter(this.parameterCCYear);
      String ccCVC = iwc.getParameter(this.parameterCCCVC);

      String year = iwc.getParameter(CalendarParameters.PARAMETER_YEAR);
      String month = iwc.getParameter(CalendarParameters.PARAMETER_MONTH);
      String day = iwc.getParameter(CalendarParameters.PARAMETER_DAY);

      String supplierId = iwc.getParameter(this.parameterSupplierId);

      //TEMP BEGINS
        String fromDate = iwc.getParameter(this.parameterFromDate);
        String manyDays = iwc.getParameter(this.parameterManyDays);
      //TEMP ENDS

/*
      try {
        _stamp = new IWTimestamp(Integer.parseInt(day), Integer.parseInt(month), Integer.parseInt(year));
      }catch (NumberFormatException n) {
        n.printStackTrace(System.err);
      }*/
      IWTimestamp _fromDate = new IWTimestamp(fromDate);

      String sBookingId = iwc.getParameter(this.parameterBookingId);

      int iBookingId = -1;
      if (sBookingId != null) iBookingId = Integer.parseInt(sBookingId);

      int returner = 0;

      String many;
      int iMany = 0;
      int iPickupId;


      String sOnline = iwc.getParameter(this.parameterOnlineBooking);
      boolean onlineOnly = false;
      if (sOnline != null && sOnline.equals("true")) {
        onlineOnly = true;
      }else if (sOnline != null && sOnline.equals("false")) {
        onlineOnly = false;
      }

      ProductPrice[] prices = {};
      ProductPrice[] misc = {};
      Timeframe[] timeframes = _product.getTimeframes();
			prices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(_service.getID(), -1,iAddressId, onlineOnly, key);
			misc = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getMiscellaneousPrices(_service.getID(), -1, iAddressId, onlineOnly);

			ProductPrice[] pPrices = new ProductPrice[prices.length + misc.length];
			for (int i = 0; i < prices.length; i++) {
				pPrices[i] = prices[i];
			}
			for (int i = 0; i < misc.length; i++) {
				pPrices[i+prices.length] = misc[i];
			}

      int lbookingId = -1;

      boolean displayFormInternal = false;
      PriceCategory pCat;

      try {
        int[] manys = new int[pPrices.length];
        int[] manyMiscs = new int[misc.length];
        for (int i = 0; i < manys.length; i++) {
          many = iwc.getParameter("priceCategory"+pPrices[i].getID());
					//System.out.println("[Bookingform] pCat(p) = "+pPrices[i].getPriceCategory().getName() +" : "+many);
          if ( (many != null) && (!many.equals("")) && (!many.equals("0"))) {
            manys[i] = Integer.parseInt(many);
            iMany += Integer.parseInt(many);
          }else {
            manys[i] = 0;
          }
        }
        
        for (int i = 0; i < manyMiscs.length; i++) {
          many = iwc.getParameter("miscPriceCategory"+misc[i].getID());
					//System.out.println("[Bookingform] pCat(m) = "+misc[i].getPriceCategory().getName() +" : "+many);
          if ( (many != null) && (!many.equals("")) && (!many.equals("0"))) {
            manyMiscs[i] = Integer.parseInt(many);
          }else {
            manyMiscs[i] = 0;
          }
        }


        try {
          iPickupId = Integer.parseInt(pickupId);
        }catch (NumberFormatException n) {
          iPickupId = -1;
        }

        int paymentType = Booking.PAYMENT_TYPE_ID_NO_PAYMENT;
        try {
          paymentType = Integer.parseInt(sPaymentType);
        }catch (NumberFormatException nfe) {}
        int bookingType = Booking.BOOKING_TYPE_ID_ONLINE_BOOKING;

        if (supplier != null) {
          bookingType = Booking.BOOKING_TYPE_ID_SUPPLIER_BOOKING;
        }else if (_reseller != null) {
          displayFormInternal= true;
          bookingType = Booking.BOOKING_TYPE_ID_THIRD_PARTY_BOOKING;
        }else {
          bookingType = Booking.BOOKING_TYPE_ID_ONLINE_BOOKING;
        }

        int betw = 1;
        try {
          betw = Integer.parseInt(manyDays);
        }catch (NumberFormatException e) {
          //e.printStackTrace(System.err);
        }

        int[] bookingIds = new int[betw];


				if (iBookingId == -1) {
					for (int i = 0; i < betw; i++) {
						if (i != 0) {
							_fromDate = getNextAvailableDay(iwc, _fromDate);
							//_fromDate.addDays(1);
						}
						
						bookingIds[i] = getBooker(iwc).Book(_service.getID(),country, surname+" "+lastname, address, city, phone, email, _fromDate, iMany, bookingType, areaCode, paymentType, Integer.parseInt(sUserId), super.getUserId(), iAddressId, comment, code);
						if (iPickupId > 0) {
								getBooker(iwc).setPickup(bookingIds[i], iPickupId, pickupInfo);
						}
						saveServiceBooking(iwc, bookingIds[i], _fromDate);
					}
					lbookingId = bookingIds[0];
					
				} else {				
            /** Edit booking */
            List tempBookings = getBooker(iwc).getMultibleBookings(((is.idega.idegaweb.travel.data.GeneralBookingHome)com.idega.data.IDOLookup.getHome(GeneralBooking.class)).findByPrimaryKey(new Integer(iBookingId)));
            int tempBookingsSize = 0;
            if (tempBookings != null) {
            	tempBookingsSize = tempBookings.size();
            }
            
            if (tempBookingsSize < 2 && betw < 2) {
            	/** Single booking */
              bookingIds[0] = getBooker(iwc).updateBooking(iBookingId, _service.getID(), country, surname+" "+lastname, address, city, phone, email, _fromDate, iMany, areaCode, paymentType, Integer.parseInt(sUserId), super.getUserId(), iAddressId, comment, code);
            	if (iPickupId > 0) {
                getBooker(iwc).setPickup(bookingIds[0], iPickupId, pickupInfo);
            	}
							saveServiceBooking(iwc, bookingIds[0], _fromDate);
            	lbookingId = bookingIds[0];
            }else {
							/** Multiple bookings */
              GeneralBooking gBooking;
              
              /** Reduce number of days */
              if (tempBookingsSize > betw) {
								bookingIds = new int[betw];
//              	bookingIds = new int[tempBookingsSize];
              	/** Updating the days that will not be deleted */
              	for ( int j = 0; j < betw; j++) {
									if (j != 0) {
										_fromDate.addDays(1);
									}
									gBooking = (GeneralBooking) tempBookings.get(j);
									bookingIds[j] = getBooker(iwc).updateBooking(gBooking.getID(), _service.getID(), country, surname+" "+lastname, address, city, phone, email, _fromDate, iMany, areaCode, paymentType, Integer.parseInt(sUserId), super.getUserId(), iAddressId, comment, code);
									if (iPickupId > 0) {
											getBooker(iwc).setPickup(bookingIds[j], iPickupId, pickupInfo);
									}
									saveServiceBooking(iwc, bookingIds[j], _fromDate);
              	}
              	/** Deleting the rest of the days */
              	for (int j = betw; j < tempBookingsSize; j++) {
              		gBooking = (GeneralBooking) tempBookings.get(j);
              		getBooker(iwc).deleteBooking(gBooking);	
              		
              	}
              }

							/** Same number of days */
							if (tempBookingsSize == betw) {
								bookingIds = new int[tempBookingsSize];
								/** Updating the days that will not be deleted */
								for ( int j = 0; j < betw; j++) {
									if (j != 0) {
										_fromDate.addDays(1);
									}
									gBooking = (GeneralBooking) tempBookings.get(j);
									bookingIds[j] = getBooker(iwc).updateBooking(gBooking.getID(), _service.getID(), country, surname+" "+lastname, address, city, phone, email, _fromDate, iMany, areaCode, paymentType, Integer.parseInt(sUserId), super.getUserId(), iAddressId, comment, code);
									if (iPickupId > 0) {
											getBooker(iwc).setPickup(bookingIds[j], iPickupId, pickupInfo);
									}
									saveServiceBooking(iwc, bookingIds[j], _fromDate);
								}
							}

							/** Increase number of days */
							if (tempBookingsSize < betw) {
								bookingIds = new int[betw];
								/** Updates bookings that existed */
              	for (int j = 0; j < tempBookingsSize; j++) {
									if (j != 0) {
										_fromDate.addDays(1);
									}
									gBooking = (GeneralBooking) tempBookings.get(j);
									bookingIds[j] = getBooker(iwc).updateBooking(gBooking.getID(), _service.getID(), country, surname+" "+lastname, address, city, phone, email, _fromDate, iMany, areaCode, paymentType, Integer.parseInt(sUserId), super.getUserId(), iAddressId, comment, code);
									if (iPickupId > 0) {
											getBooker(iwc).setPickup(bookingIds[j], iPickupId, pickupInfo);
									}
									saveServiceBooking(iwc, bookingIds[j], _fromDate);
              	}
              	/** Creates new bookins */
              	for (int j = tempBookingsSize; j < betw; j++) {
									if (j != 0) {
										_fromDate.addDays(1);
									}
									bookingIds[j] = getBooker(iwc).Book(_service.getID(),country, surname+" "+lastname, address, city, phone, email, _fromDate, iMany, bookingType, areaCode, paymentType, Integer.parseInt(sUserId), super.getUserId(), iAddressId, comment, code);
									if (iPickupId > 0) {
											getBooker(iwc).setPickup(bookingIds[j], iPickupId, pickupInfo);
									}
									saveServiceBooking(iwc, bookingIds[j], _fromDate);
              	}
              	 
              	
							}
              lbookingId = iBookingId;
            }
        } // ends else (bookingId == -1)


        /**
         * removing booking from resellers...
         */
        for (int o = 0; o < bookingIds.length; o++) {
          try {
            GeneralBooking gBook = ((is.idega.idegaweb.travel.data.GeneralBookingHome)com.idega.data.IDOLookup.getHome(GeneralBooking.class)).findByPrimaryKey(new Integer(bookingIds[o]));
            gBook.removeFromAllResellers();
            //gBook.removeFrom(Reseller.class);
          }catch (FinderException sql) {debug(sql.getMessage());}
          catch (IDORemoveRelationshipException sql) {debug(sql.getMessage());}
        }

        /**
         * adding booking to reseller if resellerUser is chosen from dropdown...
         */
        int resId = -7;
        try {
          if (!sUserId.equals("-1")) {
            User user = ((com.idega.core.user.data.UserHome)com.idega.data.IDOLookup.getHomeLegacy(User.class)).findByPrimaryKeyLegacy(Integer.parseInt(sUserId));
            Reseller res = null;
            if (user != null) {
              res = ResellerManager.getReseller(user);
            }
            if (res != null) {
              resId = res.getID();
              for (int i = 0; i < bookingIds.length; i++) {
                try {
                  res.addTo(GeneralBooking.class, bookingIds[i]);
                }catch (SQLException sql) {debug(sql.getMessage());}
              }
            }
          }
        }catch (SQLException sql) {
          sql.printStackTrace(System.err);
        }

        if (_reseller != null) {
          if (_resellerId != resId) {
            for (int i = 0; i < bookingIds.length; i++) {
              try {
                _reseller.addTo(GeneralBooking.class, bookingIds[i]);
              }catch (SQLException sql) {debug(sql.getMessage());}
            }
          }
        }

        returner = lbookingId;

        BookingEntryHome beHome = (BookingEntryHome) IDOLookup.getHome(BookingEntry.class);
        ProductPriceHome ppHome = (ProductPriceHome) IDOLookup.getHome(ProductPrice.class);
        Booking booking;
        ProductPrice prodPrick;
        int ppID;
        for (int k = 0; k < bookingIds.length; k++) {
          if (bookingIds[k] != -1) {
          	booking = getBooking(bookingIds[k]);
            if (iBookingId == -1) {
              BookingEntry bEntry;
              for (int i = 0; i < pPrices.length; i++) {
                if (manys[i] != 0) {
                  bEntry = beHome.create();
                  try {
                  	ppID = getProductPriceID(iwc, pPrices[i], new IWTimestamp(booking.getBookingDate()), timeframes, iAddressId, onlineOnly, key);
                  	prodPrick = ppHome.findByPrimaryKey(ppID);
                  	//System.out.println("ProductPrice = "+ prodPrick.getPrice());
                  } catch (ProductPriceException p) {
                  	System.out.println("... ppID error");
                  	ppID = pPrices[i].getID();
                  }
                  //System.out.println("... ppID = "+ppID);
                  bEntry.setProductPriceId(ppID);
                  bEntry.setBookingId(bookingIds[k]);
                  bEntry.setCount(manys[i]);
                  bEntry.store();
                }
              }
              for (int i = 0; i < misc.length; i++) {
                if (manyMiscs[i] != 0) {
                  bEntry = ((is.idega.idegaweb.travel.data.BookingEntryHome)com.idega.data.IDOLookup.getHome(BookingEntry.class)).create();
                  try {
                  	ppID = getProductPriceID(iwc, misc[i], new IWTimestamp(booking.getBookingDate()), timeframes, iAddressId, onlineOnly, key);
                  } catch (ProductPriceException p) {
                  	System.out.println("MppID error");
                  	ppID = misc[i].getID();
                  }
                  //System.out.println("MppID = "+ppID);
                  bEntry.setProductPriceId(misc[i].getID());
                  bEntry.setBookingId(bookingIds[k]);
                  bEntry.setCount(manyMiscs[i]);
                  bEntry.store();
                }
              }
            }else {
              BookingEntry bEntry;
              ProductPrice price;
              boolean done = false;
              BookingEntry[] entries = getBooker(iwc).getBookingEntries(((is.idega.idegaweb.travel.data.GeneralBookingHome)com.idega.data.IDOLookup.getHome(GeneralBooking.class)).findByPrimaryKey(new Integer(bookingIds[k])));
              for (int i = 0; i < entries.length; i++) {
                entries[i].remove();
              }
              //if (entries == null)
              entries = new BookingEntry[]{};
              for (int i = 0; i < pPrices.length; i++) {
                done = false;
                for (int j = 0; j < entries.length; j++) {
                  if (pPrices[i].getID() == entries[j].getProductPriceId()) {
                    done = true;
                    entries[j].setCount(manys[i]);
                    entries[j].store();
                    break;
                  }
                }
                if (!done && manys[i] != 0) {
                  bEntry = ((is.idega.idegaweb.travel.data.BookingEntryHome)com.idega.data.IDOLookup.getHome(BookingEntry.class)).create();
                  bEntry.setProductPriceId(pPrices[i].getID());
                  bEntry.setBookingId(bookingIds[k]);
                  bEntry.setCount(manys[i]);
                  bEntry.store();
                }
              }
              for (int i = 0; i < misc.length; i++) {
                done = false;
                for (int j = 0; j < entries.length; j++) {
                  if (misc[i].getID() == entries[j].getProductPriceId()) {
                    done = true;
                    entries[j].setCount(manyMiscs[i]);
                    entries[j].store();
                    break;
                  }
                }
                if (!done && manyMiscs[i] != 0) {
                  bEntry = ((is.idega.idegaweb.travel.data.BookingEntryHome)com.idega.data.IDOLookup.getHome(BookingEntry.class)).create();
                  bEntry.setProductPriceId(misc[i].getID());
                  bEntry.setBookingId(bookingIds[k]);
                  bEntry.setCount(manyMiscs[i]);
                  bEntry.store();
                }
              }
             
            }
          }
        }

				handleCreditcardForBooking(iwc, returner, ccNumber, ccMonth, ccYear, ccCVC);

      }catch (NumberFormatException n) {
        n.printStackTrace(System.err);
      }

      return returner;
  }

	protected void handleCreditcardForBooking(IWContext iwc, int bookingId,	String ccNumber, String ccMonth,	String ccYear, String ccCVC) throws FinderException, RemoteException, CreditCardAuthorizationException {
		if (bookingId > 0 && ccNumber != null && ccMonth != null && ccYear != null && !ccNumber.equals("")) {
			String heimild;
			CreditCardMerchant merchant = null;
			try {

				GeneralBookingHome gbHome = (GeneralBookingHome) IDOLookup.getHome(GeneralBooking.class);
				GeneralBooking gBooking = gbHome.findByPrimaryKey(new Integer(bookingId));
				List bookings = getBooker(iwc).getMultibleBookings(gBooking);
				float price = getBooker(iwc).getBookingPrice(bookings); 
				
				/** Setting all bookings to Invalid */
				gBooking.setIsValid(false);
				gBooking.setPaymentTypeId(Booking.PAYMENT_TYPE_ID_NO_PAYMENT);
				gBooking.store();
				GeneralBooking tBook;
				Iterator iter1 = bookings.iterator();
				while (iter1.hasNext()) {
					tBook = (GeneralBooking) iter1.next();
					tBook.setIsValid(false);
					tBook.setPaymentTypeId(Booking.PAYMENT_TYPE_ID_NO_PAYMENT);
					tBook.store();
				}
			
				System.out.println("Starting Creditcard Payment : "+IWTimestamp.RightNow().toString());
				//float price = this.getOrderPrice(iwc, _product, _stamp, true);
				String currency = getCurrencyForBooking(gBooking);
				System.out.println("  Price = "+price+" "+currency);
				//System.out.println(" Booking prices = "+getBooker(iwc).getBookingPrice(getBooker(iwc).getMultibleBookings(gBooking)));
				if (currency == null) {
					currency = "ISK";	
				}
				CreditCardClient t = getCreditCardClient(iwc, gBooking);
				merchant = t.getCreditCardMerchant();
				//heimild = t.doSale(ccNumber,ccMonth,ccYear,price,currency);
				heimild = t.doSale(gBooking.getName(), ccNumber,ccMonth,ccYear, ccCVC, price,currency, gBooking.getReferenceNumber());
				System.out.println("Ending Creditcard Payment test : "+IWTimestamp.RightNow().toString());
				Iterator iter = bookings.iterator();
				while (iter.hasNext()) {
					tBook = (GeneralBooking) iter.next();
//					tBook = gbHome.findByPrimaryKey(iter.next());
					tBook.setIsValid(true);
					tBook.setCreditcardAuthorizationNumber(heimild);
					tBook.setPaymentTypeId(Booking.PAYMENT_TYPE_ID_CREDIT_CARD);
					tBook.store();
				}
				
				gBooking.setIsValid(true);
				gBooking.setCreditcardAuthorizationNumber(heimild);
				gBooking.setPaymentTypeId(Booking.PAYMENT_TYPE_ID_CREDIT_CARD);
				gBooking.store();
		  
			}catch(CreditCardAuthorizationException e) {
				//e.printStackTrace(System.err);
				sendErrorEmail("Online booking failed ("+e.getLocalizedMessage(iwrb)+")","Creditcard authorization failed.", merchant, e);

				throw new CreditCardAuthorizationException(e.getLocalizedMessage(iwrb));
			}catch (Exception e) {
					e.printStackTrace(System.err);
//				throw new TPosException(iwrb.getLocalizedString("travel.cannot_connect_to_cps","Could not connect to Central Payment Server"));
				sendErrorEmail("Online booking failed (unknown error)","An online booking failed.", merchant, e);
				throw new CreditCardAuthorizationException(iwrb.getLocalizedString("travel.unknown_error","Unknown error"));
			}
		}
	}
	
	protected int getProductPriceID(IWContext iwc, ProductPrice pPrice, IWTimestamp stamp, Timeframe[] timeframes, int iAddressId, boolean onlineOnly, String key) throws RemoteException, ProductPriceException {
		if (timeframes == null || timeframes.length < 2) {
			return pPrice.getID();
		} else {
			Timeframe frame = getProductBusiness(iwc).getTimeframe(_product, timeframes, stamp, -1);
			if (frame != null) {
				int categoryID = pPrice.getPriceCategoryID();
				ProductPrice[] prices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(_service.getID(), frame.getID(),iAddressId, onlineOnly, key);
				if (prices!= null || prices.length > 0) {
					for (int i = 0; i < prices.length; i++) {
						if (prices[i].getPriceCategoryID() == categoryID) {
							return prices[i].getID();
						}
					}
				} 
				ProductPrice[] misc = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getMiscellaneousPrices(_service.getID(), frame.getID(), iAddressId, onlineOnly);
				if (misc != null || misc.length > 0) {
					for (int i = 0; i < misc.length; i++) {
						if (misc[i].getPriceCategoryID() == categoryID) {
							return misc[i].getID();
						}
					}
				}
				throw new ProductPriceException(iwrb.getLocalizedString("travel.price_category_not_found", "Price category not found"));
			}
		}

		return -1;
	}
	
	protected void sendErrorEmail(String subject, String bodyHeader, CreditCardMerchant merchant, Exception e) throws CreditCardAuthorizationException {
		String error_notify_email = this.bundle.getProperty(PARAMETER_EMAIL_FOR_ERROR_NOTIFICATION);
		if (error_notify_email != null) {
			try {
				String cc_error_notify_email = this.bundle.getProperty(PARAMETER_CC_EMAIL_FOR_ERROR_NOTIFICATION);
				if (cc_error_notify_email == null) {
					cc_error_notify_email = "";	
				}
				StackTraceElement[] ste = e.getStackTrace();
				SendMail mail = new SendMail();
				StringBuffer msg = new StringBuffer();
				if (e instanceof CreditCardAuthorizationException) {
					msg.append("Error message = "+((CreditCardAuthorizationException)e).getErrorMessage()+"\n");
					msg.append("Error number = "+((CreditCardAuthorizationException)e).getErrorNumber()+"\n");
					msg.append("Display error = "+((CreditCardAuthorizationException)e).getDisplayError()+"\n\n");
					msg.append("Localized message = "+((CreditCardAuthorizationException)e).getLocalizedMessage(iwrb)+"\n\n");
				}
				msg.append(bodyHeader+"\n\n ");
				if (merchant != null) {
					msg.append("Merchant = "+merchant.getMerchantID()+"\n\n ");
				} else {
					msg.append("Merchant = NULL\n\n ");
				}
				for ( int i = 0 ; i < ste.length ; i++) {
					if (i != 0) {
						msg.append("      ");
					}
					msg.append(ste[i].toString())
					.append("\n");	
				}

				mail.send("gimmi@idega.is", error_notify_email, cc_error_notify_email, "", "mail.idega.is", subject, msg.toString());
			} catch (MessagingException e1) {
				e1.printStackTrace(System.err);
				throw new CreditCardAuthorizationException(iwrb.getLocalizedString("travel.unknown_error","Unknown error"));
			}
		}
	}

  public int sendInquery(IWContext iwc) throws Exception {
    return sendInquery(iwc, -1, false);
  }

  public int sendInquery(IWContext iwc, int bookingId, boolean returnInquiryId) throws Exception {
    String surname = iwc.getParameter("surname");
    String lastname = iwc.getParameter("lastname");
    String address = iwc.getParameter("address");
    String areaCode = iwc.getParameter("area_code");
    String email = iwc.getParameter("e-mail");
    String phone = iwc.getParameter("telephone_number");
    String comment = iwc.getParameter(this.PARAMETER_COMMENT);

    String city = iwc.getParameter("city");
    String country = iwc.getParameter("country");
    String hotelPickupPlaceId = iwc.getParameter(parameterPickupId);

//    String referenceNumber = iwc.getParameter("reference_number");
    String fromDate = iwc.getParameter(parameterFromDate);
    String manyDays = iwc.getParameter(parameterManyDays);

    try {
      int iManyDays = 1;
      if ( Integer.parseInt(manyDays) > 1) {
        iManyDays = Integer.parseInt(manyDays);
      }

      IWTimestamp fromStamp = new IWTimestamp(fromDate);
      IWTimestamp toStamp = new IWTimestamp(fromStamp);
        toStamp.addDays(iManyDays);

      if (bookingId == -1) {
        bookingId = saveBooking(iwc);
      }

      GeneralBooking gBooking = ((is.idega.idegaweb.travel.data.GeneralBookingHome)com.idega.data.IDOLookup.getHome(GeneralBooking.class)).findByPrimaryKey(new Integer(bookingId));
      List bookings = getBooker(iwc).getMultibleBookings(gBooking);
      Booking booking = null;

      int numberOfSeats = gBooking.getTotalCount();
      int counter = 0;
      int inquiryId = 0;

      while (toStamp.isLaterThan(fromStamp)) {
        booking = (Booking) bookings.get(counter);
        booking.setIsValid(false);
        booking.store();

        inquiryId = getInquirer(iwc).sendInquery(surname+" "+lastname, email, fromStamp, _product.getID() , numberOfSeats, comment, booking.getID(), _reseller);

        fromStamp.addDays(1);
        ++counter;
      }

      if (returnInquiryId) {
        return inquiryId;
      }else {
        return bookingId;
      }
    }catch (SQLException sql) {
      sql.printStackTrace();
      return -1;
    }
  }

  protected void setProduct(IWContext iwc, Product product) {
    _product = product;
    try {
      _productId = product.getID();
      _service = getTravelStockroomBusiness(iwc).getService(product);
    }catch (NullPointerException np) {
      System.err.println("BookingForm : Product == null, probably expired session");
    }catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  protected void setTimestamp(IWContext iwc) {
  	if (_booking == null) {
	    String year = iwc.getParameter(CalendarParameters.PARAMETER_YEAR);
	    String month = iwc.getParameter(CalendarParameters.PARAMETER_MONTH);
	    String day = iwc.getParameter(CalendarParameters.PARAMETER_DAY);
	    
		  _stamp = new IWTimestamp(IWTimestamp.RightNow());
		  if (year != null) {
		  	_stamp.setYear(Integer.parseInt(year));	
		  }
	    if (month != null) {
	    	_stamp.setMonth(Integer.parseInt(month));	
	    }
	    if (day != null) {
	    	_stamp.setDay(Integer.parseInt(day));	
	    }
  	}else {
  		try {
	  		setTimestamp(new IWTimestamp(_booking.getBookingDate()));	
  		}catch (RemoteException r) {
  			/** Not handled */	
  		}
  	}
    
  }

  protected DropdownMenu getDropdownMenuWithUsers(List users, String name) throws RemoteException{
    DropdownMenu usersDrop = new DropdownMenu("ic_user");
    User usr = null;

    if (!users.contains(super.getUser())) {
      users.add(0, super.getUser());
    }

    for (int i = 0; i < users.size(); i++) {
      if (users.get(i) ==null) {
        if (i != (users.size() -1)) {
          usr = (User) users.get(i+1);
          try {
            if (ResellerManager.getReseller(usr) != null) {
              usersDrop.addMenuElement(-1, ResellerManager.getReseller(usr).getName());
            }
          }catch (SQLException sql) {
            sql.printStackTrace(System.err);
          }
        }
      }else {
        usr =  (User) users.get(i);
        usersDrop.addMenuElement(usr.getID(), usr.getName());
      }
    }

    return usersDrop;
  }

  private Form getFieldErrorForm(IWContext iwc) {
		Form form = getFormMaintainingAllParameters(iwc, false, true);
		Table table = new Table();
		table.setColor(super.WHITE);
		form.add(table);
		int row = 1;
		
		table.add(super.getHeaderText(iwrb.getLocalizedString("travel.form_not_completed","Bookingform not completed")), 1, row);
		table.setRowColor(row, super.backgroundColor);

		String temp;
		for (int i = 0; i < errorFields.size(); i++) {
		  try {
				++row;
				temp = (String) errorFields.get(i);
				table.setRowColor(row, super.GRAY);
				table.add(iwrb.getLocalizedString("travel.form_parameter_name_"+temp, temp), 1,row);
		  }catch (NullPointerException npe) {
				npe.printStackTrace(System.err);
		  }
		}

	  String sBookingId = iwc.getParameter(this.parameterBookingId);

		++row;

		table.setRowColor(row, super.GRAY);
		table.add(new BackButton(iwrb.getLocalizedString("back","Back")), 1, row);
		
		return form;		
  }

  private Form getTooManyForm(IWContext iwc) {
    Form form = getFormMaintainingAllParameters(iwc, false, true);
      Table table = new Table();
      	table.setColor(super.WHITE);
        form.add(table);
      int row = 1;
      IWTimestamp temp;
      table.add(super.getHeaderText(iwrb.getLocalizedString("travel.unavailable_days","Unavailable days")), 1, row);
			table.setRowColor(row, super.backgroundColor);

      //table.add(iwrb.getLocalizedString("travel.unavailable_days","Unavailable days"), 1,row);
      for (int i = 0; i < errorDays.size(); i++) {
        try {
          ++row;
          temp = new IWTimestamp((IWTimestamp)errorDays.get(i));
          table.setRowColor(row, super.GRAY);
          table.add(getLocaleDate(temp), 1,row);
        }catch (NullPointerException npe) {
          npe.printStackTrace(System.err);
        }
      }

			String sBookingId = iwc.getParameter(this.parameterBookingId);

      ++row;
			table.setRowColor(row, super.GRAY);

      if (supplier != null) {
        table.add(iwrb.getLocalizedString("travel.too_many_book_anyway","Too many. Do you wish to book anyway ?"), 1, row);
        ++row;
				table.setRowColor(row, super.GRAY);
        table.add(new BackButton(iwrb.getLocalizedString("back","Back")), 1, row);
        table.add(Text.NON_BREAKING_SPACE, 1, row);
        table.add(new SubmitButton(iwrb.getLocalizedString("travel.book_anyway","Book anyway"),this.BookingAction, this.parameterBookAnyway), 1, row);
        if (sBookingId == null) {
        	table.add(Text.NON_BREAKING_SPACE, 1, row);
        	table.add(new SubmitButton(iwrb.getLocalizedString("travel.send_inquiry","Send inquiry"),this.BookingAction, this.parameterSendInquery), 1, row);
        }
      }else if (_reseller != null) {
      	if (sBookingId == null) {
	        table.add(iwrb.getLocalizedString("travel.too_many_send_inquiry","Too many. Do you wish to send an inquiry ?"), 1, row);
					++row;
					table.setRowColor(row, super.GRAY);
	        table.add(new SubmitButton(iwrb.getImage("buttons/yes.gif"),this.BookingAction, this.parameterSendInquery), 1, row);
	        table.add(new BackButton(iwrb.getImage("buttons/no.gif")), 1, row);
      	}else {
					table.setRowColor(row, super.GRAY);
					table.add(new BackButton(iwrb.getLocalizedString("back","Back")), 1, row);
      	}
      }

    return form;
  }

  public Table getVerifyBookingTable(IWContext iwc, Product product) throws RemoteException, SQLException{
    String surname = iwc.getParameter("surname");
    String lastname = iwc.getParameter("lastname");
    String address = iwc.getParameter("address");
    String area_code = iwc.getParameter("area_code");
    String email = iwc.getParameter("e-mail");
    String telephoneNumber = iwc.getParameter("telephone_number");
    String city = iwc.getParameter("city");
    String country = iwc.getParameter("country");
    String hotelPickupPlaceId = iwc.getParameter(is.idega.idegaweb.travel.data.PickupPlaceBMPBean.getHotelPickupPlaceTableName());
    String room_number = iwc.getParameter("room_number");
    String comment = iwc.getParameter("comment");
    String depAddressId = iwc.getParameter(parameterDepartureAddressId);

    String fromDate = iwc.getParameter(parameterFromDate);
    String manyDays = iwc.getParameter(parameterManyDays);

    String ccNumber = iwc.getParameter(parameterCCNumber);
    String ccMonth = iwc.getParameter(parameterCCMonth);
    String ccYear = iwc.getParameter(parameterCCYear);

    String inquiry = iwc.getParameter(parameterInquiry);

    boolean valid = true;
    String errorColor = "YELLOW";
    Text star = new Text(Text.NON_BREAKING_SPACE+"*");
    star.setFontColor(errorColor);


//    ProductPrice[] pPrices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(this.product.getID(), true);
    ProductPrice[] prices = {};
    ProductPrice[] misc = {};
    Timeframe tFrame = getProductBusiness(iwc).getTimeframe(product, _stamp, Integer.parseInt(depAddressId));
    if (tFrame != null && depAddressId != null) {
    		prices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(product.getID(), -1, -1, true);
      misc = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getMiscellaneousPrices(product.getID(), -1, -1, true);
//			prices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(product.getID(), tFrame.getID(), Integer.parseInt(depAddressId), true);
//			misc = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getMiscellaneousPrices(product.getID(), tFrame.getID(), Integer.parseInt(depAddressId), true);
    }

    Table table = new Table();
      table.setCellpadding(3);
      table.setCellspacing(3);
      int row = 1;

      table.mergeCells(1,1,2,1);
      table.add(getBoldTextWhite(iwrb.getLocalizedString("travel.is_information_correct","Is the following information correct ?")),1,1);


      ++row;
      table.setAlignment(1,row,"right");
      table.setAlignment(2,row,"left");
      table.add(getTextWhite(iwrb.getLocalizedString("travel.name_of_trip","Name of trip")),1,row);
      table.add(getBoldTextWhite(product.getProductName(iwc.getCurrentLocaleId())),2,row);

      ++row;
      table.setAlignment(1,row,"right");
      table.setAlignment(2,row,"left");

      IWTimestamp fromStamp = new IWTimestamp(fromDate);
      try {
        int iManyDays = Integer.parseInt(manyDays);
        IWTimestamp toStamp = new IWTimestamp(fromStamp);
        if (iManyDays > 1) {
          toStamp.addDays(iManyDays);
          table.add(getBoldTextWhite(getLocaleDate(fromStamp)+ " - "+getLocaleDate(toStamp)),2,row);
        }else {
          table.add(getBoldTextWhite(getLocaleDate(fromStamp)),2,row);
        }
      }catch (NumberFormatException n) {
        table.add(star, 2,row);
      }
      table.add(getTextWhite(iwrb.getLocalizedString("travel.date","Date")),1,row);

      ++row;
      table.setAlignment(1,row,"right");
      table.setAlignment(2,row,"left");
      table.add(getTextWhite(iwrb.getLocalizedString("travel.departure_place","Departure place")),1,row);
      table.add(getBoldTextWhite(((com.idega.block.trade.stockroom.data.TravelAddressHome)com.idega.data.IDOLookup.getHomeLegacy(TravelAddress.class)).findByPrimaryKeyLegacy(Integer.parseInt(depAddressId)).getName()),2,row);

      ++row;
      table.setAlignment(1,row,"right");
      table.setAlignment(2,row,"left");
      table.add(getTextWhite(iwrb.getLocalizedString("travel.name","Name")),1,row);
      table.add(getBoldTextWhite(surname+" "+lastname),2,row);
      if (surname.length() < 1) {
        valid = false;
        table.add(star, 2, row);
      }

      ++row;
      table.setAlignment(1,row,"right");
      table.setAlignment(2,row,"left");
      table.add(getTextWhite(iwrb.getLocalizedString("travel.address","Address")),1,row);
      table.add(getBoldTextWhite(address),2,row);
      if (address.length() < 1) {
        valid = false;
        table.add(star, 2, row);
      }

      ++row;
      table.setAlignment(1,row,"right");
      table.setAlignment(2,row,"left");
      table.add(getTextWhite(iwrb.getLocalizedString("travel.area_code","Area code")),1,row);
      table.add(getBoldTextWhite(area_code),2,row);
      if (area_code.length() < 1) {
        valid = false;
        table.add(star, 2, row);
      }

      ++row;
      table.setAlignment(1,row,"right");
      table.setAlignment(2,row,"left");
      table.add(getTextWhite(iwrb.getLocalizedString("travel.city","City")),1,row);
      table.add(getBoldTextWhite(city),2,row);
      if (city.length() < 1) {
        valid = false;
        table.add(star, 2, row);
      }

      ++row;
      table.setAlignment(1,row,"right");
      table.setAlignment(2,row,"left");
      table.add(getTextWhite(iwrb.getLocalizedString("travel.country","Country")),1,row);
      table.add(getBoldTextWhite(country),2,row);
      if (country.length() < 1) {
        valid = false;
        table.add(star, 2, row);
      }

      ++row;
      table.setAlignment(1,row,"right");
      table.setAlignment(2,row,"left");
      table.add(getTextWhite(iwrb.getLocalizedString("travel.email","E-mail")),1,row);
      table.add(getBoldTextWhite(email),2,row);
      if (email.length() < 1) {
        valid = false;
        table.add(star, 2, row);
      }

      ++row;
      table.setAlignment(1,row,"right");
      table.setAlignment(2,row,"left");
      table.add(getTextWhite(iwrb.getLocalizedString("travel.telephone_number","Telephone number")),1,row);
      table.add(getBoldTextWhite(telephoneNumber),2,row);

/*      ++row;
      table.setAlignment(1,row,"right");
      table.setAlignment(2,row,"left");
      table.add(getTextWhite(iwrb.getLocalizedString("travel.comment","Comment")),1,row);
      table.add(getBoldTextWhite(comment),2,row);
*/
      ++row;

      float price = 0;
      int total = 0;
      int current = 0;
      Currency currency = null;

      int pricesLength = prices.length;
      int miscLength = misc.length;
      ProductPrice[] pPrices = new ProductPrice[pricesLength+miscLength];
      for (int i = 0; i < pricesLength; i++) {
        pPrices[i] = prices[i];
      }
      for (int i = 0; i < miscLength; i++) {
        pPrices[i+pricesLength] = misc[i];
      }

      for (int i = 0; i < pPrices.length; i++) {
        ++row;
        table.setAlignment(1,row,"right");
        table.setAlignment(2,row,"left");

        try {
          if (i >= pricesLength) {
            current = Integer.parseInt(iwc.getParameter("miscPriceCategory"+pPrices[i].getID()));
          }else {
            current = Integer.parseInt(iwc.getParameter("priceCategory"+pPrices[i].getID()));
            total += current;
          }
        }catch (NumberFormatException n) {
          current = 0;
        }

        try {
          if (i == 0) {
          	currency = ((com.idega.block.trade.data.CurrencyHome)com.idega.data.IDOLookup.getHomeLegacy(Currency.class)).findByPrimaryKeyLegacy(pPrices[i].getCurrencyId());
          }
        	price += current * getTravelStockroomBusiness(iwc).getPrice(pPrices[i].getID() ,product.getID(),pPrices[i].getPriceCategoryID(), pPrices[i].getCurrencyId() ,IWTimestamp.getTimestampRightNow(), tFrame.getID(), Integer.parseInt(depAddressId));
        }catch (SQLException sql) {
        }catch (NumberFormatException n) {}

        table.add(getTextWhite(pPrices[i].getPriceCategory().getName()),1,row);
        table.add(getBoldTextWhite(Integer.toString(current)),2,row);
      }

      ++row;
      table.setAlignment(1,row,"right");
      table.setAlignment(2,row,"left");
      table.add(getTextWhite(iwrb.getLocalizedString("travel.total_passengers","Total passengers")),1,row);
      table.add(getBoldTextWhite(Integer.toString(total)),2,row);

      ++row;
      table.setAlignment(1,row,"right");
      table.setAlignment(2,row,"left");
      table.add(getTextWhite(iwrb.getLocalizedString("travel.price","Price")),1,row);
      price *= Integer.parseInt(manyDays);
      table.add(getBoldTextWhite(this.df.format(price) + " "),2,row);
      if (currency != null)
      table.add(getBoldTextWhite(currency.getCurrencyAbbreviation()),2,row);
			if (price <= 0) {
				valid = false;
				table.add(star, 2, row);
			}

//      SubmitButton yes = new SubmitButton(iwrb.getImage("buttons/yes.gif"),this.sAction, this.parameterBookingVerified);
      SubmitButton yes = new SubmitButton(iwrb.getLocalizedString("yes","Yes"));
//        yes.setOnSubmit("this.form."+yes.getName()+".disabled = true");
      table.add(new HiddenInput(this.sAction, PublicBooking.parameterBookingVerified),2,row);
        yes.setOnClick("this.form.submit()");
        yes.setOnClick("this.form."+yes.getName()+".disabled = true");
      Link no = new Link(iwrb.getImage("buttons/no.gif"),"#");
          no.setMarkupAttribute("onClick","history.go(-1)");


      if (inquiry == null) {
	    		valid = insertCreditcardBookingVerification(iwc, row, table, errorColor);
	    		row += 5;
      }else {
        debug("inquiry");
      }


      if (inquiry == null) {
        Text bookingsError = getBoldText(iwrb.getLocalizedString("travel.some_days_are_not_available","Some of the selected days are not available"));
          bookingsError.setFontColor(errorColor);
        try {
          BookingForm bf = getServiceHandler(iwc).getBookingForm(iwc, product);
//          TourBookingForm tbf = new TourBookingForm(iwc, product);
          int id = bf.checkBooking(iwc, false);
          if (id != BookingForm.errorTooMany) {
          }else {
            ++row;
            table.mergeCells(1, row, 2, row);
            table.add(bookingsError, 1, row);
            List errorDays = bf.getErrorDays();
            Text dayText;
            if (errorDays != null) {
              valid = false;
              for (int i = 0; i < errorDays.size(); i++) {
                ++row;
                dayText = getBoldText(getLocaleDate(((IWTimestamp) errorDays.get(i))));
                  dayText.setFontColor(errorColor);
                table.add(dayText, 2, row);
              }
            }

          }
        }catch (Exception e) {
          valid = false;
          table.mergeCells(1, row, 2, row);
          table.add(bookingsError, 1, row);
          e.printStackTrace(System.err);
        }
      }else {
        debug("INQUIRY");
      }

      ++row;
      table.setAlignment(1,row,"left");
      table.setAlignment(2,row,"right");
      table.add(no,1,row);
      if (valid) {
        table.add(yes,2,row);
      }


    return table;
  }


  public Form getErrorForm(IWContext iwc, int error) {
    switch (error) {
      case errorTooMany :
        return getTooManyForm(iwc);
      case errorFieldsEmpty :
      	return getFieldErrorForm(iwc);
      case errorTooFew :
      	errorFields.add("too_few_people");
      	return getFieldErrorForm(iwc);
      default:
        return null;
    }
  }

  public List getErrorDays() {
    return errorDays;
  }


  public void setReseller(Reseller reseller) {
    _reseller = reseller;
    _resellerId = reseller.getID();
  }

  protected Text getBoldText(String content) {
    Text text = new Text();
    text.setFontStyle(TravelManager.theBoldTextStyle);
    text.setText(content);
    return text;
  }

  protected Text getBoldTextWhite(String content) {
    Text text = getBoldText(content);
    text.setFontColor(TravelManager.WHITE);
    return text;
  }
  protected Text getTextWhite(String content) {
    Text text = new Text();
    text.setFontStyle(TravelManager.theTextStyle);
    text.setFontColor(TravelManager.WHITE);
    text.setText(content);
    return text;
  }

  public boolean getIfExpired(IWContext iwc) throws RemoteException, SQLException, TimeframeNotFoundException, ServiceNotFoundException{
    if (_reseller != null) {
      return super.getTravelStockroomBusiness(iwc).getIfExpired(_contract, _stamp);
    }else {
      return super.getTravelStockroomBusiness(iwc).getIfDay(iwc,_product, _product.getTimeframes(), _stamp);
    }

  }

  public boolean getIsDayVisible(IWContext iwc) throws RemoteException, SQLException, TimeframeNotFoundException, ServiceNotFoundException {
    return getIsDayVisible(iwc, _stamp);
  }
  public boolean getIsDayVisible(IWContext iwc, IWTimestamp stamp) throws RemoteException, SQLException, TimeframeNotFoundException, ServiceNotFoundException {
  	if (_contract != null) {
  		return super.getTravelStockroomBusiness(iwc).getIfDay(iwc, _contract, _product, stamp);
  	}
  	boolean repps = super.getTravelStockroomBusiness(iwc).getIfDay(iwc,_product, _product.getTimeframes(), stamp);
    return repps;
  }
/*
	public float getOrderPrice(IWContext iwc, Product product, IWTimestamp stamp, boolean includeAllDays)	throws RemoteException, SQLException {
		 int productId = product.getID();
		float price = 0;
		int total = 0;
		int current = 0;
		float currentPrice= 0;
		//Currency currency = null;

		String sAddressId = iwc.getParameter(this.parameterDepartureAddressId);
		int iAddressId = -1;
		if (sAddressId != null) {
			iAddressId = Integer.parseInt(sAddressId);
		}
		
		ProductPrice[] prices = {};
		ProductPrice[] misc = {};
		  prices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(product.getID(), -1, iAddressId, true);
		  misc = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getMiscellaneousPrices(product.getID(), -1, iAddressId, true);
		ProductPrice[] pPrices = new ProductPrice[prices.length + misc.length];
		for (int i = 0; i < prices.length; i++) {
			pPrices[i] = prices[i];
		}
		for (int i = 0; i < misc.length; i++) {
			pPrices[i+prices.length] = misc[i];
		}
		
		  for (int i = 0; i < pPrices.length; i++) {
		    try {
		      current = Integer.parseInt(iwc.getParameter("priceCategory"+pPrices[i].getID()));
		    }catch (NumberFormatException n) {
		      current = 0;
		    }
		    if (current > 0) {
			    currentPrice = getTravelStockroomBusiness(iwc).getPrice(pPrices[i].getID() ,productId,pPrices[i].getPriceCategoryID(), pPrices[i].getCurrencyId() ,IWTimestamp.getTimestampRightNow(), -1, iAddressId);
		    	System.out.println("[BookingForm] pPrice : "+pPrices[i].getPriceCategory().getName()+" : count = "+current+", price = "+currentPrice);
		    	total += current;
		    	price += current * currentPrice;
		    }
		  }
		
		if (includeAllDays) {
			String sMany = iwc.getParameter(this.parameterManyDays);
			int iMany = 1;
			try {
				if (sMany != null) {
					iMany = Integer.parseInt(sMany);
				}	
			}catch(Exception e) {
				e.printStackTrace(System.err);
				iMany = 1;
			}
			price = price * iMany;
		}
		
		return price;
	}
*/	
  /*
	public com.idega.block.tpos.business.TPosClient getTPosClient(IWContext iwc, GeneralBooking gBooking) throws Exception {
		TPosMerchant merchant = getTposMerchant(gBooking);
		com.idega.block.tpos.business.TPosClient t;
		if (merchant == null) {
			t = new com.idega.block.tpos.business.TPosClient(iwc);
		}else {
			t = new com.idega.block.tpos.business.TPosClient(iwc, merchant);
		}
		_TPosClient = t;
		return t;
	}*/

	public CreditCardClient getCreditCardClient(IWContext iwc, GeneralBooking gBooking) {
		try {
			int productSupplierId = gBooking.getService().getProduct().getSupplierId();
			Supplier suppTemp = ((SupplierHome) IDOLookup.getHomeLegacy(Supplier.class)).findByPrimaryKeyLegacy(productSupplierId);
			return getCreditCardBusiness(iwc).getCreditCardClient(suppTemp, new IWTimestamp(gBooking.getDateOfBooking()));
		}catch (Exception e) {
			System.out.println("CreditCardMerchant NOT found");
		}
		
		return null;
/*
		TPosMerchant merchant = null;
		try {
			int productSupplierId = gBooking.getService().getProduct().getSupplierId();
			Supplier suppTemp = ((SupplierHome) IDOLookup.getHomeLegacy(Supplier.class)).findByPrimaryKeyLegacy(productSupplierId);
			System.out.println("Trying to find TPosMerchant for supplier = "+suppTemp.getName());
			merchant = suppTemp.getTPosMerchant();
			System.out.println("TPosMerchant found");
		}catch (Exception e) {
			System.out.println("TPosMerchant NOT found for supplier, using system default");
		}
		return merchant;
		*/
	}

	public String getCurrencyForBooking(GeneralBooking gBooking) throws RemoteException, FinderException {
		BookingEntry[] entries = gBooking.getBookingEntries();
		
		Currency curr;
		CurrencyHome currHome = (CurrencyHome) IDOLookup.getHome(Currency.class);
		
		for (int i = 0; i < entries.length; i++) {
			curr = currHome.findByPrimaryKey(entries[i].getProductPrice().getCurrencyId());
			return curr.getCurrencyAbbreviation();
		}
		
		return null;
	}

	public boolean isFullyBooked(IWContext iwc, Product product, IWTimestamp stamp) throws RemoteException, CreateException, FinderException {
	  int max = 0;
	  if (_reseller != null) {
			Contract cont = super.getContractBusiness(iwc).getContract(_reseller, _product);
			if (cont != null) {
				max = cont.getAlotment();
			}	
	  } else {//if (supplier != null) {
		  ServiceDayHome sDayHome = (ServiceDayHome) IDOLookup.getHome(ServiceDay.class);
		  ServiceDay sDay;// = sDayHome.create();
		  try {
			  sDay = sDayHome.findByServiceAndDay(product.getID() , stamp.getDayOfWeek());
			  
			  if (sDay != null) {
			  	max = sDay.getMax();
			  }
		  } catch (Exception e) {
		  	logDebug("ServiceDay not found for product : "+product.getID());
		  }
	  }

	  	if (max > 0 ) {
	      List addresses;
	      try {
	        addresses = product.getDepartureAddresses(false);
	      }catch (IDOFinderException ido) {
	        ido.printStackTrace(System.err);
	        addresses = new Vector();
	      }
	      int addressId = getAddressIDToUse(iwc, addresses);
				int currentBookings = super.getBooker(iwc).getBookingsTotalCount(product.getID(), stamp, addressId);
				if (currentBookings >= max) {
					_useInquiryForm = true;
					return true;	
				}
	  	}
			
		return false;
	}

	public boolean isUnderBooked(IWContext iwc, Product product, IWTimestamp stamp) throws RemoteException, CreateException, FinderException {
	  ServiceDayHome sDayHome = (ServiceDayHome) IDOLookup.getHome(ServiceDay.class);
	  ServiceDay sDay = null;// = sDayHome.create();
	  try {
	  	sDay = sDayHome.findByServiceAndDay(product.getID() , stamp.getDayOfWeek());
	  } catch (Exception e) {
	  	logDebug("ServiceDay not found for product : "+product.getID());
	  }
	  
	  if (sDay != null) {
	  	int min = sDay.getMin();
	  	if (min > 0 ) {
	      List addresses;
	      try {
	        addresses = product.getDepartureAddresses(false);
	      }catch (IDOFinderException ido) {
	        ido.printStackTrace(System.err);
	        addresses = new Vector();
	      }
	      int addressId = getAddressIDToUse(iwc, addresses);
				int currentBookings = super.getBooker(iwc).getBookingsTotalCount(product.getID(), stamp, addressId);
				if (currentBookings < min) {
					_useInquiryForm = true;
					return true;	
				}
	  	}
	  }
		
		return false;
	}
	
	 /**
	 * @param table
	 * @param row
	 * @param hr
	 * @param star
	 * @return
	 */
	protected int addCreditCardFormElements(IWContext iwc, Product product, Table table, int row, HorizontalRule hr, String star) {
		Text subHeader;
		TextInput ccNumber = new TextInput(this.parameterCCNumber);
		ccNumber.setMaxlength(19);
		ccNumber.setLength(20);
		TextInput ccMonth = new TextInput(this.parameterCCMonth);
		ccMonth.setMaxlength(2);
		ccMonth.setLength(3);
		TextInput ccYear = new TextInput(this.parameterCCYear);
		ccYear.setMaxlength(2);
		ccYear.setLength(3);
		TextInput ccCVC = new TextInput(this.parameterCCCVC);
		ccCVC.setMaxlength(4);
		ccCVC.setLength(5);

		Text ccText = (Text) theText.clone();
		ccText.setText(iwrb.getLocalizedString("travel.credidcard_number","Creditcard number"));
		ccText.addToText(star);

		Text ccMY = (Text) theText.clone();
    ccMY.setText(iwrb.getLocalizedString("travel.month_year","Month / Year"));
		ccMY.addToText(star);

		Text ccCV = (Text) theText.clone();
		ccCV.setText(iwrb.getLocalizedString("travel.cc.cvc","Cardholder Verification Code (CVC)"));
		ccCV.addToText(star);

		Text ccExp = (Text) theText.clone();
	  ccExp.setText(iwrb.getLocalizedString("cc.what_is_cvc","What is CVC?"));

		Text ccSlash = (Text) theText.clone();
		ccSlash.setText(" / ");


		// CREDITCARD STUFF		
		if (this._useInquiryForm) {
		  table.add(new HiddenInput(this.parameterInquiry,"true"), 1, row);
		}else {
			
			Collection imgs = null;
			try {
				imgs = getCreditCardBusiness(iwc).getCreditCardTypeImages(getCreditCardBusiness(iwc).getCreditCardClient(ccMerchant));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		  ++row;
		  table.mergeCells(1,row,6,row);
		  table.add(hr,1,row);
		  ++row;
		  table.mergeCells(1,row,6,row);
		  subHeader = (Text) theBoldText.clone();
		  subHeader.setFontColor(WHITE);
		  subHeader.setText(iwrb.getLocalizedString("travel.booking_creditcard_info","Creditcard infomation"));
		  subHeader.addToText(Text.NON_BREAKING_SPACE);
		  //Text starTextTwo = (Text) theText.clone();
		  //starTextTwo.setFontColor(WHITE);
		  //starTextTwo.setText("("+iwrb.getLocalizedString("travel.visa_eurocard_and_americanexpress_only","Visa, Eurocard and American Express only.")+")");
		  
		  table.add(subHeader,1,row);
		  //table.add(starTextTwo,1,row);
		  if (imgs != null) {
		  		Iterator iter = imgs.iterator();
		  		while (iter.hasNext()) {
		  			table.add(" ", 1, row);
		  			table.add((Image) iter.next(), 1, row);
		  		}
		  }
		  table.setAlignment(1,row,"left");
		  table.setAlignment(4,row,"right");
		  table.setWidth(5,"2");

		  ++row;
		  table.mergeCells(1, row, 2, row);
		  table.mergeCells(3, row, 6, row);
		  table.add(ccText,1,row);
		  table.add(ccNumber,3,row);
		  table.setAlignment(1,row,"right");
		  
		  ++row;
		  table.add(ccMY,1,row);

		  table.mergeCells(1, row, 2, row);
		  table.mergeCells(3, row, 6, row);
		  table.add(ccMonth,3,row);
		  table.add(ccSlash,3,row);
		  table.add(ccYear,3,row);
		  
		  table.setAlignment(1,row,"right");

		  if (useCVC) {
		  		++row;
				Link cvcLink = LinkGenerator.getLinkCVCExplanationPage(iwc, ccExp);

				table.mergeCells(1, row, 2, row);
			  table.mergeCells(3, row, 6, row);
			  table.add(ccCV, 1, row);
			  table.add(ccCVC, 3, row);
				if (cvcLink != null) {
					table.add(cvcLink, 3, row);
				}
			  table.setAlignment(1,row,"right");
		  }
		}
		return row;
	}

  public boolean insertCreditcardBookingVerification(IWContext iwc, int row, Table table, String errorColor) {
    String ccNumber = iwc.getParameter(parameterCCNumber);
    String ccMonth = iwc.getParameter(parameterCCMonth);
    String ccYear = iwc.getParameter(parameterCCYear);
    String ccCVC = iwc.getParameter(parameterCCCVC);

    Text txtNumber = getTextWhite(iwrb.getLocalizedString("travel.creditcard_number","Creditcard number"));
    Text txtMonth = getTextWhite(iwrb.getLocalizedString("travel.month","Month"));
    Text txtYear = getTextWhite(iwrb.getLocalizedString("travel.year","Year"));
    Text txtCVC = getTextWhite(iwrb.getLocalizedString("travel.cc.cvc_short","CVC"));
    
    ++row;
    table.setAlignment(1,row,"right");
    table.setAlignment(2,row,"left");
    table.add(txtNumber,1,row);
    boolean valid = true;
    
    if (ccNumber.length() <5) {
      table.add(getBoldTextWhite(ccNumber),2,row);
    }else {
      for (int i = 0; i < ccNumber.length() -4; i++) {
        table.add(getBoldTextWhite("*"),2,row);
      }
      table.add(getBoldTextWhite(ccNumber.substring(ccNumber.length()-4, ccNumber.length())),2,row);
    }
    
    ++row;
    table.setAlignment(1,row,"right");
    table.setAlignment(2,row,"left");
    table.add(txtMonth,1,row);
    table.add(getBoldTextWhite(ccMonth), 2, row);
    ++row;
    table.setAlignment(1,row,"right");
    table.setAlignment(2,row,"left");
    table.add(txtYear,1,row);
    table.add(getBoldTextWhite(ccYear), 2, row);
    ++row;
    table.setAlignment(1,row,"right");
    table.setAlignment(2,row,"left");
    table.add(txtCVC,1,row);
    Text txtCVCStars = getTextWhite("");
    if (ccCVC != null) {
    			for (int i = 0; i < ccCVC.length(); i++) {
    				txtCVCStars.addToText("*");
    			}
    			table.add(txtCVCStars, 2, row);
    }
    //table.add(getBoldTextWhite(ccCVC), 2, row);
    
	    	if (ccNumber.length() < 13 || ccNumber.length() > 19) {
	    		txtNumber.setFontColor(errorColor);
	        valid = false;
	    	}
	    	if (ccMonth.length() != 2) {
	    		txtMonth.setFontColor(errorColor);
	        valid = false;
	    	}
	    	if (ccYear.length() != 2) {
	    		txtYear.setFontColor(errorColor);
	        valid = false;
	    	}
	    	if (useCVC && ccCVC.length() != 3) {
	    		txtCVC.setFontColor(errorColor);
	        valid = false;
	    	}

	    	if ( !valid ) {
	    		Text ccError = getBoldText(iwrb.getLocalizedString("travel.creditcard_information_incorrect","Creditcard information is incorrect"));
        ccError.setFontColor(errorColor);
	      ++row;
	      table.mergeCells(1, row, 2, row);
	      table.add(ccError, 1, row);
	    	}
    return valid;
  }

	
	public String getPriceCategorySearchKey() {
		return null;
	}
	
	protected Booking getBooking(int bookingID) {
		try {
			return ((GeneralBookingHome) IDOLookup.getHome(GeneralBooking.class)).findByPrimaryKey(new Integer(bookingID));
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		return null;
	}
		
}
