package is.idega.idegaweb.travel.presentation;

import com.idega.block.trade.presentation.CurrencyCalculator;
import com.idega.presentation.*;
import com.idega.presentation.text.Link;
/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class TravelCurrencyCalculatorWindow extends TravelWindow {

	public static String PARAMETER_IMAGE_ID = "tccw_im";

  public TravelCurrencyCalculatorWindow() {
    this.setTitle("Currency Calculator");
  }

  public void main(IWContext iwc) throws Exception {
		if (iwc.isParameterSet(PARAMETER_IMAGE_ID)) {
			Image image = new Image(Integer.parseInt(iwc.getParameter(PARAMETER_IMAGE_ID)));
			super.setHeaderImage(image);
		}
    super.main(iwc);
    
    CurrencyCalculator cc = new CurrencyCalculator();
    cc.maintainParameter(PARAMETER_IMAGE_ID);
    add(cc);
  }
  
  public static void setHeaderImage(Link link, int imageId) {
  	link.addParameter(PARAMETER_IMAGE_ID, imageId);
  }

}
