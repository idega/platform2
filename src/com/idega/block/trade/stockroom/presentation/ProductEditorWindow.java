package com.idega.block.trade.stockroom.presentation;

import com.idega.block.category.business.CategoryFinder;
import com.idega.block.image.presentation.*;
import com.idega.block.trade.business.CurrencyHolder;
import java.util.*;
import com.idega.block.text.business.TextFormatter;
import com.idega.util.text.TextSoap;
import com.idega.core.localisation.presentation.ICLocalePresentation;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.block.trade.business.CurrencyBusiness;
import com.idega.util.idegaTimestamp;
import java.sql.SQLException;
import com.idega.block.media.presentation.ImageInserter;
import com.idega.block.trade.stockroom.business.*;
import com.idega.idegaweb.*;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.presentation.texteditor.TextEditor;

/** @todo Henda við tækifæri... */
import com.idega.block.trade.stockroom.data.*;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class ProductEditorWindow extends IWAdminWindow {
  private static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.trade";
  public static final  String imageAttributeKey = "productimage";
  public static final String PRODUCT_ID = "prod_edit_prod_id";
  public static final String PRODUCT_CATALOG_OBJECT_INSTANCE_ID = "prod_edit_prod_cat_inst_id";
//  public static final String PRODUCT_ID = ProductBusiness.PRODUCT_ID;

  private static final String ACTION = "prod_edit_action";
  private static final String PAR_ADD_FILE = "prod_edit_add_file";
  private static final String PAR_CATEGORY = "prod_edit_category";
  private static final String PAR_CLOSE = "prod_edit_close";
  private static final String PAR_CURRENCY = "prod_edit_currency";
  private static final String PAR_DELETE = "prod_edit_del";
  private static final String PAR_DEL_VERIFIED = "prod_edit_del_verified";
  private static final String PAR_DEL_FILE = "prod_edit_del_file";
  private static final String PAR_DESCRIPTION = "prod_edit_description";
  private static final String PAR_IMAGE = "prod_edit_image";
  private static final String PAR_IMAGE_THUMBNAIL = "prod_edit_image_thumb";
  private static final String PAR_NAME = "prod_edit_name";
  private static final String PAR_NEW = "prod_edit_new";
  private static final String PAR_NUMBER = "prod_edit_number";
  private static final String PAR_PRICE = "prod_edit_price";
  private static final String PAR_SAVE = "prod_edit_save";
  private static final String PAR_TEASER = "prod_edit_teaser";

  private IWResourceBundle iwrb;
  private IWBundle bundle;
  private IWBundle core;
  private ProductEditorBusiness _business = ProductEditorBusiness.getInstance();

  private Product _product = null;
  private DropdownMenu _currencies = null;
  private int _productId = -1;
  private int iLocaleID = -1;
  private int _catalogICObjectInstanceId = -1;

  public ProductEditorWindow() {
    setUnMerged();
    setWidth(600);
    setHeight(650);
    setResizable(true);
    setTitle("Product Editor");
  }


  public void main(IWContext iwc) {
    init(iwc);

    String action = iwc.getParameter(ACTION);

    if (action == null || action.equals("")) {
      String delImg = iwc.getParameter(PAR_DEL_FILE);
      if (delImg != null && !delImg.equals("")) {
        try {
          _business.removeImage(_product, Integer.parseInt(delImg));
        }catch (NumberFormatException nfe) {}
      }

      displayForm(iwc);
    }else if (action.equals(this.PAR_NEW)) {
      _product = null;
      _productId = -1;
      displayForm(iwc);
    }else if (action.equals(this.PAR_SAVE)) {
      if (saveProduct(iwc)) {
        displayForm(iwc);
      }else {

      }
    }else if (action.equals(this.PAR_DELETE)) {
      verifyDelete(iwc);
    }else if (action.equals(this.PAR_DEL_VERIFIED)) {
      if (_business.deleteProduct(_product)) {
        closeWindow();
      }
    }else if (action.equals(this.PAR_CLOSE)) {
      closeWindow();
    }else if (action.equals(this.PAR_ADD_FILE)) {
      String imageId = iwc.getParameter(PAR_IMAGE);
      if (imageId != null) {
        _business.addImage(_product, Integer.parseInt(imageId));
      }
      saveProduct(iwc);
      displayForm(iwc);
    }
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  private void init(IWContext iwc) {
    bundle = getBundle(iwc);
    iwrb = bundle.getResourceBundle(iwc);
    core = iwc.getApplication().getCoreBundle();

    addTitle(iwrb.getLocalizedString("product_editor","Product Editor"));
    try {
      String sProductId = iwc.getParameter(this.PRODUCT_ID);
      _productId = Integer.parseInt(sProductId);
      if (_productId != -1) {
        _product = ProductBusiness.getProduct(_productId);
      }
    }catch (Exception e) {
      //e.printStackTrace(System.err);
      _productId = -1;
      _product = null;
    }

    String pCatObjId = iwc.getParameter(this.PRODUCT_CATALOG_OBJECT_INSTANCE_ID);
    if (pCatObjId != null) {
      try {
        this._catalogICObjectInstanceId = Integer.parseInt(pCatObjId);
      }catch (NumberFormatException n) {n.printStackTrace(System.err);}
    }

    setCurrencies(iwc);
    String currCurr = getBundle(iwc).getProperty("iw_default_currency", "USD");
    _currencies = _business.getCurrencyDropdown(this.PAR_CURRENCY, currCurr);
  }

  private void setCurrencies(IWContext iwc) {
    Locale currentLocale = iwc.getCurrentLocale(),chosenLocale;
    String sLocaleId = iwc.getParameter(ProductBusiness.PARAMETER_LOCALE_DROP);
    iLocaleID = -1;
    if(sLocaleId!= null){
      iLocaleID = Integer.parseInt(sLocaleId);
      chosenLocale = ICLocaleBusiness.getLocale(iLocaleID);
    }
    else{
      chosenLocale = currentLocale;
      iLocaleID = ICLocaleBusiness.getLocaleId(chosenLocale);
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
    //TextArea description = new TextArea(PAR_DESCRIPTION);
    TextEditor description = new TextEditor();
    description.setInputName(PAR_DESCRIPTION);

    TextArea teaser = new TextArea(PAR_TEASER);
    TextInput price = new TextInput(PAR_PRICE);
      name.setSize(67);
      //description.setWidth(70);
      //description.setHeight(15);
      teaser.setWidth(70);
      teaser.setHeight(4);

    if (_product != null) {
      number.setContent(_product.getNumber());
      name.setContent(ProductBusiness.getProductName(_product, iLocaleID));
      description.setContent(ProductBusiness.getProductDescription(_product, iLocaleID));
      teaser.setContent(ProductBusiness.getProductTeaser(_product, iLocaleID));
      ProductPrice pPrice = StockroomBusiness.getPrice(_product);
      if (pPrice != null) {
        price.setContent(Integer.toString((int) pPrice.getPrice()));
        _currencies.setSelectedElement(Integer.toString(pPrice.getCurrencyId()));
      }else {
        price.setContent("0");
      }
    }

    DropdownMenu localeDrop = ICLocalePresentation.getLocaleDropdownIdKeyed(ProductBusiness.PARAMETER_LOCALE_DROP);
      localeDrop.setToSubmit();
      localeDrop.setSelectedElement(Integer.toString(iLocaleID));
    super.addLeft(iwrb.getLocalizedString("locale","Locale")+": ",localeDrop,false);

    super.addHiddenInput(new HiddenInput(this.PRODUCT_ID, Integer.toString(_productId)));

//    super.addLeft(iwrb.getLocalizedString("item_number","Item number"), number, true);

    Table topTable = new Table(5, 2);
      topTable.setCellpaddingAndCellspacing(0);
        super.setStyle(price);
        super.setStyle(_currencies);
        super.setStyle(number);

    topTable.add(super.formatText(iwrb.getLocalizedString("item_number","Item number")), 1, 1);
    topTable.add(super.formatText(Text.NON_BREAKING_SPACE), 2, 1);
    topTable.add(number, 1, 2);
    topTable.add(super.formatText(iwrb.getLocalizedString("price","Price")), 3, 1);
    topTable.add(price, 3, 2);
    topTable.add(super.formatText(Text.NON_BREAKING_SPACE), 4, 1);
    topTable.add(super.formatText(iwrb.getLocalizedString("currency","Currency")), 5, 1);
    topTable.add(_currencies, 5, 2);

    super.addLeft(topTable, false);


    super.addLeft(iwrb.getLocalizedString("name","Name"), name, true);
    super.addLeft(iwrb.getLocalizedString("teaser","Teaser"), teaser, true);
    super.addLeft(iwrb.getLocalizedString("description","Description"), description, true);

    Table imageTable = getImageTable(iwc);
    super.addRight(iwrb.getLocalizedString("images","Images"),imageTable,true,false);


    SubmitButton saveBtn = new SubmitButton(iwrb.getLocalizedImageButton("save","Save"), this.ACTION, this.PAR_SAVE);
    SubmitButton deleteBtn = new SubmitButton(iwrb.getLocalizedImageButton("delete","Delete"), this.ACTION, this.PAR_DELETE);
    SubmitButton closeBtn = new SubmitButton(iwrb.getLocalizedImageButton("close","Close"), this.ACTION, this.PAR_CLOSE);
    SubmitButton newBtn = new SubmitButton(iwrb.getLocalizedImageButton("create_new","Create new"), this.ACTION, this.PAR_NEW);


    if (this._catalogICObjectInstanceId != -1) {
      super.addHiddenInput(new HiddenInput(this.PRODUCT_CATALOG_OBJECT_INSTANCE_ID, Integer.toString(_catalogICObjectInstanceId)));
      SelectionBox catSel = _business.getCategorySelectionBox(_product, PAR_CATEGORY, _catalogICObjectInstanceId);
      super.addLeft(iwrb.getLocalizedString("product_category","Product category"), catSel, true);
    }



    Table table = new Table();
    table.add(newBtn);
    table.setAlignment(1,1, "right");
    table.setWidth("100%");

    super.addLeft("",table, false, false);

    super.addSubmitButton(closeBtn);
    super.addSubmitButton(deleteBtn);
    super.addSubmitButton(saveBtn);
  }

  private boolean saveProduct(IWContext iwc) {
    String number = iwc.getParameter(PAR_NUMBER);
    String name = iwc.getParameter(PAR_NAME);
    String description = iwc.getParameter(PAR_DESCRIPTION);
    String teaser = iwc.getParameter(PAR_TEASER);
    String price = iwc.getParameter(PAR_PRICE);
    String currency = iwc.getParameter(PAR_CURRENCY);
    String thumbnailId = iwc.getParameter(PAR_IMAGE_THUMBNAIL);
    if (thumbnailId == null) thumbnailId = "-1";
    String[] categories = (String[]) iwc.getParameterValues(PAR_CATEGORY);

    boolean returner = false;

    if (!name.equals("")) {
      if (_product == null) {
        try {
          _productId = ProductBusiness.createProduct(null, name, number, description, true, iLocaleID);
          _product = ProductBusiness.getProduct(_productId);
          _business.setCategories(_product, categories);
          ProductBusiness.setProductTeaser(_product, iLocaleID, teaser);
          if (_business.setPrice(_product, price, currency)) {
          }else {
            super.addLeft(iwrb.getLocalizedString("price_not_saved","Price was not saved"));
          }
          returner = true;
        }catch (Exception e) {
          returner = false;
          e.printStackTrace(System.err);
        }
      }else {
        try {
          _product = ProductBusiness.getProduct(ProductBusiness.updateProduct(this._productId, null, name, number, description, true, iLocaleID));

          _business.setThumbnail(_product, Integer.parseInt(thumbnailId));
          _business.setCategories(_product, categories);
          ProductBusiness.setProductTeaser(_product, iLocaleID, teaser);
          if (_business.setPrice(_product, price, currency)) {
          }else {
            super.addLeft(iwrb.getLocalizedString("price_not_saved","Price was not saved"));
          }
          returner = true;
        }catch (Exception e) {
          returner = false;
          e.printStackTrace(System.err);
        }
      }
    }

    return returner;
  }

  private void saveFailed(IWContext iwc) {
    super.addLeft(iwrb.getLocalizedString("save_failed","Save failed"), "" );

    BackButton back = new BackButton(iwrb.getLocalizedImageButton("back","Back"));

    super.addSubmitButton(back);
  }

  private void verifyDelete(IWContext iwc) {
    super.addHiddenInput(new HiddenInput(this.PRODUCT_ID, Integer.toString(_productId)));
    StringBuffer text = new StringBuffer();
      text.append(iwrb.getLocalizedString("are_you_sure_you_want_to_delete","Are you sure you want to delete this product")).append(" : ").append(ProductBusiness.getProductName(_product));
    super.addLeft(iwrb.getLocalizedString("delete","Delete"), text.toString() );

    SubmitButton ok = new SubmitButton(iwrb.getLocalizedImageButton("ok","OK"), this.ACTION, this.PAR_DEL_VERIFIED);
    SubmitButton cancel = new SubmitButton(iwrb.getLocalizedImageButton("cancel","Cancel"), this.ACTION, "");

    super.addSubmitButton(ok);
    super.addSubmitButton(cancel);
  }

  private void closeWindow() {
    this.setParentToReload();
    this.close();
  }

  private Table getImageTable(IWContext iwc) {
    ImageInserter imageInserter = new ImageInserter(PAR_IMAGE);
    imageInserter.setHasUseBox(false);
    SubmitButton addButton = new SubmitButton(core.getImage("/shared/create.gif","Add to news"),ACTION, PAR_ADD_FILE);

    Table imageTable = new Table();
    int imgRow = 1;
      imageTable.mergeCells(1,imgRow,4,imgRow);
      imageTable.add(imageInserter,1,imgRow++);
      imageTable.mergeCells(1,imgRow,4,imgRow);
      imageTable.add(addButton,1,imgRow++);

    List files = _business.getFiles(_product);

    if(files != null && files.size() > 0){
      RadioButton radio;
      int imageId = _product.getFileId();

      Iterator I = files.iterator();

      ++imgRow;
      imageTable.add(formatText(iwrb.getLocalizedString("thumbnail","Thumb")), 1, imgRow);
      imageTable.add(formatText(iwrb.getLocalizedString("image","Image")), 2, imgRow);
      ++imgRow;

      while(I.hasNext()){
        try {
          Image immi = _business.getImage(I.next());
          int immiId = immi.getImageID(iwc);
          immi.setMaxImageWidth(50);

          Link edit = ImageAttributeSetter.getLink(core.getImage("/shared/edit.gif"),immiId,imageAttributeKey);
          SubmitButton delete = new SubmitButton(core.getImage("/shared/delete.gif"), PAR_DEL_FILE, Integer.toString(immiId));
          radio = new RadioButton(PAR_IMAGE_THUMBNAIL, Integer.toString(immiId));
          if (imageId == immiId) {
            radio.setSelected();
          }

          imageTable.add(radio, 1 ,imgRow);
          imageTable.add(immi,2,imgRow);
          imageTable.add(edit,3,imgRow);
          imageTable.add(delete,4,imgRow);
          imgRow++;
        }
        catch (Exception ex) {

        }
      }
      radio = new RadioButton(PAR_IMAGE_THUMBNAIL, "-1");
      if (imageId == -1) {
        radio.setSelected();
      }
      imageTable.add(radio, 1,imgRow);
      imageTable.add(formatText(iwrb.getLocalizedString("no_thumbnail","No thumbnail")), 2, imgRow);
      imageTable.mergeCells(2, imgRow, 4, imgRow);
      ++imgRow;

    }

    return imageTable;
  }

}
