package com.idega.block.trade.stockroom.presentation;

import java.rmi.RemoteException;

import com.idega.block.trade.stockroom.business.ProductBusiness;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class ProductEditor extends Block {
  private static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.trade";
  public static final String PRODUCT_ID = "prod_edit_prod_id";

  private IWResourceBundle iwrb;
  private IWBundle bundle;

  private Product _product = null;
  private int _productId = -1;

  public ProductEditor() {
  }

  public void main(IWContext iwc) throws RemoteException{
    init(iwc);
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  private void init(IWContext iwc) throws RemoteException {
    this.bundle = getBundle(iwc);
    this.iwrb = this.bundle.getResourceBundle(iwc);

    try {
      String sProductId = iwc.getParameter(ProductEditor.PRODUCT_ID);
      this._productId = Integer.parseInt(sProductId);
      this._product = getProductBusiness(iwc).getProduct(this._productId);
    }catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  private ProductBusiness getProductBusiness(IWApplicationContext iwac) throws RemoteException {
    return (ProductBusiness) IBOLookup.getServiceInstance(iwac, ProductBusiness.class);
  }
}
