package com.idega.block.boxoffice.presentation;

import java.text.DecimalFormat;

import com.idega.idegaweb.block.presentation.Builderaware;
import com.idega.block.boxoffice.business.BoxBusiness;
import com.idega.block.boxoffice.business.BoxFinder;
import com.idega.block.boxoffice.data.BoxCategory;
import com.idega.block.boxoffice.data.BoxEntity;
import com.idega.block.boxoffice.data.BoxLink;
import com.idega.core.component.data.ICObjectInstance;
import com.idega.core.file.data.ICFile;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.util.IWTimestamp;
import com.idega.util.text.StyleConstants;
import com.idega.util.text.TextSoap;
import com.idega.util.text.TextStyler;

public class Box extends Block implements Builderaware {

	private boolean _showHeaders;
	private static final String _DEFAULT_ICON_PREFIX = "icfileicons/ui/iw/";
	private static final String _DEFAULT_ICON_SUFFIX = ".gif";

	private boolean _showMimeType;
	private boolean _showFileSize;
	private int _boxID = -1;
	private int _boxCategoryID = -1;
	private boolean _isAdmin = false;
	private String _attribute;
	private int _iLocaleID;
	private int _layout = -1;

	public final static int BOX_VIEW = 1;
	public final static int CATEGORY_VIEW = 2;
	public final static int COLLECTION_VIEW = 3;

	public final static String BOX_VIEW_STRING = "BOX VIEW";
	public final static String CATEGORY_VIEW_STRING = "CATEGORY VIEW";

	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.boxoffice";
	protected IWResourceBundle _iwrb;
	protected IWBundle _iwb;
	protected IWBundle _iwbBox;
	private Image _deleteImage;
	private Image _createImage;
	private Image _editImage;
	private Image _detachImage;

	private Table _myTable;
	private boolean _newObjInst = false;
	private boolean _newWithAttribute = false;

	private boolean _styles = true;
	private int _numberOfColumns;
	private String _headerColor;
	private String _borderColor;
	private String _inlineColor;
	private String _boxWidth;
	private String _boxHeight;
	private int _boxSpacing;
	private int _numberOfDisplayed;
	private String _categoryStyle;
	private String _linkStyle;
	private String _visitedStyle;
	private String _activeStyle;
	private String _hoverStyle;
	private String _name;
	private String _headerStyle;
	private String _highlightColor = "#0000FF";
	private boolean _showOnlyBelongingToUser = false;
	private boolean _showCollection = false;

	private String _target;

	public Box() {
		setDefaultValues();
	}

	public Box(int boxID) {
		this();
		_boxID = boxID;
	}

	public Box(String attribute) {
		this();
		_attribute = attribute;
	}

	public void main(IWContext iwc) throws Exception {
		_iwrb = getResourceBundle(iwc);
		_iwbBox = getBundle(iwc);
		_iwb = iwc.getIWMainApplication().getBundle(IW_CORE_BUNDLE_IDENTIFIER);

		_createImage = _iwb.getImage("shared/create.gif");
		_deleteImage = _iwb.getImage("shared/delete.gif");
		_editImage = _iwb.getImage("shared/edit.gif");
		_detachImage = _iwb.getImage("shared/detach.gif");

		_isAdmin = iwc.hasEditPermission(this);
		//_isAdmin = true;
		_iLocaleID = ICLocaleBusiness.getLocaleId(iwc.getCurrentLocale());

		iwc.removeApplicationAttribute(BoxBusiness.PARAMETER_LINK_ID);
		iwc.removeApplicationAttribute(BoxBusiness.PARAMETER_NEW_OBJECT_INSTANCE);

		BoxEntity box = null;

		_myTable = new Table(1, 2);
		_myTable.setCellpadding(0);
		_myTable.setCellspacing(0);
		_myTable.setBorder(0);

		if (_boxID <= 0) {
			String sBoxID = iwc.getParameter(BoxBusiness.PARAMETER_BOX_ID);
			if (sBoxID != null)
				_boxID = Integer.parseInt(sBoxID);
			else if (getICObjectInstanceID() > 0) {
				_boxID = BoxFinder.getRelatedEntityId(getICObjectInstance());
				if (_boxID <= 0) {
					BoxBusiness.saveBox(_boxID, getICObjectInstanceID(), null);
					_newObjInst = true;
				}
			}
		}

		if (_newObjInst) {
			_boxID = BoxFinder.getRelatedEntityId(((com.idega.core.component.data.ICObjectInstanceHome) com.idega.data.IDOLookup.getHomeLegacy(ICObjectInstance.class)).findByPrimaryKeyLegacy(getICObjectInstanceID()));
		}

		if (_boxID > 0) {
			box = BoxFinder.getBox(_boxID);
		}
		else if (_attribute != null) {
			box = BoxFinder.getBox(_attribute);
			if (box != null) {
				_boxID = box.getID();
			}
			else {
				BoxBusiness.saveBox(-1, -1, _attribute);
			}
			_newWithAttribute = true;
		}

		if (_newWithAttribute) {
			_boxID = BoxFinder.getBox(_attribute).getID();
		}

		if (iwc.getParameter(BoxBusiness.PARAMETER_CATEGORY_ID) != null) {
			try {
				_boxCategoryID = Integer.parseInt(iwc.getParameter(BoxBusiness.PARAMETER_CATEGORY_ID));
			}
			catch (NumberFormatException e) {
				_boxCategoryID = -1;
			}
		}

		int row = 1;
		if (_isAdmin) {
			_myTable.add(getAdminPart(), 1, row);
			row++;
		}

		_myTable.add(getBox(box, iwc), 1, row);
		add(_myTable);
	}

