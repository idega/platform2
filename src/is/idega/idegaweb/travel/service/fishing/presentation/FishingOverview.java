package is.idega.idegaweb.travel.service.fishing.presentation;
import is.idega.idegaweb.travel.presentation.TravelCurrencyCalculatorWindow;
import com.idega.presentation.text.Link;
import java.util.*;
import is.idega.idegaweb.travel.presentation.TravelManager;
import com.idega.idegaweb.IWBundle;
import is.idega.idegaweb.travel.data.*;
import com.idega.block.trade.stockroom.data.*;
import com.idega.business.IBOLookup;
import is.idega.idegaweb.travel.business.*;
import javax.ejb.FinderException;
import is.idega.idegaweb.travel.service.presentation.AbstractServiceOverview;
import java.rmi.RemoteException;
import java.sql.SQLException;

import com.idega.block.trade.data.Currency;
import com.idega.block.trade.stockroom.business.ProductPriceException;
import com.idega.core.location.data.Address;
import com.idega.data.IDOFinderException;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.util.IWCalendar;
import com.idega.util.IWTimestamp;
import com.idega.util.text.TextSoap;

/**
 * <p>Title: idegaWeb TravelBooking</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: idega</p>
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class FishingOverview extends AbstractServiceOverview {

  public FishingOverview(IWContext iwc) throws RemoteException{
    super.main(iwc);
  }

  public void main(IWContext iwc) throws RemoteException {
    super.main(iwc);
  }

  public Table getServiceInfoTable(IWContext iwc, Product product) throws IDOFinderException, SQLException, ServiceNotFoundException, TimeframeNotFoundException, RemoteException{
        Table contentTable;
        int contRow = 0;
        contentTable = new Table();

      int[] dayOfWeek = new int[] {};
      IWCalendar iwCal;

        Text nameText = (Text) theText.clone();
            nameText.setText(_iwrb.getLocalizedString("travel.name_of_product","Name of product"));
            nameText.addToText(":");
            nameText.setFontColor(super.BLACK);
        Text timeframeText = (Text) theText.clone();
            timeframeText.setText(_iwrb.getLocalizedString("travel.timeframe_only","Timeframe"));
            timeframeText.addToText(":");
            timeframeText.setFontColor(super.BLACK);
        Text departureFromText = (Text) theText.clone();
            departureFromText.setText(_iwrb.getLocalizedString("travel.departure_from","Departure from"));
            departureFromText.addToText(":");
            departureFromText.setFontColor(super.BLACK);
        Text departureTimeText = (Text) theText.clone();
            departureTimeText.setText(_iwrb.getLocalizedString("travel.departure_time","Departure time"));
            departureTimeText.addToText(":");
            departureTimeText.setFontColor(super.BLACK);
        Text arrivalFromText = (Text) theText.clone();
            arrivalFromText.setText(_iwrb.getLocalizedString("travel.arrival_at","Arrival at"));
            arrivalFromText.addToText(":");
            arrivalFromText.setFontColor(super.BLACK);
        Text arrivalTimeText = (Text) theText.clone();
            arrivalTimeText.setText(_iwrb.getLocalizedString("travel.arrival_time","Arrival time"));
            arrivalTimeText.addToText(":");
            arrivalTimeText.setFontColor(super.BLACK);
        Text activeDaysText = (Text) theText.clone();
            activeDaysText.setText(_iwrb.getLocalizedString("travel.active_days","Active days"));
            activeDaysText.addToText(":");
            activeDaysText.setFontColor(super.BLACK);

        Image imageToClone = _iwrb.getImage("images/picture.gif");
        Image image;

        Service service;
        Timeframe[] timeframes;
        List depAddresses;
        TravelAddress depAddress;
        Address arrAddress;

        IWTimestamp depTimeStamp;
        IWTimestamp arrTimeStamp;
        Text prodName;
        Text timeframeTxt;
        Text depFrom;
        Text arrFrom;
        Text arrTime;
        Text actDays;

        Text nameOfCategory;
        Text priceText;
        ProductPrice[] prices;
        Currency currency;

        String stampTxt1;
        String stampTxt2;



        service = getTravelStockroomBusiness(iwc).getService(product);
        timeframes = product.getTimeframes();
        try {
          depAddresses = product.getDepartureAddresses(true);
        }catch (IDOFinderException ido) {
          ido.printStackTrace(System.err);
          depAddresses = new Vector();
        }
        depAddress = getProductBusiness(iwc).getDepartureAddress(product);
        arrAddress = getProductBusiness(iwc).getArrivalAddress(product);
        if (product.getFileId() != -1) {
          image = new Image(product.getFileId());
          image.setMaxImageWidth(138);
        }else{
          image = (Image) imageToClone.clone();
        }
        prodName = (Text) theBoldText.clone();
            prodName.setText(getProductBusiness(iwc).getProductNameWithNumber(product));
            prodName.setFontColor(super.BLACK);


        if (service.getDepartureTime() != null) {
          depTimeStamp = new IWTimestamp(service.getDepartureTime());
        }

        arrFrom = (Text) theBoldText.clone();
            arrFrom.setFontColor(super.BLACK);
        if (arrAddress != null)
            arrFrom.setText(arrAddress.getStreetName());

        arrTime = (Text) theBoldText.clone();
            arrTime.setFontColor(super.BLACK);
        if (service.getArrivalTime() != null) {
          arrTimeStamp = new IWTimestamp(service.getArrivalTime());
          arrTime.setText(TextSoap.addZero(arrTimeStamp.getHour())+":"+TextSoap.addZero(arrTimeStamp.getMinute()));
        }

        actDays = (Text) theBoldText.clone();
            actDays.setFontColor(super.BLACK);

        ++contRow;
        contentTable.mergeCells(1,contRow,1,contRow+3);
        contentTable.add(image,1,contRow);
        contentTable.setVerticalAlignment(1,contRow,"top");
        contentTable.add(nameText,2,contRow);
        contentTable.setVerticalAlignment(2,contRow,"top");
        contentTable.setVerticalAlignment(3,contRow,"top");
        contentTable.setVerticalAlignment(4,contRow,"top");
        contentTable.setVerticalAlignment(5,contRow,"top");
        contentTable.setAlignment(2,contRow,"right");
        contentTable.setAlignment(3,contRow,"left");
        contentTable.setAlignment(4,contRow,"right");
        contentTable.setAlignment(5,contRow,"left");
        contentTable.add(prodName,3,contRow);
        contentTable.setRowColor(contRow, super.GRAY);

        try {
          ServiceDayHome sdayHome = (ServiceDayHome) IDOLookup.getHome(ServiceDay.class);
          dayOfWeek = sdayHome.getDaysOfWeek(((Integer) service.getPrimaryKey()).intValue());
          if (dayOfWeek.length == 7) {
            actDays.setText(_iwrb.getLocalizedString("travel.daily","daily"));
          }else {
            for (int j = 0; j < dayOfWeek.length; j++) {
              if (j > 0) actDays.addToText(", ");
              actDays.addToText(dayOfWeekName[dayOfWeek[j]]);
            }
          }
        }catch (Exception e) {
          e.printStackTrace(System.err);
        }

        contentTable.add(activeDaysText,4,contRow);
        contentTable.add(actDays,5,contRow);

        ++contRow;

        for (int l = 0; l < depAddresses.size(); l++) {
          depAddress = (TravelAddress) depAddresses.get(l);
          depFrom = (Text) theBoldText.clone();
          depFrom.setFontColor(super.BLACK);
          depFrom.setText(depAddress.getName());

          contentTable.setVerticalAlignment(2,contRow,"top");
          contentTable.setVerticalAlignment(3,contRow,"top");
          contentTable.setVerticalAlignment(4,contRow,"top");
          contentTable.setVerticalAlignment(5,contRow,"top");
          contentTable.add(departureFromText,2,contRow);

          contentTable.setAlignment(2,contRow,"right");
          contentTable.setAlignment(3,contRow,"left");
          contentTable.setAlignment(4,contRow,"right");
          contentTable.setAlignment(5,contRow,"left");
          contentTable.add(depFrom,3,contRow);

          contentTable.setRowColor(contRow, super.GRAY);
          ++contRow;
          for (int k = 0; k < timeframes.length; k++) {
            prices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(product.getID(), timeframes[k].getID(), depAddress.getID(), new int[] {PriceCategoryBMPBean.PRICE_VISIBILITY_BOTH_PRIVATE_AND_PUBLIC});
            if (prices.length > 0) {
              timeframeTxt = (Text) theBoldText.clone();
                timeframeTxt.setFontColor(super.BLACK);
                if (timeframes.length == 0) {
                  stampTxt1 = _iwrb.getLocalizedString("travel.not_configured","Not configured");
                  timeframeTxt.addToText(stampTxt1);
                }else {
                  iwCal = new IWCalendar(new IWTimestamp(timeframes[k].getFrom()));
                  stampTxt1 = iwCal.getLocaleDate();
                  iwCal = new IWCalendar(new IWTimestamp(timeframes[k].getTo()));
                  stampTxt2 = iwCal.getLocaleDate();
                  try {
                    if (timeframes[0].getIfYearly() ){
                      stampTxt1 = stampTxt1.substring(0, stampTxt1.length() -4);
                      stampTxt2 = stampTxt2.substring(0, stampTxt2.length() -4);
                    }
                  }catch (ArrayIndexOutOfBoundsException ai) {}
                  timeframeTxt.setText(stampTxt1 + " - ");
                  timeframeTxt.addToText(stampTxt2);
                }
              contentTable.setVerticalAlignment(2,contRow,"top");
              contentTable.setVerticalAlignment(3,contRow,"top");
              contentTable.setVerticalAlignment(4,contRow,"top");
              contentTable.setVerticalAlignment(5,contRow,"top");
              contentTable.setAlignment(2,contRow,"right");
              contentTable.setAlignment(3,contRow,"left");
              contentTable.add(timeframeTxt,3,contRow);
              contentTable.setRowColor(contRow, super.GRAY);
            }

            for (int j = 0; j < prices.length; j++) {
            	try {
              	currency = ((com.idega.block.trade.data.CurrencyHome)com.idega.data.IDOLookup.getHomeLegacy(Currency.class)).findByPrimaryKeyLegacy(prices[j].getCurrencyId());
            	}catch (Exception e) {
            		currency = null;	
            	}
              nameOfCategory = (Text) theText.clone();
                nameOfCategory.setFontColor(super.BLACK);
                nameOfCategory.setText(prices[j].getPriceCategory().getName());
                nameOfCategory.addToText(":");
              priceText = (Text) theBoldText.clone();
                priceText.setFontColor(super.BLACK);
              try {
                if (service == null) {debug("SERVICE");}
                if (prices[j] == null) {debug("PRICES");}
                if (timeframes[k] == null) {debug("TIMEFRAMEs");}
                if (depAddress == null) {debug("ADDRESS");}
                priceText.setText(Integer.toString( (int) getTravelStockroomBusiness(iwc).getPrice(prices[j].getID(),((Integer) service.getPrimaryKey()).intValue(),prices[j].getPriceCategoryID() , prices[j].getCurrencyId(), IWTimestamp.getTimestampRightNow(), timeframes[k].getID(), depAddress.getID() ) ));
                priceText.addToText(Text.NON_BREAKING_SPACE);
                priceText.addToText(currency.getCurrencyAbbreviation());
              }catch (Exception p) {
                priceText.setText("Rangt upp sett");
              }

              if (prices[j].getPriceType() == com.idega.block.trade.stockroom.data.ProductPriceBMPBean.PRICETYPE_DISCOUNT) {
                priceText.addToText(Text.NON_BREAKING_SPACE+"("+prices[j].getPrice()+"%)");
              }

              contentTable.setVerticalAlignment(4,contRow,"top");
              contentTable.setVerticalAlignment(5,contRow,"top");
              contentTable.setAlignment(4,contRow,"right");
              contentTable.setAlignment(5,contRow,"left");

              contentTable.add(nameOfCategory,4,contRow);
              contentTable.add(priceText,5,contRow);
              contentTable.setRowColor(contRow, super.GRAY);
              ++contRow;
            }

          }
        }

        contentTable.setVerticalAlignment(2,contRow,"top");
        contentTable.setVerticalAlignment(3,contRow,"top");
        contentTable.setVerticalAlignment(4,contRow,"top");
        contentTable.setVerticalAlignment(5,contRow,"top");
        contentTable.add(arrivalFromText,2,contRow);
        contentTable.add(arrivalTimeText,4,contRow);
        contentTable.setAlignment(2,contRow,"right");
        contentTable.setAlignment(3,contRow,"left");
        contentTable.setAlignment(4,contRow,"right");
        contentTable.setAlignment(5,contRow,"left");
        contentTable.add(arrFrom,3,contRow);
        contentTable.add(arrTime,5,contRow);
        contentTable.setRowColor(contRow, super.GRAY);



        contentTable.setWidth("100%");
        contentTable.setBorder(0);
        contentTable.setAlignment("center");
        contentTable.setWidth(1,"138");
        contentTable.setWidth(2,"90");
        contentTable.setWidth(4,"130");
        contentTable.setWidth(5,"110");
        contentTable.setCellspacing(1);
        contentTable.setColor(super.WHITE);

    return contentTable;
  }
  public Table getPublicServiceInfoTable(IWContext iwc, Product product) throws RemoteException, FinderException{
    TravelSessionManager tsm = (TravelSessionManager) IBOLookup.getSessionInstance(iwc, TravelSessionManager.class);
    Supplier supplier =( (SupplierHome) IDOLookup.getHome(Supplier.class)).findByPrimaryKey(product.getSupplierId());
    IWBundle bundle = tsm.getIWBundle();
    Service service = ((ServiceHome) IDOLookup.getHome(Service.class)).findByPrimaryKey(product.getPrimaryKey() );

    Image background = bundle.getImage("images/sb_background.gif");
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
      Timeframe[] timeframes = product.getTimeframes();
      Currency currency;

      Text nameText = getText(_iwrb.getLocalizedString("travel.name","Name"));
      Text daysText = getText(_iwrb.getLocalizedString("travel.active_days","Active days"));
      Text timeframeText = getText(_iwrb.getLocalizedString("travel.timeframe","Timeframe"));
      Text supplierText = getText(_iwrb.getLocalizedString("travel.supplier","Supplier"));
      Text departureFromText = getText(_iwrb.getLocalizedString("travel.departure_from","Departure from"));
      Text departureTimeText = getText(_iwrb.getLocalizedString("travel.departure_time","Departure time"));
      Text pricesText = getText(_iwrb.getLocalizedString("travel.prices","Prices"));
      Image image = TravelManager.getDefaultImage(_iwrb);
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
		  Address a = supplier.getAddress();
		  if (a != null) {
		  		supplierTextBold.addToText(", "+a.getStreetName());
		  		if (a.getStreetNumber() != null) {
		  			supplierTextBold.addToText(" "+a.getStreetNumber());
		  		}
		  }

		  departureFromTextBold.setText(depAddress.getName());
      departureTimeTextBold.setText(TextSoap.addZero(depTimeStamp.getHour())+":"+TextSoap.addZero(depTimeStamp.getMinute()));

      String[] dayOfWeekName = new String[8];
      IWCalendar cal = new IWCalendar();
      Locale locale = tsm.getLocale();
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
        days = sdayHome.getDaysOfWeek(product.getID());
      }catch (Exception e) {
        e.printStackTrace(System.err);
      }

      if (days.length == 7) {
        daysTextBold.setText(_iwrb.getLocalizedString("travel.daily","daily"));
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


      String stampTxt1 = _iwrb.getLocalizedString("travel.not_configured","Not configured");
      String stampTxt2 = _iwrb.getLocalizedString("travel.not_configured","Not configured");
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
                priceText.setText(_iwrb.getLocalizedString("travel.not_configured","Not configured"));
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

      Link currCalc = new Link(_iwrb.getLocalizedImageButton("travel.currency_calculator","Currency calculator"));
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
    return aroundTable;
  }

  private Text getBoldText(String content) {
    Text text = new Text();
    text.setStyle(TravelManager.theBoldTextStyle);
    text.setBold(true);
    text.setText(content);
    return text;
  }

}