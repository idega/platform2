package com.idega.block.reports.data;


public interface ReportCategoryAttributeHome extends com.idega.data.IDOHome
{
 public ReportCategoryAttribute create() throws javax.ejb.CreateException;
 public ReportCategoryAttribute createLegacy();
 public ReportCategoryAttribute findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public ReportCategoryAttribute findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public ReportCategoryAttribute findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}