package com.idega.block.finance.data;


public interface FinanceCategoryHome extends com.idega.data.IDOHome
{
 public FinanceCategory create() throws javax.ejb.CreateException;
 public FinanceCategory createLegacy();
 public FinanceCategory findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public FinanceCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public FinanceCategory findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}