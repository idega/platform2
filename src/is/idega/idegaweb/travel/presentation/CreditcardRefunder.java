package is.idega.idegaweb.travel.presentation;

import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.idegaweb.*;
import is.idega.idegaweb.travel.business.TravelStockroomBusiness;
import java.util.*;
import com.idega.core.data.*;
import com.idega.block.trade.stockroom.data.*;
import com.idega.block.trade.stockroom.business.*;
import java.sql.SQLException;
import is.idega.idegaweb.travel.data.*;
import is.idega.idegaweb.travel.business.*;
import com.idega.util.IWTimestamp;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class CreditcardRefunder extends TravelManager {

  public CreditcardRefunder() {

  }


  public static Form creditcardRefunderForm(IWContext iwc, IWResourceBundle iwrb) {
    Form form = new Form();
    Table table = new Table();
    form.add(table);

    Text tSecureForm = new Text(iwrb.getLocalizedString("travel.secure_form","Secure form"));
      tSecureForm.setFontStyle(TravelManager.theBoldTextStyle);
      tSecureForm.setFontColor(TravelManager.WHITE);

    Link lSecureForm = new Link(tSecureForm);
      lSecureForm.setHttps(true);
      lSecureForm.setWindowToOpen(CreditcardRefunderWindow.class);

    form.add(lSecureForm);

    return form;
  }

}
