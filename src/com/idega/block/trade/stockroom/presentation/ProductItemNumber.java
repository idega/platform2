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

public class ProductItemNumber extends ProductItem {

  private String defaultText = "Product Number";

  public ProductItemNumber() { }
  public ProductItemNumber(int productId) throws RemoteException, FinderException{
    super(productId);
  }
  public ProductItemNumber(Product product) throws RemoteException{
    super(product);
  }

  public void main(IWContext iwc) throws Exception {
    super.main(iwc);
    drawObject();
  }

  private void drawObject() throws RemoteException{
    Text text = getText(this.defaultText);
    if ( this._product != null ) {
      text.setText(this._product.getNumber());
    }
    add(text);
  }

}
