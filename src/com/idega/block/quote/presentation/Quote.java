package com.idega.block.quote.presentation;

import com.idega.block.IWBlock;
import com.idega.block.quote.business.QuoteHolder;
import com.idega.block.quote.business.QuoteBusiness;
import com.idega.block.quote.business.QuoteFinder;

import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;

import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;

import com.idega.core.localisation.business.ICLocaleBusiness;

/**
 * Title: Quote block
 * Description: A block that displays random quotes from the database
 * Copyright: Copyright (c) 2000-2002 idega.is All Rights Reserved
 * Company: idega
  *@author <a href="mailto:laddi@idega.is">Thorhallur "Laddi" Helgason</a>
 * @version 1.2
 */

public class Quote extends Block implements IWBlock {

private int _quoteID;
private boolean _isAdmin = false;
private int _iLocaleID;
private int _row = 1;

private Table _myTable;

private String width_;
private String height_;
private String quoteStyle_;
private String authorStyle_;
private String originStyle_;
private String alignment_ = Table.HORIZONTAL_ALIGN_CENTER;

private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.quote";
protected IWResourceBundle _iwrb;
protected IWBundle _iwb;

public Quote(){
  setDefaultValues();
}

public Quote(int quoteID){
  this();
  _quoteID = quoteID;
}

  public void main(IWContext iwc) throws Exception {
    _iwrb = getResourceBundle(iwc);
    _iwb = getBundle(iwc);

    _isAdmin = iwc.hasEditPermission(this);
    _iLocaleID = ICLocaleBusiness.getLocaleId(iwc.getCurrentLocale());

    drawTable();

    if ( _isAdmin )
      _myTable.add(getAdminTable(iwc),1,_row++);
    _myTable.add(getQuoteTable(iwc),1,_row);

    add(_myTable);
  }

  private Table getQuoteTable(IWContext iwc) {
    Table table = new Table();
      table.setBorder(0);
      table.setWidth("100%");
      table.setHeight("100%");

    QuoteHolder quote = QuoteBusiness.getRandomQuote(iwc,_iLocaleID);
    if ( quote != null ) {
      table.setAlignment(1,1,"left");
      table.setAlignment(1,3,"right");

      _quoteID = quote.getQuoteID();

      String originString = quote.getOrigin();
      String textString = quote.getText();
      if ( textString == null ) {
	textString = "";
      }
      String authorString = quote.getAuthor();
      if ( authorString == null || authorString.length() == 0 ) {
	authorString = _iwrb.getLocalizedString("unknown","Unknown");
      }

      Text quoteOrigin = formatText(originString+":",originStyle_);
      Text quoteText = formatText("\""+textString+"\"",quoteStyle_);
        quoteText.setHorizontalAlignment(alignment_);
      Text quoteAuthor = formatText("-"+Text.getNonBrakingSpace().getText()+authorString,authorStyle_);

      if ( originString != null && originString.length() > 0 ) {
	table.add(quoteOrigin,1,1);
	table.add(quoteText,1,2);
	table.add(quoteAuthor,1,3);
	table.setHeight(1,2,"100%");
      }
      else {
	table.mergeCells(1,1,1,2);
	table.setHeight(1,1,"100%");
	table.setVerticalAlignment(1,1,"middle");

	table.add(quoteText,1,1);
	table.add(quoteAuthor,1,3);
      }
    }
    else {
      table.setAlignment(1,1,"center");
      table.addText(_iwrb.getLocalizedString("no_quotes","No quotes in database..."));
    }

    return table;
  }

  private Table getAdminTable(IWContext iwc) {
    Table table = new Table(3,1);
      table.setCellpadding(0);
      table.setCellspacing(0);

    table.add(getCreateLink(iwc),1,1);
    table.add(getEditLink(iwc),2,1);
    table.add(getDeleteLink(iwc),3,1);

    return table;
  }

  private void drawTable() {
    _myTable = new Table();
    _myTable.setCellpadding(0);
    _myTable.setCellspacing(0);
    _myTable.setWidth(width_);
    _myTable.setHeight(height_);
  }

  private Text formatText(String string, String style) {
    Text text = new Text(string);
      text.setFontStyle(style);
    return text;
  }

