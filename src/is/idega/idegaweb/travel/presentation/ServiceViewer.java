package is.idega.idegaweb.travel.presentation;

import com.idega.idegaweb.*;
import java.util.*;
import com.idega.util.*;
import com.idega.core.localisation.business.LocaleSwitcher;
import com.idega.presentation.Block;

import com.idega.presentation.text.*;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.block.text.presentation.TextReader;

import com.idega.block.trade.stockroom.data.*;
import com.idega.block.trade.data.Currency;
import is.idega.idegaweb.travel.data.*;
import com.idega.core.data.*;

import com.idega.util.text.TextSoap;

import is.idega.idegaweb.travel.business.TravelStockroomBusiness;
import com.idega.block.trade.stockroom.business.ProductPriceException;
import is.idega.idegaweb.travel.business.TravelStockroomBusiness.*;
import is.idega.idegaweb.travel.service.tour.business.TourBusiness;
import com.idega.block.trade.stockroom.business.ProductBusiness;
import com.idega.block.text.business.TextFinder;
import com.idega.block.text.data.*;
import com.idega.core.localisation.business.ICLocaleBusiness;

import java.sql.SQLException;
import java.text.DecimalFormat;

/**
 * Title: com.idega.idegaweb.travel.presentation.ServiceViewer
 * Description: This is a presentation class for viewing service information. Single view<br>
 * and multiple views supported and date filtering.
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:eiki@idega.is">Eirikur Hrafnsson</a>
 * @version 1.0
 */

public class ServiceViewer extends Window {

  public static final String IW_BUNDLE_IDENTIFIER = "is.idega.travel";
  public static final String IW_TRAVEL_SERVICE_ID = "iw_tr_serv_id";
  public static final String IW_TRAVEL_SUPPLIER_ID = "iw_tr_suppl_id";

  private static final String IW_TRAVEL_ADD_BUY_BUTTON = "iw_tr_add_buy_button";
  private static final String IW_TRAVEL_ADD_MORE_BUTTON = "iw_tr_add_more_button";

  private DecimalFormat df = new DecimalFormat("0.00");

  private IWBundle iwb;
  private IWResourceBundle iwrb;

  private TravelStockroomBusiness tsb;
  private idegaCalendar cal;
  private String[] dayOfWeekName;

  private Supplier supplier;
  private Service service;

  private idegaTimestamp dateFrom,dateTo;
  private String width,height,color1,color2;
  private int windowWidth = 600;
  private int windowHeight = 600;
  private Link link;
  private Text text;
  private Text boldText;
  private boolean showBuyButton = false;
  private boolean showMoreButton = true;
  private boolean listView = false;

  private String sService = null;
  private String sSupplier = null;
  private int iLocaleID = -1;

  public ServiceViewer(){
    super.setWidth(windowWidth);
    super.setHeight(windowHeight);
    this.setAllMargins(4);
    //setAllMargins(0);
  }

  private void init(IWContext iwc) {
    iwb = this.getBundle(iwc);
    iwrb = this.getResourceBundle(iwc);

    this.setTitle(iwrb.getLocalizedString("book_service","Book service"));

    sService = iwc.getParameter(IW_TRAVEL_SERVICE_ID);
    sSupplier = iwc.getParameter(IW_TRAVEL_SUPPLIER_ID);

    if( sService!=null ){
      if( iwc.getSessionAttribute(IW_TRAVEL_ADD_MORE_BUTTON+sService)!=null ) showMoreButton = true;
      if( iwc.getSessionAttribute(IW_TRAVEL_ADD_BUY_BUTTON+sService)!=null ) showBuyButton = true;
    }

    tsb = TravelStockroomBusiness.getNewInstance();
    cal = new idegaCalendar();
    dayOfWeekName = new String[8];

    link = new Link();
    link.setAsImageButton(true);/**@todo localize check if this gets cloned*/

    text = new Text();
    boldText = new Text();
    boldText.setBold(true);

    dayOfWeekName[ServiceDay.SUNDAY] = cal.getNameOfDay(ServiceDay.SUNDAY ,iwc).substring(0,3);
    dayOfWeekName[ServiceDay.MONDAY] = cal.getNameOfDay(ServiceDay.MONDAY ,iwc).substring(0,3);
    dayOfWeekName[ServiceDay.TUESDAY] = cal.getNameOfDay(ServiceDay.TUESDAY ,iwc).substring(0,3);
    dayOfWeekName[ServiceDay.WEDNESDAY] = cal.getNameOfDay(ServiceDay.WEDNESDAY ,iwc).substring(0,3);
    dayOfWeekName[ServiceDay.THURSDAY] = cal.getNameOfDay(ServiceDay.THURSDAY ,iwc).substring(0,3);
    dayOfWeekName[ServiceDay.FRIDAY] = cal.getNameOfDay(ServiceDay.FRIDAY ,iwc).substring(0,3);
    dayOfWeekName[ServiceDay.SATURDAY] = cal.getNameOfDay(ServiceDay.SATURDAY ,iwc).substring(0,3);
  }

