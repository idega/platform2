package com.idega.block.trade.stockroom.presentation;

import java.rmi.RemoteException;

import javax.ejb.FinderException;

import com.idega.block.trade.stockroom.data.Product;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Text;

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
  public ProductItemName(int productId) throws RemoteException, FinderException{
    super(productId);
  }
  public ProductItemName(Product product) throws RemoteException{
    super(product);
  }

  public void main(IWContext iwc) throws Exception {
    super.main(iwc);
    drawObject();
  }

  private void drawObject() throws RemoteException{
    Text text = getText(defaultText);
    if ( _product != null ) {
      text.setText(_product.getProductName(_productLocaleId));
    }
    add(text);
  }

}