	private Table getBox(BoxEntity box, IWContext iwc) {
		setStyles();

		Table boxTable = new Table();
		boxTable.setCellpadding(0);
		boxTable.setCellspacing(_boxSpacing);

		BoxCategory[] categories = BoxFinder.getCategoriesInBox(box);
		if (categories != null) {
			switch (_layout) {
				case BOX_VIEW :
					getBoxView(box, categories, boxTable,iwc);
					break;
				case CATEGORY_VIEW :
					boxTable.setWidth(_boxWidth);
					getCategoryView(box, categories, boxTable, iwc);
					break;
				case COLLECTION_VIEW :
					boxTable.setWidth(_boxWidth);
					boxTable.setCellspacing(0);
					getCollectionView(box, categories, boxTable, iwc);
					break;
			}
		}

		return boxTable;
	}

	private void getBoxView(BoxEntity box, BoxCategory[] categories, Table boxTable, IWContext iwc) {
		int row = 1;
		int column = 1;

		for (int a = 0; a < categories.length; a++) {
			String categoryString = BoxBusiness.getLocalizedString(categories[a], _iLocaleID);
			if (categoryString == null) {
				categoryString = "$language$";
			}

			Text categoryText = new Text(categoryString);
			categoryText.setFontStyle(_categoryStyle);

			Table table = new Table();
			table.setCellpadding(3);
			table.setCellspacing(1);
			table.setWidth(_boxWidth);
			table.setHeight(_boxHeight);
			table.setHeight(2, "100%");
			table.setColor(_borderColor);
			table.setColor(1, 1, _headerColor);
			table.setColor(1, 2, _inlineColor);
			table.setAlignment(1, 1, "center");
			table.setVerticalAlignment(1, 1, "middle");
			table.setVerticalAlignment(1, 2, "top");

			table.add(categoryText, 1, 1);

			Table linksTable = new Table();
			linksTable.setRows(_numberOfDisplayed + 1);
			linksTable.setWidth("100%");
			if (_isAdmin)
				linksTable.setHeight("100%");
			table.add(linksTable, 1, 2);

			int linkRow = 1;

			BoxLink[] links = null;
//			System.out.println("Getting links in getBoxView()");
//			System.out.println("_showOnlyBelongingToUser = " + _showOnlyBelongingToUser);
			if (_showOnlyBelongingToUser)
				links = BoxFinder.getLinksInBoxByUser(box, categories[a],iwc.getUserId());
			else
				links = BoxFinder.getLinksInBox(box, categories[a]);
			int linksLength = _numberOfDisplayed;
			if (links != null) {
				if (links.length < linksLength) {
					linksLength = links.length;
				}

				for (int b = 0; b < linksLength; b++) {
					Link link = getLink(links[b]);
					if (link != null) {
						linksTable.add(link, 1, linkRow);
						linksTable.setWidth(1, linkRow, "100%");

						if (_isAdmin) {
							linksTable.add(getEditLink(links[b].getID()), 2, linkRow);
							linksTable.add(getDeleteLink(links[b].getID()), 2, linkRow);
						}
						linkRow++;
					}
				}

				if (_isAdmin) {
					linksTable.add(getAddLink(categories[a].getID()), 1, _numberOfDisplayed + 1);
					linksTable.setHeight(1, _numberOfDisplayed + 1, "100%");
					linksTable.setVerticalAlignment(1, _numberOfDisplayed + 1, "bottom");
				}
			}

			if (column % _numberOfColumns == 0) {
				boxTable.add(table, column, row);
				row++;
				column = 1;
			}
			else {
				boxTable.add(table, column, row);
				column++;
			}
		}
	}

