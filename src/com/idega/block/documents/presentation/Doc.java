package com.idega.block.documents.presentation;

import java.rmi.RemoteException;

import com.idega.block.documents.business.DocBusiness;
import com.idega.block.documents.data.DocLink;
import com.idega.builder.dynamicpagetrigger.business.DPTCopySession;
import com.idega.builder.dynamicpagetrigger.util.DPTInheritable;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.core.category.data.InformationCategory;
import com.idega.core.category.data.InformationFolder;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.block.business.FolderBlockBusiness;
import com.idega.idegaweb.block.presentation.Builderaware;
import com.idega.idegaweb.block.presentation.FolderBlock;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.util.IWTimestamp;
import com.idega.util.text.StyleConstants;
import com.idega.util.text.TextSoap;
import com.idega.util.text.TextStyler;

/**
 *  Description of the Class
 *
 *@author     <a href="gummi@idega.is">Gudmundur Agust Saemundsson</a>
 *@created    15. mars 2002
 *@version    1.0
 */
public class Doc extends FolderBlock implements Builderaware,DPTInheritable {

	private int _folderID = -1;
	private int _catID = -1;
	private boolean _isAdmin = false;
	private String _attribute;
	private int _iLocaleID;
	private int _layout = -1;

	/**
	 *  Description of the Field
	 */
	public final static int BOX_VIEW = 1;
	/**
	 *  Description of the Field
	 */
	public final static int CATEGORY_VIEW = 2;
	/**
	 *  Description of the Field
	 */
	public final static int COLLECTION_VIEW = 3;

	/**
	 *  Description of the Field
	 */
	public final static String BOX_VIEW_STRING = "BOX VIEW";
	/**
	 *  Description of the Field
	 */
	public final static String CATEGORY_VIEW_STRING = "CATEGORY VIEW";

	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.documents";
	/**
	 *  Description of the Field
	 */
	protected IWResourceBundle _iwrb;
	/**
	 *  Description of the Field
	 */
	protected IWBundle _iwbCore;
	/**
	 *  Description of the Field
	 */
	protected IWBundle _iwbDoc;
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
	private String _informationStyle;
	private String _name;
	private String _highlightColor = "#0000FF";

	private String _target;

	private static String _addPermisson = "add";
	//private static String _infoPermission = "info";

	private boolean _hasEditPermission = false;
	private boolean _hasAddPermission = false;
	//private boolean _hasInfoPermission = false;

	/**
	 *  Constructor for the Doc object
	 */
	public Doc() {
		setDefaultValues();
	}

	/**
	 *  Constructor for the Doc object
	 *
	 *@param  folderID  Description of the Parameter
	 */
	public Doc(int folderID) {
		this();
		_folderID = folderID;
	}

	/**
	 *  Constructor for the Doc object
	 *
	 *@param  attribute  Description of the Parameter
	 */
	public Doc(String attribute) {
		this();
		_attribute = attribute;
	}

	public void registerPermissionKeys() {
		registerPermissionKey(_addPermisson);
		//registerPermissionKey(_infoPermission);
	}

