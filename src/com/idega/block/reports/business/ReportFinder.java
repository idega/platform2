package com.idega.block.reports.business;

import com.idega.data.EntityFinder;
import java.util.*;
import com.idega.block.reports.data.*;
import com.idega.core.data.ICObjectInstance;
import java.sql.SQLException;
import com.idega.core.data.ICObject;
import com.idega.block.category.business.*;
import com.idega.core.data.ICCategory;
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
      Report eReport = (Report)Report.getStaticInstance(Report.class);
      return eReport.getNumberOfRecords(eReport.getColumnCategoryId(),String.valueOf(iCategoryId));
    }
    catch (SQLException ex) {

    }
    return 0;

  }

  public static ICCategory getCategory(int iCategoryId){
    return CategoryFinder.getInstance().getCategory(iCategoryId);
  }

   public static Report getReport(int iReportId){
    return (Report) Report.getEntityInstance(Report.class,iReportId);
  }

  public static ReportInfo getReportInfo(int iReportInfoId){
    return (ReportInfo) ReportInfo.getEntityInstance(ReportInfo.class,iReportInfoId);
  }

   public static ReportColumnInfo getReportColumnInfo(int iReportColumnInfoId){
    return (ReportColumnInfo) ReportColumnInfo.getEntityInstance(ReportColumnInfo.class,iReportColumnInfoId);
  }

  public static ReportColumnInfo getReportInfoFromReport(int iReportId){
    try {
      List l = EntityFinder.findAllByColumn(new ReportColumnInfo(),ReportColumnInfo.getColumnReportId(),iReportId);
      if(l!=null)
        return (ReportColumnInfo)l.get(0);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }


  public static int getObjectInstanceCategoryId(int iObjectInstanceId,boolean CreateNew){
    return CategoryFinder.getInstance().getObjectInstanceCategoryId(iObjectInstanceId,CreateNew,new ReportCategory().getCategoryType());
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
      return EntityFinder.findAll(new ReportCategory());
    }
    catch (SQLException ex) {

    }
    return null;
  }


  public static List listOfReports(int iCategoryId){
    try {
      return EntityFinder.findAllByColumn(new Report(),Report.getColumnCategoryId(),iCategoryId);
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
      L = EntityFinder.findAllByColumnOrdered(new ReportItem(),ReportItem.getColumnCategoryId(),iCatId,ReportItem.getColumnNameDisplayOrder());
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      L = null;
    }
    return L;
  }

  public static List listOfDataClasses(){
    try {
      return EntityFinder.findAllByColumn(new ICObject(),ICObject.getObjectTypeColumnName(),ICObject.COMPONENT_TYPE_DATA);
    }
    catch (SQLException ex) {

    }
    return null;
  }

  public static List listOfReportColumnInfo(int iReportId){
    try {
      return EntityFinder.findAllByColumnOrdered(new ReportColumnInfo(),ReportColumnInfo.getColumnReportId(),iReportId,ReportColumnInfo.getColumnColNumber());
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
      ReportInfo info = new ReportInfo();
      StringBuffer sql = new StringBuffer("select * from ").append(info.getEntityTableName());
      sql.append(" where ").append(info.getColumnCategoryId()).append(" = ").append(iCategoryId);
      sql.append(" and ").append(info.getColumnType()).append(" = '").append(type).append("'");
      return EntityFinder.findAll(info,sql.toString());
    }
    catch (Exception ex) {

    }
    return null;
  }

   public static List listOfReportInfo(String type){
    try {
      ReportInfo info = new ReportInfo();
      StringBuffer sql = new StringBuffer("select * from ").append(info.getEntityTableName());
      if(type!=null)
        sql.append(" where ").append(info.getColumnType()).append(" = '").append(type).append("'");
      else
        sql.append(" order by ").append(info.getColumnType());
      return EntityFinder.findAll(info,sql.toString());
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }

  public static List listOfRelatedReportInfo(Report eReport){
    try {
      return EntityFinder.findRelated(eReport,new ReportInfo());
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }
/*
  public static List listOfReportColumnInfo(int iReportId){
    try {
      ReportInfo info = new ReportInfo();
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