	private void getCategoryView(BoxEntity box, BoxCategory[] categories, Table boxTable, IWContext iwc) {
		int row = 1;

		Table categoryTable = new Table(2, categories.length);
		categoryTable.setCellpadding(1);
		categoryTable.setCellspacing(0);
		categoryTable.setWidth(2, "100%");

		TextStyler styler = new TextStyler(_categoryStyle);
		styler.setStyleValue(StyleConstants.ATTRIBUTE_COLOR, _highlightColor);

		for (int a = 0; a < categories.length; a++) {
			if (a == 0 && _boxCategoryID == -1)
				_boxCategoryID = categories[a].getID();

			String categoryString = BoxBusiness.getLocalizedString(categories[a], _iLocaleID);
			if (categoryString == null) {
				categoryString = "$language$";
			}

			Text categoryText = new Text(categoryString);
			if (_boxCategoryID == categories[a].getID())
				categoryText.setFontStyle(styler.getStyleString());
			else
				categoryText.setFontStyle(_categoryStyle);

			Image categoryImage = _iwbBox.getImage("shared/category.gif");

			Link categoryImageLink = new Link(categoryImage);
			categoryImageLink.addParameter(BoxBusiness.PARAMETER_CATEGORY_ID, categories[a].getID());
			Link categoryLink = new Link(categoryText);
			categoryLink.addParameter(BoxBusiness.PARAMETER_CATEGORY_ID, categories[a].getID());

			categoryTable.add(categoryImageLink, 1, a + 1);
			categoryTable.add(categoryLink, 2, a + 1);
		}

		BoxCategory category = BoxFinder.getCategory(_boxCategoryID);
		boxTable.add(categoryTable, 1, row);
		row++;
		boxTable.setHeight(1, row, "5");
		row++;

		if (_boxCategoryID != -1) {
			int linkRow = 1;

			BoxLink[] links = null;
			System.out.println("Getting links in getCategoryView()");
			System.out.println("_showOnlyBelongingToUser = " + _showOnlyBelongingToUser);
			
			if (_showOnlyBelongingToUser)
				links = BoxFinder.getLinksInBoxByUser(box, category,iwc.getUserId());
			else
				links = BoxFinder.getLinksInBox(box, category);
			
			if (links != null && category != null) {
				Table linksTable = new Table();
				linksTable.setCellpadding(0);
				linksTable.setCellspacing(1);
				linksTable.setWidth("100%");
				linksTable.setWidth(1, "100%");

				Text documentNameText = new Text(_iwrb.getLocalizedString("document_name", "Document name"));
				documentNameText.setFontStyle(_linkStyle + " font-weight: bold;");
				linksTable.add(documentNameText, 1, linkRow);

				Text documentDateText = new Text(_iwrb.getLocalizedString("document_date", "Date"));
				documentDateText.setFontStyle(_linkStyle + " font-weight: bold;");
				linksTable.add(documentDateText, 2, linkRow);
				linkRow++;

				for (int b = 0; b < links.length; b++) {
					Link link = getLink(links[b]);
					IWTimestamp stamp = new IWTimestamp(links[b].getCreationDate());

					if (link != null) {
						linksTable.add(link, 1, linkRow);

						Text dateText = new Text(TextSoap.addZero(stamp.getDay()) + "." + TextSoap.addZero(stamp.getMonth()) + "." + Integer.toString(stamp.getYear()));
						dateText.setFontStyle(_linkStyle);
						linksTable.add(dateText, 2, linkRow);

						if (_isAdmin) {
							linksTable.add(getEditLink(links[b].getID()), 3, linkRow);
							linksTable.add(getDeleteLink(links[b].getID()), 3, linkRow);
						}
						linkRow++;
					}
				}
				boxTable.add(linksTable, 1, row);
				row++;
			}
		}
		if (_isAdmin && _boxCategoryID != -1) {
			boxTable.add(getAddLink(category.getID()), 1, row);
		}
	}

