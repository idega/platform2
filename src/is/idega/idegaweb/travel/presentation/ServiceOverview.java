package is.idega.idegaweb.travel.presentation;

import com.idega.presentation.Block;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.text.*;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.block.trade.stockroom.data.*;
import com.idega.block.calendar.presentation.SmallCalendar;
import com.idega.util.idegaTimestamp;
import com.idega.util.idegaCalendar;
import com.idega.util.text.TextSoap;
import com.idega.core.accesscontrol.business.AccessControl;
import is.idega.idegaweb.travel.business.TravelStockroomBusiness;
import java.sql.SQLException;
import com.idega.block.trade.data.Currency;
import com.idega.block.trade.stockroom.business.*;
import java.text.DecimalFormat;

import is.idega.idegaweb.travel.business.TravelStockroomBusiness.*;

import is.idega.idegaweb.travel.data.*;
import com.idega.core.data.*;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class ServiceOverview extends TravelManager {

  private IWBundle bundle;
  private IWResourceBundle iwrb;

  private String actionParameter = "service_overview_action";
  private String deleteParameter = "service_to_delete_id";

  private Supplier supplier;

  private idegaCalendar cal = new idegaCalendar();
  TravelStockroomBusiness tsb = TravelStockroomBusiness.getNewInstance();
  String[] dayOfWeekName = new String[8];


  public ServiceOverview() {
  }

  public ServiceOverview(IWContext iwc)throws Exception {
    super.main(iwc);
    init(iwc);
  }

  public void add(PresentationObject mo) {
    super.add(mo);
  }


  public void main(IWContext iwc) throws Exception{
      super.main(iwc);
      supplier = super.getSupplier();
      init(iwc);

      if (super.isLoggedOn(iwc) ) {

          String action = iwc.getParameter(actionParameter);
          if (action == null) {action = "";}

          if (action.equals("")) {
              displayForm(iwc);
          }else if (action.equals("delete")) {
              deleteServices(iwc);
              displayForm(iwc);
          }

          super.addBreak();
      }else {
        add(super.getLoggedOffTable(iwc));
      }
      //super.add(tm);
  }

  private void init(IWContext iwc) {
      bundle = super.getBundle();
      iwrb = super.getResourceBundle();
      dayOfWeekName[ServiceDay.SUNDAY] = cal.getNameOfDay(ServiceDay.SUNDAY ,iwc).substring(0,3);
      dayOfWeekName[ServiceDay.MONDAY] = cal.getNameOfDay(ServiceDay.MONDAY ,iwc).substring(0,3);
      dayOfWeekName[ServiceDay.TUESDAY] = cal.getNameOfDay(ServiceDay.TUESDAY ,iwc).substring(0,3);
      dayOfWeekName[ServiceDay.WEDNESDAY] = cal.getNameOfDay(ServiceDay.WEDNESDAY ,iwc).substring(0,3);
      dayOfWeekName[ServiceDay.THURSDAY] = cal.getNameOfDay(ServiceDay.THURSDAY ,iwc).substring(0,3);
      dayOfWeekName[ServiceDay.FRIDAY] = cal.getNameOfDay(ServiceDay.FRIDAY ,iwc).substring(0,3);
      dayOfWeekName[ServiceDay.SATURDAY] = cal.getNameOfDay(ServiceDay.SATURDAY ,iwc).substring(0,3);
  }

  public Table getTopTable(IWContext iwc) {
      Table topTable = new Table(4,2);
        topTable.setBorder(0);
        topTable.setWidth("90%");

      DateInput active_from = new DateInput("active_from");
          idegaTimestamp fromStamp = getFromIdegaTimestamp(iwc);
          active_from.setDate(fromStamp.getSQLDate());
      DateInput active_to = new DateInput("active_to");
          idegaTimestamp toStamp = getToIdegaTimestamp(iwc);
          active_to.setDate(toStamp.getSQLDate());

      Text tfFromText = (Text) theText.clone();
          tfFromText.setText(iwrb.getLocalizedString("travel.from","from"));
      Text tfToText = (Text) theText.clone();
          tfToText.setText(iwrb.getLocalizedString("travel.to","to"));


      Text timeframeText = (Text) theText.clone();
          timeframeText.setText(iwrb.getLocalizedString("travel.timeframe_only","Timeframe"));
          timeframeText.addToText(":");

      topTable.setColumnAlignment(1,"right");
      topTable.setColumnAlignment(2,"left");
      topTable.add(timeframeText,1,1);
      topTable.add(tfFromText,1,1);
      topTable.add(active_from,2,1);
      topTable.add(tfToText,2,1);
      topTable.add(active_to,2,1);
      topTable.mergeCells(2,1,4,1);
//    topTable.mergeCells(2,2,4,2);



      topTable.setAlignment(4,2,"right");
      topTable.add(new SubmitButton("TEMP-Sækja"),4,2);

      return topTable;
  }

  public void deleteServices(IWContext iwc) throws SQLException{
    String[] serviceIds = (String[]) iwc.getParameterValues(deleteParameter);
    Service serviceToDelete;
    for (int i = 0; i < serviceIds.length; i++) {
        serviceToDelete = new Service(Integer.parseInt(serviceIds[i]));
        serviceToDelete.delete();
    }

  }


  public void displayForm(IWContext iwc) {
      add(Text.getBreak());
      Form form = new Form();
      Table topTable = this.getTopTable(iwc);
        form.add(Text.BREAK);
      Table table = new Table();
        table.setBorder(0);
        form.add(table);

      table.setWidth("90%");
      String sYear = iwc.getParameter("year");
      if (sYear == null) {
          sYear = Text.emptyString().toString();
      }

      int row = 0;
      idegaTimestamp stamp = idegaTimestamp.RightNow();



      Link delete;
      Link getLink;
      Link bookClone = new Link(iwrb.getImage("/buttons/book.gif"),Booking.class);
        bookClone.addParameter(super.sAction, super.parameterBooking);
      Link editClone = new Link(iwrb.getImage("/buttons/change.gif"),ServiceDesigner.class);
        editClone.addParameter(super.sAction, super.parameterServiceDesigner);

      Link book;
      Link edit;



      Supplier supplier = super.getSupplier();
      if (supplier != null) {
        //Product[] products = tsb.getProducts(supplier.getID(), this.getFromIdegaTimestamp(iwc), this.getToIdegaTimestamp(iwc));

        Product[] products = tsb.getProducts(supplier.getID());


        Table contentTable;
        is.idega.idegaweb.travel.presentation.ServiceViewer sv = new is.idega.idegaweb.travel.presentation.ServiceViewer();
          sv.setSupplier(supplier);
          sv.setZebraColors("#FFFFFF","#CCCCCC");
//        table.add(sv);

        for (int i = 0; i < products.length; i++) {
          try {
            contentTable = getProductInfoTable(iwc,iwrb,products[i]);

            ++row;
            table.mergeCells(1,row,5,row);
            table.add(contentTable,1,row);



            ++row;
            table.mergeCells(1,row,5,row);
            table.setAlignment(1,row,"right");

            getLink = new Link(iwrb.getImage("buttons/link.gif"));
              getLink.setWindowToOpen(LinkGenerator.class);
              getLink.addParameter(LinkGenerator.parameterProductId ,products[i].getID());

            delete = new Link(iwrb.getImage("buttons/delete.gif"));
              delete.addParameter(actionParameter,"delete");
              delete.addParameter(deleteParameter,products[i].getID());

            book = (Link) bookClone.clone();
              book.addParameter(Booking.parameterProductId,products[i].getID());

            edit = (Link) editClone.clone();
              edit.addParameter(ServiceDesigner.parameterUpdateServiceId, products[i].getID());

            if (super.isInPermissionGroup) {
              table.add(edit,1,row);
              table.add("&nbsp;&nbsp;",1,row);
            }
            table.add(book,1,row);
            table.add("&nbsp;&nbsp;",1,row);
            if (this.supplier != null) {
              table.add(getLink,1,row);
              table.add("&nbsp;&nbsp;",1,row);
            }
            if (super.isInPermissionGroup) {
              table.add(delete,1,row);
            }


            table.setColor(1,row,super.backgroundColor);
            ++row;
            table.mergeCells(1,row,5,row);
            table.setColor(1,row,super.backgroundColor);
            HorizontalRule hr = new HorizontalRule("100%",1);
              hr.setAlignment(hr.ALIGN_CENTER);
              hr.setNoShade(true);
              hr.setColor(super.textColor);

            ++row;
            table.mergeCells(1,row,5,row);
          }catch (TravelStockroomBusiness.ServiceNotFoundException snf) {
            snf.printStackTrace(System.err);
          }catch (TravelStockroomBusiness.TimeframeNotFoundException tnf) {
            tnf.printStackTrace(System.err);
          }catch (SQLException sql) {
            sql.printStackTrace(System.err);
          }
        }

      }

      add(form);
  }


  // BUSINESS
  public idegaTimestamp getFromIdegaTimestamp(IWContext iwc) {
      idegaTimestamp stamp = null;
      String from_time = iwc.getParameter("active_from");
      if (from_time!= null) {
          stamp = new idegaTimestamp(from_time);
      }
      else {
          stamp = idegaTimestamp.RightNow();
      }
      return stamp;
  }

  // BUSINESS
  public idegaTimestamp getToIdegaTimestamp(IWContext iwc) {
      idegaTimestamp stamp = null;
      String from_time = iwc.getParameter("active_to");
      if (from_time!= null) {
          stamp = new idegaTimestamp(from_time);
      }
      else {
          stamp = idegaTimestamp.RightNow();
          stamp.addDays(15);
      }
      return stamp;
  }



  public Table getProductInfoTable(IWContext iwc, IWResourceBundle iwrb, Product product) throws SQLException, ServiceNotFoundException, TimeframeNotFoundException {
        Table contentTable;
        int contRow = 0;
        contentTable = new Table();

      DecimalFormat df = new DecimalFormat("0.00");
      int[] dayOfWeek = new int[] {};

        Text nameText = (Text) theText.clone();
            nameText.setText(iwrb.getLocalizedString("travel.name_of_trip","Name of trip"));
            nameText.addToText(":");
            nameText.setFontColor(super.BLACK);
        Text timeframeText = (Text) theText.clone();
            timeframeText.setText(iwrb.getLocalizedString("travel.timeframe_only","Timeframe"));
            timeframeText.addToText(":");
            timeframeText.setFontColor(super.BLACK);
        Text departureFromText = (Text) theText.clone();
            departureFromText.setText(iwrb.getLocalizedString("travel.departure_from","Departure from"));
            departureFromText.addToText(":");
            departureFromText.setFontColor(super.BLACK);
        Text departureTimeText = (Text) theText.clone();
            departureTimeText.setText(iwrb.getLocalizedString("travel.departure_time","Departure time"));
            departureTimeText.addToText(":");
            departureTimeText.setFontColor(super.BLACK);
        Text arrivalFromText = (Text) theText.clone();
            arrivalFromText.setText(iwrb.getLocalizedString("travel.arrival_at","Arrival at"));
            arrivalFromText.addToText(":");
            arrivalFromText.setFontColor(super.BLACK);
        Text arrivalTimeText = (Text) theText.clone();
            arrivalTimeText.setText(iwrb.getLocalizedString("travel.arrival_time","Arrival time"));
            arrivalTimeText.addToText(":");
            arrivalTimeText.setFontColor(super.BLACK);
        Text activeDaysText = (Text) theText.clone();
            activeDaysText.setText(iwrb.getLocalizedString("travel.active_days","Active days"));
            activeDaysText.addToText(":");
            activeDaysText.setFontColor(super.BLACK);

        Image imageToClone = iwrb.getImage("images/picture.gif");
        Image image;

        Service service;
        Timeframe[] timeframes;
        Address depAddress;
        Address arrAddress;

        idegaTimestamp depTimeStamp;
        idegaTimestamp arrTimeStamp;
        Text prodName;
        Text timeframeTxt;
        Text depFrom;
        Text depTime;
        Text arrFrom;
        Text arrTime;
        Text actDays;

        Text nameOfCategory;
        Text priceText;
        ProductPrice[] prices;
        Currency currency;

        String stampTxt1;
        String stampTxt2;



        service = TravelStockroomBusiness.getService(product);
//        timeframe = TravelStockroomBusiness.getTimeframe(product);
        timeframes = product.getTimeframes();
        depAddress = ProductBusiness.getDepartureAddress(product);
        arrAddress = ProductBusiness.getArrivalAddress(product);
        if (product.getFileId() != -1) {
          image = new Image(product.getFileId());
          image.setMaxImageWidth(138);
        }else{
          image = (Image) imageToClone.clone();
        }
        prodName = (Text) theBoldText.clone();
            prodName.setText(ProductBusiness.getProductNameWithNumber(product));
            prodName.setFontColor(super.BLACK);



        depFrom = (Text) theBoldText.clone();
            depFrom.setFontColor(super.BLACK);
        if (depAddress != null)
            depFrom.setText(depAddress.getStreetName());

        depTimeStamp = new idegaTimestamp(service.getDepartureTime());
        depTime = (Text) theBoldText.clone();
            depTime.setFontColor(super.BLACK);
            depTime.setText(TextSoap.addZero(depTimeStamp.getHour())+":"+TextSoap.addZero(depTimeStamp.getMinute()));

        arrFrom = (Text) theBoldText.clone();
            arrFrom.setFontColor(super.BLACK);
        if (arrAddress != null)
            arrFrom.setText(arrAddress.getStreetName());

        arrTimeStamp = new idegaTimestamp(service.getArrivalTime());
        arrTime = (Text) theBoldText.clone();
            arrTime.setFontColor(super.BLACK);
            arrTime.setText(TextSoap.addZero(arrTimeStamp.getHour())+":"+TextSoap.addZero(arrTimeStamp.getMinute()));

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

        dayOfWeek = ServiceDay.getDaysOfWeek(service.getID());
        for (int j = 0; j < dayOfWeek.length; j++) {
          if (j > 0) actDays.addToText(", ");
          actDays.addToText(dayOfWeekName[dayOfWeek[j]]);
        }

        contentTable.add(activeDaysText,4,contRow);
        contentTable.add(actDays,5,contRow);

        ++contRow;
        contentTable.setVerticalAlignment(2,contRow,"top");
        contentTable.setVerticalAlignment(3,contRow,"top");
        contentTable.setVerticalAlignment(4,contRow,"top");
        contentTable.setVerticalAlignment(5,contRow,"top");
        contentTable.add(departureFromText,2,contRow);
        contentTable.add(departureTimeText,4,contRow);
        contentTable.setAlignment(2,contRow,"right");
        contentTable.setAlignment(3,contRow,"left");
        contentTable.setAlignment(4,contRow,"right");
        contentTable.setAlignment(5,contRow,"left");
        contentTable.add(depFrom,3,contRow);
        contentTable.add(depTime,5,contRow);
        contentTable.setRowColor(contRow, super.GRAY);

        ++contRow;
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

        ++contRow;
        contentTable.setVerticalAlignment(2,contRow,"top");
        contentTable.setVerticalAlignment(3,contRow,"top");
        contentTable.setAlignment(2,contRow,"right");
        contentTable.setAlignment(3,contRow,"left");
        contentTable.setRowColor(contRow, super.GRAY);
        contentTable.add(timeframeText,2,contRow);

//        contentTable.mergeCells(2,contRow,2,(contRow+prices.length-1));
//        contentTable.mergeCells(3,contRow,3,(contRow+prices.length-1));
        for (int k = 0; k < timeframes.length; k++) {
          prices = ProductPrice.getProductPrices(product.getID(), timeframes[k].getID(), false);
          timeframeTxt = (Text) theBoldText.clone();
              timeframeTxt.setFontColor(super.BLACK);

              if (timeframes.length == 0) {
                stampTxt1 = iwrb.getLocalizedString("travel.not_configured","Not configured");
                timeframeTxt.addToText(stampTxt1);
              }else {
                stampTxt1 = new idegaTimestamp(timeframes[k].getFrom()).getLocaleDate(iwc);
                stampTxt2 = new idegaTimestamp(timeframes[k].getTo()).getLocaleDate(iwc);
                try {
                  if (timeframes[0].getIfYearly() ){
                    stampTxt1 = stampTxt1.substring(0, stampTxt1.length() -4);
                    stampTxt2 = stampTxt2.substring(0, stampTxt2.length() -4);
                  }
                }catch (ArrayIndexOutOfBoundsException ai) {}
                timeframeTxt.setText(stampTxt1 + " - ");
                timeframeTxt.addToText(stampTxt2);
              }
          contentTable.setAlignment(2,contRow,"right");
          contentTable.setAlignment(3,contRow,"left");
          contentTable.add(timeframeTxt,3,contRow);
          contentTable.setRowColor(contRow, super.GRAY);

          if (prices.length == 0) {
            ++contRow;
          }
          for (int j = 0; j < prices.length; j++) {
            currency = new Currency(prices[j].getCurrencyId());
            nameOfCategory = (Text) theText.clone();
              nameOfCategory.setFontColor(super.BLACK);
              nameOfCategory.setText(prices[j].getPriceCategory().getName());
              nameOfCategory.addToText(":");
            priceText = (Text) theBoldText.clone();
              priceText.setFontColor(super.BLACK);
            try {
              priceText.setText(df.format(tsb.getPrice(prices[j].getID(),service.getID(),prices[j].getPriceCategoryID() , prices[j].getCurrencyId(), idegaTimestamp.getTimestampRightNow()) ) );
              priceText.addToText(Text.NON_BREAKING_SPACE);
              priceText.addToText(currency.getCurrencyAbbreviation());
            }catch (ProductPriceException p) {
              priceText.setText("Rangt upp sett, hafið samband við gimmi@idega.is");
            }

            if (prices[j].getPriceType() == ProductPrice.PRICETYPE_DISCOUNT) {
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

        contentTable.setWidth("100%");
        contentTable.setBorder(0);
        contentTable.setAlignment("center");
        contentTable.setWidth(1,"138");
        contentTable.setWidth(2,"90");
        contentTable.setWidth(4,"90");
        contentTable.setWidth(5,"150");
        contentTable.setCellspacing(1);
        contentTable.setColor(super.WHITE);

    return contentTable;
  }

}
