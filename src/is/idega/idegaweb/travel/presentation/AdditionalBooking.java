package is.idega.travel.presentation;

import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.textObject.*;
import is.idega.travel.data.*;
import com.idega.block.trade.stockroom.data.*;
import is.idega.travel.business.*;
import com.idega.util.idegaTimestamp;

import java.sql.SQLException;
/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class AdditionalBooking extends TravelWindow {

  public static String parameterServiceId = "addBookServiceId";
  public static String parameterDate = "addBookDate";

  public static String parameterSave = "addBookSave";
  public static String sAction = "addBookAction";

  TravelStockroomBusiness tsb = TravelStockroomBusiness.getNewInstance();
  Service service;
  idegaTimestamp stamp;

  public AdditionalBooking() {
    super.setHeight(200);
    super.setWidth(400);
    super.setTitle("idegaWeb Travel");
  }

  public void main(ModuleInfo modinfo) {
    super.main(modinfo);
    initialize(modinfo);

    if (service != null) {
      String action = modinfo.getParameter(this.sAction);
      if (action == null) {
        displayForm(modinfo);
      }else if (action.equals(this.parameterSave) ) {
        saveBooking(modinfo);
        super.close(true);
      }
    }else {
      add("Session útrunnið ??");
    }
  }

  private void initialize(ModuleInfo modinfo) {
    try {
        service = new Service(Integer.parseInt(modinfo.getParameter(this.parameterServiceId)));
        stamp = new idegaTimestamp(modinfo.getParameter(this.parameterDate));
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }
  }

  private void displayForm(ModuleInfo modinfo) {
      Form form = new Form();
      Table table = new Table();
        form.add(table);
      int row = 1;

      ProductPrice[] pPrices = tsb.getProductPrices(service.getID(), false);
      PriceCategory category;

      Text nameText = (Text) text.clone();
          nameText.setText(iwrb.getLocalizedString("travel.name","name"));
      TextInput name = new TextInput("name");
          name.setSize(18);

      Text pPriceCatNameText;
      ResultOutput pPriceText;
      TextInput pPriceMany;
      Text totalText = (Text) text.clone();
        totalText.setBold();
        totalText.setText("T - Total");
      ResultOutput TotalPassTextInput = new ResultOutput("total_pass","0");
        TotalPassTextInput.setSize(5);
      ResultOutput TotalTextInput = new ResultOutput("total","0");
        TotalTextInput.setSize(8);

    ++row;
    table.add(nameText,1,row);
    table.add(name,2,row);
      for (int i = 0; i < pPrices.length; i++) {
        try {
            ++row;
            category = pPrices[i].getPriceCategory();
            int price = (int) tsb.getPrice(service.getID(),pPrices[i].getPriceCategoryID(),pPrices[i].getCurrencyId(),idegaTimestamp.getTimestampRightNow());
            pPriceCatNameText = (Text) text.clone();
              pPriceCatNameText.setText(category.getName());

            pPriceText = new ResultOutput("thePrice"+i,"0");
              pPriceText.setSize(8);

            pPriceMany = new TextInput("priceCategory"+i ,"0");
              pPriceMany.setSize(5);
              pPriceMany.setAsNotEmpty("T - Ekki tómt");
              pPriceMany.setAsIntegers("T - Bara tölur takk");

            pPriceText.add(pPriceMany,"*"+price);
            TotalPassTextInput.add(pPriceMany);
            TotalTextInput.add(pPriceMany,"*"+price);


            table.add(pPriceCatNameText, 1,row);
            table.add(pPriceMany,2,row);
            table.add(pPriceText, 2,row);

            table.add(Integer.toString(price),2,row);
        }catch (SQLException sql) {
          sql.printStackTrace(System.err);
        }
    }

    ++row;
    table.add(totalText,1,row);
    table.add(TotalPassTextInput,2,row);
    table.add(TotalTextInput,2,row);
    ++row;
    table.mergeCells(1,row,2,row);
    table.setAlignment(1,row,"right");
    table.add(new SubmitButton(iwrb.getImage("/buttons/save.gif"),this.sAction, this.parameterSave),1,row);
    table.add(new HiddenInput(this.parameterServiceId, Integer.toString(this.service.getID())));
    table.add(new HiddenInput(this.parameterDate, stamp.toSQLDateString()));

    add(form);
  }


  public void saveBooking(ModuleInfo modinfo) {
      String name = modinfo.getParameter("name");

      String many;
      int iMany = 0;
      int iHotelId;

      ProductPrice[] pPrices = tsb.getProductPrices(service.getID(), false);
      int bookingId;

      try {
        int[] manys = new int[pPrices.length];
        for (int i = 0; i < manys.length; i++) {
            many = modinfo.getParameter("priceCategory"+i);
            if ( (many != null) && (!many.equals("")) && (!many.equals("0"))) {
                manys[i] = Integer.parseInt(many);
                iMany += Integer.parseInt(many);
            }else {
                manys[i] = 0;
            }
        }


        bookingId = tsb.Book(service.getID(), -1, "", name, "", "", "", "", stamp, iMany, is.idega.travel.data.Booking.BOOKING_TYPE_ID_ADDITIONAL_BOOKING,"");

        BookingEntry bEntry;
        for (int i = 0; i < pPrices.length; i++) {
          bEntry = new BookingEntry();
            bEntry.setProductPriceId(pPrices[i].getID());
            bEntry.setBookingId(bookingId);
            bEntry.setCount(manys[i]);
          bEntry.insert();
        }



      }catch (NumberFormatException n) {
        n.printStackTrace(System.err);
      }catch (SQLException sql) {
        sql.printStackTrace(System.err);
      }


  }

}