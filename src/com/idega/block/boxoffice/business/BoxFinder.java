package com.idega.block.boxoffice.business;

import java.sql.SQLException;
import java.util.List;

import com.idega.block.boxoffice.data.BoxCategory;
import com.idega.block.boxoffice.data.BoxEntity;
import com.idega.block.boxoffice.data.BoxLink;
import com.idega.builder.data.IBPage;
import com.idega.core.business.ICObjectBusiness;
import com.idega.core.data.ICFile;
import com.idega.core.data.ICObjectInstance;
import com.idega.data.EntityFinder;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class BoxFinder {

  public static IBPage getPage(int pageID) {
    try {
      return ((com.idega.builder.data.IBPageHome)com.idega.data.IDOLookup.getHomeLegacy(IBPage.class)).findByPrimaryKeyLegacy(pageID);
    }
    catch (SQLException e) {
      return ((com.idega.builder.data.IBPageHome)com.idega.data.IDOLookup.getHomeLegacy(IBPage.class)).createLegacy();
    }
  }

  public static ICFile getFile(int fileID) {
    try {
      return ((com.idega.core.data.ICFileHome)com.idega.data.IDOLookup.getHomeLegacy(ICFile.class)).findByPrimaryKeyLegacy(fileID);
    }
    catch (SQLException e) {
      return ((com.idega.core.data.ICFileHome)com.idega.data.IDOLookup.getHomeLegacy(ICFile.class)).createLegacy();
    }
  }

  public static BoxEntity getBox(String attribute){
    try {
      List L = EntityFinder.findAllByColumn(com.idega.block.boxoffice.data.BoxEntityBMPBean.getStaticInstance(BoxEntity.class),com.idega.block.boxoffice.data.BoxEntityBMPBean.getColumnNameAttribute(),attribute);
      if(L!= null) {
        return (BoxEntity) L.get(0);
      }
      return null;
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  public static BoxEntity getBox(int boxID){
    try {
      return ((com.idega.block.boxoffice.data.BoxEntityHome)com.idega.data.IDOLookup.getHomeLegacy(BoxEntity.class)).findByPrimaryKeyLegacy(boxID);
    }
    catch (SQLException ex) {
      return null;
    }
  }

  public static BoxCategory getCategory(int boxCategoryID) {
    try {
      return ((com.idega.block.boxoffice.data.BoxCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(BoxCategory.class)).findByPrimaryKeyLegacy(boxCategoryID);
    }
    catch (SQLException e) {
      return null;
    }
  }

  public static BoxLink getLink(int boxLinkID) {
    try {
      BoxLink link = ((com.idega.block.boxoffice.data.BoxLinkHome)com.idega.data.IDOLookup.getHomeLegacy(BoxLink.class)).findByPrimaryKeyLegacy(boxLinkID);
      return link;
    }
    catch (SQLException e) {
      return null;
    }
  }

  public static List getCategoriesInBox(BoxEntity box,int userID) {
    try {
      List list = null;
      if ( box != null )
        list = EntityFinder.findRelated(box,com.idega.block.boxoffice.data.BoxCategoryBMPBean.getStaticInstance(BoxCategory.class));
      List userList = EntityFinder.findAllByColumn(com.idega.block.boxoffice.data.BoxCategoryBMPBean.getStaticInstance(BoxCategory.class),com.idega.block.boxoffice.data.BoxCategoryBMPBean.getColumnNameUserID(),userID);
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

  public static List getCategoriesNotInBox(int boxID) {
    try {
      BoxEntity box = BoxFinder.getBox(boxID);
      if ( box != null ) {
      	EntityFinder.debug = true;
        List list = EntityFinder.findNonRelated(box,com.idega.block.boxoffice.data.BoxCategoryBMPBean.getStaticInstance(BoxCategory.class));
        return list;
      }else {
	      return null;
      }
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
      return null;
    }
  }

  public static List getCategoriesInBox(int boxID) {
    try {
      BoxEntity box = BoxFinder.getBox(boxID);
      if ( box != null )
        return EntityFinder.findRelated(box,com.idega.block.boxoffice.data.BoxCategoryBMPBean.getStaticInstance(BoxCategory.class));
      return null;
    }
    catch (Exception e) {
      return null;
    }
  }

  public static BoxCategory[] getCategoriesInBox(BoxEntity box) {
    try {
      BoxCategory[] categories = (BoxCategory[]) box.findRelated(com.idega.block.boxoffice.data.BoxCategoryBMPBean.getStaticInstance(BoxCategory.class));
      if ( categories != null ) {
        return categories;
      }
      return null;
    }
    catch (Exception e) {
      return null;
    }
  }

  public static BoxLink[] getLinksInBox(BoxEntity box,BoxCategory boxCategory) {
    try {
      BoxLink[] links = (BoxLink[]) com.idega.block.boxoffice.data.BoxLinkBMPBean.getStaticInstance(BoxLink.class).findAllByColumnOrdered(com.idega.block.boxoffice.data.BoxEntityBMPBean.getColumnNameBoxID(),Integer.toString(box.getID()),com.idega.block.boxoffice.data.BoxCategoryBMPBean.getColumnNameBoxCategoryID(),Integer.toString(boxCategory.getID()),com.idega.block.boxoffice.data.BoxLinkBMPBean.getColumnNameCreationDate()+" desc","=","=");
      if ( links != null ) {
        return links;
      }
      return null;
    }
    catch (Exception e) {
      return null;
    }
  }

  public static BoxLink[] getLinksInCategory(BoxCategory boxCategory) {
    try {
      BoxLink[] links = (BoxLink[]) com.idega.block.boxoffice.data.BoxLinkBMPBean.getStaticInstance(BoxLink.class).findAllByColumnOrdered(com.idega.block.boxoffice.data.BoxCategoryBMPBean.getColumnNameBoxCategoryID(),Integer.toString(boxCategory.getID()),com.idega.block.boxoffice.data.BoxLinkBMPBean.getColumnNameCreationDate()+" desc","=");
      if ( links != null ) {
        return links;
      }
      return null;
    }
    catch (Exception e) {
      return null;
    }
  }

  // BEGIN COPY PASTE CRAP

  /**@todo make some sence into this crap**/
  public static BoxEntity getObjectInstanceFromID(int ICObjectInstanceID){
    try {
      ICObjectBusiness icob = ICObjectBusiness.getInstance();
      ICObjectInstance ICObjInst = icob.getICObjectInstance(ICObjectInstanceID);
      return (BoxEntity)icob.getRelatedEntity(ICObjInst,BoxEntity.class);
    }
    catch (com.idega.data.IDOFinderException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  public static int getRelatedEntityId(ICObjectInstance eObjectInstance){
    ICObjectBusiness bis = ICObjectBusiness.getInstance();
    return bis.getRelatedEntityId(eObjectInstance,BoxEntity.class);
  }

  public static int getObjectInstanceIdFromID(int boxID){
    try {
      BoxEntity box = ((com.idega.block.boxoffice.data.BoxEntityHome)com.idega.data.IDOLookup.getHomeLegacy(BoxEntity.class)).findByPrimaryKeyLegacy(boxID);
      List L = EntityFinder.findRelated(box,((com.idega.core.data.ICObjectInstanceHome)com.idega.data.IDOLookup.getHomeLegacy(ICObjectInstance.class)).createLegacy());
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
