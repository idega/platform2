package com.idega.block.documents.presentation;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Locale;

import com.idega.block.category.business.FolderBlockBusiness;
import com.idega.block.category.data.InformationCategory;
import com.idega.block.category.data.InformationFolder;
import com.idega.block.documents.business.DocBusiness;
import com.idega.block.media.presentation.SimpleFileChooser;
import com.idega.builder.presentation.IBPageChooser;
import com.idega.business.IBOLookup;
import com.idega.core.accesscontrol.business.LoginBusinessBean;
import com.idega.core.builder.data.ICPage;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.localisation.data.ICLocale;
import com.idega.core.localisation.presentation.ICLocalePresentation;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.block.presentation.Builderaware;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

/**
 *  Description of the Class
 *
 *@author     <a href="gummi@idega.is">Gudmundur Agust Saemundsson</a>
 *@created    15. mars 2002
 */

public class DocEditorWindow extends IWAdminWindow {

	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.documents";
	private Image _editImage;
	private Image _createImage;
	private Image _deleteImage;
	private IWBundle _iwb;
	private IWResourceBundle _iwrb;
	private FolderBlockBusiness business = null;
	private final static int _ACTION_OPENWINDOW = -1;
	private final static int _ACTION_CHANGELOCALE = 0;
	private final static int _ACTION_CHANGETYPE = 1;
	private final static int _ACTION_UPLOADFILE = 2;
	private final static int _ACTION_SAVEDOC = 3;
	private final static int _ACTION_CLOSEWITHOUTSAVING = 4;
	private final static int _ACTION_DELETEDOCLINK = 5;
	private int _action = _ACTION_OPENWINDOW;
	private final static String _PRM_LASTACTION = "doc_l_act";
	private final static String _PRM_ACTION = "doc_act";
	private final static int _MODE_NEWDOC = 0;
	public final static int _MODE_EDITDOC = 1;
	public final static int _MODE_DELETE = 2;
	public final static int _MODE_VERSION = 3;
	private int _mode = _MODE_NEWDOC;
	public static String _PRM_MODE = "doc_mode";
	private static final String _PRM_NEW_MODE = "doc_mode_n";
	public static String _PRM_DOC_ID = "doc_update";
	public static String _PRM_OBJECT_ID = DocBusiness.PARAMETER_OBJECT_ID;
	public static String _PRM_OBJECT_INSTANCE_ID = DocBusiness.PARAMETER_OBJECT_INSTANCE_ID;
	public static String _PRM_FOLDER_ID = DocBusiness.PARAMETER_FOLDER_ID;
	public static String _PRM_CONTENT_LOCALE_IDENTIFIER = DocBusiness.PARAMETER_CONTENT_LOCALE_IDENTIFIER;
	public static String _PRM_CATEGORY_ID = DocBusiness.PARAMETER_CATEGORY_ID;
	private boolean _hasEditpermission = false;
	private boolean _update = false;
	private boolean _delete = false;
	private boolean _save = false;
	private static int _DEFAULT_TYPE = DocBusiness.LINK;
	private static String _DEFAULT_TARGET = Link.TARGET_BLANK_WINDOW;
	private final static String _PRM_TYPE = "doc_type";
	private final static String _PRM_TYPE_OLD_VALUE = "doc_type_old";
	private final static String _PRM_LOCALE = "doc_locale";
	private final static String _PRM_LOCALE_OLD_VALUE = "doc_locale_old";
	private final static String _PRM_LINK_NAME = "link_name";
	private final static String _PRM_FILE_ID = DocBusiness.PARAMETER_FILE_ID;
	private final static String _PRM_PAGE_ID = DocBusiness.PARAMETER_PAGE_ID;
	private final static String _PRM_LINK_URL = DocBusiness.PARAMETER_LINK_URL;
	private final static String _PRM_LAST_UPLOADED_FILE = _PRM_FILE_ID + "_old";
	private final static String _PRM_LAST_SELECTED_PAGE = _PRM_PAGE_ID + "_old";
	private final static String _PRM_LAST_LINK_URL = _PRM_LINK_URL + "_old";
	private int _iObjInsId = -1;
	private int _iObjId = -1;
	private int _infoCatID = -1;
	private int _linkID = -1;
	private int _userID = -1;
	private int _type = -1;
	private int _fileID = -1;
	private int _pageID = -1;
	private int _localeID = -1;
	private int _folderID = -1;
	private String _linkUrl = null;
	private int _contentLocaleId = -1;
	private String _target = null;
	private String _docLinkName = null;

