package is.idega.idegaweb.travel.service.tour.presentation;

import is.idega.idegaweb.travel.presentation.TravelManager;
import is.idega.idegaweb.travel.business.TravelStockroomBusiness;
import com.idega.idegaweb.*;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.block.trade.stockroom.data.*;
import com.idega.block.trade.stockroom.business.*;
import com.idega.util.*;
import java.util.*;
import is.idega.idegaweb.travel.data.*;
import is.idega.idegaweb.travel.service.tour.data.*;
import is.idega.idegaweb.travel.service.tour.business.*;
import is.idega.idegaweb.travel.interfaces.Booking;
import is.idega.idegaweb.travel.business.Inquirer;

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

  public static String parameterCCNumber = "CCNumber";
  public static String parameterCCMonth  = "CCMonth";
  public static String parameterCCYear   = "CCYear";

  public static final int errorTooMany = -1;


  public TourBookingForm(IWContext iwc, Product product) throws Exception{
    super.main(iwc);
    setProduct(product);
    iwrb = super.getResourceBundle(iwc);
    supplier = super.getSupplier();

  }

  public Form getBookingForm() {
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

      ProductPrice[] pPrices = ProductPrice.getProductPrices(_service.getID(), false);

      if (pPrices.length > 0) {
          int row = 1;
          int textInputSizeLg = 38;
          int textInputSizeMd = 18;
          int textInputSizeSm = 5;

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

          DropdownMenu pickupMenu = null;
          TextInput roomNumber = null;
          Text tReferenceNumber = (Text) theText.clone();
            tReferenceNumber.setText(iwrb.getLocalizedString("travel.reference_number","Reference number"));
          TextInput tiReferenceNumber = new TextInput("reference_number");
            tiReferenceNumber.setSize(10);

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

          DropdownMenu usersDrop = null;


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

          if (_tour.getIsHotelPickup()) {
              ++row;
              table.mergeCells(2,row,4,row);

              Text hotelText = (Text) theText.clone();
                hotelText.setText(iwrb.getLocalizedString("travel.hotel_pickup_sm","hotel pickup"));
              HotelPickupPlace[] hotelPickup = tsb.getHotelPickupPlaces(this._service);
              pickupMenu = new DropdownMenu(hotelPickup, HotelPickupPlace.getHotelPickupPlaceTableName());
                pickupMenu.addMenuElementFirst("-1",iwrb.getLocalizedString("travel.no_hotel_pickup","No hotel pickup"));

              Text roomNumberText = (Text) theText.clone();
                roomNumberText.setText(iwrb.getLocalizedString("travel.room_number","room number"));
              roomNumber = new TextInput("room_number");
                roomNumber.setSize(textInputSizeSm);

              table.add(hotelText,1,row);
              table.add(pickupMenu,2,row);
              table.add(Text.NON_BREAKING_SPACE+Text.NON_BREAKING_SPACE,2,row);
              table.add(roomNumberText,2,row);
              table.add(Text.NON_BREAKING_SPACE+Text.NON_BREAKING_SPACE,2,row);
              table.add(roomNumber,2,row);
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
                  category = pPrices[i].getPriceCategory();
                  int price = (int) tsb.getPrice(_service.getID(),pPrices[i].getPriceCategoryID(),pPrices[i].getCurrencyId(),idegaTimestamp.getTimestampRightNow());
    //              pPrices[i].getPrice();
                  pPriceCatNameText = (Text) theText.clone();
                    pPriceCatNameText.setText(category.getName());

                  pPriceText = new ResultOutput("thePrice"+i,"0");
                    pPriceText.setSize(8);

                  pPriceMany = new TextInput("priceCategory"+i ,"0");
                    pPriceMany.setSize(5);
                    pPriceMany.setAsNotEmpty("T - Ekki tómt");
                    pPriceMany.setAsIntegers("T - Bara tölur takk");

                  if (_booking != null) {
                    if (entries != null) {
                      for (int j = 0; j < entries.length; j++) {
                        if (entries[j].getProductPriceId() == pPrices[i].getID()) {
                          pPri = entries[j].getProductPrice();
                          currentCount = entries[j].getCount();
                          currentSum = (int) (currentCount * tsb.getPrice(_productId,pPri.getPriceCategoryID(),pPri.getCurrencyId(),idegaTimestamp.getTimestampRightNow()));

                          totalCount += currentCount;
                          totalSum += currentSum;
                          pPriceMany.setContent(Integer.toString(currentCount));
                          pPriceText = new ResultOutput("thePrice"+i,Integer.toString(currentSum));
                            pPriceText.setSize(8);
                        }
                      }
                    }
                  }


                  pPriceText.add(pPriceMany,"*"+price);
                  TotalPassTextInput.add(pPriceMany);
                  TotalTextInput.add(pPriceMany,"*"+price);


                  table.add(pPriceCatNameText, 1,row);
                  table.add(pPriceMany,2,row);
                  table.add(pPriceText, 2,row);


                  txtPrice = (Text) theText.clone();
                    txtPrice.setText(Integer.toString(price));
                  //table.add(txtPrice,3,row);
                  //table.add(txtPerPerson,4,row);


              }catch (SQLException sql) {
                sql.printStackTrace(System.err);
              }
          }

          ++row;

          table.add(totalText,1,row);
          if (_booking != null) {
            TotalPassTextInput.setContent(Integer.toString(totalCount));
            TotalTextInput.setContent(Integer.toString(totalSum));
          }
          table.add(TotalPassTextInput,2,row);
          table.add(TotalTextInput,2,row);
           table.add(new HiddenInput("available",Integer.toString(available)),2,row);

          ++row;
          if (_reseller != null) {
            table.setAlignment(2,row,"right");
            table.add(tReferenceNumber,2,row);
            table.add(tiReferenceNumber,3,row);
          }

          if (super.user != null) {
            ++row;
            List users = null;
            if ( this.supplier != null) users = SupplierManager.getUsers(supplier);
            if ( _reseller != null) users = ResellerManager.getUsers(_reseller);
            if (users == null) users = com.idega.util.ListUtil.getEmptyList();
            usersDrop = new DropdownMenu(users, "ic_user");
            usersDrop.setSelectedElement(Integer.toString(super.userId));

            Text tUser = (Text) theText.clone();
              tUser.setFontColor(WHITE);
              tUser.setText(iwrb.getLocalizedString("travel.user","User"));
            table.add(tUser, 1, row);
            table.add(usersDrop, 2 ,row);
          }

          //else if ( (_reseller == null) && (supplier == null) ) {

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

          //}

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
                n.printStackTrace(System.err);
              }
            }

            if (usersDrop != null) {
              usersDrop.setSelectedElement(Integer.toString(_booking.getUserId()));
            }

          }



          ++row;
          if (_booking != null) {
            table.add(new SubmitButton(iwrb.getImage("buttons/update.gif")),4,row);
          }else {
            table.add(new SubmitButton(iwrb.getImage("buttons/book.gif")),4,row);
          }
          table.add(new HiddenInput(this.BookingAction,this.BookingParameter),4,row);
          //table.setAlignment(4,row,"right");
      }else {
        if (supplier != null || _reseller != null)
          table.add(iwrb.getLocalizedString("travel.pricecategories_not_set_up_right","Pricecategories not set up right"));
      }

      return form;
  }

  public Form getPublicBookingForm(IWContext iwc, Product product, idegaTimestamp stamp) throws ServiceNotFoundException, TimeframeNotFoundException {
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


      ProductPrice[] pPrices = ProductPrice.getProductPrices(_service.getID(), true);

      if (pPrices.length > 0) {
          int row = 1;
          int textInputSizeLg = 28;
          int textInputSizeMd = 28;//18;
          int textInputSizeSm = 28;//5;

          HorizontalRule hr = new HorizontalRule("100%");
            hr.setColor(WHITE);

          Text subHeader;

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

          DropdownMenu pickupMenu = null;
          TextInput roomNumber = null;
          Text tReferenceNumber = (Text) theText.clone();
            tReferenceNumber.setText(iwrb.getLocalizedString("travel.reference_number","Reference number"));
          TextInput tiReferenceNumber = new TextInput("reference_number");
            tiReferenceNumber.setSize(10);

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


          if (_tour.getIsHotelPickup()) {
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
              HotelPickupPlace[] hotelPickup = tsb.getHotelPickupPlaces(this._service);
              pickupMenu = new DropdownMenu(hotelPickup, HotelPickupPlace.getHotelPickupPlaceTableName());
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
          table.mergeCells(1,row,6,row);
          table.add(hr,1,row);
          ++row;
          table.mergeCells(1,row,6,row);
          subHeader = (Text) theBoldText.clone();
            subHeader.setFontColor(WHITE);
            subHeader.setText(iwrb.getLocalizedString("travel.booking_passenger_info","Passenger infomation"));
            subHeader.addToText(star);
          table.add(subHeader,1,row);
          table.setAlignment(1,row,"left");
          ++row;

//          ++row;
//          table.add(Text.NON_BREAKING_SPACE,1,row);
            Text count = (Text) super.theSmallBoldText.clone();
              count.setText(iwrb.getLocalizedString("travel.count","Count"));
            Text amount = (Text) super.theSmallBoldText.clone();
              amount.setText(iwrb.getLocalizedString("travel.amount","Amount"));
            table.add(count,2,row);
            table.add(Text.NON_BREAKING_SPACE+Text.NON_BREAKING_SPACE+Text.NON_BREAKING_SPACE+Text.NON_BREAKING_SPACE+Text.NON_BREAKING_SPACE+Text.NON_BREAKING_SPACE+Text.NON_BREAKING_SPACE+Text.NON_BREAKING_SPACE,2,row);
            table.add(amount,2,row);


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
                  category = pPrices[i].getPriceCategory();
                  int price = (int) tsb.getPrice(_service.getID(),pPrices[i].getPriceCategoryID(),pPrices[i].getCurrencyId(),idegaTimestamp.getTimestampRightNow());
    //              pPrices[i].getPrice();
                  pPriceCatNameText = (Text) theText.clone();
                    pPriceCatNameText.setText(category.getName());

                  pPriceText = new ResultOutput("thePrice"+i,"0");
                    pPriceText.setSize(8);

                  pPriceMany = new TextInput("priceCategory"+i ,"0");
                    pPriceMany.setSize(5);
                    pPriceMany.setAsNotEmpty("T - Ekki tómt");
                    pPriceMany.setAsIntegers("T - Bara tölur takk");

                  if (_booking != null) {
                    if (entries != null) {
                      for (int j = 0; j < entries.length; j++) {
                        if (entries[j].getProductPriceId() == pPrices[i].getID()) {
                          pPri = entries[j].getProductPrice();
                          currentCount = entries[j].getCount();
                          currentSum = (int) (currentCount * tsb.getPrice(_productId,pPri.getPriceCategoryID(),pPri.getCurrencyId(),idegaTimestamp.getTimestampRightNow()));

                          totalCount += currentCount;
                          totalSum += currentSum;
                          pPriceMany.setContent(Integer.toString(currentCount));
                          pPriceText = new ResultOutput("thePrice"+i,Integer.toString(currentSum));
                            pPriceText.setSize(8);
                        }
                      }
                    }
                  }


                  pPriceText.add(pPriceMany,"*"+price);
                  TotalPassTextInput.add(pPriceMany);
                  TotalTextInput.add(pPriceMany,"*"+price);


                  table.add(pPriceCatNameText, 1,row);
                  table.add(pPriceMany,2,row);
                  table.add(pPriceText, 2,row);


                  txtPrice = (Text) theText.clone();
                    txtPrice.setText(Integer.toString(price));
                  //table.add(txtPrice,3,row);
                  //table.add(txtPerPerson,4,row);

                  table.setAlignment(1,row,"right");
                  table.setAlignment(2,row,"left");

              }catch (SQLException sql) {
                sql.printStackTrace(System.err);
              }
          }

          ++row;

          table.add(totalText,1,row);
          if (_booking != null) {
            TotalPassTextInput.setContent(Integer.toString(totalCount));
            TotalTextInput.setContent(Integer.toString(totalSum));
          }
          table.add(TotalPassTextInput,2,row);
          table.add(TotalTextInput,2,row);
          table.setAlignment(1,row,"right");
          table.setAlignment(2,row,"left");
           table.add(new HiddenInput("available",Integer.toString(available)),2,row);

            TextInput ccNumber = new TextInput(this.parameterCCNumber);
              ccNumber.setMaxlength(19);
              ccNumber.setLength(20);
              //ccNumber.setAsNotEmpty("T - vantar cc númer");
              //ccNumber.setAsIntegers("T - cc númer rangt");
            TextInput ccMonth = new TextInput(this.parameterCCMonth);
              ccMonth.setMaxlength(2);
              ccMonth.setLength(3);
              //ccMonth.setAsNotEmpty("T - vantar cc manuð");
              //ccMonth.setAsIntegers("T - cc manuður rangur");
            TextInput ccYear = new TextInput(this.parameterCCYear);
              ccYear.setMaxlength(2);
              ccYear.setLength(3);
              //ccYear.setAsNotEmpty("T - vantar cc ár");
              //ccYear.setAsIntegers("T - cc ár rangt");

            Text ccText = (Text) theText.clone();
              ccText.setText(iwrb.getLocalizedString("travel.credidcard_number","Creditcard number"));

            Text ccMY = (Text) theText.clone();
              ccMY.setText(iwrb.getLocalizedString("travel.valid","valid"));

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
            subHeader.addToText(star);
          table.add(subHeader,1,row);
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
              if ( this.supplier != null) users = SupplierManager.getUsers(supplier);
              if ( _reseller != null) users = ResellerManager.getUsers(_reseller);
              if (users == null) users = com.idega.util.ListUtil.getEmptyList();
              DropdownMenu usersDrop = new DropdownMenu(users, "ic_user");
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
              table.add(new SubmitButton(iwrb.getImage("buttons/update.gif")),6,row);
            }else {
              table.add(new SubmitButton(iwrb.getImage("buttons/book.gif")),6,row);
            }
            table.add(new HiddenInput(this.BookingAction,this.BookingParameter),6,row);

            //table.setColumnAlignment(1,"right");
            //table.setColumnAlignment(2,"left");
            //table.setColumnAlignment(3,"right");
            //table.setColumnAlignment(4,"left");
            Text starText = (Text) theText.clone();
              starText.setFontColor(WHITE);
              starText.setText(iwrb.getLocalizedString("travel.fields_marked_with_a_star","* Fields marked with a star must be filled."));

            table.mergeCells(1,row,5,row);
            table.add(starText,1,row);
            table.setAlignment(6,row,"right");


          }
          else {
            table.add(notAvailSeats,1,row);
            table.add(dateText,1,row);
            table.add(pleaseFindAnotherDay,1,row);
          }
            table.setAlignment(1,1,"left");
        }
    return form;
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
      form.maintainParameter(HotelPickupPlace.getHotelPickupPlaceTableName());
      form.maintainParameter("room_number");
      form.maintainParameter("reference_number");
      form.maintainParameter(CalendarBusiness.PARAMETER_YEAR);
      form.maintainParameter(CalendarBusiness.PARAMETER_MONTH);
      form.maintainParameter(CalendarBusiness.PARAMETER_DAY);
      form.maintainParameter(this.parameterBookingId);
      form.maintainParameter(this.parameterSupplierId);
      form.maintainParameter(this.parameterCCNumber);
      form.maintainParameter(this.parameterCCMonth);
      form.maintainParameter(this.parameterCCYear);
      if (withBookingAction)
      form.maintainParameter(this.BookingAction);
      ProductPrice[] pPrices = ProductPrice.getProductPrices(this._productId, false);
      for (int i = 0; i < pPrices.length; i++) {
        form.maintainParameter("priceCategory"+i);
      }

    return form;
  }


  private int checkBooking(IWContext iwc) throws Exception {

    boolean tooMany = false;
    String sAvailable = iwc.getParameter("available");

    int iAvailable = available;


    if (sAvailable != null)
      iAvailable = Integer.parseInt(sAvailable);

    if (iAvailable != this.availableIfNoLimit) {
      String many;
      int iMany = 0;
      ProductPrice[] pPrices = ProductPrice.getProductPrices(_service.getID(), false);
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
        //form.add(new HiddenInput("numberOfSeats", Integer.toString(iMany)));

      if (iMany > iAvailable) {
          tooMany = true;
      }
    }


    if (tooMany) {
      return this.errorTooMany;
    }else {
      return saveBooking(iwc);
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
    Form form = getFormMaintainingAllParameters(false);
      Table table = new Table();
        form.add(table);

      if (supplier != null) {
        table.add(iwrb.getLocalizedString("travel.too_many_book_anyway","Too many. Do you wish to book anyway ?"));
        table.add(new BackButton("Back"));
        table.add(Text.NON_BREAKING_SPACE);
        table.add(new SubmitButton("Book anyway",this.BookingAction, this.parameterBookAnyway));
        table.add(Text.NON_BREAKING_SPACE);
        table.add(new SubmitButton("Send inquiry",this.BookingAction, this.parameterSendInquery));
      }else if (_reseller != null) {
        table.add(iwrb.getLocalizedString("travel.too_many_send_inquiry","Too many. Do you wish to send an inquiry ?"));
        table.add(new SubmitButton(iwrb.getImage("buttons/yes.gif"),this.BookingAction, this.parameterSendInquery));
        table.add(new BackButton(iwrb.getImage("buttons/no.gif")));
      }

    return form;
  }

  public int handleInsert(IWContext iwc) throws Exception{
    String action = iwc.getParameter(this.BookingAction);
    if (action != null) {
      if (action.equals(this.BookingParameter)) {
        return checkBooking(iwc);
      }else if (action.equals(this.parameterBookAnyway)) {
        return saveBooking(iwc);
      }else if (action.equals(this.parameterSendInquery)) {
        return sendInquery(iwc);
      }else {
        return -1;
      }
    }else {return -1;}

  }

  public int saveBooking(IWContext iwc) {
      String surname = iwc.getParameter("surname");
      String lastname = iwc.getParameter("lastname");
      String address = iwc.getParameter("address");
      String areaCode = iwc.getParameter("area_code");
      String email = iwc.getParameter("e-mail");
      String phone = iwc.getParameter("telephone_number");

      String city = iwc.getParameter("city");
      String country = iwc.getParameter("country");
      String hotelPickupPlaceId = iwc.getParameter(HotelPickupPlace.getHotelPickupPlaceTableName());
      String roomNumber = iwc.getParameter("room_number");
      String referenceNumber = iwc.getParameter("reference_number");

      String sUserId = iwc.getParameter("ic_user");
      if (sUserId == null) sUserId = "-1";

      String ccNumber = iwc.getParameter(this.parameterCCNumber);
      String ccMonth = iwc.getParameter(this.parameterCCMonth);
      String ccYear = iwc.getParameter(this.parameterCCYear);

      String year = iwc.getParameter(CalendarBusiness.PARAMETER_YEAR);
      String month = iwc.getParameter(CalendarBusiness.PARAMETER_MONTH);
      String day = iwc.getParameter(CalendarBusiness.PARAMETER_DAY);

      String supplierId = iwc.getParameter(this.parameterSupplierId);

      try {
        _stamp = new idegaTimestamp(Integer.parseInt(day), Integer.parseInt(month), Integer.parseInt(year));
      }catch (NumberFormatException n) {
        n.printStackTrace(System.err);
      }

      String sBookingId = iwc.getParameter(this.parameterBookingId);

      int iBookingId = -1;
      if (sBookingId != null) iBookingId = Integer.parseInt(sBookingId);

      int returner = 0;

      String many;
      int iMany = 0;
      int iHotelId;

      ProductPrice[] pPrices = ProductPrice.getProductPrices(_service.getID(), false);
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
        int bookingType = Booking.BOOKING_TYPE_ID_ONLINE_BOOKING;

        if (ccNumber != null) {
          paymentType = Booking.PAYMENT_TYPE_ID_CREDIT_CARD;
        }else {
          if (supplier != null) {
            paymentType = Booking.PAYMENT_TYPE_ID_NO_PAYMENT;
          }else if (_reseller != null) {
            paymentType = Booking.PAYMENT_TYPE_ID_VOUCHER;
          }
        }

        if (supplier != null) {
          bookingType = Booking.BOOKING_TYPE_ID_SUPPLIER_BOOKING;
        }else if (_reseller != null) {
          displayFormInternal= true;
          bookingType = Booking.BOOKING_TYPE_ID_THIRD_PARTY_BOOKING;
        }else {
          bookingType = Booking.BOOKING_TYPE_ID_ONLINE_BOOKING;
        }

        if (iBookingId == -1) {
          lbookingId = TourBooker.Book(_service.getID(), iHotelId, roomNumber, country, surname+" "+lastname, address, city, phone, email, _stamp, iMany, bookingType, areaCode, paymentType, Integer.parseInt(sUserId), super.userId);
        }else {
          lbookingId = TourBooker.updateBooking(iBookingId, _service.getID(), iHotelId, roomNumber, country, surname+" "+lastname, address, city, phone, email, _stamp, iMany, areaCode, paymentType, Integer.parseInt(sUserId), super.userId);
        }

        /*
        if (supplier != null) {
          if (iBookingId == -1) {
            lbookingId = TourBooker.BookBySupplier(_service.getID(), iHotelId, roomNumber, country, surname+" "+lastname, address, city, phone, email, _stamp, iMany, areaCode, Booking.PAYMENT_TYPE_ID_NO_PAYMENT);
          }else {
            lbookingId = TourBooker.updateBooking(iBookingId, _service.getID(), iHotelId, roomNumber, country, surname+" "+lastname, address, city, phone, email, _stamp, iMany, areaCode, Booking.PAYMENT_TYPE_ID_NO_PAYMENT);
          }
            displayFormInternal = true;
        }else if (_reseller != null) {
            if (_reseller.getReferenceNumber().equals(referenceNumber)) {
              if (iBookingId == -1) {
                lbookingId = TourBooker.Book(_service.getID(), iHotelId, roomNumber, country, surname+" "+lastname, address, city, phone, email, _stamp, iMany, Booking.BOOKING_TYPE_ID_THIRD_PARTY_BOOKING ,areaCode, Booking.PAYMENT_TYPE_ID_VOUCHER);
              }else {
                lbookingId = TourBooker.updateBooking(iBookingId, _service.getID(), iHotelId, roomNumber, country, surname+" "+lastname, address, city, phone, email, _stamp, iMany, areaCode, Booking.PAYMENT_TYPE_ID_VOUCHER);
              }
              _reseller.addTo(GeneralBooking.class, iBookingId);
              displayFormInternal= true;
            }
        }else if ((supplier == null) && (_reseller == null) ) {
            // if (Median.isCCValid(ccNumber,ccMonth, ccYear));
          if (supplierId == null) {
            iBookingId = TourBooker.Book(_service.getID(), iHotelId, roomNumber, country, surname+" "+lastname, address, city, phone, email, _stamp, iMany, Booking.BOOKING_TYPE_ID_ONLINE_BOOKING ,areaCode, Booking.PAYMENT_TYPE_ID_CREDIT_CARD);
          }else { // Supplier booked with cc number :)
            iBookingId = TourBooker.Book(_service.getID(), iHotelId, roomNumber, country, surname+" "+lastname, address, city, phone, email, _stamp, iMany, Booking.BOOKING_TYPE_ID_SUPPLIER_BOOKING ,areaCode, Booking.PAYMENT_TYPE_ID_CREDIT_CARD);
          }
        }
        */
        if (_reseller != null) {
          _reseller.addTo(GeneralBooking.class, lbookingId);
        }

        returner = lbookingId;

        if (lbookingId != -1) {
          if (iBookingId == -1) {
            BookingEntry bEntry;
            for (int i = 0; i < pPrices.length; i++) {
              if (manys[i] != 0) {
                bEntry = new BookingEntry();
                  bEntry.setProductPriceId(pPrices[i].getID());
                  bEntry.setBookingId(lbookingId);
                  bEntry.setCount(manys[i]);
                bEntry.insert();
              }
            }
          }else {
            BookingEntry bEntry;
            ProductPrice price;
            boolean done = false;
            BookingEntry[] entries = TourBooker.getBookingEntries(new TourBooking(iBookingId));
              if (entries == null) entries = new BookingEntry[]{};
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
              if (!done) {
                bEntry = new BookingEntry();
                  bEntry.setProductPriceId(pPrices[i].getID());
                  bEntry.setBookingId(lbookingId);
                  bEntry.setCount(manys[i]);
                bEntry.insert();
              }
            }
          }
        }



      }catch (NumberFormatException n) {
        n.printStackTrace(System.err);
      }catch (SQLException sql) {
        sql.printStackTrace(System.err);
      }