	private void getCollectionView(BoxEntity box, BoxCategory[] categories, Table boxTable, IWContext iwc) {
		int row = 1;

		Image image = Table.getTransparentCell(iwc);
		image.setHeight(_boxSpacing);

		for (int a = 0; a < categories.length; a++) {
			String categoryString = BoxBusiness.getLocalizedString(categories[a], _iLocaleID);
			if (categoryString == null) {
				categoryString = "$language$";
			}

			Text categoryText = new Text(categoryString);
			categoryText.setFontStyle(_categoryStyle);

			Table table = new Table();
			table.setWidth("100%");
			table.setCellspacing(0);
			table.setCellpadding(1);

			table.add(categoryText, 1, 1);

			int linkRow = 2;
			int column = 1;
			
			if (_showHeaders) {
				Text nameHeader = new Text(_iwrb.getLocalizedString("link_name", "Name"));
				if (_headerStyle != null)
					nameHeader.setStyleAttribute(_headerStyle);
				table.add(nameHeader, column++, linkRow);

				if (_showFileSize) {
					Text fileSizeHeader = new Text(_iwrb.getLocalizedString("link_file_size", "File size"));
					if (_headerStyle != null)
						fileSizeHeader.setStyleAttribute(_headerStyle);
					table.setWidth(column++, linkRow, 12);
					table.add(fileSizeHeader, column++, linkRow);
					
					if (_showMimeType) {
						Text mimetypeHeader = new Text(_iwrb.getLocalizedString("link_mimetype", "Mimetype"));
						if (_headerStyle != null)
							mimetypeHeader.setStyleAttribute(_headerStyle);
						table.setWidth(column++, linkRow, 12);
						table.add(mimetypeHeader, column, linkRow);
					}
				}
				linkRow++;
			}

			BoxLink[] links = null;
			System.out.println("Getting links in getCollectionView()");
			System.out.println("_showOnlyBelongingToUser = " + _showOnlyBelongingToUser);
			
			if (_showCollection) {
				links = BoxFinder.getLinksInCategory(categories[a]);
			}
			else {
				if (_showOnlyBelongingToUser)
					links = BoxFinder.getLinksInBoxByUser(box, categories[a], iwc.getUserId());
				else
					links = BoxFinder.getLinksInBox(box, categories[a]);
			}
			if (links != null) {
				for (int b = 0; b < links.length; b++) {
					column = 1;
					Link link = getLink(links[b]);
					if (link != null) {
						table.add(link, column++, linkRow);
						
						if (_showFileSize) {
							ICFile file = links[b].getFile();
							
							double size = 0;
							try {
								size = (double) file.getFileSize().intValue() / (double) 1024;
							}
							catch (Exception e) {
								size = 0;
							}
							DecimalFormat format = new DecimalFormat("0.0 KB");
							
							Text fileSize = new Text(format.format(size));
							fileSize.setStyle(_name);
							table.setWidth(column++, linkRow, 12);
							table.setAlignment(column, linkRow, Table.HORIZONTAL_ALIGN_RIGHT);
							table.add(fileSize, column++, linkRow);
							
							if (_showMimeType) {
								String mimeType = null;
								try {
									mimeType = file.getMimeType();
									mimeType = mimeType.replace('\\', '_');
									mimeType = mimeType.replace('/', '_');
									mimeType = mimeType.replace(':', '_');
									mimeType = mimeType.replace('*', '_');
									mimeType = mimeType.replace('?', '_');
									mimeType = mimeType.replace('<', '_');
									mimeType = mimeType.replace('>', '_');
									mimeType = mimeType.replace('|', '_');
									mimeType = mimeType.replace('\"', '_');
								}
								catch (Exception e) {
									mimeType = null;
								}
 
								if (mimeType != null) {
									Image mime = _iwb.getImage(_DEFAULT_ICON_PREFIX+mimeType+_DEFAULT_ICON_SUFFIX);
									table.setWidth(column++, linkRow, 12);
									table.add(mime, column++, linkRow);
								}
							}
						}

						if (_isAdmin) {
							table.setWidth(column++, linkRow, 5);
							table.add(getEditLink(links[b].getID()), column, linkRow);
							table.add(getDeleteLink(links[b].getID()), column, linkRow);
						}
						linkRow++;
					}
				}

				if (_isAdmin) {
					table.add(getAddLink(categories[a].getID()), 1, linkRow);
				}
			}

			boxTable.add(table, 1, row);
			row++;

			boxTable.add(image, 1, row);
			row++;
		}
	}

