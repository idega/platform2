package com.idega.block.tpos.presentation;

import com.idega.presentation.*;
import com.idega.presentation.ui.Window;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class ReceiptWindow extends Window {

  public static final String RECEIPT_SESSION_NAME = "rec_win_receipt";


  public ReceiptWindow() {
    this.setTitle("Receipt");
    this.setWidth(300);
    this.setHeight(350);
    this.setMenubar(true);
  }

  public void main(IWContext iwc) {
    Object obj = iwc.getSessionAttribute(RECEIPT_SESSION_NAME);
    if (obj != null) {
//      iwc.removeSessionAttribute(RECEIPT_SESSION_NAME);
      Receipt r = (Receipt) ((Receipt) obj).clone();
      Table table = new Table();
        table.setWidth("100%");
        table.add(r);
        table.setAlignment("center");
        table.setAlignment(1, 1, "center");
      add(table);
    }
  }

}
