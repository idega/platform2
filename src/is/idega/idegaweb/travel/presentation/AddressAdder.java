package is.idega.idegaweb.travel.presentation;

import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.block.trade.stockroom.business.*;
import com.idega.core.data.*;
import com.idega.util.idegaTimestamp;
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
  private String parameterTime = "addressAdderTime";

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
    System.err.println("productId : "+_productId);
  }

  private void error(IWContext iwc) {
    add("error");
  }

  private boolean saveDepartureAddress(IWContext iwc) {
    boolean returner = true;
      String[] name = iwc.getParameterValues(textInputNameAddress);
      String[] ids = iwc.getParameterValues(_parameterAddressId);
      int counter = 0;
      String time = "";

      for (int i = 0; i < name.length; i++) {
        try {
          ++counter;
          time = iwc.getParameter(parameterTime+counter);

          if (ids[i].equals("-1") ) {
            if (!name[i].equals("")) {
              Address newAddress = new Address();
                newAddress.setStreetName(name[i]);
                newAddress.setAddressTypeID(AddressType.getId(ProductBusiness.uniqueDepartureAddressType));
              newAddress.insert();

              TravelAddress tAddress = new TravelAddress();
                tAddress.setAddressId(newAddress.getID());
                tAddress.setAddressTypeId(TravelAddress.ADDRESS_TYPE_DEPARTURE);
                tAddress.setTime(new idegaTimestamp("2001-01-01 "+time));
                tAddress.insert();
              tAddress.addTo(_product);
            }
          }else {
            if (iwc.getParameter(this._parameterDelete+ids[i]) != null) {
              TravelAddress tAddress = new TravelAddress(Integer.parseInt(ids[i]));
              Address newAddress = new Address(tAddress.getAddressId());
                tAddress.removeFrom(_product);
                tAddress.delete();
                newAddress.delete();
            }else if (!name[i].equals("")) {
              TravelAddress tAddress = new TravelAddress(Integer.parseInt(ids[i]));
                tAddress.setTime(new idegaTimestamp("2001-01-01 "+time));
              Address newAddress = new Address(tAddress.getAddressId());
                newAddress.setStreetName(name[i]);
                newAddress.update();
              tAddress.update();
            }
          }
        }catch (Exception e) {
          e.printStackTrace(System.err);
          // Error, nenni ekki að eyða loggnum í svona vitleysu
        }
      }
      return true;
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
    Text timeTxt = (Text) text.clone();
      timeTxt.setText(iwrb.getLocalizedString("travel.time","Time"));
      timeTxt.setFontColor(TravelManager.WHITE);
      timeTxt.setBold(true);
    Text delTxt = (Text) text.clone();
      delTxt.setText(iwrb.getLocalizedString("travel.delete","delete"));
      delTxt.setFontColor(TravelManager.WHITE);
      delTxt.setBold(true);

    try {
      TravelAddress[] addresses = ProductBusiness.getDepartureAddresses(_product);
      TextInput nameInp = new TextInput(textInputNameAddress);
      CheckBox del;
      TimeInput timeInp;

      table.add(nameTxt, 1, row);
      table.add(timeTxt, 2, row);
      table.add(delTxt, 3, row);
      table.setAlignment(3,row, "center");
      table.setRowColor(row, TravelManager.backgroundColor);
      idegaTimestamp timestamp;
      int counter = 0;
      for (int i = 0; i < addresses.length; i++) {
        ++row;
        ++counter;

        nameInp = new TextInput(textInputNameAddress);
          nameInp.setContent(addresses[i].getStreetName());
        del = new CheckBox(this._parameterDelete+addresses[i].getID());
          del.setChecked(false);
        timestamp = new idegaTimestamp(addresses[i].getTime());
        timeInp = new TimeInput(this.parameterTime+counter);
          timeInp.setHour(timestamp.getHour());
          timeInp.setMinute(timestamp.getMinute());

        table.add(nameInp, 1,row);
        table.add(timeInp, 2,row);
        table.add(del, 3,row);
        table.setAlignment(3,row, "center");
        table.add(new HiddenInput(this._parameterAddressId, Integer.toString(addresses[i].getID())));

        table.setRowColor(row, TravelManager.GRAY);
      }

      for (int i = 0; i < extraFields; i++) {
        ++row;
        ++counter;

        nameInp = new TextInput(textInputNameAddress);
        timeInp = new TimeInput(this.parameterTime+counter);
        table.add(new HiddenInput(this._parameterAddressId, "-1"));
        table.add(nameInp, 1,row);
        table.add(timeInp , 2, row);
        table.setRowColor(row, TravelManager.GRAY);
      }

      SubmitButton saveBtn = new SubmitButton(iwrb.getImage("buttons/save.gif"),sAction,parameterSave);
      SubmitButton closeBtn = new SubmitButton(iwrb.getImage("buttons/close.gif"),sAction,parameterClose);


      ++row;
      table.add(closeBtn,1,row);
      table.add(saveBtn,3,row);
      table.setAlignment(3,row,"right");
      table.setRowColor(row, TravelManager.GRAY);
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
      error(iwc);
    }

    add(form);
  }

}
