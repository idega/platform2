package is.idega.idegaweb.travel.service.presentation;

import com.idega.block.trade.data.Currency;
import java.text.DecimalFormat;
import com.idega.business.IBOLookup;
import java.rmi.*;
import java.sql.*;
import java.util.*;

import javax.ejb.*;

import com.idega.block.calendar.business.*;
import com.idega.block.trade.stockroom.business.*;
import com.idega.block.trade.stockroom.data.*;
import com.idega.core.user.data.*;
import com.idega.data.*;
import com.idega.idegaweb.*;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.presentation.ui.*;
import com.idega.util.*;
import is.idega.idegaweb.travel.business.*;
import is.idega.idegaweb.travel.data.*;
import is.idega.idegaweb.travel.interfaces.Booking;
import is.idega.idegaweb.travel.presentation.*;
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
  public static final int inquirySent = -10;
  public static String parameterDepartureAddressId = "depAddrId";

  protected IWResourceBundle iwrb;
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

  public static String sAction = "bookingFormAction";
  public static String parameterSaveBooking = "bookingFormSaveBooking";

  public static String parameterFromDate = "bookingFromDate";
  public static String parameterManyDays = "bookingManyDays";
  protected String parameterOnlineBooking = "pr_onl_bking";

  protected boolean _useInquiryForm = false;
  public List errorDays = new Vector();


  public BookingForm(IWContext iwc, Product product) throws Exception{
    super.initializer(iwc);
    setProduct(iwc, product);
    iwrb = super.getResourceBundle(iwc);
    supplier = super.getSupplier();
    _reseller = super.getReseller();
    if ((_reseller != null) && (product != null)){
      try {
          Contract[] contracts = (Contract[]) (is.idega.idegaweb.travel.data.ContractBMPBean.getStaticInstance(Contract.class)).findAllByColumn(is.idega.idegaweb.travel.data.ContractBMPBean.getColumnNameResellerId(), Integer.toString(_reseller.getID()), is.idega.idegaweb.travel.data.ContractBMPBean.getColumnNameServiceId(), Integer.toString(product.getID()) );
          if (contracts.length > 0) {
            _contract = contracts[0];
            _contractId = ((Integer) _contract.getPrimaryKey()).intValue();
          }
      }catch (SQLException sql) {
          sql.printStackTrace(System.err);
      }

    }
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
      form.addParameter(CalendarBusiness.PARAMETER_YEAR,_stamp.getYear());
      form.addParameter(CalendarBusiness.PARAMETER_MONTH,_stamp.getMonth());
      form.addParameter(CalendarBusiness.PARAMETER_DAY,_stamp.getDay());
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
      addresses = _product.getDepartureAddresses(false);
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

        TextInput surname = new TextInput("surname");
            surname.setSize(textInputSizeLg);
            surname.keepStatusOnAction();
        TextInput lastname = new TextInput("lastname");
            lastname.setSize(textInputSizeLg);
            lastname.keepStatusOnAction();
        TextInput address = new TextInput("address");
            address.setSize(textInputSizeLg);
            address.keepStatusOnAction();
        TextInput areaCode = new TextInput("area_code");
            areaCode.setSize(textInputSizeSm);
            areaCode.keepStatusOnAction();
        TextInput email = new TextInput("e-mail");
            email.setSize(textInputSizeMd);
            email.keepStatusOnAction();
        TextInput telNumber = new TextInput("telephone_number");
            telNumber.setSize(textInputSizeMd);
            telNumber.keepStatusOnAction();
        TextInput city = new TextInput("city");
            city.setSize(textInputSizeLg);
            city.keepStatusOnAction();
        TextInput country = new TextInput("country");
            country.setSize(textInputSizeMd);
            country.keepStatusOnAction();
        TextArea comment = new TextArea("comment");
            comment.setWidth(Integer.toString(textInputSizeLg));
            comment.setHeight("4");
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
        }

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
        table.add(pTable, 2, row);

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

                pPriceText = new ResultOutput("thePrice"+i,"0");
                  pPriceText.setSize(8);

                pPriceMany = new TextInput("priceCategory"+i ,"0");
                  pPriceMany.setSize(5);

                if (i == pricesLength) {
                  Text tempTexti = (Text) theBoldText.clone();
                    tempTexti.setText(iwrb.getLocalizedString("travel.miscellaneous_services","Miscellaneous services"));
                  table.mergeCells(1, row, 2, row);
                  table.add(tempTexti, 1, row);
                  ++row;
                }else if (i == 0) {
                  Text tempTexti = (Text) theBoldText.clone();
                    tempTexti.setText(iwrb.getLocalizedString("travel.basic_prices","Basic prices"));
                    tempTexti.setUnderline(true);
                  table.mergeCells(1, row, 2, row);
                  table.add(tempTexti, 1, row);
                  ++row;
                }
                if (i >= pricesLength) {
                  pPriceMany.setName("miscPriceCategory"+(i-pricesLength));
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
                        pPriceText = new ResultOutput("thePrice"+i,Integer.toString(currentSum));
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

        // Virkar, vantar HTTPS

        /*  TextInput ccNumber = new TextInput(this.parameterCCNumber);
            ccNumber.setMaxlength(16);
            ccNumber.setLength(20);
            ccNumber.setAsNotEmpty("T - vantar cc númer");
            ccNumber.setAsIntegers("T - cc númer rangt");
          TextInput ccMonth = new TextInput(this.parameterCCMonth);
            ccMonth.setMaxlength(2);
            ccMonth.setLength(3);
            ccMonth.setAsNotEmpty("T - vantar cc manuð");
            ccMonth.setAsIntegers("T - cc manuður rangur");
          TextInput ccYear = new TextInput(this.parameterCCYear);
            ccYear.setMaxlength(2);
            ccYear.setLength(3);
            ccYear.setAsNotEmpty("T - vantar cc ár");
            ccYear.setAsIntegers("T - cc ár rangt");

          Text ccText = (Text) theText.clone();
            ccText.setText(iwrb.getLocalizedString("travel.credidcard_number","Creditcard number"));

          Text ccMY = (Text) theText.clone();
            ccMY.setText(iwrb.getLocalizedString("travel.month_year","month / year"));

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
        */



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

  public Form getPublicBookingForm(IWContext iwc, Product product, IWTimestamp stamp) throws RemoteException, FinderException {
    int bookings = getBooker(iwc).getBookingsTotalCount(_productId, this._stamp);
    int max = 0;
    int min = 0;

    try {
      ServiceDayHome sDayHome = (ServiceDayHome) IDOLookup.getHome(ServiceDay.class);
      ServiceDay sDay = sDayHome.create();
        sDay = sDay.getServiceDay(this._productId, stamp);
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
      return getPublicBookingFormPrivate(iwc, product, stamp);
    }catch (ServiceNotFoundException snfe) {
      throw new FinderException(snfe.getMessage());
    }catch (TimeframeNotFoundException tnfe) {
      throw new FinderException(tnfe.getMessage());
    }
  }


  private Form getPublicBookingFormPrivate(IWContext iwc, Product product, IWTimestamp stamp) throws RemoteException, ServiceNotFoundException, TimeframeNotFoundException, FinderException {
    Form form = new Form();
      form.addParameter(this.parameterOnlineBooking, "true");
    Table table = new Table();
      table.setCellpadding(0);
      table.setCellspacing(6);
      table.setBorder(0);
      table.setWidth("100%");
      form.add(table);

      if (stamp != null) {
        form.addParameter(CalendarBusiness.PARAMETER_YEAR,stamp.getYear());
        form.addParameter(CalendarBusiness.PARAMETER_MONTH,stamp.getMonth());
        form.addParameter(CalendarBusiness.PARAMETER_DAY,stamp.getDay());
      }

      boolean isDay = true;

      try {
        isDay = getTravelStockroomBusiness(iwc).getIfDay(iwc, this._product, stamp);
      }catch (SQLException sql) {
        throw new FinderException(sql.getMessage());
      }

      List addresses;
      try {
        addresses = _product.getDepartureAddresses(false);
      }catch (IDOFinderException ido) {
        ido.printStackTrace(System.err);
        addresses = new Vector();
      }
      int addressId = -1;
      String sAddressId = iwc.getParameter(parameterDepartureAddressId);
      if (sAddressId != null) {
        addressId = Integer.parseInt(sAddressId);
      }else if (addresses.size() > 0) {
        addressId = ((TravelAddress) addresses.get(0)).getID();
      }

      ProductPrice[] prices = {};
      ProductPrice[] misc = {};
      Timeframe tFrame = getProductBusiness(iwc).getTimeframe(_product, stamp, addressId);
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
        dateText.setText(stamp.getLocaleDate(iwc));
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

          DateInput fromDate = new DateInput(parameterFromDate);
            fromDate.setDay(_stamp.getDay());
            fromDate.setMonth(_stamp.getMonth());
            fromDate.setYear(_stamp.getYear());
            fromDate.setDisabled(true);

          TextInput manyDays = new TextInput(parameterManyDays);
            manyDays.setContent("1");
            manyDays.setSize(5);

          TextArea comment = new TextArea("comment");
              comment.setWidth("60");
              comment.setHeight("5");

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
          table.add(fromDate, 2, row);
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

          table.add(space, 1, row);
          table.add(pTable, 2, row);
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

                  pPriceText = new ResultOutput("thePrice"+i,"0");
                    pPriceText.setSize(8);

                  pPriceMany = new TextInput("priceCategory"+i ,"0");
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
                    pPriceMany.setName("miscPriceCategory"+(i-pricesLength));
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
                          pPriceText = new ResultOutput("thePrice"+i,Integer.toString(currentSum));
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

/*          ++row;
          table.add(commentText,1,row);
          table.add(comment,2,row);
          table.mergeCells(2, row, 6, row);

          table.setAlignment(1,row,"right");
          table.setVerticalAlignment(1,row,"top");
          table.setAlignment(2,row,"left");
*/

           table.add(new HiddenInput("available",Integer.toString(available)),2,row);

            TextInput ccNumber = new TextInput(this.parameterCCNumber);
              ccNumber.setMaxlength(19);
              ccNumber.setLength(20);
            TextInput ccMonth = new TextInput(this.parameterCCMonth);
              ccMonth.setMaxlength(2);
              ccMonth.setLength(3);
            TextInput ccYear = new TextInput(this.parameterCCYear);
              ccYear.setMaxlength(2);
              ccYear.setLength(3);

            Text ccText = (Text) theText.clone();
              ccText.setText(iwrb.getLocalizedString("travel.credidcard_number","Creditcard number"));
              ccText.addToText(star);

            Text ccMY = (Text) theText.clone();
              ccMY.setText(iwrb.getLocalizedString("travel.valid","valid"));
              ccMY.addToText(star);

            Text ccSlash = (Text) theText.clone();
              ccSlash.setText(" / ");


            // CREDITCARD STUFF
            if (this._useInquiryForm) {
              table.add(new HiddenInput(this.parameterInquiry,"true"), 1, row);
            }else {
              ++row;
              table.mergeCells(1,row,6,row);
              table.add(hr,1,row);
              ++row;
              table.mergeCells(1,row,6,row);
              subHeader = (Text) theBoldText.clone();
                subHeader.setFontColor(WHITE);
                subHeader.setText(iwrb.getLocalizedString("travel.booking_creditcard_info","Creditcard infomation"));
                subHeader.addToText(Text.NON_BREAKING_SPACE);
              Text starTextTwo = (Text) theText.clone();
                starTextTwo.setFontColor(WHITE);
                starTextTwo.setText("("+iwrb.getLocalizedString("travel.visa_eurocard_only","Visa and Eurocard only.")+")");
              table.add(subHeader,1,row);
              table.add(starTextTwo,1,row);
              table.setAlignment(1,row,"left");
              ++row;


              Text month = (Text) super.theSmallBoldText.clone();
                month.setText(iwrb.getLocalizedString("travel.month","Month"));
              Text year = (Text) super.theSmallBoldText.clone();
                year.setText(iwrb.getLocalizedString("travel.year","Year"));
              table.add(month,4,row);
              table.add(ccSlash,5,row);
              table.add(year,6,row);

//              table.setBorder(1);
            //            table.setWidth(4,"2");
              table.setAlignment(4,row,"right");
              table.setWidth(5,"2");

              ++row;
              table.add(ccText,1,row);
              table.add(ccNumber,2,row);
              table.add(ccMY,3,row);

              table.add(ccMonth,4,row);
              table.add(ccSlash,5,row);
              table.add(ccYear,6,row);

              table.setAlignment(1,row,"right");
              table.setAlignment(2,row,"left");
              table.setAlignment(3,row,"right");
              table.setAlignment(4,row,"right");
            }

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

 public Form getFormMaintainingAllParameters() {
    return getFormMaintainingAllParameters(true);
 }
 public Form getFormMaintainingAllParameters(boolean withBookingAction) {
		return getFormMaintainingAllParameters(withBookingAction, false);
 }
 public Form getFormMaintainingAllParameters(boolean withBookingAction, boolean withSAction) {
    Form form = new Form();
      form.maintainParameter("surname");
      form.maintainParameter("lastname");
      form.maintainParameter("address");
      form.maintainParameter("area_code");
      form.maintainParameter("e-mail");
      form.maintainParameter("telephone_number");
      form.maintainParameter("city");
      form.maintainParameter("country");
//      form.maintainParameter(is.idega.idegaweb.travel.data.HotelPickupPlaceBMPBean.getHotelPickupPlaceTableName());
//      form.maintainParameter("room_number");
//      form.maintainParameter("reference_number");
      form.maintainParameter(CalendarBusiness.PARAMETER_YEAR);
      form.maintainParameter(CalendarBusiness.PARAMETER_MONTH);
      form.maintainParameter(CalendarBusiness.PARAMETER_DAY);
      form.maintainParameter(this.parameterBookingId);
      form.maintainParameter(this.parameterSupplierId);
      form.maintainParameter(this.parameterCCNumber);
      form.maintainParameter(this.parameterCCMonth);
      form.maintainParameter(this.parameterCCYear);
      form.maintainParameter(this.parameterDepartureAddressId);
      form.maintainParameter(this.parameterInquiry);
      form.maintainParameter(parameterFromDate);
      form.maintainParameter(this.parameterOnlineBooking);
			if (withSAction) {
	      form.maintainParameter(this.sAction);
			}
      if (withBookingAction) {
        form.maintainParameter(this.BookingAction);
      }

      ProductPrice[] pPrices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(this._productId, false);
      for (int i = 0; i < pPrices.length; i++) {
        form.maintainParameter("priceCategory"+i);
      }
      ProductPrice[] misc = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getMiscellaneousPrices(this._productId, -1, -1, false);
      for (int i = 0; i < misc.length; i++) {
        form.maintainParameter("miscPriceCategory"+i);
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
//    System.out.println("check  = "+check);
//    System.out.println("action  = "+action);
    //debug("action = "+action);

    /** @todo fatta af hverju thetta er herna og hvort megi henda thvi
    if (this._booking == null) {
      //debug("RANUS 0");
      return 0;
    }
    */
  
   
//   if (check.equals(this.parameterSaveBooking)  ) {
      if (action != null) {
        if (action.equals(this.BookingParameter)) {
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


  public int checkBooking(IWContext iwc, boolean saveBookingIfValid) throws Exception {
    return checkBooking(iwc, saveBookingIfValid, false);
  }

  public int checkBooking(IWContext iwc, boolean saveBookingIfValid, boolean bookIfTooMany) throws Exception {
    boolean tooMany = false;

    int iMany = 0;

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


    ProductPrice[] pPrices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(_service.getID(), timeframeId, iAddressId, onlineOnly);
    int current = 0;
    for (int i = 0; i < pPrices.length; i++) {
      try {
        current = Integer.parseInt(iwc.getParameter("priceCategory"+i));
      }catch (NumberFormatException n) {
        current = 0;
      }
      iMany += current;
    }


    int serviceId = _service.getID();
    String fromDate = iwc.getParameter(this.parameterFromDate);
    String manyDays = iwc.getParameter(this.parameterManyDays);
    IWTimestamp fromStamp = null;
    IWTimestamp toStamp = null;
    int betw = 1;
    int totalSeats = 0;

    try {
      fromStamp = new IWTimestamp(fromDate);
      int iManyDays = Integer.parseInt(manyDays);
      if (iManyDays < 1) betw = 1;
      else betw = iManyDays;
    }catch (Exception e) {
      debug(e.getMessage());
    }

    ServiceDayHome sDayHome = (ServiceDayHome) IDOLookup.getHome(ServiceDay.class);
    ServiceDay sDay = sDayHome.create();

    sDay = sDay.getServiceDay(serviceId, fromStamp.getDayOfWeek());
    if (sDay != null) {
      totalSeats = sDay.getMax();
    }

    iMany -= previousBookings;


    int iAvailable;
    if (totalSeats > 0) {
      if (betw == 1) {
        iAvailable = totalSeats - getBooker(iwc).getGeneralBookingHome().getBookingsTotalCount(( (Integer) _service.getPrimaryKey()).intValue(), this._stamp, null, -1, new int[]{}, addressIds );
        if (iMany > iAvailable) {
          tooMany = true;
          errorDays.add(fromStamp);
        }
      }else {
        for (int r = 0; r < betw ; r++) {
          if (r != 0)
          fromStamp.addDays(1);
          iAvailable = totalSeats - getBooker(iwc).getGeneralBookingHome().getBookingsTotalCount(( (Integer) _service.getPrimaryKey()).intValue(), fromStamp, null, -1, new int[]{}, addressIds );
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


  public int saveBooking(IWContext iwc) throws RemoteException, CreateException, RemoveException, FinderException, SQLException{
      String surname = iwc.getParameter("surname");
      String lastname = iwc.getParameter("lastname");
      String address = iwc.getParameter("address");
      String areaCode = iwc.getParameter("area_code");
      String email = iwc.getParameter("e-mail");
      String phone = iwc.getParameter("telephone_number");

      String city = iwc.getParameter("city");
      String country = iwc.getParameter("country");
      String hotelPickupPlaceId = iwc.getParameter(is.idega.idegaweb.travel.data.HotelPickupPlaceBMPBean.getHotelPickupPlaceTableName());
      String roomNumber = iwc.getParameter("room_number");
      String sPaymentType = iwc.getParameter("payment_type");
      String comment = iwc.getParameter("comment");

      String sAddressId = iwc.getParameter(this.parameterDepartureAddressId);
      int iAddressId = -1;
      if (sAddressId != null) {
      	Integer.parseInt(sAddressId);
      }

      String sUserId = iwc.getParameter("ic_user");
      if (sUserId == null) sUserId = "-1";


      String ccNumber = iwc.getParameter(this.parameterCCNumber);
      String ccMonth = iwc.getParameter(this.parameterCCMonth);
      String ccYear = iwc.getParameter(this.parameterCCYear);

      String year = iwc.getParameter(CalendarBusiness.PARAMETER_YEAR);
      String month = iwc.getParameter(CalendarBusiness.PARAMETER_MONTH);
      String day = iwc.getParameter(CalendarBusiness.PARAMETER_DAY);

      String supplierId = iwc.getParameter(this.parameterSupplierId);

      //TEMP BEGINS
        String fromDate = iwc.getParameter(this.parameterFromDate);
        String manyDays = iwc.getParameter(this.parameterManyDays);
      //TEMP ENDS


      try {
        _stamp = new IWTimestamp(Integer.parseInt(day), Integer.parseInt(month), Integer.parseInt(year));
      }catch (NumberFormatException n) {
        n.printStackTrace(System.err);
      }
      IWTimestamp _fromDate = new IWTimestamp(_stamp);

      String sBookingId = iwc.getParameter(this.parameterBookingId);

      int iBookingId = -1;
      if (sBookingId != null) iBookingId = Integer.parseInt(sBookingId);

      int returner = 0;

      String many;
      int iMany = 0;
      int iHotelId;


      String sOnline = iwc.getParameter(this.parameterOnlineBooking);
      boolean onlineOnly = false;
      if (sOnline != null && sOnline.equals("true")) {
        onlineOnly = true;
      }else if (sOnline != null && sOnline.equals("false")) {
        onlineOnly = false;
      }

//      ProductPrice[] pPrices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(_service.getID(), false);
      ProductPrice[] pPrices = {};
      ProductPrice[] misc = {};
      int timeframeId = -1;
      Timeframe tFrame = getProductBusiness(iwc).getTimeframe(_product, _stamp, iAddressId);
      if (tFrame != null) {
        timeframeId = tFrame.getID();
        pPrices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(_service.getID(), timeframeId, iAddressId, onlineOnly);
        misc = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getMiscellaneousPrices(_service.getID(), timeframeId, iAddressId, onlineOnly);
      }else {
        pPrices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(_service.getID(), timeframeId, iAddressId, onlineOnly);
        misc = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getMiscellaneousPrices(_service.getID(), timeframeId, iAddressId, onlineOnly);
      }
      int lbookingId = -1;

      boolean displayFormInternal = false;
      PriceCategory pCat;

      try {
        int[] manys = new int[pPrices.length];
        int[] manyMiscs = new int[misc.length];
        for (int i = 0; i < manys.length; i++) {
          many = iwc.getParameter("priceCategory"+i);
          if ( (many != null) && (!many.equals("")) && (!many.equals("0"))) {
            manys[i] = Integer.parseInt(many);
            iMany += Integer.parseInt(many);
          }else {
            manys[i] = 0;
          }
        }
        for (int i = 0; i < manyMiscs.length; i++) {
          many = iwc.getParameter("miscPriceCategory"+i);
          if ( (many != null) && (!many.equals("")) && (!many.equals("0"))) {
            manyMiscs[i] = Integer.parseInt(many);
          }else {
            manyMiscs[i] = 0;
          }
        }


        try {
          iHotelId = Integer.parseInt(hotelPickupPlaceId);
        }catch (NumberFormatException n) {
          iHotelId = -1;
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

        for (int i = 0; i < betw; i++) {
          if (iBookingId == -1) {
            if (i != 0) {
              _fromDate.addDays(1);
            }
            lbookingId = getBooker(iwc).Book(_service.getID(),country, surname+" "+lastname, address, city, phone, email, _fromDate, iMany, bookingType, areaCode, paymentType, Integer.parseInt(sUserId), super.getUserId(), iAddressId, comment);
          }else {
            //handle multiple...
            List tempBookings = getBooker(iwc).getMultibleBookings(((is.idega.idegaweb.travel.data.GeneralBookingHome)com.idega.data.IDOLookup.getHome(GeneralBooking.class)).findByPrimaryKey(new Integer(iBookingId)));
            if (tempBookings == null || tempBookings.size() < 2) {
              lbookingId = getBooker(iwc).updateBooking(iBookingId, _service.getID(), country, surname+" "+lastname, address, city, phone, email, _stamp, iMany, areaCode, paymentType, Integer.parseInt(sUserId), super.getUserId(), iAddressId, comment);
            }else {
              GeneralBooking gBooking;
              for (int j = 0; j < tempBookings.size(); j++) {
                gBooking = (GeneralBooking) tempBookings.get(j);
                getBooker(iwc).updateBooking(gBooking.getID(), _service.getID(), country, surname+" "+lastname, address, city, phone, email, new IWTimestamp(gBooking.getBookingDate()), iMany, areaCode, paymentType, Integer.parseInt(sUserId), super.getUserId(), iAddressId, comment);
              }
              lbookingId = iBookingId;


            }
          }
          bookingIds[i] = lbookingId;
        }


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
        for (int k = 0; k < bookingIds.length; k++) {
          if (bookingIds[k] != -1) {
            if (iBookingId == -1) {
              BookingEntry bEntry;
              for (int i = 0; i < pPrices.length; i++) {
                if (manys[i] != 0) {
                  bEntry = beHome.create();
                  bEntry.setProductPriceId(pPrices[i].getID());
                  bEntry.setBookingId(bookingIds[k]);
                  bEntry.setCount(manys[i]);
                  bEntry.store();
                }
              }
              for (int i = 0; i < misc.length; i++) {
                if (manyMiscs[i] != 0) {
                  bEntry = ((is.idega.idegaweb.travel.data.BookingEntryHome)com.idega.data.IDOLookup.getHome(BookingEntry.class)).create();
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
              BookingEntry[] entries = getBooker(iwc).getBookingEntries(((is.idega.idegaweb.travel.data.GeneralBookingHome)com.idega.data.IDOLookup.getHome(GeneralBooking.class)).findByPrimaryKey(new Integer(iBookingId)));
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

      }catch (NumberFormatException n) {
        n.printStackTrace(System.err);
      }

      return returner;
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

    String city = iwc.getParameter("city");
    String country = iwc.getParameter("country");
    String hotelPickupPlaceId = iwc.getParameter(is.idega.idegaweb.travel.data.HotelPickupPlaceBMPBean.getHotelPickupPlaceTableName());

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

        inquiryId = getInquirer(iwc).sendInquery(surname+" "+lastname, email, fromStamp, _product.getID() , numberOfSeats, booking.getID(), _reseller);

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


  // ALL BELOW ARE IMPLEMENTED
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
    String year = iwc.getParameter(CalendarBusiness.PARAMETER_YEAR);
    String month = iwc.getParameter(CalendarBusiness.PARAMETER_MONTH);
    String day = iwc.getParameter(CalendarBusiness.PARAMETER_DAY);
    if (day != null && month != null && year != null) {
      _stamp = new IWTimestamp(Integer.parseInt(day), Integer.parseInt(month), Integer.parseInt(year));
    }else {
      _stamp = new IWTimestamp(IWTimestamp.RightNow());
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

  private Form getTooManyForm(IWContext iwc) {
    /**
     * @todo gera fínna (þeas meira fínt)
     */

    Form form = getFormMaintainingAllParameters(false, true);
      Table table = new Table();
        form.add(table);
      int row = 1;
      IWTimestamp temp;

      table.add(iwrb.getLocalizedString("travel.unavailable_days","Unavailable days"), 1,row);
      for (int i = 0; i < errorDays.size(); i++) {
        try {
          ++row;
          temp = new IWTimestamp((IWTimestamp)errorDays.get(i));
          table.add(temp.getLocaleDate(iwc), 1,row);
        }catch (NullPointerException npe) {
          npe.printStackTrace(System.err);
        }
      }

      ++row;

      if (supplier != null) {
        table.add(iwrb.getLocalizedString("travel.too_many_book_anyway","Too many. Do you wish to book anyway ?"), 1, row);
        table.add(new BackButton("Back"), 1, row);
        table.add(Text.NON_BREAKING_SPACE, 1, row);
        table.add(new SubmitButton("Book anyway",this.BookingAction, this.parameterBookAnyway), 1, row);
        table.add(Text.NON_BREAKING_SPACE, 1, row);
        table.add(new SubmitButton("Send inquiry",this.BookingAction, this.parameterSendInquery), 1, row);
      }else if (_reseller != null) {
        table.add(iwrb.getLocalizedString("travel.too_many_send_inquiry","Too many. Do you wish to send an inquiry ?"), 1, row);
        table.add(new SubmitButton(iwrb.getImage("buttons/yes.gif"),this.BookingAction, this.parameterSendInquery), 1, row);
        table.add(new BackButton(iwrb.getImage("buttons/no.gif")), 1, row);
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
    String hotelPickupPlaceId = iwc.getParameter(is.idega.idegaweb.travel.data.HotelPickupPlaceBMPBean.getHotelPickupPlaceTableName());
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
      prices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(product.getID(), tFrame.getID(), Integer.parseInt(depAddressId), true);
      misc = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getMiscellaneousPrices(product.getID(), tFrame.getID(), Integer.parseInt(depAddressId), true);
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
          table.add(getBoldTextWhite(fromStamp.getLocaleDate(iwc)+ " - "+toStamp.getLocaleDate(iwc)),2,row);
        }else {
          table.add(getBoldTextWhite(fromStamp.getLocaleDate(iwc)),2,row);
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
            current = Integer.parseInt(iwc.getParameter("miscPriceCategory"+(i-pricesLength)));
          }else {
            current = Integer.parseInt(iwc.getParameter("priceCategory"+i));
            total += current;
          }
        }catch (NumberFormatException n) {
          current = 0;
        }

        try {
          if (i == 0)
          currency = ((com.idega.block.trade.data.CurrencyHome)com.idega.data.IDOLookup.getHomeLegacy(Currency.class)).findByPrimaryKeyLegacy(pPrices[i].getCurrencyId());
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

//      SubmitButton yes = new SubmitButton(iwrb.getImage("buttons/yes.gif"),this.sAction, this.parameterBookingVerified);
      SubmitButton yes = new SubmitButton(iwrb.getLocalizedString("yes","Yes"));
//        yes.setOnSubmit("this.form."+yes.getName()+".disabled = true");
      table.add(new HiddenInput(this.sAction, PublicBooking.parameterBookingVerified),2,row);
        yes.setOnClick("this.form.submit()");
        yes.setOnClick("this.form."+yes.getName()+".disabled = true");
      Link no = new Link(iwrb.getImage("buttons/no.gif"),"#");
          no.setAttribute("onClick","history.go(-1)");


      if (inquiry == null) {
        ++row;
        table.setAlignment(1,row,"right");
        table.setAlignment(2,row,"left");
        table.add(getTextWhite(iwrb.getLocalizedString("travel.creditcard_number","Creditcard number")),1,row);
        if (ccNumber.length() <5) {
          table.add(getBoldTextWhite(ccNumber),2,row);
        }else {
          for (int i = 0; i < ccNumber.length() -4; i++) {
            table.add(getBoldTextWhite("*"),2,row);
          }
          table.add(getBoldTextWhite(ccNumber.substring(ccNumber.length()-4, ccNumber.length())),2,row);

        }
        if ( ccNumber.length() < 13 || ccNumber.length() > 19 || ccMonth.length() != 2 || ccYear.length() != 2) {
          valid = false;
          Text ccError = getBoldText(iwrb.getLocalizedString("travel.creditcard_information_incorrect","Creditcard information is incorrect"));
            ccError.setFontColor(errorColor);
          ++row;
          table.mergeCells(1, row, 2, row);
          table.add(ccError, 1, row);
        }
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
                dayText = getBoldText(((IWTimestamp) errorDays.get(i)).getLocaleDate(iwc));
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
    return super.getTravelStockroomBusiness(iwc).getIfDay(iwc,_product, _product.getTimeframes(), _stamp);
  }

	public float getOrderPrice(IWContext iwc, Product product, IWTimestamp stamp)	throws RemoteException, SQLException {
		 int productId = product.getID();
		float price = 0;
		int total = 0;
		int current = 0;
		Currency currency = null;
		
		
		ProductPrice[] pPrices = {};
		ProductPrice[] misc = {};
		  pPrices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(product.getID(), -1, -1, true);
		  misc = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getMiscellaneousPrices(product.getID(), -1, -1, true);
		
		
		  for (int i = 0; i < pPrices.length; i++) {
		    try {
		      current = Integer.parseInt(iwc.getParameter("priceCategory"+i));
		    }catch (NumberFormatException n) {
		      current = 0;
		    }
		    total += current;
		    price += current * getTravelStockroomBusiness(iwc).getPrice(pPrices[i].getID() ,productId,pPrices[i].getPriceCategoryID(), pPrices[i].getCurrencyId() ,IWTimestamp.getTimestampRightNow(), -1, -1);
		  }
		
		  for (int i = 0; i < misc.length; i++) {
		    try {
		      current = Integer.parseInt(iwc.getParameter("miscPriceCategory"+i));
		    }catch (NumberFormatException n) {
		      current = 0;
		    }
		    price += current * getTravelStockroomBusiness(iwc).getPrice(misc[i].getID() ,productId,misc[i].getPriceCategoryID(), misc[i].getCurrencyId() ,IWTimestamp.getTimestampRightNow(), -1, -1);
		  }
		
		return price;
	}

	public boolean isFullyBooked(IWContext iwc, Product product, IWTimestamp stamp) throws RemoteException, CreateException, FinderException {
	  ServiceDayHome sDayHome = (ServiceDayHome) IDOLookup.getHome(ServiceDay.class);
	  ServiceDay sDay = sDayHome.create();
	  sDay = sDay.getServiceDay(product.getID() , stamp.getDayOfWeek());
	  
	  if (sDay != null) {
	  	int max = sDay.getMax();
	  	if (max > 0 ) {
				int currentBookings = super.getBooker(iwc).getBookingsTotalCount(product.getID(), stamp);
				if (currentBookings >= max) {
					return true;	
				}
	  	}
	  }
		
		return false;
	}

	public boolean isUnderBooked(IWContext iwc, Product product, IWTimestamp stamp) throws RemoteException, CreateException, FinderException {
	  ServiceDayHome sDayHome = (ServiceDayHome) IDOLookup.getHome(ServiceDay.class);
	  ServiceDay sDay = sDayHome.create();
	  sDay = sDay.getServiceDay(product.getID() , stamp.getDayOfWeek());
	  
	  if (sDay != null) {
	  	int min = sDay.getMin();
	  	if (min > 0 ) {
				int currentBookings = super.getBooker(iwc).getBookingsTotalCount(product.getID(), stamp);
				if (currentBookings < min) {
					return true;	
				}
	  	}
	  }
		
		return false;
	}
		
}
