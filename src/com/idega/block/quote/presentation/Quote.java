package com.idega.block.quote.presentation;

import java.util.HashMap;
import java.util.Map;
import com.idega.block.quote.business.QuoteBusiness;
import com.idega.block.quote.business.QuoteHolder;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.block.presentation.Builderaware;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.util.text.TextSoap;

/**
 * Title: Quote block
 * Description: A block that displays random quotes from the database
 * Copyright: Copyright (c) 2000-2002 idega.is All Rights Reserved
 * Company: idega
  *@author <a href="mailto:laddi@idega.is">Thorhallur "Laddi" Helgason</a>
 * @modified <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.2
 */

public class Quote extends Block implements Builderaware {

	private int _quoteID = -1;
	private int _objectID = -1;
	private boolean _hasEditPermission = false;
	private int _iLocaleID;
	private int _row = 1;

	private Table _myTable;

	private static final String QUOTE_STYLE_NAME = "quote";
	private static final String ORIGIN_STYLE_NAME = "origin";
	private static final String AUTHOR_STYLE_NAME = "author";
	private static final String QUOTE_STYLE = "font-size:9px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:bold;";
	private static final String ORIGIN_STYLE = "font-size:10px;font-family:Arial,Helvetica,sans-serif;";
	private static final String AUTHOR_STYLE = "font-size:9px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:bold;";

	private String width_;
	private String height_;
	private String quoteStyle_;
	private String authorStyle_;
	private String originStyle_;
	private String alignment_ = Table.HORIZONTAL_ALIGN_CENTER;

	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.quote";
	protected IWResourceBundle _iwrb;
	protected IWBundle _iwb;
	
	private boolean _alwaysFetchFromDatabase = false;
	private boolean _showAuthor = true;
	private boolean _showOrigin = true;
	private boolean _showQuotes = true;

	public Quote() {
		setDefaultValues();
	}

	public Quote(int quoteID) {
		this();
		_quoteID = quoteID;
	}

	public void main(IWContext iwc) throws Exception {
		_iwb = getBundle(iwc);
		_iwrb = _iwb.getResourceBundle(iwc.getCurrentLocale());
		_objectID = getICObjectInstanceID();

		_hasEditPermission = iwc.hasEditPermission(this);
		_iLocaleID = ICLocaleBusiness.getLocaleId(iwc.getCurrentLocale());

		drawTable();
		QuoteHolder quote = getQuoteBusiness().getRandomQuote(iwc, _iLocaleID, _objectID, _alwaysFetchFromDatabase);
		if (quote != null)
			_quoteID = quote.getQuoteID();

		if (_hasEditPermission)
			_myTable.add(getAdminTable(iwc), 1, _row++);

		_myTable.add(getQuoteTable(iwc, quote), 1, _row);
		add(_myTable);
	}

	private Table getQuoteTable(IWContext iwc, QuoteHolder quote) {
		Table table = new Table();
		table.setBorder(0);
		table.setWidth("100%");
		table.setHeight("100%");

		if (quote != null) {
			table.setAlignment(1, 1, "left");
			table.setAlignment(1, 3, "right");

			String originString = quote.getOrigin();
			String textString = quote.getText();
			if (textString == null) {
				textString = "";
			}
			String authorString = quote.getAuthor();
			if (authorString == null || authorString.length() == 0) {
				authorString = _iwrb.getLocalizedString("unknown", "Unknown");
			}

			Text quoteOrigin = getStyleText(originString + ":",ORIGIN_STYLE_NAME);
			if ( this.originStyle_ != null )
				quoteOrigin.setStyleAttribute(originStyle_);
			
			Text quoteText = null;
			if (_showQuotes) {
				quoteText = getStyleText("\"" + TextSoap.formatText(textString) + "\"", QUOTE_STYLE_NAME);
			}
			else {
				quoteText = getStyleText(TextSoap.formatText(textString), QUOTE_STYLE_NAME);
			}
			quoteText.setHorizontalAlignment(alignment_);
			if ( this.quoteStyle_ != null )
				quoteText.setStyleAttribute(quoteStyle_);
			
			Text quoteAuthor = getStyleText("-" + Text.getNonBrakingSpace().getText() + authorString, AUTHOR_STYLE_NAME);
			if ( this.authorStyle_ != null )
				quoteAuthor.setStyleAttribute(authorStyle_);

			if (_showOrigin && originString != null && originString.length() > 0) {
				table.add(quoteOrigin, 1, 1);
				table.add(quoteText, 1, 2);
				table.add(quoteAuthor, 1, 3);
				table.setHeight(1, 2, "100%");
			}
			else {
				table.mergeCells(1, 1, 1, 2);
				table.setHeight(1, 1, "100%");
				table.setVerticalAlignment(1, 1, "middle");

				table.add(quoteText, 1, 1);
				if (_showAuthor) {
					table.add(quoteAuthor, 1, 3);
				}
			}
		}
		else {
			table.setAlignment(1, 1, "center");
			table.addText(_iwrb.getLocalizedString("no_quotes", "No quotes in database..."));
		}

		return table;
	}

	private Table getAdminTable(IWContext iwc) {
		Table table = new Table(3, 1);
		table.setCellpadding(0);
		table.setCellspacing(0);

		table.add(getCreateLink(iwc), 1, 1);
		if (_quoteID != -1) {
			table.add(getEditLink(iwc), 2, 1);
			table.add(getDeleteLink(iwc), 3, 1);
		}

		return table;
	}

