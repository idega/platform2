package com.idega.block.trade.stockroom.presentation;

import com.idega.core.data.ICCategory;
import com.idega.presentation.text.*;
import com.idega.block.text.business.TextFormatter;
import java.sql.SQLException;
import com.idega.util.idegaTimestamp;
import com.idega.block.trade.stockroom.data.Product;
import java.util.Collections;
import com.idega.block.trade.stockroom.business.*;
import java.util.List;
import com.idega.presentation.*;

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

 public ProductCatalogLayoutExpandedList() { }


 public PresentationObject getCatalog( ProductCatalog productCatalog, IWContext iwc, List productCategories ) {
  this.productCatalog = productCatalog;
  this.iwc = iwc;
  Table table = new Table();
  table.setCellpaddingAndCellspacing(0);
  table.setWidth("100%");
  int row = 0;

  Table productTable;

  Product selectedProduct = productCatalog.getSelectedProduct(iwc);
  Product product;

  boolean addedRow = false;
  Link editLink;
  Text nameText;
  Link nameLink;

  List products; //= productCatalog.getProducts(productCategories, true);
  ICCategory pCat;
  Link configCategory;

  for (int j = 0; j < productCategories.size(); j++) {
    pCat = (ICCategory) productCategories.get(j);
    products = ProductBusiness.getProducts((ICCategory) pCat);
    productCatalog.sortList(products);
    ++row;

    if (productCatalog._hasEditPermission) {
      configCategory = new Link(productCatalog.iDetach);
	configCategory.setWindowToOpen(ProductCategoryEditor.class);
	configCategory.addParameter(ProductCategoryEditor.SELECTED_CATEGORY, pCat.getID());
      table.add(configCategory, 1,row);
    }
    table.add(productCatalog.getCategoryText(pCat.getName()), 1,row);

    for (int i = 0; i < products.size(); i++) {
      productTable = new Table();
      productTable.setCellpaddingAndCellspacing(0);
      product = (Product) products.get(i);
      ++row;
      if (productCatalog._hasEditPermission) {
	editLink = productCatalog.getProductEditorLink(product);
	productTable.add(editLink, 1, 1);
      }
      if (productCatalog._useAnchor) {
	productTable.add(productCatalog.getAnchor(product.getID()), 1,1);
      }

      if ( productCatalog._productIsLink ) {
	if (productCatalog._useAnchor) {
	  nameLink = new AnchorLink(productCatalog.getText(ProductBusiness.getProductName(product, productCatalog._currentLocaleId)), productCatalog.getAnchorString(product.getID()));
	}else {
	  nameLink = new Link(productCatalog.getText(ProductBusiness.getProductName(product, productCatalog._currentLocaleId)));
	}
	nameLink.addParameter(ProductBusiness.PRODUCT_ID, product.getID());
	productTable.add(nameLink, 1,1);
      }
      else {
	nameText = productCatalog.getText(ProductBusiness.getProductName(product, productCatalog._currentLocaleId));
	productTable.add(nameText, 1,1);
      }
      row = expand(product, table, productTable, row);
    }
  }




  return table;
 }

  private int expand(Product product, Table table, Table productTable, int row) {
    if (productCatalog._showThumbnail) {
      imageId = product.getFileId();
      if (imageId != -1) {
	try {
	  Table imageTable = new Table(1,1);
	  imageTable.setCellpaddingAndCellspacing(0);
	  imageTable.setAlignment(Table.HORIZONTAL_ALIGN_RIGHT);
	  imageTable.add(new Image(imageId), 1, 1);
	  productTable.add(imageTable,1,2);
	}
	catch (SQLException sql) {
	  sql.printStackTrace(System.err);
	}
      }
    }

    if (productCatalog._showDescription) {
      description = ProductBusiness.getProductDescription(product, productCatalog._currentLocaleId);
      description = TextFormatter.formatText(description, -1, null);
      productTable.add(productCatalog.getText(description), 1, 2);
    }

    return row;
  }
}