  public void main(IWContext iwc) throws Exception{
    // Verður að vera hér, ekki færa...
    String languageString = iwc.getParameter(LocaleSwitcher.languageParameterString);
    if(languageString!=null){
      Locale locale = LocaleUtil.getLocale(languageString);
      if(locale!=null){
	iwc.setCurrentLocale(locale);
        iLocaleID = ICLocaleBusiness.getLocaleId(locale);
      }
    }
    // ...hingað

    init(iwc);
    handleEvents(iwc);
  }

 private void handleEvents(IWContext iwc) throws Exception {
    if( sService!=null ){//single views
      try {
        service = new Service(Integer.parseInt(sService));
      }
      catch (Exception ex) {
        //do nothing
      }
    }
    else if( sSupplier!=null ){//multi view
      try {
        listView = true;
        supplier = new Supplier(Integer.parseInt(sSupplier));
      }
      catch (Exception ex) {
        //do nothing
      }
    }

    if( service!=null ){
      add(getServiceInfoTable(iwc));
    }
    else{
      add(getServiceListTable(iwc));
    }
  }

  private String getServiceDepartures(Service serv){
    StringBuffer depart = new StringBuffer();
    try {
      int[] dagur = ServiceDay.getDaysOfWeek(serv.getID());

      if( (dagur.length == 7) ){
        depart.append(iwrb.getLocalizedString("every.day","Every day")).append(Text.NON_BREAKING_SPACE);
      }
      else{
        for (int i = 0; i < dagur.length; i++) {
          depart.append(dayOfWeekName[dagur[i]]);
          depart.append(Text.NON_BREAKING_SPACE);
        }
      }

      /*
      idegaTimestamp stamp = new idegaTimestamp(serv.getDepartureTime());
      depart.append(stamp.getHour());
      depart.append(":");
      depart.append(TextSoap.addZero(stamp.getMinute()));
      */
    }
    catch (Exception ex) {
      ex.printStackTrace(System.err);
    }

    return depart.toString();
  }

  private String getServiceDurationString(Service serv){
    String duration;

    idegaTimestamp stamp = new idegaTimestamp(serv.getDepartureTime());
    idegaTimestamp stamp2 = new idegaTimestamp(serv.getArrivalTime());
    int minutes = stamp.getMinutesBetween(stamp,stamp2);
    int hours = (int) minutes/60;
    int minutesleft = minutes % 60;

    if(hours==0){
      duration = String.valueOf(minutesleft)+"m";
    }
    else {
      if(minutesleft==0){
        duration = hours+"h";
      }
      else{
        duration = hours+"h "+minutesleft+"m";/**@todo lacalize**/
      }
    }
    return duration;
  }

