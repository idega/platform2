package com.idega.block.trade.stockroom.presentation;

import com.idega.idegaweb.IWResourceBundle;
import com.idega.util.idegaTimestamp;
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

public class ProductCatalogLayoutProductList extends AbstractProductCatalogLayout {

  private String PARAMETER_ORDER_BY = "pr_cat_lay_pr_list_ord";
  private int orderBy = ProductComparator.NUMBER;

  IWResourceBundle _iwrb;
  ProductCatalog _productCatalog;

  public ProductCatalogLayoutProductList() {
  }

  public PresentationObject getCatalog(ProductCatalog productCatalog, IWContext iwc, List productCategories) {
    _productCatalog = productCatalog;
    _iwrb = _productCatalog.iwrb;
    String sOrderBy = iwc.getParameter(this.PARAMETER_ORDER_BY);
    if (sOrderBy != null) {
      orderBy = Integer.parseInt(sOrderBy);
    }


    return toEMM(productCategories);
  }
  private Form toEMM(List productCategories) {
    Form form = new Form();

    debug("Getting products..."+idegaTimestamp.RightNow().toString());
    List products = new Vector();

    /**
     * @todo hondla ad ekkert category sé valið
     * @todo birta ekki sama product 2svar (eða oftar)
     */

    if (productCategories != null && productCategories.size() > 0) {
      ICCategory pCat;
      for (int i = 0; i < productCategories.size(); i++) {
        pCat = (ICCategory) productCategories.get(i);
        products.addAll(ProductBusiness.getProducts(pCat));
      }
    }else {
      products = ProductBusiness.getProducts();
    }

    int manyProducts = products.size();
    debug("Start sorting..... "+idegaTimestamp.RightNow().toString());
    Collections.sort(products, new ProductComparator(orderBy));
    debug("Done sorting...... "+idegaTimestamp.RightNow().toString());

    int totalPages = manyProducts / _productCatalog.productsPerPage;
    if (manyProducts % _productCatalog.productsPerPage != 0) {
      ++totalPages;
    }

    //int totalPages = 1;

    int startProductId = 0;
    if (_productCatalog.currentPage > 1) {
      startProductId = _productCatalog.productsPerPage * (_productCatalog.currentPage - 1);
    }
    int stopProductId = startProductId + _productCatalog.productsPerPage;
    if (manyProducts < stopProductId) {
      stopProductId = manyProducts;
    }

    Table table = new Table();
      table.setCellspacing(2);


    int row = 0;
    Product product;
    ProductPrice pPrice;
    int fileId;
    Image image;
    float price;
    Link productLink;

    ++row;

    Link number = new Link(_productCatalog.getCategoryText(_iwrb.getLocalizedString("item_number","Item number")));
      number.addParameter(this.PARAMETER_ORDER_BY, ProductComparator.NUMBER);
    Link name = new Link(_productCatalog.getCategoryText(_iwrb.getLocalizedString("name","Name")));
      name.addParameter(this.PARAMETER_ORDER_BY, ProductComparator.NAME);
    Link lPrice = new Link(_productCatalog.getCategoryText(_iwrb.getLocalizedString("price","Price")));
      lPrice.addParameter(this.PARAMETER_ORDER_BY, ProductComparator.PRICE);

    table.add(number, 1,row);
    table.add(name, 2,row);
    table.add(lPrice, 3,row);
    if (_productCatalog._showImage) {
      table.add(_productCatalog.getCategoryText(_iwrb.getLocalizedString("thumbnail","Thumbnail")), 4,row);
    }

    for (int i = startProductId; i < stopProductId; i++) {
//    for (int i = 0; i < products.size(); i++) {
      ++row;
      try {
        product = (Product) products.get(i);
        fileId = product.getFileId();
        pPrice = StockroomBusiness.getPrice(product);
        price = 0;
        if (pPrice != null) {
          price = pPrice.getPrice();
        }

        table.add(_productCatalog.getText(product.getNumber()), 1,row);

        if (_productCatalog._productIsLink) {
          productLink = new Link(_productCatalog.getText(ProductBusiness.getProductName(product, _productCatalog._currentLocaleId)));
          productLink.addParameter(ProductViewer.PRODUCT_ID, product.getID());
          if (_productCatalog._productLinkPage != null) {
            productLink.setPage(_productCatalog._productLinkPage);
          }else {
            productLink.setWindowToOpen(ProductViewerWindow.class);
          }
          table.add(productLink, 2,row);
        }else {
          table.add(_productCatalog.getText(ProductBusiness.getProductName(product, _productCatalog._currentLocaleId)), 2,row);
        }
//        table.add(ProductCatalog.getText(ProductBusiness.getProductName(product)), 2,row);

        if (fileId != -1) {
          if (_productCatalog._showImage) {
            image = new Image(fileId);
            image.setMaxImageWidth(100);
            table.add(image, 4, row);
          }
        }

        if (price != 0) {
          table.add(_productCatalog.getText(Integer.toString((int) price)), 3, row);
        }else {
          table.add(_productCatalog.getText("0"), 3, row);
        }

        if (_productCatalog._showTeaser) {
          table.mergeCells(4, row, 4, ++row);
          table.add(_productCatalog.getText(ProductBusiness.getProductTeaser(product, _productCatalog._currentLocaleId)), 2, row);
        }


      }catch (Exception e) {
        table.add("Villa", 1, row);
        e.printStackTrace(System.err);
      }
    }

    table.setColumnVerticalAlignment(1, "top");
    table.setColumnVerticalAlignment(2, "top");
    table.setColumnVerticalAlignment(3, "top");
    table.setColumnVerticalAlignment(4, "top");
    table.setColumnAlignment(3, "right");

    Parameter orderPar = new Parameter(this.PARAMETER_ORDER_BY, Integer.toString(orderBy));
    List parameters = new Vector();
      parameters.add(orderPar);

    form.add(_productCatalog.getPagesTable(totalPages, parameters));
    form.add(table);

    return form;
  }

}