	/**
	 *  Constructor for the DocEditorWindow object
	 */
	public DocEditorWindow() {
		setWidth(420);
		setHeight(340);
		setUnMerged();
		//setMethod("get");
		setStatus(true);
	}

	/**
	 *  Description of the Method
	 *
	 *@param  iwc            Description of the Parameter
	 *@exception  Exception  Description of the Exception
	 */
	public void main(IWContext iwc) throws Exception {
		business = (FolderBlockBusiness) IBOLookup.getServiceInstance(iwc,FolderBlockBusiness.class);
		_hasEditpermission = true;
		//AccessControl.hasEditPermission(this,iwc);
		_iwb = iwc.getIWMainApplication().getBundle(Builderaware.IW_CORE_BUNDLE_IDENTIFIER);
		_iwrb = getResourceBundle(iwc);
		addTitle(_iwrb.getLocalizedString("doc_admin", "Doc Admin"));
		Locale currentLocale = iwc.getCurrentLocale();
		try {
			_userID = LoginBusinessBean.getUser(iwc).getID();
		} catch (Exception e) {
			_userID = -1;
		}
		_editImage = _iwb.getImage("shared/edit.gif");
		_createImage = _iwb.getImage("shared/create.gif");
		_deleteImage = _iwb.getImage("shared/delete.gif");
		String sLocaleId = iwc.getParameter(DocBusiness.PARAMETER_LOCALE_DROP);
		if (sLocaleId != null) {
			_localeID = Integer.parseInt(sLocaleId);
		} else {
			_localeID = ICLocaleBusiness.getLocaleId(currentLocale);
		}
		setActionAndMode(iwc);
		processParameters(iwc);
		setDefultValues();
		this.debugParameters(iwc);
		if (_hasEditpermission) {
			switch (_action) {
				case _ACTION_OPENWINDOW :
					switch (_mode) {
						case _MODE_NEWDOC :
						case _MODE_EDITDOC :
							lineUpElements();
							break;
						case _MODE_DELETE :
							confirmLineup();
							break;
						default :
							addLeft("MODE: " + _mode + ", not yet supported");
							addSubmitButton(new CloseButton(_iwrb.getImage("close.gif")));
							break;
					}
					break;
				case _ACTION_CHANGELOCALE :
				case _ACTION_CHANGETYPE :
				case _ACTION_UPLOADFILE :
					lineUpElements();
					break;
				case _ACTION_SAVEDOC :
					switch (_type) {
						case DocBusiness.LINK :
						case DocBusiness.FILE :
						case DocBusiness.PAGE :
							saveDocLink(iwc);
							closeEditor(iwc);
							break;
						default :
							//addLeft(_iwrb.getLocalizedString("error_type_not_supported","Type: "+ _type + ", not supported"));
							addLeft("Type: " + _type + ", not supported");
							addSubmitButton(new CloseButton(_iwrb.getImage("close.gif")));
							break;
					}
					break;
				case _ACTION_CLOSEWITHOUTSAVING :
					closeEditor(iwc);
					break;
				case _ACTION_DELETEDOCLINK :
					deleteDocLink(iwc);
					closeEditor(iwc);
					break;
				default :
					addLeft("ACTION: " + _action + ", not yet supported");
					addSubmitButton(new CloseButton(_iwrb.getImage("close.gif")));
					break;
			}
		} else {
			if (_action == _ACTION_CLOSEWITHOUTSAVING) {
				closeEditor(iwc);
			} else {
				noAccess();
			}
		}
	}
	/**
	 *  Sets the defultValues attribute of the DocEditorWindow object
	 */
	private void setDefultValues() {
		if (_type == -1) {
			_type = _DEFAULT_TYPE;
		}
		if (_target == null) {
			_target = _DEFAULT_TARGET;
		}
	}

