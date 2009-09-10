package com.idega.block.reports.data;


public interface ReportHome extends com.idega.data.IDOHome
{
 public Report create() throws javax.ejb.CreateException;
 public Report createLegacy();
 public Report findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Report findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Report findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}