package is.idega.idegaweb.travel.service.tour.presentation;

import com.idega.data.IDOFinderException;
import is.idega.idegaweb.travel.presentation.TravelManager;
import is.idega.idegaweb.travel.business.TravelStockroomBusiness;
import com.idega.idegaweb.*;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.block.trade.stockroom.data.*;
import com.idega.block.trade.stockroom.business.*;
import com.idega.core.user.data.User;
import com.idega.core.data.Address;
import com.idega.util.*;
import java.util.*;
import is.idega.idegaweb.travel.data.*;
import is.idega.idegaweb.travel.service.tour.data.*;
import is.idega.idegaweb.travel.service.tour.business.*;
import is.idega.idegaweb.travel.interfaces.Booking;
import is.idega.idegaweb.travel.business.*;

import com.idega.block.calendar.business.CalendarBusiness;
import is.idega.idegaweb.travel.business.TravelStockroomBusiness.*;
import java.sql.SQLException;

/**
 * Title:        idegaWeb Travel
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href mailto:"gimmi@idega.is">Grímur Jónsson</a>
 * @version 1.0
 */

public class TourBookingForm extends TravelManager {
  IWResourceBundle iwrb;
  Supplier supplier;
  TravelStockroomBusiness tsb = new TravelStockroomBusiness();

  private Product _product;
  private Service _service;
  private Tour _tour;
  private Contract _contract;
  private idegaTimestamp _stamp;
  private TourBooking _booking;
  private Reseller _reseller;

  private int _productId;
  private int _resellerId;

  int available = is.idega.idegaweb.travel.presentation.Booking.available;
  int availableIfNoLimit = is.idega.idegaweb.travel.presentation.Booking.availableIfNoLimit;

  public static String BookingAction = is.idega.idegaweb.travel.presentation.Booking.BookingAction;
  private String BookingParameter = is.idega.idegaweb.travel.presentation.Booking.BookingParameter;
  public static String parameterBookingId = is.idega.idegaweb.travel.presentation.Booking.parameterBookingId;
  public static String parameterUpdateBooking = "bookingUpdateBooking";
  private static String parameterBookAnyway = "bookingBookAnyway";
  private static String parameterSendInquery = "bookingSendInquery";
  private static String parameterSupplierId = "bookingSupplierId";
  public static String parameterDepartureAddressId = "depAddrId";
  public static String parameterCCNumber = "CCNumber";
  public static String parameterCCMonth  = "CCMonth";
  public static String parameterCCYear   = "CCYear";

  public static String sAction = "bookingFormAction";
  public static String parameterSaveBooking = "bookingFormSaveBooking";

  public static String parameterFromDate = "bookingFromDate";
  public static String parameterManyDays = "bookingManyDays";


  public static final int errorTooMany = -1;
  public static final int inquirySent = -10;
  public List errorDays = new Vector();


  public TourBookingForm(IWContext iwc, Product product) throws Exception{
    super.main(iwc);
    setProduct(product);
    iwrb = super.getResourceBundle(iwc);
    supplier = super.getSupplier();
    setTimestamp(iwc);
  }

  private void setTimestamp(IWContext iwc) {
    String year = iwc.getParameter(CalendarBusiness.PARAMETER_YEAR);
    String month = iwc.getParameter(CalendarBusiness.PARAMETER_MONTH);
    String day = iwc.getParameter(CalendarBusiness.PARAMETER_DAY);
    if (day != null && month != null && year != null) {
      _stamp = new idegaTimestamp(Integer.parseInt(day), Integer.parseInt(month), Integer.parseInt(year));
    }else {
      _stamp = new idegaTimestamp(idegaTimestamp.RightNow());
    }
  }

