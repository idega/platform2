package is.idega.idegaweb.travel.service.hotel.presentation;

import is.idega.idegaweb.travel.business.ServiceNotFoundException;
import is.idega.idegaweb.travel.business.TimeframeNotFoundException;
import is.idega.idegaweb.travel.data.BookingEntry;
import is.idega.idegaweb.travel.data.Contract;
import is.idega.idegaweb.travel.data.PickupPlace;
import is.idega.idegaweb.travel.data.PickupPlaceHome;
import is.idega.idegaweb.travel.data.ServiceDay;
import is.idega.idegaweb.travel.data.ServiceDayHome;
import is.idega.idegaweb.travel.presentation.PublicBooking;
import is.idega.idegaweb.travel.service.hotel.business.HotelBooker;
import is.idega.idegaweb.travel.service.hotel.business.HotelBusiness;
import is.idega.idegaweb.travel.service.hotel.data.Hotel;
import is.idega.idegaweb.travel.service.hotel.data.HotelHome;
import is.idega.idegaweb.travel.service.presentation.BookingForm;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.trade.data.Currency;
import com.idega.block.trade.stockroom.business.ProductPriceException;
import com.idega.block.trade.stockroom.business.ResellerManager;
import com.idega.block.trade.stockroom.business.SupplierManager;
import com.idega.block.trade.stockroom.data.PriceCategory;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.ProductPrice;
import com.idega.block.trade.stockroom.data.ProductPriceBMPBean;
import com.idega.block.trade.stockroom.data.Timeframe;
import com.idega.block.trade.stockroom.data.TravelAddress;
import com.idega.business.IBOLookup;
import com.idega.data.IDOException;
import com.idega.data.IDOFinderException;
import com.idega.data.IDOLookup;
import com.idega.data.IDORelationshipException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.presentation.CalendarParameters;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.HorizontalRule;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.ResultOutput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWTimestamp;

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

    if (!getIfExpired(iwc)) {
      return getForm(iwc);
    }else {
      return getExpiredForm(iwc);
    }

  }

  public boolean getIfExpired(IWContext iwc) throws RemoteException{
    boolean isExpired = false;
    if (_reseller != null) {
      return isExpired = getHotelBusiness(iwc).getIfExpired(_contract, _stamp);
    } else {
    	IWTimestamp now = IWTimestamp.RightNow();
    	now.addDays(-1);
    	
    	return now.isLaterThanOrEquals(_stamp);
    }
  }

  public boolean getIsDayVisible(IWContext iwc) throws RemoteException, SQLException, TimeframeNotFoundException, ServiceNotFoundException {
		return getIsDayVisible(iwc, _stamp);
  }
  public boolean getIsDayVisible(IWContext iwc, IWTimestamp stamp) throws RemoteException, SQLException, TimeframeNotFoundException, ServiceNotFoundException {
  	if (this._contract != null) {
  		return super.getIsDayVisible(iwc, stamp);
  	} else {
  		return getHotelBusiness(iwc).getIfDay(iwc,_product, _product.getTimeframes(), stamp, false, true);
  	}
  }


  private Form getForm(IWContext iwc) throws RemoteException, FinderException {
  	
    Form form = new Form();
    Table table = new Table();
    table.setBorder(0);
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
//    table.setColumnAlignment(3,"right");
//    table.setColumnAlignment(4,"left");

//      ProductPrice[] pPrices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(_service.getID(), false);
    List addresses;
    try {
			addresses = super.getProductBusiness(iwc).getDepartureAddresses(_product, _stamp, false);
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

//          DateInput toDate = new DateInput("baraBogus");
//          toDate.setDisabled(true);


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
        Text toText = (Text) theText.clone();
            toText.setText(iwrb.getLocalizedString("travel.departure_date","Departure date"));
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
	          comment.setWidth("350");
	          comment.setHeight("60");
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


	      DropdownMenu pickupMenu = null;
	      TextInput roomNumber = null;

	      PickupPlaceHome hppHome = (PickupPlaceHome) IDOLookup.getHome(PickupPlace.class);
	      Collection hotelPickup = hppHome.findHotelPickupPlaces(this._service);
	      //HotelPickupPlace[] hotelPickup = (HotelPickupPlace[]) coll.toArray(new HotelPickupPlace[]{});
	      if (hotelPickup.size() > 0) {
	          ++row;
	          table.mergeCells(2,row,4,row);
	
	          Text hotelText = (Text) theText.clone();
	            hotelText.setText(iwrb.getLocalizedString("travel.pickup_sm","pickup"));
	          pickupMenu = new DropdownMenu(hotelPickup, parameterPickupId);
	            pickupMenu.addMenuElementFirst("-1",iwrb.getLocalizedString("travel.no_pickup","No pickup"));
	            pickupMenu.keepStatusOnAction();
	
	          Text roomNumberText = (Text) theText.clone();
	            roomNumberText.setText(iwrb.getLocalizedString("travel.extra_info","extra_info"));
	          roomNumber = new TextInput(parameterPickupInf);
	            roomNumber.setSize(textInputSizeSm);
	            roomNumber.keepStatusOnAction();
	
	          table.add(hotelText,1,row);
	          table.add(pickupMenu,2,row);
	          table.add(Text.NON_BREAKING_SPACE+Text.NON_BREAKING_SPACE,2,row);
	          table.add(roomNumberText,2,row);
	          table.add(Text.NON_BREAKING_SPACE+Text.NON_BREAKING_SPACE,2,row);
	          table.add(roomNumber,2,row);
	      }
	
				++row;
				table.add(fromText, 1, row);
				table.add(fromDate, 2, row);
				++row;
				table.add(manyDaysText, 1, row);
				table.add(manyDays, 2, row);

        if (_booking != null) {
//        	fromDate.setDate(_booking.getBookingDate());
						fromDate.setDisabled(false);
						if (this._multipleBookings) {
							bookingDays = super._multipleBookingNumber[1];
							manyDays.setContent(Integer.toString(bookingDays));	
						}
//          ++row;
//          table.add(toText, 1, row);
//          table.add(toDate, 2, row);
//          manyDays.setOnBlur("this.form."+toDate.sety+".value=\"2002-11-23\"");
        }/*else {
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

        Table pTable = new Table(3,1);
          pTable.setWidth(1, Integer.toString(pWidthLeft));
          pTable.setWidth(2, Integer.toString(pWidthCenter));
          pTable.setWidth(3, Integer.toString(pWidthRight));
          pTable.setCellpaddingAndCellspacing(0);
        table.add(pTable, 2, row+1);
//			pTable.setBorder(1);

        Text count = (Text) super.theSmallBoldText.clone();
          count.setText(iwrb.getLocalizedString("travel.number_of_units","Number of units"));
        Text unitPrice = (Text) super.theSmallBoldText.clone();
          unitPrice.setText(iwrb.getLocalizedString("travel.unit_price","Unit price"));
        Text amount = (Text) super.theSmallBoldText.clone();
          amount.setText(iwrb.getLocalizedString("travel.total_price","Total price"));

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

	              if (pricesLength == 1 && i < pricesLength) {
	                pPriceText = new ResultOutput("thePrice"+pPrices[i].getID(),Integer.toString(price));
	                pPriceText.setSize(8);
	                pPriceMany = new TextInput("priceCategory"+pPrices[i].getID() ,"1");
	                pPriceMany.setSize(5);
	                TotalPassTextInput.setContent("1");
	                TotalTextInput.setContent(Integer.toString(price));
	              } else {
	                pPriceText = new ResultOutput("thePrice"+pPrices[i].getID(),"0");
	                pPriceText.setSize(8);
	                pPriceMany = new TextInput("priceCategory"+pPrices[i].getID() ,"0");
	                pPriceMany.setSize(5);
	              }

 //                 pPriceText = new ResultOutput("thePrice"+pPrices[i].getID(),"0");
 //                 pPriceText.setSize(8);

//                pPriceMany = new TextInput("priceCategory"+pPrices[i].getID() ,"0");
//                  pPriceMany.setSize(5);

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
                        Collection pTimeframes;
												try {
	                        price = (int) getTravelStockroomBusiness(iwc).getPrice(pPri.getID(), _productId,pPri.getPriceCategoryID(),pPri.getCurrencyId(),IWTimestamp.getTimestampRightNow(), timeframeId, addressId);
												} catch (ProductPriceException e) {
													try {
		                        int pTimeframeId = -1;
														pTimeframes = pPri.getTimeframes();
		                        if (pTimeframes != null && pTimeframes.size()>0) {
		                        	Iterator its = pTimeframes.iterator();
		                        	pTimeframeId = ((Timeframe)its.next()).getID();	
		                        }
		                        price = (int) getTravelStockroomBusiness(iwc).getPrice(pPri.getID(), _productId,pPri.getPriceCategoryID(),pPri.getCurrencyId(),IWTimestamp.getTimestampRightNow(), pTimeframeId, addressId);
													} catch (IDORelationshipException idoe) {
														idoe.printStackTrace(System.err);
													}
												}
//                        price = (int) getTravelStockroomBusiness(iwc).getPrice(pPri.getID(), _productId,pPri.getPriceCategoryID(),pPri.getCurrencyId(),IWTimestamp.getTimestampRightNow(), pTimeframeId, addressId);
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
        ++row;

        table.add(totalText,1,row);

        if (_booking != null) {
          TotalPassTextInput.setContent(Integer.toString(totalCount));
          TotalTextInput.setContent(Integer.toString(totalSum * bookingDays));
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
	        if (pickupMenu != null) {
	          try {
	            pickupMenu.setSelectedElement(Integer.toString(_booking.getPickupPlaceID()));
	            roomNumber.setContent(_booking.getPickupExtraInfo());

	          }catch (NullPointerException n) {
	            //n.printStackTrace(System.err);
	          }
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

  public Form getPublicBookingForm(IWContext iwc, Product product) throws RemoteException, FinderException {
		/*
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
	
	    /// ef ferd er fullbokud eda ef ferd er vanbokud 
	    if ((max > 0 && max <= bookings) || (min > 0 && min > bookings) ){
	      _useInquiryForm = true;
	    }
	    */
		try {
			/** Not tested 100% here, but seems to work at other places... */
			if (isFullyBooked(iwc, product, _stamp) || isUnderBooked(iwc, product, _stamp)) {
				_useInquiryForm	= true;
			}
		} catch (CreateException e) {
			e.printStackTrace(System.err);
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
        isDay = this.getIsDayVisible(iwc);
//        isDay = getTravelStockroomBusiness(iwc).getIfDay(iwc, this._product, stamp);
      }catch (SQLException sql) {
        throw new FinderException(sql.getMessage());
      }

      ProductPrice[] prices = {};
      ProductPrice[] misc = {};
      Timeframe tFrame = getProductBusiness(iwc).getTimeframe(_product, _stamp, -1);
      int timeframeId = -1;
      if (tFrame != null) {
      	timeframeId = tFrame.getID();
        prices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(_service.getID(), timeframeId, -1, true);
        misc = ProductPriceBMPBean.getMiscellaneousPrices(_service.getID(), timeframeId, -1, true);
      }else {
        prices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(_service.getID(), -1, -1, true);
        misc = ProductPriceBMPBean.getMiscellaneousPrices(_service.getID(), -1, -1, true);
      }


      Text availSeats = (Text) theText.clone();
        availSeats.setText(iwrb.getLocalizedString("travel.there_is_availability","There is availability "));

      Text notAvailSeats = (Text) theText.clone();
        notAvailSeats.setText(iwrb.getLocalizedString("travel.there_is_no_availability","There is no availability "));

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
              fromText.setText(iwrb.getLocalizedString("travel.date_of_arrival","Date of arrival"));
          Text manyDaysText = (Text) theText.clone();
              manyDaysText.setText(iwrb.getLocalizedString("travel.number_of_nights","Number of days"));
          Text commentText = (Text) theText.clone();
              commentText.setText(iwrb.getLocalizedString("travel.comment","Comment"));


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
            fromDate.setDisabled(true);*/

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

          ++row;
          table.add(fromText, 1, row);
          table.add(new HiddenInput(parameterFromDate, _stamp.toSQLString()));
          Text currDate = (Text) theText.clone();
          currDate.setText(_stamp.getLocaleDate(iwc.getCurrentLocale()));
          table.add(currDate,  2, row);//fromDate, 2, row);
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

//	        Text count = (Text) super.theSmallBoldText.clone();
//	          count.setText(iwrb.getLocalizedString("travel.number_of_seats","Number of seats"));
//	        Text unitPrice = (Text) super.theSmallBoldText.clone();
//	          unitPrice.setText(iwrb.getLocalizedString("travel.price_per_seat","Price pr. seat"));
          Text count = (Text) super.theSmallBoldText.clone();
            count.setText(iwrb.getLocalizedString("travel.number_of_units","Number of units"));
          Text unitPrice = (Text) super.theSmallBoldText.clone();
            unitPrice.setText(iwrb.getLocalizedString("travel.unit_price","Unit price"));
          Text amount = (Text) super.theSmallBoldText.clone();
            amount.setText(iwrb.getLocalizedString("travel.total_price","Total price"));
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
                  int price = (int) getTravelStockroomBusiness(iwc).getPrice(pPrices[i].getID() ,_product.getID(),pPrices[i].getPriceCategoryID(),pPrices[i].getCurrencyId(),IWTimestamp.getTimestampRightNow(), timeframeId, -1);
    //              pPrices[i].getPrice();
                  pPriceCatNameText = (Text) theText.clone();
                    pPriceCatNameText.setText(category.getName());

                    if (pricesLength == 1) {
                      pPriceText = new ResultOutput("thePrice"+pPrices[i].getID(),Integer.toString(price));
                      pPriceText.setSize(8);
                      pPriceMany = new TextInput("priceCategory"+pPrices[i].getID() ,"1");
                      pPriceMany.setSize(5);
    	                		TotalPassTextInput.setContent("1");
    	                		TotalTextInput.setContent(Integer.toString(price));
                    } else {
                      pPriceText = new ResultOutput("thePrice"+pPrices[i].getID(),"0");
                      pPriceText.setSize(8);
                      pPriceMany = new TextInput("priceCategory"+pPrices[i].getID() ,"0");
                      pPriceMany.setSize(5);
                    }
                    

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
                          currentSum = (int) (currentCount * getTravelStockroomBusiness(iwc).getPrice(pPri.getID(), _productId,pPri.getPriceCategoryID(),pPri.getCurrencyId(),IWTimestamp.getTimestampRightNow(), timeframeId, -1));

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


          PickupPlaceHome hppHome = (PickupPlaceHome) IDOLookup.getHome(PickupPlace.class);
          Collection hotelPickup = hppHome.findHotelPickupPlaces(this._service);
          //HotelPickupPlace[] hotelPickup = (HotelPickupPlace[]) coll.toArray(new HotelPickupPlace[]{});
//          HotelPickupPlace[] hotelPickup = tsb.getHotelPickupPlaces(this._service);
          if (hotelPickup.size() > 0) {
              ++row;
              table.mergeCells(1,row,6,row);
              table.add(hr,1,row);
              ++row;
              table.mergeCells(1,row,6,row);
              subHeader = (Text) theBoldText.clone();
                subHeader.setFontColor(WHITE);
                subHeader.setText(iwrb.getLocalizedString("travel.booking_choose_pickup","If you choose to be picked up, select your preferred pickup place from the list below"));
              table.add(subHeader,1,row);
              table.setAlignment(1,row,"left");
              ++row;
              ++row;

				      DropdownMenu pickupMenu = null;
				      TextInput roomNumber = null;

              Text hotelText = (Text) theText.clone();
                hotelText.setText(iwrb.getLocalizedString("travel.pickup_sm","pickup"));
              pickupMenu = new DropdownMenu(hotelPickup, parameterPickupId);
                pickupMenu.addMenuElementFirst("-1",iwrb.getLocalizedString("travel.no_pickup","No pickup"));

              Text roomNumberText = (Text) theText.clone();
                roomNumberText.setText(iwrb.getLocalizedString("travel.extra_info","extra info"));
              roomNumber = new TextInput(parameterPickupInf);
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

  public Table getVerifyBookingTable(IWContext iwc, Product product) throws RemoteException, SQLException{
    String surname = iwc.getParameter("surname");
    String lastname = iwc.getParameter("lastname");
    String address = iwc.getParameter("address");
    String area_code = iwc.getParameter("area_code");
    String email = iwc.getParameter("e-mail");
    String telephoneNumber = iwc.getParameter("telephone_number");
    String city = iwc.getParameter("city");
    String country = iwc.getParameter("country");
    String comment = iwc.getParameter("comment");

    String fromDate = iwc.getParameter(parameterFromDate);
    String manyDays = iwc.getParameter(parameterManyDays);
    
    String pickupId = iwc.getParameter(parameterPickupId);
    String pickupInf = iwc.getParameter(parameterPickupInf);

    String ccNumber = iwc.getParameter(parameterCCNumber);
    String ccMonth = iwc.getParameter(parameterCCMonth);
    String ccYear = iwc.getParameter(parameterCCYear);

    String inquiry = iwc.getParameter(parameterInquiry);
    int productId = product.getID();

    boolean valid = true;
    String errorColor = "YELLOW";
    Text star = new Text(Text.NON_BREAKING_SPACE+"*");
      star.setFontColor(errorColor);

	  IWTimestamp fromStamp = new IWTimestamp(fromDate);

	  ProductPrice[] prices = {};
	  ProductPrice[] misc = {};
	  Timeframe tFrame = getProductBusiness(iwc).getTimeframe(_product, fromStamp, -1);
	  int timeframeId = -1;
	  if (tFrame != null) {
	  	timeframeId = tFrame.getID();
	    prices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(_service.getID(), timeframeId, -1, true);
	    misc = ProductPriceBMPBean.getMiscellaneousPrices(_service.getID(), timeframeId, -1, true);
	  }else {
	    prices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(_service.getID(), -1, -1, true);
	    misc = ProductPriceBMPBean.getMiscellaneousPrices(_service.getID(), -1, -1, true);
	  }
  
/*    ProductPrice[] pPrices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(this.product.getID(), true);
    ProductPrice[] prices = {};
    ProductPrice[] misc = {};
      prices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(product.getID(), -1, -1, true);
      misc = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getMiscellaneousPrices(product.getID(), -1, -1, true);
*/
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

			if (pickupId != null) {
				try {
					PickupPlace pickup = ((PickupPlaceHome) IDOLookup.getHome(PickupPlace.class)).findByPrimaryKey(new Integer(pickupId));
		      ++row;
		      table.setAlignment(1,row,"right");
		      table.setAlignment(2,row,"left");
		      table.add(getTextWhite(iwrb.getLocalizedString("travel.pickup","Pickup")),1,row);
		      table.add(getBoldTextWhite(pickup.getAddress().getStreetName()),2,row);
		      ++row;
		      table.setAlignment(1,row,"right");
		      table.setAlignment(2,row,"left");
		      table.add(getTextWhite(iwrb.getLocalizedString("travel.pickup_info","Pickup  info")),1,row);
		      table.add(getBoldTextWhite(pickupInf),2,row);
				} catch (FinderException e) {
					e.printStackTrace(System.err);
				}
				
			}

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
          if (i == 0)
          currency = ((com.idega.block.trade.data.CurrencyHome)com.idega.data.IDOLookup.getHomeLegacy(Currency.class)).findByPrimaryKeyLegacy(pPrices[i].getCurrencyId());
	      	price += current * getTravelStockroomBusiness(iwc).getPrice(pPrices[i].getID() ,productId,pPrices[i].getPriceCategoryID(), pPrices[i].getCurrencyId() ,IWTimestamp.getTimestampRightNow(), timeframeId, -1);
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
      table.add(getBoldTextWhite(df.format(price) + " "),2,row);
      if (currency != null) {
        table.add(getBoldTextWhite(currency.getCurrencyAbbreviation()),2,row);
      }
			if (price <= 0) {
				valid = false;
				table.add(star, 2, row);
			}


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

//      SubmitButton yes = new SubmitButton(iwrb.getImage("buttons/yes.gif"),this.sAction, this.parameterBookingVerified);
      SubmitButton yes = new SubmitButton(iwrb.getLocalizedString("yes","Yes"));
//        yes.setOnSubmit("this.form."+yes.getName()+".disabled = true");
//      table.add("[HotelBookingForm] adding "+super.sAction+" as "+PublicBooking.parameterBookingVerified, 2, row);
//        yes.setOnClick("this.form.submit()");
  //      yes.setOnClick("this.form."+yes.getName()+".disabled = true");

      Link no = new Link(iwrb.getImage("buttons/no.gif"),"#");
//          no.setAttribute("onClick","history.go(-1)");

      ++row;
      table.setAlignment(1,row,"left");
      table.setAlignment(2,row,"right");
      table.add(no,1,row);
      if (valid) {
//	      table.add(new HiddenInput(this.sAction, "Test jamms"),2,row);
	      table.add(new HiddenInput(this.sAction, PublicBooking.parameterBookingVerified),2,row);
	      table.add(new HiddenInput("Gimmi", "Test"),2,row);
        table.add(yes,2,row);
      }


    return table;
  }

  public int checkBooking(IWContext iwc, boolean saveBookingIfValid, boolean bookIfTooMany) throws Exception {
		return checkBooking(iwc, saveBookingIfValid, bookIfTooMany, false);
  }
  
  public int checkBooking(IWContext iwc, boolean saveBookingIfValid, boolean bookIfTooMany, boolean bookIfTooFew) throws Exception {
    boolean tooMany = false;


    int iMany = 0;

    String sBookingId = iwc.getParameter(this.parameterBookingId);
    int iBookingId = -1;
    String key = iwc.getParameter(this.parameterPriceCategoryKey);
		String count2Chk = iwc.getParameter(parameterCountToCheck);
		if (count2Chk != null) {
			try {
				iMany = Integer.parseInt(count2Chk);
			}catch (Exception e){}
		}

    String sOnline = iwc.getParameter(this.parameterOnlineBooking);
    boolean onlineOnly = false;
    if (sOnline != null && sOnline.equals("true")) {
      onlineOnly = true;
    }else if (sOnline != null && sOnline.equals("false")) {
      onlineOnly = false;
    }

    Timeframe tFrame = getProductBusiness(iwc).getTimeframe(_product, _stamp, -1);

    ProductPrice[] pPrices = {};
    if (tFrame == null) {
      pPrices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(_service.getID(), -1, -1, onlineOnly, key);
    }else {
      pPrices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(_service.getID(), tFrame.getID(), -1, onlineOnly, key);
    }
    
    if ( iMany == 0) {
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
    Hotel hotel = ((HotelHome) IDOLookup.getHome(Hotel.class)).findByPrimaryKey(_service.getPrimaryKey());
    String fromDate = iwc.getParameter(this.parameterFromDate);
    String manyDays = iwc.getParameter(this.parameterManyDays);
    IWTimestamp fromStamp = null;
    IWTimestamp toStamp = null;
//    int betw = 1;
//    int heildarbokanir = 0;
    int bookingTotal = 0;
		int iManyDays = 1;
		try {
			iManyDays = Integer.parseInt(manyDays);
		}catch (NumberFormatException n) {}
  
    try {
      fromStamp = new IWTimestamp(fromDate);
//      heildarbokanir = getHotelBooker(iwc).getNumberOfReservedRooms(product.getID(), stamp, null);
//    	heildarbokanir = getHotelBooker(iwc).getNumberOfReservedRooms(serviceId, fromStamp, null);
//			heildarbokanir = getHotelBooker(iwc).getBookingsTotalCount(serviceId, fromStamp);
			if (_booking != null) {
				bookingTotal = _booking.getTotalCount();
//				heildarbokanir -= bookingTotal;	
			}

//      if (iManyDays < 1) betw = 1;
//      else betw = iManyDays;
    }catch (Exception e) {
    	e.printStackTrace(System.err);
    }

		int totalRooms = 0;
		
		int maxPerRoom = hotel.getMaxPerUnit();
		
		
//    iMany;
		if (maxPerRoom > 0) {
			if (iMany > maxPerRoom) {
			  tooMany = true;
			  errorDays.add(fromStamp);
			}
		}

		//System.out.println("[HotelBookingForm] tooMany = "+tooMany+", iMany = "+iMany+", bookingTotal = "+bookingTotal+", maxPerRoom = "+maxPerRoom);
		if (!tooMany) {
	    int iAvailable;
//	    if (totalRooms > 0) {
	    	for ( int j = 0; j < iManyDays; j++) {
	    		if (j != 0) {
						fromStamp.addDays(1);	    			
	    		}

					if (_reseller != null) {
						Contract cont = super.getContractBusiness(iwc).getContract(_reseller, _product);
						if (cont != null) {
							totalRooms = cont.getAlotment();
							if (totalRooms < 1) {
								totalRooms = hotel.getNumberOfUnits();
								if (totalRooms < 1) {
									ServiceDayHome sDayHome = (ServiceDayHome) IDOLookup.getHome(ServiceDay.class);
									ServiceDay sDay;// = sDayHome.create();
									sDay = sDayHome.findByServiceAndDay(_product.getID() , fromStamp.getDayOfWeek());
				  
									if (sDay != null) {
										totalRooms = sDay.getMax();
									}
								}
							}
						}	
					}
					else {
						totalRooms = hotel.getNumberOfUnits();
						if (totalRooms < 1) {
							ServiceDayHome sDayHome = (ServiceDayHome) IDOLookup.getHome(ServiceDay.class);
							ServiceDay sDay;// = sDayHome.create();
							sDay = sDayHome.findByServiceAndDay(_product.getID() , fromStamp.getDayOfWeek());
			  
							if (sDay != null) {
								totalRooms = sDay.getMax();
							}
						}
					}

					if (totalRooms > 0) {
		    		//		    iAvailable = totalRooms - heildarbokanir;
				    iAvailable = totalRooms + bookingTotal - getBooker(iwc).getGeneralBookingHome().getBookingsTotalCount(( (Integer) _service.getPrimaryKey()).intValue(), fromStamp, null, -1, new int[]{}, null );
	
						//System.out.println("[HotelBookingform] date : "+fromStamp+", totalRooms : "+totalRooms+", iAvailable = "+iAvailable+" ("+totalRooms+" + "+bookingTotal+" - "+getBooker(iwc).getGeneralBookingHome().getBookingsTotalCount(( (Integer) _service.getPrimaryKey()).intValue(), fromStamp, null, -1, new int[]{}, null )+")");
	
						//System.out.println("iAvail = totalRooms + bookingTotal - heildarbokanir ....."+iAvailable+" = "+totalRooms+" + " +bookingTotal+ " - "+getBooker(iwc).getGeneralBookingHome().getBookingsTotalCount(( (Integer) _service.getPrimaryKey()).intValue(), fromStamp, null, -1, new int[]{}, null ));
				    if (iMany > iAvailable) {
		//			  if (iAvailable <= 0 ) {
				    	tooMany = true;
				    	errorDays.add(new IWTimestamp(fromStamp));
				    }
					}
	    	}
		    
//	    }
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



	private HotelBooker getHotelBooker(IWApplicationContext iwac) throws RemoteException {
    return (HotelBooker) IBOLookup.getServiceInstance(iwac, HotelBooker.class);
	}

  private HotelBusiness getHotelBusiness(IWContext iwc) throws RemoteException{
    return (HotelBusiness) IBOLookup.getServiceInstance(iwc, HotelBusiness.class);
  }
  
  private HotelHome getHotelHome() throws RemoteException {
  	return (HotelHome) IDOLookup.getHome(Hotel.class);	
  }

	public boolean isFullyBooked(IWContext iwc, Product product, IWTimestamp stamp) throws RemoteException, CreateException, FinderException {
		Hotel hotel = getHotelHome().findByPrimaryKey( product.getPrimaryKey() );
		
		int max = 0;
		
		if (_reseller != null) {
			Contract cont = super.getContractBusiness(iwc).getContract(_reseller, _product);
			if (cont != null) {
				max = cont.getAlotment();
			}	
		}else { //if (supplier != null) {
			max = hotel.getNumberOfUnits();
			if (max < 1) {
				ServiceDayHome sDayHome = (ServiceDayHome) IDOLookup.getHome(ServiceDay.class);
				ServiceDay sDay;// = sDayHome.create();
				try {
					sDay = sDayHome.findByServiceAndDay(product.getID() , stamp.getDayOfWeek());
					if (sDay != null) {
						max = sDay.getMax();
					}
				}catch (Exception e) {
					logDebug("No serviceDay found for product: "+product.getID());
				}
		  
			}
		}
	
		if (max > 0) {
	    List addresses;
	    try {
	      addresses = product.getDepartureAddresses(false);
	    }catch (IDOFinderException ido) {
	      ido.printStackTrace(System.err);
	      addresses = new Vector();
	    }
	    
	    int addressId = super.getAddressIDToUse(iwc, addresses);
			int currentBookings = getHotelBooker(iwc).getBookingsTotalCount(product.getID(), stamp, addressId);
			return (currentBookings >= max);
		}
		
		return false;
	}

	public void saveServiceBooking(
		IWContext iwc,
		int bookingId,
		IWTimestamp stamp)
		throws RemoteException, IDOException {

	}
	
	public String getPriceCategorySearchKey() {
		return HotelSetup.HOTEL_SEARCH_PRICE_CATEGORY_KEY;
	}	

}
