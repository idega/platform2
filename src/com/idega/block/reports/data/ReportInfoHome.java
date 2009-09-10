package com.idega.block.reports.data;


public interface ReportInfoHome extends com.idega.data.IDOHome
{
 public ReportInfo create() throws javax.ejb.CreateException;
 public ReportInfo createLegacy();
 public ReportInfo findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public ReportInfo findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public ReportInfo findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}