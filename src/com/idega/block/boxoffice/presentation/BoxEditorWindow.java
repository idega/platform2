package com.idega.block.boxoffice.presentation;


import java.sql.*;
import java.util.*;
import java.io.*;
import com.idega.util.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.core.localisation.presentation.ICLocalePresentation;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.data.ICLocale;
import com.idega.block.boxoffice.data.*;
import com.idega.block.boxoffice.business.*;
import com.idega.core.accesscontrol.business.AccessControl;
import com.idega.block.login.business.LoginBusiness;
import com.idega.block.text.business.TextFinder;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.builder.presentation.IBFileChooser;
import com.idega.builder.presentation.IBPageChooser;

public class BoxEditorWindow extends IWAdminWindow{

private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.poll";
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

  public void main(ModuleInfo modinfo) throws Exception {
    /**
     * @todo permission
     */
    _isAdmin = true; //AccessControl.hasEditPermission(this,modinfo);
    _superAdmin = AccessControl.isAdmin(modinfo);
    _iwb = getBundle(modinfo);
    _iwrb = getResourceBundle(modinfo);
    addTitle(_iwrb.getLocalizedString("box_admin","Box Admin"));
    Locale currentLocale = modinfo.getCurrentLocale(),chosenLocale;

    try {
      _userID = LoginBusiness.getUser(modinfo).getID();
    }
    catch (Exception e) {
      _userID = -1;
    }

    _editImage = _iwrb.getImage("edit.gif");
      _editImage.setHorizontalSpacing(4);
      _editImage.setVerticalSpacing(3);
    _createImage = _iwrb.getImage("create.gif");
      _createImage.setHorizontalSpacing(4);
      _createImage.setVerticalSpacing(3);
    _deleteImage = _iwrb.getImage("delete.gif");
      _deleteImage.setHorizontalSpacing(4);
      _deleteImage.setVerticalSpacing(3);

    String sLocaleId = modinfo.getParameter(BoxBusiness.PARAMETER_LOCALE_DROP);

    int iLocaleId = -1;
    if(sLocaleId!= null){
      iLocaleId = Integer.parseInt(sLocaleId);
      chosenLocale = TextFinder.getLocale(iLocaleId);
    }
    else{
      chosenLocale = currentLocale;
      iLocaleId = ICLocaleBusiness.getLocaleId(chosenLocale);
    }

    if ( _isAdmin ) {
      processForm(modinfo, iLocaleId,sLocaleId);
    }
    else {
      noAccess();
    }
  }

