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
      //newsCat.setValid(false);
      //newsCat.update();
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
    ICCategory nc = new ICCategory( iCategoryId );

    if(iObjectInstanceId > 0  ){
      ICObjectInstance obj = new ICObjectInstance(iObjectInstanceId);
      nc.removeFrom(obj);
    }
    nc.delete();

  }

  public static ICCategory saveCategory(int iCategoryId,String sName,String sDesc,int iObjectInstanceId,String type){
    javax.transaction.TransactionManager t = com.idega.transaction.IdegaTransactionManager.getInstance();
    try{
     t.begin();
      boolean update = false;
      ICCategory Cat = null;
      if(iCategoryId > 0){
        update = true;
        Cat = new ICCategory(iCategoryId );
      }
      else{
        Cat = new ICCategory();
      }

      Cat.setName(sName);
      Cat.setDescription(sDesc);
      Cat.setValid(true);
      Cat.setCreated(idegaTimestamp.getTimestampRightNow());
      Cat.setType(type);

      if(update){
        Cat.update();
      }
      else{
        Cat.insert();
      }
      // Binding category to instanceId
      if(iObjectInstanceId > 0){
        ICObjectInstance objIns = new ICObjectInstance(iObjectInstanceId);
        // Allows only one category per instanceId
        objIns.removeFrom(new ICCategory());
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
    return saveCategory(-1,"Category - "+iObjectInstanceId,"Category - "+iObjectInstanceId,iObjectInstanceId ,type).getID();
  }
}