package is.idega.travel.presentation;

import com.idega.presentation.Block;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.text.*;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.block.trade.stockroom.data.*;
import com.idega.jmodule.calendar.presentation.SmallCalendar;
import com.idega.util.idegaTimestamp;
import com.idega.util.idegaCalendar;
import com.idega.util.text.TextSoap;
import com.idega.core.accesscontrol.business.AccessControl;
import is.idega.travel.business.TravelStockroomBusiness;
import java.sql.SQLException;
import com.idega.block.trade.data.Currency;
import com.idega.block.trade.stockroom.business.ProductPriceException;

import is.idega.travel.data.*;
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

  private idegaCalendar cal = new idegaCalendar();

  public ServiceOverview() {
  }

  public void add(PresentationObject mo) {
    super.add(mo);
  }


  public void main(IWContext iwc) throws SQLException{
      super.main(iwc);
      bundle = super.getBundle();
      iwrb = super.getResourceBundle();

      String action = iwc.getParameter(actionParameter);
      if (action == null) {action = "";}

      if (action.equals("")) {
          displayForm(iwc);
      }else if (action.equals("delete")) {
          deleteServices(iwc);
          displayForm(iwc);
      }

      super.addBreak();
      //super.add(tm);
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
        table.setWidth(1,"100");
        table.setWidth(2,"90");
        table.setWidth(4,"90");
      ShadowBox sb = new ShadowBox();
        form.add(sb);
        sb.setWidth("90%");
        sb.setAlignment("center");
        sb.add(table);

      table.setWidth("95%");
      String sYear = iwc.getParameter("year");
      if (sYear == null) {
          sYear = Text.emptyString().toString();
      }

      int row = 0;
      idegaTimestamp stamp = idegaTimestamp.RightNow();

      String[] dayOfWeekName = new String[8];
        dayOfWeekName[ServiceDay.SUNDAY] = cal.getNameOfDay(ServiceDay.SUNDAY ,iwc).substring(0,3);
        dayOfWeekName[ServiceDay.MONDAY] = cal.getNameOfDay(ServiceDay.MONDAY ,iwc).substring(0,3);
        dayOfWeekName[ServiceDay.TUESDAY] = cal.getNameOfDay(ServiceDay.TUESDAY ,iwc).substring(0,3);
        dayOfWeekName[ServiceDay.WEDNESDAY] = cal.getNameOfDay(ServiceDay.WEDNESDAY ,iwc).substring(0,3);
        dayOfWeekName[ServiceDay.THURSDAY] = cal.getNameOfDay(ServiceDay.THURSDAY ,iwc).substring(0,3);
        dayOfWeekName[ServiceDay.FRIDAY] = cal.getNameOfDay(ServiceDay.FRIDAY ,iwc).substring(0,3);
        dayOfWeekName[ServiceDay.SATURDAY] = cal.getNameOfDay(ServiceDay.SATURDAY ,iwc).substring(0,3);

      int[] dayOfWeek = new int[] {};

      Link delete;
      Link getLink;
      Link bookClone = new Link(iwrb.getImage("/buttons/book.gif"),Booking.class);
        bookClone.addParameter(super.sAction, super.parameterBooking);
      Link editClone = new Link(iwrb.getImage("/buttons/change.gif"),ServiceDesigner.class);
        editClone.addParameter(super.sAction, super.parameterServiceDesigner);

      Link book;
      Link edit;


      Text nameText = (Text) theText.clone();
          nameText.setText(iwrb.getLocalizedString("travel.name_of_trip","Name of trip"));
          nameText.addToText(":");
      Text timeframeText = (Text) theText.clone();
          timeframeText.setText(iwrb.getLocalizedString("travel.timeframe_only","Timeframe"));
          timeframeText.addToText(":");
      Text departureFromText = (Text) theText.clone();
          departureFromText.setText(iwrb.getLocalizedString("travel.departure_from","Departure from"));
          departureFromText.addToText(":");
      Text departureTimeText = (Text) theText.clone();
          departureTimeText.setText(iwrb.getLocalizedString("travel.departure_time","Departure time"));
          departureTimeText.addToText(":");
      Text activeDaysText = (Text) theText.clone();
          activeDaysText.setText(iwrb.getLocalizedString("travel.active_days","Active days"));
          activeDaysText.addToText(":");

      Image image = new Image("/pics/mynd.gif");

      Supplier supplier = super.getSupplier();
      if (supplier != null) {
        TravelStockroomBusiness tsb = TravelStockroomBusiness.getNewInstance();
        //Product[] products = tsb.getProducts(supplier.getID(), this.getFromIdegaTimestamp(iwc), this.getToIdegaTimestamp(iwc));
        Product[] products = tsb.getProducts(supplier.getID());

        Service service;
        Timeframe timeframe;
        Address address;

        idegaTimestamp depTimeStamp;
        Text prodName;
        Text timeframeTxt;
        Text depFrom;
        Text depTime;
        Text actDays;

        Text nameOfCategory;
        Text priceText;
        ProductPrice[] prices;
        Currency currency;


        for (int i = 0; i < products.length; i++) {
          try {
            service = TravelStockroomBusiness.getService(products[i]);
            timeframe = TravelStockroomBusiness.getTimeframe(products[i]);
            address = service.getAddress();

            prodName = (Text) theBoldText.clone();
                prodName.setText(service.getName());

            timeframeTxt = (Text) theBoldText.clone();
                timeframeTxt.setText(new idegaTimestamp(timeframe.getFrom()).getLocaleDate(iwc) + " - ");
                timeframeTxt.addToText(Text.BREAK);
                timeframeTxt.addToText(new idegaTimestamp(timeframe.getTo()).getLocaleDate(iwc));

            depFrom = (Text) theBoldText.clone();
            if (address != null)
                depFrom.setText(address.getStreetName());

            depTimeStamp = new idegaTimestamp(service.getDepartureTime());
            depTime = (Text) theBoldText.clone();
                depTime.setText(TextSoap.addZero(depTimeStamp.getHour())+":"+TextSoap.addZero(depTimeStamp.getMinute()));

            actDays = (Text) theBoldText.clone();

            ++row;
            table.mergeCells(1,row,1,row+3);
            table.add(image,1,row);
            table.add(nameText,2,row);
            table.add(timeframeText,4,row);
            table.setVerticalAlignment(2,row,"top");
            table.setVerticalAlignment(3,row,"top");
            table.setVerticalAlignment(4,row,"top");
            table.setVerticalAlignment(5,row,"top");
            table.setAlignment(2,row,"right");
            table.setAlignment(3,row,"left");
            table.setAlignment(4,row,"right");
            table.setAlignment(5,row,"left");
            table.add(prodName,3,row);
            table.add(timeframeTxt,5,row);

            ++row;
            table.setVerticalAlignment(2,row,"top");
            table.setVerticalAlignment(3,row,"top");
            table.setVerticalAlignment(4,row,"top");
            table.setVerticalAlignment(5,row,"top");
            table.add(departureFromText,2,row);
            table.add(departureTimeText,4,row);
            table.setAlignment(2,row,"right");
            table.setAlignment(3,row,"left");
            table.setAlignment(4,row,"right");
            table.setAlignment(5,row,"left");
            table.add(depFrom,3,row);
            table.add(depTime,5,row);

            ++row;
            table.setVerticalAlignment(2,row,"top");
            table.setVerticalAlignment(3,row,"top");
            table.setAlignment(2,row,"right");
            table.setAlignment(3,row,"left");

            dayOfWeek = ServiceDay.getDaysOfWeek(service.getID());
            for (int j = 0; j < dayOfWeek.length; j++) {
              if (j > 0) actDays.addToText(", ");
              actDays.addToText(dayOfWeekName[dayOfWeek[j]]);
            }

            table.add(activeDaysText,2,row);
            table.add(actDays,3,row);


            prices = ProductPrice.getProductPrices(service.getID(), false);
            table.mergeCells(2,row,2,(row+prices.length-1));
            table.mergeCells(3,row,3,(row+prices.length-1));

            for (int j = 0; j < prices.length; j++) {
              currency = new Currency(prices[j].getCurrencyId());
              nameOfCategory = (Text) theText.clone();
                nameOfCategory.setText(prices[j].getPriceCategory().getName());
                nameOfCategory.addToText(":");
              priceText = (Text) theBoldText.clone();
              try {
                priceText.setText(Integer.toString((int)tsb.getPrice(prices[j].getID(),service.getID(),prices[j].getPriceCategoryID() , prices[j].getCurrencyId(), idegaTimestamp.getTimestampRightNow()) ) );
                priceText.addToText(Text.NON_BREAKING_SPACE);
                priceText.addToText(currency.getCurrencyAbbreviation());
              }catch (ProductPriceException p) {
                priceText.setText("T - rangt upp sett");
              }

              if (prices[j].getPriceType() == ProductPrice.PRICETYPE_DISCOUNT) {
                priceText.addToText(Text.NON_BREAKING_SPACE+"("+prices[j].getPrice()+"%)");
              }



              table.setVerticalAlignment(4,row,"top");
              table.setVerticalAlignment(5,row,"top");
              table.setAlignment(4,row,"right");
              table.setAlignment(5,row,"left");

              table.add(nameOfCategory,4,row);
              table.add(priceText,5,row);
              ++row;
            }





            ++row;
            table.mergeCells(1,row,5,row);
            table.setAlignment(1,row,"right");

            getLink = new Link("getLink");
              getLink.setWindowToOpen(LinkGenerator.class);
              getLink.addParameter(LinkGenerator.parameterProductId ,service.getID());

            delete = new Link(iwrb.getImage("buttons/delete.gif"));
              delete.addParameter(actionParameter,"delete");
              delete.addParameter(deleteParameter,service.getID());

            book = (Link) bookClone.clone();
              book.addParameter(Booking.parameterProductId,products[i].getID());

            edit = (Link) editClone.clone();
              edit.addParameter(ServiceDesigner.parameterUpdateServiceId, products[i].getID());

            table.add(edit,1,row);
            table.add("&nbsp;&nbsp;",1,row);
            table.add(book,1,row);
            table.add("&nbsp;&nbsp;",1,row);
            table.add(getLink,1,row);
            table.add("&nbsp;&nbsp;",1,row);
            table.add(delete,1,row);


            table.setColor(1,row,super.backgroundColor);

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



}