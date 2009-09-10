package com.idega.block.dictionary.presentation;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.ejb.FinderException;
import com.idega.block.category.data.ICCategory;
import com.idega.block.category.presentation.CategoryBlock;
import com.idega.block.dictionary.business.DictionaryBusiness;
import com.idega.block.dictionary.business.DictionaryComparator;
import com.idega.block.dictionary.data.Word;
import com.idega.data.IDOException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.block.presentation.Builderaware;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.util.text.TextSoap;

public class Dictionary extends CategoryBlock implements Builderaware {

	private boolean _isAdmin = false;
	private Table _myTable;

	private int _objectID = -1;
	private int _state = -1;
	private boolean _stateSet = false;
	private int _wordID = -1;
	private int _numberOfColumns = 3;

	private boolean _styles = true;
	private boolean _showCategoryName = true;
	private String _width;
	private String _textStyle;
	private String _headerStyle;

	private String _linkStyle;
	private String _linkHoverStyle;
	private String _linkName;

	private DictionaryBusiness _dicBusiness;
	private Image _divider;

	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.dictionary";
	protected IWResourceBundle _iwrb;
	protected IWBundle _iwb;
	protected IWBundle _iwcb;

	public Dictionary() {
		setDefaultValues();
	}

	public void main(IWContext iwc) throws Exception {
		this._iwrb = getResourceBundle(iwc);
		this._iwb = getBundle(iwc);
		this._iwcb = iwc.getIWMainApplication().getBundle(IW_CORE_BUNDLE_IDENTIFIER);

		this._objectID = getICObjectInstanceID();
		this._isAdmin = iwc.hasEditPermission(this);
		this._dicBusiness = DictionaryBusiness.getDictionaryBusinessInstace();
		this._divider = this._iwb.getImage("shared/dotted.gif");
		getParameters(iwc);

		this._myTable = new Table();
		this._myTable.setCellpadding(0);
		this._myTable.setCellspacing(0);
		this._myTable.setBorder(0);
		this._myTable.setWidth(this._width);

		int row = 1;
		if (this._isAdmin) {
			this._myTable.add(getAdminPart(iwc), 1, row);
			row++;
		}

		this._myTable.add(getDictionaryViewer(iwc), 1, row);
		add(this._myTable);
	}

	private Table getDictionaryViewer(IWContext iwc) throws FinderException, RemoteException, IDOException {
		Table table = null;
		setStyles();

		switch (this._state) {
			case DictionaryBusiness.WORD_VIEW :
				table = getWordView(iwc);
				break;
			case DictionaryBusiness.CATEGORY_COLLECTION :
				table = getCategoryCollection(iwc);
				break;
			case DictionaryBusiness.RANDOM_WORD :
				table = getRandomWord(iwc);
				break;
		}
		return table;
	}

	private Table getCategoryCollection(IWContext iwc) throws FinderException, RemoteException {
		List collection = new Vector(getCategories());

		if (collection != null && collection.size() > 0) {
			Collections.sort(collection,new DictionaryComparator(iwc.getCurrentLocale(),DictionaryComparator.CATEGORY_NAME));
			Table table = new Table();
			table.setCellpaddingAndCellspacing(0);
			table.setWidth(Table.HUNDRED_PERCENT);
			Table wordTable;
			int row = 1;

			Iterator iter = collection.iterator();
			while (iter.hasNext()) {
				ICCategory category = (ICCategory) iter.next();
				if (this._showCategoryName) {
					table.setHeight(row, "16");
					table.add(formatText(category.getName(), this._headerStyle), 1, row++);
					table.setBackgroundImage(1, row++, this._divider);
				}

				List words = new Vector(this._dicBusiness.getWordHome().findAllWordsByCategory(((Integer) category.getPrimaryKey()).intValue()));
				if (words != null && words.size() > 0) {
					Collections.sort(words, new DictionaryComparator(DictionaryComparator.WORD_NAME));
					wordTable = new Table();
					wordTable.setColumns(this._numberOfColumns);
					wordTable.setWidth(Table.HUNDRED_PERCENT);
					for (int a = 1; a <= this._numberOfColumns; a++) {
						wordTable.setWidth(a, String.valueOf(100 / this._numberOfColumns) + "%");
					}

					int wordRow = 1;
					int wordColumn = 1;
					int size = words.size();
					int switchColumn = size / this._numberOfColumns;
					if (size % this._numberOfColumns > 0) {
						switchColumn++;
					}
					if (size <= this._numberOfColumns) {
						switchColumn = 1;
					}

					Iterator iter2 = words.iterator();
					while (iter2.hasNext()) {
						Word word = (Word) iter2.next();
						wordTable.add(getWordLink(word), wordColumn, wordRow);
						if (wordRow == switchColumn) {
							wordRow = 1;
							wordColumn++;
						}
						else {
							wordRow++;
						}
					}
					table.add(wordTable, 1, row++);
					table.setHeight(row++, "16");
				}
			}

			return table;
		}
		else {
			return new Table();
		}
	}