  public Form getBookingForm(IWContext iwc) throws SQLException {
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
        addresses = ProductBusiness.getDepartureAddresses(_product, false);
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


      ProductPrice[] pPrices = {};
      Timeframe tFrame = ProductBusiness.getTimeframe(_product, _stamp);
      if (tFrame != null) {
        pPrices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(_service.getID(), tFrame.getID(), addressId, false);
      }

      if (pPrices.length > 0) {
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

          DropdownMenu depAddr = new DropdownMenu(addresses, this.parameterDepartureAddressId);
            depAddr.setToSubmit();
            depAddr.setSelectedElement(Integer.toString(addressId));
          DropdownMenu pickupMenu = null;
          TextInput roomNumber = null;

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

          DropdownMenu usersDrop = null;
          DropdownMenu payType = Booker.getPaymentTypeDropdown(iwrb, "payment_type");

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

          HotelPickupPlace[] hotelPickup = tsb.getHotelPickupPlaces(this._service);
          if (hotelPickup.length > 0) {
              ++row;
              table.mergeCells(2,row,4,row);

              Text hotelText = (Text) theText.clone();
                hotelText.setText(iwrb.getLocalizedString("travel.hotel_pickup_sm","hotel pickup"));
              pickupMenu = new DropdownMenu(hotelPickup, is.idega.idegaweb.travel.data.HotelPickupPlaceBMPBean.getHotelPickupPlaceTableName());
                pickupMenu.addMenuElementFirst("-1",iwrb.getLocalizedString("travel.no_hotel_pickup","No hotel pickup"));
                pickupMenu.keepStatusOnAction();

              Text roomNumberText = (Text) theText.clone();
                roomNumberText.setText(iwrb.getLocalizedString("travel.room_number","room number"));
              roomNumber = new TextInput("room_number");
                roomNumber.setSize(textInputSizeSm);
                roomNumber.keepStatusOnAction();

              table.add(hotelText,1,row);
              table.add(pickupMenu,2,row);
              table.add(Text.NON_BREAKING_SPACE+Text.NON_BREAKING_SPACE,2,row);
              table.add(roomNumberText,2,row);
              table.add(Text.NON_BREAKING_SPACE+Text.NON_BREAKING_SPACE,2,row);
              table.add(roomNumber,2,row);
          }

          /**
           * @todo hondla edit booking ...
           */
          if (_booking == null) {
            ++row;
            table.add(fromText, 1, row);
            table.add(fromDate, 2, row);

            ++row;
            table.add(manyDaysText, 1, row);
            table.add(manyDays, 2, row);
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
            entries = TourBooker.getBookingEntries(_booking);
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


          for (int i = 0; i < pPrices.length; i++) {
              try {
                  ++row;
                  category = pPrices[i].getPriceCategory();
                  int price = (int) tsb.getPrice(pPrices[i].getID(), _service.getID(),pPrices[i].getPriceCategoryID(),pPrices[i].getCurrencyId(),idegaTimestamp.getTimestampRightNow(), tFrame.getID(), addressId);
    //              pPrices[i].getPrice();
                  pPriceCatNameText = (Text) theText.clone();
                    pPriceCatNameText.setText(category.getName());

                  pPriceText = new ResultOutput("thePrice"+i,"0");
                    pPriceText.setSize(8);

                  pPriceMany = new TextInput("priceCategory"+i ,"0");
                    pPriceMany.setSize(5);
                    //pPriceMany.setAsNotEmpty("T - Ekki tómt");
                    //pPriceMany.setAsIntegers("T - Bara tölur takk");

                  if (_booking != null) {
                    if (entries != null) {
                      for (int j = 0; j < entries.length; j++) {
                        if (entries[j].getProductPrice().getPriceCategoryID() == pPrices[i].getPriceCategoryID()) {
                          pPri = entries[j].getProductPrice();
                          currentCount = entries[j].getCount();
                          price = (int) tsb.getPrice(pPri.getID(), _productId,pPri.getPriceCategoryID(),pPri.getCurrencyId(),idegaTimestamp.getTimestampRightNow(), tFrame.getID(), addressId);
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

                  pTable = new Table(3,1);
                    pTable.setWidth(1, Integer.toString(pWidthLeft));
                    pTable.setWidth(2, Integer.toString(pWidthCenter));
                    pTable.setWidth(3, Integer.toString(pWidthRight));
                    pTable.setCellpaddingAndCellspacing(0);
                    pTable.add(pPriceMany,1,1);
                    pTable.add(txtPrice,2,1);
                    pTable.add(pPriceText, 3,1);
                  table.add(pTable, 2, row);

              }catch (SQLException sql) {
                sql.printStackTrace(System.err);
              }
          }

          ++row;
          table.mergeCells(1,row,4,row);
//          table.add(pTable, 1, row);
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

          if (super.user != null) {
            ++row;
            List users = null;
            if ( this.supplier != null) users = SupplierManager.getUsersIncludingResellers(supplier);
            if ( _reseller != null) users = ResellerManager.getUsersIncludingSubResellers(_reseller);
            if (users == null) users = com.idega.util.ListUtil.getEmptyList();
            usersDrop = this.getDropdownMenuWithUsers(users, "ic_user");
            usersDrop.setSelectedElement(Integer.toString(super.userId));
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

            if (pickupMenu != null) {
              try {
                pickupMenu.setSelectedElement(Integer.toString(_booking.getHotelPickupPlaceID()));
                roomNumber.setContent(_booking.getRoomNumber());
              }catch (NullPointerException n) {
                //n.printStackTrace(System.err);
              }
            }

            if (usersDrop != null) {
              usersDrop.setSelectedElement(Integer.toString(_booking.getUserId()));
            }
            payType.setSelectedElement(Integer.toString(_booking.getPaymentTypeId()));

          }



          ++row;
          if (_booking != null) {
            table.add(new SubmitButton(iwrb.getImage("buttons/update.gif"), this.sAction, this.parameterSaveBooking),4,row);
          }else {
            table.add(new SubmitButton(iwrb.getImage("buttons/book.gif"), this.sAction, this.parameterSaveBooking),4,row);
          }
          table.add(new HiddenInput(this.BookingAction,this.BookingParameter),4,row);
          //table.setAlignment(4,row,"right");
      }else {
        if (supplier != null || _reseller != null)
          table.add(iwrb.getLocalizedString("travel.pricecategories_not_set_up_right","Pricecategories not set up right"));
      }

      return form;
  }

  public Form getPublicBookingForm(IWContext iwc, Product product, idegaTimestamp stamp) throws ServiceNotFoundException, TimeframeNotFoundException, SQLException {
    Form form = new Form();
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

      isDay = TourBusiness.getIfDay(iwc, _tour, stamp, false);

      if (isDay) {
        if (_tour.getTotalSeats() > 0)
        if (_tour.getTotalSeats() <= Booker.getNumberOfBookings(_tour.getID(), stamp) ) {
          isDay = false;
        }
      }

      List addresses;
      try {
        addresses = ProductBusiness.getDepartureAddresses(_product, false);
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

      ProductPrice[] pPrices = {};
      Timeframe tFrame = ProductBusiness.getTimeframe(_product, stamp);
      if (tFrame != null) {
        pPrices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(_service.getID(), tFrame.getID(), addressId, true);
      }

      Text availSeats = (Text) theText.clone();
        availSeats.setText(iwrb.getLocalizedString("travel.there_are_available_seats","There are available seats "));

      Text notAvailSeats = (Text) theText.clone();
        notAvailSeats.setText(iwrb.getLocalizedString("travel.there_are_no_available_seats","There are no available seats "));

      Text dateText = (Text) theBoldText.clone();
        dateText.setText(stamp.getLocaleDate(iwc));
        dateText.addToText("."+Text.NON_BREAKING_SPACE);

      Text pleaseBook = (Text) theText.clone();
        pleaseBook.setText(iwrb.getLocalizedString("travel.please_book","Please book"));

      Text pleaseFindAnotherDay = (Text) theText.clone();
        pleaseFindAnotherDay.setText(iwrb.getLocalizedString("travel.please_find_another_day","Please find another day"));

      if (pPrices.length > 0) {
          int row = 1;
          int textInputSizeLg = 28;
          int textInputSizeMd = 28;//18;
          int textInputSizeSm = 28;//5;

          HorizontalRule hr = new HorizontalRule("100%");
            hr.setColor(WHITE);

          Text subHeader;


          table.mergeCells(1,row,6,row);


          if (isDay) {
            table.add(availSeats,1,row);
            table.add(dateText,1,row);
            table.add(pleaseBook,1,row);
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
              //emailText.setText(star);
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

          DropdownMenu depAddr = new DropdownMenu(addresses, this.parameterDepartureAddressId);
            depAddr.setToSubmit();
            depAddr.setSelectedElement(Integer.toString(addressId));

          DropdownMenu pickupMenu = null;
          TextInput roomNumber = null;

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

          Table priceTable = new Table();
            priceTable.setBorder(0);
            priceTable.setCellpadding(0);
            priceTable.setCellspacing(6);
          int pRow = 1;

          priceTable.add(count, 1, pRow);
          priceTable.add(unitPrice, 2, pRow);
          priceTable.add(amount, 3, pRow);

          table.add(space, 1, row);
          table.add(priceTable, 2, row);
          table.mergeCells(2, row, 2, row + pPrices.length +1);


          BookingEntry[] entries = null;
          ProductPrice pPri = null;
          int totalCount = 0;
          int totalSum = 0;
          int currentSum = 0;
          int currentCount = 0;
          if (_booking != null) {
            entries = TourBooker.getBookingEntries(_booking);
          }

          for (int i = 0; i < pPrices.length; i++) {
              try {
                  ++row;
                  ++pRow;
                  category = pPrices[i].getPriceCategory();
                  int price = (int) tsb.getPrice(pPrices[i].getID() ,_product.getID(),pPrices[i].getPriceCategoryID(),pPrices[i].getCurrencyId(),idegaTimestamp.getTimestampRightNow(), tFrame.getID(), addressId);
    //              pPrices[i].getPrice();
                  pPriceCatNameText = (Text) theText.clone();
                    pPriceCatNameText.setText(category.getName());

                  pPriceText = new ResultOutput("thePrice"+i,"0");
                    pPriceText.setSize(8);

                  pPriceMany = new TextInput("priceCategory"+i ,"0");
                    pPriceMany.setSize(5);

                  if (_booking != null) {
                    if (entries != null) {
                      for (int j = 0; j < entries.length; j++) {
                        if (entries[j].getProductPriceId() == pPrices[i].getID()) {
                          pPri = entries[j].getProductPrice();
                          currentCount = entries[j].getCount();
                          currentSum = (int) (currentCount * tsb.getPrice(pPri.getID(), _productId,pPri.getPriceCategoryID(),pPri.getCurrencyId(),idegaTimestamp.getTimestampRightNow(), tFrame.getID(), addressId));

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
                  priceTable.add(pPriceMany,1,pRow);
                  priceTable.add(pPriceText, 3,pRow);

                  txtPrice = (Text) theText.clone();
                    txtPrice.setText(Integer.toString(price));
                  priceTable.add(txtPrice, 2,pRow);
//                  table.add(txtPerPerson,3,row);

                  table.setAlignment(1,row,"right");
                  table.setAlignment(2,row,"left");
                  table.setAlignment(3,row,"left");

              }catch (SQLException sql) {
                sql.printStackTrace(System.err);
              }
          }

          ++row;
          ++pRow;

          table.add(totalText,1,row);
          if (_booking != null) {
            TotalPassTextInput.setContent(Integer.toString(totalCount));
            TotalTextInput.setContent(Integer.toString(totalSum));
          }
          priceTable.add(TotalPassTextInput,1,pRow);
          priceTable.add(TotalTextInput,3,pRow);
          priceTable.setColumnAlignment(2, "right");
          table.setAlignment(1,row,"right");
          table.setAlignment(2,row,"left");


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


          HotelPickupPlace[] hotelPickup = tsb.getHotelPickupPlaces(this._service);
          if (hotelPickup.length > 0) {
              ++row;
              table.mergeCells(1,row,6,row);
              table.add(hr,1,row);
              ++row;
              table.mergeCells(1,row,6,row);
              subHeader = (Text) theBoldText.clone();
                subHeader.setFontColor(WHITE);
                subHeader.setText(iwrb.getLocalizedString("travel.booking_choose_hotel","If you choose to be picked up at your hotel, select it from the list below"));
              table.add(subHeader,1,row);
              table.setAlignment(1,row,"left");
              ++row;
              ++row;

              Text hotelText = (Text) theText.clone();
                hotelText.setText(iwrb.getLocalizedString("travel.hotel_pickup_sm","hotel pickup"));
              pickupMenu = new DropdownMenu(hotelPickup, is.idega.idegaweb.travel.data.HotelPickupPlaceBMPBean.getHotelPickupPlaceTableName());
                pickupMenu.addMenuElementFirst("-1",iwrb.getLocalizedString("travel.no_hotel_pickup","No hotel pickup"));

              Text roomNumberText = (Text) theText.clone();
                roomNumberText.setText(iwrb.getLocalizedString("travel.room_number","room number"));
              roomNumber = new TextInput("room_number");
                roomNumber.setSize(textInputSizeSm);

              table.add(hotelText,1,row);
              table.add(pickupMenu,2,row);
              table.add(Text.NON_BREAKING_SPACE+Text.NON_BREAKING_SPACE,2,row);
              table.add(roomNumberText,3,row);
              table.add(Text.NON_BREAKING_SPACE+Text.NON_BREAKING_SPACE,2,row);
              table.add(roomNumber,4,row);

              table.setAlignment(1,row,"right");
              table.setAlignment(2,row,"left");
              table.setAlignment(3,row,"right");
              table.setAlignment(4,row,"left");
              table.mergeCells(4,row,6,row);
          }

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

            table.setBorder(0);
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

            if (super.user != null) {
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
              if (users == null) users = com.idega.util.ListUtil.getEmptyList();
//              DropdownMenu usersDrop = new DropdownMenu(users, "ic_user");
              DropdownMenu usersDrop = this.getDropdownMenuWithUsers(users, "ic_user");
              usersDrop.setSelectedElement(Integer.toString(super.userId));

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
              table.add(new SubmitButton(iwrb.getImage("buttons/book.gif"), this.sAction, this.parameterSaveBooking),6,row);
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

  private DropdownMenu getDropdownMenuWithUsers(List users, String name) {
    DropdownMenu usersDrop = new DropdownMenu("ic_user");
    User usr = null;

    if (!users.contains(super.user)) {
      users.add(0, super.user);
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

 public Form getFormMaintainingAllParameters() {
    return getFormMaintainingAllParameters(true);
 }
 public Form getFormMaintainingAllParameters(boolean withBookingAction) {
    Form form = new Form();
      form.maintainParameter("surname");
      form.maintainParameter("lastname");
      form.maintainParameter("address");
      form.maintainParameter("area_code");
      form.maintainParameter("e-mail");
      form.maintainParameter("telephone_number");
      form.maintainParameter("city");
      form.maintainParameter("country");
      form.maintainParameter(is.idega.idegaweb.travel.data.HotelPickupPlaceBMPBean.getHotelPickupPlaceTableName());
      form.maintainParameter("room_number");
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
      form.maintainParameter(this.sAction);
      if (withBookingAction)
      form.maintainParameter(this.BookingAction);
      ProductPrice[] pPrices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(this._productId, false);
      for (int i = 0; i < pPrices.length; i++) {
        form.maintainParameter("priceCategory"+i);
      }
      form.maintainParameter(this.parameterFromDate);
      form.maintainParameter(this.parameterManyDays);
      form.maintainParameter("ic_user");

    return form;
  }

  public List getErrorDays() {
    return errorDays;
  }

  public int checkBooking(IWContext iwc, boolean saveBookingIfValid) throws Exception {
    boolean tooMany = false;
    String sAvailable = iwc.getParameter("available");

    int iMany = 0;

    String sAddressId = iwc.getParameter(this.parameterDepartureAddressId);
    int iAddressId = Integer.parseInt(sAddressId);
    Timeframe tFrame = ProductBusiness.getTimeframe(_product, _stamp);
    String sBookingId = iwc.getParameter(this.parameterBookingId);
    int iBookingId = -1;

    int previousBookings = 0;
    if (sBookingId != null){
      iBookingId = Integer.parseInt(sBookingId);
      try {
        GeneralBooking gBook = ((is.idega.idegaweb.travel.data.GeneralBookingHome)com.idega.data.IDOLookup.getHomeLegacy(GeneralBooking.class)).findByPrimaryKeyLegacy(iBookingId);
        previousBookings = gBook.getTotalCount();
      }catch (SQLException sql) {
        sql.printStackTrace(System.err);
      }
    }

    ProductPrice[] pPrices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(_service.getID(), tFrame.getID(), iAddressId, false);
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
    idegaTimestamp fromStamp = null;
    idegaTimestamp toStamp = null;
    int betw = 1;
    int totalSeats = _tour.getTotalSeats();

    try {
      fromStamp = new idegaTimestamp(fromDate);
      int iManyDays = Integer.parseInt(manyDays);
      if (iManyDays < 1) betw = 1;
      else betw = iManyDays;
    }catch (Exception e) {}

    iMany -= previousBookings;

    int iAvailable;
    if (totalSeats > 0) {
      if (betw == 1) {
        iAvailable = totalSeats - TourBooker.getNumberOfBookings(serviceId, _stamp);
        if (iMany > iAvailable) {
          tooMany = true;
          errorDays.add(fromStamp);
        }
      }else {
        for (int r = 0; r < betw ; r++) {
          if (r != 0)
          if (_tour != null) {
            fromStamp = new idegaTimestamp(TourBusiness.getNextAvailableDay(iwc, _tour, _product, fromStamp));
          }else {
            fromStamp.addDays(1);
          }
          iAvailable = totalSeats - TourBooker.getNumberOfBookings(serviceId, fromStamp);
          if (iMany > iAvailable) {
              tooMany = true;
              errorDays.add(fromStamp);
          }
        }
      }
    }


    if (tooMany) {
      return this.errorTooMany;
    }else {
      if (saveBookingIfValid) {
        return saveBooking(iwc);
      }else {
        return 0;
      }
    }


  }

  public Form getErrorForm(IWContext iwc, int error) {
    switch (error) {
      case TourBookingForm.errorTooMany :
        return getTooManyForm(iwc);
      default:
        return null;
    }

  }



  private Form getTooManyForm(IWContext iwc) {
    /**
     * @todo gera fínna (þeas meira fínt)
     */

    Form form = getFormMaintainingAllParameters(false);
      Table table = new Table();
        form.add(table);
      int row = 1;
      idegaTimestamp temp;

      table.add(iwrb.getLocalizedString("travel.unavailable_days","Unavailable days"), 1,row);
      for (int i = 0; i < errorDays.size(); i++) {
        try {
          ++row;
          temp = new idegaTimestamp((idegaTimestamp)errorDays.get(i));
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


  /**
   * return bookingId, 0 if nothing is done,  -10 if inquiry is sent
   */
  public int handleInsert(IWContext iwc) throws Exception{
    String check = iwc.getParameter(sAction);
    String action = iwc.getParameter(this.BookingAction);
    if (check.equals(this.parameterSaveBooking)) {
      if (action != null) {
        if (action.equals(this.BookingParameter)) {
          return checkBooking(iwc, true);
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
    }else {
      return 0;
    }

  }

  public int saveBooking(IWContext iwc) throws SQLException {
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

      String sAddressId = iwc.getParameter(this.parameterDepartureAddressId);
      int iAddressId = Integer.parseInt(sAddressId);

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
        _stamp = new idegaTimestamp(Integer.parseInt(day), Integer.parseInt(month), Integer.parseInt(year));
      }catch (NumberFormatException n) {
        n.printStackTrace(System.err);
      }
      idegaTimestamp _fromDate = new idegaTimestamp(_stamp);

      String sBookingId = iwc.getParameter(this.parameterBookingId);

      int iBookingId = -1;
      if (sBookingId != null) iBookingId = Integer.parseInt(sBookingId);

      int returner = 0;

      String many;
      int iMany = 0;
      int iHotelId;

//      ProductPrice[] pPrices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(_service.getID(), false);
      ProductPrice[] pPrices = {};
      Timeframe tFrame = ProductBusiness.getTimeframe(_product, _stamp);
      if (tFrame != null) {
        pPrices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(_service.getID(), tFrame.getID(), iAddressId, true);
      }
      int lbookingId = -1;

      boolean displayFormInternal = false;

      try {
        int[] manys = new int[pPrices.length];
        for (int i = 0; i < manys.length; i++) {
            many = iwc.getParameter("priceCategory"+i);
            if ( (many != null) && (!many.equals("")) && (!many.equals("0"))) {
                manys[i] = Integer.parseInt(many);
                iMany += Integer.parseInt(many);
            }else {
                manys[i] = 0;
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

        /*
        if (ccNumber != null) {
          paymentType = Booking.PAYMENT_TYPE_ID_CREDIT_CARD;
        }else {
          if (supplier != null) {
            paymentType = Booking.PAYMENT_TYPE_ID_NO_PAYMENT;
          }else if (_reseller != null) {
            paymentType = Booking.PAYMENT_TYPE_ID_VOUCHER;
          }
        }*/

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
              if (_tour != null) {
                _fromDate = new idegaTimestamp(TourBusiness.getNextAvailableDay(iwc, _tour, _product, _fromDate));
              }else {
                _fromDate.addDays(1);
              }
            }
            lbookingId = TourBooker.Book(_service.getID(), iHotelId, roomNumber, country, surname+" "+lastname, address, city, phone, email, _fromDate, iMany, bookingType, areaCode, paymentType, Integer.parseInt(sUserId), super.userId, iAddressId);
          }else {
            //handle multiple...
            List tempBookings = Booker.getMultibleBookings(((is.idega.idegaweb.travel.data.GeneralBookingHome)com.idega.data.IDOLookup.getHomeLegacy(GeneralBooking.class)).findByPrimaryKeyLegacy(iBookingId));
            if (tempBookings == null || tempBookings.size() < 2) {
              lbookingId = TourBooker.updateBooking(iBookingId, _service.getID(), iHotelId, roomNumber, country, surname+" "+lastname, address, city, phone, email, _stamp, iMany, areaCode, paymentType, Integer.parseInt(sUserId), super.userId, iAddressId);
            }else {
              GeneralBooking gBooking;
              for (int j = 0; j < tempBookings.size(); j++) {
                gBooking = (GeneralBooking) tempBookings.get(j);
                TourBooker.updateBooking(gBooking.getID(), _service.getID(), iHotelId, roomNumber, country, surname+" "+lastname, address, city, phone, email, new idegaTimestamp(gBooking.getBookingDate()), iMany, areaCode, paymentType, Integer.parseInt(sUserId), super.userId, iAddressId);
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
            GeneralBooking gBook = ((is.idega.idegaweb.travel.data.GeneralBookingHome)com.idega.data.IDOLookup.getHomeLegacy(GeneralBooking.class)).findByPrimaryKeyLegacy(bookingIds[o]);
            gBook.removeFrom(Reseller.class);
          }catch (SQLException sql) {debug(sql.getMessage());}
        }

        /**
         * adding booking to reseller if resellerUser is chosen from dropdown...
         */
        int resId = -7;
        try {
          if (!sUserId.equals("-1")) {
            User user = ((com.idega.core.user.data.UserHome)com.idega.data.IDOLookup.getHomeLegacy(User.class)).findByPrimaryKeyLegacy(Integer.parseInt(sUserId));
            Reseller res = ResellerManager.getReseller(user);
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

        for (int k = 0; k < bookingIds.length; k++) {
          if (bookingIds[k] != -1) {
            if (iBookingId == -1) {
              BookingEntry bEntry;
              for (int i = 0; i < pPrices.length; i++) {
                if (manys[i] != 0) {
                  bEntry = ((is.idega.idegaweb.travel.data.BookingEntryHome)com.idega.data.IDOLookup.getHomeLegacy(BookingEntry.class)).createLegacy();
                    bEntry.setProductPriceId(pPrices[i].getID());
                    bEntry.setBookingId(bookingIds[k]);
                    bEntry.setCount(manys[i]);
                  bEntry.insert();
                }
              }
            }else {
              BookingEntry bEntry;
              ProductPrice price;
              boolean done = false;
              BookingEntry[] entries = TourBooker.getBookingEntries(((is.idega.idegaweb.travel.service.tour.data.TourBookingHome)com.idega.data.IDOLookup.getHomeLegacy(TourBooking.class)).findByPrimaryKeyLegacy(iBookingId));
              for (int i = 0; i < entries.length; i++) {
                entries[i].delete();
              }
              //if (entries == null)
              entries = new BookingEntry[]{};
              for (int i = 0; i < pPrices.length; i++) {
                done = false;
                for (int j = 0; j < entries.length; j++) {
                  if (pPrices[i].getID() == entries[j].getProductPriceId()) {
                    done = true;
                    entries[j].setCount(manys[i]);
                    entries[j].update();
                    break;
                  }
                }
                if (!done && manys[i] != 0) {
                  bEntry = ((is.idega.idegaweb.travel.data.BookingEntryHome)com.idega.data.IDOLookup.getHomeLegacy(BookingEntry.class)).createLegacy();
                    bEntry.setProductPriceId(pPrices[i].getID());
                    bEntry.setBookingId(bookingIds[k]);
                    bEntry.setCount(manys[i]);
                  bEntry.insert();
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

      idegaTimestamp fromStamp = new idegaTimestamp(fromDate);
      idegaTimestamp toStamp = new idegaTimestamp(fromStamp);
        toStamp.addDays(iManyDays);

      int bookingId = saveBooking(iwc);

      GeneralBooking gBooking = ((is.idega.idegaweb.travel.data.GeneralBookingHome)com.idega.data.IDOLookup.getHomeLegacy(GeneralBooking.class)).findByPrimaryKeyLegacy(bookingId);
      List bookings = Booker.getMultibleBookings(gBooking);
      Booking booking = null;

      int numberOfSeats = gBooking.getTotalCount();
      int counter = 0;

      while (toStamp.isLaterThan(fromStamp)) {
        booking = (Booking) bookings.get(counter);
        booking.setIsValid(false);
        booking.update();

        Inquirer.sendInquery(surname+" "+lastname, email, fromStamp, _product.getID() , numberOfSeats, booking.getID(), _reseller);

        if (_tour != null) {
          fromStamp = TourBusiness.getNextAvailableDay(iwc, _tour, _product, fromStamp);
        }else {
          fromStamp.addDays(1);
        }
        ++counter;
      }

        return bookingId;
    }catch (SQLException sql) {
      sql.printStackTrace();
      return -1;
    }
  }

/**
 * @todo Finish update
 * @todo Check booking
 */


  private void setProduct(Product product) {
    _product = product;
    _productId = product.getID();
    try {
      _service = TravelStockroomBusiness.getService(product);
      try {
        _tour = ((is.idega.idegaweb.travel.service.tour.data.TourHome)com.idega.data.IDOLookup.getHomeLegacy(Tour.class)).findByPrimaryKeyLegacy(product.getID());
      }catch (SQLException sql) {
        sql.printStackTrace(System.err);
      }
    }catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  public void setReseller(Reseller reseller) {
    _reseller = reseller;
    _resellerId = reseller.getID();
  }

  public void setBooking(Booking booking) throws SQLException {
    this._booking = ((is.idega.idegaweb.travel.service.tour.data.TourBookingHome)com.idega.data.IDOLookup.getHomeLegacy(TourBooking.class)).findByPrimaryKeyLegacy(booking.getID());
  }

  public void setTimestamp(idegaTimestamp stamp) {
    this._stamp = new idegaTimestamp(stamp);
  }

}
