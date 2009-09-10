package com.idega.block.datareport.data;


public interface ReportDesignFileHome extends com.idega.data.IDOHome
{
 public ReportDesignFile create() throws javax.ejb.CreateException;
 public ReportDesignFile findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}