  private Link getCreateLink(IWContext iwc) {
    Link link = new Link(iwc.getApplication().getBundle(this.IW_CORE_BUNDLE_IDENTIFIER).getImage("shared/create.gif",_iwrb.getLocalizedString("new_quote","New Quote")));
      link.setWindowToOpen(QuoteEditor.class);
      link.addParameter(QuoteBusiness.PARAMETER_MODE,QuoteBusiness.PARAMETER_NEW);
    return link;
  }

  private Link getEditLink(IWContext iwc) {
    Link link = new Link(iwc.getApplication().getBundle(this.IW_CORE_BUNDLE_IDENTIFIER).getImage("shared/edit.gif",_iwrb.getLocalizedString("edit_quote","Edit Quote")));
      link.setWindowToOpen(QuoteEditor.class);
      link.addParameter(QuoteBusiness.PARAMETER_MODE,QuoteBusiness.PARAMETER_EDIT);
      link.addParameter(QuoteBusiness.PARAMETER_QUOTE_ID,_quoteID);
    return link;
  }

  private Link getDeleteLink(IWContext iwc) {
    Link link = new Link(iwc.getApplication().getBundle(this.IW_CORE_BUNDLE_IDENTIFIER).getImage("shared/delete.gif",_iwrb.getLocalizedString("delete_quote","Delete Quote")));
      link.setWindowToOpen(QuoteEditor.class);
      link.addParameter(QuoteBusiness.PARAMETER_MODE,QuoteBusiness.PARAMETER_DELETE);
      link.addParameter(QuoteBusiness.PARAMETER_QUOTE_ID,_quoteID);
    return link;
  }

  private void setDefaultValues() {
    height_ = "60";
    width_ = "150";
    originStyle_ = "font-size:7pt;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:bold;";
    quoteStyle_ = "font-size:8pt;font-family:Arial,Helvetica,sans-serif;";
    authorStyle_ = "font-size:7pt;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:bold;";
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public void setWidth(String width) {
    width_ = width;
  }

  public String getWidth() {
    return width_;
  }

  public void setHeight(String height) {
    height_ = height;
  }

  public String getHeight() {
    return height_;
  }

  public void setOriginStyle(String style) {
    originStyle_ = style;
  }

  public void setTextStyle(String style) {
    quoteStyle_ = style;
  }

  public void setAuthorStyle(String style) {
    authorStyle_ = style;
  }

  public void setHorizontalAlignment(String alignment) {
    alignment_ = alignment;
  }

  public String getHorizontalAlignment() {
    return alignment_;
  }

  public boolean deleteBlock(int ICObjectInstanceID) {
    return false;
  }

  public Object clone() {
    Quote obj = null;
    try {
      obj = (Quote) super.clone();

      if ( this._myTable != null ) {
	obj._myTable = (Table) this._myTable.clone();
      }
    }
    catch (Exception ex) {
      ex.printStackTrace(System.err);
    }
    return obj;
  }

  /** @deprecated */ public void setQuoteWidth(String width) { setWidth(width); }
  /** @deprecated */ public void setQuoteWidth(int width) { setWidth(Integer.toString(width)); }
  /** @deprecated */ public void setQuoteHeight(String height) { setHeight(height); }
  /** @deprecated */ public void setQuoteHeight(int height) { setHeight(Integer.toString(height)); }
  /** @deprecated */ public void setQuoteOriginStyle(String style) { setOriginStyle(style); }
  /** @deprecated */ public void setQuoteTextStyle(String style) { setTextStyle(style); }
  /** @deprecated */ public void setQuoteAuthorStyle(String style) { setAuthorStyle(style); }
  /** @deprecated */ public void setQuoteOriginSize(String size) {}
  /** @deprecated */ public void setQuoteOriginSize(int size) {}
  /** @deprecated */ public void setQuoteOriginColor(String color) {}
  /** @deprecated */ public void setQuoteOriginFace(String face) {}
  /** @deprecated */ public void setQuoteTextSize(String size) {}
  /** @deprecated */ public void setQuoteTextSize(int size) {}
  /** @deprecated */ public void setQuoteTextColor(String color) {}
  /** @deprecated */ public void setQuoteTextFace(String face) {}
  /** @deprecated */ public void setQuoteAuthorSize(String size) {}
  /** @deprecated */ public void setQuoteAuthorSize(int size) {}
  /** @deprecated */ public void setQuoteAuthorColor(String color) {}
  /** @deprecated */ public void setQuoteAuthorFace(String face) {}
}