	/**
	 *  Description of the Method
	 *
	 *@param  iwc            Description of the Parameter
	 *@exception  Exception  Description of the Exception
	 */
	public void main(IWContext iwc) throws Exception {
		_iwrb = getResourceBundle(iwc);
		_iwbDoc = getBundle(iwc);
		_iwbCore = iwc.getIWMainApplication().getBundle(IW_CORE_BUNDLE_IDENTIFIER);
		

		_createImage = _iwbCore.getImage("shared/create.gif");
		_deleteImage = _iwbCore.getImage("shared/delete.gif");
		_editImage = _iwbCore.getImage("shared/edit.gif");
		_detachImage = _iwbCore.getImage("shared/detach.gif");

		_isAdmin = iwc.hasEditPermission(this);
		//System.out.println("Doc . _isAdmin " + _isAdmin);
		_hasEditPermission = iwc.hasEditPermission(this);

		if (_hasEditPermission) {
			_hasAddPermission = true;
		} else {
			_hasAddPermission = iwc.hasPermission(_addPermisson, this);
		}

		//_isAdmin = true;
		_iLocaleID = ICLocaleBusiness.getLocaleId(iwc.getCurrentLocale());

		iwc.removeApplicationAttribute(DocBusiness.PARAMETER_LINK_ID);
		iwc.removeApplicationAttribute(DocBusiness.PARAMETER_NEW_OBJECT_INSTANCE);

		InformationFolder folder = null;

		if (_folderID < 0) {
			folder = this.getWorkFolder();
			if (folder != null) {
				_folderID = folder.getID();
			}
		} else {
			/**
			 * @todo move from DocFinder to FolderBusiness
			 */
			folder = DocBusiness.getFolder(_folderID);
		}

		_myTable = new Table(1, 2);
		_myTable.setCellpadding(0);
		_myTable.setCellspacing(0);
		//_myTable.setBorder(1);
		
		
		//toolbar
		if(_hasEditPermission){
			Link change = getCategoryLink();
			change.setImage(_iwbCore.getImage("/shared/detach.gif"));
			_myTable.add(change,1,1);
		}
		

		//    if(_folderID <= 0){
		//      String sDocID = iwc.getParameter(DocBusiness.PARAMETER_FOLDER_ID);
		//      if(sDocID != null)
		//        _folderID = Integer.parseInt(sDocID);
		//      else if(getICObjectInstanceID() > 0){
		//        _folderID = DocFinder.getObjectInstanceID(getICObjectInstance());
		//        if(_folderID <= 0 ){
		//          DocBusiness.saveDoc(_folderID,getICObjectInstanceID(),null);
		//          _newObjInst = true;
		//        }
		//      }
		//    }
		//
		//    if ( _newObjInst ) {
		//      _folderID = DocFinder.getObjectInstanceID(new ICObjectInstance(getICObjectInstanceID()));
		//    }
		//
		//    if(_folderID > 0) {
		//      folder = DocFinder.getFolder(_folderID);
		//    }
		//    else if ( _attribute != null ){
		//      folder = DocFinder.getFolder(_attribute);
		//      if ( folder != null ) {
		//        _folderID = folder.getID();
		//      }
		//      else {
		//        DocBusiness.saveDoc(-1,-1,_attribute);
		//      }
		//      _newWithAttribute = true;
		//    }
		//
		//    if ( _newWithAttribute ) {
		//      _folderID = DocFinder.getFolder(_attribute).getID();
		//    }
		
		
		

		if (iwc.getParameter(DocBusiness.PARAMETER_CATEGORY_ID) != null) {
			try {
				_catID = Integer.parseInt(iwc.getParameter(DocBusiness.PARAMETER_CATEGORY_ID));
			} catch (NumberFormatException e) {
				_catID = -1;
			}
		}

		int row = 1;
		if (_hasEditPermission) {
			_myTable.add(getAdminPart(), 1, row);
			row++;
		}

//		System.out.println("_myTable.add(getPresentation(folder,iwc),1,row);: " + folder + "," + iwc + "," + 1 + "," + row);
		_myTable.add(getPresentation(folder, iwc), 1, row);
		add(_myTable);
	}

	/**
	 *  Gets the presentation attribute of the Doc object
	 *
	 *@param  folder  Description of the Parameter
	 *@param  iwc     Description of the Parameter
	 *@return         The presentation value
	 */
	private Table getPresentation(InformationFolder folder, IWContext iwc) {
		setStyles();

		Table boxTable = new Table();
		boxTable.setCellpadding(0);
		boxTable.setCellspacing(_boxSpacing);

		InformationCategory[] categories = this.getCategoriesToView();
		if (categories != null) {
			switch (_layout) {
				case BOX_VIEW :
					System.out.println("getBoxView(folder,categories,boxTable);: " + folder + "," + categories + "," + boxTable);
					getBoxView(folder, categories, boxTable, iwc);
					break;
				case CATEGORY_VIEW :
					boxTable.setWidth(_boxWidth);
					getCategoryView(folder, categories, boxTable, iwc);
					break;
				case COLLECTION_VIEW :
					boxTable.setWidth(_boxWidth);
					boxTable.setCellspacing(0);
					getCollectionView(folder, categories, boxTable, iwc);
					break;
			}
		}

		return boxTable;
	}

