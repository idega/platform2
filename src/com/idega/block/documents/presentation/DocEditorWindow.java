package com.idega.block.documents.presentation;

import com.idega.block.IWBlock;
import com.idega.block.documents.business.DocBusiness;
import com.idega.block.documents.business.DocFinder;
import com.idega.block.login.business.LoginBusiness;
import com.idega.block.media.presentation.SimpleFileChooser;
import com.idega.block.text.business.TextFinder;
import com.idega.builder.data.IBPage;
import com.idega.builder.presentation.IBPageChooser;
import com.idega.core.data.ICLocale;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.localisation.presentation.ICLocalePresentation;
import com.idega.core.presentation.InformationCategory;
import com.idega.core.presentation.InformationFolder;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.block.business.FolderBlockBusiness;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import java.util.List;
import java.util.Locale;

public class DocEditorWindow extends IWAdminWindow{

  private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.documents";
  private Image _editImage;
  private Image _createImage;
  private Image _deleteImage;
  private IWBundle _iwb;
  private IWResourceBundle _iwrb;
  private FolderBlockBusiness business = null;

  private static final int _ACTION_OPENWINDOW = -1;
  private static final int _ACTION_CHANGELOCALE = 0;
  private static final int _ACTION_CHANGETYPE = 1;
  private static final int _ACTION_UPLOADFILE = 2;
  private static final int _ACTION_SAVEDOC = 3;
  private static final int _ACTION_CLOSEWITHOUTSAVING = 4;
  private int _action = _ACTION_OPENWINDOW;
  private static final String _PRM_LASTACTION = "doc_l_act";
  private static final String _PRM_ACTION = "doc_act";

  private static final int _MODE_NEWDOC = 0;
  public static final int _MODE_EDITDOC = 1;
  public static final int _MODE_DELETE = 2;
  public static final int _MODE_VERSION = 3;
  private int _mode = _MODE_NEWDOC;

  public static String _PRM_MODE = "doc_mode";
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


  private static final String _PRM_TYPE = "doc_type";
  private static final String _PRM_TYPE_OLD_VALUE = "doc_type_old";
  private static final String _PRM_LOCALE = "doc_locale";
  private static final String _PRM_LOCALE_OLD_VALUE = "doc_locale_old";
  private static final String _PRM_LINK_NAME = "link_name";

  private static final String _PRM_FILE_ID = DocBusiness.PARAMETER_FILE_ID;
  private static final String _PRM_PAGE_ID = DocBusiness.PARAMETER_PAGE_ID;
  private static final String _PRM_LINK_URL = DocBusiness.PARAMETER_LINK_URL;

  private static final String _PRM_LAST_UPLOADED_FILE = _PRM_FILE_ID+"_old";
  private static final String _PRM_LAST_SELECTED_PAGE = _PRM_PAGE_ID+"_old";
  private static final String _PRM_LAST_LINK_URL = _PRM_LINK_URL+"_old";

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


  public DocEditorWindow(){
    setWidth(420);
    setHeight(340);
    setUnMerged();
    //setMethod("get");
    setStatus(true);
  }

