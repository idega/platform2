package com.idega.block.banner.presentation;


import java.io.IOException;
import java.sql.SQLException;

import com.idega.block.banner.business.BannerBusiness;
import com.idega.block.banner.business.BannerFinder;
import com.idega.block.banner.data.AdEntity;
import com.idega.block.banner.data.BannerEntity;
import com.idega.block.media.presentation.ImageInserter;
import com.idega.core.accesscontrol.business.LoginBusinessBean;
import com.idega.core.file.data.ICFile;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.block.presentation.Builderaware;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWTimestamp;

public class BannerEditorWindow extends IWAdminWindow{

private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.banner";
private boolean _isAdmin = false;
private boolean _superAdmin = false;
private boolean _update = false;
private boolean _save = false;
private int _iObjInsId = -1;

private int _bannerID = -1;
private int _adID = -1;
private int _userID = -1;
private boolean _newObjInst = false;
private String _newWithAttribute;
private Image _editImage;
private Image _createImage;
private Image _deleteImage;
private Image _detachImage;

private IWBundle _iwb;
private IWResourceBundle _iwrb;

public BannerEditorWindow(){
  setWidth(440);
  setHeight(420);
  setUnMerged();
  setMethod("get");
}

  public void main(IWContext iwc) throws Exception {
    /**
     * @todo permission
     */
    _isAdmin = true;
    _superAdmin = iwc.hasEditPermission(this);
    _iwb = iwc.getIWMainApplication().getBundle(Builderaware.IW_CORE_BUNDLE_IDENTIFIER);
    _iwrb = getResourceBundle(iwc);
    addTitle(_iwrb.getLocalizedString("banner_admin","Banner Admin"));

    try {
      _userID = LoginBusinessBean.getUser(iwc).getID();
    }
    catch (Exception e) {
      _userID = -1;
    }


    _editImage = _iwb.getImage("shared/edit.gif",_iwrb.getLocalizedString("edit","Edit"));
      //_editImage.setHorizontalSpacing(4);
      //_editImage.setVerticalSpacing(3);
    _createImage = _iwb.getImage("shared/create.gif",_iwrb.getLocalizedString("create","Create"));
      //_createImage.setHorizontalSpacing(4);
      //_createImage.setVerticalSpacing(3);
    _deleteImage = _iwb.getImage("shared/delete.gif",_iwrb.getLocalizedString("delete","Delete"));
      //_deleteImage.setHorizontalSpacing(4);
      //_deleteImage.setVerticalSpacing(3);
    _detachImage = _iwb.getImage("shared/detach.gif",_iwrb.getLocalizedString("detach","Detach"));

    if ( _isAdmin ) {
      processForm(iwc);
    }
    else {
      noAccess();
    }
  }

  private void processForm(IWContext iwc) {
    if ( iwc.getParameter(BannerBusiness.PARAMETER_BANNER_ID) != null ) {
      try {
        _bannerID = Integer.parseInt(iwc.getParameter(BannerBusiness.PARAMETER_BANNER_ID));
      }
      catch (NumberFormatException e) {
        _bannerID = -1;
      }
    }

    if ( iwc.getParameter(BannerBusiness.PARAMETER_AD_ID) != null ) {
      try {
        _adID = Integer.parseInt(iwc.getParameter(BannerBusiness.PARAMETER_AD_ID));
      }
      catch (NumberFormatException e) {
        _adID = -1;
      }
    }

    if ( iwc.getParameter(BannerBusiness.PARAMETER_MODE) != null ) {
      if ( iwc.getParameter(BannerBusiness.PARAMETER_MODE).equalsIgnoreCase(BannerBusiness.PARAMETER_CLOSE) ) {
        closeEditor(iwc);
      }
      else if ( iwc.getParameter(BannerBusiness.PARAMETER_MODE).equalsIgnoreCase(BannerBusiness.PARAMETER_SAVE) ) {
        if ( _adID > -2 ) {
          saveAd(iwc);
        		clearCache(iwc);
        }
      }
      else if ( iwc.getParameter(BannerBusiness.PARAMETER_MODE).equalsIgnoreCase(BannerBusiness.PARAMETER_NEW) ) {
        newAd(iwc);
      }
    }

    if ( iwc.getParameter(BannerBusiness.PARAMETER_DELETE_FILE) != null ) {
      if ( iwc.getParameter(BannerBusiness.PARAMETER_DELETE_FILE).equalsIgnoreCase(BannerBusiness.PARAMETER_TRUE) ) {
        removeFile(iwc);
        clearCache(iwc);
      }
    }

    if ( iwc.getParameter(BannerBusiness.PARAMETER_DETACH_AD) != null ) {
      if ( iwc.getParameter(BannerBusiness.PARAMETER_DETACH_AD).equalsIgnoreCase(BannerBusiness.PARAMETER_TRUE) ) {
        detachAd();
        clearCache(iwc);
      }
    }

    if ( _adID != -1 ) {
      if ( iwc.getParameter(BannerBusiness.PARAMETER_DELETE) != null ) {
        deleteAd(iwc);
        clearCache(iwc);
      }
      else {
        _update = true;
      }
    }

    initializeFields();
  }

