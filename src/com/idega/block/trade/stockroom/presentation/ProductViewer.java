package com.idega.block.trade.stockroom.presentation;

import com.idega.presentation.text.*;
import com.idega.block.trade.stockroom.business.*;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.idegaweb.*;
import com.idega.presentation.*;
import com.idega.presentation.Block;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class ProductViewer extends Block {
  public static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.trade";
  public static final String PRODUCT_ID = "pr_vw_prod_id";

  private IWResourceBundle iwrb;
  private IWBundle bundle;
  private Image iEdit = null;

  private Product _product = null;
  private int _productId = -1;

  public ProductViewer() {
  }

  public void main(IWContext iwc) {
    init(iwc);
    if (_product != null) {

      if (hasEditPermission()) {
        Link lEdit = ProductEditorWindow.getEditorLink(_productId);
          lEdit.setImage(iEdit);
        add(lEdit);
      }

      add(ProductBusiness.getProductName(_product));

    }else {

      add("product er samasem null");

    }
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  private void init(IWContext iwc) {
    bundle = getBundle(iwc);
    iwrb = bundle.getResourceBundle(iwc);

    try {
      String sProductId = iwc.getParameter(this.PRODUCT_ID);
      if (sProductId != null) {
        _productId = Integer.parseInt(sProductId);
        _product = ProductBusiness.getProduct(_productId);
        if (!_product.getIsValid()) {
          _product = null;
        }
      }
    }catch (Exception e) {
      e.printStackTrace(System.err);
    }

    IWBundle  coreBundle = iwc.getApplication().getCoreBundle();
    iEdit   = coreBundle.getImage("shared/edit.gif");
  }
/*
  public boolean deleteBlock(int ICObjectInstanceId) {
    return false;
  }*/
}