  public void main(IWContext iwc) throws Exception {

    this.debugParameters(iwc);

    business = FolderBlockBusiness.getInstance();
    /**
     * @todo permission
     */
    _hasEditpermission = true; //AccessControl.hasEditPermission(this,iwc);
    _iwb = iwc.getApplication().getBundle(IWBlock.IW_CORE_BUNDLE_IDENTIFIER);
    _iwrb = getResourceBundle(iwc);
    addTitle(_iwrb.getLocalizedString("doc_admin","Doc Admin"));
    Locale currentLocale = iwc.getCurrentLocale(),chosenLocale;

    try {
      _userID = LoginBusiness.getUser(iwc).getID();
    }
    catch (Exception e) {
      _userID = -1;
    }

    _editImage = _iwb.getImage("shared/edit.gif");
    _createImage = _iwb.getImage("shared/create.gif");
    _deleteImage = _iwb.getImage("shared/delete.gif");

    String sLocaleId = iwc.getParameter(DocBusiness.PARAMETER_LOCALE_DROP);

    int iLocaleId = -1;
    if(sLocaleId!= null){
      iLocaleId = Integer.parseInt(sLocaleId);
      chosenLocale = TextFinder.getLocale(iLocaleId);
    }
    else{
      chosenLocale = currentLocale;
      iLocaleId = ICLocaleBusiness.getLocaleId(chosenLocale);
    }

    setActionAndMode(iwc);
    processParameters(iwc, iLocaleId,sLocaleId);
    setDefultValues();

    //debug
    System.out.println("-----------------------------------");
    System.out.println("_action = "+ _action);
    System.out.println("_mode = "+ _mode);
    System.out.println("-----------------------------------");
    this.debugParameters(iwc);


    if ( _hasEditpermission ) {
      String instId = iwc.getParameter(DocBusiness.PARAMETER_OBJECT_INSTANCE_ID);
      int instanceId = -1;
      if(instId != null){
        instanceId = Integer.parseInt(instId);
      }

      switch (_action) {
        case _ACTION_OPENWINDOW:
          switch (_mode) {
            case _MODE_NEWDOC:
            case _MODE_EDITDOC:
              lineUpElements(iLocaleId, instanceId);
              break;
            case _MODE_DELETE:
              deleteDocLink(iwc);
              closeEditor(iwc);
              break;
            default :
              addLeft("MODE: "+ _mode + ", not yet supported");
              addSubmitButton(new CloseButton(_iwrb.getImage("close.gif")));
              break;
          }
          break;
        case _ACTION_CHANGELOCALE:
        case _ACTION_CHANGETYPE:
        case _ACTION_UPLOADFILE:
          lineUpElements(iLocaleId, instanceId);
          break;
        case _ACTION_SAVEDOC:
          System.out.println("Saving...");
          switch (_type) {
            case DocBusiness.LINK:
              saveLink(iwc,iLocaleId);
              closeEditor(iwc);
              break;
            case DocBusiness.FILE:
              saveFile(iwc,iLocaleId);
              closeEditor(iwc);
              break;
            case DocBusiness.PAGE:
              savePage(iwc,iLocaleId);
              closeEditor(iwc);
              break;
            default:
              //addLeft(_iwrb.getLocalizedString("error_type_not_supported","Type: "+ _type + ", not supported"));
              addLeft("Type: "+ _type + ", not supported");
              addSubmitButton(new CloseButton(_iwrb.getImage("close.gif")));
              break;
          }
          break;
        case _ACTION_CLOSEWITHOUTSAVING:
          closeEditor(iwc);
          break;
        default:
          addLeft("ACTION: "+ _action + ", not yet supported");
          addSubmitButton(new CloseButton(_iwrb.getImage("close.gif")));
          break;
      }
    } else {
      if( _action == _ACTION_CLOSEWITHOUTSAVING){
        closeEditor(iwc);
      } else {
        noAccess();
      }
    }
  }


  private void setDefultValues(){
    if(_type == -1){
//      if(_action == _ACTION_UPLOADFILE){
//        _type = DocBusiness.FILE;
//      } else {
        _type = _DEFAULT_TYPE;
//      }
    }
    if(_target == null){
      _target = _DEFAULT_TARGET;
    }

  }

  private void setActionAndMode(IWContext iwc){

    // mode
    try {
      _mode = Integer.parseInt(iwc.getParameter(_PRM_MODE));
    }
    catch (Exception ex) {
      _mode = _MODE_NEWDOC;
    }

    this.addHiddenInput(new HiddenInput(_PRM_MODE,Integer.toString(_mode)));


    // action
    String type = iwc.getParameter(_PRM_TYPE);
    String typeOld = iwc.getParameter(_PRM_TYPE_OLD_VALUE);
    String locale = iwc.getParameter(_PRM_LOCALE);
    String localeOld = iwc.getParameter(_PRM_LOCALE_OLD_VALUE);
    String file = iwc.getParameter(_PRM_FILE_ID);

    String lastAction = iwc.getParameter(_PRM_LASTACTION);

    if(lastAction != null){
      String action = iwc.getParameter(_PRM_ACTION);
      if(action != null && !"".equals(action)){
        _action = Integer.parseInt(action);
      } else if(file != null || (type != null && type.equals(typeOld) && Integer.toString(DocBusiness.FILE).equals(type))){
        _action = _ACTION_UPLOADFILE;
      } else if( type != null && !type.equals(typeOld)){
        _action = _ACTION_CHANGETYPE;
      } else if( locale != null && !locale.equals(localeOld)){
        _action = _ACTION_CHANGELOCALE;
      }
    } else {
      _action = _ACTION_OPENWINDOW;
    }

    this.addHiddenInput(new HiddenInput(_PRM_LASTACTION,Integer.toString(_action)));
    if(type != null){
      this.addHiddenInput(new HiddenInput(_PRM_TYPE_OLD_VALUE,type));
    }
    if(locale != null){
      this.addHiddenInput(new HiddenInput(_PRM_LOCALE_OLD_VALUE,locale));
    }
  }


