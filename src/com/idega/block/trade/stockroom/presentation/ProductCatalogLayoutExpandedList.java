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
  table.setWidth("100%");
  int row = 0;

  Product selectedProduct = productCatalog.getSelectedProduct(iwc);
  Product product;

  if (selectedProduct == null) {
    debug("selected product id == null");
  }else {
    debug("selected product id = "+selectedProduct.getID());
  }

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
    table.add(productCatalog.getCategoryText(pCat.getName()), 1,row);
    table.mergeCells(1,row, 2, row);
    if (productCatalog._hasEditPermission) {
      configCategory = new Link(productCatalog.iDetach);
        configCategory.setWindowToOpen(ProductCategoryEditor.class);
        configCategory.addParameter(ProductCategoryEditor.SELECTED_CATEGORY, pCat.getID());
      table.add(configCategory, 3,row);
      table.setAlignment(3, row, "right");
    }
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

      nameLink = new Link(productCatalog.getText(ProductBusiness.getProductName(product, productCatalog._currentLocaleId)));
      nameLink.addParameter(ProductBusiness.PRODUCT_ID, product.getID());

      table.add(nameLink, 1,row);
      table.mergeCells(1, row, 3, row);

      if (productCatalog._expandSelectedOnly) {
        if (selectedProduct != null && (selectedProduct.getID() == product.getID())) {
          row = expand(product, table ,row);
        }
      }else {
        row = expand(product, table, row);
      }

    }
  }




  return table;
 }

  private int expand(Product product, Table table, int row) {
    boolean addedRow = false;
    if (productCatalog._showNumber) {
    }
    if (productCatalog._showTeaser) {
      if (!addedRow){
        ++row;
        addedRow = true;
      }
      teaser = ProductBusiness.getProductTeaser(product, productCatalog._currentLocaleId);
      teaser = TextFormatter.formatText(teaser, -1, null);
      table.add(productCatalog.getText(teaser), 2, row);
      table.setVerticalAlignment(2, row, Table.VERTICAL_ALIGN_TOP);
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
          table.setVerticalAlignment(3, row, Table.VERTICAL_ALIGN_TOP);
        }catch (SQLException sql) {
          sql.printStackTrace(System.err);
        }
      }
    }


    //// addin closerlookbutton();
    if (productCatalog._productIsLink) {
      ++row;
      table.mergeCells(1, row, 3, row);
      table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
      String viewerIm = productCatalog.iwrb.getLocalizedString("more","more");
/*      Link viewerLn = new Link(viewerIm);
        viewerLn.setWindowToOpen(ProductViewerWindow.class);
        viewerLn.addParameter(ProductBusiness.PRODUCT_ID, product.getID());
*/      Link po = productCatalog.getNameLink(product, productCatalog.getText(productCatalog.iwrb.getLocalizedString("more","more")));
      table.add(po, 1, row);
    }


    return row;

  }
}
