package com.idega.block.finance.data;


public interface AccountEntryHome extends com.idega.data.IDOHome
{
 public AccountEntry create() throws javax.ejb.CreateException;
 public AccountEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findByAccountAndAssessmentRound(java.lang.Integer p0,java.lang.Integer p1)throws javax.ejb.FinderException;
 public java.util.Collection findByAccountAndStatus(java.lang.Integer p0,java.lang.String p1,java.sql.Date p2,java.sql.Date p3,String assessmentStatus)throws javax.ejb.FinderException;
 public java.util.Collection findByAssessmentRound(java.lang.Integer p0)throws javax.ejb.FinderException;
 public java.util.Collection findByEntryGroup(java.lang.Integer p0)throws javax.ejb.FinderException;
 public java.util.Collection findUnGrouped(java.sql.Date p0,java.sql.Date p1)throws javax.ejb.FinderException;
 public int countByGroup(java.lang.Integer p0)throws com.idega.data.IDOException;
 public java.sql.Date getMaxDateByAccount(java.lang.Integer p0)throws com.idega.data.IDOException;
 public double getTotalSumByAccount(java.lang.Integer p0)throws java.sql.SQLException;
 public double getTotalSumByAccount(java.lang.Integer accountID,String assessmentStatus)throws java.sql.SQLException;
 public double getTotalSumByAccountAndAssessmentRound(java.lang.Integer p0,java.lang.Integer p1)throws java.sql.SQLException;
 public double getTotalSumByAssessmentRound(java.lang.Integer p0)throws java.sql.SQLException;

}