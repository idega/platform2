package com.idega.block.category.business;

import java.sql.*;
import com.idega.presentation.IWContext;
import com.idega.block.news.data.*;
import com.idega.block.text.data.LocalizedText;
import com.idega.block.text.business.*;
import com.idega.core.data.ICObjectInstance;
import com.idega.util.idegaTimestamp;
import com.idega.core.data.ICFile;
import java.util.List;
import java.util.Vector;
import java.util.Iterator;
import com.idega.data.EntityFinder;
import com.idega.block.text.data.Content;
import com.idega.core.data.ICCategory;

public class CategoryBusiness{


  public static boolean disconnectBlock(int instanceid){
    List L = CategoryFinder.listOfCategoryForObjectInstanceId(instanceid);
    if(L!= null){
      Iterator I = L.iterator();
      while(I.hasNext()){
        ICCategory Cat = (ICCategory) I.next();
        disconnectCategory(Cat,instanceid);
      }
      return true;
    }
    else
      return false;

  }

  public static boolean disconnectCategory(ICCategory Cat,int iObjectInstanceId){
    try {
      if(iObjectInstanceId > 0  ){
        ICObjectInstance obj = new ICObjectInstance(iObjectInstanceId);
        Cat.removeFrom(obj);
      }
      return true;
    }
    catch (SQLException ex) {

    }
    return false;
  }


  public static boolean deleteBlock(int instanceid){
    List L = CategoryFinder.listOfCategoryForObjectInstanceId(instanceid);
    if(L!= null){
      Iterator I = L.iterator();
      while(I.hasNext()){
        ICCategory N = (ICCategory) I.next();
        try{
          deleteCategory(N.getID(),instanceid );
        }
        catch(SQLException sql){

        }
      }
      return true;
    }
    else
      return false;
  }

  public static void deleteCategory(int iCategoryId) throws SQLException{
    deleteCategory(iCategoryId ,CategoryFinder.getObjectInstanceIdFromCategoryId(iCategoryId));
  }

  public static void deleteCategory(int iCategoryId ,int iObjectInstanceId) throws SQLException {
    ICCategory nc = (ICCategory) CategoryFinder.getCategory( iCategoryId );

    if(iObjectInstanceId > 0  ){
      ICObjectInstance obj = new ICObjectInstance(iObjectInstanceId);
      nc.removeFrom(obj);
    }
    nc.delete();

  }

  public static ICCategory saveCategory(int iCategoryId,String sName,String sDesc,int iObjectInstanceId,String type,boolean allowMultible){
    ICCategory Cat = new ICCategory();
    if(iCategoryId > 0)
      Cat = CategoryFinder.getCategory(iCategoryId);
    Cat.setName(sName);
    Cat.setDescription(sDesc);
    Cat.setType(type);
    return saveCategory(Cat,iObjectInstanceId,allowMultible);
  }

  public static ICCategory saveCategory(int iCategoryId,String sName,String sDesc,int iObjectInstanceId,String type){
    return saveCategory(iCategoryId,sName,sDesc,iObjectInstanceId,type,false);
  }

  public static ICCategory saveCategory(ICCategory Cat,int iObjectInstanceId,boolean allowMultible){
    javax.transaction.TransactionManager t = com.idega.transaction.IdegaTransactionManager.getInstance();
    try{
     t.begin();
      if(Cat.getID()>0){
        Cat.update();
      }
      else{
        Cat.setCreated(idegaTimestamp.getTimestampRightNow());
        Cat.setValid(true);
        Cat.insert();
      }
      // Binding category to instanceId
      if(iObjectInstanceId > 0){
        ICObjectInstance objIns = new ICObjectInstance(iObjectInstanceId);
        // Allows only one category per instanceId
        if(!allowMultible)
          objIns.removeFrom((ICCategory)ICCategory.getEntityInstance(ICCategory.class));
        Cat.addTo(objIns);
      }
      t.commit();
      return Cat;
    }
    catch(Exception e) {
      try {
        t.rollback();
      }
      catch(javax.transaction.SystemException ex) {
        ex.printStackTrace();
      }
      e.printStackTrace();
    }
    return null;
  }


  public static int createCategory(int iObjectInstanceId,String type){
    return saveCategory(-1,"Category - "+iObjectInstanceId,"Category - "+iObjectInstanceId,iObjectInstanceId ,type,false).getID();
  }

}