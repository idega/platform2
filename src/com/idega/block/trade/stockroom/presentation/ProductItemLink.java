package com.idega.block.trade.stockroom.presentation;

import java.rmi.RemoteException;
import javax.ejb.FinderException;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Link;

/**
 *  Title: idegaWeb TravelBooking Description: Copyright: Copyright (c) 2001
 *  Company: idega
 *
 *@author     <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 *@created    9. mars 2002
 *@version    1.0
 */

public class ProductItemLink extends ProductItem {

  public ProductItemLink() { }
  public ProductItemLink(int productId) throws RemoteException, FinderException{
    super(productId);
  }
  public ProductItemLink(Product product) throws RemoteException {
    super(product);
  }

  public void main(IWContext iwc) throws Exception {
    super.main(iwc);
    drawObject(iwc);
  }

  private void drawObject(IWContext iwc) throws RemoteException{
    Link link = new Link(new ProductItemName());
      link.addParameter(getProductBusiness(iwc).getProductIdParameter(), _productId);
    add(link);
  }

}
