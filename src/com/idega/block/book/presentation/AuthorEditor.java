package com.idega.block.book.presentation;

import java.rmi.RemoteException;

import javax.ejb.FinderException;

import com.idega.block.book.business.BookBusiness;
import com.idega.block.book.data.Author;
import com.idega.block.media.presentation.ImageInserter;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;

public class AuthorEditor extends IWAdminWindow{

private int _authorID = -1;
private boolean _update = false;
private boolean _save = false;
private int _objectID = -1;
private Author _author;

private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.book";
private IWBundle _iwb;
private IWResourceBundle _iwrb;

public AuthorEditor(){
  setWidth(500);
  setHeight(300);
  setUnMerged();
}

  public void main(IWContext iwc) throws Exception {
    _iwb = getBundle(iwc);
    _iwrb = getResourceBundle(iwc);
    addTitle(_iwrb.getLocalizedString("add_author","Add author"));

    try {
      _authorID = Integer.parseInt(iwc.getParameter(BookBusiness.PARAMETER_AUTHOR_ID));
    }
    catch (NumberFormatException e) {
      _authorID = -1;
    }

    String mode = iwc.getParameter(BookBusiness.PARAMETER_MODE);

    if ( mode.equalsIgnoreCase(BookBusiness.PARAMETER_EDIT) ) {
      if ( _authorID != -1 ) {
	_update = true;
	_author = getBookBusiness().getAuthor(_authorID);
	if ( _author == null ) _update = false;
      }
      processForm();
    }
    else if ( mode.equalsIgnoreCase(BookBusiness.PARAMETER_NEW) ) {
      processForm();
    }
    else if ( mode.equalsIgnoreCase(BookBusiness.PARAMETER_DELETE) ) {
      deleteAuthor();
    }
    else if ( mode.equalsIgnoreCase(BookBusiness.PARAMETER_SAVE) ) {
      saveAuthor(iwc);
    }
  }

  private void processForm() throws FinderException,RemoteException {
    TextInput authorName = new TextInput(BookBusiness.PARAMETER_NAME);
      authorName.setLength(24);
    TextArea authorDescription = new TextArea(BookBusiness.PARAMETER_DESCRIPTION,54,12);
    ImageInserter imageInsert = new ImageInserter(BookBusiness.PARAMETER_IMAGE_ID);
      imageInsert.setMaxImageWidth(130);
      imageInsert.setHasUseBox(false);

    if ( _update ) {
      if ( _author.getName() != null ) {
	authorName.setContent(_author.getName());
      }
      if ( _author.getDescription() != null ) {
	authorDescription.setContent(_author.getDescription());
      }
      if ( _author.getImage() != -1 ) {
	imageInsert.setImageId(_author.getImage());
      }
    }
    addLeft(_iwrb.getLocalizedString("author_name","Author name")+":",authorName,true);
    addLeft(_iwrb.getLocalizedString("book_description","Description")+":",authorDescription,true);
    addRight(_iwrb.getLocalizedString("image","Image")+":",imageInsert,true,false);

    addHiddenInput(new HiddenInput(BookBusiness.PARAMETER_AUTHOR_ID,Integer.toString(_authorID)));

    addSubmitButton(new CloseButton(_iwrb.getLocalizedImageButton("close","CLOSE")));
    addSubmitButton(new SubmitButton(_iwrb.getLocalizedImageButton("save","SAVE"),BookBusiness.PARAMETER_MODE,BookBusiness.PARAMETER_SAVE));
  }

  private void saveAuthor(IWContext iwc) {
    String name = iwc.getParameter(BookBusiness.PARAMETER_NAME);
    String description = iwc.getParameter(BookBusiness.PARAMETER_DESCRIPTION);
    String imageID = iwc.getParameter(BookBusiness.PARAMETER_IMAGE_ID);

    getBookBusiness().saveAuthor(_authorID,name,description,imageID);

    setParentToReload();
    close();
  }

  private void deleteAuthor() {
    getBookBusiness().deleteAuthor(_authorID);
    setParentToReload();
    close();
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  private BookBusiness getBookBusiness(){
    return BookBusiness.getBookBusinessInstace();
  }
}

