package com.idega.block.trade.presentation;

import com.idega.presentation.*;
import com.idega.presentation.ui.*;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class CurrencyCalculatorWindow extends Window {

  public CurrencyCalculatorWindow() {
    this.setTitle("Currency Calculator");
  }

  public void main(IWContext iwc) {
    CurrencyCalculator cc = new CurrencyCalculator();
    add(cc);
  }

}
