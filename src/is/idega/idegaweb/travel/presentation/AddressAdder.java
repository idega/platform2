package is.idega.travel.presentation;

import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class AddressAdder extends TravelWindow {

  public static String parameterDeparture = "addressAdderDeparture";
  public static String parameterArrival = "addressAdderArrival";

  private String sAction = "addressAdderAction";
  private String parameterSave = "addressAdderSave";
  private String parameterClose = "addressAdderClose";

  private String textInputNameAddress = "addressAddress";

  public AddressAdder() {
    super.setTitle("idegaWeb Travel");
  }

  public void main(IWContext iwc) {
    super.main(iwc);


    String action = iwc.getParameter(sAction);
    if (action == null) action = "";

    if (action.equals("")) {
      drawForm(iwc);
    }else if (action.equals(parameterSave)) {
      if (saveAddress(iwc)) {
        super.close(true);
      }else {
        error(iwc);
      }
    }else if (action.equals(parameterClose)) {
      super.close(false);
    }

  }

  private void error(IWContext iwc) {
    add("error");
  }

  private boolean saveAddress(IWContext iwc) {
    boolean returner = true;

    return returner;
  }

  private void drawForm(IWContext iwc) {
    Form form = new Form();

    Table table = new Table();
      form.add(table);
      table.setAlignment("center");
      table.setBorder(1);

      Text nameTxt = (Text) text.clone();
        nameTxt.setText(iwrb.getLocalizedString("travel.address","Address"));

      TextInput nameInp = new TextInput(textInputNameAddress);

      SubmitButton saveBtn = new SubmitButton(iwrb.getImage("buttons/save.gif"),sAction,parameterSave);
      SubmitButton closeBtn = new SubmitButton(iwrb.getImage("buttons/close.gif"),sAction,parameterClose);


      table.add(nameTxt,1,2);
      table.add(nameInp,2,2);
      table.add(closeBtn,1,3);
      table.add(saveBtn,2,3);
      table.setAlignment(2,3,"right");

    add(form);
  }

}