	private Table getAdminPart() {
		Table adminTable = new Table();
		adminTable.setCellpadding(0);
		adminTable.setCellspacing(0);

		Link adminLink = new Link(_createImage);
		adminLink.setWindowToOpen(BoxEditorWindow.class);
		adminLink.addParameter(BoxBusiness.PARAMETER_BOX_ID, _boxID);
		adminLink.addParameter(BoxBusiness.PARAMETER_NEW_OBJECT_INSTANCE, BoxBusiness.PARAMETER_TRUE);
		adminTable.add(adminLink, 1, 1);

		Link categoryLink = new Link(_editImage);
		categoryLink.setWindowToOpen(BoxCategoryEditor.class);
		categoryLink.addParameter(BoxBusiness.PARAMETER_BOX_ID, _boxID);
		adminTable.add(categoryLink, 2, 1);

		Link detachLink = new Link(_detachImage);
		detachLink.setWindowToOpen(BoxCategoryChooser.class);
		detachLink.addParameter(BoxBusiness.PARAMETER_BOX_ID, _boxID);
		adminTable.add(detachLink, 2, 1);

		return adminTable;
	}

	private Link getLink(BoxLink boxLink) {
		String linkString = BoxBusiness.getLocalizedString(boxLink, _iLocaleID);
		if (linkString != null) {
			Link link = new Link(linkString);
			if (_styles) {
				link.setStyle(_name);
			}
			else {
				link.setFontSize(1);
			}
			link.setOnMouseOver("window.status='" + linkString + "'; return true;");
			link.setOnMouseOut("window.status=''; return true;");

			String URL = boxLink.getURL();
			int fileID = boxLink.getFileID();
			int pageID = boxLink.getPageID();
			String target = boxLink.getTarget();

			if (URL != null) {
				if (URL.indexOf("http://") == -1) {
					URL = "http://" + URL;
				}
				link.setURL(URL);
			}
			else if (fileID != -1) {
				link.setFile(fileID);
			}
			else if (pageID != -1) {
				link.setPage(pageID);
			}
			if (target != null) {
				link.setTarget(target);
			}
			return link;
		}
		return null;
	}

	private Link getAddLink(int categoryID) {
		Link addLink = new Link(_createImage);
		addLink.setWindowToOpen(BoxEditorWindow.class);
		addLink.addParameter(BoxBusiness.PARAMETER_BOX_ID, _boxID);
		addLink.addParameter(BoxBusiness.PARAMETER_CATEGORY_ID, categoryID);
		addLink.addParameter(BoxBusiness.PARAMETER_NEW_OBJECT_INSTANCE, BoxBusiness.PARAMETER_TRUE);
		return addLink;
	}

	private Link getEditLink(int linkID) {
		Link editLink = new Link(_editImage);
		editLink.setWindowToOpen(BoxEditorWindow.class);
		editLink.addParameter(BoxBusiness.PARAMETER_LINK_ID, linkID);
		editLink.addParameter(BoxBusiness.PARAMETER_BOX_ID, _boxID);
		return editLink;
	}

	private Link getDeleteLink(int linkID) {
		Link deleteLink = new Link(_deleteImage);
		deleteLink.setWindowToOpen(BoxEditorWindow.class);
		deleteLink.addParameter(BoxBusiness.PARAMETER_LINK_ID, linkID);
		deleteLink.addParameter(BoxBusiness.PARAMETER_BOX_ID, _boxID);
		deleteLink.addParameter(BoxBusiness.PARAMETER_DELETE, BoxBusiness.PARAMETER_TRUE);
		return deleteLink;
	}

