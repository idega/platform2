package com.idega.block.trade.stockroom.presentation;

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

 public ProductCatalogLayoutExpandedList() { }


 public PresentationObject getCatalog( ProductCatalog productCatalog, IWContext iwc, List productCategories ) {
  this.productCatalog = productCatalog;
  Table table = new Table();
  table.setWidth("100%");
  int row = 1;

  Product selectedProduct = productCatalog.getSelectedProduct(iwc);
  Product product;

  boolean addedRow = false;
  Link editLink;
  Text nameText;

  List products = productCatalog.getProducts(productCategories, true);

  for (int i = 0; i < products.size(); i++) {
    product = (Product) products.get(i);
    ++row;
    if (productCatalog._hasEditPermission) {
      editLink = ProductEditorWindow.getEditorLink(product.getID());
      editLink.setImage(productCatalog.iEdit);
      table.add(editLink, 1, row);
    }
    if (productCatalog._useAnchor) {
      table.add(productCatalog.getAnchor(product.getID()), 1,row);
    }

    table.add(productCatalog.getNamePresentationObject(product, true), 1,row);
    table.mergeCells(1, row, 3, row);

    if (productCatalog._expandSelectedOnly) {
      if (selectedProduct != null && (selectedProduct.getID() == product.getID())) {
        row = expand(product, table ,row);
      }
    }else {
      row = expand(product, table, row);
    }

  }



  return table;
 }

  private int expand(Product product, Table table, int row) {
    boolean addedRow = false;
    if (productCatalog._showNumber) {
    }
    if (productCatalog._showTeaser) {
    }
    if (productCatalog._showPrice) {
    }

    if (productCatalog._showDescription) {
      if (!addedRow){
        ++row;
        addedRow = true;
      }
      description = ProductBusiness.getProductDescription(product, productCatalog._currentLocaleId);
      description = TextFormatter.formatText(description, -1, null);
      table.add(productCatalog.getText(description), 2, row);
      table.setVerticalAlignment(2, row, Table.VERTICAL_ALIGN_TOP);
    }
    if (productCatalog._showImage) {
      if (!addedRow){
        ++row;
        addedRow = true;
      }
      imageId = product.getFileId();
      if (imageId != -1) {
        try {
          table.add(new Image(imageId), 3, row);
        }catch (SQLException sql) {
          sql.printStackTrace(System.err);
        }
      }
    }
    return row;
  }
}
