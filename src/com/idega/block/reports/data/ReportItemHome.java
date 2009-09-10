package com.idega.block.reports.data;


public interface ReportItemHome extends com.idega.data.IDOHome
{
 public ReportItem create() throws javax.ejb.CreateException;
 public ReportItem createLegacy();
 public ReportItem findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public ReportItem findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public ReportItem findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}