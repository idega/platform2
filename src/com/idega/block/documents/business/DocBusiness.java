package com.idega.block.documents.business;

import com.idega.block.documents.data.DocLink;
import com.idega.block.text.business.TextFinder;
import com.idega.block.text.data.LocalizedText;
import com.idega.core.data.ICInformationCategory;
import com.idega.core.data.ICInformationFolder;
import com.idega.core.data.ICObjectInstance;
import com.idega.core.user.data.User;
import com.idega.data.GenericEntity;
import com.idega.util.idegaTimestamp;
import java.sql.SQLException;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class DocBusiness {

public static final String PARAMETER_BOX_VIEW = "doc_view";
public static final String PARAMETER_FOLDER_ID = "doc_id";
public static final String PARAMETER_CATEGORY_ID = "doc_cat_id";
public static final String PARAMETER_OBJECT_INSTANCE_ID = "doc_inst_id";
public static final String PARAMETER_OBJECT_ID = "doc_obj_id";
public static final String PARAMETER_CONTENT_LOCALE_IDENTIFIER = "doc_content_locale_id";
public static final String PARAMETER_CATEGORY_DROPDOWN_ID = "doc_cat_ddown_id";
public static final String PARAMETER_CATEGORY_NAME = "doc_cat_name";
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

public static final String CATEGORY_SELECTION = "related_groups";

public static final int LINK = 1;
public static final int FILE = 2;
public static final int PAGE = 3;

  /**
   * @todo finde out what attribute does
   */
  public static void saveDoc(int boxID,int InstanceId,String attribute){
    try {
      boolean update = false;

      ICInformationFolder folder = new ICInformationFolder();
      if ( boxID != -1 ) {
        update = true;
        folder = DocFinder.getFolder(boxID);
        if ( folder == null ) {
          folder = new ICInformationFolder();
          update = false;
        }
      }

      if(attribute != null){
        ICInformationFolder boxAttribute = DocFinder.getFolder(attribute);
        if ( boxAttribute != null ) {
          folder = boxAttribute;
          update = true;
        }
        //folder.setAttribute(attribute);
      }

      if ( update ) {
        try {
          folder.update();
        }
        catch (SQLException e) {
          e.printStackTrace(System.err);
        }
      }
      else {
        folder.insert();
        if(InstanceId > 0){
          ICObjectInstance objIns = new ICObjectInstance(InstanceId);
          folder.addTo(objIns);
        }
      }
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public static void addToDoc(ICInformationFolder folder, int boxCategoryID) {
    try {
      ICInformationCategory category = DocFinder.getCategory(boxCategoryID);
      if ( category != null ) {
        ICInformationCategory[] categories = (ICInformationCategory[]) folder.findRelated(category);
        if ( categories == null || categories.length == 0 ) {
          folder.addTo(category);
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  public static boolean deleteDoc(ICInformationFolder folder) {
    try {
      if ( folder != null ) {
        folder.delete();
      }
      return true;
    }
    catch (SQLException e) {
      e.printStackTrace(System.err);
      return false;
    }
  }

  public static boolean deleteDoc(int iObjectInstanceId) {
    ICInformationFolder folder = DocFinder.getObjectInstanceFromID(iObjectInstanceId);
    if(folder !=null){
      return deleteDoc(folder,iObjectInstanceId);
    }
    return false;
  }

  public static boolean deleteDoc(int iDocId,int iObjectInstanceId) {
          try{
      ICInformationFolder folder= new ICInformationFolder(iDocId);
                  if(folder !=null){
                          return deleteDoc(folder,iObjectInstanceId);
                  }
          }
          catch(SQLException ex){

          }
          return false;

  }

  public static boolean deleteDoc(ICInformationFolder folder,int iObjectInstanceId) {
    try {
      if (folder !=null ) {

				disconnectDoc(folder,iObjectInstanceId);
        folder.delete();
      }
      return true;
    }
    catch (SQLException e) {
      e.printStackTrace(System.err);
      return false;
    }
  }

  public static boolean disconnectDoc(int instanceid){
		ICInformationFolder folder = DocFinder.getObjectInstanceFromID(instanceid);
    if(folder!= null){
			return disconnectDoc(folder,instanceid);

    }
    return false;

  }

  public static boolean disconnectDoc(ICInformationFolder folder,int iObjectInstanceId){
    try {
      if(iObjectInstanceId > 0  ){
        ICObjectInstance obj = new ICObjectInstance(iObjectInstanceId);
        folder.removeFrom(obj);
      }
      return true;
    }
    catch (SQLException ex) {

    }
    return false;
  }

  /**
   * @todo check performance
   *
   *
   * @param userID
   * @param catId
   * @param folderId
   * @param linkID
   * @param boxLinkName
   * @param fileID
   * @param pageID
   * @param boxLinkURL
   * @param target
   * @param iLocaleID
   * @return
   */
  public static int saveLink(int userID,int catId,int folderId,int linkID,String boxLinkName,int fileID,int pageID,String boxLinkURL,String target,int iLocaleID) {
    System.out.println("saveLink(int userID,int catId,int folderId,int linkID,String boxLinkName,int fileID,int pageID,String boxLinkURL,String target,int iLocaleID)");
    System.out.println(userID+", "+catId+", "+folderId+", "+linkID+", "+boxLinkName+", "+fileID+", "+pageID+", "+boxLinkURL+", "+target+", "+iLocaleID);
    boolean update = false;
    boolean newLocText = false;
    int _linkID = -1;

    DocLink link = null;
    if ( linkID != -1 ) {
      update = true;
      link = DocFinder.getLink(linkID);
      if ( link != null ) {
        if ( boxLinkURL != null ) {
          if ( update ) {
            try {
              link.setColumnAsNull(DocLink.getColumnNamePageID());
              link.setColumnAsNull(DocLink.getColumnNameFileID());
            }
            catch (SQLException e) {
              e.printStackTrace(System.err);
            }
          }
        }
        else if ( fileID != -1 ) {
          if ( update ) {
            try {
              link.setColumnAsNull(DocLink.getColumnNamePageID());
              link.setColumnAsNull(DocLink.getColumnNameURL());
            }
            catch (SQLException e) {
              e.printStackTrace(System.err);
            }
          }
        }
        else if ( pageID != -1 ) {
          if ( update ) {
            try {
              link.setColumnAsNull(DocLink.getColumnNameFileID());
              link.setColumnAsNull(DocLink.getColumnNameURL());
            }
            catch (SQLException e) {
              e.printStackTrace(System.err);
            }
          }
        }
      }
    } else {
      link = new DocLink();
    }

    if ( update ) {
      link = DocFinder.getLink(linkID);
      if ( link == null ) {
        link = new DocLink();
        update = false;
      }
    }

    if(boxLinkName != null){
      link.setName(boxLinkName);
    } else {
      link.setName("Untitled");
    }

    if ( catId != -1 ) {
      link.setCategoryID(catId);
    }

    if ( target != null ) {
      link.setTarget(target);
    }

    if ( boxLinkURL != null ) {
      link.setURL(boxLinkURL);
    }

    if ( fileID != -1 ) {
      link.setFileID(fileID);
    }

    if ( pageID != -1 ) {
      link.setPageID(pageID);
    }

    if ( !update ) {
      try {
        link.setCreationDate(new idegaTimestamp().getTimestampRightNow());
        if ( folderId != -1 ) {
          link.setFolderID(folderId);
        }
        if(userID != -1){
          link.setUser(new User(userID));
        }
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
/*
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

    */

    return _linkID;
  }

  public static void deleteLink(DocLink link) {
    try {
      if ( link != null ) {
        /**
         * @todo: mark as deleted, not delete it
         */
        link.delete();
      }
    }
    catch (SQLException e) {
      e.printStackTrace(System.err);
    }
  }

  public static void deleteLink(int linkID) {
    deleteLink(DocFinder.getLink(linkID));
  }

  public static int saveCategory(int userID,int boxCategoryID,String categoryName,int iLocaleID) {
    boolean update = false;
    boolean newLocText = false;
    int _boxCategoryID = -1;

    if ( boxCategoryID != -1 ) {
      update = true;
    }

    ICInformationCategory category = new ICInformationCategory();
    if ( update ) {
      category = DocFinder.getCategory(boxCategoryID);
      if ( category == null ) {
        category = new ICInformationCategory();
        update = false;
      }

    }

    if ( !update ) {
      try {
        /**
         * @todo check if ok to comment out category.setUser(new User(userID));
         */
        //category.setUser(new User(userID));
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
		locText.setBody("");
		locText.setCreated(com.idega.util.idegaTimestamp.getTimestampRightNow());

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
      ICInformationCategory category = DocFinder.getCategory(boxCategoryID);
      if ( category != null ) {
        deleteLinks(category);
        category.delete();
      }
    }
    catch (SQLException e) {
      e.printStackTrace(System.err);
    }
  }
/*
  public static DropdownMenu getCategories(String name, int iLocaleId, ICInformationFolder folder, int userID) {
    DropdownMenu drp = new DropdownMenu(name);

    List list = DocFinder.getCategoriesInDoc(folder,userID);
    if( list != null ) {
      for ( int a = 0; a < list.size(); a++) {
        LocalizedText locText = TextFinder.getLocalizedText((ICInformationCategory)list.get(a),iLocaleId);
        String locString = "$language$";
        if ( locText != null ) {
          locString = locText.getHeadline();
        }
        drp.addMenuElement(((ICInformationCategory)list.get(a)).getID(),locString);
      }
    }

    return drp;
  }
*/
  public static void detachCategory(int boxID, int boxCategoryID) {
    try {
      ICInformationFolder boxEntity = DocFinder.getFolder(boxID);
      ICInformationCategory boxCategory = DocFinder.getCategory(boxCategoryID);

      if ( boxEntity != null && boxCategory != null ) {
        boxEntity.removeFrom(boxCategory);
      }
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  public static void deleteLinks(ICInformationCategory boxCategory) {
    try {
      DocLink[] links = DocFinder.getLinksInCategory(boxCategory);
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