package com.idega.block.trade.stockroom.presentation;

import com.idega.builder.data.IBPage;
import com.idega.block.trade.stockroom.business.*;
import com.idega.block.trade.stockroom.data.*;
import com.idega.core.business.*;
import com.idega.core.data.*;
import com.idega.core.localisation.business.*;
import com.idega.idegaweb.*;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import java.util.*;

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
  private List categoryList;

  Locale _locale;
  int _localeId = -1;
  String _fontStyle;
  String _headerFontStyle;
  Image _seperator = null;
  boolean _useHRasSeperator = false;
  boolean _showRandom = false;
  boolean _showNewest = false;
  String _imageWidth = null;
  String _textAlignment = Paragraph.HORIZONTAL_ALIGN_LEFT;
  String _imageAlignment = Paragraph.HORIZONTAL_ALIGN_RIGHT;
  IBPage _productPage;
  Image _productImage;
  boolean _showProductLink = false;
  boolean _showTeaser = false;
  boolean _showThumbnail = false;
  String _spaceBetween = "3";
  boolean _showImages = true;

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

      if ( _product != null ) {
	List list = ProductBusiness.getProductCategories(_product);
	if ( list != null && categoryList != null ) {
	  boolean showProduct = false;
	  Iterator iter = list.iterator();
	  while (iter.hasNext()) {
	    if ( categoryList.contains(iter.next()) )
	      showProduct = true;
	  }
	  if ( !showProduct )
	    _product = null;
	}
      }
    } catch (Exception e) {
      e.printStackTrace(System.err);
    }

    if ( _product == null ) {
      if ( _showRandom ) {
	if ( categoryList != null ) {
	  List products = ProductBusiness.getProducts(categoryList);
	  if ( products != null && products.size() > 0 ) {
	    int random = (int) Math.round(Math.random() * (products.size() - 1));
	    this._product = (Product) products.get(random);
	    this._productId = _product.getID();
	  }
	}
      }
      if ( _showNewest ) {
	if ( categoryList != null ) {
	  List products = ProductBusiness.getProducts(categoryList);
	  if ( products != null && products.size() > 0 ) {
	    Collections.sort(products,new ProductComparator(ProductComparator.CREATION_DATE));
	    this._product = (Product) products.get(0);
	    this._productId = _product.getID();
	  }
	}
      }
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
    text.setHorizontalAlignment(_textAlignment);
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

  public void setImageWidth(String width) {
    this._imageWidth = width;
  }

  public void setDescriptionAlignment(String alignment) {
    _textAlignment = alignment;
  }

  public void setImageAlignment(String alignment) {
    _imageAlignment = alignment;
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

  public void setCategory(String name,String categoryID) {
    if ( categoryList == null )
      categoryList = new Vector();
    ProductCategory category = ProductBusiness.getProductCategory(Integer.parseInt(categoryID));
    if ( category != null )
      categoryList.add(category);
  }

  public void setShowRandomProduct(boolean showRandom) {
    _showRandom = showRandom;
    _showNewest = !showRandom;
  }

  public void setProductPage(IBPage page) {
    _productPage = page;
  }

  public void setProductImage(Image image) {
    _productImage = image;
  }

  public void setShowProductLink(boolean showLink) {
    _showProductLink = showLink;
  }

  public void setShowTeaser(boolean showTeaser) {
    _showTeaser = showTeaser;
  }

  public void setShowThumbnail(boolean showThumbnail) {
    _showThumbnail = showThumbnail;
  }

  public void setSpaceBetweenTitleAndBody(String spaceBetween) {
    _spaceBetween = spaceBetween;
  }

  public void setShowNewestProduct(boolean showNewest) {
    _showNewest = showNewest;
    _showRandom = !showNewest;
  }

  public void setShowImages(boolean showImages) {
    _showImages = showImages;
  }
}

