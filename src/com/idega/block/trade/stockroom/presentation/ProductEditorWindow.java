package com.idega.block.trade.stockroom.presentation;

import com.idega.block.media.presentation.ImageInserter;
import com.idega.block.trade.stockroom.business.*;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.idegaweb.*;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class ProductEditorWindow extends IWAdminWindow {
  public static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.trade";
  public static final String PRODUCT_ID = "prod_edit_prod_id";

  private static final String ACTION = "prod_edit_action";
  private static final String PAR_SAVE = "prod_edit_save";
  private static final String PAR_NUMBER = "prod_edit_number";
  private static final String PAR_NAME = "prod_edit_name";
  private static final String PAR_DESCRIPTION = "prod_edit_description";
  private static final String PAR_PRICE = "prod_edit_price";
  private static final String PAR_IMAGE = "prod_edit_image";

  private IWResourceBundle iwrb;
  private IWBundle bundle;

  private Product _product = null;
  private int _productId = -1;

  public ProductEditorWindow() {
    setUnMerged();
    setWidth(500);
    setTitle("Product Editor");
  }


  public void main(IWContext iwc) {
    init(iwc);

    String action = iwc.getParameter(ACTION);
    if (action == null) {
      displayForm(iwc);
    }else if (action.equals(this.PAR_SAVE)) {
      saveProduct(iwc);
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
      _productId = Integer.parseInt(sProductId);
      if (_productId != -1) {
        _product = ProductBusiness.getProduct(_productId);
      }
    }catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  public static Link getEditorLink(int productId) {
    Link link = new Link();
      link.setWindowToOpen(ProductEditorWindow.class);
      link.addParameter(ProductEditor.PRODUCT_ID, productId);
    return link;
  }

  private void displayForm(IWContext iwc) {
    simpleForm(iwc);

  }

  private void simpleForm(IWContext iwc) {
    TextInput number = new TextInput(PAR_NUMBER);
    TextInput name = new TextInput(PAR_NAME);
    TextArea description = new TextArea(PAR_DESCRIPTION);
    TextInput price = new TextInput(PAR_PRICE);
    ImageInserter imageInserter = new ImageInserter();

    if (_product != null) {
      number.setContent(_product.getNumber());
      name.setContent(ProductBusiness.getProductName(_product));
      description.setContent(ProductBusiness.getProductDescription(_product));
      int imageId = _product.getFileId();
      if (imageId != -1) {
        imageInserter = new ImageInserter(imageId);
      }
    }
    imageInserter.setHasUseBox(false);


    super.addHiddenInput(new HiddenInput(this.PRODUCT_ID, Integer.toString(_productId)));

    super.addLeft(iwrb.getLocalizedString("item_number","Item number"), number, true);
    super.addLeft(iwrb.getLocalizedString("name","Name"), name, true);
    super.addLeft(iwrb.getLocalizedString("description","Description"), description, true);
    super.addLeft(iwrb.getLocalizedString("price","Price"), price, true);


    super.addRight(iwrb.getLocalizedString("image","Image"), imageInserter, false);

    SubmitButton saveBtn = new SubmitButton(iwrb.getLocalizedImageButton("save","Save"), this.ACTION, this.PAR_SAVE);

    super.addSubmitButton(saveBtn);
  }

  private void saveProduct(IWContext iwc) {
    String number = iwc.getParameter(PAR_NUMBER);
    String name = iwc.getParameter(PAR_NAME);
    String description = iwc.getParameter(PAR_DESCRIPTION);
    String price = iwc.getParameter(PAR_PRICE);
    String image = iwc.getParameter(PAR_IMAGE);

    if (_product == null) {
      add(" NEW : ");
    }else {
      add(" EDIT : ");
    }
    add(name);
  }

  private Text getText(String content) {
    Text text = new Text(content);

    return text;
  }
 }