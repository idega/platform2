package com.idega.block.trade.stockroom.presentation;

import java.text.NumberFormat;
import java.util.Locale;
import java.sql.SQLException;
import com.idega.block.trade.data.Currency;
import com.idega.block.trade.stockroom.business.*;
import com.idega.presentation.text.*;
import com.idega.block.trade.stockroom.data.*;
import com.idega.presentation.*;

/**
 *  Title: idegaWeb TravelBooking Description: Copyright: Copyright (c) 2001
 *  Company: idega
 *
 *@author     <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 *@created    9. mars 2002
 *@version    1.0
 */

public class ProductItemPrice extends ProductItem {

  private String defaultText = "Product Price";
  private boolean showCurrency = false;

  public ProductItemPrice() { }
  public ProductItemPrice(int productId) throws SQLException{
    super(productId);
  }
  public ProductItemPrice(Product product) {
    super(product);
  }

  public void main(IWContext iwc) {
    super.main(iwc);
    drawObject(iwc);
  }

  private void drawObject(IWContext iwc) {
    Text text = getText(defaultText);
    if ( _product != null ) {
      ProductPrice pPrice = StockroomBusiness.getPrice(_product);
      if (pPrice != null && pPrice.getPrice() > 0 ) {
	NumberFormat format = NumberFormat.getCurrencyInstance(iwc.getCurrentLocale());
	text.setText(format.format((double)pPrice.getPrice()));
	if (this.showCurrency) {
	  try {
	    text.addToText(Text.NON_BREAKING_SPACE);
	    text.addToText(((com.idega.block.trade.data.CurrencyHome)com.idega.data.IDOLookup.getHomeLegacy(Currency.class)).findByPrimaryKeyLegacy(pPrice.getCurrencyId()).getName());
	  }catch (SQLException sql) {}
	}
      }else {
	text.setText("");
      }
    }
    add(text);
  }

  public void setShowCurrency(boolean show) {
    this.showCurrency = show;
  }

}
