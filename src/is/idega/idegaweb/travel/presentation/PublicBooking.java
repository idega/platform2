package is.idega.idegaweb.travel.presentation;

import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.idegaweb.*;
import com.idega.util.*;
import com.idega.util.text.*;
import is.idega.idegaweb.travel.business.TravelStockroomBusiness;
import java.text.DecimalFormat;
import com.idega.block.calendar.business.CalendarBusiness;

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
      Table table = new Table(2,4);
        table.setWidth("90%");
        table.setAlignment("center");
        table.setCellspacing(1);
        table.setColor(TravelManager.WHITE);
        table.setBorder(0);

      Image background = bundle.getImage("images/sb_background.gif");
      Image seeAndBuy = iwrb.getImage("images/see_and_buy.gif");

      table.setBackgroundImage(1, 3, background);
      table.setRowColor(4, backgroundColor);
      table.setColor(2, 2, backgroundColor);
      table.setColor(2, 3, "#CCCCCC");
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
    Text checkAvail = getBoldTextWhite(iwrb.getLocalizedString("travel.check_availability","Check availability and select date by the calendar below"));

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
      aroundTable.setCellpadding(0);
      aroundTable.setCellspacing(0);
      aroundTable.setBackgroundImage(1,1,background);
      aroundTable.setBackgroundImage(2,1,background);
      aroundTable.setBackgroundImage(1,2,background);
      aroundTable.setWidth(1,"1");
      aroundTable.setHeight(1,"1");


    Table table = new Table();
      aroundTable.add(table,2,2);
    form.add(aroundTable);



    try {
//      ServiceOverview so = new ServiceOverview(iwc);
//        form.add(so.getProductInfoTable(iwc, iwrb, product));

      table.setWidth("95%");
      table.setAlignment("center");
      table.setBorder(0);

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

      Image arrow = bundle.getImage("images/black_arrow.gif");
        arrow.setAlignment("center");

      Text space = getText(" : ");


      Text nameTextBold = getBoldText("");
      Text timeframeTextBold = getBoldText("");
      Text supplierTextBold = getBoldText("");
      Text departureFromTextBold = getBoldText("");
      Text departureTimeTextBold = getBoldText("");
      Text pricesTextBold = getBoldText("");
      Text nameOfCategory = getBoldText("");
      Text priceText = getBoldText("");
      Text currencyText = getBoldText("");

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
//      table.add(arrow,1,3);
      table.setAlignment(1,3,"left");

      table.add(departureTimeText,3,3);
      table.add(space,3,3);
      table.add(departureTimeTextBold,3,3);

      table.add(Text.NON_BREAKING_SPACE,2,4);

      Table pTable = new Table();
        pTable.setWidth(300);
        pTable.setCellspacing(0);

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
            currencyText = getBoldText(currency.getCurrencyAbbreviation());
            pTable.add(currencyText,4,pRow);
          }catch (ProductPriceException p) {
            priceText.setText("T - rangt upp sett");
          }

          pTable.add(nameOfCategory,2,pRow);
          pTable.add(priceText,3,pRow);
          if (j%2 == 1) {
            pTable.setColor(2,pRow,"#F1F1F1");
            pTable.setColor(3,pRow,"#F1F1F1");
            pTable.setColor(4,pRow,"#F1F1F1");
          }

        }
        pTable.setWidth(1,"50");
        pTable.setWidth(3,"2");
        pTable.setWidth(4,"2");
        pTable.setColumnAlignment(1,"left");
        pTable.setColumnAlignment(2,"left");
        pTable.setColumnAlignment(3,"right");
        pTable.setColumnAlignment(4,"left");

      table.add(pTable,2,5);
      table.setAlignment(2,5,"right");

      table.setAlignment(3,1,"right");
      table.setAlignment(3,2,"right");
      table.setAlignment(3,3,"right");
      table.setVerticalAlignment(1,3,"top");
      table.mergeCells(1,1,2,1);
      table.mergeCells(1,2,2,2);
      table.mergeCells(1,3,1,5);
      table.mergeCells(2,5,3,5);
//      table.setWidth(1,"138");
      table.setWidth(3,"350");


    }catch (Exception e) {
      e.printStackTrace(System.err);
    }
    return form;
  }



  private Form leftBottom(IWContext iwc) {
    try {
      TourBookingForm tbf = new TourBookingForm(iwc);
        tbf.setProduct(product);

      Form form = new Form();
      String action = iwc.getParameter(this.sAction);
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


      try {
/*        String action = iwc.getParameter(TourBookingForm.BookingAction);
        if (action != null) {
          TourBookingForm tbf = new TourBookingForm(iwc);
            tbf.setProduct(product);
            System.err.println("Created booking_id = "+tbf.handleInsert(iwc));
        }
*/
      }catch (Exception e) {
        e.printStackTrace(System.err);
      }

      return form;
    }catch (Exception e) {
      e.printStackTrace(System.err);
      return new Form();
    }
  }


  private Table getVerifyBookingTable(IWContext iwc) {
    Table table = new Table();

      Text viss = getBoldTextWhite("Ertu viss");
      SubmitButton yes = new SubmitButton(iwrb.getImage("buttons/yes.gif"),this.sAction, this.parameterBookingVerified);
      SubmitButton no = new SubmitButton(iwrb.getImage("buttons/no.gif"),this.sAction, "");
      table.add(viss,1,1);
      table.add(yes,2,1);
      table.add(no,3,1);



    return table;
  }

  private Table doBooking(IWContext iwc) {
    Table table = new Table();
      String ccNumber = iwc.getParameter(TourBookingForm.parameterCCNumber);
      String ccMonth  = iwc.getParameter(TourBookingForm.parameterCCMonth);
      String ccYear   = iwc.getParameter(TourBookingForm.parameterCCYear);

      Text display = getBoldTextWhite("");

      try {
        System.out.println("Starting TPOS test");
        com.idega.block.tpos.business.TPosClient t = new com.idega.block.tpos.business.TPosClient(iwc);
        String heimild = t.doSale(ccNumber,ccMonth,ccYear,20000,"ISK");
        System.out.println("heimild = " + heimild);
        System.out.println("Ending TPOS test");
      }
      catch(com.idega.block.tpos.business.TPosException e) {
        System.out.println("message = " + e.getErrorMessage());
        System.out.println("number = " + e.getErrorNumber());
        System.out.println("display = " + e.getDisplayError());
        display.setText(e.getDisplayError());
      }
      catch (Exception e) {
        e.printStackTrace(System.err);
      }
      table.add(display);

    return table;
  }

}