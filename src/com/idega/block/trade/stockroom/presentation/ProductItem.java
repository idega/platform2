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
  protected static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.trade";

  protected Product _product;
  protected int _productId = -1;
  protected Locale _locale;
  protected int _localeId = -1;
  protected Image _defaultImage;

  private String _fontStyle;

  public ProductItem() {
  }

  public ProductItem(int productId) throws SQLException {
    setProduct(productId);
  }

  public ProductItem(Product product) {
    setProduct(product);
  }

  public void main(IWContext iwc) {
    initialize(iwc);
  }

  public String getBundleIdentifier() {
    return IW_BUNDLE_IDENTIFIER;
  }

  private void initialize(IWContext iwc) {
    String sProductId = iwc.getParameter(ProductBusiness.PRODUCT_ID);
    if (sProductId != null) {
      if (!sProductId.equals("-1")) {
        try {
          _productId = Integer.parseInt(sProductId);
          _product = ProductBusiness.getProduct(_productId);
        }catch (SQLException sql) {
          sql.printStackTrace(System.err);
        }
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

  protected void setProduct(Product product) {
    _product = product;
    _productId = _product.getID();
  }

  protected void setProduct(int productId) throws SQLException {
    _productId = productId;
    _product = ProductBusiness.getProduct(_productId);
  }

  public void setFontStyle(String style) {
    this._fontStyle = style;
  }

}