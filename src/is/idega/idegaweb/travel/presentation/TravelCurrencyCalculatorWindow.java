package is.idega.idegaweb.travel.presentation;

import com.idega.block.trade.presentation.CurrencyCalculator;
import com.idega.presentation.*;
/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class TravelCurrencyCalculatorWindow extends TravelWindow {

  public TravelCurrencyCalculatorWindow() {
    this.setTitle("Currency Calculator");
  }

  public void main(IWContext iwc) {
    super.main(iwc);
    CurrencyCalculator cc = new CurrencyCalculator();
    add(cc);
  }

}
