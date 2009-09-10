package com.idega.block.boxoffice.presentation;


import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import com.idega.block.boxoffice.business.BoxBusiness;
import com.idega.block.boxoffice.business.BoxFinder;
import com.idega.block.boxoffice.data.BoxLink;
import com.idega.block.media.presentation.FileChooser;
import com.idega.builder.presentation.IBPageChooser;
import com.idega.core.accesscontrol.business.LoginBusinessBean;
import com.idega.core.localisation.business.ICLocaleBusiness;
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

public class BoxEditorWindow extends IWAdminWindow{

private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.boxoffice";
private boolean _isAdmin = false;
private boolean _superAdmin = false;
private boolean _update = false;
private boolean _save = false;
private int _iObjInsId = -1;

private int _boxID = -1;
private int _boxCategoryID = -1;
private int _linkID = -1;
private int _userID = -1;
private boolean _newObjInst = false;
private String _newWithAttribute;
private Image _editImage;
private Image _createImage;
private Image _deleteImage;
private int _type = -1;
private int _fileID = -1;
private int _pageID = -1;
private String _target;

private IWBundle _iwb;
private IWResourceBundle _iwrb;

public BoxEditorWindow(){
  setWidth(420);
  setHeight(340);
  setUnMerged();
  setMethod("get");
  setStatus(true);
}

  public void main(IWContext iwc) throws Exception {
    /**
     * @todo permission
     */
    this._isAdmin = true; //AccessControl.hasEditPermission(this,iwc);
    this._superAdmin = iwc.hasEditPermission(this);
    this._iwb = iwc.getIWMainApplication().getBundle(Builderaware.IW_CORE_BUNDLE_IDENTIFIER);
    this._iwrb = getResourceBundle(iwc);
    addTitle(this._iwrb.getLocalizedString("box_admin","Box Admin"));
    Locale currentLocale = iwc.getCurrentLocale();
    Locale chosenLocale;
    
    iwc.removeSessionAttribute(BoxBusiness.PARAMETER_CATEGORY_ID);

    try {
      this._userID = LoginBusinessBean.getUser(iwc).getID();
    }
    catch (Exception e) {
      this._userID = -1;
    }

    this._editImage = this._iwb.getImage("shared/edit.gif");
    this._createImage = this._iwb.getImage("shared/create.gif");
    this._deleteImage = this._iwb.getImage("shared/delete.gif");

    String sLocaleId = iwc.getParameter(BoxBusiness.PARAMETER_LOCALE_DROP);

    int iLocaleId = -1;
    if(sLocaleId!= null){
      iLocaleId = Integer.parseInt(sLocaleId);
      chosenLocale = ICLocaleBusiness.getLocaleReturnIcelandicLocaleIfNotFound(iLocaleId);
    }
    else{
      chosenLocale = currentLocale;
      iLocaleId = ICLocaleBusiness.getLocaleId(chosenLocale);
    }

    if ( this._isAdmin ) {
      processForm(iwc, iLocaleId,sLocaleId);
    }
    else {
      noAccess();
    }
  }

