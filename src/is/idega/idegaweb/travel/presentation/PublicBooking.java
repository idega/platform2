package is.idega.idegaweb.travel.presentation;

import com.idega.block.tpos.data.TPosMerchant;
import javax.ejb.FinderException;
import java.rmi.RemoteException;
import com.idega.business.IBOLookup;
import javax.mail.MessagingException;
import com.idega.core.data.Email;
import com.idega.data.IDOLookup;
import com.idega.transaction.IdegaTransactionManager;
import javax.transaction.TransactionManager;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.idegaweb.*;
import com.idega.util.*;
import com.idega.util.text.*;
import is.idega.idegaweb.travel.business.*;
import java.text.DecimalFormat;
import java.util.*;
import com.idega.block.calendar.business.CalendarBusiness;
import com.idega.core.data.Address;
import com.idega.block.trade.stockroom.data.*;
import com.idega.block.trade.stockroom.business.*;
import is.idega.idegaweb.travel.data.*;
import com.idega.block.trade.data.Currency;

import is.idega.idegaweb.travel.service.tour.presentation.TourBookingForm;
import is.idega.idegaweb.travel.service.presentation.BookingForm;
import is.idega.idegaweb.travel.service.business.ServiceHandler;

import com.idega.block.trade.stockroom.business.ProductPriceException;
import com.idega.block.tpos.presentation.*;
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

  private IWTimestamp stamp;
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

  private void init(IWContext iwc) throws RemoteException{
    bundle = getBundle(iwc);
    iwrb = bundle.getResourceBundle(iwc.getCurrentLocale());
    super.getParentPage().setExpiryDate("Tue, 20 Aug 1996 14:25:27 GMT");
    iwc.getResponse().addHeader("Expires","Tue, 20 Aug 1996 14:25:27 GMT");

    String year = iwc.getParameter(CalendarBusiness.PARAMETER_YEAR);
    String month = iwc.getParameter(CalendarBusiness.PARAMETER_MONTH);
    String day = iwc.getParameter(CalendarBusiness.PARAMETER_DAY);
    if (year != null && month != null && day != null) {
      stamp = new IWTimestamp(Integer.parseInt(day), Integer.parseInt(month), Integer.parseInt(year));
    }else {
      stamp = IWTimestamp.RightNow();
    }


    String sProductId = iwc.getParameter(this.parameterProductId);
    if (sProductId != null) {
      try {

        productId = Integer.parseInt(sProductId);
        product = getProductBusiness(iwc).getProduct(productId);
        if (!product.getIsValid()) {
          throw new SQLException("Product not valid");
        }
        service = ((is.idega.idegaweb.travel.data.ServiceHome)com.idega.data.IDOLookup.getHome(Service.class)).findByPrimaryKey(new Integer(productId));
        timeframes = product.getTimeframes();
        supplier = ((com.idega.block.trade.stockroom.data.SupplierHome)com.idega.data.IDOLookup.getHomeLegacy(Supplier.class)).findByPrimaryKeyLegacy(product.getSupplierId());

      }catch (SQLException s) {
        s.printStackTrace(System.err);
      }catch (FinderException f) {
        f.printStackTrace(System.err);
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
      Image image = bundle.getImage("verisignseals/verisign_logo.gif");
        image.setWidth(100);
        image.setHeight(42);
      Link verisign = new Link(image, "https://digitalid.verisign.com/as2/3537e1357d56f9db899a65d84e97d2c9");
        verisign.setTarget(Link.TARGET_NEW_WINDOW);
      table.add(verisign, 2,4);
      table.setAlignment(2,4, "center");
      table.setVerticalAlignment(2, 4, "bottom");

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
    Table table = new Table(1,2);
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

      IWTimestamp depTimeStamp = new IWTimestamp(service.getDepartureTime());
      List depAddresses = product.getDepartureAddresses(true);
      TravelAddress depAddress = getProductBusiness(iwc).getDepartureAddress(product);
      Currency currency;

      Text nameText = getText(iwrb.getLocalizedString("travel.name","Name"));
      Text daysText = getText(iwrb.getLocalizedString("travel.active_days","Active days"));
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
      Text daysTextBold = getBoldText("");
      Text supplierTextBold = getBoldText("");
      Text departureFromTextBold = getBoldText("");
      Text departureTimeTextBold = getBoldText("");
      Text pricesTextBold = getBoldText("");
      Text nameOfCategory = getBoldText("");
      Text priceText = getBoldText("");
      Text currencyText = getBoldText("");

      nameTextBold.setText(getProductBusiness(iwc).getProductNameWithNumber(product, true, iwc.getCurrentLocaleId()));
      supplierTextBold.setText(supplier.getName());
      departureFromTextBold.setText(depAddress.getName());
      departureTimeTextBold.setText(TextSoap.addZero(depTimeStamp.getHour())+":"+TextSoap.addZero(depTimeStamp.getMinute()));

      String[] dayOfWeekName = new String[8];
      IWCalendar cal = new IWCalendar();
      Locale locale = iwc.getCurrentLocale();
        dayOfWeekName[is.idega.idegaweb.travel.data.ServiceDayBMPBean.SUNDAY] = cal.getDayName(is.idega.idegaweb.travel.data.ServiceDayBMPBean.SUNDAY ,locale,IWCalendar.LONG).substring(0,3);
        dayOfWeekName[is.idega.idegaweb.travel.data.ServiceDayBMPBean.MONDAY] = cal.getDayName(is.idega.idegaweb.travel.data.ServiceDayBMPBean.MONDAY ,locale,IWCalendar.LONG).substring(0,3);
        dayOfWeekName[is.idega.idegaweb.travel.data.ServiceDayBMPBean.TUESDAY] = cal.getDayName(is.idega.idegaweb.travel.data.ServiceDayBMPBean.TUESDAY ,locale,IWCalendar.LONG).substring(0,3);
        dayOfWeekName[is.idega.idegaweb.travel.data.ServiceDayBMPBean.WEDNESDAY] = cal.getDayName(is.idega.idegaweb.travel.data.ServiceDayBMPBean.WEDNESDAY ,locale,IWCalendar.LONG).substring(0,3);
        dayOfWeekName[is.idega.idegaweb.travel.data.ServiceDayBMPBean.THURSDAY] = cal.getDayName(is.idega.idegaweb.travel.data.ServiceDayBMPBean.THURSDAY ,locale,IWCalendar.LONG).substring(0,3);
        dayOfWeekName[is.idega.idegaweb.travel.data.ServiceDayBMPBean.FRIDAY] = cal.getDayName(is.idega.idegaweb.travel.data.ServiceDayBMPBean.FRIDAY ,locale,IWCalendar.LONG).substring(0,3);
        dayOfWeekName[is.idega.idegaweb.travel.data.ServiceDayBMPBean.SATURDAY] = cal.getDayName(is.idega.idegaweb.travel.data.ServiceDayBMPBean.SATURDAY ,locale,IWCalendar.LONG).substring(0,3);
      int[] days = new int[]{};//is.idega.idegaweb.travel.data.ServiceDayBMPBean.getDaysOfWeek(product.getID());
      try {
        ServiceDayHome sdayHome = (ServiceDayHome) IDOLookup.getHome(ServiceDay.class);
        ServiceDay sDay = sdayHome.create();
        days = sDay.getDaysOfWeek(product.getID());
      }catch (Exception e) {
        e.printStackTrace(System.err);
      }

      if (days.length == 7) {
        daysTextBold.setText(iwrb.getLocalizedString("travel.daily","daily"));
      }else {
        for (int j = 0; j < days.length; j++) {
          if (j > 0) daysTextBold.addToText(", ");
          daysTextBold.addToText(dayOfWeekName[days[j]]);
        }
      }

      table.add(nameText,1,1);
      table.add(space,1,1);
      table.add(nameTextBold,1,1);

      table.add(supplierText,1,2);
      table.add(space,1,2);
      table.add(supplierTextBold,1,2);

      table.add(image,1,3);
      table.setAlignment(1,3,"left");

      table.add(daysText,2,2);
      table.add(space,2,2);
      table.add(daysTextBold,2,2);


      String stampTxt1 = iwrb.getLocalizedString("travel.not_configured","Not configured");
      String stampTxt2 = iwrb.getLocalizedString("travel.not_configured","Not configured");
      ProductPrice[] prices;
      Text timeframeTextBold;

      Table pTable = new Table();
        pTable.setCellspacing(0);

      int pRow = 1;
      for (int l = 0; l < depAddresses.size(); l++) {
        depAddress = (TravelAddress) depAddresses.get(l);
        departureFromTextBold = getBoldText(depAddress.getName());
          departureFromTextBold.addToText(Text.NON_BREAKING_SPACE+Text.NON_BREAKING_SPACE);
        pTable.add(departureFromTextBold, 1, pRow);
        for (int i = 0; i < timeframes.length; i++) {
          prices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(product.getID(), timeframes[i].getID(), depAddress.getID(), true);
          if (prices.length > 0) {
            stampTxt1 = new IWTimestamp(timeframes[i].getFrom()).getLocaleDate(iwc);
            stampTxt2 = new IWTimestamp(timeframes[i].getTo()).getLocaleDate(iwc);
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
              currency = ((com.idega.block.trade.data.CurrencyHome)com.idega.data.IDOLookup.getHomeLegacy(Currency.class)).findByPrimaryKeyLegacy(prices[j].getCurrencyId());
              nameOfCategory = getText(prices[j].getPriceCategory().getName());
                nameOfCategory.addToText(Text.NON_BREAKING_SPACE+":"+Text.NON_BREAKING_SPACE+Text.NON_BREAKING_SPACE);
              try {
                priceText = getBoldText(Integer.toString( (int) getTravelStockroomBusiness(iwc).getPrice(prices[j].getID(),((Integer) service.getPrimaryKey()).intValue(),prices[j].getPriceCategoryID() , prices[j].getCurrencyId(), IWTimestamp.getTimestampRightNow(), timeframes[i].getID(), depAddress.getID()) ) );
                currencyText = getBoldText(currency.getCurrencyAbbreviation());
                pTable.add(currencyText,5,pRow);
              }catch (ProductPriceException p) {
                priceText.setText(iwrb.getLocalizedString("travel.not_configured","Not configured"));
              }

              pTable.add(nameOfCategory,3,pRow);
              pTable.add(priceText,4,pRow);
              ++pRow;
            }
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

      Link currCalc = new Link(iwrb.getLocalizedImageButton("travel.currency_calculator","Currency calculator"));
        currCalc.setWindowToOpen(TravelCurrencyCalculatorWindow.class);
//      table.add(currCalc, 2, 3);

      table.setAlignment(2,1,"right");
      table.setAlignment(2,2,"right");
      table.setAlignment(2,3,"right");
      table.setAlignment(2,4,"right");
      table.setVerticalAlignment(1,3,"top");
      table.setVerticalAlignment(1,4,"top");
      table.mergeCells(1,1,2,1);
//      table.mergeCells(1,2,2,2);
      table.mergeCells(1,3,1,5);
      table.mergeCells(2,3,2,5);
//      table.setWidth(1,"138");
//      table.setWidth(3,"350");
//      table.setWidth(2,"350");
//      table.setBorder(1);


    }catch (Exception e) {
      e.printStackTrace(System.err);
    }
    return form;
  }



  private Form leftBottom(IWContext iwc) {
    try {
      BookingForm bf = getServiceHandler(iwc).getBookingForm(iwc, product);
//      TourBookingForm tbf = new TourBookingForm(iwc, product);
      CalendarHandler ch  = new CalendarHandler(iwc);
        ch.setProduct(product);

      boolean legalDay = false;
      legalDay = getTravelStockroomBusiness(iwc).getIfDay(iwc, product, stamp);


      Form form = new Form();

      if (legalDay) {
        String action = iwc.getParameter(this.sAction);

        String tbfAction = iwc.getParameter(TourBookingForm.sAction);
        if (tbfAction == null || !tbfAction.equals(TourBookingForm.parameterSaveBooking)) {
          action = "";
        }

        if (action == null || action.equals("")) {
            form = bf.getPublicBookingForm(iwc, product, stamp);
            form.maintainParameter(this.parameterProductId);
            form.addParameter(this.sAction,this.parameterSubmitBooking);
        }else if (action.equals(this.parameterSubmitBooking)) {
            form = bf.getFormMaintainingAllParameters();
            form.maintainParameter(this.parameterProductId);
            form.add(getVerifyBookingTable(iwc));
        }else if (action.equals(this.parameterBookingVerified)) {
            form = bf.getFormMaintainingAllParameters();
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

  private Table getVerifyBookingTable(IWContext iwc) throws RemoteException, SQLException{
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
    String depAddressId = iwc.getParameter(TourBookingForm.parameterDepartureAddressId);

    String fromDate = iwc.getParameter(TourBookingForm.parameterFromDate);
    String manyDays = iwc.getParameter(TourBookingForm.parameterManyDays);

    String ccNumber = iwc.getParameter(TourBookingForm.parameterCCNumber);
    String ccMonth = iwc.getParameter(TourBookingForm.parameterCCMonth);
    String ccYear = iwc.getParameter(TourBookingForm.parameterCCYear);

    String inquiry = iwc.getParameter(TourBookingForm.parameterInquiry);

    boolean valid = true;
    String errorColor = "YELLOW";
    Text star = new Text(Text.NON_BREAKING_SPACE+"*");
      star.setFontColor(errorColor);


//    ProductPrice[] pPrices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(this.product.getID(), true);
    ProductPrice[] prices = {};
    ProductPrice[] misc = {};
    Timeframe tFrame = getProductBusiness(iwc).getTimeframe(this.product, stamp, Integer.parseInt(depAddressId));
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
          price += current * getTravelStockroomBusiness(iwc).getPrice(pPrices[i].getID() ,this.productId,pPrices[i].getPriceCategoryID(), pPrices[i].getCurrencyId() ,IWTimestamp.getTimestampRightNow(), tFrame.getID(), Integer.parseInt(depAddressId));
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
      table.add(new HiddenInput(this.sAction, this.parameterBookingVerified),2,row);
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

  private Table doBooking(IWContext iwc) throws RemoteException{
    Table table = new Table();
      String ccNumber = iwc.getParameter(TourBookingForm.parameterCCNumber);
      String ccMonth  = iwc.getParameter(TourBookingForm.parameterCCMonth);
      String ccYear   = iwc.getParameter(TourBookingForm.parameterCCYear);
      String depAddr  = iwc.getParameter(TourBookingForm.parameterDepartureAddressId);

      Text display = getBoldTextWhite("");
      boolean success = false;
      boolean inquirySent = false;
      String heimild = "";

      com.idega.block.tpos.business.TPosClient t = null;
      GeneralBooking  gBooking = null;

      TransactionManager tm = IdegaTransactionManager.getInstance();
      try {
        tm.begin();

        float price = 0;
        int total = 0;
        int current = 0;
        Currency currency = null;

        int days = Integer.parseInt(iwc.getParameter(TourBookingForm.parameterManyDays));

        ProductPrice[] pPrices = {};
        ProductPrice[] misc = {};
        Timeframe tFrame = getProductBusiness(iwc).getTimeframe(this.product, stamp, Integer.parseInt(depAddr));
        if (tFrame != null) {
          pPrices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(product.getID(), tFrame.getID(), Integer.parseInt(depAddr), true);
          misc = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getMiscellaneousPrices(product.getID(), tFrame.getID(), Integer.parseInt(depAddr), true);
        }

        for (int j = 0; j < days; j++) {

          for (int i = 0; i < pPrices.length; i++) {
            try {
              current = Integer.parseInt(iwc.getParameter("priceCategory"+i));
            }catch (NumberFormatException n) {
              current = 0;
            }
            total += current;
            price += current * getTravelStockroomBusiness(iwc).getPrice(pPrices[i].getID() ,this.productId,pPrices[i].getPriceCategoryID(), pPrices[i].getCurrencyId() ,IWTimestamp.getTimestampRightNow(), tFrame.getID(), Integer.parseInt(depAddr));
          }

          for (int i = 0; i < misc.length; i++) {
            try {
              current = Integer.parseInt(iwc.getParameter("miscPriceCategory"+i));
            }catch (NumberFormatException n) {
              current = 0;
            }
            price += current * getTravelStockroomBusiness(iwc).getPrice(misc[i].getID() ,this.productId,misc[i].getPriceCategoryID(), misc[i].getCurrencyId() ,IWTimestamp.getTimestampRightNow(), tFrame.getID(), Integer.parseInt(depAddr));
          }

        }

        BookingForm bf = getServiceHandler(iwc).getBookingForm(iwc, product);
//        TourBookingForm tbf = new TourBookingForm(iwc,product);
        int bookingId = bf.handleInsert(iwc);
        gBooking = ((is.idega.idegaweb.travel.data.GeneralBookingHome)com.idega.data.IDOLookup.getHome(GeneralBooking.class)).findByPrimaryKey(new Integer(bookingId));

        if (bookingId == BookingForm.inquirySent) {
          inquirySent = true;
          tm.commit();
        }else {
           try {
            System.out.println("Starting TPOS test : "+IWTimestamp.RightNow().toString());
            TPosMerchant merchant = null;
            try {
              int productSupplierId = gBooking.getService().getProduct().getSupplierId();
              Supplier suppTemp = ((SupplierHome) IDOLookup.getHomeLegacy(Supplier.class)).findByPrimaryKeyLegacy(productSupplierId);
              merchant = suppTemp.getTPosMerchant();
              System.out.println("TPosMerchant found");
            }catch (Exception e) {
              System.out.println("TPosMerchant NOT found for supplier, using system default");
            }
            if (merchant == null) {
              t = new com.idega.block.tpos.business.TPosClient(iwc);
            }else {
              t = new com.idega.block.tpos.business.TPosClient(iwc, merchant);
            }
            heimild = t.doSale(ccNumber,ccMonth,ccYear,price,"ISK");
            //System.out.println("heimild = " + heimild);
            System.out.println("Ending TPOS test : "+IWTimestamp.RightNow().toString());
          }catch(com.idega.block.tpos.business.TPosException e) {
            System.out.println("TPOS errormessage = " + e.getErrorMessage());
            System.out.println("number = " + e.getErrorNumber());
            System.out.println("display = " + e.getDisplayError());
            int number = Integer.parseInt(e.getErrorNumber());
            switch (number) {
              case 6:
              case 12:
              case 19:
                display.setText(iwrb.getLocalizedString("travel.creditcard_number_incorrect","Creditcard number incorrect"));
                break;
              case 10:
              case 22:
              case 74:
                display.setText(iwrb.getLocalizedString("travel.creditcard_type_not_accepted","Creditcard type not accepted"));
                break;
              case 17:
              case 18:
                display.setText(iwrb.getLocalizedString("travel.creditcard_is_expired","Creditcard is expired"));
                break;
              case 48:
              case 49:
              case 50:
              case 51:
              case 56:
              case 57:
              case 76:
              case 79:
              case 2002:
              case 2010:
                display.setText(iwrb.getLocalizedString("travel.cannot_connect_to_cps","Could not connect to Central Payment Server"));
                break;
              case 7:
              case 37:
              case 69:
              case 75:
                display.setText(iwrb.getLocalizedString("travel.creditcard_autorization_failed","Authorization failed"));
                break;
              /*case 69:
                display.setText(e.getErrorMessage());
                break;*/
              case 20:
              case 31:
                display.setText(iwrb.getLocalizedString("travel.transaction_not_permitted","Transaction not permitted"));
                break;
              case 99999:
                display.setText(iwrb.getLocalizedString("travel.booking_was_not_confirmed_try_again_later","Booking was not confirmed. Please try again later"));
                break;
              default:
                display.setText(iwrb.getLocalizedString("travel.cannot_connect","Cannot communicate with server"));
                break;
            }

            throw e;
          }

          gBooking.setCreditcardAuthorizationNumber(heimild);
          gBooking.store();

          debug("commiting");
          tm.commit();
          success = true;
        }

      }catch(com.idega.block.tpos.business.TPosException e) {
        display.addToText(" ( "+e.getMessage()+" )");
        //e.printStackTrace(System.err);
        gBooking.setIsValid(false);
        gBooking.store();
        try {
          tm.commit();
        }catch(Exception ex) {
          debug("commit failed");
          ex.printStackTrace(System.err);
          try {
            tm.rollback();
          }catch (javax.transaction.SystemException se) {
            se.printStackTrace(System.err);
          }
        }

        gBooking = null;
        success = false;
      }catch (Exception e) {
        display.addToText(" ( "+e.getMessage()+" )");
        e.printStackTrace(System.err);
        try {
          tm.rollback();
        }catch (javax.transaction.SystemException se) {
          se.printStackTrace(System.err);
        }
      }

      if (success && gBooking != null) {
        boolean sendEmail = false;
        try {
          ProductHome pHome = (ProductHome)com.idega.data.IDOLookup.getHome(Product.class);
          Product prod = pHome.findByPrimaryKey(new Integer(gBooking.getServiceID()));
          Supplier suppl = ((SupplierHome) IDOLookup.getHomeLegacy(Supplier.class)).findByPrimaryKeyLegacy(prod.getSupplierId());
          Settings settings = suppl.getSettings();
          Email sEmail = suppl.getEmail();
          String suppEmail = "";
          if (sEmail != null) {
            suppEmail = sEmail.getEmailAddress();
          }
          String bookEmail = gBooking.getEmail();
          boolean doubleSendSuccessful = false;

          if (settings.getIfDoubleConfirmation()) {
            try {
              sendEmail = true;
              StringBuffer mailText = new StringBuffer();
              mailText.append(iwrb.getLocalizedString("travel.email_double_confirmation","This email is to confirm that your booking has been received, and confirmed."));
              mailText.append("\n").append(iwrb.getLocalizedString("travel.name",   "Name    ")).append(" : ").append(gBooking.getName());
              mailText.append("\n").append(iwrb.getLocalizedString("travel.service","Service ")).append(" : ").append(getProductBusiness(iwc).getProductNameWithNumber(prod, true, iwc.getCurrentLocaleId()));
              mailText.append("\n").append(iwrb.getLocalizedString("travel.date",   "Date    ")).append(" : ").append(new IWTimestamp(gBooking.getBookingDate()).getLocaleDate(iwc));
              mailText.append("\n").append(iwrb.getLocalizedString("travel.seats",  "Seats   ")).append(" : ").append(gBooking.getTotalCount());

              SendMail sm = new SendMail();
                sm.send(suppEmail, bookEmail, "", "", "mail.idega.is", "Booking",mailText.toString());
              doubleSendSuccessful = true;
            }catch (MessagingException me) {
              doubleSendSuccessful = false;
              me.printStackTrace(System.err);
            }
          }

          if (settings.getIfEmailAfterOnlineBooking()) {
            try {
              String subject = "Booking";

              StringBuffer mailText = new StringBuffer();
              mailText.append(iwrb.getLocalizedString("travel.email_after_online_booking","You have just received a booking through nat.sidan.is."));
              mailText.append("\n").append(iwrb.getLocalizedString("travel.name",   "Name    ")).append(" : ").append(gBooking.getName());
              mailText.append("\n").append(iwrb.getLocalizedString("travel.service","Service ")).append(" : ").append(getProductBusiness(iwc).getProductNameWithNumber(prod, true, iwc.getCurrentLocaleId()));
              mailText.append("\n").append(iwrb.getLocalizedString("travel.date",   "Date    ")).append(" : ").append(new IWTimestamp(gBooking.getBookingDate()).getLocaleDate(iwc));
              mailText.append("\n").append(iwrb.getLocalizedString("travel.seats",  "Seats   ")).append(" : ").append(gBooking.getTotalCount());
              if (doubleSendSuccessful) {
                mailText.append("\n\n").append(iwrb.getLocalizedString("travel.double_confirmation_has_been_sent","Double confirmation has been sent."));
              }else {
                mailText.append("\n\n").append(iwrb.getLocalizedString("travel.double_confirmation_has_not_been_sent","Double confirmation has NOT been sent."));
                mailText.append("\n").append("   - ").append(iwrb.getLocalizedString("travel.email_was_probably_incorrect","E-mail was probably incorrect."));
                subject = "Booking - double confirmation failed!";
              }


              SendMail sm = new SendMail();
                sm.send(suppEmail, suppEmail, "", "", "mail.idega.is", subject,mailText.toString());
            }catch (MessagingException me) {
              me.printStackTrace(System.err);
            }
          }

        }catch (Exception e) {
          e.printStackTrace(System.err);
        }

        table.add(getBoldTextWhite(gBooking.getName()));
        table.add(getBoldTextWhite(", "));
        table.add(getBoldTextWhite(iwrb.getLocalizedString("travel.you_booking_has_been_confirmed","your booking has been confirmed.")));
        table.add(Text.BREAK);
        table.add(Text.BREAK);
        if (sendEmail) {
          table.add(getBoldTextWhite(iwrb.getLocalizedString("travel.you_will_reveice_an_email_shortly","You will receive an email shortly confirming your booking.")));
          table.add(Text.BREAK);
          table.add(Text.BREAK);
        }
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
          printVoucher.addParameter(VoucherWindow.parameterBookingId, gBooking.getID());
          printVoucher.setWindowToOpen(VoucherWindow.class);

        if (t != null) {
          com.idega.block.tpos.presentation.Receipt r = new com.idega.block.tpos.presentation.Receipt(t, supplier);
          iwc.setSessionAttribute(ReceiptWindow.RECEIPT_SESSION_NAME, r);

          Link printCCReceipt = new Link(getBoldTextWhite(iwrb.getLocalizedString("travel.print_cc_receipt","Print creditcard receipt")));
            printCCReceipt.setWindowToOpen(ReceiptWindow.class);
          table.add(Text.NON_BREAKING_SPACE+Text.NON_BREAKING_SPACE, 1,2);
          table.add(printCCReceipt, 1, 2);
        }

        table.add(printVoucher,1,3);
        table.setAlignment(1,1,"left");
        table.setAlignment(1,2,"right");
        table.setAlignment(1,3,"right");
      }else if (inquirySent) {
        table.add(getBoldTextWhite(iwrb.getLocalizedString("travel.inquiry_has_been_sent","Inquiry has been sent")));
        table.add(Text.BREAK);
        table.add(getBoldTextWhite(iwrb.getLocalizedString("travel.you_will_reveice_an_confirmation_email_shortly","You will receive an confirmation email shortly.")));
      }else {
        table.add(display);
        if (gBooking == null) {
          debug("gBooking == null");
        }
      }

    return table;
  }

  protected TravelStockroomBusiness getTravelStockroomBusiness(IWApplicationContext iwac) throws RemoteException {
    return (TravelStockroomBusiness) IBOLookup.getServiceInstance(iwac, TravelStockroomBusiness.class);
  }
  protected ServiceHandler getServiceHandler(IWApplicationContext iwac) throws RemoteException {
    return (ServiceHandler) IBOLookup.getServiceInstance(iwac, ServiceHandler.class);
  }

  protected ProductBusiness getProductBusiness(IWApplicationContext iwac) throws RemoteException {
    return (ProductBusiness) IBOLookup.getServiceInstance(iwac, ProductBusiness.class);
  }

}