	private void setActionAndMode(IWContext iwc) {
		// mode
		String newMode = iwc.getParameter(_PRM_NEW_MODE);
		String mode = iwc.getParameter(_PRM_MODE);
		if (newMode != null) {
			try {
				_mode = Integer.parseInt(newMode);
			} catch (Exception ex) {
				try {
					_mode = Integer.parseInt(mode);
				} catch (Exception e) {
					_mode = _MODE_NEWDOC;
				}
			}
		} else {
			try {
				_mode = Integer.parseInt(mode);
			} catch (Exception ex) {
				_mode = _MODE_NEWDOC;
			}
		}
		this.addHiddenInput(new HiddenInput(_PRM_MODE, Integer.toString(_mode)));
		// action
		String type = iwc.getParameter(_PRM_TYPE);
		String typeOld = iwc.getParameter(_PRM_TYPE_OLD_VALUE);
		String locale = iwc.getParameter(_PRM_LOCALE);
		String localeOld = iwc.getParameter(_PRM_LOCALE_OLD_VALUE);
		String file = iwc.getParameter(_PRM_FILE_ID);
		String lastAction = iwc.getParameter(_PRM_LASTACTION);
		if (lastAction != null) {
			String action = iwc.getParameter(_PRM_ACTION);
			if (action != null && !"".equals(action)) {
				_action = Integer.parseInt(action);
			} else if (file != null || (type != null && type.equals(typeOld) && Integer.toString(DocBusiness.FILE).equals(type))) {
				_action = _ACTION_UPLOADFILE;
			} else if (type != null && !type.equals(typeOld)) {
				_action = _ACTION_CHANGETYPE;
			} else if (locale != null && !locale.equals(localeOld)) {
				_action = _ACTION_CHANGELOCALE;
			}
		} else {
			_action = _ACTION_OPENWINDOW;
		}
		this.addHiddenInput(new HiddenInput(_PRM_LASTACTION, Integer.toString(_action)));
		if (type != null) {
			this.addHiddenInput(new HiddenInput(_PRM_TYPE_OLD_VALUE, type));
		}
		if (locale != null) {
			this.addHiddenInput(new HiddenInput(_PRM_LOCALE_OLD_VALUE, locale));
		}
	}
	private void processParameters(IWContext iwc) {
		com.idega.block.documents.data.DocLink editLink = null;
		if (iwc.getParameter(_PRM_DOC_ID) != null) {
			try {
				_linkID = Integer.parseInt(iwc.getParameter(_PRM_DOC_ID));
				this.getUnderlyingForm().maintainParameter(_PRM_DOC_ID);
			} catch (NumberFormatException e) {
				_linkID = -1;
			}
			if (_linkID != -1 && _mode == _MODE_EDITDOC && _action == _ACTION_OPENWINDOW) {
				editLink = DocBusiness.getLink(_linkID);
				//              if(editLink == null){
				//                // ERROR
				//              }
			}
		}

		String sLocale = iwc.getParameter(_PRM_CONTENT_LOCALE_IDENTIFIER);
		if (sLocale != null) {
			try {
				_contentLocaleId = Integer.parseInt(sLocale);
				this.addHiddenInput(new HiddenInput(_PRM_CONTENT_LOCALE_IDENTIFIER, Integer.toString(_contentLocaleId)));
			} catch (NumberFormatException e) {
				ICLocale locale = ICLocaleBusiness.getICLocale(sLocale);
				if (locale != null) {
					_contentLocaleId = locale.getLocaleID();
					this.addHiddenInput(new HiddenInput(_PRM_CONTENT_LOCALE_IDENTIFIER, Integer.toString(_contentLocaleId)));
				} else {
					_contentLocaleId = -1;
				}
			}
		}

		if (iwc.getParameter(DocBusiness.PARAMETER_OBJECT_ID) != null) {
			try {
				_iObjId = Integer.parseInt(iwc.getParameter(DocBusiness.PARAMETER_OBJECT_ID));
			} catch (NumberFormatException e) {
				_iObjId = -1;
			}
		}
		String objId = iwc.getParameter(_PRM_OBJECT_ID);
		if (objId != null) {
			try {
				_iObjId = Integer.parseInt(objId);
			} catch (NumberFormatException e) {
				_iObjId = -1;
			}
		}
		String instanceId = iwc.getParameter(_PRM_OBJECT_INSTANCE_ID);
		if (instanceId != null) {
			try {
				_iObjInsId = Integer.parseInt(instanceId);
			} catch (NumberFormatException e) {
				_iObjInsId = -1;
			}
		}
		String folderID = iwc.getParameter(_PRM_FOLDER_ID);
		if (folderID != null) {
			try {
				_folderID = Integer.parseInt(folderID);
			} catch (NumberFormatException e) {
				_folderID = -1;
			}
		}
		if (editLink != null) { // if _mode = _MODE_EDITDOC
			_target = editLink.getTarget();
			_linkUrl = editLink.getURL();
			_docLinkName = editLink.getName();
			_infoCatID = editLink.getCategoryID();
			_fileID = editLink.getFileID();
			_pageID = editLink.getPageID();
			if (_linkUrl != null) {
				_type = DocBusiness.LINK;
			} else if (_pageID != -1) {
				_type = DocBusiness.PAGE;
			} else if (_fileID != -1) {
				_type = DocBusiness.FILE;
			}
		} else {
			if (iwc.getParameter(_PRM_TYPE) != null) {
				try {
					_type = Integer.parseInt(iwc.getParameter(_PRM_TYPE));
				} catch (NumberFormatException e) {
					_type = -1;
				}
			} else {
				if (iwc.getParameter(_PRM_TYPE_OLD_VALUE) != null) {
					try {
						_type = Integer.parseInt(iwc.getParameter(_PRM_TYPE_OLD_VALUE));
					} catch (NumberFormatException e) {
						_type = -1;
					}
				}
			}
			if (iwc.getParameter(DocBusiness.PARAMETER_TARGET) != null) {
				_target = iwc.getParameter(DocBusiness.PARAMETER_TARGET);
			} else {
				_target = null;
			}
			if (iwc.getParameter(_PRM_LINK_NAME) != null) {
				_docLinkName = iwc.getParameter(_PRM_LINK_NAME);
			} else {
				_docLinkName = null;
			}
			if (iwc.getParameter(DocBusiness.PARAMETER_CATEGORY_ID) != null) {
				try {
					_infoCatID = Integer.parseInt(iwc.getParameter(DocBusiness.PARAMETER_CATEGORY_ID));
				} catch (NumberFormatException e) {
					_infoCatID = -1;
				}
			}
			if (iwc.getParameter(_PRM_FILE_ID) != null) {
				try {
					_fileID = Integer.parseInt(iwc.getParameter(_PRM_FILE_ID));
				} catch (NumberFormatException e) {
					_fileID = -1;
				}
				this.addHiddenInput(new HiddenInput(_PRM_LAST_UPLOADED_FILE, iwc.getParameter(_PRM_FILE_ID)));
			} else if (iwc.getParameter(_PRM_LAST_UPLOADED_FILE) != null) {
				try {
					_fileID = Integer.parseInt(iwc.getParameter(_PRM_LAST_UPLOADED_FILE));
				} catch (NumberFormatException e) {
					_fileID = -1;
				}
				switch (_action) {
					case _ACTION_UPLOADFILE :
						break;
					default :
						this.addHiddenInput(new HiddenInput(_PRM_LAST_UPLOADED_FILE, iwc.getParameter(_PRM_LAST_UPLOADED_FILE)));
						break;
				}
			}
			if (iwc.getParameter(_PRM_PAGE_ID) != null) {
				try {
					_pageID = Integer.parseInt(iwc.getParameter(_PRM_PAGE_ID));
				} catch (NumberFormatException e) {
					_pageID = -1;
				}
				this.addHiddenInput(new HiddenInput(_PRM_LAST_SELECTED_PAGE, iwc.getParameter(_PRM_PAGE_ID)));
			} else if (iwc.getParameter(_PRM_LAST_SELECTED_PAGE) != null) {
				try {
					_pageID = Integer.parseInt(iwc.getParameter(_PRM_LAST_SELECTED_PAGE));
				} catch (NumberFormatException e) {
					_pageID = -1;
				}
				this.addHiddenInput(new HiddenInput(_PRM_LAST_SELECTED_PAGE, iwc.getParameter(_PRM_LAST_SELECTED_PAGE)));
			}
			if (iwc.getParameter(_PRM_LINK_URL) != null) {
				_linkUrl = iwc.getParameter(_PRM_LINK_URL);
				if (_linkUrl.equals("")) {
					_linkUrl = null;
				}
				if (_linkUrl != null) {
					this.addHiddenInput(new HiddenInput(_PRM_LAST_LINK_URL, _linkUrl));
				}
			} else if (iwc.getParameter(_PRM_LAST_LINK_URL) != null) {
				_linkUrl = iwc.getParameter(_PRM_LAST_LINK_URL);
				if (_linkUrl.equals("")) {
					_linkUrl = null;
				}
				if (_linkUrl != null) {
					this.addHiddenInput(new HiddenInput(_PRM_LAST_LINK_URL, _linkUrl));
				}
			}
		}
	}
	private void confirmLineup() {
		addLeft("Are you sure you want to delete this document?");
		addSubmitButton(new SubmitButton(_iwrb.getLocalizedImageButton("yes", "YES"), _PRM_ACTION, Integer.toString(_ACTION_DELETEDOCLINK)));
		addSubmitButton(new SubmitButton(_iwrb.getLocalizedImageButton("cancel", "CANCEL"), _PRM_ACTION, Integer.toString(_ACTION_CLOSEWITHOUTSAVING)));
	}