	private void setDefaultValues() {
		_layout = BOX_VIEW;
		_numberOfColumns = 3;
		_headerColor = "#D8D8D8";
		_borderColor = "#6E6E6E";
		_inlineColor = "#FFFFFF";
		_boxWidth = "120";
		_boxHeight = "120";
		_boxSpacing = 3;
		_numberOfDisplayed = 4;
		_categoryStyle = "font-family: Arial, Helvetica, sans-serif; font-size: 8pt; font-weight: bold";
		_linkStyle = "font-family: Arial, Helvetica,sans-serif; font-size: 8pt; color: #000000;";
		_visitedStyle = "font-family: Arial, Helvetica,sans-serif; font-size: 8pt; color: #000000;";
		_activeStyle = "font-family: Arial, Helvetica,sans-serif; font-size: 8pt; color: #000000;";
		_hoverStyle = "font-family: Arial, Helvetica,sans-serif; font-size: 8pt; color: #000000;";
		_target = Link.TARGET_TOP_WINDOW;
	}

	public void setNumberOfColumns(int columns) {
		_numberOfColumns = columns;
	}

	public void setHeaderColor(String color) {
		_headerColor = color;
	}

	public void setBorderColor(String color) {
		_borderColor = color;
	}

	public void setInlineColor(String color) {
		_inlineColor = color;
	}

	/**
	 * @deprecated
	 */
	public void setBoxWidth(String width) {
		_boxWidth = width;
	}

	/**
	 * @deprecated
	 */
	public void setBoxHeight(String height) {
		_boxHeight = height;
	}

	public void setWidth(String width) {
		_boxWidth = width;
	}

	public void setWidth(int width) {
		setWidth(Integer.toString(width));
	}

	public void setHeight(String height) {
		_boxHeight = height;
	}

	public void setHeight(int height) {
		setHeight(Integer.toString(height));
	}

	public void setLayout(int layout) {
		_layout = layout;
	}

	public void setBoxSpacing(int spacing) {
		_boxSpacing = spacing;
	}

	public void setNumberOfDisplayed(int number) {
		_numberOfDisplayed = number;
	}

	public void setCategoryStyle(String style) {
		_categoryStyle = style;
	}

	public void setTarget(String target) {
		_target = target;
	}

	public void setShowOnlyBelongingToUser(boolean show) {
		_showOnlyBelongingToUser = show;
	}
	
	public boolean getShowOnlyBelongingToUser() {
		return _showOnlyBelongingToUser;
	}

	/**
	 * @deprecated
	 */
	public void setLinkStyle(String linkStyle, String activeStyle, String visitedStyle, String hoverStyle) {
		_linkStyle = linkStyle;
		_visitedStyle = linkStyle;
		_activeStyle = visitedStyle;
		_hoverStyle = hoverStyle;
	}

	public void setLinkStyle(String linkStyle, String hoverStyle) {
		_linkStyle = linkStyle;
		_hoverStyle = hoverStyle;
	}

	public void setSelectedCategoryColor(String color) {
		_highlightColor = color;
	}

	private void setStyles() {
		if (_name == null)
			_name = this.getName();
		if (_name == null) {
			if (_attribute == null)
				_name = "boxoffice_" + Integer.toString(_boxID);
			else
				_name = "boxoffice_" + _attribute;
		}

		if (getParentPage() != null) {
			getParentPage().setStyleDefinition("A." + _name, _linkStyle);
			getParentPage().setStyleDefinition("A." + _name + ":hover", _hoverStyle);
		}
		else {
			_styles = false;
		}
	}

	public boolean deleteBlock(int ICObjectInstanceId) {
		return BoxBusiness.deleteBox(ICObjectInstanceId);
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	public Object clone() {
		Box obj = null;
		try {
			obj = (Box) super.clone();

			if (this._myTable != null) {
				obj._myTable = (Table) this._myTable.clone();
			}
		}
		catch (Exception ex) {
			ex.printStackTrace(System.err);
		}
		return obj;
	}

	/**
	 * @param b
	 */
	public void setShowFileSize(boolean b) {
		_showFileSize = b;
	}

	/**
	 * @param b
	 */
	public void setShowMimeType(boolean b) {
		_showMimeType = b;
	}

	/**
	 * @param b
	 */
	public void setShowHeaders(boolean b) {
		_showHeaders = b;
	}

	public void setShowAllLinksInCategories(boolean showAllLinks) {
		_showCollection = showAllLinks;
	}
}