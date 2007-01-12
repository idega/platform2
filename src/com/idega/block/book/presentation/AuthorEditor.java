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
    this._iwb = getBundle(iwc);
    this._iwrb = getResourceBundle(iwc);
    addTitle(this._iwrb.getLocalizedString("add_author","Add author"));

    try {
      this._authorID = Integer.parseInt(iwc.getParameter(BookBusiness.PARAMETER_AUTHOR_ID));
    }
    catch (NumberFormatException e) {
      this._authorID = -1;
    }

    String mode = iwc.getParameter(BookBusiness.PARAMETER_MODE);

    if ( mode.equalsIgnoreCase(BookBusiness.PARAMETER_EDIT) ) {
      if ( this._authorID != -1 ) {
	this._update = true;
	this._author = getBookBusiness().getAuthor(this._authorID);
	if ( this._author == null ) {
		this._update = false;
	}
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

    if ( this._update ) {
      if ( this._author.getName() != null ) {
	authorName.setContent(this._author.getName());
      }
      if ( this._author.getDescription() != null ) {
	authorDescription.setContent(this._author.getDescription());
      }
      if ( this._author.getImage() != -1 ) {
	imageInsert.setImageId(this._author.getImage());
      }
    }
    addLeft(this._iwrb.getLocalizedString("author_name","Author name")+":",authorName,true);
    addLeft(this._iwrb.getLocalizedString("book_description","Description")+":",authorDescription,true);
    addRight(this._iwrb.getLocalizedString("image","Image")+":",imageInsert,true,false);

    addHiddenInput(new HiddenInput(BookBusiness.PARAMETER_AUTHOR_ID,Integer.toString(this._authorID)));

    addSubmitButton(new CloseButton(this._iwrb.getLocalizedImageButton("close","CLOSE")));
    addSubmitButton(new SubmitButton(this._iwrb.getLocalizedImageButton("save","SAVE"),BookBusiness.PARAMETER_MODE,BookBusiness.PARAMETER_SAVE));
  }

  private void saveAuthor(IWContext iwc) {
    String name = iwc.getParameter(BookBusiness.PARAMETER_NAME);
    String description = iwc.getParameter(BookBusiness.PARAMETER_DESCRIPTION);
    String imageID = iwc.getParameter(BookBusiness.PARAMETER_IMAGE_ID);

    getBookBusiness().saveAuthor(this._authorID,name,description,imageID);

    setParentToReload();
    close();
  }

  private void deleteAuthor() {
    getBookBusiness().deleteAuthor(this._authorID);
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

