package com.idega.block.finance.business;

import com.idega.block.finance.data.*;
import com.idega.util.idegaTimestamp;
import com.idega.core.data.ICObjectInstance;
import com.idega.core.data.ICFile;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Iterator;
import java.util.Map;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
 * @author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public  class FinanceBusiness {

  public static int saveCategory(int iCategoryId,int iObjectInstanceId,String Name,String info){
    int id = -1;
    try {
      FinanceCategory cat = new FinanceCategory();
      boolean update = false;
      if(iCategoryId > 0){
        cat = new FinanceCategory(iCategoryId);
        update = true;
      }
      cat.setName(Name);
      cat.setDescription(info);
      if(update)
        cat.update();
      else
        cat.insert();
      // Binding category to instanceId
      if(iObjectInstanceId > 0){
        ICObjectInstance objIns = new ICObjectInstance(iObjectInstanceId);
      // Allows only one category per instanceId
        objIns.removeFrom(new FinanceCategory());
        cat.addTo(objIns);
      }
      id = cat.getID();
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
    return id;
    }

    public static boolean disconnectBlock(int instanceid){
    List L = FinanceFinder.getInstance().listOfEntityForObjectInstanceId(instanceid);
    if(L!= null){
      Iterator I = L.iterator();
      while(I.hasNext()){
        FinanceCategory cat = (FinanceCategory) I.next();
        disconnectCategory(cat,instanceid);
      }
      return true;
    }
    else
      return false;

  }



  public static boolean disconnectCategory(FinanceCategory Cat,int iObjectInstanceId){
    try {
      Cat.setValid(false);
      Cat.update();
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
    return disconnectBlock(instanceid);
  }

  public static void deleteCategory(int iCategoryId){
    deleteCategory(iCategoryId ,FinanceFinder.getInstance().getObjectInstanceIdFromCategoryId(iCategoryId));
  }

  public static void deleteCategory(int iCategoryId ,int iObjectInstanceId) {
    javax.transaction.TransactionManager t = com.idega.transaction.IdegaTransactionManager.getInstance();
    try {
      t.begin();
    //  List O = TextFinder.listOfObjectInstanceTexts();
      FinanceCategory cat = new FinanceCategory( iCategoryId );
      if(iObjectInstanceId > 0  ){
        ICObjectInstance obj = new ICObjectInstance(iObjectInstanceId);
        cat.removeFrom(obj);
      }
      cat.delete();
     t.commit();
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
  }

  public static int createCategory(int iObjectInstanceId){
    return saveCategory(-1,iObjectInstanceId,"Finance","Finance category" );
  }

  public static boolean updateCategoryDescription(int id ,String description){
    try {
      FinanceCategory cat = new FinanceCategory(id);
      cat.setDescription(description);
      cat.update();
      return true;
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
    return false;
  }

  public static boolean saveTariffKey(int id, String sName,String sInfo, int iCategoryId){
     try {
      TariffKey key = new TariffKey();
      boolean update = false;
      if(iCategoryId > 0){
        if(id > 0){
          key = new TariffKey(id);
          update = true;
        }
        key.setName(sName);
        key.setInfo(sInfo);
        key.setCategoryId(iCategoryId);
        if(update){
          key.update();
        }
        else{
          key.insert();
        }
        return true;
        }
        return false;
      }
      catch (SQLException ex) {
        ex.printStackTrace();
      }
      return false;
  }

  public static boolean deleteTariffKey(int id){
    try {
      new TariffKey(id).delete();
      return true;
    }
    catch (SQLException ex) {

    }
    return false;
  }

  public static boolean deleteTariffIndex(int id){
    try {
      new TariffIndex(id).delete();
      return true;
    }
    catch (SQLException ex) {

    }
    return false;
  }

  public static boolean saveTariffIndex(int id, String sName,String sInfo,
      String sType, float newvalue,float oldvalue,Timestamp stamp, int iCategoryId){
     try {
      TariffIndex ti = new TariffIndex();
      boolean update = false;
      if(iCategoryId > 0){
        if(id > 0){
          ti = new TariffIndex(id);
          update = true;
        }

        ti = new TariffIndex();
        ti.setName(sName);
        ti.setInfo(sInfo);

        ti.setOldValue(oldvalue);
        ti.setIndex(newvalue);
        ti.setDate(stamp);
        ti.setType(sType);
        ti.setCategoryId(iCategoryId);
        ti.setNewValue(newvalue);
        if(update){
          ti.update();
        }
        else{
          ti.insert();
        }
        return true;
        }
        return false;
      }
      catch (SQLException ex) {
        ex.printStackTrace();
      }
      return false;
  }
  public static boolean saveAccountKey(int id, String sName,String sInfo,int TariffKeyId, int iCategoryId){
     try {
      AccountKey key = new AccountKey();
      boolean update = false;
      if(id > 0){
        key = new AccountKey(id);
        update = true;
      }
      key.setName(sName);
      key.setInfo(sInfo);
      key.setTariffKeyId(TariffKeyId);
      key.setCategoryId(iCategoryId);
      if(update)
        key.update();
      else
        key.insert();
      return true;
      }
      catch (SQLException ex) {
        //ex.printStackTrace();
      }
      return false;
  }
  public static boolean deleteAccountKey(int id){
    try {
      new AccountKey(id).delete();
      return true;
    }
    catch (SQLException ex) {

    }
    return false;
  }

   public static boolean saveTariff(int id, String sName,String sInfo,
      String sAtt,String sIndex,boolean useIndex, Timestamp indexStamp,
      float Price,int iAccountKeyId,int iTariffGroupId){

     try {
      Tariff tariff = new Tariff();
      boolean update = false;
      if(id > 0){
        tariff = new Tariff(id);
        update = true;
      }
      tariff.setName(sName);
      tariff.setInfo(sInfo);
      tariff.setTariffAttribute(sAtt);
      tariff.setAccountKeyId(iAccountKeyId);
      tariff.setTariffGroupId(iTariffGroupId);
      tariff.setPrice(Price);

      tariff.setUseFromDate(idegaTimestamp.getTimestampRightNow());
      tariff.setUseToDate(idegaTimestamp.getTimestampRightNow());

      tariff.setIndexType(sIndex);
      tariff.setUseIndex(useIndex);
      if(indexStamp!=null)
        tariff.setIndexUpdated(indexStamp);
      if(update)
        tariff.update();
      else
        tariff.insert();
      return true;
      }
      catch (SQLException ex) {
        //ex.printStackTrace();
      }
      return false;
  }

  public static boolean deleteTariff(int id){
    try {
      new Tariff(id).delete();
      return true;
    }
    catch (SQLException ex) {

    }
    return false;
  }

  public static int saveTariffGroup(int id, String sName,String sInfo,
      int HandlerId,boolean useIndex,int iCategoryId){

      int rid = -1;
     try {
      TariffGroup tariff = new TariffGroup();
      boolean update = false;
      if(id > 0){
        tariff = new TariffGroup(id);
        update = true;
      }
      tariff.setName(sName);
      tariff.setInfo(sInfo);
      tariff.setCategoryId(iCategoryId);
      tariff.setUseIndex(useIndex);
      if(HandlerId > 0)
        tariff.setHandlerId(HandlerId);
      if(update)
        tariff.update();
      else
        tariff.insert();
      rid = tariff.getID();
      }
      catch (SQLException ex) {
        //ex.printStackTrace();
      }
      return rid;
  }

  //savePaymentType(ID,sName,sInfo,iCategoryId,payments,cost,percent);
   public static boolean savePaymentType(int id, String sName,String sInfo,
    int iCategoryId,Integer payments,Float cost,Float percent){
     try {
      PaymentType key = new PaymentType();
      boolean update = false;
      if(iCategoryId > 0){
        if(id > 0){
          key = new PaymentType(id);
          update = true;
        }
        key.setName(sName);
        key.setInfo(sInfo);
        key.setCategoryId(iCategoryId);
        if(payments!=null)
          key.setPayments(payments.intValue());
        if(cost !=null)
          key.setAmountCost(cost.floatValue());
        if(percent !=null)
          key.setPercentCost(percent.floatValue());
        if(update){
          key.update();
        }
        else{
          key.insert();
        }
        return true;
        }
        return false;
      }
      catch (SQLException ex) {
        ex.printStackTrace();
      }
      return false;
  }


}

