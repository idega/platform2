package com.idega.block.book.presentation;

import java.rmi.RemoteException;

import javax.ejb.FinderException;

import com.idega.block.book.business.BookBusiness;
import com.idega.block.book.data.Publisher;
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

public class PublisherEditor extends IWAdminWindow{

private int _publisherID = -1;
private boolean _update = false;
private boolean _save = false;
private int _objectID = -1;
private Publisher _publisher;

private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.book";
private IWBundle _iwb;
private IWResourceBundle _iwrb;

public PublisherEditor(){
  setWidth(500);
  setHeight(300);
  setUnMerged();
}

  public void main(IWContext iwc) throws Exception {
    this._iwb = getBundle(iwc);
    this._iwrb = getResourceBundle(iwc);
    addTitle(this._iwrb.getLocalizedString("add_publisher","Add publisher"));

    try {
      this._publisherID = Integer.parseInt(iwc.getParameter(BookBusiness.PARAMETER_PUBLISHER_ID));
    }
    catch (NumberFormatException e) {
      this._publisherID = -1;
    }

    String mode = iwc.getParameter(BookBusiness.PARAMETER_MODE);

    if ( mode.equalsIgnoreCase(BookBusiness.PARAMETER_EDIT) ) {
      if ( this._publisherID != -1 ) {
	this._update = true;
	this._publisher = getBookBusiness().getPublisher(this._publisherID);
	if ( this._publisher == null ) {
		this._update = false;
	}
      }
      processForm();
    }
    else if ( mode.equalsIgnoreCase(BookBusiness.PARAMETER_NEW) ) {
      processForm();
    }
    else if ( mode.equalsIgnoreCase(BookBusiness.PARAMETER_DELETE) ) {
      deletePublisher();
    }
    else if ( mode.equalsIgnoreCase(BookBusiness.PARAMETER_SAVE) ) {
      savePublisher(iwc);
    }
  }

  private void processForm() throws FinderException,RemoteException {
    TextInput publisherName = new TextInput(BookBusiness.PARAMETER_NAME);
      publisherName.setLength(24);
    TextArea publisherDescription = new TextArea(BookBusiness.PARAMETER_DESCRIPTION,54,12);
    ImageInserter imageInsert = new ImageInserter(BookBusiness.PARAMETER_IMAGE_ID);
      imageInsert.setMaxImageWidth(130);
      imageInsert.setHasUseBox(false);

    if ( this._update ) {
      if ( this._publisher.getName() != null ) {
	publisherName.setContent(this._publisher.getName());
      }
      if ( this._publisher.getDescription() != null ) {
	publisherDescription.setContent(this._publisher.getDescription());
      }
      if ( this._publisher.getImage() != -1 ) {
	imageInsert.setImageId(this._publisher.getImage());
      }
    }
    addLeft(this._iwrb.getLocalizedString("publisher_name","Publisher name")+":",publisherName,true);
    addLeft(this._iwrb.getLocalizedString("book_description","Description")+":",publisherDescription,true);
    addRight(this._iwrb.getLocalizedString("image","Image")+":",imageInsert,true,false);

    addHiddenInput(new HiddenInput(BookBusiness.PARAMETER_PUBLISHER_ID,Integer.toString(this._publisherID)));

    addSubmitButton(new CloseButton(this._iwrb.getLocalizedImageButton("close","CLOSE")));
    addSubmitButton(new SubmitButton(this._iwrb.getLocalizedImageButton("save","SAVE"),BookBusiness.PARAMETER_MODE,BookBusiness.PARAMETER_SAVE));
  }

  private void savePublisher(IWContext iwc) {
    String name = iwc.getParameter(BookBusiness.PARAMETER_NAME);
    String description = iwc.getParameter(BookBusiness.PARAMETER_DESCRIPTION);
    String imageID = iwc.getParameter(BookBusiness.PARAMETER_IMAGE_ID);

    getBookBusiness().savePublisher(this._publisherID,name,description,imageID);

    setParentToReload();
    close();
  }

  private void deletePublisher() {
    getBookBusiness().deletePublisher(this._publisherID);
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

