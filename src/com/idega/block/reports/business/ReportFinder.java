package com.idega.block.reports.business;

import com.idega.data.EntityFinder;
import java.util.List;
import com.idega.block.reports.data.*;
import com.idega.core.data.ICObjectInstance;
import java.sql.SQLException;
import com.idega.core.data.ICObject;

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
			return eReport.getNumberOfRecords(eReport.getColumnNameCategory(),String.valueOf(iCategoryId));
		}
		catch (SQLException ex) {

		}
		return 0;

	}

	public static ReportCategory getCategory(int iCategoryId){
    if( iCategoryId > 0){
		  try {
        return new ReportCategory(iCategoryId );
      }
      catch (SQLException ex) {

				ex.printStackTrace();
      }
		}
		else
			return ReportBusiness.createReportCategory(-1);
		return null;
  }


	public static int getObjectInstanceCategoryId(int iObjectInstanceId,boolean CreateNew){
    int id = -1;
    try {
      ICObjectInstance obj = new ICObjectInstance(iObjectInstanceId);
      id = getObjectInstanceCategoryId(obj);
			 if(id <= 0 && CreateNew ){
        id = ReportBusiness.createCategory(iObjectInstanceId );
      }
    }
    catch (Exception ex) {

    }
    return id;
  }

	public static int getObjectInstanceCategoryId(ICObjectInstance eObjectInstance){
    try {
      List L = EntityFinder.findRelated(eObjectInstance ,new ReportCategory());
      if(L!= null){
        return ((ReportCategory) L.get(0)).getID();
      }
      else
        return -1;
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      return -2;
    }
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

	public static int getObjectInstanceIdFromCategoryId(int iCategoryId){
    try {
      ReportCategory nw = new ReportCategory(iCategoryId);
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
			return EntityFinder.findAllByColumn(new Report(),Report.getColumnNameCategory(),iCategoryId);
		}
		catch (Exception ex) {

		}
		return null;
	}

	public static List listOfEntityForObjectInstanceId(int instanceid){
    try {
      ICObjectInstance obj = new ICObjectInstance(instanceid );
      return listOfEntityForObjectInstanceId(obj);
    }
    catch (SQLException ex) {
      return null;
    }
  }

   public static List listOfReportItems(int iCatId){
    List L = null;
    try {
      L = EntityFinder.findAllByColumnOrdered(new ReportItem(),ReportItem.getColumnNameCategory(),iCatId,ReportItem.getColumnNameDisplayOrder());
    }
    catch (SQLException ex) {
			ex.printStackTrace();
      L = null;
    }
    return L;
  }

  public static List listOfEntityForObjectInstanceId( ICObjectInstance obj){
    try {
      List L = EntityFinder.findRelated(obj,new ReportCategory());
      return L;
    }
    catch (SQLException ex) {
      return null;
    }
  }

	public static List listOfDataClasses(){
	  try {
      return EntityFinder.findAllByColumn(new ICObject(),ICObject.getObjectTypeColumnName(),ICObject.COMPONENT_TYPE_DATA);
    }
    catch (SQLException ex) {

    }
		return null;
	}

}