package com.idega.block.trade.stockroom.presentation;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

import javax.ejb.FinderException;

import com.idega.block.text.business.TextFormatter;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.core.data.ICCategory;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.AnchorLink;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;

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


 public PresentationObject getCatalog( ProductCatalog productCatalog, IWContext iwc, List productCategories ) throws RemoteException, FinderException{
  this.productCatalog = productCatalog;
  this.iwc = iwc;
  Table table = new Table();
  table.setCellpaddingAndCellspacing(0);
  table.setWidth("100%");
  int row = 1;

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
    products = getProductBusiness(iwc).getProducts((ICCategory) pCat);
    productCatalog.sortList(products);

    if (productCatalog._hasEditPermission) {
      configCategory = new Link(productCatalog.iDetach);
	configCategory.setWindowToOpen(ProductCategoryEditor.class);
	configCategory.addParameter(ProductCategoryEditor.SELECTED_CATEGORY, pCat.getID());
      table.add(configCategory, 1,row);
    }
    if ( productCatalog._showCategoryName )
      table.add(productCatalog.getCategoryText(pCat.getName()), 1,row++);
    else
      row++;

    for (int i = 0; i < products.size(); i++) {
      productTable = new Table();
      productTable.setCellpaddingAndCellspacing(0);
      productTable.setWidth("100%");
      product = (Product) products.get(i);
      if (productCatalog._hasEditPermission) {
	editLink = productCatalog.getProductEditorLink(product);
	productTable.add(editLink, 1, 1);
      }
      if (productCatalog._useAnchor) {
	productTable.add(productCatalog.getAnchor(product.getID()), 1,1);
      }

      if ( productCatalog._productIsLink ) {
	if (productCatalog._useAnchor) {
	  nameLink = new AnchorLink(productCatalog.getText(product.getProductName(productCatalog._currentLocaleId)), productCatalog.getAnchorString(product.getID()));
	  nameLink.setBold();
	}else {
	  nameLink = new Link(productCatalog.getText(product.getProductName(productCatalog._currentLocaleId)));
	  nameLink.setBold();
	}
	nameLink.addParameter(getProductBusiness(iwc).getProductIdParameter(), product.getID());
	productTable.add(nameLink, 1,1);
      }
      else {
	if ( productCatalog._showCategoryName ) {
	  nameText = productCatalog.getText(product.getProductName(productCatalog._currentLocaleId));
	  nameText.setBold();
	}
	else {
	  nameText = productCatalog.getCategoryText(product.getProductName(productCatalog._currentLocaleId));
	}
	productTable.add(nameText, 1,1);
      }
      row = expand(product, table, productTable, row);
    }
  }




  return table;
 }

  private int expand(Product product, Table table, Table productTable, int row) throws RemoteException{
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
      description = product.getProductDescription(productCatalog._currentLocaleId);
      description = TextFormatter.formatText(description, -1, null);
      productTable.add(productCatalog.getText(description), 1, 2);
    }

    table.add(productTable,1,row++);
    table.setHeight(1,row++,"20");

    return row;
  }

}