  private void processForm(IWContext iwc, int iLocaleId, String sLocaleID) {
    if ( iwc.getParameter(BoxBusiness.PARAMETER_TYPE) != null ) {
      try {
        this._type = Integer.parseInt(iwc.getParameter(BoxBusiness.PARAMETER_TYPE));
      }
      catch (NumberFormatException e) {
        this._type = -1;
      }
    }

    if ( iwc.getParameter(BoxBusiness.PARAMETER_TARGET) != null ) {
      this._target = iwc.getParameter(BoxBusiness.PARAMETER_TARGET);
    }

    if ( iwc.getParameter(BoxBusiness.PARAMETER_BOX_ID) != null ) {
      try {
        this._boxID = Integer.parseInt(iwc.getParameter(BoxBusiness.PARAMETER_BOX_ID));
      }
      catch (NumberFormatException e) {
        this._boxID = -1;
      }
    }

    if ( iwc.getParameter(BoxBusiness.PARAMETER_NEW_OBJECT_INSTANCE) != null ) {
      this._newObjInst = false;
    }

    if ( iwc.getParameter(BoxBusiness.PARAMETER_LINK_ID) != null ) {
      try {
        this._linkID = Integer.parseInt(iwc.getParameter(BoxBusiness.PARAMETER_LINK_ID));
        iwc.setSessionAttribute(BoxBusiness.PARAMETER_LINK_ID,new Integer(this._linkID));
      }
      catch (NumberFormatException e) {
        this._linkID = -1;
      }
    }

    if ( sLocaleID != null ) {
      saveBoxLink(iwc,iLocaleId,false);
    }

    if ( (Integer) iwc.getSessionAttribute(BoxBusiness.PARAMETER_LINK_ID) != null ) {
      try {
        this._linkID = ((Integer) iwc.getSessionAttribute(BoxBusiness.PARAMETER_LINK_ID)).intValue();
      }
      catch (NumberFormatException e) {
        this._linkID = -1;
      }
    }

    if ( iwc.getParameter(BoxBusiness.PARAMETER_CATEGORY_ID) != null ) {
      try {
        this._boxCategoryID = Integer.parseInt(iwc.getParameter(BoxBusiness.PARAMETER_CATEGORY_ID));
      }
      catch (NumberFormatException e) {
        this._boxCategoryID = -1;
      }
    }

    if ( iwc.getParameter(BoxBusiness.PARAMETER_FILE_ID) != null ) {
      try {
        this._fileID = Integer.parseInt(iwc.getParameter(BoxBusiness.PARAMETER_FILE_ID));
      }
      catch (NumberFormatException e) {
        this._fileID = -1;
      }
    }

    if ( iwc.getParameter(BoxBusiness.PARAMETER_PAGE_ID) != null ) {
      try {
        this._pageID = Integer.parseInt(iwc.getParameter(BoxBusiness.PARAMETER_PAGE_ID));
      }
      catch (NumberFormatException e) {
        this._pageID = -1;
      }
    }

    DropdownMenu localeDrop = ICLocalePresentation.getLocaleDropdownIdKeyed(BoxBusiness.PARAMETER_LOCALE_DROP);
      localeDrop.setToSubmit();
      localeDrop.setSelectedElement(Integer.toString(iLocaleId));
    addLeft(this._iwrb.getLocalizedString("locale","Locale")+": ",localeDrop,false);
    addHiddenInput(new HiddenInput(BoxBusiness.PARAMETER_BOX_ID,Integer.toString(this._boxID)));

    if ( iwc.getParameter(BoxBusiness.PARAMETER_MODE) != null ) {
      if ( iwc.getParameter(BoxBusiness.PARAMETER_MODE).equalsIgnoreCase(BoxBusiness.PARAMETER_CLOSE) ) {
        closeEditor(iwc);
      }
      else if ( iwc.getParameter(BoxBusiness.PARAMETER_MODE).equalsIgnoreCase(BoxBusiness.PARAMETER_SAVE) ) {
        System.out.println("Saving...");
        saveBoxLink(iwc,iLocaleId,true);
      }
    }

    if ( this._linkID != -1 ) {
      if ( iwc.getParameter(BoxBusiness.PARAMETER_DELETE) != null ) {
        deleteBoxLink(iwc);
      }
      else {
        this._update = true;
      }
    }

    initializeFields(iLocaleId);
  }