  private void initializeFields() {
    BannerEntity banner = BannerFinder.getBanner(_bannerID);
    AdEntity ad = null;
    if ( _update ) {
      ad = BannerFinder.getAd(_adID);
    }

    Table adTable = new Table();
      adTable.setCellpadding(0);
      adTable.setCellspacing(0);

    DropdownMenu adDrop = BannerBusiness.getAdsDrop(BannerBusiness.PARAMETER_AD_ID,banner,_userID);
      adDrop.addMenuElementFirst("-1","");
      adDrop.setToSubmit();
      adDrop.setMarkupAttribute("style",STYLE);
      if ( _adID != -1 ) {
        adDrop.setSelectedElement(Integer.toString(_adID));
      }
    adTable.add(adDrop,1,1);

    Link newAdLink = new Link(_createImage);
      newAdLink.addParameter(BannerBusiness.PARAMETER_BANNER_ID,_bannerID);
      newAdLink.addParameter(BannerBusiness.PARAMETER_MODE,BannerBusiness.PARAMETER_NEW);
    if ( _update ) {
      adTable.setWidth(2,1,"5");
      adTable.add(newAdLink,3,1);
    }

    Link deleteAdLink = new Link(_deleteImage);
      deleteAdLink.addParameter(BannerBusiness.PARAMETER_BANNER_ID,_bannerID);
      deleteAdLink.addParameter(BannerBusiness.PARAMETER_AD_ID,_adID);
      deleteAdLink.addParameter(BannerBusiness.PARAMETER_DELETE,BannerBusiness.PARAMETER_TRUE);
    if ( _update ) {
      adTable.add(deleteAdLink,3,1);
    }

    Link detachAdLink = new Link(_detachImage);
      detachAdLink.addParameter(BannerBusiness.PARAMETER_BANNER_ID,_bannerID);
      detachAdLink.addParameter(BannerBusiness.PARAMETER_AD_ID,_adID);
      detachAdLink.addParameter(BannerBusiness.PARAMETER_DETACH_AD,BannerBusiness.PARAMETER_TRUE);
    if ( _update && BannerBusiness.isRelated(_bannerID,_adID) ) {
      adTable.add(detachAdLink,3,1);
    }

    addLeft(_iwrb.getLocalizedString("ad","Ad")+":",adTable,true,false);

    TextInput adName = new TextInput(BannerBusiness.PARAMETER_AD_NAME);
      adName.setLength(32);
      if ( ad != null && ad.getAdName() != null ) {
        adName.setContent(ad.getAdName());
      }
    addLeft(_iwrb.getLocalizedString("name","Name")+":",adName,true);

    TextInput adMaxHits = new TextInput(BannerBusiness.PARAMETER_MAX_HITS);
      adMaxHits.setLength(6);
      if ( ad != null && ad.getMaxHits() != -1 ) {
        adMaxHits.setContent(Integer.toString(ad.getMaxHits()));
      }
    addLeft(_iwrb.getLocalizedString("max_hits","Max hits")+": ",adMaxHits,false);

    TextInput adMaxImpressions = new TextInput(BannerBusiness.PARAMETER_MAX_IMPRESSIONS);
      adMaxImpressions.setLength(6);
      if ( ad != null && ad.getMaxImpressions() != -1 ) {
        adMaxImpressions.setContent(Integer.toString(ad.getMaxImpressions()));
      }
    addLeft(_iwrb.getLocalizedString("max_impressions","Max impressions")+": ",adMaxImpressions,false);

    TextInput adURL = new TextInput(BannerBusiness.PARAMETER_URL);
      adURL.setLength(24);
      if ( ad != null && ad.getURL() != null ) {
        adURL.setContent(ad.getURL());
      }
      else {
        adURL.setContent("http://");
      }
    addLeft(_iwrb.getLocalizedString("url","URL")+":",adURL,true);

    DateInput beginDate = new DateInput(BannerBusiness.PARAMETER_BEGIN_DATE);
      beginDate.setStyleAttribute("style",STYLE);
      if ( ad != null && ad.getBeginDate() != null ) {
        beginDate.setDate(new IWTimestamp(ad.getBeginDate()).getSQLDate());
      }
    addLeft(_iwrb.getLocalizedString("valid_from","Valid from")+": ",beginDate,true);

    DateInput endDate = new DateInput(BannerBusiness.PARAMETER_END_DATE);
      endDate.setStyleAttribute("style",STYLE);
      if ( ad != null && ad.getEndDate() != null ) {
        endDate.setDate(new IWTimestamp(ad.getEndDate()).getSQLDate());
      }
    addLeft(_iwrb.getLocalizedString("valid_to","Valid to")+": ",endDate,true);

    ImageInserter image = new ImageInserter(BannerBusiness.PARAMETER_FILE_ID);
      image.setHasUseBox(false);
    addRight(_iwrb.getLocalizedString("new_image","New image")+":",image,true,false);

    if ( ad != null ) {
      ICFile[] files = BannerFinder.getFilesInAd(ad);
      if ( files != null ) {
        Table filesTable = new Table();
          filesTable.setWidth("100%");
          filesTable.setCellpadding(2);
          filesTable.setCellspacing(0);

        Image fileImage;
        Link deleteFile;

        for ( int a = 0; a < files.length; a++ ) {
          try {
            fileImage = new Image(((Integer)files[a].getPrimaryKey()).intValue());
          }
          catch (Exception e) {
            fileImage = new Image();
          }
          fileImage.setWidth(120);
          fileImage.setBorder(1);

          deleteFile = new Link(_deleteImage);
            deleteFile.addParameter(BannerBusiness.PARAMETER_BANNER_ID,_bannerID);
            deleteFile.addParameter(BannerBusiness.PARAMETER_AD_ID,_adID);
            deleteFile.addParameter(BannerBusiness.PARAMETER_FILE_ID,files[a].getPrimaryKey().toString());
            deleteFile.addParameter(BannerBusiness.PARAMETER_DELETE_FILE,BannerBusiness.PARAMETER_TRUE);

          filesTable.add(deleteFile,1,a+1);
          filesTable.add(fileImage,2,a+1);
        }

        filesTable.setColumnVerticalAlignment(1,"top");
        addRight("",filesTable,false,false);
      }
    }

    addHiddenInput(new HiddenInput(BannerBusiness.PARAMETER_BANNER_ID,Integer.toString(_bannerID)));
    addSubmitButton(new SubmitButton(_iwrb.getLocalizedImageButton("close","CLOSE"),BannerBusiness.PARAMETER_MODE,BannerBusiness.PARAMETER_CLOSE));
    addSubmitButton(new SubmitButton(_iwrb.getLocalizedImageButton("save","SAVE"),BannerBusiness.PARAMETER_MODE,BannerBusiness.PARAMETER_SAVE));
  }

