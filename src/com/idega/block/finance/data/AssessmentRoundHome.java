package com.idega.block.finance.data;


public interface AssessmentRoundHome extends com.idega.data.IDOHome
{
 public AssessmentRound create() throws javax.ejb.CreateException;
 public AssessmentRound findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findByCategoryAndTariffGroup(java.lang.Integer p0,java.lang.Integer p1,java.sql.Date p2,java.sql.Date p3,String status,int p4,int p5)throws javax.ejb.FinderException;
 public int getCountByCategoryAndTariffGroup(java.lang.Integer p0,java.lang.Integer p1,java.sql.Date p2,java.sql.Date p3,String status)throws com.idega.data.IDOException;

}