package is.idega.idegaweb.travel.presentation;

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

public class Voucher extends TravelManager {

  private IWContext _iwc;

  public Voucher(IWContext iwc) throws Exception{
    super.main(iwc);
    _iwc = iwc;
  }

  private Text getText(String content) {
    Text text = (Text) theText.clone();
      text.setFontColor(BLACK);
      text.setText(content);
    return text;
  }

  public Table getVoucher() {
    Table bigTable = new Table();
      bigTable.setColor(BLACK);
    Table table = new Table();
      table.setColor(WHITE);
      table.setWidth("100%");

    return bigTable;
  }
}