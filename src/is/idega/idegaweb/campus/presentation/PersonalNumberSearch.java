/*
 * $Id: PersonalNumberSearch.java,v 1.6 2004/06/11 17:26:50 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.presentation;

import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HelpButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

/**
 * @author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */
public class PersonalNumberSearch extends Block {
  public static final String PERSONAL_NUMBER = "cam_personal_number";
  public static final int LAYOUT_VERTICAL = 1;
  public static final int LAYOUT_HORIZONTAL = 2;
  public static final int LAYOUT_STACKED = 3;

  private int _inputLength = 10;
  private int _maxInputLength = -1;
  private int _layout = -1;
  private int _pageId;
  private int _numberTextSize;

  private String _backgroundImageUrl = "";
  private String _numberWidth = "";
  private String _numberHeight = "";
  private String _numberText;
  private String _colour = "";
  private String _numberTextColour;
  private String _styleAttribute = "font-size: 10pt";
  private String _textStyles = "font-family: Arial,Helvetica,sans-serif; font-size: 8pt; font-weight: bold; color: #000000; text-decoration: none;";
  private String _submitButtonAlignment;
  private boolean _hasHelpButton = false;


  private final static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.campus";

  private Table _outerTable;

  private Form _myForm;

  private Image _numberImage;

  protected IWResourceBundle _iwrb;
  protected IWBundle _iwb;

  public PersonalNumberSearch() {
    super();
    setDefaultValues();
  }

  public void main(IWContext iwc) throws Exception {
    _iwb = getBundle(iwc);
    _iwrb = getResourceBundle(iwc);

    _numberImage = _iwrb.getLocalizedImageButton("get","Get");

    _numberText = _iwrb.getLocalizedString("personal_number","Kennitala");
    setup();

    _outerTable.add(_myForm);
    add(_outerTable);
  }

  private void setup() {
    Table numberTable = new Table(1,2);
    numberTable.setBorder(0);
    numberTable.setWidth(_numberWidth);
    numberTable.setHeight(_numberHeight);
    if (!_colour.equals("")) {
      numberTable.setColor(_colour);
    }
    numberTable.setCellpadding(0);
    numberTable.setCellspacing(0);
    //if(!"".equals(_backgroundImageUrl))
    numberTable.setBackgroundImage(new Image(_backgroundImageUrl));

    HelpButton helpButton = new HelpButton(_iwrb.getLocalizedString("help_headline","Personal number"),_iwrb.getLocalizedString("personal_number_help","Help"));

    Text numberTexti = new Text(_numberText);
    if (_numberTextSize != -1) {
      numberTexti.setFontSize(_numberTextSize);
    }

    if (_numberTextColour != null) {
      numberTexti.setFontColor(_numberTextColour);
    }

    numberTexti.setFontStyle(_textStyles);

    Table inputTable;

    TextInput number = new TextInput(PERSONAL_NUMBER);
    number.setMarkupAttribute("style",_styleAttribute);
    number.setSize(_inputLength);
    if (_maxInputLength > 0)
      number.setMaxlength(_maxInputLength);

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

        inputTable.add(numberTexti,2,1);
        inputTable.add(number,2,2);
        inputTable.setAlignment(2,1,"right");
        inputTable.setAlignment(2,2,"right");

        numberTable.add(inputTable,1,1);
        break;

      case LAYOUT_VERTICAL:
        inputTable = new Table(3,3);
        inputTable.setBorder(0);
        if (!(_colour.equals(""))) {
          inputTable.setColor(_colour);
        }
        inputTable.setCellpadding(0);
        inputTable.setCellspacing(0);
        inputTable.setHorizontalAlignment("center");
        inputTable.mergeCells(1,2,3,2);
        inputTable.addText("",1,2);
        inputTable.setHeight(2,"10");
        inputTable.setAlignment(1,1,"right");
        inputTable.setAlignment(1,3,"right");

        inputTable.add(numberTexti,1,1);
        inputTable.add(number,3,1);

        numberTable.add(inputTable,1,1);
        break;

      case LAYOUT_STACKED:
        inputTable = new Table(1,2);
        inputTable.setBorder(0);
        inputTable.setCellpadding(0);
        inputTable.setCellspacing(0);
        inputTable.setHorizontalAlignment("center");
        inputTable.addText("",1,2);
        inputTable.setHeight(1,"2");
        if (!(_colour.equals(""))) {
          inputTable.setColor(_colour);
        }
        inputTable.setAlignment(1,1,"left");
        inputTable.setAlignment(1,2,"left");

        inputTable.add(numberTexti,1,1);
        inputTable.add(number,1,2);

        numberTable.add(inputTable,1,1);
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
      submitTable.add(new SubmitButton(_numberImage,"commit"),1,1);
    }
    else {
      submitTable.add(new SubmitButton(_numberImage,"commit"),2,1);
      submitTable.add(helpButton,1,1);
    }

    numberTable.add(submitTable,1,2);
    _myForm.add(numberTable);
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
    _numberWidth = "148";
    _numberHeight = "89";
    _submitButtonAlignment = "center";
    _layout = LAYOUT_VERTICAL;

    _outerTable = new Table();
    _outerTable.setCellpadding(0);
    _outerTable.setCellspacing(0);
    _outerTable.setHorizontalAlignment("left");

    _myForm = new Form();
    _myForm.setMethod("post");
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

  public void setMaxInputLength(int inputLength) {
    _maxInputLength = inputLength;
  }

  public void setnumberTextSize(int size) {
    _numberTextSize = size;
  }

  public void setnumberTextColor(String color) {
    _numberTextColour = color;
  }

  public void setColor(String color) {
    _colour = color;
  }

  public void setHeight(String height) {
    _numberHeight = height;
  }

  public void setWidth(String width) {
    _numberWidth = width;
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
    _pageId = ((Integer)page.getPrimaryKey()).intValue();
  }

  public synchronized Object clone() {
    PersonalNumberSearch obj = null;
    try {
      obj = (PersonalNumberSearch)super.clone();
      if(_outerTable!=null)
        obj._outerTable = (Table)_outerTable.clone();
      if(_numberImage!=null)
        obj._numberImage = (Image)_numberImage.clone();
      if(_myForm!=null)
        obj._myForm = (Form)_myForm.clone();


    }
    catch(Exception ex) {
      ex.printStackTrace(System.err);
    }
    return obj;
  }
}
