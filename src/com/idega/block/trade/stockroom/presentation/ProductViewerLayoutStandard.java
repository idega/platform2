/*
 * Created on 8.7.2003
 */
package com.idega.block.trade.stockroom.presentation;

import java.rmi.RemoteException;
import com.idega.block.trade.stockroom.business.ProductBusiness;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.business.IBOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.util.text.TextSoap;
import com.idega.util.text.TextStyler;

/**
 * @author laddi
 */
public class ProductViewerLayoutStandard extends AbstractProductViewerLayout {

	private String _name = "Demo";
	private String _number = "Number";
	private String _teaser = "Teaser";
	private String _description = "Desription";
	private String _style = null;
	
	private Product _product = null;
	private ProductItemPrice _price = null;
	private ProductItemMetaData _metadata = null;

	/* (non-Javadoc)
	 * @see com.idega.block.trade.stockroom.presentation.AbstractProductViewerLayout#getDemo(com.idega.block.trade.stockroom.presentation.ProductViewer, com.idega.presentation.IWContext)
	 */
	public PresentationObject getDemo(ProductViewer productViewer, IWContext iwc) throws RemoteException {
		return null;
	}

	public PresentationObject getViewer(ProductViewer productViewer, Product product, IWContext iwc) throws RemoteException {
		_name = product.getProductName(productViewer._localeId);
		_description = TextSoap.formatText(product.getProductDescription(productViewer._localeId));
		_teaser = TextSoap.formatText(product.getProductTeaser(productViewer._localeId));
		
		_product = product;
		_price = new ProductItemPrice(product);
		_metadata = new ProductItemMetaData(product);
		if (productViewer._fontStyle != null) {
			TextStyler styler = new TextStyler(productViewer._fontStyle);
			_metadata.setFontStyle(styler.getStyleString());
			
			styler.setStyleValue("font-weight", "bold");
			_style = styler.getStyleString();
			_metadata.setHeaderFontStyle(_style);
		}

		if (productViewer._priceFontStyle != null) {
			_price.setFontStyle(productViewer._priceFontStyle);
		}
		_price.setShowCurrency(productViewer._showCurrency);
		_price.setShowLocalized(true);

		return printViewer(productViewer, iwc);
	}

	public PresentationObject printViewer(ProductViewer productViewer, IWContext iwc) throws RemoteException {
		Table table = new Table(3, 3);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.mergeCells(1, 1, 3, 1);
		table.setWidth(2, 12);
		if (productViewer._imageWidth != null)
			table.setWidth(1, 3, Integer.parseInt(productViewer._imageWidth));
		table.setRowVerticalAlignment(3, Table.VERTICAL_ALIGN_TOP);

		Table contentTable = new Table();
		contentTable.setColumns(1);
		contentTable.setWidth(Table.HUNDRED_PERCENT);
		contentTable.setCellpaddingAndCellspacing(0);
		int contentRow = 1;
		
		Text header = productViewer.getHeaderText(this._name);
		Text description = productViewer.getText(this._teaser);
		Text teaser = productViewer.getText(this._description);

		int row = 1;

		table.add(header, 1, row++);
		table.setHeight(row++, "12");

		ProductItemThumbnail thumb = new ProductItemThumbnail(_product);
		if (productViewer._imageWidth != null) {
			try {
				thumb.setWidth(Integer.parseInt(productViewer._imageWidth));
			}
			catch (NumberFormatException e) {
			}
		}
		table.add(thumb, 1, row);
		
		table.add(contentTable, 3, row);
		
		contentTable.add(description, 1, contentRow++);

		if (productViewer._showMetaData && _product != null) {
			if (productViewer._spaceBetween > 0)
				contentTable.setHeight(1, contentRow++, String.valueOf(productViewer._spaceBetween));
			contentTable.add(_metadata, 1, contentRow++);
		}

		if (productViewer._spaceBetween > 0)
			contentTable.setHeight(1, contentRow++, String.valueOf(productViewer._spaceBetween));
		contentTable.add(_price, 1, contentRow++);
		
		if (productViewer._spaceBetween > 0)
			contentTable.setHeight(1, contentRow++, String.valueOf(productViewer._spaceBetween));
		contentTable.add(teaser, 1, contentRow++);
		
		return table;
	}

	private ProductBusiness getProductBusiness(IWContext iwc) throws RemoteException {
		return (ProductBusiness) IBOLookup.getServiceInstance(iwc, ProductBusiness.class);
	}
}
