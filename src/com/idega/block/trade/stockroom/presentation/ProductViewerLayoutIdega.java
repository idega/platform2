package com.idega.block.trade.stockroom.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.idega.block.text.business.TextFormatter;
import com.idega.block.trade.stockroom.business.ProductBusiness;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.ProductCategory;
import com.idega.business.IBOLookup;
import com.idega.core.file.data.ICFile;
import com.idega.data.IDORelationshipException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.HorizontalRule;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;

/**
 *  Title: idegaWeb TravelBooking Description: Copyright: Copyright (c) 2001
 *  Company: idega
 *
 *@author     <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 *@created    9. mars 2002
 *@version    1.0
 */

public class ProductViewerLayoutIdega extends AbstractProductViewerLayout {

	private String _name = "Demo";
	private String _number = "Number";
	private String _teaser = "Teaser";
	private String _description = "Desription";
	private ProductItemPrice _price = null;
	private List _images = new Vector();
	private Product _product = null;
	private ProductItemMetaData _metadata = null;

	public ProductViewerLayoutIdega() {
	}

	/**
	 * @todo Handle multible images...
	 */
	public PresentationObject getDemo(ProductViewer productViewer, IWContext iwc) throws RemoteException {
		String IMAGE_BUNDLE_IDENTIFIER = "com.idega.block.image";
		Image image = iwc.getIWMainApplication().getBundle(IMAGE_BUNDLE_IDENTIFIER).getLocalizedImage("picture.gif", productViewer._locale);
		_images.add(image);

		_description = TextFormatter.getLoremIpsumString(iwc);

		return printViewer(productViewer, iwc);
	}

	public PresentationObject getViewer(ProductViewer productViewer, Product product, IWContext iwc) throws RemoteException {
		_name = product.getProductName(productViewer._localeId);
		_description = product.getProductDescription(productViewer._localeId);
		_description = TextFormatter.formatText(_description, 1, Table.HUNDRED_PERCENT);
		_teaser = product.getProductTeaser(productViewer._localeId);
		_teaser = TextFormatter.formatText(_teaser, 1, Table.HUNDRED_PERCENT);
		_product = product;
		_price = new ProductItemPrice(product);
		_metadata = new ProductItemMetaData(product);
		if (productViewer._priceFontStyle != null) {
			_price.setFontStyle(productViewer._priceFontStyle);
		}
		_price.setShowCurrency(productViewer._showCurrency);

		try {
			Collection coll = product.getICFile();
			if (coll != null) {
				Iterator iter = coll.iterator();
				while (iter.hasNext()) {
					ICFile item = (ICFile) iter.next();
					_images.add(item);
				}
			}
			//      _images = EntityFinder.getInstance().findRelated(product, ICFile.class);
		}
		catch (IDORelationshipException ido) {
			ido.printStackTrace(System.err);
		}

		return printViewer(productViewer, iwc);
	}

	public PresentationObject printViewer(ProductViewer productViewer, IWContext iwc) throws RemoteException {
		Table table = new Table();
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setCellpadding(0);
		table.setCellspacing(0);

		Text header = productViewer.getHeaderText(this._name);
		Text description = null;
		if (productViewer._showTeaser)
			description = productViewer.getText(this._teaser);
		else
			description = productViewer.getText(this._description);

		int row = 1;

		table.add(header, 1, row);
		if (productViewer._showPrice && _price != null) {
			table.add(productViewer.getText(" - ", false), 1, row);
			table.add(_price, 1, row++);
		}
		else {
			row++;
		}

		table.setHeight(row++, "6");

		if (productViewer._useHRasSeperator) {
			HorizontalRule hr = new HorizontalRule("100%");
			table.add(hr, 1, row++);
		}
		else {
			if (productViewer._seperator != null) {
				table.add(productViewer._seperator, 1, row++);
			}
			else {
				table.setHeight(1, row++, String.valueOf(productViewer._spaceBetween));
			}
		}

		if (_product != null && productViewer._showImages) {
			Table imageTable = new Table(1, 1);
			imageTable.setCellpadding(0);
			imageTable.setCellspacing(0);
			if (productViewer._showThumbnail) {
				ProductItemThumbnail thumb = new ProductItemThumbnail(_product);
				if (productViewer._imageWidth != null) {
					try {
						thumb.setWidth(Integer.parseInt(productViewer._imageWidth));
					}
					catch (NumberFormatException e) {
					}
				}
				imageTable.add(thumb);
				table.setAlignment(1, row, productViewer._imageAlignment);
				table.add(imageTable, 1, row);
			}
			else {
				ProductItemImages pii = new ProductItemImages(_product);
				pii.setVerticalView(true);
				pii.setImageAlignment(Table.HORIZONTAL_ALIGN_CENTER);
				if (productViewer._imageWidth != null) {
					try {
						pii.setWidth(Integer.parseInt(productViewer._imageWidth));
					}
					catch (NumberFormatException e) {
						pii.setWidth(0);
					}
				}
				imageTable.add(pii);
				table.setAlignment(1, row, productViewer._imageAlignment);
				table.add(imageTable, 1, row);
			}
		}
		table.add(description, 1, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_LEFT);
		
		if (productViewer._showMetaData && _product != null) {
			table.setHeight(1, ++row, String.valueOf(productViewer._spaceBetween));
			table.add(_metadata, 1, ++row);
		}

		if (productViewer._showProductLink && _product != null) {
			table.setHeight(1, ++row, String.valueOf(productViewer._spaceBetween));

			Link link = new Link();
			if (productViewer._productImage != null)
				link.setPresentationObject(productViewer._productImage);
			else
				link.setText(this._name);

			if (productViewer._productPage != null)
				link.setPage(productViewer._productPage);
			link.addParameter(getProductBusiness(iwc).getProductIdParameter(), _product.getID());

			if (productViewer._addCategoryID) {
				try {
					List list = getProductBusiness(iwc).getProductCategories(_product);
					if (list != null) {
						Iterator iter = list.iterator();
						while (iter.hasNext()) {
							link.addParameter(ProductCatalog.CATEGORY_ID, ((ProductCategory) iter.next()).getID());
						}
					}
				}
				catch (IDORelationshipException e) {
				}
			}

			table.add(link, 1, ++row);
		}

		return table;
	}

	private ProductBusiness getProductBusiness(IWContext iwc) throws RemoteException {
		return (ProductBusiness) IBOLookup.getServiceInstance(iwc, ProductBusiness.class);
	}
}