package com.idega.block.trade.stockroom.presentation;

import java.util.*;
import com.idega.block.trade.stockroom.data.ProductCategory;
import com.idega.data.IDOFinderException;
import com.idega.core.data.ICFile;
import com.idega.data.EntityFinder;
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
    _description = TextFormatter.formatText(_description,1,Table.HUNDRED_PERCENT);
    _teaser = ProductBusiness.getProductTeaser(product,productViewer._localeId);
    _teaser = TextFormatter.formatText(_teaser,1,Table.HUNDRED_PERCENT);
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

    table.add(header, 1, row++);

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

    if (_product != null && productViewer._showImages) {
      Table imageTable = new Table(1,1);
      imageTable.setCellpadding(0);
      imageTable.setCellspacing(0);
      imageTable.setAlignment(productViewer._imageAlignment);
      if ( productViewer._showThumbnail ) {
	ProductItemThumbnail thumb = new ProductItemThumbnail(_product);
	if ( productViewer._imageWidth != null ) {
	  try {
	    thumb.setWidth(Integer.parseInt(productViewer._imageWidth));
	  }
	  catch (NumberFormatException e) {
	  }
	}
	imageTable.add(thumb);
	table.add(imageTable, 1, row);
      }
      else {
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
    }
    table.add(description, 1, row);

    if ( productViewer._showProductLink && _product != null ) {
      table.setHeight(1,++row,productViewer._spaceBetween);

      Link link = new Link();
      if ( productViewer._productImage != null )
	link.setPresentationObject(productViewer._productImage);
      else
	link.setText(this._name);

      if ( productViewer._productPage != null )
	link.setPage(productViewer._productPage);
      link.addParameter(ProductBusiness.PRODUCT_ID, _product.getID());

      if ( productViewer._addCategoryID ) {
	try {
	  List list = ProductBusiness.getProductCategories(_product);
	  if ( list != null ) {
	    Iterator iter = list.iterator();
	    while (iter.hasNext()) {
	      link.addParameter(ProductCatalog.CATEGORY_ID,((ProductCategory)iter.next()).getID());
	    }
	  }
	}
	catch (IDOFinderException e) {
	}
      }

      table.add(link,1,++row);
    }

    return table;
  }
}