	private Table getWordView(IWContext iwc) throws FinderException, RemoteException {
		Table table = new Table();
		table.setWidth(Table.HUNDRED_PERCENT);

		if (this._wordID != -1) {
			int row = 1;
			Word word = this._dicBusiness.getWord(this._wordID);
			if (word != null) {
				table.add(formatText(word.getWord(), this._headerStyle), 1, row++);
				if (word.getImageID() != -1) {
					Image image = this._dicBusiness.getImage(word.getImageID());
					image.setHorizontalSpacing(8);
					image.setVerticalSpacing(4);
					image.setAlignment(Image.ALIGNMENT_RIGHT);
					table.add(image, 1, row);
				}
				table.add(formatText(TextSoap.formatText(word.getDescription())), 1, row++);
				if (this._state != DictionaryBusiness.RANDOM_WORD) {
					table.add(getBackLink(), 1, row++);
				}

				if (this._isAdmin) {
					table.add(getAdminButtons(this._wordID), 1, row);
				}
			}
			else {
				return getCategoryCollection(iwc);
			}
		}
		return table;
	}

	private Table getRandomWord(IWContext iwc) throws FinderException, RemoteException {
		int[] categories = getCategoryIds();
		Word word = this._dicBusiness.getRandomWord(categories);
		if (word != null) {
			this._wordID = ((Integer) word.getPrimaryKey()).intValue();
			return getWordView(iwc);
		}
		else {
			return new Table();
		}
	}

	private Table getAdminButtons(int wordID) {
		Table table = new Table(2, 1);
		table.setCellpaddingAndCellspacing(0);

		Image editImage = this._iwcb.getImage("shared/edit.gif");
		editImage.setAlt(this._iwrb.getLocalizedString("edit", "Edit"));
		Link editLink = new Link(editImage);
		editLink.setWindowToOpen(WordEditor.class);
		editLink.addParameter(DictionaryBusiness.PARAMETER_MODE, DictionaryBusiness.PARAMETER_EDIT);
		editLink.addParameter(DictionaryBusiness.PARAMETER_WORD_ID, wordID);
		Image deleteImage = this._iwcb.getImage("shared/delete.gif");
		deleteImage.setAlt(this._iwrb.getLocalizedString("delete", "Delete"));
		Link deleteLink = new Link(this._iwcb.getImage("shared/delete.gif"));
		deleteLink.setWindowToOpen(WordEditor.class);
		deleteLink.addParameter(DictionaryBusiness.PARAMETER_MODE, DictionaryBusiness.PARAMETER_DELETE);
		deleteLink.addParameter(DictionaryBusiness.PARAMETER_WORD_ID, wordID);
		table.add(editLink, 1, 1);
		table.add(deleteLink, 2, 1);

		return table;
	}

	private Link getWordLink(Word word) {
		try {
			Link link = new Link(word.getWord());
			link.addParameter(DictionaryBusiness.PARAMETER_STATE, DictionaryBusiness.WORD_VIEW);
			link.addParameter(DictionaryBusiness.PARAMETER_WORD_ID, ((Integer) word.getPrimaryKey()).intValue());
			if (this._styles) {
				link.setStyle(this._linkName);
			}
			return link;
		}
		catch (RemoteException e) {
			return null;
		}
	}

