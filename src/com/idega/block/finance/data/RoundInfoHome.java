package com.idega.block.finance.data;


public interface RoundInfoHome extends com.idega.data.IDOHome
{
 public RoundInfo create() throws javax.ejb.CreateException;
 public RoundInfo findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findByCategoryAndGroup(java.lang.Integer p0,java.lang.Integer p1,java.sql.Date p2,java.sql.Date p3)throws javax.ejb.FinderException;

}