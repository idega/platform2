package com.idega.block.finance.data;


public interface FinanceHandlerInfoHome extends com.idega.data.IDOHome
{
 public FinanceHandlerInfo create() throws javax.ejb.CreateException;
 public FinanceHandlerInfo findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;

}