	/**
	 *  Description of the Method
	 *
	 *@param  iLocaleId   Description of the Parameter
	 *@param  instanceId  Description of the Parameter
	 */
	private void lineUpElements() throws RemoteException {
		//    DocLink link = DocFinder.getLink(_linkID);
		//    /**
		//     * @todo: localice
		//     */
		//
		//    String docLinkName = null;
		this.getUnderlyingForm().maintainParameter(_PRM_OBJECT_ID);
		this.getUnderlyingForm().maintainParameter(_PRM_OBJECT_INSTANCE_ID);
		this.getUnderlyingForm().maintainParameter(_PRM_FOLDER_ID);
		if (_contentLocaleId != -1) {
			this.addHiddenInput(new HiddenInput(DocBusiness.PARAMETER_LOCALE_DROP, Integer.toString(_contentLocaleId)));
		} else {
			DropdownMenu localeDrop = ICLocalePresentation.getLocaleDropdownIdKeyed(DocBusiness.PARAMETER_LOCALE_DROP);
			localeDrop.setToSubmit();
			localeDrop.setSelectedElement(Integer.toString(_localeID));
			addLeft(_iwrb.getLocalizedString("locale", "Locale") + ": ", localeDrop, false);
		}
		DropdownMenu categoryDrop = new DropdownMenu(DocBusiness.PARAMETER_CATEGORY_ID);
		List list = null;
		if (_iObjInsId != -1) {
			list = business.getInstanceCategories(_iObjInsId);
		}
		if (list != null) {
			for (int a = 0; a < list.size(); a++) {
				//        LocalizedText locText = TextFinder.getLocalizedText((ICInformationCategory)list.get(a),iLocaleId);
				//        String locString = "$language$";
				//        if ( locText != null ) {
				//          locString = locText.getHeadline();
				String catName = ((InformationCategory)list.get(a)).getName();
				//        }
				categoryDrop.addMenuElement(((InformationCategory)list.get(a)).getID(), catName);
			}
			categoryDrop.setSelectedElement(Integer.toString(_infoCatID));
		}
		TextInput linkName = new TextInput(_PRM_LINK_NAME);
		linkName.setLength(36);
		if (_docLinkName != null) {
			linkName.setContent(_docLinkName);
		}
		DropdownMenu typeDrop = new DropdownMenu(_PRM_TYPE);
		typeDrop.addMenuElement(DocBusiness.LINK, _iwrb.getLocalizedString("link", "Link"));
		typeDrop.addMenuElement(DocBusiness.FILE, _iwrb.getLocalizedString("file", "File"));
		typeDrop.addMenuElement(DocBusiness.PAGE, _iwrb.getLocalizedString("page", "Page"));
		typeDrop.setSelectedElement(Integer.toString(_type));
		typeDrop.setToSubmit();
		DropdownMenu targetDrop = new DropdownMenu(DocBusiness.PARAMETER_TARGET);
		targetDrop.addMenuElement(Link.TARGET_BLANK_WINDOW, _iwrb.getLocalizedString("_blank", "New Window"));
		targetDrop.addMenuElement(Link.TARGET_SELF_WINDOW, _iwrb.getLocalizedString("_self", "Same Window"));
		targetDrop.addMenuElement(Link.TARGET_PARENT_WINDOW, _iwrb.getLocalizedString("_parent", "Parent frame"));
		targetDrop.addMenuElement(Link.TARGET_TOP_WINDOW, _iwrb.getLocalizedString("_top", "Top frame"));
		if (_target != null) {
			targetDrop.setSelectedElement(_target);
		}
		addLeft(_iwrb.getLocalizedString("category", "Category") + ":", categoryDrop, true);
		addLeft(_iwrb.getLocalizedString("link_name", "Name") + ":", linkName, true);
		addLeft(_iwrb.getLocalizedString("type", "Type") + ":", typeDrop, true);
		switch (_type) {
			case DocBusiness.LINK :
				TextInput linkURL = new TextInput(_PRM_LINK_URL);
				linkURL.setLength(30);
				if (_linkUrl != null) {
					linkURL.setContent(_linkUrl);
				} else {
					linkURL.setContent("http://");
				}
				addLeft(_iwrb.getLocalizedString("link", "Link") + ":", linkURL, true);
				break;
			case DocBusiness.FILE :
				SimpleFileChooser fileChooser = new SimpleFileChooser(this.getUnderlyingForm(), _PRM_FILE_ID);
				this.setStyle(fileChooser);
				if (_fileID != -1 && (_action == _ACTION_CHANGETYPE || (_mode == _MODE_EDITDOC && _action == _ACTION_OPENWINDOW))) {
					fileChooser.setSelectedFile(_fileID);
				}
				addLeft(_iwrb.getLocalizedString("file", "File") + ":", fileChooser, true);
				break;
			case DocBusiness.PAGE :
				IBPageChooser pageChooser = new IBPageChooser(_PRM_PAGE_ID, STYLE);
				if (_pageID != -1) {
					ICPage page = DocBusiness.getPage(_pageID);
					if (page != null) {
						pageChooser.setSelectedPage(_pageID, page.getName());
					}
				}
				addLeft(_iwrb.getLocalizedString("page", "Page") + ":", pageChooser, true);
				break;
		}
		addLeft(_iwrb.getLocalizedString("target", "Target") + ":", targetDrop, true);
		addSubmitButton(new SubmitButton(_iwrb.getLocalizedImageButton("save", "SAVE"), _PRM_ACTION, Integer.toString(_ACTION_SAVEDOC)));
		addSubmitButton(new SubmitButton(_iwrb.getLocalizedImageButton("close", "CLOSE"), _PRM_ACTION, Integer.toString(_ACTION_CLOSEWITHOUTSAVING)));
	}
	
