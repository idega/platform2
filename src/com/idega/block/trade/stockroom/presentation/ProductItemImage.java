package com.idega.block.trade.stockroom.presentation;

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

public class ProductItemImage extends ProductItem {

  private Image defaultImage = _defaultImage;
  private int _width = 0;
  private int _height = 0;

  public ProductItemImage() { }

  public void main(IWContext iwc) {
    super.main(iwc);
    drawObject();
  }

  private void drawObject() {
    Image image = defaultImage;
    if ( _product != null ) {
      int fileId = _product.getFileId();
      if (fileId != -1) {
        image = getImage(fileId);
      }
    }

    if (image != null) {
      if (_width > 0) image.setWidth(_width);
      if (_height > 0) image.setWidth(_height);
      add(image);
    }

  }

  public void setWidth(int width) {
    _width = width;
  }

  public void setHeight(int height){
    _height = height;
  }


}
