package com.idega.block.trade.stockroom.presentation;

import javax.ejb.FinderException;
import com.idega.data.IDOLookup;
import java.rmi.RemoteException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.business.IBOLookup;
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

  public ProductItem(int productId) throws FinderException, RemoteException {
    setProduct(productId);
  }

  public ProductItem(Product product) throws RemoteException {
    setProduct(product);
  }

  public void main(IWContext iwc) throws Exception{
    initialize(iwc);
  }

  public String getBundleIdentifier() {
    return IW_BUNDLE_IDENTIFIER;
  }

  private void initialize(IWContext iwc) throws RemoteException{
    String sProductId = iwc.getParameter(getProductBusiness(iwc).getProductIdParameter());
    if (sProductId != null) {
      if (!sProductId.equals("-1")) {
        try {
          _productId = Integer.parseInt(sProductId);
          _product = getProductBusiness(iwc).getProduct(_productId);
        }catch (FinderException sql) {
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

  protected void setProduct(Product product) throws RemoteException{
    _product = product;
    _productId = _product.getID();
  }

  protected void setProduct(int productId) throws FinderException, RemoteException {
    _productId = productId;
    _product = ((ProductHome) IDOLookup.getHomeLegacy(Product.class)).findByPrimaryKey(new Integer(_productId));
//    _product = getProductBusiness.getProduct(_productId);
  }

  public void setFontStyle(String style) {
    this._fontStyle = style;
  }

  protected StockroomBusiness getStockroomBusiness(IWApplicationContext iwac) throws RemoteException{
    return (StockroomBusiness) IBOLookup.getServiceInstance(iwac, StockroomBusiness.class);
  }

  protected ProductBusiness getProductBusiness(IWApplicationContext iwac) throws RemoteException {
    return (ProductBusiness) IBOLookup.getServiceInstance(iwac, ProductBusiness.class);
  }
}
