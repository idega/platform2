/*
 * $Id: ReferenceNumber.java,v 1.14 2002/02/27 18:45:51 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.application.presentation;

import com.idega.block.application.business.ReferenceNumberHandler;
import com.idega.presentation.Block;
import com.idega.presentation.Table;
import com.idega.presentation.Image;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.Parameter;
import com.idega.presentation.ui.HelpButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.text.Text;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWBundle;

/**
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class ReferenceNumber extends Block {
  public static final String CAM_REF_NUMBER = "cam_ref_number";
  public static final int LAYOUT_VERTICAL = 1;
  public static final int LAYOUT_HORIZONTAL = 2;
  public static final int LAYOUT_STACKED = 3;

  private int referenceTextSize_ = -1;
  private int inputLength_ = 10;
  private int layout_ = -1;
  private int pageId;

  private String backgroundImageUrl_ = "";
  private String referenceWidth_ = "";
  private String referenceHeight_ = "";
  private String referenceText_;
  private String color_ = "";
  private String referenceTextColor_;
  private String styleAttribute_ = "font-size: 10pt";
  private String textStyles = "font-family: Arial,Helvetica,sans-serif; font-size: 8pt; font-weight: bold; color: #000000; text-decoration: none;";
  private String submitButtonAlignment_;
  private boolean hasHelpButton = false;

  private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.application";

  private Table outerTable_;

  private Form myForm_;

  private Image referenceImage_;

  protected IWResourceBundle iwrb_;
  protected IWBundle iwb_;

  public ReferenceNumber() {
    super();
    setDefaultValues();
  }

  public void main(IWContext iwc) throws Exception {
    iwb_ = getBundle(iwc);
    iwrb_ = getResourceBundle(iwc);

    referenceImage_ = iwrb_.getLocalizedImageButton("get","Get");

    referenceText_ = iwrb_.getLocalizedString("referenceNumber","Tilvísunarnr.");
    setup();

    outerTable_.add(myForm_);
    add(outerTable_);
  }

  private void setup() {
    Table referenceTable = new Table(1,2);
    referenceTable.setBorder(0);
    referenceTable.setWidth(referenceWidth_);
    referenceTable.setHeight(referenceHeight_);
    if (!color_.equals("")) {
      referenceTable.setColor(color_);
    }
    referenceTable.setCellpadding(0);
    referenceTable.setCellspacing(0);
    referenceTable.setBackgroundImage(new Image(backgroundImageUrl_));

    HelpButton helpButton = new HelpButton(iwrb_.getLocalizedString("help_headline","Reference number"),iwrb_.getLocalizedString("help","Help"));

    Text referenceTexti = new Text(referenceText_);
    if (referenceTextSize_ != -1) {
      referenceTexti.setFontSize(referenceTextSize_);
    }

    if (referenceTextColor_ != null) {
      referenceTexti.setFontColor(referenceTextColor_);
    }

    referenceTexti.setFontStyle(textStyles);

    Table inputTable;

    TextInput reference = new TextInput(CAM_REF_NUMBER);
    reference.setAttribute("style",styleAttribute_);
    reference.setSize(inputLength_);

    switch (layout_) {
      case LAYOUT_HORIZONTAL:
        inputTable = new Table(3,2);
        inputTable.setBorder(0);
        if (!(color_.equals(""))) {
          inputTable.setColor(color_);
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
        if (!(color_.equals(""))) {
          inputTable.setColor(color_);
        }
        inputTable.setCellpadding(0);
        inputTable.setCellspacing(0);
        inputTable.setAlignment("center");
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
        inputTable.setAlignment("center");
        inputTable.addText("",1,2);
        inputTable.setHeight(1,"2");
        if (!(color_.equals(""))) {
          inputTable.setColor(color_);
        }
        inputTable.setAlignment(1,1,"left");
        inputTable.setAlignment(1,2,"left");

        inputTable.add(referenceTexti,1,1);
        inputTable.add(reference,1,2);

        referenceTable.add(inputTable,1,1);
        break;
    }

    Table submitTable = new Table(1,1);
    if (hasHelpButton) {
      submitTable = new Table(2,1);
    }
    submitTable.setBorder(0);
    if (!color_.equals("")) {
      submitTable.setColor(color_);
    }
    submitTable.setRowVerticalAlignment(1,"middle");
    if (!hasHelpButton) {
      submitTable.setAlignment(1,1,submitButtonAlignment_);
    }
    else {
      submitTable.setAlignment(2,1,"right");
    }
    submitTable.setWidth("100%");

    if (!hasHelpButton) {
      submitTable.add(new SubmitButton(referenceImage_,"tengja"),1,1);
    }
    else {
      submitTable.add(new SubmitButton(referenceImage_,"tengja"),2,1);
      submitTable.add(helpButton,1,1);
    }

    referenceTable.add(submitTable,1,2);
    myForm_.add(referenceTable);
    if(pageId > 0){
      myForm_.setPageToSubmitTo(pageId);
    }
  }

  public String getBundleIdentifier() {
    return(IW_BUNDLE_IDENTIFIER);
  }

  public void setHelpButton(boolean usehelp){
    hasHelpButton = usehelp;
  }

  public void addHelpButton() {
    hasHelpButton = true;
  }

  public void setLayout(int layout) {
    layout_ = layout;
  }

  private void setDefaultValues() {
    referenceWidth_ = "148";
    referenceHeight_ = "89";
    submitButtonAlignment_ = "center";
    layout_ = LAYOUT_VERTICAL;

    outerTable_ = new Table();
    outerTable_.setCellpadding(0);
    outerTable_.setCellspacing(0);
    outerTable_.setAlignment("left");

    myForm_ = new Form();
    myForm_.setEventListener(ReferenceNumberHandler.class.getName());
    myForm_.add(new HiddenInput("cam_fact_view","50"));
    myForm_.setMethod("post");
    //myForm_.maintainAllParameters();
  }

  public void setVertical() {
    layout_ = LAYOUT_VERTICAL;
  }

  public void setHorizontal() {
    layout_ = LAYOUT_HORIZONTAL;
  }

  public void setStacked() {
    layout_ = LAYOUT_STACKED;
  }

  public void setStyle(String styleAttribute) {
    styleAttribute_ = styleAttribute;
  }

  public void setInputLength(int inputLength) {
    inputLength_ = inputLength;
  }

  public void setReferenceTextSize(int size) {
    referenceTextSize_ = size;
  }

  public void setReferenceTextColor(String color) {
    referenceTextColor_ = color;
  }

  public void setColor(String color) {
    color_ = color;
  }

  public void setHeight(String height) {
    referenceHeight_ = height;
  }

  public void setWidth(String width) {
    referenceWidth_ = width;
  }

  public void setBackgroundImageUrl(String url) {
    backgroundImageUrl_ = url;
  }

  public void setSubmitButtonAlignment(String alignment) {
    submitButtonAlignment_ = alignment;
  }

  public void setTextStyle(String styleAttribute){
    this.textStyles=styleAttribute;
  }

  public void setPage(com.idega.builder.data.IBPage page){
    this.pageId = page.getID();
  }

  public synchronized Object clone() {
    ReferenceNumber obj = null;
    try {
      obj = (ReferenceNumber)super.clone();
      if(this.outerTable_!=null)
        obj.outerTable_ = (Table)this.outerTable_.clone();
      if(this.referenceImage_!=null)
        obj.referenceImage_ = (Image)this.referenceImage_.clone();
      if(this.myForm_!=null)
        obj.myForm_ = (Form)this.myForm_.clone();


    }
    catch(Exception ex) {
      ex.printStackTrace(System.err);
    }
    return obj;
  }


}