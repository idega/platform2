package com.idega.block.trade.stockroom.presentation;

import com.idega.presentation.*;
import com.idega.idegaweb.presentation.IWAdminWindow;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class ProductViewerWindow extends IWAdminWindow {

  public ProductViewerWindow() {
  }

  public void main(IWContext iwc) {
    ProductViewer pv = new ProductViewer();
    add(pv);
  }
}
