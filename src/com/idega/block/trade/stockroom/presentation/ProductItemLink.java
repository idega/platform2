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

public class ProductItemLink extends ProductItem {

  private Image defaultImage = _defaultImage;
  private int _width = 0;
  private int _height = 0;

  public ProductItemLink() { }
  public ProductItemLink(int productId) throws SQLException{
    super(productId);
  }
  public ProductItemLink(Product product) {
    super(product);
  }

  public void main(IWContext iwc) {
    super.main(iwc);
    drawObject();
  }

  private void drawObject() {
    Link link = new Link(new ProductItemName());
      link.addParameter(ProductBusiness.PRODUCT_ID, _productId);
    add(link);
  }

}
