package com.idega.block.trade.stockroom.presentation;

import com.idega.data.IDOFinderException;
import com.idega.core.data.ICFile;
import com.idega.data.EntityFinder;
import java.util.Vector;
import java.util.List;
import com.idega.block.text.business.TextFormatter;
import com.idega.util.text.TextSoap;
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

  private String _name = "Demo";
  private String _number = "Number";
  private String _teaser = "Teaser";
  private String _description = "Desription";
  private List _images = new Vector();
  private Product _product = null;

  public ProductViewerLayoutIdega() { }

  /**
   * @todo Handle multible images...
   */
  public PresentationObject getDemo(ProductViewer productViewer, IWContext iwc) {
    String IMAGE_BUNDLE_IDENTIFIER="com.idega.block.image";
    Image image = iwc.getApplication().getBundle(IMAGE_BUNDLE_IDENTIFIER).getLocalizedImage("picture.gif", productViewer._locale);
    _images.add(image);

    _description = TextFormatter.getLoremIpsumString(iwc);

    return printViewer(productViewer, iwc);
  }


  public PresentationObject getViewer(ProductViewer productViewer, Product product, IWContext iwc) {
    _name = ProductBusiness.getProductName(product, productViewer._localeId);
    _description = ProductBusiness.getProductDescription(product, productViewer._localeId);
    _description = TextSoap.formatText(_description);
    _product = product;
    try {
      _images = EntityFinder.getInstance().findRelated(product, ICFile.class);
    }catch (IDOFinderException ido) {
      ido.printStackTrace(System.err);
    }

    return printViewer(productViewer, iwc);
  }

  public PresentationObject printViewer(ProductViewer productViewer, IWContext iwc) {
    Table table = new Table();
      table.setWidth(Table.HUNDRED_PERCENT);

    Text header = productViewer.getHeaderText(this._name);
    Text description = productViewer.getText(this._description);

    table.add(header, 1, 1);

    table.mergeCells(1, 2, 2, 2);

    if (productViewer._useHRasSeperator) {
      HorizontalRule hr = new HorizontalRule("100%");
      table.add(hr, 1, 2);
    }else {
      if (productViewer._seperator != null) {
        table.add(productViewer._seperator, 1, 2);
      }
    }
    table.add(description, 1, 3);
    if (_product != null) {
      ProductItemImages pii = new ProductItemImages(_product);
        pii.setVerticalView(true);
        pii.setImageAlignment("right");
      table.add(pii, 2, 3);
    }

    table.setAlignment(2, 1, Table.HORIZONTAL_ALIGN_RIGHT);
    table.setAlignment(2, 3, Table.HORIZONTAL_ALIGN_RIGHT);
    table.setVerticalAlignment(1,1, Table.VERTICAL_ALIGN_BOTTOM);
    table.setVerticalAlignment(2,1, Table.VERTICAL_ALIGN_BOTTOM);
    table.setVerticalAlignment(1,3, Table.VERTICAL_ALIGN_TOP);
    table.setVerticalAlignment(2,3, Table.VERTICAL_ALIGN_TOP);

    return table;
  }



}