	/**
	 *  Description of the Method
	 *
	 *@param  iwc        Description of the Parameter
	 *@return            Description of the Return Value
	 */
	private boolean saveDocLink(IWContext iwc) throws RemoteException {
		int fileID = -1;
		int pageID = -1;
		String linkUrl = null;
		String docLinkName = "Untitled";
		int folderID = -1;
		int localeID = _contentLocaleId;
		if (_contentLocaleId == -1 && _localeID == -1) {
			localeID = iwc.getCurrentLocaleId();
		} else if (_contentLocaleId != -1) {
			localeID = _contentLocaleId;
		} else {
			localeID = _localeID;
		}

		switch (_type) {
			case DocBusiness.LINK :
				if (_linkUrl != null && !(_linkUrl.equalsIgnoreCase("http://") && _linkUrl.length() > 0)) {
					linkUrl = _linkUrl;
				}
				break;
			case DocBusiness.FILE :
				fileID = _fileID;
				break;
			case DocBusiness.PAGE :
				pageID = _pageID;
				break;
			default :
				return false;
		}

		if (_docLinkName != null && _docLinkName.length() > 0) {
			docLinkName = _docLinkName;
		}

		if (docLinkName != null && _iObjInsId != -1) {
			if (_folderID == -1) {
				InformationFolder folder = business.getInstanceWorkeFolder(_iObjInsId, _iObjId, localeID, true);
				if (folder != null) {
					folderID = folder.getID();
				}
			} else {
				folderID = _folderID;
			}
			try {
				_linkID = DocBusiness.saveLink(_userID, _infoCatID, folderID, _linkID, docLinkName, fileID, pageID, linkUrl, _target, localeID);
				this.addHiddenInput(new HiddenInput(_PRM_DOC_ID, Integer.toString(_linkID)));
				return true;
			} catch (Exception e) {
				e.printStackTrace(System.err);
			}
		}
		return false;
	}

	/**
	 *  Description of the Method
	 *
	 *@param  iwc  Description of the Parameter
	 */
	private void deleteDocLink(IWContext iwc) {
		DocBusiness.deleteLink(_linkID);
	}

	/**
	 *  Description of the Method
	 *
	 *@param  iwc  Description of the Parameter
	 */
	private void closeEditor(IWContext iwc) {
		setParentToReload();
		close();
	}

	/**
	 *  Description of the Method
	 *
	 *@exception  Exception  Description of the Exception
	 */
	private void noAccess() throws Exception {
		addLeft(_iwrb.getLocalizedString("no_access", "Login first!"));
		addSubmitButton(new CloseButton(_iwrb.getImage("close.gif")));
	}

	/**
	 *  Gets the bundleIdentifier attribute of the DocEditorWindow object
	 *
	 *@return    The bundleIdentifier value
	 */
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

}