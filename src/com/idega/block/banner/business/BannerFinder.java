package com.idega.block.banner.business;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.FinderException;

import com.idega.block.banner.data.AdEntity;
import com.idega.block.banner.data.BannerEntity;
import com.idega.core.component.business.ICObjectBusiness;
import com.idega.core.component.data.ICObjectInstance;
import com.idega.core.file.data.ICFile;
import com.idega.data.EntityFinder;
import com.idega.data.IDOLookupException;

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
      List L = EntityFinder.findAllByColumn(com.idega.block.banner.data.BannerEntityBMPBean.getStaticInstance(BannerEntity.class),com.idega.block.banner.data.BannerEntityBMPBean.getColumnNameAttribute(),attribute);
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
      return ((com.idega.block.banner.data.BannerEntityHome)com.idega.data.IDOLookup.getHomeLegacy(BannerEntity.class)).findByPrimaryKeyLegacy(bannerID);
    }
    catch (SQLException ex) {
      return null;
    }
  }

  public static AdEntity getAd(int adID) {
    try {
      AdEntity ad = ((com.idega.block.banner.data.AdEntityHome)com.idega.data.IDOLookup.getHomeLegacy(AdEntity.class)).findByPrimaryKeyLegacy(adID);
      return ad;
    }
    catch (SQLException e) {
      return null;
    }
  }


  public static List getAdsInBanner(BannerEntity banner,int userID) {
    try {
      List list = null;
      if ( banner != null )
        list = EntityFinder.findRelated(banner,com.idega.block.banner.data.AdEntityBMPBean.getStaticInstance(AdEntity.class));
      List userList = EntityFinder.findAllByColumn(com.idega.block.banner.data.AdEntityBMPBean.getStaticInstance(AdEntity.class),com.idega.block.banner.data.AdEntityBMPBean.getColumnNameUserID(),userID);
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
      AdEntity[] ads = (AdEntity[]) banner.findRelated(com.idega.block.banner.data.AdEntityBMPBean.getStaticInstance(AdEntity.class));
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
      return EntityFinder.findRelated(banner,com.idega.block.banner.data.AdEntityBMPBean.getStaticInstance(AdEntity.class));
    }
    catch (Exception e) {
      return null;
    }
  }

  public static ICFile getFile(int ICFileID) {
    try {
      return ((com.idega.core.file.data.ICFileHome)com.idega.data.IDOLookup.getHome(ICFile.class)).findByPrimaryKey(new Integer(ICFileID));
    }
    catch (IDOLookupException e) {
		e.printStackTrace();
	} catch (FinderException e) {
		e.printStackTrace();
	}
	return null;
  }

  public static ICFile[] getFilesInAd(AdEntity ad) {
    try {
      ICFile[] files = (ICFile[]) ad.findRelated(com.idega.core.file.data.ICFileBMPBean.getStaticInstance(ICFile.class));
      if ( files != null ) {
        return files;
      }
      return null;
    }
    catch (Exception e) {
      return null;
    }
  }

  // BEGIN COPY PASTE CRAP

  public static BannerEntity getObjectInstanceFromID(int ICObjectInstanceID){
    try {
      ICObjectBusiness icob = ICObjectBusiness.getInstance();
      ICObjectInstance ICObjInst = icob.getICObjectInstance(ICObjectInstanceID);
      return (BannerEntity)icob.getRelatedEntity(ICObjInst,BannerEntity.class);
    }
    catch (com.idega.data.IDOFinderException ex) {
      ex.printStackTrace();
      return null;
    }
  }


  public static int getRelatedEntityId(ICObjectInstance eObjectInstance){
  	try {
	    ICObjectBusiness bis = ICObjectBusiness.getInstance();
	    return bis.getRelatedEntityId(eObjectInstance,BannerEntity.class);
  	} catch (NullPointerException n) {
  		System.err.println("[BannerFinder] Exception caught...returning -1");
  		n.printStackTrace();
  		return -1;
  	}
  }

  public static int getObjectInstanceIdFromID(int bannerID){
    try {
      BannerEntity banner = ((com.idega.block.banner.data.BannerEntityHome)com.idega.data.IDOLookup.getHomeLegacy(BannerEntity.class)).findByPrimaryKeyLegacy(bannerID);
      List L = EntityFinder.findRelated(banner,((com.idega.core.component.data.ICObjectInstanceHome)com.idega.data.IDOLookup.getHomeLegacy(ICObjectInstance.class)).createLegacy());
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

}
