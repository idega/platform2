package com.idega.block.trade.stockroom.presentation;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

import javax.ejb.FinderException;

import com.idega.block.category.data.ICCategory;
import com.idega.block.text.business.TextFormatter;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.AnchorLink;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;

/**
 *  Title: idegaWeb ProductCatalog Description: Copyright: Copyright (c) 2002
 *  Company: idega
 *
 *@author     <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 *@created    10. mars 2002
 *@version    1.0
 */

public class ProductCatalogLayoutExpandedList extends AbstractProductCatalogLayout {

	private ProductCatalog productCatalog;
	private int imageId = -1;
	private String description;
	private String teaser;

	private IWContext iwc;

	public ProductCatalogLayoutExpandedList() {
	}

	public PresentationObject getCatalog(ProductCatalog productCatalog, IWContext iwc, List productCategories) throws RemoteException, FinderException {
		this.productCatalog = productCatalog;
		this.iwc = iwc;
		Table table = new Table();
		table.setCellpaddingAndCellspacing(0);
		table.setWidth("100%");
		int row = 1;

		Table productTable;

		Product product;

		Link editLink;
		Text nameText;
		Link nameLink;

		List products; //= productCatalog.getProducts(productCategories, true);
		ICCategory pCat;
		Link configCategory;

		for (int j = 0; j < productCategories.size(); j++) {
			pCat = (ICCategory) productCategories.get(j);
			products = getProductBusiness(iwc).getProducts((ICCategory) pCat);
			productCatalog.sortList(products);

			if (productCatalog._hasEditPermission) {
				configCategory = new Link(productCatalog.iDetach);
				configCategory.setWindowToOpen(ProductCategoryEditor.class);
				configCategory.addParameter(ProductCategoryEditor.SELECTED_CATEGORY, pCat.getID());
				table.add(configCategory, 1, row);
			}
			if (productCatalog._showCategoryName)
				table.add(productCatalog.getCategoryText(pCat.getName()), 1, row++);
			else
				row++;

			int column = 1;
			for (int i = 0; i < products.size(); i++) {
				column = 1;
				productTable = new Table();
				productTable.setCellpaddingAndCellspacing(0);
				productTable.setWidth(Table.HUNDRED_PERCENT);
				product = (Product) products.get(i);
				if (productCatalog._hasEditPermission) {
					editLink = productCatalog.getProductEditorLink(product);
					productTable.add(editLink, column++, 1);
				}
				else {
					if (productCatalog._indent > 0)
						productTable.setWidth(column++, productCatalog._indent);
				}
				
				if (productCatalog._useAnchor) {
					productTable.add(productCatalog.getAnchor(product.getID()), 2, 1);
				}

				if (productCatalog._productIsLink) {
					if (productCatalog._useAnchor) {
						nameLink = new AnchorLink(productCatalog.getText(product.getProductName(productCatalog._currentLocaleId)), productCatalog.getAnchorString(product.getID()));
						nameLink.setBold();
					}
					else {
						nameLink = new Link(productCatalog.getText(product.getProductName(productCatalog._currentLocaleId)));
						if (productCatalog._productLinkPage != null)
							nameLink.setPage(productCatalog._productLinkPage);
						nameLink.setBold();
					}
					nameLink.addParameter(getProductBusiness(iwc).getProductIdParameter(), product.getID());
					productTable.add(nameLink, column, 1);
				}
				else {
					if (productCatalog._showCategoryName) {
						nameText = productCatalog.getText(product.getProductName(productCatalog._currentLocaleId));
						nameText.setBold();
					}
					else {
						nameText = productCatalog.getCategoryText(product.getProductName(productCatalog._currentLocaleId));
					}
					productTable.add(nameText, column, 1);
				}
				row = expand(product, table, productTable, row, column, productCatalog._spaceBetweenEntries);
			}
			
			if (productCatalog._spaceBetween > 0)
				table.setHeight(row++, productCatalog._spaceBetween);
		}

		return table;
	}

	private int expand(Product product, Table table, Table productTable, int row, int column, int spaceBetween) throws RemoteException {
		if (productCatalog._showThumbnail) {
			imageId = product.getFileId();
			if (imageId != -1) {
				try {
					Table imageTable = new Table(1, 1);
					imageTable.setCellpaddingAndCellspacing(0);
					imageTable.add(new Image(imageId), 1, 1);
					productTable.setAlignment(column, 2, Table.HORIZONTAL_ALIGN_RIGHT);
					productTable.add(imageTable, column, 2);
				}
				catch (SQLException sql) {
					sql.printStackTrace(System.err);
				}
			}
		}

		if (productCatalog._showDescription) {
			description = product.getProductDescription(productCatalog._currentLocaleId);
			description = TextFormatter.formatText(description, -1, null);
			productTable.add(productCatalog.getText(description), column, 2);
		}

		table.add(productTable, 1, row++);
		if (spaceBetween > 0)
			table.setHeight(row++, spaceBetween);

		return row;
	}

}
