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
    Table table = new Table();
      table.setWidth("100%");

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
	  configCategory = productCatalog.getProductCategoryEditorLink(pCat);
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

	    if (productCatalog._useAnchor) {
	      table.add(productCatalog.getAnchor(product.getID()),1,row);
	    }

	    if (productCatalog._hasEditPermission) {
	      table.add(productCatalog.getProductEditorLink(product), 1, row);
	      table.add(Text.NON_BREAKING_SPACE);
	    }
	    table.add(productCatalog.getNamePresentationObject(product), 1, row);

	    if (productCatalog._showThumbnail && !productCatalog._showTeaser) {
	      if (fileId != -1) {
		image = new Image(fileId);
		table.add(image, 4, row);
	      }
	    }

	    if (productCatalog._showTeaser) {
	      teaser = ProductBusiness.getProductTeaser(product, productCatalog._currentLocaleId);
	      if (!teaser.equals("")) {
		++row;
		table.mergeCells(1, row, 3, row);
		table.setWidth(1, row, "100%");
	    if (productCatalog._showThumbnail) {
	      if (fileId != -1) {
			ICFile file = ((com.idega.core.data.ICFileHome)com.idega.data.IDOLookup.getHomeLegacy(ICFile.class)).findByPrimaryKeyLegacy(fileId);  
			String attributes = file.getMetaData(ProductEditorWindow.imageAttributeKey);
			image = new Image(fileId);
			if ( attributes != null )
				image.setAttributes(getAttributeMap(attributes));
			table.add(image, 1, row);
	      }
	    }

		table.add(productCatalog.getTeaserText(teaser), 1,row);
		if ( productCatalog._linkImage != null ) {
			table.setHeight(++row, 3);
			table.mergeCells(1, row, 3, row);
			table.add(productCatalog.getNameLink(product, productCatalog._linkImage, false),1,row);
		}
		
		table.setHeight(++row,"12");
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

    return table;
  }

}