	private void drawTable() {
		_myTable = new Table();
		_myTable.setCellpadding(0);
		_myTable.setCellspacing(0);
		_myTable.setWidth(width_);
		if (height_ != null)
			_myTable.setHeight(height_);
	}

	private Link getCreateLink(IWContext iwc) {
		Link link = new Link(iwc.getIWMainApplication().getBundle(this.IW_CORE_BUNDLE_IDENTIFIER).getImage("shared/create.gif", _iwrb.getLocalizedString("new_quote", "New Quote")));
		link.setWindowToOpen(QuoteEditor.class);
		link.addParameter(QuoteBusiness.PARAMETER_MODE, QuoteBusiness.PARAMETER_NEW);
		link.addParameter(QuoteBusiness.PARAMETER_OBJECT_INSTANCE_ID, _objectID);
		return link;
	}

	private Link getEditLink(IWContext iwc) {
		Link link = new Link(iwc.getIWMainApplication().getBundle(this.IW_CORE_BUNDLE_IDENTIFIER).getImage("shared/edit.gif", _iwrb.getLocalizedString("edit_quote", "Edit Quote")));
		link.setWindowToOpen(QuoteEditor.class);
		link.addParameter(QuoteBusiness.PARAMETER_MODE, QuoteBusiness.PARAMETER_EDIT);
		link.addParameter(QuoteBusiness.PARAMETER_QUOTE_ID, _quoteID);
		link.addParameter(QuoteBusiness.PARAMETER_OBJECT_INSTANCE_ID, _objectID);
		return link;
	}

	private Link getDeleteLink(IWContext iwc) {
		Link link = new Link(iwc.getIWMainApplication().getBundle(this.IW_CORE_BUNDLE_IDENTIFIER).getImage("shared/delete.gif", _iwrb.getLocalizedString("delete_quote", "Delete Quote")));
		link.setWindowToOpen(QuoteEditor.class);
		link.addParameter(QuoteBusiness.PARAMETER_MODE, QuoteBusiness.PARAMETER_DELETE);
		link.addParameter(QuoteBusiness.PARAMETER_QUOTE_ID, _quoteID);
		link.addParameter(QuoteBusiness.PARAMETER_OBJECT_INSTANCE_ID, _objectID);
		return link;
	}

	private void setDefaultValues() {
		width_ = "150";
	}

	public String getBundleIdentifier() {
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
			if (this._myTable != null) {
				obj._myTable = (Table) this._myTable.clone();
			}
		}
		catch (Exception ex) {
			ex.printStackTrace(System.err);
		}
		return obj;
	}

	private QuoteBusiness getQuoteBusiness() {
		return QuoteBusiness.getQuoteBusinessInstace();
	}

	/**
	 * @see com.idega.presentation.Block#getStyleNames()
	 */
	public Map getStyleNames() {
		HashMap map = new HashMap();
		String[] styleNames = { QUOTE_STYLE_NAME, ORIGIN_STYLE_NAME, AUTHOR_STYLE_NAME };
		String[] styleValues = { QUOTE_STYLE, ORIGIN_STYLE, AUTHOR_STYLE };

		for (int a = 0; a < styleNames.length; a++) {
			map.put(styleNames[a], styleValues[a]);
		}

		return map;
	}

	/** @deprecated */
	public void setQuoteWidth(String width) {
		setWidth(width);
	}
	/** @deprecated */
	public void setQuoteWidth(int width) {
		setWidth(Integer.toString(width));
	}
	/** @deprecated */
	public void setQuoteHeight(String height) {
		setHeight(height);
	}
	/** @deprecated */
	public void setQuoteHeight(int height) {
		setHeight(Integer.toString(height));
	}
	/** @deprecated */
	public void setQuoteOriginStyle(String style) {
		setOriginStyle(style);
	}
	/** @deprecated */
	public void setQuoteTextStyle(String style) {
		setTextStyle(style);
	}
	/** @deprecated */
	public void setQuoteAuthorStyle(String style) {
		setAuthorStyle(style);
	}
	/** @deprecated */
	public void setQuoteOriginSize(String size) {
	}
	/** @deprecated */
	public void setQuoteOriginSize(int size) {
	}
	/** @deprecated */
	public void setQuoteOriginColor(String color) {
	}
	/** @deprecated */
	public void setQuoteOriginFace(String face) {
	}
	/** @deprecated */
	public void setQuoteTextSize(String size) {
	}
	/** @deprecated */
	public void setQuoteTextSize(int size) {
	}
	/** @deprecated */
	public void setQuoteTextColor(String color) {
	}
	/** @deprecated */
	public void setQuoteTextFace(String face) {
	}
	/** @deprecated */
	public void setQuoteAuthorSize(String size) {
	}
	/** @deprecated */
	public void setQuoteAuthorSize(int size) {
	}
	/** @deprecated */
	public void setQuoteAuthorColor(String color) {
	}
	/** @deprecated */
	public void setQuoteAuthorFace(String face) {
	}
	

	public void setToGetNewQuoteOnEveryReload(boolean fetchFromDatabase) {
		_alwaysFetchFromDatabase = fetchFromDatabase;
	}

	/**
	 * @param author The _showAuthor to set.
	 */
	public void setShowAuthor(boolean author) {
		_showAuthor = author;
	}
	
	/**
	 * @param origin The _showOrigin to set.
	 */
	public void setShowOrigin(boolean origin) {
		_showOrigin = origin;
	}
	
	/**
	 * @param quotes The _showQuotes to set.
	 */
	public void setShowQuotes(boolean quotes) {
		_showQuotes = quotes;
	}
}
