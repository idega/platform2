package com.idega.block.finance.data;


public interface AccountUserHome extends com.idega.data.IDOHome
{
 public AccountUser create() throws javax.ejb.CreateException;
 public AccountUser findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findByAssessmentRound(java.lang.Integer p0)throws javax.ejb.FinderException;
 public java.util.Collection findBySearch(java.lang.String p0,java.lang.String p1,java.lang.String p2)throws javax.ejb.FinderException;

}