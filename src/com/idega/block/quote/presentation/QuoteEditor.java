package com.idega.block.quote.presentation;

import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.interfaceobject.TextArea;
import com.idega.jmodule.object.interfaceobject.TextInput;
import com.idega.jmodule.object.interfaceobject.SubmitButton;
import com.idega.jmodule.object.interfaceobject.CloseButton;
import com.idega.jmodule.object.interfaceobject.HiddenInput;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.accesscontrol.business.AccessControl;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.block.quote.business.QuoteBusiness;
import com.idega.block.quote.data.QuoteEntity;

public class QuoteEditor extends IWAdminWindow{

private int _quoteID = -1;
private boolean _isAdmin = false;
private boolean _update = false;
private boolean _save = false;
private int _iLocaleID;
private QuoteEntity _quote;

private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.quote";
private IWBundle _iwb;
private IWResourceBundle _iwrb;

public QuoteEditor(){
  setWidth(420);
  setHeight(270);
  setUnMerged();
  setMethod("get");
}

	public void main(ModuleInfo modinfo) throws Exception {
    _isAdmin=AccessControl.hasEditPermission(new Quote(),modinfo);
    _iwb = getBundle(modinfo);
    _iwrb = getResourceBundle(modinfo);
    addTitle(_iwrb.getLocalizedString("quote_admin","Quote Admin"));
    _iLocaleID = ICLocaleBusiness.getLocaleId(modinfo.getCurrentLocale());

    try {
      _quoteID = Integer.parseInt(modinfo.getParameter(QuoteBusiness.PARAMETER_QUOTE_ID));
      _quote = QuoteBusiness.getQuote(_quoteID);
    }
    catch (NumberFormatException e) {
      _quoteID = -1;
    }

    String mode = modinfo.getParameter(QuoteBusiness.PARAMETER_MODE);

    if ( mode.equalsIgnoreCase(QuoteBusiness.PARAMETER_EDIT) ) {
      if ( _quoteID != -1 ) {
        _update = true;
      }
      processForm();
    }
    else if ( mode.equalsIgnoreCase(QuoteBusiness.PARAMETER_NEW) ) {
      processForm();
    }
    else if ( mode.equalsIgnoreCase(QuoteBusiness.PARAMETER_DELETE) ) {
      deleteQuote();
    }
    else if ( mode.equalsIgnoreCase(QuoteBusiness.PARAMETER_SAVE) ) {
      saveQuote(modinfo);
    }
	}

  private void processForm() {
    TextInput quoteOrigin = new TextInput(QuoteBusiness.PARAMETER_QUOTE_ORIGIN);
      quoteOrigin.setLength(24);
    TextInput quoteAuthor = new TextInput(QuoteBusiness.PARAMETER_QUOTE_AUTHOR);
      quoteAuthor.setLength(24);
    TextArea quoteText = new TextArea(QuoteBusiness.PARAMETER_QUOTE_TEXT,40,6);

    if ( _update && _quote != null ) {
      if ( _quote.getQuoteOrigin() != null ) {
        quoteOrigin.setContent(_quote.getQuoteOrigin());
      }
      if ( _quote.getQuoteText() != null ) {
        quoteText.setContent(_quote.getQuoteText());
      }
      if ( _quote.getQuoteAuthor() != null ) {
        quoteAuthor.setContent(_quote.getQuoteAuthor());
      }
    }

    addLeft(_iwrb.getLocalizedString("origin","Origin")+":",quoteOrigin,true);
    addLeft(_iwrb.getLocalizedString("quote","Quote")+":",quoteText,true);
    addLeft(_iwrb.getLocalizedString("author","Author")+":",quoteAuthor,true);
    addHiddenInput(new HiddenInput(QuoteBusiness.PARAMETER_QUOTE_ID,Integer.toString(_quoteID)));

    addSubmitButton(new CloseButton(_iwrb.getImage("close.gif")));
    addSubmitButton(new SubmitButton(_iwrb.getImage("save.gif"),QuoteBusiness.PARAMETER_MODE,QuoteBusiness.PARAMETER_SAVE));
  }

  private void saveQuote(ModuleInfo modinfo) {
    String quoteOrigin = modinfo.getParameter(QuoteBusiness.PARAMETER_QUOTE_ORIGIN);
    String quoteText = modinfo.getParameter(QuoteBusiness.PARAMETER_QUOTE_TEXT);
    String quoteAuthor = modinfo.getParameter(QuoteBusiness.PARAMETER_QUOTE_AUTHOR);

    QuoteBusiness.saveQuote(_quoteID,_iLocaleID,quoteOrigin,quoteText,quoteAuthor);

    setParentToReload();
    close();
  }

  private void deleteQuote() {
    QuoteBusiness.deleteQuote(_quoteID);
    setParentToReload();
    close();
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }
}

