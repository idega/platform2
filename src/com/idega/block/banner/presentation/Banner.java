package com.idega.block.banner.presentation;

import com.idega.block.IWBlock;
import com.idega.jmodule.object.JModuleObject;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.Image;
import com.idega.jmodule.object.textObject.Link;
import com.idega.jmodule.object.textObject.Text;
import com.idega.jmodule.object.interfaceobject.HeaderTable;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWBundle;
import com.idega.core.accesscontrol.business.AccessControl;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.data.ICObjectInstance;
import com.idega.block.banner.data.*;
import com.idega.block.banner.business.*;

public class Banner extends JModuleObject implements IWBlock {

private int _bannerID = -1;
private boolean _isAdmin = false;
private String _attribute;
private int _iLocaleID;

private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.banner";
protected IWResourceBundle _iwrb;
protected IWBundle _iwb;

private Table _myTable;
private boolean _newObjInst = false;
private boolean _newWithAttribute = false;

public Banner(){
}

public Banner(int bannerID){
  this();
	_bannerID = bannerID;
}

public Banner(String attribute){
  this();
	_attribute = attribute;
}

	public void main(ModuleInfo modinfo) throws Exception {
    _iwrb = getResourceBundle(modinfo);
    _iwb = getBundle(modinfo);

    _isAdmin = AccessControl.hasEditPermission(this,modinfo);
    _iLocaleID = ICLocaleBusiness.getLocaleId(modinfo.getCurrentLocale());

    String mode = modinfo.getParameter(BannerBusiness.PARAMETER_MODE);
    if ( mode != null ) {
      doMode(mode,modinfo);
    }

    BannerEntity banner = null;

    _myTable = new Table(1,2);
      _myTable.setCellpadding(0);
      _myTable.setCellspacing(0);
      _myTable.setBorder(0);

    if(_bannerID <= 0){
      String sBannerID = modinfo.getParameter(BannerBusiness.PARAMETER_BANNER_ID);
      if(sBannerID != null)
        _bannerID = Integer.parseInt(sBannerID);
      else if(getICObjectInstanceID() > 0){
        _bannerID = BannerFinder.getObjectInstanceID(getICObjectInstance());
        if(_bannerID <= 0 ){
          BannerBusiness.saveBanner(_bannerID,getICObjectInstanceID(),null);
          _newObjInst = true;
        }
      }
    }

    if ( _newObjInst ) {
      _bannerID = BannerFinder.getObjectInstanceID(new ICObjectInstance(getICObjectInstanceID()));
    }

    if(_bannerID > 0) {
      banner = BannerFinder.getBanner(_bannerID);
    }
    else if ( _attribute != null ){
      banner = BannerFinder.getBanner(_attribute);
      if ( banner != null ) {
        _bannerID = banner.getID();
      }
      else {
        BannerBusiness.saveBanner(-1,-1,_attribute);
      }
      _newWithAttribute = true;
    }

    if ( _newWithAttribute ) {
      _bannerID = BannerFinder.getBanner(_attribute).getID();
    }

    int row = 1;
    if(_isAdmin){
      _myTable.add(getAdminPart(),1,row);
      row++;
    }

    _myTable.add(getBanner(banner),1,row);
    add(_myTable);
	}

  private Link getBanner(BannerEntity banner) {
    Link bannerLink = null;
    AdEntity ad = null;
    Image image = null;

    if ( banner != null ) {
      ad = BannerBusiness.getCurrentAd(banner);
    }

    if ( ad != null ) {
      int imageID = BannerBusiness.getImageID(ad);
      if ( imageID != -1 ) {
        image = BannerBusiness.getImage(imageID);
      }

      if ( image != null ) {
        bannerLink = new Link(image);
        BannerBusiness.updateImpressions(ad);

        bannerLink.addParameter(BannerBusiness.PARAMETER_MODE,BannerBusiness.PARAMETER_CLICKED);
        bannerLink.addParameter(BannerBusiness.PARAMETER_AD_ID,ad.getID());
      }
    }

    if ( bannerLink != null )
      return bannerLink;

    return new Link();
  }

  private Link getAdminPart() {
    Link adminLink = new Link(_iwrb.getImage("bannermanager.gif"));
      adminLink.setWindowToOpen(BannerEditorWindow.class);
      adminLink.addParameter(BannerBusiness.PARAMETER_BANNER_ID,_bannerID);

    return adminLink;
  }

  private void doMode(String mode, ModuleInfo modinfo) {
    if ( mode.equalsIgnoreCase(BannerBusiness.PARAMETER_CLICKED) ) {
      String adID = modinfo.getParameter(BannerBusiness.PARAMETER_AD_ID);
      String URL = null;
      if ( adID != null ) {
        URL = BannerBusiness.updateHits(Integer.parseInt(adID));
      }
      if ( URL != null ) {
        getParentPage().setToRedirect(URL);
      }
    }
  }

  public boolean deleteBlock(int ICObjectInstanceID) {
    BannerEntity banner = BannerFinder.getObjectInstanceFromID(ICObjectInstanceID);
    if ( banner != null ) {
      return BannerBusiness.deleteBanner(banner);
    }
    return false;
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public Object clone() {
    Banner obj = null;
    try {
      obj = (Banner) super.clone();

      if ( this._myTable != null ) {
        obj._myTable = (Table) this._myTable.clone();
      }
    }
    catch (Exception ex) {
      ex.printStackTrace(System.err);
    }
    return obj;
  }
}