package com.idega.block.survey.data;


public interface SurveyReplyHome extends com.idega.data.IDOHome
{
 public SurveyReply create() throws javax.ejb.CreateException;
 public SurveyReply findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}