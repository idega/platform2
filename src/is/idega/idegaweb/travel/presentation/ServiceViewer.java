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

public class ServiceViewer extends Block {

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

  private idegaTimestamp dateFrom;
  private idegaTimestamp dateTo;
  private String width;
  private String height;
  private String color1;
  private String color2;
  private Link link;
  private Text text;

  private void init(IWContext iwc) {
    iwb = this.getBundle(iwc);
    iwrb = this.getResourceBundle(iwc);

    tsb = TravelStockroomBusiness.getNewInstance();
    cal = new idegaCalendar();
    dayOfWeekName = new String[8];
    if(link==null){
      link = new Link();
      link.setAsImageButton(true);/**@todo localize*/
    }
    if(text==null) text = new Text();

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

      for (int i = 0; i < dagur.length; i++) {
        depart.append(dayOfWeekName[dagur[i]]);
        depart.append(Text.NON_BREAKING_SPACE);
      }

      idegaTimestamp stamp = new idegaTimestamp(serv.getDepartureTime());
      depart.append(stamp.getHour());
      depart.append(":");
      depart.append(stamp.getMinute());

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
        duration = hours+"h"+minutesleft+"m";
      }
    }
    return duration;
  }

  private Table getServiceListTable(IWContext iwc){
    List prodlist;
    Table content = new Table();
    if( (color1!=null) && (color2!=null) ){
      content.setHorizontalZebraColored(color1,color2);
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

       while( (iter!=null) && iter.hasNext() ) {
        Product prod = (Product) iter.next();
        try{
          serv = tsb.getService(prod);
        //number
          Text number = (Text) text.clone();
          number.setText(prod.getNumber());
          number.setBold(true);
          content.add(number,x,y);
        //description
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
          price.setText(getServicePrice(serv));
          content.add(price,++x,y);
        //Info and buy buttons
          Link more = (Link)link.clone();
          more.setWindowToOpen(ServiceViewer.class);
          more.addParameter(IW_TRAVEL_SERVICE_ID,prod.getID());
          more.setText("more");
          content.add(more,++x,y);

          Link buy = LinkGenerator.getLink(iwc,prod.getID());
          buy.setText("buy");
          content.add(buy,++x,y);


        }
        catch(Exception ex){
          ex.printStackTrace(System.err);
        }

        y++;
        x = 1;
      }
    }

    return content;
  }

  private Table getServiceInfoTable(IWContext iwc){
    Table content = new Table(1,4);
    try {
      int i = 1;
      Product product = new Product(service.getID());
      //number
      Text numberAndName = (Text) text.clone();
      numberAndName.setText(product.getNumber()+" - "+ProductBusiness.getProductName(product));
      numberAndName.setBold(true);
      content.add(numberAndName,1,i);
      //description
      //content.add(new TextReader(TextFinder.getLocalizedText(product,product.getID(),ICLocaleBusiness.getLocaleId(iwc.getCurrentLocale())).getBody()),1,2);//insert a textreader
      content.add(ProductBusiness.getProductDescription(product),1,++i);/** @todo insert a textreader**/
      content.add("META DATA",1,++i);

      Link buy = LinkGenerator.getLink(iwc,service.getID());
      buy.setAsImageButton(true);
      content.add(buy,1,++i);
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


  private String getServicePrice(Service service){
    StringBuffer price = new StringBuffer();
    ProductPrice[] prices = ProductPrice.getProductPrices(service.getID(), false);
    Currency currency;

    for (int j = 0; j < prices.length; j++) {
      try {
        if (j<0) price.append(Text.BREAK);
        currency = new Currency(prices[j].getCurrencyId());
        price.append(prices[j].getPriceCategory().getName());
        price.append(Text.NON_BREAKING_SPACE);
        price.append(TextSoap.decimalFormat(tsb.getPrice(prices[j].getID(),service.getID(),prices[j].getPriceCategoryID(),prices[j].getCurrencyId(),idegaTimestamp.getTimestampRightNow()),2));
        price.append(Text.NON_BREAKING_SPACE);
        price.append(currency.getCurrencyAbbreviation());

      }catch (Exception e) {
        price.append("rangt upp sett");
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
