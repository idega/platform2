package com.idega.block.reports.business;

import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.idega.block.category.business.CategoryFinder;
import com.idega.block.category.data.ICCategory;
import com.idega.block.reports.data.Report;
import com.idega.block.reports.data.ReportCategory;
import com.idega.block.reports.data.ReportColumnInfo;
import com.idega.block.reports.data.ReportInfo;
import com.idega.block.reports.data.ReportItem;
import com.idega.core.component.data.ICObject;
import com.idega.core.component.data.ICObjectInstance;
import com.idega.data.EntityFinder;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;

/**
 * Title:        idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */

public class ReportFinder {

  public ReportFinder() {
  }

  public static int	countReportsInCategory(int iCategoryId){
    try {
      Report eReport = (Report)com.idega.block.reports.data.ReportBMPBean.getStaticInstance(Report.class);
      return eReport.getNumberOfRecords(com.idega.block.reports.data.ReportBMPBean.getColumnCategoryId(),String.valueOf(iCategoryId));
    }
    catch (SQLException ex) {

    }
    return 0;

  }

  public static ICCategory getCategory(int iCategoryId){
    return CategoryFinder.getInstance().getCategory(iCategoryId);
  }

   public static Report getReport(int iReportId){
    return (Report) com.idega.block.reports.data.ReportBMPBean.getEntityInstance(Report.class,iReportId);
  }

  public static ReportInfo getReportInfo(int iReportInfoId){
    return (ReportInfo) com.idega.block.reports.data.ReportInfoBMPBean.getEntityInstance(ReportInfo.class,iReportInfoId);
  }

   public static ReportColumnInfo getReportColumnInfo(int iReportColumnInfoId){
    return (ReportColumnInfo) com.idega.block.reports.data.ReportColumnInfoBMPBean.getEntityInstance(ReportColumnInfo.class,iReportColumnInfoId);
  }

