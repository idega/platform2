package is.idega.idegaweb.travel.presentation;

import com.idega.presentation.Block;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;

import com.idega.presentation.text.*;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.block.text.presentation.TextReader;

import com.idega.block.trade.stockroom.data.*;
import com.idega.block.trade.data.Currency;
import is.idega.idegaweb.travel.data.*;
import com.idega.core.data.*;

import com.idega.util.idegaTimestamp;
import com.idega.util.idegaCalendar;
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
import java.util.Iterator;
import java.util.List;

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
  private int windowHeight = 480;
  private Link link;
  private Text text;
  private Text boldText;
  private boolean showBuyButton = true;
  private boolean showMoreButton = true;
  private boolean listView = false;


  private void init(IWContext iwc) {
    iwb = this.getBundle(iwc);
    iwrb = this.getResourceBundle(iwc);
    super.setWidth(windowWidth);
    super.setHeight(windowHeight);

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
    init(iwc);
    handleEvents(iwc);
  }

 private void handleEvents(IWContext iwc) throws Exception {
    String sService = iwc.getParameter(IW_TRAVEL_SERVICE_ID);
    String sSupplier= iwc.getParameter(IW_TRAVEL_SUPPLIER_ID);

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

      if( (dagur.length == 7) && (!listView) ){
        depart.append("Every day at ");/**localize**/
      }
      else{
        for (int i = 0; i < dagur.length; i++) {
          depart.append(dayOfWeekName[dagur[i]]);
          depart.append(Text.NON_BREAKING_SPACE);
        }
      }

      idegaTimestamp stamp = new idegaTimestamp(serv.getDepartureTime());
      depart.append(stamp.getHour());
      depart.append(":");
      depart.append(TextSoap.addZero(stamp.getMinute()));
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
          desc.setText(ProductBusiness.getProductName(prod));
          content.add(desc,++x,y);

        //active days
          Text days = (Text) text.clone();
          days.setText(getServiceDepartures(serv));
          content.add(days,++x,y);
        //timeframe - trip length
          Text duration = (Text) text.clone();
          duration.setText(getServiceDurationString(serv));
          content.add(duration,++x,y);
        //Price
          Text price = (Text) text.clone();
          price.setText(getServicePrice(serv, timeframe.getID()));
          content.add(price,++x,y);
        //Info and buy buttons
          if( showMoreButton){
            ServiceViewer viewer = new ServiceViewer();
            viewer.showBuyButton(showBuyButton);
            viewer.showMoreButton(showMoreButton);
            Link more = new Link(viewer,iwrb.getLocalizedString("travel.more.button","more"));
            //more.setWindowToOpen(ServiceViewer.class);
            more.addParameter(IW_TRAVEL_SERVICE_ID,prod.getID());
            more.setAsImageButton(true);
            content.add(more,++x,y);
          }

          if( showBuyButton){
            Link buy = LinkGenerator.getLink(iwc,prod.getID());
            buy.setText(iwrb.getLocalizedString("travel.buy.button","buy"));
            buy.setAsImageButton(true);
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

    if( (color1!=null) && (color2!=null) ){
      content.setHorizontalZebraColored(color1,color2);
    }

    return content;
  }

  private Table getServiceInfoTable(IWContext iwc){
    Table content = new Table();
    content.setCellspacing(0);
    content.setCellpadding(2);

    try {
      int y = 1;
      Product product = new Product(service.getID());
      //number
      Text numberAndName = (Text) text.clone();
      numberAndName.setText(product.getNumber()+" - "+ProductBusiness.getProductName(product));
      numberAndName.setBold(true);
      content.add(numberAndName,1,y);
      //description
      TxText descriptionText = product.getText();
      if (descriptionText != null) {
        content.add(new TextReader(product.getText().getID()),1,++y);//insert a textreader
      }

      //active days
        Text day = (Text) boldText.clone();
        day.setText(iwrb.getLocalizedString("travel.serviceviewer.departures","Departures: "));
        Text days = new Text(getServiceDepartures(service));
        content.add(day,1,++y);
        content.add(days,2,y);

      //timeframe - trip length
        Text dur = (Text) boldText.clone();
        dur.setText(iwrb.getLocalizedString("travel.serviceviewer.duration","Duration: "));
        Text duration = new Text(getServiceDurationString(service));
        content.add(dur,1,++y);
        content.add(duration,2,y);
      //Price
          /**
           * @todo Laga fyrir multi timeframes...
           */
        Timeframe timeframe = product.getTimeframe();
        Text price = (Text) boldText.clone();
        price.setText(iwrb.getLocalizedString("travel.serviceviewer.info.price","Price: "));
        Text prices = new Text(getServicePrice(service, timeframe.getID()));
        content.add(price,1,++y);
        content.add(prices,2,y);



      if( showBuyButton){
        Link buy = LinkGenerator.getLink(iwc,service.getID());
        buy.setText(iwrb.getLocalizedString("travel.buy.button","buy"));
        buy.setAsImageButton(true);
        content.add(buy,2,++y);
        content.setAlignment(2,y,"right");
      }
    }
    catch (Exception ex) {
      ex.printStackTrace(System.err);
    }

        content.mergeCells(1,1,2,1);
    content.mergeCells(1,2,2,2);

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


  private String getServicePrice(Service service, int timeframeId){
    StringBuffer price = new StringBuffer();
    /**
     * @todo replace...
     */
    ProductPrice[] prices = ProductPrice.getProductPrices(service.getID(), false);
//    ProductPrice[] prices = ProductPrice.getProductPrices(service.getID(), timeframeId, false);
    Currency currency;

    for (int j = 0; j < prices.length; j++) {
      try {
        if ((j>0)){
           price.append(Text.BREAK);
        }
        currency = new Currency(prices[j].getCurrencyId());
        price.append(prices[j].getPriceCategory().getName());
        price.append(Text.NON_BREAKING_SPACE);
        price.append(TextSoap.decimalFormat(tsb.getPrice(prices[j].getID(),service.getID(),prices[j].getPriceCategoryID(),prices[j].getCurrencyId(),idegaTimestamp.getTimestampRightNow()),2));
        price.append(Text.NON_BREAKING_SPACE);
        price.append(currency.getCurrencyAbbreviation());

      }catch (Exception e) {
        price.append("An error occurred please contact gimmi@idega.is .");
        e.printStackTrace(System.err);
      }

      /*if (prices[j].getPriceType() != ProductPrice.PRICETYPE_DISCOUNT) {
        priceText.addToText(Text.NON_BREAKING_SPACE+"("+prices[j].getPrice()+"%)");
      }*/
    }
    return price.toString();
  }

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
