package com.idega.block.reports.data;


public interface ReportCategoryHome extends com.idega.data.IDOHome
{
 public ReportCategory create() throws javax.ejb.CreateException;
 public ReportCategory createLegacy();
 public ReportCategory findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public ReportCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public ReportCategory findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}