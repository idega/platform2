/*
 * $Id: ReferenceNumber.java,v 1.23 2004/06/21 16:48:39 aron Exp $
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

  private int inputLength = 10;
  private int layout = -1;
  private int pageId;
  private int referenceTextSize;

  private String backgroundImageUrl = null;
  private String referenceWidth = "";
  private String referenceHeight = "";
  private String referenceText;
  private String colour = "";
  private String referenceTextColour;
  private String styleAttribute = "font-size: 10pt";
  private String textStyles = "font-family: Arial,Helvetica,sans-serif; font-size: 8pt; font-weight: bold; color: #000000; text-decoration: none;";
  private String submitButtonAlignment;
  private boolean hasHelpButton = false;

  private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.application";

  private Table outerTable;

  private Form myForm;

  private Image referenceImage;

  protected IWResourceBundle iwrb;
  protected IWBundle iwb;

  public ReferenceNumber() {
    super();
    setDefaultValues();
  }

  public void main(IWContext iwc) throws Exception {
    iwb = getBundle(iwc);
    iwrb = getResourceBundle(iwc);

    referenceImage = iwrb.getLocalizedImageButton("get","Get");

    referenceText = iwrb.getLocalizedString("referenceNumber","Tilvísunarnr.");
    setup();

    outerTable.add(myForm);
    add(outerTable);
  }

  private void setup() {
    Table referenceTable = new Table(1,2);
    referenceTable.setBorder(0);
    referenceTable.setWidth(referenceWidth);
    referenceTable.setHeight(referenceHeight);
    if (!colour.equals("")) {
      referenceTable.setColor(colour);
    }
    referenceTable.setCellpadding(0);
    referenceTable.setCellspacing(0);
    if(!"".equals(backgroundImageUrl))
    referenceTable.setBackgroundImage(new Image(backgroundImageUrl));

    HelpButton helpButton = new HelpButton(iwrb.getLocalizedString("help_headline","Reference number"),iwrb.getLocalizedString("help","Help"));

    Text referenceTexti = new Text(referenceText);
    if (referenceTextSize != -1) {
      referenceTexti.setFontSize(referenceTextSize);
    }

    if (referenceTextColour != null) {
      referenceTexti.setFontColor(referenceTextColour);
    }

    referenceTexti.setFontStyle(textStyles);

    Table inputTable;

    TextInput reference = new TextInput(CAM_REF_NUMBER);
    reference.setMarkupAttribute("style",styleAttribute);
    reference.setSize(inputLength);

    switch (layout) {
      case LAYOUT_HORIZONTAL:
        inputTable = new Table(3,2);
        inputTable.setBorder(0);
        if (!(colour.equals(""))) {
          inputTable.setColor(colour);
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
        if (!(colour.equals(""))) {
          inputTable.setColor(colour);
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
        if (!(colour.equals(""))) {
          inputTable.setColor(colour);
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
    if (hasHelpButton) {
      submitTable = new Table(2,1);
    }
    submitTable.setBorder(0);
    if (!colour.equals("")) {
      submitTable.setColor(colour);
    }
    submitTable.setRowVerticalAlignment(1,"middle");
    if (!hasHelpButton) {
      submitTable.setAlignment(1,1,submitButtonAlignment);
    }
    else {
      submitTable.setAlignment(2,1,"right");
    }
    submitTable.setWidth("100%");

    if (!hasHelpButton) {
      submitTable.add(new SubmitButton(referenceImage,"tengja"),1,1);
    }
    else {
      submitTable.add(new SubmitButton(referenceImage,"tengja"),2,1);
      submitTable.add(helpButton,1,1);
    }

    referenceTable.add(submitTable,1,2);
    myForm.add(referenceTable);
    if(pageId > 0){
      myForm.setPageToSubmitTo(pageId);
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
    this.layout = layout;
  }

  private void setDefaultValues() {
    referenceWidth = "148";
    referenceHeight = "89";
    submitButtonAlignment = "center";
    layout = LAYOUT_VERTICAL;

    outerTable = new Table();
    outerTable.setCellpadding(0);
    outerTable.setCellspacing(0);

    myForm = new Form();
    //myForm.setEventListener(ReferenceNumberHandler.class.getName());
    myForm.add(new HiddenInput("cam_fact_view","50"));
    myForm.setMethod("post");
    //_myForm.maintainAllParameters();
  }

  public void setVertical() {
    layout = LAYOUT_VERTICAL;
  }

  public void setHorizontal() {
    layout = LAYOUT_HORIZONTAL;
  }

  public void setStacked() {
    layout = LAYOUT_STACKED;
  }

  public void setStyle(String styleAttribute) {
    this.styleAttribute = styleAttribute;
  }

  public void setInputLength(int inputLength) {
    this.inputLength = inputLength;
  }

  public void setReferenceTextSize(int size) {
    referenceTextSize = size;
  }

  public void setReferenceTextColor(String color) {
    referenceTextColour = color;
  }

  public void setColor(String color) {
    colour = color;
  }

  public void setHeight(String height) {
    referenceHeight = height;
  }

  public void setWidth(String width) {
    referenceWidth = width;
  }

  public void setBackgroundImageUrl(String url) {
    backgroundImageUrl = url;
  }

  public void setSubmitButtonAlignment(String alignment) {
    submitButtonAlignment = alignment;
  }

  public void setTextStyle(String styleAttribute){
    textStyles=styleAttribute;
  }

  public void setPage(com.idega.core.builder.data.ICPage page){
    pageId = page.getID();
  }

  public synchronized Object clone() {
    ReferenceNumber obj = null;
    try {
      obj = (ReferenceNumber)super.clone();
      if(outerTable!=null)
        obj.outerTable = (Table)outerTable.clone();
      if(referenceImage!=null)
        obj.referenceImage = (Image)referenceImage.clone();
      if(myForm!=null)
        obj.myForm = (Form)myForm.clone();


    }
    catch(Exception ex) {
      ex.printStackTrace(System.err);
    }
    return obj;
  }
}