  private void processParameters(IWContext iwc, int iLocaleId, String sLocaleID) {

    if ( iwc.getParameter(_PRM_TYPE) != null ) {
      try {
        _type = Integer.parseInt(iwc.getParameter(_PRM_TYPE));
      }
      catch (NumberFormatException e) {
        _type = -1;
      }
    } else {
      if ( iwc.getParameter(_PRM_TYPE_OLD_VALUE) != null ) {
        try {
          _type = Integer.parseInt(iwc.getParameter(_PRM_TYPE_OLD_VALUE));
        }
        catch (NumberFormatException e) {
          _type = -1;
        }
      }
    }

    String sLocale = iwc.getParameter(DocBusiness.PARAMETER_CONTENT_LOCALE_IDENTIFIER);
    if ( sLocale != null ) {
      try {
        _contentLocaleId = Integer.parseInt(sLocale);
        this.addHiddenInput(new HiddenInput(DocBusiness.PARAMETER_CONTENT_LOCALE_IDENTIFIER,Integer.toString(_contentLocaleId)));
      } catch (NumberFormatException e) {
        ICLocale locale = ICLocaleBusiness.getICLocale(sLocale);
        if(locale != null){
          _contentLocaleId = locale.getID();
          this.addHiddenInput(new HiddenInput(DocBusiness.PARAMETER_CONTENT_LOCALE_IDENTIFIER,Integer.toString(_contentLocaleId)));
        } else {
          _contentLocaleId = -1;
        }
      }
    }

    String localeString = iwc.getParameter(DocBusiness.PARAMETER_LOCALE_ID);
    if(localeString != null){
      try {
        if(localeString != null){
          _localeID = Integer.parseInt(localeString);
        } else {
          _localeID = -1;
        }
      }
      catch (NumberFormatException e) {
        _localeID = -1;
      }
    }


    if ( iwc.getParameter(DocBusiness.PARAMETER_OBJECT_ID) != null ) {
      try {
        _iObjId = Integer.parseInt(iwc.getParameter(DocBusiness.PARAMETER_OBJECT_ID));
      }
      catch (NumberFormatException e) {
        _iObjId = -1;
      }
    }



    if ( iwc.getParameter(DocBusiness.PARAMETER_TARGET) != null ) {
      _target = iwc.getParameter(DocBusiness.PARAMETER_TARGET);
    } else {
      _target = null;
    }

    if ( iwc.getParameter(_PRM_LINK_NAME) != null ) {
      _docLinkName = iwc.getParameter(_PRM_LINK_NAME);
    } else {
      _docLinkName = null;
    }

    if ( iwc.getParameter(DocBusiness.PARAMETER_LINK_ID) != null ) {
      try {
        _linkID = Integer.parseInt(iwc.getParameter(DocBusiness.PARAMETER_LINK_ID));
      }
      catch (NumberFormatException e) {
        _linkID = -1;
      }
    }

    if ( (Integer) iwc.getSessionAttribute(DocBusiness.PARAMETER_LINK_ID) != null ) {
      try {
        _linkID = ((Integer) iwc.getSessionAttribute(DocBusiness.PARAMETER_LINK_ID)).intValue();
      }
      catch (NumberFormatException e) {
        _linkID = -1;
      }
    }

    if ( iwc.getParameter(DocBusiness.PARAMETER_CATEGORY_ID) != null ) {
      try {
        _infoCatID = Integer.parseInt(iwc.getParameter(DocBusiness.PARAMETER_CATEGORY_ID));
      }
      catch (NumberFormatException e) {
        _infoCatID = -1;
      }
    }

    if ( iwc.getParameter(_PRM_FILE_ID) != null ) {
      try {
        _fileID = Integer.parseInt(iwc.getParameter(_PRM_FILE_ID));
      }
      catch (NumberFormatException e) {
        _fileID = -1;
      }
      this.addHiddenInput(new HiddenInput(_PRM_LAST_UPLOADED_FILE,iwc.getParameter(_PRM_FILE_ID)));
    } else if(iwc.getParameter(_PRM_LAST_UPLOADED_FILE) != null ){
      try {
        _fileID = Integer.parseInt(iwc.getParameter(_PRM_LAST_UPLOADED_FILE));
      }
      catch (NumberFormatException e) {
        _pageID = -1;
      }
      switch (_action) {
        case _ACTION_UPLOADFILE:
          break;
        default:
          this.addHiddenInput(new HiddenInput(_PRM_LAST_UPLOADED_FILE,iwc.getParameter(_PRM_LAST_UPLOADED_FILE)));
          break;
      }
    }

    if ( iwc.getParameter(_PRM_PAGE_ID) != null ) {
      try {
        _pageID = Integer.parseInt(iwc.getParameter(_PRM_PAGE_ID));
      }
      catch (NumberFormatException e) {
        _pageID = -1;
      }
      this.addHiddenInput(new HiddenInput(_PRM_LAST_SELECTED_PAGE,iwc.getParameter(_PRM_PAGE_ID)));
    }else if(iwc.getParameter(_PRM_LAST_SELECTED_PAGE) != null ){
      try {
        _pageID = Integer.parseInt(iwc.getParameter(_PRM_LAST_SELECTED_PAGE));
      }
      catch (NumberFormatException e) {
        _pageID = -1;
      }
      this.addHiddenInput(new HiddenInput(_PRM_LAST_SELECTED_PAGE,iwc.getParameter(_PRM_LAST_SELECTED_PAGE)));
    }


    if ( iwc.getParameter(_PRM_LINK_URL) != null ) {
      _linkUrl = iwc.getParameter(_PRM_LINK_URL);
      if(_linkUrl.equals("")){
        _linkUrl = null;
      }
      if(_linkUrl != null){
        this.addHiddenInput(new HiddenInput(_PRM_LAST_LINK_URL,_linkUrl));
      }
    }else if(iwc.getParameter(_PRM_LAST_LINK_URL) != null ){
      _linkUrl = iwc.getParameter(_PRM_LAST_LINK_URL);
      if(_linkUrl.equals("")){
        _linkUrl = null;
      }
      if(_linkUrl != null){
        this.addHiddenInput(new HiddenInput(_PRM_LAST_LINK_URL,_linkUrl));
      }
    }

    String objId = iwc.getParameter(_PRM_OBJECT_ID);
    if ( objId != null ) {
      try {
        _iObjId = Integer.parseInt(objId);
      }
      catch (NumberFormatException e) {
        _iObjId = -1;
      }
    }

    String instanceId = iwc.getParameter(_PRM_OBJECT_INSTANCE_ID);
    if ( instanceId != null ) {
      try {
        _iObjInsId = Integer.parseInt(instanceId);
      }
      catch (NumberFormatException e) {
        _iObjInsId = -1;
      }
    }

    String folderID = iwc.getParameter(_PRM_FOLDER_ID);
    if ( folderID != null ) {
      try {
        _folderID = Integer.parseInt(folderID);
      }
      catch (NumberFormatException e) {
        _folderID = -1;
      }
    }

  }