  private void processForm(ModuleInfo modinfo, int iLocaleId, String sLocaleID) {
    if ( modinfo.getParameter(BoxBusiness.PARAMETER_TYPE) != null ) {
      try {
        _type = Integer.parseInt(modinfo.getParameter(BoxBusiness.PARAMETER_TYPE));
      }
      catch (NumberFormatException e) {
        _type = -1;
      }
    }
    if ( _type == -1 ) {
      _type = BoxBusiness.LINK;
    }

    if ( modinfo.getParameter(BoxBusiness.PARAMETER_TARGET) != null ) {
      _target = modinfo.getParameter(BoxBusiness.PARAMETER_TARGET);
    }

    if ( modinfo.getParameter(BoxBusiness.PARAMETER_BOX_ID) != null ) {
      try {
        _boxID = Integer.parseInt(modinfo.getParameter(BoxBusiness.PARAMETER_BOX_ID));
      }
      catch (NumberFormatException e) {
        _boxID = -1;
      }
    }

    if ( modinfo.getParameter(BoxBusiness.PARAMETER_NEW_OBJECT_INSTANCE) != null ) {
      _newObjInst = true;
      modinfo.setApplicationAttribute(BoxBusiness.PARAMETER_NEW_OBJECT_INSTANCE,BoxBusiness.PARAMETER_TRUE);
    }

    if ( (String) modinfo.getApplicationAttribute(BoxBusiness.PARAMETER_NEW_OBJECT_INSTANCE) != null ) {
      _newObjInst = true;
    }

    if ( modinfo.getParameter(BoxBusiness.PARAMETER_LINK_ID) != null ) {
      try {
        _linkID = Integer.parseInt(modinfo.getParameter(BoxBusiness.PARAMETER_LINK_ID));
        modinfo.setApplicationAttribute(BoxBusiness.PARAMETER_LINK_ID,new Integer(_linkID));
      }
      catch (NumberFormatException e) {
        _linkID = -1;
      }
    }

    if ( sLocaleID != null ) {
      saveBoxLink(modinfo,iLocaleId,false);
    }

    if ( (Integer) modinfo.getApplicationAttribute(BoxBusiness.PARAMETER_LINK_ID) != null ) {
      try {
        _linkID = ((Integer) modinfo.getApplicationAttribute(BoxBusiness.PARAMETER_LINK_ID)).intValue();
      }
      catch (NumberFormatException e) {
        _linkID = -1;
      }
    }

    if ( modinfo.getParameter(BoxBusiness.PARAMETER_CATEGORY_ID) != null ) {
      try {
        _boxCategoryID = Integer.parseInt(modinfo.getParameter(BoxBusiness.PARAMETER_CATEGORY_ID));
      }
      catch (NumberFormatException e) {
        _boxCategoryID = -1;
      }
    }

    if ( modinfo.getParameter(BoxBusiness.PARAMETER_FILE_ID) != null ) {
      try {
        _fileID = Integer.parseInt(modinfo.getParameter(BoxBusiness.PARAMETER_FILE_ID));
      }
      catch (NumberFormatException e) {
        _fileID = -1;
      }
    }

    if ( modinfo.getParameter(BoxBusiness.PARAMETER_PAGE_ID) != null ) {
      try {
        _pageID = Integer.parseInt(modinfo.getParameter(BoxBusiness.PARAMETER_PAGE_ID));
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

    if ( modinfo.getParameter(BoxBusiness.PARAMETER_MODE) != null ) {
      if ( modinfo.getParameter(BoxBusiness.PARAMETER_MODE).equalsIgnoreCase(BoxBusiness.PARAMETER_CLOSE) ) {
        closeEditor(modinfo);
      }
      else if ( modinfo.getParameter(BoxBusiness.PARAMETER_MODE).equalsIgnoreCase(BoxBusiness.PARAMETER_SAVE) ) {
        saveBoxLink(modinfo,iLocaleId,true);
      }
    }

    if ( _linkID != -1 ) {
      if ( modinfo.getParameter(BoxBusiness.PARAMETER_DELETE) != null ) {
        deleteBoxLink(modinfo);
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

    Table categoryTable = new Table(3,1);
      categoryTable.setCellpadding(0);
      categoryTable.setCellspacing(0);
      categoryTable.setWidth(2,1,"6");

    DropdownMenu categoryDrop = BoxBusiness.getCategories(BoxBusiness.PARAMETER_CATEGORY_ID,iLocaleID,BoxFinder.getBox(_boxID),_userID);
      categoryDrop.setAttribute("style",STYLE);
      if ( _update ) {
        categoryDrop.setSelectedElement(Integer.toString(link.getBoxCategoryID()));
      }
      else if ( _boxCategoryID != -1 ) {
        categoryDrop.setSelectedElement(Integer.toString(_boxCategoryID));
      }
    categoryTable.add(categoryDrop,1,1);

    Link categoryLink = new Link(_iwrb.getImage("edit.gif"));
      categoryLink.setWindowToOpen(BoxCategoryEditor.class);
      categoryLink.addParameter(BoxBusiness.PARAMETER_BOX_ID,_boxID);
      categoryLink.addParameter(BoxBusiness.PARAMETER_CATEGORY_ID,_boxCategoryID);
    categoryTable.add(categoryLink,3,1);

    TextInput linkName = new TextInput(BoxBusiness.PARAMETER_LINK_NAME);
      linkName.setLength(36);
      if ( _update && locString != null ) {
        linkName.setContent(locString);
      }

    DropdownMenu typeDrop = new DropdownMenu(BoxBusiness.PARAMETER_TYPE);
      typeDrop.addMenuElement(BoxBusiness.LINK,_iwrb.getLocalizedString("link","Link"));
      typeDrop.addMenuElement(BoxBusiness.FILE,_iwrb.getLocalizedString("file","File"));
      typeDrop.addMenuElement(BoxBusiness.PAGE,_iwrb.getLocalizedString("page","Page"));
      typeDrop.setSelectedElement(Integer.toString(_type));
      typeDrop.setToSubmit();

    DropdownMenu targetDrop = new DropdownMenu(BoxBusiness.PARAMETER_TARGET);
      targetDrop.addMenuElement(Link.TARGET_BLANK_WINDOW,Link.TARGET_BLANK_WINDOW);
      targetDrop.addMenuElement(Link.TARGET_NEW_WINDOW,Link.TARGET_NEW_WINDOW);
      targetDrop.addMenuElement(Link.TARGET_PARENT_WINDOW,Link.TARGET_PARENT_WINDOW);
      targetDrop.addMenuElement(Link.TARGET_TOP_WINDOW,Link.TARGET_TOP_WINDOW);
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

    IBFileChooser fileChooser = new IBFileChooser(BoxBusiness.PARAMETER_FILE_ID,STYLE);
    IBPageChooser pageChooser = new IBPageChooser(BoxBusiness.PARAMETER_PAGE_ID,STYLE);

    addLeft(_iwrb.getLocalizedString("category","Category")+":",categoryTable,true,false);
    addLeft(_iwrb.getLocalizedString("link_name","Name")+":",linkName,true);
    addLeft(_iwrb.getLocalizedString("type","Type"),typeDrop,true);

    if ( _type == BoxBusiness.LINK )
      addLeft(_iwrb.getLocalizedString("link_url","URL")+":",linkURL,true);
    else if ( _type == BoxBusiness.FILE )
      addLeft(_iwrb.getLocalizedString("link_file","File")+":",fileChooser,true);
    else if ( _type == BoxBusiness.PAGE )
      addLeft(_iwrb.getLocalizedString("link_page","Page")+":",pageChooser,true);

    addLeft(_iwrb.getLocalizedString("target","Target")+":",targetDrop,true);

    addHiddenInput(new HiddenInput(BoxBusiness.PARAMETER_LINK_ID,Integer.toString(_linkID)));
    addHiddenInput(new HiddenInput(BoxBusiness.PARAMETER_LOCALE_ID,Integer.toString(iLocaleID)));

    addSubmitButton(new SubmitButton(_iwrb.getImage("close.gif"),BoxBusiness.PARAMETER_MODE,BoxBusiness.PARAMETER_CLOSE));
    addSubmitButton(new SubmitButton(_iwrb.getImage("save.gif"),BoxBusiness.PARAMETER_MODE,BoxBusiness.PARAMETER_SAVE));
  }

  private void saveBoxLink(ModuleInfo modinfo,int iLocaleID,boolean setToClose) {
    String boxLinkName = modinfo.getParameter(BoxBusiness.PARAMETER_LINK_NAME);
    String boxLinkURL = modinfo.getParameter(BoxBusiness.PARAMETER_LINK_URL);
    String categoryID = modinfo.getParameter(BoxBusiness.PARAMETER_CATEGORY_ID);

    String localeString = modinfo.getParameter(BoxBusiness.PARAMETER_LOCALE_ID);
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
      linkID = BoxBusiness.saveLink(_userID,_boxID,_boxCategoryID,_linkID,boxLinkName,_fileID,_pageID,boxLinkURL,_target,Integer.parseInt(localeString));
      modinfo.setApplicationAttribute(BoxBusiness.PARAMETER_LINK_ID,new Integer(linkID));
    }

    if ( setToClose ) {
      modinfo.removeApplicationAttribute(BoxBusiness.PARAMETER_LINK_ID);
      modinfo.removeApplicationAttribute(BoxBusiness.PARAMETER_NEW_OBJECT_INSTANCE);
      setParentToReload();
      close();
    }
  }

  private void deleteBoxLink(ModuleInfo modinfo) {
    modinfo.removeApplicationAttribute(BoxBusiness.PARAMETER_LINK_ID);
    modinfo.removeApplicationAttribute(BoxBusiness.PARAMETER_NEW_OBJECT_INSTANCE);
    BoxBusiness.deleteLink(_linkID);
    setParentToReload();
    close();
  }

  private void closeEditor(ModuleInfo modinfo) {
    modinfo.removeApplicationAttribute(BoxBusiness.PARAMETER_LINK_ID);
    modinfo.removeApplicationAttribute(BoxBusiness.PARAMETER_NEW_OBJECT_INSTANCE);
    if ( this._newObjInst ) {
      deleteBoxLink(modinfo);
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