package com.idega.block.trade.stockroom.presentation;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Locale;

import javax.ejb.FinderException;

import com.idega.block.trade.stockroom.business.ProductBusiness;
import com.idega.block.trade.stockroom.business.StockroomBusiness;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.ProductHome;
import com.idega.business.IBOLookup;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;

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
  protected IWResourceBundle _iwrb;
  protected IWBundle _iwb;
  protected IWResourceBundle _productIWRB;
  protected Locale _productLocale;
  protected int _productLocaleId = -1;
  protected String _fontStyle;
  protected String _headerFontStyle;

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
    if (sProductId != null && _product == null) {
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
    _iwb = getBundle(iwc);
    this._iwrb = _iwb.getResourceBundle(iwc);

    String IMAGE_BUNDLE_IDENTIFIER="com.idega.block.media";
    _defaultImage = iwc.getIWMainApplication().getBundle(IMAGE_BUNDLE_IDENTIFIER).getLocalizedImage("picture.gif", _locale);

    if (_product != null) {
	    if ( null == _product.getProductName(_localeId, null)) {
		  		_productLocale = iwc.getIWMainApplication().getSettings().getDefaultLocale();
		  		_productLocaleId = ICLocaleBusiness.getLocaleId(_productLocale);
		  		_productIWRB = _iwb.getResourceBundle(_productLocale);
	    } else {
	    		_productLocale = _locale;
	    		_productLocaleId = _localeId;
		  		_productIWRB = _iwrb;
	    }
    }
    
  }

  protected Text getText(String content) {
    Text text = new Text(content);
    if (this._fontStyle != null) {
      text.setFontStyle(this._fontStyle);
    }
    return text;
  }

  protected Link getLink(String content, String URL) {
		Link link = new Link(content, URL);
		link.setTarget(Link.TARGET_NEW_WINDOW);
		if (this._fontStyle != null) {
			link.setFontStyle(this._fontStyle);
		}
		return link;
  }

  protected Text getHeaderText(String content) {
	Text text = new Text(content);
	if (this._headerFontStyle != null) {
	  text.setFontStyle(this._headerFontStyle);
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

  public void setHeaderFontStyle(String style) {
		this._headerFontStyle = style;
  }

  protected StockroomBusiness getStockroomBusiness(IWApplicationContext iwac) throws RemoteException{
    return (StockroomBusiness) IBOLookup.getServiceInstance(iwac, StockroomBusiness.class);
  }

  protected ProductBusiness getProductBusiness(IWApplicationContext iwac) throws RemoteException {
    return (ProductBusiness) IBOLookup.getServiceInstance(iwac, ProductBusiness.class);
  }
}