  private Table getServiceListTable(IWContext iwc){
    List prodlist;
    Table content = new Table();
    content.setCellspacing(0);
    content.setCellpadding(2);

    boolean useColors = false;
    String currentColor = color1;
    if( (color1!=null) && (color2!=null) ){
      useColors = true;
    }

    if( (dateFrom!=null) && (dateTo!=null) ){
      prodlist = ProductBusiness.getProducts(supplier.getID(),dateFrom,dateTo);
    }
    else{
      prodlist = ProductBusiness.getProducts(supplier.getID());
    }

    if( prodlist!=null ){
      Iterator iter = prodlist.iterator();

      int x = 1;
      int y = 1;
      Service serv = null;

      /** @todo header localized for the table**/
  /*      Text dur = (Text) boldText.clone();
        day.setText(iwrb.getLocalizedString("travel.serviceviewer.departures","Departures: "));
        content.add(days,2,y);

      //timeframe - trip length
        Text dur = (Text) boldText.clone();
        dur.setText(iwrb.getLocalizedString("travel.serviceviewer.duration","Duration: "));
        content.add(days,2,y);

      //Price
        Text price = (Text) boldText.clone();
        price.setText(iwrb.getLocalizedString("travel.serviceviewer.info.price","Price: "));
        content.add(days,2,y);
*/



       while( (iter!=null) && iter.hasNext() ) {
        Product prod = (Product) iter.next();
        try{
          serv = tsb.getService(prod);
          if (y % 2 == 0) {
            currentColor = color2;
          }else {
            currentColor = color1;
          }

          /**
           * @todo Laga fyrir multi timeframes...
           */
           Timeframe timeframe = prod.getTimeframe();
        //number
          Text number = (Text) text.clone();
          number.setText(prod.getNumber());
          number.setBold(true);
          content.add(number,x,y);

        //name
          Text desc = (Text) text.clone();
          desc.setText(ProductBusiness.getProductName(prod, iLocaleID));
//          content.add(desc,++x,y);
            Link more = new Link(desc);
            more.setWindowToOpen(ServiceViewer.class);
            more.addParameter(IW_TRAVEL_SERVICE_ID,prod.getID());
            content.add(more,++x,y);

        //active days
          Text days = (Text) text.clone();
          days.setText(getServiceDepartures(serv));
          content.add(days,++x,y);
        //timeframe - trip length
          Text duration = (Text) text.clone();
          duration.setText(getServiceDurationString(serv));
          content.add(duration,++x,y);
        //Price
          Table price = getServicePrice(iwc, serv, true);/*(Text) text.clone();
          //price.setText(getServicePrice(serv, timeframe.getID()));
          price.setText(getServicePrice(serv,true));*/

          content.add(price,++x,y);
        //Info and buy buttons
/*          if( showMoreButton){
            iwc.setSessionAttribute(IW_TRAVEL_ADD_MORE_BUTTON+prod.getID(),String.valueOf(showMoreButton));

            Link more = new Link(iwrb.getLocalizedString("travel.more.button","more"));
            more.setWindowToOpen(ServiceViewer.class);
            more.addParameter(IW_TRAVEL_SERVICE_ID,prod.getID());
            more.setAsImageButton(true);
            content.add(more,++x,y);
          }*/

          if (useColors) {
            content.setColor(1, y, currentColor);
            content.setColor(2, y, currentColor);
            content.setColor(3, y, currentColor);
            content.setColor(4, y, currentColor);
            content.setColor(5, y, currentColor);
          }

          if( showBuyButton){
            iwc.setSessionAttribute(IW_TRAVEL_ADD_BUY_BUTTON+prod.getID(),String.valueOf(showBuyButton));
            Link buy = LinkGenerator.getLink(iwc,prod.getID());
            buy.setImage(iwrb.getImage("buttons/book_nat.gif"));
//            buy.setAsImageButton(true);
            content.add(buy,++x,y);
          }

        }
        catch(Exception ex){
          ex.printStackTrace(System.err);
        }

        y++;
        x = 1;
      }
    }
/*
    if( (color1!=null) && (color2!=null) ){
      content.setHorizontalZebraColored(color1,color2);
    }
*/
    return content;
  }

