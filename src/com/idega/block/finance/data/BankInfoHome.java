package com.idega.block.finance.data;

import javax.ejb.FinderException;


public interface BankInfoHome extends com.idega.data.IDOHome
{
 public BankInfo create() throws javax.ejb.CreateException;
 public BankInfo findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public BankInfo findByGroupId(int groupId) throws FinderException;

}