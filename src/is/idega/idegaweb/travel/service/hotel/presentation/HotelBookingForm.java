package is.idega.idegaweb.travel.service.hotel.presentation;

import java.rmi.*;
import java.sql.*;
import java.util.*;

import javax.ejb.*;

import com.idega.block.calendar.business.*;
import com.idega.block.trade.stockroom.business.*;
import com.idega.block.trade.stockroom.data.*;
import com.idega.data.*;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.presentation.ui.*;
import com.idega.util.*;
import is.idega.idegaweb.travel.data.*;
import is.idega.idegaweb.travel.service.presentation.*;

/**
 * <p>Title: idega</p>
 * <p>Description: software</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: idega software</p>
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class HotelBookingForm extends BookingForm {

  public HotelBookingForm(IWContext iwc, Product product) throws Exception{
    super(iwc, product);
  }

  public Form getBookingForm(IWContext iwc) throws RemoteException, FinderException {
    Form form = new Form();
    Table table = new Table();
    table.setBorder(1);
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
//    table.setColumnAlignment(3,"right");
//    table.setColumnAlignment(4,"left");

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
    if (tFrame != null) {
      prices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(_service.getID(), tFrame.getID(), addressId, false);
      misc = ProductPriceBMPBean.getMiscellaneousPrices(_service.getID(), tFrame.getID(), addressId, false);
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
            fromText.setText(iwrb.getLocalizedString("travel.arrival_date","Arrival date"));
        Text manyDaysText = (Text) theText.clone();
            manyDaysText.setText(iwrb.getLocalizedString("travel.number_of_nights","Number of nights"));
//            Text toText = (Text) theText.clone();
//                toText.setText(iwrb.getLocalizedString("travel.departure_day","Departure day"));
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
        table.add(surnameText,1,row);
        table.add(surname,2,row);

        ++row;
        table.add(lastnameText,1,row);
        table.add(lastname,2,row);

        ++row;
        table.add(addressText,1,row);
        table.add(address,2,row);

        ++row;
        table.add(cityText,1,row);
        table.add(city,2,row);

        ++row;
        table.add(areaCodeText,1,row);
        table.add(areaCode,2,row);

        ++row;
        table.add(countryText,1,row);
        table.add(country,2,row);

        ++row;
        table.add(emailText,1,row);
        table.add(email,2,row);

        ++row;
        table.add(telNumberText,1,row);
        table.add(telNumber,2,row);

        if (addresses.size() > 1) {
          ++row;
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
                int price = (int) getTravelStockroomBusiness(iwc).getPrice(pPrices[i].getID(), _service.getID(),pPrices[i].getPriceCategoryID(),pPrices[i].getCurrencyId(),IWTimestamp.getTimestampRightNow(), tFrame.getID(), addressId);
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
          table.add(new SubmitButton(iwrb.getImage("buttons/update.gif"), this.sAction, this.parameterSaveBooking),2,row);
        }else {
          table.add(new SubmitButton(iwrb.getImage("buttons/book.gif"), this.sAction, this.parameterSaveBooking),2,row);
        }
        table.add(new HiddenInput(this.BookingAction,this.BookingParameter),2,row);
        table.setAlignment(2, row, Table.HORIZONTAL_ALIGN_RIGHT);
    }else {
      if (supplier != null || _reseller != null)
        table.add(iwrb.getLocalizedString("travel.pricecategories_not_set_up_right","Pricecategories not set up right"));
    }

    return form;
  }

}