  private Table getServiceInfoTable(IWContext iwc){
    Table content = new Table();
    content.setWidth("100%");
    content.setCellspacing(0);
    content.setCellpadding(2);
//    content.setBorder(1);

    try {
      int y = 1;
      Product product = ProductBusiness.getProduct(service.getID());
      //number
      Text numberAndName = getBoldText(ProductBusiness.getProductNameWithNumber(product, true, iLocaleID));//.getNumber()+" - "+ProductBusiness.getProductName(product, iLocaleID));
        numberAndName.setFontStyle("font-family:Verdana,Arial,Helvetica,sans-serif;font-size:14pt;font-weight:bold;color:#000099;");
      content.add(numberAndName,1,y);
      content.mergeCells(1,y,2,y);

      HorizontalRule hr = new HorizontalRule("100%");
        hr.setColor("#FF9900");
        hr.setNoShade(true);
        hr.setHeight(1);
      content.add(hr, 1, ++y);
      content.mergeCells(1, y, 2, y);

      //description
      TxText descriptionText = product.getText();
      if (descriptionText != null) {
        TextReader textReader = new TextReader(product.getText().getID());
          textReader.setHeadlineStyle("font-family:Arial,Helvetica,sans-serif;font-size:12pt;font-weight:bold;");
          textReader.setTextStyle("font-family:Arial,Helvetica,sans-serif;font-size:8pt;");
        content.add(textReader,1,++y);//insert a textreader
        content.mergeCells(1,y,2,y);
      }

      content.add(hr, 1, ++y);
      content.mergeCells(1, y, 2, y);

      //active days
        Text day = (Text) boldText.clone();
        day.setText(iwrb.getLocalizedString("travel.serviceviewer.departures","Departures: "));
        day.setFontStyle("font-family:Arial,Helvetica,sans-serif;font-size:9pt;font-weight:bold;color:#000099;");
        day.addToText(Text.NON_BREAKING_SPACE);
        Text days = getText(getServiceDepartures(service));
        content.add(day,1,++y);
        content.add(days,2,y);
        content.setAlignment(1, y, Table.HORIZONTAL_ALIGN_RIGHT);

      //timeframe - trip length
        Text dur = (Text) boldText.clone();
        dur.setText(iwrb.getLocalizedString("travel.serviceviewer.duration","Duration: "));
        dur.setFontStyle("font-family:Arial,Helvetica,sans-serif;font-size:9pt;font-weight:bold;color:#000099;");
        dur.addToText(Text.NON_BREAKING_SPACE);
        Text duration = getText(getServiceDurationString(service));
        content.add(dur,1,++y);
        content.add(duration,2,y);
        content.setAlignment(1, y, Table.HORIZONTAL_ALIGN_RIGHT);
      //Price
          /**
           * @todo Laga fyrir multi timeframes...
           */
        Timeframe timeframe = product.getTimeframe();
        Text price = (Text) boldText.clone();
        price.setText(iwrb.getLocalizedString("travel.serviceviewer.info.price","Price: "));
        price.setFontStyle("font-family:Arial,Helvetica,sans-serif;font-size:9pt;font-weight:bold;color:#000099;");
        price.addToText(Text.NON_BREAKING_SPACE);
//        Text prices = new Text(getServicePrice(service, timeframe.getID()));
        Table prices = getServicePrice(iwc, service,false);
        content.add(price,1,++y);
        content.setVerticalAlignment(1,y,Table.VERTICAL_ALIGN_TOP);
        content.setVerticalAlignment(2,y,Table.VERTICAL_ALIGN_TOP);
        content.setAlignment(1, y, Table.HORIZONTAL_ALIGN_RIGHT);
        content.add(prices,2,y);


      content.add(hr, 1, ++y);
      content.mergeCells(1,y,2,y);

      if( showBuyButton){
        Link buy = LinkGenerator.getLink(iwc,service.getID());
        buy.setImage(iwrb.getImage("buttons/book_nat.gif"));
//        buy.setText(iwrb.getLocalizedString("travel.buy.button","buy"));
//        buy.setAsImageButton(true);
        content.add(buy,2,++y);
        content.setAlignment(2,y,"right");
      }
    }
    catch (Exception ex) {
      ex.printStackTrace(System.err);
    }

    return content;
  }



  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER ;
  }

  public void setSupplier(Supplier supplier){
    this.supplier = supplier;
  }

  public void setService(Service service) {
    this.service = service;
  }

  public void setDateFrom(idegaTimestamp dateFrom){
    this.dateFrom = dateFrom;
  }

  public void setDateTo(idegaTimestamp dateTo){
    this.dateTo = dateTo;
  }

  public void setWidth(String width){
   this.width = width;
  }

  public void setHeight(String height){
   this.height = height;
  }

  public void setWindowHeight(int windowHeight){
   this.windowHeight = windowHeight;
  }

  public void setWindowWidth(int windowWidth){
   this.windowWidth = windowWidth;
  }

  public void setTextPrototype(Text text){
   this.text = text;
  }

  public void setLinkPrototype(Link link){
   this.link = link;
  }

  public void setZebraColors(String color1, String color2){
   this.color1 = color1;
   this.color2 = color2;
  }

  public void showBuyButton(boolean showBuyButton){
   this.showBuyButton = showBuyButton;
  }

  public void showMoreButton(boolean showMoreButton){
   this.showMoreButton = showMoreButton;
  }


  private Text getBoldText(String content) {
    Text texti = getText(content);
      texti.setFontStyle("font-family:Arial,Helvetica,sans-serif;font-size:8pt;font-weight:bold;");
    return texti;
  }

  private Text getText(String content) {
    Text texti = (Text) text.clone();
      texti.setText(content);
      texti.setFontStyle("font-family:Arial,Helvetica,sans-serif;font-size:8pt;");
    return texti;
  }

  private Table getServicePrice(IWContext iwc, Service service, boolean cutOff){
    Table pTable = new Table();
      pTable.setWidth(Table.HUNDRED_PERCENT);
    int pRow = 1;

    try {

    Text departureFromTextBold = getBoldText("");
    Text timeframeTextBold = getBoldText("");
    Text priceText = getBoldText("");
    Text currencyText = getBoldText("");

    Text nameOfCategory = getText("");

    Product product = ProductBusiness.getProduct(service.getID());
    TravelAddress[] depAddresses = ProductBusiness.getDepartureAddresses(product);
    Timeframe[] timeframes = product.getTimeframes();
    ProductPrice[] prices = null;

    if (cutOff) {
      prices = ProductPrice.getProductPrices(product.getID(), timeframes[0].getID(), depAddresses[0].getID(), true);
      if (prices.length > 0) {
        pTable.add(prices[0].getPriceCategory().getName()+Text.NON_BREAKING_SPACE+Text.NON_BREAKING_SPACE+df.format(TravelStockroomBusiness.getPrice(prices[0].getID(), service.getID(),prices[0].getPriceCategoryID() , prices[0].getCurrencyId(), idegaTimestamp.getTimestampRightNow(), timeframes[0].getID(), depAddresses[0].getID()) ) );
      }
    }else {
      Currency currency;

      String stampTxt1 = "";
      String stampTxt2 = "";


        for (int l = 0; l < depAddresses.length; l++) {
          departureFromTextBold = getBoldText(depAddresses[l].getName());
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
                priceText = getBoldText(df.format(TravelStockroomBusiness.getPrice(prices[j].getID(), service.getID(),prices[j].getPriceCategoryID() , prices[j].getCurrencyId(), idegaTimestamp.getTimestampRightNow()) ) );
                currencyText = getBoldText(currency.getCurrencyAbbreviation());
                pTable.add(currencyText,5,pRow);
              }catch (ProductPriceException p) {
                priceText.setText("Rangt upp sett");
              }

              pTable.add(nameOfCategory,3,pRow);
              pTable.add(priceText,4,pRow);
              ++pRow;
            }
          }
          pTable.setHeight(pRow, "6");
          ++pRow;
        }

        pTable.setColumnAlignment(1,"left");
        pTable.setColumnAlignment(2,"left");
        pTable.setColumnAlignment(3,"left");
        pTable.setColumnAlignment(4,"right");
        pTable.setColumnAlignment(5,"left");
