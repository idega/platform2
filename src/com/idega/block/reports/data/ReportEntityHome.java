package com.idega.block.reports.data;


public interface ReportEntityHome extends com.idega.data.IDOHome
{
 public ReportEntity create() throws javax.ejb.CreateException;
 public ReportEntity createLegacy();
 public ReportEntity findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public ReportEntity findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public ReportEntity findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}