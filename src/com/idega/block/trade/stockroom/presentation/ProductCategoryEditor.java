package com.idega.block.trade.stockroom.presentation;

import com.idega.block.presentation.CategoryWindow;
import java.rmi.RemoteException;
import com.idega.business.IBOLookup;
import javax.transaction.*;
import com.idega.transaction.IdegaTransactionManager;
import com.idega.data.EntityFinder;
import java.sql.SQLException;
import com.idega.block.trade.stockroom.data.*;
import com.idega.block.trade.stockroom.business.*;
import java.util.*;
import com.idega.presentation.text.*;
import com.idega.presentation.ui.*;
import com.idega.idegaweb.*;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.*;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class ProductCategoryEditor extends CategoryWindow {
  private IWBundle bundle = null;
  private IWResourceBundle iwrb = null;
  public static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.trade";
  public static final String SELECTED_CATEGORY = "pr_cat_seleted_cat";

  private static final String _action = "pr_cat_editor_action";
  private static final String _parameterSaveCategory = "pr_cat_view_cat";
  private static final String _parameterClose = "pr_cat_close";
  private static final String _parameterProductIn = "pr_cat_pr_in";
  private static final String _parameterProductOut = "pr_cat_pr_out";

  private static final String _parameterSelectedCategory = SELECTED_CATEGORY;
  private int maxWidth = 50;
  private int height = 10;


  int _selectedCategory = -1;
  ProductCategory _productCategory = null;
  List _categories = null;
  private int localeId = 1;

  public ProductCategoryEditor() {
    super.setWidth(600);
    super.setHeight(460);
    super.setUnMerged();
  }

  public void main(IWContext iwc) throws Exception{
    init(iwc);

    if ( _selectedCategory != -1) {
      String action = iwc.getParameter(_action);
      if (action == null) {
        viewCategory(iwc);
      }else if (action.equals(_parameterClose)) {
        super.setParentToReload();
        super.close();
      }else {
        saveAssignment(iwc);
      }
    }
  }

  private void init(IWContext iwc) {
    bundle = getBundle(iwc);
    iwrb = bundle.getResourceBundle(iwc);
    localeId = iwc.getCurrentLocaleId();

    try {
      String sSelCat = iwc.getParameter(SELECTED_CATEGORY);
      if (sSelCat != null){
        _selectedCategory = Integer.parseInt(sSelCat);
        _productCategory = ((com.idega.block.trade.stockroom.data.ProductCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(ProductCategory.class)).findByPrimaryKeyLegacy(_selectedCategory);
      }
      _categories = getProductBusiness(iwc).getProductCategories();
    }catch (Exception e) {
      e.printStackTrace(System.err);
      _categories = new Vector();
    }

  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  private void viewCategory(IWContext iwc) throws RemoteException {
    try {
      List products = getProductBusiness(iwc).getProducts(_productCategory);
      List allProducts = getProductBusiness(iwc).getProducts();
      allProducts.removeAll(products);

      /*ProductComparator compare = new ProductComparator();
      compare.sortBy(compare.NAME);

      Collections.sort(allProducts, compare);
      Collections.sort(products, compare);*/

      SelectionDoubleBox sdb = new SelectionDoubleBox(this._parameterProductOut, this._parameterProductIn);

      Product product;
      Iterator iter = allProducts.iterator();
      while (iter.hasNext()) {
        product = (Product) iter.next();
        sdb.getLeftBox().addMenuElement(((Integer) product.getPrimaryKey()).intValue(), product.getProductName(localeId));
      }
      iter = products.iterator();
      while (iter.hasNext()) {
        product = (Product) iter.next();
        sdb.getRightBox().addMenuElement(((Integer) product.getPrimaryKey()).intValue(), product.getProductName(localeId));
      }
      //sdb.getLeftBox().addMenuElements(allProducts);
      //sdb.getRightBox().addMenuElements(products);

      sdb.getRightBox().selectAllOnSubmit();
      sdb.getLeftBox().setHeight(height);
      sdb.getRightBox().setHeight(height);
      sdb.getLeftBox().setStyleAttribute(IWConstants.BUILDER_FONT_STYLE_INTERFACE+"width:130px");
      sdb.getRightBox().setStyleAttribute(IWConstants.BUILDER_FONT_STYLE_INTERFACE+"width:130px");

      SubmitButton save = new SubmitButton(iwrb.getLocalizedImageButton("save","Save"), this._action, this._parameterSaveCategory);
      SubmitButton close = new SubmitButton(iwrb.getLocalizedImageButton("close","Close"), this._action, this._parameterClose);

      Table table = new Table();

      this.addHiddenInput(new HiddenInput(this.SELECTED_CATEGORY, Integer.toString(this._selectedCategory)));
      super.maintainClearCacheKeyInForm(iwc);

      sdb.addToScripts(this.getParentPage().getAssociatedScript());

      super.addLeft(iwrb.getLocalizedString("available_products","Available products"), sdb.getLeftBox(), true);
      super.addLeft(iwrb.getLocalizedString("add_selected","Add selected")+Text.NON_BREAKING_SPACE+Text.NON_BREAKING_SPACE, sdb.getRightButton(), false);
      super.addLeft(iwrb.getLocalizedString("remove_selected","Remove selected")+Text.NON_BREAKING_SPACE+Text.NON_BREAKING_SPACE, sdb.getLeftButton(), false);
      super.addLeft(iwrb.getLocalizedString("selected_products","Selected products"), sdb.getRightBox(), true);

      super.addSubmitButton(close);
      super.addSubmitButton(save);

    }catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  private void saveAssignment(IWContext iwc) throws RemoteException {
    String[] in = iwc.getParameterValues(this._parameterProductIn);

    Product product;
    TransactionManager tm = IdegaTransactionManager.getInstance();
    try {
      tm.begin();
      List products = EntityFinder.getInstance().findRelated(_productCategory, Product.class);
      _productCategory.removeProducts(products);

      if (in != null) {
        for (int i = 0; i < in.length; i++) {
          _productCategory.addTo(Product.class, Integer.parseInt(in[i]));
        }
      }

      tm.commit();
      iwc.getApplication().getIWCacheManager().invalidateCache(ProductCatalog.CACHE_KEY);
    }catch (Exception e) {
      e.printStackTrace(System.err);
      try {
        tm.rollback();
      }catch (SystemException se) {
        se.printStackTrace(System.err);
      }
    }
    super.clearCache(iwc);
    viewCategory(iwc);
  }

  private Text getText(String content) {
    Text text = new Text(content);

    return text;
  }

  private ProductBusiness getProductBusiness(IWApplicationContext iwac) throws RemoteException{
    return (ProductBusiness) IBOLookup.getServiceInstance(iwac, ProductBusiness.class);
  }

}