//        pTable.setHorizontalZebraColored("#FFFFFF","#F1F1F1");

    }

    }catch (SQLException sql) {

    }

    pTable.setCellspacing(0);
    return pTable;
  }
/*
  private Table getServicePriceNew(IWContext iwc, Service service, boolean cutOff) {
      Table pTable = new Table();
        pTable.setCellspacing(0);

    try {
      Text departureFromTextBold = getBoldText("");
      Text timeframeTextBold = getBoldText("");
      Text priceText = getBoldText("");
      Text currencyText = getBoldText("");

      Text nameOfCategory = getText("");

      Product product = ProductBusiness.getProduct(service.getID());
      TravelAddress[] depAddresses = ProductBusiness.getDepartureAddresses(product);
      Timeframe[] timeframes = product.getTimeframes();
      ProductPrice[] prices = null;

      if (cutOff) {
        prices = ProductPrice.getProductPrices(product.getID(), timeframes[0].getID(), depAddresses[0].getID(), true);
        if (prices.length > 0) {
          pTable.add(prices[0].getPriceCategory().getName()+Text.NON_BREAKING_SPACE+Text.NON_BREAKING_SPACE+df.format(TravelStockroomBusiness.getPrice(prices[0].getID(), service.getID(),prices[0].getPriceCategoryID() , prices[0].getCurrencyId(), idegaTimestamp.getTimestampRightNow(), timeframes[0].getID(), depAddresses[0].getID()) ) );
        }
      }else {
        Currency currency;

        String stampTxt1 = "";
        String stampTxt2 = "";

        int pRow = 1;
        for (int l = 0; l < depAddresses.length; l++) {
          departureFromTextBold = getBoldText(depAddresses[l].getName());
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
      }
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }

    return pTable;
  }
*/

