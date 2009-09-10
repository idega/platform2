package com.idega.block.book.presentation;

import java.rmi.RemoteException;

import javax.ejb.FinderException;

import com.idega.block.book.business.BookBusiness;
import com.idega.block.book.data.Review;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;

public class ReviewEditor extends IWAdminWindow{

private int _reviewID = -1;
private int _bookID = -1;
private boolean _update = false;
private boolean _save = false;
private int _objectID = -1;
private Review _review;

private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.book";
private IWBundle _iwb;
private IWResourceBundle _iwrb;

public ReviewEditor(){
  setWidth(500);
  setHeight(300);
  setUnMerged();
}

  public void main(IWContext iwc) throws Exception {
    this._iwb = getBundle(iwc);
    this._iwrb = getResourceBundle(iwc);
    addTitle(this._iwrb.getLocalizedString("add_review","Add publisher"));

    try {
      this._reviewID = Integer.parseInt(iwc.getParameter(BookBusiness.PARAMETER_REVIEW_ID));
    }
    catch (NumberFormatException e) {
      this._reviewID = -1;
    }

    try {
      this._bookID = Integer.parseInt(iwc.getParameter(BookBusiness.PARAMETER_BOOK_ID));
    }
    catch (NumberFormatException e) {
      this._bookID = -1;
    }

    String mode = iwc.getParameter(BookBusiness.PARAMETER_MODE);

    if ( mode.equalsIgnoreCase(BookBusiness.PARAMETER_EDIT) ) {
      if ( this._reviewID != -1 ) {
	this._update = true;
	this._review = getBookBusiness().getReview(this._reviewID);
	if ( this._review == null ) {
		this._update = false;
	}
      }
      processForm();
    }
    else if ( mode.equalsIgnoreCase(BookBusiness.PARAMETER_NEW) ) {
      processForm();
    }
    else if ( mode.equalsIgnoreCase(BookBusiness.PARAMETER_DELETE) ) {
      deleteReview();
    }
    else if ( mode.equalsIgnoreCase(BookBusiness.PARAMETER_SAVE) ) {
      saveReview(iwc);
    }
  }

  private void processForm() throws FinderException,RemoteException {
    TextInput reviewerName = new TextInput(BookBusiness.PARAMETER_NAME);
      reviewerName.setLength(24);
    TextArea bookReview = new TextArea(BookBusiness.PARAMETER_DESCRIPTION,54,8);
    DropdownMenu menu = new DropdownMenu(BookBusiness.PARAMETER_RATING);
    for ( int a = 1; a <= 10; a++ ) {
      menu.addMenuElement(a,String.valueOf(a));
      if ( a == 5 ) {
		menu.setSelectedElement(String.valueOf(a));
	}
    }

    if ( this._update ) {
      if ( this._review.getName() != null ) {
	reviewerName.setContent(this._review.getName());
      }
      if ( this._review.getReview() != null ) {
	bookReview.setContent(this._review.getReview());
      }
      if ( this._review.getRating() != -1 ) {
	menu.setSelectedElement(String.valueOf(this._review.getRating()));
      }
    }
    addLeft(this._iwrb.getLocalizedString("reviewer_name","Name")+":",reviewerName,true);
    addLeft(this._iwrb.getLocalizedString("review","Review")+":",bookReview,true);
    addLeft(this._iwrb.getLocalizedString("rating","Rating")+":"+Text.NON_BREAKING_SPACE,menu,false);

    addHiddenInput(new HiddenInput(BookBusiness.PARAMETER_REVIEW_ID,Integer.toString(this._reviewID)));
    addHiddenInput(new HiddenInput(BookBusiness.PARAMETER_BOOK_ID,Integer.toString(this._bookID)));

    addSubmitButton(new CloseButton(this._iwrb.getLocalizedImageButton("close","CLOSE")));
    addSubmitButton(new SubmitButton(this._iwrb.getLocalizedImageButton("save","SAVE"),BookBusiness.PARAMETER_MODE,BookBusiness.PARAMETER_SAVE));
  }

  private void saveReview(IWContext iwc) {
    String name = iwc.getParameter(BookBusiness.PARAMETER_NAME);
    String description = iwc.getParameter(BookBusiness.PARAMETER_DESCRIPTION);
    String rating = iwc.getParameter(BookBusiness.PARAMETER_RATING);

    getBookBusiness().saveReview(this._reviewID,this._bookID,name,description,rating);

    setParentToReload();
    close();
  }

  private void deleteReview() {
    getBookBusiness().deleteReview(this._reviewID);
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

