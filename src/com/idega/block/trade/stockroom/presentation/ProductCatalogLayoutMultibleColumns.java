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

public class ProductCatalogLayoutMultibleColumns extends AbstractProductCatalogLayout {

  public ProductCatalogLayoutMultibleColumns() {
  }

  public PresentationObject getCatalog(ProductCatalog productCatalog, IWContext iwc, List productCategories) {
    Table table = new Table();
      table.setWidth("100%");
    int row = 1;
    int firstRow = 0;
    int column = 1;

    ICCategory pCat;
    Product product;

    List catProducts;
    Link configCategory;
    Link productLink;
    Text nameText;

    for (int i = 0; i < productCategories.size(); i++) {
      try {
	pCat = (ICCategory) productCategories.get(i);
	if (productCatalog._showCategoryName) {
	  table.add(productCatalog.getCategoryText(pCat.getName()), 1,row);
	  firstRow = 1;
	}

	if (productCatalog._hasEditPermission) {
	  configCategory = productCatalog.getProductCategoryEditorLink(pCat);
	  table.add(Text.getNonBrakingSpace(),1,row);
	  table.add(configCategory, 1,row);
	  firstRow = 1;
	}

	if (productCatalog._showCategoryName || productCatalog._hasEditPermission) {
	  firstRow = 0;
	  ++row;
	}

	catProducts = ProductBusiness.getProducts(pCat);
	productCatalog.sortList(catProducts);

	int numberOfRows = catProducts.size() / productCatalog._numberOfColumns;
	if ( catProducts.size() % productCatalog._numberOfColumns > 0 )
	  numberOfRows++;

	for (int j = 0; j < catProducts.size(); j++) {
	  try {
	    product = (Product) catProducts.get(j);

	    if (productCatalog._hasEditPermission) {
	      table.add(productCatalog.getProductEditorLink(product), column, row);
	      table.add(Text.NON_BREAKING_SPACE);
	    }
	    table.add(productCatalog.getNamePresentationObject(product), column, row);

	  }
	  catch (Exception e) {
	    e.printStackTrace(System.err);
	  }
	  if ( row == numberOfRows ) {
	    row = firstRow;
	    column++;
	  }

	  ++row;
	}


      }catch (Exception e) {
	e.printStackTrace(System.err);
      }
    }

    return table;
  }

}