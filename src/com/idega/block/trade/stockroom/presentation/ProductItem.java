package com.idega.block.trade.stockroom.presentation;

import com.idega.block.media.presentation.ImageInserter;
import com.idega.block.image.presentation.ImageViewer;
import com.idega.core.localisation.business.ICLocaleBusiness;
import java.sql.SQLException;
import com.idega.block.trade.stockroom.business.*;
import com.idega.presentation.text.*;
import com.idega.presentation.*;
import java.util.Locale;
import com.idega.block.trade.stockroom.data.*;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class ProductItem extends Block {

  protected Product _product;
  protected int _productId = -1;
  protected Locale _locale;
  protected int _localeId = -1;
  protected Image _defaultImage;

  private String _fontStyle;

  public ProductItem() {
  }

  public void main(IWContext iwc) {
    initialize(iwc);
  }

  private void initialize(IWContext iwc) {
    String sProductId = iwc.getParameter(ProductViewer.PRODUCT_ID);
    if (sProductId != null) {
      try {
        _productId = Integer.parseInt(sProductId);
        _product = ProductBusiness.getProduct(_productId);
      }catch (SQLException sql) {
        sql.printStackTrace(System.err);
      }
    }
    this._locale = iwc.getCurrentLocale();
    this._localeId = ICLocaleBusiness.getLocaleId(_locale);

    String IMAGE_BUNDLE_IDENTIFIER="com.idega.block.image";
    _defaultImage = iwc.getApplication().getBundle(IMAGE_BUNDLE_IDENTIFIER).getLocalizedImage("picture.gif", _locale);
  }

  protected Text getText(String content) {
    Text text = new Text(content);
    if (this._fontStyle != null) {
      text.setFontStyle(this._fontStyle);
    }
    return text;
  }

  protected Image getImage(int imageId) {
    try {
      return new Image(imageId);
    }catch (SQLException sql) {
      return null;
    }
  }

  public void setFontStyle(String style) {
    this._fontStyle = style;
  }

}