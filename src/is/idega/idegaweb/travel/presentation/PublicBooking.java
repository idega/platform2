package is.idega.idegaweb.travel.presentation;

import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.idegaweb.*;
import com.idega.util.*;
import com.idega.util.text.*;
import is.idega.idegaweb.travel.business.TravelStockroomBusiness;
import java.text.DecimalFormat;

import com.idega.core.data.Address;
import com.idega.block.trade.stockroom.data.*;
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
  Timeframe timeframe;
  Supplier supplier;
  int productId = -1;

  private DecimalFormat df = new DecimalFormat("0.00");
  private Text text = new Text("");
  private Text boldText = new Text("");


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

    String sProductId = iwc.getParameter(LinkGenerator.parameterProductId);
    if (sProductId != null) {
      try {

        productId = Integer.parseInt(sProductId);
        product = new Product(productId);
        if (!product.getIsValid()) {
          throw new SQLException("Product not valid");
        }
        service = new Service(productId);
        timeframe = service.getTimeframe();
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
      Table table = new Table(2,3);
        table.setWidth("90%");
        table.setHeight("90%");
        table.setAlignment("center");
        table.setCellspacing(1);
        table.setColor(TravelManager.WHITE);
        table.setBorder(0);



      Image background = bundle.getImage("images/sb_background.gif");
      table.setBackgroundImage(1, 2, background);
      table.setRowColor(3, TravelManager.backgroundColor);
      table.setColor(2, 1, TravelManager.backgroundColor);
      table.mergeCells(2, 1, 2, 2);
      table.setVerticalAlignment(1,1,"top");
      table.setVerticalAlignment(1,2,"top");
      table.setHeight(1, "20");
      table.setHeight(3, "20");
      table.setWidth(2, "20");


      table.add(leftTop(iwc),1,1);
      table.add(leftBottom(iwc),1,2);
      table.add(right(iwc),2,1);

      add(table);
  }


  private Table right(IWContext iwc)  {
    Table table = new Table(1,2);
      table.setVerticalAlignment(1,1,"top");
      table.setVerticalAlignment(1,2,"top");

    try {
      CalendarHandler ch = new CalendarHandler(iwc);
        ch.setProduct(product);
      table.add(ch.getCalendarTable(iwc));
    }catch (Exception e) {
      e.printStackTrace(System.err);
    }

    return table;
  }

  private Form leftTop(IWContext iwc) {
    Form form = new Form();
//    Table table = new Table();
//    form.add(table);


    try {
      ServiceOverview so = new ServiceOverview(iwc);
        form.add(so.getProductInfoTable(iwc, iwrb, product));
/*
      table.setWidth("95%");
      table.setAlignment("center");
      table.setBorder(1);

      String stampTxt1 = new idegaTimestamp(timeframe.getFrom()).getLocaleDate(iwc);
      String stampTxt2 = new idegaTimestamp(timeframe.getTo()).getLocaleDate(iwc);
      idegaTimestamp depTimeStamp = new idegaTimestamp(service.getDepartureTime());
      Address depAddress = TravelStockroomBusiness.getDepartureAddress(service);
      ProductPrice[] prices = ProductPrice.getProductPrices(service.getID(), false);
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

      Text space = getText(" : ");


      Text nameTextBold = getBoldText("");
      Text timeframeTextBold = getBoldText("");
      Text supplierTextBold = getBoldText("");
      Text departureFromTextBold = getBoldText("");
      Text departureTimeTextBold = getBoldText("");
      Text pricesTextBold = getBoldText("");
      Text nameOfCategory = getBoldText("");
      Text priceText = getBoldText("");

      nameTextBold.setText(product.getName());
      timeframeTextBold.setText(stampTxt1+" - "+stampTxt2);
      supplierTextBold.setText(supplier.getName());
      departureFromTextBold.setText(depAddress.getStreetName());
      departureTimeTextBold.setText(TextSoap.addZero(depTimeStamp.getHour())+":"+TextSoap.addZero(depTimeStamp.getMinute()));


      table.add(nameText,1,1);
      table.add(space,1,1);
      table.add(nameTextBold,1,1);

      table.add(timeframeText,3,1);
      table.add(space,3,1);
      table.add(timeframeTextBold,3,1);

      table.add(supplierText,1,2);
      table.add(space,1,2);
      table.add(supplierTextBold,1,2);

      table.add(departureFromText,3,2);
      table.add(space,3,2);
      table.add(departureFromTextBold,3,2);

      table.add(image,1,3);

      table.add(departureTimeText,3,3);
      table.add(space,3,3);
      table.add(departureTimeTextBold,3,3);

      table.add(Text.NON_BREAKING_SPACE,2,4);

      Table pTable = new Table();
        pTable.setWidth("100%");

        pTable.add(pricesText,1,1);
        pTable.add(space,1,1);
        int pRow = 0;
        for (int j = 0; j < prices.length; j++) {
          ++pRow;
          currency = new Currency(prices[j].getCurrencyId());
          nameOfCategory = getBoldText(prices[j].getPriceCategory().getName());
            nameOfCategory.addToText(":");
          try {
            priceText = getBoldText(df.format(TravelStockroomBusiness.getPrice(prices[j].getID(),service.getID(),prices[j].getPriceCategoryID() , prices[j].getCurrencyId(), idegaTimestamp.getTimestampRightNow()) ) );
            priceText.addToText(Text.NON_BREAKING_SPACE);
            priceText.addToText(currency.getCurrencyAbbreviation());
          }catch (ProductPriceException p) {
            priceText.setText("T - rangt upp sett");
          }

          pTable.add(nameOfCategory,2,pRow);
          pTable.add(priceText,3,pRow);

        }
        pTable.setWidth(1,"2");
        pTable.setWidth(3,"2");
        pTable.setRowAlignment(1,"left");
        pTable.setRowAlignment(2,"left");
        pTable.setRowAlignment(3,"left");

      table.add(pTable,2,5);

      table.setAlignment(3,1,"right");
      table.setAlignment(3,2,"right");
      table.setAlignment(3,3,"right");
      table.setVerticalAlignment(1,3,"top");
      table.mergeCells(1,1,2,1);
      table.mergeCells(1,2,2,2);
      table.mergeCells(1,3,1,5);
      table.mergeCells(2,5,3,5);
      table.setWidth(1,"138");
      table.setWidth(1,"238");
*/

    }catch (Exception e) {
      e.printStackTrace(System.err);
    }
    return form;
  }



  private Form leftBottom(IWContext iwc) {
    try {
      TourBookingForm tbf = new TourBookingForm(iwc);
        tbf.setProduct(product);

      return tbf.getPublicBookingForm();
    }catch (Exception e) {
      e.printStackTrace(System.err);
      return new Form();
    }
  }

}