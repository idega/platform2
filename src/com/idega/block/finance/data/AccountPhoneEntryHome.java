package com.idega.block.finance.data;

import java.util.Collection;

import javax.ejb.FinderException;


public interface AccountPhoneEntryHome extends com.idega.data.IDOHome
{
 public AccountPhoneEntry create() throws javax.ejb.CreateException;
 public AccountPhoneEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findByAccountAndStatus(java.lang.Integer p0,java.lang.String p1,java.sql.Date p2,java.sql.Date p3)throws javax.ejb.FinderException;
 public java.util.Collection findUnbilledByAccountAndPeriod(java.lang.Integer p0,java.sql.Date p1,java.sql.Date p2)throws javax.ejb.FinderException;
 public Collection findByPhoneNumber(String number)throws FinderException;
}