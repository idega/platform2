package com.idega.block.trade.presentation;

import java.util.Vector;
import com.idega.block.trade.business.CurrencyBusiness;
import com.idega.presentation.IWContext;
import com.idega.presentation.remotescripting.RemoteScriptHandler;
import com.idega.presentation.text.Text;
import com.idega.presentation.remotescripting.RemoteScriptCollection;
import com.idega.presentation.remotescripting.RemoteScriptingResults;
import com.idega.util.text.TextSoap;


/**
 * @author gimmi
 */
public class CurrencyCalculationCollectionHandler implements RemoteScriptCollection {

	public RemoteScriptingResults getResults(IWContext iwc) {
		String sFrom = iwc.getParameter(CurrencyCalculator.PARAMETER_FROM_CURRENCY);
		String sTo = iwc.getParameter(CurrencyCalculator.PARAMETER_TO_CURRENCY);
//		String sPrice = iwc.getParameter(RemoteScriptHandler.PARAMETER_SOURCE_PARAMETER_NAME);
		String sPrice = iwc.getParameter(CurrencyCalculator.PARAMETER_PRICE);
				
    
    
		if (sFrom != null && sTo != null && sPrice != null && !sPrice.trim().equals("")) {
      sPrice = TextSoap.findAndReplace(sPrice, ',', '.');
      String price = "";
      try {
      	price = TextSoap.decimalFormat(Float.toString(CurrencyBusiness.convertCurrency(sFrom, sTo, Float.parseFloat(sPrice))), 2)+Text.NON_BREAKING_SPACE+sTo;
      } catch (NumberFormatException e) {
      	price = "-";
      }
      
      Vector vector = new Vector();
      vector.add(price);
      RemoteScriptingResults rsr = new RemoteScriptingResults(RemoteScriptHandler.getLayerName(CurrencyCalculator.PARAMETER_PRICE), vector);
     
      return rsr;
		}
		else {
			return null;
		}
	}
}