  private void initializeFields(int iLocaleID) {
    BoxLink link = BoxFinder.getLink(this._linkID);
    String locString = BoxBusiness.getLocalizedString(link,iLocaleID);

    if ( link == null ) {
      this._update = false;
    }
    else {
      this._update = true;
    }
    if ( this._target == null && this._update ) {
      this._target = link.getTarget();
      if ( this._target == null ) {
        this._target = Link.TARGET_BLANK_WINDOW;
      }
    }
    if ( this._type == -1 && this._update ) {
      if ( link.getPageID() != -1 ) {
		this._type = BoxBusiness.PAGE;
	}
	else if ( link.getFileID() != -1 ) {
		this._type = BoxBusiness.FILE;
	}
	else if ( link.getURL() != null ) {
		this._type = BoxBusiness.LINK;
	}
    }
    if ( this._type == -1 ) {
      this._type = BoxBusiness.LINK;
    }

    DropdownMenu categoryDrop = BoxBusiness.getCategories(BoxBusiness.PARAMETER_CATEGORY_ID,iLocaleID,BoxFinder.getBox(this._boxID),this._userID);
      if ( this._update ) {
        categoryDrop.setSelectedElement(Integer.toString(link.getBoxCategoryID()));
      }
      else if ( this._boxCategoryID != -1 ) {
        categoryDrop.setSelectedElement(Integer.toString(this._boxCategoryID));
      }

    TextInput linkName = new TextInput(BoxBusiness.PARAMETER_LINK_NAME);
      linkName.setLength(36);
      if ( this._update && locString != null ) {
        linkName.setContent(locString);
        addHiddenInput(new HiddenInput(BoxBusiness.PARAMETER_NEW_OBJECT_INSTANCE,"false"));
      }

    DropdownMenu typeDrop = new DropdownMenu(BoxBusiness.PARAMETER_TYPE);
      typeDrop.addMenuElement(BoxBusiness.LINK,this._iwrb.getLocalizedString("link","Link"));
      typeDrop.addMenuElement(BoxBusiness.FILE,this._iwrb.getLocalizedString("file","File"));
      typeDrop.addMenuElement(BoxBusiness.PAGE,this._iwrb.getLocalizedString("page","Page"));
      typeDrop.setSelectedElement(Integer.toString(this._type));
      typeDrop.setToSubmit();

    DropdownMenu targetDrop = new DropdownMenu(BoxBusiness.PARAMETER_TARGET);
      targetDrop.addMenuElement(Link.TARGET_BLANK_WINDOW,this._iwrb.getLocalizedString("_blank","New Window"));
      targetDrop.addMenuElement(Link.TARGET_SELF_WINDOW,this._iwrb.getLocalizedString("_self","Same Window"));
      targetDrop.addMenuElement(Link.TARGET_PARENT_WINDOW,this._iwrb.getLocalizedString("_parent","Parent frame"));
      targetDrop.addMenuElement(Link.TARGET_TOP_WINDOW,this._iwrb.getLocalizedString("_top","Top frame"));
      targetDrop.setSelectedElement(this._target);

    TextInput linkURL = new TextInput(BoxBusiness.PARAMETER_LINK_URL);
      linkURL.setLength(30);
      if ( this._update && link.getURL() != null ) {
        linkURL.setContent(link.getURL());
      }
      else {
        linkURL.setContent("http://");
      }

    /**
     * @todo File uploading
     */

    FileChooser fileChooser = new FileChooser(BoxBusiness.PARAMETER_FILE_ID,STYLE);
    if ( link != null && this._update ) {
      if ( link.getFileID() != -1 ) {
		fileChooser.setSelectedFile(BoxFinder.getFile(link.getFileID()));
	}
    }
    IBPageChooser pageChooser = new IBPageChooser(BoxBusiness.PARAMETER_PAGE_ID,STYLE);
    if ( link != null && this._update ) {
      if ( link.getPageID() != -1 ) {
		pageChooser.setSelectedPage(BoxFinder.getPage(link.getPageID()));
	}
    }

    addLeft(this._iwrb.getLocalizedString("category","Category")+":",categoryDrop,true);
    addLeft(this._iwrb.getLocalizedString("link_name","Name")+":",linkName,true);
    addLeft(this._iwrb.getLocalizedString("type","Type")+":",typeDrop,true);

    if ( this._type == BoxBusiness.LINK ) {
		addLeft(this._iwrb.getLocalizedString("link","Link")+":",linkURL,true);
	}
	else if ( this._type == BoxBusiness.FILE ) {
		addLeft(this._iwrb.getLocalizedString("file","File")+":",fileChooser,true);
	}
	else if ( this._type == BoxBusiness.PAGE ) {
		addLeft(this._iwrb.getLocalizedString("page","Page")+":",pageChooser,true);
	}

    addLeft(this._iwrb.getLocalizedString("target","Target")+":",targetDrop,true);

    addHiddenInput(new HiddenInput(BoxBusiness.PARAMETER_LINK_ID,Integer.toString(this._linkID)));
    addHiddenInput(new HiddenInput(BoxBusiness.PARAMETER_LOCALE_ID,Integer.toString(iLocaleID)));

    addSubmitButton(new SubmitButton(this._iwrb.getLocalizedImageButton("close","CLOSE"),BoxBusiness.PARAMETER_MODE,BoxBusiness.PARAMETER_CLOSE));
    addSubmitButton(new SubmitButton(this._iwrb.getLocalizedImageButton("save","SAVE"),BoxBusiness.PARAMETER_MODE,BoxBusiness.PARAMETER_SAVE));
  }