//      if (displayForm)
//      if (displayFormInternal)
//      displayForm(iwc);


      return returner;
  }

  private int sendInquery(IWContext iwc) throws Exception {
    String surname = iwc.getParameter("surname");
    String lastname = iwc.getParameter("lastname");
    String address = iwc.getParameter("address");
    String areaCode = iwc.getParameter("area_code");
    String email = iwc.getParameter("e-mail");
    String phone = iwc.getParameter("telephone_number");

    String city = iwc.getParameter("city");
    String country = iwc.getParameter("country");
    String hotelPickupPlaceId = iwc.getParameter(HotelPickupPlace.getHotelPickupPlaceTableName());

    String referenceNumber = iwc.getParameter("reference_number");

    try {
        int bookingId = saveBooking(iwc);
        GeneralBooking booking = new GeneralBooking(bookingId);
          booking.setIsValid(false);
          booking.update();

        int numberOfSeats = booking.getTotalCount();


        Inquirer.sendInquery(surname+" "+lastname, email, _stamp, _product.getID() , numberOfSeats, bookingId, _reseller);
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
        _tour = new Tour(product.getID());
      }catch (SQLException sql) {
        sql.printStackTrace(System.err);
      }
    }catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }
/*
  public void setTour(Tour tour) {
    this._tour = tour;
    try {
      setProduct(new Product(tour.getID() ));
    }catch (SQLException s) {
      s.printStackTrace(System.err);
    }
  }
*/
  public void setReseller(Reseller reseller) {
    _reseller = reseller;
    _resellerId = reseller.getID();
  }

  public void setBooking(Booking booking) throws SQLException {
    this._booking = new TourBooking(booking.getID());
  }

  public void setTimestamp(idegaTimestamp stamp) {
    this._stamp = new idegaTimestamp(stamp);
  }


}
