package com.idega.block.trade.stockroom.presentation;

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

public class ProductCatalogLayoutSingleFile extends AbstractProductCatalogLayout {

  public ProductCatalogLayoutSingleFile() {
  }

  public PresentationObject getCatalog(ProductCatalog productCatalog, IWContext iwc, List productCategories) {

    Form form = new Form();
    Table table = new Table();
      form.add(table);

    int row = 0;


    ICCategory pCat;
    Product product;
    int fileId;
    Image image;
    String teaser;
    String description;

    List catProducts;
    Link configCategory;
    Link productLink;
    Text nameText;

    for (int i = 0; i < productCategories.size(); i++) {
      if (i != 0){
        ++row;
        table.add(productCatalog.getCategoryText(Text.NON_BREAKING_SPACE), 1,row);
      }
      ++row;
      try {
        pCat = (ICCategory) productCategories.get(i);
        if (productCatalog._showCategoryName) {
          table.mergeCells(1, row, 2, row);
          table.add(productCatalog.getCategoryText(pCat.getName()), 1,row);
        }

        if (productCatalog._hasEditPermission) {
          configCategory = new Link(productCatalog.iDetach);
            configCategory.setWindowToOpen(ProductCategoryEditor.class);
            configCategory.addParameter(ProductCategoryEditor.SELECTED_CATEGORY, pCat.getID());
          table.add(configCategory, 3,row);
          table.setAlignment(3, row, "right");
        }

        if (!productCatalog._showCategoryName && !productCatalog._hasEditPermission) {
          --row;
        }

        catProducts = ProductBusiness.getProducts(pCat);//.getInstance().findRelated(pCat, Product.class);
        productCatalog.sortList(catProducts);
        for (int j = 0; j < catProducts.size(); j++) {
          ++row;
          table.mergeCells(1, row, 3, row);
          try {
            product = (Product) catProducts.get(j);
            fileId = product.getFileId();
            //table.add(getText(product.getNumber()), 1,row);

            nameText = productCatalog.getText(ProductBusiness.getProductName(product, productCatalog._currentLocaleId));
            if (productCatalog._useAnchor) {
              table.add(productCatalog.getAnchor(product.getID()),1,row);
            }

            /*
            nameText.setName(Integer.toString(product.getID()));
            if (productCatalog._productIsLink) {
              if (productCatalog._useAnchor) {
                productLink = new AnchorLink(nameText, productCatalog.getAnchorString(product.getID()));
              }else {
                productLink = new Link(nameText);
              }
              productLink.addParameter(ProductBusiness.PRODUCT_ID, product.getID());
              if (productCatalog._productLinkPage != null) {
                productLink.setPage(productCatalog._productLinkPage);
              }else if (productCatalog._viewerInWindow) {
                productLink.setWindowToOpen(ProductViewerWindow.class);
              }
              table.add(productLink, 1,row);
            }else {
              table.add(nameText, 1,row);
            }*/
            table.add(productCatalog.getNamePresentationObject(product), 1, row);

            if (fileId != -1) {

              //image = new Image(fileId);
              //table.add(image, 4, row);
              //table.add("MYND FYLGIR", 4, row);
            }

            if (productCatalog._showTeaser) {
              teaser = ProductBusiness.getProductTeaser(product, productCatalog._currentLocaleId);
              if (!teaser.equals("")) {
                ++row;
                table.setWidth(2, row, "100%");
                table.mergeCells(2, row, 3, row);
                table.add(productCatalog.getText(teaser), 2,row);
              }
            }

            if (productCatalog._showDescription) {
              description = ProductBusiness.getProductDescription(product, productCatalog._currentLocaleId);
              if (!description.equals("")) {
                ++row;
                table.setWidth(2, row, "100%");
                table.mergeCells(2, row, 3, row);
                table.add(productCatalog.getText(description), 2,row);
              }
            }

          }catch (Exception e) {
            e.printStackTrace(System.err);
          }
        }


      }catch (Exception e) {
        e.printStackTrace(System.err);
      }
    }


//    add(getPagesTable(totalPages));
    //add(form);

    return form;
  }

}