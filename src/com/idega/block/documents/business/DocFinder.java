package com.idega.block.documents.business;

import com.idega.block.documents.data.DocLink;
import com.idega.builder.data.IBPage;
import com.idega.core.data.ICFile;
import com.idega.core.data.ICInformationCategory;
import com.idega.core.data.ICInformationFolder;
import com.idega.core.data.ICObjectInstance;
import com.idega.core.presentation.InformationCategory;
import com.idega.core.presentation.InformationFolder;
import com.idega.data.EntityFinder;
import java.sql.SQLException;
import java.util.List;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @version 1.0
 */

public class DocFinder {

  public static IBPage getPage(int pageID) {
    try {
      return new IBPage(pageID);
    }
    catch (SQLException e) {
      return new IBPage();
    }
  }

  public static ICFile getFile(int fileID) {
    try {
      return new ICFile(fileID);
    }
    catch (SQLException e) {
      return new ICFile();
    }
  }

  /**
   * @todo: reimplement
   */
  public static ICInformationFolder getFolder(String attribute){
/*    try {
      List L = null; // EntityFinder.findAllByColumn(ICInformationFolder.getStaticInstance(ICInformationFolder.class),ICInformationFolder.getColumnNameAttribute(),attribute);
      if(L!= null) {
        return (ICInformationFolder) L.get(0);
      }
      return null;
    }
    catch (SQLException ex) {
      ex.printStackTrace();*/
      return null;
   // }
  }

  public static ICInformationFolder getFolder(int folderID){
    try {
      return new ICInformationFolder(folderID);
    }
    catch (SQLException ex) {
      return null;
    }
  }

  public static ICInformationCategory getCategory(int infoCatID) {
    try {
      return new ICInformationCategory(infoCatID);
    }
    catch (SQLException e) {
      return null;
    }
  }

  public static DocLink getLink(int boxLinkID) {
    try {
      DocLink link = new DocLink(boxLinkID);
      return link;
    }
    catch (SQLException e) {
      return null;
    }
  }
/*
  public static int getObjectInstanceID(ICObjectInstance eObjectInstance){
    try {
      List L = EntityFinder.findRelated(eObjectInstance,new ICInformationFolder());
      if(L!= null){
        return ((ICInformationFolder) L.get(0)).getID();
      }
      else
        return -1;
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      return -2;

    }
  }
*/
  public static int getObjectInstanceIdFromID(int folderID){
    try {
      ICInformationFolder box = new ICInformationFolder(folderID);
      List L = EntityFinder.findRelated(box,new ICObjectInstance());
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

  public static ICInformationFolder getObjectInstanceFromID(int ICObjectInstanceID){
    try {
      ICObjectInstance ICObjInst = new ICObjectInstance(ICObjectInstanceID);
      List L = EntityFinder.findRelated(ICObjInst,ICInformationFolder.getStaticInstance(ICInformationFolder.class));
      if(L!= null){
        return (ICInformationFolder) L.get(0);
      }
      else
        return null;
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  /**
   * @todo: reimplement
   */
//  public static List getCategoriesInDoc(ICInformationFolder box,int userID) {
//    try {
//      List list = null;
//      if ( box != null )
//        list = EntityFinder.findRelated(box,ICInformationCategory.getStaticInstance(ICInformationCategory.class));
//      List userList = null; // EntityFinder.findAllByColumn(ICInformationCategory.getStaticInstance(ICInformationCategory.class),ICInformationCategory.getColumnNameUserID(),userID);
//      if ( userList != null ) {
//        if ( list != null ) {
//          for ( int a = 0; a < list.size(); a++ ) {
//            if ( !userList.contains(list.get(a)) )
//              userList.add(list.get(a));
//          }
//        }
//        return userList;
//      }
//      else {
//        if ( list != null ) {
//          return list;
//        }
//      }
//      return null;
//    }
//    catch (Exception e) {
//      e.printStackTrace(System.err);
//      return null;
//    }
//  }
//
//  public static List getCategoriesNotInUse(int folderID) {
//    try {
//      ICInformationFolder folder = DocFinder.getFolder(folderID);
//      if ( box != null )
//        return EntityFinder.findNonRelated(folder,ICInformationCategory.getStaticInstance(ICInformationCategory.class));
//      return null;
//    }
//    catch (Exception e) {
//      e.printStackTrace(System.err);
//      return null;
//    }
//  }
//
//  public static List getCategoriesInUse(int folderID) {
//    try {
//      ICInformationFolder box = DocFinder.getFolder(folderID);
//      if ( box != null )
//        return EntityFinder.findRelated(box,ICInformationCategory.getStaticInstance(ICInformationCategory.class));
//      return null;
//    }
//    catch (Exception e) {
//      return null;
//    }
//  }
//
//  public static ICInformationCategory[] getCategoriesInUse(ICInformationFolder box) {
//    try {
//      ICInformationCategory[] categories = (ICInformationCategory[]) box.findRelated(ICInformationCategory.getStaticInstance(ICInformationCategory.class));
//      if ( categories != null ) {
//        return categories;
//      }
//      return null;
//    }
//    catch (Exception e) {
//      return null;
//    }
//  }

  /**
   * @todo: reimplement
   */
  public static DocLink[] getLinksInFolderCategory(InformationFolder folder,InformationCategory infoCat) {
    try {
      DocLink staticLink = (DocLink)DocLink.getStaticInstance(DocLink.class);
      DocLink[] links = (DocLink[]) staticLink.findAllByColumnOrdered(DocLink.getColumnNameFolderID(),Integer.toString(folder.getID()),DocLink.getColumnNameCatID(),Integer.toString(infoCat.getID()),DocLink.getColumnNameCreationDate()+" desc","=","=");
      if ( links != null ) {
        return links;
      }
      return null;
    }
    catch (Exception e) {
      return null;
    }
  }

  /**
   * @deprecated
   */
  public static DocLink[] getLinksInCategory(InformationCategory infoCat) {
    try {
      DocLink[] links = null; // (DocLink[]) DocLink.getStaticInstance(DocLink.class).findAllByColumnOrdered(infoCat.getColumnNameDocCategoryID(),Integer.toString(infoCat.getID()),DocLink.getColumnNameCreationDate()+" desc","=");
      if ( links != null ) {
        return links;
      }
      return null;
    }
    catch (Exception e) {
      return null;
    }
  }
}