package is.idega.idegaweb.travel.presentation;

import com.idega.idegaweb.*;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import is.idega.idegaweb.travel.business.TravelStockroomBusiness;
import com.idega.block.trade.stockroom.data.*;
import is.idega.idegaweb.travel.data.*;
import com.idega.core.data.*;
import java.sql.SQLException;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class HotelPickupPlaceDesigner extends TravelManager {

  private static String parameterHotelPickupPlaceId = "parameterHotelPickupPlaceId";
  private static String parameterSaveHotelPickupPlaceInfo = "parameterSaveHotelPickupPlaceInfo";

  private static String sAction = "actionForHPPD";

  private Supplier supplier = null;
  private IWResourceBundle iwrb = null;


  public HotelPickupPlaceDesigner(IWContext iwc) throws Exception{
    super.main(iwc);
    supplier = super.getSupplier();
    iwrb = super.getResourceBundle();

    //handleInsert(iwc, supplier);
  }

  public static void handleInsert(IWContext iwc, Supplier supplier) {
    String action = iwc.getParameter(sAction);
    if (action != null) {
      if (action.equals(parameterSaveHotelPickupPlaceInfo)) {
        saveHotelPickupPlaces(iwc, supplier);
      }
    }
  }

  private static boolean saveHotelPickupPlaces(IWContext iwc, Supplier supplier) {
    String[] hppIds = iwc.getParameterValues("parameterHotelPickupPlaceId");
    String[] hppNames = iwc.getParameterValues("hotel_pickup_place_name");

    boolean returner = false;
    String del;
    if (hppNames != null && hppIds != null)
    try {
      for (int i = 0; i < hppNames.length; i++) {
        if (hppIds[i].equals("-1")) {
          if  ( (hppNames[i] != null) && (!hppNames[i].equals("")) ) {
            Address hotelPickupAddress = new Address();
              hotelPickupAddress.setAddressTypeID(AddressType.getId(TravelStockroomBusiness.uniqueHotelPickupAddressType));
              hotelPickupAddress.setStreetName(hppNames[i]);
              hotelPickupAddress.insert();

            HotelPickupPlace hpp = new HotelPickupPlace();
              hpp.setName(hppNames[i]);
              hpp.setAddress(hotelPickupAddress);
              hpp.insert();
            hpp.addTo(supplier);

          }
        }else {
          del = iwc.getParameter("hotel_pickup_place_to_delete_"+hppIds[i]);

          HotelPickupPlace hpp = new HotelPickupPlace(Integer.parseInt(hppIds[i]));
          Address address = hpp.getAddress();

          if (del == null) {
            hpp.setName(hppNames[i]);
            hpp.update();
            if (address != null) {
              address.setStreetName(hppNames[i]);
              address.update();
            }
          }else {
            hpp.removeFrom(supplier);
          }

        }

      }
      returner = true;
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }

      return returner;
  }


  public Form getHotelPickupPlaceForm(int supplierId) throws SQLException{
    int extraFields = 3;
    int textInputWidth = 60;

    Supplier supplier = new Supplier(supplierId);

    Form form = new Form();
    Table table = new Table();
      form.add(table);
      table.setBorder(0);
      table.setColor(TravelManager.WHITE);
      table.setCellspacing(1);
      table.setAlignment("center");
      HotelPickupPlace[] places = TravelStockroomBusiness.getHotelPickupPlaces(supplier);

    Text header = (Text) theText.clone();
      header.setText(iwrb.getLocalizedString("travel.hotel_pickup","Hotel pick-up"));
      header.setBold();
    Text deleteTxt = (Text) theText.clone();
      deleteTxt.setText(iwrb.getLocalizedString("travel.delete","Delete"));
      deleteTxt.setBold();

    Text nrTxt;
    TextInput nameInp;
    CheckBox delBox;

    table.setWidth(1,"15");
    table.setWidth(3,"2");

    int row = 1;
    table.add(header,2,row);
    table.add(deleteTxt,3,row);
    table.setRowColor(row,super.backgroundColor);


    int counter = 0;

    for (int i = 0; i < places.length; i++) {
        ++row;
        ++counter;
        nrTxt = (Text) super.smallText.clone();
          nrTxt.setFontColor(super.BLACK);
          nrTxt.setText(Integer.toString(counter));

        nameInp = new TextInput("hotel_pickup_place_name");
          nameInp.setContent(places[i].getAddress().getStreetName());
          nameInp.setSize(textInputWidth);

        delBox = new CheckBox("hotel_pickup_place_to_delete_"+places[i].getID());

        table.setRowColor(row,super.GRAY);

        table.add(new HiddenInput(this.parameterHotelPickupPlaceId, Integer.toString(places[i].getID())),1,row);
        table.add(nrTxt,1,row);
        table.add(Text.NON_BREAKING_SPACE,2,row);
        table.add(nameInp,2,row);
        table.add(Text.NON_BREAKING_SPACE,2,row);
        table.add(delBox,3,row);
    }
    for (int i = 0; i < extraFields; i++) {
        ++row;
        ++counter;
        nrTxt = (Text) super.smallText.clone();
          nrTxt.setFontColor(super.BLACK);
          nrTxt.setText(Integer.toString(counter));

        nameInp = new TextInput("hotel_pickup_place_name");
          nameInp.setSize(textInputWidth);

        table.setRowColor(row,super.GRAY);

        table.add(new HiddenInput(this.parameterHotelPickupPlaceId, "-1"),1,row);
        table.add(nrTxt,1,row);
        table.add(Text.NON_BREAKING_SPACE,2,row);
        table.add(nameInp,2,row);
        table.add(Text.NON_BREAKING_SPACE,2,row);
    }


    ++row;
    table.setRowColor(row,super.GRAY);
    SubmitButton lSave = new SubmitButton(iwrb.getImage("buttons/save.gif"),this.sAction, this.parameterSaveHotelPickupPlaceInfo);
    table.mergeCells(1,row,3,row);
    if (super.isInPermissionGroup) {
      table.add(lSave,1,row);
    }
    table.setColumnAlignment(1,"center");
    table.setColumnAlignment(3,"center");
    table.setAlignment(1,row,"right");

    return form;
  }


}