  private void lineUpElements(int iLocaleId, int instanceId) {
//    DocLink link = DocFinder.getLink(_linkID);
//    /**
//     * @todo: localice
//     */
//
//    String docLinkName = null;

    this.getUnderlyingForm().maintainParameter(_PRM_OBJECT_ID);
    this.getUnderlyingForm().maintainParameter(_PRM_OBJECT_INSTANCE_ID);
    this.getUnderlyingForm().maintainParameter(_PRM_FOLDER_ID);


    if(_contentLocaleId != -1){
      this.addHiddenInput(new HiddenInput(DocBusiness.PARAMETER_LOCALE_DROP, Integer.toString(_contentLocaleId)));
    }else{
      DropdownMenu localeDrop = ICLocalePresentation.getLocaleDropdownIdKeyed(DocBusiness.PARAMETER_LOCALE_DROP);
        localeDrop.setToSubmit();
        localeDrop.setSelectedElement(Integer.toString(iLocaleId));
      addLeft(_iwrb.getLocalizedString("locale","Locale")+": ",localeDrop,false);
    }


    DropdownMenu categoryDrop = new DropdownMenu(DocBusiness.PARAMETER_CATEGORY_ID);

    List list = null;
    if(instanceId != -1){
      list = business.getInstanceCategories(instanceId);
    }

    if( list != null ) {
      for ( int a = 0; a < list.size(); a++) {
//        LocalizedText locText = TextFinder.getLocalizedText((ICInformationCategory)list.get(a),iLocaleId);
//        String locString = "$language$";
//        if ( locText != null ) {
//          locString = locText.getHeadline();
          String catName = ((InformationCategory)list.get(a)).getName();
//        }
        categoryDrop.addMenuElement(((InformationCategory)list.get(a)).getID(),catName);
      }
      categoryDrop.setSelectedElement(Integer.toString(_infoCatID));
    }



    TextInput linkName = new TextInput(_PRM_LINK_NAME);
      linkName.setLength(36);
      if(_docLinkName != null){
        linkName.setContent(_docLinkName);
      }

    DropdownMenu typeDrop = new DropdownMenu(_PRM_TYPE);
      typeDrop.addMenuElement(DocBusiness.LINK,_iwrb.getLocalizedString("link","Link"));
      typeDrop.addMenuElement(DocBusiness.FILE,_iwrb.getLocalizedString("file","File"));
      typeDrop.addMenuElement(DocBusiness.PAGE,_iwrb.getLocalizedString("page","Page"));
      typeDrop.setSelectedElement(Integer.toString(_type));
      typeDrop.setToSubmit();

    DropdownMenu targetDrop = new DropdownMenu(DocBusiness.PARAMETER_TARGET);
      targetDrop.addMenuElement(Link.TARGET_BLANK_WINDOW,_iwrb.getLocalizedString("_blank","New Window"));
      targetDrop.addMenuElement(Link.TARGET_SELF_WINDOW,_iwrb.getLocalizedString("_self","Same Window"));
      targetDrop.addMenuElement(Link.TARGET_PARENT_WINDOW,_iwrb.getLocalizedString("_parent","Parent frame"));
      targetDrop.addMenuElement(Link.TARGET_TOP_WINDOW,_iwrb.getLocalizedString("_top","Top frame"));
      if(_target != null){
        targetDrop.setSelectedElement(_target);
      }


    addLeft(_iwrb.getLocalizedString("category","Category")+":",categoryDrop,true);
    addLeft(_iwrb.getLocalizedString("link_name","Name")+":",linkName,true);
    addLeft(_iwrb.getLocalizedString("type","Type")+":",typeDrop,true);


    switch (_type) {
      case DocBusiness.LINK:
        TextInput linkURL = new TextInput(_PRM_LINK_URL);
        linkURL.setLength(30);
        if(_linkUrl != null){
          linkURL.setContent(_linkUrl);
        } else {
          linkURL.setContent("http://");
        }
        addLeft(_iwrb.getLocalizedString("link","Link")+":",linkURL,true);
        break;
      case DocBusiness.FILE:
        SimpleFileChooser fileChooser = new SimpleFileChooser(this.getUnderlyingForm(),_PRM_FILE_ID);
        this.setStyle(fileChooser);
        if ( _action == _ACTION_CHANGETYPE && _fileID != -1 ){
          fileChooser.setSelectedFile(_fileID);
        }
        addLeft(_iwrb.getLocalizedString("file","File")+":",fileChooser,true);
        break;
      case DocBusiness.PAGE:
        IBPageChooser pageChooser = new IBPageChooser(_PRM_PAGE_ID,STYLE);
        if ( _pageID != -1 ){
          IBPage page = DocFinder.getPage(_pageID);
          if(page != null){
            pageChooser.setSelectedPage(_pageID,page.getName());
          }
        }
        addLeft(_iwrb.getLocalizedString("page","Page")+":",pageChooser,true);
        break;
    }

    addLeft(_iwrb.getLocalizedString("target","Target")+":",targetDrop,true);


    addSubmitButton(new SubmitButton(_iwrb.getLocalizedImageButton("close","CLOSE"),_PRM_ACTION,Integer.toString(_ACTION_CLOSEWITHOUTSAVING)));
    addSubmitButton(new SubmitButton(_iwrb.getLocalizedImageButton("save","SAVE"),_PRM_ACTION,Integer.toString(_ACTION_SAVEDOC)));
  }

