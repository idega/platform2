package is.idega.idegaweb.travel.service.fishing.presentation;
import is.idega.idegaweb.travel.business.ServiceNotFoundException;
import is.idega.idegaweb.travel.business.TimeframeNotFoundException;
import is.idega.idegaweb.travel.data.Service;
import is.idega.idegaweb.travel.data.ServiceDay;
import is.idega.idegaweb.travel.data.ServiceDayHome;
import is.idega.idegaweb.travel.service.presentation.AbstractServiceOverview;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import com.idega.block.trade.data.Currency;
import com.idega.block.trade.stockroom.business.ProductPriceException;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.ProductPrice;
import com.idega.block.trade.stockroom.data.Timeframe;
import com.idega.block.trade.stockroom.data.TravelAddress;
import com.idega.core.data.Address;
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
          ServiceDay sDay = sdayHome.create();
          dayOfWeek = sDay.getDaysOfWeek(((Integer) service.getPrimaryKey()).intValue());
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
            prices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(product.getID(), timeframes[k].getID(), depAddress.getID(), false);
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
              currency = ((com.idega.block.trade.data.CurrencyHome)com.idega.data.IDOLookup.getHomeLegacy(Currency.class)).findByPrimaryKeyLegacy(prices[j].getCurrencyId());
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
              }catch (ProductPriceException p) {
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

}