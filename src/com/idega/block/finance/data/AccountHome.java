package com.idega.block.finance.data;


public interface AccountHome extends com.idega.data.IDOHome
{
 public Account create() throws javax.ejb.CreateException;
 public Account findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllByUserId(int p0)throws javax.ejb.FinderException;
 public java.util.Collection findAllByUserIdAndType(int p0,java.lang.String p1)throws javax.ejb.FinderException;
 public java.util.Collection findByAssessmentRound(int p0)throws javax.ejb.FinderException;
 public java.util.Collection findByAssessmentRound(java.lang.Integer p0,int p1,int p2)throws javax.ejb.FinderException;
 public java.util.Collection findBySQL(java.lang.String p0)throws javax.ejb.FinderException;
 public java.util.Collection findBySearch(java.lang.String p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,int p5)throws javax.ejb.FinderException;
 public int countByAssessmentRound(java.lang.Integer p0)throws com.idega.data.IDOException;
 public int countByTypeAndCategory(java.lang.String p0,java.lang.Integer p1)throws com.idega.data.IDOException;

}