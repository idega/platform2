/*
 * $Id: ReferenceNumber.java,v 1.22 2004/06/04 17:39:59 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.application.presentation;

import com.idega.block.application.business.ReferenceNumberHandler;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HelpButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

/**
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class ReferenceNumber extends Block {
  public static final String CAM_REF_NUMBER = "cam_ref_number";
  public static final int LAYOUT_VERTICAL = 1;
  public static final int LAYOUT_HORIZONTAL = 2;
  public static final int LAYOUT_STACKED = 3;

  private int _inputLength = 10;
  private int _layout = -1;
  private int _pageId;
  private int _referenceTextSize;

  private String _backgroundImageUrl = null;
  private String _referenceWidth = "";
  private String _referenceHeight = "";
  private String _referenceText;
  private String _colour = "";
  private String _referenceTextColour;
  private String _styleAttribute = "font-size: 10pt";
  private String _textStyles = "font-family: Arial,Helvetica,sans-serif; font-size: 8pt; font-weight: bold; color: #000000; text-decoration: none;";
  private String _submitButtonAlignment;
  private boolean _hasHelpButton = false;

  private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.application";

  private Table _outerTable;

  private Form _myForm;

  private Image _referenceImage;

  protected IWResourceBundle _iwrb;
  protected IWBundle _iwb;

  public ReferenceNumber() {
    super();
    setDefaultValues();
  }

  public void main(IWContext iwc) throws Exception {
    _iwb = getBundle(iwc);
    _iwrb = getResourceBundle(iwc);

    _referenceImage = _iwrb.getLocalizedImageButton("get","Get");

    _referenceText = _iwrb.getLocalizedString("referenceNumber","Tilvísunarnr.");
    setup();

    _outerTable.add(_myForm);
    add(_outerTable);
  }

  private void setup() {
    Table referenceTable = new Table(1,2);
    referenceTable.setBorder(0);
    referenceTable.setWidth(_referenceWidth);
    referenceTable.setHeight(_referenceHeight);
    if (!_colour.equals("")) {
      referenceTable.setColor(_colour);
    }
    referenceTable.setCellpadding(0);
    referenceTable.setCellspacing(0);
    if(!"".equals(_backgroundImageUrl))
    referenceTable.setBackgroundImage(new Image(_backgroundImageUrl));

    HelpButton helpButton = new HelpButton(_iwrb.getLocalizedString("help_headline","Reference number"),_iwrb.getLocalizedString("help","Help"));

    Text referenceTexti = new Text(_referenceText);
    if (_referenceTextSize != -1) {
      referenceTexti.setFontSize(_referenceTextSize);
    }

    if (_referenceTextColour != null) {
      referenceTexti.setFontColor(_referenceTextColour);
    }

    referenceTexti.setFontStyle(_textStyles);

    Table inputTable;

    TextInput reference = new TextInput(CAM_REF_NUMBER);
    reference.setMarkupAttribute("style",_styleAttribute);
    reference.setSize(_inputLength);

    switch (_layout) {
      case LAYOUT_HORIZONTAL:
        inputTable = new Table(3,2);
        inputTable.setBorder(0);
        if (!(_colour.equals(""))) {
          inputTable.setColor(_colour);
        }
        inputTable.setCellpadding(0);
        inputTable.setCellspacing(0);
        inputTable.setAlignment(2,1,"right");
        inputTable.setAlignment(2,2,"right");
        inputTable.setWidth("100%");

        inputTable.add(referenceTexti,2,1);
        inputTable.add(reference,2,2);
        inputTable.setAlignment(2,1,"right");
        inputTable.setAlignment(2,2,"right");

        referenceTable.add(inputTable,1,1);
        break;

      case LAYOUT_VERTICAL:
        inputTable = new Table(3,3);
        inputTable.setBorder(0);
        if (!(_colour.equals(""))) {
          inputTable.setColor(_colour);
        }
        inputTable.setCellpadding(0);
        inputTable.setCellspacing(0);
        inputTable.mergeCells(1,2,3,2);
        inputTable.addText("",1,2);
        inputTable.setHeight(2,"10");
        inputTable.setAlignment(1,1,"right");
        inputTable.setAlignment(1,3,"right");

        inputTable.add(referenceTexti,1,1);
        inputTable.add(reference,3,1);

        referenceTable.add(inputTable,1,1);
        break;

      case LAYOUT_STACKED:
        inputTable = new Table(1,2);
        inputTable.setBorder(0);
        inputTable.setCellpadding(0);
        inputTable.setCellspacing(0);
        inputTable.addText("",1,2);
        inputTable.setHeight(1,"2");
        if (!(_colour.equals(""))) {
          inputTable.setColor(_colour);
        }
        inputTable.setAlignment(1,1,"left");
        inputTable.setAlignment(1,2,"left");

        inputTable.add(referenceTexti,1,1);
        inputTable.add(reference,1,2);

        referenceTable.setAlignment(1,1,"center");
        referenceTable.add(inputTable,1,1);
        break;
    }

    Table submitTable = new Table(1,1);
    if (_hasHelpButton) {
      submitTable = new Table(2,1);
    }
    submitTable.setBorder(0);
    if (!_colour.equals("")) {
      submitTable.setColor(_colour);
    }
    submitTable.setRowVerticalAlignment(1,"middle");
    if (!_hasHelpButton) {
      submitTable.setAlignment(1,1,_submitButtonAlignment);
    }
    else {
      submitTable.setAlignment(2,1,"right");
    }
    submitTable.setWidth("100%");

    if (!_hasHelpButton) {
      submitTable.add(new SubmitButton(_referenceImage,"tengja"),1,1);
    }
    else {
      submitTable.add(new SubmitButton(_referenceImage,"tengja"),2,1);
      submitTable.add(helpButton,1,1);
    }

    referenceTable.add(submitTable,1,2);
    _myForm.add(referenceTable);
    if(_pageId > 0){
      _myForm.setPageToSubmitTo(_pageId);
    }
  }

  public String getBundleIdentifier() {
    return(IW_BUNDLE_IDENTIFIER);
  }

  public void setHelpButton(boolean usehelp){
    _hasHelpButton = usehelp;
  }

  public void addHelpButton() {
    _hasHelpButton = true;
  }

  public void setLayout(int layout) {
    _layout = layout;
  }

  private void setDefaultValues() {
    _referenceWidth = "148";
    _referenceHeight = "89";
    _submitButtonAlignment = "center";
    _layout = LAYOUT_VERTICAL;

    _outerTable = new Table();
    _outerTable.setCellpadding(0);
    _outerTable.setCellspacing(0);

    _myForm = new Form();
    _myForm.setEventListener(ReferenceNumberHandler.class.getName());
    _myForm.add(new HiddenInput("cam_fact_view","50"));
    _myForm.setMethod("post");
    //_myForm.maintainAllParameters();
  }

  public void setVertical() {
    _layout = LAYOUT_VERTICAL;
  }

  public void setHorizontal() {
    _layout = LAYOUT_HORIZONTAL;
  }

  public void setStacked() {
    _layout = LAYOUT_STACKED;
  }

  public void setStyle(String styleAttribute) {
    _styleAttribute = styleAttribute;
  }

  public void setInputLength(int inputLength) {
    _inputLength = inputLength;
  }

  public void setReferenceTextSize(int size) {
    _referenceTextSize = size;
  }

  public void setReferenceTextColor(String color) {
    _referenceTextColour = color;
  }

  public void setColor(String color) {
    _colour = color;
  }

  public void setHeight(String height) {
    _referenceHeight = height;
  }

  public void setWidth(String width) {
    _referenceWidth = width;
  }

  public void setBackgroundImageUrl(String url) {
    _backgroundImageUrl = url;
  }

  public void setSubmitButtonAlignment(String alignment) {
    _submitButtonAlignment = alignment;
  }

  public void setTextStyle(String styleAttribute){
    _textStyles=styleAttribute;
  }

  public void setPage(com.idega.core.builder.data.ICPage page){
    _pageId = page.getID();
  }

  public synchronized Object clone() {
    ReferenceNumber obj = null;
    try {
      obj = (ReferenceNumber)super.clone();
      if(_outerTable!=null)
        obj._outerTable = (Table)_outerTable.clone();
      if(_referenceImage!=null)
        obj._referenceImage = (Image)_referenceImage.clone();
      if(_myForm!=null)
        obj._myForm = (Form)_myForm.clone();


    }
    catch(Exception ex) {
      ex.printStackTrace(System.err);
    }
    return obj;
  }
}
