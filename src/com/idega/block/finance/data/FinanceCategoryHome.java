package com.idega.block.finance.data;


public interface FinanceCategoryHome extends com.idega.data.IDOHome
{
 public FinanceCategory create() throws javax.ejb.CreateException;
 public FinanceCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}