	/**
	 *  Gets the boxView attribute of the Doc object
	 *
	 *@param  folder      Description of the Parameter
	 *@param  categories  Description of the Parameter
	 *@param  boxTable    Description of the Parameter
	 */
	private void getBoxView(InformationFolder folder, InformationCategory[] categories, Table boxTable, IWContext iwc) {
		int row = 1;
		int column = 1;
		if (categories != null) {
			for (int a = 0; a < categories.length; a++) {
				//tmp
				String categoryString = categories[a].getName();
				//DocBusiness.getLocalizedString(categories[a],_iLocaleID);
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
				if (_hasEditPermission) {
					linksTable.setHeight("100%");
				}
				table.add(linksTable, 1, 2);

				int linkRow = 1;

				DocLink[] links = DocBusiness.getLinksInFolderCategory(folder, categories[a]);
				int linksLength = _numberOfDisplayed;
				if (links != null) {
					if (links.length < linksLength) {
						linksLength = links.length;
					}

					for (int b = 0; b < linksLength; b++) {
						Link link = getLink(links[b], iwc);
						if (link != null) {
							linksTable.add(link, 1, linkRow);
							linksTable.setWidth(1, linkRow, "100%");

							if (_hasEditPermission || _hasAddPermission) {
								linksTable.add(getEditLink(links[b].getID()), 2, linkRow);
								linksTable.add(getDeleteLink(links[b].getID()), 2, linkRow);
							}
							linkRow++;
						}
					}

					if (_hasAddPermission) {
						linksTable.add(getAddLink(categories[a].getID()), 1, _numberOfDisplayed + 1);
						linksTable.setHeight(1, _numberOfDisplayed + 1, "100%");
						linksTable.setVerticalAlignment(1, _numberOfDisplayed + 1, "bottom");
					}
				}

				if (column % _numberOfColumns == 0) {
					boxTable.add(table, column, row);
					row++;
					column = 1;
				} else {
					boxTable.add(table, column, row);
					column++;
				}
			}
		}
	}