  private void saveBoxLink(IWContext iwc,int iLocaleID,boolean setToClose) {
    String boxLinkName = iwc.getParameter(BoxBusiness.PARAMETER_LINK_NAME);
    String boxLinkURL = iwc.getParameter(BoxBusiness.PARAMETER_LINK_URL);
    String categoryID = iwc.getParameter(BoxBusiness.PARAMETER_CATEGORY_ID);

    String localeString = iwc.getParameter(BoxBusiness.PARAMETER_LOCALE_ID);
    int linkID = -1;

    if ( categoryID != null ) {
      try {
        this._boxCategoryID = Integer.parseInt(categoryID);
      }
      catch (NumberFormatException e) {
        this._boxCategoryID = -1;
      }
    }

    if ( this._type == BoxBusiness.LINK ) {
      this._fileID = -1;
      this._pageID = -1;
    }
    else if ( this._type == BoxBusiness.FILE ) {
      this._pageID = -1;
      boxLinkURL = null;
    }
    else if ( this._type == BoxBusiness.PAGE ) {
      this._fileID = -1;
      boxLinkURL = null;
    }

    if ( boxLinkName == null || boxLinkName.length() == 0 ) {
      boxLinkName = null;
    }
    if ( boxLinkURL != null && ( boxLinkURL.equalsIgnoreCase("http://") || boxLinkURL.length() == 0 ) ) {
      boxLinkURL = null;
    }

    if ( localeString != null && boxLinkName != null ) {
      try {
        linkID = BoxBusiness.saveLink(this._userID,this._boxID,this._boxCategoryID,this._linkID,boxLinkName,this._fileID,this._pageID,boxLinkURL,this._target,Integer.parseInt(localeString));
      }
      catch (Exception e) {
        e.printStackTrace(System.err);
      }

      iwc.setSessionAttribute(BoxBusiness.PARAMETER_LINK_ID,new Integer(linkID));
    }
  }

  private void deleteBoxLink(IWContext iwc) {
    System.out.println("Deleting...");
    iwc.removeSessionAttribute(BoxBusiness.PARAMETER_LINK_ID);
    BoxBusiness.deleteLink(this._linkID);
    setParentToReload();
    close();
  }

  private void closeEditor(IWContext iwc) {
    iwc.removeSessionAttribute(BoxBusiness.PARAMETER_LINK_ID);
    if ( this._newObjInst ) {
      deleteBoxLink(iwc);
    }
    setParentToReload();
    close();
  }

  private void noAccess() throws IOException,SQLException {
    addLeft(this._iwrb.getLocalizedString("no_access","Login first!"));
    addSubmitButton(new CloseButton(this._iwrb.getImage("close.gif")));
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

}
