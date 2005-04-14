package is.idega.idegaweb.travel.presentation;

import is.idega.idegaweb.travel.data.PickupPlace;
import is.idega.idegaweb.travel.data.PickupPlaceBMPBean;
import is.idega.idegaweb.travel.data.PickupPlaceHome;
import is.idega.idegaweb.travel.data.Service;
import is.idega.idegaweb.travel.data.ServiceHome;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.trade.stockroom.business.ProductBusinessBean;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.core.location.data.Address;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOLookup;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class AddressDesigner extends TravelManager {

  private String PARAMETER_PICKUP_PLACE_ID = "prm_pp_id";
  private String PARAMETER_PICKUP_PLACE_NAME = "pp_nm";
  private String PARAMETER_PICKUP_PLACE_TO_DELETE = "pp_t_dlt_";
  private String PARAMETER_PICKUP_PLACE_TO_ADD_TO_ALL = "pp_ata_";
  private String PARAMETER_ADDRESS_TYPE = "prm_adr_ty";
  private String PARAMETER_SAVE_PICKUP_PLACE_INFO = "prm_sv_pp_inf";
  

	private int textInputWidth = 60;
  private String ACTION = "act_ad";

  private Supplier supplier = null;
  private IWResourceBundle iwrb = null;


  public AddressDesigner(IWContext iwc) throws Exception{
    super.main(iwc);
    supplier = super.getSupplier();
    iwrb = super.getResourceBundle();

    handleInsert(iwc, supplier);
  }

  private void handleInsert(IWContext iwc, Supplier supplier) throws RemoteException, FinderException{
    String action = iwc.getParameter(ACTION);
    if (action != null) {
      if (action.equals(PARAMETER_SAVE_PICKUP_PLACE_INFO)) {
        saveHotelPickupPlaces(iwc, supplier);
      }
    }
  }

  private boolean saveHotelPickupPlaces(IWContext iwc, Supplier supplier) throws RemoteException, FinderException{
    String[] hppIds = iwc.getParameterValues(PARAMETER_PICKUP_PLACE_ID);
    String[] hppNames = iwc.getParameterValues(PARAMETER_PICKUP_PLACE_NAME);
    String[] type = iwc.getParameterValues(PARAMETER_ADDRESS_TYPE);

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
            Address hotelPickupAddress = ((com.idega.core.location.data.AddressHome)com.idega.data.IDOLookup.getHomeLegacy(Address.class)).createLegacy();
              hotelPickupAddress.setAddressTypeID(com.idega.core.location.data.AddressTypeBMPBean.getId(ProductBusinessBean.uniqueHotelPickupAddressType));
              hotelPickupAddress.setStreetName(hppNames[i]);
              hotelPickupAddress.insert();

            PickupPlace hpp = ((is.idega.idegaweb.travel.data.PickupPlaceHome)com.idega.data.IDOLookup.getHome(PickupPlace.class)).create();
              hpp.setName(hppNames[i]);
              hpp.setAddress(hotelPickupAddress);
              hpp.setType(Integer.parseInt(type[i]));
              //hpp.setAsPickup();
              hpp.store();
            hpp.addToSupplier(supplier);
//            hpp.addTo(supplier);

          }
        }else {
          del = iwc.getParameter(PARAMETER_PICKUP_PLACE_TO_DELETE+hppIds[i]);
          add = iwc.getParameter(PARAMETER_PICKUP_PLACE_TO_ADD_TO_ALL+hppIds[i]);

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

  public Form getPickupPlaceDesignerForm(IWContext iwc, int supplierId) throws RemoteException, FinderException, SQLException{
		return getAddressDesignerForm(iwc, supplierId);
  }
  
	public Form getDropoffPlaceDesignerForm(IWContext iwc, int supplierId) throws RemoteException, FinderException, SQLException{
		return getAddressDesignerForm(iwc, supplierId);
	}
	
  public Form getAddressDesignerForm(IWContext iwc, int supplierId) throws RemoteException, FinderException, SQLException{
    int extraFields = 3;

    Supplier supplier = ((com.idega.block.trade.stockroom.data.SupplierHome)com.idega.data.IDOLookup.getHomeLegacy(Supplier.class)).findByPrimaryKeyLegacy(supplierId);

    Form form = new Form();
    Table table = new Table();
      form.add(table);
      table.setBorder(0);
      table.setColor(TravelManager.WHITE);
      table.setCellspacing(1);
      table.setAlignment("center");
      PickupPlaceHome pPlaceHome = (PickupPlaceHome) IDOLookup.getHome(PickupPlace.class);

    Text header = (Text) theText.clone();
      header.setText(iwrb.getLocalizedString("travel.pickup","pick-up"));
      header.setBold();
    Text deleteTxt = (Text) theText.clone();
      deleteTxt.setText(iwrb.getLocalizedString("travel.delete","Delete"));
      deleteTxt.setBold();
    Text addTxt = (Text) theText.clone();
      addTxt.setText(iwrb.getLocalizedString("travel.add_to_all","Add to all"));
      addTxt.setBold();

    table.setWidth(1,"15");
    table.setWidth(3,"2");
//    table.setWidth(4,"2");

    int row = 1;
		int counter = 0;
		int tempCounter = 0;


		/** Pickup ...*/ 
    table.add(header,2,row);
    table.add(deleteTxt,3,row);
    table.add(addTxt,4,row);
    table.setRowColor(row,super.backgroundColor);

		Collection coll = pPlaceHome.findHotelPickupPlaces(supplier);
    tempCounter = row;
		row = insertPlaces(table, pPlaceHome, coll, row);
		counter = row - tempCounter;

	  Text nrTxt;
	  TextInput nameInp;
	  CheckBox delBox;
	  CheckBox addToAll;
	  HiddenInput hidden;
		
	  for (int i = 0; i < extraFields; i++) {
		  ++row;
		  ++counter;
		  nrTxt = (Text) super.smallText.clone();
			nrTxt.setFontColor(super.BLACK);
			nrTxt.setText(Integer.toString(counter));
	
		  nameInp = new TextInput(PARAMETER_PICKUP_PLACE_NAME);
			nameInp.setSize(textInputWidth);
			
				  hidden = new HiddenInput(PARAMETER_ADDRESS_TYPE, Integer.toString(PickupPlaceBMPBean.TYPE_PICKUP));
	
		  table.setRowColor(row,super.GRAY);
	
		  table.add(new HiddenInput(this.PARAMETER_PICKUP_PLACE_ID, "-1"),1,row);
		  table.add(nrTxt,1,row);
		  table.add(Text.NON_BREAKING_SPACE,2,row);
		  table.add(nameInp,2,row);
		  table.add(Text.NON_BREAKING_SPACE,2,row);
		  table.add(hidden, 2, row);
	  }
		/** ... Pickup*/ 

		
		/** Dropoff ...*/
		++row;
		header = (Text) header.clone(); 
		header.setText(iwrb.getLocalizedString("travel.dropoff","Drop-off"));
		table.add(header,2,row);
		table.add(deleteTxt,3,row);
		table.add(addTxt,4,row);
		table.setRowColor(row,super.backgroundColor);

		coll = pPlaceHome.findDropoffPlaces(supplier);
		tempCounter = row;
		row = insertPlaces(table, pPlaceHome, coll, row);
		counter = row - tempCounter;
		

		for (int i = 0; i < extraFields; i++) {
			++row;
			++counter;
			nrTxt = (Text) super.smallText.clone();
			  nrTxt.setFontColor(super.BLACK);
			  nrTxt.setText(Integer.toString(counter));
	
			nameInp = new TextInput(PARAMETER_PICKUP_PLACE_NAME);
			  nameInp.setSize(textInputWidth);
			hidden = new HiddenInput(PARAMETER_ADDRESS_TYPE, Integer.toString(PickupPlaceBMPBean.TYPE_DROPOFF));
	
			table.setRowColor(row,super.GRAY);
	
			table.add(new HiddenInput(this.PARAMETER_PICKUP_PLACE_ID, "-1"),1,row);
			table.add(nrTxt,1,row);
			table.add(Text.NON_BREAKING_SPACE,2,row);
			table.add(nameInp,2,row);
			table.add(Text.NON_BREAKING_SPACE,2,row);
			table.add(hidden, 2, row);
		}
		/** ... Dropoff */

    ++row;
    table.setRowColor(row,super.GRAY);
    SubmitButton lSave = new SubmitButton(iwrb.getImage("buttons/save.gif"),this.ACTION, this.PARAMETER_SAVE_PICKUP_PLACE_INFO);
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

private int insertPlaces(Table table,	PickupPlaceHome pPlaceHome,	Collection coll,	int row)	throws FinderException {
	if (coll != null && !coll.isEmpty()) {
		Iterator iter = coll.iterator();
		PickupPlace pPlace;
		Text nrTxt;
		TextInput nameInp;
		CheckBox delBox;
		CheckBox addToAll;
		HiddenInput hidden;
		int counter = 0;
			
		while (iter.hasNext()) {
			pPlace = (PickupPlace) iter.next();
			++row;
			++counter;
			nrTxt = (Text) super.smallText.clone();
			  nrTxt.setFontColor(super.BLACK);
			  nrTxt.setText(Integer.toString(counter));
		
			nameInp = new TextInput(PARAMETER_PICKUP_PLACE_NAME);
			  nameInp.setContent(pPlace.getAddress().getStreetAddress());
			  nameInp.setSize(textInputWidth);
		
			delBox = new CheckBox(PARAMETER_PICKUP_PLACE_TO_DELETE+pPlace.getPrimaryKey().toString());
			addToAll = new CheckBox(PARAMETER_PICKUP_PLACE_TO_ADD_TO_ALL+pPlace.getPrimaryKey().toString());
		        
			hidden = new HiddenInput(PARAMETER_ADDRESS_TYPE, Integer.toString(pPlace.getType()));
		
			table.setRowColor(row,super.GRAY);
		
			table.add(new HiddenInput(this.PARAMETER_PICKUP_PLACE_ID, pPlace.getPrimaryKey().toString()),1,row);
			table.add(nrTxt,1,row);
			table.add(Text.NON_BREAKING_SPACE,2,row);
			table.add(nameInp,2,row);
			table.add(Text.NON_BREAKING_SPACE,2,row);
			table.add(delBox,3,row);
			table.add(addToAll,4,row);
			table.add(hidden, 4, row);
		}
	}
	return row;
}

  private Service getService(Product product) throws RemoteException, FinderException{
    ServiceHome sHome = (ServiceHome) IDOLookup.getHome(Service.class);
    return sHome.findByPrimaryKey(product.getPrimaryKey());
  }

}