  private void saveAd(IWContext iwc) {
    String name = iwc.getParameter(BannerBusiness.PARAMETER_AD_NAME);
    String maxHits = iwc.getParameter(BannerBusiness.PARAMETER_MAX_HITS);
    String maxImpressions = iwc.getParameter(BannerBusiness.PARAMETER_MAX_IMPRESSIONS);
    String url = iwc.getParameter(BannerBusiness.PARAMETER_URL);
    String beginDate = iwc.getParameter(BannerBusiness.PARAMETER_BEGIN_DATE);
    String endDate = iwc.getParameter(BannerBusiness.PARAMETER_END_DATE);
    String fileID = iwc.getParameter(BannerBusiness.PARAMETER_FILE_ID);
    if ( fileID == null || fileID.length() == 0 ) {
      fileID = "-1";
    }

    _adID = BannerBusiness.saveAd(_userID,_bannerID,_adID,name,maxHits,maxImpressions,beginDate,endDate,url,fileID);
  }
  
  private void clearCache(IWContext iwc) {
    iwc.getIWMainApplication().getIWCacheManager().invalidateCache(Banner.CACHE_KEY);
  }

  private void newAd(IWContext iwc) {
    _adID = -1;
  }

  private void deleteAd(IWContext iwc) {
    BannerBusiness.deleteAd(_adID);
  }

  private void closeEditor(IWContext iwc) {
    setParentToReload();
    close();
  }

  private void detachAd() {
    BannerBusiness.removeAdFromBanner(_adID,_bannerID);
  }

  private void removeFile(IWContext iwc) {
    String fileID = iwc.getParameter(BannerBusiness.PARAMETER_FILE_ID);
    int _fileID = -1;

    try {
      _fileID = Integer.parseInt(fileID);
    }
    catch (NumberFormatException e) {
      _fileID = -1;
    }

    BannerBusiness.removeFileFromAd(_adID,_fileID);
  }

  private void noAccess() throws IOException,SQLException {
    addLeft(_iwrb.getLocalizedString("no_access","Login first!"));
    addSubmitButton(new CloseButton(_iwrb.getImage("close.gif")));
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }
}