	private void setStyles() {
		if (this._linkName == null) {
			this._linkName = "dicLink_" + this._objectID;
		}

		if (getParentPage() != null) {
			getParentPage().setStyleDefinition("A." + this._linkName, this._linkStyle);
			getParentPage().setStyleDefinition("A." + this._linkName + ":hover", this._linkHoverStyle);
		}
		else {
			this._styles = false;
		}
	}

	private Text formatText(String textString) {
		return formatText(textString, this._textStyle);
	}

	private Text formatText(String textString, String style) {
		Text text = new Text(textString);
		text.setFontStyle(style);
		return text;
	}

	private Table getAdminPart(IWContext iwc) {
		Table table = new Table(2, 1);
		table.setCellpaddingAndCellspacing(0);

		Image addImage = this._iwcb.getImage("shared/create.gif");
		addImage.setAlt(this._iwrb.getLocalizedString("add_book", "Add book"));
		Link addLink = new Link(addImage);
		addLink.addParameter(DictionaryBusiness.PARAMETER_MODE, DictionaryBusiness.PARAMETER_NEW);
		addLink.setWindowToOpen(WordEditor.class);
		table.add(addLink, 1, 1);

		Image categoryImage = this._iwcb.getImage("shared/edit.gif");
		categoryImage.setAlt(this._iwrb.getLocalizedString("categories", "Categories"));
		Link categoryLink = this.getCategoryLink();
		categoryLink.setPresentationObject(categoryImage);
		table.add(categoryLink, 2, 1);

		return table;
	}

	private Link getBackLink() {
		Link link = new Link(this._iwrb.getLocalizedString("back", "Back"));
		link.setAsBackLink();
		link.setStyle(this._linkName);
		return link;
	}

	private void getParameters(IWContext iwc) {
		if (!this._stateSet) {
			try {
				this._state = Integer.parseInt(iwc.getParameter(DictionaryBusiness.PARAMETER_STATE));
			}
			catch (NumberFormatException e) {
				this._state = DictionaryBusiness.CATEGORY_COLLECTION;
			}
		}

		try {
			this._wordID = Integer.parseInt(iwc.getParameter(DictionaryBusiness.PARAMETER_WORD_ID));
		}
		catch (NumberFormatException e) {
			this._wordID = -1;
		}
	}

	public void setNumberOfColumns(int columns) {
		this._numberOfColumns = columns;
	}

	public void setLayout(int layout) {
		this._state = layout;
		this._stateSet = true;
	}

	public void setTextStyle(String style) {
		this._textStyle = style;
	}

	public void setHeaderStyle(String style) {
		this._headerStyle = style;
	}

	public void setLinkStyle(String style, String hoverStyle) {
		this._linkStyle = style;
		this._linkHoverStyle = hoverStyle;
	}

	public void setWidth(String width) {
		this._width = width;
	}

	public void setShowCategoryName(boolean showName) {
		this._showCategoryName = showName;
	}

	private void setDefaultValues() {
		this._width = Table.HUNDRED_PERCENT;

		this._textStyle = "font-family: Arial,Helvetica,sans-serif; font-size: 11px;";
		this._headerStyle = "font-family: Verdana,Arial,Helvetica,sans-serif; font-size: 11px; font-weight: bold;";
		this._linkStyle = "font-family: Arial,Helvetica,sans-serif; font-size: 11px; text-decoration: underline; color: #000000;";
		this._linkHoverStyle = "font-family: Arial,Helvetica,sans-serif; font-size: 11px; text-decoration: underline; color: #000000;";
	}

	public synchronized Object clone() {
		Dictionary obj = null;
		try {
			obj = (Dictionary) super.clone();

			if (this._myTable != null) {
				obj._myTable = (Table) this._myTable.clone();
			}
			if (this._divider != null) {
				obj._divider = (Image) this._divider.clone();
			}
		}
		catch (Exception ex) {
			ex.printStackTrace(System.err);
		}
		return obj;
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	public String getCategoryType() {
		return "dictionary";
	}

	public boolean getMultible() {
		return true;
	}
}