/*
  public Table getProductInfoTable(IWContext iwc, IWResourceBundle iwrb, Product product) throws SQLException, ServiceNotFoundException, TimeframeNotFoundException {
    Image imageToClone = iwrb.getImage("images/picture.gif");
    Image image;

    Service service;
    Timeframe timeframe;
    Address depAddress;
    Address arrAddress;

    idegaTimestamp depTimeStamp;
    idegaTimestamp arrTimeStamp;

    ProductPrice[] prices;
    Currency currency;


    int contRow = 0;
    Table contentTable = new Table();

    int[] dayOfWeek = new int[] {};

    nameText.setText(iwrb.getLocalizedString("travel.name_of_trip","Name of trip"));
    timeframeText.setText(iwrb.getLocalizedString("travel.timeframe_only","Timeframe"));
    departureFromText.setText(iwrb.getLocalizedString("travel.departure_from","Departure from"));
    departureTimeText.setText(iwrb.getLocalizedString("travel.departure_time","Departure time"));
    arrivalFromText.setText(iwrb.getLocalizedString("travel.arrival_at","Arrival at"));
    arrivalFromText.addToText(":");
    arrivalTimeText.setText(iwrb.getLocalizedString("travel.arrival_time","Arrival time"));
    activeDaysText.setText(iwrb.getLocalizedString("travel.active_days","Active days"));

    service = TravelStockroomBusiness.getService(product);
    timeframe = TravelStockroomBusiness.getTimeframe(product);
    depAddress = TourBusiness.getDepartureAddress(service);
    arrAddress = TourBusiness.getArrivalAddress(service);
    if (product.getFileId() != -1) {
      image = new Image(product.getFileId());
      image.setMaxImageWidth(138);
    }
    else{
      image = (Image) imageToClone.clone();
    }

    prodName.setText(service.getName());

    if (timeframe == null) {
      stampTxt1 = iwrb.getLocalizedString("travel.not_configured","Not configured");
      timeframeTxt.addToText(stampTxt1);
    }
    else {
      stampTxt1 = new idegaTimestamp(timeframe.getFrom()).getLocaleDate(iwc);
      stampTxt2 = new idegaTimestamp(timeframe.getTo()).getLocaleDate(iwc);
      try {
        if (timeframe.getIfYearly() ){
          stampTxt1 = stampTxt1.substring(0, stampTxt1.length() -4);
          stampTxt2 = stampTxt2.substring(0, stampTxt2.length() -4);
        }
      }catch (ArrayIndexOutOfBoundsException ai) {}
      timeframeTxt.setText(stampTxt1 + " - ");
      timeframeTxt.addToText(Text.BREAK);
      timeframeTxt.addToText(stampTxt2);
    }

    if (depAddress != null){
      depFrom.setText(depAddress.getStreetName());
    }

    depTimeStamp = new idegaTimestamp(service.getDepartureTime());

    depTime.setText(TextSoap.addZero(depTimeStamp.getHour())+":"+TextSoap.addZero(depTimeStamp.getMinute()));

    if (arrAddress != null){
      arrFrom.setText(arrAddress.getStreetName());
    }

    arrTimeStamp = new idegaTimestamp(service.getArrivalTime());

    arrTime.setText(TextSoap.addZero(arrTimeStamp.getHour())+":"+TextSoap.addZero(arrTimeStamp.getMinute()));

    actDays = (Text) theBoldText.clone();
      actDays.setFontColor(super.BLACK);

    dayOfWeek = ServiceDay.getDaysOfWeek(service.getID());
    for (int j = 0; j < dayOfWeek.length; j++) {
      if (j > 0) actDays.addToText(", ");
      actDays.addToText(dayOfWeekName[dayOfWeek[j]]);
    }

    prices = ProductPrice.getProductPrices(service.getID(), false);

    for (int j = 0; j < prices.length; j++) {
      currency = new Currency(prices[j].getCurrencyId());
      nameOfCategory.setText(prices[j].getPriceCategory().getName());
      try {
        priceText.setText(df.format(tsb.getPrice(prices[j].getID(),service.getID(),prices[j].getPriceCategoryID() , prices[j].getCurrencyId(), idegaTimestamp.getTimestampRightNow()) ) );
        priceText.addToText(currency.getCurrencyAbbreviation());
      }catch (ProductPriceException p) {
        priceText.setText("T - rangt upp sett");
      }

      if (prices[j].getPriceType() == ProductPrice.PRICETYPE_DISCOUNT) {
        priceText.addToText(Text.NON_BREAKING_SPACE+"("+prices[j].getPrice()+"%)");
      }

    }
  }
*/
}
