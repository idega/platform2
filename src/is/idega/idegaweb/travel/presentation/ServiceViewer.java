package is.idega.idegaweb.travel.presentation;

import is.idega.idegaweb.travel.business.TravelStockroomBusiness;
import is.idega.idegaweb.travel.data.Service;
import is.idega.idegaweb.travel.data.ServiceDay;
import is.idega.idegaweb.travel.data.ServiceDayHome;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.block.text.data.TxText;
import com.idega.block.text.presentation.TextReader;
import com.idega.block.trade.data.Currency;
import com.idega.block.trade.stockroom.business.ProductBusiness;
import com.idega.block.trade.stockroom.business.ProductPriceException;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.ProductHome;
import com.idega.block.trade.stockroom.data.ProductPrice;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.block.trade.stockroom.data.Timeframe;
import com.idega.block.trade.stockroom.data.TravelAddress;
import com.idega.business.IBOLookup;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.localisation.business.LocaleSwitcher;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.HorizontalRule;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Window;
import com.idega.util.IWCalendar;
import com.idega.util.IWTimestamp;
import com.idega.util.LocaleUtil;

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
  public static final String CACHE_KEY = "iw_tr_sv_cachekey_";

  public static final String IW_BUNDLE_IDENTIFIER = "is.idega.travel";
  public static final String IW_TRAVEL_SERVICE_ID = "iw_tr_serv_id";
  public static final String IW_TRAVEL_SUPPLIER_ID = "iw_tr_suppl_id";

  private static final String IW_TRAVEL_ADD_BUY_BUTTON = "iw_tr_add_buy_button";
  private static final String IW_TRAVEL_ADD_MORE_BUTTON = "iw_tr_add_more_button";

  private DecimalFormat df = new DecimalFormat("0.00");

  private IWBundle iwb;
  private IWResourceBundle iwrb;

  private TravelStockroomBusiness tsb;
  private IWCalendar cal;
  private String[] dayOfWeekName;

  private Supplier supplier;
  private Service service;

  private IWTimestamp dateFrom,dateTo;
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
    super.setScrollbar(true);
    this.setAllMargins(4);
    //setAllMargins(0);
  }

  private void init(IWContext iwc) throws RemoteException{
    iwb = this.getBundle(iwc);
    iwrb = this.getResourceBundle(iwc);

    this.setTitle(iwrb.getLocalizedString("book_service","Book service"));

    sService = iwc.getParameter(IW_TRAVEL_SERVICE_ID);
    sSupplier = iwc.getParameter(IW_TRAVEL_SUPPLIER_ID);

    if( sService!=null ){
      if( iwc.getSessionAttribute(IW_TRAVEL_ADD_MORE_BUTTON+sService)!=null ) showMoreButton = true;
      if( iwc.getSessionAttribute(IW_TRAVEL_ADD_BUY_BUTTON+sService)!=null ) showBuyButton = true;
    }

    tsb = getTravelStockroomBusiness(iwc);
    cal = new IWCalendar();
    dayOfWeekName = new String[8];

    link = new Link();
    link.setAsImageButton(true);/**@todo localize check if this gets cloned*/

    text = new Text();
    boldText = new Text();
    boldText.setBold(true);

    Locale locale = iwc.getCurrentLocale();
    dayOfWeekName[is.idega.idegaweb.travel.data.ServiceDayBMPBean.SUNDAY] = cal.getDayName(is.idega.idegaweb.travel.data.ServiceDayBMPBean.SUNDAY ,locale,IWCalendar.LONG).substring(0,3);
    dayOfWeekName[is.idega.idegaweb.travel.data.ServiceDayBMPBean.MONDAY] = cal.getDayName(is.idega.idegaweb.travel.data.ServiceDayBMPBean.MONDAY ,locale,IWCalendar.LONG).substring(0,3);
    dayOfWeekName[is.idega.idegaweb.travel.data.ServiceDayBMPBean.TUESDAY] = cal.getDayName(is.idega.idegaweb.travel.data.ServiceDayBMPBean.TUESDAY ,locale,IWCalendar.LONG).substring(0,3);
    dayOfWeekName[is.idega.idegaweb.travel.data.ServiceDayBMPBean.WEDNESDAY] = cal.getDayName(is.idega.idegaweb.travel.data.ServiceDayBMPBean.WEDNESDAY ,locale,IWCalendar.LONG).substring(0,3);
    dayOfWeekName[is.idega.idegaweb.travel.data.ServiceDayBMPBean.THURSDAY] = cal.getDayName(is.idega.idegaweb.travel.data.ServiceDayBMPBean.THURSDAY ,locale,IWCalendar.LONG).substring(0,3);
    dayOfWeekName[is.idega.idegaweb.travel.data.ServiceDayBMPBean.FRIDAY] = cal.getDayName(is.idega.idegaweb.travel.data.ServiceDayBMPBean.FRIDAY ,locale,IWCalendar.LONG).substring(0,3);
    dayOfWeekName[is.idega.idegaweb.travel.data.ServiceDayBMPBean.SATURDAY] = cal.getDayName(is.idega.idegaweb.travel.data.ServiceDayBMPBean.SATURDAY ,locale,IWCalendar.LONG).substring(0,3);
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
        service = ((is.idega.idegaweb.travel.data.ServiceHome)com.idega.data.IDOLookup.getHome(Service.class)).findByPrimaryKey(new Integer(sService));
      }
      catch (Exception ex) {
        //do nothing
      }
    }
    else if( sSupplier!=null ){//multi view
      try {
        listView = true;
        supplier = ((com.idega.block.trade.stockroom.data.SupplierHome)com.idega.data.IDOLookup.getHomeLegacy(Supplier.class)).findByPrimaryKeyLegacy(Integer.parseInt(sSupplier));
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
      int[] dagur = new int[]{};//is.idega.idegaweb.travel.data.ServiceDayBMPBean.getDaysOfWeek(serv.getID());
      try {
        ServiceDayHome sdayHome = (ServiceDayHome) IDOLookup.getHome(ServiceDay.class);
        dagur = sdayHome.getDaysOfWeek(((Integer) serv.getPrimaryKey()).intValue());
      }catch (Exception e) {
        e.printStackTrace(System.err);
      }

      if( (dagur.length == 7) ){
        depart.append(iwrb.getLocalizedString("every.day","Every day")).append(Text.NON_BREAKING_SPACE);
      }
      else{
        for (int i = 0; i < dagur.length; i++) {
          depart.append(dayOfWeekName[dagur[i]]);
          depart.append(Text.NON_BREAKING_SPACE);
        }
      }

    }
    catch (Exception ex) {
      ex.printStackTrace(System.err);
    }

    return depart.toString();
  }

  private String getServiceDurationString(Service serv) throws RemoteException{
    String duration;

    IWTimestamp stamp = new IWTimestamp(serv.getDepartureTime());
    IWTimestamp stamp2 = new IWTimestamp(serv.getArrivalTime());
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

  private Table getServiceListTable(IWContext iwc) throws RemoteException, FinderException{
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
      prodlist = getProductBusiness(iwc).getProducts(supplier.getID(),dateFrom,dateTo);
    }
    else{
      Collection coll = getProductHome().findProducts(supplier.getID());
      prodlist = new Vector(coll);
//      prodlist = getProductHome().getProducts(supplier.getID());
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



      ProductHome pHome = (ProductHome) IDOLookup.getHome(Product.class);
       while( (iter!=null) && iter.hasNext() ) {
        Product prod = (Product) iter.next();
//        Product prod = (Product) iter.next();
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
          desc.setText(prod.getProductName(iLocaleID));
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
      Product product = getProductBusiness(iwc).getProduct((Integer)service.getPrimaryKey());
      //number
      Text numberAndName = getBoldText(getProductBusiness(iwc).getProductNameWithNumber(product, true, iLocaleID));//.getNumber()+" - "+ProductBusiness.getProductName(product, iLocaleID));
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
        TextReader textReader = new TextReader(descriptionText.getID());
          textReader.setHeadlineStyle("font-family:Arial,Helvetica,sans-serif;font-size:12pt;font-weight:bold;");
          textReader.setTextStyle("font-family:Arial,Helvetica,sans-serif;font-size:8pt;");
          textReader.setCacheable(false);
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
        Link buy = LinkGenerator.getLink(iwc,((Integer) service.getPrimaryKey()).intValue());
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

  public void setDateFrom(IWTimestamp dateFrom){
    this.dateFrom = dateFrom;
  }

  public void setDateTo(IWTimestamp dateTo){
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

  private Table getServicePrice(IWContext iwc, Service service, boolean cutOff) throws RemoteException, FinderException{
    Table pTable = new Table();
      pTable.setWidth(Table.HUNDRED_PERCENT);
    int pRow = 1;

    try {

    Text departureFromTextBold = getBoldText("");
    Text timeframeTextBold = getBoldText("");
    Text priceText = getBoldText("");
    Text currencyText = getBoldText("");

    Text nameOfCategory = getText("");

    Product product = getProductBusiness(iwc).getProduct((Integer)service.getPrimaryKey());
    List depAddresses = product.getDepartureAddresses(true);
    TravelAddress tAddress;
    Timeframe[] timeframes = product.getTimeframes();
    ProductPrice[] prices = null;

    if (cutOff) {
      prices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(product.getID(), timeframes[0].getID(), ((TravelAddress) depAddresses.get(0)).getID(), true);
      if (prices.length > 0) {
        pTable.add(prices[0].getPriceCategory().getName()+Text.NON_BREAKING_SPACE+Text.NON_BREAKING_SPACE+df.format(getTravelStockroomBusiness(iwc).getPrice(prices[0].getID(), ((Integer) service.getPrimaryKey()).intValue(),prices[0].getPriceCategoryID() , prices[0].getCurrencyId(), IWTimestamp.getTimestampRightNow(), timeframes[0].getID(),  ((TravelAddress) depAddresses.get(0)).getID()) ) );
      }
    }else {
      Currency currency;

      String stampTxt1 = "";
      String stampTxt2 = "";


//        for (int l = 0; l < depAddresses.length; l++) {
        Iterator iter = depAddresses.iterator();
        while (iter.hasNext()) {
          tAddress = (TravelAddress) iter.next();
          departureFromTextBold = getBoldText(tAddress.getName());
            departureFromTextBold.addToText(Text.NON_BREAKING_SPACE+Text.NON_BREAKING_SPACE);
          pTable.add(departureFromTextBold, 1, pRow);
          for (int i = 0; i < timeframes.length; i++) {
            prices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(product.getID(), timeframes[i].getID(), tAddress.getID(), true);
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
                  priceText = getBoldText(df.format(getTravelStockroomBusiness(iwc).getPrice(prices[j].getID(),((Integer) service.getPrimaryKey()).intValue(),prices[j].getPriceCategoryID() , prices[j].getCurrencyId(), IWTimestamp.getTimestampRightNow()) ) );
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

  protected ProductBusiness getProductBusiness(IWApplicationContext iwac) throws RemoteException {
    return (ProductBusiness) IBOLookup.getServiceInstance(iwac, ProductBusiness.class);
  }
  protected TravelStockroomBusiness getTravelStockroomBusiness(IWApplicationContext iwac) throws RemoteException {
    return (TravelStockroomBusiness) IBOLookup.getServiceInstance(iwac, TravelStockroomBusiness.class);
  }
  protected ProductHome getProductHome() throws RemoteException {
    return (ProductHome) IDOLookup.getHome(Product.class);
  }
}

