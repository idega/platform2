package com.idega.block.trade.stockroom.presentation;

import java.rmi.RemoteException;
import com.idega.block.trade.stockroom.business.ProductBusiness;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.business.IBOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.util.text.TextSoap;

/**
 *  Title: idegaWeb TravelBooking Description: Copyright: Copyright (c) 2001
 *  Company: idega
 *
 *@author     <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 *@created    9. mars 2002
 *@version    1.0
 */

public class ProductViewerLayoutTeaser extends AbstractProductViewerLayout {

	private String _name = "Demo";
	private String _number = "Number";
	private String _teaser = "Teaser";
	private String _style = null;
	
	private Product _product = null;
	private ProductItemPrice _price = null;
	private ProductItemThumbnail _thumb = null;

	/* (non-Javadoc)
	 * @see com.idega.block.trade.stockroom.presentation.AbstractProductViewerLayout#getDemo(com.idega.block.trade.stockroom.presentation.ProductViewer, com.idega.presentation.IWContext)
	 */
	public PresentationObject getDemo(ProductViewer productViewer, IWContext iwc) throws RemoteException {
		return null;
	}

	public PresentationObject getViewer(ProductViewer productViewer, Product product, IWContext iwc) throws RemoteException {
		_name = product.getProductName(productViewer._localeId);
		_teaser = TextSoap.formatText(product.getProductTeaser(productViewer._localeId));
		
		_product = product;
		_price = new ProductItemPrice(product);
		if (productViewer._priceFontStyle != null) {
			_price.setFontStyle(productViewer._priceFontStyle);
		}
		_price.setShowCurrency(productViewer._showCurrency);
		_price.setShowLocalized(true);

		_thumb = new ProductItemThumbnail(_product);
		_thumb.setAddBorder(productViewer._showBorder);
		if (productViewer._imageWidth != null) {
			try {
				_thumb.setWidth(Integer.parseInt(productViewer._imageWidth));
			}
			catch (NumberFormatException e) {
			}
		}

		return printViewer(productViewer, iwc);
	}

	public PresentationObject printViewer(ProductViewer productViewer, IWContext iwc) throws RemoteException {
		Table table = new Table(3, 6);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.mergeCells(1, 1, 3, 1);
		table.mergeCells(3, 3, 3, 4);
		table.mergeCells(1, 6, 3, 6);
		table.setWidth(2, 4);
		if (productViewer._imageWidth != null)
			table.setWidth(3, 3, Integer.parseInt(productViewer._imageWidth));
		table.setRowVerticalAlignment(3, Table.VERTICAL_ALIGN_TOP);
		table.setRowVerticalAlignment(4, Table.VERTICAL_ALIGN_BOTTOM);

		Text header = productViewer.getHeaderText(this._name);
		Text teaser = productViewer.getText(this._teaser);

		int row = 1;

		table.add(header, 1, row++);
		table.setHeight(row++, "6");

		table.add(_thumb, 3, row);
		table.add(teaser, 1, row++);
		table.add(_price, 1, row++);
		
		Link link = new Link(productViewer.getLocalizedString("more", ">> more", iwc));
		if (productViewer._productPage != null)
			link.setPage(productViewer._productPage);
		link.addParameter(getProductBusiness(iwc).getProductIdParameter(), _product.getID());
		if (productViewer._linkFontStyle != null)
			link.setStyleAttribute(productViewer._linkFontStyle);

		table.setHeight(row++, "6");
		table.add(link, 1, row++);
		
		return table;
	}

	private ProductBusiness getProductBusiness(IWContext iwc) throws RemoteException {
		return (ProductBusiness) IBOLookup.getServiceInstance(iwc, ProductBusiness.class);
	}
}