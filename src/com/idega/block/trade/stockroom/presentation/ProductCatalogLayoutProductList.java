package com.idega.block.trade.stockroom.presentation;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.block.category.data.ICCategory;
import com.idega.block.trade.data.Currency;
import com.idega.block.trade.stockroom.business.ProductComparator;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.ProductPrice;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.Parameter;

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
  IWContext _iwc;

  public ProductCatalogLayoutProductList() {
  }

  public PresentationObject getCatalog(ProductCatalog productCatalog, IWContext iwc, List productCategories) throws RemoteException, FinderException{
    _productCatalog = productCatalog;
    _iwrb = _productCatalog.iwrb;
    _iwc = _productCatalog.iwc;
    String sOrderBy = iwc.getParameter(this.PARAMETER_ORDER_BY);
    if (sOrderBy != null) {
      orderBy = Integer.parseInt(sOrderBy);
    }else {
      orderBy = productCatalog._orderProductsBy;
    }


    return toEMM(productCategories);
  }

  private Form toEMM(List productCategories) throws RemoteException, FinderException{
    Form form = new Form();

    List products = new Vector();

    if (productCategories != null && productCategories.size() > 0) {
      products = _productCatalog.getProducts(productCategories, false);
    }else {
      products = getProductBusiness(_iwc).getProducts();
    }

    int manyProducts = products.size();
    _productCatalog.sortList(products, orderBy);

    int totalPages = manyProducts / _productCatalog.productsPerPage;
    if (manyProducts % _productCatalog.productsPerPage != 0) {
      ++totalPages;
    }


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
      table.setWidth("100%");


    int row = 0;
    Product product;
    ProductPrice pPrice;
    int fileId;
    Image image;
    float price;

    ++row;
    Link number = new Link(_productCatalog.getCategoryText(_iwrb.getLocalizedString("item_number","Item number")));
      number.addParameter(this.PARAMETER_ORDER_BY, ProductComparator.NUMBER);
    Link name = new Link(_productCatalog.getCategoryText(_iwrb.getLocalizedString("name","Name")));
      name.addParameter(this.PARAMETER_ORDER_BY, ProductComparator.NAME);
    Link lPrice = new Link(_productCatalog.getCategoryText(_iwrb.getLocalizedString("price","Price")));
      lPrice.addParameter(this.PARAMETER_ORDER_BY, ProductComparator.PRICE);

    if (productCategories != null && productCategories.size() > 0)
    if (_productCatalog._hasEditPermission) {
      ICCategory pCat;
      table.mergeCells(1, row, 2, row);
      table.add(_productCatalog.getCategoryText(_productCatalog.iwrb.getLocalizedString("categories_in_use","Prodct categories in use :")), 1 ,row);
      for (int i = 0; i < productCategories.size(); i++) {
        ++row;
        pCat = (ICCategory) productCategories.get(i);
        table.add(_productCatalog.getText(pCat.getName()), 2, row);
        table.add(Text.NON_BREAKING_SPACE, 2,row);
        table.add(_productCatalog.getProductCategoryEditorLink(pCat), 2, row);
      }
      ++row;
    }


    if (_productCatalog._showNumber) {
      table.add(number, 1,row);
      table.setVerticalAlignment(1, row, Table.VERTICAL_ALIGN_BOTTOM);
    }
    table.add(name, 2,row);
    table.setVerticalAlignment(2, row, Table.VERTICAL_ALIGN_BOTTOM);
    if (_productCatalog._showPrice) {
      table.add(lPrice, 3,row);
      table.setVerticalAlignment(3, row, Table.VERTICAL_ALIGN_BOTTOM);
    }
    if (_productCatalog._showThumbnail) {
      table.add(_productCatalog.getCategoryText(_iwrb.getLocalizedString("thumbnail","Thumbnail")), 4,row);
      table.setVerticalAlignment(4, row, Table.VERTICAL_ALIGN_BOTTOM);
    }

    for (int i = startProductId; i < stopProductId; i++) {
      ++row;
      try {
        product = (Product) products.get(i);
        fileId = product.getFileId();
        pPrice = getStockroomBusiness(_iwc).getPrice(product);
        price = 0;
        if (pPrice != null) {
          price = pPrice.getPrice();
        }

        if (_productCatalog._useAnchor) {
          table.add(_productCatalog.getAnchor(product.getID()), 1, row);
        }

        if (_productCatalog._showNumber) {
          table.add(_productCatalog.getText(product.getNumber()), 1,row);
        }
        if (_productCatalog._hasEditPermission) {
          table.add(_productCatalog.getProductEditorLink(product), 2, row);
          table.add(Text.NON_BREAKING_SPACE, 2, row);
        }
        table.add(_productCatalog.getNamePresentationObject(product), 2, row);

        if (fileId != -1) {
          if (_productCatalog._showThumbnail) {
            image = new Image(fileId);
            table.add(image, 4, row);
          }
        }

        if (_productCatalog._showPrice) {
          if (price != 0) {
            table.add(_productCatalog.getText(Integer.toString((int) price)), 3, row);
            if (_productCatalog._showCurrency)  {
              table.add(_productCatalog.getText(Text.NON_BREAKING_SPACE), 3, row);
              table.add(((com.idega.block.trade.data.CurrencyHome)com.idega.data.IDOLookup.getHomeLegacy(Currency.class)).findByPrimaryKeyLegacy(pPrice.getCurrencyId()).getName(), 3, row);
            }
          }else {
            table.add(_productCatalog.getText("0"), 3, row);
          }
        }

        if (_productCatalog._showDescription && _productCatalog._showTeaser) {
          table.mergeCells(4, row, 4 ,row +2);
        }else if (_productCatalog._showDescription || _productCatalog._showTeaser) {
          table.mergeCells(4, row, 4 ,row +1);
        }

        if (_productCatalog._showTeaser) {
          ++row;
          table.add(_productCatalog.getText(product.getProductTeaser(_productCatalog._currentLocaleId)), 2, row);
        }

        if (_productCatalog._showDescription) {
          ++row;
          table.add(_productCatalog.getText(product.getProductDescription(_productCatalog._currentLocaleId)), 2, row);
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

    if (totalPages > 1) {
      ++row;
      table.mergeCells(1, row, 4 ,row);
      table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_CENTER);
      table.add(_productCatalog.getPagesTable(totalPages, parameters), 1,row);

    }
    form.add(table);

    return form;
  }
}
