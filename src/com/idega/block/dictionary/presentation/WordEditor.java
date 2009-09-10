package com.idega.block.dictionary.presentation;

import java.rmi.RemoteException;

import javax.ejb.FinderException;

import com.idega.block.dictionary.business.DictionaryBusiness;
import com.idega.block.dictionary.data.Word;
import com.idega.block.media.presentation.ImageInserter;
import com.idega.data.IDOException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;

public class WordEditor extends IWAdminWindow{

private int _wordID = -1;
private boolean _update = false;
private boolean _save = false;
private Word _word;

private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.dictionary";
private IWBundle _iwb;
private IWResourceBundle _iwrb;

public WordEditor(){
  setWidth(500);
  setHeight(300);
  setUnMerged();
}

  public void main(IWContext iwc) throws Exception {
    this._iwb = getBundle(iwc);
    this._iwrb = getResourceBundle(iwc);
    addTitle(this._iwrb.getLocalizedString("add_word","Add word"));

    try {
      this._wordID = Integer.parseInt(iwc.getParameter(DictionaryBusiness.PARAMETER_WORD_ID));
    }
    catch (NumberFormatException e) {
      this._wordID = -1;
    }

    String mode = iwc.getParameter(DictionaryBusiness.PARAMETER_MODE);

    if ( mode.equalsIgnoreCase(DictionaryBusiness.PARAMETER_EDIT) ) {
      if ( this._wordID != -1 ) {
	this._update = true;
	this._word = getDictionaryBusiness().getWord(this._wordID);
	if ( this._word == null ) {
		this._update = false;
	}
      }
      processForm();
    }
    else if ( mode.equalsIgnoreCase(DictionaryBusiness.PARAMETER_NEW) ) {
      processForm();
    }
    else if ( mode.equalsIgnoreCase(DictionaryBusiness.PARAMETER_DELETE) ) {
      deleteWord();
    }
    else if ( mode.equalsIgnoreCase(DictionaryBusiness.PARAMETER_SAVE) ) {
      saveWord(iwc);
    }
  }

  private void processForm() throws FinderException,RemoteException,IDOException {
    TextInput word = new TextInput(DictionaryBusiness.PARAMETER_WORD);
      word.setLength(24);
    TextArea description = new TextArea(DictionaryBusiness.PARAMETER_DESCRIPTION,54,9);
    DropdownMenu menu = getDictionaryBusiness().getCategoryMenu();
    ImageInserter imageInsert = new ImageInserter(DictionaryBusiness.PARAMETER_IMAGE_ID);
      imageInsert.setMaxImageWidth(130);
      imageInsert.setHasUseBox(false);

    if ( this._update ) {
      if ( this._word.getWord() != null ) {
	word.setContent(this._word.getWord());
      }
      if ( this._word.getDescription() != null ) {
	description.setContent(this._word.getDescription());
      }
      if ( this._word.getCategoryID() != -1 ) {
	menu.setSelectedElement(String.valueOf(this._word.getCategoryID()));
      }
      if ( this._word.getImageID() != -1 ) {
	imageInsert.setImageId(this._word.getImageID());
      }
    }
    addLeft(this._iwrb.getLocalizedString("word","Word")+":",word,true);
    addLeft(this._iwrb.getLocalizedString("description","Description")+":",description,true);
    addLeft(this._iwrb.getLocalizedString("category","Category")+":",menu,true);
    addRight(this._iwrb.getLocalizedString("image","Image")+":",imageInsert,true,false);

    addHiddenInput(new HiddenInput(DictionaryBusiness.PARAMETER_WORD_ID,Integer.toString(this._wordID)));

    addSubmitButton(new CloseButton(this._iwrb.getLocalizedImageButton("close","CLOSE")));
    addSubmitButton(new SubmitButton(this._iwrb.getLocalizedImageButton("save","SAVE"),DictionaryBusiness.PARAMETER_MODE,DictionaryBusiness.PARAMETER_SAVE));
  }

  private void saveWord(IWContext iwc) {
    iwc.removeSessionAttribute(DictionaryBusiness.PARAMETER_IMAGE_ID);
    String name = iwc.getParameter(DictionaryBusiness.PARAMETER_WORD);
    String description = iwc.getParameter(DictionaryBusiness.PARAMETER_DESCRIPTION);
    String categoryID = iwc.getParameter(DictionaryBusiness.PARAMETER_CATEGORY_ID);
    String imageID = iwc.getParameter(DictionaryBusiness.PARAMETER_IMAGE_ID);

    getDictionaryBusiness().saveWord(this._wordID,categoryID,name,description,imageID);

    setParentToReload();
    close();
  }

  private void deleteWord() {
    getDictionaryBusiness().deleteWord(this._wordID);
    setParentToReload();
    close();
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  private DictionaryBusiness getDictionaryBusiness(){
    return DictionaryBusiness.getDictionaryBusinessInstace();
  }
}

