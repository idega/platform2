package com.idega.block.trade.stockroom.presentation;

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

  public ProductItemPrice() { }

  public void main(IWContext iwc) {
    super.main(iwc);
    drawObject();
  }

  private void drawObject() {
    Text text = getText(defaultText);
    if ( _product != null ) {
      ProductPrice pPrice = StockroomBusiness.getPrice(_product);
      if (pPrice != null) {
        text.setText(Integer.toString((int) pPrice.getPrice()));
      }else {
        text.setText("0");
      }
    }
    add(text);
  }

}
