package com.idega.block.text.presentation;

import com.idega.block.text.data.TxText;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.ui.AbstractChooser;
/**
 * Title: com.idega.block.text.presentation.TextChooser
 * Description: The chooser object for localized text
 * Copyright:    Copyright (c) 2001
 * Company:      idega software
 * @author Eirikur S. Hrafnsson eiki@idega.is
 * @version 1.0
 */

public class TextChooser extends AbstractChooser {
  private String style;
  private Image _chooseButtonImage;
  public static String RELOAD_PARENT_PARAMETER = "tx_no_reload";
  private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.text";


  public TextChooser(String chooserName) {
    addForm(false);
    addTextInput(false);
    setChooserParameter(chooserName);
    super.setParameterValue("a","b");
  }

  public TextChooser(String chooserName,String style) {
    this(chooserName);
    setInputStyle(style);
  }

  public Class getChooserWindowClass() {
    return TextEditorWindow.class;
  }

  public void main(IWContext iwc){
    IWBundle iwb = iwc.getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER);
    if (_chooseButtonImage != null) {
      setChooseButtonImage(_chooseButtonImage);
    }else {
      setChooseButtonImage(iwb.getImage("open.gif","Choose file"));
    }
    iwc.setSessionAttribute(RELOAD_PARENT_PARAMETER, "true");
    if( getChooserValue()!= null ){
      super.setParameterValue(getChooserParameter(), getChooserValue());
//      iwc.setSessionAttribute(MediaBusiness.getMediaParameterNameInSession(iwc),getChooserValue());
    }
  }

  public void setSelectedText(TxText text){
    super.setChooserValue("",text.getID());
  }
  
  public void setSelectedText(int id){
    super.setChooserValue("",id);
  }

  public void setValue(Object text){
    setSelectedText((TxText)text);
  }

  public void setChooseImage(Image image) {
    _chooseButtonImage = image;
  }

}
