package com.idega.block.trade.stockroom.presentation;

import java.rmi.RemoteException;
import java.util.List;

import com.idega.block.category.data.ICCategory;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;

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

  public PresentationObject getCatalog(ProductCatalog productCatalog, IWContext iwc, List productCategories) throws RemoteException {
    Table table = new Table();
      table.setWidth("100%");
    int row = 1;
    int firstRow = 0;
    int column = 1;

    ICCategory pCat;
    Product product;

    List catProducts;
    Link configCategory;

    for (int i = 0; i < productCategories.size(); i++) {
      try {
	pCat = (ICCategory) productCategories.get(i);
	if (productCatalog._showCategoryName) {
	  table.add(productCatalog.getCategoryText(pCat.getName()), 1,row);
	}

	if (productCatalog._hasEditPermission) {
	  configCategory = productCatalog.getProductCategoryEditorLink(pCat);
	  table.add(Text.getNonBrakingSpace(),1,row);
	  table.add(configCategory, 1,row);
	}

	if (productCatalog._showCategoryName || productCatalog._hasEditPermission) {
	  firstRow = 1;
	  ++row;
	}

	catProducts = getProductBusiness(iwc).getProducts(pCat);
	productCatalog.sortList(catProducts);

	int numberOfRows = ( catProducts.size() / productCatalog._numberOfColumns ) + firstRow;
	if ( (catProducts.size() % productCatalog._numberOfColumns) > 0 )
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
	column = 1;

	int percent = 100 / productCatalog._numberOfColumns;
	for ( int a = 1; a <= productCatalog._numberOfColumns; a++ ) {
	  table.setWidth(a,String.valueOf(percent)+"%");
	}
      }
      catch (Exception e) {
	e.printStackTrace(System.err);
      }
    }

    return table;
  }

}
