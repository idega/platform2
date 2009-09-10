package com.idega.block.reports.data;


public interface ReportColumnInfoHome extends com.idega.data.IDOHome
{
 public ReportColumnInfo create() throws javax.ejb.CreateException;
 public ReportColumnInfo createLegacy();
 public ReportColumnInfo findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public ReportColumnInfo findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public ReportColumnInfo findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}