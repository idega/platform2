package com.idega.block.banner.business;

import com.idega.data.EntityFinder;
import com.idega.block.banner.data.*;
import java.sql.SQLException;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.data.ICObjectInstance;
import com.idega.core.data.ICFile;
import java.util.List;
import java.util.Iterator;
import java.util.Random;
import com.idega.util.idegaTimestamp;
import com.idega.data.GenericEntity;
import com.idega.jmodule.object.interfaceobject.DropdownMenu;
import com.idega.jmodule.object.Image;

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
public static final String PARAMETER_CLICKED = "clicked";
public static final String PARAMETER_CLOSE = "close";
public static final String PARAMETER_DELETE = "delete";
public static final String PARAMETER_FALSE = "false";
public static final String PARAMETER_FILE_ID = "file_id";
public static final String PARAMETER_LINK_URL = "link_url";
public static final String PARAMETER_MAX_HITS = "max_hits";
public static final String PARAMETER_MAX_IMPRESSIONS = "max_impressions";
public static final String PARAMETER_MODE = "mode";
public static final String PARAMETER_NEW_ATTRIBUTE = "new_attribute";
public static final String PARAMETER_NEW_OBJECT_INSTANCE = "new_obj_inst";
public static final String PARAMETER_SAVE = "save";
public static final String PARAMETER_TRUE = "true";
public static final String PARAMETER_URL = "url";

  public static void saveBanner(int bannerID,int InstanceId,String attribute){
    try {
      boolean update = false;

      BannerEntity banner = new BannerEntity();
      if ( bannerID != -1 ) {
        update = true;
        banner = BannerFinder.getBanner(bannerID);
        if ( banner == null ) {
          banner = new BannerEntity();
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
          ICObjectInstance objIns = new ICObjectInstance(InstanceId);
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
      if ( ad != null ) {
        AdEntity[] ads = BannerFinder.getAdsInBanner(banner);
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

  public static void saveCategory(int userID,int adID,String adName,int hits,int maxHits,int impressions,int maxImpressions,idegaTimestamp beginDate,idegaTimestamp endDate,String URL) {
    boolean update = false;

    if ( adID != -1 ) {
      update = true;
    }
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
        ICFile[] files = BannerFinder.getFilesInAd(ad);
        if ( files == null || files.length == 0 ) {
          ad.addTo(file);
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
      idegaTimestamp date = new idegaTimestamp();
      idegaTimestamp beginDate;
      idegaTimestamp endDate;
      boolean remove;

      if ( adList != null ) {
        for ( int a = 0; a < adList.size(); a++ ) {
          remove = false;
          beginDate = null;
          endDate = null;

          AdEntity ad = (AdEntity) adList.get(a);

          if ( ad.getBeginDate() != null )
            beginDate = new idegaTimestamp(ad.getBeginDate());
          if ( ad.getEndDate() != null )
            endDate = new idegaTimestamp(ad.getEndDate());

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
              if ( beginDate.isLaterThan(date) )
                remove = true;
            }
          }
          if ( !remove ) {
            if ( endDate != null ) {
              if ( date.isLaterThan(endDate) )
                remove = true;
            }
          }

          if ( remove )
            adList.remove(a);
        }
      }

      if ( adList != null ) {
        if ( adList.size() > 1 ) {
          Random number = new Random();
          int random = number.nextInt(adList.size());
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
          Random number = new Random();
          int random = number.nextInt(files.length);
          return files[random].getID();
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

  public static void updateImpressions(AdEntity ad) {
    try {
      ad.setImpressions(ad.getImpressions()+1);
      ad.update();
    }
    catch (SQLException e) {
      e.printStackTrace(System.err);
    }
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
        String adName = ((AdEntity)list.get(a)).getURL();
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