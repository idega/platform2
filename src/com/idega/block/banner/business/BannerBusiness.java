package com.idega.block.banner.business;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import com.idega.block.banner.data.AdEntity;
import com.idega.block.banner.data.BannerEntity;
import com.idega.core.component.data.ICObjectInstance;
import com.idega.core.file.data.ICFile;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.util.IWTimestamp;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class BannerBusiness {

public static final String PARAMETER_AD_ID = "ad_id";
public static final String PARAMETER_AD_NAME = "ad_name";
public static final String PARAMETER_BANNER_ID = "banner_id";
public static final String PARAMETER_BEGIN_DATE = "begin_date";
public static final String PARAMETER_CLICKED = "clicked";
public static final String PARAMETER_CLOSE = "close";
public static final String PARAMETER_DELETE = "delete";
public static final String PARAMETER_DELETE_FILE = "delete_file";
public static final String PARAMETER_DETACH_AD = "detach_ad";
public static final String PARAMETER_END_DATE = "end_date";
public static final String PARAMETER_FALSE = "false";
public static final String PARAMETER_FILE_ID = "file_id";
public static final String PARAMETER_LINK_URL = "link_url";
public static final String PARAMETER_MAX_HITS = "max_hits";
public static final String PARAMETER_MAX_IMPRESSIONS = "max_impressions";
public static final String PARAMETER_MODE = "mode";
public static final String PARAMETER_NEW = "new";
public static final String PARAMETER_NEW_ATTRIBUTE = "new_attribute";
public static final String PARAMETER_NEW_OBJECT_INSTANCE = "new_obj_inst";
public static final String PARAMETER_SAVE = "save";
public static final String PARAMETER_TRUE = "true";
public static final String PARAMETER_URL = "url";

public static final String COOKIE_NAME = "idegaAD_";

  public static void saveBanner(int bannerID,int InstanceId,String attribute){
    try {
      boolean update = false;

      BannerEntity banner = ((com.idega.block.banner.data.BannerEntityHome)com.idega.data.IDOLookup.getHomeLegacy(BannerEntity.class)).createLegacy();
      if ( bannerID != -1 ) {
        update = true;
        banner = BannerFinder.getBanner(bannerID);
        if ( banner == null ) {
          banner = ((com.idega.block.banner.data.BannerEntityHome)com.idega.data.IDOLookup.getHomeLegacy(BannerEntity.class)).createLegacy();
          update = false;
        }
      }

      if(attribute != null){
        BannerEntity bannerAttribute = BannerFinder.getBanner(attribute);
        if ( bannerAttribute != null ) {
          banner = bannerAttribute;
          update = true;
        }
        banner.setAttribute(attribute);
      }
      else {
      		banner.setAttribute("BLANK_VALUE");
      }

      if ( update ) {
        try {
          banner.update();
        }
        catch (SQLException e) {
          e.printStackTrace(System.err);
        }
      }
      else {
        banner.insert();
        if(InstanceId > 0){
          ICObjectInstance objIns = ((com.idega.core.component.data.ICObjectInstanceHome)com.idega.data.IDOLookup.getHomeLegacy(ICObjectInstance.class)).findByPrimaryKeyLegacy(InstanceId);
          banner.addTo(objIns);
        }
      }
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public static void addToBanner(BannerEntity banner, int adID) {
    try {
      AdEntity ad = BannerFinder.getAd(adID);
      addToBanner(banner,ad);
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  public static void addToBanner(BannerEntity banner, AdEntity ad) {
    try {
      if ( ad != null && banner != null ) {
        AdEntity[] ads = (AdEntity[]) banner.findRelated(ad);
        if ( ads == null || ads.length == 0 ) {
          banner.addTo(ad);
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  public static boolean deleteBanner(BannerEntity banner) {
    try {
      if ( banner != null ) {
        banner.delete();
      }
      return true;
    }
    catch (SQLException e) {
      e.printStackTrace(System.err);
      return false;
    }
  }

  public static int saveAd(int userID,int bannerID,int adID,String adName,String maxHits,String maxImpressions,String beginDate,String endDate,String URL,String fileID) {
    boolean update = false;
    BannerEntity banner = BannerFinder.getBanner(bannerID);

    AdEntity ad = ((com.idega.block.banner.data.AdEntityHome)com.idega.data.IDOLookup.getHomeLegacy(AdEntity.class)).createLegacy();
    if ( adID != -1 ) {
      update = true;
      ad = BannerFinder.getAd(adID);
      if ( ad == null ) {
        ad = ((com.idega.block.banner.data.AdEntityHome)com.idega.data.IDOLookup.getHomeLegacy(AdEntity.class)).createLegacy();
        update = false;
      }
    }

    int _maxHits = 0;
    int _maxImpressions = 0;
    int _fileID = -1;

    try {
      _maxHits = Integer.parseInt(maxHits);
    }
    catch (NumberFormatException e) {
      _maxHits = 0;
    }

    try {
      _maxImpressions = Integer.parseInt(maxImpressions);
    }
    catch (NumberFormatException e) {
      _maxImpressions = 0;
    }

    try {
      _fileID = Integer.parseInt(fileID);
    }
    catch (NumberFormatException ex) {
      _fileID = -1;
    }

    ad.setMaxHits(_maxHits);
    ad.setMaxImpressions(_maxImpressions);

    if ( beginDate != null && beginDate.length() > 0 ) {
      ad.setBeginDate(new IWTimestamp(beginDate).getTimestamp());
    }
    if ( endDate != null && endDate.length() > 0 ) {
      ad.setEndDate(new IWTimestamp(endDate).getTimestamp());
    }
    if ( adName != null && adName.length() > 0 ) {
      ad.setAdName(adName);
    }
    if ( URL != null && URL.length() > 0 ) {
      ad.setURL(URL);
    }

    if ( update ) {
      try {
        ad.update();
      }
      catch (SQLException ex) {
        ex.printStackTrace(System.err);
      }
    }
    else {
      ad.setUserID(userID);
      ad.setHits(0);
      ad.setImpressions(0);
      try {
        ad.insert();
      }
      catch (SQLException ex) {
        ex.printStackTrace(System.err);
      }
    }

    addToBanner(banner,ad);

    if ( _fileID != -1 ) {
      addFileToBanner(ad,_fileID);
    }

    return ad.getID();
  }

  public static void deleteAd(AdEntity ad) {
    try {
      if ( ad != null ) {
        ad.delete();
      }
    }
    catch (SQLException e) {
      e.printStackTrace(System.err);
    }
  }

  public static void removeFileFromAd(int adID,int ICFileID) {
    try {
      AdEntity ad = BannerFinder.getAd(adID);
      ICFile file = BannerFinder.getFile(ICFileID);

      if ( ad != null && file != null ) {
      	ad.removeFrom(ICFile.class,((Integer)file.getPrimaryKey()).intValue());

      }
    }
    catch (SQLException e) {
      e.printStackTrace(System.err);
    }
  }

  public static void removeAdFromBanner(int adID,int bannerID) {
    try {
      AdEntity ad = BannerFinder.getAd(adID);
      BannerEntity banner = BannerFinder.getBanner(bannerID);

      if ( ad != null && banner != null ) {
        ad.removeFrom(banner);
      }
    }
    catch (SQLException e) {
      e.printStackTrace(System.err);
    }
  }

  public static void deleteAd(int adID) {
    deleteAd(BannerFinder.getAd(adID));
  }

  public static DropdownMenu getAds(String name, BannerEntity banner, int userID) {
    DropdownMenu drp = new DropdownMenu(name);

    List list = BannerFinder.getAdsInBanner(banner,userID);
    if( list != null ) {
      for ( int a = 0; a < list.size(); a++) {
        drp.addMenuElement(((AdEntity)list.get(a)).getID(),((AdEntity)list.get(a)).getAdName());
      }
    }

    return drp;
  }

  public static void addFileToBanner(AdEntity ad, int ICFileID) {
    try {
      ICFile file = BannerFinder.getFile(ICFileID);
      if ( ad != null ) {
        if ( file != null ) {
          Collection files = ad.getRelatedFiles();
          if ( files == null || files.size() == 0 ) {
            ad.addTo(ICFile.class, ((Integer)file.getPrimaryKey()).intValue());
          }
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  public static AdEntity getCurrentAd(BannerEntity banner) {
    try {
      List adList = BannerFinder.getAds(banner);
      IWTimestamp date = new IWTimestamp();
      IWTimestamp beginDate;
      IWTimestamp endDate;
      boolean remove;
      AdEntity ad;

      if ( adList != null ) {
        for ( int a = 0; a < adList.size(); a++ ) {
          remove = false;
          beginDate = null;
          endDate = null;

          ad = (AdEntity) adList.get(a);

          if ( ad.getBeginDate() != null ) {
			beginDate = new IWTimestamp(ad.getBeginDate());
		}
          if ( ad.getEndDate() != null ) {
			endDate = new IWTimestamp(ad.getEndDate());
		}

          if ( !remove ) {
            if ( ad.getHits() > 0 && ad.getMaxHits() > 0 && ad.getHits() >= ad.getMaxHits() ) {
              remove = true;
            }
          }
          if ( !remove ) {
            if ( ad.getImpressions() > 0 && ad.getMaxImpressions() > 0 && ad.getImpressions() >= ad.getMaxImpressions() ) {
              remove = true;
            }
          }
          if ( !remove ) {
            if ( beginDate != null ) {
              if ( beginDate.isLaterThan(date) ) {
				remove = true;
			}
            }
          }
          if ( !remove ) {
            if ( endDate != null ) {
              endDate.addDays(1);
              if ( date.isLaterThan(endDate) ) {
				remove = true;
			}
            }
          }

          if ( remove ) {
			adList.remove(a);
		}
        }
      }

      if ( adList != null ) {
        if ( adList.size() > 1 ) {
          int random = (int) Math.round(Math.random() * (adList.size() - 1));
          return (AdEntity) adList.get(random);
        }
        return (AdEntity) adList.get(0);
      }

      return null;
    }
    catch (Exception e) {
      return null;
    }
  }

  public static int getImageID(AdEntity ad) {
    try {
      if ( ad != null ) {
        ICFile[] files = BannerFinder.getFilesInAd(ad);
        if ( files != null ) {
          int random = (int) Math.round(Math.random() * (files.length - 1));
          return ((Integer)files[random].getPrimaryKey()).intValue();
        }
        return -1;
      }
      return -1;
    }
    catch (Exception e) {
      return -1;
    }
  }

  public static Image getImage(int imageID) {
    try {
      return new Image(imageID);
    }
    catch (SQLException e) {
      return null;
    }
  }

  public static void updateImpressions(IWContext iwc,AdEntity ad) {
    if ( notSeenBefore(iwc,ad.getID()) ) {
      /*String cookieString = COOKIE_NAME+Integer.toString(ad.getID());
      System.out.println(cookieString);

      Cookie cookie = new Cookie(cookieString,"true");
      cookie.setMaxAge(31 * 24 * 60 * 60);
      cookie.setPath("/");
      iwc.addCookies(cookie);*/

      try {
        ad.setImpressions(ad.getImpressions()+1);
        ad.update();
      }
      catch (SQLException e) {
        e.printStackTrace(System.err);
      }
    }
  }

  public static boolean notSeenBefore(IWContext iwc, int adID) {
    /*Cookie[] cookies = iwc.getCookies();
    String cookieString = COOKIE_NAME+adID;*/

    boolean returner = true;

    /*if (cookies != null && cookies.length > 0) {
      for (int i = 0 ; i < cookies.length ; i++) {
        System.out.println("Cookie: "+cookies[i].getName()+"="+cookies[i].getValue());
        if ( cookies[i].getName().equals(cookieString) ) {
          returner = false;
          continue;
        }
      }
    }*/

    return returner;
  }

  public static String updateHits(int adID) {
    try {
      AdEntity ad = BannerFinder.getAd(adID);
      if ( ad != null ) {
        ad.setHits(ad.getHits()+1);
        ad.update();
        return ad.getURL();
      }
      return null;
    }
    catch (SQLException e) {
      return null;
    }
  }

  public static DropdownMenu getAdsDrop(String name, BannerEntity banner, int userID) {
    DropdownMenu drp = new DropdownMenu(name);

    List list = BannerFinder.getAdsInBanner(banner,userID);
    if( list != null ) {
      for ( int a = 0; a < list.size(); a++) {
        String adName = ((AdEntity)list.get(a)).getAdName();
        if ( adName == null ) {
          adName = Integer.toString(((AdEntity)list.get(a)).getID());
        }
        if ( isRelated(banner,(AdEntity)list.get(a)) ) {
          adName = "* "+adName;
        }
        drp.addMenuElement(((AdEntity)list.get(a)).getID(),adName);
      }
    }

    return drp;
  }

  public static boolean isRelated(int bannerID,int adID) {
    try {
      AdEntity ad = BannerFinder.getAd(adID);
      BannerEntity banner = BannerFinder.getBanner(bannerID);

      if ( ad != null && banner != null ) {
        return isRelated(banner,ad);
      }
      return false;
    }
    catch (Exception e) {
      return false;
    }
  }

  public static boolean isRelated(BannerEntity banner,AdEntity ad) {
    try {
      AdEntity[] adEntity = (AdEntity[]) banner.findRelated(ad);
      if ( adEntity != null && adEntity.length > 0 ) {
        return true;
      }
      return false;
    }
    catch (Exception e) {
      return false;
    }
  }
}
