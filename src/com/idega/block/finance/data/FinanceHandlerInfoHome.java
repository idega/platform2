package com.idega.block.finance.data;


public interface FinanceHandlerInfoHome extends com.idega.data.IDOHome
{
 public FinanceHandlerInfo create() throws javax.ejb.CreateException;
 public FinanceHandlerInfo createLegacy();
 public FinanceHandlerInfo findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public FinanceHandlerInfo findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public FinanceHandlerInfo findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}