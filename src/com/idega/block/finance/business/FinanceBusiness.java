package com.idega.block.finance.business;








/**

 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
 * @author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */



public  class FinanceBusiness {

/*

  public static int saveCategory(int iCategoryId,int iObjectInstanceId,String Name,String info){

    int id = -1;

    try {

      FinanceCategory cat = ((com.idega.block.finance.data.FinanceCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(FinanceCategory.class)).createLegacy();

      boolean update = false;

      if(iCategoryId > 0){

        cat = ((com.idega.block.finance.data.FinanceCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(FinanceCategory.class)).findByPrimaryKeyLegacy(iCategoryId);

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

        ICObjectInstance objIns = ((com.idega.core.component.data.ICObjectInstanceHome)com.idega.data.IDOLookup.getHomeLegacy(ICObjectInstance.class)).findByPrimaryKeyLegacy(iObjectInstanceId);

      // Allows only one category per instanceId

        objIns.removeFrom(((com.idega.block.finance.data.FinanceCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(FinanceCategory.class)).createLegacy());

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

        ICObjectInstance obj = ((com.idega.core.component.data.ICObjectInstanceHome)com.idega.data.IDOLookup.getHomeLegacy(ICObjectInstance.class)).findByPrimaryKeyLegacy(iObjectInstanceId);

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

      FinanceCategory cat = ((com.idega.block.finance.data.FinanceCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(FinanceCategory.class)).findByPrimaryKeyLegacy( iCategoryId );

      if(iObjectInstanceId > 0  ){

        ICObjectInstance obj = ((com.idega.core.component.data.ICObjectInstanceHome)com.idega.data.IDOLookup.getHomeLegacy(ICObjectInstance.class)).findByPrimaryKeyLegacy(iObjectInstanceId);

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

      FinanceCategory cat = ((com.idega.block.finance.data.FinanceCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(FinanceCategory.class)).findByPrimaryKeyLegacy(id);

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

      TariffKey key = ((com.idega.block.finance.data.TariffKeyHome)com.idega.data.IDOLookup.getHomeLegacy(TariffKey.class)).createLegacy();

      boolean update = false;

      if(iCategoryId > 0){

        if(id > 0){

          key = ((com.idega.block.finance.data.TariffKeyHome)com.idega.data.IDOLookup.getHomeLegacy(TariffKey.class)).findByPrimaryKeyLegacy(id);

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

      ((com.idega.block.finance.data.TariffKeyHome)com.idega.data.IDOLookup.getHomeLegacy(TariffKey.class)).findByPrimaryKeyLegacy(id).delete();

      return true;

    }

    catch (SQLException ex) {



    }

    return false;

  }



  public static boolean deleteTariffIndex(int id){

    try {

      ((com.idega.block.finance.data.TariffIndexHome)com.idega.data.IDOLookup.getHomeLegacy(TariffIndex.class)).findByPrimaryKeyLegacy(id).delete();

      return true;

    }

    catch (SQLException ex) {



    }

    return false;

  }



  public static boolean saveTariffIndex(int id, String sName,String sInfo,

      String sType, float newvalue,float oldvalue,Timestamp stamp, int iCategoryId){

     try {

      TariffIndex ti = ((com.idega.block.finance.data.TariffIndexHome)com.idega.data.IDOLookup.getHomeLegacy(TariffIndex.class)).createLegacy();

      boolean update = false;

      if(iCategoryId > 0){

        if(id > 0){

          ti = ((com.idega.block.finance.data.TariffIndexHome)com.idega.data.IDOLookup.getHomeLegacy(TariffIndex.class)).findByPrimaryKeyLegacy(id);

          update = true;

        }



        ti = ((com.idega.block.finance.data.TariffIndexHome)com.idega.data.IDOLookup.getHomeLegacy(TariffIndex.class)).createLegacy();

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

      AccountKey key = ((com.idega.block.finance.data.AccountKeyHome)com.idega.data.IDOLookup.getHomeLegacy(AccountKey.class)).createLegacy();

      boolean update = false;

      if(id > 0){

        key = ((com.idega.block.finance.data.AccountKeyHome)com.idega.data.IDOLookup.getHomeLegacy(AccountKey.class)).findByPrimaryKeyLegacy(id);

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

      ((com.idega.block.finance.data.AccountKeyHome)com.idega.data.IDOLookup.getHomeLegacy(AccountKey.class)).findByPrimaryKeyLegacy(id).delete();

      return true;

    }

    catch (SQLException ex) {



    }

    return false;

  }
  
  public static boolean updateTariffPrice(int id, float Price, Timestamp indexStamp){
  		try {
			Tariff tariff = ((TariffHome)com.idega.data.IDOLookup.getHome(Tariff.class)).findByPrimaryKey(new Integer(id));
			tariff.setPrice(Price);
			if(indexStamp!=null)
				tariff.setIndexUpdated(indexStamp);
			tariff.store();
			return true;
			
		} catch (Exception e) {
			//e.printStackTrace();
			return false;
		}
  }



   public static boolean saveTariff(int id, String sName,String sInfo,

      String sAtt,String sIndex,boolean useIndex, Timestamp indexStamp,

      float Price,int iAccountKeyId,int iTariffGroupId){



     try {

      Tariff tariff = ((TariffHome)com.idega.data.IDOLookup.getHome(Tariff.class)).create();

      if(id > 0){

        tariff = ((TariffHome)com.idega.data.IDOLookup.getHome(Tariff.class)).findByPrimaryKey(new Integer(id));

      }

      tariff.setName(sName);

      tariff.setInfo(sInfo);

      tariff.setTariffAttribute(sAtt);

      tariff.setAccountKeyId(iAccountKeyId);

      tariff.setTariffGroupId(iTariffGroupId);

      tariff.setPrice(Price);



      tariff.setUseFromDate(IWTimestamp.getTimestampRightNow());

      tariff.setUseToDate(IWTimestamp.getTimestampRightNow());



      tariff.setIndexType(sIndex);

      tariff.setUseIndex(useIndex);

      if(indexStamp!=null)

        tariff.setIndexUpdated(indexStamp);

        tariff.store();

      return true;

      }

      catch (Exception ex) {

        //ex.printStackTrace();

      }

      return false;

  }



  public static boolean deleteTariff(int id){

    try {

      ((TariffHome)com.idega.data.IDOLookup.getHome(Tariff.class)).findByPrimaryKey(new Integer(id)).remove();

      return true;

    }

    catch (Exception ex) {



    }

    return false;

  }



  public static int saveTariffGroup(int id, String sName,String sInfo,

      int HandlerId,boolean useIndex,int iCategoryId){



      int rid = -1;

     try {

      TariffGroup tariff = ((com.idega.block.finance.data.TariffGroupHome)com.idega.data.IDOLookup.getHomeLegacy(TariffGroup.class)).createLegacy();

      boolean update = false;

      if(id > 0){

        tariff = ((com.idega.block.finance.data.TariffGroupHome)com.idega.data.IDOLookup.getHomeLegacy(TariffGroup.class)).findByPrimaryKeyLegacy(id);

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

      PaymentType key = ((com.idega.block.finance.data.PaymentTypeHome)com.idega.data.IDOLookup.getHomeLegacy(PaymentType.class)).createLegacy();

      boolean update = false;

      if(iCategoryId > 0){

        if(id > 0){

          key = ((com.idega.block.finance.data.PaymentTypeHome)com.idega.data.IDOLookup.getHomeLegacy(PaymentType.class)).findByPrimaryKeyLegacy(id);

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

*/



}