  /**
   * @todo implement
   * @param iwc
   * @param iLocaleID
   */
  private void saveFile(IWContext iwc,int iLocaleID){
    saveDocLink(iwc,iLocaleID);
  }

  /**
   * @todo implement
   * @param iwc
   * @param iLocaleID
   */
  private void savePage(IWContext iwc,int iLocaleID){
    saveDocLink(iwc,iLocaleID);
  }

  /**
   * @todo implement
   * @param iwc
   * @param iLocaleID
   */
  private void saveLink(IWContext iwc,int iLocaleID){
    saveDocLink(iwc,iLocaleID);
  }

  private boolean saveDocLink(IWContext iwc,int iLocaleID) {

    int fileID = -1;
    int pageID = -1;
    String linkUrl = null;
    String docLinkName = "Untitled";
    int folderID = -1;
    int localeID = _contentLocaleId;

    if(_contentLocaleId == -1 && _localeID == -1){
      localeID = iwc.getCurrentLocaleId();
    } else if(_contentLocaleId != -1){
      localeID = _contentLocaleId;
    } else {
      localeID = _localeID;
    }


    switch (_type) {
      case DocBusiness.LINK:
        if ( _linkUrl != null && !( _linkUrl.equalsIgnoreCase("http://") && _linkUrl.length() > 0 ) ) {
          linkUrl = _linkUrl;
        }
        break;
      case DocBusiness.FILE:
        fileID = _fileID;
        break;
      case DocBusiness.PAGE:
        pageID = _pageID;
        break;
      default:
        return false;
    }

    if ( _docLinkName  != null && _docLinkName.length() > 0 ) {
      docLinkName = _docLinkName;
    }

    System.out.println("_iObjInsId = "+_iObjInsId+" docLinkName = "+docLinkName);


    if ( docLinkName != null && _iObjInsId != -1) {
      if(_folderID == -1){
        InformationFolder folder = business.getInstanceWorkeFolder(_iObjInsId, _iObjId, localeID, true);
        if(folder != null){
          folderID = folder.getID();
        }
      } else {
        folderID = _folderID;
      }
      try {
        _linkID = DocBusiness.saveLink(_userID,_infoCatID,folderID,_linkID,docLinkName,fileID,pageID,linkUrl,_target,localeID);
        return true;
      }
      catch (Exception e) {
        e.printStackTrace(System.err);
      }
    }
    return false;
  }

  private void deleteDocLink(IWContext iwc) {
    System.out.println("Deleting...");
    //tmp
    iwc.removeSessionAttribute(DocBusiness.PARAMETER_LINK_ID);
    DocBusiness.deleteLink(_linkID);
//    setParentToReload();
//    close();
  }

  private void closeEditor(IWContext iwc) {
    //tmp
    iwc.removeSessionAttribute(DocBusiness.PARAMETER_LINK_ID);
    setParentToReload();
    close();
  }

  private void noAccess() throws Exception {
    addLeft(_iwrb.getLocalizedString("no_access","Login first!"));
    addSubmitButton(new CloseButton(_iwrb.getImage("close.gif")));
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

}
