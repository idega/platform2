package com.idega.block.boxoffice.business;

import com.idega.data.EntityFinder;
import com.idega.block.boxoffice.data.*;
import java.sql.SQLException;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.data.ICObjectInstance;
import java.util.List;
import com.idega.util.idegaTimestamp;
import com.idega.block.text.business.TextFinder;
import com.idega.block.text.data.LocalizedText;
import com.idega.data.GenericEntity;
import com.idega.presentation.ui.DropdownMenu;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class BoxBusiness {

public static final String PARAMETER_BOX_VIEW = "box_view";
public static final String PARAMETER_BOX_ID = "box_id";
public static final String PARAMETER_CATEGORY_ID = "category_id";
public static final String PARAMETER_CATEGORY_DROPDOWN_ID = "category_dropdown_id";
public static final String PARAMETER_CATEGORY_NAME = "category_name";
public static final String PARAMETER_CLOSE = "close";
public static final String PARAMETER_DELETE = "delete";
public static final String PARAMETER_DETACH = "detach";
public static final String PARAMETER_FALSE = "false";
public static final String PARAMETER_FILE_ID = "file_id";
public static final String PARAMETER_LINK_ID = "link_id";
public static final String PARAMETER_LINK_NAME = "link_name";
public static final String PARAMETER_LINK_URL = "link_url";
public static final String PARAMETER_LOCALE_DROP = "locale_drop";
public static final String PARAMETER_LOCALE_ID = "locale_id";
public static final String PARAMETER_MODE = "mode";
public static final String PARAMETER_NEW_ATTRIBUTE = "new_attribute";
public static final String PARAMETER_NEW_OBJECT_INSTANCE = "new_obj_inst";
public static final String PARAMETER_PAGE_ID = "page_id";
public static final String PARAMETER_SAVE = "save";
public static final String PARAMETER_TARGET = "target";
public static final String PARAMETER_TRUE = "true";
public static final String PARAMETER_TYPE = "type";

public static final int LINK = 1;
public static final int FILE = 2;
public static final int PAGE = 3;

  public static void saveBox(int boxID,int InstanceId,String attribute){
    try {
      boolean update = false;

      BoxEntity box = new BoxEntity();
      if ( boxID != -1 ) {
        update = true;
        box = BoxFinder.getBox(boxID);
        if ( box == null ) {
          box = new BoxEntity();
          update = false;
        }
      }

      if(attribute != null){
        BoxEntity boxAttribute = BoxFinder.getBox(attribute);
        if ( boxAttribute != null ) {
          box = boxAttribute;
          update = true;
        }
        box.setAttribute(attribute);
      }

      if ( update ) {
        try {
          box.update();
        }
        catch (SQLException e) {
          e.printStackTrace(System.err);
        }
      }
      else {
        box.insert();
        if(InstanceId > 0){
          ICObjectInstance objIns = new ICObjectInstance(InstanceId);
          box.addTo(objIns);
        }
      }
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public static void addToBox(BoxEntity box, int boxCategoryID) {
    try {
      BoxCategory category = BoxFinder.getCategory(boxCategoryID);
      if ( category != null ) {
        BoxCategory[] categories = (BoxCategory[]) box.findRelated(category);
        if ( categories == null || categories.length == 0 ) {
          box.addTo(category);
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  public static boolean deleteBox(BoxEntity box) {
    try {
      if ( box != null ) {
        box.delete();
      }
      return true;
    }
    catch (SQLException e) {
      e.printStackTrace(System.err);
      return false;
    }
  }

  public static int saveLink(int userID,int boxID,int boxCategoryID,int linkID,String boxLinkName,int fileID,int pageID,String boxLinkURL,String target,int iLocaleID) {
    boolean update = false;
    boolean newLocText = false;
    int _linkID = -1;

    if ( linkID != -1 ) {
      update = true;
    }

    BoxLink boxLink = BoxFinder.getLink(linkID);
    if ( boxLink != null ) {
      if ( boxLinkURL != null ) {
        if ( update ) {
          try {
            boxLink.setColumnAsNull(BoxLink.getColumnNamePageID());
            boxLink.setColumnAsNull(BoxLink.getColumnNameFileID());
          }
          catch (SQLException e) {
            e.printStackTrace(System.err);
          }
        }
      }
      else if ( fileID != -1 ) {
        if ( update ) {
          try {
            boxLink.setColumnAsNull(BoxLink.getColumnNamePageID());
            boxLink.setColumnAsNull(BoxLink.getColumnNameURL());
          }
          catch (SQLException e) {
            e.printStackTrace(System.err);
          }
        }
      }
      else if ( pageID != -1 ) {
        if ( update ) {
          try {
            boxLink.setColumnAsNull(BoxLink.getColumnNameFileID());
            boxLink.setColumnAsNull(BoxLink.getColumnNameURL());
          }
          catch (SQLException e) {
            e.printStackTrace(System.err);
          }
        }
      }
    }

    BoxLink link = new BoxLink();
    if ( update ) {
      link = BoxFinder.getLink(linkID);
      if ( link == null ) {
        link = new BoxLink();
        update = false;
      }
    }

    if ( boxCategoryID != -1 ) {
      link.setBoxCategoryID(boxCategoryID);
    }
    if ( target != null ) {
      link.setTarget(target);
    }

    if ( boxLinkURL != null ) {
      link.setURL(boxLinkURL);
    }
    else if ( fileID != -1 ) {
      link.setFileID(fileID);
    }
    else if ( pageID != -1 ) {
      link.setPageID(pageID);
    }

    if ( !update ) {
      try {
        link.setCreationDate(new idegaTimestamp().getTimestampRightNow());
        link.setBoxID(boxID);
        link.setUserID(userID);
        link.insert();
        _linkID = link.getID();
      }
      catch (SQLException e) {
        e.printStackTrace(System.err);
      }
    }
    else {
      try {
        link.update();
        _linkID = link.getID();
      }
      catch (SQLException e) {
        e.printStackTrace(System.err);
      }
    }

    if ( boxCategoryID != -1 ) {
      addToBox(BoxFinder.getBox(boxID),boxCategoryID);
    }

    LocalizedText locText = TextFinder.getLocalizedText(link,iLocaleID);
    if ( locText == null ) {
      locText = new LocalizedText();
      newLocText = true;
    }

    locText.setHeadline(boxLinkName);

    if ( newLocText ) {
      locText.setLocaleId(iLocaleID);
      try {
        locText.insert();
        locText.addTo(link);
      }
      catch (SQLException e) {
        e.printStackTrace(System.err);
      }
    }
    else {
      try {
        locText.update();
      }
      catch (SQLException e) {
        e.printStackTrace(System.err);
      }
    }
    return _linkID;
  }

  public static void deleteLink(BoxLink link) {
    try {
      if ( link != null ) {
        link.removeFrom(LocalizedText.getStaticInstance(LocalizedText.class));
        link.delete();
      }
    }
    catch (SQLException e) {
      e.printStackTrace(System.err);
    }
  }

  public static void deleteLink(int linkID) {
    deleteLink(BoxFinder.getLink(linkID));
  }

  public static int saveCategory(int userID,int boxCategoryID,String categoryName,int iLocaleID) {
    boolean update = false;
    boolean newLocText = false;
    int _boxCategoryID = -1;

    if ( boxCategoryID != -1 ) {
      update = true;
    }

    BoxCategory category = new BoxCategory();
    if ( update ) {
      category = BoxFinder.getCategory(boxCategoryID);
      if ( category == null ) {
        category = new BoxCategory();
        update = false;
      }

    }

    if ( !update ) {
      try {
        category.setUserID(userID);
        category.insert();
        _boxCategoryID = category.getID();
      }
      catch (SQLException e) {
        e.printStackTrace(System.err);
      }
    }
    else {
      try {
        category.update();
        _boxCategoryID = category.getID();
      }
      catch (SQLException e) {
        e.printStackTrace(System.err);
      }
    }

    LocalizedText locText = TextFinder.getLocalizedText(category,iLocaleID);
    if ( locText == null ) {
      locText = new LocalizedText();
      newLocText = true;
    }

    locText.setHeadline(categoryName);

    if ( newLocText ) {
      locText.setLocaleId(iLocaleID);
      try {
        locText.insert();
        locText.addTo(category);
      }
      catch (SQLException e) {
        e.printStackTrace(System.err);
      }
    }
    else {
      try {
        locText.update();
      }
      catch (SQLException e) {
        e.printStackTrace(System.err);
      }
    }
    return _boxCategoryID;
  }

  public static void deleteCategory(int boxCategoryID) {
    try {
      BoxCategory category = BoxFinder.getCategory(boxCategoryID);
      if ( category != null ) {
        category.delete();
      }
    }
    catch (SQLException e) {
      e.printStackTrace(System.err);
    }
  }

  public static DropdownMenu getCategories(String name, int iLocaleId, BoxEntity box, int userID) {
    DropdownMenu drp = new DropdownMenu(name);

    List list = BoxFinder.getCategoriesInBox(box,userID);
    if( list != null ) {
      for ( int a = 0; a < list.size(); a++) {
        LocalizedText locText = TextFinder.getLocalizedText((BoxCategory)list.get(a),iLocaleId);
        String locString = "$language$";
        if ( locText != null ) {
          locString = locText.getHeadline();
        }
        drp.addMenuElement(((BoxCategory)list.get(a)).getID(),locString);
      }
    }

    return drp;
  }

  public static void detachCategory(int boxID, int boxCategoryID) {
    try {
      BoxEntity boxEntity = BoxFinder.getBox(boxID);
      BoxCategory boxCategory = BoxFinder.getCategory(boxCategoryID);

      if ( boxEntity != null && boxCategory != null ) {
        boxEntity.removeFrom(boxCategory);
        deleteLinks(boxEntity,boxCategory);
      }
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  public static void deleteLinks(BoxEntity boxEntity, BoxCategory boxCategory) {
    try {
      BoxLink[] links = BoxFinder.getLinksInBox(boxEntity,boxCategory);
      if ( links != null ) {
        for ( int a = 0; a < links.length; a++ ) {
          deleteLink(links[a]);
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  public static String getLocalizedString(GenericEntity entity, int iLocaleID) {
    String locString = null;

    if ( entity != null ) {
      LocalizedText locText = TextFinder.getLocalizedText(entity,iLocaleID);
      if ( locText != null ) {
        locString = locText.getHeadline();
      }
    }

    return locString;
  }

}