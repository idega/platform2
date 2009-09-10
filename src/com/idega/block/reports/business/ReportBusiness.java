package com.idega.block.reports.business;

import com.idega.block.reports.data.*;
import com.idega.core.component.data.ICObjectInstance;
import com.idega.data.GenericEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Iterator;

/**
 * Title:        idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */

public class ReportBusiness {

  public ReportBusiness() {
  }

	public static ReportCategory createReportCategory(int iObjectInstanceId){
    return saveReportCategory(-1,iObjectInstanceId,"Reports","Reports" );
  }

	public static int createCategory(int iObjectInstanceId){
    return saveCategory(-1,iObjectInstanceId,"Reports","Reports" );
  }

	public static void deleteCategory(int iCategoryId){
    deleteCategory(iCategoryId ,ReportFinder.getObjectInstanceIdFromCategoryId(iCategoryId));
  }

	public static void deleteCategory(int iCategoryId ,int iObjectInstanceId) {
    javax.transaction.TransactionManager t = com.idega.transaction.IdegaTransactionManager.getInstance();
    try {
      t.begin();
      ReportCategory nc = ((com.idega.block.reports.data.ReportCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(ReportCategory.class)).findByPrimaryKeyLegacy( iCategoryId );
      List L = ReportFinder.listOfReports(nc.getID());
      if(L != null){
        Report rep;

        for (int i = 0; i < L.size(); i++) {
          rep = (Report) L.get(i);
          deleteReport(rep.getID());
        }
      }

      if(iObjectInstanceId > 0  ){
        ICObjectInstance obj = ((com.idega.core.component.data.ICObjectInstanceHome)com.idega.data.IDOLookup.getHomeLegacy(ICObjectInstance.class)).findByPrimaryKeyLegacy(iObjectInstanceId);
        nc.removeFrom(obj);
      }
      nc.delete();
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

	public static int saveCategory(int iCategoryId,int iObjectInstanceId,String Name,String info){
	  return saveReportCategory(iCategoryId,iObjectInstanceId,Name,info).getID();
	}

	public static ReportCategory saveReportCategory(int iCategoryId,int iObjectInstanceId,String Name,String info){
		ReportCategory cat  = null;
		try {
                  cat = ((com.idega.block.reports.data.ReportCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(ReportCategory.class)).createLegacy();
                  boolean update = false;
                  if(iCategoryId > 0){
                    cat = ((com.idega.block.reports.data.ReportCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(ReportCategory.class)).findByPrimaryKeyLegacy(iCategoryId);
                          update = true;
                  }
                  cat.setName(Name);
                  cat.setDescription(info);

                  if(update) {
					cat.update();
				}
				else {
					cat.insert();
				}
                          // Binding category to instanceId
                  if(iObjectInstanceId > 0){
                          ICObjectInstance objIns = ((com.idega.core.component.data.ICObjectInstanceHome)com.idega.data.IDOLookup.getHomeLegacy(ICObjectInstance.class)).findByPrimaryKeyLegacy(iObjectInstanceId);
                          // Allows only one category per instanceId
                          objIns.removeFrom(((com.idega.block.reports.data.ReportCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(ReportCategory.class)).createLegacy());
                          cat.addTo(objIns);
      }
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}
		return cat;
	}

	public static boolean disconnectCategory(ReportCategory eCategory,int iObjectInstanceId){
    try {
      eCategory.setValid(false);
      eCategory.update();
      if(iObjectInstanceId > 0  ){
        ICObjectInstance obj = ((com.idega.core.component.data.ICObjectInstanceHome)com.idega.data.IDOLookup.getHomeLegacy(ICObjectInstance.class)).findByPrimaryKeyLegacy(iObjectInstanceId);
        eCategory.removeFrom(obj);
      }

      return true;
    }
    catch (SQLException ex) {

    }
    return false;
  }

	public static boolean disconnectBlock(int instanceid){
    List L = ReportFinder.listOfEntityForObjectInstanceId(instanceid);
    if(L!= null){
      Iterator I = L.iterator();
      while(I.hasNext()){
        ReportCategory N = (ReportCategory) I.next();
        disconnectCategory(N,instanceid);
      }
      return true;
    }
	else {
		return false;
	}

  }

  public static boolean deleteReport(int id){
    return deleteReportEntity(Report.class,id);
  }

  public static boolean deleteReportInfo(int id){
    return deleteReportEntity(ReportInfo.class,id);
  }

  public static boolean deleteReportColumnInfo(int id){
    return deleteReportEntity(ReportColumnInfo.class,id);
  }


	 public static boolean deleteReportEntity(Class entityClass,int iEntityId){
    try {
      com.idega.data.GenericEntity.getEntityInstance(entityClass,iEntityId).delete();
      return true;
    }
    catch (SQLException ex) {
      return false;
    }
  }

	 public static boolean deleteBlock(int instanceid){
		/*
    List L = ContractFinder.listOfEntityForObjectInstanceId(instanceid);
    if(L!= null){
      Iterator I = L.iterator();
      while(I.hasNext()){
        ContractCategory N = (ContractCategory) I.next();
        deleteCategory(N.getID(),instanceid );
      }
      return true;
    }
    else
      return false;
		*/
		return disconnectBlock(instanceid);
  }

  public static boolean saveReportColumnInfo(ReportColumnInfo info){
    try{
      if(info.getID() > 0) {
		info.update();
	}
	else {
		info.insert();
	}
        return true;
      }
    catch(SQLException ex){
      ex.printStackTrace();
    }
    return false;
  }

  public static boolean saveReportInfo(ReportInfo info){
    try{
      if(info.getID() > 0) {
		info.update();
	}
	else {
		info.insert();
	}
        return true;
      }
    catch(SQLException ex){
      ex.printStackTrace();
    }
    return false;
  }

  public static void saveRelatedReportInfo(int iReportId,int[] ReportInfoIds){
    try {
      GenericEntity.getEntityInstance(Report.class,iReportId).removeFrom(ReportInfo.class);
      for (int i = 0; i < ReportInfoIds.length; i++) {
        GenericEntity.getEntityInstance(Report.class,iReportId).addTo(ReportInfo.class,ReportInfoIds[i]);
      }
    }
    catch (Exception ex) {

    }
  }


}
