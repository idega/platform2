package com.idega.block.trade.stockroom.presentation;

import com.idega.data.IDOFinderException;
import com.idega.core.business.CategoryFinder;
import com.idega.core.localisation.business.*;
import com.idega.core.data.*;
import com.idega.block.category.business.CategoryBusiness;
import com.idega.builder.data.IBPage;
import com.idega.data.EntityFinder;
import com.idega.block.presentation.CategoryBlock;
import com.idega.util.idegaTimestamp;
import java.util.*;

import com.idega.idegaweb.*;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.presentation.Block;
import com.idega.block.trade.stockroom.business.*;
import com.idega.block.trade.stockroom.data.*;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class ProductCatalog extends CategoryBlock{
  private static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.trade";
  private static final String _VIEW_PAGE = "prod_cat_view_page";
  private static final String _ORDER_BY ="prod_cat_order_by";
  public static final String CACHE_KEY ="prod_cat_cache_key";
  public static final String CATEGORY_ID = "pr_cat_id";


  int productsPerPage = 10;
  int currentPage = 1;
  int orderBy = -1;
  int _numberOfColumns = 1;

  IWResourceBundle iwrb;
  IWContext iwc;
  private IWBundle bundle;

  Image iCreate = null;
  Image iDelete = null;
  Image iEdit = null;
  Image iDetach = null;

  private String _fontStyle = null;
  private String _catFontStyle = null;
  private String _teaserFontStyle = null;
  private String _anchorString = "prodCatAnchorID_";

  String _width = null;
  IBPage _productLinkPage = null;
  String _windowString = null;
  boolean _expandSelectedOnly = false;
  boolean _productIsLink = false;
  boolean _showCategoryName = true;
  boolean _showCurrency = false;
  boolean _showDescription = false;
//  boolean _showImage = false;
  boolean _showNumber = false;
  boolean _showPrice = false;
  boolean _showTeaser = false;
  boolean _showThumbnail = false;
  boolean _useAnchor = false;
  int _orderProductsBy = -1;
  boolean _addCategoryID = false;
  int _spaceBetween = 0;
  int _iconSpacing = 4;

  Locale _currentLocale = null;
  int _currentLocaleId = -1;
  boolean _hasEditPermission = false;
  boolean _allowMulitpleCategories = true;
  boolean _useParameterCategory = false;
  Image _iconImage;
  List productCategories;

  private Class _layoutClass = ProductCatalogLayoutSingleFile.class;
  private AbstractProductCatalogLayout layout = null;

  public ProductCatalog() {
    super.setCacheable(CACHE_KEY, 999999999);
    super.setAutoCreate(false);
  }

  public void main(IWContext iwc) {
    init(iwc);
    catalog(iwc);
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }



  private void init(IWContext iwc) {
    bundle = getBundle(iwc);
    iwrb = bundle.getResourceBundle(iwc);
    this.iwc = iwc;


    setAutoCreate(false);

    this._currentLocale = iwc.getCurrentLocale();
    this._currentLocaleId = ICLocaleBusiness.getLocaleId(_currentLocale);
    this._hasEditPermission = this.hasEditPermission();
    if ( iwc.isInPreviewMode() )
      this._hasEditPermission = false;

    IWBundle  coreBundle = iwc.getApplication().getCoreBundle();
    iCreate = coreBundle.getImage("shared/create.gif");
    iDelete = coreBundle.getImage("shared/delete.gif");
    iEdit   = coreBundle.getImage("shared/edit.gif");
    iDetach = coreBundle.getImage("shared/detach.gif");

    try {
      layout = (AbstractProductCatalogLayout) this._layoutClass.newInstance();
    }catch (IllegalAccessException iae) {
      iae.printStackTrace(System.err);
    }catch (InstantiationException ie) {
      ie.printStackTrace(System.err);
    }

    try {
      String sCurrentPage = iwc.getParameter(this._VIEW_PAGE);
      String sOrderBy = iwc.getParameter(this._ORDER_BY);
      if (sCurrentPage != null) {
	this.currentPage = Integer.parseInt(sCurrentPage);
      }
      if (sOrderBy != null) {
	this.orderBy = Integer.parseInt(sOrderBy);
      }
    }catch (NumberFormatException n) {}


  }

  private void catalog(IWContext iwc) {
    try {
      Link createLink = ProductEditorWindow.getEditorLink(-1);
	createLink.setImage(iCreate);
	createLink.addParameter(ProductEditorWindow.PRODUCT_CATALOG_OBJECT_INSTANCE_ID, getICObjectInstanceID());
      Link detachLink = getCategoryLink(com.idega.block.trade.stockroom.data.ProductCategoryBMPBean.CATEGORY_TYPE_PRODUCT);
	detachLink.setImage(iDetach);

      if (hasEditPermission()) {
	add(createLink);
	add(detachLink);
      }

      layout = (AbstractProductCatalogLayout) this._layoutClass.newInstance();

      productCategories = new Vector();
      if ( iwc.isParameterSet(CATEGORY_ID) ) {
	String[] categoryIDs = iwc.getParameterValues(CATEGORY_ID);
	for ( int a = 0; a < categoryIDs.length; a++ ) {
	  productCategories.add(CategoryFinder.getInstance().getCategory(Integer.parseInt(categoryIDs[a])));
	}
      }

      if ( productCategories.size() == 0 || !_useParameterCategory ) {
	try {
	  productCategories = (List) getCategories();
	  if (productCategories == null) {
	    productCategories = new Vector();
	  }
	}catch (Exception e) {
	  e.printStackTrace(System.err);
	}
      }

      Table table = new Table();
	table.setCellpadding(0);
	table.setCellspacing(0);
	if (_width != null) {
	  table.setWidth(_width);
	}
      PresentationObject po = layout.getCatalog(this, iwc, productCategories);

      table.add(po);

      add(table);
    }catch (IllegalAccessException iae) {
      iae.printStackTrace(System.err);
    }catch (InstantiationException ie) {
      ie.printStackTrace(System.err);
    }
  }



  Table getPagesTable(int pages, List parameters) {
      Table pagesTable = new Table(pages+2, 1);
	pagesTable.setCellpadding(2);
	pagesTable.setCellspacing(2);

      if (parameters == null) parameters = new Vector();
      Parameter parameter;

      Text pageText;
      if (currentPage > 1) {
	pageText = getText(iwrb.getLocalizedString("travel.previous","Previous"));
	Link prevLink = new Link(pageText);
	  prevLink.addParameter(_VIEW_PAGE, currentPage -1);
	  for (int l = 0; l < parameters.size(); l++) {
	    parameter = (Parameter) parameters.get(l);
	    prevLink.addParameter(parameter);
	  }

	pagesTable.add(prevLink, 1, 1);
      }

      Link pageLink;
      for (int i = 1; i <= pages; i++) {
	if (i == currentPage) {
	  pageText = getText(Integer.toString(i));
	  pageText.setBold(true);
	}else {
	  pageText = getText(Integer.toString(i));
	}
	pageLink = new Link(pageText);
	  pageLink.addParameter(_VIEW_PAGE, i);
	  for (int l = 0; l < parameters.size(); l++) {
	    parameter = (Parameter) parameters.get(l);
	    pageLink.addParameter(parameter);
	  }


	pagesTable.add(pageLink, i+1, 1);
      }

      if (currentPage < pages) {
	pageText = getText(iwrb.getLocalizedString("travel.next","Next"));
	Link nextLink = new Link(pageText);
	  nextLink.addParameter(_VIEW_PAGE, currentPage + 1);
	  for (int l = 0; l < parameters.size(); l++) {
	    parameter = (Parameter) parameters.get(l);
	    nextLink.addParameter(parameter);
	  }
	pagesTable.add(nextLink, pages + 2, 1);
      }

      return pagesTable;
  }

  public void setFontStyle(String fontStyle) {
    this._fontStyle = fontStyle;
  }

  public void setCategoryFontStyle(String catFontStyle) {
    this._catFontStyle = catFontStyle;
  }

  public void setTeaserFontStyle(String teaserFontStyle) {
    this._teaserFontStyle = teaserFontStyle;
  }

  public void setItemsPerPage(int numberOfItems) {
    this.productsPerPage = numberOfItems;
  }

  public void setProductLinkPage(IBPage page) {
    this._productLinkPage = page;
  }

  public void setProductAsLink(boolean isLink) {
    this._productIsLink = isLink;
  }

  public void setAllowMultipleCategories(boolean allow) {
    this._allowMulitpleCategories = allow;
  }

  public void setWidth(String width) {
    this._width = width;
  }

  public String getCategoryType(){
    return com.idega.block.trade.stockroom.data.ProductCategoryBMPBean.CATEGORY_TYPE_PRODUCT;
  }

  public void setShowCategoryName(boolean showName) {
    this._showCategoryName = showName;
  }
/*
  public void setShowImage(boolean showImage) {
    this._showImages = showImage;
  }*/

  public void setShowTeaser(boolean showTeaser) {
    this._showTeaser = showTeaser;
  }

  public void setUseAnchor(boolean useAnchor) {
    this._useAnchor = useAnchor;
  }

  public void setViewerToOpenInWindow(String windowString) {
    this._windowString = windowString;
  }

  public void setExpandSelectedOnly(boolean expaneSelectedOnly) {
    this._expandSelectedOnly = expaneSelectedOnly;
  }

  public void setShowDescription(boolean showDescription) {
    this._showDescription = showDescription;
  }

  public void setShowPrice(boolean showPrice) {
    this._showPrice = showPrice;
  }

  public void setShowCurrency(boolean showCurrency) {
    this._showCurrency = showCurrency;
  }

  public void setShowNumber(boolean showNumber) {
    this._showNumber = showNumber;
  }

  public void setShowThumbnail(boolean showThumbnail) {
    this._showThumbnail = showThumbnail;
  }

  public boolean getMultible() {
    return this._allowMulitpleCategories;
  }

  public boolean deleteBlock(int ICObjectInstanceId) {
    //return super.removeInstanceCategories();
    return CategoryBusiness.getInstance().disconnectBlock(ICObjectInstanceId);
  }

  public void setLayoutClassName(String className) {
    try {
      this._layoutClass = Class.forName(className);
    }catch (ClassNotFoundException cnf) {
      cnf.printStackTrace(System.err);
    }
  }

  public void setNumberOfColumns(int numberOfColumns) {
    this._numberOfColumns = numberOfColumns;
  }

  public void setOrderBy(int orderProductsBy) {
    this._orderProductsBy = orderProductsBy;
  }


  Text getText(String content) {
    Text text = new Text(content);
    if (_fontStyle != null) {
      text.setFontStyle(_fontStyle);
    }
    //text.setHorizontalAlignment(Paragraph.HORIZONTAL_ALIGN_JUSTIFY);
    return text;
  }

  Text getCategoryText(String content) {
    Text text = new Text(content);
    if (_catFontStyle != null) {
      text.setFontStyle(_catFontStyle);
    }
    return text;
  }

  Text getTeaserText(String content) {
    Text text = new Text(content);
    if (_teaserFontStyle != null) {
      text.setFontStyle(_teaserFontStyle);
    }
    //text.setHorizontalAlignment(Paragraph.HORIZONTAL_ALIGN_JUSTIFY);
    return text;
  }

  String getAnchorString(int productId) {
    return this._anchorString+productId;
  }

  Anchor getAnchor(int productId) {
    Anchor anchor = new Anchor(getAnchorString(productId));
    return anchor;
  }

  PresentationObject getNamePresentationObject(Product product) {
    return getNamePresentationObject(product, false);
  }

  PresentationObject getNamePresentationObject(Product product, boolean useCategoryStyle) {
    return getNamePresentationObject(product, ProductBusiness.getProductName(product,_currentLocaleId), useCategoryStyle);
  }

  PresentationObject getNamePresentationObject(Product product, String displayString, boolean useCategoryStyle) {
    Text nameText = null;
    if (useCategoryStyle) {
      nameText = getCategoryText(displayString);
    }else {
      nameText = getText(displayString);
    }
    Link productLink;

    /**
     * @todo Fix LINK to handle popup anchor....
     */
     if (this._windowString != null) {
      _useAnchor = false;
     }

    if (_productIsLink) {
      productLink = getNameLink(product, nameText, _useAnchor);
      if (productLink != null) {
	return productLink;
      }else {
	return nameText;
      }
    }else {
      return nameText;
    }
  }

  Link getNameLink(Product product, Text nameText, boolean useAnchor) {
    Link productLink;
    if (_productIsLink) {
      if (useAnchor) {
	productLink = new AnchorLink(nameText, getAnchorString(product.getID()));
      }else {
	productLink = new Link(nameText);
      }

      productLink.addParameter(ProductBusiness.PRODUCT_ID, product.getID());
      if (_productLinkPage != null) {
	productLink.setPage(_productLinkPage);
      }else if (this._windowString != null) {
	productLink.setWindowToOpenScript(_windowString);
      }

      if ( _addCategoryID ) {
	try {
	  if ( productCategories != null && productCategories.size() > 0 && _useParameterCategory ) {
	    List list = productCategories;
	    if ( list != null ) {
	      Iterator iter = list.iterator();
	      while (iter.hasNext()) {
		productLink.addParameter(CATEGORY_ID,((ICCategory)iter.next()).getID());
	      }
	    }
	  }
	  else {
	    List list = ProductBusiness.getProductCategories(product);
	    if ( list != null ) {
	      Iterator iter = list.iterator();
	      while (iter.hasNext()) {
		productLink.addParameter(CATEGORY_ID,((ProductCategory)iter.next()).getID());
	      }
	    }
	  }
	}
	catch (IDOFinderException e) {

	}
	//productLink.addParameter(CATEGORY_ID,product.g);
      }

      return productLink;
    }else{
      return null;
    }
  }

  Link getCategoryLink(ICCategory category, String nameText) {
    Link categoryLink = new Link(getCategoryText(nameText));

    if (_productLinkPage != null) {
      categoryLink.setPage(_productLinkPage);
    }

    if ( _addCategoryID ) {
      categoryLink.addParameter(CATEGORY_ID,category.getID());
    }

    return categoryLink;
  }

  Link getProductEditorLink(Product product) {
    Link link =  ProductEditorWindow.getEditorLink(product.getID());
      link.setImage(this.iEdit);
      link.addParameter(ProductEditorWindow.PRODUCT_CATALOG_OBJECT_INSTANCE_ID, this.getICObjectInstanceID());
    return link;
  }

  Link getProductCategoryEditorLink(ICCategory productCategory) {
    Link link = new Link(this.iDetach);
      link.addParameter(ProductCategoryEditor.SELECTED_CATEGORY, productCategory.getID());
      link.setWindowToOpen(ProductCategoryEditor.class);
    return link;
  }


  static Product getSelectedProduct(IWContext iwc) {
    String sProductId = iwc.getParameter(ProductBusiness.PRODUCT_ID);
    if (sProductId != null) {
      try {
	Product product = ProductBusiness.getProduct(Integer.parseInt(sProductId));
	return product;
      }catch (Exception e) {
	e.printStackTrace(System.err);
      }
    }
    return null;
  }

  List getProducts(List productCategories) {
    return getProducts(productCategories, true);
  }

  List getProducts(List productCategories, boolean order) {
    List list = ProductBusiness.getProducts(productCategories);
    if (order) {
      sortList(list);
    }
    return list;
  }

  void sortList(List products) {
    sortList(products, this._orderProductsBy);
  }

  void sortList(List products, int orderBy) {
    /**
     * @todo Caching....
     */

    if (this._orderProductsBy != -1 && products != null) {
      Collections.sort(products, new ProductComparator(orderBy, this._currentLocaleId));
    }
  }

  protected String getCacheState(IWContext iwc, String cacheStatePrefix) {
    Product prod =getSelectedProduct(iwc);
    String returnString = cacheStatePrefix+getICObjectInstanceID();
    if ( iwc.isParameterSet(CATEGORY_ID) ) {
      returnString = returnString+"_"+iwc.getParameter(CATEGORY_ID);
    }
    if (prod != null) {
      returnString = returnString+"_"+prod.getID();
    }
    return returnString;
  }

  public void setAddCategoryID(boolean addID) {
    _addCategoryID = addID;
  }

  public void setIconImage(Image iconImage) {
    _iconImage = iconImage;
  }

  public void setSpaceBetween(int spaceBetween) {
    _spaceBetween = spaceBetween;
  }

  public void setIconSpacing(int iconSpacing) {
    _iconSpacing = iconSpacing;
  }

  public void setUseParameterCategory(boolean useParameterCategory) {
    _useParameterCategory = useParameterCategory;
  }
}