package com.idega.block.finance.data;


public interface AssessmentRoundHome extends com.idega.data.IDOHome
{
 public AssessmentRound create() throws javax.ejb.CreateException;
 public AssessmentRound createLegacy();
 public AssessmentRound findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public AssessmentRound findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public AssessmentRound findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}