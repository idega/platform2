/*
 * Created on 14.7.2003
 */
package com.idega.block.trade.stockroom.presentation;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.ejb.FinderException;

import com.idega.block.category.data.ICCategory;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.ProductCategory;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;

/**
 * @author laddi
 */
public class ProductCatalogLayoutWineList extends AbstractProductCatalogLayout {

	private static final String METADATA = "metadata_";
	private ProductCategory productCategory;

	/* (non-Javadoc)
	 * @see com.idega.block.trade.stockroom.presentation.AbstractProductCatalogLayout#getCatalog(com.idega.block.trade.stockroom.presentation.ProductCatalog, com.idega.presentation.IWContext, java.util.List)
	 */
	public PresentationObject getCatalog(ProductCatalog productCatalog, IWContext iwc, List productCategories) throws RemoteException, FinderException {
		parse(iwc);

		if (productCategory != null) {
			if (productCategory.getChildCount() <= 0) 
				return getProductOverview(productCatalog, iwc, productCategory);
		}

		Table table = new Table();
		table.setWidth("100%");
		table.setCellpaddingAndCellspacing(0);

		int row = 1;

		ICCategory pCat;
		boolean showCategory = true;
		boolean hasParameterSet = false;

		Iterator iter = null;
		if (productCategory != null) {
			iter = productCategory.getChildren();
			hasParameterSet = true;
			Text categoryText = productCatalog.getCategoryText(productCategory.getName());
			if (productCatalog._headerFontStyle != null)
				categoryText.setStyleAttribute(productCatalog._headerFontStyle);
			if (productCatalog._hasEditPermission)
				table.add(productCatalog.getProductCategoryEditorLink(productCategory), 1, row);
			table.add(categoryText, 1, row++);
			if (productCatalog._spaceBetween > 0)
				table.setHeight(row++ , productCatalog._spaceBetween);
		}
		else
			iter = productCategories.iterator();
			
		while (iter.hasNext()) {
			pCat = (ICCategory) iter.next();
			if (!hasParameterSet && productCategories.contains((ICCategory)pCat.getParentNode()))
				showCategory = false;
			else
				showCategory = true;
			
			if (showCategory) {
				Link link = new Link(productCatalog.getCategoryText(pCat.getName()));
				link.addParameter(ProductCatalog.CATEGORY_ID, pCat.getPrimaryKey().toString());
				if (productCatalog._catFontStyle != null)
					link.setStyleAttribute(productCatalog._catFontStyle);
				//link.addParameter(productCatalog.prmClrCache, "true");
				if (productCatalog._hasEditPermission)
					table.add(productCatalog.getProductCategoryEditorLink(pCat), 1, row);
				table.add(link, 1, row++);
				if (iter.hasNext() && productCatalog._spaceBetweenEntries > 0)
					table.setHeight(row++ , productCatalog._spaceBetweenEntries);
			}
		}
		
		return table;
	}

	private Table getProductOverview(ProductCatalog productCatalog, IWContext iwc, ICCategory category) throws RemoteException {
		List catProducts;
		Product product;

		try {
			catProducts = getProductBusiness(iwc).getProducts(category);
		}
		catch (RemoteException e1) {
			catProducts = new ArrayList();
		}
		catch (FinderException e1) {
			catProducts = new ArrayList();
		}
		
		productCatalog.sortList(catProducts);
		Table table = new Table();
		table.setCellpaddingAndCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		int row = 1;

		Text categoryText = productCatalog.getCategoryText(productCategory.getName());
		if (productCatalog._headerFontStyle != null)
			categoryText.setStyleAttribute(productCatalog._headerFontStyle);
		if (productCatalog._hasEditPermission)
			table.add(productCatalog.getProductCategoryEditorLink(productCategory), 1, row);
		table.add(categoryText, 1, row++);
		if (productCatalog._spaceBetween > 0)
			table.setHeight(row++ , productCatalog._spaceBetween);

		Table productTable = new Table();
		productTable.setColumns(4);
		productTable.setCellspacing(1);
		productTable.setCellpadding(2);
		productTable.setWidth(Table.HUNDRED_PERCENT);
		productTable.setWidth(2, 50);
		productTable.setWidth(3, 50);
		productTable.setWidth(4, 70);
		table.add(productTable, 1, row);
		
		int pRow = 1;

		Iterator iter = catProducts.iterator();
		while (iter.hasNext()) {
			product = (Product) iter.next();
			ProductItemTeaser teaser = new ProductItemTeaser(product);
			ProductItemPrice price = new ProductItemPrice(product);
			price.setShowCurrency(true);
			price.setShowLocalized(true);
			Map metadata = product.getMetaDataAttributes();
			if (metadata == null)
				metadata = new Hashtable();
			
			if (productCatalog._topColor != null)
				productTable.setRowColor(pRow, productCatalog._topColor);
			else
				productTable.setRowColor(pRow, "#E0E0E0");
			
			if (productCatalog._hasEditPermission)
				productTable.add(productCatalog.getProductEditorLink(product), 1, pRow);
			productTable.add(productCatalog.getNamePresentationObject(product), 1, pRow);
			if (metadata.containsKey(METADATA + "amount"))
				productTable.add((String) metadata.get(METADATA + "amount") + " ml", 2, pRow);
			productTable.setNoWrap(2, pRow);
			productTable.setAlignment(2, pRow, Table.HORIZONTAL_ALIGN_CENTER);
			
			if (metadata.containsKey(METADATA + "strength"))
				productTable.add((String) metadata.get(METADATA + "strength") + "%", 3, pRow);
			productTable.setNoWrap(3, pRow);
			productTable.setAlignment(3, pRow, Table.HORIZONTAL_ALIGN_CENTER);

			productTable.add(price, 4, pRow);
			productTable.setNoWrap(4, pRow);
			productTable.setAlignment(4, pRow, Table.HORIZONTAL_ALIGN_RIGHT);
			pRow++;
			
			productTable.setRowVerticalAlignment(pRow, Table.VERTICAL_ALIGN_TOP);
			productTable.add(teaser, 1, pRow);

			if (metadata.containsKey(METADATA + "best_with")) {
				StringTokenizer tokens = new StringTokenizer(product.getMetaData(METADATA + "best_with"), ",");
				while (tokens.hasMoreTokens()) {
					String token = tokens.nextToken();
					Image image = productCatalog.iwrb.getIWBundleParent().getImage("shared/"+token+".gif");
					image.setAlt(productCatalog.iwrb.getLocalizedString(METADATA + "multi_" + token, token));
					image.setHeight(18);
					productTable.add(image, 2, pRow);
				}
				productTable.mergeCells(2, pRow, 4, pRow);
				productTable.setAlignment(2, pRow, Table.HORIZONTAL_ALIGN_RIGHT);
			}

			if (metadata.containsKey(METADATA + "awards_" + iwc.getCurrentLocale().toString())) {
				productTable.add(new Break(2), 1, pRow);
				productTable.add((String) metadata.get(METADATA + "awards_" + iwc.getCurrentLocale().toString()), 1, pRow);
			}
			pRow++;

			if (iter.hasNext() && productCatalog._spaceBetweenEntries > 0)
				productTable.setHeight(pRow++, productCatalog._spaceBetweenEntries);
		}

		return table;
	}

	private void parse(IWContext iwc) {
		try {
			if (iwc.isParameterSet(ProductCatalog.CATEGORY_ID)) {
				productCategory = getProductBusiness(iwc).getProductCategory(Integer.parseInt(iwc.getParameter(ProductCatalog.CATEGORY_ID)));
			}
		}
		catch (RemoteException e) {
			productCategory = null;
		}
	}
}