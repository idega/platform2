package com.idega.block.banner.business;

import com.idega.data.EntityFinder;
import com.idega.block.banner.data.*;
import com.idega.core.data.ICFile;
import java.sql.SQLException;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.data.ICObjectInstance;
import java.util.List;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class BannerFinder {

  public static BannerEntity getBanner(String attribute){
    try {
      List L = EntityFinder.findAllByColumn(BannerEntity.getStaticInstance(BannerEntity.class),BannerEntity.getColumnNameAttribute(),attribute);
      if(L!= null) {
        return (BannerEntity) L.get(0);
      }
      return null;
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  public static BannerEntity getBanner(int bannerID){
    try {
      return new BannerEntity(bannerID);
    }
    catch (SQLException ex) {
      return null;
    }
  }

  public static AdEntity getAd(int adID) {
    try {
      AdEntity ad = new AdEntity(adID);
      return ad;
    }
    catch (SQLException e) {
      return null;
    }
  }

  public static int getObjectInstanceID(ICObjectInstance eObjectInstance){
    try {
      List L = EntityFinder.findRelated(eObjectInstance,new BannerEntity());
      if(L!= null){
        return ((BannerEntity) L.get(0)).getID();
      }
      else
        return -1;
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      return -2;

    }
  }

  public static int getObjectInstanceIdFromID(int bannerID){
    try {
      BannerEntity banner = new BannerEntity(bannerID);
      List L = EntityFinder.findRelated(banner,new ICObjectInstance());
      if(L!= null){
        return ((ICObjectInstance) L.get(0)).getID();
      }
      else
        return -1;
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      return -1;

    }
  }

  public static BannerEntity getObjectInstanceFromID(int ICObjectInstanceID){
    try {
      ICObjectInstance ICObjInst = new ICObjectInstance(ICObjectInstanceID);
      List L = EntityFinder.findRelated(ICObjInst,BannerEntity.getStaticInstance(BannerEntity.class));
      if(L!= null){
        return (BannerEntity) L.get(0);
      }
      else
        return null;
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  public static List getAdsInBanner(BannerEntity banner,int userID) {
    try {
      List list = null;
      if ( banner != null )
        list = EntityFinder.findRelated(banner,AdEntity.getStaticInstance(AdEntity.class));
      List userList = EntityFinder.findAllByColumn(AdEntity.getStaticInstance(AdEntity.class),AdEntity.getColumnNameUserID(),userID);
      if ( userList != null ) {
        if ( list != null ) {
          for ( int a = 0; a < list.size(); a++ ) {
            if ( !userList.contains(list.get(a)) )
              userList.add(list.get(a));
          }
        }
        return userList;
      }
      else {
        if ( list != null ) {
          return list;
        }
      }
      return null;
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
      return null;
    }
  }

  public static AdEntity[] getAdsInBanner(BannerEntity banner) {
    try {
      AdEntity[] ads = (AdEntity[]) banner.findRelated(AdEntity.getStaticInstance(AdEntity.class));
      if ( ads != null ) {
        return ads;
      }
      return null;
    }
    catch (Exception e) {
      return null;
    }
  }

  public static List getAds(BannerEntity banner) {
    try {
      return EntityFinder.findRelated(banner,AdEntity.getStaticInstance(AdEntity.class));
    }
    catch (Exception e) {
      return null;
    }
  }

  public static ICFile getFile(int ICFileID) {
    try {
      return new ICFile(ICFileID);
    }
    catch (SQLException e) {
      return null;
    }
  }

  public static ICFile[] getFilesInAd(AdEntity ad) {
    try {
      ICFile[] files = (ICFile[]) ad.findRelated(ICFile.getStaticInstance(ICFile.class));
      if ( files != null ) {
        return files;
      }
      return null;
    }
    catch (Exception e) {
      return null;
    }
  }
}