package com.idega.block.trade.stockroom.presentation;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.block.trade.stockroom.business.ProductBusiness;
import com.idega.block.trade.stockroom.business.ProductComparator;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.ProductCategory;
import com.idega.core.builder.data.ICPage;
import com.idega.business.IBOLookup;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Paragraph;
import com.idega.presentation.text.Text;

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
	String _priceFontStyle;
	String _linkFontStyle;
	Image _seperator = null;
	boolean _useHRasSeperator = false;
	boolean _showRandom = false;
	boolean _showNewest = false;
	String _imageWidth = null;
	String _textAlignment = Paragraph.HORIZONTAL_ALIGN_LEFT;
	String _imageAlignment = Paragraph.HORIZONTAL_ALIGN_RIGHT;
	ICPage _productPage;
	Image _productImage;
	boolean _showProductLink = false;
	boolean _showTeaser = false;
	boolean _showThumbnail = false;
	boolean _showPrice = false;
	boolean _showCurrency = false;
	int _spaceBetween = 3;
	boolean _showImages = true;
	boolean _addCategoryID = false;

	boolean _showMetaData = true;

	boolean _showFromParameter = true;
	boolean _showBorder = true;

	/**
	 * @param showFromParameter
	 */
	public void setShowFromParameter(boolean showFromParameter) {
		_showFromParameter = showFromParameter;
	}

	public ProductViewer() {
	}

	public void main(IWContext iwc) throws Exception {

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

	private void init(IWContext iwc) throws RemoteException {
		bundle = getBundle(iwc);
		iwrb = bundle.getResourceBundle(iwc);

		this._locale = iwc.getCurrentLocale();
		this._localeId = ICLocaleBusiness.getLocaleId(_locale);

		if (_showFromParameter) {
			try {
				String sProductId = iwc.getParameter(getProductBusiness(iwc).getProductIdParameter());
				if (sProductId != null) {
					_productId = Integer.parseInt(sProductId);
					_product = getProductBusiness(iwc).getProduct(_productId);
					if (!_product.getIsValid()) {
						_product = null;
					}
				}
	
				if (_product != null) {
					List list = getProductBusiness(iwc).getProductCategories(_product);
					if (list != null && categoryList != null) {
						boolean showProduct = false;
						Iterator iter = list.iterator();
						while (iter.hasNext()) {
							if (categoryList.contains(iter.next()))
								showProduct = true;
						}
						if (!showProduct)
							_product = null;
					}
				}
			}
			catch (Exception e) {
				e.printStackTrace(System.err);
			}
		}

		if (categoryList == null) {
			categoryList = getCategoriesFromParameter(iwc);
		}

		if (_product == null) {
			try {
				if (_showRandom) {
					if (categoryList != null) {
						List products = getProductBusiness(iwc).getProducts(categoryList);
						if (products != null && products.size() > 0) {
							int random = (int) Math.round(Math.random() * (products.size() - 1));
							this._product = (Product) products.get(random);
							this._productId = ((Integer) _product.getPrimaryKey()).intValue();
						}
					}
				}
				if (_showNewest) {
					if (categoryList != null) {
						List products = getProductBusiness(iwc).getProducts(categoryList);
						if (products != null && products.size() > 0) {
							Collections.sort(products, new ProductComparator(ProductComparator.CREATION_DATE, iwc.getCurrentLocale()));
							this._product = (Product) products.get(0);
							this._productId = ((Integer) _product.getPrimaryKey()).intValue();
						}
					}
				}
			}
			catch (FinderException fe) {
				fe.printStackTrace(System.err);
			}
		}

		IWBundle coreBundle = iwc.getIWMainApplication().getCoreBundle();
		iEdit = coreBundle.getImage("shared/edit.gif");
	}

	private void getViewer(IWContext iwc) throws RemoteException {
		try {
			AbstractProductViewerLayout layout = (AbstractProductViewerLayout) this._viewerLayoutClass.newInstance();
			PresentationObject po = null;

			if (this._product == null) {
				po = layout.getDemo(this, iwc);
			}
			else {
				po = layout.getViewer(this, _product, iwc);
			}

			Table table = new Table(1, 1);
			table.setCellpadding(0);
			table.setCellspacing(0);

			if (_width != null)
				table.setWidth(_width);
			table.add(po);

			add(table);
		}
		catch (IllegalAccessException iae) {
			iae.printStackTrace(System.err);
		}
		catch (InstantiationException ie) {
			ie.printStackTrace(System.err);
		}
	}

	Text getText(String content) {
		return getText(content, true);
	}

	Text getText(String content, boolean useAlignment) {
		Text text = new Text(content);
		if (this._fontStyle != null)
			text.setFontStyle(_fontStyle);
		if (useAlignment)
			text.setHorizontalAlignment(_textAlignment);
		return text;
	}

	Text getHeaderText(String content) {
		Text text = new Text(content);
		if (this._headerFontStyle != null)
			text.setFontStyle(_headerFontStyle);
		return text;
	}

	Text getPriceText(String content) {
		Text text = new Text(content);
		if (this._priceFontStyle != null)
			text.setFontStyle(_priceFontStyle);
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
		}
		catch (ClassNotFoundException cnf) {
			cnf.printStackTrace(System.err);
		}
	}

	public void setSeperatorImage(Image seperator) {
		this._seperator = seperator;
	}

	public void setUseHorizontalRuleAsSeperator(boolean use) {
		this._useHRasSeperator = use;
	}

	public void setCategory(String name, String categoryID) throws RemoteException {
		if (categoryList == null)
			categoryList = new Vector();
		/** @todo FIXA getInstanceCRAP ..... */
		ProductCategory category = getProductBusiness(IWContext.getInstance()).getProductCategory(Integer.parseInt(categoryID));
		if (category != null)
			categoryList.add(category);
	}

	public void setShowRandomProduct(boolean showRandom) {
		_showRandom = showRandom;
		_showNewest = !showRandom;
	}

	public void setProductPage(ICPage page) {
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

	public void setShowMetaData(boolean showMetaData) {
		_showMetaData = showMetaData;
	}

	public void setSpaceBetweenTitleAndBody(String spaceBetween) {
		_spaceBetween = Integer.parseInt(spaceBetween);
	}

	public void setShowNewestProduct(boolean showNewest) {
		_showNewest = showNewest;
		_showRandom = !showNewest;
	}

	public void setShowImages(boolean showImages) {
		_showImages = showImages;
	}

	public void setAddCategoryID(boolean addID) {
		_addCategoryID = addID;
	}

	public void setShowPrice(boolean showPrice) {
		_showPrice = showPrice;
	}

	public void setShowCurrency(boolean showCurrency) {
		_showCurrency = showCurrency;
	}

	public void setPriceFontStyle(String style) {
		_priceFontStyle = style;
	}

	public void setLinkFontStyle(String style) {
		_linkFontStyle = style;
	}

	private List getCategoriesFromParameter(IWContext iwc) throws RemoteException {
		Vector vector = new Vector();
		String[] categories = iwc.getParameterValues(ProductCatalog.CATEGORY_ID);
		if (categories != null) {
			for (int a = 0; a < categories.length; a++) {
				ProductCategory category = getProductBusiness(iwc).getProductCategory(Integer.parseInt(categories[a]));
				if (category != null)
					vector.add(category);
			}
		}
		return vector;
	}

	private ProductBusiness getProductBusiness(IWContext iwc) throws RemoteException {
		return (ProductBusiness) IBOLookup.getServiceInstance(iwc, ProductBusiness.class);
	}
	/**
	 * @param showBorder
	 */
	public void setShowBorder(boolean showBorder) {
		_showBorder = showBorder;
	}

}
