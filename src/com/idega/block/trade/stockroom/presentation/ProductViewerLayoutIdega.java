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
      table.setCellpadding(0);
      table.setCellspacing(0);

    Text header = productViewer.getHeaderText(this._name);
    Text description = null;
    if ( productViewer._showTeaser )
      description = productViewer.getText(this._teaser);
    else
      description = productViewer.getText(this._description);

    int row = 1;

    table.add(header, 1, row);

    if (productViewer._useHRasSeperator) {
      HorizontalRule hr = new HorizontalRule("100%");
      table.add(hr, 1, row++);
    }
    else {
      if (productViewer._seperator != null) {
	table.add(productViewer._seperator, 1, row++);
      }
      else {
	table.setHeight(1,row++,productViewer._spaceBetween);
      }
    }
    if (_product != null) {
      Table imageTable = new Table(1,1);
      imageTable.setCellpadding(0);
      imageTable.setCellspacing(0);
      imageTable.setAlignment(productViewer._imageAlignment);
      ProductItemImages pii = new ProductItemImages(_product);
	pii.setVerticalView(true);
	pii.setImageAlignment(Table.HORIZONTAL_ALIGN_CENTER);
	if ( productViewer._imageWidth != null ) {
	  try {
	    pii.setWidth(Integer.parseInt(productViewer._imageWidth));
	  }
	  catch (NumberFormatException e) {
	    pii.setWidth(0);
	  }
	}
      imageTable.add(pii);
      table.add(imageTable, 1, row);
    }
    table.add(description, 1, row);

    if ( productViewer._showProductLink ) {
      table.setHeight(1,++row,productViewer._spaceBetween);

      Link link = new Link();
      if ( productViewer._productImage != null )
	link.setPresentationObject(productViewer._productImage);
      else
	link.setText(this._name);

      if ( productViewer._productPage != null )
	link.setPage(productViewer._productPage);

      table.add(link,1,++row);
    }

    return table;
  }



}

