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


 public ProductCatalogLayoutExpandedList() { }


 public PresentationObject getCatalog( ProductCatalog productCatalog, IWContext iwc, List productCategories ) {
  Table table = new Table();
  table.setWidth("100%");
  int row = 1;

  Product product;
  boolean expandSelectedOnly = productCatalog._expandSelectedOnly;
  boolean useAnchor = productCatalog._useAnchor;

  boolean addedRow = false;
  int imageId = -1;
  String description;
  Link editLink;

  List products = ProductBusiness.getProducts(productCategories);
  debug("BEFORE SORT "+idegaTimestamp.RightNow().toString());
  if (productCatalog._orderProductsBy != -1) {
    Collections.sort(products, new ProductComparator(productCatalog._orderProductsBy, productCatalog._currentLocaleId));
  }
  debug("AFTER SORT "+idegaTimestamp.RightNow().toString());


  for (int i = 0; i < products.size(); i++) {
    addedRow = false;
    product = (Product) products.get(i);
    ++row;/*
    if (productCatalog._hasEditPermission) {
      editLink = ProductEditorWindow.getEditorLink(product.getID());
      editLink.setImage(productCatalog.iEdit);
      table.add(editLink, 1, row);
    }
    if (productCatalog._useAnchor) {
      table.add(productCatalog.getAnchor(product.getID()), 1,row);
    }*/
    table.add(productCatalog.getCategoryText(ProductBusiness.getProductName(product, productCatalog._currentLocaleId)), 1,row);
    if (!productCatalog._expandSelectedOnly) {

      if (productCatalog._showNumber) {
      }
      if (productCatalog._showTeaser) {
      }
      if (productCatalog._showPrice) {
      }

      if (productCatalog._showDescription) {
        if (!addedRow){
          ++row;
          table.mergeCells(1, row, 3, row);
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
          table.mergeCells(1, row, 3, row);
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
    }

  }



  return table;
 }
}
