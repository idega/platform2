package com.idega.block.finance.data;


public interface AccountInfoHome extends com.idega.data.IDOHome
{
 public AccountInfo create() throws javax.ejb.CreateException;
 public AccountInfo findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findByAssessmentRound(java.lang.Integer p0)throws javax.ejb.FinderException;
 public java.util.Collection findByAssessmentRound(java.lang.Integer p0,int p1,int p2)throws javax.ejb.FinderException;
 public java.util.Collection findByOwner(java.lang.Integer p0)throws javax.ejb.FinderException;
 public java.util.Collection findByOwnerAndType(java.lang.Integer p0,java.lang.String p1)throws javax.ejb.FinderException;

}