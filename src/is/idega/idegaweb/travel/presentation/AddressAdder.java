package is.idega.idegaweb.travel.presentation;

import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.block.trade.stockroom.business.*;
import com.idega.core.data.*;
import com.idega.block.trade.stockroom.data.*;
import java.sql.SQLException;
/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class AddressAdder extends TravelWindow {

  public static String _parameterProductId = "parameterProductId";

  private static String _parameterAddressId = "parameterAddressId";
  private static String _parameterDelete = "parameterDelete";
  private static String parameterDeparture = "addressAdderDeparture";
  private static String parameterArrival = "addressAdderArrival";

  private String sAction = "addressAdderAction";
  private String parameterSave = "addressAdderSave";
  private String parameterClose = "addressAdderClose";

  private String textInputNameAddress = "addressAddress";

  private Product _product;
  private int _productId = -1;
  private int extraFields = 3;

  public AddressAdder() {
    super.setTitle("idegaWeb Travel");
  }

  public void main(IWContext iwc) {
    super.main(iwc);
    init(iwc);



    if (_product != null) {
      String action = iwc.getParameter(sAction);
      if (action == null) action = "";

      if (action.equals("")) {
        drawForm(iwc);
      }else if (action.equals(parameterSave)) {
        if (saveDepartureAddress(iwc)) {
          drawForm(iwc);
        }else {
          error(iwc);
        }
      }else if (action.equals(parameterClose)) {
        super.close(true);
      }
    }else {
      error(iwc);
    }

  }

  private void init(IWContext iwc) {
    try {
      String sProductId = iwc.getParameter(_parameterProductId);
      if (sProductId != null) {
        _productId = Integer.parseInt(sProductId);
        _product = new Product(_productId);
      }
    }catch (SQLException sql) {
      sql.printStackTrace();
    }
  }

  private void error(IWContext iwc) {
    add("error");
  }

  private boolean saveDepartureAddress(IWContext iwc) {
    boolean returner = true;
    try {
      String[] name = iwc.getParameterValues(textInputNameAddress);
      String[] ids = iwc.getParameterValues(this._parameterAddressId);

      for (int i = 0; i < name.length; i++) {
        if (ids[i].equals("-1") ) {
          if (!name[i].equals("")) {
            Address newAddress = new Address();
              newAddress.setStreetName(name[i]);
              newAddress.setAddressTypeID(AddressType.getId(ProductBusiness.uniqueDepartureAddressType));
            newAddress.insert();
            newAddress.addTo(_product);
          }
        }else {
          if (iwc.getParameter(this._parameterDelete+ids[i]) != null) {
            Address newAddress = new Address(Integer.parseInt(ids[i]));
             newAddress.removeFrom(_product);
             newAddress.delete();
          }else if (!name[i].equals("")) {
            Address newAddress = new Address(Integer.parseInt(ids[i]));
              newAddress.setStreetName(name[i]);
            newAddress.update();
          }
        }
      }
      return true;
    }catch (Exception e) {
      return false;
    }
  }

  private void drawForm(IWContext iwc) {
    Form form = new Form();
      form.maintainParameter(_parameterProductId);

    Table table = new Table();
      form.add(table);
      table.setAlignment("center");
      table.setBorder(0);
      table.setCellpadding(2);
      table.setCellspacing(1);
    int row = 1;

    Text nameTxt = (Text) text.clone();
      nameTxt.setText(iwrb.getLocalizedString("travel.address","Address"));
      nameTxt.setFontColor(TravelManager.WHITE);
      nameTxt.setBold(true);
    Text delTxt = (Text) text.clone();
      delTxt.setText(iwrb.getLocalizedString("travel.delete","delete"));
      delTxt.setFontColor(TravelManager.WHITE);
      delTxt.setBold(true);

    try {
      Address[] addresses = ProductBusiness.getDepartureAddresses(_product);
      TextInput nameInp = new TextInput(textInputNameAddress);
      CheckBox del;

      table.add(nameTxt, 1, row);
      table.add(delTxt, 2, row);
      table.setAlignment(2,row, "center");
      table.setRowColor(row, TravelManager.backgroundColor);

      for (int i = 0; i < addresses.length; i++) {
        ++row;

        nameInp = new TextInput(textInputNameAddress);
          nameInp.setContent(addresses[i].getStreetName());
        del = new CheckBox(this._parameterDelete+addresses[i].getID());
          del.setChecked(false);

        table.add(nameInp, 1,row);
        table.add(del, 2,row);
        table.setAlignment(2,row, "center");
        table.add(new HiddenInput(this._parameterAddressId, Integer.toString(addresses[i].getID())));

        table.setRowColor(row, TravelManager.GRAY);
      }

      for (int i = 0; i < extraFields; i++) {
        ++row;
        table.mergeCells(1,row,2,row);

        nameInp = new TextInput(textInputNameAddress);
        table.add(new HiddenInput(this._parameterAddressId, "-1"));
        table.add(nameInp, 1,row);
        table.setColor(1,row, TravelManager.GRAY);
      }

      SubmitButton saveBtn = new SubmitButton(iwrb.getImage("buttons/save.gif"),sAction,parameterSave);
      SubmitButton closeBtn = new SubmitButton(iwrb.getImage("buttons/close.gif"),sAction,parameterClose);


      ++row;
      table.add(closeBtn,1,row);
      table.add(saveBtn,2,row);
      table.setAlignment(2,row,"right");
      table.setRowColor(row, TravelManager.GRAY);
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
      error(iwc);
    }

    add(form);
  }

}
