package com.idega.block.quote.presentation;

import java.sql.*;
import java.util.*;
import java.io.*;
import com.idega.util.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.block.quote.data.*;
import com.idega.block.quote.business.QuoteBusiness;
import com.idega.data.*;
import com.idega.util.text.*;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.accesscontrol.business.AccessControl;

public class Quote extends JModuleObject{

private int _quoteID;
private boolean _isAdmin = false;
private int _iLocaleID;

private Table _myTable;

private String quoteWidth;
private String quoteHeight;

private String quoteStyle;
private String authorStyle;
private String originStyle;

private String originTextSize;
private String quoteTextSize;
private String authorTextSize;

private String originTextColor;
private String quoteTextColor;
private String authorTextColor;

private String originTextFace;
private String quoteTextFace;
private String authorTextFace;

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

	public void main(ModuleInfo modinfo) throws Exception {
    _iwrb = getResourceBundle(modinfo);
    _iwb = getBundle(modinfo);

    _isAdmin = AccessControl.hasEditPermission(this,modinfo);
    _iLocaleID = ICLocaleBusiness.getLocaleId(modinfo.getCurrentLocale());

		drawTable();
	}

	private void drawTable() throws SQLException {

		_myTable = new Table();
			_myTable.setBorder(0);
			_myTable.setWidth(quoteWidth);
			_myTable.setHeight(quoteHeight);

		QuoteEntity quote = QuoteBusiness.getRandomQuote(_iLocaleID);

		if ( quote != null ) {

			_myTable.setAlignment(1,3,"right");
			_myTable.setAlignment(1,1,"left");
			_myTable.setAlignment(1,2,"center");

			_quoteID = quote.getID();

			String originString = quote.getQuoteOrigin();
      String textString = quote.getQuoteText();
      if ( textString == null ) {
        textString = "";
      }
      String authorString = quote.getQuoteAuthor();
      if ( authorString == null || authorString.length() == 0 ) {
        authorString = _iwrb.getLocalizedString("unknown","Unknown");
      }

      Text quoteOrigin = new Text(originString+":");
				if ( originStyle != null ) {
          quoteOrigin.setFontStyle(originStyle);
				}
        else {
          quoteOrigin.setBold();
          quoteOrigin.setFontSize(originTextSize);
          quoteOrigin.setFontColor(originTextColor);
          quoteOrigin.setFontFace(originTextFace);
        }

			Text quoteText = new Text("\""+textString+"\"");
				if ( quoteStyle != null ) {
          quoteText.setFontStyle(quoteStyle);
				}
        else {
          quoteText.setItalic();
          quoteText.setFontColor(quoteTextColor);
          quoteText.setFontSize(quoteTextSize);
          quoteText.setFontFace(quoteTextFace);
        }

			Text quoteAuthor = new Text("-"+Text.getNonBrakingSpace().getText()+authorString);
				if ( authorStyle != null ) {
          quoteAuthor.setFontStyle(authorStyle);
				}
        else {
          quoteAuthor.setBold();
          quoteAuthor.setFontSize(authorTextSize);
          quoteAuthor.setFontColor(authorTextColor);
          quoteAuthor.setFontFace(authorTextFace);
        }

			if ( originString != null && originString.length() > 0 ) {
				_myTable.add(quoteOrigin,1,1);
				_myTable.add(quoteText,1,2);
				_myTable.add(quoteAuthor,1,3);
			}

			else {
				_myTable.mergeCells(1,1,1,2);
				_myTable.setAlignment(1,1,"center");
				_myTable.setVerticalAlignment(1,1,"middle");

				_myTable.add(quoteText,1,1);
				_myTable.add(quoteAuthor,1,3);
			}

		}

		else {
			_myTable.setAlignment(1,1,"center");
			_myTable.addText(_iwrb.getLocalizedString("no_quotes","No quotes in database..."));
		}

		if ( _isAdmin ) {
			int tableHeight = _myTable.getRows()+1;
			int tableWidth = _myTable.getColumns();

			Table formTable = new Table(3,1);

			Link createLink = new Link(_iwb.getImage("shared/create.gif",_iwrb.getLocalizedString("new_quote","New Quote"),15,15));
        createLink.setWindowToOpen(QuoteEditor.class);
				createLink.addParameter(QuoteBusiness.PARAMETER_MODE,QuoteBusiness.PARAMETER_NEW);
			Link editLink = new Link(_iwb.getImage("shared/edit.gif",_iwrb.getLocalizedString("edit_quote","Edit Quote"),15,15));
        editLink.setWindowToOpen(QuoteEditor.class);
				editLink.addParameter(QuoteBusiness.PARAMETER_MODE,QuoteBusiness.PARAMETER_EDIT);
				editLink.addParameter(QuoteBusiness.PARAMETER_QUOTE_ID,_quoteID);
			Link deleteLink = new Link(_iwb.getImage("shared/delete.gif",_iwrb.getLocalizedString("delete_quote","Delete Quote"),15,15));
        deleteLink.setWindowToOpen(QuoteEditor.class);
				deleteLink.addParameter(QuoteBusiness.PARAMETER_MODE,QuoteBusiness.PARAMETER_DELETE);
				deleteLink.addParameter(QuoteBusiness.PARAMETER_QUOTE_ID,_quoteID);

			formTable.add(createLink,1,1);
			formTable.add(editLink,2,1);
			formTable.add(deleteLink,3,1);

			_myTable.add(formTable,1,tableHeight);
		}

		add(_myTable);
	}

