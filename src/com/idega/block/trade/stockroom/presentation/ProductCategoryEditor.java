package com.idega.block.trade.stockroom.presentation;

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

public class ProductCategoryEditor extends IWAdminWindow {
  private IWBundle bundle = null;
  private IWResourceBundle iwrb = null;
  public static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.trade";
  public static final String SELECTED_CATEGORY = "pr_cat_seleted_cat";

  private static final String _action = "pr_cat_editor_action";
  private static final String _parameterSaveCategory = "pr_cat_view_cat";
  private static final String _parameterProductIn = "pr_cat_pr_in";
  private static final String _parameterProductOut = "pr_cat_pr_out";

  private static final String _parameterSelectedCategory = SELECTED_CATEGORY;

  int _selectedCategory = -1;
  ProductCategory _productCategory = null;
  List _categories = null;

  public ProductCategoryEditor() {
    super.setWidth(800);
    super.setHeight(400);
  }

  public void main(IWContext iwc) {
    init(iwc);

    if ( _selectedCategory != -1) {
      String action = iwc.getParameter(_action);
      if (action == null) {
        viewCategory(iwc);
      }else {
        saveAssignment(iwc);
      }
    }
  }

  private void init(IWContext iwc) {
    bundle = getBundle(iwc);
    iwrb = bundle.getResourceBundle(iwc);

    try {
      String sSelCat = iwc.getParameter(SELECTED_CATEGORY);
      if (sSelCat != null){
        _selectedCategory = Integer.parseInt(sSelCat);
        _productCategory = new ProductCategory(_selectedCategory);
      }
      _categories = ProductBusiness.getProductCategories();
    }catch (Exception e) {
      e.printStackTrace(System.err);
      _categories = new Vector();
    }
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  private void viewCategory(IWContext iwc) {
    try {
      List products = ProductBusiness.getProducts(_productCategory);//EntityFinder.getInstance().findRelated(_productCategory, Product.class);
      List allProducts = ProductBusiness.getProducts();
      allProducts.removeAll(products);

      SelectionDoubleBox sdb = new SelectionDoubleBox(this._parameterProductOut, this._parameterProductIn);
        sdb.getLeftBox().addMenuElements(allProducts);
        sdb.getRightBox().addMenuElements(products);
        sdb.getRightBox().selectAllOnSubmit();

      SubmitButton save = new SubmitButton(iwrb.getLocalizedImageButton("save","Save"), this._action, this._parameterSaveCategory);

      Form form = new Form();
      Table table = new Table();
        form.add(table);
        form.maintainParameter(this.SELECTED_CATEGORY);

      table.add(sdb,1, 1);
      table.add(save,1, 2);
      table.setAlignment(1, 2, "right");

      add(form);
    }catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  private void saveAssignment(IWContext iwc) {
    String[] in = iwc.getParameterValues(this._parameterProductIn);

    Product product;
    TransactionManager tm = IdegaTransactionManager.getInstance();
    try {
      tm.begin();
      List products = EntityFinder.getInstance().findRelated(_productCategory, Product.class);
      _productCategory.removeFrom((Product[]) products.toArray(new Product[]{}));


      if (in != null) {
        for (int i = 0; i < in.length; i++) {
          _productCategory.addTo(Product.class, Integer.parseInt(in[i]));
        }
      }

      tm.commit();
    }catch (Exception e) {
      e.printStackTrace(System.err);
      try {
        tm.rollback();
      }catch (SystemException se) {
        se.printStackTrace(System.err);
      }
    }
    viewCategory(iwc);
  }

  private Text getText(String content) {
    Text text = new Text(content);

    return text;
  }
}