  public static ReportColumnInfo getReportInfoFromReport(int iReportId){
    try {
      List l = EntityFinder.findAllByColumn(((com.idega.block.reports.data.ReportColumnInfoHome)com.idega.data.IDOLookup.getHomeLegacy(ReportColumnInfo.class)).createLegacy(),com.idega.block.reports.data.ReportColumnInfoBMPBean.getColumnReportId(),iReportId);
      if(l!=null)
        return (ReportColumnInfo)l.get(0);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }


  public static int getObjectInstanceCategoryId(int iObjectInstanceId,boolean CreateNew){
    return CategoryFinder.getInstance().getObjectInstanceCategoryId(iObjectInstanceId,CreateNew,((com.idega.block.reports.data.ReportCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(ReportCategory.class)).createLegacy().getCategoryType());
  }

  public static int getObjectInstanceCategoryId(ICObjectInstance eObjectInstance){
    return CategoryFinder.getInstance().getObjectInstanceCategoryId(eObjectInstance);
  }

  public static int getObjectInstanceCategoryId(int iObjectInstanceId){
    return CategoryFinder.getInstance().getInstance().getObjectInstanceCategoryId(iObjectInstanceId);
  }

  public static int getObjectInstanceIdFromCategoryId(int iCategoryId){
    return CategoryFinder.getInstance().getObjectInstanceIdFromCategoryId(iCategoryId);
  }

  public static List listOfCategories(){
    try {
      return EntityFinder.findAll(((com.idega.block.reports.data.ReportCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(ReportCategory.class)).createLegacy());
    }
    catch (SQLException ex) {

    }
    return null;
  }


  public static List listOfReports(int iCategoryId){
    try {
      return EntityFinder.findAllByColumn(((com.idega.block.reports.data.ReportHome)com.idega.data.IDOLookup.getHomeLegacy(Report.class)).createLegacy(),com.idega.block.reports.data.ReportBMPBean.getColumnCategoryId(),iCategoryId);
    }
    catch (Exception ex) {

    }
    return null;
  }

  public static List listOfEntityForObjectInstanceId(int instanceid){
    return CategoryFinder.getInstance().listOfCategoryForObjectInstanceId(instanceid);
  }
  public static List listOfEntityForObjectInstanceId( ICObjectInstance obj){
    return CategoryFinder.getInstance().listOfCategoryForObjectInstanceId(obj);
  }

   public static List listOfReportItems(int iCatId){
    List L = null;
    try {
      L = EntityFinder.findAllByColumnOrdered(((com.idega.block.reports.data.ReportItemHome)com.idega.data.IDOLookup.getHomeLegacy(ReportItem.class)).createLegacy(),com.idega.block.reports.data.ReportItemBMPBean.getColumnCategoryId(),iCatId,com.idega.block.reports.data.ReportItemBMPBean.getColumnNameDisplayOrder());
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      L = null;
    }
    return L;
  }

  public static List listOfDataClasses(){
    try {
      return EntityFinder.findAllByColumn(((com.idega.core.component.data.ICObjectHome)com.idega.data.IDOLookup.getHomeLegacy(ICObject.class)).createLegacy(),com.idega.core.component.data.ICObjectBMPBean.getObjectTypeColumnName(),com.idega.core.component.data.ICObjectBMPBean.COMPONENT_TYPE_DATA);
    }
    catch (SQLException ex) {

    }
    return null;
  }

  public static List listOfReportColumnInfo(int iReportId){
    try {
      return EntityFinder.findAllByColumnOrdered(((com.idega.block.reports.data.ReportColumnInfoHome)com.idega.data.IDOLookup.getHomeLegacy(ReportColumnInfo.class)).createLegacy(),com.idega.block.reports.data.ReportColumnInfoBMPBean.getColumnReportId(),iReportId,com.idega.block.reports.data.ReportColumnInfoBMPBean.getColumnColNumber());
    }
    catch (Exception ex) {

    }
    return null;
  }

  public static Map mapOfReportColumnInfoByColumnNumber(int iReportId){
    List L = listOfReportColumnInfo(iReportId) ;
    if(L!=null){
      Iterator iter = L.iterator();
      ReportColumnInfo info;
      Hashtable H = new Hashtable(L.size());
      while (iter.hasNext()) {
        info = (ReportColumnInfo) iter.next();
        if(!H.containsKey(new Integer(info.getColumnNumber())))
          H.put(new Integer(info.getColumnNumber()),info);
      }
      return H;
    }
    return null;
  }

  public static List listOfReportInfo(int iCategoryId,String type){
    try {
      ReportInfo info = ((com.idega.block.reports.data.ReportInfoHome)com.idega.data.IDOLookup.getHomeLegacy(ReportInfo.class)).createLegacy();
      StringBuffer sql = new StringBuffer("select * from ").append(com.idega.block.reports.data.ReportInfoBMPBean.getEntityTableName());
      sql.append(" where ").append(com.idega.block.reports.data.ReportInfoBMPBean.getColumnCategoryId()).append(" = ").append(iCategoryId);
      sql.append(" and ").append(com.idega.block.reports.data.ReportInfoBMPBean.getColumnType()).append(" = '").append(type).append("'");
      return EntityFinder.findAll(info,sql.toString());
    }
    catch (Exception ex) {

    }
    return null;
  }

   public static List listOfReportInfo(String type){
    try {
      ReportInfo info = ((com.idega.block.reports.data.ReportInfoHome)com.idega.data.IDOLookup.getHomeLegacy(ReportInfo.class)).createLegacy();
      StringBuffer sql = new StringBuffer("select * from ").append(com.idega.block.reports.data.ReportInfoBMPBean.getEntityTableName());
      if(type!=null)
        sql.append(" where ").append(com.idega.block.reports.data.ReportInfoBMPBean.getColumnType()).append(" = '").append(type).append("'");
      else
        sql.append(" order by ").append(com.idega.block.reports.data.ReportInfoBMPBean.getColumnType());
      return EntityFinder.findAll(info,sql.toString());
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }

  public static List listOfRelatedReportInfo(Report eReport){
    try {
      return EntityFinder.findRelated(eReport,((com.idega.block.reports.data.ReportInfoHome)com.idega.data.IDOLookup.getHomeLegacy(ReportInfo.class)).createLegacy());
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }
/*
  public static List listOfReportColumnInfo(int iReportId){
    try {
      ReportInfo info = ((com.idega.block.reports.data.ReportInfoHome)com.idega.data.IDOLookup.getHomeLegacy(ReportInfo.class)).createLegacy();
      StringBuffer sql = new StringBuffer("select * from ").append(info.getEntityTableName());
      sql.append(" where ").append(info.getColumnCategoryId()).append(" = ").append(iCategoryId);
      sql.append(" and ").append(info.getColumnType()).append(" = '").append(type).append("'");
      return EntityFinder.findAll(info,sql.toString());
    }
    catch (Exception ex) {

    }
    return null;
  }
*/
  public static String[] pageSizes = { "A4","A3","A2" };

  public static Rectangle getPageSize(String page){
    if(page.equals("A4"))
      return PageSize.A4;
    else if(page.equals("A3"))
      return PageSize.A3;
    else if(page.equals("A2"))
      return PageSize.A2;
    else
      return PageSize.A4;

  }


}