  private void setDefaultValues() {
    quoteHeight = "100";
    quoteWidth = "150";
    quoteTextColor = "#000000";
    quoteTextSize = Text.FONT_SIZE_10_HTML_2;
    quoteTextFace = Text.FONT_FACE_VERDANA;
    authorTextColor = "#000000";
    authorTextSize = Text.FONT_SIZE_7_HTML_1;
    authorTextFace = Text.FONT_FACE_VERDANA;
    originTextColor = "#000000";
    originTextSize = Text.FONT_SIZE_7_HTML_1;
    originTextFace = Text.FONT_FACE_VERDANA;
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public void setQuoteWidth(String width) {
    quoteWidth = width;
  }

  public void setQuoteWidth(int width) {
    setQuoteWidth(Integer.toString(width));
  }

  public void setQuoteHeight(String height) {
    quoteHeight = height;
  }

  public void setQuoteHeight(int height) {
    setQuoteHeight(Integer.toString(height));
  }

  public void setQuoteOriginSize(String size) {
    originTextSize = size;
  }

  public void setQuoteOriginSize(int size) {
    setQuoteOriginSize(Integer.toString(size));
  }

  public void setQuoteOriginColor(String color) {
    originTextColor = color;
  }

  public void setQuoteOriginFace(String face) {
    originTextFace = face;
  }

  public void setQuoteOriginStyle(String style) {
    originStyle = style;
  }

  public void setQuoteTextSize(String size) {
    quoteTextSize = size;
  }

  public void setQuoteTextSize(int size) {
    setQuoteTextSize(Integer.toString(size));
  }

  public void setQuoteTextColor(String color) {
    quoteTextColor = color;
  }

  public void setQuoteTextFace(String face) {
    quoteTextFace = face;
  }

  public void setQuoteTextStyle(String style) {
    quoteStyle = style;
  }

  public void setQuoteAuthorSize(String size) {
    authorTextSize = size;
  }

  public void setQuoteAuthorSize(int size) {
    setQuoteAuthorSize(Integer.toString(size));
  }

  public void setQuoteAuthorColor(String color) {
    authorTextColor = color;
  }

  public void setQuoteAuthorFace(String face) {
    authorTextFace = face;
  }

  public void setQuoteAuthorStyle(String style) {
    authorStyle = style;
  }

}
