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
    _isAdmin = true; //AccessControl.hasEditPermission(this,iwc);
    _superAdmin = iwc.hasEditPermission(this);
    _iwb = iwc.getIWMainApplication().getBundle(Builderaware.IW_CORE_BUNDLE_IDENTIFIER);
    _iwrb = getResourceBundle(iwc);
    addTitle(_iwrb.getLocalizedString("box_admin","Box Admin"));
    Locale currentLocale = iwc.getCurrentLocale();
    Locale chosenLocale;
    
    iwc.removeSessionAttribute(BoxBusiness.PARAMETER_CATEGORY_ID);

    try {
      _userID = LoginBusinessBean.getUser(iwc).getID();
    }
    catch (Exception e) {
      _userID = -1;
    }

    _editImage = _iwb.getImage("shared/edit.gif");
    _createImage = _iwb.getImage("shared/create.gif");
    _deleteImage = _iwb.getImage("shared/delete.gif");

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

    if ( _isAdmin ) {
      processForm(iwc, iLocaleId,sLocaleId);
    }
    else {
      noAccess();
    }
  }

  private void processForm(IWContext iwc, int iLocaleId, String sLocaleID) {
    if ( iwc.getParameter(BoxBusiness.PARAMETER_TYPE) != null ) {
      try {
        _type = Integer.parseInt(iwc.getParameter(BoxBusiness.PARAMETER_TYPE));
      }
      catch (NumberFormatException e) {
        _type = -1;
      }
    }

    if ( iwc.getParameter(BoxBusiness.PARAMETER_TARGET) != null ) {
      _target = iwc.getParameter(BoxBusiness.PARAMETER_TARGET);
    }

    if ( iwc.getParameter(BoxBusiness.PARAMETER_BOX_ID) != null ) {
      try {
        _boxID = Integer.parseInt(iwc.getParameter(BoxBusiness.PARAMETER_BOX_ID));
      }
      catch (NumberFormatException e) {
        _boxID = -1;
      }
    }

    if ( iwc.getParameter(BoxBusiness.PARAMETER_NEW_OBJECT_INSTANCE) != null ) {
      _newObjInst = false;
    }

    if ( iwc.getParameter(BoxBusiness.PARAMETER_LINK_ID) != null ) {
      try {
        _linkID = Integer.parseInt(iwc.getParameter(BoxBusiness.PARAMETER_LINK_ID));
        iwc.setSessionAttribute(BoxBusiness.PARAMETER_LINK_ID,new Integer(_linkID));
      }
      catch (NumberFormatException e) {
        _linkID = -1;
      }
    }

    if ( sLocaleID != null ) {
      saveBoxLink(iwc,iLocaleId,false);
    }

    if ( (Integer) iwc.getSessionAttribute(BoxBusiness.PARAMETER_LINK_ID) != null ) {
      try {
        _linkID = ((Integer) iwc.getSessionAttribute(BoxBusiness.PARAMETER_LINK_ID)).intValue();
      }
      catch (NumberFormatException e) {
        _linkID = -1;
      }
    }

    if ( iwc.getParameter(BoxBusiness.PARAMETER_CATEGORY_ID) != null ) {
      try {
        _boxCategoryID = Integer.parseInt(iwc.getParameter(BoxBusiness.PARAMETER_CATEGORY_ID));
      }
      catch (NumberFormatException e) {
        _boxCategoryID = -1;
      }
    }

    if ( iwc.getParameter(BoxBusiness.PARAMETER_FILE_ID) != null ) {
      try {
        _fileID = Integer.parseInt(iwc.getParameter(BoxBusiness.PARAMETER_FILE_ID));
      }
      catch (NumberFormatException e) {
        _fileID = -1;
      }
    }

    if ( iwc.getParameter(BoxBusiness.PARAMETER_PAGE_ID) != null ) {
      try {
        _pageID = Integer.parseInt(iwc.getParameter(BoxBusiness.PARAMETER_PAGE_ID));
      }
      catch (NumberFormatException e) {
        _pageID = -1;
      }
    }

    DropdownMenu localeDrop = ICLocalePresentation.getLocaleDropdownIdKeyed(BoxBusiness.PARAMETER_LOCALE_DROP);
      localeDrop.setToSubmit();
      localeDrop.setSelectedElement(Integer.toString(iLocaleId));
    addLeft(_iwrb.getLocalizedString("locale","Locale")+": ",localeDrop,false);
    addHiddenInput(new HiddenInput(BoxBusiness.PARAMETER_BOX_ID,Integer.toString(_boxID)));

    if ( iwc.getParameter(BoxBusiness.PARAMETER_MODE) != null ) {
      if ( iwc.getParameter(BoxBusiness.PARAMETER_MODE).equalsIgnoreCase(BoxBusiness.PARAMETER_CLOSE) ) {
        closeEditor(iwc);
      }
      else if ( iwc.getParameter(BoxBusiness.PARAMETER_MODE).equalsIgnoreCase(BoxBusiness.PARAMETER_SAVE) ) {
        System.out.println("Saving...");
        saveBoxLink(iwc,iLocaleId,true);
      }
    }

    if ( _linkID != -1 ) {
      if ( iwc.getParameter(BoxBusiness.PARAMETER_DELETE) != null ) {
        deleteBoxLink(iwc);
      }
      else {
        _update = true;
      }
    }

    initializeFields(iLocaleId);
  }

  private void initializeFields(int iLocaleID) {
    BoxLink link = BoxFinder.getLink(_linkID);
    String locString = BoxBusiness.getLocalizedString(link,iLocaleID);

    if ( link == null ) {
      _update = false;
    }
    else {
      _update = true;
    }
    if ( _target == null && _update ) {
      _target = link.getTarget();
      if ( _target == null ) {
        _target = Link.TARGET_BLANK_WINDOW;
      }
    }
    if ( _type == -1 && _update ) {
      if ( link.getPageID() != -1 )
        _type = BoxBusiness.PAGE;
      else if ( link.getFileID() != -1 )
        _type = BoxBusiness.FILE;
      else if ( link.getURL() != null )
        _type = BoxBusiness.LINK;
    }
    if ( _type == -1 ) {
      _type = BoxBusiness.LINK;
    }

    DropdownMenu categoryDrop = BoxBusiness.getCategories(BoxBusiness.PARAMETER_CATEGORY_ID,iLocaleID,BoxFinder.getBox(_boxID),_userID);
      if ( _update ) {
        categoryDrop.setSelectedElement(Integer.toString(link.getBoxCategoryID()));
      }
      else if ( _boxCategoryID != -1 ) {
        categoryDrop.setSelectedElement(Integer.toString(_boxCategoryID));
      }

    TextInput linkName = new TextInput(BoxBusiness.PARAMETER_LINK_NAME);
      linkName.setLength(36);
      if ( _update && locString != null ) {
        linkName.setContent(locString);
        addHiddenInput(new HiddenInput(BoxBusiness.PARAMETER_NEW_OBJECT_INSTANCE,"false"));
      }

    DropdownMenu typeDrop = new DropdownMenu(BoxBusiness.PARAMETER_TYPE);
      typeDrop.addMenuElement(BoxBusiness.LINK,_iwrb.getLocalizedString("link","Link"));
      typeDrop.addMenuElement(BoxBusiness.FILE,_iwrb.getLocalizedString("file","File"));
      typeDrop.addMenuElement(BoxBusiness.PAGE,_iwrb.getLocalizedString("page","Page"));
      typeDrop.setSelectedElement(Integer.toString(_type));
      typeDrop.setToSubmit();

    DropdownMenu targetDrop = new DropdownMenu(BoxBusiness.PARAMETER_TARGET);
      targetDrop.addMenuElement(Link.TARGET_BLANK_WINDOW,_iwrb.getLocalizedString("_blank","New Window"));
      targetDrop.addMenuElement(Link.TARGET_SELF_WINDOW,_iwrb.getLocalizedString("_self","Same Window"));
      targetDrop.addMenuElement(Link.TARGET_PARENT_WINDOW,_iwrb.getLocalizedString("_parent","Parent frame"));
      targetDrop.addMenuElement(Link.TARGET_TOP_WINDOW,_iwrb.getLocalizedString("_top","Top frame"));
      targetDrop.setSelectedElement(_target);

    TextInput linkURL = new TextInput(BoxBusiness.PARAMETER_LINK_URL);
      linkURL.setLength(30);
      if ( _update && link.getURL() != null ) {
        linkURL.setContent(link.getURL());
      }
      else {
        linkURL.setContent("http://");
      }

    /**
     * @todo File uploading
     */

    FileChooser fileChooser = new FileChooser(BoxBusiness.PARAMETER_FILE_ID,STYLE);
    if ( link != null && _update ) {
      if ( link.getFileID() != -1 )
        fileChooser.setSelectedFile(BoxFinder.getFile(link.getFileID()));
    }
    IBPageChooser pageChooser = new IBPageChooser(BoxBusiness.PARAMETER_PAGE_ID,STYLE);
    if ( link != null && _update ) {
      if ( link.getPageID() != -1 )
        pageChooser.setSelectedPage(BoxFinder.getPage(link.getPageID()));
    }

    addLeft(_iwrb.getLocalizedString("category","Category")+":",categoryDrop,true);
    addLeft(_iwrb.getLocalizedString("link_name","Name")+":",linkName,true);
    addLeft(_iwrb.getLocalizedString("type","Type")+":",typeDrop,true);

    if ( _type == BoxBusiness.LINK )
      addLeft(_iwrb.getLocalizedString("link","Link")+":",linkURL,true);
    else if ( _type == BoxBusiness.FILE )
      addLeft(_iwrb.getLocalizedString("file","File")+":",fileChooser,true);
    else if ( _type == BoxBusiness.PAGE )
      addLeft(_iwrb.getLocalizedString("page","Page")+":",pageChooser,true);

    addLeft(_iwrb.getLocalizedString("target","Target")+":",targetDrop,true);

    addHiddenInput(new HiddenInput(BoxBusiness.PARAMETER_LINK_ID,Integer.toString(_linkID)));
    addHiddenInput(new HiddenInput(BoxBusiness.PARAMETER_LOCALE_ID,Integer.toString(iLocaleID)));

    addSubmitButton(new SubmitButton(_iwrb.getLocalizedImageButton("close","CLOSE"),BoxBusiness.PARAMETER_MODE,BoxBusiness.PARAMETER_CLOSE));
    addSubmitButton(new SubmitButton(_iwrb.getLocalizedImageButton("save","SAVE"),BoxBusiness.PARAMETER_MODE,BoxBusiness.PARAMETER_SAVE));
  }

  private void saveBoxLink(IWContext iwc,int iLocaleID,boolean setToClose) {
    String boxLinkName = iwc.getParameter(BoxBusiness.PARAMETER_LINK_NAME);
    String boxLinkURL = iwc.getParameter(BoxBusiness.PARAMETER_LINK_URL);
    String categoryID = iwc.getParameter(BoxBusiness.PARAMETER_CATEGORY_ID);

    String localeString = iwc.getParameter(BoxBusiness.PARAMETER_LOCALE_ID);
    int linkID = -1;

    if ( categoryID != null ) {
      try {
        _boxCategoryID = Integer.parseInt(categoryID);
      }
      catch (NumberFormatException e) {
        _boxCategoryID = -1;
      }
    }

    if ( _type == BoxBusiness.LINK ) {
      _fileID = -1;
      _pageID = -1;
    }
    else if ( _type == BoxBusiness.FILE ) {
      _pageID = -1;
      boxLinkURL = null;
    }
    else if ( _type == BoxBusiness.PAGE ) {
      _fileID = -1;
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
        linkID = BoxBusiness.saveLink(_userID,_boxID,_boxCategoryID,_linkID,boxLinkName,_fileID,_pageID,boxLinkURL,_target,Integer.parseInt(localeString));
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
    BoxBusiness.deleteLink(_linkID);
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
    addLeft(_iwrb.getLocalizedString("no_access","Login first!"));
    addSubmitButton(new CloseButton(_iwrb.getImage("close.gif")));
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

}
