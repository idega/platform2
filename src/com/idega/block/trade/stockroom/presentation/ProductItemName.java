package com.idega.block.trade.stockroom.presentation;

import java.sql.SQLException;
import com.idega.presentation.text.*;
import com.idega.block.trade.stockroom.data.*;
import com.idega.presentation.*;
import com.idega.block.trade.stockroom.business.ProductBusiness;
import com.idega.block.trade.stockroom.presentation.ProductItem;

/**
 *  Title: idegaWeb TravelBooking Description: Copyright: Copyright (c) 2001
 *  Company: idega
 *
 *@author     <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 *@created    9. mars 2002
 *@version    1.0
 */

public class ProductItemName extends ProductItem {

  private String defaultText = "Product Name";

  public ProductItemName() { }
  public ProductItemName(int productId) throws SQLException{
    super(productId);
  }
  public ProductItemName(Product product) {
    super(product);
  }

  public void main(IWContext iwc) {
    super.main(iwc);
    drawObject();
  }

  private void drawObject() {
    Text text = getText(defaultText);
    if ( _product != null ) {
      text.setText(ProductBusiness.getProductName(_product,_localeId));
    }
    add(text);
  }

}
