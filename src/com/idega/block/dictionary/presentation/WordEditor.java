package com.idega.block.dictionary.presentation;

import com.idega.data.IDOException;
import javax.ejb.FinderException;
import com.idega.presentation.ui.*;
import com.idega.block.media.presentation.ImageInserter;
import java.rmi.RemoteException;
import com.idega.block.dictionary.data.Word;
import com.idega.presentation.IWContext;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.accesscontrol.business.AccessControl;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.block.dictionary.business.DictionaryBusiness;

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
    _iwb = getBundle(iwc);
    _iwrb = getResourceBundle(iwc);
    addTitle(_iwrb.getLocalizedString("add_word","Add word"));

    try {
      _wordID = Integer.parseInt(iwc.getParameter(DictionaryBusiness.PARAMETER_WORD_ID));
    }
    catch (NumberFormatException e) {
      _wordID = -1;
    }

    String mode = iwc.getParameter(DictionaryBusiness.PARAMETER_MODE);

    if ( mode.equalsIgnoreCase(DictionaryBusiness.PARAMETER_EDIT) ) {
      if ( _wordID != -1 ) {
	_update = true;
	_word = getDictionaryBusiness().getWord(_wordID);
	if ( _word == null ) _update = false;
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

    if ( _update ) {
      if ( _word.getWord() != null ) {
	word.setContent(_word.getWord());
      }
      if ( _word.getDescription() != null ) {
	description.setContent(_word.getDescription());
      }
      if ( _word.getCategoryID() != -1 ) {
	menu.setSelectedElement(String.valueOf(_word.getCategoryID()));
      }
      if ( _word.getImageID() != -1 ) {
	imageInsert.setImageId(_word.getImageID());
      }
    }
    addLeft(_iwrb.getLocalizedString("word","Word")+":",word,true);
    addLeft(_iwrb.getLocalizedString("description","Description")+":",description,true);
    addLeft(_iwrb.getLocalizedString("category","Category")+":",menu,true);
    addRight(_iwrb.getLocalizedString("image","Image")+":",imageInsert,true,false);

    addHiddenInput(new HiddenInput(DictionaryBusiness.PARAMETER_WORD_ID,Integer.toString(_wordID)));

    addSubmitButton(new CloseButton(_iwrb.getLocalizedImageButton("close","CLOSE")));
    addSubmitButton(new SubmitButton(_iwrb.getLocalizedImageButton("save","SAVE"),DictionaryBusiness.PARAMETER_MODE,DictionaryBusiness.PARAMETER_SAVE));
  }

  private void saveWord(IWContext iwc) {
    iwc.removeSessionAttribute(DictionaryBusiness.PARAMETER_IMAGE_ID);
    String name = iwc.getParameter(DictionaryBusiness.PARAMETER_WORD);
    String description = iwc.getParameter(DictionaryBusiness.PARAMETER_DESCRIPTION);
    String categoryID = iwc.getParameter(DictionaryBusiness.PARAMETER_CATEGORY_ID);
    String imageID = iwc.getParameter(DictionaryBusiness.PARAMETER_IMAGE_ID);

    getDictionaryBusiness().saveWord(_wordID,categoryID,name,description,imageID);

    setParentToReload();
    close();
  }

  private void deleteWord() {
    getDictionaryBusiness().deleteWord(_wordID);
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

