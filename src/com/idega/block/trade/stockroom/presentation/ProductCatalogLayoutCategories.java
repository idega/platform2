package com.idega.block.trade.stockroom.presentation;
import java.util.List;
import com.idega.block.category.data.ICCategory;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */
public class ProductCatalogLayoutCategories extends AbstractProductCatalogLayout {
	public ProductCatalogLayoutCategories() {
	}
	public PresentationObject getCatalog(ProductCatalog productCatalog, IWContext iwc, List productCategories) {
		Table table = new Table();
		table.setWidth("100%");
		table.setCellpaddingAndCellspacing(0);
		Image spacer = Table.getTransparentCell(iwc);
		spacer.setWidth(5);
		int row = 1;
		int column = 1;
		ICCategory pCat;
		Link configCategory;
		Image spaceBetween = (Image) spacer.clone();
		spaceBetween.setHeight(productCatalog._spaceBetween);
		for (int i = 0; i < productCategories.size(); i++) {
			try {
				if (productCatalog._spaceBetween > 0) {
					table.add(spaceBetween, 1, row++);
				}
				pCat = (ICCategory) productCategories.get(i);
				if (productCatalog._iconImage != null) {
					Image iconImage = (Image) productCatalog._iconImage.clone();
					iconImage.setVerticalSpacing(productCatalog._iconSpacing);
					table.add(iconImage, column++, row);
					table.add(spacer, column++, row);
				}
				table.add(productCatalog.getCategoryLink(pCat, pCat.getName()), column++, row);
				if (productCatalog._hasEditPermission) {
					configCategory = productCatalog.getProductCategoryEditorLink(pCat);
					table.add(configCategory, column, row);
				}
				table.setRowVerticalAlignment(row++, Table.VERTICAL_ALIGN_TOP);
				column = 1;
			}
			catch (Exception e) {
				e.printStackTrace(System.err);
			}
		}
		return table;
	}
}
