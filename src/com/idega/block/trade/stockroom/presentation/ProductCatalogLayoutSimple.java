package com.idega.block.trade.stockroom.presentation;

import java.rmi.RemoteException;
import java.util.List;

import com.idega.block.category.data.ICCategory;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class ProductCatalogLayoutSimple extends AbstractProductCatalogLayout {

  public ProductCatalogLayoutSimple() {
  }

  public PresentationObject getCatalog(ProductCatalog productCatalog, IWContext iwc, List productCategories) throws RemoteException{
    Table table = new Table();
      table.setWidth("100%");
      table.setCellpaddingAndCellspacing(0);
    Image spacer = Table.getTransparentCell(iwc);
      spacer.setWidth(5);

    int row = 1;


    ICCategory pCat;
    Product product;

    List catProducts;

    for (int i = 0; i < productCategories.size(); i++) {
      try {
	pCat = (ICCategory) productCategories.get(i);
	if (productCatalog._showCategoryName) {
	  table.add(productCatalog.getCategoryText(pCat.getName()),1,row++);
	}

	catProducts = getProductBusiness(iwc).getProducts(pCat);
	productCatalog.sortList(catProducts);
	Table productTable = new Table();
	  productTable.setCellpaddingAndCellspacing(0);
	table.add(productTable,1,row++);
	table.setHeight(row++,"9");
	int pRow = 1;
	int pColumn = 1;

    Image spaceBetween = (Image) spacer.clone();
	spaceBetween.setHeight(productCatalog._spaceBetween);


	for (int j = 0; j < catProducts.size(); j++) {
	  try {
	    if ( productCatalog._spaceBetween > 0 ) {
	      productTable.add(spaceBetween,1,pRow++);
	    }

	    product = (Product) catProducts.get(j);

	    if ( productCatalog._iconImage != null ) {
	      Image iconImage = (Image) productCatalog._iconImage.clone();
	      iconImage.setVerticalSpacing(productCatalog._iconSpacing);
	      productTable.add(iconImage,pColumn++,pRow);
	      productTable.add(spacer,pColumn++,pRow);
	    }
	    productTable.add(productCatalog.getNamePresentationObject(product), pColumn++, pRow);

	    if (productCatalog._hasEditPermission) {
	      productTable.add(productCatalog.getProductEditorLink(product), pColumn++, pRow);
	    }
	    productTable.setRowVerticalAlignment(pRow++,Table.VERTICAL_ALIGN_TOP);

	    pColumn = 1;
	  }
	  catch (Exception e) {
	    e.printStackTrace(System.err);
	  }
	}
      }
      catch (Exception e) {
	e.printStackTrace(System.err);
      }
    }


//    add(getPagesTable(totalPages));
    //add(form);

    return table;
  }

}
