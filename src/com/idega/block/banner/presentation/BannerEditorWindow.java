package com.idega.block.banner.presentation;


import java.sql.*;
import java.util.*;
import java.io.*;
import com.idega.util.*;
import com.idega.presentation.text.*;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.block.media.presentation.ImageInserter;
import com.idega.block.banner.data.*;
import com.idega.block.banner.business.*;
import com.idega.core.accesscontrol.business.AccessControl;
import com.idega.block.login.business.LoginBusiness;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;

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

private IWBundle _iwb;
private IWResourceBundle _iwrb;

public BannerEditorWindow(){
  setWidth(420);
  setHeight(340);
  setUnMerged();
  setMethod("get");
}

  public void main(IWContext iwc) throws Exception {
    /**
     * @todo permission
     */
    _isAdmin = true; //AccessControl.hasEditPermission(this,iwc);
    _superAdmin = AccessControl.isAdmin(iwc);
    _iwb = getBundle(iwc);
    _iwrb = getResourceBundle(iwc);
    addTitle(_iwrb.getLocalizedString("banner_admin","Banner Admin"));

    try {
      _userID = LoginBusiness.getUser(iwc).getID();
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
        if ( _adID > -2 )
          saveAd(iwc);
      }
    }

    if ( _adID != -1 ) {
      if ( iwc.getParameter(BannerBusiness.PARAMETER_DELETE) != null ) {
        deleteAd(iwc);
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
      adDrop.addMenuElementFirst("-1977","");
      adDrop.setToSubmit();
      adDrop.setAttribute("style",STYLE);
      if ( _adID != -1 ) {
        adDrop.setSelectedElement(Integer.toString(_adID));
      }
    adTable.add(adDrop,1,1);

    Image deleteAdImage = _iwrb.getImage("delete.gif");
      deleteAdImage.setHorizontalSpacing(3);

    Link deleteAdLink = new Link(deleteAdImage);
      deleteAdLink.addParameter(BannerBusiness.PARAMETER_AD_ID,_adID);
      deleteAdLink.addParameter(BannerBusiness.PARAMETER_DELETE,BannerBusiness.PARAMETER_TRUE);
    if ( _update ) {
      adTable.add(deleteAdLink,2,1);
    }

    addLeft(_iwrb.getLocalizedString("ad","Ad")+":",adTable,true,false);

    TextInput adName = new TextInput(BannerBusiness.PARAMETER_AD_NAME);
      adName.setLength(24);
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

    /*ImageInserter image = new ImageInserter(BannerBusiness.PARAMETER_FILE_ID);
      image.setWindowClassToOpen(com.idega.block.media.presentation.SimpleChooserWindow.class);
      image.setHasUseBox(false);
    addRight(_iwrb.getLocalizedString("banner","Banner")+":",image,true,false);*/

    addSubmitButton(new SubmitButton(_iwrb.getImage("close.gif"),BannerBusiness.PARAMETER_MODE,BannerBusiness.PARAMETER_CLOSE));
    addSubmitButton(new SubmitButton(_iwrb.getImage("save.gif"),BannerBusiness.PARAMETER_MODE,BannerBusiness.PARAMETER_SAVE));
  }

  private void saveAd(IWContext iwc) {
    setParentToReload();
    close();
    _adID = -1;
  }

  private void deleteAd(IWContext iwc) {
    BannerBusiness.deleteAd(_adID);
    setParentToReload();
    close();
  }

  private void closeEditor(IWContext iwc) {
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