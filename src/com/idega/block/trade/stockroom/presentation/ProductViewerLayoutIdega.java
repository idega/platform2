package com.idega.block.trade.stockroom.presentation;

import java.sql.SQLException;
import com.idega.block.trade.stockroom.business.*;
import com.idega.presentation.text.*;
import com.idega.presentation.*;
import com.idega.block.trade.stockroom.data.Product;

/**
 *  Title: idegaWeb TravelBooking Description: Copyright: Copyright (c) 2001
 *  Company: idega
 *
 *@author     <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 *@created    9. mars 2002
 *@version    1.0
 */

public class ProductViewerLayoutIdega extends AbstractProductViewerLayout {

  private String name = "Product";
  private String number = "Number";
  private String teaser = "Teaser";
  private String description = "Desription";
  private Image image = null;

  public ProductViewerLayoutIdega() { }

  /**
   * @todo Handle multible images...
   */
  public PresentationObject getDemo(ProductViewer productViewer, Product product, IWContext iwc) {
    String IMAGE_BUNDLE_IDENTIFIER="com.idega.block.image";
    image = iwc.getApplication().getBundle(IMAGE_BUNDLE_IDENTIFIER).getLocalizedImage("picture.gif", productViewer._locale);

    return getViewer(productViewer, product, iwc);
  }

  public PresentationObject getViewer(ProductViewer productViewer, Product product, IWContext iwc) {
    Table table = new Table();

    /**
     * @todo Gera Demo...
     */

    Text header = productViewer.getHeaderText(ProductBusiness.getProductName(product, productViewer._localeId));
    Text description = productViewer.getText(ProductBusiness.getProductDescription(product, productViewer._localeId));
    Image image = null;

    HorizontalRule hr = new HorizontalRule("100%");

    int fileId = product.getFileId();
    if (fileId != -1) {
      try {
        image = new Image(fileId);
      }catch (SQLException sql) {
        sql.printStackTrace(System.err);
      }
    }

    table.add(header, 1, 1);
    table.mergeCells(1, 2, 2, 2);
    table.add(hr, 1, 2);
    table.add(description, 1, 3);
    if (image != null) {
      table.add(image, 2, 3);
    }

    table.setVerticalAlignment(1,3, "top");
    table.setVerticalAlignment(2,3, "top");

    return table;
  }

}
