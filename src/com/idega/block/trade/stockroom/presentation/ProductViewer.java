package com.idega.block.trade.stockroom.presentation;

import com.idega.core.localisation.business.ICLocaleBusiness;
import java.util.Locale;
import com.idega.presentation.text.*;
import com.idega.block.trade.stockroom.business.*;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.idegaweb.*;
import com.idega.presentation.*;
import com.idega.presentation.Block;

/**
 *  Title: idegaWeb TravelBooking Description: Copyright: Copyright (c) 2001
 *  Company: idega
 *
 *@author     <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 *@created    9. mars 2002
 *@version    1.0
 */

public class ProductViewer extends Block {
  private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.trade";

  private IWResourceBundle iwrb;
  private IWBundle bundle;
  private Image iEdit = null;

  private Product _product = null;
  private int _productId = -1;

  private Class _viewerLayoutClass = ProductViewerLayoutIdega.class;
  private String _width;

  Locale _locale;
  int _localeId = -1;
  String _fontStyle;
  String _headerFontStyle;
  Image _seperator = null;
  boolean _useHRasSeperator = false;

  public ProductViewer() { }



  public void main(IWContext iwc) {

    init(iwc);

    if (hasEditPermission()) {
      Link lEdit = ProductEditorWindow.getEditorLink(_productId);
      lEdit.setImage(iEdit);
      add(lEdit);
    }

    getViewer(iwc);

  }

  public String getBundleIdentifier() {
      return IW_BUNDLE_IDENTIFIER;
  }

  private void init(IWContext iwc) {
    bundle = getBundle(iwc);
    iwrb = bundle.getResourceBundle(iwc);

    this._locale = iwc.getCurrentLocale();
    this._localeId = ICLocaleBusiness.getLocaleId(_locale);

    try {
      String sProductId = iwc.getParameter(ProductBusiness.PRODUCT_ID);
      if (sProductId != null) {
        _productId = Integer.parseInt(sProductId);
        _product = ProductBusiness.getProduct(_productId);
        if (!_product.getIsValid()) {
          _product = null;
        }
      }
    } catch (Exception e) {
      e.printStackTrace(System.err);
    }

    IWBundle coreBundle = iwc.getApplication().getCoreBundle();
    iEdit = coreBundle.getImage("shared/edit.gif");
  }



  private void getViewer(IWContext iwc) {
    try {
      AbstractProductViewerLayout layout = (AbstractProductViewerLayout) this._viewerLayoutClass.newInstance();
      PresentationObject po = null;

      if (this._product == null) {
        po = layout.getDemo(this, iwc);
      }else {
        po = layout.getViewer(this, _product, iwc);
      }

      Table table = new Table(1, 1);
      table.setCellpadding(0);
      table.setCellspacing(0);

      if (_width != null)
        table.setWidth(_width);
      table.add(po);

      add(table);
    } catch (IllegalAccessException iae) {
      iae.printStackTrace(System.err);
    } catch (InstantiationException ie) {
      ie.printStackTrace(System.err);
    }
  }

  Text getText(String content) {
    Text text = new Text(content);
    if (this._fontStyle != null) text.setFontStyle(_fontStyle);
    return text;
  }

  Text getHeaderText(String content) {
    Text text = new Text(content);
    if (this._headerFontStyle != null) text.setFontStyle(_headerFontStyle);
    return text;
  }

  public void setFontStyle(String style) {
    this._fontStyle = style;
  }

  public void setHeaderFontStyle(String style) {
    this._headerFontStyle = style;
  }

  public void setWidth(String width) {
    this._width = width;
  }

  public void setLayoutClassName(String className) {
    try {
      this._viewerLayoutClass = Class.forName(className);
    } catch (ClassNotFoundException cnf) {
      cnf.printStackTrace(System.err);
    }
  }

  public void setSeperatorImage(Image seperator) {
    this._seperator = seperator;
  }

  public void setUseHorizontalRuleAsSeperator(boolean use) {
    this._useHRasSeperator = use;
  }
}

