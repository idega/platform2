package is.idega.idegaweb.travel.presentation;

import java.rmi.*;
import java.sql.*;
import java.util.*;

import javax.ejb.*;

import com.idega.block.trade.stockroom.business.*;
import com.idega.block.trade.stockroom.data.*;
import com.idega.core.data.*;
import com.idega.data.*;
import com.idega.idegaweb.*;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.presentation.ui.*;
import is.idega.idegaweb.travel.data.*;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class HotelPickupPlaceDesigner extends TravelManager {

  private String parameterHotelPickupPlaceId = "parameterHotelPickupPlaceId";
  private String parameterSaveHotelPickupPlaceInfo = "parameterSaveHotelPickupPlaceInfo";

  private String sAction = "actionForHPPD";

  private Supplier supplier = null;
  private IWResourceBundle iwrb = null;


  public HotelPickupPlaceDesigner(IWContext iwc) throws Exception{
    super.main(iwc);
    supplier = super.getSupplier();
    iwrb = super.getResourceBundle();

    handleInsert(iwc, supplier);
  }

  private void handleInsert(IWContext iwc, Supplier supplier) throws RemoteException, FinderException{
    String action = iwc.getParameter(sAction);
    if (action != null) {
      if (action.equals(parameterSaveHotelPickupPlaceInfo)) {
        saveHotelPickupPlaces(iwc, supplier);
      }
    }
  }

  private boolean saveHotelPickupPlaces(IWContext iwc, Supplier supplier) throws RemoteException, FinderException{
    String[] hppIds = iwc.getParameterValues("parameterHotelPickupPlaceId");
    String[] hppNames = iwc.getParameterValues("hotel_pickup_place_name");

    boolean returner = false;
    String del;
    String add;
    List products = getProductBusiness(iwc).getProducts(supplier.getID());
    int prodsSize = products.size();
    Product product;
    if (hppNames != null && hppIds != null)
    try {
      for (int i = 0; i < hppNames.length; i++) {
        if (hppIds[i].equals("-1")) {
          if  ( (hppNames[i] != null) && (!hppNames[i].equals("")) ) {
            Address hotelPickupAddress = ((com.idega.core.data.AddressHome)com.idega.data.IDOLookup.getHomeLegacy(Address.class)).createLegacy();
              hotelPickupAddress.setAddressTypeID(com.idega.core.data.AddressTypeBMPBean.getId(ProductBusinessBean.uniqueHotelPickupAddressType));
              hotelPickupAddress.setStreetName(hppNames[i]);
              hotelPickupAddress.insert();

            PickupPlace hpp = ((is.idega.idegaweb.travel.data.PickupPlaceHome)com.idega.data.IDOLookup.getHome(PickupPlace.class)).create();
              hpp.setName(hppNames[i]);
              hpp.setAddress(hotelPickupAddress);
              hpp.store();
            hpp.addToSupplier(supplier);
//            hpp.addTo(supplier);

          }
        }else {
          del = iwc.getParameter("hotel_pickup_place_to_delete_"+hppIds[i]);
          add = iwc.getParameter("hotel_pickup_place_add_to_all_"+hppIds[i]);

          PickupPlace hpp = ((is.idega.idegaweb.travel.data.PickupPlaceHome)com.idega.data.IDOLookup.getHome(PickupPlace.class)).findByPrimaryKey(new Integer(hppIds[i]));
          Address address = hpp.getAddress();

          if (del == null) {
            hpp.setName(hppNames[i]);
            hpp.store();
            if (address != null) {
              address.setStreetName(hppNames[i]);
              address.update();
            }
            if (add != null) {
              for (int j = 0; j < prodsSize; j++) {
                product = (Product) products.get(j) ;
                try {
                  hpp.addToService(getService(product));
                }catch (IDOAddRelationshipException are) {
                  //System.err.println("address \""+hppNames[i]+"\" ALREADY added to service \""+ProductBusiness.getProductName(product)+"\"");
                }
              }
            }
          }else {
            System.err.println("hpp_id = "+hpp.getPrimaryKey().toString());
            System.err.println("hppids[i] = "+hppIds[i]);
            hpp.removeFromSupplier(supplier);
//            hpp.removeFrom(supplier);
          }

        }

      }
      returner = true;
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }catch (CreateException ce) {
      ce.printStackTrace(System.err);
    }catch (IDOAddRelationshipException are) {
      are.printStackTrace(System.err);
    }catch (IDORemoveRelationshipException rre) {
      rre.printStackTrace(System.err);
    }

      return returner;
  }


  public Form getHotelPickupPlaceForm(IWContext iwc, int supplierId) throws RemoteException, FinderException, SQLException{
    int extraFields = 3;
    int textInputWidth = 60;

    Supplier supplier = ((com.idega.block.trade.stockroom.data.SupplierHome)com.idega.data.IDOLookup.getHomeLegacy(Supplier.class)).findByPrimaryKeyLegacy(supplierId);

    Form form = new Form();
    Table table = new Table();
      form.add(table);
      table.setBorder(0);
      table.setColor(TravelManager.WHITE);
      table.setCellspacing(1);
      table.setAlignment("center");
      PickupPlaceHome hppHome = (PickupPlaceHome) IDOLookup.getHome(PickupPlace.class);
      Collection coll = hppHome.findHotelPickupPlaces(supplier);
      PickupPlace[] places = (PickupPlace[]) coll.toArray(new PickupPlace[]{});

    Text header = (Text) theText.clone();
      header.setText(iwrb.getLocalizedString("travel.hotel_pickup","Hotel pick-up"));
      header.setBold();
    Text deleteTxt = (Text) theText.clone();
      deleteTxt.setText(iwrb.getLocalizedString("travel.delete","Delete"));
      deleteTxt.setBold();
    Text addTxt = (Text) theText.clone();
      addTxt.setText(iwrb.getLocalizedString("travel.add_to_all","Add to all"));
      addTxt.setBold();

    Text nrTxt;
    TextInput nameInp;
    CheckBox delBox;
    CheckBox addToAll;

    table.setWidth(1,"15");
    table.setWidth(3,"2");
//    table.setWidth(4,"2");

    int row = 1;
    table.add(header,2,row);
    table.add(deleteTxt,3,row);
    table.add(addTxt,4,row);
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

        delBox = new CheckBox("hotel_pickup_place_to_delete_"+places[i].getPrimaryKey().toString());
        addToAll = new CheckBox("hotel_pickup_place_add_to_all_"+places[i].getPrimaryKey().toString());

        table.setRowColor(row,super.GRAY);

        table.add(new HiddenInput(this.parameterHotelPickupPlaceId, places[i].getPrimaryKey().toString()),1,row);
        table.add(nrTxt,1,row);
        table.add(Text.NON_BREAKING_SPACE,2,row);
        table.add(nameInp,2,row);
        table.add(Text.NON_BREAKING_SPACE,2,row);
        table.add(delBox,3,row);
        table.add(addToAll,4,row);
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
    table.mergeCells(1,row,4,row);
    if (super.isInPermissionGroup) {
      table.add(lSave,1,row);
    }
    table.setColumnAlignment(1,"center");
    table.setColumnAlignment(3,"center");
    table.setColumnAlignment(4,"center");
    table.setAlignment(1,row,"right");

    return form;
  }

  private Service getService(Product product) throws RemoteException, FinderException{
    ServiceHome sHome = (ServiceHome) IDOLookup.getHome(Service.class);
    return sHome.findByPrimaryKey(product.getPrimaryKey());
  }

}
