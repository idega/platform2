package com.idega.block.category.business;

import com.idega.data.EntityFinder;
import com.idega.block.text.business.*;
import com.idega.block.text.data.*;
import com.idega.util.LocaleUtil;
import com.idega.block.news.data.*;
import com.idega.block.text.data.*;
import com.idega.util.idegaTimestamp;
import java.util.List;
import java.util.Vector;
import java.util.Hashtable;
import java.sql.SQLException;
import java.util.Locale;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.data.ICObjectInstance;
import com.idega.core.data.ICFile;
import com.idega.core.data.ICCategory;
import com.idega.core.data.ICBusiness;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class CategoryFinder {

  public static ICCategory getCategory(int iCategoryId){
    if( iCategoryId > 0){
      try {
        return new ICCategory(iCategoryId );
      }
      catch (SQLException ex) {

      }
    }
    return null;
  }

  public static List listOfCategories(String type){
    try {
      return EntityFinder.findAllByColumn(new ICCategory(),ICCategory.getColumnType(),type);
    }
    catch (SQLException ex) {
      return null;
    }
  }

  public static List listOfValidCategories(){
    try {
      return EntityFinder.findAllByColumn(new ICCategory(),ICCategory.getColumnValid(),"Y");
    }
    catch (SQLException ex) {
      return null;
    }
  }

  public static List listOfValidCategories(String type){
    try {
      return EntityFinder.findAllByColumn(new ICCategory(),ICCategory.getColumnValid(),"Y",ICCategory.getColumnType(),type);
    }
    catch (SQLException ex) {
      return null;
    }
  }

  public static List listOfInValidCategories(){
    try {
      return EntityFinder.findAllByColumn(new ICCategory(),ICCategory.getColumnValid(),"N");
    }
    catch (SQLException ex) {
      return null;
    }
  }

  public static List listOfInValidCategories(String type){
    try {
      return EntityFinder.findAllByColumn(new ICCategory(),ICCategory.getColumnValid(),"N",ICCategory.getColumnType(),type);
    }
    catch (SQLException ex) {
      return null;
    }
  }



  public static int getObjectInstanceIdFromCategoryId(int iCategoryId){
    try {
      ICCategory nw = new ICCategory(iCategoryId);
      List L = EntityFinder.findRelated( nw,new ICObjectInstance());
      if(L!= null){
        return ((ICObjectInstance) L.get(0)).getID();
      }
      else
        return -1;
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      return -2;
    }
  }

  public static int getObjectInstanceCategoryId(int iObjectInstanceId,boolean CreateNew,String type){
    int id = -1;
    try {
      ICObjectInstance obj = new ICObjectInstance(iObjectInstanceId);
      id = getObjectInstanceCategoryId(obj);
      if(id <= 0 && CreateNew ){
        id = CategoryBusiness.createCategory(iObjectInstanceId ,type);
      }
    }
    catch (Exception ex) {

    }
    return id;
  }

  public static int getObjectInstanceCategoryId(int iObjectInstanceId){
    try {
      ICObjectInstance obj = new ICObjectInstance(iObjectInstanceId);
      return getObjectInstanceCategoryId(obj);
    }
    catch (Exception ex) {

    }
    return -1;
  }

  public static int getObjectInstanceCategoryId(ICObjectInstance eObjectInstance){
    try {
      List L = EntityFinder.findRelated(eObjectInstance ,new ICCategory());
      if(L!= null){
        return ((ICCategory) L.get(0)).getID();
      }
      else
        return -1;
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      return -2;
    }
  }

  public static List listOfCategoryForObjectInstanceId(int instanceid){
    try {
      ICObjectInstance obj = new ICObjectInstance(instanceid );
      return listOfCategoryForObjectInstanceId(obj);
    }
    catch (SQLException ex) {
      return null;
    }
  }

  public static List listOfCategoryForObjectInstanceId( ICObjectInstance obj){
    try {
      List L = EntityFinder.findRelated(obj,new ICCategory());
      return L;
    }
    catch (SQLException ex) {
      return null;
    }
  }
}