package is.idega.idegaweb.travel.presentation;

import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.idegaweb.*;
import com.idega.util.*;
import com.idega.util.text.*;
import is.idega.idegaweb.travel.business.TravelStockroomBusiness;
import java.text.DecimalFormat;
import java.util.*;
import com.idega.block.calendar.business.CalendarBusiness;
import com.idega.core.data.Address;
import com.idega.block.trade.stockroom.data.*;
import com.idega.block.trade.stockroom.business.*;
import is.idega.idegaweb.travel.data.*;
import com.idega.block.trade.data.Currency;

import is.idega.idegaweb.travel.service.tour.presentation.TourBookingForm;

import com.idega.block.trade.stockroom.business.ProductPriceException;
import java.sql.SQLException;
/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class PublicBooking extends Block  {

  public static String IW_BUNDLE_IDENTIFIER="is.idega.travel";
  IWResourceBundle iwrb;
  IWBundle bundle;
  Product product;
  Service service;
  Timeframe[] timeframes;
  Supplier supplier;
  int productId = -1;

  private idegaTimestamp stamp;
  private String parameterProductId = LinkGenerator.parameterProductId;
  private DecimalFormat df = new DecimalFormat("0.00");
  private Text text = new Text("");
  private Text boldText = new Text("");
  private String backgroundColor = "#1A4B8E";

  private String sAction = "publicBookingAction";
  private String parameterSubmitBooking = "publicBookingSubmitBooking";
  private String parameterBookingVerified = "publicBookingBookingVerified";


  public PublicBooking() {
  }

  public void main(IWContext iwc)throws Exception {
    init(iwc);

    if (productId != -1 ) {
      displayForm(iwc);
    }else {
    }
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  private void init(IWContext iwc) {
    bundle = getBundle(iwc);
    iwrb = bundle.getResourceBundle(iwc.getCurrentLocale());

    String year = iwc.getParameter(CalendarBusiness.PARAMETER_YEAR);
    String month = iwc.getParameter(CalendarBusiness.PARAMETER_MONTH);
    String day = iwc.getParameter(CalendarBusiness.PARAMETER_DAY);
    if (year != null && month != null && day != null) {
      stamp = new idegaTimestamp(Integer.parseInt(day), Integer.parseInt(month), Integer.parseInt(year));
    }else {
      stamp = idegaTimestamp.RightNow();
    }


    String sProductId = iwc.getParameter(this.parameterProductId);
    if (sProductId != null) {
      try {

        productId = Integer.parseInt(sProductId);
        product = new Product(productId);
        if (!product.getIsValid()) {
          throw new SQLException("Product not valid");
        }
        service = new Service(productId);
        timeframes = product.getTimeframes();
        supplier = new Supplier(product.getSupplierId());

      }catch (SQLException s) {
        s.printStackTrace(System.err);
      }
    }
    boldText.setFontStyle("font-face: Verdana, Helvetica, sans-serif; font-size: "+Text.FONT_SIZE_10_STYLE_TAG+"; font-weight: bold;");
      boldText.setFontColor("#000000");
    text.setFontStyle("font-face: Verdana, Helvetica, sans-serif; font-size: "+Text.FONT_SIZE_10_STYLE_TAG+";");
      text.setFontColor("#000000");
  }

  private Text getText(String content) {
    Text temp = (Text) text.clone();
      temp.setText(content);
    return temp;
  }
  private Text getBoldText(String content) {
    Text temp = (Text) boldText.clone();
      temp.setText(content);
    return temp;
  }
  private Text getTextWhite(String content) {
    Text temp = (Text) text.clone();
      temp.setText(content);
      temp.setFontColor("#FFFFFF");
    return temp;
  }
  private Text getBoldTextWhite(String content) {
    Text temp = (Text) boldText.clone();
      temp.setText(content);
      temp.setFontColor("#FFFFFF");
    return temp;
  }


  private void displayForm(IWContext iwc) {
      Table table = new Table(2,4);
        table.setWidth("90%");
        table.setAlignment("center");
        table.setCellspacing(1);
        table.setColor(TravelManager.WHITE);
        table.setBorder(0);

      Image background = bundle.getImage("images/sb_background.gif");
      Image seeAndBuy = iwrb.getImage("images/see_and_buy.gif");

      table.setBackgroundImage(1, 3, background);
      table.setColor(1, 4, backgroundColor);
      table.setColor(2, 2, "#CCCCCC");
      table.setColor(2, 3, "#CCCCCC");
      table.setColor(2, 4, "#CCCCCC");
      table.setAlignment(1,1,"left");
      table.setVerticalAlignment(1,2,"top");
      table.setVerticalAlignment(1,3,"top");
      table.setVerticalAlignment(2,2,"top");
      table.setVerticalAlignment(2,3,"top");
      table.setAlignment(2,2,"center");
      table.setAlignment(2,3,"center");
      table.setHeight(2, "20");
      table.setHeight(4, "20");
      table.setWidth(2, "20");


      //table.add(seeAndBuy,1,1);
      table.add(leftTop(iwc),1,2);
      table.add(leftBottom(iwc),1,3);
      table.add(rightTop(iwc),2,2);
      table.add(rightBottom(iwc),2,3);

      add(table);
  }


  private Table rightTop(IWContext iwc) {
    Table table = new Table(1,3);
      table.setVerticalAlignment(1,1,"top");
      table.setVerticalAlignment(1,2,"top");
      table.setVerticalAlignment(1,3,"top");
      table.setAlignment(1,1,"center");
      table.setAlignment(1,2,"center");
      table.setAlignment(1,3,"center");

    Image arrow = bundle.getImage("images/white_arrow.gif");
    Image bookNow = iwrb.getImage("images/day_requested.gif");
    Text checkAvail = getBoldText(iwrb.getLocalizedString("travel.check_availability","Check availability and select date by the calendar below"));

    table.add(bookNow,1,1);
    table.add(checkAvail,1,2);
    table.add(arrow,1,3);

    return table;
  }

  private Table rightBottom(IWContext iwc)  {
    Table table = new Table(1,1);
      table.setAlignment(1,1,"center");
      table.setVerticalAlignment(1,1,"top");

    try {
      CalendarHandler ch = new CalendarHandler(iwc);
        ch.setProduct(product);
        ch.setBackgroundColor("#CCCCCC");
        ch.setInActiveCellColor("#666666");
        ch.setFontColor("#000000");
        ch.setTodayColor("#99CCFF");
        ch.setFullyBookedColor("#CC3333");
        ch.setAvailableDayColor("#FFFFFF");
        ch.addParameterToLink(this.parameterProductId, productId);
        ch.setClassToLinkTo(PublicBooking.class);
        ch.setTimestamp(stamp);
        ch.showInquiries(false);
        ch.sm.T.setBorder(0);
        ch.sm.T.setCellspacing(2);
      table.add(ch.getCalendarTable(iwc),1,1);
    }catch (Exception e) {
      e.printStackTrace(System.err);
    }

    return table;
  }

  private Form leftTop(IWContext iwc) {
    Image background = bundle.getImage("images/sb_background.gif");

    Form form = new Form();
    Table aroundTable = new Table(2,2);
      aroundTable.setWidth("100%");
      aroundTable.setHeight("100%");
      aroundTable.setCellpadding(0);
      aroundTable.setCellspacing(0);
      aroundTable.setBackgroundImage(1,1,background);
      aroundTable.setBackgroundImage(2,1,background);
      aroundTable.setBackgroundImage(1,2,background);
      aroundTable.setWidth(1,"1");
      aroundTable.setHeight(1,"1");
      aroundTable.setBorder(0);


    Table table = new Table();
      aroundTable.add(table,2,2);
    form.add(aroundTable);



    try {
//      ServiceOverview so = new ServiceOverview(iwc);
//        form.add(so.getProductInfoTable(iwc, iwrb, product));

      table.setWidth("100%");
      table.setHeight("100%");
      table.setAlignment("center");
      table.setBorder(0);

      idegaTimestamp depTimeStamp = new idegaTimestamp(service.getDepartureTime());
      Address[] depAddresses = ProductBusiness.getDepartureAddresses(product);
      Address depAddress = ProductBusiness.getDepartureAddress(product);
      Currency currency;

      Text nameText = getText(iwrb.getLocalizedString("travel.name","Name"));
      Text timeframeText = getText(iwrb.getLocalizedString("travel.timeframe","Timeframe"));
      Text supplierText = getText(iwrb.getLocalizedString("travel.supplier","Supplier"));
      Text departureFromText = getText(iwrb.getLocalizedString("travel.departure_from","Departure from"));
      Text departureTimeText = getText(iwrb.getLocalizedString("travel.departure_time","Departure time"));
      Text pricesText = getText(iwrb.getLocalizedString("travel.prices","Prices"));
      Image image = TravelManager.getDefaultImage(iwrb);
      if (product.getFileId() != -1) {
        image = new Image(product.getFileId());
      }
      image.setMaxImageWidth(138);

      Image arrow = bundle.getImage("images/black_arrow.gif");
        arrow.setAlignment("center");

      Text space = getText(" : ");


      Text nameTextBold = getBoldText("");
      Text supplierTextBold = getBoldText("");
      Text departureFromTextBold = getBoldText("");
      Text departureTimeTextBold = getBoldText("");
      Text pricesTextBold = getBoldText("");
      Text nameOfCategory = getBoldText("");
      Text priceText = getBoldText("");
      Text currencyText = getBoldText("");

      nameTextBold.setText(ProductBusiness.getProductName(product));
      supplierTextBold.setText(supplier.getName());
      departureFromTextBold.setText(depAddress.getStreetName());
      departureTimeTextBold.setText(TextSoap.addZero(depTimeStamp.getHour())+":"+TextSoap.addZero(depTimeStamp.getMinute()));


      table.add(nameText,1,1);
      table.add(space,1,1);
      table.add(nameTextBold,1,1);


      table.add(supplierText,1,2);
      table.add(space,1,2);
      table.add(supplierTextBold,1,2);

/*      table.add(departureFromText,3,1);
      table.add(space,3,1);
      table.add(departureFromTextBold,3,1);
*/
      table.add(image,1,3);
//      table.add(arrow,1,3);
      table.setAlignment(1,3,"left");

      table.add(departureTimeText,2,2);
      table.add(space,2,2);
      table.add(departureTimeTextBold,2,2);

      table.add(Text.NON_BREAKING_SPACE,2,3);

      String stampTxt1 = iwrb.getLocalizedString("travel.not_configured","Not configured");
      String stampTxt2 = iwrb.getLocalizedString("travel.not_configured","Not configured");
      ProductPrice[] prices;// = ProductPrice.getProductPrices(service.getID(), true);
      Text timeframeTextBold;

      Table pTable = new Table();
        pTable.setCellspacing(0);

        //pTable.add(pricesText,1,1);
        int pRow = 1;
      for (int l = 0; l < depAddresses.length; l++) {
        departureFromTextBold = getBoldText(depAddresses[l].getStreetName());
          departureFromTextBold.addToText(Text.NON_BREAKING_SPACE+Text.NON_BREAKING_SPACE);
        pTable.add(departureFromTextBold, 1, pRow);
        for (int i = 0; i < timeframes.length; i++) {
          prices = ProductPrice.getProductPrices(product.getID(), timeframes[i].getID(), depAddresses[l].getID(), true);
          stampTxt1 = new idegaTimestamp(timeframes[i].getFrom()).getLocaleDate(iwc);
          stampTxt2 = new idegaTimestamp(timeframes[i].getTo()).getLocaleDate(iwc);
          if (timeframes[i].getIfYearly()) {
            try {
              stampTxt1 = stampTxt1.substring(0, stampTxt1.length()-4);
              stampTxt2 = stampTxt2.substring(0, stampTxt2.length()-4);
            }catch (NumberFormatException n) {}
          }
          timeframeTextBold = getText("");
            timeframeTextBold.setText(stampTxt1+" - "+stampTxt2+Text.NON_BREAKING_SPACE+Text.NON_BREAKING_SPACE);
          pTable.add(timeframeTextBold,2,pRow);

          if (prices.length == 0) {
            ++pRow;
          }
          for (int j = 0; j < prices.length; j++) {
            currency = new Currency(prices[j].getCurrencyId());
            nameOfCategory = getText(prices[j].getPriceCategory().getName());
              nameOfCategory.addToText(Text.NON_BREAKING_SPACE+":"+Text.NON_BREAKING_SPACE+Text.NON_BREAKING_SPACE);
            try {
              priceText = getBoldText(df.format(TravelStockroomBusiness.getPrice(prices[j].getID(), service.getID(),prices[j].getPriceCategoryID() , prices[j].getCurrencyId(), idegaTimestamp.getTimestampRightNow(), timeframes[i].getID(), depAddresses[l].getID()) ) );
              currencyText = getBoldText(currency.getCurrencyAbbreviation());
              pTable.add(currencyText,5,pRow);
            }catch (ProductPriceException p) {
              priceText.setText("T - rangt upp sett");
            }

            pTable.add(nameOfCategory,3,pRow);
            pTable.add(priceText,4,pRow);
            ++pRow;
          }

        }
      }

      pTable.setColumnAlignment(1,"left");
      pTable.setColumnAlignment(2,"left");
      pTable.setColumnAlignment(3,"left");
      pTable.setColumnAlignment(4,"right");
      pTable.setColumnAlignment(5,"left");
      pTable.setHorizontalZebraColored("#FFFFFF","#F1F1F1");

      table.add(pTable,2,3);
//      table.setAlignment(2,4,"right");

      table.setAlignment(2,1,"right");
      table.setAlignment(2,2,"right");
      table.setAlignment(2,3,"right");
      table.setVerticalAlignment(1,3,"top");
      table.mergeCells(1,1,2,1);
//      table.mergeCells(1,2,2,2);
//      table.mergeCells(1,3,1,5);
      table.mergeCells(2,3,2,4);
//      table.setWidth(1,"138");
//      table.setWidth(3,"350");
//      table.setWidth(2,"350");
//        table.setBorder(1);


    }catch (Exception e) {
      e.printStackTrace(System.err);
    }
    return form;
  }



  private Form leftBottom(IWContext iwc) {
    try {
      TourBookingForm tbf = new TourBookingForm(iwc, product);
      CalendarHandler ch  = new CalendarHandler(iwc);
        ch.setProduct(product);

      boolean legalDay = false;
      for (int i = 0; i < timeframes.length; i++) {
        if (stamp.isLaterThanOrEquals(idegaTimestamp.RightNow()) && idegaTimestamp.isInTimeframe(new idegaTimestamp(timeframes[i].getFrom()), new idegaTimestamp(timeframes[i].getTo()), stamp, timeframes[i].getIfYearly())) {
          legalDay = true;
          break;
        }
      }


      Form form = new Form();

      if (legalDay) {
        String action = iwc.getParameter(this.sAction);

        String tbfAction = iwc.getParameter(TourBookingForm.sAction);
        if (tbfAction == null || !tbfAction.equals(TourBookingForm.parameterSaveBooking)) {
          action = "";
        }

        if (action == null || action.equals("")) {
            form = tbf.getPublicBookingForm(iwc, product, stamp);
            form.maintainParameter(this.parameterProductId);
            form.addParameter(this.sAction,this.parameterSubmitBooking);
        }else if (action.equals(this.parameterSubmitBooking)) {
            form = tbf.getFormMaintainingAllParameters();
            form.maintainParameter(this.parameterProductId);
            form.add(getVerifyBookingTable(iwc));
        }else if (action.equals(this.parameterBookingVerified)) {
            form = tbf.getFormMaintainingAllParameters();
            form.maintainParameter(this.parameterProductId);
            form.add(doBooking(iwc));
        }
      }else {
          form.add(getNoSeatsAvailable(iwc));
      }

      return form;
    }catch (Exception e) {
      e.printStackTrace(System.err);
      return new Form();
    }
  }


  public Table getNoSeatsAvailable(IWContext iwc) {
    Table table = new Table();
      table.setCellpadding(0);
      table.setCellspacing(6);

          Text notAvailSeats = new Text();
            notAvailSeats.setFontStyle(TravelManager.theTextStyle);
            notAvailSeats.setFontColor(TravelManager.WHITE);
            notAvailSeats.setText(iwrb.getLocalizedString("travel.there_are_no_available_seats ","There are no available seats "));

          Text dateText = new Text();
            dateText.setFontStyle(TravelManager.theBoldTextStyle);
            dateText.setFontColor(TravelManager.WHITE);
            dateText.setText(stamp.getLocaleDate(iwc));
            dateText.addToText("."+Text.NON_BREAKING_SPACE);

          Text pleaseFindAnotherDay = new Text();
            pleaseFindAnotherDay.setFontStyle(TravelManager.theTextStyle);
            pleaseFindAnotherDay.setFontColor(TravelManager.WHITE);
            pleaseFindAnotherDay.setText(iwrb.getLocalizedString("travel.please_find_another_day","Please find another day"));

      table.add(notAvailSeats);
      table.add(dateText);
      table.add(pleaseFindAnotherDay);

    return table;
  }

  private Table getVerifyBookingTable(IWContext iwc) throws SQLException{
    String surname = iwc.getParameter("surname");
    String lastname = iwc.getParameter("lastname");
    String address = iwc.getParameter("address");
    String area_code = iwc.getParameter("area_code");
    String email = iwc.getParameter("e-mail");
    String telephoneNumber = iwc.getParameter("telephone_number");
    String city = iwc.getParameter("city");
    String country = iwc.getParameter("country");
    String hotelPickupPlaceId = iwc.getParameter(HotelPickupPlace.getHotelPickupPlaceTableName());
    String room_number = iwc.getParameter("room_number");
    String depAddressId = iwc.getParameter(TourBookingForm.parameterDepartureAddressId);

//    ProductPrice[] pPrices = ProductPrice.getProductPrices(this.product.getID(), true);
    ProductPrice[] pPrices = {};
    Timeframe tFrame = ProductBusiness.getTimeframe(this.product, stamp);
    if (tFrame != null && depAddressId != null) {
      pPrices = ProductPrice.getProductPrices(product.getID(), tFrame.getID(), Integer.parseInt(depAddressId), true);
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
      table.add(getBoldTextWhite(ProductBusiness.getProductName(product)),2,row);

      ++row;
      table.setAlignment(1,row,"right");
      table.setAlignment(2,row,"left");
      table.add(getTextWhite(iwrb.getLocalizedString("travel.date","Date")),1,row);
      table.add(getBoldTextWhite(this.stamp.getLocaleDate(iwc)),2,row);

      ++row;
      table.setAlignment(1,row,"right");
      table.setAlignment(2,row,"left");
      table.add(getTextWhite(iwrb.getLocalizedString("travel.departure_place","Departure place")),1,row);
      table.add(getBoldTextWhite(new Address(Integer.parseInt(depAddressId)).getStreetName()),2,row);

      ++row;
      table.setAlignment(1,row,"right");
      table.setAlignment(2,row,"left");
      table.add(getTextWhite(iwrb.getLocalizedString("travel.name","Name")),1,row);
      table.add(getBoldTextWhite(surname+" "+lastname),2,row);

      ++row;
      table.setAlignment(1,row,"right");
      table.setAlignment(2,row,"left");
      table.add(getTextWhite(iwrb.getLocalizedString("travel.address","Address")),1,row);
      table.add(getBoldTextWhite(address),2,row);

      ++row;
      table.setAlignment(1,row,"right");
      table.setAlignment(2,row,"left");
      table.add(getTextWhite(iwrb.getLocalizedString("travel.area_code","Area code")),1,row);
      table.add(getBoldTextWhite(area_code),2,row);

      ++row;
      table.setAlignment(1,row,"right");
      table.setAlignment(2,row,"left");
      table.add(getTextWhite(iwrb.getLocalizedString("travel.city","City")),1,row);
      table.add(getBoldTextWhite(city),2,row);

      ++row;
      table.setAlignment(1,row,"right");
      table.setAlignment(2,row,"left");
      table.add(getTextWhite(iwrb.getLocalizedString("travel.country","Country")),1,row);
      table.add(getBoldTextWhite(country),2,row);

      ++row;
      table.setAlignment(1,row,"right");
      table.setAlignment(2,row,"left");
      table.add(getTextWhite(iwrb.getLocalizedString("travel.email","E-mail")),1,row);
      table.add(getBoldTextWhite(email),2,row);

      ++row;
      table.setAlignment(1,row,"right");
      table.setAlignment(2,row,"left");
      table.add(getTextWhite(iwrb.getLocalizedString("travel.telephone_number","Telephone number")),1,row);
      table.add(getBoldTextWhite(telephoneNumber),2,row);

      ++row;

      float price = 0;
      int total = 0;
      int current = 0;
      Currency currency = null;

      for (int i = 0; i < pPrices.length; i++) {
        ++row;
        table.setAlignment(1,row,"right");
        table.setAlignment(2,row,"left");

        try {
          current = Integer.parseInt(iwc.getParameter("priceCategory"+i));
        }catch (NumberFormatException n) {
          current = 0;
        }

        total += current;
        try {
          if (i == 0)
          currency = new Currency(pPrices[i].getCurrencyId());
          price += current * TravelStockroomBusiness.getPrice(pPrices[i].getID() ,this.productId,pPrices[i].getPriceCategoryID(), pPrices[i].getCurrencyId() ,idegaTimestamp.getTimestampRightNow(), tFrame.getID(), Integer.parseInt(depAddressId));
        }catch (SQLException sql) {}

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
      table.add(getBoldTextWhite(this.df.format(price) + " "),2,row);
      if (currency != null)
      table.add(getBoldTextWhite(currency.getCurrencyAbbreviation()),2,row);

      SubmitButton yes = new SubmitButton(iwrb.getImage("buttons/yes.gif"),this.sAction, this.parameterBookingVerified);
      Link no = new Link(iwrb.getImage("buttons/no.gif"),"#");
          no.setAttribute("onClick","history.go(-1)");

//        return backLink;

  //    SubmitButton no = new SubmitButton(iwrb.getImage("buttons/no.gif"),this.sAction, "");

      ++row;
//      table.setWidth(1,"20%");
      table.setAlignment(1,row,"left");
      table.setAlignment(2,row,"right");
      table.add(no,1,row);
      table.add(yes,2,row);



    return table;
  }

  private Table doBooking(IWContext iwc) {
    Table table = new Table();
      String ccNumber = iwc.getParameter(TourBookingForm.parameterCCNumber);
      String ccMonth  = iwc.getParameter(TourBookingForm.parameterCCMonth);
      String ccYear   = iwc.getParameter(TourBookingForm.parameterCCYear);
      String depAddr  = iwc.getParameter(TourBookingForm.parameterDepartureAddressId);

      Text display = getBoldTextWhite("");
      boolean success = false;
      String heimild = "";

      try {
        float price = 0;
        int total = 0;
        int current = 0;

        //ProductPrice[] pPrices = ProductPrice.getProductPrices(this.product.getID(), true);
        ProductPrice[] pPrices = {};
        Timeframe tFrame = ProductBusiness.getTimeframe(this.product, stamp);
        if (tFrame != null) {
          pPrices = ProductPrice.getProductPrices(product.getID(), tFrame.getID(), Integer.parseInt(depAddr), true);
        }

        for (int i = 0; i < pPrices.length; i++) {
          try {
            current = Integer.parseInt(iwc.getParameter("priceCategory"+i));
          }catch (NumberFormatException n) {
            current = 0;
          }

          total += current;
          if (i == 0)
          price += current * TravelStockroomBusiness.getPrice(pPrices[i].getID() ,this.productId,pPrices[i].getPriceCategoryID(), pPrices[i].getCurrencyId() ,idegaTimestamp.getTimestampRightNow(), tFrame.getID(), Integer.parseInt(depAddr));

        }

        System.out.println("Starting TPOS test : "+idegaTimestamp.RightNow().toString());
        com.idega.block.tpos.business.TPosClient t = new com.idega.block.tpos.business.TPosClient(iwc);
        System.err.println("price : "+price);
        heimild = t.doSale(ccNumber,ccMonth,ccYear,price,"ISK");
        //System.out.println("heimild = " + heimild);
        System.out.println("Ending TPOS test : "+idegaTimestamp.RightNow().toString());
        success = true;
      }
      catch(com.idega.block.tpos.business.TPosException e) {
        System.out.println("TPOS errormessage = " + e.getErrorMessage());
        System.out.println("number = " + e.getErrorNumber());
        System.out.println("display = " + e.getDisplayError());
        //display.setText(e.getDisplayError());
        display.setText(iwrb.getLocalizedString("travel.creditcard_autorization_failed","Authorization failed"));
      }
      catch (Exception e) {
        e.printStackTrace(System.err);
      }

      if (success) {
        try {
          int bookingId = -1;
          TourBookingForm tbf = new TourBookingForm(iwc,product);
          bookingId = tbf.handleInsert(iwc);

          GeneralBooking gBooking = new GeneralBooking(bookingId);

          table.add(getBoldTextWhite(gBooking.getName()));
          table.add(getBoldTextWhite(", "));
          table.add(getBoldTextWhite(iwrb.getLocalizedString("travel.you_booking_has_been_confirmed","your booking has been confirmed.")));
          table.add(Text.BREAK);
          table.add(Text.BREAK);
          table.add(getBoldTextWhite(iwrb.getLocalizedString("travel.your_credidcard_authorization_number_is","Your creditcard authorization number is")));
          table.add(getBoldTextWhite(" : "));
          table.add(getBoldTextWhite(heimild));
          table.add(Text.BREAK);
          table.add(getBoldTextWhite(iwrb.getLocalizedString("travel.your_reference_number_is","Your reference number is")));
          table.add(getBoldTextWhite(" : "));
          table.add(getBoldTextWhite(gBooking.getReferenceNumber()));
          table.add(Text.BREAK);
          //table.add(getBoldTextWhite(gBooking.getReferenceNumber()));
          //table.add(Text.BREAK);
          table.add(Text.BREAK);
          table.add(getBoldTextWhite(iwrb.getLocalizedString("travel.if_unable_to_print","If you are unable to print the voucher, write the reference number down else proceed to printing the voucher.")));

          Link printVoucher = new Link(getBoldTextWhite(iwrb.getLocalizedString("travel.print_voucher","Print voucher")));
            printVoucher.addParameter(VoucherWindow.parameterBookingId, bookingId);
            printVoucher.setWindowToOpen(VoucherWindow.class);

          table.add(printVoucher,1,2);
          table.setAlignment(1,1,"left");
          table.setAlignment(1,2,"right");
        }catch (Exception e) {
          table.add(getBoldTextWhite(iwrb.getLocalizedString("travel.booking_not_successful_try_again_later","Booking not successful. Please try again later.")));
          e.printStackTrace(System.err);
        }

      }else {
        table.add(display);
      }

    return table;
  }

}