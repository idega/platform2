package is.idega.travel.presentation;

import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.textObject.*;
import is.idega.travel.business.TravelStockroomBusiness;
import com.idega.block.trade.stockroom.data.*;
import is.idega.travel.data.*;
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

public class HotelPickupPlaceDesigner extends TravelWindow {

  private int supplierId = -1;
  private Supplier supplier;
  private TravelStockroomBusiness tsb = TravelStockroomBusiness.getNewInstance();

  private String dropdownMenuName = "hppdId";

  private String action = "hppdAction";

  private String parameterCreate = "createPickupPlace";
  private String parameterDelete = "deletePickupPlace";
  private String parameterSave = "savePickupPlace";
  private String parameterClose = "closeWindow";

  public HotelPickupPlaceDesigner() {
    super.setTitle("idegaWeb Travel");
  }

  public void main(ModuleInfo modinfo) {
    super.main(modinfo);

    if (checkForEverything(modinfo)) {
      String action = modinfo.getParameter(this.action);


      if (action == null) {
        mainMenu(modinfo);
      }else if (action.equals(this.parameterCreate) ) {
        createPickupPlace(modinfo);
      }else if (action.equals(this.parameterDelete) ) {
        deletePickupPlace(modinfo);
      }else if (action.equals(this.parameterSave) ) {
        savePickupPlace(modinfo);
      }else if (action.equals(this.parameterClose) ) {
        closer();
      }

    }
  }

  public void closer() {
    jPage.setParentToReload();
    jPage.setOnUnLoad("window.opener."+ServiceDesigner.NAME_OF_FORM+".submit()");
    super.close(false);
  }

  private void savePickupPlace(ModuleInfo modinfo) {
      String address = modinfo.getParameter("address");

      try {
        if  ( (address != null) && (!address.equals("")) ) {
          Address hotelPickupAddress = new Address();
            hotelPickupAddress.setAddressTypeID(AddressType.getId(TravelStockroomBusiness.uniqueHotelPickupAddressType));
            hotelPickupAddress.setStreetName(address);
            hotelPickupAddress.insert();

          HotelPickupPlace hpp = new HotelPickupPlace();
            hpp.setName(address);
            hpp.setAddress(hotelPickupAddress);
            hpp.insert();

          hpp.addTo(this.supplier);

        }
      }catch (SQLException sql) {
        sql.printStackTrace(System.err);
      }
      mainMenu(modinfo);
  }

  private void deletePickupPlace(ModuleInfo modinfo) {
    String placeId = modinfo.getParameter(this.dropdownMenuName);
    try {
      HotelPickupPlace place = new HotelPickupPlace(Integer.parseInt(placeId));
        place.delete();
    }
    catch (Exception e) {
      add("T- Villa");
    }
    mainMenu(modinfo);
  }

  private void createPickupPlace(ModuleInfo modinfo) {
    Form form = new Form();
    Table table = new Table();
      form.add(table);

      Text address = (Text) super.text.clone();
        address.setText(iwrb.getLocalizedString("travel.address_long","Address"));

      TextInput addressInput = new TextInput("address");

      SubmitButton save = new SubmitButton("T-Save",this.action, this.parameterSave);


      table.add(address,1,2);
      table.add(addressInput,2,2);
      table.mergeCells(1,3,2,3);
      table.add(save,1,3);

    add(form);
  }


  private void mainMenu(ModuleInfo modinfo) {
    int row = 1;
    Form form = new Form();
    Table table = new Table();
      form.add(table);
      table.setBorder(0);
      table.setAlignment("center");
      table.setWidth("90%");
      HotelPickupPlace[] places = tsb.getHotelPickupPlaces(supplier);

    Text header = (Text) text.clone();
      header.setText(iwrb.getLocalizedString("travel.hotel_pickup","Hotel pick-up"));
      header.setFontSize(Text.FONT_SIZE_12_HTML_3);
      header.setBold();

    SubmitButton lNew = new SubmitButton("T - new",this.action, this.parameterCreate);
    SubmitButton lDel = new SubmitButton("T - del",this.action, this.parameterDelete);

    DropdownMenu dmPlaces = new DropdownMenu(places, this.dropdownMenuName);

    table.add(header,1,row);
    table.setAlignment(1,row,"center");
    table.mergeCells(1,row,2,row);
    ++row;
    table.add(dmPlaces,1,row);
    table.mergeCells(1,row,2,row);
    ++row;
    table.add(lNew,1,row);
    table.add(lDel,2,row);
    table.setAlignment(1,row,"left");
    table.setAlignment(2,row,"right");

    SubmitButton lClose = new SubmitButton("T - Close",this.action, this.parameterClose);

    Paragraph p = new Paragraph();
      p.add(lClose);
      p.setAlign("right");
    form.add(p);

    add(form);
  }

  private boolean checkForEverything(ModuleInfo modinfo) {
    boolean returner = true;
    try {

      this.supplierId = tsb.getUserSupplierId(modinfo);
      this.supplier = new Supplier(supplierId);

    }catch (Exception e) {
      e.printStackTrace(System.err);
      returner = false;
    }
    return returner;
  }

  }