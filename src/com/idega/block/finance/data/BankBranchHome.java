package com.idega.block.finance.data;


public interface BankBranchHome extends com.idega.data.IDOHome
{
 public BankBranch create() throws javax.ejb.CreateException;
 public BankBranch findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}