package com.idega.block.trade.stockroom.presentation;

import com.idega.builder.data.IBPage;
import com.idega.data.EntityFinder;
import com.idega.block.presentation.CategoryBlock;
import com.idega.util.idegaTimestamp;
import java.util.*;

import com.idega.idegaweb.*;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.presentation.Block;
import com.idega.block.trade.stockroom.business.*;
import com.idega.block.trade.stockroom.data.*;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class ProductCatalog extends CategoryBlock{
  public static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.trade";
  private static final String _VIEW_PAGE = "prod_cat_view_page";
  private static final String _ORDER_BY ="prod_cat_order_by";

  private int productsPerPage = 10;
  private int currentPage = 1;
  private int orderBy = ProductComparator.NUMBER;

  private IWResourceBundle iwrb;
  private IWBundle bundle;

  private Image iCreate = null;
  private Image iDelete = null;
  private Image iEdit = null;
  private Image iDetach = null;

  private String _fontStyle = null;
  private String _catFontStyle = null;
  private IBPage _productLinkPage = null;
  private boolean _productIsLink = false;

  public ProductCatalog() {
  }

  public void main(IWContext iwc) {
    init(iwc);
    catalog(iwc);
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  private void init(IWContext iwc) {
    bundle = getBundle(iwc);
    iwrb = bundle.getResourceBundle(iwc);

    IWBundle  coreBundle = iwc.getApplication().getCoreBundle();
    iCreate = coreBundle.getImage("shared/create.gif");
    iDelete = coreBundle.getImage("shared/delete.gif");
    iEdit   = coreBundle.getImage("shared/edit.gif");
    iDetach = coreBundle.getImage("shared/detach.gif");


    try {
      String sCurrentPage = iwc.getParameter(this._VIEW_PAGE);
      String sOrderBy = iwc.getParameter(this._ORDER_BY);
      if (sCurrentPage != null) {
        this.currentPage = Integer.parseInt(sCurrentPage);
      }
      if (sOrderBy != null) {
        this.orderBy = Integer.parseInt(sOrderBy);
      }
    }catch (NumberFormatException n) {}


  }

  private void catalog(IWContext iwc) {
/*    debug("Getting products..."+idegaTimestamp.RightNow().toString());
    List products = ProductBusiness.getProducts();
    int manyProducts = products.size();
    debug("Start sorting..... "+idegaTimestamp.RightNow().toString());
    Collections.sort(products, new ProductComparator(orderBy));
    debug("Done sorting...... "+idegaTimestamp.RightNow().toString());
*/
    /*int totalPages = manyProducts / productsPerPage;
    if (manyProducts % productsPerPage != 0) {
      ++totalPages;
    }

    int totalPages = 1;

    int startProductId = 0;
    if (currentPage > 1) {
      startProductId = productsPerPage * (currentPage - 1);
    }
    int stopProductId = startProductId + productsPerPage;
    //if (manyProducts < stopProductId) {
    //  stopProductId = manyProducts;
    //}
*/


/*
    for (int i = startProductId; i < stopProductId; i++) {
//    for (int i = 0; i < products.size(); i++) {
      ++row;
      try {
        product = (Product) products.get(i);
        fileId = product.getFileId();
        table.add(getText(product.getNumber()), 1,row);
        table.add(getText(ProductBusiness.getProductName(product)), 2,row);
        if (fileId != -1) {
          //image = new Image(fileId);
          //table.add(image, 4, row);
          table.add("MYND FYLGIR", 4, row);
        }

      }catch (Exception e) {
        table.add("Villa", 1, row);
        e.printStackTrace(System.err);
      }
    }
*/

    idegaCatalog(iwc);
  }

  private void idegaCatalog(IWContext iwc) {
    Form form = new Form();
    Table table = new Table();
      form.add(table);

    int row = 1;

    List productCategories = new Vector();
    try {
      productCategories = ProductBusiness.getProductCategories();
    }catch (Exception e) {
      e.printStackTrace(System.err);
    }

    ProductCategory pCat;
    Product product;
    int fileId;
    Image image;

    List catProducts;
    Link configCategory;
    Link productLink;
    for (int i = 0; i < productCategories.size(); i++) {
      ++row;
      try {
        pCat = (ProductCategory) productCategories.get(i);
        configCategory = new Link(iDetach);
          configCategory.setWindowToOpen(ProductCategoryEditor.class);
          configCategory.addParameter(ProductCategoryEditor.SELECTED_CATEGORY, pCat.getID());
        table.add(getCategoryText(pCat.getName()), 1,row);
        if (hasEditPermission()) {
          table.add(configCategory, 2,row);
        }
        catProducts = EntityFinder.getInstance().findRelated(pCat, Product.class);
        for (int j = 0; j < catProducts.size(); j++) {
          ++row;
          try {
            product = (Product) catProducts.get(j);
            fileId = product.getFileId();
            //table.add(getText(product.getNumber()), 1,row);

            if (this._productIsLink) {
              productLink = new Link(getText(ProductBusiness.getProductName(product)));
              productLink.addParameter(ProductViewer.PRODUCT_ID, product.getID());
              if (this._productLinkPage != null) {
                productLink.setPage(_productLinkPage);
              }else {
                productLink.setWindowToOpen(ProductViewerWindow.class);
              }
              table.add(productLink, 1,row);
            }else {
              table.add(getText(ProductBusiness.getProductName(product)), 1,row);
            }

            if (fileId != -1) {
              //image = new Image(fileId);
              //table.add(image, 4, row);
              //table.add("MYND FYLGIR", 4, row);
            }

          }catch (Exception e) {
            table.add("Villa", 1, row);
            e.printStackTrace(System.err);
          }
        }


      }catch (Exception e) {
        table.add("Villa", 1, row);
        e.printStackTrace(System.err);
      }
    }

    Link createLink = ProductEditorWindow.getEditorLink(-1);
      createLink.setImage(iCreate);
    Link detachLink = getCategoryLink(ProductCategory.CATEGORY_TYPE_PRODUCT);
      detachLink.setImage(iDetach);

    if (hasEditPermission()) {
      add(createLink);
      add(detachLink);
    }
//    add(getPagesTable(totalPages));
    add(form);
  }

  private Table getPagesTable(int pages) {
    Table pagesTable = new Table(pages+2, 1);
      pagesTable.setCellpadding(2);
      pagesTable.setCellspacing(2);

    Text pageText;

    if (currentPage > 1) {
      pageText = getText(iwrb.getLocalizedString("travel.previous","Previous"));
      Link prevLink = new Link(pageText);
        prevLink.addParameter(this._VIEW_PAGE, currentPage -1);
      pagesTable.add(prevLink, 1, 1);
    }

    Link pageLink;
    for (int i = 1; i <= pages; i++) {
      if (i == currentPage) {
        pageText = getText(Integer.toString(i));
        pageText.setBold(true);
      }else {
        pageText = getText(Integer.toString(i));
      }
      pageLink = new Link(pageText);
        pageLink.addParameter(this._VIEW_PAGE, i);
      pagesTable.add(pageLink, i+1, 1);
    }

    if (currentPage < pages) {
      pageText = getText(iwrb.getLocalizedString("travel.next","Next"));
      Link nextLink = new Link(pageText);
        nextLink.addParameter(this._VIEW_PAGE, currentPage + 1);
      pagesTable.add(nextLink, pages + 2, 1);
    }

    return pagesTable;
  }

  private Text getText(String content) {
    Text text = new Text(content);
    if (_fontStyle != null) {
      text.setFontStyle(_fontStyle);
    }
    return text;
  }

  private Text getCategoryText(String content) {
    Text text = new Text(content);
    if (_catFontStyle != null) {
      text.setFontStyle(_catFontStyle);
    }
    return text;
  }

  public void setFontStyle(String fontStyle) {
    this._fontStyle = fontStyle;
  }

  public void setCategoryFontStyle(String catFontStyle) {
    this._catFontStyle = catFontStyle;
  }

  public void setItemsPerPage(int numberOfItems) {
    this.productsPerPage = numberOfItems;
  }

  public void setProductLinkPage(IBPage page) {
    this._productLinkPage = page;
    setProductAsLink(true);
  }

  public void setProductAsLink(boolean isLink) {
    this._productIsLink = true;
  }
}
