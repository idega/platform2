package com.idega.block.trade.stockroom.presentation;

import javax.ejb.FinderException;
import java.rmi.RemoteException;
import com.idega.block.text.business.TextFormatter;
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

public class ProductItemDescription extends ProductItem {

  private String defaultText = "Product Description";

  public ProductItemDescription() { }
  public ProductItemDescription(int productId) throws RemoteException, FinderException{
    super(productId);
  }
  public ProductItemDescription(Product product) throws RemoteException {
    super(product);
  }

  public void main(IWContext iwc) throws Exception {
    super.main(iwc);
    drawObject(iwc);
  }

  private void drawObject(IWContext iwc) throws RemoteException{
    Text text = getText(defaultText);
    if ( _product != null ) {
      String textString = TextFormatter.formatText(_product.getProductDescription(_localeId),1,"100%");
      text.setText(textString);
    }
    add(text);
  }

}
