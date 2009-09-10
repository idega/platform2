package com.idega.block.survey.data;


public interface SurveyEntityHome extends com.idega.data.IDOHome
{
 public SurveyEntity create() throws javax.ejb.CreateException;
 public SurveyEntity findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findActiveSurveys(com.idega.block.category.data.ICInformationFolder p0,java.sql.Timestamp p1)throws javax.ejb.FinderException;
 public java.util.Collection findAllSurveys(com.idega.block.category.data.ICInformationFolder p0)throws javax.ejb.FinderException;

}