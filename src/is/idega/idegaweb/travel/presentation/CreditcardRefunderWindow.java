package is.idega.idegaweb.travel.presentation;

import com.idega.presentation.*;
import com.idega.presentation.ui.*;
/**
 * Title:        idegaWeb Travel
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href mailto:"gimmi@idega.is">Grímur Jónsson</a>
 * @version 1.0
 */

public class CreditcardRefunderWindow extends TravelWindow {

  private String sAction = "ccrAction";

  public CreditcardRefunderWindow() {
  }

  public void main(IWContext iwc){
    super.main(iwc);

    String action = iwc.getParameter(this.sAction);
    if (action == null) {
      getRefundForm();

    }else {

    }


  }


  public void getRefundForm() {
    Form form = new Form();
    Table table = new Table();
    form.add(table);

    table.add("test");

    add(form);
  }

}