package com.idega.block.category.business;

import com.idega.data.EntityFinder;

import com.idega.util.idegaTimestamp;
import java.util.List;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Map;
import java.util.HashSet;
import java.util.Collection;
import java.sql.SQLException;
import java.util.Locale;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.data.ICObjectInstance;
import com.idega.core.data.ICFile;
import com.idega.core.data.ICCategory;
import com.idega.core.data.ICBusiness;
import com.idega.data.GenericEntity;
import com.idega.data.CategoryEntity;
import com.idega.data.EntityControl;
import com.idega.data.IDOFinderException;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class CategoryFinder {

  private static CategoryFinder categoryFinder;

  public static ICCategory getCategory(int iCategoryId){
    if( iCategoryId > 0){
        return (ICCategory) ICCategory.getEntityInstance(ICCategory.class,iCategoryId);
    }
    return null;
  }

  public static List listOfCategories(String type){
    try {
      return EntityFinder.findAllByColumn(ICCategory.getStaticInstance(ICCategory.class),ICCategory.getColumnType(),type);
    }
    catch (SQLException ex) {
      return null;
    }
  }

  public static List listOfValidCategories(){
    try {
      return EntityFinder.findAllByColumn(ICCategory.getStaticInstance(ICCategory.class),ICCategory.getColumnValid(),"Y");
    }
    catch (SQLException ex) {
      return null;
    }
  }

  public static List listOfValidCategories(String type){
    try {
      return EntityFinder.findAllByColumn(ICCategory.getStaticInstance(ICCategory.class),ICCategory.getColumnValid(),"Y",ICCategory.getColumnType(),type);
    }
    catch (SQLException ex) {
      return null;
    }
  }

  public static List listOfInValidCategories(){
    try {
      return EntityFinder.findAllByColumn(ICCategory.getStaticInstance(ICCategory.class),ICCategory.getColumnValid(),"N");
    }
    catch (SQLException ex) {
      return null;
    }
  }

  public static List listOfInValidCategories(String type){
    try {
      return EntityFinder.findAllByColumn(ICCategory.getStaticInstance(ICCategory.class),ICCategory.getColumnValid(),"N",ICCategory.getColumnType(),type);
    }
    catch (SQLException ex) {
      return null;
    }
  }



  public static int getObjectInstanceIdFromCategoryId(int iCategoryId){
    try {
      ICCategory nw = (ICCategory) getCategory(iCategoryId);
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
      ex.printStackTrace();
    }
    return id;
  }

  public static int[] getObjectInstanceCategoryIds(int iObjectInstanceId,boolean CreateNew,String type){
    int[] ids = new int[0];
    try {
      ids = getObjectInstanceCategoryIds(iObjectInstanceId);
      if(ids.length == 0 && CreateNew ){
        ids = new int[1];
        ids[0] = CategoryBusiness.createCategory(iObjectInstanceId ,type);
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return ids;
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
      List L = EntityFinder.findRelated(eObjectInstance ,(GenericEntity)ICCategory.getStaticInstance(ICCategory.class));
      if(L!= null){
        return ((GenericEntity) L.get(0)).getID();
      }
      else
        return -1;
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      return -2;
    }
  }

  public static int[] getObjectInstanceCategoryIds(int ICObjectInstanceId){
    //select ic_category_id from IC_CATEGORY_ic_object_instance where ic_object_instance_ID=51
    StringBuffer sql = new StringBuffer("select ");
    ICCategory cat = (ICCategory) ICCategory.getStaticInstance(ICCategory.class);
    ICObjectInstance obj = (ICObjectInstance) ICObjectInstance.getStaticInstance(ICObjectInstance.class);
    sql.append(cat.getIDColumnName()).append(" from ");
    sql.append(EntityControl.getManyToManyRelationShipTableName(ICCategory.class,ICObjectInstance.class));
    sql.append(" where ").append(obj.getIDColumnName()).append(" = ").append(ICObjectInstanceId);
    try {
      String[] sids = com.idega.data.SimpleQuerier.executeStringQuery(sql.toString());
      if(sids!=null && sids.length >0){
        int[] ids = new int[sids.length];
        for (int i = 0; i < sids.length; i++) {
          ids[i] = Integer.parseInt(sids[i]);
        }
        return ids;
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return new int[0];
    /*
    try {
      EntityFinder.debug = true;
      List L = EntityFinder.findRelated(eObjectInstance ,(GenericEntity)ICCategory.getStaticInstance(ICCategory.class));
      EntityFinder.debug = false;
      if(L!= null){
        java.util.Iterator iter = L.iterator();
        int[] ids = new int[L.size()];
        for (int i = 0; i < ids.length; i++) {
          ids[i] = ((GenericEntity) L.get(0)).getID();
        }
        return ids;
      }
      else
        return new int[0];
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      return new int[0];
    }
    */
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
      List L = EntityFinder.findRelated(obj,ICCategory.getStaticInstance(ICCategory.class));
      return L;
    }
    catch (SQLException ex) {
      return null;
    }
  }

  private static String getRelatedSQL(int iObjectInstanceId){
    StringBuffer sql = new StringBuffer("select ");
    sql.append(((ICCategory)ICCategory.getStaticInstance(ICCategory.class)).getIDColumnName());
    sql.append(" from ").append(com.idega.data.EntityControl.getManyToManyRelationShipTableName(ICCategory.class,ICObjectInstance.class));
    sql.append(" where ").append(((ICObjectInstance)ICObjectInstance.getStaticInstance(ICObjectInstance.class)).getIDColumnName());
    sql.append(" = ").append(iObjectInstanceId);
    return sql.toString();
  }

  private static String getRelatedEntitySQL(String tablename,int iObjectInstanceId){
    StringBuffer sql = new StringBuffer("select ");
    sql.append(tablename).append(".* from ").append(tablename).append(",");
    String middletable = EntityControl.getManyToManyRelationShipTableName(ICCategory.class,ICObjectInstance.class);
    sql.append(middletable);
    sql.append(" where ").append(((ICObjectInstance)ICObjectInstance.getStaticInstance(ICObjectInstance.class)).getIDColumnName());
    sql.append(" = ").append(iObjectInstanceId);
    String idname = ((ICCategory)ICCategory.getStaticInstance(ICCategory.class)).getIDColumnName();
    sql.append(" and ").append( middletable).append(".").append(idname );
    sql.append(" = ").append(tablename).append(".").append(idname);
    return sql.toString();
  }

  /**
   *  Returns a Collection of ICCategory entities
   *  with specified type
   */
  public static Collection getCategories(int[] ids,String type){
    StringBuffer sql = new StringBuffer("select * from ");
    ICCategory cat = (ICCategory)ICCategory.getStaticInstance(ICCategory.class);
    sql.append(cat.getEntityTableName());
    sql.append(" where ").append(cat.getColumnType()).append(" = ").append(type);
    sql.append(" and ").append(cat.getIDColumnName()).append(" in (");
    for (int i = 0; i < ids.length; i++) {
      if(i>0)
        sql.append(",");
      sql.append(ids[i]);
    }
    sql.append(" )");
    try{
      return EntityFinder.getInstance().findAll(ICCategory.class , sql.toString());
    }
    catch(IDOFinderException ex){

    }
    return null;
  }

  /**
   *  Returns a Collection of ICCategory-ids that
   *  have reference to a ICObjectInstance
   */
  public static Collection collectCategoryIntegerIds(int iObjectInstanceId){
    String[] ids = null;

    try {
      String sql = getRelatedSQL(iObjectInstanceId);
      ids = com.idega.data.SimpleQuerier.executeStringQuery(sql);
      if(ids != null){
        HashSet H = new HashSet();
        Integer I;
        for (int i = 0; i < ids.length; i++) {
          I = new Integer(ids[i]);
          if(!H.contains(I)){
            H.add(I);
          }
        }
        return H;
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }

    return null;
  }

  public Collection listOfCategoryEntity(Class categoryEntityClass,int iCategoryId){
    if(categoryEntityClass.getSuperclass().equals(CategoryEntity.class)){
      try{
        return EntityFinder.getInstance().findAllByColumn(categoryEntityClass,CategoryEntity.getColumnCategoryId(),iCategoryId);
      }
      catch(IDOFinderException ex){

      }
    }
    return null;
  }
/*
  public Collection listOfCategoryEntity(Class categoryEntityClass,int[] iCategoryIds){
    if(categoryEntityClass.getSuperclass().equals(CategoryEntity.class)){
      return EntityFinder.getInstance().findAllByColumn(categoryEntityClass,CategoryEntity.getColumnCategoryId(),iCategoryId);
    }
    else return null;
  }
*/
  public Collection listOfCategoryEntityByInstanceId(Class categoryEntityClass,int ObjectInstanceId){
    if(categoryEntityClass.getSuperclass().equals(CategoryEntity.class)){

      try{
         String entityName = ((CategoryEntity) categoryEntityClass.newInstance()).getEntityName();
        return EntityFinder.getInstance().findAll(categoryEntityClass,getRelatedEntitySQL(entityName,ObjectInstanceId));
      }
      catch(IDOFinderException ex){

      }
      catch(Exception ex){

      }
    }
   return null;
  }

  public static CategoryFinder getInstance(){
    if(categoryFinder== null)
      categoryFinder = new CategoryFinder();
    return categoryFinder;
  }
}