	/**
	 *  Gets the categoryView attribute of the Doc object
	 *
	 *@param  folder      Description of the Parameter
	 *@param  categories  Description of the Parameter
	 *@param  boxTable    Description of the Parameter
	 *@param  iwc         Description of the Parameter
	 */
	private void getCategoryView(InformationFolder folder, InformationCategory[] categories, Table boxTable, IWContext iwc) {
		int row = 1;

		Table categoryTable = new Table();
		categoryTable.setCellpadding(1);
		categoryTable.setCellspacing(0);
		categoryTable.setWidth(2, "100%");
		int icObjectInstanceID = this.getICObjectInstanceID();
		int ic_obj_inst_id = icObjectInstanceID;
		try {
			ic_obj_inst_id = Integer.parseInt(iwc.getParameter(DocBusiness.PARAMETER_OBJECT_INSTANCE_ID));
		} catch (Exception e) {
			ic_obj_inst_id = icObjectInstanceID;
		}

		TextStyler styler = new TextStyler(_categoryStyle);
		styler.setStyleValue(StyleConstants.ATTRIBUTE_COLOR, _highlightColor);
		Image categoryImage = _iwbDoc.getImage("shared/category.gif");

		for (int a = 0; a <= categories.length; a++) {
			if (a == 0 && (_catID == -1 || ic_obj_inst_id != icObjectInstanceID)&&(a < categories.length)) {
				_catID = categories[a].getID();
			}

			//tmp
			String categoryString = null;
			if (a < categories.length) {
				categoryString = categories[a].getName();
				// DocBusiness.getLocalizedString(categories[a],_iLocaleID);
				if (categoryString == null) {
					categoryString = "$language$";
				}
			} else
				categoryString = _iwrb.getLocalizedString("all_documents", "All documents");

			int categoryID = 0;
			if (a < categories.length)
				categoryID = categories[a].getID();

			Text categoryText = new Text(categoryString);
			if (_catID == categoryID) {
				categoryText.setFontStyle(styler.getStyleString());
			} else {
				categoryText.setFontStyle(_categoryStyle);
			}

			Link categoryImageLink = new Link(categoryImage);
			categoryImageLink.addParameter(DocBusiness.PARAMETER_CATEGORY_ID, categoryID);
			categoryImageLink.addParameter(DocBusiness.PARAMETER_OBJECT_INSTANCE_ID, icObjectInstanceID);
			Link categoryLink = new Link(categoryText);
			categoryLink.addParameter(DocBusiness.PARAMETER_CATEGORY_ID, categoryID);
			categoryLink.addParameter(DocBusiness.PARAMETER_OBJECT_INSTANCE_ID, icObjectInstanceID);

			categoryTable.add(categoryImageLink, 1, a + 1);
			categoryTable.add(categoryLink, 2, a + 1);

			int documents = 0;
			if (a < categories.length)
				documents = DocBusiness.getNumberOfLinksInFolderCategory(folder, categories[a]);
			else
				documents = DocBusiness.getNumberOfLinksInFolder(folder);

			Text documentText = new Text(Text.NON_BREAKING_SPACE + "(" + String.valueOf(documents) + Text.NON_BREAKING_SPACE + _iwrb.getLocalizedString("documents", "documents") + ")");
			documentText.setFontStyle(_informationStyle);
			categoryTable.add(documentText, 3, a + 1);
		}

		InformationCategory category = DocBusiness.getCategory(_catID);
		boxTable.add(categoryTable, 1, row);
		row++;
		boxTable.setHeight(1, row, "5");
		row++;

		if (_catID != -1) {
			int linkRow = 1;

			DocLink[] links = null;
			if (_catID != 0)
				links = DocBusiness.getLinksInFolderCategory(folder, category);
			else
				links = DocBusiness.getLinksInFolder(folder);

			if (links != null && ic_obj_inst_id == icObjectInstanceID) {
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
					Link link = getLink(links[b], iwc);
					IWTimestamp stamp = null;
					try {
						stamp = new IWTimestamp(links[b].getCreationDate());
					} catch (NullPointerException n) {}

					if (link != null) {
						linksTable.add(link, 1, linkRow);

						Text dateText = new Text("");
						if (stamp != null) {
							dateText.setText(TextSoap.addZero(stamp.getDay()) + "." + TextSoap.addZero(stamp.getMonth()) + "." + Integer.toString(stamp.getYear()));
						}
						dateText.setFontStyle(_linkStyle);
						linksTable.add(dateText, 2, linkRow);

						if (_hasEditPermission || _hasAddPermission) {
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
		if (_hasAddPermission && _catID != -1 && _catID != 0 && ic_obj_inst_id == icObjectInstanceID) {
			boxTable.add(getAddLink(category.getID()), 1, row);
		}
	}

	/**
	 *  Gets the collectionView attribute of the Doc object
	 *
	 *@param  folder      Description of the Parameter
	 *@param  categories  Description of the Parameter
	 *@param  boxTable    Description of the Parameter
	 *@param  iwc         Description of the Parameter
	 */
	private void getCollectionView(InformationFolder folder, InformationCategory[] categories, Table boxTable, IWContext iwc) {
		int row = 1;

		Image image = Table.getTransparentCell(iwc);
		image.setHeight(_boxSpacing);

		for (int a = 0; a < categories.length; a++) {
			//tmp
			String categoryString = categories[a].getName();
			//DocBusiness.getLocalizedString(categories[a],_iLocaleID);
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

			DocLink[] links = DocBusiness.getLinksInFolderCategory(folder, categories[a]);
			if (links != null) {
				for (int b = 0; b < links.length; b++) {
					Link link = getLink(links[b], iwc);
					if (link != null) {
						table.add(link, 1, linkRow);
						table.setWidth(1, linkRow, "100%");

						if (_hasEditPermission || _hasAddPermission) {
							table.add(getEditLink(links[b].getID()), 2, linkRow);
							table.add(getDeleteLink(links[b].getID()), 2, linkRow);
						}
						linkRow++;
					}
				}

				if (_hasAddPermission) {
					table.add(getAddLink(categories[a].getID()), 1, linkRow);
				}
			}

			boxTable.add(table, 1, row);
			row++;

			boxTable.add(image, 1, row);
			row++;
		}
	}

	/**
	 *  Gets the adminPart attribute of the Doc object
	 *
	 *@return    The adminPart value
	 */
	private Table getAdminPart() {
		Table adminTable = new Table();
		adminTable.setCellpadding(0);
		adminTable.setCellspacing(0);

		//    Link adminLink = new Link(_createImage);
		//      adminLink.setWindowToOpen(DocEditorWindow.class);
		//      addParameter(DocBusiness.PARAMETER_OBJECT_ID,this.getICObjectID());
		//      adminLink.addParameter(DocBusiness.PARAMETER_NEW_OBJECT_INSTANCE,DocBusiness.PARAMETER_TRUE);
		//      adminLink.addParameter(DocBusiness.PARAMETER_OBJECT_INSTANCE_ID, this.getICObjectInstanceID());
		//      if(this.getContentLocaleID() != -1){
		//        adminLink.addParameter(DocBusiness.PARAMETER_CONTENT_LOCALE_ID, this.getContentLocaleID());
		//      }
		//    adminTable.add(adminLink,1,1);

		//    Link categoryLink = new Link(_editImage);
		//      categoryLink.setWindowToOpen(DocCategoryEditor.class);
		//      categoryLink.addParameter(DocBusiness.PARAMETER_BOX_ID,_folderID);
		//    adminTable.add(categoryLink,2,1);
		//
		//    Link detachLink = new Link(_detachImage);
		//      detachLink.setWindowToOpen(DocCategoryChooser.class);
		//      detachLink.addParameter(DocBusiness.PARAMETER_BOX_ID,_folderID);
		//    adminTable.add(detachLink,2,1);
		//
		return adminTable;
	}

	/**
	 *  Gets the link attribute of the Doc object
	 *
	 *@param  docLink  Description of the Parameter
	 *@return          The link value
	 */
	private Link getLink(DocLink docLink, IWContext iwc) {
		String linkString = docLink.getName();
		//DocBusiness.getLocalizedString(boxLink,_iLocaleID);
		if (linkString != null) {
			Link link = new Link(linkString);
			if (_styles) {
				link.setStyle(_name);
			} else {
				link.setFontSize(1);
			}
			link.setOnMouseOver("window.status='" + linkString + "'; return true;");
			link.setOnMouseOut("window.status=''; return true;");

			String URL = docLink.getURL();
			int fileID = docLink.getFileID();
			int pageID = docLink.getPageID();
			String target = docLink.getTarget();

			if (URL != null) {
				if (URL.indexOf("http://") == -1) {
					URL = "http://" + URL;
				}
				link.setURL(URL);
			} else if (fileID != -1) {
				//System.out.println("link.setUrl(Mediabusiness.getMediaUrl(fileID,iwc))");
				link.setFile(fileID);
			} else if (pageID != -1) {
				link.setPage(pageID);
			}
			if (target != null) {
				link.setTarget(target);
			}
			return link;
		}
		return null;
	}

	/**
	 *  Gets the addLink attribute of the Doc object
	 *
	 *@param  categoryID  Description of the Parameter
	 *@return             The addLink value
	 */
	private Link getAddLink(int categoryID) {
		Link addLink = new Link(_createImage);

		addLink.setWindowToOpen(DocEditorWindow.class);
		addLink.addParameter(DocEditorWindow._PRM_OBJECT_ID, this.getICObjectID());
		addLink.addParameter(DocEditorWindow._PRM_CATEGORY_ID, categoryID);
		addLink.addParameter(DocEditorWindow._PRM_OBJECT_INSTANCE_ID, this.getICObjectInstanceID());
		if (this.getContentLocaleIdentifier() != null) {
			addLink.addParameter(DocBusiness.PARAMETER_CONTENT_LOCALE_IDENTIFIER, this.getContentLocaleIdentifier());
			addLink.addParameter(DocEditorWindow._PRM_FOLDER_ID, _folderID);
		}

		return addLink;
	}

	/**
	 *  Gets the editLink attribute of the Doc object
	 *
	 *@param  linkID  Description of the Parameter
	 *@return         The editLink value
	 */
	private Link getEditLink(int linkID) {
		Link editLink = new Link(_editImage);
		editLink.setWindowToOpen(DocEditorWindow.class);
		editLink.addParameter(DocEditorWindow._PRM_DOC_ID, linkID);
		editLink.addParameter(DocEditorWindow._PRM_MODE, DocEditorWindow._MODE_EDITDOC);
		editLink.addParameter(DocEditorWindow._PRM_OBJECT_ID, this.getICObjectID());
		editLink.addParameter(DocEditorWindow._PRM_OBJECT_INSTANCE_ID, this.getICObjectInstanceID());
		if (this.getContentLocaleIdentifier() != null) {
			editLink.addParameter(DocEditorWindow._PRM_CONTENT_LOCALE_IDENTIFIER, this.getContentLocaleIdentifier());
			editLink.addParameter(DocEditorWindow._PRM_FOLDER_ID, _folderID);
		}
		return editLink;
	}

	/**
	 *  Gets the deleteLink attribute of the Doc object
	 *
	 *@param  linkID  Description of the Parameter
	 *@return         The deleteLink value
	 */
	private Link getDeleteLink(int linkID) {
		Link deleteLink = new Link(_deleteImage);
		deleteLink.setWindowToOpen(DocEditorWindow.class);
		deleteLink.addParameter(DocEditorWindow._PRM_DOC_ID, linkID);
		deleteLink.addParameter(DocEditorWindow._PRM_MODE, DocEditorWindow._MODE_DELETE);
		//        deleteLink.addParameter(DocBusiness.PARAMETER_FOLDER_ID, _folderID);
		//        deleteLink.addParameter(DocBusiness.PARAMETER_OBJECT_INSTANCE_ID, this.getICObjectInstanceID());
		//        deleteLink.addParameter(DocBusiness.PARAMETER_DELETE, DocBusiness.PARAMETER_TRUE);
		return deleteLink;
	}

	/**
	 *  Sets the defaultValues attribute of the Doc object
	 */
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
		_informationStyle = "font-face: Arial, Helvetica, sans-serif; font-size: 10px; color: #999999;";
		_categoryStyle = "font-face: Arial, Helvetica, sans-serif; font-size: 11px; font-weight: bold";
		_linkStyle = "font-face: Arial, Helvetica,sans-serif; font-size: 11px; color: #000000;";
		_visitedStyle = "font-face: Arial, Helvetica,sans-serif; font-size: 11px; color: #000000;";
		_activeStyle = "font-face: Arial, Helvetica,sans-serif; font-size: 11px; color: #000000;";
		_hoverStyle = "font-face: Arial, Helvetica,sans-serif; font-size: 11px; color: #000000;";
		_target = Link.TARGET_TOP_WINDOW;
	}

	/**
	 *  Sets the numberOfColumns attribute of the Doc object
	 *
	 *@param  columns  The new numberOfColumns value
	 */
	public void setNumberOfColumns(int columns) {
		_numberOfColumns = columns;
	}

	/**
	 *  Sets the headerColor attribute of the Doc object
	 *
	 *@param  color  The new headerColor value
	*/
	public void setHeaderColor(String color) {
		_headerColor = color;
	}

	/**
	 *  Sets the borderColor attribute of the Doc object
	 *
	 *@param  color  The new borderColor value
	 */
	public void setBorderColor(String color) {
		_borderColor = color;
	}

	/**
	 *  Sets the inlineColor attribute of the Doc object
	 *
	 *@param  color  The new inlineColor value
	 */
	public void setInlineColor(String color) {
		_inlineColor = color;
	}

	/**
	 *  Sets the width attribute of the Doc object
	 *
	 *@param  width  The new width value
	 */
	public void setWidth(String width) {
		_boxWidth = width;
	}

	/**
	 *  Sets the width attribute of the Doc object
	 *
	 *@param  width  The new width value
	 */
	public void setWidth(int width) {
		setWidth(Integer.toString(width));
	}

	/**
	 *  Sets the height attribute of the Doc object
	 *
	 *@param  height  The new height value
	 */
	public void setHeight(String height) {
		_boxHeight = height;
	}

	/**
	 *  Sets the height attribute of the Doc object
	 *
	 *@param  height  The new height value
	 */
	public void setHeight(int height) {
		setHeight(Integer.toString(height));
	}

	/**
	 *  Sets the layout attribute of the Doc object
	 *
	 *@param  layout  The new layout value
	 */
	public void setLayout(int layout) {
		_layout = layout;
	}

	/**
	 *  Sets the boxSpacing attribute of the Doc object
	 *
	 *@param  spacing  The new boxSpacing value
	 */
	public void setBoxSpacing(int spacing) {
		_boxSpacing = spacing;
	}

	/**
	 *  Sets the numberOfDisplayed attribute of the Doc object
	 *
	 *@param  number  The new numberOfDisplayed value
	 */
	public void setNumberOfDisplayed(int number) {
		_numberOfDisplayed = number;
	}

	/**
	 *  Sets the categoryStyle attribute of the Doc object
	 *
	 *@param  style  The new categoryStyle value
	 */
	public void setCategoryStyle(String style) {
		_categoryStyle = style;
	}

	/**
	 *  Sets the informationStyle attribute of the Doc object
	 *
	 *@param  style  The new informationStyle value
	 */
	public void setInformationStyle(String style) {
		_informationStyle = style;
	}

	/**
	 *  Sets the target attribute of the Doc object
	 *
	 *@param  target  The new target value
	 */
	public void setTarget(String target) {
		_target = target;
	}

	/**
	 *  Sets the linkStyle attribute of the Doc object
	 *
	 *@param  linkStyle     The new linkStyle value
	 *@param  activeStyle   The new linkStyle value
	 *@param  visitedStyle  The new linkStyle value
	 *@param  hoverStyle    The new linkStyle value
	 */
	public void setLinkStyle(String linkStyle, String activeStyle, String visitedStyle, String hoverStyle) {
		_linkStyle = linkStyle;
		_visitedStyle = linkStyle;
		_activeStyle = visitedStyle;
		_hoverStyle = hoverStyle;
	}

	/**
	 *  Sets the selectedCategoryColor attribute of the Doc object
	 *
	 *@param  color  The new selectedCategoryColor value
	 */
	public void setSelectedCategoryColor(String color) {
		_highlightColor = color;
	}

	/**
	 *  Sets the styles attribute of the Doc object
	 */
	private void setStyles() {
		if (_name == null) {
			_name = this.getName();
		}
		if (_name == null) {
			if (_attribute == null) {
				_name = "documents_" + Integer.toString(_folderID);
			} else {
				_name = "documents_" + _attribute;
			}
		}

		if (getParentPage() != null) {
			getParentPage().setStyleDefinition("A." + _name + ":link", _linkStyle);
			getParentPage().setStyleDefinition("A." + _name + ":visited", _visitedStyle);
			getParentPage().setStyleDefinition("A." + _name + ":active", _activeStyle);
			getParentPage().setStyleDefinition("A." + _name + ":hover", _hoverStyle);
		} else {
			_styles = false;
		}
	}

	//    /**
	//     *  Description of the Method
	//     *
	//     *@param  ICObjectInstanceId  Description of the Parameter
	//     *@return                     Description of the Return Value
	//     */
	//    public boolean deleteBlock(int ICObjectInstanceId) {
	//        return DocBusiness.deleteDoc(ICObjectInstanceId);
	//    }

	/**
	 *  Gets the bundleIdentifier attribute of the Doc object
	 *
	 *@return    The bundleIdentifier value
	 */
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	public Object clone() {
		Doc obj = null;
		try {
			obj = (Doc)super.clone();

			if (this._myTable != null) {
				obj._myTable = (Table)this._myTable.clone();
			}
		} catch (Exception ex) {
			ex.printStackTrace(System.err);
		}
		return obj;
	}

	/**
	 *  Gets the categoryType attribute of the Doc object
	 *
	 *@return    The categoryType value
	 */
	public String getCategoryType() {
		return "doc_documents";
	}
	
	public boolean copyICObjectInstance(String pageKey,int newInstanceID, DPTCopySession copySession) {
		try {
			return ((FolderBlockBusiness)IBOLookup.getServiceInstance(getIWApplicationContext(),FolderBlockBusiness.class)).copyCategoryAttachments(this.getBlockInstanceID(), newInstanceID);
		} catch (IBOLookupException e) {
			e.printStackTrace();
			return false;
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		}
	}

}