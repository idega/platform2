package com.idega.block.trade.stockroom.presentation;

import com.idega.core.data.*;
import com.idega.block.trade.stockroom.data.*;
import com.idega.block.trade.stockroom.business.*;
import java.util.*;
import com.idega.presentation.text.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.*;

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

    List catProducts;
    Link configCategory;

    for (int i = 0; i < productCategories.size(); i++) {
      try {
	pCat = (ICCategory) productCategories.get(i);

	if ( productCatalog._iconImage != null ) {
	  Image iconImage = (Image) productCatalog._iconImage.clone();
	  iconImage.setVerticalSpacing(productCatalog._iconSpacing);
	  table.add(iconImage,column++,row);
	  table.add(spacer,column++,row);
	}
	table.add(productCatalog.getCategoryLink(pCat,pCat.getName()), column++,row);

	if (productCatalog._hasEditPermission) {
	  configCategory = productCatalog.getProductCategoryEditorLink(pCat);
	  table.add(configCategory, column,row);
	}

	table.setRowVerticalAlignment(row++,Table.VERTICAL_ALIGN_TOP);
	column = 1;

	if ( productCatalog._spaceBetween > 0 ) {
	  table.setHeight(row++,productCatalog._spaceBetween);
	}

      }
      catch (Exception e) {
	e.printStackTrace(System.err);
      }
    }

    return table;
  }

}
