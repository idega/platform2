package is.idega.idegaweb.travel.service.tour.presentation;

import is.idega.idegaweb.travel.presentation.TravelManager;
import is.idega.idegaweb.travel.business.TravelStockroomBusiness;
import com.idega.idegaweb.*;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.block.trade.stockroom.data.*;
import com.idega.util.*;
import is.idega.idegaweb.travel.data.*;
import is.idega.idegaweb.travel.service.tour.data.*;
import is.idega.idegaweb.travel.service.tour.business.TourBooker;
import is.idega.idegaweb.travel.interfaces.Booking;
import is.idega.idegaweb.travel.business.Inquirer;

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

  public static String BookingAction = "booking_action";
  private String BookingParameter = "booking";
  public static String parameterBookingId = "bookingBookingId";
  public static String parameterUpdateBooking = "bookingUpdateBooking";
  private static String parameterBookAnyway = "bookingBookAnyway";
  private static String parameterSendInquery = "bookingSendInquery";


  public TourBookingForm(IWContext iwc) throws Exception{
    super.main(iwc);
    iwrb = super.getResourceBundle(iwc);
    supplier = super.getSupplier();

  }

  public Form getBookingForm() {
      Form form = new Form();
      Table table = new Table();
        form.add(table);
        form.addParameter("year",Integer.toString(_stamp.getYear()));
        form.addParameter("month",Integer.toString(_stamp.getMonth()));
        form.addParameter("day",Integer.toString(_stamp.getDay()));
        table.setWidth("100%");

      table.setColumnAlignment(1,"right");
      table.setColumnAlignment(2,"left");
      table.setColumnAlignment(3,"right");
      table.setColumnAlignment(4,"left");

      ProductPrice[] pPrices = ProductPrice.getProductPrices(_service.getID(), true);

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
          }else if ( (_reseller == null) && (supplier == null) ) {
            TextInput ccNumber = new TextInput("ccNumber");
              ccNumber.setMaxlength(16);
              ccNumber.setLength(20);
              ccNumber.setAsNotEmpty("T - vantar cc númer");
              ccNumber.setAsIntegers("T - cc númer rangt");
            TextInput ccMonth = new TextInput("ccMonth");
              ccMonth.setMaxlength(2);
              ccMonth.setLength(3);
              ccMonth.setAsNotEmpty("T - vantar cc manuð");
              ccMonth.setAsIntegers("T - cc manuður rangur");
            TextInput ccYear = new TextInput("ccYear");
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


          }

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

  private int checkBooking(IWContext iwc) throws Exception {
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
      form.maintainParameter("year");
      form.maintainParameter("month");
      form.maintainParameter("day");
      form.maintainParameter(this.parameterBookingId);

    boolean tooMany = false;
    String sAvailable = iwc.getParameter("available");


    int iAvailable = Integer.parseInt(sAvailable);

    if (iAvailable != available) {
      String many;
      int iMany = 0;
      ProductPrice[] pPrices = ProductPrice.getProductPrices(_service.getID(), false);
        int[] manys = new int[pPrices.length];
        for (int i = 0; i < manys.length; i++) {
            form.maintainParameter("priceCategory"+i);
            many = iwc.getParameter("priceCategory"+i);
            if ( (many != null) && (!many.equals("")) && (!many.equals("0"))) {
                manys[i] = Integer.parseInt(many);
                iMany += Integer.parseInt(many);
            }else {
                manys[i] = 0;
            }
        }
        form.add(new HiddenInput("numberOfSeats", Integer.toString(iMany)));

      if (iMany > iAvailable) {
          tooMany = true;
      }
    }


    if (tooMany) {
      Table table = new Table();
        form.add(table);

      if (supplier != null) {
        table.add(iwrb.getLocalizedString("travel.too_many_book_anyway","Too many, book anyway ?"));
        table.add(new SubmitButton(iwrb.getImage("buttons/yes.gif"),this.BookingAction, this.parameterBookAnyway));
        table.add(new BackButton(iwrb.getImage("buttons/no.gif")));
      }else if (_reseller != null) {
        table.add(iwrb.getLocalizedString("travel.too_many_send_inquiry","Too many, send inquiry ?"));
        table.add(new SubmitButton(iwrb.getImage("buttons/yes.gif"),this.BookingAction, this.parameterSendInquery));
        table.add(new BackButton(iwrb.getImage("buttons/no.gif")));
      }

      add(form);
      return -1;
    }else {
      return saveBooking(iwc);
    }

  }

  public int handleInsert(IWContext iwc) throws Exception{
    String action = iwc.getParameter(this.BookingAction);
      System.err.println("ACTION "+action);
    if (action.equals(this.BookingParameter)) {
      return checkBooking(iwc);
    }else if (action.equals(this.parameterBookAnyway)) {
      return saveBooking(iwc);
    }else if (action.equals(this.parameterSendInquery)) {
      return sendInquery(iwc);
    }else {
      return -1;
    }

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

      String ccNumber = iwc.getParameter("ccNumber");
      String ccMonth = iwc.getParameter("ccMonth");
      String ccYear = iwc.getParameter("ccYear");

      String year = iwc.getParameter("year");
      String month = iwc.getParameter("month");
      String day = iwc.getParameter("day");
      try {
        _stamp = new idegaTimestamp(Integer.parseInt(day), Integer.parseInt(month), Integer.parseInt(year));
      }catch (NumberFormatException n) {}

      String sBookingId = iwc.getParameter(this.parameterBookingId);
      System.err.println("sBookingId "+sBookingId);

      int iBookingId = -1;
      if (sBookingId != null) iBookingId = Integer.parseInt(sBookingId);
      System.err.println("iBookingId "+iBookingId);

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

        if (supplier != null) {
          if (iBookingId == -1) {
            System.err.println("1");
            lbookingId = TourBooker.BookBySupplier(_service.getID(), iHotelId, roomNumber, country, surname+" "+lastname, address, city, phone, email, _stamp, iMany, areaCode);
          }else {
            System.err.println("2");
            lbookingId = TourBooker.updateBooking(iBookingId, _service.getID(), iHotelId, roomNumber, country, surname+" "+lastname, address, city, phone, email, _stamp, iMany, areaCode);
          }
            displayFormInternal = true;
        }else if (_reseller != null) {
            if (_reseller.getReferenceNumber().equals(referenceNumber)) {
              if (iBookingId == -1) {
            System.err.println("3");
                lbookingId = TourBooker.Book(_service.getID(), iHotelId, roomNumber, country, surname+" "+lastname, address, city, phone, email, _stamp, iMany, Booking.BOOKING_TYPE_ID_THIRD_PARTY_BOOKING ,areaCode);
              }else {
            System.err.println("4");
                lbookingId = TourBooker.updateBooking(iBookingId, _service.getID(), iHotelId, roomNumber, country, surname+" "+lastname, address, city, phone, email, _stamp, iMany, areaCode);
              }
              _reseller.addTo(GeneralBooking.class, iBookingId);
              displayFormInternal= true;
            }
        }else if ((supplier == null) && (_reseller == null) ) {
            // if (Median.isCCValid(ccNumber,ccMonth, ccYear));
            iBookingId = TourBooker.Book(_service.getID(), iHotelId, roomNumber, country, surname+" "+lastname, address, city, phone, email, _stamp, iMany, Booking.BOOKING_TYPE_ID_ONLINE_BOOKING ,areaCode);
        }

        returner = iBookingId;

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
      if (displayFormInternal)
//      displayForm(iwc);
  System.err.println("TourBookingForm displayForm shit");


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
    String numberOfSeats = iwc.getParameter("numberOfSeats");

    String referenceNumber = iwc.getParameter("reference_number");

    try {
        int bookingId = saveBooking(iwc);
        GeneralBooking booking = new GeneralBooking(bookingId);
          booking.setIsValid(false);
          booking.update();


        Inquirer.sendInquery(surname+" "+lastname, email, _stamp, _product.getID() , Integer.parseInt(numberOfSeats), bookingId, _reseller);
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


  public void setProduct(Product product) {
    _product = product;
    _productId = product.getID();
    try {
      _service = TravelStockroomBusiness.getService(product);
    }catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void setTour(Tour tour) {
    this._tour = tour;
    try {
      setProduct(new Product(tour.getID() ));
    }catch (SQLException s) {
      s.printStackTrace(System.err);
    }
  }

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
